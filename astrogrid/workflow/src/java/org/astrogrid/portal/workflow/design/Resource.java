/*
 * @(#)Resource.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import org.apache.log4j.Logger ;
import java.text.MessageFormat ;
/**
 * The <code>Resource</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 08-Sep-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class Resource {

    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Resource.class ) ; 
        
    private String
        type = null ;
  
       
    public String toXMLString() {
        if( TRACE_ENABLED ) trace( "Resource.toXMLString() entry") ;  
          
        String 
            response = null ;
                                     
        try {
            
            Object []
                inserts = new Object[1] ;
            inserts[0] = this.getType() ;
            
            response = MessageFormat.format( WorkflowDD.RESOURCE_TEMPLATE, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Resource.toXMLString() exit") ;    
        }       
        
        return response ;    
      
    } // end toXMLString()
    

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  
} // end of class Resource
