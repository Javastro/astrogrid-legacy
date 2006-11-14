package org.astrogrid.desktop.modules.adqlEditor.nodes ;


import java.util.Enumeration;
import java.util.Hashtable;
import java.lang.Integer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.SchemaProperty; 
import org.apache.xmlbeans.SchemaType; 
import org.apache.xmlbeans.SimpleValue; 
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlLong ;
import org.apache.xmlbeans.XmlDouble ;

import javax.swing.tree.DefaultMutableTreeNode ;

import org.astrogrid.desktop.modules.adqlEditor.*;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandExec;
import org.astrogrid.desktop.modules.adqlEditor.commands.CommandInfo;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTransformer;

/**
 * Represents the data for a single node in the XmlTree. This class (known as a
 * "user object" from the JTree perspective) provides a way to get information
 * about the node by essentially wrapping the XmlObject instance that the node
 * represents. 
 */
public class AdqlNode extends DefaultMutableTreeNode {
    
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    private static final Log log = LogFactory.getLog( AdqlNode.class ) ;
    
    
    private static AdqlTransformer transformer = new AdqlTransformer() ;
    
    private boolean expanded = false ;
    private int useableWidth = 0 ;
    
    private AdqlNode() {}
    
    AdqlNode( AdqlNode parent, XmlObject o ) {
        this( parent, o, true ) ;
    }
    
    AdqlNode( AdqlNode parent, XmlObject o, boolean allowsChildren ) {
        super( o, allowsChildren ) ;  
        if( parent != null )
            parent.maintainNodeIndex( this ) ;
        if( allowsChildren )
            build( o ) ;
    }
    
    AdqlNode( AdqlNode parent, XmlObject o, int childNodeIndex ) {
        super( o, true ) ;  
        if( parent != null )
            parent.maintainNodeIndex( this, childNodeIndex ) ;
        if( allowsChildren )
            build( o ) ;
    }
    
    protected void maintainNodeIndex( AdqlNode childNode ) {
        // Time to check where the position within the xml tree so we
        // can keep the display and the tree in line...  
        XmlObject childObject = childNode.getXmlObject() ;
        int index = AdqlUtils.findFilteredChildIndex( this.getXmlObject(), childObject ) ;
        if( index < 0 )
            add( childNode ) ; 
        else
            insert( childNode, index ) ;
    }
    
    protected void maintainNodeIndex( AdqlNode childNode, int childNodeIndex ) {
        int size = getChildCount() ;
        if( childNodeIndex > size-1 )
            add( childNode ) ;
        else
            insert( childNode, childNodeIndex ) ;      
    }
    
    protected void build( XmlObject o ) {
        XmlObject[] childArray = o.selectPath("./*") ;
        if( childArray != null ) {
            for( int i=0; i<childArray.length; i++ ) {
                if( AdqlUtils.isNodeForming( childArray[i] ) ) {
                    NodeFactory.newInstance( this, childArray[i] ) ;
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
            //
            // As you can see, this is not very generic.
            // See AdqlData.EDITABLE_ATTRIBUTES and AdqlData.EDITABLE_ELEMENTS
            // for some idea how the following can be made more aesthetic...
            if( shortName.equals( AdqlData.COMPARISON_PRED_TYPE ) ) {
                // "Comparison" here is the name of an xml attribute. Sorry. Not very generic.
                displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), "Comparison" )).getStringValue() ) ;
            }
            else if( shortName.equals( AdqlData.BINARY_EXPRESSION_TYPE ) 
                     ||
                     shortName.equals( AdqlData.UNARY_EXPRESSION_TYPE ) ) {
                // "Oper" here is the name of an xml attribute. Sorry. Not very generic.
                displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), "Oper" )).getStringValue() ) ;
            }
            else if( shortName.equals( AdqlData.AGGREGATE_FUNCTION_TYPE ) 
                     ||
                     shortName.equals( AdqlData.MATH_FUNCTION_TYPE )
                     ||
                     shortName.equals( AdqlData.TRIG_FUNCTION_TYPE ) ) {
                // "Name" here is the name of an xml attribute. Sorry. Not very generic.
                displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), "Name" )).getStringValue() ) ;
            }
            else if( shortName.equals( AdqlData.JOIN_TABLE_TYPE ) ) {
                displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), "Qualifier" )).getStringValue() ) ;
            }
            else {
                displayInfo = displayName ;
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
    
    public void debug_LogChildren() {
        AdqlNode[] children = getChildren() ;
        if( children.length == 0 ) {
            log.debug( "Has no children." ) ;
        } 
        else {
            for( int i=0; i<children.length; i++ ) {
                log.debug( "child as xmlobject " + i + ":") ;
                log.debug( children[i].getXmlObject().toString() ) ;
                log.debug( "AdqlNode hashcode: " + children[i].hashCode() ) ;
                log.debug( "xmlobject hashcode: " + children[i].getXmlObject().hashCode() ) ;
            }
        }
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
    
    
    public AdqlNode insert( CommandInfo ci, XmlObject source ) {
        return NodeFactory.newInstance( this, add( ci ).set( source ) ) ;
    }
    
    public AdqlNode insert( CommandInfo ci ) {
        return NodeFactory.newInstance( this, add( ci ) ) ;
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
        return NodeFactory.newInstance( this, newObject.set( source ) ) ;
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
                    newInstance = NodeFactory.newInstance( this, newObject.set( source ) ) ;
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
            log.error( exception ) ;
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
        NodeFactory.removeInstance( this, ci.getChildEntry() ) ;
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
        ci.getChildEntry().removeFromParent() ;
        return NodeFactory.newInstance( this, targetObject, index ) ; 
    }
    
    public static class UnsupportedObjectException extends Exception {
        
        public UnsupportedObjectException(String message) {
            super(message);
        }
        
    }
}