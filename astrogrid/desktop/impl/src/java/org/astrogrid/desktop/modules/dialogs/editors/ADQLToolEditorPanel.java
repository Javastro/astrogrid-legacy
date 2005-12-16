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

import org.astrogrid.acr.astrogrid.ApplicationInformation;

import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Input;

import org.astrogrid.acr.ServiceException;
import org.w3c.dom.Document;

import java.math.BigInteger;
import java.math.BigDecimal ;
import java.io.*;
import java.net.URI;
import java.lang.reflect.*;
import java.util.Properties;
import java.util.Stack;
import java.util.HashMap;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.TabularDatabaseInformation;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.ivoa.Adql074;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.workflowBuilder.models.SimpleWorkflowTreeModel;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.desktop.modules.ag.MyspaceInternal ;
import org.astrogrid.desktop.icons.IconHelper;
// import org.astrogrid.adql.*;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree ;
import org.astrogrid.desktop.modules.adqlEditor.AdqlEntry ;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils ;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData ;
import org.astrogrid.desktop.modules.system.transformers.AdqlTransformer ;

import org.apache.xerces.impl.dtd.XMLSimpleType;
import org.apache.xmlbeans.*;

import sun.security.krb5.internal.crypto.c;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter ;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ListIterator ;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Hashtable ;
import java.util.List;

import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath ;
import javax.swing.tree.DefaultTreeModel; 

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeExpansionListener ;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeWillExpandListener ;
import javax.swing.event.TreeExpansionEvent ;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.DefaultListModel;
import javax.swing.tree.DefaultTreeCellRenderer ;

import jedit.JEditTextArea;
import jedit.SyntaxDocument;
import jedit.TSQLTokenMarker;
import jedit.TextAreaDefaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.adql.v1_0.beans.* ;

/**
 * @author jl99
 *
 */
public class ADQLToolEditorPanel extends AbstractToolEditorPanel implements ToolEditListener, TreeModelListener {
    
    private static final Log log = LogFactory.getLog( ADQLToolEditorPanel.class ) ;
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    private static final char[] ALIAS_NAMES = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    
    private ParameterValue queryParam = null ;
    
    protected final MyspaceInternal myspace ;  
    protected final UIComponent parent;
    protected final RegistryChooser regChooser;
    protected final Adql074 validator;
    protected final ResourceChooserInternal resourceChooser;
    
    // This is the tree on the left.
    private AdqlTree adqlTree ;
    
    private static XmlObject root ;
    
    private JButton chooseResourceButton ;
    private JButton validateButton ;
    // private JMenu columnMenu;
    // private Hashtable tableMenus = new Hashtable() ;  
    private ArrayList clipBoard = new ArrayList() ;
    private AliasStack aliasStack ;
    private TabularDatabaseInformation catalogueResource = null ;
    private HashMap fromTables = new HashMap() ;
    private Popup popup = null ;
    private BranchExpansionListener branchExpansionListener = null ;
    private AdqlTransformer transformer ;
    
