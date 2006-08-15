package org.astrogrid.desktop.modules.adqlEditor ;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.io.IOException;
import java.io.InputStream;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.ArrayList; 
import java.util.ListIterator;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Stack;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ComboBoxModel;
import javax.swing.plaf.basic.BasicTreeUI ;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel ;
import javax.swing.JTree;
import javax.swing.JComboBox ;
import javax.swing.KeyStroke;
import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlAnySimpleType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlCursor;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.DatabaseBean;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Catalog;
import org.astrogrid.acr.ivoa.resource.DataCollection;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.desktop.modules.dialogs.editors.ADQLToolEditorPanel;
import org.astrogrid.desktop.modules.adqlEditor.commands.*;
import org.astrogrid.desktop.modules.adqlEditor.nodes.*;
/**
 * A tree view on XML, with nodes representing both elements and attributes. See
 * {@link XmlEntry}and {@link XmlModel}for information on how information
 * about the underlying XML is retrieved for use in the tree. Those classes use
 * XMLBeans to provide a wrapper by which the tree exposes the underlying XML.
 */
public final class AdqlTree extends JTree
{
    private static final boolean DEBUG_ENABLED = false ;
    private static final boolean TRACE_ENABLED = false ;
    private static final Log log = LogFactory.getLog( AdqlTree.class ) ;
    private static final String EDIT_PROMPT_NAME = "Ctrl+Space" ;
    private static final String POPUP_PROMPT_NAME = "Ctrl+M/m" ;
    private static final char[] ALIAS_NAMES = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    
    private boolean debug = false ;
    
    private Registry registry ;
    private URI toolIvorn ;
    private AliasStack aliasStack ;
    private DataCollection catalogueResource = null ;
    private HashMap fromTables = new HashMap() ;
    private XmlObject xmlFrom ;
    
    private boolean editingActive = false ;
    private int availableWidth ;
    private CommandFactory commandFactory ;
    private String adqlSchemaVersion ;
    
    /**
     * Constructs the tree using <em>xmlFile</em> as an original source of
     * nodes.
     * 
     * @param xmlFile The XML file the new tree should represent.
     */
    public AdqlTree( File xmlFile, Registry registry, URI toolIvorn ) {
        initialize( AdqlTree.parseXml( xmlFile ), registry, toolIvorn ) ;
    }
    
    
    public AdqlTree( InputStream xmlStream, Registry registry, URI toolIvorn ) {
        initialize( AdqlTree.parseXml( xmlStream ), registry, toolIvorn ) ;
    }
    
    public AdqlTree(  Registry registry, URI toolIvorn ) {
        initialize( AdqlTree.parseXml( AdqlData.NEW_QUERY ), registry, toolIvorn ) ;
    }
    
    public AdqlTree( String xmlString, Registry registry, URI toolIvorn ) {
        initialize( AdqlTree.parseXml( xmlString ), registry, toolIvorn ) ;
    }
    
    public AdqlTree( org.w3c.dom.Node node, Registry registry, URI toolIvorn ) {
        initialize( AdqlTree.parseXml( node ), registry, toolIvorn ) ;
    }
    
    public void setTree( AdqlNode rootEntry, Registry registry, URI toolIvorn ) {
        this.initialize( rootEntry, registry, toolIvorn ) ;
    }
    
    public AdqlTreeCellRenderer getTreeCellRenderer() {
        return (AdqlTreeCellRenderer)super.getCellRenderer() ;
    }
 

    public boolean isPathEditable( TreePath path ) {
        boolean result = false ;
        if( path != null && path.getPathCount() > 1) {
            AdqlNode entry = (AdqlNode)path.getLastPathComponent() ;
            result = entry.isBottomLeafEditable() ;
        }
        return result ;
    }
    
    
    public void openBranches() {
        Object[] obj = new Object[2] ;
        obj[0] = getModel().getRoot() ;
        AdqlNode childEntries[] = ((AdqlNode)obj[0]).getChildren() ;
        for( int i=0; i<childEntries.length; i++ ) {
            obj[1] = childEntries[i] ;
            TreePath path = new TreePath( obj ) ;
            expandPath( path ) ;
        } 
    }
    
    
    
    public String convertValueToText( Object value
                                    , boolean sel
                                    , boolean expanded
                                    , boolean leaf
                                    , int row
                                    , boolean hasFocus) {
        if( value != null && value instanceof AdqlNode ) {
            
            if( debug == true ) {
                debug = false ;
            }
            
            String html = null ;
            try {
                 html = ((AdqlNode)value).toHtml( expanded, leaf, this ) ;
            }
            catch( Exception ex ) {
                log.debug( "AdqlTree.convertValueToText(): ", ex );
            }
            
            //
            // The following code ensures we draw a tasteful border around any displays
            // which are multi-lined. Dont like the way it is done, but hopefully this
            // will be a short-term measure.
            if( html.indexOf( "<br>") != -1 ) {
                ((JLabel)getCellRenderer()).setBorder( BorderFactory.createEtchedBorder() ) ;
            }
            else {
               ((JLabel)getCellRenderer()).setBorder( BorderFactory.createEmptyBorder() ) ;
            }          
            return super.convertValueToText( html, sel, expanded, leaf, row, hasFocus ) ;       
        }
        return super.convertValueToText( value, sel, expanded, leaf, row, hasFocus ) ;
    }
    
 
    public boolean isEditingActive() {
        return editingActive;
    }
    public void setEditingActive(boolean editingActive) {
        this.editingActive = editingActive;
    }
    
    
    /**
     * Parses <em>xmlFile</em> into XMLBeans types (XmlObject instances),
     * returning the instance representing the root.
     * 
     * @param xmlFile The XML file to parse.
     * @return An XmlObject representing the root of the parsed XML.
     */
    public static AdqlNode parseXml(File xmlFile)
    {
        AdqlNode entry = null ;
        try {
            entry = NodeFactory.newInstance( SelectDocument.Factory.parse( xmlFile ) ) ;
        } catch (XmlException xmle) {
            System.err.println(xmle.toString());
        } catch (IOException ioe) {
            System.err.println(ioe.toString());
        }
        return entry ;
    }
    
