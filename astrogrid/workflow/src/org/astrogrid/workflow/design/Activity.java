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
package org.astrogrid.workflow.design ;

import org.apache.log4j.Logger ;
import java.util.HashMap ;
import java.util.Map ;
import java.util.Collections ;

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
        
    private static Map 
        usedKeys ;
        
    static {
 //       uniqueKey =   new Integer( Math.random() * Integer.MAX_VALUE ).intValue() ;  
        usedKeys = Collections.synchronizedMap( new HashMap() ) ;
    }
    
    public static synchronized String generateKey() {
/*        
        while( usedKeys.containsKey( uniqueKey ) ) {
            uniqueKey = new Double( uniqueKey.doubleValue() + 1 ) ;
        }
        
        usedKeys.put( uniqueKey, uniqueKey ) ;
*/        
        return null ;
        
    } // end of generateKey()
        
    private String 
        key ;
        
    private Activity
        parent ;
        
    public Activity() {
        key = Activity.generateKey() ;
    }
    
    public Activity( String key ) {
        this.key = key ;
        usedKeys.put( key, key ) ;
    }

	public String getKey() { return key; }
        
	public void setParent( Activity parent ) { this.parent = parent ; }
	public Activity getParent() { return parent ; }
    
    public boolean delete() {
        return ((ActivityContainer)this.getParent()).removeChild( this ) ;    
    }
    
    protected void remove() {
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

    } // end of remove()
    
    
    public ActivityIterator getChildren() { return null ; }
    
    
    public Workflow getWorkflow() {
        
        Activity
            parent = this.getParent(),
            workflowCandidate = this ;
            
        while( parent != null ) {
            workflowCandidate = parent ;
            parent = this.getParent() ;
        }
        return (Workflow)workflowCandidate ;
        
    } // end of getWorkflow()
    
    
    public int getIndex() {
        return ( (ActivityContainer)this.getParent() ).getIndex( this ) ;
    }
    
    public void setIndex( int index ) {
        ( (ActivityContainer)this.getParent() ).setIndex( index, this ) ;
    }
    
    public abstract String toXMLString() ;
    
} // end of class Activity
