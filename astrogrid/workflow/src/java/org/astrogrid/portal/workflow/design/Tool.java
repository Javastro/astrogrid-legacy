/*
 * @(#)Tool.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import java.util.List ;
import java.util.ListIterator ;
import org.apache.log4j.Logger ;
import org.w3c.dom.* ;

/**
 * The <code>Tool</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 20-Nov-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.4
 */
public class Tool {
  
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Tool.class ) ;    
        
    private ToolFactory
        factory ;
    
    private String
        name ;
        
    private List
        inputParameters,
        outputParameters ;    
        
    protected Tool( ToolFactory factory ) {
        if( TRACE_ENABLED ) trace( "Tool( ToolFactory ) entry") ;  
        this.factory = factory ;      
    }
    
    
    public ListIterator getInputParameters() {
        if( TRACE_ENABLED ) trace( "Tool.getInputParameters() entry") ; 
        return null ;
    }
    
    
    public ListIterator getOutputParameters() {
        if( TRACE_ENABLED ) trace( "Tool.getOutputParameters() entry") ; 
        return null ;
    }
    
    
    public Parameter newInputParameter( String name ) {
        if( TRACE_ENABLED ) trace( "Tool.newInputParameter() entry") ;
        return null ;
    }
 
 
    public Parameter newInputParameter( Parameter param ) {
        if( TRACE_ENABLED ) trace( "Tool.newInputParameter() entry") ;
        return null ;
    }
 
    
    public Parameter newOutputParameter( String name ) {
        if( TRACE_ENABLED ) trace( "Tool.newInputParameter() entry") ;
        return null ;
    }
    
    
    public Parameter newOutputParameter( Parameter param ) {
        if( TRACE_ENABLED ) trace( "Tool.newOutputParameter() entry") ;
        return null ;
     }
    
       
    protected String toXMLString() {
        if( TRACE_ENABLED ) trace( "Tool.toXMLString() entry") ;
        return null ;
    }
    
    
    protected String toJESXMLString() {
        if( TRACE_ENABLED ) trace( "Tool.toJESXMLString() entry") ;
        return null ;
    }
    
     
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }

	/**
	   */
	public void setInputParameters(List list) {
		inputParameters = list;
	}

	/**
	  */
	public void setOutputParameters(List list) {
		outputParameters = list;
	}

	/**
	  */
	protected ToolFactory getFactory() {
		return factory;
	}

	/**
	  */
	public String getName() {
		return name;
	}

} // end of class Tool
