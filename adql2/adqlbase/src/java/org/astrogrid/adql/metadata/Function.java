/*$Id: Function.java,v 1.1 2007/07/29 11:26:55 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql.metadata;

/**
 * Function
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Jul 29, 2007
 */
public class Function {
    
    private final String name ;
    private final int[] cardinality ;

    public Function( String name, int lowerCardinality, int upperCardinality ) {
        this.name = name ;
        this.cardinality = new int[ 2 ] ;
        if( lowerCardinality < 0 ) {
            this.cardinality[0] = 0 ;
        }
        else {
            this.cardinality[0] = lowerCardinality ;
        }
        if( upperCardinality < 0 ) {
            this.cardinality[1] = -1 ;
        }
        else {
            this.cardinality[1] = upperCardinality ;
        }
    }
    
    public Function( String name, int cardinality ) {
        this( name, cardinality, cardinality ) ;
    }
    
    public String getName() {
        return name ;
    }
    
    public int[] getCardinality() {
        return new int[] { cardinality[0], cardinality[1] } ;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if( obj == this ) {
            return true ;
        }
        else if( obj instanceof Function ) {
            if( name.equals( ((Function)obj).name ) ) {
                if( cardinality[0] == ((Function)obj).cardinality[0]
                    &&
                    cardinality[1] == ((Function)obj).cardinality[1]
                  ) {
                    return true ;
                }
            }
        }
        return false ;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return name.hashCode();
    }
    
}


/*
$Log: Function.java,v $
Revision 1.1  2007/07/29 11:26:55  jl99
User defined function calls accommodated with metadata

*/