    public ADQLToolEditorPanel( ToolModel toolModel
                              , ResourceChooserInternal resourceChooser
                              , RegistryChooser regChooser
                              , Adql074 adql
                              , UIComponent parent
                              , MyspaceInternal myspace ) {
        super( toolModel );
        this.validator = adql;
        this.parent = parent;
        this.regChooser = regChooser;
        this.resourceChooser = resourceChooser ; 
        this.myspace = myspace ;
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
        String[] toks = listADQLParameters(toolModel.getTool().getInterface(),toolModel.getInfo());
        if (toks.length > 0) {
            setEnabled(true);
            queryParam = (ParameterValue)toolModel.getTool().findXPathValue("input/parameter[name='" + toks[0] +"']");
            this.removeAll() ;
            this.init() ;
        }
    }
    
   
    private AdqlTree setAdqlTree() { 
        if( TRACE_ENABLED ) log.debug( "setAdqlTree"  ) ;
        this.adqlTree = null ;
        aliasStack = new AliasStack() ;
        if( queryParam == null ) {
            if( this.adqlTree == null ) {
                this.adqlTree = new AdqlTree() ;
            }
        }
        else if( queryParam.hasIndirect() == true ) {
                  
            try {
                URI fileLocation = new URI( queryParam.getValue().trim() ) ;               
                InputStream is = myspace.getInputStream( fileLocation );
                if( this.adqlTree == null ) {
                    this.adqlTree = new AdqlTree( is );
                }
                else {
                    this.adqlTree.setTree( AdqlTree.parseXml( is ) ) ;
                }
                
            } catch (Exception e) {
                e.printStackTrace() ;
            } 
              
        }
        else {          
           log.debug( "Query is inline..." ) ;
           String query = queryParam.getValue() ;
           if( query == null || query.length() < 5 ) {
               this.adqlTree = new AdqlTree() ;
           }
           else if( query.startsWith( "<" ) ) {
               // Assume this is an instream adql/x query...
               StringBuffer buffer = new StringBuffer( query ) ;
               int index ;
               String nameSpace74 = "http://www.ivoa.net/xml/ADQL/v0.74" ;
               String nameSpace10 = "http://www.ivoa.net/xml/ADQL/v1.0" ;
               // Here is a kludge ...
               // "http://www.ivoa.net/xml/ADQL/v1.0"
               index = buffer.indexOf( nameSpace74 ) ;
               if( index != -1 ) {
                   log.debug( "Namespace replaced" ) ;
                   buffer.replace( index, index + nameSpace74.length(), nameSpace10 ) ;
               }
               try {          
                   this.adqlTree = new AdqlTree( query ) ;
               }
               catch ( Exception ex ) {
                   this.adqlTree = new AdqlTree() ;
               }
           }
           else {
               // Assume this is an instream adql/s query...          
               Document doc = null ;
               try {          
                   doc = validator.s2x( query ) ;
                   this.adqlTree = new AdqlTree( doc ) ;
               }
               // this needs to be refined...
               catch ( Exception se ) {
                   log.debug( "Failed parsing by validator...", se ) ;
                   this.adqlTree = new AdqlTree() ;
               }
           } 
        } 
        this.adqlTree.setEnabled( true ) ;
        this.adqlTree.addTreeExpansionListener( new BranchExpansionListener() ) ;
        this.adqlTree.addMouseListener( new Popup() ) ;
        root = ((AdqlEntry)(this.adqlTree.getModel().getRoot())).getXmlObject() ;
        
        if( DEBUG_ENABLED ) {
            log.debug( "ADQLToolEditorPanel.setAdqlTree()" ) ;
            log.debug( "Pretty print from root: " ) ;
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            log.debug( root.toString() ) ;
        }
 
        this.setAdqlParameter() ;
        this.adqlTree.getModel().addTreeModelListener( this ); 
        return this.adqlTree ;
    }
    
    
    private void init() {
        setLayout( new GridBagLayout() ) ;       
        GridBagConstraints gbc ;
        JSplitPane splAdqlToolEditor = new JSplitPane();

        // Put the tree panel in the left side of the split pane.
        splAdqlToolEditor.setLeftComponent( initTreeView() ) ;

        // Put the content panel in the right side of the split pane.
//        splAdqlToolEditor.setRightComponent( initEditorView() ) ;
        splAdqlToolEditor.setRightComponent( initTextView() ) ;

        // Set the rest of the split pane's properties,
        splAdqlToolEditor.setDividerLocation(300);
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
    
    
    private JPanel initTreeView() {
        // Create the components for the left side of the split pane:
        // the panel, scrolling panel, and the XML tree it will contain.
        JPanel pnlTree = new JPanel();
        JScrollPane scrTree = new JScrollPane();
        scrTree.setViewportView( setAdqlTree() ) ;
        pnlTree.setLayout( new GridBagLayout() ) ;
        GridBagConstraints gbc = new GridBagConstraints() ;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = 7 ;
        gbc.gridwidth = 8 ;
        gbc.gridx = 0 ;
        gbc.gridy = 0 ;   
        gbc.fill = GridBagConstraints.BOTH ;
        pnlTree.add(scrTree, gbc);

        gbc.weighty = 0;
        gbc.gridheight = 1 ;
        gbc.gridwidth = 8 ;
        gbc.gridx = 0 ;
        gbc.gridy = 7 ;        
        gbc.fill = GridBagConstraints.HORIZONTAL ;
        
        Box buttonBox = new Box( BoxLayout.X_AXIS );
        buttonBox.add( getChooseResourceButton() );
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add( getValidateButton() ); 
        pnlTree.add(buttonBox, gbc);
   
        return pnlTree ;    
    }
    
    
    private JSplitPane initEditorView() {
        JSplitPane splEditorView = new JSplitPane();
        splEditorView.setTopComponent( initNodeEditor() ) ;
        splEditorView.setBottomComponent( initTextView() ) ;
        splEditorView.setOrientation( JSplitPane.VERTICAL_SPLIT ) ;
        return splEditorView ;
    }
    
    private JPanel initNodeEditor() {
        return new NodeEditor() ;
    }
    
    private JPanel initTextView() {
        // Create the components for the right side of the split pane:
        JPanel pnlContent = new JPanel();
        JScrollPane scrContent = new JScrollPane();
        final JTextPane txtpnlContent = new JTextPane();
        scrContent.setViewportView(txtpnlContent);
        pnlContent.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 1;
        pnlContent.add(scrContent, gbc);
        
        adqlTree.addTreeSelectionListener( new TreeSelectionListener() {
            public void valueChanged( TreeSelectionEvent e ) {

                AdqlEntry selectedEntry = (AdqlEntry) adqlTree.getLastSelectedPathComponent();
                if (selectedEntry == null)
                    return;

                StringBuffer buffer = new StringBuffer();
                XmlObject node = selectedEntry.getXmlObject();
                XmlCursor nodeCursor = node.newCursor();
                XmlOptions options = new XmlOptions();
                options.setSavePrettyPrint();
                options.setSavePrettyPrintIndent(4);
                String xmlString = nodeCursor.xmlText(options);
                nodeCursor.dispose() ;
                buffer
                	.append( xmlString )
                	.append( "\n\n\nIn Adql/s:\n")
                	.append( transformer.transformToAdqls( xmlString ) ) ;
                txtpnlContent.setText( buffer.toString() ) ;
            }
        }) ;
        
        
        return pnlContent ;
    }
    
    
    /** returns true if this app has an adql parameter */
    public static String[] listADQLParameters(String interfaceName,ApplicationInformation info) {
        InterfaceBean ib = null;
        List results = new ArrayList();
        for (int i = 0; i < info.getInterfaces().length; i++) {        
            if (info.getInterfaces()[i].getName().equals(interfaceName)) {
                ib = info.getInterfaces()[i];
            }            
        }
        if (ib == null) {
            return new String[]{};
        }
        for (int i =0; i < ib.getInputs().length; i++) {
            ParameterReferenceBean prb = ib.getInputs()[i];
            ParameterBean pb = (ParameterBean)info.getParameters().get(prb.getRef());
            if (pb == null) {
                return new String[]{};
            }
            if (pb.getType().equalsIgnoreCase("adql")) {
                results.add(pb.getName());
            }
        }
        return (String[])results.toArray(new String[]{});
        
    }
    

    /** applicable when it's a dsa-style tool - ie. has an ADQL parameter*/
    public boolean isApplicable(Tool t, ApplicationInformation info) {
        return t != null && info != null && listADQLParameters(t.getInterface(),info).length > 0;
    }

    
    
    
    public void treeNodesChanged(TreeModelEvent e) {
        if( e != null && e.getSource() != ADQLToolEditorPanel.this ) {
            this.setAdqlParameter() ;
        }
    }
    public void treeNodesInserted(TreeModelEvent e) {
        if( e != null && e.getSource() != ADQLToolEditorPanel.this ) {
            this.setAdqlParameter() ;
        }
    }
    public void treeNodesRemoved(TreeModelEvent e) {
        if( e != null && e.getSource() != ADQLToolEditorPanel.this ) {
            this.setAdqlParameter() ;
        }
    }
    public void treeStructureChanged(TreeModelEvent e) {
        if( e != null && e.getSource() != ADQLToolEditorPanel.this ) {
            this.setAdqlParameter() ;
        }
    }
    
    private class NodeEditor extends JPanel {
        
        private JLabel identityInfo ;
        private JList list ;
        private DefaultListModel model ;
        
        public NodeEditor() {
            setLayout( new GridBagLayout() ) ;
            GridBagConstraints gbc = new GridBagConstraints() ;
            gbc.anchor = GridBagConstraints.CENTER ;
            gbc.fill = GridBagConstraints.HORIZONTAL ;
            gbc.gridheight = 1 ;
            gbc.gridwidth = 9 ;
            gbc.weightx = 1 ;
            gbc.weighty = 0 ;
            gbc.gridx = 0 ;
            gbc.gridy = 0 ;
            identityInfo = new JLabel( " " ) ;
            add( identityInfo, gbc ) ;
            JScrollPane scroll = new JScrollPane() ;
            model = new DefaultListModel() ;
            list = new JList( model ) ;
            scroll.setViewportView( list ) ;
            gbc.anchor = GridBagConstraints.WEST ;
            gbc.fill = GridBagConstraints.BOTH ;
            gbc.weightx = 1 ;
            gbc.weighty = 1 ;
            gbc.gridheight = 5 ;
            gbc.gridwidth = 7 ;
            gbc.gridx = 0 ;
            gbc.gridy = 1 ;
            add( scroll, gbc ) ;
//            gbc.anchor = GridBagConstraints.WEST ;
//            gbc.fill = GridBagConstraints.VERTICAL ;
//            gbc.weightx = 0 ;
//            gbc.weighty = 1 ;
//            gbc.gridheight = 5 ;
//            gbc.gridwidth = 2 ;
//            gbc.gridx = 7 ;
//            gbc.gridy = 1 ;
//            add( new ButtonBox(), gbc ) ;
            new SelectionListener() ;
              
        }
        
        
        private class SelectionListener implements TreeSelectionListener {
            
            public SelectionListener() {
                adqlTree.addTreeSelectionListener( this ) ;
            }
           
            public void valueChanged( TreeSelectionEvent event ) {
                AdqlEntry chosenNode = (AdqlEntry) adqlTree.getLastSelectedPathComponent();   
                if( chosenNode == null )
                    return ;
            
                model.removeAllElements() ;
                XmlObject node = chosenNode.getXmlObject() ;                  
                XmlCursor cursor = node.newCursor() ;
                
                identityInfo.setText( AdqlUtils.extractDisplayName( node ) ) ;
                
                if( node.schemaType().isSimpleType() ) {
                    String value = null ;
                    try {
                        value = ((SimpleValue)node).getStringValue() ;
                        if( value != null ) {
                            value = value.trim() ;
                        }
                    }
                    catch( Exception ex ) {
                        value = "{missing value}" ;
                    }              
                    if(  value.length() > 0  ) {
                        model.addElement( "Value: " + value ) ;
                    }               
                }
       
                if( cursor.toFirstAttribute() ) {
                    do {
                        if( cursor.getName().getLocalPart().equals( "type" ) )
                            continue ;
                        model.addElement( "Attribute: " + cursor.getName().getLocalPart() + "=\"" + cursor.getTextValue() +"\"" ) ;
                    } while( cursor.toNextAttribute() ) ;
                }
                // I dont think I should need to do this, but it works...
                cursor.dispose() ;
                cursor = node.newCursor() ;
                
                if( cursor.toFirstChild() ) {
                    do {
                        model.addElement( "Element: " + cursor.getName().getLocalPart() ) ;
                    } while( cursor.toNextSibling() ) ;
                }
 
                cursor.dispose() ;
                
            } 
        
        } 
        
    } // end of class NodeEditor
    
    
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
            AdqlEntry entry = (AdqlEntry)event.getPath().getLastPathComponent() ;
            entry.setExpanded( false ) ;
//            AdqlTree.AdqlTreeCellRenderer renderer = adqlTree.getTreeCellRenderer() ;
//            renderer.unsetNextValue( entry ) ;
        }
    
