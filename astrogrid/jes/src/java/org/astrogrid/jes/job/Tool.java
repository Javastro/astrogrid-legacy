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

package org.astrogrid.jes.job ;

import org.astrogrid.jes.jobcontroller.*;
import org.astrogrid.jes.jobscheduler.*;

import java.util.List ;
import java.util.ListIterator ;
import java.util.ArrayList ;
//import java.util.Collections ;
import org.apache.log4j.Logger ;
import org.w3c.dom.* ;
import java.text.MessageFormat ;

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
             
    private List createParameters( Element element ){
        if( TRACE_ENABLED ) trace( "Tool.createParameters() entry") ;
        
        List
            list = new ArrayList() ;
        
        try {
            
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( SubmissionRequestDD.PAREMETER_ELEMENT ) ) {
                           list.add( new Parameter( this, element ) )  ;
                    }
                    
                } // end if
                                
            } // end for       
             
        }
        finally {
            if( TRACE_ENABLED ) trace( "Tool.createParameters() exit") ;
        }

        return list ;
        
    } // end of Tool.createParameters()  
    
    private JobStep
        parent ;
    
    private String
        name ;
        
    private List
        inputParameters = new ArrayList(),
        outputParameters = new ArrayList() ;    
        
        
    public Tool() {
    }
    
    public Tool( String name ) {
        this.name = name ;
    }
        
         
    public Tool( JobStep parent, Element element ) {
        if( TRACE_ENABLED ) trace( "Tool( Element ) entry") ;  
        
        try {
            
            this.parent = parent ;
            name = element.getAttribute( SubmissionRequestDD.TOOL_NAME_ATTR ) ;
            
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( SubmissionRequestDD.INPUT_ELEMENT ) ) {
                        this.inputParameters = this.createParameters( element ) ;   
                    }
                    else if ( element.getTagName().equals( SubmissionRequestDD.OUTPUT_ELEMENT ) ) {
                        this.outputParameters = this.createParameters( element ) ;   
                    }  
                    
                } // end if
                                
            } // end for        
           
        }
        finally {
            if( TRACE_ENABLED ) trace( "Tool(Element) exit") ;
        }
                   
    }
    
    

    /**
      */
    public String getName() {
        return this.name;
    }
    
    
    public ListIterator getInputParameters() {
        if( TRACE_ENABLED ) trace( "Tool.getInputParameters() entry") ; 
        return this.inputParameters.listIterator() ;
    }
    
    
    public ListIterator getOutputParameters() {
        if( TRACE_ENABLED ) trace( "Tool.getOutputParameters() entry") ; 
        return this.outputParameters.listIterator() ;
    }
    
     
    private static void trace( String traceString ) {
        // System.out.println( traceString ) ;
        logger.debug( traceString ) ;
    }
    
    
    private static void debug( String logString ){
        // System.out.println( logString ) ;
        logger.debug( logString ) ;
    }


	/**
	   */
	public void setInputParameters(List list) {
		inputParameters = list;
	}

	/**
	   */
	public void setName(String string) {
		name = string;
	}

	/**
	   */
	public void setOutputParameters(List list) {
		outputParameters = list;
	}

	public void setParent(JobStep parent) {
		this.parent = parent;
	}

	public JobStep getParent() {
		return parent;
	}
    
    public String toJESXMLString() {
        if( TRACE_ENABLED ) trace( "Tool.toJESXMLString() entry") ;
        
        String 
            response = null,
            inputParams,
            outputParams ;
        StringBuffer
            buffer = null ;
                                     
        try {
            
            buffer = new StringBuffer(128) ;
            ListIterator
                it = inputParameters.listIterator();
            while( it.hasNext() ) {
                buffer.append( ((Parameter)it.next()).toJESXMLString()) ;
            }
            inputParams = buffer.toString() ;
            buffer = new StringBuffer(128) ;
            it = outputParameters.listIterator();
            while( it.hasNext() ) {
                buffer.append( ((Parameter)it.next()).toJESXMLString()) ;
            }
            outputParams = buffer.toString() ;
            
            Object []
                inserts = new Object[3] ;
            inserts[0] = this.getName() ;
            inserts[1] = inputParams ;
            inserts[2] = outputParams ;

            response = MessageFormat.format( ScheduleRequestDD.JOBTOOL_TEMPLATE, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Tool.toJESXMLString() exit") ;    
        }       
        
        return response ;      

    }

} // end of class Tool
