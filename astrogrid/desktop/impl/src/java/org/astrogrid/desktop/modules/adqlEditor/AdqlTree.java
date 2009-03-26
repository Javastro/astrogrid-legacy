package org.astrogrid.desktop.modules.adqlEditor ;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.CellEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.ToolTipManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.impl.values.XmlValueDisconnectedException ;
import org.astrogrid.acr.astrogrid.TableBean;
import org.astrogrid.acr.astrogrid.ColumnBean;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.*;
import org.astrogrid.adql.v1_0.beans.ColumnReferenceType;
import org.astrogrid.adql.v1_0.beans.SelectDocument;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.adql.v1_0.beans.TableType;
import org.astrogrid.adql.AdqlCompiler;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandFactory;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandInfo;
import org.astrogrid.desktop.modules.adqlEditor.commands.EditEnumeratedAttributeCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EditEnumeratedElementCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EditSingletonTextCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.EditTupleTextCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.CutCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.MultipleColumnInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.StandardInsertCommand;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec.Result;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
import org.astrogrid.desktop.modules.adqlEditor.nodes.NestingNode;
import org.astrogrid.desktop.modules.adqlEditor.nodes.NodeFactory;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
/**
 * A tree view on XML, with nodes representing both elements and attributes. See
 * {@link XmlEntry}and {@link XmlModel}for information on how information
 * about the underlying XML is retrieved for use in the tree. Those classes use
 * XMLBeans to provide a wrapper by which the tree exposes the underlying XML.
 */
public final class AdqlTree extends JTree
{
    private static final Log log = LogFactory.getLog( AdqlTree.class ) ;
    private static final String EDIT_PROMPT_NAME = "Ctrl+Space" ;
    private static final String POPUP_PROMPT_NAME = "Ctrl+M/m" ;
    private static final char[] ALIAS_NAMES = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    
    private Registry registry ;
    private URI toolIvorn ;
    private AliasStack aliasStack ;
    
    private CatalogService catalogueService ;
    private String title ;
    
    private HashMap fromTables = new HashMap() ;
    private HashMap fromTablesUsingAlias = new HashMap() ;
    
    private int availableWidth ;
    private CommandFactory commandFactory ;
    private NodeFactory nodeFactory ;
    private String adqlSchemaVersion ;
    private final ChangeEvent changeEvent = new ChangeEvent( this ) ;
    protected final UIComponent parent;
      
    public AdqlTree( UIComponent parent, File xmlFile, Registry registry, URI toolIvorn, URI tabulaData ) {
        this.parent = parent ;
        initialize( parseXml( xmlFile ), registry, toolIvorn, tabulaData ) ;
    }
     
    public AdqlTree( UIComponent parent, InputStream xmlStream, Registry registry, URI toolIvorn, URI tabulaData ) {
        this.parent = parent ;
        initialize( parseXml( xmlStream ), registry, toolIvorn, tabulaData ) ;
    }
    
    public AdqlTree( UIComponent parent, Registry registry, URI toolIvorn, URI tabulaData ) {
        this.parent = parent ;
        initialize( parseXml( AdqlData.NEW_QUERY ), registry, toolIvorn, tabulaData ) ;
    }
    
    public AdqlTree( UIComponent parent, String xmlString, Registry registry, URI toolIvorn, URI tabulaData ) {
        this.parent = parent ;
        initialize( parseXml( xmlString ), registry, toolIvorn, tabulaData ) ;
    }
    
    public AdqlTree( UIComponent parent, org.w3c.dom.Node node, Registry registry, URI toolIvorn, URI tabulaData ) {
        this.parent = parent ;
        initialize( parseXml( node ), registry, toolIvorn, tabulaData ) ;
    }
    
    public void setTree( AdqlNode rootEntry, Registry registry, URI toolIvorn, URI tabulaData ) {
        this.initialize( rootEntry, registry, toolIvorn, tabulaData ) ;
    }
    
    public AdqlTreeCellRenderer getTreeCellRenderer() {
        return (AdqlTreeCellRenderer)super.getCellRenderer() ;
    }
 
    public NodeFactory getNodeFactory() {
        if( nodeFactory == null ) {
            nodeFactory = new NodeFactory( this ) ;
        }
        return nodeFactory ;
    }

