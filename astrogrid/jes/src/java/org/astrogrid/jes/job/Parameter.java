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

package org.astrogrid.jes.job;

import org.astrogrid.jes.jobcontroller.SubmissionRequestDD;
import org.astrogrid.jes.jobscheduler.ScheduleRequestDD;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import java.text.MessageFormat;

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
        
    private static Log
        logger = LogFactory.getLog( Parameter.class ) ;    
    
    private Tool
        parent ;
    
    private String
        name = null,
        type = null;
        
    private String
        location = null ;
        
    private String
        contents = null ;
        
    public Parameter() {
    }
     
    public Parameter( String name ) {
        this.name = name ;   
    }
    
    public Parameter( Tool parent, Element element ) {
        if( TRACE_ENABLED ) trace( "Parameter(Element) exit") ; 
             
        try {
            
            trace( "Parameter: " + XMLUtils.ElementToString( element ) ) ;
            
            this.parent = parent ;
            this.name = element.getAttribute( SubmissionRequestDD.PAREMETER_NAME_ATTR ) ;
            this.type = element.getAttribute( SubmissionRequestDD.PAREMETER_TYPE_ATTR ) ;
//            this.location = element.getAttribute( SubmissionRequestDD.PAREMETER_LOCATION_ATTR ) ;
//            this.contents = element.getNodeValue() ;
           
            // If the parameter is instream, the parameter contents is given by the node value, but...
            // If the parameter is a remote reference (to a file within MySpace),
            // then the location is set by the node value.
            if( this.isRemoteReference() ) {
                trace( "parameter is remote reference") ;
                //bug#106
                this.location = XMLUtils.getChildCharacterData( element ) ;
                this.contents = "" ;    
            }
            else {
                trace( "parameter contents is instream") ;
                //bug#106
                this.contents = XMLUtils.getChildCharacterData( element ) ;
                this.location = "" ;
            }
                       
        }
        finally {
            if( TRACE_ENABLED ) trace( "Parameter(Element)() exit") ;
        }

    } // end of Parameter(Element)
        
  
    public void setName( String name ){
        this.name = name ;
    }
  
    public String getName() {
        return this.name ;  
    }
    

    public String getType() {
        return this.type ;
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
	public void setType(String string) {
		type = string;
	}

    public void setParent(Tool parent) {
		this.parent = parent;
	}

    public Tool getParent() {
		return parent;
	}
    
    
    protected String toJESXMLString() {
        if( TRACE_ENABLED ) trace( "Parameter.toJESXMLString() entry") ;
        String 
         response = null ;
                                     
        try {
            
            Object []
                inserts = new Object[3] ;
            inserts[0] = this.getName() ;
            inserts[1] = this.getType() ;
//            inserts[2] = ( this.getLocation() == null ) ? " " :  "location=\"" + this.getLocation() + "\"" ;
//            inserts[3] = ( this.getContents() == null ) ? " " :  this.getContents() ;

            if( this.isRemoteReference() ) {
                inserts[2] = ( this.getLocation() == null ) ? " " :  this.getLocation() ;
            }
            else {
                inserts[2] = ( this.getContents() == null ) ? " " :  this.getContents() ;
            }          

            response = MessageFormat.format( ScheduleRequestDD.JOBPARAMETER_TEMPLATE, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Parameter.toJESXMLString() exit") ;    
        }       
        
        return response ;  
             
    }
    
    
    public boolean isRemoteReference() {
        boolean
            bRemoteRef = false ;
            
        if( this.type != null
            &&
            ( this.type.indexOf( "MySpace_FileReference") != -1 
              ||
              this.type.indexOf( "MySpace_VOTableReference") != -1 ) ) {
                  
            bRemoteRef = true ;   
             
        }
        return bRemoteRef ;
    }
    
    public boolean isInStream() {
        return !isRemoteReference() ;
    }
    
     
} // end of class Parameter
