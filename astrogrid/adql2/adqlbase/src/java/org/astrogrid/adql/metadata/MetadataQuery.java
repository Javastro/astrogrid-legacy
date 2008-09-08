/*$Id: MetadataQuery.java,v 1.2 2008/09/08 15:37:24 jl99 Exp $
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
    public boolean isFunction( String functionName ) ;
    public int[] getFunctionCardinality( String functionName ) ;

}