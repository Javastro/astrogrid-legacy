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

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.text.MessageFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;
import org.astrogrid.datacenter.Util;
import org.astrogrid.datacenter.config.ConfigurationKeys;
import org.astrogrid.datacenter.job.Job;
import org.astrogrid.i18n.AstroGridMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * The <code>Allocation</code> class represents 
 * what?
 * <p>
 *  its a monster class, and in a fron-line package
 *  FUTURE - consider replacing with an interface and an abstract 'impl' class. Allocations are created by an abstract MySpace factory anyhow,
 * so their internals should be implementation-dependent.
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
        
    public final static String
        SUBCOMPONENT_NAME = Util.getComponentName( Allocation.class ) ;   
		
	private static String
	    ASTROGRIDERROR_MYSPACEMANAGER_RETURNED_AN_ERROR = "AGDTCE00300",
	    ASTROGRIDERROR_AXIS_FAULT_WHEN_INVOKING_MYSPACEMANAGER = "AGDTCE00310",
	    ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE = "AGDTCE00320" ;
	    
	private static String
	    MYSPACE_SUCCESS = "success" ;
		    
	private String
	    filePath = null ;
	    
	private OutputStream
	    outputStream = null ;
	
	
		
	
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
			   requestTemplate = job.getConfiguration().getProperty( ConfigurationKeys.MYSPACE_REQUEST_TEMPLATE
                                                , ConfigurationKeys.MYSPACE_CATEGORY ) ;
			Object []
			   inserts = new Object[4] ;
			inserts[0] = job.getUserId() ;
			inserts[1] = job.getCommunity() ;
			inserts[2] = job.getId() ;
			inserts[3] = filePath ;
			
			Object []
			   parms = new Object[] { MessageFormat.format( requestTemplate, inserts ) } ;

			Call 
			   call = (Call) new Service().createCall() ;
			   			  
			call.setTargetEndpointAddress( new URL( job.getConfiguration().getProperty( ConfigurationKeys.MYSPACE_URL
                                                                   , ConfigurationKeys.MYSPACE_CATEGORY ) ) ) ;
			call.setOperationName( "upLoad" ) ;  // Set method to invoke		
			call.addParameter("jobDetails", XMLType.XSD_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			
			mySpaceResponse = (String) call.invoke( parms ) ;

		}
		catch ( Exception af ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_AXIS_FAULT_WHEN_INVOKING_MYSPACEMANAGER
                                              , SUBCOMPONENT_NAME ) ; 
			logger.error( message.toString(), af ) ;
			throw new AllocationException( message , af ) ;
		} 
		finally {
			try { diagnoseResponse( mySpaceResponse ) ; }
			finally { if( TRACE_ENABLED ) logger.debug( "informMySpace(): exit") ; }	
		}	
		
    } // end of informMySpace()
    
    
    private void diagnoseResponse( String responseXML ) throws AllocationException {	
		if( TRACE_ENABLED ) logger.debug( "diagnoseResponse() entry") ;
		
		Document 
		   responseDoc = null;
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
			   element = null ;
			   
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
		        status = element.getFirstChild().getNodeValue(),  // retrieve the status value
		        details = null ;
		   if (status != null)
		       status.trim() ;
	        
		   // If it is not a success, we also need the reason 
		   // (the value of the details element)...
		   if( !status.equalsIgnoreCase( MYSPACE_SUCCESS ) ) {	   			   	    
			   for( int i=0 ; i < nodeList2.getLength() ; i++ ) {		   	
				  if( nodeList2.item(i).getNodeType() != Node.ELEMENT_NODE )
					  continue ;						
				  element = (Element) nodeList2.item(i) ;				   			
				  if( element.getTagName().equals( MySpaceManagerResponseDD.DETAILS_ELEMENT_02 ) ) 
					  break ;
			   } // end for	
		   
			   details = element.getFirstChild().getNodeValue() ;
			   if (details != null)
			       details = details.trim() ;
		    
		       AstroGridMessage
			      message = new AstroGridMessage( ASTROGRIDERROR_MYSPACEMANAGER_RETURNED_AN_ERROR
                                                , SUBCOMPONENT_NAME
                                                , status
                                                , details ) ; 
		       logger.error( message.toString() ) ;
		       throw new AllocationException( message );	
		      			   
		   } // end for
		   
		}
		catch ( ParserConfigurationException pe ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE
                                              , SUBCOMPONENT_NAME ) ; 
			logger.error( message.toString(), pe ) ;
			throw new AllocationException( message, pe );
		} 
		catch ( SAXException se ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE
                                              , SUBCOMPONENT_NAME ) ; 
			logger.error( message.toString(), se ) ;
			throw new AllocationException( message, se );
		}	
		catch ( IOException ie ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE 
                                              , SUBCOMPONENT_NAME ) ; 
			logger.error( message.toString(), ie ) ;
			throw new AllocationException( message, ie );
		}			
		finally {
			if( TRACE_ENABLED ) logger.debug( "diagnoseResponse() exit") ;	
		}

	} // end of diagnoseResponse()
    	 	

}// end of class Allocation