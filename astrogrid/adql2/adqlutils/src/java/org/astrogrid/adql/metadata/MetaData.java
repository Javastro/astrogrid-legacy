/*$Id: MetaData.java,v 1.2 2008/09/08 15:37:22 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql.metadata;

/**
 * MetaData
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Jul 29, 2007
 */
public class MetaData {
    
    protected final Key key ;

    public MetaData( String name, String[] qualifiers ) {
        this.key = Key.buildKey( name, qualifiers ) ;
    }
    
    public MetaData( String name, MetaData parent ) {
        this.key = Key.buildKey( name, parent.key ) ;
    }
    
    public MetaData( String name ) {
        this.key = Key.buildKey( name ) ;
    }
    
    public MetaData( String[] nameAndQualifiers ) {
        this.key = Key.buildKey( nameAndQualifiers ) ;
    }
    
    public String getName() {
        return this.key.getName() ;
    }
    
    public String[] getQualifiers() {
        return this.key.getQualifiers() ;
    }
    
    public Key getKey() {
        return this.key ;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if( obj == this ) {
            return true ;
        }
        else if( obj instanceof MetaData ) {
            if( this.key == ( ((MetaData)obj).key ) ) {
                    return true ;
            }
        }
        return false ;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return this.key.hashCode();
    }
    
}

/*
$Log: 

*/