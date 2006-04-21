
package org.astrogrid.desktop.modules.adqlEditor ;


import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlLong;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;


/**
 * Represents the data for a single node in the XmlTree. This class (known as a
 * "user object" from the JTree perspective) provides a way to get information
 * about the node by essentially wrapping the XmlObject instance that the node
 * represents. 
 */
public final class AdqlEntry extends DefaultMutableTreeNode {
    
    private static final boolean DEBUG_ENABLED = false ;
    private static final boolean TRACE_ENABLED = false ;
    private static final Log log = LogFactory.getLog( AdqlEntry.class ) ;
    
    public static final Hashtable HIDE_CHILDREN ; 
    static {
        HIDE_CHILDREN = new Hashtable() ;
        HIDE_CHILDREN.put( "atomType", "atomType" ) ;
    }
    
    public static final String MISSING_VALUE = "{ missing }" ;
    
    public static final Hashtable VALUE_GETTERS ;
    static {
        VALUE_GETTERS = new Hashtable() ;
        VALUE_GETTERS.put( "REAL", "REAL" ) ;
        VALUE_GETTERS.put( "INTEGER", "INTEGER" ) ;
        VALUE_GETTERS.put( "STRING", "STRING" ) ; // Not sure about this one.
    }
    
    private static AdqlTransformer transformer = new AdqlTransformer() ;
    
    private XmlObject[] hiddenChildren = null ;
    
    public static AdqlEntry newInstance( XmlObject rootObject ) {

        AdqlEntry rootEntry = newInstance( null, rootObject ) ;
        return rootEntry ;
    }
     
    public static AdqlEntry newInstance( AdqlEntry parent, XmlObject xmlObject ) {
        AdqlEntry entry = buildInstance( xmlObject ) ;    
        if( parent != null && !parent.isChildHidingRequired() ) {
            // Time to check where the position within the xml tree so we
            // can keep the display and the tree in line...
            int index = findChildIndex( parent.getXmlObject(), xmlObject ) ;
            if( index == -1 )
                parent.add( entry ) ; 
            else
                parent.insert( entry, index ) ;
        }
        return entry ;
    }
    
    public static boolean removeInstance( AdqlEntry parent, AdqlEntry child ) {
        boolean retValue = false ;
        XmlObject co ;  // child object
        XmlObject po ; // parent object     
        XmlCursor cursor = null ;
        try {
            //
            // Simple bit first. Remove the child entry from the mutable tree...
            parent.remove( child ) ;
            //
            // XmlCursor is plain wonderful! 
            // I use it to remove the object from the parent.
            co = child.getXmlObject() ;  // child object
            po = parent.getXmlObject() ; // parent object
            cursor = po.newCursor() ;
            cursor.toFirstChild() ; // There has to be a first child!
            do {
                if( co == cursor.getObject() ) {
                    retValue = cursor.removeXml() ;
                    break ;
                }
            } while( cursor.toNextSibling() ) ;
            retValue = true ;
        }
        catch( Exception ex ) {
            log.debug( "Failure to remove instance..." ) ;
            log.debug( "Parent:" ) ;
            parent.debug_LogChildren() ;
                        
            log.debug( "Child:" ) ;
            log.debug( child.getXmlObject().toString() ) ;
            log.debug( "hashCode: " + child.hashCode() ) ;
        } 
        finally {
            if( cursor != null )
                cursor.dispose() ;
        }
        return retValue ;
    }
    
    
    public static boolean disconnectInstance( AdqlEntry parent, AdqlEntry child ) {
        parent.remove( child ) ;
        return true ;
    }

    public static boolean reconnectInstance( AdqlEntry parent, AdqlEntry child, int index ) {
        parent.insert( child, index ) ;
        return true ;
    } 

    public static boolean reconnectInstance( AdqlEntry parent, AdqlEntry child ) {
        parent.add( child ) ;
        return true ;
    } 
    
