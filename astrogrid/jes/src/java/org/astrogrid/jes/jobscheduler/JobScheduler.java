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

import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.astrogrid.AstroGridException;
import org.astrogrid.applications.delegate.ApplicationController;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.applications.delegate.beans.ParameterValues;
import org.astrogrid.applications.delegate.beans.User;
import org.astrogrid.community.common.util.CommunityMessage;
import org.astrogrid.i18n.AstroGridMessage;
import org.astrogrid.jes.JES;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.job.Job;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.JobStep;
import org.astrogrid.jes.jobcontroller.SubmissionRequestDD;

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
	    ASTROGRIDERROR_FAILED_TO_LOCATE_TOOL        = "AGJESE00520",
        ASTROGRIDERROR_FAILED_WHEN_CONTACTING_APPLICATION_CONTROLLER   = "AGJESE00525"  ;
	    			
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
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST
                                              , this.getComponentName() ) ; 
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
            
            // Schedule one or more job steps....
            this.scheduleSteps( job ) ;

			factory.updateJob( job ) ;              // Update any changed details to the database                           		
			bCleanCommit = factory.end ( true ) ;   // Commit and cleanup

        }
        catch( AstroGridException jex ) {
        	
	        AstroGridMessage  
		       generalMessage = new AstroGridMessage( ASTROGRIDERROR_ULTIMATE_SCHEDULEFAILURE
                                                    , this.getComponentName() ) ;
	        logger.error( generalMessage.toString() ) ;
	        
        }
        finally {
        	if( bCleanCommit == false ) {
				try{ factory.end ( false ) ; } catch( JesException jex ) {;}   // Rollback and cleanup
        	}
	        if( TRACE_ENABLED ) logger.debug( "scheduleJob() exit") ;
        } 
         	 
    } // end of scheduleJob()
    
    
    private void scheduleSteps( Job job ) throws JesException {
        if( TRACE_ENABLED ) logger.debug( "scheduleSteps(): entry") ;
        
        ArrayList
            steps = null ;
        Iterator
            iterator = null ;
        JobStep
            step = null ;
        String
            communitySnippet = null ;
        
        try {
            
            communitySnippet = CommunityMessage.getMessage( job.getToken()
                                                          , job.getUserId() + "@" + job.getCommunity()
                                                          , job.getGroup() ) ;
            
            steps = this.identifyDispatchableCandidates( job ) ;
            iterator = steps.listIterator() ;
            
            while( iterator.hasNext() ) {
                step = (JobStep)iterator.next() ;
                this.dispatchOneStep( communitySnippet, step ) ;
            }
            
            job.setStatus( Job.STATUS_RUNNING ) ;
                  
        }
        catch ( Exception ex ) {
            job.setStatus( Job.STATUS_IN_ERROR ) ;
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_FORMAT_RUN_REQUEST
                                              , this.getComponentName() ) ; 
            logger.error( message.toString(), ex ) ;
            throw new JesException( message ) ;
        } 
        finally {
            if( TRACE_ENABLED ) logger.debug( "scheduleSteps(): exit") ;
        }
        
    } // end of scheduleSteps()
	
    /**
     * 
     * 
     * @param communitySnippet
     * @param step
     * @throws JesException
     */
    private void dispatchOneStep( String communitySnippet, JobStep step )  throws JesException {
        if( TRACE_ENABLED ) logger.debug( "dispatchOneStep(): entry") ; 
        
        String
            requestXML = null,
            toolLocation = null,
            jobMonitorURL = null,
            toolInterface = null ;
        ApplicationController
            applicationController = null ;
        int
            applicationID = 0;
        ParameterValues
            parameterValues = null ;
        boolean
            bSubmit = true ;
        
        try {
           
            toolLocation = locateTool( step ) ;
            toolInterface = this.getToolInterface( step ) ;
            applicationController = DelegateFactory.createDelegate( toolLocation ) ;
            parameterValues.setParameterSpec( step.getTool().toJESXMLString() ) ; 
            parameterValues.setMethodName( toolInterface ) ;
            
            final Job parent = step.getParent();
            final User user = new User();
            user.setAccount(parent.getUserId());
            user.setGroup(parent.getGroup());
            user.setToken(parent.getToken());
            
            // set the URL for the JobMonitor so that it can be contacted... 
            jobMonitorURL = JES.getProperty( JES.MONITOR_URL, JES.MONITOR_CATEGORY ) ; 
           
//            applicationID = applicationController.initializeApplication( step.getParent().getId()
//                                                                       , step.getStepNumber().toString()
//                                                                       , jobMonitorURL
//                                                                       , user
//                                                                       , parameterValues ) ;
//                                                                        
//            bSubmit = applicationController.executeApplication( applicationID ) ;
 
            if( bSubmit == true ) { 
                step.setStatus( JobStep.STATUS_RUNNING ) ;                                                          
            }
            else {
                step.setStatus( JobStep.STATUS_IN_ERROR ) ;
                AstroGridMessage
                    message = new AstroGridMessage( ASTROGRIDERROR_FAILED_WHEN_CONTACTING_APPLICATION_CONTROLLER
                                                  , this.getComponentName() ) ; 
                throw new JesException(message) ;
            }
  
        }
        catch( Exception rex ) {
            step.setStatus( JobStep.STATUS_IN_ERROR ) ;
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_FAILED_WHEN_CONTACTING_APPLICATION_CONTROLLER
                                              , this.getComponentName() ) ; 
            logger.error( message.toString(), rex ) ;
            throw new JesException(message) ;
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "dispatchOneStep(): exit") ; 
        }
      
    } // end of dispatchOneStep()
    
	
	private String extractJobURN( Document jobDoc ) { 
		return jobDoc.getDocumentElement().getAttribute( ScheduleRequestDD.JOB_URN_ATTR ).trim() ;	
	} 
    
    
    private String extractCommunitySnippet( Document jobDoc ) { 
        if( TRACE_ENABLED ) logger.debug( "JobScheduler.extractCommunitySnippet(): entry") ;

        String
            communitySnippet = null ;
        Element 
            element ;
        
        try {
                       
           NodeList
              nodeList = jobDoc.getDocumentElement().getChildNodes() ;
               
           for( int i=0 ; i < nodeList.getLength() ; i++ ) {
                        
               if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {  
               
                   element = (Element) nodeList.item(i) ;
                   if( element.getTagName().equals( SubmissionRequestDD.TOOL_ELEMENT ) ) {
                       // JBL: Where is the CommunitySnippet  ?????
                   }
                                      
               }
               
           } // end for
           
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "JobScheduler.extractCommunitySnippet(): exit") ;           
        }
        
        return communitySnippet ;
          
    } 
    
    
	private String locateTool( JobStep step ) throws JesException { 
		if( TRACE_ENABLED ) logger.debug( "JobScheduler.locateTool() entry") ;
		
		String
		    toolLocation  = null,
            toolName = step.getTool().getName() ;
            
		
		try {
		
            toolLocation = JES.getProperty( JES.TOOLS_LOCATION + toolName
                                          , JES.TOOLS_CATEGORY ) ;

		}
		catch ( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_LOCATE_TOOL
                                              , this.getComponentName() ) ; 
			logger.error( message.toString(), ex ) ;
            throw new JesException(message) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "JobScheduler.locateTool() exit") ;	
		}
		
		return toolLocation ;					
		
	} // end JobScheduler.locateTool()
    
    
    private String getToolInterface( JobStep step ) throws JesException { 
        if( TRACE_ENABLED ) logger.debug( "JobScheduler.getToolInterface() entry") ;
        
        String
            toolInterface  = null,
            toolName = step.getTool().getName() ;
             
        try {
        
            toolInterface = JES.getProperty( JES.TOOLS_INTERFACE + toolName
                                          , JES.TOOLS_CATEGORY ) ;

        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "JobScheduler.getToolInterface() exit") ;   
        }
        
        return toolInterface ;                   
        
    } // end JobScheduler.getToolInterface()
        
    
    private ArrayList identifyDispatchableCandidates( Job job ) {
        if( TRACE_ENABLED ) logger.debug( "JobScheduler.identifyDispatchableCandidates(): entry") ;
        
        Iterator
           iterator = job.getJobSteps() ;
        JobStep
           jobStep = null,
           guardStep = null ;
        String
           joinCondition = null ;
        HashMap
           guardSteps = new HashMap() ;
        ArrayList
           candidates = new ArrayList() ;
        
        try {
            
           while( iterator.hasNext() ) {
                
              jobStep = (JobStep)iterator.next() ;
              guardSteps.put( jobStep.getSequenceNumber(), jobStep ) ;
                  
              // If step status is initialized, the guardstep must be checked
              // for its status against the join condition for this step...
              if( jobStep.getStatus().equals( JobStep.STATUS_INITIALIZED ) ) {
                    
                 joinCondition = jobStep.getJoinCondition() ;
                 guardStep = (JobStep)guardSteps.get( new Integer( jobStep.getSequenceNumber().intValue() - 1 ) ) ;
                    
                 // If there is no guard step (the first step in a job?), 
                 // assume this step should execute...
                 if( guardStep == null ) {
                     this.maintainCandidateList( candidates, jobStep ) ; 
                     continue ;   
                 }  
                      
                 // If a guardstep has finished (either OK or in error)
                 // and the join condition is "any", the step should execute... 
                 if( 
                     ( guardStep.getStatus().equals( JobStep.STATUS_COMPLETED )
                       ||
                       guardStep.getStatus().equals( JobStep.STATUS_IN_ERROR ) )
                     && 
                     joinCondition.equals( JobStep.JOINCONDITION_ANY ) 
                   ) {
                         
                     this.maintainCandidateList( candidates, jobStep ) ;
                     continue ;   
                 }
                       
                 // Those that should execute provided the previous
                 // guardstep completed successfully are candidates... 
                 if( guardStep.getStatus().equals( JobStep.STATUS_COMPLETED ) 
                     &&
                     joinCondition.equals( JobStep.JOINCONDITION_TRUE )
                 ) {
                     this.maintainCandidateList( candidates, jobStep ) ;
                     continue ;   
                 }
                    
                 // Those that should execute only when the previous
                 // guardstep completed with an error are candidates...
                 if( guardStep.getStatus().equals( JobStep.STATUS_IN_ERROR ) 
                     &&
                     joinCondition.equals( JobStep.JOINCONDITION_FALSE )
                 ) {
                     this.maintainCandidateList( candidates, jobStep ) ;
                     continue ;   
                 }
                    
              } // end if
                
                    // If we get here, this step is no longer a candidate
                    // for execution...loop to examine the next step...
                
           } // end while 
           
           logger.debug( "dispatchable candidates number: " + candidates.size() ) ;
                  
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "JobScheduler.identifyDispatchableCandidates(): exit") ;        
        }
        
        return candidates ;
        
    } // end of identifyDispatchableCandidates()
    
    
    private void maintainCandidateList( ArrayList list, JobStep candidate ) {
        if( TRACE_ENABLED ) logger.debug( "maintainCandidateList(): enter") ; 
        
        try { 
            
            // If the list is empty, no problems in adding this candidate...
            if( list.isEmpty() ) {
                list.add( candidate ) ;
                return ;
            }
            
            // Get the sequence number of any member of the candidate collection...
            Integer
                sequenceNumber = ((JobStep)list.get(0)).getSequenceNumber() ;
               
            // If the sequence numbers match, add the candidate...               
            if( candidate.getSequenceNumber().compareTo( sequenceNumber ) == 0 ) {
                list.add( candidate ) ;  
            } 
            // If the sequence number is less than the collection, we need to clear
            // the collection and start again. We should only schedule jobsteps that
            // are of the same sequence number (i.e. execute those concurrently)
            else if( candidate.getSequenceNumber().compareTo( sequenceNumber ) < 0 ) {
                list.clear() ;
                list.add( candidate ) ;
            }
            
            // Ignore any candidates with a higher sequence number than the collection
                
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "maintainCandidateList(): exit") ;    
        }
        
    } // end of maintainCandidateList()
	
    
    protected String getComponentName() { return JES.getClassName( this.getClass() ); }


} // end of class JobScheduler