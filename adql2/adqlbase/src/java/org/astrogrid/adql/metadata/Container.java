/*$Id: Container.java,v 1.2 2007/07/29 22:51:56 jl99 Exp $
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
 * Container
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Jul 29, 2007
 */
public class Container {
    
    private Hashtable htFunctions = new Hashtable( 32 ) ;
    private Hashtable htColumns = new Hashtable( 256 ) ;
    
    public Container() {}
    
    public void pushFunction( Function f ) {
        this.htFunctions.put( f.getKey(), f ) ;
    }
    
    public Function getFunction( String name ) {
        return (Function)htFunctions.get( name ) ;
    }
    
    public boolean isFunction( String name ) {
        return htFunctions.containsKey( name ) ;
    }
    
    public void pushColumn( Column c ) {
        this.htColumns.put( c.getKey(), c ) ;
    }
    
    public Column getColumn( String name ) {
        return (Column)htColumns.get( name ) ;
    }
    
    public Column getColumn( String name, String[] qualifiers ) {
        return (Column)htColumns.get( Key.formKeyString( name, qualifiers ) ) ;
    }
    
    public boolean isColumn( String name ) {
        return htColumns.containsKey( name ) ;
    }
    
    public boolean isColumn( String name, String[] qualifiers ) {
        return htColumns.containsKey( Key.formKeyString( name, qualifiers ) ) ;
    }

}


/*
$Log: Container.java,v $
Revision 1.2  2007/07/29 22:51:56  jl99
First commit of meta data low level implementation

Revision 1.1  2007/07/29 11:26:55  jl99
User defined function calls accommodated with metadata

*/