/*
 * @(#)Activity.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.portal.workflow.design.activity;

import org.apache.log4j.Logger ;
import org.astrogrid.portal.workflow.design.Workflow;


/**
 * The <code>Activity</code> class represents... 
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
public abstract class Activity {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Activity.class ) ; 
        

    private ActivityKey 
        key ;
        
    private Activity
        parent ;
        
    public Activity() {
        if( TRACE_ENABLED ) trace( "Activity() entry") ; 
        
        try {  
            key = ActivityKey.createKey() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Activity() exit") ; 
        }
        
    }

	public ActivityKey getKey() { return key; }
        
	public void setParent( Activity parent ) { this.parent = parent ; }
	public Activity getParent() { return parent ; }
    
    public boolean delete() {
        return ((ActivityContainer)this.getParent()).removeChild( this ) ;    
    }
    
    protected void remove() {
        if( TRACE_ENABLED ) trace( "Activity.remove() entry") ;    
        
        try { 
            // First we tell the workflow to forget this activity,
            // i.e. remove it from its navigation collection...
            this.getWorkflow().removeActivity( this ) ;
        
            // Then - if this is itself a container of activities -
            // We cascade the removal of a whole branch...
            if( this instanceof ActivityContainer ) {
                ActivityIterator
                    iterator = ((ActivityContainer)this).getChildren() ;
                // For every element in the collection...
                // (1) We invoke remove on each. This is equivalent to
                //     a cascade delete, as the remove action cascades 
                //     down into any children this element itself possesses.
                // (2) We remove it from the current collection.
                while( iterator.hasNext() ) {
                    iterator.next().remove() ; 
                    iterator.remove() ;
                } // end while 
            
            } // end if
            
        }
        finally {
            if( TRACE_ENABLED ) trace( "Activity.remove() exit") ;  
        }
 
    } // end of remove()
    
    
    public ActivityIterator getChildren() { return null ; }
    
    
    public Workflow getWorkflow() {
        if( TRACE_ENABLED ) trace( "Activity.getWorkflow() entry") ;       
              
        try {
            
            Activity
                 parent = this.getParent(),
                 workflowCandidate = this ;
                 
            while( parent != null ) {
                 workflowCandidate = parent ;
                 parent = this.getParent() ;
             }
             
             return (Workflow)workflowCandidate ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Activity.getWorkflow() exit") ;       
        }
            
    } // end of getWorkflow()
    
    
    public int getIndex() {
        return ( (ActivityContainer)this.getParent() ).getIndex( this ) ;
    }
    
    public void setIndex( int index ) {
        ( (ActivityContainer)this.getParent() ).setIndex( index, this ) ;
    }
    
    public abstract String toXMLString() ;
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  
    
} // end of class Activity
