package org.astrogrid.desktop.modules.adqlEditor.nodes ;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTransformer;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandInfo;

/**
 * Represents the data for a single node in the XmlTree. This class (known as a
 * "user object" from the JTree perspective) provides a way to get information
 * about the node by essentially wrapping the XmlObject instance that the node
 * represents. 
 */
public class AdqlNode extends DefaultMutableTreeNode {
    
    private static final Log log = LogFactory.getLog( AdqlNode.class ) ;
    
    private static HashMap dMap = new HashMap(64) ;
    private static Random dRandom = new Random() ;
    
    private static AdqlTransformer transformer = new AdqlTransformer() ;
    
    protected NodeFactory nodeFactory ;
    private boolean expanded = false ;
    private int useableWidth = 0 ;
    private Integer did = newDid() ; // debug identifier
    
    private AdqlNode() {}
    
    AdqlNode( NodeFactory nodeFactory, AdqlNode parent, XmlObject o ) {
        this( nodeFactory, parent, o, true ) ;
    }
    
    AdqlNode( NodeFactory nodeFactory
            , AdqlNode parent
            , XmlObject o
            , boolean childAllowedChildren ) {
        super( o, childAllowedChildren ) ;  
        this.nodeFactory = nodeFactory ;
        if( parent != null )
            parent.maintainNodeIndex( parent, this ) ;
        if( allowsChildren )
            build( o ) ;
    }
    
    AdqlNode( NodeFactory nodeFactory
            , AdqlNode parent
            , XmlObject o
            , int childNodeIndex ) {
        this( nodeFactory, parent, o, childNodeIndex, true );
    }
    
    AdqlNode( NodeFactory nodeFactory
            , AdqlNode parent
            , XmlObject o
            , int childNodeIndex
            , boolean childAllowedChildren ) {
        super( o, childAllowedChildren ) ;  
        this.nodeFactory = nodeFactory ;
        if( parent != null )
            parent.maintainNodeIndex( parent, this, childNodeIndex ) ;
        if( allowsChildren )
            build( o ) ;
    }
    
    private static synchronized Integer newDid() {
        Integer did ;
        do {
            did = new Integer( dRandom.nextInt() ) ;
        } while ( dMap.containsKey( did ) ) ;
        return did ;
    }
    
    protected void maintainNodeIndex( AdqlNode parent, AdqlNode childNode ) {
        // Time to check where the position within the xml tree so we
        // can keep the display and the tree in line...  
        XmlObject childObject = childNode.getXmlObject() ;
        int index = AdqlUtils.findFilteredChildIndex( this.getXmlObject(), childObject ) ;
        if( index < 0 )
            nodeFactory.add( parent, childNode ) ;
        else
            nodeFactory.insert( parent, childNode, index ) ;
    }
    
    protected void maintainNodeIndex( AdqlNode parent, AdqlNode childNode, int childNodeIndex ) {
        int size = getChildCount() ;
        if( childNodeIndex > size-1 )
            nodeFactory.add( parent, childNode ) ;
        else
            nodeFactory.insert( parent, childNode, childNodeIndex ) ;      
    }
    
    protected void build( XmlObject o ) {
        XmlObject[] childArray = o.selectPath("./*") ;
        if( childArray != null ) {
            for( int i=0; i<childArray.length; i++ ) {
                if( AdqlUtils.isNodeForming( childArray[i] ) ) {
                    nodeFactory.newInstance( this, childArray[i] ) ;
                }              
            }       
        }
    }

    public boolean isHidingRequired() {
        return HidingNode.isHidingRequired( getXmlObject() )  ;
    }
    
    public boolean isNestingRequired() {
        return NestingNode.isNestingRequired( getXmlObject() ) ;
    }
    
    public boolean isBottomLeafEditable() {
        return AdqlUtils.isEditable( getSchemaType() ) ;     
    }
    
    public String getDisplayName() {
        return AdqlUtils.extractDisplayName( this.getXmlObject() ) ;
    }
    
    
    public boolean isTableLinked() {
        return AdqlUtils.isTableLinked( getSchemaType() ) ;
    }
    
    public boolean isColumnLinked() {
        return AdqlUtils.isColumnLinked( getSchemaType() ) ;
    }
    
    
    /**
     * Gets the number of children of the XML this entry represents.
     * 
     * @return The number of children.
     */
    @Override
    public int getChildCount() {
        int result = 0 ;
        if( this.children != null ) 
            result = this.children.size() ; ;
        return result ;
    }

