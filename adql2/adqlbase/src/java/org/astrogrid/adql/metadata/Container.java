/*$Id: Container.java,v 1.1 2007/07/29 11:26:55 jl99 Exp $
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
    
    private Hashtable htFunctions = new Hashtable() ;
    
    public Container() {}
    
    public void pushFunction( Function f ) {
        this.htFunctions.put( f.getName(), f ) ;
    }
    
    public Function getFunction( String name ) {
        return (Function)htFunctions.get( name ) ;
    }
    
    public boolean isFunction( String name ) {
        return htFunctions.containsKey( name ) ;
    }

}


/*
$Log: Container.java,v $
Revision 1.1  2007/07/29 11:26:55  jl99
User defined function calls accommodated with metadata

*/