/*
 * @(#)ActivityKey.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design.activity;

/**
 * The <code>ActivityKey</code> class is a thin wrapper
 * for the String key of an Activiy.
 *
 * @author  Jeff Lusted
 * @version 1.0 27-Aug-2003
 * @see  org.astrogrid.workflow.design.Activity   
 * @see     
 * @since   AstroGrid 1.3
 */
public class ActivityKey {
    
    private static int
        highWaterMark = 0 ;
    
    private int 
        key ;
        
        
    public static synchronized ActivityKey createKey() {
        return new ActivityKey( highWaterMark++ ) ;
    }
    
    
    public ActivityKey( int key ) {
        this.key = key ;
    }
    
    
    public ActivityKey( String key ) {
        this.key = new Integer( key ).intValue() ;          
    }
    
    
    public String toString() {
        return new Integer( key).toString() ; 
    }
    
    public int hashCode() {
        return key ;
    }
    
} // end of class ActivityKey