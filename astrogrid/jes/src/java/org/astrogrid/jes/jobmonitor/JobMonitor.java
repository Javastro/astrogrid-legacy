/* 
 * @(#)JobScheduler.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */
package org.astrogrid.jes.jobmonitor; 

import org.astrogrid.jes.i18n.* ;
import org.astrogrid.jes.job.Job ;
import org.astrogrid.jes.job.JobStep ;
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

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.utils.XMLUtils;

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
	
	private static final boolean 
		TRACE_ENABLED = true ;
			
	private static final String 
		CONFIG_FILENAME              = "ASTROGRID_jesconfig.properties",
		CONFIG_MESSAGES_BASENAME     = "MESSAGES.INSTALLATION.BASENAME" ,
		CONFIG_MESSAGES_LANGUAGECODE = "MESSAGES.INSTALLATION.LANGUAGECODE" ,
		CONFIG_MESSAGES_COUNTRYCODE  = "MESSAGES.INSTALLATION.COUNTRYCODE" ;
	    
	private static final String
		ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE    = "AGJESZ00001:Monitor: Could not read my configuration file",
		ASTROGRIDERROR_JES_NOT_INITIALIZED          = "AGJESZ00002:Monitor: Not initialized. Perhaps my configuration file is missing.",
		ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGJESE00030",
		ASTROGRIDERROR_ULTIMATE_MONITORFAILURE      = "AGJESE00040",
		ASTROGRIDERROR_FAILED_TO_FORMAT_RESPONSE    = "AGJESE00400",
	    ASTROGRIDERROR_FAILED_TO_INFORM_SCHEDULER   = "AGJESE00410",
	    ASTROGRIDERROR_FAILED_TO_FORMAT_SCHEDULE    = "AGJESE00420";
	        			
	private static final String
	    PARSER_VALIDATION = "PARSER.VALIDATION" ;
	    
	private static final String 
		MONITOR_JOB_RESPONSE_TEMPLATE = "MONITOR_JOB_RESPONSE.TEMPLATE",
	    MONITOR_JOB_REQUEST_TEMPLATE = "MONITOR_JOB_REQUEST.TEMPLATE" ;
	    			
	private static Logger 
		logger = Logger.getLogger( JobMonitor.class ) ;
		
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
			// Does this really need to be synchronized?
			synchronized( configurationProperties ) {
				retValue = configurationProperties.getProperty( key ) ;
			}
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "getProperty(): exit") ;			
		}
		
		return ( retValue == null ? "" : retValue.trim() ) ;
		
	} // end of getProperty()


	private void checkPropertiesLoaded() throws JobMonitorException {
		if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() entry") ;
		if( configurationProperties == null ) {
			Message
				message = new Message( ASTROGRIDERROR_JES_NOT_INITIALIZED ) ;
			logger.error( message.toString() ) ;
		}
		if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() exit") ;
	} // end checkPropertiesLoaded()
	
	
	private Document parseRequest( String jobXML ) throws JobMonitorException {  	
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
			throw new JobMonitorException( message, ex );
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "parseRequest() exit") ;	
		}
		
		return submitDoc ;

	} // end parseRequest()
	
	
    public void monitorJob( Document monitorJobDocument ) {
		if( TRACE_ENABLED ) logger.debug( "monitorJob() entry") ;
    	
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
	        // Each JES monitor MUST be properly initialized! 
	        checkPropertiesLoaded() ;
	           
			// Create the necessary Job structures.
			// This involves persistence, so we bracket the transaction 
			// before finding and updating the JobStep status...
	        factory = Job.getFactory() ;
	        factory.begin() ;
	        job = factory.findJob( this.extractJobURN( monitorJobDocument ) ) ;
	        updateJobStepStatus( job, monitorJobDocument ) ;
	        
			// If not all job steps are finished, prod the scheduler into life...
			// (This is where the job itself can be marked as finished)
			if( jobFinished( job ) == false ) {
				scheduleJob( job ) ;
			}
                    		
			bCleanCommit = factory.end( true ) ;   // Commit and cleanup

        }
        catch( JesException jex ) {
        	
	        Message
		       detailMessage = jex.getAstroGridMessage() ,  
		       generalMessage = new Message( ASTROGRIDERROR_ULTIMATE_MONITORFAILURE ) ;
	        logger.error( detailMessage.toString(), jex ) ;
	        logger.error( generalMessage.toString() ) ;
	        
        }
        finally {
        	if( bCleanCommit == false ) {
				try{ factory.end ( false ) ; } catch( JesException jex ) {;}   // Rollback and cleanup
        	}
	        logger.debug( response.toString() );
	        if( TRACE_ENABLED ) logger.debug( "monitorJob() exit") ;
        } 
         	
    } // end of monitorJob()
	
	
	private boolean jobFinished( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "jobFinished(): entry") ;
		
		// JBL Note: Does a JobStep in error mean a Job is in error?
		// i.e. Does a JobStep in error mean a Job has stopped (finished)?
		boolean
		   bJobFinished = true ; // assume job finished or in error
		Iterator
		   iterator = job.getJobSteps() ;
		JobStep
		   jobStep = null ;
		
		try {
			
			while( iterator.hasNext() ) {
				
			    jobStep = (JobStep)iterator.next() ;
			    	
			    if( 
			        jobStep.getStatus().equals( Job.STATUS_INITIALIZED ) 
			        ||
				    jobStep.getStatus().equals( Job.STATUS_RUNNING )   )  {
				    
					bJobFinished = false ;
				    break ;
				    
				}
				
			} // end while 
			
			if( bJobFinished == true )
			   job.setStatus( Job.STATUS_COMPLETED ) ;
			
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "jobFinished(): exit") ;		
		}
		
		return bJobFinished ;
			
	} // end of jobFinished()
	
	
	private void updateJobStepStatus( Job job, Document monitorJobDocument ) {
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
	    	
			nodeList = monitorJobDocument.getChildNodes() ;  			
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {						
				if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
				    continue ;
					
				element = (Element) nodeList.item(i) ;
				if( element.getTagName().equals( MonitorRequestDD.JOBSTEP_ELEMENT ) ) {
				    stepNumber = element.getAttribute( MonitorRequestDD.JOBSTEP_NUMBER_ATTR ).trim() ;  
					status = element.getAttribute( MonitorRequestDD.JOBSTEP_STATUS_ATTR ).trim() ; 
					break ;  
				}
				
			} // end for
			
		    while( iterator.hasNext() ) {
				
			    jobStep = (JobStep)iterator.next() ;
			    	
			    if( jobStep.getStepNumber().equals( Integer.valueOf( stepNumber ) ) ) {
				   jobStep.setStatus( status ) ;
				   break ;
			    }
				
		    } // end while  
		    
	    }
	    finally {
	    	
			if( TRACE_ENABLED ) logger.debug( "updateJobStepStatus(): exit") ;
	    }
		
	} // end of updateJobStepStatus()
	  	
	  	
	private void scheduleJob( Job job ) throws JesException { 
		if( TRACE_ENABLED ) logger.debug( "scheduleJob(): exit") ;
		
		try {
			
			  Call
			     call = new Service().createCall() ;

			  // call.setTargetEndpointAddress( new URL( job.get ) );
			  
			  SOAPBodyElement[] 
			     input = new SOAPBodyElement[1];
			     
			  InputSource
			     jobSource = new InputSource( new StringReader( this.formatScheduleRequest( job ) ) ) ; 

			  DocumentBuilder 
			     builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
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
			if( TRACE_ENABLED ) logger.debug( "scheduleJob() exit") ;	
		}					
		
	} // end scheduleJob()
	
	
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
	
	
	private String extractJobURN( Document jobDoc ) { 
		if( TRACE_ENABLED ) logger.debug( "extractJobURN(): entry") ;	
		return jobDoc.getAttribute( MonitorRequestDD.JOB_URN_ATTR ) ;
		if( TRACE_ENABLED ) logger.debug( "extractJobURN(): exit") ;	
	} // end of extractJobURN()
	

} // end of class JobMonitor