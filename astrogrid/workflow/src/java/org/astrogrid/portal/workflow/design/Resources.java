/*
 * @(#)Resources.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.portal.workflow.design;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.List ;
import java.util.ListIterator ;

/**
 * The <code>Resources</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 25-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class Resources {
       
    private List
        names = Collections.synchronizedList( new ArrayList() ) ;
        
	public boolean add( String name ) {
		return this.names.add( name ) ;
	}
    
    public void add( int index, String name ) {
        this.names.add( index, name ) ;
    }

    public String get( int index ) {
        return (String)names.get( index ) ;
    }
    
    public boolean isEmpty() {
        return names.isEmpty() ;
    }
    
    public String remove( int index ) {
        return (String)names.remove( index ) ;
    }
    
    public String set( int index, String name ) {
        return (String)names.set( index, name ) ;
    }
    
    public ListIterator listIterator() {
        return names.listIterator() ;
    }

} // end of class Resources