    /**
     * Gets the child at <em>index</em> from among the children of the XML
     * this entry represents.
     * 
     * @param index The index number for the child to get.
     * @return An entry representing the child.
     */
    public AdqlNode getChild(int index)
    {
        AdqlNode childEntry = (AdqlNode)this.children.elementAt(index);
        return childEntry;
    }

    /**
     * Gets the children of the XML this entry represents.
     * 
     * @return An entry array representing the children.
     */
    public AdqlNode[] getChildren() {
        int childCount = getChildCount() ;
        AdqlNode[] entryChildren = new AdqlNode[childCount];
        if( childCount > 0 ) {
            Enumeration e = this.children.elements() ;
            for( int i = 0; e.hasMoreElements(); i++ ) {
                entryChildren[i] =  (AdqlNode)e.nextElement() ;
            }
        }    
        return entryChildren;
    }

    private String dealWithPatternContext( String displayName ) {
        String elementName = null ;
        XmlCursor cursor = this.getXmlObject().newCursor() ;
        if( !cursor.currentTokenType().isStart() ) 
            cursor.toFirstChild(); 
        try {
            elementName = cursor.getName().getLocalPart() ;
        }
        catch ( Exception ex ) {
            ;
        }
        cursor.dispose() ;
        if( AdqlData.PATTERN_ELEMENT_NAME.equals( elementName ) ) 
             return elementName ;
        return displayName ;
    
    }
    
    
 
    public XmlObject getXmlObject() {
        XmlObject xmlObject = null ;
        Object obj = getUserObject() ;
        if( obj == null ) {
            log.debug( "user object is null" ) ;
        }
        else if( obj instanceof XmlObject ) {
            xmlObject = (XmlObject)obj ;
        }
        else if( obj instanceof String ) {
            xmlObject = XmlString.Factory.newInstance() ;
            ((XmlString)xmlObject).setStringValue( (String)obj ) ;
            log.debug( "Just created transient XmlString object" ) ;
        }
        else if( obj instanceof AdqlNode ) {
            log.debug( "user object is an AdqlNode" ) ;
        }
        else {
            log.debug( "unknown user object" ) ;
        }
        return xmlObject ;
    }
    
    
    public SchemaProperty[] getElementProperties() {
        return  getXmlObject().schemaType().getElementProperties() ;
    }
    
    public SchemaType getSchemaType() {
        return getXmlObject().schemaType() ;
    }
    
    public String getShortTypeName() {
        String retValue = "" ;
        SchemaType type = this.getXmlObject().schemaType() ;
        if( type.isAnonymousType() == false ) {
            retValue = type.getName().getLocalPart() ;
        }
        return retValue ;
    }
    
    
    public void setExpanded( boolean expanded ) {
        this.expanded = expanded ;
    }
    
    public StringBuffer debug_LogChildren( StringBuffer buffer ) {
        AdqlNode[] children = getChildren() ;
        if( children.length == 0 ) {
            buffer.append( "\nHas no children." ) ;
        } 
        else {
            for( int i=0; i<children.length; i++ ) {
                buffer
                    .append( "\nchild " + i + " as xmlobject: " )
                    .append( children[i].getXmlObject().toString() ) 
                    .append( "\n   AdqlNode hashcode: " + children[i].hashCode() ) 
                    .append( "\n   xmlobject hashcode: " + children[i].getXmlObject().hashCode() ) ;
            }
        }
        return buffer ;
    }
    

  
    /**
     * @return Returns the expanded.
     */
    public boolean isExpanded() {
        return expanded;
    }
    public int getUseableWidth() {
        return useableWidth;
    }
    public void setUseableWidth(int useableWidth) {
        this.useableWidth = useableWidth;
    }
    
    public String getElementContextPath() {
        TreeNode[] nodes = this.getPath() ;
        XmlObject xmlObj = null ;
        StringBuffer buffer = new StringBuffer( nodes.length * 32 ) ;
        buffer.append( '/' ) ;
        for( int i=1; i<nodes.length; i++ ) {
            xmlObj = ((AdqlNode)nodes[i]).getXmlObject() ;
            buffer
               .append( AdqlUtils.extractElementLocalName( xmlObj ) )
               .append( "[@type='" )
               .append( AdqlUtils.getLocalName( xmlObj ) )
               .append( "']" ) ;
            if( i<nodes.length-1 )
                buffer.append( '/' ) ;
        }
        return buffer.toString() ;
    }
    
