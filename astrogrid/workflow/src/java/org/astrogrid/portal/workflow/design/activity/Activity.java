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
 * @testcase org.astrogrid.portal.workflow.test.org.astrogrid.portal.workflow.design.activity.TestActivity
 * 
 * @deprecated use workflow-objects object model instead
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
     
    /*
     * An Activity requires a parent. so the default constructor is private.
     * 
     */   
    private Activity() {
    }
    
    
    /*
     * An Activity requires:
     * 
     * (1) A parent. 
     *     The exception is the Workflow, which is top of the Activity tree.
     * 
     * (2) A key unique within a Workflow.
     * 
     */    
    public Activity( Activity parent ) {
        if( TRACE_ENABLED ) trace( "Activity() entry") ; 
        
        try {  
            this.parent = parent ;
            this.key = ActivityKey.createKey() ;
            this.getWorkflow().putActivity( this ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Activity() exit") ; 
        }
        
    } // end of Activity( Activity parent )
    

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
                    ((Activity)iterator.next()).remove() ; 
                    iterator.remove() ;
                } // end while 
            
            } // end if
            
        }
        finally {
            if( TRACE_ENABLED ) trace( "Activity.remove() exit") ;  
        }
 
    } // end of remove()
    
    
    /*
     * To be overridden by ActivityContainer
     */
    public ActivityIterator getChildren() { return null ; }
    
    
    /*
     * Attempts to walk up the Activity tree to find the Workflow,
     * which is the top of the tree.
     */
    public Workflow getWorkflow() {
        if( TRACE_ENABLED ) trace( "Activity.getWorkflow() entry") ;       
              
        Workflow
            workflow = null ;
              
        try {
            
            // If this gets invoked on a Workflow, 
            // we are already at the top of the tree...
            if( this instanceof Workflow ) {
                workflow = (Workflow)this ;
            }
            else {
                
                // Walk up the tree, stopping when
                // the Activity has no parent, which
                // is the marker for Workflow...
                   
                Activity
                     parent = this.getParent(),
                     workflowCandidate = this ;
                 
                while( parent != null ) {
                     workflowCandidate = parent ;
                     parent = parent.getParent() ;
                 }
             
                 if( workflowCandidate instanceof Workflow ) {
                     workflow = (Workflow)workflowCandidate ;
                 }
                 else {
                     debug( "Integrity failure in Activity tree") ;
                 }
                                    
            }

        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Activity.getWorkflow() exit") ;       
        }
        
        return workflow ;
            
    } // end of getWorkflow()
    
    
    /*
     * Returns relative postion of a child within its parent container
     */
    public int getIndex() {
        return ( (ActivityContainer)this.getParent() ).getIndex( this ) ;
    }
    
    
    /*
     * Alters the relative postion of a child within its parent container.
     * NB: This will alter the position of other children of the same
     *     container.
     */
    public void setIndex( int index ) {
        ( (ActivityContainer)this.getParent() ).setIndex( index, this ) ;
    }
    
    
//   public int hashCode() {
//        return this.getKey().hashCode() ;
//    }
    
    
    public boolean equals( Object o ) {
        
         boolean
             retValue = false ;
            
         if( (o != null) && (o instanceof Activity) ){
             retValue = ( o.hashCode() == this.hashCode() ) ;         
         }
        
         return retValue ;
        
     }
    
    
    public abstract String toXMLString() ;
    public abstract String toJESXMLString() ;  
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  
    
} // end of class Activity
