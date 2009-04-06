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
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Matcher;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaParticle;
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
import org.apache.xmlbeans.XmlPositiveInteger;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlUnsignedInt;
import org.apache.xmlbeans.XmlUnsignedLong;
import org.apache.xmlbeans.XmlUnsignedShort;
import org.astrogrid.adql.v1_0.beans.AggregateFunctionType;
import org.astrogrid.adql.v1_0.beans.ArrayOfFromTableType;
import org.astrogrid.adql.v1_0.beans.JoinTableType;
import org.astrogrid.adql.v1_0.beans.MathFunctionType;
import org.astrogrid.adql.v1_0.beans.SelectType;
import org.astrogrid.adql.v1_0.beans.TrigonometricFunctionType;
import org.astrogrid.desktop.modules.adqlEditor.commands.CopyHolder;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;

// import org.astrogrid.adql.v1_0.beans.* ;

/**
 * @author jl99
 *
 * 
 */
public final class AdqlUtils {
    
    private static final Log log = LogFactory.getLog( AdqlUtils.class ) ;
    
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
        ArrayList<SchemaType> list = new ArrayList<SchemaType>() ;
        findSubtypes( list, aType ) ;
        SchemaType[] foundTypes = new SchemaType[ list.size() ] ;
        ListIterator<SchemaType> iterator ;
        int i = 0 ;
        for( iterator=list.listIterator(); iterator.hasNext(); i++) {
            foundTypes[i] = iterator.next() ;
        }
        return foundTypes ;
    }
    
    static public String[] getEnumValuesGivenDrivingType( SchemaType drivingType ) {    
        SchemaStringEnumEntry[] stringEnumEntries = drivingType.getStringEnumEntries() ;
        ArrayList<String> enumList = new ArrayList<String>( stringEnumEntries.length + 2 ) ;
        String[] enumStrings = null ;
        String entry = null ;
        for( int j=0; j<stringEnumEntries.length; j++ ) {
            entry = stringEnumEntries[j].getString() ;
            if( !AdqlData.ENUM_FILTERED_VALUES.containsKey( entry ) ) {
                enumList.add( entry ) ;
                if( isEnumValueSynonymed(  entry ) ) {
                    String synonym = getSlaveEnumSynonym( entry ) ;
                    if( synonym != entry ) { //@todo NWW - do you mean .equals() here?
                        enumList.add( synonym ) ;
                    }
                }
            }
        }
        enumStrings = new String[ enumList.size() ] ;
        enumStrings = enumList.toArray( enumStrings );
        return enumStrings ;
    }
    
    static public SchemaType getEnumeratedAttributeTypeGivenDrivenType( SchemaType drivenType ) {
        String drivenTypeName = getLocalName( drivenType ) ;
        String attrbTypeName = (String)AdqlData.ENUMERATED_ATTRIBUTES.get( drivenTypeName ) ;
        SchemaType attrbSchemaType = getAttributeType( attrbTypeName, drivenType.getTypeSystem() ) ;
        return attrbSchemaType ;
    }
    
    static public String[] getEnumValuesGivenDrivenType( SchemaType drivenType ) {
        String drivenLocalTypeName = getLocalName( drivenType ) ;
        String drivingLocalTypeName = (String)AdqlData.ENUMERATED_ATTRIBUTES.get( drivenLocalTypeName ) ;
        if( drivingLocalTypeName == null || drivingLocalTypeName.length() == 0 ) {
            drivingLocalTypeName = AdqlData.ENUMERATED_ELEMENTS.get( drivenLocalTypeName ) ;
        }
        SchemaType drivingType = getType( drivenType, drivingLocalTypeName ) ;
        return getEnumValuesGivenDrivingType( drivingType ) ;
    }
    
    static private void findSubtypes( ArrayList<SchemaType> list, SchemaType aType ){
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
            if( getLocalName( sgTypes[i] ).equals( unqualifiedName ) ) {
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
            isDrivenByEnumeratedElement( type ) ) {
            return true ;
        }
        return false ;
    }
    
    public static boolean isEnumerated( XmlObject xmlObject ) {
        return isEnumerated( xmlObject.schemaType() ) ;
    }
    
    public static boolean isDrivenByEnumeratedAttribute( SchemaType type ) {
        try {
            return AdqlData.ENUMERATED_ATTRIBUTES.containsKey( getLocalName( type ) ) ;
        }
        catch( Exception ex ) {
            ;
        }          
        return false ;
    }
    
    public static boolean isDrivenByEnumeratedAttribute( XmlObject xmlObject ) {    
        return isDrivenByEnumeratedAttribute( xmlObject.schemaType() ) ;
    }
    
    
    public static boolean isEditable( SchemaType type ) {
        try {
            if( type.isAnonymousType() ){
                return false ;
            }
            else if( type.isSimpleType() ) {
                return true ;
            }
            else if( AdqlData.EDITABLE_ATTRIBUTES.containsKey( getLocalName( type ) ) ) {
                return true ;
            }
            else if( AdqlData.EDITABLE_ELEMENTS.containsKey( getLocalName( type ) ) ) {
                return true ;
            }
        }
        catch( Exception ex ) {
            ;
        }          
        return false ;
    }
    
    public static boolean isEditable( XmlObject xmlObject ) {    
        return isEditable( xmlObject.schemaType() ) ;
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
    
    
    public static boolean isRegularIdentifier( String name ) {
        boolean retVal = false ;      
        Matcher m = AdqlData.REGULAR_IDENTIFIER.matcher( name );
        if( m.matches() ) {
            if( !AdqlData.ADQL_RESERVED_WORDS.contains( name.toUpperCase() ) ) {
                retVal = true  ;
            }
        }
        return retVal ;
    }
    
    public static XmlObject modifyQuotedIdentifiers( XmlObject xmlObject ) {
        XmlCursor cursor = xmlObject.newCursor() ;
        XmlObject element ;
        cursor.toStartDoc() ;
        cursor.toFirstChild() ; // There has to be a first child!
        do {
            if( cursor.isStart() ) {
                element = cursor.getObject() ;
                if( AdqlUtils.isColumnLinked( element ) ) {
                    setPossibleModifiedAttributeValue( element, "table" ) ;
                    setPossibleModifiedAttributeValue( element, "name" ) ;      
                } 
                else if( AdqlUtils.isTableLinked( element ) ) {
                    setPossibleModifiedAttributeValue( element, "alias" ) ;
                    setPossibleModifiedAttributeValue( element, "name" ) ;       
                }
            }                 
        } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
        cursor.dispose() ;       
        return xmlObject ;
    }
    
    public static XmlObject unModifyQuotedIdentifiers( XmlObject xmlObject ) {
        XmlCursor cursor = xmlObject.newCursor() ;
        XmlObject element ;
        cursor.toStartDoc() ;
        cursor.toFirstChild() ; // There has to be a first child!
        do {
            if( cursor.isStart() ) {
                element = cursor.getObject() ;
                if( AdqlUtils.isColumnLinked( element ) ) {
                    unsetPossibleModifiedAttributeValue( element, "table" ) ;
                    unsetPossibleModifiedAttributeValue( element, "name" ) ;      
                } 
                else if( AdqlUtils.isTableLinked( element ) ) {
                    unsetPossibleModifiedAttributeValue( element, "alias" ) ;
                    unsetPossibleModifiedAttributeValue( element, "name" ) ;       
                }
            }                 
        } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
        cursor.dispose() ;       
        return xmlObject ;
    }
    
    
    
    private static void setPossibleModifiedAttributeValue( XmlObject element, String attrName ) {        
        SimpleValue sv = (SimpleValue)AdqlUtils.get( element, attrName ) ;
        String id ;
        if( sv != null ) {
            id = sv.getStringValue() ;
            if( id != null ) {
                if( !AdqlUtils.isRegularIdentifier( id ) ) {
                    id = "\"" + id + "\"" ;
                }
                AdqlUtils.set( element, attrName, XmlString.Factory.newValue( id ) ) ;
            }           
        }
    }
    
    private static void unsetPossibleModifiedAttributeValue( XmlObject element, String attrName ) {        
        SimpleValue sv = (SimpleValue)AdqlUtils.get( element, attrName ) ;
        String id ;
        if( sv != null ) {
            id = sv.getStringValue() ;
            if( id != null ) {
                if( id.startsWith("\"") ) {
                    id = id.substring(1, id.length()-1 ) ;
                }
                AdqlUtils.set( element, attrName, XmlString.Factory.newValue( id ) ) ;
            }           
        }
    }
    
    
    public static boolean isDrivenByEnumeratedElement( SchemaType type ) {
        try {
            return AdqlData.ENUMERATED_ELEMENTS.containsKey( getLocalName( type ) ) ;
        }
        catch( Exception ex ) {
            ;
        }          
        return false ;
    }
    
    public static boolean isEnumeratedElement( XmlObject xmlObject ) {    
        return isEnumeratedElement( xmlObject.schemaType() ) ;
    }
    
    
    public static boolean isEnumeratedElement( SchemaType type ) {
        try {
            return AdqlData.ENUMERATED_ELEMENTS.containsValue( getLocalName( type ) ) ;
        }
        catch( Exception ex ) {
            ;
        }          
        return false ;
    }
    
    public static boolean isDrivenByEnumeratedElement( XmlObject xmlObject ) {    
        return isDrivenByEnumeratedElement( xmlObject.schemaType() ) ;
    }
    
    public static boolean isNodeForming( SchemaType type ) {
        try {
            return !AdqlData.NON_NODE_FORMING.containsKey( getLocalName( type ) ) ;
        }
        catch( Exception ex ) {}
        return false ;
    }
    
    public static boolean isEnumValueSynonymed( String enumValue  ) {
        boolean retVal = false ;
        try {
            retVal = AdqlData.ENUM_SYNONYMS.containsKey(enumValue) ;
        }
        catch( Exception ex ) {}
        return retVal ;
    }
    
    public static String getMasterEnumSynonym( String enumValue ) {
        String[] enumArray = null ;
        String retVal = null ;
        try {
            enumArray = AdqlData.ENUM_SYNONYMS.get( enumValue ) ;
            if( enumArray == null ) {
                retVal = enumValue ;
            }
            else if( enumArray[1].equalsIgnoreCase( enumValue ) ) {
                retVal = enumArray[0] ;
            }
            else {
                retVal = enumValue ;
            }
        }
        catch( Exception ex ) {
            retVal = enumValue ;
        }
        return retVal ;
    }
    
    public static String getSlaveEnumSynonym( String enumValue ) {
        String[] enumArray = null ;
        String retVal = null ;
        try {
            enumArray = AdqlData.ENUM_SYNONYMS.get( enumValue ) ;
            if( enumArray == null ) {
                retVal = enumValue ;
            }
            else if( enumArray[0].equalsIgnoreCase( enumValue ) ) {
                retVal = enumArray[1] ;
            }
            else {
                retVal = enumValue ;
            }
        }
        catch( Exception ex ) {
            retVal = enumValue ;
        }
        return retVal ;
    }
    
    public static boolean isNodeForming( XmlObject xmlObject ) {
        return isNodeForming( xmlObject.schemaType() ) ;
    }
    
    public static boolean isCascadeable( XmlObject xmlObject ) {
        return isCascadeable( xmlObject.schemaType() ) ;
    }
    
    public static boolean isCascadeable( SchemaType type ) {
        boolean answer = false ;   
        try {
            answer = AdqlData.CASCADEABLE.containsKey( getLocalName( type ) ) ;
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
            answer = AdqlData.METADATA_LINK_COLUMN.containsKey( getLocalName( type ) ) ;
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
            retValue = !AdqlData.UNSUPPORTED_TYPES.containsKey( getLocalName( type ) ) ;
        }
        catch( Exception ex ) {
            if( log.isDebugEnabled() )
                log.debug( "Unsupported XML type" );
        }
        return retValue ;
    }
    
    public static boolean isSupportedTypeWithinParent( SchemaType child, SchemaType parent ) {
        if( parent == ArrayOfFromTableType.type
            &&
            child == JoinTableType.type ) {
            return false ;
        }
        //
        // This allows use of the 2 comment elements in the schema without creating them
        // as nodes in a tree. Elements: StartComment and EndComment.
        // (a bit tricky)...
        else if( parent == SelectType.type && ( child.isBuiltinType() || child.isAnonymousType() ) ) {
            return false ;
        }
        return true ;
    }
         
    public static boolean isTableLinked( XmlObject xmlObject ) {
        return isTableLinked( xmlObject.schemaType() ) ;
    }
    
    
    public static boolean isTableLinked( SchemaType type  ) {
        boolean answer = false ;
        try {
            answer = AdqlData.METADATA_LINK_TABLE.containsKey( getLocalName( type ) ) ;
        }
        catch( Exception ex ) {
            ;
        }             
        return answer ;
    }
    
    public static boolean isCardinalityImposed( SchemaProperty schemaProperty ) {
        return isCardinalityImposed( schemaProperty.getName().getLocalPart() ) ;
    }
    
    public static boolean isCardinalityImposed( String elementName  ) {
        boolean answer = false ;
        try {
            answer = AdqlData.IMPOSED_CARDINIALITIES.containsKey( elementName ) ;
        }
        catch( Exception ex ) {
            ;
        }             
        return answer ;
    }
    
    public static Integer[] getImposedCardinality( String elementName ) {
        Integer[] cardinalityArray = null ;
        try {
            cardinalityArray = AdqlData.IMPOSED_CARDINIALITIES.get( elementName ) ;
        }
        catch( Exception ex ) {
            ;
        }
        return cardinalityArray ;
    }
    
    public static Integer[] getImposedCardinality( SchemaProperty schemaProperty ) {
        return getImposedCardinality( schemaProperty.getName().getLocalPart() ) ;
    }
    
    public static boolean isFunctionType( SchemaType schemaType ) {
        boolean answer = false ;
        if( schemaType == MathFunctionType.type 
            ||
            schemaType == TrigonometricFunctionType.type
            ||
            schemaType == AggregateFunctionType.type ) {
            answer = true ;
        }
        return answer ;
    }
    
    public static Integer[] getFunctionCardinalities( XmlObject func ) {
        XmlString functionName = (XmlString)AdqlUtils.get( func, "Name" ) ;
        String name = functionName.getStringValue() ;
        return AdqlData.FUNCTION_CARDINALITIES.get( name ) ;
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
            retValue = (String)AdqlData.T2D_NAMES.get( getLocalName( schemaType ) ) ;
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
        if( getLocalName( type ).equals( name ) ) 
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
    
    static public XmlObject getParent( XmlObject o ) {
        XmlObject parent = null ;
        XmlCursor cursor = o.newCursor() ;
        if( cursor.toParent() == true )
            parent = cursor.getObject() ;
        cursor.dispose() ;
        return parent ;
    }
    
    static public boolean areTypesEqual( SchemaType typeOne, SchemaType typeTwo ) {
        return typeOne.getName().equals( typeTwo.getName() ) ;

    }
    
    static public boolean areTypesEqual( XmlObject objOne, XmlObject objTwo ) {
        return areTypesEqual( objOne.schemaType(), objTwo.schemaType() ) ;
    }
    
    static public boolean areTypesEqual( XmlObject obj, SchemaType type ) {
        if( obj == null || type == null )
            return false ;
        SchemaType objType = obj.schemaType() ;
        if( objType == null )
            return false ;
        QName qname1 = objType.getName() ;
        QName qname2 = type.getName() ;
        if( qname1 == null || qname2 == null )
            return false ;
        return qname1.equals( qname2 ) ;
    }
    
    static public boolean areTypesEqual(  XmlObject objOne, String typeTwoLocalName ) {
        SchemaType typeOne = objOne.schemaType() ;
        return areTypesEqual( typeOne, getType( typeOne, typeTwoLocalName ) ) ;
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
        if( name == null )
            return "" ;
        name = name.trim();
        if( name.length() == 0 )
            return "" ;
        return (name.substring(0,1).toUpperCase() + name.substring(1)) ;
    }
        
    static private Object[] getInterfaces( Class cls ) {
        ArrayList<Class> iList = new ArrayList<Class>() ;
        Class[] interfaces = cls.getInterfaces() ;
        for( int i=0; i<interfaces.length; i++ ) {
            getInterfaces( interfaces[i], iList ) ;
        }
        return iList.toArray() ;
    }
    
    static private void getInterfaces( Class iFace, ArrayList<Class> iList ) {
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
                log.error( "invoke() failed: ", itx.getCause() ) ;
            }
            catch( Exception ex2 ) {
                log.error( "invoke() failed: ", ex2 );
            }
        }
        return retObj ;
    }
    
    
    public static boolean isSuitablePasteIntoTarget( AdqlNode entry, XmlObject clipboardObject ) {
        return isSuitablePasteIntoTarget( entry, clipboardObject.schemaType() ) ;
    }

    public static boolean isSuitablePasteIntoTarget( AdqlNode entry, SchemaType clipboardType ) {
       return  ( findSuitablePasteIntoTarget( entry, clipboardType ) != null ) ;
    }
    
    public static SchemaType findSuitablePasteIntoTarget( AdqlNode entry, XmlObject clipboardObject ) {
       return  findSuitablePasteIntoTarget( entry, clipboardObject.schemaType() ) ;
    }
    
    public static SchemaType findSuitablePasteIntoTarget( AdqlNode entry, SchemaType clipboardType ) {
        SchemaProperty[] elementProperties = entry.getElementProperties() ;
        for ( int i = 0 ; i < elementProperties.length ; i++ ) {
            if( elementProperties[i].getType().isAssignableFrom( clipboardType ) ) { 
                return elementProperties[i].getType() ;
            }
        }
        return null ;
    }

    public static boolean isSuitablePasteOverTarget( AdqlNode entry, XmlObject pasteObject ) {
        return isSuitablePasteOverTarget( entry, pasteObject.schemaType() ) ;
    }

    public static boolean isSuitablePasteOverTarget( AdqlNode entry, SchemaType pasteType ) {
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
    
    public static int findChildIndex( XmlObject parent, XmlObject child ) {
        return findChildIndexWithOptionalFilter( parent, child, false ) ;
    }
    
    public static int findFilteredChildIndex( XmlObject parent, XmlObject child ) {      
        return findChildIndexWithOptionalFilter( parent, child, true ) ;
    }
    
    private static int findChildIndexWithOptionalFilter( XmlObject parent
                                                       , XmlObject child
                                                       , boolean useFilter ) {
        int index = 0 ;
        boolean found = false ;
        XmlCursor cursor = parent.newCursor() ;
        XmlObject xmlObject = null ;
        cursor.toFirstChild() ; // There has to be a first child!
        do {
            xmlObject = cursor.getObject() ;
            if( child == xmlObject ) {
                found = true ;
                break ;
            }
            if( useFilter && !isNodeForming( xmlObject ) ) {
                continue ;
            }
            else {
                index++ ;
            }
        } while( cursor.toNextSibling() ) ;
        if( found == false ) {
            index = -1 ;
        }
        cursor.dispose() ;
        return index ;
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
        	    //NW - Jeff - is the lack of a 'break' intentional here?
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
    
    
    public static String[] getEditableAttributes( String localName ) {
        return (String[])AdqlData.EDITABLE_ATTRIBUTES.get( localName ) ;
    }
    
    public static String[] getEditableAttributes( SchemaType type ) {
        return getEditableAttributes( getLocalName( type ) ) ;
    }
    
    public static String[] getEditableAttributes( XmlObject xmlObject ) {
        return getEditableAttributes( xmlObject.schemaType() ) ;
    }
    
    public static String[] getEditableElements( String localName ) {
        return AdqlData.EDITABLE_ELEMENTS.get( localName ) ;
    }
    
    public static String[] getEditableElements( SchemaType type ) {
        return getEditableElements( getLocalName( type ) ) ;
    }
    
    public static String[] getEditableElements( XmlObject xmlObject ) {
        return getEditableElements( xmlObject.schemaType() ) ;
    }

    //
    // Just be careful here.
    // Being attribute driven is NOT the same as having an enumerated attribute!!!
    //
    public static boolean isAttributeDriven( SchemaType type ) {
        String[] name = (String[])AdqlData.EDITABLE_ATTRIBUTES.get( getLocalName( type ) ) ;
        return ( name != null && name.length == 1 ) ;
    }
    
    
    public static boolean isAttributeDriven( XmlObject xmlObject ) {
        return isAttributeDriven( xmlObject.schemaType() ) ;
    }
    
    public static XmlObject setAttributeDrivenDefaults( XmlObject xmlObject ) {
        SchemaType type = xmlObject.schemaType() ;
        String[] attributeNames = getEditableAttributes( type ) ;
        XmlString tempObject = XmlString.Factory.newInstance() ;
        tempObject.setStringValue( (String)AdqlData.ATTRIBUTE_DEFAULTS.get( getLocalName( type ) ) ) ;       
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
        SchemaType type = xmlObject.schemaType() ;
        String value = (String)AdqlData.DERIVED_DEFAULTS.get( getLocalName( type ) ) ;
        XmlObject retVal = xmlObject ;
        if( value != null ) {        
            // Cooerce the empty element into an XmlString...
            XmlString tempObject = (XmlString)xmlObject.changeType( XmlString.type ) ;
            tempObject.setStringValue( value ) ;
            // Cooerce back to original type...
            retVal = tempObject.changeType( type ) ;
        }       
        return retVal ;
    }
    
    public static boolean isCopyHolderIdenticalToSystemClipboard( CopyHolder copy, AdqlTransformer transformer ) {
        //
        // The first part attempts to see whether the system clipboard contains an exact
        // replica of the local CopyHolder based clipboard. OK: this is a cludge to overcome
        // some of the difficulties of in-context menus. (I hope temporarilly).
        // If the two match exactly, then we use the local clipboard copy. This allows
        // us to grey out (or not) the paste action where the underlying type is incorrect.
        // The local clipboard contains enough info to determine type.
        // This is currently impossible to do from the system clipboard because we have no
        // way of testing ADQL type from text present in the system clipboard.
        //
        boolean retValue = false ;
        XmlCursor nodeCursor = null ;
        try {
            XmlObject userObject = copy.getSource() ;       
            userObject = AdqlUtils.modifyQuotedIdentifiers( userObject ) ;
            nodeCursor = userObject.newCursor();
            String text = nodeCursor.xmlText();                    
            userObject = AdqlUtils.unModifyQuotedIdentifiers( userObject ) ;
            String adqls = transformer.transformToAdqls( text, " " ) ; 
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable t = clipboard.getContents( null ) ;
            String contents = ((String)t.getTransferData( DataFlavor.stringFlavor )).trim() ;
            if( contents.equalsIgnoreCase( adqls ) ) {
                retValue = true ;
            }
        } 
        catch ( Exception ex ) {
            ;
        }
        finally {
            if( nodeCursor != null )
                nodeCursor.dispose() ;
        }       
        return retValue ;
    }

}
