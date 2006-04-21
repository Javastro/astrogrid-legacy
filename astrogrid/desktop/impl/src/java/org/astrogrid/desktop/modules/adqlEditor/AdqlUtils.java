/* AdqlUtils.java
 * Created on 21-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor ;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeSystem;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlDecimal;
import org.apache.xmlbeans.XmlDouble;
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlInteger;
import org.apache.xmlbeans.XmlLong;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlPositiveInteger;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.XmlUnsignedLong;
import org.apache.xmlbeans.XmlUnsignedShort;

// import org.astrogrid.adql.v1_0.beans.* ;

/**
 * @author jl99
 *
 * 
 */
public final class AdqlUtils {
    
    private static final Log logger = LogFactory.getLog( AdqlUtils.class ) ;
    private static final boolean DEBUG_ENABLED = false ;
    private static final boolean TRACE_ENABLED = false ;
    
    private static final String EMPTY_STRING = "".intern() ;
     
    /**
     * 
     */
    private AdqlUtils() {}
    
    static public SchemaType[] getContainedSubtypes( XmlObject anObject ) {
        return getContainedSubtypes( anObject.schemaType() ) ;
    }
    
    static public SchemaType[] getContainedSubtypes( SchemaType aType ) {
        if( aType.isSimpleType() )
            return new SchemaType[0] ;
        SchemaType[] containedTypes = null ;
        SchemaParticle particle = aType.getContentModel() ;
        if( particle == null )
            return new SchemaType[0] ;
        int childCount = particle.countOfParticleChild() ;
        if( childCount == 0 ) {
            // Not too sure about this
            containedTypes = new SchemaType[ 1 ] ;
            containedTypes[0] = particle.getType() ;
        }
        else {
            containedTypes = new SchemaType[ childCount ] ;
            SchemaParticle[] particles = particle.getParticleChildren() ;
            for(int i=0; i<childCount; i++ ) {
                containedTypes[i] = particles[i].getType() ;
            }
        }
        return containedTypes ;
    }
    
    
    static public SchemaType[] getConcreteSubtypes( XmlObject anObject ) {
        return getConcreteSubtypes( anObject.schemaType() ) ;
    }
    
    static public SchemaType[] getConcreteSubtypes( SchemaType aType ) {
        ArrayList list = new ArrayList() ;
        findSubtypes( list, aType ) ;
        SchemaType[] foundTypes = new SchemaType[ list.size() ] ;
        ListIterator iterator ;
        int i = 0 ;
        for( iterator=list.listIterator(); iterator.hasNext(); i++) {
            foundTypes[i] = (SchemaType)iterator.next() ;
        }
        return foundTypes ;
    }
    
    static private void findSubtypes( ArrayList list, SchemaType aType ){
        if( aType.isSimpleType() ) 
            return ;
//        SchemaParticle particle = aType.getContentModel() ;
//        SchemaType subtype ;
//        if( particle == null ) 
//            return ;
//        subtype = particle.getType() ;
//        if( subtype == null )
//            return ;
        SchemaTypeSystem typeSystem = aType.getTypeSystem() ;
        SchemaType[] types = typeSystem.globalTypes() ;
        
        for( int i=0 ; i< types.length; i++ ) {
            if( aType.isAssignableFrom( types[i] ) 
                &&
                !aType.getName().equals( types[i].getName() ) ) {
                if( types[i].isAbstract() == false 
                    &&
                    !list.contains(types[i]) ) {
                    list.add( types[i] ) ;
                }
                findSubtypes( list, types[i] ) ;
            }
        }
           
    }
    
    
    static public SchemaLocalAttribute[] getAttributes( XmlObject anObject ) {
        return getAttributes( anObject.schemaType() )  ;
    }
    
