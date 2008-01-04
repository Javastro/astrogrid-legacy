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
package org.astrogrid.desktop.modules.adqlEditor; 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.adql.AdqlException;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.adqlEditor.commands.ColumnInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandFactory;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandInfo;
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
import org.astrogrid.desktop.modules.system.pref.Preference;
import org.astrogrid.desktop.modules.ui.TabularMetadataViewer;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.UIComponentImpl;
import org.astrogrid.desktop.modules.ui.taskrunner.UIComponentWithMenu;

/**
 * @author jl99
 *
 *
 *@modified nww - simplifed by refactoring from 'a panel that edits a tool document' to
 *' a panel that edits an ADQL parameter'. Also made the parameter immutable - the editor cannot
 *be switched to edit a different parameter - instead, construct a new editor.
 *
 *In the future, might want to go even further - make this 'a panel that edits an adql string' and 
 *leave it's clients to insert the adql into the tool document / whatever
 */
public class ADQLEditorPanel 
       extends JPanel
       implements TreeModelListener
                , ChangeListener
                , PropertyChangeListener{
    
    private static final Log log = LogFactory.getLog( ADQLEditorPanel.class ) ;
    private StringBuffer logIndent ;
    
    public static final String GOOD_COMPILE_ICON = "tick16.png" ;
    public static final String BAD_COMPILE_ICON = "no16.png" ;
    
    private final ParameterValue queryParam;
    /** registry resource that describes the application this query is targeted at 
     * probably always going to be a CeaApplication at the moment, but use the
     * most general type 'Resource', to allow us to target TAP when it appears
     * */
    protected final Resource targetApplication;
    protected final UIComponent parent;
    protected final RegistryGoogle regChooser;
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
          
    public ADQLEditorPanel(ParameterValue queryParam
    						  , Resource targetApplication
    						  , UIComponentWithMenu parent
    						  ,RegistryGoogle regChooser
                              , Registry registry
                              , Preference showDebugPanePref ) {
    	this.queryParam = queryParam;
    	this.targetApplication = targetApplication;
        this.parent = parent;
        this.regChooser = regChooser;
        this.registry = registry ;
        this.transformer = new AdqlTransformer() ;
        this.showDebugPanePreference = showDebugPanePref;
        this.showDebugPanePreference.addPropertyChangeListener(this);        
        this.init();
        new ContextMenuAssistant(this,parent.getContextMenu());
       
    }
    
    /** class to manage showing / hiding of context menu items. 
     * 
     * populated with example menu items at the moment - in real use, the menu items 
     * would probably be replaced with Action classes defined separately, 
     * so this class would just populate / depopulate the menu and control the visibility of the items.
     * */
    private class ContextMenuAssistant implements HierarchyListener{
        private final JMenu contextMenu;
        private final JMenuItem it;
        private final JMenu subMenu;

        public ContextMenuAssistant(JPanel p, JMenu contextMenu) {
            this.contextMenu = contextMenu;
            it = new JMenuItem("context-sensitive-example");
         //   it.setVisible(false); temporarily made invisible for the first beta release.
            subMenu = new JMenu("context-submenu");
            subMenu.add(new JMenuItem("an entry"));
          //  subMenu.setVisible(false);  temporarily made invisible for the first beta release.
            p.addHierarchyListener(this);
        }


        public void hierarchyChanged(HierarchyEvent e) {
            long flags = e.getChangeFlags();            
            if ((flags &  HierarchyEvent.SHOWING_CHANGED ) != 0){
                contextMenu.setEnabled(isShowing());
            } else if ((flags &  HierarchyEvent.PARENT_CHANGED) != 0) {
                    if (getParent() == null) {
                        // remove the menu entries
                        contextMenu.remove(it);
                        contextMenu.remove(subMenu);
                    } else {
                        // add the menu entries.
                        contextMenu.add(it);
                        contextMenu.add(subMenu);
                    }                  
            } 
        }
    }

      
    private AdqlTree setAdqlTree() { 
        if( log.isTraceEnabled() ) enterTrace( "setAdqlTree()"  ) ;
        URI toolIvorn = targetApplication.getId() ;
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
        URL fileLocation = null ;
        try {
            fileLocation = new URL( queryParam.getValue().trim() ) ;
            bis = new BufferedInputStream(fileLocation.openStream() , 2000 ) ;
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
    

    private void _init() {   
        setLayout(new BorderLayout());
        // Put the editing panels plus the metadata panels in the top half of the split pane.
        add( initTopView(),BorderLayout.CENTER) ;

        // Put the diagnostics panel at the bottom of the split pane.
        add( initBottomView(),BorderLayout.SOUTH) ; 
        setPreferredSize(new Dimension(400,450));
    } // end of init()
    
    private void init() {
        setLayout( new GridBagLayout() ) ;       
        GridBagConstraints gbc ;
        JSplitPane splAdqlToolEditor = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        splAdqlToolEditor.setBorder(null);
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
        topView.setBorder(null);
      //  topView.setPreferredSize(new Dimension(600,350));
        topView.setMaximumSize(null);
        // Put the editor panels in the left side of the split pane.
        topView.setLeftComponent( initLeftHandView() ) ;

        // Put the metadata/details panels in the right side of the split pane.
        topView.setRightComponent( initRightHandView() ) ;

        // Set the rest of the split pane's properties,
        // apparantly float doesn't work unless splut pane is already visible
        topView.setDividerLocation(250);
        topView.setResizeWeight( 0.60 ) ;
        topView.setOneTouchExpandable( true ) ; 
        return topView ;
    }
    
    private JPanel initLeftHandView() {
        
        // LHS top pane...
        JPanel lhsPanel = new JPanel() ;
        lhsPanel.setLayout( new GridBagLayout() ) ;
        lhsPanel.setBorder(BorderFactory.createEmptyBorder());
        
        // Create the components for the left side of the split pane:
        // the panel, scrolling panel, and the XML tree it will contain.
        tabbedEditorPane = new JTabbedPane();
        tabbedEditorPane.setBorder(BorderFactory.createEmptyBorder());
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
        tabbedBottomPane.setBorder(null);
        Dimension dim = new Dimension(400,80);
        tabbedBottomPane.setPreferredSize(dim);
        tabbedBottomPane.setMaximumSize(dim);
        tabbedBottomPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedBottomPane.setTabPlacement(SwingConstants.TOP);
        
        formatDiagnosticsTab( ) ;
        
        formatHistoryTab( ) ;
        
        return tabbedBottomPane ;          
    }
    
    private void formatDiagnosticsTab(  ) {
        diagnostics = new JTextPane();
        diagnostics.setBorder(BorderFactory.createEmptyBorder());
        JScrollPane scrContent = new JScrollPane(diagnostics);
        scrContent.setBorder(BorderFactory.createEmptyBorder());
        tabbedBottomPane.addTab( "Diagnostics", scrContent ) ;
    }
    
    private void formatHistoryTab( ) {

        JScrollPane hscrContent = new JScrollPane() ;
        hscrContent.setBorder(BorderFactory.createEmptyBorder());
        
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
        history.setBorder(BorderFactory.createEmptyBorder());
        hscrContent.setViewportView( history );
        tabbedBottomPane.addTab( "History stack", hscrContent ) ;
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
    //    rhsPanel.setPreferredSize(new Dimension(400,350));
        rhsPanel.setLayout( new GridBagLayout() ) ;
        
        // Top bit will be a series of metadata tabs...
        tabbedMetadataPane = new JTabbedPane();
        tabbedMetadataPane.setBorder(BorderFactory.createEmptyBorder());
        tabbedMetadataPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedMetadataPane.setTabPlacement(SwingConstants.TOP);
        
        // First catalogue panel...
        JPanel catalog1 = new JPanel();
        tabbedMetadataPane.addTab( "Archive", catalog1 ) ;
        
        //@todo replace this with a flip panel.
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
    
    public void treeNodesChanged(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.setAdqlParameter() ;
            refreshDebugView() ;
        }
    }
    public void treeNodesInserted(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.update() ;
            refreshDebugView() ;
        }
    }
    public void treeNodesRemoved(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.update() ;
            refreshDebugView() ;
        }
    }
    public void treeStructureChanged(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.update() ;
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
        String[] messages = validateUponAdqlsFromRoot() ;      
        if( messages.length != 0 ){
            statusAfterValidate = false ;
            setDiagnosticsIcon( false ) ;            
            StringBuffer buffer = new StringBuffer( 1024 ) ;
          for (int i = 0; i < messages.length; i++) {
              buffer.append( "[" + (i+1) + "] " ).append( messages[i] ).append( '\n' ) ;
          }
          this.diagnostics.setText( buffer.toString() ) ;         
        }
        else {
            this.diagnostics.setText( "" )  ;
            setDiagnosticsIcon( true ) ; 
            statusAfterValidate = true ;           
        }
        if( log.isTraceEnabled() ) exitTrace( "validateAdql()" ) ; 
    }
    
    private String[] validateUponAdqlsFromRoot() {
        String[] messages ;
        XmlObject userObject = adqlTree.getRoot() ; 
        userObject = AdqlUtils.modifyQuotedIdentifiers( userObject ) ;
        XmlCursor nodeCursor = userObject.newCursor();
        String text = nodeCursor.xmlText();
        nodeCursor.dispose() ;
        userObject = AdqlUtils.unModifyQuotedIdentifiers( userObject ) ;
        String adqls = transformer.transformToAdqls( text, " " ) ; 
        
        try {  
            adqlTree.getCommandFactory().getAdqlCompiler( adqls ).compileToXmlText() ;
            messages = new String[0] ;
        }
        catch( AdqlException adqlex ) {
            messages = adqlex.getMessages() ;
            if( messages == null ) {
                messages = ( new String[] { "Internal compiler error. See log."} ) ;
            }  
            else if ( messages.length == 0 ) {
                messages = ( new String[] { "Internal compiler error. See log."} ) ;
            }
        }
        catch( Exception ex ) {
            messages = ( new String[] { "Internal compiler error. See log."} ) ;
        }
        return messages ;   
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
	        if( log.isTraceEnabled() ) { enterTrace( "CutAction.actionPerformed()" ) ; }
	        try {
	            clipBoard.push( command.getCopy() ) ;    
	            if( command.execute() != CommandExec.FAILED ) {
	                DefaultTreeModel model =  (DefaultTreeModel)adqlTree.getModel() ;
	                model.nodeStructureChanged( command.getParentEntry() ) ;
	                adqlTree.ensureSomeNodeSelected( this.command ) ;
	                adqlTree.repaint() ;
	            }
	        }
	        finally {
	            if( log.isTraceEnabled() ) { exitTrace( "CutAction.actionPerformed()" ) ; }
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
            if( log.isTraceEnabled() ) { enterTrace( "PasteOverAction.actionPerformed()" ) ; }
            try {
                if( command.execute() != CommandExec.FAILED ) {
                    DefaultTreeModel model =  (DefaultTreeModel)adqlTree.getModel() ;
                    model.nodeStructureChanged( command.getParentEntry() ) ;
                    adqlTree.ensureSomeNodeSelected( this.command ) ;
                    adqlTree.repaint() ;
                }               
            }
            finally {
                if( log.isTraceEnabled() ) { exitTrace( "PasteOverAction.actionPerformed()" ) ; }
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
            if( log.isTraceEnabled() ) { enterTrace( "PasteNextToAction.actionPerformed()" ) ; }
	        try {
            if( command.execute() != CommandExec.FAILED ) {
                DefaultTreeModel model =  (DefaultTreeModel)adqlTree.getModel() ;
                model.nodeStructureChanged( command.getParentEntry() ) ;
                adqlTree.ensureSomeNodeSelected( this.command ) ;
                adqlTree.repaint() ;
        	}
            }
            finally {
                if( log.isTraceEnabled() ) { exitTrace( "PasteNextToAction.actionPerformed()" ) ; }
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
            if( log.isTraceEnabled() ) { enterTrace( "PasteIntoAction.actionPerformed()" ) ; }
            try {
	        if( command.execute() != CommandExec.FAILED ) {
                DefaultTreeModel model =  (DefaultTreeModel)adqlTree.getModel() ;
                model.nodeStructureChanged( command.getParentEntry() ) ;
                adqlTree.ensureSomeNodeSelected( this.command ) ;
                adqlTree.repaint() ;
        	}
            }
            finally {
                if( log.isTraceEnabled() ) { exitTrace( "PasteIntoAction.actionPerformed()" ) ; }
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
            if( log.isTraceEnabled() ) { enterTrace( "UndoAction.actionPerformed()" ) ; }
            try {
                CommandInfo ci = (CommandInfo)undoManager.getCommandToBeUndone() ;           
                undoManager.undo() ;
                DefaultTreeModel model =  (DefaultTreeModel)adqlTree.getModel() ;
                model.nodeStructureChanged( ci.getParentEntry() ) ;
                adqlTree.ensureSomeNodeSelected( ci ) ;
                adqlTree.repaint() ;
            }
            finally {
                if( log.isTraceEnabled() ) { exitTrace( "UndoAction.actionPerformed()" ) ; }
            }
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
            if( log.isTraceEnabled() ) { enterTrace( "RedoAction.actionPerformed()" ) ; }
            try {
            CommandInfo ci = (CommandInfo)undoManager.getCommandToBeRedone() ;          
	        undoManager.redo() ;
            DefaultTreeModel model =  (DefaultTreeModel)adqlTree.getModel() ;
            model.nodeStructureChanged( ci.getParentEntry() ) ;
            adqlTree.ensureSomeNodeSelected( ci ) ;
	        adqlTree.repaint() ;
            }
            finally {
                if( log.isTraceEnabled() ) { exitTrace( "RedoAction.actionPerformed()" ) ; }
            }
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
            if( log.isTraceEnabled() ) { enterTrace( "InsertAction.actionPerformed()" ) ; }
            try {
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
                adqlTree.ensureSomeNodeSelected( this.command ) ;
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
            }
            finally {
                if( log.isTraceEnabled() ) { exitTrace( "InsertAction.actionPerformed()" ) ; }
            }
 
        } // end of actionPerformed() 
        
	} // end of class InsertAction
	
    protected JButton getChooseResourceButton() {
        if (chooseResourceButton == null) {
            chooseResourceButton = new JButton("Set Archive Definition..");
            chooseResourceButton.addActionListener(new ActionListener() {               
                public void actionPerformed(ActionEvent e) {
                   Resource[] selection = regChooser.selectResourcesXQueryFilter( "Select Catalogue description for " 
                                                                   + targetApplication.getTitle()
                                                                   ,false
                                                                   , "(@xsi:type &= '*TabularDB')"
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
                   ADQLEditorPanel.this.adqlMainView.executeEditCommand() ;
                }
            });
        }
        return validateEditButton ;
    }
    
    private void formatCatalogTab() {
        if( tabbedMetadataPane.getTabCount() == 1 ) {
        	DataCollection catalogueResource = adqlTree.getCatalogueResource() ;
            if( catalogueResource.getCatalogues().length > 0) {
            	Catalog dbs = catalogueResource.getCatalogues()[0];           
                String title = dbs.getName() ; 
                if( title == null || title.trim().length() == 0 ); {
                    title = catalogueResource.getTitle() ;
                }
                tabbedMetadataPane.setTitleAt( 0, title );
                
                // First catalogue panel...
                //JPanel catalog1 = (JPanel)tabbedMetadataPane.getComponentAt( 0 ) ;
                TabularMetadataViewer tmv = new AdqlEditorTabularMetadataViewer(this,adqlTree);
                tmv.display(catalogueResource);
                tabbedMetadataPane.setComponentAt(0,tmv);
        
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

    private class AdqlMainView extends JSplitPane {
        
        // AdqlNode selectedNode ;
        // Integer selectedNodeToken = null ;
        private final JTabbedPane owner ;
        private final AdqlsView textPane = new AdqlsView();
        
        public AdqlMainView( JTabbedPane owner ) {
            super(JSplitPane.VERTICAL_SPLIT ) ;
            this.owner = owner ;
        
            // On creation, default to top Select node..
            adqlTree.setSelectionToTopSelectNode();
            adqlTree.setBorder(BorderFactory.createEmptyBorder());
            adqlTree.openBranches() ;
            
            final JScrollPane scrTree = new JScrollPane(adqlTree,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);            
            scrTree.addComponentListener ( 
                    new ComponentAdapter() {
                        public void componentResized( ComponentEvent e ) {
                            adqlTree.setAvailableWidth( scrTree.getViewportBorderBounds().width ) ;
                        }
                    } 
            ) ;
            scrTree.setBorder(BorderFactory.createEmptyBorder());
            
            //
                 
            this.setBorder(BorderFactory.createEmptyBorder());
            this.setTopComponent( scrTree ) ;
                                  
            //
            // Set up the text pane in a scrolling panel etc...
            JScrollPane textScrollContent =new JScrollPane(textPane,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);            
            textPane.setBorder(BorderFactory.createEmptyBorder());
            textScrollContent.setBorder(BorderFactory.createEmptyBorder());
            this.setBottomComponent( textScrollContent ) ;
            this.setDividerLocation(200);
            this.setOneTouchExpandable(false); // folk find this irritating.
            
            this.owner.addTab( "ADQL", this ) ; 
        }
        
        protected void executeEditCommand() {
            textPane.executeEditCommand();
        }
        
    }  
    
    /**Extractef from AdqlMainView - the adql display panel */
  public class AdqlsView extends  JTextPane  implements TreeSelectionListener, FocusListener, TreeModelListener {

        // tree selection listener interface.
        public void valueChanged( TreeSelectionEvent e ) {
//          AdqlNode newSelectedNode = (AdqlNode)adqlTree.getLastSelectedPathComponent() ;
//          if( newSelectedNode != null ) {
//              selectedNodeToken = adqlTree.getCommandFactory().getEditStore().add( newSelectedNode ) ;
                displayText() ;
                
//          }
        }
        
        // focus listener interface
        public void focusGained(FocusEvent e) {
            editWindowOldImage = getText() ;
            bEditWindowUpdatedByFocusGained = true ;
        }
        public void focusLost(FocusEvent e) {
            if( bEditWindowUpdatedByFocusGained ) {
                maintainHistory( editWindowOldImage, getText() ) ;
                bEditWindowUpdatedByFocusGained = false ;
            }
        }                      

        // TreeModelListener interface
        public void treeNodesChanged(TreeModelEvent e) {    
            displayText();
        }

        public void treeNodesInserted(TreeModelEvent e) {
            displayText();
        }

        public void treeNodesRemoved(TreeModelEvent e) {
            displayText();            
        }

        public void treeStructureChanged(TreeModelEvent e) {
            displayText();            
        }    
        
        public AdqlsView() {
            this(true);
        }
        /**
         * 
         * @param followPath if true, this pane should show just the subset of the query
         * selected in the tree view.
         */
        public AdqlsView(boolean followPath) {
            super() ;
            this.followPath = followPath;
            if (followPath) {
                // no point listening to tree selections if we still display the same thing.
                adqlTree.addTreeSelectionListener(this);
            }
            adqlTree.getModel().addTreeModelListener(this);
            addFocusListener(this);
            initKeyProcessing() ;
            displayText();
            
            this.originalBorder = getBorder();
            this.warnBorder = BorderFactory.createLineBorder(Color.RED);            
        }
        
        final private Border originalBorder;
        final private Border warnBorder;
        
        private final boolean followPath;
 
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
        
        private void refreshFromModel() {         
 //            adqlTree.setTree( NodeFactory.newInstance( this.controller.getRoot() ), registry, toolModel.getInfo().getId() );
//            adqlTree.getModel().addTreeModelListener( ADQLToolEditorPanel.this );
            setAdqlParameter() ;
//            adqlTree.openBranches() ;
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

        public void displayText() {
            if( bEditWindowUpdatedByFocusGained ) {
                maintainHistory( editWindowOldImage,getText() ) ;
                bEditWindowUpdatedByFocusGained = false ;
            }
            
            AdqlNode node;
            if (followPath) {
                node = (AdqlNode)adqlTree.getLastSelectedPathComponent() ;
            } else {
                node =  (AdqlNode)adqlTree.getModel().getRoot();
            }
            // AdqlNode node = adqlTree.getCommandFactory().getEditStore().get( selectedNodeToken ) ;
 
            try {
                if( node != null ) {
                    XmlObject userObject = node.getXmlObject() ;       
                    userObject = AdqlUtils.modifyQuotedIdentifiers( userObject ) ;
                    XmlCursor nodeCursor = userObject.newCursor();
                    String text = nodeCursor.xmlText();
                    nodeCursor.dispose() ;
                    userObject = AdqlUtils.unModifyQuotedIdentifiers( userObject ) ;
                    setText( transformer.transformToAdqls( text, " " ) ) ; 
                }
                else {
                    setText( "" ) ;
                }       
            }
            catch( Exception ex ) {
                setText( "" ) ;
            }
            
        }
        
        private void initKeyProcessing() {
                // First we need to find the default action for the Enter key and preserve this.
                // We will use it to invoke the default action after having processed the Enter key.
                KeyStroke keyStroke = KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ) ;
                Object key =getInputMap( JComponent.WHEN_FOCUSED ).get( keyStroke ) ;
                final Action action = getActionMap().get( key ) ;
                // Set up our own key for the Enter key...
                getInputMap( JComponent.WHEN_FOCUSED ).put( keyStroke, "ValidateAdql" ) ; 
                //            adqlText.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT )
                //              .put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "ValidateAdqlString" ) ;

                //
                // Now our substitute action. Note how we invoke the default action at the end...
                // (JL Note: not sure whether I should invoke the default process first!!!)
                getActionMap().put( "ValidateAdql", new AbstractAction (){
                    public void actionPerformed(ActionEvent e) {
                        if( log.isTraceEnabled() ) enterTrace( "AdqlMainView.VK_ENTER.actionPerformed()" ) ;
//                        AdqlNode newSelectedNode = (AdqlNode)adqlTree.getLastSelectedPathComponent() ;
//                        if( newSelectedNode != null ) {
//                            selectedNodeToken = adqlTree.getCommandFactory().getEditStore().add( newSelectedNode ) ;
//                            executeEditCommand( selectedNodeToken ) ;
//                        } 
                        executeEditCommand() ;
                        action.actionPerformed( e ) ;
                        if( log.isTraceEnabled() ) exitTrace( "AdqlMainView.VK_ENTER.actionPerformed()" ) ;
                    }
                } );                  
        }
        
        public void executeEditCommand() {
            if( log.isTraceEnabled() ) { enterTrace( "AdqlMainView.executeEditCommand()" ) ; }
            try {
//            AdqlNode node = adqlTree.getCommandFactory().getEditStore().get( selectedNodeToken ) ;
            String image = getText() ;
            if (!followPath) { // set selection to top selection node first, as this edit comes from an editor containing all the adqls.
                adqlTree.setSelectionToTopSelectNode();
            }
            AdqlNode node =  (AdqlNode)adqlTree.getLastSelectedPathComponent() ;
            
            if( node == null ) {
                maintainHistory( null, image ) ;
                return ;
            }
            
            //
            // There appears still to be an occasional problem of losing visual selection 
            // yet having a selectedNodeToken. The situation is accompanied by having no parent node,
            // which will produce a NullPointerException in initializing the command. This is caught 
            // and logged when the AbstractCommand is initialized.
            // So, safety first, maintain the edit history so no editing is lost, then return
            // to give the user another try. 
            EditCommand editCommand = adqlTree.getCommandFactory().newEditCommand( adqlTree, node, image ) ;          
            if(!editCommand.isInitializedStatusGood() ) {
                maintainHistory( null, image ) ;
                return ;
            }
            
            CommandExec.Result result = editCommand.execute() ;
            if( result == CommandExec.FAILED ) {
                setDiagnosticsIcon( false ) ;        
                setBorder(warnBorder);
                String[] messages = editCommand.getMessages() ; 
                StringBuffer buffer = new StringBuffer( messages.length * 64 ) ;
                for( int i=0; i<messages.length; i++ ) {
                    buffer.append( "[" + (i+1) + "] " ).append( messages[i] ).append( '\n' ) ;
                }
                diagnostics.setText( buffer.toString() ) ;
            }
            else {       
                setDiagnosticsIcon( true ) ;
                diagnostics.setText( "" ) ;
                setBorder(originalBorder);                
                // Refresh tree...
                // NB: Cosmetically this requires improvement.
                // Refreshing using the child entry is cosmetically better, but does not properly update
                // the model, leading to exceptions being thrown when a menu is built (particularly on
                // a delete action). Using the parent entry gives no errors, but tends to collapse 
                // branches in the parent. Some method of using the parent but ensuring branches remain
                // open is called for.               
//                ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( editCommand.getChildEntry() ) ;
                ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( editCommand.getParentEntry() ) ;
                
                editCommand.adjustBranches() ;
                        
                //
                // Highlight the edited node...
//                TreeSelectionModel selectionModel =  (TreeSelectionModel)adqlTree.getSelectionModel() ; 
//                selectionModel.setSelectionPath( new TreePath( editCommand.getChildEntry().getPath() ) ) ;
                adqlTree.clearSelection() ;
                adqlTree.ensureSomeNodeSelected( editCommand ) ;
                adqlTree.repaint() ;
            }
            maintainHistory( null, image ) ;
            }
            finally {
                if( log.isTraceEnabled() ) { exitTrace( "AdqlMainView.executeEditCommand()" ) ; }
            }
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

    
    
    
    /* (non-Javadoc)
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged( ChangeEvent e) {
        if( log.isTraceEnabled() ) enterTrace( "stateChanged()"  ) ;
        if( e.getSource() == adqlTree ) {
            if( adqlTree.isCatalogueResourceSet() ) {
                chooseResourceButton.setEnabled( false ) ;
                formatCatalogTab() ;
            }
            else {
                chooseResourceButton.setEnabled( true ) ;
            } 
            if( log.isTraceEnabled() ) {
                log.debug( "e.getSource() == adqlTree" ) ;
                exitTrace( "stateChanged()"  ) ;
            }
            return ;
        }
        if( e.getSource() instanceof JTabbedPane == false ) {
            if( log.isTraceEnabled() ) {
                log.debug( "e.getSource() instanceof JTabbedPane == false" ) ;
                exitTrace( "stateChanged()"  ) ;
            }
            return ;
        }
          
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
                String text = menu.getText();
                if( name == null )
                    continue ;
                if( name.equals("Edit") || text.equals("Edit")) {
                    bEditFound = true ;
                    menu.setEnabled( true ) ;
                }
                else if( name.equals( "Options" ) || text.equals("Edit") ) {
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
                if( log.isDebugEnabled() ) {
                    log.debug( "site of: causes a NPE on startup." ) ;
                }
            	// NW - temporarily edited out - causes a NPE on startup.
            	//
            	//     buildEditMenu( menu, (AdqlNode)adqlTree.getLastSelectedPathComponent() ) ;
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
        if( log.isTraceEnabled() ) exitTrace( "stateChanged()"  ) ;
    }
    

    /** called when value of prefernece changes */
	public void propertyChange(PropertyChangeEvent evt) {
		// test which preference has changed (in future, might be listening to more than one prefernece 
		if (evt.getSource() == this.showDebugPanePreference) {
		     boolean bState = this.showDebugPanePreference.asBoolean();
             if( bState) {
                 ADQLEditorPanel.this.adqlXmlView = new AdqlXmlView( tabbedEditorPane ) ;
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