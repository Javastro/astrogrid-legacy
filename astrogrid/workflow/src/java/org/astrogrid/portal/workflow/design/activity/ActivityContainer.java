/*
 * @(#)ActivityContainer.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design.activity;

import java.util.LinkedList; 
import java.util.ListIterator ;
import org.apache.log4j.Logger ;
import org.astrogrid.portal.workflow.design.Flow;
import org.astrogrid.portal.workflow.design.Sequence;
import org.astrogrid.portal.workflow.design.Step;

import org.astrogrid.portal.workflow.design.*;

import java.text.MessageFormat ;
import org.apache.log4j.Logger ;
import org.astrogrid.i18n.*;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.design.activity.*;

import org.w3c.dom.* ;

/**
 * The <code>ActivityContainer</code> class represents... 
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
public abstract class ActivityContainer extends Activity {

    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( ActivityContainer.class ) ; 
    
    private LinkedList
        children = new LinkedList() ;
      
    public ActivityContainer() {
        super() ;
    }
    
    
    public ActivityContainer( Element element ) {
        super() ;
        if( TRACE_ENABLED ) trace( "ActivityContainer(Element) entry") ; 
        
        try {
                       
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( WorkflowDD.SEQUENCE_ELEMENT ) ) {
                        this.add( new Sequence( element ) ) ;   
                    }   
                    else if( element.getTagName().equals( WorkflowDD.FLOW_ELEMENT ) ) {
                        this.add( new Flow( element ) ) ;                
                    }
                    else if( element.getTagName().equals( WorkflowDD.STEP_ELEMENT ) ) {
                        this.add( new Step( element ) ) ;                
                    }
                    
                } // end if
                                
            } // end for        

            
        }
        finally {
            if( TRACE_ENABLED ) trace( "ActivityContainer(Element) exit") ;
        }
        
     }
    
    
    
    public synchronized Sequence createSequence() {
        return this.createSequence( children.size() ) ;
    }
    
    
    public synchronized Sequence createSequence( int index ) {
        return (Sequence)this.add( index, new Sequence() ) ;
    }
    
    
    public synchronized Flow createFlow() {
        return this.createFlow( children.size() ) ;
    }
    
    
    public synchronized Flow createFlow( int index ) {
        return (Flow)this.add( index, new Flow() ) ;
    }
    
    
    public synchronized Step createStep( int index ) {
        return (Step)this.add( index, new Step() ) ;        
    }
   
    
    public Activity add( int index, Activity activity ) {
        if( TRACE_ENABLED ) trace( "Activity.add(int,Activity) entry") ;
        
        try {
            children.add( index, activity ) ;
            activity.setParent( this ) ;
            this.getWorkflow().putActivity( activity ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Activity.add(int,Activity) exit") ;
        }

        return activity ;    
                        
    }
    
    
    public Activity add( Activity activity ) {
        if( TRACE_ENABLED ) trace( "Activity.add(Activity) entry") ;
        
        try {
            children.add( activity ) ;
            activity.setParent( this ) ;
            this.getWorkflow().putActivity( activity ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Activity.add(Activity) exit") ;
        }

        return activity ;    
                        
    }
    
    
    public boolean removeChild( Activity activity ) { 
        this.remove() ;     
        return children.remove( activity ) ;
    }


    public ActivityIterator getChildren() { return new ActivityIterator( children.listIterator() ) ; }
    
    
    public int getIndex( Activity activity ) {
        return children.indexOf( activity ) ; 
    }
    
    public void setIndex( int index, Activity activity ) {
        this.children.remove( activity ) ;
        this.children.add( index, activity ) ;
    }
    
    
    public String toXMLString() {
        if( TRACE_ENABLED ) trace( "toXMLString() entry") ;   
              
        StringBuffer 
            childrenBuffer = new StringBuffer( 256 ) ;
        ListIterator
            iterator = this.children.listIterator() ;
                                              
        try {
            
            while ( iterator.hasNext() ) {   
                childrenBuffer.append( ((Activity) iterator.next()).toXMLString() ) ;
            }
            
        }
        finally {
            if( TRACE_ENABLED ) trace( "toXMLString() exit") ;    
        }       
        
        return childrenBuffer.toString() ;
          
    } // end of toXMLString()
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  
    
} // end of class ActivityContainer