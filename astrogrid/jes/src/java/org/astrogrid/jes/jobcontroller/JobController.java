/*
 * @(#)JobController.java   1.0
 *
 * AstroGrid Copyright notice.
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
		ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE    = "AGJESZ00001:JobController: Could not read my configuration file",
		ASTROGRIDERROR_JES_NOT_INITIALIZED          = "AGJESZ00002:JobController: Not initialized. Perhaps my configuration file is missing.",
		ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGJESE00030",
		ASTROGRIDERROR_ULTIMATE_SUBMITFAILURE       = "AGJESE00040";
	    			
	private static final String
	    PARSER_VALIDATION = "PARSER.VALIDATION" ;
	    			
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
	        bCommit = false ; // We assume things go wrong! 
			
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
	        job = factory.createJob( submitDoc ) ;
                    			
	        // Inform JobScheduler (within JES) that job requires scheduling...
	        informJobScheduler( job ) ;

            response = formatGoodResponse( job ) ;
			bCommit = true ;  // Successful completion!!!
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
			factory.end ( bCommit ) ;   // Commit or rollback as appropriate
	        logger.debug( response.toString() );
	        if( TRACE_ENABLED ) logger.debug( "submitJob() exit") ;
        }
    	
        return response ;  
         	
    } // end of submitJob()
    
    
    private String formatGoodResponse( Job job ) {
        return new String() ;
    }
  
    
	private String formatBadResponse( Job job, Message errorMessage ) {
		return new String() ;
	}   
	
	
	private void informJobScheduler( Job job ) throws JesException { 
		
	}
	
	
} // end of class JobController