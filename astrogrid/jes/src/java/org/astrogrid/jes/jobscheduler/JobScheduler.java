/*
 * @(#)JobScheduler.java   1.0
 *  
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobscheduler;

import org.astrogrid.AstroGridException ;
import org.astrogrid.i18n.* ;
import org.astrogrid.jes.* ;
import org.astrogrid.jes.job.Job ;
import org.astrogrid.jes.job.JobStep ;
import org.astrogrid.jes.job.Catalog ;
import org.astrogrid.jes.job.JobFactory ;
import org.astrogrid.jes.jobcontroller.SubmissionRequestDD ;

import org.apache.log4j.Logger;

import java.io.StringReader ; 
import java.text.MessageFormat ;
import java.util.Iterator ;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

import org.apache.axis.client.Service;
import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;
import org.apache.axis.utils.XMLUtils;

import java.net.URL;


/**
 * The <code>JobScheduler</code> class represents ...
 * <p>
 * .... For example:
 * <p><blockquote><pre>
 *     
 * </pre></blockquote><p>
 *
 * @author  Jeff Lusted
 * @version 1.0 28-May-2003
 * @since   AstroGrid 1.2
 */
public class JobScheduler {
	
	/** Compile-time switch used to turn tracing on/off. 
	  * Set this to false to eliminate all trace statements within the byte code.*/	 	
	private static final boolean 
		TRACE_ENABLED = true ;
	    
	private static final String
        ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGJESE00490",
		ASTROGRIDERROR_ULTIMATE_SCHEDULEFAILURE     = "AGJESE00500",
	    ASTROGRIDERROR_FAILED_TO_FORMAT_RUN_REQUEST = "AGJESE00510",
	    ASTROGRIDERROR_FAILED_TO_CONTACT_DATACENTER = "AGJESE00520" ;
	    			
