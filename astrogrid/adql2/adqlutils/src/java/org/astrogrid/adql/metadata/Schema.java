/*$Id: Schema.java,v 1.2 2008/09/08 15:37:22 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql.metadata;

/**
 * Schema
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Jul 30, 2007
 */
public class Schema extends MetaData {
    
    public Schema( String name, String catalog ) {
        super( name, new String[] { catalog } ) ;
    }

    public Schema( String name ) {
        super( name ) ;
    }
    
    public Schema( String[] nameAndQualifiers ) {
        super( nameAndQualifiers ) ;
    }
    
    public String getCatalog() {
        return this.key.qualifiers[0] ;
    }
 
}


/*
$Log: Schema.java,v $
Revision 1.2  2008/09/08 15:37:22  jl99
Merge of branch adql_jl_2575_mark2 into HEAD

Revision 1.1.2.1  2008/08/29 14:49:07  jl99
First mass commit for the new project adql2

Revision 1.1.2.1  2008/08/01 18:50:36  jl99
Complete reorg for purposes of setting up a maven build

Revision 1.2  2007/08/02 14:14:12  jl99
Bug fix and code tidy

Revision 1.1  2007/07/29 23:33:59  jl99
tidy

*/