/*
 * @(#)Parameter.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import java.net.*; 
import org.w3c.dom.* ;
import org.apache.log4j.Logger ;


/**
 * The <code>Parameter</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 18-Nov-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.4
 */
public class Parameter {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Parameter.class ) ;    
    
    private String
        name = null,
        type = null,
        documentation = null ;
        
    private String
        location = null ;
        
    private String
        contents = null ;
        
    private Cardinality
        cardinality = null ;
        
    private Parameter() {
    }
        
    
    public Parameter( Element element ) {
        if( TRACE_ENABLED ) trace( "Parameter(Element) exit") ;  
             
        try {
            
            this.name = element.getAttribute( WorkflowDD.PAREMETER_NAME_ATTR ) ;
            this.type = element.getAttribute( WorkflowDD.PAREMETER_TYPE_ATTR ) ;
            this.location = element.getAttribute( WorkflowDD.PAREMETER_LOCATION_ATTR ) ;
            
            NodeList
               nodeList = element.getChildNodes() ; 
                           
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {           
                
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                    
                    element = (Element) nodeList.item(i) ;
                
                    if ( element.getTagName().equals( WorkflowDD.PAREMETER_ELEMENT ) ) {
                           
                    }
                    
                } // end if
                                
            } // end for       
             
        }
        finally {
            if( TRACE_ENABLED ) trace( "Parameter(Element)() exit") ;
        }

    } // end of Parameter(Element)
        
  
    public String getName() {
        return this.name ;  
    }
    
    public String getDocumentation() {
        return this.documentation ;
    }
    
    public String getType() {
        return this.type ;
    }
   
    public Cardinality getCardinality() {
        return this.cardinality ;
    }


	public String getContents() {
		return contents;
	}


	public String getLocation() {
		return location;
	}


	public void setContents(String string) {
		contents = string;
	}


	public void setLocation( String url ) {
		location = url;
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

} // end of class Parameter
