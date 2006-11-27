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

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedInputStream;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Arrays ;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
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
import javax.swing.event.EventListenerList;
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
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.TableBeanComparator;
import org.astrogrid.acr.dialogs.RegistryGoogle;
import org.astrogrid.acr.ivoa.Adql074;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.adql.AdqlStoX;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils ;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTransformer;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.TableMetadataPanel;
import org.astrogrid.desktop.modules.adqlEditor.commands.AbstractCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.ColumnInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandFactory;
import org.astrogrid.desktop.modules.adqlEditor.commands.CutCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EnumeratedInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EnumeratedElementInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.PasteIntoCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.PasteNextToCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.PasteOverCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.StandardInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.TableInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
import org.astrogrid.desktop.modules.adqlEditor.nodes.NodeFactory;
import org.astrogrid.desktop.modules.ag.ApplicationsImpl;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.oldquery.sql.Sql2Adql;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author jl99
 *
 */
public class ADQLToolEditorPanel extends AbstractToolEditorPanel implements ToolEditListener, TreeModelListener {
    
    private static final Log log = LogFactory.getLog( ADQLToolEditorPanel.class ) ;
    private static final boolean DEBUG_ENABLED = false ;
    private static final boolean TRACE_ENABLED = false ;
    private static StringBuffer logIndent ;
    static {
        if( DEBUG_ENABLED | TRACE_ENABLED ) {
            logIndent = new StringBuffer() ;
        }
    }   
  
    private ParameterValue queryParam = null ;
    
    protected final MyspaceInternal myspace ;  
    protected final UIComponent parent;
    protected final RegistryGoogle regChooser;
    protected final ResourceChooserInternal resourceChooser;
    protected final Registry registry ;
      
    private AdqlStoX adqlCompiler ;
    
    // LHS Editor tabs...
    private JTabbedPane tabbedEditorPane ;
    // This is the tree tab...
    private AdqlTree adqlTree ;
    private AdqlTreeView adqlTreeView ;
    private ControllerImpl controller ;
    
    // This is the text tab...
    private AdqlStringView adqlStringView ;
    
    // This is the adql/x tab...
    private AdqlXmlView adqlXmlView ;
    
    //RHS Metadata tabs...
    private JTabbedPane tabbedMetadataPane ;
    
    // Bottom View Tabs...
    private JTabbedPane tabbedBottomPane;
    // This is the diagnostics tab...
    private JTextPane diagnostics ;
    
    //private static XmlObject root ;
    
    private JButton chooseResourceButton ;
    
    //
    // This is a limited adhoc approach to clipboard and editing.
    // Needs to be rewritten to fit into undoable framework...
    private XmlObject clipBoard = null ;

    private Popup popup = null ;
    private BranchExpansionListener branchExpansionListener = null ;
    private AdqlTransformer transformer ;
    private boolean statusAfterValidate = false ;
          
    public ADQLToolEditorPanel( ToolModel toolModel
                              , ResourceChooserInternal resourceChooser
                              , RegistryGoogle regChooser
                              , UIComponent parent
                              , MyspaceInternal myspace
                              , Registry registry ) {
        super( toolModel );
        this.parent = parent;
        this.regChooser = regChooser;
        this.resourceChooser = resourceChooser ; 
        this.myspace = myspace ;
        this.registry = registry ;
        this.transformer = new AdqlTransformer() ;
        this.toolModel.addToolEditListener( this );
    }
    
 
    public void parameterAdded(ToolEditEvent te) {
        // can't apply to this - assume query is mandatory
    }
    
    public void parameterChanged(ToolEditEvent te) {
        if( TRACE_ENABLED ) log.debug( "parameterChanged(ToolEditEvent te)"  ) ;
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
    }
   
    public void parameterRemoved(ToolEditEvent te) {
        // can't apply - assume query is mandatory.
    }
    
    public void toolChanged(ToolEditEvent te) {
        if( TRACE_ENABLED ) log.debug( "toolChanged(ToolEditEvent te)"  ) ;
        toolSet(te);
    }
   
    public void toolCleared(ToolEditEvent te) {
        if( TRACE_ENABLED ) log.debug( "toolCleared(ToolEditEvent te)"  ) ;
        queryParam = null;
        setEnabled(false);
        this.removeAll() ;
    }
    
