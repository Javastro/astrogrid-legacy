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

package org.astrogrid.portal.workflow.design;

import org.apache.log4j.Logger;
/**
 * The <code>ActivityKey</code> class is a thin wrapper
 * for the String key of an Activiy.
 *
 * @author  Jeff Lusted
 * @version 1.0 27-Aug-2003
 * @see  org.astrogrid.workflow.design.Activity   
 * @see     
 * @since   AstroGrid 1.3
 * @modified NWW moved into workflow.design package.
 * @modified NWW made package private.
 */
final class ActivityKey {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( ActivityKey.class ) ; 
        
    private static int
        highWaterMark = 0 ;
    
    private int 
        key ;
        
        
    public static synchronized ActivityKey createKey() {
        if( TRACE_ENABLED ) trace( "ActivityKey.createKey() entry") ; 
        ActivityKey
            key = null ;
        try {
            key = new ActivityKey( highWaterMark++ ) ;
            debug( "key: " + key.toString() ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "ActivityKey.createKey() exit") ; 
        }
        
        return key ;
    }
    
    
    public ActivityKey( int key ) {
        if( TRACE_ENABLED ) trace( "ActivityKey(int) entry/exit") ; 
        this.key = key ;
    }
    
    
    public ActivityKey( String key ) {
        if( TRACE_ENABLED ) trace( "ActivityKey(String) entry/exit") ; 
        this.key = new Integer( key ).intValue() ;          
    }
    
    
    public String toString() {
        return new Integer( key ).toString() ; 
    }
    
    public int hashCode() {
        return key ;
    }
    
    public boolean equals( Object o ) {
        
        boolean
            retValue = false ;
            
        if( (o != null) && (o instanceof ActivityKey) ){
             retValue = ( o.hashCode() == this.hashCode() ) ;         
         }
        
        return retValue ;
        
    }
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }
    
} // end of class ActivityKey