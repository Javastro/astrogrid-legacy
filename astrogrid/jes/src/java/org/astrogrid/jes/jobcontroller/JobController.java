/*
 * @(#)JobController.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobcontroller ;

import org.astrogrid.jes.i18n.* ;
import org.astrogrid.jes.job.Job ;
import org.astrogrid.jes.job.JobFactory ;

import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.ResourceBundle ;
import java.util.Locale ;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader ; 
import java.text.MessageFormat ;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.utils.XMLUtils;

import java.net.URL;


/**
 * The <code>JobController</code> class represents one of the top level
 * components in the AstroGrid Job Entry System (JES), the other components
 * being the <code>JobScheduler</code> and the <code>JobMonitor</code>. 
 * <p>
 * The <code>JobController</code> accepts a request for job submission and
 * creates the necessary job structures within the Job database for scheduling
 * and tracking the Job through the AstroGrid system. It informs the JobScheduler
 * that there is a new candidate for scheduling before returning a reply which
 * contains the unique identifier for the job.
 * <p>
 * The mainline argument (the workflow) is held within the method submitJob(),
 * which should be referred to for further detail. The basic workflow is:
 *      1. Load the JobController properties (if not already loaded).
 *      2. Analyse the job submission document and create the appropriate 
 *         data structures within the Job database.
 *      3  Inform the JobScheduler.
 *      4. Format a reply, passing back the unique job identifier.
 * <p>	
 * The above does not cover use cases where errors occur.
 * <p>
 * An instance of a JobController is stateless, with some provisos:
 * 1. The JobController is driven by a properties file, held at class level.
 * 2. AstroGrid messages are held in a manner amenable to internationalization.
 * These are also loaded from a properties file, held at class level.
 * 3. Finally, and importantly, the JobController utilizes an entity which does
 * contain state - the Job entity, which currently represents a number of tables 
 * held in any suitable JDBC compliant database. However, again this is not 
 * an absolute restriction.
 *
 *
 * @author  Jeff Lusted
 * @version 1.0 28-May-2003
 * @since   AstroGrid 1.2
 */
public class JobController {

	/** Compile-time switch used to turn tracing on/off. 
	  * Set this to false to eliminate all trace statements within the byte code.*/	 	
	private static final boolean 
		TRACE_ENABLED = true ;
			
	private static final String 
		CONFIG_FILENAME              = "ASTROGRID_jesconfig.properties",
		CONFIG_MESSAGES_BASENAME     = "MESSAGES.INSTALLATION.BASENAME" ,
		CONFIG_MESSAGES_LANGUAGECODE = "MESSAGES.INSTALLATION.LANGUAGECODE" ,
		CONFIG_MESSAGES_COUNTRYCODE  = "MESSAGES.INSTALLATION.COUNTRYCODE" ;
	    
	private static final String
		ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE    = "AGJESZ00001:JobController: Could not read my configuration file",
		ASTROGRIDERROR_JES_NOT_INITIALIZED          = "AGJESZ00002:JobController: Not initialized. Perhaps my configuration file is missing.",
		ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGJESE00030",
		ASTROGRIDERROR_ULTIMATE_SUBMITFAILURE       = "AGJESE00040",
	    ASTROGRIDINFO_JOB_SUCCESSFULLY_SUBMITTED    = "AGJESI00050",
		ASTROGRIDERROR_FAILED_TO_FORMAT_RESPONSE    = "AGJESE00400",
	    ASTROGRIDERROR_FAILED_TO_INFORM_SCHEDULER   = "AGJESE00410",
	    ASTROGRIDERROR_FAILED_TO_FORMAT_SCHEDULE    = "AGJESE00420";
	        			
	private static final String
	    PARSER_VALIDATION = "PARSER.VALIDATION" ;
	    