    public void toolSet(ToolEditEvent te) {
        if( TRACE_ENABLED ) log.debug( "toolCleared(ToolEditEvent te)"  ) ;
        String[] toks = ApplicationsImpl.listADQLParameters(toolModel.getTool().getInterface(),toolModel.getInfo());
        if (toks.length > 0) {
            setEnabled(true);
            queryParam = (ParameterValue)toolModel.getTool().findXPathValue("input/parameter[name='" + toks[0] +"']");
            this.removeAll() ;
            this.init() ;
        }
    }
    
   
    private AdqlTree setAdqlTree() { 
        if( TRACE_ENABLED ) log.debug( "setAdqlTree"  ) ;
        URI toolIvorn = toolModel.getInfo().getId() ;
        String query = null ;
        this.adqlTree = null ;
        if( queryParam == null ) {
            if( this.adqlTree == null ) {
                this.adqlTree = new AdqlTree( registry, toolIvorn ) ;
            }
        }
        //
        // Not quite sure of the reason for the hasIndirect() method call.
        // However, I will test for the presence of the indirection flag AND
        // whether it is set to true....
        else if( queryParam.hasIndirect() == true  && queryParam.getIndirect() == true ) {
            if( DEBUG_ENABLED ) log.debug( "Query is a remote reference." ) ;
            query = readQuery() ;
            if( query == null || query.length() < 5 ) {
                this.adqlTree = new AdqlTree( registry, toolIvorn ) ;
            }
            else if( query.startsWith( "<" ) ) {
                query = adaptToVersion( query ) ;
                try {          
                    this.adqlTree = new AdqlTree( query, registry, toolIvorn ) ;
                }
                catch ( Exception ex ) {
                    this.adqlTree = new AdqlTree( registry, toolIvorn ) ;
                }
            }  
            if( DEBUG_ENABLED ) log.debug( "...setting indirect to false" ) ;
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
            if( DEBUG_ENABLED ) log.debug( "Query is inline..." ) ;
           query = queryParam.getValue() ;
           if( query == null || query.length() < 5 ) {
               this.adqlTree = new AdqlTree( registry, toolIvorn ) ;
           }
           else if( query.startsWith( "<" ) ) {
               // Assume this is an instream adql/x query...
               query = adaptToVersion( query ) ;
               try {          
                   this.adqlTree = new AdqlTree( query, registry, toolIvorn ) ;
               }
               catch ( Exception ex ) {
                   this.adqlTree = new AdqlTree( registry, toolIvorn ) ;
               }
           }
           else { 
               // Assume this is an instream adql/s query...  
               
               // JL Note. I'm not supporting this for the moment
               // because there is no easy way to see how the metadata
               // can be recovered from the adql/s in a simple way
               // (maybe some specialized comment?)
               // So for the moment...
               this.adqlTree = new AdqlTree( registry, toolIvorn ) ;
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
        if( DEBUG_ENABLED ) {
            log.debug( "ADQLToolEditorPanel.setAdqlTree()" ) ;
            log.debug( "Pretty print from root: " ) ;
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            log.debug( getRoot().toString() ) ;
        }
        this.setAdqlParameter() ;
        this.adqlTree.getModel().addTreeModelListener( this );
        return this.adqlTree ;
    }
    
    
    private String adaptToVersion( String query ) {
        String returnQuery = query ;
        StringBuffer buffer = new StringBuffer( returnQuery ) ;
        int index = buffer.indexOf( AdqlData.NAMESPACE_0_74 ) ;
        if( index != -1 ) {
            if( DEBUG_ENABLED ) log.debug( "Namespace replaced" ) ;
            buffer.replace( index
                          , index + AdqlData.NAMESPACE_0_74.length()
                          , AdqlData.NAMESPACE_1_0 ) ;
            returnQuery = buffer.toString() ;
        }
        else if( buffer.indexOf( AdqlData.NAMESPACE_1_0) == -1 ) {
            log.error( "Unrecognized namespace" ) ;
        }       
        return returnQuery ;
    }
    
    
    private String readQuery() {
        String retQuery = null ;
        BufferedInputStream bis = null ; 
        try {
            URI fileLocation = new URI( queryParam.getValue().trim() ) ;
            bis = new BufferedInputStream( myspace.getInputStream( fileLocation ), 2000 ) ;
            StringBuffer buffer = new StringBuffer( 2000 ) ;
            int c ;
            while( (c = bis.read()) != -1 ) {
                buffer.append( (char)c ) ;
            } 
            retQuery = buffer.toString() ;           
        }
        catch( Exception exception ) {
            if( DEBUG_ENABLED ) log.debug( "Failed to read adql file" ) ;
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
    
    private JTabbedPane initLeftHandView() {
        // Create the components for the left side of the split pane:
        // the panel, scrolling panel, and the XML tree it will contain.
        tabbedEditorPane = new JTabbedPane();
        tabbedEditorPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedEditorPane.setTabPlacement(SwingConstants.TOP);
        
        // Tree panel...
        this.controller = new ControllerImpl() ;
        adqlTreeView = new AdqlTreeView( tabbedEditorPane, controller ) ;
 
        // Text panel...
        adqlStringView = new AdqlStringView( tabbedEditorPane, controller ) ;     
        
        // Adql/x panel...
        adqlXmlView = new AdqlXmlView( tabbedEditorPane, controller ) ;
              
        adqlTree.addTreeSelectionListener( new TreeSelectionListener() {
            public void valueChanged( TreeSelectionEvent e ) {
//                setXmlAndStringContent() ;      
            }
        }) ;
        
        return tabbedEditorPane ;    
    }
    
    private JTabbedPane initBottomView() {
        // Create the components for the bottom view:
        // Idea is for this to contain diagnostics
        tabbedBottomPane = new JTabbedPane();
        tabbedBottomPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedBottomPane.setTabPlacement(SwingConstants.TOP);
        
        JPanel pnlContent = new JPanel();
        JScrollPane scrContent = new JScrollPane();
        diagnostics = new JTextPane();
        scrContent.setViewportView( diagnostics );
        pnlContent.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 1;
        pnlContent.add(scrContent, gbc);
        tabbedBottomPane.addTab( "Diagnostics", pnlContent ) ;
            
        return tabbedBottomPane ;          
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
        if( adqlTree.isCatalogueResourceSet() ) {
            chooseResourceButton.setEnabled( false ) ;
            formatCatalogTab() ;
        }
        else {
            chooseResourceButton.setEnabled( true ) ;
        }             
        return rhsPanel ;
    }
    
        

    /** applicable when it's a dsa-style tool - ie. has an ADQL parameter*/
    public boolean isApplicable(Tool t, CeaApplication info) {
        return t != null && info != null && ApplicationsImpl.listADQLParameters(t.getInterface(),info).length > 0;
    }

    
    
    
    public void treeNodesChanged(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.setAdqlParameter() ;
        }
    }
    public void treeNodesInserted(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.update() ;
        }
    }
    public void treeNodesRemoved(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.update() ;
        }
    }
    public void treeStructureChanged(TreeModelEvent e) {
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.update() ;
        }
    }
    