    /**
     * set the selection to the top select node - nb, not the root node.
     *             // (The root node contains the SelectDocumentType, not the SelectType!!!)
     */
    public void setSelectionToTopSelectNode() {
            AdqlNode root = (AdqlNode)getModel().getRoot() ;
            Object[] childArray = root.getChildren() ;
            for( int i=0; i<childArray.length; i++ ) {
                XmlObject xo = ((AdqlNode)childArray[i]).getXmlObject() ;
                if( xo.schemaType() == SelectType.type ) {
                    TreeNode[] nodes = ((AdqlNode)childArray[i]).getPath() ;
                    TreePath rootSelectPath =  new TreePath( nodes );
                    setSelectionPath( rootSelectPath) ;
                    break ;
                }
            }
       
    }
    
    @Override
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
    
    
    
    @Override
    public String convertValueToText( Object value
                                    , boolean sel
                                    , boolean expanded
                                    , boolean leaf
                                    , int row
                                    , boolean hasFocus) {
        if( value != null && value instanceof AdqlNode ) {
            
            String html = null ;
            try {
                AdqlNode node = (AdqlNode)value ;
                if( log.isDebugEnabled() && node.getParent() instanceof NestingNode ) {
                    log.debug( "Index within parent: " + node.getParent().getIndex( node ) ) ;
                }
                html = node.toHtml( expanded, leaf, this ) ;
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
            catch( Exception ex ) {
                if( log.isDebugEnabled() ) {
                   debugConvertValueToText( value, sel, expanded, leaf, row, hasFocus, ex ) ;
                }               
                value = "ConvertValueToText error" ;
            }
        }
        return super.convertValueToText( value, sel, expanded, leaf, row, hasFocus ) ;
    }
    
    private void debugConvertValueToText( Object value
                                        , boolean sel
                                        , boolean expanded
                                        , boolean leaf
                                        , int row
                                        , boolean hasFocus
                                        , Exception ex ) {
        
        StringBuffer buffer = new StringBuffer() ;
        buffer
          .append( "debugConvertValueToText(): ") 
          .append( "\nsel: " + sel ) 
          .append( "\nexpanded: " + expanded ) 
          .append( "\nleaf: " + leaf ) 
          .append( "\nrow: " + row ) 
          .append( "\nhasFocus: " + hasFocus ) ;    
                
        AdqlNode node = (AdqlNode)value ;
        String sValue = null ;
        try {
            sValue = node.toString() ;
            buffer.append( "\nvalue: " + sValue ) ;
        }
        catch ( XmlValueDisconnectedException xvdex ) {
            buffer.append( "\nXmlValueDisconnectedException thrown when " +
                           "accessing the value for debug purposes..." ) ;
            CommandFactory.EditStore editStore = this.commandFactory.getEditStore() ;
            Integer token = editStore.get( node ) ;
            if( token == null ) {
                buffer.append( "\nToken for node not present in EditStore" ) ;
            }
            else {
                buffer.append( "\nEditStore token for node: " + token ) ;
            }
            buffer.append( "Node type: " + node.getShortTypeName() ) ;
        } 
        buffer.append( "\nOriginal instigating exception was: " + ex.getClass() ) ;
        buffer.append( "\nLocalized message: " + ex.getLocalizedMessage() ) ;
        log.debug( buffer ) ;
    }
    
    public AdqlNode parseXml(File xmlFile)
    {
        AdqlNode entry = null ;
        try {
            entry = getNodeFactory().newInstance( SelectDocument.Factory.parse( xmlFile ) ) ;
        }
        catch( Exception ex ) {
            log.warn( "AdqlTree incorrectly initialized.", ex ) ;
        }
        return entry ;
    }
    
    public AdqlNode parseXml( InputStream xmlStream )
    {
        AdqlNode entry = null ;
        try {
            entry = getNodeFactory().newInstance( SelectDocument.Factory.parse( xmlStream ) ) ;
        } catch( Exception ex ) {
            log.warn( "AdqlTree incorrectly initialized.", ex ) ;
        }
        return entry ;
    }
    
    
    public AdqlNode parseXml( String xmlString )
    {
        AdqlNode entry = null ;
        try {
            entry = getNodeFactory().newInstance( SelectDocument.Factory.parse( xmlString ) ) ;
        } catch( Exception ex ) {
            log.warn( "AdqlTree incorrectly initialized.", ex ) ;
        }
        return entry ;
    }
    
    public AdqlNode parseXml( org.w3c.dom.Node xmlNode )
    {
        AdqlNode entry = null ;
        try {
            entry = getNodeFactory().newInstance( SelectDocument.Factory.parse( xmlNode ) ) ;
        } catch( Exception ex ) {
            log.warn( "AdqlTree incorrectly initialized.", ex ) ;
        }
        return entry ;
    }
    

    /**
     * Sets up the components that make up this tree.
     * 
     */
    
    private void initialize( AdqlNode rootEntry, Registry registry, URI toolIvorn, URI tabulaData ) {
        this.commandFactory = new CommandFactory( this ) ;
        getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION ) ;
        this.registry = registry ;
        this.toolIvorn = toolIvorn ;
        retrieveAdqlSchemaVersion() ;
        this.resetCatalogueData( tabulaData ) ;
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
      
        ToolTipManager.sharedInstance().registerComponent( this ) ;
    }
    
