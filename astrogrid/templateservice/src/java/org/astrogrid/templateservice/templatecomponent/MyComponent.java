/*
 * @(#)MyComponent.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.templateservice.templatecomponent ;

import org.astrogrid.templateservice.* ;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.astrogrid.Configurator ;
import org.astrogrid.AstroGridException ;
import org.astrogrid.i18n.AstroGridMessage;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


/**
 * The <code>MyComponent</code> class is 
 *
 * @author  Jeff Lusted
 * @version 1.0 28-May-2003
 * @since   AstroGrid 1.2
 */
public class MyComponent {

	/** Compile-time switch used to turn tracing on/off. 
	  * Set this to false to eliminate all trace statements within the byte code.*/	 
	private static final boolean 
		TRACE_ENABLED = true ;
			
	private static final String
	    ASTROGRIDERROR_FAILED_TO_PARSE_REQUEST  = "AGMYCE00030",
	    ASTROGRIDERROR_ULTIMATE_REQUESTFAILURE  = "AGMYCE00040";
		
	/** Log4J logger for this class. */    			    			   			
	private static Logger 
		logger = Logger.getLogger( MyComponent.class ) ;
	
	
	/**
	  *  
	  * Default constructor.
	  * <p>
	  * 
	  **/       	
	public MyComponent() {
		if( TRACE_ENABLED ) logger.debug( "MyComponent(): entry/exit") ;
	}
	
	
	/**
	  * <p> 
	  * Represents the mainline workflow argument for a typical AstroGrid component. 
	  * <p>
	  * 
	  * @param requestXML - The service request XML received as a String.
	  * @return A String. Maybe for testing purposes only if this service
	  * is presented as a one-way call.
	  * 
	  **/     
    public String mainline( String requestXML ) { 	
    	if( TRACE_ENABLED ) logger.debug( "mainline() entry") ;
    	
		String
			response = null ;
			
		try { 
			// If the properties file is not loaded, we bail out...
			// Each component MUST be properly initialized! 
            MYS.getInstance().checkPropertiesLoaded() ;
  		
    		// Parse the request...
         	Document
    		   doc = parseRequest( requestXML ) ;
               
            // YOUR MAIN PROCESSING SHOULD GO HERE...
            ;
				 
			// REPLACE THIS WITH A SUITABLE ACCESSOR...
			response = "place some suitable accessor here" ;	
    	}
    	catch( AstroGridException mex ) {
			AstroGridMessage
			    detailMessage = mex.getAstroGridMessage() ,  
				generalMessage = new AstroGridMessage( ASTROGRIDERROR_ULTIMATE_REQUESTFAILURE
                                                     , this.getComponentName() ) ;
			logger.error( detailMessage.toString(), mex ) ;
			logger.error( generalMessage.toString() ) ;
 				
			// If we are responding, we would format our error response here...
			if( response == null ) {
				response = generalMessage.toString() + "/n" + detailMessage.toString();
			}
    	}
    	finally {
    		resourceCleanup() ;
			if( TRACE_ENABLED ) logger.debug( "runQuery() exit") ;
    	}
    	
        return response ;   	
    	 	
    } // end mainline()


    private Document parseRequest( String requestXML ) throws MyComponentException {  	
		if( TRACE_ENABLED ) logger.debug( "parseRequest() entry") ;
		
		Document 
		   doc = null;
		DocumentBuilderFactory 
		   factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder 
	       builder = null;
	       
		try {
                    
		   factory.setValidating( Boolean.getBoolean( MYS.getProperty( MYS.DATASETAGENT_PARSER_VALIDATION
		                                                             , MYS.DATASETAGENT_CATEGORY )  )  ) ; 		
		   builder = factory.newDocumentBuilder();
		   logger.debug( requestXML ) ;
		   InputSource
		      jobSource = new InputSource( new StringReader( requestXML ) );
           doc = builder.parse( jobSource );
		}
		catch ( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_REQUEST
                                              , this.getComponentName() ) ; 
			logger.error( message.toString(), ex ) ;
			throw new MyComponentException( message, ex );
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "parseRequest() exit") ;	
		}
		
		return doc ;

    } // end parseRequest()
    

    private void resourceCleanup() {   	
		if( TRACE_ENABLED ) logger.debug( "resourceCleanup() entry") ;

        try {
            // YOUR CODE HERE...
            ;
        }
        catch( Exception ex) {
            ;
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "resourceCleanup() exit") ;   
        }
	
    }

 
	public String getComponentName() { return Configurator.getClassName( MyComponent.class ) ; }


} // end of class
