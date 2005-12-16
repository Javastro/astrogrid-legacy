package org.astrogrid.desktop.modules.adqlEditor ;

import org.apache.xerces.impl.dtd.XMLSimpleType;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.* ;

import java.util.Hashtable;

//import net.ivoa.xml.adql.v10.*;
import org.astrogrid.adql.v1_0.beans.* ;

import java.awt.Rectangle;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.JLabel ;
import javax.swing.JPanel;
import javax.swing.Action ;
import java.awt.GridBagLayout;
import javax.swing.KeyStroke ;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.DefaultCellEditor; 
import javax.swing.JTextField;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JComponent ; 
import javax.swing.tree.DefaultTreeModel;
import javax.swing.AbstractAction;
import javax.swing.JTextArea ;
import javax.swing.BorderFactory; 
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent ;

import java.awt.Insets;
import java.util.Vector ;
import java.awt.Component;
import java.awt.Color; 
import java.awt.event.ActionEvent ;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent ;
import java.awt.event.ActionListener;
import java.awt.Dimension ;
import java.awt.GridBagConstraints;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EventObject;
import java.io.InputStream;
/**
 * A tree view on XML, with nodes representing both elements and attributes. See
 * {@link XmlEntry}and {@link XmlModel}for information on how information
 * about the underlying XML is retrieved for use in the tree. Those classes use
 * XMLBeans to provide a wrapper by which the tree exposes the underlying XML.
 */
public final class AdqlTree extends JTree
{
    
    private static final String EDIT_PROMPT_NAME = "Ctrl+Space" ;
    private static final String POPUP_PROMPT_NAME = "Ctrl+M/m" ;
    
    private boolean editingActive = false ;
    private LowLevelEditor llEditor ;
    
    //  Not absolutely sure this field is thread safe
    private Rectangle treeVisibleRect ;
    
    /**
     * Constructs the tree using <em>xmlFile</em> as an original source of
     * nodes.
     * 
     * @param xmlFile The XML file the new tree should represent.
     */
    public AdqlTree( File xmlFile ) {
        initComponents( AdqlTree.parseXml( xmlFile ) ) ;
    }
    
    
    public AdqlTree( InputStream xmlStream ) {
        initComponents( AdqlTree.parseXml( xmlStream ) ) ;
    }
    
    public AdqlTree() {
        initComponents( AdqlTree.parseXml( AdqlData.NEW_QUERY ) ) ;
    }
    
    public AdqlTree( String xmlString ) {
        initComponents( AdqlTree.parseXml( xmlString ) ) ;
    }
    
    public AdqlTree( org.w3c.dom.Node node ) {
        initComponents( AdqlTree.parseXml( node ) ) ;
    }
    
    public void setTree( AdqlEntry rootEntry ) {
        this.initComponents( rootEntry ) ;
    }
    
    public AdqlTreeCellRenderer getTreeCellRenderer() {
        return (AdqlTreeCellRenderer)super.getCellRenderer() ;
    }
 

