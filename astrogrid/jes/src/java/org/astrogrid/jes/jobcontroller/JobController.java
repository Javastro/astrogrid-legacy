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
import java.text.MessageFormat ;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.axis.utils.Options;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.Vector;


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
		ASTROGRIDERROR_FAILED_TO_FORMAT_RESPONSE    = "AGJESE00400",
	    ASTROGRIDERROR_FAILED_TO_INFORM_SCHEDULER   = "AGJESE00410" ;
	    			
	private static final String
	    PARSER_VALIDATION = "PARSER.VALIDATION" ;
	    
	private static final String 
		SUBMIT_JOB_RESPONSE_TEMPLATE = "SUBMIT_JOB_RESPONSE.TEMPLATE" ;
		
    private static final String 
	    SCHEDULER_URL = "SCHEDULER.URL" ;
	    			
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
        return formatResponse( job, "" ) ;
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
		
	} // end of formatBadResponse()
	
	  	
	private void informJobScheduler( Job job ) throws JesException { 
		if( TRACE_ENABLED ) logger.debug( "informJobScheduler() exit") ;
		
		try {
			
			  Call
			     call = new Service().createCall() ;

			  call.setTargetEndpointAddress( new URL( getProperty( SCHEDULER_URL ) ) );
			  
			  SOAPBodyElement[] 
			     input = new SOAPBodyElement[1];

			  DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			  Document doc            = builder.newDocument();   
//			  Element cdataElem       = doc.createElementNS("urn:foo", "e3");
//			  CDATASection cdata      = doc.createCDATASection("Text with\n\tImportant  <b>  whitespace </b> and tags! ");	    
//			  cdataElem.appendChild(cdata) ;
		
			  input[0] = new SOAPBodyElement( doc ) ;
        
			  Vector
			     elems = (Vector) call.invoke( input ) ;
			  SOAPBodyElement 
			     elem = null ;
			  Element         
			     e = null ;

			  elem = (SOAPBodyElement) elems.get(0) ;
			  e    = elem.getAsDOM() ;

			  String str = "Res elem[0]=" + XMLUtils.ElementToString(e) ;

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
	
	
} // end of class JobController