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

package org.astrogrid.portal.workflow.design;

import org.astrogrid.portal.workflow.intf.*;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;
import java.util.ListIterator;

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
 * @modified NWW moved into workflow.design package
 * @modified NWW made package private.
 */
abstract class ActivityContainer extends Activity {

    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( ActivityContainer.class ) ; 
    
    private LinkedList
        children = new LinkedList() ;
      
    protected ActivityContainer( Activity parent ) {
        super( parent ) ;
    }
    
    
    protected ActivityContainer( String communitySnippet
                            , Element element
                            , Activity parent ) {
        super( parent ) ;
        if( TRACE_ENABLED ) trace( "ActivityContainer(Element) entry") ; 
        
        try {
                       
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( WorkflowDD.SEQUENCE_ELEMENT ) ) {
                        this.add( new Sequence( communitySnippet, element, this ) ) ;   
                    }   
                    else if( element.getTagName().equals( WorkflowDD.FLOW_ELEMENT ) ) {
                        this.add( new Flow( communitySnippet, element, this ) ) ;                
                    }
                    else if( element.getTagName().equals( WorkflowDD.STEP_ELEMENT ) ) {
                        this.add( new Step( communitySnippet, element, this ) ) ;                
                    }
                    
                } // end if
                                
            } // end for        

            
        }
        finally {
            if( TRACE_ENABLED ) trace( "ActivityContainer(Element) exit") ;
        }
        
     }
    
    
    
    protected synchronized ISequence createSequence() {
        return this.createSequence( children.size() ) ;
    }
    
    
    protected synchronized ISequence createSequence( int index ) {
        return (ISequence)this.add( index, new Sequence( this ) ) ;
    }
    
    
    protected synchronized IFlow createFlow() {
        return this.createFlow( children.size() ) ;
    }
    
    
    protected synchronized Flow createFlow( int index ) {
        return (Flow)this.add( index, new Flow( this ) ) ;
    }
    
    
    protected synchronized IStep createStep( int index ) {
        return (IStep)this.add( index, new Step( this ) ) ;        
    }
   
    
    protected IActivity add( int index, Activity activity ) {
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
    
    
    protected IActivity add( Activity activity ) {
        if( TRACE_ENABLED ) trace( "Activity.add(Activity) entry") ;
        
        try {
            children.add( activity ) ;
//           activity.setParent( this ) ;
            this.getWorkflow().putActivity( activity ) ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "Activity.add(Activity) exit") ;
        }

        return activity ;    
                        
    }
    
    
    protected boolean removeChild( Activity activity ) { 
        this.remove() ;     
        return children.remove( activity ) ;
    }

    protected ActivityIterator getChildren() { return new ActivityIterator( children.listIterator() ) ; }
    
    
    protected int getIndex( Activity activity ) {
        return children.indexOf( activity ) ; 
    }
    
    protected void setIndex( int index, Activity activity ) {
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
    
    
    public String toJESXMLString() {
        if( TRACE_ENABLED ) trace( "toJESXMLString() entry") ;   
              
        StringBuffer 
            childrenBuffer = new StringBuffer( 256 ) ;
        ListIterator
            iterator = this.children.listIterator() ;
                                              
        try {
            
            while ( iterator.hasNext() ) {   
                childrenBuffer.append( ((Activity) iterator.next()).toJESXMLString() ) ;
            }
            
        }
        finally {
            if( TRACE_ENABLED ) trace( "toJESXMLString() exit") ;    
        }       
        
        return childrenBuffer.toString() ;
          
    } // end of toJESXMLString()
    
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  
    
} // end of class ActivityContainer