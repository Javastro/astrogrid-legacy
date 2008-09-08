/*$Id: Key.java,v 1.2 2008/09/08 15:37:22 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql.metadata;

import java.util.Hashtable;

/**
 * Key
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Jul 29, 2007
 */
public class Key {

    private static Hashtable keys = new Hashtable( 1024 ) ;
    
    public static Key buildKey( String name ) {
        synchronized( keys ) {
            return initKey( name, name, null ) ;
        }
    }
    
    public static Key buildKey( String name, String qualifier ) {
        return buildKey( name, new String[] { qualifier } ) ;
    }
    
    public static Key buildKey( String name, String qualifier1, String qualifier2 ) {
        return buildKey( name, new String[] { qualifier1, qualifier2 } ) ;
    }
    
    public static Key buildKey( String name
                              , String qualifier1
                              , String qualifier2
                              , String qualifier3 ) {
        return buildKey( name, new String[] { qualifier1, qualifier2, qualifier3 } ) ;
    }
    
    public static Key buildKey( String name, String[] qualifiers ) {        
        String candidate =  formKeyString( name, qualifiers ) ;
        return initKey( candidate, name, qualifiers ) ;   
    }
    
    public static Key buildKey( String[] nameAndQualifiers ) {
        if( nameAndQualifiers.length == 1 ) {
            return buildKey( nameAndQualifiers[0] ) ;
        }
        else {
            String[] qualifiers = new String[ nameAndQualifiers.length - 1 ] ;
            String name = nameAndQualifiers[ nameAndQualifiers.length - 1 ] ;
            System.arraycopy( nameAndQualifiers, 0, qualifiers, 0,  nameAndQualifiers.length - 1 ) ;
            return buildKey( name, qualifiers ) ;
        }
        
    }
    
    public static Key buildKey( String name, Key parent ) {
        String candidate = parent.keyValue + '.' + name ;
        String[] qualifiers = new String[ parent.qualifiers.length + 1 ] ;
        System.arraycopy( parent.qualifiers, 0, qualifiers, 0, parent.qualifiers.length ) ;
        qualifiers[ parent.qualifiers.length ] = name ;
        return initKey( candidate, name, qualifiers ) ;
    }
    
    public static String formKeyString( String name ) {
        return name ;
    }
    
    public static String formKeyString( String[] nameAndQualifiers ) {
        StringBuffer buffer = new StringBuffer( nameAndQualifiers.length * 16 ) ;
        for( int i=0; i<nameAndQualifiers.length; i++ ) {
            buffer.append( nameAndQualifiers[i] ).append( '.' ) ;
        }
        buffer.deleteCharAt( buffer.length()-1 ) ;
        return buffer.toString() ;
    }

    public static String formKeyString( String name, String[] qualifiers ) {
        if( qualifiers == null ) {
            return name ;
        }
        else {
            StringBuffer buffer = new StringBuffer( name.length() * 4 ) ;
            for( int i=0; i<qualifiers.length; i++ ) {
                buffer.append( qualifiers[i] ).append( '.' ) ;
            }
            buffer.append( name ) ;
            return buffer.toString() ;
        }
    }
    
    private static Key initKey( String candidate, String name, String[] qualifiers ) {
        synchronized( keys ) {
            Key k = (Key)keys.get( candidate ) ;
            if( k == null ) {
                k = new Key( candidate, name, qualifiers ) ;
                keys.put( k, k ) ;
            }
            return k ;
        }      
    }
    
    protected String keyValue ;
    protected String[] qualifiers ;
    protected String name ;
    
    private Key( String keyValue, String name, String[] qualifiers ) {
        this.keyValue = keyValue ;
        this.name = name ;
        if( qualifiers == null ) {
            this.qualifiers = new String[ 1 ] ;
        }
        else {
            this.qualifiers = qualifiers ; 
        }
    }
     
    public String getKeyValue() {
        return this.keyValue ;
    }
    
    public String getName() {
        return this.name ;
    }
    
    public String[] getQualifiers() {
        if( this.qualifiers == null ) {
            return new String[1] ;
        }
        String[] retVal = new String[ this.qualifiers.length ];
        System.arraycopy( this.qualifiers, 0, retVal, 0, this.qualifiers.length ) ;
        return retVal ;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals( Object obj ) {
        if( obj == this ) {
            return true ;
        }
        else if( obj instanceof String ) {
            if( obj == this.keyValue ) {
                return true ;
            }
            else if( this.keyValue.equals( (String)obj) ) {
                return true ;
            }
        }
        return false ;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.keyValue.hashCode() ;
    }
        
}


/*
$Log: Key.java,v $
Revision 1.2  2008/09/08 15:37:22  jl99
Merge of branch adql_jl_2575_mark2 into HEAD

Revision 1.1.2.1  2008/08/29 14:49:07  jl99
First mass commit for the new project adql2

Revision 1.1.2.1  2008/08/01 18:50:36  jl99
Complete reorg for purposes of setting up a maven build

Revision 1.3  2007/08/02 14:14:12  jl99
Bug fix and code tidy

Revision 1.2  2007/07/29 23:33:59  jl99
tidy

Revision 1.1  2007/07/29 22:51:56  jl99
First commit of meta data low level implementation

*/