    private void update() {
        this.setAdqlParameter() ;
        this.validateAdql() ;
    }
    
    private void validateAdql() {
        if( TRACE_ENABLED ) enterTrace( "ADQLToolEditorPanel.validateAdql" ) ; 
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
        if( TRACE_ENABLED ) exitTrace( "ADQLToolEditorPanel.validateAdql" ) ; 
    }
    
    
    private interface Controller {
        public void updateModel( Object source, XmlObject root ) ;
        public XmlObject getRootInstance() ;
        public void addChangeListener( ChangeListener l ) ;
        public void removeChangeListener( ChangeListener l ) ;
    }
    
    private class ControllerImpl implements Controller {
        
        private EventListenerList listenerList = new EventListenerList();
        private XmlObject root ;
              
        public ControllerImpl() {
        }
        
        public void updateModel( Object source, XmlObject root ) {
            if( TRACE_ENABLED ) enterTrace( "ControllerImpl.updateModel" ) ; 
            this.root = root ;
            fireStateChanged( source ) ;
            if( TRACE_ENABLED ) exitTrace( "ControllerImpl.updateModel" ) ; 
        }
        
        public XmlObject getRootInstance() {
            return root ;
        }
        
        
        public void addChangeListener(ChangeListener l) {
            listenerList.add(ChangeListener.class, l);
        }
        
 
        public void removeChangeListener(ChangeListener l) {
            listenerList.remove(ChangeListener.class, l);
        }

        private void fireStateChanged( Object source ) {
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for( int i=listeners.length-2 ; i>=0 ; i-=2 ) {
                if( listeners[i] == ChangeListener.class ) {
                    ChangeEvent changeEvent = new ChangeEvent( source ) ;
                    ( (ChangeListener)listeners[i+1] ).stateChanged( changeEvent ) ;
                }          
            }
        }   
    }
    
    
//    private class BranchWillExpandListener implements TreeWillExpandListener {
//        
//        public void treeWillCollapse( TreeExpansionEvent event ) throws ExpandVetoException {
//            AdqlEntry entry = (AdqlEntry)event.getPath().getLastPathComponent() ;
//            AdqlTree.AdqlTreeCellRenderer renderer = adqlTree.getTreeCellRenderer() ;
// //           renderer.setNextValue( entry, false ) ;
//        }
//  
//        public void treeWillExpand( TreeExpansionEvent event ) throws ExpandVetoException {
//            AdqlEntry entry = (AdqlEntry)event.getPath().getLastPathComponent() ;
//            AdqlTree.AdqlTreeCellRenderer renderer = adqlTree.getTreeCellRenderer() ;
// //           renderer.setNextValue( entry, true ) ;
//        }
//    }
    
    private class BranchExpansionListener implements TreeExpansionListener {
        
        public void treeCollapsed( TreeExpansionEvent event ) {
            AdqlNode entry = (AdqlNode)event.getPath().getLastPathComponent() ;
            entry.setExpanded( false ) ;
//            AdqlTree.AdqlTreeCellRenderer renderer = adqlTree.getTreeCellRenderer() ;
//            renderer.unsetNextValue( entry ) ;
        }
    
