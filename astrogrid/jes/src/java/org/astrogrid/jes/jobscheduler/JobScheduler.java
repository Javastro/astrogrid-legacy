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

import org.astrogrid.jes.i18n.* ;
import org.astrogrid.jes.job.Job ;
import org.astrogrid.jes.job.JobStep ;
import org.astrogrid.jes.job.Catalog ;
import org.astrogrid.jes.job.JobFactory ;

import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.ResourceBundle ;
import java.util.Locale ;
import java.io.FileInputStream;
import java.io.IOException;
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
// import org.apache.axis.utils.XMLUtils;

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
	
	private static final boolean 
		TRACE_ENABLED = true ;
			
	private static final String 
		CONFIG_FILENAME              = "ASTROGRID_jesconfig.properties",
		CONFIG_MESSAGES_BASENAME     = "MESSAGES.INSTALLATION.BASENAME" ,
		CONFIG_MESSAGES_LANGUAGECODE = "MESSAGES.INSTALLATION.LANGUAGECODE" ,
		CONFIG_MESSAGES_COUNTRYCODE  = "MESSAGES.INSTALLATION.COUNTRYCODE" ;
	    
	private static final String
		ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE    = "AGJESZ00001:JobScheduler: Could not read my configuration file",
		ASTROGRIDERROR_JES_NOT_INITIALIZED          = "AGJESZ00002:JobScheduler: Not initialized. Perhaps my configuration file is missing.",
		ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGJESE00490",
		ASTROGRIDERROR_ULTIMATE_SCHEDULEFAILURE     = "AGJESE00500",
	    ASTROGRIDERROR_FAILED_TO_FORMAT_RUN_REQUEST = "AGJESE00510",
	    ASTROGRIDERROR_FAILED_TO_CONTACT_DATACENTER = "AGJESE00520" ;
	        			
	private static final String
	    PARSER_VALIDATION = "PARSER.VALIDATION" ;
	    
	private static final String 
		SUBMIT_JOB_RESPONSE_TEMPLATE = "SUBMIT_JOB_RESPONSE.TEMPLATE",
	    SCHEDULE_JOB_REQUEST_TEMPLATE = "SCHEDULE_JOB_REQUEST.TEMPLATE",
        REQUISTRY_REQUEST_TEMPLATE = "ASTROGRID_REQUISTRY_REQUEST.TEMPLATE";
	    
	private static final String
        ASTROGRID_REGISTRY_URL = "ASTROGRID_REGISTRY.URL" ;
	    			
	private static Logger 
		logger = Logger.getLogger( JobScheduler.class ) ;
		
	private static Properties
		configurationProperties = null ;
	
	static {
		doConfigure();
	}
	
	private static void doConfigure() {
		if( TRACE_ENABLED ) logger.debug( "doConfigure(): entry") ;
				
		configurationProperties = new Properties();
		
		try {
			FileInputStream istream = new FileInputStream( CONFIG_FILENAME );
			configurationProperties.load(istream);
			istream.close();
			logger.debug( configurationProperties.toString() ) ;
			
			// If successful so far, load installation-type messages
			configureMessages() ;
		}
		catch ( IOException ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE, CONFIG_FILENAME ) ;
			logger.error( message.toString(), ex ) ;
			configurationProperties = null ;
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "doConfigure(): exit") ;			
		}
		
		return ;

	} // end of doConfigure()
	  
	  
	private static void configureMessages() {
		if( TRACE_ENABLED ) logger.debug( "configureMessages(): entry") ;
			
		try {
			
			String 
				messageBundleBaseName = getProperty( CONFIG_MESSAGES_BASENAME ), 
				language = getProperty( CONFIG_MESSAGES_LANGUAGECODE ),
				country = getProperty( CONFIG_MESSAGES_COUNTRYCODE ) ;
				
			logger.debug( "messageBundleBaseName[" + messageBundleBaseName + "]\t" +
			              "language[" + language + "]\t" +
			              "country[" + country + "]" ) ;		
				
			if( messageBundleBaseName != null ) {
				     
				if( (language != null) && (!language.equals("")) )  {
			    	
				   Locale
					  locale =  new Locale( language, (country != null ? country : "") );
				   Message.setMessageResource( ResourceBundle.getBundle( messageBundleBaseName, locale ) ) ;	
				   
				}
				else {
				   Message.setMessageResource( ResourceBundle.getBundle( messageBundleBaseName ) ) ;	
				}
			    
			}
			
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "configureMessages(): exit") ;	 
		}
		 	  
	} // end of configureMessages()
	  
	
	public static String getProperty( String key ) {
		if( TRACE_ENABLED ) logger.debug( "getProperty(): entry") ;
		
		String
			retValue ;
		try {	
			retValue = configurationProperties.getProperty( key ) ;
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "getProperty(): exit") ;			
		}
		
		return ( retValue == null ? "" : retValue.trim() ) ;
		
	} // end of getProperty()


	private void checkPropertiesLoaded() throws JobSchedulerException {
		if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() entry") ;
		try {
			if( configurationProperties == null ) {
				Message
					message = new Message( ASTROGRIDERROR_JES_NOT_INITIALIZED ) ;
				logger.error( message.toString() ) ;
				throw new JobSchedulerException( message ) ;
			}
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() exit") ;
		}

	} // end checkPropertiesLoaded()
	
	
	private Document parseRequest( String jobXML ) throws JobSchedulerException {  	
		if( TRACE_ENABLED ) logger.debug( "parseRequest() entry") ;
		
		Document 
		   submitDoc = null;
		DocumentBuilderFactory 
		   factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder 
		   builder = null;
	       
		try {
		   factory.setValidating( Boolean.getBoolean( getProperty( PARSER_VALIDATION ) ) ) ; 		    
		   builder = factory.newDocumentBuilder();
		   logger.debug( jobXML ) ;
		   InputSource
			  jobSource = new InputSource( new StringReader( jobXML ) );
			submitDoc = builder.parse( jobSource );
		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST ) ; 
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
	        checkPropertiesLoaded() ;
	        
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
                    		
			bCleanCommit = factory.end ( true ) ;   // Commit and cleanup

        }
        catch( JesException jex ) {
        	
	        Message
		       detailMessage = jex.getAstroGridMessage() ,  
		       generalMessage = new Message( ASTROGRIDERROR_ULTIMATE_SCHEDULEFAILURE ) ;
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
            serviceLocation = findService( findCatalog(job) ) ;
            if( serviceLocation == null ) 
                serviceLocation = enquireOfRegistry( findCatalog(job) ) ;        
		}
		catch( Exception ex ) {
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "locateDatacenter(): exit") ;	
		}
		
		return serviceLocation ;
		
	} // end of locateDatacenter()
	
	
	private Catalog findCatalog( Job job ) {	
		if( TRACE_ENABLED ) logger.debug( "findCatalog(): entry") ;
		
		JobStep
		   jobStep = null ;
		Catalog
		   catalog = null ;
		String
		   service = null,
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
                 if( candidateService == null   &&   catIt.hasNext() )
                     catIt.remove() ;
			} // end outer while
			
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "findCatalog(): exit") ;	
		}
		
		return catalog ;
		
	} // end of findCatalog()
	
	
	private String findService( Catalog catalog ) {		
		if( TRACE_ENABLED ) logger.debug( "findService(): entry") ;
		
		String
		   service = null,
		   candidateService = null ;
		
		try {
			
			// Now try to get first service with "genuine" location...
			Iterator
			   serviceIt = catalog.getServices() ; 
			
			while( serviceIt.hasNext() ) {
               service = (String)serviceIt.next() ;
               if( service != null  &&  !service.equals("") && candidateService == null ) { 
                   candidateService = service ; 
               } else if( serviceIt.hasNext() ){
               	   serviceIt.remove() ;
               }
           	} // end outer while
			
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "findService(): exit") ;	
		}
		
		return candidateService ;		
		
	} // end of findService()
	
	  	
	private void startJob( String datacenterLocation, Job job ) throws JesException { 
		if( TRACE_ENABLED ) logger.debug( "startJob() exit") ;
		
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

		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_FAILED_TO_CONTACT_DATACENTER ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "startJob() exit") ;	
		}					
		
	} // end startJob()
	
	
	private String formatRunRequest( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "formatRunRequest() exit") ;
		
		String
		    request = null ;
		
        try {
        	
        	// JBL Note: This is probably not sufficient in anything but the short term...
        	request = job.getDocumentXML() ;

		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_FAILED_TO_FORMAT_RUN_REQUEST ) ; 
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
	
	private String enquireOfRegistry( Catalog catalog ) throws JesException { 
		if( TRACE_ENABLED ) logger.debug( "enquireOfRegistry() exit") ;
		
		String
		    datacenterLocation  = null ;
		
		try {
			
			// JBL Note:  BEWARE!!! Most of this is guess work.
			
			Object []
			   parms = new Object[] { formatRegistryRequest( catalog ) } ;
			
			Call 
			   call = (Call) new Service().createCall() ;			  

			call.setTargetEndpointAddress( new URL( JobScheduler.getProperty( ASTROGRID_REGISTRY_URL ) ) ) ;
			call.setOperationName( "runQuery" ) ;  // Set method to invoke		
			call.addParameter("jobXML", XMLType.XSD_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);   // JBL Note: Is this OK?
			
			call.invoke( parms ) ;

		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_FAILED_TO_CONTACT_DATACENTER ) ; 
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
                 template = JobScheduler.getProperty( REQUISTRY_REQUEST_TEMPLATE ) ;
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
	

} // end of class JobScheduler