    public static boolean isChildHidingRequired( XmlObject xmlbean ) {
        boolean retValue = false ;
        SchemaType type = xmlbean.schemaType() ;
        if(  ( type.isBuiltinType() == false )
             &&
             ( type.isAnonymousType() == false )
             &&
             ( HIDE_CHILDREN.containsKey( type.getName().getLocalPart() ) ) ) {
            retValue = true ;
        }
        return retValue ;
    }
    
      
    public static int findChildIndex( XmlObject parent, XmlObject child ) {
        int index = 0 ;
        boolean found = false ;
        XmlCursor cursor = parent.newCursor() ;
        cursor.toFirstChild() ; // There has to be a first child!
        do {
            if( child == cursor.getObject() ) {
                found = true ;
                break ;
            }
            index++ ;
        } while( cursor.toNextSibling() ) ;
        if( found == false ) {
            index = -1 ;
        }
        cursor.dispose() ;
        return index ;
    }
    
    
    private static AdqlEntry buildInstance( XmlObject xmlObject ) {
        AdqlEntry entry = null ;
        XmlObject[] childArray = xmlObject.selectPath("./*") ;
        if( childArray != null && childArray.length > 0 ) {
            if( isChildHidingRequired( xmlObject ) ) {
                entry = new AdqlEntry( xmlObject, false ) ;
                entry.hiddenChildren = childArray ;
            }
            else {
                entry = new AdqlEntry( xmlObject ) ;
                for( int i=0; i<childArray.length; i++ ) {
                    if( childArray[i].schemaType().isAttributeType() == false )
                        entry.add( buildInstance( childArray[i] ) ) ;
                }
            }                    
        }
        else {
            entry = new AdqlEntry( xmlObject, !isChildHidingRequired( xmlObject ) ) ;
        }
        return entry ;
    }
    
    private boolean expanded = false ;
    private int useableWidth = 0 ;
    
    private AdqlEntry() {}
    
    private AdqlEntry( XmlObject o ) {
        super( o ) ;
    }
    
    private AdqlEntry( XmlObject o, boolean allowsChildren ) {
        super( o, allowsChildren ) ;
    }

    public boolean isChildHidingRequired() {
        return isChildHidingRequired( this.getXmlObject() ) ;
    }
    
    // This is a hodge podge.
    public boolean isBottomLeafEditable() {
        boolean result = false ;
        if( this.getXmlObject().schemaType().isAnonymousType() )
            return false ;
        String name = this.getXmlObject().schemaType().getName().getLocalPart() ;
       
        if( this.getXmlObject().schemaType().isSimpleType() 
            ||
            this.isChildHidingRequired() ) {
            result = true ;
        }
        if( AdqlData.EDITABLE.containsKey( name ) )
            result = true ;
        return result ;
    }
    
    public String getDisplayName() {
        return AdqlUtils.extractDisplayName( this.getXmlObject() ) ;
    }
    
    
    public boolean isTableLinked() {
        return (this.getDisplayName().toUpperCase().indexOf( "TABLE" ) != -1) ;
    }
    
