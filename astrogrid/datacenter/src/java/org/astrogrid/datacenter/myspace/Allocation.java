/*
 * @(#)Allocation.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.myspace;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.datasetagent.*;
import org.astrogrid.datacenter.i18n.*;
import java.io.OutputStream;

/*
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.utils.XMLUtils;
import java.net.URL;
*/

/**
 * The <code>Allocation</code> class represents 
 * <p>
 *
 * @author  Jeff Lusted
 * @version 1.0 27-May-2003
 * @since   AstroGrid 1.2
 */
public class Allocation {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Allocation.class ) ;
		
	private static String
		ASTROGRIDERROR_COULD_NOT_CREATE_MYSPACEFACTORY_IMPL = "AGDTCE00090" ;
        
	private static String
		MYSPACEFACTORY_KEY = "MYSPACEFACTORY_KEY",
	    MYSPACE_URL = "MYSPACE.URL" ; 
	
	public static MySpaceFactory
	    factory ;
	    
	private OutputStream
	    outputStream = null ;
	
	public static MySpaceFactory getFactory() throws AllocationException { 
		if( TRACE_ENABLED ) logger.debug( "getFactory(): entry") ;   	
    	
		String
			implementationFactoryName = DatasetAgent.getProperty( MYSPACEFACTORY_KEY ) ;
    	
		try{
			// Note the double lock strategy				
			if( factory == null ){
				synchronized ( Allocation.class ) {
					if( factory == null ){
						Object
						   obj = Class.forName( implementationFactoryName ).newInstance() ;			    			
						factory = (MySpaceFactory)obj ;
					}
				} // end synchronized
			}
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_MYSPACEFACTORY_IMPL, implementationFactoryName ) ;
			logger.error( message.toString(), ex ) ;
			throw new AllocationException( message, ex );
		}
		finally{
			if( TRACE_ENABLED ) logger.debug( "getFactory(): exit") ; 	
		}    
					
		return factory; 
	
	} // end of getFactory()
		
	
    public Allocation() {
    	
    }
  
    
    public Allocation( OutputStream outputStream ) {
		if( TRACE_ENABLED ) logger.debug( "Allocation(): entry") ; 	
        this.outputStream = outputStream ;	
		if( TRACE_ENABLED ) logger.debug( "Allocation(): exit") ; 	
    }
    
    
    public OutputStream getOutputStream() {
    	return outputStream ;
    }
   
   
    public void close () {
		if( TRACE_ENABLED ) logger.debug( "close(): entry") ; 	
		
    	if( outputStream != null ) {
    	    try{ 
    	    	outputStream.close(); 
    	    } 
    	    catch( java.io.IOException ex ) {;} 
    	    finally{ 
    	    	outputStream = null ; 
				if( TRACE_ENABLED ) logger.debug( "close(): exit") ; 	
    	    } 
    	}  
    	
    }// end of close()
    
    
    public void informMySpace() throws AllocationException {
		if( TRACE_ENABLED ) logger.debug( "informMySpace(): entry") ;	
		   	
		try {
/*			
			  Call
				 call = new Service().createCall() ;

			  call.setTargetEndpointAddress( new URL( getProperty( MYSPACE_URL ) ) );
			     
			  InputSource
				 jobSource = new InputSource( new StringReader( formatScheduleRequest( job ) ) ) ;

			  DocumentBuilder 
				 builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
			  SOAPBodyElement[] 
				 input = new SOAPBodyElement[1];
			     
			  input[0] = new SOAPBodyElement( builder.parse( jobSource ) ) ;
*/			
//			call.setProperty(Call.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
//			call.setProperty(Call.SOAPACTION_URI_PROPERTY, "getQuote");
//			call.setProperty(Call.ENCODINGSTYLE_URI_PROPERTY,
//					"http://schemas.xmlsoap.org/soap/encoding/");
//			call.setOperationName(new QName("urn:xmltoday-delayed-quotes", "getQuote"));
//			call.addParameter("symbol", XMLType.XSD_STRING, ParameterMode.IN);
//			call.setReturnType(XMLType.XSD_FLOAT);

			/* Define some service specific properties */
			/*******************************************/
//			call.setProperty(Call.USERNAME_PROPERTY, opts.getUser());
//			call.setProperty(Call.PASSWORD_PROPERTY, opts.getPassword());

			/* Get symbol and invoke the service */
			/*************************************/
//			Object result = call.invoke(new Object[] {symbol = args[0]});

//			return ((Float) result).floatValue();
			       
//			  call.invoke( input ) ;

		}
		catch ( Exception ex ) {
			Message
				message = new Message( "exception within Allocation" ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "informMySpace(): exit") ;	
		}	
		
    } // end of informMySpace()


}// end of class Allocation