    protected KeyStroke getMicroEditAccelerator() {
        return KeyStroke.getKeyStroke( KeyEvent.VK_SPACE, InputEvent.CTRL_MASK ) ;
    }
    

    private synchronized void resetCatalogueData( final URI tabulaData ) {
        
        log.debug( tabulaData ) ;
        
        (new BackgroundWorker( parent, "Fetching Catalogue Data" ,BackgroundWorker.LONG_TIMEOUT,Thread.MAX_PRIORITY) {
            @Override
            protected Object construct() throws Exception {
                CatalogService cs = null ;
                if( tabulaData != null ) {
                        Resource res = registry.getResource( tabulaData );
                        cs = (CatalogService)res ;
                }
                return cs ;                      
            }
            @Override
            protected void doFinished( Object result ) {
                catalogueService = (CatalogService)result ;
                if( catalogueService != null ){
                    title = catalogueService.getShortName() ;
                    if( title == null ) {
                        title = catalogueService.getTitle() ;
                    }
                    reestablishTablesCollection() ;
                    fireStateChanged() ;
                }
            }
            @Override
            protected void doError(Throwable ex) {
                parent.showTransientWarning("Failed to find catalogue data",ExceptionFormatter.formatException(ex));
                if( log.isDebugEnabled() ) {
                    log.debug( "Worker thread failed searching for catalogue data: "
                             + tabulaData, ex )  ;    
                }
            }
            
            
        }).start();
     
    }
    
    private void retrieveAdqlSchemaVersion() {
        adqlSchemaVersion = AdqlCompiler.ReadProcessingInstruction( (SelectDocument)getRoot()
                                                                  , AdqlData.PI_ADQL_SCHEMA_VERSION_TAG ) ;
        if( adqlSchemaVersion == null ) {
            AdqlCompiler.WriteProcessingInstruction( (SelectDocument)getRoot()
                                                   , AdqlData.PI_ADQL_SCHEMA_VERSION_TAG 
                                                   , AdqlData.PI_ADQL_SCHEMA_VERSION_VALUE ) ;
        }
        // OK. This is a placeholder for future processing, when some action will be
        // required to accommodate changes in ADQL between versions.
    }
    
    
    protected void reestablishTablesCollection() {
        XmlString xTableName = null ;
        XmlString xAlias = null ;
        String alias = null ;
        //
        // Loop through the whole of the query looking for table types....
        XmlCursor cursor = getRoot().newCursor() ;
        try {
            while( !cursor.toNextToken().isNone() ) {
                if( cursor.isStart()  
                    && 
                    ( cursor.getObject().schemaType().getName().getLocalPart().equals( AdqlData.TABLE_TYPE ) 
                      ||
                      cursor.getObject().schemaType().getName().getLocalPart().equals( AdqlData.ARCHIVE_TABLE_TYPE ) )                       
                ) {
                    xTableName = (XmlString)AdqlUtils.get( cursor.getObject(), "name" ) ;
                    String tableName = xTableName.getStringValue() ;
                    //
                    // Ignore the dummy table...
                    // (Should be deleted in any case from the query on the first column or table insert)
                    if( tableName.equals( AdqlData.DUMMY_TABLE_NAME ) ) {
                        continue ;
                    }
                    xAlias = (XmlString)AdqlUtils.get( cursor.getObject(), "alias" ) ;
                    alias = (xAlias == null ? null : xAlias.getStringValue())  ; 
                    if( log.isDebugEnabled() ) {
                        log.debug( "table name: " + xTableName.getStringValue() 
                                 + " with alias: "
                                 + (xAlias==null ? "null" : xAlias.getStringValue()) ) ;
                    }  
                    if( alias != null ) {
                        TableData td = (TableData)fromTables.get( tableName ) ;
                        if( td != null ) {
                            td.addAlias( alias ) ;
                        }
                        else {
                            fromTables.put( tableName
                                          , new TableData( findTableBean( tableName ), alias ) ) ;
                        }
                    } 
                }
            } // end while
        }
        finally {
            cursor.dispose();
        }
 
    }
    

