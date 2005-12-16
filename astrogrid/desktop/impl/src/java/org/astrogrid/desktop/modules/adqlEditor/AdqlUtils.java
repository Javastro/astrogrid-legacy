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
import org.apache.xmlbeans.* ;
import java.util.Hashtable ;
import org.apache.xmlbeans.SchemaType;

import java.util.ArrayList;
import java.util.ListIterator;
import java.lang.reflect.*;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.dialogs.editors.ADQLToolEditorPanel;

import org.astrogrid.adql.v1_0.beans.* ;

/**
 * @author jl99
 *
 * 
 */
public final class AdqlUtils {
    
    private static final Log logger = LogFactory.getLog( AdqlUtils.class ) ;
    private static final boolean DEBUG_ENABLED = true ;
    private static final boolean TRACE_ENABLED = true ;
    
    private static final String EMPTY_STRING = "".intern() ;
    
    public static final Hashtable DISPLAY_NAME_FILTER ; 
    static {
        DISPLAY_NAME_FILTER = new Hashtable() ;
        DISPLAY_NAME_FILTER.put( "Select", "Query:" ) ;
        DISPLAY_NAME_FILTER.put( "Selection List", "Select" ) ;  
        DISPLAY_NAME_FILTER.put( "Column Reference", "Column" ) ;  
        DISPLAY_NAME_FILTER.put( "Comparison Pred", "Comparison" ) ; 
        DISPLAY_NAME_FILTER.put( "Region Search", "Region" ) ; 
    }
    
 
    
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
    
