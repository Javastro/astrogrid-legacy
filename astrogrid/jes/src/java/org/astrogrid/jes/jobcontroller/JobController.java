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

import org.astrogrid.jes.*;
import org.astrogrid.i18n.* ;
import org.astrogrid.AstroGridException ;
import org.astrogrid.jes.job.Job ;
import org.astrogrid.jes.job.JobFactory ;
import org.astrogrid.jes.JES ;

import org.apache.log4j.Logger;

import java.io.StringReader ; 
import java.text.MessageFormat ;
import java.util.Date ;
import java.sql.Timestamp ;
import java.util.ListIterator;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

import org.apache.axis.client.Service;
import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import javax.xml.rpc.ParameterMode;
import org.apache.axis.message.SOAPBodyElement ;
// import org.apache.axis.utils.XMLUtils ;

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
 * 
 * Bug#12   Jeff Lusted - 30-June-2003   NullPointerException under error conditions.
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
        ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGJESE00030",
		ASTROGRIDERROR_ULTIMATE_SUBMITFAILURE       = "AGJESE00040",
	    ASTROGRIDINFO_JOB_SUCCESSFULLY_SUBMITTED    = "AGJESI00050",
		ASTROGRIDERROR_FAILED_TO_FORMAT_RESPONSE    = "AGJESE00400",
	    ASTROGRIDERROR_FAILED_TO_INFORM_SCHEDULER   = "AGJESE00410",
	    ASTROGRIDERROR_FAILED_TO_FORMAT_SCHEDULE    = "AGJESE00420",
        ASTROGRIDERROR_FAILED_TO_CONTACT_MESSAGELOG = "AGJESE00060",
        ASTROGRIDINFO_JOB_STATUS_MESSAGE            = "AGJESI00070",
        ASTROGRIDERROR_ULTIMATE_LISTFAILURE         = "AGJESE00830" ; ;
		
	/** Log4J logger for this class. */    			    			
	private static Logger 
		logger = Logger.getLogger( JobController.class ) ;
        

    public JobController(){
        if( TRACE_ENABLED ) logger.debug( "JobController() entry/exit") ;
    }
		
        
	private Document parseRequest( String jobXML ) throws JobControllerException {  	
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
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST
                                              , this.getComponentName() ) ; 
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
	  * 
	  * Bug#12   Jeff Lusted - 30-June-2003
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
	        JES.getInstance().checkPropertiesLoaded() ;   
    		
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
        catch( AstroGridException jex ) {
        	
	        AstroGridMessage
		       detailMessage = jex.getAstroGridMessage() ,  
		       generalMessage = new AstroGridMessage( ASTROGRIDERROR_ULTIMATE_SUBMITFAILURE
                                                    , this.getComponentName() ) ;
	        logger.error( detailMessage.toString(), jex ) ;
	        logger.error( generalMessage.toString() ) ;
					
	        // Format our error response here (partly Bug#12:  generate <<<some>>> response)...
			if( job != null ) 
			    response = formatBadResponse( job, detailMessage ) ;
	        
        }
        finally {
        	if( bCleanCommit == false ) {
				try{ factory.end ( false ) ; } catch( JesException jex ) {;}   // Rollback and cleanup
        	}
			// Inform JobScheduler (within JES) that a job may require scheduling...
			if( job != null ) informJobScheduler( job ) ;
			// And finally, inform the AstroGrid message log of the submission details...
//			if( job != null ) informAstroGridMessageLog( job ) ;
			
			// (partly Bug#12) Log some response, even when response is null...
	        logger.debug(  (response != null)  ?  response.toString()  :  "reponse is null" );
	        
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
		AstroGridMessage
			message = null ;
		try {
			message = new AstroGridMessage( ASTROGRIDINFO_JOB_SUCCESSFULLY_SUBMITTED
                                          , this.getComponentName() ) ; 
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
	private String formatBadResponse( Job job, AstroGridMessage errorMessage ) {
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
		if( TRACE_ENABLED ) logger.debug( "JobController.formatResponse() entry") ;
		
        String 
            response = null ;
              
		try {
            
            response = JES.getProperty( JES.CONTROLLER_SUBMIT_JOB_RESPONSE_TEMPLATE
                                      , JES.CONTROLLER_CATEGORY ) ;
        
            logger.debug( "response template: " + response ) ;
			 
			Object []
			   inserts = new Object[5] ;
			inserts[0] = job.getUserId() ;
			inserts[1] = job.getCommunity() ;
			inserts[2] = job.getDate() ;
			inserts[3] = job.getId() ;
			inserts[4] = aMessage ;
            
            logger.debug( "inserts[0]: " + inserts[0] ) ;
            logger.debug( "inserts[1]: " + inserts[1] ) ;
            logger.debug( "inserts[2]: " + inserts[2] ) ;
            logger.debug( "inserts[3]: " + inserts[3] ) ;
            logger.debug( "inserts[4]: " + inserts[4] ) ;
            
			response = MessageFormat.format( response, inserts ) ;

		}
		catch ( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_FORMAT_RESPONSE
                                              , this.getComponentName() ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "JobController.formatResponse() exit") ;	
		}		
		
		return response ;
		
	} // end of formatResponse()


    /**
      * <p> 
      * Represents a main service call against the JobController. 
      * <p>
      * 
      * @param jobListXML - The service request XML received as a String.
      * @return A String containing the list as a reponse document in XML.
      * 
      * @see ?Request.xsd in CVS
      * @see ?Response.xsd in CVS
      * 
      **/     
    public String readJobList( String jobListXML ) {
        if( TRACE_ENABLED ) logger.debug( "jobList() entry") ;
        
        String
            response = null ;
        JobFactory
            factory = null ;
        Document
            listRequestDocument = null ;
        ListIterator
            iterator = null ;
        String
            userid = null,
            community = null ;
            
        try { 
            // If properties file is not loaded, we bail out...
            // Each JES MUST be properly initialized! 
            JES.getInstance().checkPropertiesLoaded() ;   
            
            // Parse the request... 
            listRequestDocument = parseRequest( jobListXML ) ;
            userid = extractUserid( listRequestDocument ) ;
            community = extractCommunity( listRequestDocument ) ;
            factory = Job.getFactory() ;
            iterator = factory.findUserJobs( userid, community, jobListXML ) ; 
            response = formatListGoodResponse( userid, community, iterator ) ;

        }
        catch( AstroGridException jex ) {
            
            AstroGridMessage
               detailMessage = jex.getAstroGridMessage() ,  
               generalMessage = new AstroGridMessage( ASTROGRIDERROR_ULTIMATE_LISTFAILURE
                                                    , this.getComponentName()
                                                    , userid + "@" + community ) ;
            logger.error( generalMessage.toString() ) ;
            response = formatListErrorResponse( userid, community, detailMessage ) ;
                              
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "jobList() exit") ;
        }
        
        return response ;  
            
    } // end of jobList()
    
    
    private String extractUserid( Document doc ) {
        if( TRACE_ENABLED ) logger.debug( "extractUserid() entry") ;
        
        String
            name ;
        
        try {
            Element
               element = doc.getDocumentElement() ;   
               
            name = element.getAttribute( JobListRequestDD.JOBLIST_USERID_ATTR ).trim() ;
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "extractUserid() exit") ;
        }
        
        return name ;
        
    }
    
    
    private String extractCommunity( Document doc ) {
        if( TRACE_ENABLED ) logger.debug( "extractCommunity() entry") ;
        
       String
           community ;
        
       try {
           Element
              element = doc.getDocumentElement() ;   
               
           community = element.getAttribute( JobListRequestDD.JOBLIST_COMMUNITY_ATTR ).trim() ;
       }
       finally {
           if( TRACE_ENABLED ) logger.debug( "extractCommunity() exit") ;
       }
        
       return community ;
    }
    
    
    private String formatListGoodResponse( String userid, String community, ListIterator iterator ) {
         if( TRACE_ENABLED ) logger.debug( "formatListGoodResponse() entry") ;
        
        String
            response = null ;
        // append is here to allow for an empty list (i.e. no jobs)
        StringBuffer
            rBuffer = new StringBuffer( 256 ).append(" ") ; 
        Job
            job = null ;
            
        Object []
            inserts = new Object[ 5 ] ;
      
        try {
            
            // Format the list itself ...
            while( iterator.hasNext() ) {
                
                job = (Job)iterator.next() ;
                inserts[0] = job.getName() ;                 
                inserts[1] = job.getDescription() ; 
                inserts[2] = job.getStatus() ;           
                inserts[3] = job.getDate() ;
                inserts[4] = job.getId() ;
                
                rBuffer.append( MessageFormat.format( JobListResponseDD.JOB_TEMPLATE, inserts ) ) ;
                 
            } // end while
            
            // Format the header details ...
            inserts = new Object[ 4 ] ;
            inserts[0] = "" ; // no message                 
            inserts[1] = userid ;           
            inserts[2] = community ;
            inserts[3] = rBuffer.toString() ;
             
            response = MessageFormat.format( JobListResponseDD.RESPONSE_TEMPLATE, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "formatListGoodResponse() exit") ;
        }
        
        return response ;
        
    } // end of formatListGoodResponse()
    
    
    private String formatListErrorResponse( String userid, String community, AstroGridMessage message ) {
        if( TRACE_ENABLED ) logger.debug( "formatListErrorResponse() entry") ;
        
        String
            response = null ;
                   
        try {
            
            Object []
                inserts = new Object[ 4 ] ;
            inserts[0] = message.toString() ;                 
            inserts[1] = userid ;           
            inserts[2] = community ;
            inserts[3] = "" ; // no list
             
            response = MessageFormat.format( JobListResponseDD.RESPONSE_TEMPLATE, inserts ) ;
            
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "formatListErrorResponse() exit") ;
        }
        
        return response ;
        
    } // end of formatListErrorResponse()
    
    
    
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

			call.setTargetEndpointAddress( new URL( JES.getProperty( JES.SCHEDULER_URL
			                                                       , JES.SCHEDULER_CATEGORY )  )  ) ;
			call.setOperationName( "scheduleJob" ) ;  // Set method to invoke		
			call.addParameter("scheduleJobXML", XMLType.XSD_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);
			
			// JBL Note: Axis documentation states the immediate return aspect is
			// not yet implemented, so we may need a fudge here.
			call.invokeOneWay( parms ) ;

		}
		catch ( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_INFORM_SCHEDULER
                                              , this.getComponentName()) ; 
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
		   response = JES.getProperty( JES.SCHEDULER_JOB_REQUEST_TEMPLATE
		                             , JES.SCHEDULER_CATEGORY ) ;
		
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
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_FORMAT_SCHEDULE
                                              , this.getComponentName() ) ; 
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
			call.setTargetEndpointAddress( new URL( JES.getProperty( JES.MESSAGE_LOG_URL
			                                                       , JES.MESSAGE_LOG_CATEGORY )  )  ) ;
      
			SOAPBodyElement[] 
			   bodyElement = new SOAPBodyElement[1];
			   
			// The request document is simple enough to keep a template of it in 
			// a properties' file and use the MessageFormat class to complete it...
			String
				requestString = JES.getProperty( JES.MESSAGE_LOG_REQUEST_TEMPLATE
				                               , JES.MESSAGE_LOG_CATEGORY ) ;
			Object []
				inserts = new Object[ 5 ] ;
			inserts[0] = JES.getProperty( JES.CONTROLLER_URL
			                            , JES.CONTROLLER_CATEGORY ) ;             // source
			inserts[1] = JES.getProperty( JES.MESSAGE_LOG_URL
                                        , JES.MESSAGE_LOG_CATEGORY ) ;            // destination
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
			AstroGridMessage
			   message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_CONTACT_MESSAGELOG
                                             , this.getComponentName() ) ;
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
		
		AstroGridMessage
		   message = null ;	
		
		try {
			// AGJESI00070=:JobController: Job status [{0}] job name [{1}] userid [{2}] community [{3}] job id [{4}] 
			Object []
				inserts = new Object[ 6 ] ;
            inserts[0] = this.getComponentName() ;                 
			inserts[1] = job.getStatus() ;           
			inserts[2] = job.getName() ;
			inserts[3] = job.getUserId() ;
			inserts[4] = job.getCommunity() ;
			inserts[5] = job.getId() ;
			 
			message = new AstroGridMessage( ASTROGRIDINFO_JOB_STATUS_MESSAGE, inserts ) ;
					
		}
		catch( Exception ex ) {
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "formatStatusMessage(): exit") ;		
		}
		
		return message.toString() ;
		
	} // end of formatStatusMessage()
	

	protected String getComponentName() { return JES.getClassName( this.getClass() ); }


} // end of class JobController