    static public SchemaLocalAttribute[] getAttributes( SchemaType aSchemaType ) {
        SchemaLocalAttribute[] retArray = null ;
        if( aSchemaType.isSimpleType() ) {
            retArray = new SchemaLocalAttribute[0] ;
        }
        else {
            SchemaAttributeModel model = aSchemaType.getAttributeModel() ;
            if( model != null ) {
                retArray = model.getAttributes() ;
            }
            else {
                retArray = new SchemaLocalAttribute[0] ;
            }
        }
        return retArray ;
    }
    
    
    static public SchemaType getAttributeType( String unqualifiedName, SchemaTypeSystem typeSystem ) {
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
    
    
    static public String getAttributeName( SchemaType childType, SchemaType attributeType ) {
        SchemaProperty[] attrProperties = childType.getAttributeProperties() ;
        for( int i=0; i<attrProperties.length; i++ ) {
            if( areTypesEqual( attrProperties[i].getType(), attributeType ) ) {
                return attrProperties[i].getJavaPropertyName() ;
            }
        }
        return null ;
    }
    
    
    public static boolean isEnumerated( SchemaType type ) {
        if( isDrivenByEnumeratedAttribute( type )
            ||
            isEnumeratedElement( type ) ) {
            return true ;
        }
        return false ;
    }
    
    public static boolean isEnumerated( XmlObject xmlObject ) {
        return isEnumerated( xmlObject.schemaType() ) ;
    }
    
    public static boolean isDrivenByEnumeratedAttribute( SchemaType type ) {
        try {
            return AdqlData.ENUMERATED_ATTRIBUTES.containsKey( type.getName().getLocalPart() ) ;
        }
        catch( Exception ex ) {
            ;
        }          
        return false ;
    }
    
    public static boolean isDrivenByEnumeratedAttribute( XmlObject xmlObject ) {    
        return isDrivenByEnumeratedAttribute( xmlObject.schemaType() ) ;
    }
    
    
    // Use org.apache.xmlbeans.SchemaLocalAttribute to test the return value:
    // OPTIONAL, PROHIBITED or REQUIRED 
    public static int getAttributeUsage( XmlObject element, String attributeName ) {
        QName qName = new QName( attributeName ) ;
        return element.schemaType().getAttributeModel().getAttribute( qName  ).getUse() ;
    }
    
    public static boolean isOptionalAttribute( XmlObject element, String attributeName ) {
        return ( getAttributeUsage( element, attributeName ) == SchemaLocalAttribute.OPTIONAL ) ;
    }
    
    public static boolean isRequiredAttribute( XmlObject element, String attributeName  ) {
        return ( getAttributeUsage( element, attributeName ) == SchemaLocalAttribute.REQUIRED ) ;
    }
    
    
    
    public static boolean isEnumeratedElement( SchemaType type ) {
        try {
            return AdqlData.ENUMERATED_ATTRIBUTES.containsKey( type.getName().getLocalPart() ) ;
        }
        catch( Exception ex ) {
            ;
        }          
        return false ;
    }
    
    public static boolean isEnumeratedElement( XmlObject xmlObject ) {    
        return isEnumeratedElement( xmlObject.schemaType() ) ;
    }
    
    
    public static boolean isCascadeable( XmlObject xmlObject ) {
        return isCascadeable( xmlObject.schemaType() ) ;
    }
    
    public static boolean isCascadeable( SchemaType type ) {
        boolean answer = false ;   
        try {
            answer = AdqlData.CASCADEABLE.containsKey( type.getName().getLocalPart() ) ;
        }
        catch( Exception ex ) {
            ;
        }                          
        return answer ;
    }
    
    public static boolean isColumnLinked( XmlObject xmlObject ) {
        return isColumnLinked( xmlObject.schemaType() ) ;
    }
    
    public static boolean isColumnLinked( SchemaType type ) {
        boolean answer = false ;     
        try {
            answer = AdqlData.METADATA_LINK_COLUMN.containsKey( type.getName().getLocalPart() ) ;
        }
        catch( Exception ex ) {
            ;
        }          
        return answer ;
    }
    
    // NB: This does not deal with element context, only type.
    public static boolean isSupportedType( SchemaType type ) {
        boolean retValue = false ;
        try {
            retValue = !AdqlData.UNSUPPORTED_TYPES.containsKey( type.getName().getLocalPart() ) ;
        }
        catch( Exception ex ) {
            ;
        }
        return retValue ;
    }
   
      
    public static boolean isTableLinked( XmlObject xmlObject ) {
        return isTableLinked( xmlObject.schemaType() ) ;
    }
    
    
    public static boolean isTableLinked( SchemaType type  ) {
        boolean answer = false ;
        try {
            answer = AdqlData.METADATA_LINK_TABLE.containsKey( type.getName().getLocalPart() ) ;
        }
        catch( Exception ex ) {
            ;
        }             
        return answer ;
    }
    
    
    static public String extractDisplayName( String name ) {
        String retValue = (String)AdqlData.T2D_NAMES.get( name ) ; ;
        return ( retValue == null ? EMPTY_STRING : retValue ) ;
    }
    
    static public String extractDisplayName( XmlObject xmlObject ) {
        String retValue = extractDisplayName( xmlObject.schemaType() ) ;
        if( retValue == EMPTY_STRING ) {
            retValue = extractDisplayNameWithCursor( xmlObject ) ;
        }    
        return retValue ;
    }
    
    static public String extractDisplayNameWithCursor( XmlObject xmlObject ) {
        String retValue = null ;
        XmlCursor cursor = xmlObject.newCursor() ;
        retValue = extractDisplayName( cursor ) ;
        cursor.dispose() ;
        return retValue ;
    }
    
    static public String extractDisplayName( SchemaType schemaType ) {
        String retValue = null ;
        if( schemaType.isAnonymousType() ) {
            retValue = EMPTY_STRING ;
        }
        else {
            retValue = (String)AdqlData.T2D_NAMES.get( schemaType.getName().getLocalPart() ) ;
            if( retValue == null )
                retValue = EMPTY_STRING ;
        }
        return retValue ;
    }
    
    static public String extractDisplayName( XmlCursor cursor ) {
        String name = null ;
        String elementName = null ;
        if( !cursor.currentTokenType().isStart() ) 
            cursor.toFirstChild(); 
        cursor.push() ;
        try {
            elementName = cursor.getName().getLocalPart() ;
        }
        catch ( Exception ex ) {
            elementName = EMPTY_STRING ;
//            cursor.pop();
//            System.out.println( "Printing out cursor tokens..." ) ;
//            while( !cursor.toNextToken().isNone() ) {
//                System.out.println( cursor.currentTokenType().toString() ) ;
//                System.out.println( cursor.getName() ) ;
//            }
//            System.out.println( "\n==== Stack trace follows... ====" ) ;
//            ex.printStackTrace() ;
        }
        cursor.pop() ;
//        name = cursor.getAttributeText( new QName( "label" ) ) ;
//        if( name == null || name.length() == 0 ) {
//            try {
//                name = cursor.getName().getLocalPart() ;
//            }
//            catch( Exception ex ) {
//                ;
//            }
            if( cursor.toFirstAttribute() ) {
                do {
                    if( cursor.getName().getLocalPart().equals( "type" ) ) {
                        name = cursor.getTextValue() ;
                        // Removes any qualified namespace stuff from the value
                        // (Possible for attributes!)...
                        String [] parts = name.split( ":" ) ;
                        if( parts.length > 1 )
                            name = parts[1] ;
                        break ;
                    }
                } while( cursor.toNextAttribute() ) ;
            }
//        }
        if( name != null )
            name = (String)AdqlData.T2D_NAMES.get( name ) ;
        if( name == null )
            name = (String)AdqlData.T2D_NAMES.get( elementName ) ;
        if( name == null )
            name = EMPTY_STRING ;
        return name ; 
    }
    
    
    static public String extractElementLocalName( XmlObject xmlObject ) {
        String elementName = null ;
        XmlCursor cursor = xmlObject.newCursor() ; 
        if( !cursor.currentTokenType().isStart() ) 
            cursor.toFirstChild(); 
        cursor.push() ;
        try {
            elementName = cursor.getName().getLocalPart() ;
        }
        catch ( Exception ex ) {
            elementName = EMPTY_STRING ;
        }
        cursor.dispose() ;
        return elementName ;
    }
    
    static public boolean localNameEquals( XmlObject o, String name ) {
        SchemaType type = o.schemaType() ;
        if( type.isBuiltinType() || type.isAnonymousType() )
            return false ;
        if( type.getName().getLocalPart().equals( name ) ) 
            return true ;
        return false ;
    }
    
    static public String getLocalName( XmlObject object ) {
        return getLocalName( object.schemaType() ) ; 
    }
    
    static public String getLocalName( SchemaType type ) {
        return type.getName().getLocalPart();
    }
    
    
    static public SchemaType getType( XmlObject object, String localName ) {
        return getType( object.schemaType(), localName ) ;
    }
    
    static public SchemaType getType( SchemaType someType, String localName ) {
        SchemaType target = null ;
        SchemaType[] globalTypes = someType.getTypeSystem().globalTypes() ;
        for( int i=0; i<globalTypes.length; i++ ) {
            if( globalTypes[i].getName().getLocalPart().equals( localName ) ) {
                target = globalTypes[i] ;
                break ;
            }
        }
        return target ;
    }
    
    static public boolean areTypesEqual( SchemaType typeOne, SchemaType typeTwo ) {
        return typeOne.getName().equals( typeTwo.getName() ) ;
    }
    
    
    static public boolean areTypesEqual( SchemaType typeOne, String typeTwoLocalName ) {
        return areTypesEqual( typeOne, getType( typeOne, typeTwoLocalName ) ) ;
    }
    
    
    static public String normalizeName( String name ) {
        //
        // Removes the redundant "Type" word from the end...
        int index = name.lastIndexOf( "Type" ) ;
        if( index > 0 )
            name = name.substring( 0, index ) ;
        //
        // Removes any qualified namespace stuff from the value
        // (Possible for attributes!)...
        String [] parts = name.split( ":" ) ;
        if( parts.length > 1 )
            name = parts[1] ;
        //
        // Splits words into normal English spacing
        // and ensures the name begins with a capital...
        StringBuffer buffer = new StringBuffer() ;
        char[] chars = name.toCharArray() ;
        buffer.append( Character.toUpperCase( chars[0] ) ) ;
        for( int i=1; i<chars.length; i++ ) {
            if( Character.isUpperCase( chars[i] ) ) {
                buffer.append( ' ' ) ;
            }
            buffer.append( chars[i] ) ;
        }
        return buffer.toString() ;
    }
    
    
    static public XmlObject get( XmlObject o, String name ) {
        return (XmlObject)invoke( o, "get"+capitalize( name ), null, null ) ;
    }
    
    static public void set( XmlObject o, String name, XmlObject param ) {
        invoke( o, "set"+capitalize( name ), new Object[] { param }, new Class[] { param.getClass() } ) ;
    }
    
    static public boolean isSet( XmlObject o, String name ) {
        return ((Boolean)invoke( o, "isSet"+capitalize( name ), null, null )).booleanValue() ;
    }
    
    static public void unset( XmlObject o, String name ) {
        invoke( o, "unset"+capitalize( name ), null, null ) ;
    }
    
    static public XmlObject addNew( XmlObject o, String name ) {
        return (XmlObject)invoke( o, "addNew"+capitalize(name), null, null ) ;
    }
    
    static public Object[] getArray( XmlObject o, String name ) {
        return (Object[])invoke( o, "get"+capitalize(name)+"Array" , null, null ) ;
    }
    
    static public Object getArray( XmlObject o, String name, int param ) {
        return invoke( o, "get"+capitalize( name )+"Array", new Object[]{new Integer(param)}, new Class[]{ Integer.TYPE } ) ;
    }  
    
    static public int sizeOfArray( XmlObject o, String name ) {
        return ((Integer)invoke( o, "sizeOf"+capitalize( name )+"Array", null, null )).intValue() ;
    }
    
    
    static public void setArray( XmlObject o, String name, XmlObject[] array ) {
        //
        // This may well not work because of the difficulty of setting the correct
        // interface within the call for the array object.
        // In fact the array argument may require reformatting in order to work.
        // Requires experimentation!!!
        invoke( o, "set"+capitalize( name )+"Array", new Object[]{ array }, new Class[] { array.getClass() } ) ;
    }
    
    // void setItemArray(int i, net.ivoa.xml.adql.v10.SelectionItemType item);
    static public Object setArray( XmlObject o, String name, int param1, XmlObject param2 ) {
        return invoke( o
                     , "set"+capitalize( name )+"Array"
                     , new Object[]{ new Integer(param1), param2 }
                     , new Class[] { Integer.TYPE, param2.getClass() } ) ;
    } 
    
    // net.ivoa.xml.adql.v10.SelectionItemType insertNewItem(int i);
    static public XmlObject insertNewInArray( XmlObject o, String name, int param ) {
        return (XmlObject)invoke( o
                                , "insertNew"+capitalize( name )
                                , new Object[]{ new Integer(param) }
                                , new Class[] { Integer.TYPE } ) ;
    }
    
    // net.ivoa.xml.adql.v10.SelectionItemType addNewItem();
    static public XmlObject addNewToEndOfArray( XmlObject o, String name ) {
        return (XmlObject)invoke( o, "addNew"+capitalize( name ), null, null ) ;
    }
    
    // void removeItem(int i);
    static public void removeFromArray( XmlObject o, String name, int param ) {
        invoke( o
              , "remove"+capitalize( name )
              , new Integer[]{ new Integer(param) }
              , new Class[] { Integer.TYPE } ) ;
    }
    
    static public XmlObject newInstance( SchemaType st ) {
        return XmlObject.Factory.newInstance().changeType( st ) ;
    }
    
    static private String capitalize( String name ) {
        return (name.substring(0,1).toUpperCase() + name.substring(1)).trim() ;
    }
        
    static private Object[] getInterfaces( Class cls ) {
        ArrayList iList = new ArrayList() ;
        Class[] interfaces = cls.getInterfaces() ;
        for( int i=0; i<interfaces.length; i++ ) {
            getInterfaces( interfaces[i], iList ) ;
        }
        return iList.toArray() ;
    }
    
    static private void getInterfaces( Class iFace, ArrayList iList ) {
        iList.add( iFace ) ;
        Class[] interfaces = iFace.getInterfaces() ;
        for( int i=0; i<interfaces.length; i++ ) {
            if( iList.contains( interfaces[i] ) )
                continue ;
            getInterfaces( interfaces[i], iList ) ;
        }
    }
    

    static private Method getMethod( Object o, String methodName, Class[] paramTypes ) {
        Method method = null ;
        if( paramTypes == null ) {
            try {
                method = o.getClass().getMethod( methodName, paramTypes ) ;
            }
            catch( Exception ex ) {
                ;
            } 
        }
        else {
            Class[] paramTypes2 = new Class[ paramTypes.length ] ;
            findMethod: for( int i=0; i<paramTypes.length; i++ ) {
                Object[] cls = getInterfaces( paramTypes[i] ) ;
                if( cls.length == 0 ) {
                    cls = new Object[1] ;
                    cls[0] = paramTypes[i] ;
                }
                for( int j=0; j<cls.length; j++ ) {
                    System.arraycopy( paramTypes, 0,  paramTypes2, 0, paramTypes.length );
                    paramTypes2[i] = (Class)cls[j] ;
                    try {
                        method = o.getClass().getMethod( methodName, paramTypes2 ) ;
                        break findMethod ;
                    }
                    catch( Exception ex ) {
                        ;
                    }         
                } // end for
            }  // end findMethod for 
        }      
        return method ;
    }
    
    
    static private Object invoke( Object o, String methodName, Object[] params, Class[] paramTypes ) {
        Object retObj = null ;
        Method method = null ;

        if( methodName.startsWith( "set" ) || methodName.startsWith( "get" ) ) {
            method = getMethod( o, 'x' + methodName, paramTypes ) ;
        }
        if( method == null ) {
            method = getMethod( o, methodName, paramTypes ) ;
        }
        if( method != null ) {
            try {
                retObj = method.invoke( o, params ) ;
            }
            catch( java.lang.reflect.InvocationTargetException itx ) {
                logger.error( "AdqlUtils.invoke: ", itx.getCause() ) ;
            }
            catch( Exception ex2 ) {
                logger.error( "AdqlUtils.invoke: ", ex2 );
            }
        }
        return retObj ;
    }
    
    
    public static boolean isSuitablePasteIntoTarget( AdqlEntry entry, XmlObject clipboardObject ) {
        return isSuitablePasteIntoTarget( entry, clipboardObject.schemaType() ) ;
    }

    public static boolean isSuitablePasteIntoTarget( AdqlEntry entry, SchemaType clipboardType ) {
       return ( findSuitablePasteIntoTarget( entry, clipboardType ) != null ) ;
    }
    
    public static SchemaType findSuitablePasteIntoTarget( AdqlEntry entry, XmlObject clipboardObject ) {
       return  findSuitablePasteIntoTarget( entry, clipboardObject.schemaType() ) ;
    }
    
    public static SchemaType findSuitablePasteIntoTarget( AdqlEntry entry, SchemaType clipboardType ) {
        SchemaProperty[] elementProperties = entry.getElementProperties() ;
        for ( int i = 0 ; i < elementProperties.length ; i++ ) {
            if( elementProperties[i].getType().isAssignableFrom( clipboardType ) ) { 
                return elementProperties[i].getType() ;
            }
        }
        return null ;
    }

    public static boolean isSuitablePasteOverTarget( AdqlEntry entry, XmlObject pasteObject ) {
        return isSuitablePasteOverTarget( entry, pasteObject.schemaType() ) ;
    }

    public static boolean isSuitablePasteOverTarget( AdqlEntry entry, SchemaType pasteType ) {
        SchemaType entryType = entry.getXmlObject().schemaType() ;
        // Checks that the entry type derives from or is the same type as paste type:
        if( entryType.isAssignableFrom( pasteType ) )
            return true ;
        // Checks that the entry type and the paste type share a base type OTHER THAN the any type:
        // ( This is probably not sufficient in all circumstances. Better would be to test whether
        //   the entry type was a member of an array with a base type that matched the common
        //   base type. Worth thinking over. )
        if( !entryType.getCommonBaseType( pasteType ).getName().equals( XmlObject.type.getName() ) ) {
            return true ;
        }
        return false ;
    }

    public static XmlObject setDefaultValue( XmlObject xmlObject ) {
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

    public static XmlObject setBuiltInDefaults( XmlObject xmlObject ) {
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

    public static boolean isAttributeDriven( SchemaType type ) {
        String[] name = (String[])AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
        return ( name != null && name.length == 1 ) ;
    }

    public static XmlObject setAttributeDrivenDefaults( XmlObject xmlObject ) {
        XmlObject retVal = xmlObject ;
        SchemaType type = xmlObject.schemaType() ;
        String[] attributeNames = (String[])AdqlData.EDITABLE.get( type.getName().getLocalPart() ) ;
        XmlString tempObject = XmlString.Factory.newInstance() ;
        tempObject.setStringValue( (String)AdqlData.ATTRIBUTE_DEFAULTS.get( type.getName().getLocalPart() ) ) ; 
        
        //
        // There is a better way to do this provided I can get at the fully qualified name
        // of the attribute. Needs thinking about.
        // Another point. The schema may itself define a default value. This too can be picked up.
        SchemaType attrType = null ;
        SchemaProperty[] attrProperties = type.getAttributeProperties() ;
        for( int i=0; i<attrProperties.length; i++ ) {
            if( attrProperties[i].getJavaPropertyName().equals( attributeNames[0] ) ) {
                attrType = attrProperties[i].getType();
                break ;
            }
        }
        XmlObject valueObject = tempObject.changeType( attrType ) ;        
        set( xmlObject, attributeNames[0], valueObject ) ; 
        return xmlObject ;
    }

    public static XmlObject setDerivedSimpleDefaults( XmlObject xmlObject ) {
        XmlObject retVal = xmlObject ;
        SchemaType type = xmlObject.schemaType() ;
        // Cooerce the empty element into an XmlString...
        XmlString tempObject = (XmlString)xmlObject.changeType( XmlString.type ) ;
        tempObject.setStringValue( (String)AdqlData.DERIVED_DEFAULTS.get( type.getName().getLocalPart() ) ) ;
        // Cooerce back to original type...
        retVal = tempObject.changeType( type ) ;
        return retVal ;
    }

}
