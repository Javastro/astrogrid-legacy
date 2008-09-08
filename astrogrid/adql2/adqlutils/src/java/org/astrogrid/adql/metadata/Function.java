/*$Id: Function.java,v 1.2 2008/09/08 15:37:22 jl99 Exp $
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
public class Function extends MetaData {

    private final int[] cardinality ;

    public Function( String name, int lowerCardinality, int upperCardinality ) {
        super( name ) ;
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

    public int[] getCardinality() {
        return new int[] { cardinality[0], cardinality[1] } ;
    }

}


/*
$Log: Function.java,v $
Revision 1.2  2008/09/08 15:37:22  jl99
Merge of branch adql_jl_2575_mark2 into HEAD

Revision 1.1.2.1  2008/08/29 14:49:07  jl99
First mass commit for the new project adql2

Revision 1.1.2.1  2008/08/01 18:50:36  jl99
Complete reorg for purposes of setting up a maven build

Revision 1.2  2007/07/29 22:51:56  jl99
First commit of meta data low level implementation

Revision 1.1  2007/07/29 11:26:55  jl99
User defined function calls accommodated with metadata

*/