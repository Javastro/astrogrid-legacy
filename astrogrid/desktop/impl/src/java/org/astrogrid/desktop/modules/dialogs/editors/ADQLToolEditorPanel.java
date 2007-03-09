/* ADQLToolEditorPanel.java
 * Created on 13-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.dialogs.editors;

import javax.swing.JViewport ;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import java.awt.Color ;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory ;
import javax.swing.Box ;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon ;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem ;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.event.MenuListener ;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.astrogrid.acr.system.Configuration ;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.TableBeanComparator;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTransformer;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.TableMetadataPanel;
import org.astrogrid.desktop.modules.adqlEditor.commands.ColumnInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandFactory;
import org.astrogrid.desktop.modules.adqlEditor.commands.CopyHolder;
import org.astrogrid.desktop.modules.adqlEditor.commands.CutCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EditCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EnumeratedElementInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EnumeratedInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.PasteIntoCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.PasteNextToCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.PasteOverCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.StandardInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.TableInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
import org.astrogrid.desktop.modules.ag.ApplicationsImpl;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.system.Preference;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
// import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author jl99
 *
 */
public class ADQLToolEditorPanel 
       extends AbstractToolEditorPanel 
       implements ToolEditListener
                , TreeModelListener
                , ChangeListener
                , PropertyChangeListener{
    
    private static final Log log = LogFactory.getLog( ADQLToolEditorPanel.class ) ;
    private StringBuffer logIndent ;
    
    public static final String CONFIG_KEY_DEBUG_VIEW = "adqleditor.debugview" ; 
    public static final String GOOD_COMPILE_ICON = "greentick.gif" ;
    public static final String BAD_COMPILE_ICON = "redcross.gif" ;
    
    private ParameterValue queryParam = null ;
    
    protected final MyspaceInternal myspace ;  
    protected final UIComponent parent;
    protected final RegistryGoogle regChooser;
    protected final ResourceChooserInternal resourceChooser;
    protected final Registry registry ;
    protected final Preference showDebugPanePreference;
      
    // LHS Editor tabs...
    private JTabbedPane tabbedEditorPane ;
    
    private AdqlTree adqlTree ;
    private AdqlXmlView adqlXmlView ;
    private AdqlMainView adqlMainView ;
    
    //RHS Metadata tabs...
    private JTabbedPane tabbedMetadataPane ;
    
    // Bottom View Tabs...
    private JTabbedPane tabbedBottomPane;
    // This is the diagnostics tab...
    private JTextPane diagnostics ;
    private JTextPane history ;
    
    private JButton chooseResourceButton ;
    
    private JButton validateEditButton ;
    
    private SizedStack clipBoard = new SizedStack() ;
    
    private String editWindowOldImage ;
    private boolean bEditWindowUpdatedByFocusGained = false ;
    
    private SizedStack historyStack = new SizedStack() ;
    final JLabel historyStackCount = new JLabel() ;       
    final JButton historyTopButton = new JButton( "Top" ) ;
    final JButton historyUpButton = new JButton( "Up" ) ;
    final JButton historyDownButton = new JButton( "Down" ) ;
    final JButton historyBottomButton = new JButton( "Bottom" ) ;
    
    private class SizedStack extends Stack {
        
        private int maxSize ;
        private int currentPosition = 0 ;
        
        public SizedStack() {
            this( 16 ) ;
        }
        
        public SizedStack( int maxSize ) {
            this.maxSize = maxSize ;
        }

        public Object push( Object item ) {
            if( size() == maxSize ) {
                removeElementAt( 0 ) ;
            }
            currentPosition = 1 ;
            return super.push(item);
        }
        
        public Object peek( int position ) {
            if( isEmpty() )
                throw new java.util.EmptyStackException() ;            
            return get( vectorDisplacement() ) ;
        }
        
        public Object down() {
            if( currentPosition >= size() )
                return null ;
            return peek( currentPosition++ ) ;
        }
        
        public Object up() {
            if( currentPosition <= 1  )
                return null ;
            return peek( currentPosition-- ) ;         
        }
        
        public Object top() {
            if( isEmpty() )
                return null ;
            currentPosition = 1 ;
            return peek()  ;
        }
        
        public Object bottom() {
            if( isEmpty() )
                return null ;
            currentPosition = size() ;
            return peek( currentPosition )  ;
        }
        
        public boolean isAtBottom() {
            if( isEmpty() )
                return false ;
            return ( currentPosition == size() ) ;
        }
        
        public boolean isAtTop() {
            if( isEmpty() )
                return false ;
            return ( currentPosition == 1 ) ;
        }

        public int getCurrentPosition() {
            return currentPosition;
        }

        public void setCurrentPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }
        
        private int vectorDisplacement() {
            if( isEmpty() )
                return -1 ;
            return size() - currentPosition ;
        }
               
    }
    
    // private BranchExpansionListener branchExpansionListener = null ;
    private AdqlTransformer transformer ;
    private boolean statusAfterValidate = false ;
          
    public ADQLToolEditorPanel( ToolModel toolModel
                              , ResourceChooserInternal resourceChooser
                              , RegistryGoogle regChooser
                              , UIComponent parent
                              , MyspaceInternal myspace
                              , Registry registry
                              , Preference showDebugPanePref ) {
        super( toolModel );
        this.parent = parent;
        this.regChooser = regChooser;
        this.resourceChooser = resourceChooser ; 
        this.myspace = myspace ;
        this.registry = registry ;
        this.transformer = new AdqlTransformer() ;
        this.showDebugPanePreference = showDebugPanePref;
        this.showDebugPanePreference.addPropertyChangeListener(this);
        this.toolModel.addToolEditListener( this );
    }
    
 
    public void parameterAdded(ToolEditEvent te) {
        // can't apply to this - assume query is mandatory
    }
    
    public void parameterChanged(ToolEditEvent te) {
        if( log.isTraceEnabled() ) enterTrace( "parameterChanged(ToolEditEvent te)"  ) ;
        if ( te.getSource() != ADQLToolEditorPanel.this // only listen if change has come from elsewhere..
             && 
             te.getChangedParameter() == queryParam ) { 
            // Jeff. 
            // This requires more thought.
            // I dont think it is sensible to allow users to edit the
            // instream parameter directly (ie: outside of this editor)
            // so for the moment I shall ignore any edits.
            // There is of course the added problem of distinguishing
            // between switches from local to remote and vice versa.
            
            // The above is too simple. Whilst I experiment,
            // I've enabled it once more.
            	this.removeAll() ;
            	this.init() ;
            }
        if( log.isTraceEnabled() ) exitTrace( "parameterChanged(ToolEditEvent te)"  ) ;
    }
   
    public void parameterRemoved(ToolEditEvent te) {
        // can't apply - assume query is mandatory.
    }
    
    public void toolChanged(ToolEditEvent te) {
        if( log.isTraceEnabled() ) enterTrace( "toolChanged(ToolEditEvent te)"  ) ;
        toolSet(te);
        if( log.isTraceEnabled() ) exitTrace( "toolChanged(ToolEditEvent te)"  ) ;
    }
   
    public void toolCleared(ToolEditEvent te) {
        if( log.isTraceEnabled() ) enterTrace( "toolCleared(ToolEditEvent te)"  ) ;
        queryParam = null;
        setEnabled(false);
        this.removeAll() ;
        if( log.isTraceEnabled() ) exitTrace( "toolCleared(ToolEditEvent te)"  ) ;
    }
    
    public void toolSet(ToolEditEvent te) {
        if( log.isTraceEnabled() ) enterTrace( "toolCleared(ToolEditEvent te)"  ) ;
        String[] toks = ApplicationsImpl.listADQLParameters(toolModel.getTool().getInterface(),toolModel.getInfo());
        if (toks.length > 0) {
            setEnabled(true);
            queryParam = (ParameterValue)toolModel.getTool().findXPathValue("input/parameter[name='" + toks[0] +"']");
            this.removeAll() ;
            this.init() ;
        }
        if( log.isTraceEnabled() ) exitTrace( "toolCleared(ToolEditEvent te)"  ) ;
    }
      
    private AdqlTree setAdqlTree() { 
        if( log.isTraceEnabled() ) enterTrace( "setAdqlTree()"  ) ;
        URI toolIvorn = toolModel.getInfo().getId() ;
        String query = null ;
        this.adqlTree = null ;
        if( queryParam == null ) {
            if( this.adqlTree == null ) {
                this.adqlTree = new AdqlTree( parent, registry, toolIvorn ) ;
            }
        }
        //
        // Not quite sure of the reason for the hasIndirect() method call.
        // However, I will test for the presence of the indirection flag AND
        // whether it is set to true....
        else if( queryParam.hasIndirect() == true  && queryParam.getIndirect() == true ) {
            if( log.isDebugEnabled() ) log.debug( "Query is a remote reference." ) ;
            query = readQuery() ;
            if( query == null || query.length() < 5 ) {
                this.adqlTree = new AdqlTree( parent, registry, toolIvorn ) ;
            }
            else if( query.startsWith( "<" ) ) {
                query = adaptToVersion( query ) ;
                try {          
                    this.adqlTree = new AdqlTree( parent, query, registry, toolIvorn ) ;
                }
                catch ( Exception ex ) {
                    this.adqlTree = new AdqlTree( parent, registry, toolIvorn ) ;
                }
            }  
            if( log.isDebugEnabled() ) log.debug( "...setting indirect to false" ) ;
            queryParam.setIndirect( false ) ;
//            try {
//                URI fileLocation = new URI( queryParam.getValue().trim() ) ;               
//                InputStream is = myspace.getInputStream( fileLocation );
//           
//                if( this.adqlTree == null ) {
//                    this.adqlTree = new AdqlTree( is );
//                }
//                else {
//                    this.adqlTree.setTree( AdqlTree.parseXml( is ) ) ;
//                }
//                
//            } catch (Exception e) {
//                this.adqlTree = new AdqlTree() ;
//                e.printStackTrace() ;
//            } 
              
        }
        else {          
            if( log.isDebugEnabled() ) log.debug( "Query is inline..." ) ;
           query = queryParam.getValue() ;
           if( query == null || query.length() < 5 ) {
               this.adqlTree = new AdqlTree( parent, registry, toolIvorn ) ;
           }
           else if( query.startsWith( "<" ) ) {
               // Assume this is an instream adql/x query...
               query = adaptToVersion( query ) ;
               try {          
                   this.adqlTree = new AdqlTree( parent, query, registry, toolIvorn ) ;
               }
               catch ( Exception ex ) {
                   this.adqlTree = new AdqlTree( parent, registry, toolIvorn ) ;
               }
           }
           else { 
               // Assume this is an instream adql/s query...  
               
               // JL Note. I'm not supporting this for the moment
               // because there is no easy way to see how the metadata
               // can be recovered from the adql/s in a simple way
               // (maybe some specialized comment?)
               // So for the moment...
               this.adqlTree = new AdqlTree( parent, registry, toolIvorn ) ;
//                      
//               Document doc = null ;
//               try {          
//                   doc = validator.s2x( query ) ;
//                   this.adqlTree = new AdqlTree( doc ) ;
//               }
//               // this needs to be refined...
//               catch ( Exception se ) {
//                   log.debug( "Failed parsing by validator...", se ) ;
//                   this.adqlTree = new AdqlTree() ;
//               }
           } 
        } 
        this.adqlTree.setEnabled( true ) ;
        this.adqlTree.addTreeExpansionListener( new BranchExpansionListener() ) ;
        this.adqlTree.addMouseListener( new Popup() ) ;   
        this.adqlTree.addChangeListener( this ) ;
        if( log.isDebugEnabled() ) {
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            log.debug( "setAdqlTree() pretty print from root:\n" + getRoot().toString() ) ;
        }
        this.setAdqlParameter() ;
        this.adqlTree.getModel().addTreeModelListener( this );
        if( log.isTraceEnabled() ) exitTrace( "setAdqlTree()"  ) ;
        return this.adqlTree ;
    }
    
    
    private String adaptToVersion( String query ) {
        String returnQuery = query ;
        StringBuffer buffer = new StringBuffer( returnQuery ) ;
        int index = buffer.indexOf( AdqlData.NAMESPACE_0_74 ) ;
        if( index != -1 ) {
            if( log.isDebugEnabled() ) log.debug( "Namespace replaced" ) ;
            buffer.replace( index
                          , index + AdqlData.NAMESPACE_0_74.length()
                          , AdqlData.NAMESPACE_1_0 ) ;
            returnQuery = buffer.toString() ;
        }
        else if( buffer.indexOf( AdqlData.NAMESPACE_1_0) == -1 ) {
            log.error( "Unrecognized namespace in query:\n" + query ) ;
        }       
        return returnQuery ;
    }
    
    
    private String readQuery() {
        String retQuery = null ;
        BufferedInputStream bis = null ; 
        URI fileLocation = null ;
        try {
            fileLocation = new URI( queryParam.getValue().trim() ) ;
            bis = new BufferedInputStream( myspace.getInputStream( fileLocation ), 2000 ) ;
            StringBuffer buffer = new StringBuffer( 2000 ) ;
            int c ;
            while( (c = bis.read()) != -1 ) {
                buffer.append( (char)c ) ;
            } 
            retQuery = buffer.toString() ;           
        }
        catch( Exception exception ) {
            log.error( "Failed to read adql file: " + fileLocation, exception ) ;
        }
        finally {
            try{ bis.close(); } catch( Exception ex ) {;}
        }
        return retQuery ;
    }
    
    
    
   
    
