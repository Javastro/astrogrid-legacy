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
import org.apache.log4j.Logger ;
import java.text.MessageFormat ;

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
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Resources.class ) ; 
       
    private List
        resourceCollection = Collections.synchronizedList( new ArrayList() ) ;
    
    public String toXMLString() {
        if( TRACE_ENABLED ) trace( "Resources.toXMLString() entry") ;  
          
       String 
          response = null ;
       StringBuffer
          buffer = new StringBuffer( 64 ) ;
       ListIterator
          iterator = this.resourceCollection.listIterator();
                                     
       try {
            
           while ( iterator.hasNext() ) {   
               buffer.append( ((Resource) iterator.next()).toXMLString() ) ;
           }
            
           Object []
              inserts = new Object[1] ;
           inserts[0] = buffer.toString() ;
            
           response = MessageFormat.format( WorkflowDD.RESOURCES_TEMPLATE, inserts ) ;

       }
       finally {
           if( TRACE_ENABLED ) trace( "Step.toXMLString() exit") ;    
       }       
        
       return response ;        
        
    }
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  

} // end of class Resources
