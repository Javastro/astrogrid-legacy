/*$Id: Table.java,v 1.3 2007/08/02 14:14:12 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql.metadata;

/**
 * Table
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Jul 29, 2007
 */
public class Table extends MetaData {
    
    public Table( String name, Schema parent ) {
        super( name, parent ) ;
    }
    
    public Table( String name, String[] qualifiers ) {
        super( name, qualifiers ) ;
    }
    
    public Table( String[] nameAndQualifiers ) {
        super( nameAndQualifiers ) ;
    }
    
    public Table( String name ) {
        super( name ) ;
    }

}


/*
$Log: Table.java,v $
Revision 1.3  2007/08/02 14:14:12  jl99
Bug fix and code tidy

Revision 1.2  2007/07/29 23:33:59  jl99
tidy

Revision 1.1  2007/07/29 22:51:56  jl99
First commit of meta data low level implementation

*/