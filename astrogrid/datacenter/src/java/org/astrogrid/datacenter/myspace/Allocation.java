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
import org.astrogrid.datacenter.datasetagent.* ;
import org.astrogrid.datacenter.job.* ;
import org.astrogrid.datacenter.i18n.* ;
import java.io.OutputStream;
import java.text.MessageFormat ;

import org.apache.axis.client.Call;
// import org.apache.axis.client.Service;
// import org.apache.axis.message.SOAPBodyElement;
// import org.apache.axis.utils.XMLUtils;
import java.net.URL;


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
		ASTROGRIDERROR_COULD_NOT_CREATE_MYSPACEFACTORY_IMPL = "AGDTCE00090",
	    ASTROGRIDERROR_MYSPACEMANAGER_RETURNED_AN_ERROR = "AGDTCE00300",
	    ASTROGRIDERROR_AXIS_FAULT_WHEN_INVOKING_MYSPACEMANAGER = "AGDTCE00310",
	    ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE = "AGDTCE00320" ;
	    
	private static String
	    MYSPACE_SUCCESS = "success" ;
	    
	private static String
		MYSPACEFACTORY_KEY = "MYSPACE.FACTORY_KEY",
	    MYSPACE_URL = "MYSPACE.URL",
	    MYSPACE_MANAGER_REQUEST_TEMPLATE = "MYSPACE.MANAGER.REQUEST_TEMPLATE" ;
	
	public static MySpaceFactory
	    factory ;
	    
	private String
	    filePath = null ;
	    
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
		
	
    public Allocation() {}
  
    
    public Allocation( String filePath, OutputStream outputStream ) {
		if( TRACE_ENABLED ) logger.debug( "Allocation(): entry") ; 
		this.filePath = filePath ;	
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
    
    
    public void informMySpace( Job job ) throws AllocationException {
		if( TRACE_ENABLED ) logger.debug( "informMySpace(): entry") ;	
		
		String
		   mySpaceResponse = null ;
		   	
		try {
			
			String
			   requestTemplate = DatasetAgent.getProperty( MYSPACE_MANAGER_REQUEST_TEMPLATE ) ;
			Object []
			   inserts = new Object[4] ;
			inserts[0] = job.getUserId() ;
			inserts[1] = job.getCommunity() ;
			inserts[2] = job.getId() ;
			inserts[3] = filePath ;
			
			Object []
			   parms = new Object[] { MessageFormat.format( requestTemplate, inserts ) } ;
					
			Call
			   call = new Service().createCall() ;

			call.setTargetEndpointAddress( new URL( getProperty( MYSPACE_URL ) ) );
			call.setProperty( Call.SOAPACTION_USE_PROPERTY, Boolean.TRUE ) ;
			call.setProperty( Call.SOAPACTION_URI_PROPERTY, "getQuote" ) ;
			call.setProperty( Call.ENCODINGSTYLE_URI_PROPERTY, "http://schemas.xmlsoap.org/soap/encoding/" ) ;
			call.setOperationName( new QName( "urn:xmltoday-delayed-quotes", "getQuote") ) ;
			call.addParameter( "symbol", XMLType.XSD_STRING, ParameterMode.IN ) ;
			call.setReturnType( XMLType.XSD_STRING ) ;
//			call.setProperty(Call.USERNAME_PROPERTY, opts.getUser());
//			call.setProperty(Call.PASSWORD_PROPERTY, opts.getPassword());

			String 
			    result = (String) call.invoke( parms ) ;
			    
			diagnoseResponse( result ) ;

		}
		catch ( AxisFault af ) {
			Message
				message = new Message( ASTROGRIDERROR_AXIS_FAULT_WHEN_INVOKING_MYSPACEMANAGER, af ) ; 
			logger.error( message.toString(), af ) ;
			throw new AllocationException( message , af ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "informMySpace(): exit") ;	
		}	
		
    } // end of informMySpace()
    
    
    private void diagnoseResponse( String responseXML ) throws AllocationException {	
		if( TRACE_ENABLED ) logger.debug( "diagnoseResponse() entry") ;
		
		Document 
		   queryDoc = null;
		DocumentBuilderFactory 
		   factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder 
		   builder = null;
	       
		try { 		
		   builder = factory.newDocumentBuilder();
		   logger.debug( responseXML ) ;
		   InputSource
			  responseSource = new InputSource( new StringReader( responseXML ) );
		   responseDoc = builder.parse( responseSource );
		   
		   NodeList
			  nodeList1 = responseDoc.getChildNodes() ;
			   
		   Element
			   element ;
			
		   // Focus on the results element...   
		   for( int i=0 ; i < nodeList1.getLength() ; i++ ) {
				if( nodeList1.item(i).getNodeType() != Node.ELEMENT_NODE )
					continue ;						
				element = (Element) nodeList1.item(i) ;				   			
				if( element.getTagName().equals( MySpaceManagerResponseDD.RESULTS_ELEMENT ) ) 
					break ;
		   } // end for	

           nodeList1 = element.getChildNodes() ;
			
		   // Focus on top level status element...   
		   for( int i=0 ; i < nodeList1.getLength() ; i++ ) {
			   if( nodeList1.item(i).getNodeType() != Node.ELEMENT_NODE )
				   continue ;						
			   element = (Element) nodeList1.item(i) ;				   			
			   if( element.getTagName().equals( MySpaceManagerResponseDD.STATUS_ELEMENT_01 ) ) 
				   break ;
		   } // end for	

		   NodeList
			  nodeList2 = element.getChildNodes() ;	
			 
			 
		   // Focus on second level status element... 
		   for( int i=0 ; i < nodeList2.getLength() ; i++ ) {
				if( nodeList2.item(i).getNodeType() != Node.ELEMENT_NODE )
					continue ;						
				element = (Element) nodeList2.item(i) ;				   			
				if( element.getTagName().equals( MySpaceManagerResponseDD.STATUS_ELEMENT_02 ) ) 
					break ;
		   } // end for	
		   
		   String
		        status = element.getNodeValue().trim(),  // retrieve the status value
		        details = null ;
		        
		   // If it is not a success, we also need the reason 
		   // (the value of the details element)...
		   if( !status.equalsIgnoreCase( MYSPACE_SUCCESS ) ) {	   			   
		    
			   for( int i=0 ; i < nodeList1.getLength() ; i++ ) {		   	
				  if( nodeList1.item(i).getNodeType() != Node.ELEMENT_NODE )
					  continue ;						
				  element = (Element) nodeList1.item(i) ;				   			
				  if( element.getTagName().equals( MySpaceManagerResponseDD.DETAILS_ELEMENT_02 ) ) 
					  break ;
			   } // end for	
			   
			   details = element.getNodeValue().trim() ;
		    
		       Message
			      message = new Message( ASTROGRIDERROR_MYSPACEMANAGER_RETURNED_AN_ERROR, status, details ) ; 
		       logger.error( message.toString() ) ;
		       throw new DatasetAgentException( message );	
		      			   
		   } // end for
		   
		}
		catch ( NullPointerException ex ) {
			Message
				message = new Message( ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE ) ; 
			logger.error( message.toString(), ex ) ;
			throw new DatasetAgentException( message, ex );
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "diagnoseResponse() exit") ;	
		}

	} // end of diagnoseResponse()
    	 	

}// end of class Allocation