    public boolean isPathEditable( TreePath path ) {
        boolean result = false ;
        if( path != null && path.getPathCount() > 1) {
            AdqlEntry entry = (AdqlEntry)path.getLastPathComponent() ;
            result = entry.isBottomLeafEditable() ;
        }
        return result ;
    }
    
    
    public String convertValueToText( Object value
                                    , boolean sel
                                    , boolean expanded
                                    , boolean leaf
                                    , int row
                                    , boolean hasFocus) {
        if( value != null && value instanceof AdqlEntry ) {
            String html = ((AdqlEntry)value).toHtml( expanded, leaf, this ) ;
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
    public static AdqlEntry parseXml(File xmlFile)
    {
        AdqlEntry entry = null ;
        try {
            entry = AdqlEntry.newInstance( SelectDocument.Factory.parse( xmlFile ) ) ;
        } catch (XmlException xmle) {
            System.err.println(xmle.toString());
        } catch (IOException ioe) {
            System.err.println(ioe.toString());
        }
        return entry ;
    }
    
    public static AdqlEntry parseXml( InputStream xmlStream )
    {
        AdqlEntry entry = null ;
        try {
            entry = AdqlEntry.newInstance( SelectDocument.Factory.parse( xmlStream ) ) ;
        } catch (XmlException xmle) {
            System.err.println(xmle.toString());
        } catch (IOException ioe) {
            System.err.println(ioe.toString());
        }
        return entry ;
    }
    
    
    public static AdqlEntry parseXml( String xmlString )
    {
        AdqlEntry entry = null ;
        try {
            entry = AdqlEntry.newInstance( SelectDocument.Factory.parse( xmlString ) ) ;
        } catch (XmlException xmle) {
            System.err.println(xmle.toString());
        }
        return entry ;
    }
    
    public static AdqlEntry parseXml( org.w3c.dom.Node xmlNode )
    {
        AdqlEntry entry = null ;
        try {
            entry = AdqlEntry.newInstance( SelectDocument.Factory.parse( xmlNode ) ) ;
        } catch (XmlException xmle) {
            System.err.println(xmle.toString());
        }
        return entry ;
    }
    

    /**
     * Sets up the components that make up this tree.
     * 
     */
    private void initComponents( AdqlEntry rootEntry ) {
   
        setModel( new DefaultTreeModel( rootEntry ) );
        getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION ) ;
        // Uncomment these lines to provide your own GIF files for
        // tree icons.
        //        renderer.setLeafIcon(createImageIcon("images/leaf.gif"));
        //        renderer.setOpenIcon(createImageIcon("images/minus.gif"));
        //        renderer.setClosedIcon(createImageIcon("images/plus.gif"));
        // DefaultTreeCellEditor editor = new DefaultTreeCellEditor( this, renderer ) ;
        DefaultTreeCellRenderer renderer = new AdqlTreeCellRenderer() ;
        setCellRenderer( renderer ) ;
        this.llEditor = new LowLevelEditor() ;
        setCellEditor( new AdqlTreeCellEditor2( this, renderer, new DefaultCellEditor( this.llEditor ) ) ) ;
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
    
 
    public class AdqlTreeCellRenderer extends DefaultTreeCellRenderer {
        
        
        
        private boolean expanded ;
        private boolean useOppositeExpansionState = false ;
        
        public AdqlTreeCellRenderer() {
            super.setIcon( null ) ;
            super.setLeafIcon( null ) ;
            super.setOpenIcon( null ) ;
            super.setClosedIcon( null ) ;
            AdqlTree.this.addComponentListener ( 
                new ComponentAdapter() {
                    public void componentResized( ComponentEvent e ) {
                        treeVisibleRect = AdqlTree.this.getVisibleRect() ;
                    }
                } 
            ) ;
        }
        
        public void setIcon( javax.swing.Icon icon ) { }
        
        
        public Component getTreeCellRendererComponent( JTree tree
                                                     , Object value
                                                     , boolean sel
                                                     , boolean expanded
                                                     , boolean leaf
                                                     , int row
                                                     , boolean hasFocus) {
            
            AdqlEntry entry = (AdqlEntry)value ;
            int level = ((AdqlEntry)value).getLevel() ;           
            int displacement = ( level + 1 ) * 50 ;  // rough and ready; guess for the moment.
            treeVisibleRect = AdqlTree.this.getVisibleRect() ;
            int width = treeVisibleRect.width - displacement ;
            // We cannot let things get rediculously small ...
            if( width < 250 )
                width = 250 ;
            entry.setUseableWidth( width ) ;
            return (JLabel)super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus) ;
        }
        
        public void setUseOppositeExpansionState() {
            useOppositeExpansionState = true ;
        }
        
        public Dimension getPreferredSize() {
//            int displacement = level * 20 ;  // rough and ready guess for the moment.
//            int width = treeVisibleRect.width - displacement ;
//            if( width < 250 )
//                width = 250 ;
            Dimension d = super.getPreferredSize() ;
//            if( !expanded )
//                d.height = d.height * 2 ;         
            return d ;
        }
       
    } // end of class AdqlTreeCellRenderer
    

    
    public class AdqlTreeCellEditor extends JTextArea implements TreeCellEditor {
        
        java.util.Vector listeners = new Vector() ;
        Object value ;
        JTree tree ;
        
        public AdqlTreeCellEditor() {
        }
        
        
        public Component getTreeCellEditorComponent( JTree tree
                								   , Object value
                								   , boolean sel
                								   , boolean expanded
                								   , boolean leaf
                								   , int row ) {
//            System.out.println( "AdqlTreeCellEditor.getTreeCellEditorComponent" ) ;
//            System.out.println( "value.getClass().getName(): " + value.getClass().getName() ) ;
            this.value = value ;
            setLineWrap( true ) ;
            setWrapStyleWord( true ) ;
            this.setBorder( BorderFactory.createLineBorder( Color.black, 1 ) ) ;
            this.tree = tree ;
            if( value != null )
                ; // setText( ((AdqlEntry)value).toText( expanded, AdqlTree.this ) ) ;
            return this ;
        }
        
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize() ;          
            if( d.width < 500 ) {
                d.width = 500 ;
            }
            return d ;
        }
        
            
        /* (non-Javadoc)
         * @see javax.swing.CellEditor#addCellEditorListener(javax.swing.event.CellEditorListener)
         */
        public void addCellEditorListener(CellEditorListener l) {
            listeners.add( l ) ;

        }
        /* (non-Javadoc)
         * @see javax.swing.CellEditor#cancelCellEditing()
         */
        public void cancelCellEditing() {
            this.setText( this.value.toString() ) ;
        }
        /* (non-Javadoc)
         * @see javax.swing.CellEditor#getCellEditorValue()
         */
        public Object getCellEditorValue() {
//            System.out.println( "getCellEditorValue()..." ) ;
            if( this.value == null )  {
//                System.out.println( "value is null" ) ;
                return null ;
            }
            else {
//                System.out.println( "value.getClass().getName(): " + this.value.getClass().getName() ) ;
                return ((AdqlEntry)this.value).getUserObject() ;
            }
        }
        /* 
         * This is controlled elsewhere; viz: in the EditPromptAction and elsewhere.
         * There should be no need to return anything other than true.
         */
        public boolean isCellEditable( EventObject anEvent ) {
           return true;
        }
        
        /*
         * @see javax.swing.CellEditor#removeCellEditorListener(javax.swing.event.CellEditorListener)
         */
        public void removeCellEditorListener(CellEditorListener l) {
            listeners.removeElement( l ) ;
        }
        /* (non-Javadoc)
         * @see javax.swing.CellEditor#shouldSelectCell(java.util.EventObject)
         */
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }
        /* (non-Javadoc)
         * @see javax.swing.CellEditor#stopCellEditing()
         */
        public boolean stopCellEditing() {
//            System.out.println( "stopCellEditing: \n" + this.getText() ) ;
            return true;
        }
       
        
    } // end of class AdqlTreeCellEditor
    
    
    public class AdqlTreeCellEditor2 extends DefaultTreeCellEditor {
        // TODO Validation of basic values required.
        
        public void cancelCellEditing() {
            super.cancelCellEditing();
        }
        protected void prepareForEditing() {
            super.prepareForEditing();
        }
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
        private AdqlEntry entry ;
        private XmlObject xmlValueObject ;
        private DefaultCellEditor editor ;
        
        public AdqlTreeCellEditor2( JTree tree, DefaultTreeCellRenderer renderer, DefaultCellEditor editor ) {
            super( tree, renderer, editor ) ;  
            editor.setClickCountToStart( -1 ) ;
        }
        
        public Component getTreeCellEditorComponent( JTree tree
                								   , Object value
									               , boolean sel
									               , boolean expanded
									               , boolean leaf
									               , int row ) {
            if( value != null ) {
                this.entry = (AdqlEntry)value ;
                this.xmlValueObject = ((AdqlEntry)value).getXmlObject() ;
                if( this.xmlValueObject != null ) 
                    AdqlTree.this.llEditor.setValue( this.xmlValueObject ) ;
            }      
            Component c = super.getTreeCellEditorComponent( tree, value, sel, expanded, leaf, row ) ;
            //System.out.println( "AdqlTreeCellEditor.getTreeCellEditorComponent" ) ;
            //System.out.println( "value.getClass().getName(): " + value.getClass().getName() ) ;
//            this.value = value ;
//            if( value != null )
//               setText( ((AdqlEntry)value).toText( expanded, AdqlTree.this ) ) ;
            return c ;
        }
        
        public boolean isCellEditable(EventObject event) {
            return super.isCellEditable(event);
        }
        protected boolean shouldStartEditingTimer(EventObject event) {
            return false;
        }
        protected void startEditingTimer() {
            // super.startEditingTimer();
        }
        
        public Object getCellEditorValue() {
            XmlObject retVal = null ;
//          System.out.println( "getCellEditorValue()..." ) ;
          if( this.xmlValueObject != null )  {
              SchemaType type = xmlValueObject.schemaType() ;
              if( isAttributeDriven( type ) ) {
                  setAttributeValue( llEditor.getText() ) ;
              }
              else {
                  setElementValue( llEditor.getText() ) ;
              }
              
              retVal = this.xmlValueObject ;
              this.xmlValueObject = null ;
              // At a guess this needs to set the value within the xmlValueObject using the String value
              // from the editor text box. 
              System.out.println( "getCellEditorValue(): " + llEditor.getText() ) ;
              // Then reset the low level editor.
              llEditor.setValue( null ) ;
              ((DefaultTreeModel)AdqlTree.this.getModel()).nodeChanged( this.entry ) ;       
              
          }
          return retVal ;
      }
        
      private void setAttributeValue( String value ) {
          SchemaType type = xmlValueObject.schemaType() ;
          String attributeName = (String)AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
          XmlString tempObject = XmlString.Factory.newInstance() ;
          tempObject.setStringValue( value ) ; 
          
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
          AdqlUtils.set( xmlValueObject, attributeName, valueObject ) ; 
      }
      
      private void setElementValue( String value ) {
          SchemaType type = xmlValueObject.schemaType() ;
          SchemaType listType = type.getListItemType() ;
          if( listType == null ) {
              ((XmlAnySimpleType)xmlValueObject).setStringValue( value )  ;
          }
          else {
              // This surprisingly works...
              ((XmlAnySimpleType)xmlValueObject).setStringValue( value )  ;
          }
      }
                
    }
    
    public class LowLevelEditor extends JTextField {
        
        private XmlObject xmlObject ;
        
        public LowLevelEditor() {
            super() ;
        }
        
        public void setValue( XmlObject xmlObject ) {
            this.xmlObject = xmlObject ;
        }
        
        public XmlObject getValue() {
            return this.xmlObject ;
        }
        
        public void setText( String string ) {
            String value ;
            if( this.xmlObject == null ) {
                System.out.println( "xmlObject was null" ) ;
                return ;
            }
            try {
                if( isAttributeDriven( this.xmlObject.schemaType() ) ) {
                    value = extractAttributeValue( xmlObject ) ;
                }
                else {
                    value = ((SimpleValue)this.xmlObject).getStringValue() ;
                }
 
                System.out.println( "getStringValue(): " + value ) ;
            }
            catch( Exception ex ) {
                value = "Invalid value returned from getStringValue()" ;
                ex.printStackTrace() ;
            }
            if( value != null )
                super.setText( value ) ;
 //           super.setText( string ) ;
        }
        
  
        
    }
    
    
    // NOte this is duplicated in the ADQLToolEditorPanel
    private boolean isAttributeDriven( SchemaType type ) {
        String name = (String)AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
        return ( name != null && name.length() > 0 ) ;
    }
    
    private String extractAttributeValue( XmlObject xmlObject ) {
        String retVal = null ;
        XmlObject intermediateObject ;         
        SchemaType type = xmlObject.schemaType() ;
        String attributeName = (String)AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
        intermediateObject = AdqlUtils.get( xmlObject, attributeName ) ;
        retVal = ((SimpleValue)(intermediateObject)).getStringValue() ;
        return retVal ;
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
                    AdqlEntry selectedEntry = (AdqlEntry)getLastSelectedPathComponent();
                    if( selectedEntry.isBottomLeafEditable() ) {
                        startEditingAtPath( path ) ;
                        editingActive = true ;
                    }
                }
            }         
            
        }
        
    } // end of class EditPromptAction
    
} // end of class AdqlTree