    public String getRelativeElementContextPath( AdqlNode ancestor ) {
        Enumeration e = this.pathFromAncestorEnumeration( ancestor ) ;
        XmlObject xmlObj = null ;
        StringBuffer buffer = new StringBuffer( 128 ) ;
        e.nextElement() ;
        buffer.append( '.' ) ;
        while( e.hasMoreElements() ) {
            buffer.append( '/' ) ;
            xmlObj = ((AdqlNode)e.nextElement()).getXmlObject() ;
            buffer
               .append( AdqlUtils.extractElementLocalName( xmlObj ) )
               .append( "[@type='" )
               .append( AdqlUtils.getLocalName( xmlObj ) )
               .append( "']" ) ;     
        }
        return buffer.toString() ;
    }
     
    public AdqlNode insert( CommandInfo ci, XmlObject source ) {
        return nodeFactory.newInstance( this, add( ci ).set( source ) ) ;
    }
    
    public AdqlNode insert( CommandInfo ci ) {
        return nodeFactory.newInstance( this, add( ci ) ) ;
    }
    
    public AdqlNode insert( CommandInfo ci, XmlObject source, int index ) {
        XmlObject newObject = null ; 
        if( ci.isChildHeldInArray() ) {                
            newObject = AdqlUtils.insertNewInArray( ci.getParentObject(), ci.getChildElementName(), index ) ;
            newObject = newObject.changeType( ci.getChildType() ) ;
        }
        else {         
            log.error( "AdqlNode.insert(CommandIndfo,XmlObject,int) invoked where parent is NOT COMPOSED solely of an array of elements/types!" ) ;
        }
        return nodeFactory.newInstance( this, newObject.set( source ) ) ;
    }
    
    public AdqlNode insert( CommandInfo ci, XmlObject source, boolean before ) {
        AdqlNode newInstance = null ;
        try {          
            //
	        // ASSUMPTION. The parent is composed solely of an array of elements/types!	        	             
	        if( ci.isChildHeldInArray() ) {   
	            // First identify where in the array the selected entry is....
	            Object selectedObject = ci.getChildObject() ;
	            String childElementName = ci.getChildElementName() ;
	            Object[] objects = AdqlUtils.getArray( ci.getParentObject(), childElementName ) ;
	            int offset = -1 ;
	            for( int i=0; i<objects.length; i++ ) {
	                if( selectedObject == objects[i] ) {
	                    offset = i ;
	                    break ;
	                }
	            }
	            if( offset != -1 ) {   
	                XmlObject newObject = null ;	
	                if( before ) {
	                    newObject = AdqlUtils.insertNewInArray( ci.getParentObject(), childElementName, offset ) ;
	                }
	                // If not before, everything else must be after...
	                else {
	                    if( objects.length == offset+1 ) {          
	                        // Simply add to the end of the array...
	                        newObject = AdqlUtils.addNewToEndOfArray( ci.getParentObject(), childElementName ) ;
	                    }
	                    else {
	                        newObject = AdqlUtils.insertNewInArray( ci.getParentObject(), childElementName, offset+1 ) ;
	                    }
	                } 
                    newInstance = nodeFactory.newInstance( this, newObject.set( source ) ) ;
	            }
	            else {
	                log.error( "Serious error in array structure with Paste before/after!" ) ;
	            }
            }
	        else {
	            log.error( "Paste before/after invoked on an element whose parent is NOT COMPOSED solely of an array of elements/types!" ) ;
	        }	        
        }
        catch( Exception exception ) {
            log.error( "Insert failure.", exception ) ;
        }
        return newInstance ;
    }
    
