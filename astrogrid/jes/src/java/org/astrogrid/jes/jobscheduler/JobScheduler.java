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
import java.util.HashMap ;
import java.util.ArrayList;
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
	    ASTROGRIDERROR_FAILED_TO_CONTACT_DATACENTER = "AGJESE00520",
        ASTROGRIDERROR_FAILED_TO_CONTACT_REGISTRY   = "AGJESE00525";
	    			
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
        
        try {
            
            steps = this.identifyDispatchableCandidates( job ) ;
            iterator = steps.listIterator() ;
            
            InputSource
               jobSource = new InputSource( new StringReader( job.getDocumentXML() ) );
            
            Document
               doc = null ; 
            
            while( iterator.hasNext() ) {
                step = (JobStep)iterator.next() ;
                doc = XMLUtils.newDocument( jobSource ) ;
                this.dispatchOneStep( step, doc ) ;
                step.getParent().setStatus( Job.STATUS_RUNNING ) ;
            }
                  
        }
        catch ( Exception ex ) {
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
	
    
    private void dispatchOneStep( JobStep step, Document doc )  throws JesException {
        if( TRACE_ENABLED ) logger.debug( "dispatchOneStep(): entry") ; 
        
        String
            requestXML = null,
            location = null ;
        
        try {
            
            requestXML = this.formatRunRequest( step, doc ) ;
            location = this.findService( step ) ;
            dispatch( requestXML, location ) ;
            
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "dispatchOneStep(): exit") ; 
        }
      
    } // end of dispatchOneStep()
    
	
	private String locateDatacenter ( JobStep step ) {
		if( TRACE_ENABLED ) logger.debug( "locateDatacenter(): entry") ;
		
		String
		    serviceLocation = null ;
		
		try {
            serviceLocation = findService( step ) ;
            if( serviceLocation == null ) 
                serviceLocation = enquireOfRegistry( step ) ;        
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


    private String findService( JobStep step ) { 
        if( TRACE_ENABLED ) logger.debug( "findService( JobStep ): entry") ;
        
        Catalog
           catalog = null ;
        String
           candidateService = null ;
        
        try {
            
            // Now try to get first catalog with a service location attached...
            Iterator
               catIt = step.getQuery().getCatalogs();
            
            while( catIt.hasNext() ) {
                
                 catalog = (Catalog)catIt.next() ;
                 candidateService = findService( catalog ) ; 
                 if( candidateService != null  )
                     break ;
                     
            } // end while
            
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "findService( JobStep ): exit") ; 
        }
        
        return candidateService ;
        
    } // end of findService( JobStep )
	
	
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
    
    
    private void dispatch( String requestXML, String datacenterLocation ) throws JesException { 
        if( TRACE_ENABLED ) logger.debug( "dispatch() entry") ;
        
        try {
            
            Object []
               parms = new Object[] { requestXML } ;
            
            Call 
               call = (Call) new Service().createCall() ;             

            call.setTargetEndpointAddress( datacenterLocation ) ;
            call.setOperationName( "runQuery" ) ;  // Set method to invoke      
            call.addParameter("jobXML", XMLType.XSD_STRING,ParameterMode.IN);
            call.setReturnType(XMLType.XSD_STRING);   
            call.invokeOneWay( parms ) ;            

        }
        catch ( Exception ex ) {
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_CONTACT_DATACENTER
                                              , this.getComponentName() ) ; 
            logger.error( message.toString(), ex ) ;
            throw new JesException( message ) ;
        } 
        finally {
            if( TRACE_ENABLED ) logger.debug( "dispatch() exit") ;  
        }                   
        
    } // end dispatch()
	
	
    private String formatRunRequest( JobStep step, Document doc ) throws JesException {
        if( TRACE_ENABLED ) logger.debug( "formatRunRequest() entry") ;
        
        String
            request = null ;
        
        try {
               
            Element
               element = null ,
               jobElement = doc.getDocumentElement() ;   // This should pick up the "job" element
              
            // set the Job id (i.e. its job URN)...
            jobElement.setAttribute( SubmissionRequestDD.JOB_URN_ATTR, step.getParent().getId() ) ;
         
            // set the URL for the JobMonitor so that it can be contacted by the datacenter... 
            jobElement.setAttribute( SubmissionRequestDD.JOB_MONITOR_URL_ATTR
                                   , JES.getProperty( JES.MONITOR_URL, JES.MONITOR_CATEGORY ) ) ; 
              
            NodeList
               nodeList = jobElement.getChildNodes() ; 
            ArrayList
               stepsToBeEliminated = new ArrayList() ; 
            Integer
               stepNumber = null ;   
               
            // identify jobsteps to be eliminated...
            for( int i=0 ; i < nodeList.getLength() ; i++ ) {
                           
                if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
                   
                    element = (Element) nodeList.item(i) ;                  

                    if( element.getTagName().equals( SubmissionRequestDD.JOBSTEP_ELEMENT ) ) {                      
                       
                        stepNumber = new Integer( element.getAttribute( SubmissionRequestDD.JOBSTEP_STEPNUMBER_ATTR).trim() ) ;

                        if( !stepNumber.equals( step.getStepNumber() ) ) {
                       	
                            stepsToBeEliminated.add( element ) ;
                        }
                        
                    }
                
                } // end if
                
            } // end for
            
            Iterator
                iterator = stepsToBeEliminated.iterator() ;
                
            while ( iterator.hasNext() ) {           	
                element = (Element)iterator.next() ;
                jobElement.removeChild( element ) ;
            }
            
            request = XMLUtils.DocumentToString( doc ) ;
              
        }
        catch ( Exception ex ) {
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_FORMAT_RUN_REQUEST
                                              , this.getComponentName() ) ; 
            logger.error( message.toString(), ex ) ;
            throw new JesException( message ) ;
        } 
        finally {
            if( TRACE_ENABLED ) logger.debug( "formatRunRequest() exit") ;  
        }       
        
        return request ;
        
    } // end of formatRunRequest()
    
	
	private String extractJobURN( Document jobDoc ) { 
		return jobDoc.getDocumentElement().getAttribute( ScheduleRequestDD.JOB_URN_ATTR ).trim() ;	
	} 
	
    
	private String enquireOfRegistry( JobStep step ) throws JesException { 
		if( TRACE_ENABLED ) logger.debug( "enquireOfRegistry() entry") ;
		
		String
		    datacenterLocation  = null ;
		
		try {
			
			// JBL Note:  BEWARE!!! Most of this is guess work.
			
			Catalog
			   catalog = (Catalog)step.getQuery().getCatalogs().next() ;
			
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
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_CONTACT_REGISTRY
                                              , this.getComponentName() ) ; 
			logger.error( message.toString(), ex ) ;
            throw new JesException(message) ;
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
    
    
    private ArrayList identifyDispatchableCandidates( Job job ) {
        if( TRACE_ENABLED ) logger.debug( "identifyDispatchableCandidates(): entry") ;
        
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
            if( TRACE_ENABLED ) logger.debug( "identifyDispatchableCandidates(): exit") ;        
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
	
    
	protected String getComponentName() { return this.getClass().getName() ; }


} // end of class JobScheduler