    public TableBean findTableBean( String tableName ) {
        TableBean[] tables = catalogueService.getTables() ;
        for( int i=0; i<tables.length; i++ ) {
            if( tables[i].getName().equalsIgnoreCase( tableName ) ) {
                return tables[i] ;
            }
        }
        return null ;
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
        return nodeFactory.getRootEntry().getXmlObject() ;
    }
    
    public AdqlNode getRootNode() {
        return nodeFactory.getRootEntry() ;
    }
    

    private String extractAttributeValue( XmlObject xmlObject ) {
        String retVal = null ;
        XmlObject intermediateObject ;         
        String[] attributeName = AdqlUtils.getEditableAttributes( xmlObject ) ;
        intermediateObject = AdqlUtils.get( xmlObject, attributeName[0] ) ;
        if( intermediateObject != null )
            retVal = ((SimpleValue)(intermediateObject)).getStringValue() ;
        return retVal ;
    }
    
    
    public TableData getTableData( String tableName ) {
        TableData tableData = (TableData)fromTables.get( tableName ) ;
        return tableData ;
    }
    
    public CatalogService getCatalogService() {
        return catalogueService;
    }
    
    public boolean isCatalogServiceSet() {
        return catalogueService != null ;
    }
    
    public HashMap getFromTables() {
        return fromTables;
    }
    
    public String popAliasStack() {
    	if( aliasStack == null ) {
    		aliasStack = new AliasStack() ;
    	}
        return aliasStack.pop() ;
    }
    