    private XmlObject add( CommandInfo ci ) {
        XmlObject parentObject = getXmlObject() ;
        XmlObject newObject = null ; 
        SchemaType childType = ci.getChildType() ;
        if( ci.isChildHeldInArray() ) {                
            newObject = AdqlUtils.addNewToEndOfArray( parentObject, ci.getChildElementName() ) ;
            newObject = newObject.changeType( childType ) ;
            newObject = AdqlUtils.setDefaultValue( newObject ) ;
        }
        else {
            if( childType.isBuiltinType() ) {
                newObject = XmlObject.Factory.newInstance().changeType( childType ) ;
                newObject = AdqlUtils.setDefaultValue( newObject ) ;
            }
            else {            
                newObject = AdqlUtils.addNew( parentObject, ci.getChildElementName() ) ;
                if( newObject != null ) {
                    newObject = newObject.changeType( childType ) ;
                    newObject = AdqlUtils.setDefaultValue( newObject ) ;
                }
                else {
                    newObject = XmlObject.Factory.newInstance().changeType( ci.getChildElement().javaBasedOnType() ) ;
                    newObject = newObject.changeType( childType ) ;
                    newObject = AdqlUtils.setDefaultValue( newObject ) ;
                    AdqlUtils.set( parentObject, ci.getChildElementName(), newObject ) ; 
                }
            }
        } 
        return newObject ;
    }
    
    public void remove( CommandInfo ci ) {
        nodeFactory.removeInstance( this, ci.getChildEntry() ) ;
    }
    
    public void removeNode( AdqlNode child ) {
        nodeFactory.removeInstance( this , child ) ;
    }
    
    public AdqlNode replace( CommandInfo ci, XmlObject source ) {
        XmlObject targetObject = ci.getChildObject() ;
        targetObject = targetObject.set( source ) ; 
        Enumeration e = ci.getParentEntry().children() ;
        AdqlNode childNode = ci.getChildEntry() ;
        int index = 0 ;
        while( e.hasMoreElements() ) {
            if( childNode == e.nextElement() ) {
                break ;
            }
            index++ ;
        }
        nodeFactory.remove( ci.getParentEntry(), ci.getChildEntry() ) ;
        return nodeFactory.newInstance( this, targetObject, index ) ; 
    }
    
    public String toHtml( boolean expanded, boolean leaf, AdqlTree tree ) { 
        String displayName = null;
        try {
            displayName = AdqlUtils.extractDisplayName( getXmlObject() ) ;
        }
        catch( Exception ex ) {
            log.debug( "AdqlNode.toHtml(boolean,boolean,AdqlTree)", ex ) ;
        }
        String displayInfo = null ;
        if( expanded ) {
            String shortName = getShortTypeName() ;
            displayInfo = displayName ;
            String[] attrOrElementNames = AdqlUtils.getEditableAttributes( shortName ) ;
            if( attrOrElementNames != null && attrOrElementNames.length == 1 ) {
                displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), attrOrElementNames[0] )).getStringValue() ) ;
            } 
            else {
                attrOrElementNames = AdqlUtils.getEditableElements( shortName ) ;
                if( attrOrElementNames != null && attrOrElementNames.length == 1 ) {
                    displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), attrOrElementNames[0] )).getStringValue() ) ;
                }
            }
        }
        else {
            // Get the ADQL/s representation of this element ...
            displayInfo = transformer.transformDisplayValues( this, tree, expanded, leaf ) ; 
            int index = displayInfo.toUpperCase().indexOf( displayName.toUpperCase() ) ;
            // This is a temporary kludge....
            if( index == -1 ) {
                index = displayInfo.indexOf( '>', "<html>".length() ) ;
                StringBuffer buffer = new StringBuffer( displayInfo.length() + 32 ) ;
                buffer
                	.append( displayInfo.substring( 0, index+1 ) ) 
                	.append( displayName )
                	.append( ' ' ) 
                	.append( displayInfo.substring( index+1 ) ) ;
                displayInfo = buffer.toString() ;
            } 
        }
    
        if( displayInfo == null || displayInfo.trim().length() == 0 ) {
            displayInfo = "NO NAME" ;
        } 
        return displayInfo ;
    }

    public static class UnsupportedObjectException extends Exception {
        
        public UnsupportedObjectException(String message) {
            super(message);
        }
        
    }

    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }

    public Integer getDid() {
        if( log.isDebugEnabled() )
            return did;
        else
            return new Integer(-1) ;
    }
    
    public String toDebugString() {
        StringBuffer buffer = new StringBuffer() ;
        buffer.append( "\nAdqlNode did: " ).append( did ) ;
        try {
            buffer.append( super.toString() ) ;
        }
        catch( Exception ex ) {
            buffer.append( "Exception thrown" ).append( ex.getClass() ) ;
        }
        return buffer.toString() ;
    }
    
    
}