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
import java.io.InputStream;
import java.io.IOException;
import java.io.StringReader ; 
import java.text.MessageFormat ;
import java.util.Date ;
import java.sql.Timestamp ;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

import org.apache.axis.client.Service;
import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;
import org.apache.axis.message.SOAPBodyElement ;
import org.apache.axis.utils.XMLUtils ;

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
 * an absolute restriction. Note well: the JobController does not hold Job
 * as an instance variable.
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
		
	/** Compile-time switch used to turn assertions on/off. 
	  * Set this to false to eliminate all assertions statements within the byte code.*/	 	
	private static final boolean 
		ASSERTIONS_ENABLED = true ;
			
	private static final String 
	/** Properties' file for this component. */  
		CONFIG_FILENAME              = "ASTROGRID_jesconfig.properties",
	/** Key within the component's Properties' file which helps identify the appropriate
	 *  language ResourceBundle. */  
		CONFIG_MESSAGES_BASENAME     = "MESSAGES.INSTALLATION.BASENAME" ,
	/** Key within the component's Properties' file which helps identify the appropriate
	 *  language ResourceBundle. */  
		CONFIG_MESSAGES_LANGUAGECODE = "MESSAGES.INSTALLATION.LANGUAGECODE" ,
	/** Key within the component's Properties' file which helps identify the appropriate
	 *  language ResourceBundle. */  
		CONFIG_MESSAGES_COUNTRYCODE  = "MESSAGES.INSTALLATION.COUNTRYCODE" ;
	    
	private static final String
		ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE    = "AGJESZ00001:JobController: Could not read my configuration file {0}",
		ASTROGRIDERROR_JES_NOT_INITIALIZED          = "AGJESZ00002:JobController: Not initialized. Perhaps my configuration file is missing.",
		ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGJESE00030",
		ASTROGRIDERROR_ULTIMATE_SUBMITFAILURE       = "AGJESE00040",
	    ASTROGRIDINFO_JOB_SUCCESSFULLY_SUBMITTED    = "AGJESI00050",
		ASTROGRIDERROR_FAILED_TO_FORMAT_RESPONSE    = "AGJESE00400",
	    ASTROGRIDERROR_FAILED_TO_INFORM_SCHEDULER   = "AGJESE00410",
	    ASTROGRIDERROR_FAILED_TO_FORMAT_SCHEDULE    = "AGJESE00420",
        ASTROGRIDERROR_FAILED_TO_CONTACT_MESSAGELOG = "AGJESE00060",
        ASTROGRIDINFO_JOB_STATUS_MESSAGE            = "AGJESI00070" ;
	        			
	/** Key within the component's Properties' file signifying whether the web service request
	 *  document is to be parsed with validation turned on or off*/  
	private static final String
	    PARSER_VALIDATION = "PARSER.VALIDATION" ;
	    
	private static final String 
	/** Key within the component's Properties' file identifying the template
	 *  used for the submit job response */  
		SUBMIT_JOB_RESPONSE_TEMPLATE = "SUBMIT_JOB_RESPONSE.TEMPLATE",
	/** Key within the component's Properties' file identifying the template
	 *  used for the schedule job request */  
	    SCHEDULE_JOB_REQUEST_TEMPLATE = "SCHEDULE_JOB_REQUEST.TEMPLATE",
	/** Key within the component's Properties' file identifying the template
	 *  used in formatting a message document for the AstroGrid message log */  
        MESSAGE_LOG_REQUEST_TEMPLATE  = "ASTROGRID_MESSAGE_LOG_REQUEST.TEMPLATE" ;
		
    private static final String 
	/** Key within the component's Properties' file identifying the url
	 * to be used for JobScheduler requests */  
	    SCHEDULER_URL = "SCHEDULER.URL" ,
	/** Key within the component's Properties' file identifying the url
	 * to be used for AstroGrid message log requests */  
        MESSAGE_LOG_URL = "ASTROGRID_MESSAGE_LOG.URL" ,
	/** Key within the component's Properties' file identifying the url
	 * to the current JobController */  
        CONTROLLER_URL = "CONTROLLER.URL" ;

	/** Log4J logger for this class. */    			    			
	private static Logger 
		logger = Logger.getLogger( JobController.class ) ;
		
	/** The JobController's properties' file. */  
	private static Properties
		configurationProperties = null ;
	
	static {
		doConfigure();
	}
	
	public static boolean isNull ( Object o ) { return ( o == null ? true : false ) ;
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
			InputStream 
	            istream = JobController.class.getClassLoader().getResourceAsStream( CONFIG_FILENAME ) ;
			configurationProperties.load(istream);
			istream.close();
			logger.debug( configurationProperties.toString() ) ;
			
			// If successful so far, load installation-type messages
			configureMessages() ;
		}
		catch ( Exception ex ) {
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
			retValue = configurationProperties.getProperty( key.toUpperCase() ) ;
		if( TRACE_ENABLED ) logger.debug( "getProperty(): exit") ;			
		return ( retValue == null ? "" : retValue.trim() ) ;
		
	} // end of getProperty()


	private void checkPropertiesLoaded() throws JobControllerException {
		if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() entry") ;
		
		try{
			if( configurationProperties == null ) {
				Message
					message = new Message( ASTROGRIDERROR_JES_NOT_INITIALIZED ) ;
				logger.error( message.toString() ) ;
				throw new JobControllerException( message ) ;
			}
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() exit") ;
		}

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
	
	
	/**
	  * <p> 
	  * Represents the mainline workflow argument for the JobController. 
	  * <p>
	  * Shows the JobController to be a component with no state.
	  * It neither uses nor creates instance variables. In the EJB model 
	  * it would be considered a stateless session bean.
	  * 
	  * @param jobXML - The service request XML received as a String.
	  * @return A String containing the reponse document in XML.
	  * 
	  * @see SubmitJobRequest.xsd in CVS
	  * @see SubmitJobResponse.xsd in CVS
	  **/     
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
                    			
            response = formatGoodResponse( job ) ;

        }
        catch( JesException jex ) {
        	
	        Message
		       detailMessage = jex.getAstroGridMessage() ,  
		       generalMessage = new Message( ASTROGRIDERROR_ULTIMATE_SUBMITFAILURE ) ;
	        logger.error( detailMessage.toString(), jex ) ;
	        logger.error( generalMessage.toString() ) ;
					
	        // Format our error response here...
			if( job != null ) formatBadResponse( job, detailMessage ) ;
	        
        }
        finally {
        	if( bCleanCommit == false ) {
				try{ factory.end ( false ) ; } catch( JesException jex ) {;}   // Rollback and cleanup
        	}
			// Inform JobScheduler (within JES) that a job may require scheduling...
			if( job != null ) informJobScheduler( job ) ;
			// And finally, inform the AstroGrid message log of the submission details...
			if( job != null ) informAstroGridMessageLog( job ) ;
	        logger.debug( response.toString() );
	        if( TRACE_ENABLED ) logger.debug( "submitJob() exit") ;
        }
    	
        return response ;  
         	
    } // end of submitJob()
    
    
	/**
	  * <p> 
	  * Formats the "good" response to the web service - Job successfully submitted. 
	  * <p>
	  * 
	  * @param job - The job entity
	  * @return A String containing the message.
	  * 
	  * @see <code>String formatResponse( Job job, String aMessage )</code>
	  **/      
    private String formatGoodResponse( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "formatGoodResponse() entry") ;
		
		String
		    retValue = null ;
		Message
			message = null ;
		try {
			message = new Message( ASTROGRIDINFO_JOB_SUCCESSFULLY_SUBMITTED ) ; 
			retValue = formatResponse( job, message.toString() ) ;
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "formatGoodResponse() exit") ;
		}
		
        return  retValue ;
        
    }
  
  
	/**
	  * <p> 
	  * Formats a "bad" response to the web service. 
	  * <p>
	  * 
	  * @param job - The job entity
	  * @param errorMessage - appropriate error message
	  * @return A String containing the formatted message.
	  * 
	  * @see org.astrogrid.jes.i18n.Message
	  * @see <code>String formatResponse( Job job, String aMessage )</code>
	  **/         
	private String formatBadResponse( Job job, Message errorMessage ) {
		if( TRACE_ENABLED ) logger.debug( "formatBadResponse() entry") ;
		String
		   retValue = null ;
		try {
		   retValue = formatResponse( job, errorMessage.toString() ) ;
		}
		finally {
		   if( TRACE_ENABLED ) logger.debug( "formatBadResponse() exit") ;
		}		
		return retValue ;
	}   


	/**
	  * <p> 
	  * Worker routine for formatting the web service response document.
	  * <p>
	  * The response document is a simple XML document and this 
	  * routine uses an appropriately simple technique for 
	  * producing it, requiring a template loaded from a properties'
	  * file, together with the use of class <code>MessageFormat</code>.
	  * 
	  * @param job - The job entity
	  * @param aMessage - an appropriate message as a String
	  * @return A String containing the formatted document.
	  * 
	  * @see SubmitJobResponse.xsd in CVS
	  * @see java.text.MessageFormat
	  * @see the appropriate properties' file.
	  **/         
	private String formatResponse( Job job, String aMessage ) {
		if( TRACE_ENABLED ) logger.debug( "formatResponse() entry") ;
		
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
	
	
	/**
	  * <p> 
	  * Invokes the web service for job scheduling.
	  * <p>
	  * JobController, JobScheduler and JobMonitor are fairly
	  * loosely coupled components linked together by their
	  * shared use of the Job database. Here the JobController
	  * is touching the Scheduler with a oneway call to see
	  * whether the given Job (passed with this call) can be 
	  * appropriately scheduled to run somewhere.
	  * 
	  * The call itself is timely rather than system significant.
	  * It informs the scheduler that something is ready <code>now</code>.
	  * The system will eventually schedule the Job even if the
	  * call fails.
	  * 
	  * JBL Note: A candidate for refactoring.
	  * 
	  * @param job - The job entity
	  * @return void
	  * 
	  * @see ScheduleJobRequest.xsd in CVS
	  * @see java.text.MessageFormat
	  * @see the appropriate properties' file.
	  * @see formatScheduleRequest( job )
	  **/           	
	private void informJobScheduler( Job job ) { 
		if( TRACE_ENABLED ) logger.debug( "informJobScheduler() entry") ;

		try {

			Object []
			   parms = new Object[] { formatScheduleRequest( job ) } ;
			   
			Call 
			   call = (Call) new Service().createCall() ;			  

			call.setTargetEndpointAddress( new URL( JobController.getProperty( SCHEDULER_URL ) ) ) ;
			call.setOperationName( "scheduleJob" ) ;  // Set method to invoke		
			call.addParameter("scheduleJobXML", XMLType.XSD_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			
			// JBL Note: Axis documentation states the immediate return aspect is
			// not yet implemented, so we may need a fudge here.
			call.invokeOneWay( parms ) ;

		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_FAILED_TO_INFORM_SCHEDULER, ex ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "informJobScheduler(): exit") ;	
		}					
		
	} // end informJobScheduler()
	
	
	/**
	  * <p> 
	  * Worker routine for formatting the job scheduling web service 
	  * response document.
	  * <p>
	  * The scheduling request document is a simple XML document 
	  * and this routine uses an appropriately simple technique for 
	  * producing it, requiring a template loaded from a properties'
	  * file, together with the use of class <code>MessageFormat</code>.
	  * 
	  * @param job - The job entity
	  * @return A String containing the formatted request document.
	  * 
	  * @see SubmitJobResponse.xsd in CVS
	  * @see java.text.MessageFormat
	  * @see the appropriate properties' file.
	  **/         
	private String formatScheduleRequest( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "formatScheduleRequest() entry") ;
		
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
	
	
	/**
	  * <p> 
	  * Invokes the web service for using the AstroGrid message log.
	  * <p>
	  * The message log is the AstroGrid way of informing users 
	  * of significant events, e.g. in this case the submission 
	  * details of a job (successful or otherwise). 
	  * This is an AstroGrid service over and above the normal 
	  * component contract, which for JobController is discharged 
	  * by the reponse document to the JobController submission web service.
	  * 
	  * JBL Note: A candidate for refactoring.
	  * 
	  * @param job - The job entity
	  * @return void
	  * 
	  * @see formatStatusMessage( Job job )
	  **/           	
	private void informAstroGridMessageLog( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "informAstroGridMessageLog(): entry") ;
		
		try {
			
			Call 
			   call = (Call) new Service().createCall() ;
			   
			// We keep the appropriate end-point in a properties' file.
			// JBL note: Is this sufficient?
			call.setTargetEndpointAddress( new URL( JobController.getProperty( MESSAGE_LOG_URL ) ) ) ;
      
			SOAPBodyElement[] 
			   bodyElement = new SOAPBodyElement[1];
			   
			// The request document is simple enough to keep a template of it in 
			// a properties' file and use the MessageFormat class to complete it...
			String
				requestString = JobController.getProperty( MESSAGE_LOG_REQUEST_TEMPLATE ) ;
			Object []
				inserts = new Object[ 5 ] ;
			inserts[0] = JobController.getProperty( CONTROLLER_URL ) ;            // source
			inserts[1] = JobController.getProperty( MESSAGE_LOG_URL ) ;           // destination
			inserts[2] = new Timestamp( new Date().getTime() ).toString() ;       // timestamp - is this OK?
			inserts[3] = "Job submitted" ;                                        // subject
			
			// We use a worker routine to format the actual message log message...
			// JBL Note: this requires elucidation...
			inserts[4] = formatStatusMessage( job ) ;
			 
			InputSource
				requestSource = new InputSource( new StringReader( MessageFormat.format( requestString, inserts ) ) ) ;
				
// JBL Note: the following is giving errors. Talk to Peter S' ...
//			bodyElement[0] = new SOAPBodyElement( XMLUtils.newDocument( requestSource ).getDocumentElement() ) ;
    
//			logger.debug( "[call] url: " + JobController.getProperty( MESSAGE_LOG_URL ) ) ;
//			logger.debug( "[call] msg: " + bodyElement[0] ) ;
       
//			Object 
//			   result = call.invoke(bodyElement);

//			logger.debug( "[call] res: " + result ) ;
    
		}
		catch( Exception ex ) {
			Message
			   message = new Message( ASTROGRIDERROR_FAILED_TO_CONTACT_MESSAGELOG ) ;
			logger.debug( message.toString(), ex ) ;
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "informAstroGridMessageLog(): exit") ;	
		}
					
	} // end of informAstroGridMessageLog()
	
	
	/**
	  * <p> 
	  * Worker routine for formatting the job status message passed 
	  * within the AstroGrid message log request document.
	  * <p>
	  * 
	  * @param job - The job entity
	  * @return A String containing the formatted job status message.
	  * 
	  * @see java.text.MessageFormat
	  * @see the appropriate messages' properties' file.
	  **/         
	private String formatStatusMessage ( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "formatStatusMessage(): entry") ;	
		
		Message
		   message = null ;	
		
		try {
			// AGJESI00070=:JobController: Job status [{0}] job name [{1}] userid [{2}] community [{3}] job id [{4}] 
			Object []
				inserts = new Object[ 5 ] ;
			inserts[0] = job.getStatus() ;           
			inserts[1] = job.getName() ;
			inserts[2] = job.getUserId() ;
			inserts[3] = job.getCommunity() ;
			inserts[4] = job.getId() ;
			 
			message = new Message( ASTROGRIDINFO_JOB_STATUS_MESSAGE, inserts ) ;
					
		}
		catch( Exception ex ) {
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "formatStatusMessage(): exit") ;		
		}
		
		return message.toString() ;
		
	} // end of formatStatusMessage()
	

} // end of class JobController