    public void pushAliasStack( String alias ) {
    	if( aliasStack == null ) {
    		aliasStack = new AliasStack() ;
    	}
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
                stopEditing() ;            
                TreePath path = getSelectionPath() ;             
                if( path != null ) {
                    AdqlNode selectedEntry = (AdqlNode)getLastSelectedPathComponent();
                    if( selectedEntry.isBottomLeafEditable() ) {
                        startEditingAtPath( path ) ;
                    }
                }        
        }
        
    } // end of class EditPromptAction
    
    
    public class AdqlTreeCellRenderer extends DefaultTreeCellRenderer {
              
        private volatile int useableWidth ;
        
        public AdqlTreeCellRenderer() {
            setIcon( null ) ;
            setLeafIcon( null ) ;
            setOpenIcon( null ) ;
            setClosedIcon( null ) ;
            setHorizontalAlignment( LEFT ) ;
            setVerticalAlignment( TOP ) ;
            setBorder( BorderFactory.createEtchedBorder() ) ;
        }
        
        @Override
        public void setIcon( javax.swing.Icon icon ) { }
               
        @Override
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
            
        @Override
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
        EnumeratedAttributeEditor enumeratedAttributeEditor ;
        EnumeratedElementEditor enumeratedElementEditor ;
        SingletonTextEditor singletonTextEditor ;
        TupleTextEditor tupleTextEditor ;
        CellEditor currentEditor ;
        AdqlTreeCellRenderer renderer ;
        
        public AdqlTreeCellEditor( AdqlTreeCellRenderer renderer ) {
            this.renderer = renderer ;
            enumeratedAttributeEditor = new EnumeratedAttributeEditor() ;
            enumeratedAttributeEditor.setFont( AdqlTree.this.getFont() ) ;
            enumeratedAttributeEditor.addActionListener( this ) ;
            enumeratedElementEditor = new EnumeratedElementEditor() ;
            enumeratedElementEditor.setFont( AdqlTree.this.getFont() ) ;
            enumeratedElementEditor.addActionListener( this ) ;
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
            if( AdqlUtils.isDrivenByEnumeratedAttribute( adqlNode.getSchemaType() ) ) {
                currentEditor = enumeratedAttributeEditor ;
                enumeratedAttributeEditor.setComboValue() ;
            }
            else if( AdqlUtils.isDrivenByEnumeratedElement( adqlNode.getSchemaType() ) ) {
                currentEditor = enumeratedElementEditor ;
                enumeratedElementEditor.setComboValue() ;
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
            return currentEditor.getCellEditorValue() ; 
        }
        public boolean isCellEditable( EventObject anEvent ) {
            if( anEvent instanceof MouseEvent ) { 
                return false ;
            }
            return true ;
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
            String[] names = AdqlUtils.getEditableAttributes( entry.getXmlObject() ) ;
            return ( names != null && names.length > 1 ) ;
        }
        
        
        
        // 
        // NB: See note to AbstractEnumeratedEditor.
        public class EnumeratedAttributeEditor extends AbstractEnumeratedEditor {

            EditEnumeratedAttributeCommand command = null ;
                    
            public EnumeratedAttributeEditor() {
                super() ;
            }
            
            @Override
            public void setComboValue() {              
                ((DefaultComboBoxModel)getModel()).removeAllElements() ;
                command = getCommandFactory().newEditEnumeratedAttributeCommand(AdqlTree.this, adqlNode ) ;
                String enumeratedValues[] = command.getEnumeratedValues() ; 
                String currentValue = command.getOldValue() ;
                for( int i=0; i<enumeratedValues.length; i++ ) {
                    addItem( enumeratedValues[i] ) ;
                    if( currentValue.equals( enumeratedValues[i] ) ) {
                        setSelectedIndex( i ) ;
                    }
                }               
            }
            
            @Override
            protected void setEnumeratedValue( String value ) {
                command.setNewValue( value ) ;
                Result result = command.execute();
                if( result != CommandExec.OK ) {
                    log.warn( "Failure to set attribute value." ) ;
                }
  
            }                
        } // end of class EnumeratedAttributeEditor
        
        
        public class SingletonTextEditor extends JTextField implements CellEditor {
            
            EditSingletonTextCommand command = null ;
            
            public SingletonTextEditor() {
                super() ;
            }
            
            public void addCellEditorListener(CellEditorListener l) {
                AdqlTreeCellEditor.this.addCellEditorListener( l ) ;
            }
            public void cancelCellEditing() {}
            
            public Object getCellEditorValue() {
                setElementValue( getText() ) ;
                return command.getChildObject() ;
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
                command = getCommandFactory().newEditSingletonTextCommand( AdqlTree.this, adqlNode ) ;
                setText( command.getOldValue() ) ;
            }
            
            @Override
            public void setBounds( Rectangle rectangle ) {
                super.setBounds( AdqlTreeCellEditor.this.setBounds( rectangle ) ) ;
            }
            
            @Override
            public void setBounds( int x, int y, int w, int h ) {
                Rectangle r = AdqlTreeCellEditor.this.setBounds( x, y, w, h ) ;
                super.setBounds( r.x, r.y, r.width, r.height ) ;
            }
            
            private void setElementValue( String value ) {
                command.setNewValue( value ) ;
                CommandExec.Result result = command.execute() ;
                if( result != CommandExec.OK ) {           
                    log.warn( "Failure to set element value. " ) ;
                }              
            }
            
        }
        
        public class TupleTextEditor extends Box implements CellEditor {
            
            private EditTupleTextCommand command = null ;
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
                return command.getChildObject() ;
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
                command = getCommandFactory().newEditTupleTextCommand( AdqlTree.this, adqlNode, fromTables ) ;
                String values[] = command.getOldValues() ;
                for( int i=0; i<values.length; i++ ) {
                    if( values[i] == null ) {
                        fields[i].setText( "" ) ;
                    }
                    else {
                        fields[i].setText( values[i] ) ;
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
            
            private void setElementValue() {
                //
                // We need old values purely for the size...
                String[] values = new String[ command.getOldValues().length ] ;
                for( int i=0; i<values.length; i++ ) {
                    values[i] = getNewValue( fields[i] ) ;
                }
                command.setNewValues( values ) ;
                CommandExec.Result result = command.execute() ;
                if( result != CommandExec.OK ) {           
                    log.warn( "Failure to set element value. " ) ;
                }              
            } // end of setElementValue()
            
            private String getNewValue( JTextField field ) {
                String newValue = field.getText() ;
                if( newValue != null ) {
                    newValue = newValue.trim() ;
                    if( newValue.length() == 0 ) {
                        newValue = null ;
                    }
                    else {
                        // This is a short-term measure to deal with
                        // quoted identifiers. In fact eliminating any
                        // possibility at this level. This needs to 
                        // be eliminated when we allow delimited identifiers
                        // within the xml!!!
                        if( newValue.startsWith( "\"" ) ) {
                            newValue = newValue.substring( 1 ) ;
                        }
                        if( newValue.endsWith( "\"" ) ) {
                            newValue = newValue.substring( 0, newValue.length()-1 ) ;
                        }
                        // This is a little bit of fail safe craziness!
                        if( newValue.indexOf( '\"' ) > 0 ) {
                            StringBuffer buffer = new StringBuffer() ;
                            String[] tempValues = newValue.split( "\"" ) ;
                            if( tempValues != null && tempValues.length > 0 ) {
                                for( int i=0; i<tempValues.length; i++ ) {
                                    buffer.append( tempValues[i] ) ;
                                }
                                newValue = buffer.toString() ;
                            }                         
                        }
                    }                   
                }
                return newValue ;
            }
            
        } // end of class TupleTextEditor


        //
        // NB: The classes AbstractEnumeratedEditor, EnumeratedAttributeEditor and EnumeratedElementEditor
        // were produced to see whether I could capitalize on commonality. As it turns out, enumerated
        // attributes and enumerated elements are to all intents and purposes identical, much to my surprize.
        // For the moment, I'm keeping them rather than collapsing them all into one class, just in case
        // developments show up the need.
        //
        public abstract class AbstractEnumeratedEditor extends JComboBox implements CellEditor {
                    
            public AbstractEnumeratedEditor() {
                super() ;
           //     setKeySelectionManager( new EnumeratedEditor.KeySelectionManager() ) ;
                setPrototypeDisplayValue( "mmmmmmmmmmmmmmmm"  ) ;
                setEditable( true ) ; 
            }
            
            public void addCellEditorListener( CellEditorListener l ) {
                AdqlTreeCellEditor.this.addCellEditorListener( l ) ;
            }
            public void cancelCellEditing() {}
            
            public Object getCellEditorValue() {
                setEnumeratedValue( (String)getSelectedItem() ) ;
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
            
            public abstract void setComboValue() ;
            
            public void setBounds( Rectangle rectangle ) {
                super.setBounds( AdqlTreeCellEditor.this.setBounds( rectangle ) ) ;
            }
            
            public void setBounds( int x, int y, int w, int h ) {
                Rectangle r = AdqlTreeCellEditor.this.setBounds( x, y, w, h ) ;
                super.setBounds( r.x, r.y, r.width, r.height ) ;
            }
            
            protected abstract void setEnumeratedValue( String value ) ;
        
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
                
        } // end of class AbstractEnumeratedEditor


        // 
        // NB: See note to AbstractEnumeratedEditor.
        public class EnumeratedElementEditor extends AbstractEnumeratedEditor {
            
            EditEnumeratedElementCommand command ;
    
            public EnumeratedElementEditor() {
                super() ;
            }
                
            public void setComboValue() {              
                ((DefaultComboBoxModel)getModel()).removeAllElements() ;
                command = getCommandFactory().newEditEnumeratedElementCommand( AdqlTree.this, adqlNode ) ;
                String[] enumeratedValues = command.getEnumeratedValues() ;                           
                String currentValue = command.getOldValue() ;
                for( int i=0; i<enumeratedValues.length; i++ ) {
                    addItem( enumeratedValues[i] ) ;
                    if( currentValue.equals( enumeratedValues[i] ) ) {
                        setSelectedIndex( i ) ;
                    }
                }            
            }
            
            protected void setEnumeratedValue( String value ) {
                command.setNewValue( value ) ;
                Result result = command.execute();
                if( result != CommandExec.OK ) {
                    log.warn( "Failure to set element value:" ) ; ;
                }
            }
            
        } // end of class EnumeratedElementEditor
        
    } // end of class AdqlTreeCellEditor
    
    public class TableData {
        
        public TableBean table ;
//        public String alias ;
        public ArrayList aliases = new ArrayList() ;
        
        public TableData( TableBean table, String alias ) {
            this.table = table ;
//            this.alias = alias ; 
            this.aliases.add( alias ) ;
        }
        
        public TableBean getTable() {
            return table ;
        }
        
        
        /**
         * @return Returns the alias.
         */
//        public String getAlias() {
//            return alias;
//        }
//        /**
//         * @param alias The alias to set.
//         */
//        public void setAlias(String alias) {
//            this.alias = alias;
//        }
        
        public void addAlias( String alias ) {
            this.aliases.add( alias ) ;
        }
        
        public boolean removeAlias( String alias ) {
            return this.aliases.remove( alias ) ;
        }
        
        public String[] getAliases() {
            return (String[])aliases.toArray( new String[aliases.size() ] ) ;
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
    
    
    public void addChangeListener( ChangeListener chl ) {
        listenerList.add( ChangeListener.class, chl ) ;
    }

    public void removeChangeListener( ChangeListener chl ) {
        listenerList.remove( ChangeListener.class, chl ) ;
    }

    public ChangeListener[] getChangeListeners() {
        return ( ChangeListener[] )listenerList.getListeners( ChangeListener.class ) ;
    }

    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for( int i=listeners.length-2; i>=0; i-=2) {
            if( listeners[i] == ChangeListener.class ) {
                ((ChangeListener)listeners[i+1]).stateChanged( changeEvent ) ;
            }          
        }
    }
    
    protected void ensureSomeNodeSelected( CommandInfo ci ) {
        ensureSomeNodeSelected( ci, false ) ;       
    }
    
    protected void ensureSomeNodeSelected( CommandInfo ci, boolean bUndo ) {
      
        if( this.isSelectionEmpty() ) {
            
            TreeNode[] nodes = null ;
            
            if( ci instanceof CutCommand ) {
                if( bUndo ) {
                    nodes = ci.getChildEntry().getPath() ;
                }
                else {
                    nodes = ci.getParentEntry().getPath();
                }                
            }
            else if( ci instanceof StandardInsertCommand 
                     ||
                     ci instanceof MultipleColumnInsertCommand ) {
                if( bUndo ) {
                    nodes = ci.getParentEntry().getPath();
                }
                else {
                    nodes = ci.getChildEntry().getPath() ;
                }               
            }
            else {
                nodes = ci.getChildEntry().getPath() ;
            }
            this.setSelectionPath( new TreePath( nodes ) ) ;
         
        } // end if

    } // end of ensureSomeNodeSelected()
    
    /* (non-Javadoc)
     * @see javax.swing.JTree#getToolTipText(java.awt.event.MouseEvent)
     */
    public String getToolTipText(MouseEvent event) {
        if( event == null )
            return null ;
        TreePath path = this.getPathForLocation( event.getX(), event.getY() ) ;
        if( path == null )
            return null ;
        AdqlNode node = (AdqlNode)path.getLastPathComponent() ;
        XmlObject xo = node.getXmlObject() ;
        SchemaType type = xo.schemaType() ;
        if( type == SelectType.type ) {
            SelectType st = (SelectType)xo ;
            if( st.isSetStartComment() ) {
                return "<HTML>" + st.getStartComment().replaceAll( "\n", "<BR>" ) ;
            }
        } 
        else if( type == ColumnReferenceType.type ) {
            ColumnReferenceType crt = (ColumnReferenceType)xo ;
            TableData td = (TableData)getFromTablesUsingAlias().get( crt.getTable() ) ;
            if( td == null )
                return null ;
            ColumnBean[] cbs = td.getTable().getColumns() ;
            for( int i=0; i<cbs.length; i++ ) {
                if( cbs[i].getName().equalsIgnoreCase( crt.getName() ) ) {
                    return "<HTML>" + cbs[i].getDescription().replaceAll( "\n", "<BR>" ) ;
                }
            }           
        }
        else if( type == TableType.type ) {
            TableType tt = (TableType)xo ;
            TableData td = (TableData)fromTables.get( tt.getName() ) ;
            if( td == null )
                return null ;
            return  "<HTML>" + td.getTable().getDescription().replaceAll( "\n", "<BR>" ) ;
        }
        return null ;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    //
    // This method is relatively inefficient,
    // although there should not be that many tables in a query
    // (even for LEDAS)...
    private HashMap getFromTablesUsingAlias() {
        fromTablesUsingAlias.clear() ;
        Iterator it = fromTables.values().iterator() ;
        while( it.hasNext() ) {
            TableData td = (TableData)it.next() ;
            fromTablesUsingAlias.put( td.getTable().getName(), td) ;
            String[] aliases = td.getAliases() ;
            for( int i=0; i<aliases.length; i++ ) {
                fromTablesUsingAlias.put( aliases[i], td ) ;
            }               
        }

        return fromTablesUsingAlias ;
    }
    
} // end of class AdqlTree