        public void treeExpanded( TreeExpansionEvent event ) {
            AdqlEntry entry = (AdqlEntry)event.getPath().getLastPathComponent() ;
            entry.setExpanded( true ) ;
 //           AdqlTree.AdqlTreeCellRenderer renderer = adqlTree.getTreeCellRenderer() ;
 //           renderer.unsetNextValue( entry ) ;
        }
        
    } // end of class BranchExpansionListener
 
	private class Popup extends MouseAdapter {
	    	    
	    private Hashtable popupMenus = new Hashtable() ;
	    
	    public void mouseReleased( MouseEvent event ) {
	        if( event.isPopupTrigger() 
	            ||
	            event.getButton() == MouseEvent.BUTTON3 ) {
	            int x = event.getX() ;
	            int y = event.getY() ;
	            TreePath path = adqlTree.getPathForLocation( x, y ) ;
	            if( path != null ) {
	                adqlTree.setSelectionPath( path ) ;
	                adqlTree.scrollPathToVisible( path ) ;                
	                showPopup( path, x, y ) ;
	            }                
	        }
	    } // end of Popup.mouseReleased( MouseEvent event )
	    
	    private void showPopup( TreePath path, int x, int y ) {
	        AdqlEntry entry = (AdqlEntry)adqlTree.getLastSelectedPathComponent() ;
		    getPopupMenu( entry ).show( adqlTree, x, y ) ;
	    }
	    
	    private JPopupMenu getPopupMenu( AdqlEntry entry ) {
	        String displayName = entry.getDisplayName() ;
	        JPopupMenu popup = (JPopupMenu)popupMenus.get( displayName ) ;
	        if( true ) {
//	        if( popup == null ) {
	            popup = buildPopup( entry, displayName ) ;
	        }
	        return popup ;
	    }
	    
	    
	    // 
	    // This is far too large! Refactor.
	    private JPopupMenu buildPopup( AdqlEntry entry, String targetName ) {
	        JPopupMenu popup = new JPopupMenu( targetName ) ;
	        // Place a name tag at the top with a separator.
	        // This is purely cosmetic. It does nothing.
	        // Can remove later if redundant / not liked.
	        popup.add( targetName ) ;
	        popup.addSeparator() ;
	        if( entry.isBottomLeafEditable() ) {
//	            popup.add( new EditAction( entry ) ) ;
	        }
	        popup.add( new CutAction( entry ) ) ;
//	        popup.add( new CopyAction( entry ) ) ;
//	        popup.add( new PasteAction( entry ) ) ;
	        
	        ArrayList commands = buildChildCommandData( entry, targetName ) ; 
	        SchemaType[] concreteTypes ;
	        if( commands.isEmpty() == false ) {
	            InsertAction insertAction ;
	            if( commands.size() == 1 ) {
	                CommandBean c = (CommandBean)commands.get(0) ;
	                concreteTypes = c.getConcreteTypes() ;
	                if( concreteTypes.length == 1 ) {
	                    if( c.isSupportedType( 0 ) ) {
		                    popup.addSeparator() ;
		                    if( c.isConcreteTypeCascadeable( 0 ) ) {
//		                        JMenu columnMenu = getCascadeableMenu( "Insert-1 " + AdqlUtils.extractDisplayName( concreteTypes[0] ), c, 0 );
		                        JMenu columnMenu = getCascadeableMenu( "Insert " + c.extractDisplayNameForType(0), c, 0 );
		                        popup.add( columnMenu ) ;
		                    }
		                    else {
//		                        insertAction = new InsertAction( "Insert-2 " + AdqlUtils.extractDisplayName( concreteTypes[0] ), c, 0 ) ;
		                        insertAction = new InsertAction( "Insert " + c.extractDisplayNameForType(0), c, 0 ) ;
			                    popup.add( insertAction ).setEnabled( c.isEnabled()) ;	
		                    }         
	                    }
	                }
	                else {
	                    JMenu insertMenu = new JMenu( "Insert" ) ;
			            popup.addSeparator() ;
			            popup.add( insertMenu ) ;
	                    for( int i=0; i<concreteTypes.length; i++ ) {
	                        if( c.isSupportedType( i ) ) {
		                        if( c.isConcreteTypeCascadeable( i ) ) {
//			                        JMenu columnMenu = getCascadeableMenu( AdqlUtils.extractDisplayName( concreteTypes[i] ), c, i );
		                            JMenu columnMenu = getCascadeableMenu( c.extractDisplayNameForType(i), c, i );
			                        insertMenu.add( columnMenu ) ;
			                    }
			                    else {
//			                        insertAction = new InsertAction( AdqlUtils.extractDisplayName( concreteTypes[i] ), c, i ) ;
			                        insertAction = new InsertAction( c.extractDisplayNameForType(i), c, i ) ;
			                        insertMenu.add( insertAction ).setEnabled( c.isEnabled() );
			                    }       
	                        }
	                    }
	                }
	            }
	            else {
	                JMenu insertMenu = new JMenu( "Insert" ) ;
		            ListIterator iterator ;
		            popup.addSeparator() ;
		            popup.add( insertMenu ) ;
		            iterator = commands.listIterator() ;
		            while( iterator.hasNext() ) {
		                CommandBean c = (CommandBean)iterator.next() ;
		                concreteTypes = c.getConcreteTypes() ;
		                if( concreteTypes.length == 0 ) {
//		                    insertAction = new InsertAction( AdqlUtils.extractDisplayName( c.getElement().getName().getLocalPart() ), c, -1 ) ;
		                    insertAction = new InsertAction( c.extractDisplayNameForType(-1), c, -1 ) ;
		                    insertMenu.add( insertAction ).setEnabled( c.isEnabled() ) ;
		                }
		                else {
		                    for( int i=0; i<concreteTypes.length; i++ ) {
		                        if( c.isSupportedType( 0 ) ) {
		                            if( c.isConcreteTypeCascadeable( i ) ) {
//		                                JMenu columnMenu = getCascadeableMenu( AdqlUtils.extractDisplayName( concreteTypes[i] ), c, i );
		                                JMenu columnMenu = getCascadeableMenu( c.extractDisplayNameForType(i), c, i );
				                        insertMenu.add( columnMenu ) ;
				                    }
				                    else {
//				                        insertAction = new InsertAction( AdqlUtils.extractDisplayName( concreteTypes[i] ), c, i );
				                        insertAction = new InsertAction( c.extractDisplayNameForType(i), c, i );
				                        insertMenu.add( insertAction ).setEnabled( c.isEnabled() );
				                    }        
		                        }         
		                    }
		                }
		            }
	            }
	        }       
	        adqlTree.add( popup ) ;
	        popupMenus.put( targetName, popup ) ;
	        return popup ;
	    }
	    
	    private ArrayList buildChildCommandData( AdqlEntry entry, String targetName ) {
	        ArrayList commands = new ArrayList() ;
	        SchemaProperty[] elements = entry.getElementProperties() ;
	        if( elements != null ) {
	            for( int i=0; i<elements.length; i++ ) {
	                commands.add( new CommandBean( entry
	                                             , targetName
	                                             , elements[i] ) ) ;
	            }
	        }
	        return commands ;
	    }
	    
	    
	    private JMenu getCascadeableMenu( String name, CommandBean commandBean, int concreteTypeIndex ) {
	        JMenu menu = new JMenu( name ) ;
	    
	        if( commandBean.isConcreteTypeColumnLinked( concreteTypeIndex ) ) {
	            if( fromTables.isEmpty() == false ) {
	                menu = getInsertColumnMenu( name, commandBean, concreteTypeIndex ) ;
		            menu.setText( name ) ;
		            menu.setEnabled( commandBean.isEnabled() ) ;
	            }
	            else {
	                menu.setEnabled( false ) ;
	            }
	
	        }
	        else if( commandBean.isConcreteTypeTableLinked( concreteTypeIndex ) ) {
	            if( catalogueResource != null ) { 
	                menu = getInsertTableMenu( name, commandBean, concreteTypeIndex ) ;
	                menu.setText( name ) ;
	                menu.setEnabled( commandBean.isEnabled() ) ;
	            }
	            else {
	                menu.setEnabled( false ) ;
	            }
	        }
	        else if( commandBean.isConcreteTypeEnumerable( concreteTypeIndex ) ) {
	            JMenuItem[] menuItems = getEnumeratedMenus( commandBean, concreteTypeIndex ) ;
	            for( int i=0; i<menuItems.length; i++ ) {
	                menu.add( menuItems[i] ) ;
	            } 
	            menu.setEnabled( commandBean.isEnabled() ) ;
	        }        
	        return menu;
	    }
	    
	    
	    private JMenuItem[] getEnumeratedMenus( CommandBean commandBean, int typeIndex  ) {
	        JMenuItem[] menus = null ;
	        SchemaType schemaType = null ; // Take care! This is used in two contexts.
	        SchemaStringEnumEntry[] stringEnumEntries = null ;
	        String parentTypeName = null ;
	        String attributeTypeName = null ; // ie: a type used by attributes.
	        String name = null ;
	        try {
	            schemaType = commandBean.getConcreteTypes()[typeIndex];
	            parentTypeName = schemaType.getName().getLocalPart() ;
	            attributeTypeName = (String)AdqlData.ENUMERATED_ATTRIBUTES.get( parentTypeName ) ;
	            schemaType = getAttributeType( attributeTypeName, schemaType.getTypeSystem() ) ;
	            stringEnumEntries = schemaType.getStringEnumEntries() ;
	            menus = new JMenuItem[ stringEnumEntries.length ] ;
		        for( int i=0; i<stringEnumEntries.length; i++ ) {
		            name = stringEnumEntries[i].getString() ;
		            if( name != null ) {
		                menus[i] = new JMenuItem( new InsertEnumeratedAction( name, commandBean, typeIndex ) ) ;
		            }
		        }
	        }
	        catch( Exception ex ) {
	            menus = new JMenuItem[]{ new JMenuItem( "whoops" ) } ;
	            ex.printStackTrace() ;
	        }
	        return menus ;
	    }
	    
	    private SchemaType getAttributeType( String unqualifiedName, SchemaTypeSystem typeSystem ) {
	        SchemaType schemaType = null ;
	        SchemaType[] sgTypes = typeSystem.globalTypes() ;
	        for( int i=0; i<sgTypes.length; i++ ) {
	            if( sgTypes[i].getName().getLocalPart().equals( unqualifiedName ) ) {
	                schemaType = sgTypes[i] ;
	                break ;
	            }
	        }
	        return schemaType ;
	    }
	    
	   
	    
	    public class CommandBean {      
	        private String displayName ;
	        private SchemaProperty element ;
	        private AdqlEntry entry ;
	        private SchemaType[] concreteTypes ;
	        private boolean enabledStatus = false ;
	        private BigInteger minOccurs ;
	        private BigInteger maxOccurs ;
	        
	        public CommandBean( AdqlEntry entry
	                          , String displayName
	                          , SchemaProperty element ) {
	            this.entry = entry ;
	            this.displayName = displayName ;
	            this.element = element ;
	            this.minOccurs = element.getMinOccurs() ;
	            this.maxOccurs = element.getMaxOccurs() ;
	            this.concreteTypes = AdqlUtils.getConcreteSubtypes( element.getType() ) ;
	            // Experiment. The idea of concreteSubtypes was where an element used abstract
	            // types. We searched for the concrete ones that fulfilled its existence.
	            // However, elements always have a concrete type. They may be defined directly
	            // as of a particular type, which I have not been picking up. So....
	            // If there are no concrete subtypes (ie: the element does not depend on abstract
	            // types), then we use its basic definition...
	            if( concreteTypes == null || concreteTypes.length == 0 ) {
	                concreteTypes = new SchemaType[] { element.getType() } ;
	            }
	            this.extractEnabledStatus() ;
	        }
	        public String getDisplayName() { return displayName; }
            public SchemaProperty getElement() { return element; }
            public AdqlEntry getEntry() { return entry; }
            public String getElementName() { return element.getName().getLocalPart() ; }
            public BigInteger getMinOccurs() { return minOccurs ; }
            public BigInteger getMaxOccurs() { return maxOccurs ; }
            public SchemaType[] getConcreteTypes() { return concreteTypes ; }
            public boolean isEnabled() { return this.enabledStatus ; }
            public boolean isOptionalSingleton() {
                if( minOccurs.intValue() > 0  ||  maxOccurs == null  ||  maxOccurs.intValue() > 1 )
                    return false ;
                return true ;
            }
            public boolean isSupportedType( int indexOfType ) {
                boolean retValue = true ;
                // Testing for -1 and allowing it through as supported is balancing on a knife.
                // Good! Bound to find out something.
                if( indexOfType != -1  
                    &&
                    AdqlData.UNSUPPORTED_TYPES.containsKey( concreteTypes[indexOfType].getName().getLocalPart() ) )
                    retValue = false ;
                return retValue ;
            }
            public boolean isMandatorySingleton() {
                if( minOccurs.intValue() != 1  ||  maxOccurs == null  ||  maxOccurs.intValue() != 1 )
                    return false ;
                return true ;
            }
            public boolean isArray() {
                if( maxOccurs == null  ||  maxOccurs.intValue() > 1 )
                    return true ;
                return false ;
            }
            private void extractEnabledStatus() {
                XmlObject o = entry.getXmlObject() ;
                String e = getElementName() ;
                if( isOptionalSingleton() ) {
                    boolean isSet = AdqlUtils.isSet( o, e );
                    if( !isSet ) 
                        this.enabledStatus = true ;
                }
                else if( isMandatorySingleton() ) {
                    Object obj = AdqlUtils.get( o, e );
                    if( obj == null )
                        this.enabledStatus = true ;
                }
                else if( isArray() ) {
                    if( this.maxOccurs == null 
                        ||
                        AdqlUtils.sizeOfArray( o, e ) < this.maxOccurs.intValue() )
                    	this.enabledStatus = true ;
                }
                else {
                    // We should report this.
                }
            }
            
            public boolean isConcreteTypeCascadeable( int index ) {
                boolean answer = false ;
                String name = null ;
                if( index > -1  && this.concreteTypes.length > 0 ) {
                    try {
                        name = concreteTypes[index].getName().getLocalPart();
                        answer = AdqlData.CASCADEABLE.containsKey( name ) ;
                    }
                    catch( Exception ex ) {
                        ex.printStackTrace() ;
                    }                    
                }
                return answer ;
            }
            
            private boolean isConcreteTypeTableLinked( int index ) {
                boolean answer = false ;
                String name = null ;
                if( index > -1  && this.concreteTypes.length > 0 ) {
                    try {
                        name = concreteTypes[index].getName().getLocalPart();
                        answer = AdqlData.METADATA_LINK_TABLE.containsKey( name ) ;
                    }
                    catch( Exception ex ) {
                        ex.printStackTrace() ;
                    }             
                }
                return answer ;
            }
            
            private boolean isConcreteTypeColumnLinked( int index ) {
                boolean answer = false ;
                String name = null ;
                if( index > -1  && this.concreteTypes.length > 0 ) {
                    try {
                        name = concreteTypes[index].getName().getLocalPart();
                        answer = AdqlData.METADATA_LINK_COLUMN.containsKey( name ) ;
                    }
                    catch( Exception ex ) {
                        ex.printStackTrace() ;
                    }          
                }
                return answer ;
            }
            
            private boolean isConcreteTypeEnumerable( int index ) {
                boolean answer = false ;
                String name = null ;
                if( index > -1  && this.concreteTypes.length > 0 ) {
                    try {
                        name = concreteTypes[index].getName().getLocalPart();
                        answer = AdqlData.ENUMERATED_ATTRIBUTES.containsKey( name ) ;
                    }
                    catch( Exception ex ) {
                        ex.printStackTrace() ;
                    }          
                }
                return answer ;
            }
            
            public String extractDisplayNameForType( int indexOfType ) {
                String name = null ;
                if( (concreteTypes.length > 0) 
                    &&
                    (concreteTypes[ indexOfType ].isBuiltinType() == false) ) {
                    name = AdqlUtils.extractDisplayName( concreteTypes[ indexOfType ] ) ;
                }
                if( name == null || name.length() == 0 ) {
                    name = AdqlUtils.extractDisplayName( element.getName().getLocalPart() ) ;
                }
                return name ;
            }
	                
	    } // end of class CommandBean
	    
	} // end of class PopupMenu
	
	private class CutAction extends AbstractAction {
	    private AdqlEntry entry ;
	       
	    public CutAction( AdqlEntry entry ) {
	        super( "Cut" ) ;
	        this.entry = entry ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
	        TreePath path = adqlTree.getSelectionPath() ;
	        // If the path is null or there is no parent
	        // Then we cannot delete this entry...
	        if( path == null || path.getPathCount() < 2 )
	            return ;
	        // Get the select element path which we will also not allow to be removed
	        // (This is one below the document root)...
	        TreePath selectRoot = new TreePath( new Object[] { path.getPathComponent(0), path.getPathComponent(1) } ) ;
	        if( path.equals( selectRoot ) ) 
	            return ;
	        //
	        // This bit ensures we can "ungrey" (ie enable) the table choice that we have just
	        // cut from a particular selection. Note this is probably weak, as I am assuming you
	        // can only choose a table once. This is probably not the case when we take joinTableType
	        // into account!!!
	        try {
	            String typeName = entry.getXmlObject().schemaType().getName().getLocalPart() ;
	            if( typeName.equals( "tableType" ) || typeName.equals( "archiveTableType" ) ) {
	                String name = ((XmlString)AdqlUtils.get( entry.getXmlObject(), "name " )).getStringValue() ;
	                if( name != null && fromTables.containsKey( name ) ) 
	                    fromTables.remove( name ) ;
	            }
	        }
	        catch( Exception ex ){
	            ; ex.printStackTrace() ;
	        } 
	        // Now do the business...
	        AdqlEntry parent = (AdqlEntry)path.getParentPath().getLastPathComponent()  ;
	        AdqlEntry.removeInstance( parent, entry ) ;
	        clipBoard.add( entry ) ;
	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( parent ) ;
            adqlTree.repaint() ;
	    }
	}
	
	
	private class CopyAction extends AbstractAction {
	    private AdqlEntry entry ;
	       
	    public CopyAction( AdqlEntry entry ) {
	        super( "Copy" ) ;
	        this.entry = entry ;
	    }
	    
	    public void actionPerformed( ActionEvent e ) {
	        TreePath path = adqlTree.getSelectionPath() ;
	        // If the path is null or there is no relevant parent
	        // Then we cannot copy this entry...
	        if( path == null || path.getPathCount() < 2 )
	            return ;
	        clipBoard.add( entry ) ;
	    }
	}
	
	private class PasteAction extends AbstractAction {
	    private AdqlEntry entry ;
	       
	    public PasteAction( AdqlEntry entry ) {
	        super( "Paste" ) ;
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
	
	
	private class EditAction extends AbstractAction {
	    private AdqlEntry entry ;
	       
	    public EditAction( AdqlEntry entry ) {
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
	
	public class InsertAction extends AbstractAction {
	    
	    protected Popup.CommandBean commandBean ;
	    protected int indexOfConcreteSubtype ;
	    protected AdqlEntry newEntry ;
	    protected XmlObject newValueObject ;
	    
	    public InsertAction( String name, Popup.CommandBean commandBean ) {
	        super( name ) ;
	        this.commandBean = commandBean ;
	        indexOfConcreteSubtype = -1 ;   
	        if( name.length() == 0 )
	           log.debug( "Empty name for " + commandBean.getElementName() ) ;
	    }
	    
	    public InsertAction( String name, Popup.CommandBean commandBean, int indexOfConcreteSubtype ) {
	        super( name ) ;
	        this.commandBean = commandBean ;
	        this.indexOfConcreteSubtype = indexOfConcreteSubtype ;
	        if( name.length() == 0 ) {
	            log.debug( "Empty name for " + commandBean.getElementName() ) ;
	            if( indexOfConcreteSubtype == -1 )
	                log.debug( "with no concrete subtypes." ) ;
	            else 
	                log.debug( "... " + commandBean.getConcreteTypes()[indexOfConcreteSubtype].toString() ) ;
	        }
	    }
	    
	    
        public void actionPerformed(ActionEvent e) {
            TreePath path = adqlTree.getSelectionPath() ;
            if( path == null )
                return ;
            
            AdqlEntry parent = commandBean.getEntry() ;          
            if( this.indexOfConcreteSubtype == -1 ) {
                this.insertType() ;
            }
            else {
                this.insertConcreteSubtype() ;
            }
            
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
            
	        DefaultTreeModel model =  (DefaultTreeModel)adqlTree.getModel() ;
	        model.nodeStructureChanged( parent ) ;
	        path = path.pathByAddingChild( newEntry ) ;
	        adqlTree.scrollPathToVisible( path ) ;
            adqlTree.repaint() ;
            
            // Switch to edit for a newly created bottom leaf...
            if( newEntry.isBottomLeafEditable() ) {
                adqlTree.setEditingActive( true );  // is this required?
                adqlTree.startEditingAtPath( path ) ;
                adqlTree.setEditingActive( false );  // is this required?
                model.nodeChanged( newEntry ) ;
            }
            

            setAdqlParameter() ;
 
        } // end of actionPerformed() 
        
        protected void insertType() {
            log.debug( "insertType() just adding " + commandBean.getElementName() ) ;
            AdqlEntry parent = commandBean.getEntry() ;
            XmlObject o = parent.getXmlObject() ;
            XmlObject newObject = AdqlUtils.addNew( o, commandBean.getElementName() ) ;
            this.newEntry = AdqlEntry.newInstance( parent, newObject ) ;
        }
        
        protected void insertConcreteSubtype() {
            AdqlEntry parent = commandBean.getEntry() ;
            XmlObject parentObject = parent.getXmlObject() ;
            SchemaType schemaType = commandBean.getConcreteTypes()[indexOfConcreteSubtype] ;
            log.debug( "insertConcreteSubtype() just adding " + schemaType.getName().getLocalPart() ) ;
            XmlObject newObject = null ; 
            this.newEntry = null ;
            if( commandBean.isArray() ) {                
                newObject = AdqlUtils.addNewToEndOfArray( parentObject, commandBean.getElementName() ) ;
                newObject = newObject.changeType( schemaType ) ;
                newObject = setDefaultValue( newObject ) ;
                newEntry = AdqlEntry.newInstance( parent, newObject ) ;
            }
            else {
                if( schemaType.isBuiltinType() ) {
                    newValueObject = XmlObject.Factory.newInstance().changeType( schemaType ) ;
                    newValueObject = this.setDefaultValue( newValueObject ) ;
                    AdqlUtils.set( parentObject, commandBean.getElementName(), newValueObject ) ;                   
                    XmlCursor cursor = parentObject.newCursor() ;
                    cursor.toFirstChild();
                    do {
                        if( cursor.getName().getLocalPart().equals( commandBean.getElementName() ) ) {
                            newObject = cursor.getObject() ;
//                            break ;
                        }
                    } while( cursor.toNextSibling() ) ;                   
                    newEntry = AdqlEntry.newInstance( parent, newObject ) ;
                }
                else {            
                    newObject = AdqlUtils.addNew( parentObject, commandBean.getElementName() ) ;
                    if( newObject != null ) {
                        newObject = newObject.changeType( schemaType ) ;
                        newObject = setDefaultValue( newObject ) ;
                        newEntry = AdqlEntry.newInstance( parent, newObject ) ;
                    }
                    else {
                        newValueObject = XmlObject.Factory.newInstance().changeType( commandBean.getElement().javaBasedOnType() ) ;
                        newValueObject = newValueObject.changeType( schemaType ) ;
                        newValueObject = setDefaultValue( newValueObject ) ;
                        AdqlUtils.set( parentObject, commandBean.getElementName(), newValueObject ) ; 
//                        newValueObject = newValueObject.changeType( schemaType ) ;
                        XmlCursor cursor = parentObject.newCursor() ;
                        cursor.toFirstChild();
                        do {
                            if( cursor.getName().getLocalPart().equals( commandBean.getElementName() ) ) {
                                newObject = cursor.getObject() ;
                                break ;
                            }
                        } while( cursor.toNextSibling() ) ;                   
                        newEntry = AdqlEntry.newInstance( parent, newObject ) ;
                    }
                }
            }   
        }
        
        
        private XmlObject setDefaultValue( XmlObject xmlObject ) {
            XmlObject retVal = xmlObject ;     
            if( xmlObject != null ) {
                SchemaType type = xmlObject.schemaType() ;
                if( type.isBuiltinType() ){
                    retVal = setBuiltInDefaults( xmlObject ) ;
                }
                else if( isAttributeDriven( type ) ) {
                    retVal = setAttributeDrivenDefaults( xmlObject ) ;
                }
                else if( type.isSimpleType() ) {
                    retVal = setDerivedSimpleDefaults( xmlObject ) ;
                }
            }
            return retVal ;
        }
        
        private XmlObject setBuiltInDefaults( XmlObject xmlObject ) {
            int typeCode = xmlObject.schemaType().getBuiltinTypeCode() ;
            switch( typeCode ) {              
            	case SchemaType.BTC_STRING: 
            	    ((XmlString)xmlObject).setStringValue( "" ) ;
            		break ;
            	case SchemaType.BTC_DECIMAL:
            	    ((XmlDecimal)xmlObject).setBigDecimalValue( new BigDecimal(0) ) ;
            		break ;
            	case SchemaType.BTC_FLOAT:
            	    ((XmlFloat)xmlObject).setFloatValue( 0 ) ;
            		break ;
            	case SchemaType.BTC_INT:
            	    ((XmlInt)xmlObject).setIntValue( 0 ) ;
            		break ;
               	case SchemaType.BTC_INTEGER:
            	    ((XmlInteger)xmlObject).setBigIntegerValue( new BigInteger("0") ) ;
            		break ;
            	case SchemaType.BTC_DOUBLE:
            	    ((XmlDouble)xmlObject).setDoubleValue( 0 ) ;
        			break ;
               	case SchemaType.BTC_LONG:
            	    ((XmlLong)xmlObject).setLongValue( 0 ) ;
              	case SchemaType.BTC_UNSIGNED_LONG:
            	    ((XmlUnsignedLong)xmlObject).setBigDecimalValue( new BigDecimal(0) ) ;
        			break ;
              	case SchemaType.BTC_POSITIVE_INTEGER:
            	    ((XmlPositiveInteger)xmlObject).setBigDecimalValue( new BigDecimal(1) ) ;
        			break ;
              	case SchemaType.BTC_UNSIGNED_SHORT:
            	    ((XmlUnsignedShort)xmlObject).setIntValue( 0 ) ;
        			break ;
              	case SchemaType.BTC_UNSIGNED_INT:
            	    ((XmlUnsignedInt)xmlObject).setLongValue( 0 ) ;
        			break ;
            	default:
            	    ; // and for the rest do nothing (for the moment)                 
            }   
            return xmlObject ;
        }
        
        private boolean isAttributeDriven( SchemaType type ) {
            String name = (String)AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
            return ( name != null && name.length() > 0 ) ;
        }
        
        private XmlObject setAttributeDrivenDefaults( XmlObject xmlObject ) {
            XmlObject retVal = xmlObject ;
            SchemaType type = xmlObject.schemaType() ;
            String attributeName = (String)AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
            XmlString tempObject = XmlString.Factory.newInstance() ;
            tempObject.setStringValue( (String)AdqlData.ATTRIBUTE_DEFAULTS.get( type.getName().getLocalPart() ) ) ; 
            
            //
            // There is a better way to do this provided I can get at the fully qualified name
            // of the attribute. Needs thinking about.
            // Another point. The schema may itself define a default value. This too can be picked up.
            SchemaType attrType = null ;
            SchemaProperty[] attrProperties = type.getAttributeProperties() ;
            for( int i=0; i<attrProperties.length; i++ ) {
                if( attrProperties[i].getJavaPropertyName().equals( attributeName ) ) {
                    attrType = attrProperties[i].getType();
                    break ;
                }
            }
            XmlObject valueObject = tempObject.changeType( attrType ) ;        
            AdqlUtils.set( xmlObject, attributeName, valueObject ) ; 
            return xmlObject ;
        }
        
        private XmlObject setDerivedSimpleDefaults( XmlObject xmlObject ) {
            XmlObject retVal = xmlObject ;
            SchemaType type = xmlObject.schemaType() ;
            // Cooerce the empty element into an XmlString...
            XmlString tempObject = (XmlString)xmlObject.changeType( XmlString.type ) ;
            tempObject.setStringValue( (String)AdqlData.DERIVED_DEFAULTS.get( type.getName().getLocalPart() ) ) ;
            // Cooerce back to original type...
            retVal = tempObject.changeType( type ) ;
            return retVal ;
        }
        
} // end of class InsertAction
  

public class InsertTableAction extends InsertAction {  
    protected DatabaseBean db ;
    protected int indexOfTable ;
    
    public InsertTableAction( String name
                            , Popup.CommandBean commandBean
                            , int indexOfConcreteSubtype
                            , DatabaseBean db
                            , int indexOfTable ) {
        super( name, commandBean, indexOfConcreteSubtype ) ;
        this.db = db ;
        this.indexOfTable = indexOfTable ;
    }   
    
    protected void insertConcreteSubtype() {
        super.insertConcreteSubtype() ;
        String alias = aliasStack.pop() ;
        AdqlUtils.set( newEntry.getXmlObject()
                     , "alias"
                     , XmlString.Factory.newValue( alias ) ) ;
        AdqlUtils.set( newEntry.getXmlObject()
                     , "name"
                     , XmlString.Factory.newValue( db.getTables()[indexOfTable].getName() ) ) ;
        String name = commandBean.getConcreteTypes()[indexOfConcreteSubtype].getName().getLocalPart() ;
        if( name.equalsIgnoreCase( "archiveTableType" ) ) {
            AdqlUtils.set( newEntry.getXmlObject()
                         , "archive"
                         , XmlString.Factory.newValue( db.getName() ) ) ;
        }
        fromTables.put( db.getTables()[indexOfTable].getName(), new TableData( db, indexOfTable, alias ) ) ;
    }
   
} // end of class InsertTableAction



public class InsertColumnAction extends InsertAction {  
    protected TableData tableData ;
    protected ColumnBean column ;
    
    public InsertColumnAction( String name
                             , Popup.CommandBean commandBean
                             , int indexOfConcreteSubtype
                             , TableData tableData
                             , ColumnBean column ) {
        super( name, commandBean, indexOfConcreteSubtype ) ;
        this.tableData = tableData ;
        this.column = column ;
        this.putValue( AbstractAction.SHORT_DESCRIPTION, mkTooltip( column ) ) ;
    }   
    
    protected void insertConcreteSubtype() {
        super.insertConcreteSubtype() ;
        // Somehow there should be some choice here regarding whether the table name or alias is used.
        // And also somewhere a place the user can choose the alias rather than  have it automatically
        // assigned.
        AdqlUtils.set( newEntry.getXmlObject()
                     , "table"
//                     , XmlString.Factory.newValue( tableData.database.getTables()[ tableData.tableIndex ].getName() ) ) ;
                     , XmlString.Factory.newValue( tableData.alias ) ) ;
        AdqlUtils.set( newEntry.getXmlObject()
                     , "name"
                     , XmlString.Factory.newValue( column.getName() ) ) ;
    }
    
    private String mkTooltip(ColumnBean c) {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<b>Description</b>:").append(c.getDescription()).append("<br>");
        sb.append("<b>UCD</b>:").append(c.getUCD()).append("<br>");
        sb.append("<b>Datatype</b>:").append(c.getDatatype()).append("<br>");
        sb.append("<b>Unit</b>:").append(c.getUnit()).append("<br>");                            
        sb.append("</html>");
        return sb.toString();
    }
   
} // end of class InsertColumnAction


public class InsertEnumeratedAction extends InsertAction {  
    
    public InsertEnumeratedAction( String name
                                 , Popup.CommandBean commandBean
                                 , int indexOfConcreteSubtype ) {
        super( name, commandBean, indexOfConcreteSubtype ) ;
    }   
    
    protected void insertConcreteSubtype() {
        log.debug( "InsertEnumeratedAction" ) ;
        super.insertConcreteSubtype() ;
        XmlObject obj = newEntry.getXmlObject() ;
        SchemaType schemaType = obj.schemaType() ;
        SchemaType attrSchemaType = null ;
        String attributeName = null ;
        SchemaStringEnumEntry[] enumEntries = null ;
        SchemaProperty[] attrProperties = schemaType.getAttributeProperties() ;
        for( int i=0; i<attrProperties.length; i++ ) {
            attrSchemaType = attrProperties[i].getType() ;
            if( attrSchemaType.hasStringEnumValues() ) {
                attributeName = attrProperties[i].getJavaPropertyName() ;
                enumEntries = attrSchemaType.getStringEnumEntries() ;
//                for( int j=0; j<enumEntries.length; j++ ) {
//                    
//                }
                break ;
            }
        }
        XmlObject enumValue = XmlString.Factory.newInstance() ;
        enumValue = enumValue.set(  XmlString.Factory.newValue( ((String)super.getValue( NAME )).trim() ) ) ;
        enumValue = enumValue.changeType( attrSchemaType ) ;       
        AdqlUtils.set( newEntry.getXmlObject()
                     , attributeName
                     , enumValue ) ;  
    }
   
} // end of class InsertEnumeratedAction

    
    protected JButton getChooseResourceButton() {
        if (chooseResourceButton == null) {
            chooseResourceButton = new JButton("Set Catalog Definition..");
            chooseResourceButton.addActionListener(new ActionListener() {               
                public void actionPerformed(ActionEvent e) {
                   catalogueResource = (TabularDatabaseInformation)regChooser.chooseResourceWithFilter("Select Catalogue description for " + toolModel.getInfo().getName(),
                           "(@xsi:type like 'TabularDB')");
                   if( catalogueResource != null ) {
                       chooseResourceButton.setEnabled( false ) ;
                   }
                }
            });
        }
        return chooseResourceButton;
    }
    
    private JMenu getInsertColumnMenu( String name, Popup.CommandBean command, int typeIndex ) {
        JMenu columnMenu = new JMenu( name ) ;
        java.util.Set set = fromTables.entrySet() ;
        Iterator it = set.iterator() ;
        TableData tableData = null ;
        TableBean table = null ;
        JMenu dbMenu = new JMenu( "", true ) ;
        columnMenu.add( dbMenu );
        //
        // At present we are only dealing with one database,
        // so the database menu part is something of a cheat.
        // Also, the tables and columns will eventually need sorting!
        while( it.hasNext() ) {
            tableData = (TableData)((java.util.Map.Entry)it.next()).getValue() ;
            table = tableData.database.getTables()[ tableData.tableIndex ] ;
            JMenu tableMenu = new JMenu( table.getName(),true ) ;
            dbMenu.add( tableMenu );
            tableMenu.setToolTipText( table.getDescription() ) ;          
            tableMenu.addSeparator();
            final ColumnBean[] cols = table.getColumns();
            for (int k = 0; k < cols.length; k++) {
                tableMenu.add( new InsertColumnAction( cols[k].getName(), command, typeIndex, tableData, cols[k] ) ) ;
            }      
        }
        dbMenu.setText( tableData.database.getName() ) ;
        dbMenu.setToolTipText( tableData.database.getDescription() );     
        return columnMenu;
    }
    
    
    private JMenu getInsertTableMenu( String name, Popup.CommandBean command, int typeIndex ) {
        JMenu insertMenu = new JMenu( name ) ;
        DatabaseBean[] dbs = catalogueResource.getDatabases();
        for( int i = 0; i < dbs.length; i++ ) {
            JMenu dbMenu = new JMenu( dbs[i].getName(), true );
            dbMenu.setToolTipText( dbs[i].getDescription() );
            insertMenu.add( dbMenu );
            final TableBean[] tables = dbs[i].getTables();
            for (int j = 0; j < tables.length; j++) {
                InsertAction 
                	action = new InsertTableAction( tables[j].getName()
                                                  , command
                                                  , typeIndex
                                                  , dbs[i]
                                                  , j ) ;
                JMenuItem menuItem = dbMenu.add( action ) ;
                menuItem.setToolTipText( tables[j].getDescription() );
                // Grey out those already chosen...
                if( fromTables.containsKey( tables[j].getName() ) )
                    menuItem.setEnabled( false ) ;
            }
        }
        return insertMenu;
    }
    
    
    private void setAdqlParameter() {
        AdqlEntry rootEntry = ((AdqlEntry)adqlTree.getModel().getRoot()) ;
        XmlObject xmlRoot =  (XmlObject)rootEntry.getUserObject() ;
        // NOte. I'm not sure the following is adequate.
        XmlOptions options = new XmlOptions() ;
        options.setSaveOuter() ;
        String xmlText = xmlRoot.xmlText( options ) ;
        queryParam.setValue( xmlText ) ;
        toolModel.fireParameterChanged( ADQLToolEditorPanel.this, queryParam ) ;                    
    }
    
    protected JButton getValidateButton() {
        if (validateButton == null) {
            validateButton = new JButton("Validate ADQL");
            validateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    
                    AdqlEntry rootEntry = ((AdqlEntry)adqlTree.getModel().getRoot()) ;
                    XmlObject xmlRoot =  (XmlObject)rootEntry.getUserObject() ;
                    // NOte. I'm not sure the following is adequate.
                    XmlOptions options = new XmlOptions() ;
                    options.setSaveOuter() ;
                    String adqlx = xmlRoot.xmlText( options ) ;
                    
                    String adqls = transformer.transformToAdqls( adqlx ) ;

                    // validate sql string by trying to translate to xml - hopefully this will throw on error
                    try {
                        validator.s2x(adqls); // don't care about result.
                    	parent.setStatusMessage("<html><font color='green'>Sql is valid</font></html>");
                    } catch( Throwable sx ) {
                        Throwable th = sx.getCause() ;
                        String message = "No message available" ;
                        if( th != null ) {
                            message = th.getMessage() ;
                        }
                        parent.setStatusMessage("<html><font color='red'>INVALID - " + message + "</font></html>");
                    }
                }
            });
        }
        return validateButton;
    }
    
    private class TableData {
        public DatabaseBean database ;
        public int tableIndex ;
        public String alias ;
        
        public TableData( DatabaseBean database, int tableIndex, String alias ) {
            this.database = database ;
            this.tableIndex = tableIndex ;
            this.alias = alias ;
        }      
    }
    
    private class AliasStack {
        
        private Stack autoStack ;
        private int suffix = 0 ;
        
        public AliasStack() {
            autoStack = new Stack() ;
            for( int i=ALIAS_NAMES.length-1; i>-1; i-- ) {
                autoStack.push( Character.toString( ALIAS_NAMES[i] ) ) ;
            }
        }
        
        public String pop() {
            if( autoStack.empty() ) {
                suffix++ ;
                for( int i=ALIAS_NAMES.length-1; i>-1; i-- ) {
                    autoStack.push( Character.toString( ALIAS_NAMES[i] ) + Integer.toString( suffix ) ) ;
                }
            }
            return (String)autoStack.pop() ;
        }

    } // end of class AliasStack
    
} // end of ADQLToolEditor