        public void treeExpanded( TreeExpansionEvent event ) {
            AdqlNode entry = (AdqlNode)event.getPath().getLastPathComponent() ;
            entry.setExpanded( true ) ;
 //           AdqlTree.AdqlTreeCellRenderer renderer = adqlTree.getTreeCellRenderer() ;
 //           renderer.unsetNextValue( entry ) ;
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
	        return buildPopup( entry ) ;
	    }
	    
	    
	    private JPopupMenu buildPopup( AdqlNode entry ) {
	        JPopupMenu popup = new JPopupMenu( "AdqlTreeContextMenu" ) ;
	        // Place a name tag at the top with a separator.
	        // This is purely cosmetic. It does nothing.
	        // Can remove later if redundant / not liked.
	        popup.add( "Insert into " + entry.getDisplayName() ) ;
	        popup.addSeparator() ;
	        if( entry.isBottomLeafEditable() ) {
//	            popup.add( new EditAction( entry ) ) ;
	        }
	        popup.add( new CutAction( entry ) ) ;
	        popup.add( new CopyAction( entry ) ) ;
	        popup.add( new PasteIntoAction( entry ) ) ;
	        popup.add( new PasteOverAction( entry ) ) ;
	        popup.add( new  PasteNextToAction( entry, true ) ) ; // Before
	        popup.add( new  PasteNextToAction( entry, false ) ) ; // After	        
	        popup.addSeparator() ;
	        popup.add( new UndoAction() ) ;
	        popup.add( new RedoAction() ) ;
     
	        List commandArray = adqlTree.getCommandFactory().newInsertCommands( entry ) ;
	        if( commandArray != null ) {
		        popup.addSeparator() ;
	            ListIterator iterator = commandArray.listIterator() ;	            
	            while( iterator.hasNext() ) {
	                StandardInsertCommand command = (StandardInsertCommand)iterator.next() ;
	                if( !command.isChildSupportedType() )
	                    continue ;
	                if( command.isChildCascadeable() ) {
	                    popup.add( getCascadeableMenu( command ) ) ;
	                }
	                else {
	                    popup.add( new InsertAction( command.getChildDisplayName(), command ) ) ;
	                }
	            }
	        }      
	        adqlTree.add( popup ) ;
	        return popup ;
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
	       
	} // end of class PopupMenu
	
	private class CutAction extends AbstractAction {
	    private CutCommand command ;
	       
	    public CutAction( AdqlNode entry ) {
	        super( "Cut" ) ;
	        TreePath path = adqlTree.getSelectionPath() ;
	        // If the path is null or there is no parent
	        // Then we cannot cut...
	        if( path == null || path.getPathCount() < 2 ){
	            setEnabled( false ) ;
	            return ;
	        }     
	        // Get the select element path which we will also not allow to be removed
	        // (This is one below the document root)...
	        TreePath selectRoot = new TreePath( new Object[] { path.getPathComponent(0), path.getPathComponent(1) } ) ;
	        if( path.equals( selectRoot ) ) {
	            setEnabled( false ) ;
	            return ;
	        }
	        this.command = adqlTree.getCommandFactory().newCutCommand( adqlTree
	                                                                 , adqlTree.getCommandFactory().getUndoManager()
	                                                                 , (AdqlNode)path.getLastPathComponent() ) ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
//	        TreePath path = adqlTree.getSelectionPath() ;
	        //
	        // This bit ensures we can "ungrey" (ie enable) the table choice that we have just
	        // cut from a particular selection. Note this is probably weak, as I am assuming you
	        // can only choose a table once. This is probably not the case when we take joinTableType
	        // into account!!!
//	        try {
//	            String typeName = entry.getXmlObject().schemaType().getName().getLocalPart() ;
//	            if( typeName.equals( "tableType" ) || typeName.equals( "archiveTableType" ) ) {
//	                String name = ((XmlString)AdqlUtils.get( entry.getXmlObject(), "name " )).getStringValue() ;
//	                if( name != null && adqlTree.getFromTables().containsKey( name ) ) 
//	                    adqlTree.getFromTables().remove( name ) ;
//	            }
//	        }
//	        catch( Exception ex ){
//	            ; ex.printStackTrace() ;
//	        } 
	        // Now do the business...
	        clipBoard = command.getChildEntry().getXmlObject().copy() ;
	        if( command.execute() != CommandExec.FAILED ) {
	            adqlTree.getCommandFactory().retireOutstandingMultipleInsertCommands() ;
        	    // Refresh tree...
    	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( command.getParentEntry() ) ;
    	        adqlTree.repaint() ;
        	}
	    }
	}
	
	private class CopyAction extends AbstractAction {
	    private AdqlNode entry ;
	       
	    public CopyAction( AdqlNode entry ) {
	        super( "Copy" ) ;
	        this.entry = entry ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
	        TreePath path = adqlTree.getSelectionPath() ;
	        // If the path is null or there is no relevant parent
	        // Then we cannot copy this entry...
	        if( path == null || path.getPathCount() < 2 )
	            return ;
	        clipBoard = entry.getXmlObject().copy() ;
	    }
	}
	
	private class PasteOverAction extends AbstractAction {
	    
	    private PasteOverCommand command = null ;
	    