	private static final String 
		SUBMIT_JOB_RESPONSE_TEMPLATE = "SUBMIT_JOB_RESPONSE.TEMPLATE",
	    SCHEDULE_JOB_REQUEST_TEMPLATE = "SCHEDULE_JOB_REQUEST.TEMPLATE" ;
		
    private static final String 
	    SCHEDULER_URL = "SCHEDULER.URL" ;
	    			
	private static Logger 
		logger = Logger.getLogger( JobController.class ) ;
		
	private static Properties
		configurationProperties = null ;
	
	static {
		doConfigure();
	}


	/**
	  *  
	  * Static initialization routine called during class loading.
	  * <p>
	  * Attempts to load the component's configuration properties
	  * from a properties file. If it fails, a log message is
	  * produced. If it succeeds, attempts to configure the
	  * component's default language basis for messages.
	  * <p>
	  * @see configureMessages()
	  * <p>
	  * A candidate for refactoring...
	  * @see org.astrogrid.datacenter.datasetagent.DatasetAgent
	  * @see org.astrogrid.jes.JobScheduler
	  * @see org.astrogrid.jes.JobMonitor
	  * 
	  **/           	
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
	  

	/**
	  *  
	  * Configures the component's language basis for messages.
	  * This is the installation's default language.
	  * <p>
	  * @see org.astrogrid.datacenter.i18n.Message
	  * <p>
	  * A candidate for refactoring...
	  * @see org.astrogrid.datacenter.datasetagent.DatasetAgent
	  * @see org.astrogrid.jes.JobScheduler
	  * @see org.astrogrid.jes.JobMonitor
	  * 
	  **/         	  
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
	  

	/**
	  *  
	  * Static getter for properties from the component's properties' file.
	  * <p>
	  * 
	  * A candidate for refactoring...
	  * @see org.astrogrid.datacenter.datasetagent.DatasetAgent
	  * @see org.astrogrid.jes.JobScheduler
	  * @see org.astrogrid.jes.JobMonitor
	  * 
	  * @param key - the property key
	  * @return the String value of the property, or the empty string if null
	  * 
	  **/   	
	public static String getProperty( String key ) {
		if( TRACE_ENABLED ) logger.debug( "getProperty(): entry") ;
		
		String
			retValue = configurationProperties.getProperty( key ) ;
		if( TRACE_ENABLED ) logger.debug( "getProperty(): exit") ;			
		return ( retValue == null ? "" : retValue.trim() ) ;
		
	} // end of getProperty()