    public static AdqlNode parseXml( InputStream xmlStream )
    {
        AdqlNode entry = null ;
        try {
            entry = NodeFactory.newInstance( SelectDocument.Factory.parse( xmlStream ) ) ;
        } catch (XmlException xmle) {
            System.err.println(xmle.toString());
        } catch (IOException ioe) {
            System.err.println(ioe.toString());
        }
        return entry ;
    }
    
    
    public static AdqlNode parseXml( String xmlString )
    {
        AdqlNode entry = null ;
        try {
            entry = NodeFactory.newInstance( SelectDocument.Factory.parse( xmlString ) ) ;
        } catch (XmlException xmle) {
            System.err.println(xmle.toString());
        }
        return entry ;
    }
    
    public static AdqlNode parseXml( org.w3c.dom.Node xmlNode )
    {
        AdqlNode entry = null ;
        try {
            entry = NodeFactory.newInstance( SelectDocument.Factory.parse( xmlNode ) ) ;
        } catch (XmlException xmle) {
            System.err.println(xmle.toString());
        }
        return entry ;
    }
    

    /**
     * Sets up the components that make up this tree.
     * 
     */
    private void initialize( AdqlNode rootEntry, Registry registry, URI toolIvorn ) {
   
        this.commandFactory = new CommandFactory( this ) ;
        setModel( new DefaultTreeModel( rootEntry ) );
        getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION ) ;
        this.registry = registry ;
        this.toolIvorn = toolIvorn ;
        retrieveAdqlSchemaVersion() ;
        this.resetCatalogueData() ;
        if( catalogueResource != null ){
            reestablishTablesCollection() ;
        }
      
        AdqlTreeCellRenderer renderer = new AdqlTreeCellRenderer() ;
        setCellRenderer( renderer ) ;
        setCellEditor( new AdqlTreeCellEditor( renderer ) ) ;
        setRootVisible( false ) ;
        setShowsRootHandles( true ) ;
        setAutoscrolls( false ) ;
        setEditable( true ) ;         
        setRowHeight( 0 ) ;
        //
        // This sets up the key stroke Ctrl+Space as an editing toggle for those leafs/nodes that
        // can be directly edited. Amazingly, this is all that is required.
        EditPromptAction editPromptAction = new EditPromptAction( EDIT_PROMPT_NAME ) ;
        this.getActionMap().put( EDIT_PROMPT_NAME, editPromptAction ) ;
        this.getInputMap( JComponent.WHEN_FOCUSED )
        	.put( KeyStroke.getKeyStroke( KeyEvent.VK_SPACE, InputEvent.CTRL_MASK ), EDIT_PROMPT_NAME ) ; 
        this.getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT )
    		.put( KeyStroke.getKeyStroke( KeyEvent.VK_SPACE, InputEvent.CTRL_MASK ), EDIT_PROMPT_NAME ) ;
        
    }
    
    private void resetCatalogueData() {
        String piName = null ;
        String piValue = null ;
        this.catalogueResource = null ;
        XmlCursor cursor = getRoot().newCursor() ;
        if( DEBUG_ENABLED ) log.debug( "Searching for catalogue PI..." ) ;
        while( !cursor.toNextToken().isNone() ) {
            if( cursor.isProcinst() ) {
                piName = cursor.getName().getLocalPart() ;             
            	if( piName.equals ( AdqlData.PI_QB_REGISTRY_RESOURCES ) )  {
            	    if( DEBUG_ENABLED ) log.debug( "PI name: " + cursor.getName() ) ;
                    if( DEBUG_ENABLED ) log.debug( "PI text: " + cursor.getTextValue() ) ;
            	    piValue = cursor.getTextValue().trim() ;
            	    if( piValue.equals( "none" ) )
            	        break ;
            	    
            	    	String id = formatCatalogueId(piValue);
            	    	try {
            	            catalogueResource = (DataCollection) registry.getResource(new URI(id));
            	    	} catch (Throwable e) {
            	    		log.info("searching for table definition using " + id + " failed");
            	    	}
            	    // NB. This query of the registry is currently on the main thread!!!
                    if( DEBUG_ENABLED ) log.debug( "catalogueResource: " + (catalogueResource==null?"null":"not null") ) ;
                    break ;
            	}
            }
        } // end while
        cursor.dispose();
        //
        // Here I am trying to second-guess the appropriate database for this dsa tool.
        // Cannot see any harm to this. If it fails, the user still has the option of
        // choosing for him/herself.
        if( catalogueResource == null ) {
            String ivorn = toolIvorn.toString() ;
            int index = ivorn.lastIndexOf( "ceaApplication" ) ;
            if( index != -1 ) {
            	String id= ivorn.substring(0,index) + "TDB";
                try {
                	catalogueResource = (DataCollection)registry.getResource(new URI(id));
                } catch (Throwable e) {
                	log.info("searching for table definition using " + id + " failed");
                }
                if( DEBUG_ENABLED ) log.debug( "catalogueResource: " + (catalogueResource==null?"null":"not null") ) ;              
            }
        }
    }
    
    private void retrieveAdqlSchemaVersion() {
        String piName = null ;
        String piValue = null ;
        adqlSchemaVersion = null ;
        XmlCursor cursor = getRoot().newCursor() ;
        if( DEBUG_ENABLED ) log.debug( "Searching for schema version PI..." ) ;
        while( !cursor.toNextToken().isNone() ) {
            if( cursor.isStart()
                &&
                cursor.getObject().schemaType().getName().getLocalPart().equals( AdqlData.SELECT_TYPE ) ) {
            	    cursor.push() ; // remember where the actual Select statement is.
            }
            else if( cursor.isProcinst() ) {
                piName = cursor.getName().getLocalPart() ;
            	if( piName.equals( AdqlData.PI_ADQL_SCHEMA_VERSION_TAG ) )  {
            	    if( DEBUG_ENABLED ) log.debug( "PI name: " + cursor.getName() ) ;
                    if( DEBUG_ENABLED ) log.debug( "PI text: " + cursor.getTextValue() ) ;
            	    piValue = cursor.getTextValue().trim() ;
            	    adqlSchemaVersion = piValue ;
                    break ;
            	}         	
            }
        } // end while
        if( adqlSchemaVersion == null ) {
            cursor.pop() ; // position on the Select statement.
            // We write a version PI to track the version of the ADQL schema used...
            // This places it immediately before the Select statement.
            cursor.insertProcInst( AdqlData.PI_ADQL_SCHEMA_VERSION_TAG, AdqlData.PI_ADQL_SCHEMA_VERSION_VALUE ) ;
        }
        cursor.dispose();
        // OK. This is a placeholder for future processing, when some action will be
        // required to accommodate changes in ADQL between versions.
    }
    
    private String formatCatalogueId ( String piValue ) {
        // This value follows a standard set up in the portal prior to
	    // the establishment of ivorns and overcomes some of the problems
	    // of special characters within table names. 
	    // Note: The portal QB allows only one table (no joins) so the metadata
	    // reflects the one table. But we need the whole catalogue/database.
        String ivornString = "ivo://" + piValue.substring( 0, piValue.lastIndexOf( '!' ) ).replace( '!', '/' ) ;
        if( DEBUG_ENABLED ) log.debug( "catalogue ivorn: " + ivornString ) ;
        return ivornString ;
    }
    

    
    private void writeResourceProcessingInstruction() {
        String piName = null ;
        String piValue = null ;
        XmlCursor cursor = getRoot().newCursor() ;
        if( DEBUG_ENABLED ) log.debug( "Searching for PI's..." ) ;
        while( !cursor.toNextToken().isNone() ) {
            if( cursor.isProcinst() ) {
                piName = cursor.getName().getLocalPart() ;
                if( DEBUG_ENABLED ) log.debug( "PI name: " + cursor.getName() ) ;
                if( DEBUG_ENABLED ) log.debug( "PI text: " + cursor.getTextValue() ) ;
            	if( piName.equals( AdqlData.PI_QB_REGISTRY_RESOURCES ) )  {
            	   // OK. There's already one here.
            	   // But does it say "none"?...
            	    if( cursor.getTextValue().trim().equals( "none") ) {
            	        piValue = catalogueResource.getId().getSchemeSpecificPart().substring( 2 ) 
                        + '!' 
                        + catalogueResource.getCatalog().getTables()[0].getName() ;
            	        cursor.setTextValue( piValue ) ;
            	    }
            	}
            }
            else if( cursor.isStart()
                     &&
                     cursor.getObject().schemaType().getName().getLocalPart().equals( AdqlData.SELECT_TYPE ) ) {
                // We need to create the required PI to track the catalogue resource.
                // Currently only one catalogue. Soon we will need to cover multiple catalogues...
                // (I've simply chosen the first table to align it with the portal)
                piValue = catalogueResource.getId().getSchemeSpecificPart().substring( 2 ) 
                        + '!' 
                        + catalogueResource.getCatalog().getTables()[0].getName() ;
                if( DEBUG_ENABLED ) log.debug( "new PI Text: " +piValue ) ;
                cursor.insertProcInst( AdqlData.PI_QB_REGISTRY_RESOURCES, piValue ) ;
                break ;               
            }
        } // end while
        cursor.dispose();
    }
    
    
    private void reestablishTablesCollection() {
        aliasStack = new AliasStack() ;
        XmlString xTableName = null ;
        XmlString xAlias = null ;
        String alias = null ;
//        String greatestAlias = null ;
        Catalog db = catalogueResource.getCatalog() ;
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
                if( DEBUG_ENABLED ) log.debug( "table name: " 
                         + xTableName.getStringValue() 
                         + " with alias: "
                         + (xAlias==null ? "null" : xAlias.getStringValue()) ) ;  
                fromTables.put( xTableName.getStringValue()
                              , new TableData( db, findTableIndex( db, xTableName.getStringValue()), alias ) ) ;
//                if( alias != null && (greatestAlias == null || greatestAlias.compareTo(alias) < 0) ) {
//                        greatestAlias = alias ;
//                }
            }
        } // end while
        cursor.dispose();
        // Reset the alias stack to the "largest" previously used value... 
