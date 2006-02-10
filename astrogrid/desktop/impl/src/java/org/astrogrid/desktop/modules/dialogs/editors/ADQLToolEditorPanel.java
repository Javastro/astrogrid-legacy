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

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Dimension;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import javax.swing.event.EventListenerList ;
import javax.swing.AbstractAction;
import javax.swing.Action ;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent; 
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaStringEnumEntry;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDecimal;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlLong;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlPositiveInteger;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.XmlUnsignedLong;
import org.apache.xmlbeans.XmlUnsignedShort;
import org.apache.xmlbeans.XmlCursor.TokenType ;
import org.apache.xmlbeans.XmlError ;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.TabularDatabaseInformation;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.dialogs.RegistryChooser;
import org.astrogrid.acr.ivoa.Adql074;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.adqlEditor.AdqlCommand;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlEntry;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.TableMetadataPanel ;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree.EditPromptAction;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.desktop.modules.dialogs.ResourceChooserInternal;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditEvent;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolEditListener;
import org.astrogrid.desktop.modules.dialogs.editors.model.ToolModel;
import org.astrogrid.desktop.modules.system.transformers.AdqlTransformer;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.workflow.beans.v1.Tool;
import org.w3c.dom.Document;
import org.apache.axis.utils.XMLUtils;

/**
 * @author jl99
 *
 */
public class ADQLToolEditorPanel extends AbstractToolEditorPanel implements ToolEditListener, TreeModelListener {
    

    private static final Log log = LogFactory.getLog( ADQLToolEditorPanel.class ) ;
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    private static final char[] ALIAS_NAMES = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String PI_QB_REGISTRY_RESOURCES = "qb-registry-resources" ;
    
    private ParameterValue queryParam = null ;
    
    protected final MyspaceInternal myspace ;  
    protected final UIComponent parent;
    protected final RegistryChooser regChooser;
    protected final Adql074 validator;
    protected final ResourceChooserInternal resourceChooser;
    protected final Registry registry ;
    
    // LHS Editor tabs...
    private JTabbedPane tabbedEditorPane ;
    // This is the tree tab...
    private AdqlTree adqlTree ;
    private AdqlTreeView adqlTreeView ;
    private ControllerImpl controller ;
    
    // This is the text tab...
    // private JTextPane adqlText ;
    private AdqlStringView adqlStringView ;
    
    
    // This is the adql/x tab...
    //private JTextPane adqlXml ;
    private AdqlXmlView adqlXmlView ;
    
    //RHS Metadata tabs...
    private JTabbedPane tabbedMetadataPane ;
    
    // Bottom View Tabs...
    private JTabbedPane tabbedBottomPane;
    // This is the diagnostics tab...
    private JTextPane diagnostics ;
    
    //private static XmlObject root ;
    