	private void checkPropertiesLoaded() throws JobControllerException {
		if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() entry") ;
		if( configurationProperties == null ) {
			Message
				message = new Message( ASTROGRIDERROR_JES_NOT_INITIALIZED ) ;
			logger.error( message.toString() ) ;
		}
		if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() exit") ;
	} // end checkPropertiesLoaded()
	
	
	private Document parseRequest( String jobXML ) throws JobControllerException {  	
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
			throw new JobControllerException( message, ex );
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "parseRequest() exit") ;	
		}
		
		return submitDoc ;

	} // end parseRequest()
	
	
	
    public String submitJob( String jobXML ) {
		if( TRACE_ENABLED ) logger.debug( "submitJob() entry") ;
    	
        String
	        response = null ;
		JobFactory
		    factory = null ;
        Job
	        job = null ;
	    boolean
	        bCleanCommit = false ;    // We assume things go badly wrong! 
			
        try { 
	        // If properties file is not loaded, we bail out...
	        // Each JES MUST be properly initialized! 
	        checkPropertiesLoaded() ;
    		
	        // Parse the request... 
	        Document
	           submitDoc = parseRequest( jobXML ) ;
	           
			// Create the necessary Job structures.
			// This involves persistence, so we bracket the transaction before creating...
	        factory = Job.getFactory() ;
	        factory.begin() ;
	        job = factory.createJob( submitDoc, jobXML ) ;
                    		
			bCleanCommit = factory.end ( true ) ;   // Commit and cleanup
                    			
	        // Inform JobScheduler (within JES) that job requires scheduling...
	        informJobScheduler( job ) ;

            response = formatGoodResponse( job ) ;

        }
        catch( JesException jex ) {
        	
	        Message
		       detailMessage = jex.getAstroGridMessage() ,  
		       generalMessage = new Message( ASTROGRIDERROR_ULTIMATE_SUBMITFAILURE ) ;
	        logger.error( detailMessage.toString(), jex ) ;
	        logger.error( generalMessage.toString() ) ;
					
	        // Format our error response here...
			formatBadResponse( job, detailMessage ) ;
	        
        }
        finally {
        	if( bCleanCommit == false ) {
				try{ factory.end ( false ) ; } catch( JesException jex ) {;}   // Rollback and cleanup
        	}
	        logger.debug( response.toString() );
	        if( TRACE_ENABLED ) logger.debug( "submitJob() exit") ;
        }
    	
        return response ;  
         	
    } // end of submitJob()
    
    
    private String formatGoodResponse( Job job ) {
		Message
			message = new Message( ASTROGRIDINFO_JOB_SUCCESSFULLY_SUBMITTED ) ; 
        return formatResponse( job, message.toString() ) ;
    }
  
    
	private String formatBadResponse( Job job, Message errorMessage ) {
		return formatResponse( job, errorMessage.toString() ) ;
	}   

	
	private String formatResponse( Job job, String aMessage ) {
		if( TRACE_ENABLED ) logger.debug( "formatResponse() exit") ;
		
		String 
		   response = getProperty( SUBMIT_JOB_RESPONSE_TEMPLATE ) ;
		
		try {
			
			Object []
			   inserts = new Object[5] ;
			inserts[0] = job.getUserId() ;
			inserts[1] = job.getCommunity() ;
			inserts[2] = job.getDate() ;
			inserts[3] = job.getId() ;
			inserts[4] = aMessage ;
			
			response = MessageFormat.format( response, inserts ) ;

		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_FAILED_TO_FORMAT_RESPONSE ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "formatResponse() exit") ;	
		}		
		
		return response ;
		
	} // end of formatResponse()
	
	  	
	private void informJobScheduler( Job job ) throws JesException { 
		if( TRACE_ENABLED ) logger.debug( "informJobScheduler() exit") ;
		
		try {
			
			  Call
			     call = new Service().createCall() ;

			  call.setTargetEndpointAddress( new URL( getProperty( SCHEDULER_URL ) ) );
			     
			  InputSource
			     jobSource = new InputSource( new StringReader( formatScheduleRequest( job ) ) ) ;

			  DocumentBuilder 
			     builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
			  SOAPBodyElement[] 
			     input = new SOAPBodyElement[1];
			     
			  input[0] = new SOAPBodyElement( builder.parse( jobSource ) ) ;
        
              // JBL Note: Axis documentation states "the return immediately part isn't implemented yet"!
              call.invokeOneWay( input ) ;

		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_FAILED_TO_INFORM_SCHEDULER ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "informJobScheduler() exit") ;	
		}					
		
	} // end informJobScheduler()
	
	
	private String formatScheduleRequest( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "formatScheduleRequest() exit") ;
		
		String 
		   response = getProperty( SCHEDULE_JOB_REQUEST_TEMPLATE ) ;
		
		try {
			
			Object []
			   inserts = new Object[5] ;
			inserts[0] = job.getName() ;
			inserts[1] = job.getUserId() ;
			inserts[2] = job.getCommunity() ;
			inserts[3] = job.getDate() ;
			inserts[4] = job.getId() ;
			
			response = MessageFormat.format( response, inserts ) ;

		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_FAILED_TO_FORMAT_SCHEDULE ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "formatScheduleRequest() exit") ;	
		}		
		
		return response ;
		
	} // end of formatScheduleRequest()
	

} // end of class JobController