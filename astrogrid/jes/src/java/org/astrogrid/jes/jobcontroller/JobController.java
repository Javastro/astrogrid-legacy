/*
 * @(#)JobController.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */
package org.astrogrid.jes.jobcontroller ;

import org.astrogrid.jes.i18n.* ;

import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.ResourceBundle ;
import java.util.Locale ;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader ; 

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;


/**
 * The <code>JobController</code> class represents ...
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
public class JobController {
	
	private static final String
		submitStubResponse_success = 
		 
	"<?xml version = '1.0' encoding = 'UTF8'?>" +
	"<response>" +
	   "<job>" +
	      "<userid>jlusted</userid>" +
	      "<community>leicester</community>" +
	      "<time>28 May 2003 10.07</time>" +
	      "<jobid>jlusted:leicester:000001</jobid>" +
	   "</job>" +
	 "</response>" ;
	 
	private static final String
		submitStubResponse_failure = 
		 
	"<?xml version = '1.0' encoding = 'UTF8'?>" +
	"<response>" +
	   "<job>" +
		  "<userid>jlusted</userid>" +
		  "<community>leicester</community>" +
		  "<time>28 May 2003 10.07</time>" +
		  "<jobid>jlusted:leicester:000001</jobid>" +
	   "</job>" +
	   "<message>Job Submmision failed. Reason?</message>" +
	 "</response>" ;
	
	private static final boolean 
		TRACE_ENABLED = true ;
			
	private static final String 
		CONFIG_FILENAME              = "ASTROGRID_jesconfig.properties",
		CONFIG_MESSAGES_BASENAME     = "MESSAGES.INSTALLATION.BASENAME" ,
		CONFIG_MESSAGES_LANGUAGECODE = "MESSAGES.INSTALLATION.LANGUAGECODE" ,
		CONFIG_MESSAGES_COUNTRYCODE  = "MESSAGES.INSTALLATION.COUNTRYCODE" ;
	    
	private static final String
		ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE    = "AGJESE00010",
		ASTROGRIDERROR_JES_NOT_INITIALIZED          = "AGJESE00020",
		ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGJESE00030",
		ASTROGRIDERROR_ULTIMATE_SUBMITFAILURE       = "AGJESE00040";
	    			
	private static Logger 
		logger = Logger.getLogger( JobController.class ) ;
		
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
			
			if( messageBundleBaseName != null ) {
				     
				if( (language != null) && (language != "") )  {
			    	
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
		return retValue ;
		
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
//        Job
//	        job = null ;
			
        try { 
	        // If properties file is not loaded, we bail out...
	        // Each JES MUST be properly initialized! 
	        checkPropertiesLoaded() ;
    		
	        // Parse the request and create the necessary Job structures...
	        Document
	           submitDoc = parseRequest( jobXML ) ;
//	        job = Job.getFactory().createJob( submitDoc ) ;
                    			
	        // Inform JobScheduler (within JES) that job requires scheduling...
//	        job.informJobScheduler() ;

            response = submitStubResponse_success ;
			
        }
        catch( JesException jex ) {
	        Message
		       detailMessage = jex.getAstroGridMessage() ,  
		       generalMessage = new Message( ASTROGRIDERROR_ULTIMATE_SUBMITFAILURE ) ;
	        logger.error( detailMessage.toString(), jex ) ;
	        logger.error( generalMessage.toString() ) ;
					
	        // Format our error response here...
	        if( response == null ) {
		        // response = generalMessage.toString() + "/n" + detailMessage.toString();
				response = submitStubResponse_failure ;
	        }
	        
        }
        finally {
	        // resourceCleanup() ;
	        if( TRACE_ENABLED ) logger.debug( "submitJob() exit") ;
        }
    	
        return response ;  
         	
    } // end of submitJob()
    
    
} // end of class JobController