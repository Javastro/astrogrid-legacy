/*
 * @(#)ActivityIterator.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.workflow.design ;

import java.util.ListIterator ;

/**
 * The <code>ActivityIterator</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 21-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class ActivityIterator {
    
    private ListIterator
        iterator ;
        
    public ActivityIterator( ListIterator iterator ) {
        this.iterator = iterator ;
    }
    
    public boolean hasNext() {
        return iterator.hasNext() ;
    }
    
    public Activity next() {
        return (Activity)iterator.next() ;
    }
    
    public void remove() {
        iterator.remove() ;
    }

} // end of class ActivityIterator