//    private void queryRegistry( final String searchString ) {
//        
//        (new BackgroundWorker(parent,"Querying registry"){
//
//            protected Object construct()  {
//                try {
//                    return (registry.adqlSearchRI( searchString )[0]) ;
//                } catch (Exception e) {
//                    return e.getMessage();
//                }
//            }
//            
//            protected void doFinished( Object result ) {
//                if( result instanceof String ) {
//                    log.error( "Failed to find catalogue entry using: \n" + searchString ) ;
//                }
//                else {
//                    catalogueResource = (TabularDatabaseInformation)result ;
//                }
//            }
//        }).start();
//   
//    }
    

    private void init() {
        setLayout( new GridBagLayout() ) ;       
        GridBagConstraints gbc ;
        JSplitPane splAdqlToolEditor = new JSplitPane( JSplitPane.VERTICAL_SPLIT );

        // Put the editing panels plus the metadata panels in the top half of the split pane.
        splAdqlToolEditor.setTopComponent( initTopView() ) ;

        // Put the diagnostics panel at the bottom of the split pane.
        splAdqlToolEditor.setBottomComponent( initBottomView() ) ;

        // Set the rest of the split pane's properties,
        splAdqlToolEditor.setDividerLocation( 0.80 );
        splAdqlToolEditor.setResizeWeight( 0.80 ) ;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.NORTH;
        add(splAdqlToolEditor, gbc);
    } // end of init()
    
    
    private JSplitPane initTopView() {
        JSplitPane topView = new JSplitPane();
        
        // Put the editor panels in the left side of the split pane.
        topView.setLeftComponent( initLeftHandView() ) ;

        // Put the metadata/details panels in the right side of the split pane.
        topView.setRightComponent( initRightHandView() ) ;

        // Set the rest of the split pane's properties,
        topView.setDividerLocation( 0.60 );
        topView.setResizeWeight( 0.60 ) ;
        topView.setOneTouchExpandable( true ) ;
        return topView ;
    }
    
    private JPanel initLeftHandView() {
        
        // LHS top pane...
        JPanel lhsPanel = new JPanel() ;
        lhsPanel.setLayout( new GridBagLayout() ) ;
        
        // Create the components for the left side of the split pane:
        // the panel, scrolling panel, and the XML tree it will contain.
        tabbedEditorPane = new JTabbedPane();
        tabbedEditorPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedEditorPane.setTabPlacement(SwingConstants.TOP);
        
        // Experiment with combined views (tree/adql/s)...
        this.setAdqlTree() ;
        this.adqlMainView = new AdqlMainView( tabbedEditorPane ) ;

        // Adql/x panel ...
        // This is really only here for debug purposes...
        boolean bDebugView = showDebugPanePreference.asBoolean();
        if( bDebugView ) {
            this.adqlXmlView = new AdqlXmlView( tabbedEditorPane ) ;
        }
            
        GridBagConstraints gbc = new GridBagConstraints() ;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = 7 ;
        gbc.gridwidth = 8 ;
        gbc.gridx = 0 ;
        gbc.gridy = 0 ;   
        gbc.fill = GridBagConstraints.BOTH ;
        lhsPanel.add( tabbedEditorPane, gbc ) ;
        
        gbc.weighty = 0;
        gbc.gridheight = 1 ;
        gbc.gridwidth = 8 ;
        gbc.gridx = 0 ;
        gbc.gridy = 7 ;        
        gbc.fill = GridBagConstraints.HORIZONTAL ;
        
        lhsPanel.add( getValidateEditButton(), gbc) ;  
            
        return lhsPanel ;    
    }
    
    private JTabbedPane initBottomView() {
        //
        // Create the components for the bottom view:
       
        tabbedBottomPane = new JTabbedPane();
        tabbedBottomPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedBottomPane.setTabPlacement(SwingConstants.TOP);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 1;
        
        formatDiagnosticsTab( gbc ) ;
        
        formatHistoryTab( gbc ) ;
        
        return tabbedBottomPane ;          
    }
    
    private void formatDiagnosticsTab( GridBagConstraints gbc ) {
        JPanel pnlContent = new JPanel();
        JScrollPane scrContent = new JScrollPane();
        diagnostics = new JTextPane();
        scrContent.setViewportView( diagnostics );
        pnlContent.setLayout(new GridBagLayout());
        pnlContent.add(scrContent, gbc);
        tabbedBottomPane.addTab( "Diagnostics", pnlContent ) ;
    }
    
    private void formatHistoryTab( GridBagConstraints gbc ) {

        JPanel hpnlContent = new JPanel();
        JScrollPane hscrContent = new JScrollPane() ;
        
        historyStack = new SizedStack() ;
        historyStackCount.setText( "0 of 0" ) ;      
        historyTopButton.setEnabled( false ) ;
        historyUpButton.setEnabled( false ) ;
        historyDownButton.setEnabled( false ) ;
        historyBottomButton.setEnabled( false ) ;
        
        if( historyTopButton.getActionListeners().length == 0 ) {
            historyStackCount.setBorder( BorderFactory.createLineBorder( Color.BLACK ) ) ;    
            historyTopButton.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {           
                    setHistoryText( (String)getHistoryStack().top() ) ;
                }            
            }) ;
            historyUpButton.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {              
                    setHistoryText( (String)getHistoryStack().up() ) ;
                }            
            }) ;
            historyDownButton.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setHistoryText( (String)getHistoryStack().down() ) ;
                }            
            }) ;
            historyBottomButton.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setHistoryText( (String)getHistoryStack().bottom() ) ;
                }            
            }) ;
        }
        
        Box buttons = Box.createHorizontalBox() ;
        buttons.add( historyStackCount ) ;
        buttons.add( historyTopButton ) ;
        buttons.add( historyDownButton ) ;
        buttons.add( historyUpButton ) ;
        buttons.add( historyBottomButton ) ;
        buttons.add( Box.createHorizontalGlue() ) ;
        JViewport buttonView = new JViewport() ;
        buttonView.setView( buttons ) ;
        hscrContent.setColumnHeaderView( buttonView ) ;
 
        history = new JTextPane() ; 
        hscrContent.setViewportView( history );
        hpnlContent.setLayout(new GridBagLayout());
        hpnlContent.add(hscrContent, gbc ) ;
        tabbedBottomPane.addTab( "History stack", hpnlContent ) ;
    }
    
    private SizedStack getHistoryStack() {
        return historyStack ;
    }
    
    private void setHistoryText( String text ) {
        if( text != null ) {            
            historyStackCount.setText( historyStack.getCurrentPosition() + " of " + historyStack.size() ) ;
            history.setText( text ) ;
        }
        enableHistoryButtons() ;
    }
    
    private void enableHistoryButtons() {
        SizedStack stack = getHistoryStack() ;
        if( stack.size() <= 1 ) {
            historyTopButton.setEnabled( false ) ;
            historyUpButton.setEnabled( false ) ;
            historyDownButton.setEnabled( false ) ;
            historyBottomButton.setEnabled( false ) ;
        }
        else if( stack.isAtBottom() ) {
            historyTopButton.setEnabled( true ) ;
            historyUpButton.setEnabled( true ) ;
            historyDownButton.setEnabled( false ) ;
            historyBottomButton.setEnabled( false ) ;
        }
        else if( stack.isAtTop() ) {
            historyTopButton.setEnabled( false ) ;
            historyUpButton.setEnabled( false ) ;
            historyDownButton.setEnabled( true ) ;
            historyBottomButton.setEnabled( true ) ;
        }
        else {
            historyTopButton.setEnabled( true ) ;
            historyUpButton.setEnabled( true ) ;
            historyDownButton.setEnabled( true ) ;
            historyBottomButton.setEnabled( true ) ;
        }
    }
    
    private void setDiagnosticsIcon( boolean errorFree ) {
        ImageIcon icon = null ;
        if( errorFree ) {
            icon = IconHelper.loadIcon( GOOD_COMPILE_ICON ) ; 
        }
        else {
            icon = IconHelper.loadIcon( BAD_COMPILE_ICON ) ;
        }
        int i = tabbedBottomPane.indexOfTab( "Diagnostics" ) ;
        tabbedBottomPane.setIconAt( i, icon ) ;
    }
  
    
    private JPanel initRightHandView() {
        
        // RHS top pane...
        JPanel rhsPanel = new JPanel() ;
        rhsPanel.setLayout( new GridBagLayout() ) ;
        
        // Top bit will be a series of metadata tabs...
        tabbedMetadataPane = new JTabbedPane();
        tabbedMetadataPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedMetadataPane.setTabPlacement(SwingConstants.TOP);
        
        // First catalogue panel...
        JPanel catalog1 = new JPanel();
        tabbedMetadataPane.addTab( "Archive", catalog1 ) ;
        
        GridBagConstraints gbc = new GridBagConstraints() ;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = 7 ;
        gbc.gridwidth = 8 ;
        gbc.gridx = 0 ;
        gbc.gridy = 0 ;   
        gbc.fill = GridBagConstraints.BOTH ;
        rhsPanel.add( tabbedMetadataPane, gbc ) ;
        
        gbc.weighty = 0;
        gbc.gridheight = 1 ;
        gbc.gridwidth = 8 ;
        gbc.gridx = 0 ;
        gbc.gridy = 7 ;        
        gbc.fill = GridBagConstraints.HORIZONTAL ;
        
        rhsPanel.add(getChooseResourceButton(), gbc);  
        //
        // If we are reloading a previous adql query, then we first load
        // the catalogue data, then prime the tables collection...
        chooseResourceButton.setEnabled( false ) ;
//        if( adqlTree.isCatalogueResourceSet() ) {
//            chooseResourceButton.setEnabled( false ) ;
//            formatCatalogTab() ;
//        }
//        else {
//            chooseResourceButton.setEnabled( true ) ;
//        }             
        return rhsPanel ;
    }
    
        
    /** applicable when it's a dsa-style tool - ie. has an ADQL parameter*/
    public boolean isApplicable(Tool t, CeaApplication info) {
        return t != null && info != null && ApplicationsImpl.listADQLParameters(t.getInterface(),info).length > 0;
    }
    
    public void treeNodesChanged(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.setAdqlParameter() ;
            this.adqlMainView.displayText() ;
            refreshDebugView() ;
        }
    }
    public void treeNodesInserted(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.update() ;
            this.adqlMainView.displayText() ;
            refreshDebugView() ;
        }
    }
    public void treeNodesRemoved(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.update() ;
            this.adqlMainView.displayText() ;
            refreshDebugView() ;
        }
    }
    public void treeStructureChanged(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.update() ;
            this.adqlMainView.displayText() ;
            refreshDebugView() ;
        }
    }
    
    private void refreshDebugView() {
        if( this.adqlXmlView != null )
            this.adqlXmlView.refreshFromModel() ;
    }
    
    private void update() {
        this.setAdqlParameter() ;
        this.validateAdql() ;
    }
    
    private void validateAdql() {
        if( log.isTraceEnabled() ) enterTrace( "validateAdql()" ) ; 
        statusAfterValidate = false ;
        // Create an XmlOptions instance and set the error listener.
        XmlOptions validateOptions = new XmlOptions();
        ArrayList errorList = new ArrayList();
        validateOptions.setErrorListener(errorList);
        
        // Validate the XML.
        boolean isValid = getRoot().validate(validateOptions);
        
        // If the XML isn't valid, loop through the listener's contents,
        // printing contained messages.
        this.diagnostics.setText( "" )  ;
        if( !isValid ){
            StringBuffer buffer = new StringBuffer( 1024 ) ;
            for (int i = 0; i < errorList.size(); i++) {
                XmlError error = (XmlError)errorList.get(i);
                buffer
                	.append( "Message: " )
                	.append( error.getMessage() )
                	.append( "\n" )
                	.append( "Error at: " ) ;
                XmlObject xmlObj = error.getObjectLocation() ;
                try {
                    if( xmlObj == null ) {
                        buffer.append( "no location given." ) ;
                     }
                    else {
                        buffer.append( xmlObj.schemaType().getName().getLocalPart() ) ;
                    }
                    buffer.append( "\n" ) ;
                }
                catch( Exception ex ) {
                    buffer.append( "The XML is not well formed.\n");
                } 
            }
            this.diagnostics.setText( buffer.toString() ) ;
        }
        else {
            statusAfterValidate = true ;
        }
        if( log.isTraceEnabled() ) exitTrace( "validateAdql()" ) ; 
    }
    
    private class BranchExpansionListener implements TreeExpansionListener {
        
        public void treeCollapsed( TreeExpansionEvent event ) {
            AdqlNode entry = (AdqlNode)event.getPath().getLastPathComponent() ;
            entry.setExpanded( false ) ;
        }
    
        public void treeExpanded( TreeExpansionEvent event ) {
            AdqlNode entry = (AdqlNode)event.getPath().getLastPathComponent() ;
            entry.setExpanded( true ) ;
        }
        
    } // end of class BranchExpansionListener
 
	private class Popup extends MouseAdapter {
	    
        //fix for mac. for portability, need to check on mousePressed 
        // and mouseReleased whether it's the 'popupTrigger' event.
        // onlny way to do it - as a mac CTRL-Cick gives a different event type to a Button-3 click.
        // complicated, eh?	    
	    // http://developer.apple.com/documentation/Java/Conceptual/Java14Development/07-NativePlatformIntegration/NativePlatformIntegration.html
	    public void mouseReleased( MouseEvent event ) {
	        if( event.isPopupTrigger()  ) {
	            processPopupClick(event);                
	        }
	    } 
	    public void mousePressed(MouseEvent event) {
	        if( event.isPopupTrigger()  ) {
	            processPopupClick(event);                
	        }
	    }

		/**
		 * @param event
		 */
		private void processPopupClick(MouseEvent event) {
			int x = event.getX() ;
			int y = event.getY() ;
			TreePath path = adqlTree.getPathForLocation( x, y ) ;
			if( path != null ) {
			    adqlTree.setSelectionPath( path ) ;
			    adqlTree.scrollPathToVisible( path ) ;                
			    showPopup( path, x, y ) ;
			}
		}
	    
	    private void showPopup( TreePath path, int x, int y ) {
	        AdqlNode entry = (AdqlNode)adqlTree.getLastSelectedPathComponent() ;
		    getPopupMenu( entry ).show( adqlTree, x, y ) ;
	    }
	    
	    private JPopupMenu getPopupMenu( AdqlNode entry ) {
            JPopupMenu popup = new JPopupMenu( "AdqlTreeContextMenu" ) ;
	        buildEditMenu( popup, entry ) ;
            return popup ;
	    }
	           
	} // end of class PopupMenu
	
	private class CutAction extends AbstractAction {
	    private CutCommand command ;
	       
	    public CutAction( AdqlNode entry ) {
	        super( "Cut" ) ;
	        TreePath path = adqlTree.getSelectionPath() ;
            if( log.isDebugEnabled() ) {
                if( path == null ) {
                    log.debug("CutAction path: " + path ) ;
                }
                else {
                    log.debug( "CutAction path: " 
                             + "\n path count = " + path.getPathCount() 
                             + "\n last path component is of type " 
                             + ((AdqlNode)path.getLastPathComponent()).getShortTypeName() 
                             + "\n element context path = "
                             + ((AdqlNode)path.getLastPathComponent()).getElementContextPath() ) ;
                }
            }
	        // If the path is null or there is no parent ( count < 2 )
            // Or it is the top select element (count == 2 )
	        // Then we cannot cut...
	        if( path == null ) {
                setEnabled( false ) ;
                return ;
	        }     
            if( path.getPathCount() <= 2 ) {
                setEnabled( false ) ;
                return ;
            }
	        this.command = adqlTree.getCommandFactory().newCutCommand( adqlTree
	                                                                 , adqlTree.getCommandFactory().getUndoManager()
	                                                                 , (AdqlNode)path.getLastPathComponent() ) ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
            clipBoard.push( command.getCopy() ) ;    
	        if( command.execute() != CommandExec.FAILED ) {
    	        adqlTree.repaint() ;
        	}
	    }
	}
	
	private class CopyAction extends AbstractAction {
	    private AdqlNode entry ;
	       
	    public CopyAction( AdqlNode entry ) {
	        super( "Copy" ) ;
            TreePath path = adqlTree.getSelectionPath() ;
            if( log.isDebugEnabled() ) {
                if( path == null ) {
                    log.debug("CopyAction path: " + path ) ;
                }
                else {
                    log.debug( "CopyAction path: " 
                             + "\n path count = " + path.getPathCount() 
                             + "\n last path component is of type " 
                             + ((AdqlNode)path.getLastPathComponent()).getShortTypeName() 
                             + "\n element context path = "
                             + ((AdqlNode)path.getLastPathComponent()).getElementContextPath() ) ;
                }
            }
            // If the path is null or there is no parent ( count < 2 )
            // Then we cannot copy...
            if( path == null ) {
                setEnabled( false ) ;
                return ;
            }     
            if( path.getPathCount() < 2 ) {
                setEnabled( false ) ;
                return ;
            }
	        this.entry = entry ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
	        clipBoard.push( CopyHolder.holderForCopyPurposes( entry ) ) ;
	    }
	}
	
	private class PasteOverAction extends AbstractAction {
	    
	    private PasteOverCommand command = null ;
	    
	    public PasteOverAction( AdqlNode entry ) {
	        super( "Paste over" ) ;
	        if( preConditionsForPaste() == true ) {
	            this.command = adqlTree.getCommandFactory().newPasteOverCommand( entry, (CopyHolder)clipBoard.peek() ) ;
	        }
	        setEnabled( command != null ) ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {	        
        	if( command.execute() != CommandExec.FAILED ) {
    	        adqlTree.repaint() ;
        	}
	    }	    
	}
	
	private class PasteNextToAction extends AbstractAction {
	    private PasteNextToCommand command ;
	    
	    public PasteNextToAction( AdqlNode entry, boolean before ) {
	        super() ;
	        if( before ) {
	            super.putValue( Action.NAME, "Paste before" ) ;
	        }
	        else {
	            super.putValue( Action.NAME, "Paste after" ) ;
	        }    
	        if( preConditionsForPaste() == true ) {
	            this.command = adqlTree.getCommandFactory().newPasteNextToCommand( entry, (CopyHolder)clipBoard.peek(), before );
	        }
	        setEnabled( command != null && command.isChildEnabled() ) ;    
	    }
	    	    
	    public void actionPerformed( ActionEvent e ) {
	        if( command.execute() != CommandExec.FAILED ) {
	            adqlTree.repaint() ;
        	}
	    }
	    
	}
	
	private class PasteIntoAction extends AbstractAction {
	    private PasteIntoCommand command = null ;
	       
	    public PasteIntoAction( AdqlNode entry ) {
	        super( "Paste into" ) ;
	        if( preConditionsForPaste() == true ) {
	            this.command = 
	                adqlTree.getCommandFactory().newPasteIntoCommand( entry, (CopyHolder)clipBoard.peek() );
	        }
	        setEnabled( command != null && command.isChildEnabled() ) ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
	        if( command.execute() != CommandExec.FAILED ) {
    	        adqlTree.repaint() ;
        	}
	    }
	}
	
	private class UndoAction extends AbstractAction {
	    
	    CommandFactory.UndoManager undoManager ;
	    
	    public UndoAction() {
	        super() ;
	        undoManager = adqlTree.getCommandFactory().getUndoManager() ;
	        super.putValue( Action.NAME, undoManager.getUndoPresentationName() ) ;
	        setEnabled( undoManager.canUndo() ) ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
	        undoManager.undo() ;	    
	        adqlTree.repaint() ;
	    }
	    
	}
	
	private class RedoAction extends AbstractAction {
	    
	    CommandFactory.UndoManager undoManager ;
	    
	    public RedoAction() {
	        super() ;
	        undoManager = adqlTree.getCommandFactory().getUndoManager() ;
	        super.putValue( Action.NAME, undoManager.getRedoPresentationName() ) ;
	        setEnabled( undoManager.canRedo() ) ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
	        undoManager.redo() ;
	        adqlTree.repaint() ;	     
	    }
	    
	}
	
	private class EditAction extends AbstractAction {
	       
	    public EditAction( AdqlNode entry ) {
	        super( "Micro edit" ) ;
            TreePath path = adqlTree.getSelectionPath() ;
            if( path != null ) {
                AdqlNode selectedEntry = (AdqlNode)adqlTree.getLastSelectedPathComponent();
                if( selectedEntry.isBottomLeafEditable() ) {
                    this.setEnabled( true ) ;
                }
            }
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
            AdqlTree.EditPromptAction action = adqlTree.new EditPromptAction( "Micro Edit" ) ;
            action.actionPerformed( e ) ;
	    }
	}
	
	private class InsertAction extends AbstractAction {
	    
	    protected StandardInsertCommand command ;
	    
	    public InsertAction( String name, StandardInsertCommand command ) {
	        super( name ) ;
	        this.command = command ;
	        this.setEnabled( command.isChildEnabled() ) ;
	    }
	    
        public void actionPerformed( ActionEvent e ) {
            TreePath path = adqlTree.getSelectionPath() ;
            if( path == null )
                return ;
         
            command.setSelectedValue( e.getActionCommand() ) ;
            CommandExec.Result result = command.execute() ;
            
//            if( newEntry.isBottomLeafEditable() ) {
//                String typeName = newEntry.getSchemaType().getName().getLocalPart() ;
//                String attributeName = (String)AdqlData.EDITABLE.get( typeName ) ;
//                if( attributeName != null && attributeName.length() > 0 ) {
//                    this.setDefaultValueForAttributeDrivenElement() ; 
//                }
//                else {
//                    this.setDefaultValueForElement() ;                
//                }           
//            }
            
            if( result != CommandExec.FAILED ) {
                adqlTree.getCommandFactory().retireOutstandingMultipleInsertCommands() ;
                DefaultTreeModel model =  (DefaultTreeModel)adqlTree.getModel() ;
    	        model.nodeStructureChanged( command.getParentEntry() ) ;
//    	        path = path.pathByAddingChild( command.getChildEntry() ) ;
//    	        adqlTree.scrollPathToVisible( path ) ;
                adqlTree.repaint() ;
                
            }
            
            // Switch to edit for a newly created bottom leaf...
            // This needs reviewing where multiple inserts are required...
//            if( newEntry.isBottomLeafEditable() ) {
//                adqlTree.setEditingActive( true );  // is this required?
//                adqlTree.startEditingAtPath( path ) ;
//                adqlTree.setEditingActive( false );  // is this required?
//                model.nodeChanged( newEntry ) ;
//            }
            

//            setAdqlParameter() ;
 
        } // end of actionPerformed() 
        
	} // end of class InsertAction
	
    protected JButton getChooseResourceButton() {
        if (chooseResourceButton == null) {
            chooseResourceButton = new JButton("Set Archive Definition..");
            chooseResourceButton.addActionListener(new ActionListener() {               
                public void actionPerformed(ActionEvent e) {
                   Resource[] selection = regChooser.selectResourcesXQueryFilter( "Select Catalogue description for " 
                                                                   + toolModel.getInfo().getTitle()
                                                                   ,false
                                                                   , "(matches(@xsi:type,'.*:TabularDB'))"
                                                                   ) ;
                   if( selection != null && selection.length > 0 ) {
                       if( log.isDebugEnabled() ) log.debug( "regChooser.chooseResourceWithFilter() returned object of type: " + selection[0].getClass().getName() ) ; 
                       adqlTree.setCatalogueResource( (DataCollection)selection[0]) ;
                       chooseResourceButton.setEnabled( false ) ;
                       formatCatalogTab() ;
                   }
                }
            });
        }
        return chooseResourceButton;
    }
    
    protected JButton getValidateEditButton() {
        if ( validateEditButton == null) {
            validateEditButton = new JButton("Validate Edit");
            validateEditButton.addActionListener(new ActionListener() {               
                public void actionPerformed(ActionEvent e) {
                   // validateEditButton.setEnabled( false ) ;
                   ADQLToolEditorPanel.this.adqlMainView.executeEditCommand() ;
                }
            });
        }
        return validateEditButton ;
    }
    
    private void formatCatalogTab() {
    	DataCollection catalogueResource = adqlTree.getCatalogueResource() ;
        String title = null ;
        String description = null ;
        if( tabbedMetadataPane.getTabCount() == 1 ) {
            Catalog dbs = catalogueResource.getCatalogues()[0];           
            if( dbs != null ) {
                title = dbs.getName() ; 
                if( title == null || title.trim().length() == 0 ); {
                    title = catalogueResource.getTitle() ;
                }
                tabbedMetadataPane.setTitleAt( 0, title );
                
                // First catalogue panel...
                JPanel catalog1 = (JPanel)tabbedMetadataPane.getComponentAt( 0 ) ;
                JTabbedPane tabbedCatalogPane = new JTabbedPane();
                tabbedCatalogPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                tabbedCatalogPane.setTabPlacement( SwingConstants.RIGHT ) ;
                
                JScrollPane scrollCatalogDetails = new JScrollPane();
                JTextPane catalogDetailsTextPane = new JTextPane();
                scrollCatalogDetails.setViewportView( catalogDetailsTextPane ) ;
                description = dbs.getDescription() ;
                if( description == null || description.trim().length() == 0 ) {
                    description = catalogueResource.getContent().getDescription() ;
                }                   
                if( description == null || description.trim().length() == 0 ) {
                    description = "No description available" ;
                }
                catalogDetailsTextPane.setText( description ) ;
                tabbedCatalogPane.addTab( "Overview", scrollCatalogDetails ) ;

                final TableBean[] tables = dbs.getTables();
                // JBL sort tables here.
                TableBeanComparator comparator = new TableBeanComparator() ;
                Arrays.sort( tables, comparator ) ;
                for( int j=0; j<tables.length; j++) {
                    tabbedCatalogPane.addTab( tables[j].getName()
                                            , new TableMetadataPanel( ADQLToolEditorPanel.this
                                                                    , adqlTree
                                                                    , dbs
                                                                    , tables[j] ) ) ;
                }
                catalog1.setLayout( new GridBagLayout() ) ;
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.anchor = GridBagConstraints.NORTHWEST;
                gbc.weightx = 1;
                gbc.weighty = 1;
                catalog1.add( tabbedCatalogPane, gbc ) ;
                             
            }     
        }
    }
    
  
    private JMenu getEnumeratedMenus( StandardInsertCommand command ) {
        JMenu menu = new JMenu( command.getChildDisplayName() ) ;
        String[] attrValues = ((EnumeratedInsertCommand)command).getEnumeratedValues() ;
        for( int i=0; i<attrValues.length; i++ ) {
            menu.add( new JMenuItem( new InsertAction( attrValues[i], command ) ) );
        }
        return menu ;
    }
    
    private JMenu getEnumeratedElementMenus( StandardInsertCommand command ) {
        JMenu menu = new JMenu( command.getChildDisplayName() ) ;
        String[] elementValues = ((EnumeratedElementInsertCommand)command).getEnumeratedValues() ;
        for( int i=0; i<elementValues.length; i++ ) {
            menu.add( new JMenuItem( new InsertAction( elementValues[i], command ) ) );
        }
        return menu ;
    }
        
    private JMenu getInsertColumnMenu( StandardInsertCommand command ) {
        ColumnInsertCommand cic = (ColumnInsertCommand)command ;
        JMenu insertMenu = new JMenu( command.getChildDisplayName() ) ;
        insertMenu.setEnabled( false ) ;
        Iterator it = adqlTree.getFromTables().entrySet().iterator() ;
        ColumnInsertCommand cicForTable = null ;
        // For each table ...
        while( it.hasNext() ) {
            if( cicForTable == null ) {
                cic.setArchive( adqlTree.getCatalogueResource().getCatalogues()[0] ) ;
                cicForTable = cic ;
                insertMenu.setEnabled( true ) ;
            }
            else {
                // Shallow copy of the command...
                cicForTable = new ColumnInsertCommand( cic ) ;
            }
            AdqlTree.TableData tableData = (AdqlTree.TableData)((java.util.Map.Entry)it.next()).getValue() ;
            try {
                TableBean table = tableData.getTable() ;
                cicForTable.setTable( table ) ;
                cicForTable.setTableAlias( tableData.alias ) ;
                JMenu tableMenu = new JMenu( table.getName() ) ;
                insertMenu.add( tableMenu ) ;
                ColumnBean[] columns = table.getColumns();
                for( int i=0; i<columns.length; i++ ) {
                    tableMenu.add( new InsertAction( columns[i].getName(), cicForTable ) ) ;
                }
            }
            catch( ArrayIndexOutOfBoundsException ex ) {
                continue ;
            }                       
        }           
        return insertMenu ;
    }
       
    private JMenu getInsertTableMenu( StandardInsertCommand command ) {
        TableInsertCommand tic= (TableInsertCommand)command ;       
        JMenu insertMenu = new JMenu( command.getChildDisplayName() ) ;
        DataCollection tdb = adqlTree.getCatalogueResource() ;
        if( tdb != null ) {
            Catalog db = tdb.getCatalogues()[0];
            tic.setDatabase( db ) ;
            TableBean[] tables = db.getTables() ;
            for( int i=0; i<tables.length; i++ ){            
               insertMenu.add( new InsertAction( tables[i].getName(), command ) ) ;
            }
        }
        return insertMenu ;
    }
    
    private JMenu getInsertJoinTableMenu( StandardInsertCommand command ) {
        TableInsertCommand tic= (TableInsertCommand)command ;       
        JMenu insertMenu = new JMenu( command.getChildDisplayName() ) ;
        DataCollection tdb = adqlTree.getCatalogueResource() ;
        if( tdb != null ) {
            Catalog db = tdb.getCatalogues()[0];
            tic.setDatabase( db ) ;
            TableBean[] tables = db.getTables() ;
            for( int i=0; i<tables.length; i++ ){            
               insertMenu.add( new InsertAction( tables[i].getName(), command ) ) ;
            }
        }
        return insertMenu ;
    }
    
    
    private void setAdqlParameter() {
        if( queryParam.hasIndirect() == true )
            queryParam.setIndirect( false ) ;
        AdqlNode rootEntry = ((AdqlNode)adqlTree.getModel().getRoot()) ;
        XmlObject xmlRoot =  (XmlObject)rootEntry.getUserObject() ;
        // NOte. I'm not sure the following is adequate.
        XmlOptions options = new XmlOptions() ;
        options.setSaveOuter() ;
        String xmlText = xmlRoot.xmlText( options ) ;
        queryParam.setValue( xmlText ) ;
        toolModel.fireParameterChanged( ADQLToolEditorPanel.this, queryParam ) ;                    
    }
    

    private class AdqlXmlView extends JPanel {
        
        JTextPane textPane = new JTextPane() ;  ;
        
        public AdqlXmlView( JTabbedPane owner ) {
            super() ;
            JScrollPane xmlScrollContent = new JScrollPane();
            xmlScrollContent.setViewportView( textPane );
            textPane.setEditable( false ) ;
            this.setLayout( new BorderLayout() ) ;
            this.add(xmlScrollContent, BorderLayout.CENTER );
            owner.addTab( "Debug", this ) ; 
            this.refreshFromModel() ;
        }
        
        protected void refreshFromModel() {
            if( log.isTraceEnabled() ) enterTrace( "AdqlXmlView.refreshFromModel()" ) ;
            XmlCursor nodeCursor = adqlTree.getRoot().newCursor() ;
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            options.setSavePrettyPrintIndent(4);
            textPane.setText( nodeCursor.xmlText(options) ) ;
            nodeCursor.dispose() ;
            if( log.isTraceEnabled() ) exitTrace( "AdqlXmlView.refreshFromModel()" ) ;
        }
             
    } // end of class AdqlXmlView

    private class AdqlMainView extends JPanel {
        
        // AdqlNode selectedNode ;
        Integer selectedNodeToken ;
        JTabbedPane owner ;
        JTextPane textPane = new JTextPane() ;  
        JSplitPane splitEditor = new JSplitPane( JSplitPane.VERTICAL_SPLIT ) ;
        
        public AdqlMainView( JTabbedPane owner ) {
            super() ;
            this.owner = owner ;
        
            GridBagConstraints gbc ;
            
            final JScrollPane scrTree = new JScrollPane();
            
            scrTree.addComponentListener ( 
                    new ComponentAdapter() {
                        public void componentResized( ComponentEvent e ) {
                            adqlTree.setAvailableWidth( scrTree.getWidth() ) ;
                        }
                    } 
            ) ;
            
            adqlTree.addTreeSelectionListener( new TreeSelectionListener() {
                public void valueChanged( TreeSelectionEvent e ) {
                    
                    AdqlNode newSelectedNode = (AdqlNode)adqlTree.getLastSelectedPathComponent() ;
                    if( newSelectedNode != null ) {
                        // selectedNode = newSelectedNode ;
                        selectedNodeToken = adqlTree.getCommandFactory().getEditStore().add( newSelectedNode ) ;
                        displayText() ; 
                    }                  
                }
            } ) ;
            
            //
            // On creation, default to root node...
            // selectedNode = (AdqlNode)adqlTree.getModel().getRoot() ;
            TreeNode[] nodes = ((AdqlNode)adqlTree.getModel().getRoot()).getPath() ;
            adqlTree.setSelectionPath( new TreePath( nodes ) ) ;
                       
            scrTree.setViewportView( adqlTree ) ;           
            splitEditor.setTopComponent( scrTree ) ;
                                  
            //
            // Set up the text pane in a scrolling panel etc...
            JScrollPane textScrollContent = new JScrollPane();
            textScrollContent.setViewportView( textPane );
            splitEditor.setBottomComponent( textScrollContent ) ;
            
            
            textPane.addFocusListener(
                    new FocusListener() {
                        public void focusGained(FocusEvent e) {
                            editWindowOldImage = textPane.getText() ;
                            bEditWindowUpdatedByFocusGained = true ;
                        }
                        public void focusLost(FocusEvent e) {
                            if( bEditWindowUpdatedByFocusGained ) {
                                maintainHistory( editWindowOldImage, textPane.getText() ) ;
                                bEditWindowUpdatedByFocusGained = false ;
                            }
                           
                        }                       
                    }         
            ) ;

            // Set the rest of the split pane's properties,
            splitEditor.setDividerLocation( 0.80 );
            splitEditor.setResizeWeight( 0.80 ) ;
            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 1;

            gbc.weighty = 1;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridheight = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTH;
            add(splitEditor, gbc);
                       
            adqlTree.openBranches() ;
            this.setLayout( new BorderLayout() ) ;
            this.add(splitEditor, BorderLayout.CENTER );
            this.owner.addTab( "ADQL", this ) ; 
            
            initKeyProcessing( textPane ) ;
        }
 
        private void maintainHistory( String beforeImage, String afterImage ) {
            if( afterImage == null )
                return ;
            afterImage = afterImage.trim() ;
            if( afterImage.length() == 0 )
                return ;
            if( beforeImage != null ) {
                beforeImage = beforeImage.trim() ;
                if( afterImage.equals( beforeImage ) ) {
                    return ;
                }
            }
           
            if( !historyStack.isEmpty() && afterImage.equals( (String)historyStack.peek() ) )
                return ;    
//            java.util.Enumeration e = historyStack.elements() ;
//            while( e.hasMoreElements() ) {
//                if( content.equals( (String)e.nextElement() ) ) 
//                    return ;
//            }
            historyStack.push( afterImage ) ; 
            historyStack.setCurrentPosition( 1 ) ;
            setHistoryText( afterImage ) ;    
        }
        
        protected void refreshFromModel() {         
            if( log.isTraceEnabled() ) enterTrace( "AdqlMainView.refreshFromModel()" ) ;
//            adqlTree.setTree( NodeFactory.newInstance( this.controller.getRoot() ), registry, toolModel.getInfo().getId() );
//            adqlTree.getModel().addTreeModelListener( ADQLToolEditorPanel.this );
            setAdqlParameter() ;
//            adqlTree.openBranches() ;
            if( log.isTraceEnabled() ) exitTrace( "AdqlMainView.refreshFromModel()" ) ;
        }
        
//        protected String getDisplayText() {
//            AdqlNode node = adqlTree.getCommandFactory().getEditStore().get( selectedNodeToken ) ;
//            XmlObject userObject = node.getXmlObject() ;       
//            userObject = AdqlUtils.modifyQuotedIdentifiers( userObject ) ;
//            XmlCursor nodeCursor = userObject.newCursor();
//            String text = nodeCursor.xmlText();
//            nodeCursor.dispose() ;
//            userObject = AdqlUtils.unModifyQuotedIdentifiers( userObject ) ;
//            return transformer.transformToAdqls( text, " " ).trim() ; 
//        }
        
        protected void displayText() {
            if( bEditWindowUpdatedByFocusGained ) {
                maintainHistory( editWindowOldImage, textPane.getText() ) ;
                bEditWindowUpdatedByFocusGained = false ;
            }
            AdqlNode node = adqlTree.getCommandFactory().getEditStore().get( selectedNodeToken ) ;
            try {
                if( node != null ) {
                    XmlObject userObject = node.getXmlObject() ;       
                    userObject = AdqlUtils.modifyQuotedIdentifiers( userObject ) ;
                    XmlCursor nodeCursor = userObject.newCursor();
                    String text = nodeCursor.xmlText();
                    nodeCursor.dispose() ;
                    userObject = AdqlUtils.unModifyQuotedIdentifiers( userObject ) ;
                    textPane.setText( transformer.transformToAdqls( text, " " ) ) ; 
                }
                else {
                    textPane.setText( "" ) ;
                }       
            }
            catch( Exception ex ) {
                textPane.setText( "" ) ;
            }
            
        }
        
        protected void initKeyProcessing( JTextPane textPane ) {
            if( log.isTraceEnabled() ) enterTrace( "AdqlMainView.initKeyProcessing()" ) ;
            if( textPane != null ) {
                if( log.isDebugEnabled() ) log.debug( "initKeyProcessing triggered" ) ;
                //
                // First we need to find the default action for the Enter key and preserve this.
                // We will use it to invoke the default action after having processed the Enter key.
                KeyStroke keyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ) ;
                Object key = textPane.getInputMap( JComponent.WHEN_FOCUSED ).get( keyStroke ) ;
                final Action action = textPane.getActionMap().get( key ) ;
                // Set up our own key for the Enter key...
                textPane.getInputMap( JComponent.WHEN_FOCUSED ).put( keyStroke, "ValidateAdql" ) ; 
                //            adqlText.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT )
                //              .put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "ValidateAdqlString" ) ;
                
                //
                // Now our substitute action. Note how we invoke the default action at the end...
                // (JL Note: not sure whether I should invoke the default process first!!!)
                textPane.getActionMap().put( "ValidateAdql", new AbstractAction (){
                    public void actionPerformed(ActionEvent e) {
                        if( log.isTraceEnabled() ) enterTrace( "AdqlMainView.VK_ENTER.actionPerformed()" ) ;
                        executeEditCommand() ;
                        action.actionPerformed( e ) ;
                        if( log.isTraceEnabled() ) exitTrace( "AdqlMainView.VK_ENTER.actionPerformed()" ) ;
                    }
                } );                  
            }
            if( log.isTraceEnabled() ) exitTrace( "AdqlMainView.initKeyProcessing()" ) ;
        }
        
        protected void executeEditCommand() {
            AdqlNode node = adqlTree.getCommandFactory().getEditStore().get( selectedNodeToken ) ;
            String image = textPane.getText() ;
            EditCommand editCommand = adqlTree.getCommandFactory().newEditCommand( adqlTree, node, image ) ;
            CommandExec.Result result = editCommand.execute() ;
            if( result == CommandExec.FAILED ) {
                setDiagnosticsIcon( false ) ;
                diagnostics.setText( editCommand.getMessages()[0] ) ;
            }
            else {       
                setDiagnosticsIcon( true ) ;
                diagnostics.setText( "" ) ;
                // Refresh tree...
                // NB: Cosmetically this requires improvement.
                // Refreshing using the child entry is cosmetically better, but does not properly update
                // the model, leading to exceptions being thrown when a menu is built (particularly on
                // a delete action). Using the parent entry gives no errors, but tends to collapse 
                // branches in the parent. Some method of using the parent but ensuring branches remain
                // open is called for.               
//                ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( editCommand.getChildEntry() ) ;
                ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( editCommand.getParentEntry() ) ;
                adqlTree.repaint() ;
            }
            maintainHistory( null, image ) ;
        }
        
    }
      
    private Point[]	elastic ;
    
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        if( elastic != null && elastic.length == 2 ) {
            g.drawLine( elastic[0].x, elastic[0].y, elastic[1].x, elastic[1].y ) ;
        }
    }
    
    public void updateDisplay() {
        repaint() ;
    }
    
    protected Point[] getElastic() {
        return elastic;
    }
    
    public void setElastic(Point[] elastic) {
        this.elastic = elastic;
    }
    
    public void unsetElastic() {
        this.elastic = null ;
    }
    
    private XmlObject getRoot() {
        return adqlTree.getRoot() ;
    }
    
    private boolean preConditionsForPaste() {
        TreePath path = adqlTree.getSelectionPath() ;
        // If the path is null or there is no parent
        // Then we cannot paste into this entry...
        if( path == null )
            return false ;
        if( path.getPathCount() < 2 )
            return false ;
        // If the clipboard is empty then there is nothing to paste...
        if( clipBoard.size() == 0 )
            return false ;
        return true ;
    }


    /** Method overridden.
     * 
     * If we are about to execute with a query containing detectable errors,
     * then we issue a suitable warning message.
     * 
     * @param actionType the action about to be performed
     * @return a suitable warning message or null ;
     */
    public String getActionWarningMessage( ActionType actionType ) {
        String message = null ;
        if( actionType == EXECUTE 
            && 
            isApplicable( toolModel.getTool(), toolModel.getInfo() )
            &&
            this.statusAfterValidate == false  ) {
                message = "Query Panel: errors or unprocessed edits have been detected." ;
        }       
        return message ;
    }
    
    
    
    /* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged( ChangeEvent e) {
        if( e.getSource() == adqlTree ) {
            if( adqlTree.isCatalogueResourceSet() ) {
                chooseResourceButton.setEnabled( false ) ;
                formatCatalogTab() ;
            }
            else {
                chooseResourceButton.setEnabled( true ) ;
            }  
            return ;
        }
        if( e.getSource() instanceof JTabbedPane == false )
            return ;
        JTabbedPane tp = (JTabbedPane)e.getSource() ;
        java.awt.Component c = tp.getSelectedComponent() ;
        if( c == this ) {
            // We have just gained selection:
            boolean bEditFound = false ;
            boolean bOptionsFound = false ;
            JMenuBar mb = ((UIComponentImpl)parent).getJMenuBar() ;
            int mc = mb.getMenuCount() ;
            JMenu menu = null ;
            for( int i=0; i<mc; i++ ) {
                menu = mb.getMenu(i) ;
                String name = menu.getName() ;
                if( name == null )
                    continue ;
                if( name.equals("Edit") ) {
                    bEditFound = true ;
                    menu.setEnabled( true ) ;
                }
                else if( name.equals( "Options" ) ) {
                    bOptionsFound = true ;
                    menu.setEnabled( true ) ;
                }
            }
            if( !bEditFound ) {
                final JMenu editMenu = new JMenu( "Edit" ) ;
                editMenu.setName( "Edit") ;
                editMenu.addMenuListener( new MenuListener() {

                    public void menuCanceled( MenuEvent e ) { }
                    public void menuDeselected(MenuEvent e) { }
                    public void menuSelected(MenuEvent e) {  
                        AdqlNode entry = (AdqlNode)adqlTree.getLastSelectedPathComponent() ;                       
                        buildEditMenu( editMenu, entry ) ;
                    }
                                       
                } ) ;
               
                mb.add(editMenu) ;
                editMenu.setEnabled( true ) ;
            }
            else {
                buildEditMenu( menu, (AdqlNode)adqlTree.getLastSelectedPathComponent() ) ;
            }
        }
        else {
            // We have just lost selection:
            JMenuBar mb = ((UIComponentImpl)parent).getJMenuBar() ;
            int mc = mb.getMenuCount() ;
            for( int i=0; i<mc; i++ ) {
                JMenu menu = mb.getMenu(i) ;
                String name = menu.getName() ;
                if (name != null) {
                if( name.equals("Edit") ) {
                    menu.setEnabled( false ) ;
                }
                else if( name.equals( "Options" ) ) {
                    menu.setEnabled( false ) ;
                }
                }
            }
        }
    }
    

    /** called when value of prefernece changes */
	public void propertyChange(PropertyChangeEvent evt) {
		// test which preference has changed (in future, might be listening to more than one prefernece 
		if (evt.getSource() == this.showDebugPanePreference) {
		     boolean bState = this.showDebugPanePreference.asBoolean();
             if( bState) {
                 ADQLToolEditorPanel.this.adqlXmlView = new AdqlXmlView( tabbedEditorPane ) ;
             }
             else {                       
                 tabbedEditorPane.removeTabAt( tabbedEditorPane.indexOfComponent( adqlXmlView) ) ;
                 adqlXmlView = null ;
             }		
		}
	}
    
    
    private void buildEditMenu( JComponent menuComponent, AdqlNode entry ) {
        
        final class MenuAdapter {
            
            private JComponent menuComponent ;
            
            MenuAdapter( JComponent menuComponent ) {
                this.menuComponent = menuComponent ;
                if( this.menuComponent instanceof JPopupMenu ) {
                    ((JPopupMenu)menuComponent).removeAll() ;
                }
                else if( this.menuComponent instanceof JMenu ) {
                    ((JMenu)menuComponent).removeAll() ;
                }
                else if( menuComponent == null ){
                    log.debug( "buildEditMenu().MenuAdapter(): menuComponent = " + menuComponent ) ;
                }
                else {
                    log.debug( "buildEditMenu().MenuAdapter(): menuComponent = " + menuComponent.getClass() ) ;
                }
            }
            
            JComponent add( Action action ) {
                JComponent jc = null ;
                if( this.menuComponent instanceof JPopupMenu ) {
                    jc = ((JPopupMenu)menuComponent).add( action ) ;
                }
                else if( this.menuComponent instanceof JMenu ) {
                    jc = ((JMenu)menuComponent).add( action ) ;
                }
                return jc ;
            }
            
            JComponent add( JMenu menu ) {
                JComponent jc = null ;
                if( this.menuComponent instanceof JPopupMenu ) {
                    jc = ((JPopupMenu)menuComponent).add( menu ) ;
                }
                else if( this.menuComponent instanceof JMenu ) {
                    jc = ((JMenu)menuComponent).add( menu ) ;
                }
                return jc ;
            }
            
            JComponent add( String text) {
                JComponent jc = null ;
                if( this.menuComponent instanceof JPopupMenu ) {
                    jc = ((JPopupMenu)menuComponent).add( text ) ;
                }
                else if( this.menuComponent instanceof JMenu ) {
                    jc = ((JMenu)menuComponent).add( text ) ;
                }
                return jc ;
            }
            
            void addSeparator() {
                if( this.menuComponent instanceof JPopupMenu ) {
                    ((JPopupMenu)menuComponent).addSeparator() ;
                }
                else if( this.menuComponent instanceof JMenu ) {
                    ((JMenu)menuComponent).addSeparator() ;
                }
            }
        }
        
        //
        // Safety first...
        if( entry == null ) {
            log.debug( "buildEditMenu(JComponent, AdqlNode): AdqlNode is null." ) ;
            // Create an empty menu...
            new MenuAdapter( menuComponent ) ;
            return ;
        }
            
        MenuAdapter ma = new MenuAdapter( menuComponent ) ;
       
        if( entry.isBottomLeafEditable() ) {
            ma.add( new EditAction( entry ) ) ;
        }
        ma.add( new CutAction( entry ) ) ;
        ma.add( new CopyAction( entry ) ) ;
        ma.add( new PasteIntoAction( entry ) ) ;
        ma.add( new PasteOverAction( entry ) ) ;
        ma.add( new  PasteNextToAction( entry, true ) ) ; // Before
        ma.add( new  PasteNextToAction( entry, false ) ) ; // After          
        ma.addSeparator() ;
        ma.add( new UndoAction() ) ;
        ma.add( new RedoAction() ) ;
 
        List commandArray = adqlTree.getCommandFactory().newInsertCommands( entry ) ;
        if( commandArray != null && commandArray.size() > 0 ) {
            ma.addSeparator() ;
            ma.add( "Insert into " + entry.getDisplayName() + "..." ) ;
            // ma.addSeparator() ;
            ListIterator iterator = commandArray.listIterator() ;               
            while( iterator.hasNext() ) {
                StandardInsertCommand command = (StandardInsertCommand)iterator.next() ;
                if( !command.isChildSupportedType() )
                    continue ;
                if( command.isChildCascadeable() ) {
                    ma.add( getCascadeableMenu( command ) ) ;
                }
                else {
                    ma.add( new InsertAction( command.getChildDisplayName(), command ) ) ;
                }
            }
        }      

    }
    
    
    private JMenu getCascadeableMenu( StandardInsertCommand command ) {
        JMenu menu = new JMenu( command.getChildDisplayName() ) ;
    
        if( command.isChildColumnLinked() ) {
            if( adqlTree.getFromTables().isEmpty() == false ) {
                menu = getInsertColumnMenu( command ) ;
                menu.setEnabled( command.isChildEnabled() ) ;
            }
            else {
                menu.setEnabled( false ) ;
            }

        }
        else if( command.isChildTableLinked() ) {
            if( adqlTree.isCatalogueResourceSet() ) { 
                menu = getInsertTableMenu( command ) ;
                menu.setEnabled( command.isChildEnabled() ) ;
            }
            else {
                menu.setEnabled( false ) ;
            }
        }
        else if( command.isChildDrivenByEnumeratedAttribute() ) {
            menu = getEnumeratedMenus( command ) ;
            menu.setEnabled( command.isChildEnabled() ) ;
        }  
        else if( command.isChildDrivenByEnumeratedElement() ) {
            menu = getEnumeratedElementMenus( command ) ;
            menu.setEnabled( command.isChildEnabled() ) ;
        }
        return menu;
    }
   

    private void enterTrace( String entry ) {
        log.trace( getLogIndent().toString() + "enter: " + entry ) ;
        indentPlus() ;
    }

    private void exitTrace( String entry ) {
        indentMinus() ;
        log.trace( getLogIndent().toString() + "exit : " + entry ) ;
    }
    
    private void indentPlus() {
        logIndent.append( ' ' ) ;
    }
    
    private void indentMinus() {
        logIndent.deleteCharAt( logIndent.length()-1 ) ;
    }
    
    private StringBuffer getLogIndent() {
        if( logIndent == null ) {
            logIndent = new StringBuffer() ;
        }
        return logIndent ;
    }



    
    
} // end of ADQLToolEditor