//        if( greatestAlias != null ) {
//            // This is potentially dangerous. So until I have time to work out a better way,
//            // I'm assuming no more then 100 tables will ever be tried...
//            for( int i=0; i<100; i++ ) {
//                if( aliasStack.pop().equals( greatestAlias ) ) 
//                    break ;
//            }    
//        }
    }
    
    private int findTableIndex( Catalog db, String tableName ) {
        TableBean[] tables = db.getTables() ;
        for( int i=0; i<tables.length; i++ ) {
            if( tables[i].getName().equals( tableName ) ) {
                return i ;
            }
        }
        return -1;
    }

    
    public int getAvailableWidth() {
        return availableWidth;
    }
    public void setAvailableWidth(int availableWidth) {
        this.availableWidth = availableWidth ;
        //
        // The next two lines are a workaround to ensure the
        // BasicTreeUI resets its cache of node sizes...
        BasicTreeUI ui = (BasicTreeUI)getUI() ;
       	ui.setRightChildIndent( ui.getRightChildIndent() ) ;
    }
   
    public XmlObject getRoot() {
        return ((AdqlNode)(getModel().getRoot())).getXmlObject() ;
    }
    

    private String extractAttributeValue( XmlObject xmlObject ) {
        String retVal = null ;
        XmlObject intermediateObject ;         
        SchemaType type = xmlObject.schemaType() ;
        String[] attributeName = (String[])AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
        intermediateObject = AdqlUtils.get( xmlObject, attributeName[0] ) ;
        if( intermediateObject != null )
            retVal = ((SimpleValue)(intermediateObject)).getStringValue() ;
        return retVal ;
    }
    
    
    public TableData getTableData( String tableName ) {
        TableData tableData = null ;
        java.util.Set set = fromTables.entrySet() ;
        Iterator it = set.iterator() ;
        while( it.hasNext() ) {
            tableData = (TableData)((java.util.Map.Entry)it.next()).getValue() ;
            try {
                if( tableData.database.getTables()[ tableData.tableIndex ].getName().equals( tableName ) )
                    break ;
            }
            catch( ArrayIndexOutOfBoundsException ex ) {
                ;
            }
            tableData = null ;
        }
        return tableData ;
    }
    
    public DataCollection getCatalogueResource() {
        return catalogueResource;
    }
    
    public void setCatalogueResource( DataCollection tdbInfo ) {
        this.catalogueResource = tdbInfo ;
        this.writeResourceProcessingInstruction() ;
    }
    
    public boolean isCatalogueResourceSet() {
        return catalogueResource != null ;
    }
    
    public HashMap getFromTables() {
        return fromTables;
    }
    
    public String popAliasStack() {
        return aliasStack.pop() ;
    }
    
    public void pushAliasStack( String alias ) {
        aliasStack.push( alias ) ;
    }
    
    public CommandFactory getCommandFactory() {
        return this.commandFactory ;
    }
    
    //
    // Amazingly, this works for controlling editing.
    public class EditPromptAction extends AbstractAction {
        
        public EditPromptAction( String name ) {
            super( name ) ;
            setInvokesStopCellEditing( true ) ;
        }
        
        public void actionPerformed( ActionEvent e ) {
            if( editingActive ) {               
                //
                // This is not adequate on its own.
                // We need a listener on all tree changes that effect
                // structure and values, so that the
                // query parameter can be correctly maintained.
                stopEditing() ;            
                editingActive = false ;
            }
            else {
                TreePath path = getSelectionPath() ;             
                if( path != null ) {
                    AdqlNode selectedEntry = (AdqlNode)getLastSelectedPathComponent();
                    if( selectedEntry.isBottomLeafEditable() ) {
                        startEditingAtPath( path ) ;
                        editingActive = true ;
                    }
                }
            }         
            
        }
        
    } // end of class EditPromptAction
    
    
    public class AdqlTreeCellRenderer extends DefaultTreeCellRenderer {
        
        
        private volatile int useableWidth ;
        private boolean expanded ;
        private boolean useOppositeExpansionState = false ;
        
        public AdqlTreeCellRenderer() {
            setIcon( null ) ;
            setLeafIcon( null ) ;
            setOpenIcon( null ) ;
            setClosedIcon( null ) ;
            setHorizontalAlignment( LEFT ) ;
            setVerticalAlignment( TOP ) ;
            setBorder( BorderFactory.createEtchedBorder() ) ;
        }
        
        public void setIcon( javax.swing.Icon icon ) { }
        
        
        public Component getTreeCellRendererComponent( JTree tree
                                                     , Object value
                                                     , boolean sel
                                                     , boolean expanded
                                                     , boolean leaf
                                                     , int row
                                                     , boolean hasFocus) {
            
            AdqlNode entry = (AdqlNode)value ;
            int level = ((AdqlNode)value).getLevel() ; 
            // The following 20 pixels is a guess at what a one level displacement
            // should be. Seems to be pretty accurate.
            int displacement = ( level + 1 ) * 20 ;  
            this.useableWidth = getAvailableWidth() - displacement ;
            // We cannot let things get rediculously small ...
            if( this.useableWidth  < 200 )
                this.useableWidth  = 200 ;
            entry.setUseableWidth( this.useableWidth ) ;
            return super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus) ;
        }
        
        public void setUseOppositeExpansionState() {
            useOppositeExpansionState = true ;
        }
        
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize() ;
            d.width = this.useableWidth ;
            return d ;
        }
       
    } // end of class AdqlTreeCellRenderer
    

    
    public class AdqlTreeCellEditor implements TreeCellEditor, ActionListener {
        
        private static final int MINIMUM_WIDTH = 100 ;
        Vector listeners = new Vector() ;
        AdqlNode adqlNode ;
        EnumeratedEditor enumeratedEditor ;
        SingletonTextEditor singletonTextEditor ;
        TupleTextEditor tupleTextEditor ;
        CellEditor currentEditor ;
        AdqlTreeCellRenderer renderer ;
        
        public AdqlTreeCellEditor( AdqlTreeCellRenderer renderer ) {
            this.renderer = renderer ;
            enumeratedEditor = new EnumeratedEditor() ;
            enumeratedEditor.setFont( AdqlTree.this.getFont() ) ;
            enumeratedEditor.addActionListener( this ) ;
            singletonTextEditor = new SingletonTextEditor() ;
            singletonTextEditor.setFont( AdqlTree.this.getFont() ) ;
            singletonTextEditor.addActionListener( this ) ;
            tupleTextEditor = new TupleTextEditor() ;
            tupleTextEditor.setFont( AdqlTree.this.getFont() ) ;
           // tupleTextEditor.addActionListener( this ) ;
        }
        
        public Component getTreeCellEditorComponent( JTree tree
                                                   , Object value
                                                   , boolean isSelected
                                                   , boolean expanded
                                                   , boolean leaf
                                                   , int row ) {
            this.adqlNode = (AdqlNode)value ;
            if( AdqlUtils.isEnumerated( adqlNode.getSchemaType() ) ) {
                currentEditor = enumeratedEditor ;
                enumeratedEditor.setValue() ;
            }
            else if( isTupleEditorRequired( this.adqlNode ) ) {
                currentEditor = tupleTextEditor ;
                tupleTextEditor.setValue() ;
            }
            else {
                currentEditor = singletonTextEditor ;
                singletonTextEditor.setValue() ;
            }
            return (Component)currentEditor ;
        }
        public void addCellEditorListener( CellEditorListener l ) {
            listeners.addElement( l ) ;
        }
        public void cancelCellEditing() {
            currentEditor.cancelCellEditing() ;
        }
        public Object getCellEditorValue() {
            debug = true ;
            return currentEditor.getCellEditorValue() ; 
        }
        public boolean isCellEditable( EventObject anEvent ) {
            if( anEvent instanceof MouseEvent ) { 
                return false ;
            }
            return true ;
//            if( currentEditor == null )
//                return true ;
//            return currentEditor.isCellEditable( anEvent ) ;
        }
        public void removeCellEditorListener( CellEditorListener l ) {
            listeners.removeElement( l ) ;
        }
        public boolean shouldSelectCell( EventObject anEvent ) {
            return currentEditor.shouldSelectCell( anEvent ) ;
        }
        public boolean stopCellEditing() {
            return currentEditor.stopCellEditing() ;
        }
        
        protected void fireEditingStopped() {
            if( listeners.size() > 0 ) {
                ChangeEvent changeEvent = new ChangeEvent( this ) ;
                for( int i=listeners.size()-1; i>=0; i-- ) {
                    ( (CellEditorListener)listeners.elementAt(i) ).editingStopped( changeEvent ) ;
                }
            }
        }
                  
        public void actionPerformed( ActionEvent e ) {
            if( stopCellEditing() ) {
                fireEditingStopped() ;
            }
        }
        
        public Rectangle setBounds( Rectangle rectangle ) {
            rectangle.width = Math.max( MINIMUM_WIDTH, rectangle.width ) ;
            return rectangle ;
        }
       
        public Rectangle setBounds( int x, int y, int w, int h ) {
            Rectangle r = new Rectangle( x, y, w, h ) ;
            return setBounds( r ) ;
        }
        
        private boolean isTupleEditorRequired( AdqlNode entry ) {
            String[] names = (String[])AdqlData.EDITABLE.get( entry.getXmlObject().schemaType().getName().getLocalPart() ) ;
            return ( names != null && names.length > 1 ) ;
        }
        
        
        public class EnumeratedEditor extends JComboBox implements CellEditor {
            
            XmlObject attribObj ;
                    
            public EnumeratedEditor() {
                super() ;
           //     setKeySelectionManager( new EnumeratedEditor.KeySelectionManager() ) ;
                setPrototypeDisplayValue( new String( "mmmmmmmmmmmmmmmm" ) ) ;
                setEditable( true ) ; 
            }
            
            public void addCellEditorListener( CellEditorListener l ) {
                AdqlTreeCellEditor.this.addCellEditorListener( l ) ;
            }
            public void cancelCellEditing() {}
            
            public Object getCellEditorValue() {
                setAttributeValue( (String)getSelectedItem() ) ;
                return adqlNode.getXmlObject();
            }
            public boolean isCellEditable(EventObject anEvent) {
              return true ;
            }
            public void removeCellEditorListener(CellEditorListener l) {
                AdqlTreeCellEditor.this.removeCellEditorListener( l ) ;
            }
            public boolean shouldSelectCell(EventObject anEvent) {
                return true ;
            }
            public boolean stopCellEditing() {
                try {
                    if( getSelectedItem() == null )
                        getItemAt( 0 ) ;
                    return true ;
                }
                catch( Exception ex ) {
                    ;
                }
                return false;
            }
            
            public void setValue() {              
                DefaultComboBoxModel model = (DefaultComboBoxModel)getModel();
                model.removeAllElements() ;
                SchemaType type = adqlNode.getSchemaType() ;
                String attributeTypeName = (String)AdqlData.ENUMERATED_ATTRIBUTES.get( type.getName().getLocalPart() ) ;
                SchemaProperty[] properties = type.getAttributeProperties() ;
                XmlAnySimpleType[] enumeratedValues = null ;
                for( int i=0; i<properties.length; i++ ) {
                    if( attributeTypeName.equals( properties[i].getType().getName().getLocalPart() ) ) {
                        enumeratedValues = properties[i].getType().getEnumerationValues() ;
                        break ;
                    }
                }
                
                String[] attributeNames = (String[])AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
                // Retrieve and remember the attribute object.
                // This is used when the user has finished editing to set the new value...
                this.attribObj = AdqlUtils.get( adqlNode.getXmlObject(), attributeNames[0] ) ;
                String currentValue = ((SimpleValue)(this.attribObj)).getStringValue() ;
                String comboValue = null ;
                for( int i=0; i<enumeratedValues.length; i++ ) {
                    comboValue = enumeratedValues[i].getStringValue() ;
                    addItem( comboValue ) ;
                    if( currentValue.equals( comboValue ) ) {
                        setSelectedIndex( i ) ;
                    }
                }
                
            }
            
            public void setBounds( Rectangle rectangle ) {
                super.setBounds( AdqlTreeCellEditor.this.setBounds( rectangle ) ) ;
            }
            
            public void setBounds( int x, int y, int w, int h ) {
                Rectangle r = AdqlTreeCellEditor.this.setBounds( x, y, w, h ) ;
                super.setBounds( r.x, r.y, r.width, r.height ) ;
            }
            
            private void setAttributeValue( String value ) {
                try {
                    ((SimpleValue)this.attribObj).setStringValue( value ) ;
                }
                catch( Exception ex ) {
                    log.warn( "Failure to set attribut value:", ex ) ;
                }
  
            }

            public class KeySelectionManager implements JComboBox.KeySelectionManager {
                
                public int selectionForKey( char aKey, ComboBoxModel aModel ) {
                    int index = -1 ;
                    int move = 0 ;
                    if( aKey == KeyEvent.VK_UP || aKey == KeyEvent.VK_KP_UP ) {
                        move = -1 ;
                    }
                    else if( aKey == KeyEvent.VK_DOWN || aKey == KeyEvent.VK_KP_DOWN ) {
                        move = +1 ;
                    }
                    else {
                        return index ;
                    } 
                    DefaultComboBoxModel model = (DefaultComboBoxModel)aModel ;
                    int listSize = model.getSize() ;
                    index = model.getIndexOf( model.getSelectedItem() ) + move ;
                    if( index >= listSize )
                        index = -1 ;
                    
                    return index ;
                }
                        
            }
                
        } // end of class EnumeratedEditor
        
        public class SingletonTextEditor extends JTextField implements CellEditor {
            
            public SingletonTextEditor() {
                super() ;
            }
            
            public void addCellEditorListener(CellEditorListener l) {
                AdqlTreeCellEditor.this.addCellEditorListener( l ) ;
            }
            public void cancelCellEditing() {}
            
            public Object getCellEditorValue() {
                setElementValue( getText() ) ;
                return adqlNode.getXmlObject() ;
            }
            public boolean isCellEditable(EventObject anEvent) {
                return true ;
            }
            public void removeCellEditorListener(CellEditorListener l) {
                AdqlTreeCellEditor.this.removeCellEditorListener( l ) ;
            }
            public boolean shouldSelectCell(EventObject anEvent) {
                return true ;
            }
            public boolean stopCellEditing() {
                return true ;
            }
            
            public void setValue() {
                String value = null ;
                XmlObject xmlObject = adqlNode.getXmlObject() ;
                try {
//                    if( AdqlUtils.localNameEquals( xmlObject, "atomType") ) {
//                        value = (new AdqlNode.Atom( xmlObject )).formatDisplay() ;
//                    }
                    if( adqlNode instanceof AtomNode ) {
                        value = ((AtomNode)adqlNode).formatDisplay() ;
                    }
                    else if( AdqlUtils.isAttributeDriven( xmlObject.schemaType() ) ) {
                        String[] attributeNames = (String[])AdqlData.EDITABLE.get( xmlObject.schemaType().getName().getLocalPart() ) ;
                        XmlObject attribObj = AdqlUtils.get( adqlNode.getXmlObject(), attributeNames[0] ) ;
                        value = ((SimpleValue)(attribObj)).getStringValue() ;
                    }
                    else {
                        value = ((SimpleValue)xmlObject).getStringValue() ;
                    }

                }
                catch( Exception ex ) {
                    value = "" ;
                    log.error( ex ) ;
                }
                setText( value ) ;
            }
            
            public void setBounds( Rectangle rectangle ) {
                super.setBounds( AdqlTreeCellEditor.this.setBounds( rectangle ) ) ;
            }
            
            public void setBounds( int x, int y, int w, int h ) {
                Rectangle r = AdqlTreeCellEditor.this.setBounds( x, y, w, h ) ;
                super.setBounds( r.x, r.y, r.width, r.height ) ;
            }
            
            private void setElementValue( String value ) {
                try {
                    XmlObject xmlObject = adqlNode.getXmlObject() ;
                    SchemaType type = xmlObject.schemaType() ;
                    SchemaType listType = type.getListItemType() ;
                    if( listType != null ) {
                        // This surprisingly works for a list...
                        ((XmlAnySimpleType)xmlObject).setStringValue( value )  ;
                    }
//                    else if( AdqlUtils.localNameEquals( xmlObject, "atomType") ) {
//                        (new AdqlNode.Atom( xmlObject )).setValue( value ) ;
//                    }
                    else if( adqlNode instanceof AtomNode ) {
                        ((AtomNode)adqlNode).setValue( value ) ;
                    }
                    else if( AdqlUtils.isAttributeDriven( type ) ) {
                        String[] attributeNames = (String[])AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
                        XmlObject attr = AdqlUtils.get( xmlObject, attributeNames[0] ) ;
                        if( value == null || value.trim().length() == 0 ) {
                           // I cannot see any reason for the attribute be optional
                           // and not existing, but for safety's sake...
                           if( AdqlUtils.isOptionalAttribute( xmlObject, attributeNames[0] ) ) {
                               AdqlUtils.unset( xmlObject, attributeNames[0] ) ;
                           }
                           else {
                               ;  // we do nothing, ignoring any changes.
                           }
                        }
                        else {
                            ((XmlAnySimpleType)attr).setStringValue( value ) ;
                        }                  
                    }
                    else {
                        ((XmlAnySimpleType)xmlObject).setStringValue( value )  ;
                    }
                }
                catch( Exception ex ) {
                    log.warn( "Failure to set element values: ", ex ) ;
                }              
            }
            
        }
        
        public class TupleTextEditor extends Box implements CellEditor {
            
            private JTextField[] fields = new JTextField[2] ;
            
            public TupleTextEditor() {
                super( BoxLayout.X_AXIS ) ;
                fields[0] = new JTextField() ;
                add( fields[0] ) ;
                fields[1] = new JTextField() ;
                add( fields[1] ) ;
                
            }
            
            public void addCellEditorListener(CellEditorListener l) {
                AdqlTreeCellEditor.this.addCellEditorListener( l ) ;
            }
            public void cancelCellEditing() {}
            
            public Object getCellEditorValue() {
                setElementValue() ;
                return adqlNode.getXmlObject() ;
            }
            public boolean isCellEditable(EventObject anEvent) {
                return true ;
            }
            public void removeCellEditorListener(CellEditorListener l) {
                AdqlTreeCellEditor.this.removeCellEditorListener( l ) ;
            }
            public boolean shouldSelectCell(EventObject anEvent) {
                return true ;
            }
            public boolean stopCellEditing() {
                return true ;
            }
            
            public void setValue() {
                XmlObject xmlObject = adqlNode.getXmlObject() ;
                XmlObject attribute = null ;
                try {
                    String[] attrNames = (String[])AdqlData.EDITABLE.get( xmlObject.schemaType().getName().getLocalPart() ) ;
                    for( int i=0; i<attrNames.length; i++ ) {
                        attribute = AdqlUtils.get( xmlObject, attrNames[i] ) ;
                        if( attribute == null ) {
                            fields[i].setText( "" ) ;
                        }
                        else {
                            fields[i].setText( ((SimpleValue)attribute).getStringValue() ) ;
                        }                       
                    }
                }
                catch( Exception ex ) {
                    log.error( ex ) ;
                }
            }
            
            public void setBounds( Rectangle rectangle ) {
                super.setBounds( AdqlTreeCellEditor.this.setBounds( rectangle ) ) ;
            }
            
            public void setBounds( int x, int y, int w, int h ) {
                Rectangle r = AdqlTreeCellEditor.this.setBounds( x, y, w, h ) ;
                super.setBounds( r.x, r.y, r.width, r.height ) ;
            }
            
            private void setElementValue() {
                try {
                    String newValue = null ;
                    String oldValue = null ;
                    XmlObject element = adqlNode.getXmlObject() ;
                    XmlObject attribute = null ;
                    String[] attributeNames = (String[])AdqlData.EDITABLE.get( AdqlUtils.getLocalName( element ) ) ;
                    
                    for( int i=0; i<attributeNames.length; i++ ) {                        
                        boolean bOptional = AdqlUtils.isOptionalAttribute( element, attributeNames[i] ) ;
                        oldValue = getOldValue( element, attributeNames[i], bOptional ) ;
                        newValue = getNewValue( fields[i] ) ;
                        if( newValue == null ) {
                           if( bOptional
                               &&
                               AdqlUtils.isSet( element, attributeNames[i] ) 
                               && 
                               preValidateOK( element, attributeNames[i], bOptional, oldValue, newValue ) ) {
                               AdqlUtils.unset( element, attributeNames[i] ) ;
                               crossValidateDelete( element, attributeNames[i], oldValue, newValue ) ;
                           }
                           else {
                               ;  // Cannot delete a non-optional attribute. We do nothing, ignoring any changes.
                           }
                        }
                        else if( oldValue == null 
                                 && 
                                 preValidateOK( element, attributeNames[i], bOptional, oldValue, newValue ) ) {                          
                            AdqlUtils.set( element, attributeNames[i], XmlString.Factory.newValue( newValue ) ) ;
                            crossValidateCreate(element, attributeNames[i], oldValue, newValue ) ;                                 
                        }
                        else if( !areEqual( oldValue, newValue )
                                 && 
                                 preValidateOK( element, attributeNames[i], bOptional, oldValue, newValue ) ) {              
                            AdqlUtils.set( element, attributeNames[i], XmlString.Factory.newValue( newValue ) ) ;
                            crossValidateUpdate(element, attributeNames[i], oldValue, newValue ) ;                         
                        }                  
                    }
                }
                catch( Exception ex ) {      
                    log.warn( "Failure to set element values: ", ex ) ;
                }
            } // end of setElementValue()
            
            private String getOldValue( XmlObject element, String attributeName, boolean bOptional ) {
                String oldValue = null ;
                if( !bOptional || AdqlUtils.isSet( element, attributeName ) ) {
                    oldValue = ((SimpleValue)AdqlUtils.get( element, attributeName )).getStringValue() ;           
                }
                return oldValue ;
            }
            
            private String getNewValue( JTextField field ) {
                String newValue = field.getText() ;
                if( newValue != null ) {
                    newValue = newValue.trim() ;
                    if( newValue.length() == 0 )
                        newValue = null ;
                }
                return newValue ;
            }
            
            private boolean areEqual( String oldValue, String newValue ) {
                if( oldValue == null & newValue == null )
                    return true ;
                return String.valueOf( oldValue ).equals( newValue ) ;
            }
            
            private boolean preValidateOK( XmlObject element
                                         , String attributeName
                                         , boolean attributeOptional
                                         , String oldValue
                                         , String newValue ) {
                if( isTableValidationRequired( element, attributeName ) )
                    return preValidateTable( element, attributeName, attributeOptional, oldValue, newValue ) ;
                return true ;
            }
            
            private void crossValidateDelete( XmlObject element
                                            , String attributeName
                                            , String oldValue
                                            , String newValue ) {
                if( isTableValidationRequired( element, attributeName ) )
                    crossValidateTableDelete( element, attributeName, oldValue, newValue ) ;
            }
            
            private void crossValidateUpdate( XmlObject element
                                            , String attributeName
                                            , String oldValue
                                            , String newValue ) {
                if( isTableValidationRequired( element, attributeName ) )
                    crossValidateTableUpdate( element, attributeName, oldValue, newValue ) ;
            }
            
            private void crossValidateCreate( XmlObject element
                                            , String attributeName
                                            , String oldValue
                                            , String newValue ) {
                if( isTableValidationRequired( element, attributeName ) )
                    crossValidateTableCreate( element, attributeName, oldValue, newValue ) ;
            }
            
            private boolean isTableValidationRequired( XmlObject element, String attributeName ) {
                String elementTypeName = AdqlUtils.getLocalName( element ) ;
                if( AdqlData.METADATA_LINK_TABLE.containsKey( elementTypeName ) ) {
                    String[] attrNames = (String[])AdqlData.CROSS_VALIDATION.get( elementTypeName ) ;
                    for( int i=0; i<attrNames.length; i++ ) {
                        if( attrNames[i].equals( attributeName ) )
                            return true ;
                    }
                }
                return false ;
            }
            
            // As currently defined this is the deletion of the alias for a table.
            // We go through the query, changing every reference to this table back
            // to its full name rather than the alias.
            // eg: from a.ra to hipass.ra
            private void crossValidateTableDelete( XmlObject tableElement
                                                 , String attributeName
                                                 , String oldValue
                                                 , String newValue  ) {
                XmlCursor cursor = tableElement.newCursor() ;
                String tableName = ((SimpleValue)AdqlUtils.get( tableElement, "name" )).getStringValue() ;
                XmlObject xmlTableName = XmlString.Factory.newValue( tableName ) ;
                XmlObject element ;
                cursor.toStartDoc() ;
                cursor.toFirstChild() ; // There has to be a first child!
                do {
                    if( cursor.isStart() ) {
                        element = cursor.getObject() ;
                        // 
                        // If this is a column reference AND its table name is set to the old alias
                        // Then we set the table name to the original table name...
                        if( AdqlUtils.getLocalName( element ).equals( AdqlData.COLUMN_REFERENCE_TYPE ) 
                            &&
                            ((SimpleValue)AdqlUtils.get( element, "table" )).getStringValue().equals( oldValue ) ) {
                                AdqlUtils.set( element, "table", xmlTableName ) ;
                        }                       
                    }                 
                } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
                cursor.dispose() ;
                
                ((TableData)fromTables.get( tableName )).alias = null ;
            }
            
            
            private void crossValidateTableUpdate( XmlObject tableElement
                                                 , String attributeName
                                                 , String oldAliasValue
                                                 , String newAliasValue  ) {
                XmlCursor cursor = tableElement.newCursor() ;
                String tableName = ((SimpleValue)AdqlUtils.get( tableElement, "name" )).getStringValue() ;
                XmlObject xmlNewAlias = XmlString.Factory.newValue( newAliasValue ) ;
                XmlObject element ;
                cursor.toStartDoc() ;
                cursor.toFirstChild() ; // There has to be a first child!
                do {
                    if( cursor.isStart() ) {
                        element = cursor.getObject() ;
                        if( AdqlUtils.getLocalName( element ).equals( AdqlData.COLUMN_REFERENCE_TYPE ) 
                            &&
                            ((SimpleValue)AdqlUtils.get( element, "table" )).getStringValue().equals( oldAliasValue ) ) {
                            AdqlUtils.set( element, "table", xmlNewAlias ) ;
                        }                       
                    }                 
                } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
                cursor.dispose() ;
                ((TableData)fromTables.get( tableName )).alias = newAliasValue ;
            }
            
            
            private void crossValidateTableCreate( XmlObject tableElement
                                                 , String attributeName
                                                 , String oldAliasValue
                                                 , String newAliasValue  ) {
                XmlCursor cursor = tableElement.newCursor() ;
                String tableName = ((SimpleValue)AdqlUtils.get( tableElement, "name" )).getStringValue() ;
                XmlObject xmlAliasName = XmlString.Factory.newValue( newAliasValue ) ;
                XmlObject element ;
                cursor.toStartDoc() ;
                cursor.toFirstChild() ; // There has to be a first child!
                do {
                    if( cursor.isStart() ) {
                        element = cursor.getObject() ;
                        // 
                        // If this is a column reference AND its table name is the correct one
                        // Then we set the alias name ...
                        if( AdqlUtils.getLocalName( element ).equals( AdqlData.COLUMN_REFERENCE_TYPE ) 
                                &&
                            ((SimpleValue)AdqlUtils.get( element, "table" )).getStringValue().equals( tableName ) ) {
                            AdqlUtils.set( element, "table", xmlAliasName ) ;
                        }                       
                    }                 
                } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
                cursor.dispose() ;
                ((TableData)fromTables.get( tableName )).alias = newAliasValue ;
            }
            
            
            // Changing a table alias to one already in use will corrupt the query!
            private boolean preValidateTable( XmlObject tableElement
                                            , String attributeName
                                            , boolean attributeOptional
                                            , String oldAliasValue
                                            , String newAliasValue  ) {
                if( newAliasValue == null )
                    return true ;
                boolean retValue = true ;
                ArrayList tableList = new ArrayList(); 
                XmlObject xmlAliasName = null ;
                XmlObject element = null ;
                XmlCursor cursor = tableElement.newCursor() ;               
                cursor.toStartDoc() ;   // Reposition on the top of the document
                cursor.toFirstChild() ; // And then to the first child!
                do {
                    // Only deal with elements...
                    if( cursor.isStart() ) {
                        element = cursor.getObject() ;
                        // We make a collection of all the table elements in a query...
                        if( AdqlUtils.getLocalName( element ).equals( AdqlData.TABLE_TYPE ) ) {
                            tableList.add( element ) ;
                        }                       
                    }                 
                } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
                cursor.dispose() ;
                ListIterator iterator = tableList.listIterator() ;
                while( iterator.hasNext() ) {
                    element = (XmlObject)iterator.next() ;
                    if( element == tableElement )
                        continue ;
                    xmlAliasName = AdqlUtils.get( element, attributeName ) ;
                    if( xmlAliasName != null && ((SimpleValue)xmlAliasName).getStringValue().equals( newAliasValue ) ) {
                        retValue = false ;
                        break ;
                    }
                }              
                return retValue ;
            }
            
        } // end of class TupleTextEditor
        
    } // end of class AdqlTreeCellEditor
    
    public class TableData {
        
        public Catalog database ;
        public int tableIndex ;
        public String alias ;
        
        public TableData( Catalog database, int tableIndex, String alias ) {
            this.database = database ;
            this.tableIndex = tableIndex ;
            this.alias = alias ;
        }
        
        public TableData( Catalog database, TableBean table, String alias ) {
            this.database = database ;
            this.alias = alias ;
            final TableBean[] tables = database.getTables() ;
            for (int i = 0; i < tables.length; i++) {
                if( tables[i].getName().equals( table.getName() ) ) {
                    tableIndex = i ;
                    break ;
                }
            }  
        }
        
        public TableBean getTable() {
            return database.getTables()[ tableIndex ] ;
        }
        
        
        /**
         * @return Returns the alias.
         */
        public String getAlias() {
            return alias;
        }
        /**
         * @param alias The alias to set.
         */
        public void setAlias(String alias) {
            this.alias = alias;
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
            String newAlias = null ;
            int guard = 0 ;
            final int MAX = 1000 ;
            do {
                newAlias = _pop() ;
                guard++ ;
            } while ( this.isAlreadyInUse( newAlias ) == true && guard < MAX ) ;        
            return newAlias ;
        }
        
        public void push( String alias ) {
            autoStack.push( alias ) ;
        }
        
        private String _pop() {
            if( autoStack.empty() ) {
                suffix++ ;
                for( int i=ALIAS_NAMES.length-1; i>-1; i-- ) {
                    autoStack.push( Character.toString( ALIAS_NAMES[i] ) + Integer.toString( suffix ) ) ;
                }
            }
            return (String)autoStack.pop() ;
        }
        
        private boolean isAlreadyInUse( String newAlias ) {
            boolean retValue = false ;
            XmlObject element = null ;
            XmlObject xmlAlias = null ;
            XmlCursor cursor = getRoot().newCursor() ;               
            cursor.toStartDoc() ;   // Reposition on the top of the document
            cursor.toFirstChild() ; // And then to the first child!
            do {
                // Only deal with elements...
                if( cursor.isStart() ) {
                    element = cursor.getObject() ;
                    // We check each alias for having the same value as the proposed alias...
                    if( AdqlUtils.getLocalName( element ).equals( AdqlData.TABLE_TYPE ) ) {
                        xmlAlias = (XmlObject)AdqlUtils.get( element, "Alias" ) ;
                        if( xmlAlias != null 
                            && 
                            ((SimpleValue)xmlAlias).getStringValue().equals( newAlias ) ) {
                            retValue = true ;
                            break ;
                        }                           
                    }                       
                }                 
            } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
            cursor.dispose() ;
            return retValue ;
        }

    } // end of class AliasStack
    
    
} // end of class AdqlTree

