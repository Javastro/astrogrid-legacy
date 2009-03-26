package org.astrogrid.desktop.modules.adqlEditor.nodes ;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlLong;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.astrogrid.desktop.modules.adqlEditor.AdqlData;
import org.astrogrid.desktop.modules.adqlEditor.AdqlTree;
import org.astrogrid.desktop.modules.adqlEditor.AdqlUtils;

/**
 * 
 */
public class AtomNode extends HidingNode {
    
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    private static final Log log = LogFactory.getLog( AtomNode.class ) ;
    
    AtomNode( NodeFactory nodeFactory, AdqlNode parent, XmlObject o ) throws HidingNode.UnsupportedObjectException {
        super( nodeFactory, parent, o ) ;
    }
    
    AtomNode( NodeFactory nodeFactory, AdqlNode parent, XmlObject o, int childNodeIndex ) throws HidingNode.UnsupportedObjectException {
        super( nodeFactory, parent, o, childNodeIndex ) ;
    }
    
    @Override
    public boolean isBottomLeafEditable() {
        return true ;
    }
    
    @Override
    public String toHtml( boolean expanded, boolean leaf, AdqlTree tree ) { 
        String displayInfo = null;
        try {
            displayInfo = dealWithPatternContext( AdqlUtils.extractDisplayName( getXmlObject() ) ) ;
        }
        catch( Exception ex ) {
            log.debug( "AtomNode.toHtml()", ex ) ;
        }
        if( expanded ) {
            displayInfo = displayInfo + ' ' + formatDisplay() ;       
        }
        else {
            String hcName = AdqlUtils.extractDisplayName( getXmlObject() ) ;
            displayInfo = hcName + ' ' + formatDisplay()  ;
        }
        
        if( displayInfo.trim().length() == 0 ) {
            displayInfo = "NO NAME" ;
        } 
        return displayInfo ;
    }
    
    private String dealWithPatternContext( String displayName ) {
        String elementName = null ;
        XmlCursor cursor = getXmlObject().newCursor() ;
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
    
    
    public String getUnits() {
        XmlObject retValObj = null ;
        XmlObject atomType = getXmlObject() ;
        String retVal = null ;
        boolean isSet = AdqlUtils.isSet( atomType, "Unit" ) ;
        if( isSet ) {
            retValObj = AdqlUtils.get( atomType, "Unit" ) ;
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
        XmlObject o = AdqlUtils.get( getXmlObject(), "Literal" ) ;
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
    
//    public void setValue( String newValue ) {
//        String v = newValue.trim() ;           
//        XmlObject literal = null ;
//        XmlObject value = null ;
//        Success: {
//            // First try setting a IntegerType....
//            try {
//                Long lv = new Long( v ) ;
//                literal = AdqlUtils.newInstance( AdqlUtils.getType( atomType, AdqlData.INTEGER_TYPE ) ) ;
//                value = AdqlUtils.newInstance( XmlLong.type ) ;        
//                break Success;
//            }
//            // If we fail try setting a RealType....
//            catch( NumberFormatException nfex ) { ; }
//            try {
//                Double dv = new Double( v ) ;
//                literal = AdqlUtils.newInstance( AdqlUtils.getType( atomType, AdqlData.REAL_TYPE ) ) ;
//                value = AdqlUtils.newInstance( XmlDouble.type ) ;
//                break Success;
//            }
//            catch( NumberFormatException nfex ) { ; }
//            // If we get this far we default to a String...
//            literal = AdqlUtils.newInstance( AdqlUtils.getType( atomType, AdqlData.STRING_TYPE ) ) ;
//            value = AdqlUtils.newInstance( XmlString.type ) ;
//            if( literal != null && value != null ) {
//                ((org.apache.xmlbeans.XmlAnySimpleType)value).setStringValue( v ) ;
//                AdqlUtils.set( literal, "Value", value ) ;
//                literal = literal.changeType( AdqlUtils.getType( literal, AdqlData.LITERAL_TYPE ) ) ;
//                AdqlUtils.set( atomType, "Literal", literal ) ;
//                literal = literal.changeType( AdqlUtils.getType( literal, AdqlData.STRING_TYPE ) ) ;
//            }
//        }
////        if( literal != null && value != null ) {
////            ((org.apache.xmlbeans.XmlAnySimpleType)value).setStringValue( v ) ;
////            AdqlUtils.set( literal, "Value", value ) ;
////            literal = literal.changeType( AdqlUtils.getType( literal, AdqlData.LITERAL_TYPE ) ) ;
////            AdqlUtils.set( atomType, "Literal", literal ) ;
////        }
//            
//    }

    
    public void setValue( String newValue ) {
        String v = newValue.trim() ; 
        XmlObject atomType = getXmlObject() ;
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