    static public String extractDisplayName( String name ) {
        String retValue = null ;
        retValue = (String)AdqlData.T2D_NAMES.get( name ) ;
        if( retValue == null )
            retValue = EMPTY_STRING ;
        return retValue ;
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
    
    static public String _extractDefaultDisplayName( XmlObject o ) {
        return _extractDisplayName( o, DISPLAY_NAME_FILTER ) ;
    }
    
    static public String _extractDisplayName( XmlObject o
            							   , final Hashtable filter ) {
        String displayName = extractDisplayName( o ) ;
        if( filter.containsKey( displayName ) ) {
            displayName = (String)filter.get( displayName ) ;
        }
        return displayName ;
    }
    
    
    static public String _extractDisplayName( XmlObject o ) {
        XmlCursor cursor = o.newCursor() ;
        String displayName = extractDisplayName( cursor ) ;
        cursor.dispose() ;
        return displayName ;
    }
    
    
    static public String _extractDisplayName( XmlCursor cursor ) {
        String displayName = null ;
        if( !cursor.currentTokenType().isStart() ) 
            cursor.toFirstChild();
        displayName = cursor.getAttributeText( new QName( "label" ) ) ;
        if( displayName == null || displayName.length() == 0 ) {
            displayName = cursor.getName().getLocalPart() ;
            if( cursor.toFirstAttribute() ) {
                do {
                    if( cursor.getName().getLocalPart().equals( "type" ) ) {
                        displayName = cursor.getTextValue() ;
                        break ;
                    }
                } while( cursor.toNextAttribute() ) ;
            }
        }
        return normalizeName( displayName ) ; 
    }
    
    
    static public String normalizeName( String name ) {
        System.out.println( "normalizeName" ) ;
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
    
    
    static public XmlObject get( XmlObject o, String elementName ) {
        String methodName = "get" + capitalize( elementName ) ;
        return (XmlObject)invoke( o, methodName, null ) ;
//        if( obj != null  &&  (obj instanceof XmlObject == false) ) { 
//              methodName = "xget" + capitalize( elementName ) ; 
//              obj = invoke( o, methodName, null ) ;
//        }
    }
    
    static public void set( XmlObject o, String elementName, XmlObject param ) {
        String methodName = "set" + capitalize( elementName ) ;
        invoke( o, methodName, new Object[]{ param } ) ;
    }
    
    static public boolean isSet( XmlObject o, String elementName ) {
        String methodName = "isSet" + capitalize( elementName ) ;
        return ((Boolean)invoke( o, methodName, null )).booleanValue() ;
    }
    
    static public void unset( XmlObject o, String elementName ) {
        String methodName = "unset" + capitalize( elementName ) ;
        invoke( o, methodName, null ) ;
    }
    
    static public XmlObject addNew( XmlObject o, String elementName ) {
        String methodName = "addNew" + capitalize( elementName ) ;
        return (XmlObject)invoke( o, methodName, null ) ;
    }
    
    static public Object[] getArray( XmlObject o, String elementName ) {
        String methodName = "get" + capitalize( elementName ) + "Array" ;
        return (Object[])invoke( o, methodName, null ) ;
    }
    
    static public Object getArray( XmlObject o, String elementName, int param ) {
        String methodName = "get" + capitalize( elementName ) + "Array" ;
        return invoke( o, methodName, new Integer[]{new Integer(param)} ) ;
    }  
    
    static public int sizeOfArray( XmlObject o, String elementName ) {
        String methodName = "sizeOf" +capitalize( elementName ) + "Array" ;
        return ((Integer)invoke( o, methodName, null )).intValue() ;
    }
    
    
    static public void setArray( XmlObject o, String elementName, XmlObject[] array ) {
        String methodName = "set" + capitalize( elementName ) + "Array" ;
        invoke( o, methodName, new Object[]{ array } ) ;
    }
    
    // void setItemArray(int i, net.ivoa.xml.adql.v10.SelectionItemType item);
    static public Object setArray( XmlObject o, String elementName, int param1, XmlObject param2 ) {
        String methodName = "set" + capitalize( elementName ) + "Array" ;
        return invoke( o, methodName, new Object[]{new Integer(param1), param2} ) ;
    } 
    
    // net.ivoa.xml.adql.v10.SelectionItemType insertNewItem(int i);
    static public XmlObject insertNewInArray( XmlObject o, String elementName, int param ) {
        String methodName = "insertNew" + capitalize( elementName ) ;
        return (XmlObject)invoke( o, methodName, new Integer[]{new Integer(param)} ) ;
    }
    
    // net.ivoa.xml.adql.v10.SelectionItemType addNewItem();
    static public XmlObject addNewToEndOfArray( XmlObject o, String elementName ) {
        String methodName = "addNew" + capitalize( elementName ) ;
        System.out.println( "methodName: " + methodName ) ;
        return (XmlObject)invoke( o, methodName, null ) ;
    }
    
    // void removeItem(int i);
    static public void removeFromArray( XmlObject o, String elementName, int param ) {
        String methodName = "remove" ;
        invoke( o, methodName, new Integer[]{new Integer(param)} ) ;
    }
    
    static public XmlObject newInstance( SchemaType st ) {
        XmlObject o = XmlObject.Factory.newInstance() ;
        o.changeType( st ) ;
        return o ;
    }
    
//    static public XmlObject newInstance( SchemaType st ) {
//        Class cls[] = st.getJavaClass().getClasses() ;
//        Class factory = null ;
//        for( int i=0; i<cls.length; i++ ) {
//            // This may be weak...
//            if( cls[i].getName().indexOf( "Factory" ) != -1 ){
//                factory = cls[i] ;
//                break ;
//            }
//        }      
//        return (XmlObject)invoke( factory, "newInstance", null ) ;
//    }
    
//    static public XmlObject newInstance( SchemaType st ) {
//        XmlObject o = null ;
//        Class cls = null ;
////        Class[] parameterTypes = new Class[] { st.getClass() } ;
//        Class[] parameterTypes = null ;
//        Constructor constructor = null ;
//        try {
//            parameterTypes = new Class[] { Class.forName( "org.apache.xmlbeans.SchemaType" ) } ;
//            cls = Class.forName( st.getFullJavaImplName() ) ;
//            constructor = cls.getConstructor( parameterTypes ) ;
//            o = (XmlObject)constructor.newInstance( new SchemaType[] { st } ) ;
//        }
//        catch( Exception ex ) {
//            ex.printStackTrace() ;
//        }
//        return o ;
//    }
    
    
    static private String capitalize( String name ) {
        return (name.substring(0,1).toUpperCase() + name.substring(1)).trim() ;
    }
    
    
    static private Object invoke( Object o, String methodName, Object[] params ) {
        Object retObj = null ;
        Method method = null ;
        Class[] parameterTypes = null ;
        if( params != null ) {
            Class[] interfaces ;
            parameterTypes = new Class[ params.length ] ;
            for( int i=0; i<params.length; i++ ) {
                parameterTypes[i] = params[i].getClass() ;
                // Experiment. If the class implements an interface, we use the interface...
                interfaces = parameterTypes[i].getInterfaces() ;
                if( interfaces.length != 0 )  // this is weak, but is unlikely to be wrong with xmlbeans.
                    parameterTypes[i] = interfaces[0] ;
            }
        }
        if( methodName.startsWith( "set" ) || methodName.startsWith( "get" ) ) {
            String altName = 'x' + methodName ;
            try {
                method = o.getClass().getMethod( altName, parameterTypes ) ;
            }
            catch( NoSuchMethodException ex1 ) {
                ;
            }
        }
        if( method == null ) {
            try {
                method = o.getClass().getMethod( methodName, parameterTypes ) ;
            }
            catch( NoSuchMethodException ex1 ) {
                ;
            }
        }
        if( method == null )
            return retObj ;
        
        try {
            retObj = method.invoke( o, params ) ;
        }
        catch( java.lang.reflect.InvocationTargetException itx ) {
            itx.getCause().printStackTrace() ;
        }
        catch( Exception ex2 ) {
            ex2.printStackTrace() ;
        }
        return retObj ;
    }

}