	private static Logger 
		logger = Logger.getLogger( JobScheduler.class ) ;

	
	private Document parseRequest( String jobXML ) throws JobSchedulerException {  	
		if( TRACE_ENABLED ) logger.debug( "parseRequest() entry") ;
		
		Document 
		   submitDoc = null;
		DocumentBuilderFactory 
		   factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder 
		   builder = null;
	       
		try {
		   factory.setValidating( Boolean.getBoolean( JES.getProperty( JES.CONTROLLER_PARSER_VALIDATION
		                                                             , JES.CONTROLLER_CATEGORY )  )  ) ; 		    
		   builder = factory.newDocumentBuilder();
		   logger.debug( jobXML ) ;
		   InputSource
			  jobSource = new InputSource( new StringReader( jobXML ) );
			submitDoc = builder.parse( jobSource );
		}
		catch ( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST ) ; 
			logger.error( message.toString(), ex ) ;
			throw new JobSchedulerException( message, ex );
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "parseRequest() exit") ;	
		}
		
		return submitDoc ;

	} // end parseRequest()
	
	
    public void scheduleJob( String scheduleJobXML ) {
		if( TRACE_ENABLED ) logger.debug( "scheduleJob() entry") ;
    	
		JobFactory
		    factory = null ;
        Job
	        job = null ;
	    String
	        datacenterLocation = null ;
	    boolean
	        bCleanCommit = false ;    // We assume things go badly wrong! 
			
        try { 
	        // If properties file is not loaded, we bail out...
	        // Each JES MUST be properly initialized! 
	        JES.getInstance().checkPropertiesLoaded() ;
	        
			// Parse the request... 
			Document
			   scheduleJobDocument = parseRequest( scheduleJobXML ) ;
	           
			// Create the necessary Job structures.
			// This involves persistence, so we bracket the transaction 
			// before finding, running and updating the Job...
	        factory = Job.getFactory() ;
	        factory.begin() ;
	        job = factory.findJob( this.extractJobURN( scheduleJobDocument ) ) ;
	        
	        // Locate appropriate datacenter, using the Registry if need be...
			datacenterLocation = locateDatacenter( job ) ;
	        
			// Prod the datacenter into life...
			startJob( datacenterLocation, job ) ;

			factory.updateJob( job ) ;              // Update any changed details to the database                           		
			bCleanCommit = factory.end ( true ) ;   // Commit and cleanup

        }
        catch( AstroGridException jex ) {
        	
	        AstroGridMessage
		       detailMessage = jex.getAstroGridMessage() ,  
		       generalMessage = new AstroGridMessage( ASTROGRIDERROR_ULTIMATE_SCHEDULEFAILURE ) ;
	        logger.error( detailMessage.toString(), jex ) ;
	        logger.error( generalMessage.toString() ) ;
	        
        }
        finally {
        	if( bCleanCommit == false ) {
				try{ factory.end ( false ) ; } catch( JesException jex ) {;}   // Rollback and cleanup
        	}
	        if( TRACE_ENABLED ) logger.debug( "scheduleJob() exit") ;
        } 
         	 
    } // end of scheduleJob()
	
	
	private String locateDatacenter ( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "locateDatacenter(): entry") ;
		
		String
		    serviceLocation = null ;
		
		try {
            serviceLocation = findService( job ) ;
            if( serviceLocation == null ) 
                serviceLocation = enquireOfRegistry( job ) ;        
		}
		catch( JesException jex ) {
			logger.debug( jex.getAstroGridMessage() ) ;	
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "locateDatacenter(): exit") ;	
			logger.debug( "url: " + serviceLocation ) ;	
		}
		
		return serviceLocation ;
		
	} // end of locateDatacenter()
	
	
	private String findService( Job job ) {	
		if( TRACE_ENABLED ) logger.debug( "findService( job ): entry") ;
		
		JobStep
		   jobStep = null ;
		Catalog
		   catalog = null ;
		String
		   candidateService = null ;
		
		try {
			// JBL Note: insufficient except for iteration 2.
			// Examine our one and only JobStep...
			jobStep = (JobStep)job.getJobSteps().next() ;
			
			// Now try to get first catalog with a service location attached...
			Iterator
			   catIt = jobStep.getQuery().getCatalogs();
			
			while( catIt.hasNext() ) {
				
				 catalog = (Catalog)catIt.next() ;
	             candidateService = findService( catalog ) ; 
                 if( candidateService != null  )
                     break ;
                     
			} // end while
			
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "findService( job ): exit") ;	
		}
		
		return candidateService ;
		
	} // end of findService( Job job )
	
	
	private String findService( Catalog catalog ) {		
		if( TRACE_ENABLED ) logger.debug( "findService(): entry") ;
		
		org.astrogrid.jes.job.Service 
		   service = null ;
		String
		   url = null ;
		
		try {
			
			// Now try to get first service with "genuine" location...
			Iterator
			   serviceIt = catalog.getServices() ; 
			
			while( serviceIt.hasNext() ) {
				
               service = (org.astrogrid.jes.job.Service)serviceIt.next() ;
               url = service.getUrl() ;
               if( url != null  &&  !url.equals("") ) { 
                   break  ;
               }
               
           	} // end while
			
		}
		finally {
			logger.debug( "url: " + url ) ;
			if( TRACE_ENABLED ) logger.debug( "findService(): exit") ;	
		}
		
		return url ;		
		
	} // end of findService()
	
	  	
	private void startJob( String datacenterLocation, Job job ) throws JesException { 
		if( TRACE_ENABLED ) logger.debug( "startJob() entry") ;
		
		try {
			
			Object []
			   parms = new Object[] { formatRunRequest( job ) } ;
			
			Call 
			   call = (Call) new Service().createCall() ;			  

			call.setTargetEndpointAddress( new URL( datacenterLocation ) ) ;
			call.setOperationName( "runQuery" ) ;  // Set method to invoke		
			call.addParameter("jobXML", XMLType.XSD_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);   // JBL Note: Is this OK?
			call.invokeOneWay( parms ) ;			
			job.setStatus( Job.STATUS_RUNNING ) ;

		}
		catch ( Exception ex ) {
			// job.setStatus( Job.STATUS_INITIALIZED ) ;
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_CONTACT_DATACENTER ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "startJob() exit") ;	
		}					
		
	} // end startJob()
	
	
	private String formatRunRequest( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "formatRunRequest() entry") ;
		
		String
		    request = null ;
		
        try {
        	
        	//JBL Note: This is  adequate only for iteration two where we are expecting
        	//          one jobstep only. You have been warned!
        	
			InputSource
			   jobSource = new InputSource( new StringReader( job.getDocumentXML() ) );
			
            Document
               doc = XMLUtils.newDocument( jobSource ) ;
               
            Element
               element = doc.getDocumentElement() ;   // This should pick up the "job" element
               
            // set the Job id (i.e. its job URN)...
            element.setAttribute( SubmissionRequestDD.JOB_URN_ATTR,job.getId() ) ;
            
            // set the URL for the JobMonitor so that it can be contacted by the datacenter... 
			element.setAttribute( SubmissionRequestDD.JOB_MONITOR_URL_ATTR
			                    , JES.getProperty( JES.MONITOR_URL, JES.MONITOR_URL ) ) ; 
               
			NodeList
			   nodeList = element.getChildNodes() ;  	
			   
			// identify jobstep and add the step number attribute...
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {
							
				if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
					
					element = (Element) nodeList.item(i) ;					
                    if( element.getTagName().equals( SubmissionRequestDD.JOBSTEP_ELEMENT ) ) {                   	
                    	String
                    	   stepNumber = ((JobStep)job.getJobSteps().next()).getStepNumber().toString() ;	
						element.setAttribute( SubmissionRequestDD.JOBSTEP_STEPNUMBER_ATTR, stepNumber ) ;
					}
				
				} // end if
				
			} // end for
			
			request = XMLUtils.DocumentToString( doc ) ;
               
		}
		catch ( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_FORMAT_RUN_REQUEST ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "formatRunRequest() exit") ;	
		}		
		
		return request ;
		
	} // end of formatRunRequest()
	
	
	private String extractJobURN( Document jobDoc ) { 
		return jobDoc.getDocumentElement().getAttribute( ScheduleRequestDD.JOB_URN_ATTR ).trim() ;	
	} 
	
	private String enquireOfRegistry( Job job ) throws JesException { 
		if( TRACE_ENABLED ) logger.debug( "enquireOfRegistry() entry") ;
		
		String
		    datacenterLocation  = null ;
		
		try {
			
			// JBL Note:  BEWARE!!! Most of this is guess work.
			
			Catalog
			   catalog = (Catalog)((JobStep)job.getJobSteps().next()).getQuery().getCatalogs().next() ;
			
			Object []
			   parms = new Object[] { formatRegistryRequest( catalog ) } ;
			
			Call 
			   call = (Call) new Service().createCall() ;			  

			call.setTargetEndpointAddress( new URL( JES.getProperty( JES.REGISTRY_URL
			                                                       , JES.REGISTRY_CATEGORY )  )  ) ;
			call.setOperationName( "runQuery" ) ;  // Set method to invoke		
			call.addParameter("jobXML", XMLType.XSD_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);   // JBL Note: Is this OK?
			
			call.invoke( parms ) ;

		}
		catch ( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_CONTACT_DATACENTER ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "enquireOfRegistry() exit") ;	
		}
		
		return datacenterLocation ;					
		
	} // end enquireOfRegistry()
	
	
	private String formatRegistryRequest( Catalog catalog ) {
		if( TRACE_ENABLED ) logger.debug( "formatRegistryRequest(): entry") ;
		
		String
		     requestXML = null ;	
		
		try{
			// JBL Note:  BEWARE!!! Most of this is guess work.
			
             String
                 template = JES.getProperty( JES.REGISTRY_REQUEST_TEMPLATE
                                           , JES.REGISTRY_CATEGORY ) ;
             Object[]
                 inserts = new Object[1] ;
             inserts[0] = "SELECT * FROM " + catalog.getName() ;   // This may require a table
             requestXML = MessageFormat.format( template, inserts ) ;	
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "formatRegistryRequest(): exit") ;	
		}
		
		return requestXML ;
		
	} // end of formatRegistryRequest()
	
	protected String getComponentName() { return this.getClass().getName() ; }


} // end of class JobScheduler