    private JButton chooseResourceButton ;
    private JButton validateButton ;
    // private JMenu columnMenu;
    // private Hashtable tableMenus = new Hashtable() ; 
    // private String nameSpace = AdqlData.NAMESPACE_1_0 ; 
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
                              , MyspaceInternal myspace
                              , Registry registry ) {
        super( toolModel );
        this.validator = adql;
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
        catalogueResource = null ;
        if( fromTables != null )
            fromTables.clear() ;
        aliasStack = new AliasStack() ;
        setEnabled(false);
        this.removeAll() ;
    }
    
    public void toolSet(ToolEditEvent te) {
        if( TRACE_ENABLED ) log.debug( "toolCleared(ToolEditEvent te)"  ) ;
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
        String query = null ;
        this.adqlTree = null ;
        aliasStack = new AliasStack() ;
        if( queryParam == null ) {
            if( this.adqlTree == null ) {
                this.adqlTree = new AdqlTree() ;
            }
        }
        //
        // Not quite sure of the reason for the hasIndirect() method call.
        // However, I will test for the presence of the indirection flag AND
        // whether it is set to true....
        else if( queryParam.hasIndirect() == true  && queryParam.getIndirect() == true ) {
            log.debug( "Query is a remote reference." ) ;
            query = readQuery() ;
            if( query == null || query.length() < 5 ) {
                this.adqlTree = new AdqlTree() ;
            }
            else if( query.startsWith( "<" ) ) {
                query = adaptToVersion( query ) ;
                try {          
                    this.adqlTree = new AdqlTree( query ) ;
                }
                catch ( Exception ex ) {
                    this.adqlTree = new AdqlTree() ;
                }
            }  
            log.debug( "...setting indirect to false" ) ;
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
           log.debug( "Query is inline..." ) ;
           query = queryParam.getValue() ;
           if( query == null || query.length() < 5 ) {
               this.adqlTree = new AdqlTree() ;
           }
           else if( query.startsWith( "<" ) ) {
               // Assume this is an instream adql/x query...
               query = adaptToVersion( query ) ;
               try {          
                   this.adqlTree = new AdqlTree( query ) ;
               }
               catch ( Exception ex ) {
                   this.adqlTree = new AdqlTree() ;
               }
           }
           else {
               // Assume this is an instream adql/s query...  
               
               // JL Note. I'm not supporting this for the moment
               // because there is no easy way to see how the metadata
               // can be recovered from the adql/s in a simple way
               // (maybe some specialized comment?)
               // So for the moment...
               this.adqlTree = new AdqlTree() ;
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
        this.resetCatalogueData() ;
        this.setAdqlParameter() ;
        this.adqlTree.getModel().addTreeModelListener( this );
//        this.setXmlAndStringContent() ;
        return this.adqlTree ;
    }
    
    
    private String adaptToVersion( String query ) {
        String returnQuery = query ;
        StringBuffer buffer = new StringBuffer( returnQuery ) ;
        int index = buffer.indexOf( AdqlData.NAMESPACE_0_74 ) ;
        if( index != -1 ) {
            log.debug( "Namespace replaced" ) ;
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
            log.debug( "Failed to read adql file" ) ;
        }
        finally {
            try{ bis.close(); } catch( Exception ex ) {;}
        }
        return retQuery ;
    }
    
    
    
    private void resetCatalogueData() {
        String piName = null ;
        String piValue = null ;
        this.catalogueResource = null ;
        XmlCursor cursor = getRoot().newCursor() ;
        log.debug( "Searching for PI's..." ) ;
        while( !cursor.toNextToken().isNone() ) {
            if( cursor.isProcinst() ) {
                piName = cursor.getName().getLocalPart() ;
                log.debug( "PI name: " + cursor.getName() ) ;
            	log.debug( "PI text: " + cursor.getTextValue() ) ;
            	if( piName.equals ( PI_QB_REGISTRY_RESOURCES ) )  {
            	    piValue = cursor.getTextValue().trim() ;
            	    if( piValue.equals( "none" ) )
            	        break ;
            	    String sql = "Select * from Registry where vr:identifier = '" 
            	               + formatCatalogueId( piValue )
            	               + "'" ; 
                    queryRegistry( sql ) ;
                    log.debug( "catalogueResource: " + (catalogueResource==null?"null":"not null") ) ;
                    break ;
            	}
            }
        } // end while
        cursor.dispose();
    }
    
    private String formatCatalogueId ( String piValue ) {
        // This value follows a standard set up in the portal prior to
	    // the establishment of ivorns and overcomes some of the problems
	    // of special characters within table names. 
	    // Note: The portal QB allows only one table (no joins) so the metadata
	    // reflects the one table. But we need the whole catalogue/database.
        String ivornString = "ivo://" + piValue.substring( 0, piValue.lastIndexOf( '!' ) ).replace( '!', '/' ) ;
        log.debug( "catalogue ivorn: " + ivornString ) ;
        return ivornString ;
    }
    
    private void queryRegistry( String searchString ) {
        try {
            catalogueResource = (TabularDatabaseInformation)registry.adqlSearchRI( searchString )[0] ;
        }
        catch ( Exception ex )  {
            log.error( "Failed to find catalogue entry using: \n" + searchString ) ;
        }
    }
    
    
    private void init() {
        setLayout( new GridBagLayout() ) ;       
        GridBagConstraints gbc ;
        JSplitPane splAdqlToolEditor = new JSplitPane( JSplitPane.VERTICAL_SPLIT );

        // Put the editing panels plus the metadata panels in the top half of the split pane.
        splAdqlToolEditor.setTopComponent( initTopView() ) ;

        // Put the diagnostics panel at the bottom of the split pane.
        splAdqlToolEditor.setBottomComponent( initBottomView() ) ;

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
    
    
    private JSplitPane initTopView() {
        JSplitPane topView = new JSplitPane();
        
        // Put the editor panels in the left side of the split pane.
        topView.setLeftComponent( initLeftHandView() ) ;

        // Put the metadata/details panels in the right side of the split pane.
        topView.setRightComponent( initRightHandView() ) ;

        // Set the rest of the split pane's properties,
        topView.setDividerLocation(300);
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
//        setXmlAndStringContent() ;
        
        adqlTree.addTreeSelectionListener( new TreeSelectionListener() {
            public void valueChanged( TreeSelectionEvent e ) {
//                setXmlAndStringContent() ;      
            }
        }) ;
        
        return tabbedEditorPane ;    
    }
    
//    private void setXmlAndStringContent() {
//        if( adqlXml == null )
//            return ;
//        AdqlEntry rootEntry = (AdqlEntry) adqlTree.getModel().getRoot();
//        XmlObject node = rootEntry.getXmlObject();
//        XmlCursor nodeCursor = node.newCursor();
//        XmlOptions options = new XmlOptions();
//        options.setSavePrettyPrint();
//        options.setSavePrettyPrintIndent(4);
//        String xmlString = nodeCursor.xmlText(options);
//        nodeCursor.dispose() ;
//        String adqlString = transformer.transformToAdqls( xmlString, " " ) ;
//        // adqlStringView.setText( adqlString ) ;
//        adqlXml.setText( xmlString ) ;
//    }
    
    
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
//        JScrollPane scrollCatalog = new JScrollPane();
//        JTextPane catalogTextPane = new JTextPane();
//        scrollCatalog.setViewportView( catalogTextPane ) ;
//        catalog1.setLayout( new GridBagLayout() ) ;
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.anchor = GridBagConstraints.NORTHWEST;
//        gbc.weightx = 1;
//        gbc.weighty = 1;
//        catalog1.add( scrollCatalog, gbc )  ;
        tabbedMetadataPane.addTab( "Catalog", catalog1 ) ;
        
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
        if( catalogueResource != null ) {
            chooseResourceButton.setEnabled( false ) ;
            formatCatalogTab() ;
            reestablishTablesCollection() ;
        }
        else {
            chooseResourceButton.setEnabled( true ) ;
        }
              
        return rhsPanel ;
    }
    
    private void reestablishTablesCollection() {
        aliasStack = new AliasStack() ;
        XmlString xTableName = null ;
        XmlString xAlias = null ;
        String alias = null ;
        String greatestAlias = null ;
        DatabaseBean db = catalogueResource.getDatabases()[0] ;
        //
        // Loop through the whole of the query looking for table types....
        // (JL: This is somewhat weak. I think there may be complications
        //      with join tables )
        XmlCursor cursor = getRoot().newCursor() ;
        while( !cursor.toNextToken().isNone() ) {
            if( cursor.isStart()  
                && 
                ( cursor.getObject().schemaType().getName().getLocalPart().equals( AdqlData.TABLE_TYPE ) 
                  ||
                  cursor.getObject().schemaType().getName().getLocalPart().equals( AdqlData.ARCHIVE_TABLE_TYPE ) )                       
            ) {
                xTableName = (XmlString)AdqlUtils.get( cursor.getObject(), "name" ) ;
                xAlias = (XmlString)AdqlUtils.get( cursor.getObject(), "alias" ) ;
                alias = (xAlias == null ? null : xAlias.getStringValue())  ; 
                log.debug( "table name: " 
                         + xTableName.getStringValue() 
                         + " with alias: "
                         + (xAlias==null ? "null" : xAlias.getStringValue()) ) ;  
                fromTables.put( xTableName.getStringValue()
                              , new TableData( db, findTableIndex( db, xTableName.getStringValue()), alias ) ) ;
                if( alias != null && (greatestAlias == null || greatestAlias.compareTo(alias) < 0) ) {
                        greatestAlias = alias ;
                }
            }
        } // end while
        cursor.dispose();
        // Reset the alias stack to the "largest" previously used value... 
        if( greatestAlias != null ) {
            // This is potentially dangerous. So until I have time to work out a better way,
            // I'm assuming no more then 100 tables will ever be tried...
            for( int i=0; i<100; i++ ) {
                if( aliasStack.pop().equals( greatestAlias ) ) 
                    break ;
            }    
        }
    }
    
    private int findTableIndex( DatabaseBean db, String tableName ) {
        TableBean[] tables = db.getTables() ;
        for( int i=0; i<tables.length; i++ ) {
            if( tables[i].getName().equals( tableName ) ) {
                return i ;
            }
        }
        return -1;
    }
        
    private static XmlObject setDefaultValue( XmlObject xmlObject ) {
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
    
    private static XmlObject setBuiltInDefaults( XmlObject xmlObject ) {
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
    
    private static boolean isAttributeDriven( SchemaType type ) {
        String name = (String)AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
        return ( name != null && name.length() > 0 ) ;
    }
    
    private static XmlObject setAttributeDrivenDefaults( XmlObject xmlObject ) {
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
    
    private static XmlObject setDerivedSimpleDefaults( XmlObject xmlObject ) {
        XmlObject retVal = xmlObject ;
        SchemaType type = xmlObject.schemaType() ;
        // Cooerce the empty element into an XmlString...
        XmlString tempObject = (XmlString)xmlObject.changeType( XmlString.type ) ;
        tempObject.setStringValue( (String)AdqlData.DERIVED_DEFAULTS.get( type.getName().getLocalPart() ) ) ;
        // Cooerce back to original type...
        retVal = tempObject.changeType( type ) ;
        return retVal ;
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
        if( e != null /* && e.getSource() != ADQLToolEditorPanel.this */ ) {
            this.setAdqlParameter() ;
//            this.setXmlAndStringContent() ;
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
//        this.setXmlAndStringContent() ;
        this.validateAdql() ;
    }
    
    private void validateAdql() {
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
                if( xmlObj == null ) {
                    buffer.append( "no location given." ) ;
                 }
                else {
                    buffer.append( xmlObj.schemaType().getName().getLocalPart() ) ;
                }
                buffer.append( "\n" ) ;
            }
            this.diagnostics.setText( buffer.toString() ) ;
        }
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
            this.root = root ;
            fireStateChanged( source ) ;
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
//	        JPopupMenu popup = (JPopupMenu)popupMenus.get( displayName ) ;
//	        if( true ) {
////	        if( popup == null ) {
//	            popup = buildPopup( entry, displayName ) ;
//	        }
	        return buildPopup( entry ) ;
	    }
	    
	    
	    // 
	    // This is far too large! Refactor.
	    private JPopupMenu buildPopup( AdqlEntry entry ) {
	        JPopupMenu popup = new JPopupMenu( "AdqlTreeContextMenu" ) ;
	        // Place a name tag at the top with a separator.
	        // This is purely cosmetic. It does nothing.
	        // Can remove later if redundant / not liked.
	        popup.add( entry.getDisplayName() ) ;
	        popup.addSeparator() ;
	        if( entry.isBottomLeafEditable() ) {
//	            popup.add( new EditAction( entry ) ) ;
	        }
	        popup.add( new CutAction( entry ) ) ;
	        popup.add( new CopyAction( entry ) ) ;
	        popup.add( new PasteAction( entry ) ) ;
	        
	        ArrayList commands = AdqlCommand.buildCommands( entry ) ; 
	        SchemaType[] concreteTypes ;
	        if( commands.isEmpty() == false ) {
	            InsertAction insertAction ;
	            if( commands.size() == 1 ) {
	                AdqlCommand c = (AdqlCommand)commands.get(0) ;
	                concreteTypes = c.getConcreteTypes() ;
	                if( concreteTypes.length == 1 ) {
	                    if( c.isSupportedType( 0 ) ) {
		                    popup.addSeparator() ;
		                    if( c.isConcreteTypeCascadeable( 0 ) ) {
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
		                AdqlCommand c = (AdqlCommand)iterator.next() ;
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
	        return popup ;
	    }
	    
	    
	    private JMenu getCascadeableMenu( String name, AdqlCommand commandBean, int concreteTypeIndex ) {
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
	        else if( commandBean.isConcreteTypeDrivenByEnumeratedAttribute( concreteTypeIndex ) ) {
	            JMenuItem[] menuItems = getEnumeratedMenus( commandBean, concreteTypeIndex ) ;
	            for( int i=0; i<menuItems.length; i++ ) {
	                menu.add( menuItems[i] ) ;
	            } 
	            menu.setEnabled( commandBean.isEnabled() ) ;
	        }        
	        return menu;
	    }
	    
	    
	    private JMenuItem[] getEnumeratedMenus( AdqlCommand commandBean, int typeIndex  ) {
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
	        clipBoard.add( entry.getXmlObject().copy() ) ;
	        AdqlEntry parent = (AdqlEntry)path.getParentPath().getLastPathComponent()  ;
	        AdqlEntry.removeInstance( parent, entry ) ;
	   
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
	        clipBoard.add( entry.getXmlObject() ) ;
	    }
	}
	
	
	private class PasteAction extends AbstractAction {
	    private AdqlEntry entry ;
	       
	    public PasteAction( AdqlEntry entry ) {
	        super( "Paste" ) ;
	        this.entry = entry ;
	    }
	    
	    
	    public void actionPerformed( ActionEvent e ) {
	        TreePath path = adqlTree.getSelectionPath() ;
	        // If the path is null or there is no parent
	        // Then we cannot paste into this entry...
	        if( path == null || path.getPathCount() < 2 )
	            return ;
	        // If the clipboard is empty then there is nothing to paste...
	        if( clipBoard.isEmpty() )
	            return ;
	        // If the target allows for no children, then we dont paste into it.
	        // (This is weak. We should allow pasting over bottom leaves)
	        SchemaProperty[] elements = entry.getElementProperties() ;
	        if( elements == null || elements.length == 0 ) 
	        	return ;        
	        // Check that the last clipboard object is suitable to paste into the target...
	        XmlObject pasteObject = (XmlObject)clipBoard.get( clipBoard.size() - 1 ) ;
	        if( AdqlCommand.isSuitablePasteTarget( entry, pasteObject ) == false )
	            return ;
	        
	        // Build array of possible insert commands for target...
	        ArrayList commands = AdqlCommand.buildCommands( entry ) ; 
	        
	        // Attempt to find a suitable match of types...
	        int result[] = AdqlCommand.findSuitableMatch( commands, pasteObject ) ;	        
	        if( result[0] == -1 )
	            return ;
	        
	        // Now do the business.
	        // Insert a basic object...
	        XmlObject newObject1 = insertObject( (AdqlCommand)commands.get(result[0]), result[1] ) ;
	        // Copy contents of paste object into it...
	        XmlObject newObject2 = newObject1.set( pasteObject ) ;
	        
	        // Show a pretty print (for my sanity)
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)entry.getRoot();
            XmlObject xmlRoot = (XmlObject)root.getUserObject() ;
            System.out.println( "===== pretty print of root... =====" ) ;
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            System.out.println( xmlRoot );	                

            // Create the new tree node instance (and pray it works)...
        	AdqlEntry.newInstance( entry, newObject2 ) ;  
        	
        	// Refresh tree...
	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( entry ) ;
	        adqlTree.repaint() ;
	        
//	        SchemaType pasteType = pasteObject.schemaType() ;
//	        SchemaType childType = null ;
//	        SchemaProperty[] elementProperties = this.entry.getElementProperties() ;
//	        for ( int i = 0 ; i < elementProperties.length ; i++ ) {
//	            childType = elementProperties[i].getType() ;
//	            if( childType.isAssignableFrom( pasteType ) ) { 
//	                CommandBean command = new CommandBean( entry, entry.getDisplayName(), elements[0] ) ;
//	                XmlObject newObject1 = AdqlUtils.addNew( entry.getXmlObject(), command.getElementName() ) ;
//
//	                XmlObject newObject2 = newObject1.set( pasteObject ) ;
//	          
//	                DefaultMutableTreeNode root = (DefaultMutableTreeNode)entry.getRoot();
//	                XmlObject xmlRoot = (XmlObject)root.getUserObject() ;
//	                System.out.println( "===== pretty print of root... =====" ) ;
//	                XmlOptions opts = new XmlOptions();
//	                opts.setSavePrettyPrint();
//	                opts.setSavePrettyPrintIndent(4);
//	                System.out.println( xmlRoot );	                
//
//	            	AdqlEntry.newInstance( entry, newObject2 ) ;
//	         
//	    	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( entry ) ;
//	    	        adqlTree.repaint() ;
//	    	        break ;
//	            }	                
//	        }
	    }
	  
	    
//	    private boolean isSuitablePasteTarget( XmlObject clipboardObject ) {
//	        boolean result = false ;
//	        SchemaProperty[] elementProperties = this.entry.getElementProperties() ;
//	        SchemaType clipboardType = clipboardObject.schemaType() ;
//	        SchemaType childType = null ;
//	        for ( int i = 0 ; i < elementProperties.length ; i++ ) {
//	            childType = elementProperties[i].getType() ;
//	            if( childType.isAssignableFrom( clipboardType ) ) { 
//	                result = true ;
//	                break ;
//	            }
//	        }
//	        return result ;
//	    }
//	    
//	    private int[] findSuitableMatch( ArrayList commands, XmlObject pasteObject ) {
//	        int[] match = new int[] { -1, -1 } ;
//	        SchemaType pasteType = pasteObject.schemaType() ;
//	        SchemaType[] targetTypes = null ;
//	        ListIterator iterator = commands.listIterator() ;
//            checkCommands: while( iterator.hasNext() ) {
//                AdqlCommand c = (AdqlCommand)iterator.next() ;
//                if( c.isEnabled() == false )
//                    continue ;
//                targetTypes = c.getConcreteTypes() ;
//                for( int i=0; i<targetTypes.length; i++ ) {
//                    if( targetTypes[i].getName().equals( pasteType.getName() ) ) {
//                        match[0] = iterator.nextIndex()-1 ;
//                        match[1] = i ;
//                        break checkCommands ;
//                    }
//                }
//            } // end while
//	        return match ;
//	    }
	    
	    private XmlObject insertObject( AdqlCommand commandBean, int subType ) {
            AdqlEntry parent = commandBean.getEntry() ;
            XmlObject parentObject = parent.getXmlObject() ;
            SchemaType schemaType = commandBean.getConcreteTypes()[subType] ;
            XmlObject newObject = null ; 
            if( commandBean.isArray() ) {                
                newObject = AdqlUtils.addNewToEndOfArray( parentObject, commandBean.getElementName() ) ;
                newObject = newObject.changeType( schemaType ) ;
            }
            else {
                if( schemaType.isBuiltinType() ) {
                    newObject = XmlObject.Factory.newInstance().changeType( schemaType ) ;
                    newObject = setDefaultValue( newObject ) ;
                }
                else {            
                    newObject = AdqlUtils.addNew( parentObject, commandBean.getElementName() ) ;
                    if( newObject != null ) {
                        newObject = newObject.changeType( schemaType ) ;
                    }
                    else {
                        newObject = XmlObject.Factory.newInstance().changeType( commandBean.getElement().javaBasedOnType() ) ;
                        newObject = newObject.changeType( schemaType ) ;
                        newObject = setDefaultValue( newObject ) ;
                        AdqlUtils.set( parentObject, commandBean.getElementName(), newObject ) ; 
                    }
                }
            } 
            return newObject ;
        }
	    
//	    public void _actionPerformed2( ActionEvent e ) {
//	        TreePath path = adqlTree.getSelectionPath() ;
//	        // If the path is null or there is no parent
//	        // Then we cannot paste into this entry...
//	        if( path == null || path.getPathCount() < 2 )
//	            return ;
//	        // If the clipboard is empty then there is nothing to paste...
//	        if( clipBoard.isEmpty() )
//	            return ;
//	        AdqlEntry pasteEntry = (AdqlEntry)clipBoard.get( clipBoard.size() - 1 ) ;
//	        SchemaType pasteType = pasteEntry.getSchemaType() ;
//	        SchemaType childType = null ;
//	        SchemaProperty[] elementProperties = this.entry.getElementProperties() ;
//	        for ( int i = 0 ; i < elementProperties.length ; i++ ) {
//	            childType = elementProperties[i].getType() ;
//	            if( childType.isAssignableFrom( pasteType ) ) { 
//	                XmlObject pasteObj = pasteEntry.getXmlObject() ;
//	                XmlObject parentObj = entry.getXmlObject() ;
//	                XmlCursor parentCursor = entry.getXmlObject().newCursor() ;
//	                
//	                //parentCursor.toParent() ; // Move backwards to the parent's parent!
//	                // Now search forward until we have the correct element in sight...
//	                //System.out.println( "===== searching forward... =====" ) ;
////	                while ( !parentCursor.toNextToken().isEnd() )
////	                {
//////	                    if( parentCursor.getObject() == parentObj ) {
//////	                        // System.out.println( "parentCursor.currentTokenType(): " + parentCursor.currentTokenType() );
//////	                        System.out.println( "parent object found." );
//////	                        parentCursor.toNextToken() ;
//////	                        break ;
//////	                    }
////	                    break ;                    
////	                }
//	                
//	               
//	                
//	                // boolean bParentFirstChild = parentCursor.toFirstChild() ; // There has to be a first child!
//	                XmlCursor childCursor = pasteObj.newCursor() ;
//	                parentCursor.beginElement( pasteObj.schemaType().getName() ) ;
//	                // boolean bPasteFirstChild = childCursor.toFirstChild() ;
//	                // boolean copied = childCursor.copyXml( parentCursor ) ;
//	                // System.out.println( "copied: " + copied ) ;
//	                DefaultMutableTreeNode root = (DefaultMutableTreeNode)entry.getRoot();
//	                XmlObject xmlRoot = (XmlObject)root.getUserObject() ;
//	                System.out.println( "===== pretty print of root... =====" ) ;
//	                XmlOptions opts = new XmlOptions();
//	                opts.setSavePrettyPrint();
//	                opts.setSavePrettyPrintIndent(4);
//	                System.out.println( xmlRoot );	                
//
//	            
//	                // parentCursor.toNextSibling() ;
//	            	// AdqlEntry.newInstance( entry, parentCursor.getObject() ) ;
//	                parentCursor.dispose() ;
//	                childCursor.dispose() ;
//	    	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( entry ) ;
//	    	        adqlTree.repaint() ;
//	    	        break ;
//	            }	                
//	        }
//	    } 
	    
//	    public void _actionPerformed( ActionEvent e ) {
//	        TreePath path = adqlTree.getSelectionPath() ;
//	        // If the path is null or there is no parent
//	        // Then we cannot paste into this entry...
//	        if( path == null || path.getPathCount() < 2 )
//	            return ;
//	        // If the clipboard is empty then there is nothing to paste...
//	        if( clipBoard.isEmpty() )
//	            return ;
//	        AdqlEntry pasteEntry = (AdqlEntry)clipBoard.get( clipBoard.size() - 1 ) ;
//	        SchemaType pasteType = pasteEntry.getSchemaType() ;
//	        SchemaType childType = null ;
//	        SchemaProperty[] elementProperties = this.entry.getElementProperties() ;
//	        for ( int i = 0 ; i < elementProperties.length ; i++ ) {
//	            childType = elementProperties[i].getType() ;
//	            if( childType.isAssignableFrom( pasteType ) ) { 
//	                XmlObject xObj = pasteEntry.getXmlObject().copy() ;
//	            	AdqlEntry.newInstance( entry, xObj ) ;
//	    	        ((DefaultTreeModel)adqlTree.getModel()).nodeStructureChanged( entry ) ;
//	    	        adqlTree.repaint() ;
//	    	        break ;
//	            }	                
//	        }
//	    }
	    
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
	    
	    protected AdqlCommand commandBean ;
	    protected int indexOfConcreteSubtype ;
	    protected AdqlEntry newEntry ;
	    protected XmlObject newValueObject ;
	    
	    public InsertAction( String name, AdqlCommand commandBean ) {
	        super( name ) ;
	        this.commandBean = commandBean ;
	        indexOfConcreteSubtype = -1 ;   
	        if( name.length() == 0 )
	           log.debug( "Empty name for " + commandBean.getElementName() ) ;
	    }
	    
	    public InsertAction( String name, AdqlCommand commandBean, int indexOfConcreteSubtype ) {
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
            

//            setAdqlParameter() ;
 
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
                            , AdqlCommand commandBean
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
                             , AdqlCommand commandBean
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
        
        // But for the moment, we can test for the presence of alias...
        if( tableData.alias != null ) {
            AdqlUtils.set( newEntry.getXmlObject()
                         , "table"
                         , XmlString.Factory.newValue( tableData.alias ) ) ;
        }
        else {
            AdqlUtils.set( newEntry.getXmlObject()
                         , "table"
                         , XmlString.Factory.newValue( tableData.database.getTables()[ tableData.tableIndex ].getName() ) ) ;
        }
 
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
                                 , AdqlCommand commandBean
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
                   Object obj = regChooser.chooseResourceWithFilter( "Select Catalogue description for " 
                                                                   + toolModel.getInfo().getName()
                                                                   , "(@xsi:type like 'TabularDB')") ;
                   if( obj != null )
                       log.debug( "regChooser.chooseResourceWithFilter() returned object of type: " + obj.getClass().getName() ) ; {
                       catalogueResource = (TabularDatabaseInformation)obj ;
                   }        
                   if( catalogueResource != null ) {
                       chooseResourceButton.setEnabled( false ) ;
                       formatCatalogTab() ;
                       writeResourceProcessingInstruction() ;
                   }
                }
            });
        }
        return chooseResourceButton;
    }
    
    private void formatCatalogTab() {
        String title = null ;
        String description = null ;
        if( tabbedMetadataPane.getTabCount() == 1 ) {
            DatabaseBean[] dbs = catalogueResource.getDatabases();           
            if( dbs != null && dbs[0] != null ) {
                title = dbs[0].getName() ; 
                if( title == null || title.trim().length() == 0 ); {
                    title = catalogueResource.getName() ;
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
                description = dbs[0].getDescription() ;
                if( description == null || description.trim().length() == 0 ) {
                    description = catalogueResource.getDescription() ;
                }                   
                if( description == null || description.trim().length() == 0 ) {
                    description = "No description available" ;
                }
                catalogDetailsTextPane.setText( description ) ;
                tabbedCatalogPane.addTab( "Overview", scrollCatalogDetails ) ;

                final TableBean[] tables = dbs[0].getTables();
                for( int j=0; j<tables.length; j++) {
                    tabbedCatalogPane.addTab( tables[j].getName(), new TableMetadataPanel(  ADQLToolEditorPanel.this, adqlTree, tables[j]) ) ;
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
    
    
    private void writeResourceProcessingInstruction() {
        String piName = null ;
        String piValue = null ;
        XmlCursor cursor = getRoot().newCursor() ;
        log.debug( "Searching for PI's..." ) ;
        while( !cursor.toNextToken().isNone() ) {
            if( cursor.isProcinst() ) {
                piName = cursor.getName().getLocalPart() ;
                log.debug( "PI name: " + cursor.getName() ) ;
            	log.debug( "PI text: " + cursor.getTextValue() ) ;
            	if( piName.equals ( PI_QB_REGISTRY_RESOURCES ) )  {
            	   // OK. There's already one here.
            	   // But does it say "none"?...
            	    if( cursor.getTextValue().trim().equals( "none") ) {
            	        piValue = catalogueResource.getId().getSchemeSpecificPart().substring( 2 ) 
                        + '!' 
                        + catalogueResource.getDatabases()[0].getTables()[0].getName() ;
            	        cursor.setTextValue( piValue ) ;
            	    }
            	   break ;
            	}
            }
            else if( cursor.isStart()
                     &&
                     cursor.getObject().schemaType().getName().getLocalPart().equals( AdqlData.SELECT_TYPE ) ) {
                // We need to create the required PI...
                // (I've simply chosen the first table to align it with the portal)
                piValue = catalogueResource.getId().getSchemeSpecificPart().substring( 2 ) 
                        + '!' 
                        + catalogueResource.getDatabases()[0].getTables()[0].getName() ;
                log.debug( "new PI Text: " +piValue ) ;
                cursor.insertProcInst( PI_QB_REGISTRY_RESOURCES, piValue ) ;
                break ;               
            }
        } // end while
        cursor.dispose();
    }
    
    
    
    private JMenu getInsertColumnMenu( String name, AdqlCommand command, int typeIndex ) {
        JMenu columnMenu = new JMenu( name ) ;
        java.util.Set set = fromTables.entrySet() ;
        Iterator it = set.iterator() ;
        TableData tableData = null ;
        TableBean table = null ;
//        JMenu dbMenu = new JMenu( "", true ) ;
//        columnMenu.add( dbMenu );
        //
        // At present we are only dealing with one database,
        // so the database menu part is something of a cheat.
        // Also, the tables and columns will eventually need sorting!
        while( it.hasNext() ) {
            tableData = (TableData)((java.util.Map.Entry)it.next()).getValue() ;
            table = tableData.database.getTables()[ tableData.tableIndex ] ;
            JMenu tableMenu = new JMenu( table.getName(),true ) ;
//            dbMenu.add( tableMenu );
            columnMenu.add( tableMenu );
            tableMenu.setToolTipText( table.getDescription() ) ;          
            tableMenu.addSeparator();
            final ColumnBean[] cols = table.getColumns();
            for (int k = 0; k < cols.length; k++) {
                tableMenu.add( new InsertColumnAction( cols[k].getName(), command, typeIndex, tableData, cols[k] ) ) ;
            }      
        }
//        dbMenu.setText( tableData.database.getName() ) ;
//        dbMenu.setToolTipText( tableData.database.getDescription() );     
        return columnMenu;
    }
    
    
    private JMenu getInsertTableMenu( String name, AdqlCommand command, int typeIndex ) {
        JMenu insertMenu = new JMenu( name ) ;
        DatabaseBean[] dbs = catalogueResource.getDatabases();
        for( int i = 0; i < dbs.length; i++ ) {
//            JMenu dbMenu = new JMenu( dbs[i].getName(), true );
//            dbMenu.setToolTipText( dbs[i].getDescription() );
//            insertMenu.add( dbMenu );
            final TableBean[] tables = dbs[i].getTables();
            for (int j = 0; j < tables.length; j++) {
                InsertAction 
                	action = new InsertTableAction( tables[j].getName()
                                                  , command
                                                  , typeIndex
                                                  , dbs[i]
                                                  , j ) ;
//                JMenuItem menuItem = dbMenu.add( action ) ;
                JMenuItem menuItem = insertMenu.add( action ) ;
//                menuItem.setToolTipText( tables[j].getDescription() );
                // Grey out those already chosen...
                if( fromTables.containsKey( tables[j].getName() ) ) {
//                   menuItem.setEnabled( false ) ;
                }
            }
        }
        return insertMenu;
    }
    
    
    private void setAdqlParameter() {
//        // Only update the parameter value if the adql is instream.
//        // ( ie: If the adql parameter is a remote reference to a file
//        // containing the adql, then we simply return )...
//        if( queryParam.hasIndirect() == true )
//            return ;
        if( queryParam.hasIndirect() == true )
            queryParam.setIndirect( false ) ;
        AdqlEntry rootEntry = ((AdqlEntry)adqlTree.getModel().getRoot()) ;
        XmlObject xmlRoot =  (XmlObject)rootEntry.getUserObject() ;
        // NOte. I'm not sure the following is adequate.
        XmlOptions options = new XmlOptions() ;
        options.setSaveOuter() ;
        String xmlText = xmlRoot.xmlText( options ) ;
        queryParam.setValue( xmlText ) ;
        toolModel.fireParameterChanged( ADQLToolEditorPanel.this, queryParam ) ;                    
    }
    
//    protected JButton getValidateButton() {
//        if (validateButton == null) {
//            validateButton = new JButton("Validate ADQL");
//            validateButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    
//                    AdqlEntry rootEntry = ((AdqlEntry)adqlTree.getModel().getRoot()) ;
//                    XmlObject xmlRoot =  (XmlObject)rootEntry.getUserObject() ;
//                    // NOte. I'm not sure the following is adequate.
//                    XmlOptions options = new XmlOptions() ;
//                    options.setSaveOuter() ;
//                    String adqlx = xmlRoot.xmlText( options ) ;
//                    
//                    String adqls = transformer.transformToAdqls( adqlx ) ;
//
//                    // validate sql string by trying to translate to xml - hopefully this will throw on error
//                    try {
//                        validator.s2x(adqls); // don't care about result.
//                    	parent.setStatusMessage("<html><font color='green'>Sql is valid</font></html>");
//                    } catch( Throwable sx ) {
//                        Throwable th = sx.getCause() ;
//                        String message = "No message available" ;
//                        if( th != null ) {
//                            message = th.getMessage() ;
//                        }
//                        parent.setStatusMessage("<html><font color='red'>INVALID - " + message + "</font></html>");
//                    }
//                }
//            });
//        }
//        return validateButton;
//    }
    
    
    private class AdqlXmlView extends JPanel implements ChangeListener {
        
        JTabbedPane owner ;
        JTextPane xmlTextPane ;
        Controller controller ; 
        
        public AdqlXmlView( JTabbedPane owner, Controller controller ) {
            super() ;
            this.owner = owner ;
            this.controller = controller ;
            this.controller.addChangeListener( this ) ;
            JScrollPane xmlScrollContent = new JScrollPane();
            xmlTextPane = new JTextPane();
            xmlTextPane.setEditable( false ) ;
            xmlScrollContent.setViewportView( xmlTextPane  );
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.weightx = 1;
            gbc.weighty = 1;
            this.add( xmlScrollContent, gbc ) ;
            this.owner.addTab( "Adql/x", this ) ;
            
            //
            // Set up processing for when this tab is selected or de-selected...
            // (This is complex so see other comments.)
            initSelectedProcessing() ;
        }
        
        private void initSelectedProcessing() {
            this.owner.addChangeListener( new ChangeListener() {
                public void stateChanged( ChangeEvent e ) {
                    Object selectedComponent = ((JTabbedPane)e.getSource()).getSelectedComponent() ;
                    if( selectedComponent == ADQLToolEditorPanel.AdqlXmlView.this ) {
                        selectionGained() ;
                    }
                    else {
                        selectionLost() ;
                    }
                }
            }) ;
        }
        
        private void selectionGained() {
            XmlObject node = this.controller.getRootInstance() ;
            XmlCursor nodeCursor = node.newCursor();
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            options.setSavePrettyPrintIndent(4);
            String xmlString = nodeCursor.xmlText(options);
            nodeCursor.dispose() ;
            this.xmlTextPane.setText( xmlString ) ;
        }
        
        private void selectionLost() {
            ;
        }
        
        public void stateChanged(ChangeEvent e) {
            if( e != null && e.getSource() != this ) {
                ; 
            }

        }
    }

    private class AdqlTreeView extends JPanel implements ChangeListener {
        
        JTabbedPane owner ;
        Controller controller ;
        
        public AdqlTreeView( JTabbedPane owner, Controller controller ) {
            super() ;
            this.owner = owner ;
            this.controller = controller ;
            this.controller.addChangeListener( this ) ;
            JScrollPane scrTree = new JScrollPane();
            scrTree.setViewportView( setAdqlTree() ) ;
            this.controller.updateModel( this, ((AdqlEntry)adqlTree.getModel().getRoot()).getXmlObject() ) ;
            this.setLayout( new GridBagLayout() ) ;
            GridBagConstraints gbc = new GridBagConstraints() ;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.gridheight = 1 ;
            gbc.gridwidth = 1 ;
            gbc.gridx = 0 ;
            gbc.gridy = 0 ;   
            gbc.fill = GridBagConstraints.BOTH ;
            this.add(scrTree, gbc);
            this.owner.addTab( "Tree", this ) ; 
        }
            
        public void stateChanged( ChangeEvent e ) {
            if( e != null && e.getSource() != this ) {
                log.debug( "AdqlTreeView.stateChanged() is resetting adqlTree" ) ;
                adqlTree.setTree( AdqlEntry.newInstance( this.controller.getRootInstance() )); 
                if( catalogueResource != null ) {
                    reestablishTablesCollection() ;
                    writeResourceProcessingInstruction() ;
                }
            }
        }
    }
    
    
    private class AdqlStringView extends JPanel implements ChangeListener {
        
        JTextPane adqlTextPane ;
        Action action ;
        String adqlString ;
        boolean valid ;
        XmlObject rootOnGainingSelection ;
        XmlObject processedRoot ;
        JTabbedPane owner ;
        Controller controller ;
        
        public AdqlStringView( JTabbedPane owner, Controller controller ) {
            super() ;
            this.owner = owner ;
            this.controller = controller ;
            this.controller.addChangeListener( this ) ;
            //
            // Set up the text pane in a scrolling panel etc...
            GridBagConstraints gbc = new GridBagConstraints() ;
            JScrollPane textScrollContent = new JScrollPane();
            this.adqlTextPane = new JTextPane();
            textScrollContent.setViewportView( adqlTextPane );
            this.setLayout(new GridBagLayout());
            gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.weightx = 1;
            gbc.weighty = 1;
            this.add( textScrollContent, gbc ); 
            //
            // Set up the Enter key as a processing key for Adql.
            // ie: everytime the user presses the Enter key we
            // validate and store the results...
            initKeyProcessing() ;
   
            this.owner.addTab( "Adql/s", this ) ;
            //
            // Set up processing for when this tab is selected or de-selected...
            // (This is complex so see other comments.)
            initSelectedProcessing() ;
        }
        
        
        private void initKeyProcessing() {
            //
            // First we need to find the default action for the Enter key and preserve this.
            // We will use it to invoke the default action after having processed the Enter key.
            Object key = this.adqlTextPane.getInputMap( JComponent.WHEN_FOCUSED ).get( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ) ) ;
            this.action = this.adqlTextPane.getActionMap().get( key ) ;
            // Set up our own key for the Enter key...
            this.adqlTextPane.getInputMap( JComponent.WHEN_FOCUSED )
            .put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "ValidateAdqlString" ) ; 
            //            adqlText.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT )
            //        		.put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "ValidateAdqlString" ) ;
            
            //
            // Now our substitute action. Note how we invoke the default action at the end...
            // (JL Note: not sure whether I should invoke the default process first!!!)
            this.adqlTextPane.getActionMap().put( "ValidateAdqlString", new AbstractAction (){
                public void actionPerformed(ActionEvent e) {
                    AdqlStringView.this.validateAdql() ;
                    AdqlStringView.this.action.actionPerformed( e ) ;
                }
            } );   
        }
        
        private void initSelectedProcessing() {
            this.owner.addChangeListener( new ChangeListener() {
                public void stateChanged( ChangeEvent e ) {
                    Object selectedComponent = ((JTabbedPane)e.getSource()).getSelectedComponent() ;
                    if( selectedComponent == ADQLToolEditorPanel.AdqlStringView.this ) {
                        selectionGained() ;
                    }
                    else {
                        selectionLost() ;
                    }
                }
            }) ;
        }
        
        private void selectionGained() {
            this.rootOnGainingSelection = this.controller.getRootInstance() ;
            XmlCursor nodeCursor = rootOnGainingSelection.newCursor();
            XmlOptions options = new XmlOptions();
            options.setSavePrettyPrint();
            options.setSavePrettyPrintIndent(4);
            String xmlString = nodeCursor.xmlText(options);
            nodeCursor.dispose() ;
            this.adqlTextPane.setText( transformer.transformToAdqls( xmlString, " " ).trim() ) ; 
            this.validateAdql() ;
        }
        
        private void selectionLost() {
            this.validateAdql() ;
            if( processedRoot != null && rootOnGainingSelection != null ) {
                XmlOptions options = new XmlOptions();
                options.setSavePrettyPrint();
                options.setSavePrettyPrintIndent(4);
                XmlCursor nodeCursor = rootOnGainingSelection.newCursor();               
                String xmlStringBefore = nodeCursor.xmlText(options);
                nodeCursor.dispose() ;
                nodeCursor = processedRoot.newCursor();               
                String xmlStringAfter = nodeCursor.xmlText(options);
                nodeCursor.dispose() ;
                if( xmlStringBefore.equals( xmlStringAfter ) == false )  {
                    this.controller.updateModel( this, processedRoot ) ;
                }
            }
        }
        
        private void validateAdql( ) {
            String text = adqlTextPane.getText().trim() ;
            if( text.equals( this.adqlString ) == false ) {
                try {  
                    this.adqlString = text ;
                    Document doc = validator.s2x( text ); 
                    String xmlString = XMLUtils.DocumentToString( doc ) ;
                    this.processedRoot = SelectDocument.Factory.parse( adaptToVersion( xmlString ) ) ;
                    this.valid = true ;
                    diagnostics.setText( "Adql is valid" ) ;                  
                } catch( Throwable sx ) {
                    this.valid = false ;
                    log.debug( sx ) ;
                    Throwable th = sx.getCause() ;
                    String message = "Adql invalid. No message available" ;
                    if( th != null ) {
                        message = th.getMessage() ;
                    }
                    diagnostics.setText( message ) ;
                }
            }
        }
        
        public void stateChanged(ChangeEvent e) {
            if( e != null && e.getSource() != this ) {
                ; 
            }
        }
           
    } // end of class AdqlStringView
    
    
//    public class ValidateAdqlStringAction extends AbstractAction implements FocusListener {
//        
//        JTextPane adqlText ;
//        javax.swing.Action action ;
//              
//        public  ValidateAdqlStringAction( JTextPane adqlText ) {
//            this.adqlText = adqlText ;
//            Object key = this.adqlText.getInputMap( JComponent.WHEN_FOCUSED ).get( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ) ) ;
//            action = this.adqlText.getActionMap().get( key ) ;
//            this.adqlText.getActionMap().put( "ValidateAdqlString", this ) ;
//            this.adqlText.getInputMap( JComponent.WHEN_FOCUSED )
//            .put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "ValidateAdqlString" ) ; 
//            //            adqlText.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT )
//            //        		.put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "ValidateAdqlString" ) ;
//            this.adqlText.addFocusListener( this ) ;          
//        }
//        
//        
//        public void actionPerformed(ActionEvent e) {
//            this.validate() ;
//            this.action.actionPerformed( e ) ;
//        }
//        
//        // Not interested in gaining focus...
//        public void focusGained(FocusEvent e) {}
//        
//        // Interested in losing focus. Will do a pre-emptive validate of the Adql/s...
//        public void focusLost(FocusEvent e) {
//            this.validate() ;
//        }
//        
//        private void validate( ) {
//            diagnostics.setText( "" ) ; 
//            byte[] byteArray = null ;   
//            String text = null ;
//            try {
//                text = adqlText.getText() ;
//                Document doc = validator.s2x( text ); // don't care about result.
//                diagnostics.setText( "Adql is valid" ) ;
//            } catch( Throwable sx ) {
//                log.debug( sx ) ;
//                Throwable th = sx.getCause() ;
//                String message = "No message available" ;
//                if( th != null ) {
//                    message = th.getMessage() ;
//                }
//                diagnostics.setText( message ) ;
//            }
//        }
//        
//    } // end of class ValidateAdqlStringAction
    
    
    
    
    public class TableData {
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
    
    public TableData getTableData( String tableName ) {
        TableData tableData = null ;
        java.util.Set set = fromTables.entrySet() ;
        Iterator it = set.iterator() ;
        while( it.hasNext() ) {
            tableData = (TableData)((java.util.Map.Entry)it.next()).getValue() ;
            if( tableData.database.getTables()[ tableData.tableIndex ].getName().equals( tableName ) )
                break ;
            tableData = null ;
        }
        return tableData ;
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
        return ((AdqlEntry)(this.adqlTree.getModel().getRoot())).getXmlObject() ;
    }
    
} // end of ADQLToolEditor



