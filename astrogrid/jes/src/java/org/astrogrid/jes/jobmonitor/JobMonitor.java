/*
 * @(#)JobMonitor.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobmonitor; 

import org.astrogrid.jes.*;
import org.astrogrid.i18n.* ;
import org.astrogrid.AstroGridException;
import org.astrogrid.jes.job.Job ;
import org.astrogrid.jes.job.JobStep ;
import org.astrogrid.jes.job.JobFactory ;

import org.apache.log4j.Logger;

import java.io.StringReader ; 
import java.text.MessageFormat ;
import java.sql.Timestamp ;
import java.util.Date ;

import java.util.HashMap ;

import java.util.Iterator ;

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
 * The <code>JobMonitor</code> class represents ...
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
public class JobMonitor {

	/** Compile-time switch used to turn tracing on/off. 
	  * Set this to false to eliminate all trace statements within the byte code.*/	 		
	private static final boolean 
		TRACE_ENABLED = true ;
		
	private static final String
		ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGJESE00540",
		ASTROGRIDERROR_ULTIMATE_MONITORFAILURE      = "AGJESE00530",
	    ASTROGRIDERROR_FAILED_TO_INFORM_SCHEDULER   = "AGJESE00440",
	    ASTROGRIDERROR_FAILED_TO_FORMAT_SCHEDULE    = "AGJESE00430",
	    ASTROGRIDERROR_FAILED_TO_CONTACT_MESSAGELOG = "AGJESE00550",
        ASTROGRIDINFO_JOB_STATUS_MESSAGE            = "AGJESI00560" ; 
        
	private static Logger 
		logger = Logger.getLogger( JobMonitor.class ) ;
        
        
    public JobMonitor() {
        if( TRACE_ENABLED ) logger.debug( "JobMonitor() entry/exit") ;
    }
		
		
	private Document parseRequest( String jobXML ) throws JobMonitorException {  	
		if( TRACE_ENABLED ) logger.debug( "parseRequest() entry") ;
		
		Document 
		   submitDoc = null;
		DocumentBuilderFactory 
		   factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder 
		   builder = null;
	       
		try {
		   factory.setValidating( Boolean.getBoolean( JES.getProperty( JES.MONITOR_PARSER_VALIDATION
		                                                             , JES.MONITOR_CATEGORY )  )  ) ; 		    
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
			throw new JobMonitorException( message, ex );
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "parseRequest() exit") ;	
		}
		
		return submitDoc ;

	} // end parseRequest()
	
    
    public void monitorJobs() {
    }
	
    public void monitorJob( String monitorJobXML ) {
		if( TRACE_ENABLED ) logger.debug( "monitorJob() entry") ;
    	
        String
	        response = null ;
		JobFactory
		    factory = null ;
        Job
	        job = null ;
	    JobStep
	        jobStep = null ;
	    boolean
	        bCleanCommit = false ;    // We assume things go badly wrong! 
			
        try { 
	        // If properties file is not loaded, we bail out...
	        // Each JES monitor MUST be properly initialized! 
	        JES.getInstance().checkPropertiesLoaded() ;
	        
			// Parse the request... 
			Document
			   monitorJobDocument = parseRequest( monitorJobXML ) ;
	           
			// Create the necessary Job structures.
			// This involves persistence, so we bracket the transaction 
			// before finding and updating the JobStep status and comment...
			// (The comment comes from MySpace)
	        factory = Job.getFactory() ;
	        factory.begin() ;
	        job = factory.findJob( this.extractJobURN( monitorJobDocument ) ) ;
	        jobStep = updateJobStepStatus( job, monitorJobDocument ) ;
	       
            boolean
                bJobFinished = interrogateAndSetJobFinished( job ) ;
			
			factory.updateJob( job ) ;             // Update any changed details to the database       		
			bCleanCommit = factory.end( true ) ;   // Commit and cleanup
            
            // If not all job steps are finished, prod the scheduler into life...
            // (This is where the job itself can be marked as finished)
            if( bJobFinished == false ) {
                scheduleJob( job );
            }
            
        }
        catch( AstroGridException jex ) {
        	
	        AstroGridMessage
		       detailMessage = jex.getAstroGridMessage() ,  
		       generalMessage = new AstroGridMessage( ASTROGRIDERROR_ULTIMATE_MONITORFAILURE
                                                    , this.getComponentName() ) ;
	        logger.error( detailMessage.toString(), jex ) ;
	        logger.error( generalMessage.toString() ) ;
	        
        }
        finally {
        	if( bCleanCommit == false ) {
				try{ factory.end ( false ) ; } catch( JesException jex ) {;}   // Rollback and cleanup
        	}
        	// And finally, inform the message log of the MySpace details concerning this JobStep...
 //       	informAstroGridMessageLog( jobStep ) ;
	        logger.debug( (response == null) ? " " : response.toString() );
	        if( TRACE_ENABLED ) logger.debug( "monitorJob() exit") ;
        } 
         	
    } // end of monitorJob()
	
	
	private boolean interrogateAndSetJobFinished( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "interrogateAndSetJobFinished(): entry") ;
		
		// JBL Note: Does a JobStep in error mean a Job is in error?
		// i.e. Does a JobStep in error mean a Job has stopped (finished)?
        boolean
           bJobFinished = true ;
        boolean
           bStepStatus = true ;
		Iterator
		   iterator = job.getJobSteps() ;
		JobStep
		   jobStep = null,
           guardStep = null ;
        String
            joinCondition = null ;
        HashMap
           guardSteps = new HashMap() ;
		
		try {
			
			while( iterator.hasNext() ) {
				
			    jobStep = (JobStep)iterator.next() ;
                guardSteps.put( jobStep.getSequenceNumber(), jobStep ) ;
			    	
                // If anything is running, then job is obviously not finished...
                if( jobStep.getStatus().equals( JobStep.STATUS_RUNNING ) ) { 
                    bJobFinished = false ;                
                    break ;              
                }
                else if( jobStep.getStatus().equals( JobStep.STATUS_COMPLETED ) ) {
                    continue ;
                }
                else if( jobStep.getStatus().equals( JobStep.STATUS_IN_ERROR ) ) {
                    bStepStatus = false ;
                    continue ;
                }                 
                // If step status is initialized, the guardstep must be checked
                // for its status against the join condition for this step...
                else if( jobStep.getStatus().equals( JobStep.STATUS_INITIALIZED ) ) {
                    

                    joinCondition = jobStep.getJoinCondition() ;
                    guardStep = (JobStep)guardSteps.get( new Integer( jobStep.getSequenceNumber().intValue() - 1 ) ) ;
                    
                    // If there is no guard step, assume this step should execute...
                    if( guardStep == null )  {
                        bJobFinished = false ;
                        break ;   //JL questionable
                    }
                        
                    // Eliminate those that should execute in any case... 
                    if( joinCondition.equals( JobStep.JOINCONDITION_ANY ) ) {
                        bJobFinished = false ;
                        break ;   
                    }
                       
                    // Eliminate those that should execute provided the previous
                    // guardstep completed successfully... 
                    if( guardStep.getStatus().equals( JobStep.STATUS_COMPLETED ) 
                        &&
                        joinCondition.equals( JobStep.JOINCONDITION_TRUE )
                      ) {
                        bJobFinished = false ;
                        break ;   
                    }
                    
                    // Eliminate those that should execute only when the previous
                    // guardstep completed with an error...
                    if( guardStep.getStatus().equals( JobStep.STATUS_IN_ERROR ) 
                        &&
                        joinCondition.equals( JobStep.JOINCONDITION_FALSE )
                      ) {
                        bJobFinished = false ;
                        break ;   
                    }
                    
                } // end if
                
                // If we get here, this step is no longer a candidate
                // for execution...loop to examine the next step...
				
			} // end while 
			
			if( bJobFinished == true && bStepStatus == true ){
                job.setStatus( Job.STATUS_COMPLETED ) ;
			}
            else if( bJobFinished == true && bStepStatus == false ){
                job.setStatus( Job.STATUS_IN_ERROR ) ;
            }
            else {
                job.setStatus( Job.STATUS_RUNNING ) ;
            }
			  		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "interrogateAndSetJobFinished(): exit") ;		
		}
		
		return bJobFinished ;
			
	} // end of interrogateAndSetJobFinished()
	
	
	private JobStep updateJobStepStatus( Job job, Document monitorJobDocument ) {
		if( TRACE_ENABLED ) logger.debug( "updateJobStepStatus(): entry") ;
		
		NodeList
		   nodeList = null ; 
		Element
		   element = null ;	   
	    Iterator 
	       iterator = job.getJobSteps() ;
	    String
	       stepNumber = null ,
	       status = null ;
	    JobStep
	       jobStep = null ;
		
	    try {
	    	
			nodeList = monitorJobDocument.getDocumentElement().getChildNodes() ;  			
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {						
				if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
				    continue ;
					
				element = (Element) nodeList.item(i) ;
				if( element.getTagName().equals( MonitorRequestDD.JOBSTEP_ELEMENT ) ) {
				    stepNumber = element.getAttribute( MonitorRequestDD.JOBSTEP_NUMBER_ATTR ).trim() ;  
					status = element.getAttribute( MonitorRequestDD.JOBSTEP_STATUS_ATTR ).trim() ; 
                    logger.debug( "stepNumber: " + stepNumber ) ;
                    logger.debug( "status: " + status ) ;
					break ;  
				}
				
			} // end for
			
		    while( iterator.hasNext() ) {
			    jobStep = (JobStep)iterator.next() ;
			    if( jobStep.getStepNumber().equals( Integer.valueOf( stepNumber ) ) ) {
                    jobStep.setStatus( status ) ;
                    logger.debug( "jobStep status set to : " + status ) ;
                    break ;
			    }
		    } // end while  
		    		    
			nodeList = element.getChildNodes() ;  			
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {						
				if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
					continue ;				
				element = (Element) nodeList.item(i) ;
				if( element.getTagName().equals( MonitorRequestDD.COMMENT_ELEMENT ) ) {
                    jobStep.setComment( element.getNodeValue().trim() ) ; 
                    logger.debug( "jobStep comment set to : " + jobStep.getComment() ) ;
				}
      
			} // end for
			 
	    }
	    finally {
	    	
			if( TRACE_ENABLED ) logger.debug( "updateJobStepStatus(): exit") ;
	    }
	    
	    return jobStep ;
		
	} // end of updateJobStepStatus()
	  	
	  	
	private void scheduleJob( Job job ) throws JesException { 
		if( TRACE_ENABLED ) logger.debug( "scheduleJob(): entry") ;
		
		try {
			
			Object []
			   parms = new Object[] { formatScheduleRequest( job ) } ;
			
			Call 
			   call = (Call) new Service().createCall() ;			  

			call.setTargetEndpointAddress( new URL( JES.getProperty( JES.SCHEDULER_URL
                                                                   , JES.SCHEDULER_CATEGORY ) ) ) ;
			call.setOperationName( "scheduleJob" ) ;  // Set method to invoke		
			call.addParameter("scheduleJobXML", XMLType.XSD_STRING,ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);   // JBL Note: Is this OK?
			
			// JBL Note: Axis documentation states "the return immediately part isn't implemented yet"!
            call.invokeOneWay( parms ) ; 

		}
		catch ( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_INFORM_SCHEDULER 
                                              , this.getComponentName() ) ; 
			logger.error( message.toString(), ex ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "scheduleJob() exit") ;	
		}					
		
	} // end scheduleJob()
	
	
	private String formatScheduleRequest( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "formatScheduleRequest() exit") ;
		
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
	
	
	private String extractJobURN( Document jobDoc ) { 
	   if( TRACE_ENABLED ) logger.debug( "extractJobURN(): entry") ;	
       
       // JBL Note: Altered in Iteration 3 to accommodate the fact that the job URN
       // sent to the datacenter contains the step id also!!! This was to accommodate
       // the chance that multiple jobsteps might be submitted to the same datacenter.
       // As at present (iterations 2 and 3, and later?) a single resource (VOTable)
       // is created for each job step tagged with the job URN, without this there
       // would  be a clash of resource names.
       //
       // This will be inadequate where multiple resources are created by the same
       // job step. But would probably do no harm, although another mechanism for
       // resource identification will be required.
       //
	   String
		  jobURN = null ;
          
       try {
          jobURN = jobDoc.getDocumentElement().getAttribute( MonitorRequestDD.JOB_URN_ATTR ).trim() ;
          int
            iLastColon = jobURN.lastIndexOf(':') ;
          jobURN = jobURN.substring( 0, iLastColon ) ;
    
       }
       finally {
           if( TRACE_ENABLED ) logger.debug( "extractJobURN(): exit") ; 
       }
          
	   return jobURN ;
       
	} // end of extractJobURN()
	
	
	private void informAstroGridMessageLog( JobStep jobStep ) {
		if( TRACE_ENABLED ) logger.debug( "informAstroGridMessageLog(): entry") ;
		
		try {
			
			Call 
			   call = (Call) new Service().createCall() ;
			   
			call.setTargetEndpointAddress( new URL( JES.getProperty( JES.MESSAGE_LOG_URL
                                                                   , JES.MESSAGE_LOG_CATEGORY )  )  ) ;
      
			SOAPBodyElement[] 
			   bodyElement = new SOAPBodyElement[1];
			   
			String
			    requestString = JES.getProperty( JES.MESSAGE_LOG_REQUEST_TEMPLATE
			                                   , JES.MESSAGE_LOG_CATEGORY ) ;
			Object []
			    inserts = new Object[ 5 ] ;
			inserts[0] = JES.getProperty( JES.MONITOR_URL
			                            , JES.MONITOR_CATEGORY ) ;      // source
			inserts[1] = JES.getProperty( JES.MESSAGE_LOG_URL
			                            , JES.MESSAGE_LOG_CATEGORY ) ;  // destination
			inserts[2] = new Timestamp( new Date().getTime() ).toString() ; // timestamp - is this OK?
			inserts[3] = "JobStep Completion" ;                             // subject
			
			//JBL Note: this requires elucidation...
			inserts[4] = formatStatusMessage( jobStep ) ;
			 
			InputSource
				requestSource = new InputSource( new StringReader( MessageFormat.format( requestString, inserts ) ) ) ;
			bodyElement[0] = new SOAPBodyElement( XMLUtils.newDocument( requestSource ).getDocumentElement() ) ;
    
			logger.debug( "[call] url: " + JES.getProperty( JES.MESSAGE_LOG_URL
			                                              , JES.MESSAGE_LOG_CATEGORY ) ) ;
			logger.debug( "[call] msg: " + bodyElement[0] ) ;
      
			Object 
			   result = call.invoke(bodyElement);

			logger.debug( "[call] res: " + result ) ;
    
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
	
	
	private String formatStatusMessage ( JobStep jobStep ) {
		if( TRACE_ENABLED ) logger.debug( "formatStatusMessage(): entry") ;	
		
		AstroGridMessage
		   message = null ;
		Job
		   job = jobStep.getParent() ;	
		
		try {
			// AGJESI00560=:JobMonitor: Job status [{0}] job name [{1}] userid [{2}] community [{3}] job id [{4}] step name [{5}] \
			// step number [{6}] step status [{7}] step message [{8}]
			Object []
				inserts = new Object[ 10 ] ;
            inserts[0] = this.getComponentName() ;                 
			inserts[1] = job.getStatus() ;           
			inserts[2] = job.getName() ;
			inserts[3] = job.getUserId() ;
			inserts[4] = job.getCommunity() ;
			inserts[5] = job.getId() ;
			inserts[6] = jobStep.getName() ;
			inserts[7] = jobStep.getStepNumber() ;
			inserts[8] = jobStep.getStatus() ; 
			inserts[9] = jobStep.getComment() ;
			 
			message = new AstroGridMessage( ASTROGRIDINFO_JOB_STATUS_MESSAGE, inserts ) ;
					
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "formatStatusMessage(): exit") ;		
		}
		
		return message.toString() ;
		
	} // end of formatStatusMessage()
	

    protected String getComponentName() { return JES.getClassName( this.getClass() ); }


} // end of class JobMonitorG