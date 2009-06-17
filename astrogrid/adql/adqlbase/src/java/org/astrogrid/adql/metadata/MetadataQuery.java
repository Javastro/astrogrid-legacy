/*$Id: MetadataQuery.java,v 1.2 2009/06/17 10:13:35 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql.metadata;

/**
 * MetadataQuery
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Jan 18, 2008
 */
public interface MetadataQuery {
    
    public boolean isTable( String tableName ) ;
    public boolean isColumn( String tableName, String columnName ) ;

}


/*
$Log: MetadataQuery.java,v $
Revision 1.2  2009/06/17 10:13:35  jl99
Merge of adql v1 parser with maven 2 build.
Due to some necessary restructuring, the maven 1 build process has been removed.

Revision 1.1.2.1  2009/06/16 07:49:42  jl99
First commit of maven 2 build

Revision 1.2  2008/02/04 17:47:29  jl99
Merge of branch adql-jl-2504

Revision 1.1.2.1  2008/01/24 21:08:36  jl99
Checking for mild semantics and metadata improved

*/