    public boolean isColumnLinked() {
        return (this.getDisplayName().toUpperCase().indexOf( "COLUMN" ) != -1) ;
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
    public AdqlEntry getChild(int index)
    {
        AdqlEntry childEntry = (AdqlEntry)this.children.elementAt(index);
        return childEntry;
    }

    /**
     * Gets the children of the XML this entry represents.
     * 
     * @return An entry array representing the children.
     */
    public AdqlEntry[] getChildren() {
        int childCount = getChildCount() ;
        AdqlEntry[] entryChildren = new AdqlEntry[childCount];
        if( childCount > 0 ) {
            Enumeration e = this.children.elements() ;
            for( int i = 0; e.hasMoreElements(); i++ ) {
                entryChildren[i] =  (AdqlEntry)e.nextElement() ;
            }
        }    
        return entryChildren;
    }

    public String toHtml( boolean expanded, boolean leaf, AdqlTree tree ) { 
        String displayName = null;
        try {
            displayName = AdqlUtils.extractDisplayName( getXmlObject() ) ;
            if( AdqlUtils.localNameEquals( getXmlObject(), AdqlData.ATOM_TYPE ) ) {
                displayName = dealWithPatternContext( displayName ) ;
            }
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        String displayInfo = null ;
        if( expanded ) {
            String shortName = getShortTypeName() ;
            displayInfo = displayName ;
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
            else if( hiddenChildren != null ) {
                displayInfo = extractValue( displayName, getXmlObject() ) ;
            }
            else {
                displayInfo = extractValue( displayName, getXmlObject() ) ;
            }
        }
        else if( hiddenChildren == null ) {
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
        else {
            //System.out.println( "hiddenChildren! ..." ) ;
            String hcName = AdqlUtils.extractDisplayName( hiddenChildren[0] ) ;
            //System.out.println( "hcName: " + hcName ) ;
            String hcValue = extractValue( hcName, getXmlObject() ) ;
            //System.out.println( "hcValue: " + hcValue ) ;
            displayInfo = hcValue  ;
        }
        
        if( displayInfo == null || displayInfo.trim().length() == 0 ) {
            displayInfo = "NO NAME" ;
        } 
//        System.out.println( "displayInfo: " + displayInfo ) ;
        return displayInfo ;
    }
    
//    public String toText( boolean expanded, AdqlTree tree ) {
//        return toString( expanded, tree ) ;
//    } 
    /**
     * Returns a name that can be used as a tree node label.
     * 
     * @return The name of the element or attribute this entry represents.
     */
//    private String toString( boolean expansionState, AdqlTree tree ) {
//        String displayName = null;
//        try {
//            displayName = AdqlUtils.extractDisplayName( getXmlObject() ) ;
//        }
//        catch( Exception ex ) {
////            System.out.println( "\nException thrown in toString().\nException stack trace follows... " ) ;
////            System.out.println( "===== pretty print after exception in toString()... =====" ) ;
////            XmlOptions opts = new XmlOptions();
////            opts.setSavePrettyPrint();
////            opts.setSavePrettyPrintIndent(4);
////            System.out.println( root );
//            ex.printStackTrace() ;
//        }
//        String displayInfo = null ;
//        if( expansionState ) {
//            String shortName = getShortTypeName() ;
//            displayInfo = displayName ;
////            if( displayName.equals( "Comparison") ) {
//            if( shortName.equals( AdqlData.COMPARISON_TYPE ) ) {
//                // "Comparison" here is the name of an xml attribute. Sorry. Not very generic.
//                displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), "Comparison" )).getStringValue() ) ;
//            }
//            else if( shortName.equals( AdqlData.BINARY_EXPRESSION_TYPE ) ) {
//                displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), "Oper" )).getStringValue() ) ;
//            }
//            else if( shortName.equals( AdqlData.UNARY_EXPRESSION_TYPE ) ) {
//                displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), "Oper" )).getStringValue() ) ;
//            }
//            else if( shortName.equals( AdqlData.AGGREGATE_FUNCTION_TYPE ) ) {
//                
//            }
//            else if( hiddenChildren != null ) {
//                displayInfo = extractValue( displayName, getXmlObject() ) ;
//            }
//            else {
//                displayInfo = extractValue( displayName, getXmlObject() ) ;
//            }
//        }
//        else if( hiddenChildren == null ) {
//            String xml = getXmlObject().newCursor().xmlText() ;
//            // Get the ADQL/s representation of this element ...
////            displayInfo = transformer.transform( xml ) ;
//            if( displayInfo.startsWith( "<html") ) {            
//                int fromIndex = displayInfo.indexOf( "<font" ) ;
//                fromIndex = displayInfo.indexOf( '>', fromIndex ) ;
//                int index = displayInfo.indexOf( displayName, fromIndex ) ;
//                if( index == -1 || index > displayName.length() + 9 + fromIndex ) {
//                    displayInfo = displayName + ' ' + displayInfo ;
//                }
//            }
//        }
//        else {
//            displayInfo = AdqlUtils.extractDisplayName( hiddenChildren[0] ) ;
//            displayInfo = extractValue( displayInfo, getXmlObject() ) ;
//        }
//        return  displayInfo.trim() ;
//    }
    
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
    
    public String toString() {
        String retVal = null ;
        if( isBottomLeafEditable() ) {
            retVal = "Hello" ;
        }
        return retVal ;
    }
    
    private String extractValue( String displayName, XmlObject o ) {
//        if( VALUE_GETTERS.containsKey( displayName.toUpperCase() ) ) {
        SchemaType type = o.schemaType() ;
        if(  ( type.isBuiltinType() == false )
                &&
             ( type.isAnonymousType() == false )
                &&
             ( type.getName().getLocalPart().equals( "atomType" ) ) ) {
            return displayName + ' ' + (new Atom( o )).formatDisplay() ;
        }
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
        else if( obj instanceof AdqlEntry ) {
            log.debug( "user object is an AdqlEntry" ) ;
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
        AdqlEntry[] children = getChildren() ;
        if( children.length == 0 ) {
            log.debug( "Has no children." ) ;
        } 
        else {
            for( int i=0; i<children.length; i++ ) {
                log.debug( "child as xmlobject " + i + ":") ;
                log.debug( children[i].getXmlObject().toString() ) ;
                log.debug( "AdqlEntry hashcode: " + children[i].hashCode() ) ;
                log.debug( "xmlobject hashcode: " + children[i].getXmlObject().hashCode() ) ;
            }
        }
    }
    
    static public class Atom {
        private XmlObject atomType ;
         
        public Atom( XmlObject atomType ) {
            this.atomType = atomType ;         
        }
        
        public String getUnits() {
            XmlObject retValObj = null ;
            String retVal = null ;
            boolean isSet = AdqlUtils.isSet( this.atomType, "Unit" ) ;
            if( isSet ) {
                retValObj = AdqlUtils.get( this.atomType, "Unit" ) ;
                if( retValObj != null )
                    retVal = ((SimpleValue)retValObj).getStringValue() ;
                if( retVal != null )
                    retVal = retVal.trim() ;
            }  
            return retVal ;
        }
        
        public String getValue()  {
            XmlObject retValObj = null ;
            String retVal = "" ; // default to missing
            XmlObject o = AdqlUtils.get( this.atomType, "Literal" ) ;
            if( o != null ) {
                retValObj = AdqlUtils.get( o, "Value" ) ;
                if( retValObj != null ) {
                    // This is a guard against using the Adql/x pane to mangle 
                    // some value unacceptable to the specified type.
                    // I'm surprized, but it is possible to get this far
                    // into the Tree pane before detecting it...
                    try {
                        retVal = ((SimpleValue)retValObj).getStringValue() ;
                    }
                    catch( Exception ex ) {
                        retVal = "" ;
                    }                   
                }              
                if( retVal != null )
                    retVal = retVal.trim() ;
                if( AdqlUtils.localNameEquals( o, AdqlData.STRING_TYPE ) ) {
                    retVal = unstripQuotes( retVal ) ;
                }
                if( retVal == null || retVal.length() == 0 )
                    retVal =  "" ;
            } 
            return retVal ;
        }
        
//        public void setValue( String newValue ) {
//            String v = newValue.trim() ;           
//            XmlObject literal = null ;
//            XmlObject value = null ;
//            Success: {
//                // First try setting a IntegerType....
//                try {
//                    Long lv = new Long( v ) ;
//                    literal = AdqlUtils.newInstance( AdqlUtils.getType( atomType, AdqlData.INTEGER_TYPE ) ) ;
//                    value = AdqlUtils.newInstance( XmlLong.type ) ;        
//                    break Success;
//                }
//                // If we fail try setting a RealType....
//                catch( NumberFormatException nfex ) { ; }
//                try {
//                    Double dv = new Double( v ) ;
//                    literal = AdqlUtils.newInstance( AdqlUtils.getType( atomType, AdqlData.REAL_TYPE ) ) ;
//                    value = AdqlUtils.newInstance( XmlDouble.type ) ;
//                    break Success;
//                }
//                catch( NumberFormatException nfex ) { ; }
//                // If we get this far we default to a String...
//                literal = AdqlUtils.newInstance( AdqlUtils.getType( atomType, AdqlData.STRING_TYPE ) ) ;
//                value = AdqlUtils.newInstance( XmlString.type ) ;
//                if( literal != null && value != null ) {
//                    ((org.apache.xmlbeans.XmlAnySimpleType)value).setStringValue( v ) ;
//                    AdqlUtils.set( literal, "Value", value ) ;
//                    literal = literal.changeType( AdqlUtils.getType( literal, AdqlData.LITERAL_TYPE ) ) ;
//                    AdqlUtils.set( atomType, "Literal", literal ) ;
//                    literal = literal.changeType( AdqlUtils.getType( literal, AdqlData.STRING_TYPE ) ) ;
//                }
//            }
////            if( literal != null && value != null ) {
////                ((org.apache.xmlbeans.XmlAnySimpleType)value).setStringValue( v ) ;
////                AdqlUtils.set( literal, "Value", value ) ;
////                literal = literal.changeType( AdqlUtils.getType( literal, AdqlData.LITERAL_TYPE ) ) ;
////                AdqlUtils.set( atomType, "Literal", literal ) ;
////            }
//                
//        }
   
        
        public void setValue( String newValue ) {
            String v = newValue.trim() ;           
            XmlObject literal = AdqlUtils.get( atomType, "Literal" ) ;
            if( literal == null ) {
                literal = AdqlUtils.addNew( atomType, "Literal" ) ;
            }         
            XmlObject value = null ;
            Success: {
                // First try setting an IntegerType....
                try {
                    Long lv = new Long( v ) ;
                    literal = literal.changeType( AdqlUtils.getType( atomType, AdqlData.INTEGER_TYPE ) ) ;
                    value = AdqlUtils.newInstance( XmlLong.type ) ;        
                    break Success;
                }
                catch( NumberFormatException nfex ) { ; }
                // If we fail try setting a RealType....
                try {
                    Double dv = new Double( v ) ;
                    literal = literal.changeType( AdqlUtils.getType( atomType, AdqlData.REAL_TYPE ) ) ;
                    value = AdqlUtils.newInstance( XmlDouble.type ) ;
                    break Success;
                }
                catch( NumberFormatException nfex ) { ; }
                // If we get this far we default to a String...  
                literal = literal.changeType( AdqlUtils.getType( literal, AdqlData.STRING_TYPE ) ) ;
                value = AdqlUtils.newInstance( XmlString.type ) ;  
                v = stripQuotes( v ) ;
            }
            ((org.apache.xmlbeans.XmlAnySimpleType)value).setStringValue( v ) ;
            AdqlUtils.set( literal, "Value", value ) ;                
        }
        
        public String _formatDisplay() {
            StringBuffer buffer = new StringBuffer() ;
            String 
            	units = this.getUnits(),
            	value = this.getValue() ;          
            if( units != null ) {
                buffer.append( "Units: " ).append( units ).append( ", " ) ;
            } 
            buffer.append( "Value: " ).append( value ) ;
            return buffer.toString() ;
        }
        public String formatDisplay() {
            return this.getValue() ;          
        }
        
        private String stripQuotes( String value ) {
            String retValue = value ;
            if( value == null || value.length() < 2 )
                return value ;
            if( ( value.startsWith( "'" ) && value.endsWith( "'") )
                 ||
                ( value.startsWith( "\"" ) && value.endsWith( "\"") ) ) {
                retValue = value.substring(1,value.length()-1) ;
            }
            return retValue ;
        }
        
        private String unstripQuotes( String value ) {
            String retValue = value ;
            if( value == null )
                return value ;
            Success: {
                // First see whether this string could be construed as an IntegerType....
                try {
                    Long lv = new Long( value ) ;
                    retValue = (new StringBuffer()).append('\'').append( value ).append('\'').toString() ;
                    break Success;
                }
                catch( NumberFormatException nfex ) { ; }
                // If not, could it be construed as a RealType....
                try {
                    Double dv = new Double( value ) ;
                    retValue = (new StringBuffer()).append('\'').append( value ).append('\'').toString() ;
                    break Success;
                }
                catch( NumberFormatException nfex ) { ; }          
            }
            return retValue ;
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
}