	    public PasteOverAction( AdqlNode entry ) {
	        super( "Paste over" ) ;
	        if( preConditionsForPaste() == true ) {
	            this.command = adqlTree.getCommandFactory().newPasteOverCommand( entry, clipBoard );
	        }
	        setEnabled( command != null ) ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {	        
        	if( command.execute() != CommandExec.FAILED ) {
        	    adqlTree.getCommandFactory().retireOutstandingMultipleInsertCommands() ;
        	    // Refresh tree...
    	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( command.getParentEntry() ) ;
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
	            this.command = adqlTree.getCommandFactory().newPasteNextToCommand( entry, clipBoard, before );
	        }
	        setEnabled( command != null && command.isChildEnabled() ) ;    
	    }
	    	    
	    public void actionPerformed( ActionEvent e ) {
	        if( command.execute() != CommandExec.FAILED ) {
	            adqlTree.getCommandFactory().retireOutstandingMultipleInsertCommands() ;
        	    // Refresh tree...
    	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( command.getParentEntry() ) ;
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
	                adqlTree.getCommandFactory().newPasteIntoCommand( entry, clipBoard );
	        }
	        setEnabled( command != null && command.isChildEnabled() ) ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
	        if( command.execute() != CommandExec.FAILED ) {
	            adqlTree.getCommandFactory().retireOutstandingMultipleInsertCommands() ;
        	    // Refresh tree...
    	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( command.getParentEntry() ) ;
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
	        DefaultTreeModel model = (DefaultTreeModel)adqlTree.getModel() ;
	        AbstractCommand ac = undoManager.getCommandToBeUndone() ;
	        undoManager.undo() ;	       
	        model.nodeStructureChanged( (TreeNode)adqlTree.getModel().getRoot() ) ;	  
	        adqlTree.openBranches() ;
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
	        DefaultTreeModel model = (DefaultTreeModel)adqlTree.getModel() ;
	        AbstractCommand ac = undoManager.getCommandToBeRedone() ;
	        undoManager.redo() ;
	        model.nodeStructureChanged( (TreeNode)model.getRoot() ) ;
	        adqlTree.openBranches() ;
	        adqlTree.repaint() ;	     
	    }
	    
	}
	
	private class EditAction extends AbstractAction {
	    private AdqlNode entry ;
	       
	    public EditAction( AdqlNode entry ) {
	        super( "Edit" ) ;
	        this.entry = entry ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
//	        TreePath path = adqlTree.getSelectionPath() ;
//	        // If the path is null or there is no parent
//	        // Then we cannot delete this entry...
//	        if( path == null || path.getPathCount() < 2 )
//	            return ;
//	        // Get the select element path which we will also not allow to be removed
//	        // (This is one below the document root)...
//	        TreePath selectRoot = new TreePath( new Object[] { path.getPathComponent(0), path.getPathComponent(1) } ) ;
//	        if( path.equals( selectRoot ) ) 
//	            return ;
//	        AdqlEntry parent = (AdqlEntry)path.getParentPath().getLastPathComponent()  ;
//	        AdqlEntry.removeInstance( parent, entry ) ;
//	        clipBoard.add( entry ) ;
//	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( parent ) ;
//            adqlTree.repaint() ;
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
                                                                   , "(@xsi:type &= '*TabularDB')"
                                                                   ) ;
                   if( selection != null && selection.length > 0 ) {
                       if( DEBUG_ENABLED ) log.debug( "regChooser.chooseResourceWithFilter() returned object of type: " + selection[0].getClass().getName() ) ; 
                       adqlTree.setCatalogueResource( (DataCollection)selection[0]) ;
                       chooseResourceButton.setEnabled( false ) ;
                       formatCatalogTab() ;
                   }
                }
            });
        }
        return chooseResourceButton;
    }
    
    private void formatCatalogTab() {
    	DataCollection catalogueResource = adqlTree.getCatalogueResource() ;
        String title = null ;
        String description = null ;
        if( tabbedMetadataPane.getTabCount() == 1 ) {
            Catalog dbs = catalogueResource.getCatalog();           
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
                cic.setArchive( adqlTree.getCatalogueResource().getCatalog() ) ;
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
            Catalog db = tdb.getCatalog();
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
            Catalog db = tdb.getCatalog();
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
    

    private abstract class AdqlView extends JPanel implements ChangeListener {
        
        JTabbedPane owner ;
        Controller controller ;
        protected JComponent component ;
        boolean selected = false ;
        
        public AdqlView( JTabbedPane owner, Controller controller, JComponent component ) {
            super() ;
            this.owner = owner ;
            this.controller = controller ;
            this.component = component ;
            this.controller.addChangeListener( this ) ;
            this.initSelectedProcessing() ;
            this.initFocusProcessing() ;
        }
        
        
        protected void initKeyProcessing( JTextPane textPane ) {
            if( TRACE_ENABLED ) enterTrace( "AdqlView.initKeyProcessing" ) ;
            if( textPane != null ) {
                if( DEBUG_ENABLED ) log.debug( "initKeyProcessing triggered" ) ;
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
                        if( TRACE_ENABLED ) enterTrace( "AdqlView.VK_ENTER.actionPerformed" ) ;
                        validateAdql() ;
                        action.actionPerformed( e ) ;
                        if( TRACE_ENABLED ) exitTrace( "AdqlView.VK_ENTER.actionPerformed" ) ;
                    }
                } );                  
            }
            if( TRACE_ENABLED ) exitTrace( "AdqlView.initKeyProcessing" ) ;
        }
        
        abstract protected void validateAdql() ;
       
        protected void initSelectedProcessing() {
            if( TRACE_ENABLED ) enterTrace( "AdqlView.initSelectedProcessing" ) ;
            this.owner.addChangeListener( new ChangeListener() {
                public void stateChanged( ChangeEvent e ) {
                    if( TRACE_ENABLED ) enterTrace( "AdqlView.stateChanged" ) ;
                    Object selectedComponent = ((JTabbedPane)e.getSource()).getSelectedComponent() ;
                    if( selectedComponent == ADQLToolEditorPanel.AdqlView.this 
                        &&
                        selected == false ) {
                        selected = true ;
                        selectionGained() ;
                    }
                    else if( selected == true ){
                        selected = false ;
                        selectionLost() ;
                    }
                    if( TRACE_ENABLED ) exitTrace( "AdqlView.stateChanged" ) ;
                }
            }) ;
            if( TRACE_ENABLED ) exitTrace( "AdqlView.initSelectedProcessing" ) ;
        }
        
        protected void selectionGained() {
            if( TRACE_ENABLED ) enterTrace( "AdqlView.selectionGained" ) ;
            AdqlView.this.owner.requestFocus() ;
            if( TRACE_ENABLED ) exitTrace( "AdqlView.selectionGained" ) ;
        }
        
        abstract protected void selectionLost() ;
        
        public void stateChanged(ChangeEvent e) {
            if( TRACE_ENABLED ) enterTrace( "AdqlView.stateChanged" ) ;
            if( e != null && e.getSource() != this ) {
                refreshFromModel() ;
            }
            if( TRACE_ENABLED ) exitTrace( "AdqlView.stateChanged" ) ;
        }
        
        abstract protected void refreshFromModel() ;
        
        protected void processPotentialUpdates( XmlObject rootBefore, XmlObject rootAfter ) {
            if( TRACE_ENABLED ) enterTrace( "AdqlView.processPotentialUpdates" ) ;
            if( rootAfter != null && rootBefore != null ) {
                if( DEBUG_ENABLED ) enterTrace( "potential updates triggered" ) ;
                XmlOptions options = new XmlOptions();
                options.setSavePrettyPrint();
                options.setSavePrettyPrintIndent(4);
                XmlCursor nodeCursor = rootBefore.newCursor();               
                String xmlStringBefore = nodeCursor.xmlText(options);
                nodeCursor.dispose() ;
                nodeCursor = rootAfter.newCursor();               
                String xmlStringAfter = nodeCursor.xmlText(options);
                nodeCursor.dispose() ;
                if( xmlStringBefore.equals( xmlStringAfter ) == false )  {
                    this.controller.updateModel( this, rootAfter ) ;
                }
            }
            if( TRACE_ENABLED ) exitTrace( "AdqlView.processPotentialUpdates" ) ;
        }
        
        protected void initFocusProcessing() {  
            if( TRACE_ENABLED ) enterTrace( "AdqlView.initFocusProcessing" ) ;
            
            this.component.addFocusListener( new FocusListener() {
                public void focusGained( FocusEvent e ) {
                    if( TRACE_ENABLED ) enterTrace( "AdqlView.focusGained" ) ;
                    if( TRACE_ENABLED ) exitTrace( "AdqlView.focusGained" ) ;
                }
                public void focusLost( FocusEvent e ) {
                    if( TRACE_ENABLED ) enterTrace( "AdqlView.focusLost" ) ;
                    validateAdql() ;
                    if( TRACE_ENABLED ) exitTrace( "AdqlView.focusLost" ) ;
                }
            }) ;
            
            if( TRACE_ENABLED ) exitTrace( "AdqlView.initFocusProcessing" ) ;
        }
        
    } // end of class AdqlView
     
    private class AdqlXmlView extends AdqlView {
        
        String xmlString ;
        XmlObject rootOnGainingSelection ;
        XmlObject processedRoot ;
        
        public AdqlXmlView( JTabbedPane owner, Controller controller ) {
            super( owner, controller, new JTextPane() ) ;
            JScrollPane xmlScrollContent = new JScrollPane();
            xmlScrollContent.setViewportView( getXmlTextPane() );
            this.setLayout( new BorderLayout() ) ;
            this.add(xmlScrollContent, BorderLayout.CENTER );
            this.owner.addTab( "Adql/x", this ) ;
            //
            // Set up the Enter key as a processing key for Adql.
            // ie: everytime the user presses the Enter key we
            // validate and store the results...
            initKeyProcessing( getXmlTextPane() ) ;
        }
        
        private JTextPane getXmlTextPane() {
            return (JTextPane)component ;
        }
        
        protected void selectionGained() {
            if( TRACE_ENABLED ) enterTrace( "AdqlXmlView.selectionGained" ) ;
            super.selectionGained() ;
            refreshFromModel() ;
//            validateAdql() ;
            if( TRACE_ENABLED ) exitTrace( "AdqlXmlView.selectionGained" ) ;
        }
        
        protected void selectionLost() {
            if( TRACE_ENABLED ) enterTrace( "AdqlXmlView.selectionLost" ) ;
            validateAdql() ;
            if( TRACE_ENABLED ) exitTrace( "AdqlXmlView.selectionLost" ) ;
        }
        
        protected void refreshFromModel() {
            if( TRACE_ENABLED ) enterTrace( "AdqlXmlView.refreshFromModel" ) ;
            this.rootOnGainingSelection = this.controller.getRootInstance() ;
            this.rootOnGainingSelection = AdqlUtils.unModifyQuotedIdentifiers( this.rootOnGainingSelection ) ;
            XmlCursor nodeCursor = rootOnGainingSelection.newCursor();
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            options.setSavePrettyPrintIndent(4);
            getXmlTextPane().setText( nodeCursor.xmlText(options) ) ;
            nodeCursor.dispose() ;
            if( TRACE_ENABLED ) exitTrace( "AdqlXmlView.refreshFromModel" ) ;
        }
        
        protected void validateAdql() {
            if( TRACE_ENABLED ) enterTrace( "AdqlXmlView.validateAdql" ) ;
            validateAdql2() ;
            processPotentialUpdates( rootOnGainingSelection, processedRoot ) ;
            ADQLToolEditorPanel.this.validateAdql() ; 
            if( TRACE_ENABLED ) exitTrace( "AdqlXmlView.validateAdql" ) ;
        }
        
        protected void validateAdql2() {
            if( TRACE_ENABLED ) enterTrace( "AdqlXmlView.validateAdql2" ) ; ;
            String text = getXmlTextPane().getText().trim() ;
            if( text.equals( xmlString ) == false ) {
                try {
                    xmlString = text ;
                    processedRoot = SelectDocument.Factory.parse( xmlString ) ;
                    diagnostics.setText( "" ) ; 
                } 
                catch( XmlException xmle ) {
                    String message = xmle.getLocalizedMessage() ;
                    if( message != null && message.length() > 0) {
                        diagnostics.setText( message ) ;
                    }
                    else {
                        diagnostics.setText( xmle.toString() ) ;
                    }                 
                }
            }
            if( TRACE_ENABLED ) exitTrace( "AdqlXmlView.validateAdql2" ) ;
        }
        
    } // end of class AdqlXmlView

    private class AdqlTreeView extends AdqlView {
        
        public AdqlTreeView( JTabbedPane owner, Controller controller ) {
            super( owner, controller, setAdqlTree() ) ;
            final JScrollPane scrTree = new JScrollPane();
            
            scrTree.addComponentListener ( 
                    new ComponentAdapter() {
                        public void componentResized( ComponentEvent e ) {
                            adqlTree.setAvailableWidth( scrTree.getWidth() ) ;
                        }
                    } 
            ) ;
                       
            scrTree.setViewportView( component ) ;
            adqlTree.openBranches() ;
            this.controller.updateModel( this, ((AdqlNode)adqlTree.getModel().getRoot()).getXmlObject() ) ;
            this.setLayout( new BorderLayout() ) ;
            this.add(scrTree, BorderLayout.CENTER );
            this.owner.addTab( "Tree", this ) ; 
        }
        
        protected void refreshFromModel() {         
            if( TRACE_ENABLED ) enterTrace( "AdqlTreeView.refreshFromModel" ) ;
            adqlTree.setTree( NodeFactory.newInstance( this.controller.getRootInstance() ), registry, toolModel.getInfo().getId() );
            adqlTree.getModel().addTreeModelListener( ADQLToolEditorPanel.this );
            setAdqlParameter() ;
            adqlTree.openBranches() ;
            if( TRACE_ENABLED ) exitTrace( "AdqlTreeView.refreshFromModel" ) ;
        }
        
//        protected void selectionLost() {}
//        protected void validateAdql() {}
        
        protected void selectionGained() {
            if( TRACE_ENABLED ) enterTrace( "AdqlTreeView.selectionGained" ) ;
            super.selectionGained() ;
//            refreshFromModel() ;
            validateAdql() ;
            if( TRACE_ENABLED ) exitTrace( "AdqlTreeView.selectionGained" ) ;
        }
        
        protected void selectionLost() {
            if( TRACE_ENABLED ) enterTrace( "AdqlTreeView.selectionLost" ) ;
            validateAdql() ;
            if( TRACE_ENABLED ) exitTrace( "AdqlTreeView.selectionLost" ) ;
        }
        
        protected void validateAdql() {
            if( TRACE_ENABLED ) enterTrace( "AdqlTreeView.validateAdql" ) ;
            try {
               ADQLToolEditorPanel.this.validateAdql() ; 
            }
            catch ( Exception ex ) {
                ;
            }
            if( TRACE_ENABLED ) exitTrace( "AdqlTreeView.validateAdql" ) ;
        }
        
    }
      
    private class AdqlStringView extends AdqlView {
        
        //JTextPane adqlTextPane ;
        String adqlString ;
        XmlObject rootOnGainingSelection ;
        XmlObject processedRoot ;
        //boolean dirtyFlag = false ;
        
        public AdqlStringView( JTabbedPane owner, Controller controller ) {
            super( owner, controller, new JTextPane() ) ;
            //
            // Set up the text pane in a scrolling panel etc...
            JScrollPane textScrollContent = new JScrollPane();
            textScrollContent.setViewportView( getAdqlTextPane() );
            this.setLayout( new BorderLayout() ) ;
            this.add(textScrollContent, BorderLayout.CENTER );
            this.owner.addTab( "Adql/s", this ) ;
            //
            // Set up the Enter key as a processing key for Adql.
            // ie: everytime the user presses the Enter key we
            // validate and store the results...
            initKeyProcessing( getAdqlTextPane() ) ;           
        }
        
        private JTextPane getAdqlTextPane() {
            return (JTextPane)component ;
        }
        
        protected void selectionGained() {
            if( TRACE_ENABLED ) enterTrace( "AdqlStringView.selectionGained" ) ;
            super.selectionGained() ;
            refreshFromModel() ;
            if( TRACE_ENABLED ) exitTrace( "AdqlStringView.selectionGained" ) ;
        }
        
        protected void selectionLost() {
            if( TRACE_ENABLED ) enterTrace( "AdqlStringView.selectionLost" ) ;
            validateAdql() ;
            if( TRACE_ENABLED ) exitTrace( "AdqlStringView.selectionLost" ) ;
        }
        
        protected void refreshFromModel() {
            if( TRACE_ENABLED ) enterTrace( "AdqlStringView.refreshFromModel" ) ;
            this.rootOnGainingSelection = this.controller.getRootInstance() ;
            this.rootOnGainingSelection = AdqlUtils.modifyQuotedIdentifiers( this.rootOnGainingSelection ) ;
            XmlCursor nodeCursor = rootOnGainingSelection.newCursor();
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            options.setSavePrettyPrintIndent(4);
            String text = nodeCursor.xmlText(options);
            nodeCursor.dispose() ;
            this.rootOnGainingSelection = AdqlUtils.unModifyQuotedIdentifiers( this.rootOnGainingSelection ) ;
            getAdqlTextPane().setText( transformer.transformToAdqls( text, " " ).trim() ) ; 
            if( TRACE_ENABLED ) exitTrace( "AdqlStringView.refreshFromModel" ) ;
        }
        
        protected void validateAdql() {
            if( TRACE_ENABLED ) enterTrace( "AdqlStringView.validateAdql" ) ;
            validateAdql2() ;
            processPotentialUpdates( rootOnGainingSelection, processedRoot ) ;
            if( TRACE_ENABLED ) exitTrace( "AdqlStringView.validateAdql" ) ;
        }
        
        protected void validateAdql2() {
            if( TRACE_ENABLED ) enterTrace( "AdqlStringView.validateAdql2" ) ;
            statusAfterValidate = false ;
            String text = getAdqlTextPane().getText() ;
            if( text.lastIndexOf( ';') == -1 ){
                text = text + ';' ;
            }           
            if( text.equals( this.adqlString ) == false ) {
                try {  
                    this.adqlString = text ;
                    if( adqlCompiler == null ) {
                        adqlCompiler = new AdqlStoX( new StringReader( adqlString ) ) ;
                    }
                    else {
                        adqlCompiler.ReInit( new StringReader( adqlString ) ) ;
                    }
                    this.processedRoot = adqlCompiler.compileToXmlBeans() ;
                    diagnostics.setText( "" ) ;  
                    statusAfterValidate = true ;
                } 
                catch( Throwable sx ) {
                    String message = sx.getLocalizedMessage() ;
                    if( message != null && message.length() > 0) {
                        diagnostics.setText( message ) ;
                    }
                    else {
                        diagnostics.setText( sx.toString() ) ;
                    }   
                }
            }
            if( TRACE_ENABLED ) exitTrace( "AdqlStringView.validateAdql2" ) ;
        }
           
    } // end of class AdqlStringView

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
        if( path == null || path.getPathCount() < 2 )
            return false ;
        // If the clipboard is empty then there is nothing to paste...
        if( clipBoard == null )
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
        if( actionType == EXECUTE ) {
            if( this.statusAfterValidate == false ) {
                message = "Query Panel: errors or unprocessed edits have been detected." ;
            }
        }       
        return message ;
    }
    
    private void enterTrace( String entry ) {
        log.debug( logIndent.toString() + "enter: " + entry ) ;
        indentPlus() ;
    }

    private void exitTrace( String entry ) {
        indentMinus() ;
        log.debug( logIndent.toString() + "exit : " + entry ) ;
    }
    
    private static void indentPlus() {
        logIndent.append( ' ' ) ;
    }
    
    private static void indentMinus() {
        logIndent.deleteCharAt( logIndent.length()-1 ) ;
    }
    
    
} // end of ADQLToolEditor