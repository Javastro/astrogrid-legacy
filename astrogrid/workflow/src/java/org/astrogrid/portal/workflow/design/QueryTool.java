/*
 * @(#)QueryTool.java   1.0
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
import org.w3c.dom.* ;
/**
 * The <code>NullTool</code> class represents... 
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
public final class QueryTool implements Tool {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( QueryTool.class ) ; 
    
    private String
        name = null ,
        description = null ;
        
    private String
        xmlQuery = null ;
        
    public QueryTool() {
        if( TRACE_ENABLED ) trace( "QueryTool() entry/exit") ;  
    }
    
    public QueryTool( Element element ) {
        if( TRACE_ENABLED ) trace( "QueryTool(Element) entry/exit") ;   
    }
        
        
    public String getToolType() { return "Query" ; }
        


	public String toXMLString() { return xmlQuery ; }   
    public String toJESXMLString() { return xmlQuery ; }
    
    public String getName() { return "" ; }
    public String getDescription() { return "" ; }
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  

} // end of class QueryTool
