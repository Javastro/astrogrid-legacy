/*
 * @(#)DatasetAgent.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter;

import org.astrogrid.datacenter.i18n.* ;

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
 * The <code>DatasetAgent</code> class is the top level AstroGrid 
 * component in a data center.It contains the workflow for executing a job,
 * usually a query against an astronomical catalog. The catalog is likely held 
 * within an SQL/RDBMS style database that is JDBC compliant. However, this is
 * not an absolute restriction, and other sources can be utilized at the cost
 * of the datacenter writing an implementation factory to support their own
 * special query processing.
 * <p>
 * The mainline argument (the workflow) is held within the method runQuery(),
 * which should be referred to for further detail. The basic workflow is:
 *      1. Load the datacenter properties (if not already loaded).
 *      2. Analyse the query and create appropriate structures.
 *      3  Execute the query.
 *      4. Allocate temporary file space within the local file system.
 *      5. Convert the query into VOTable format and stream it to
 *         the allocated file.
 *      6. Inform the MySpace facility that there is a file to
 *         pick up, and give it the file location.
 *      7. Inform the JobMonitor of successful completion.
 * <p>	
 * The above does not cover use cases where errors occur.
 * <p>
 * An instance of a DatasetAgent is stateless, with some provisos:
 * 1. The datacenter is driven by a properties file, held at class level.
 * 2. AstroGrid messages are held in a manner amenable to internationalization.
 * These are also loaded from a properties file, held at class level.
 * 3. Finally, and importantly, the DatasetAgent utilizes (but does not hold it
 * as an instance variable) an entity which does contain state - the Job entity, 
 * which currently represents one table held in any suitable JDBC compliant database. 
 * However, and again, this is not an absolute restriction.
 *
 * @author  Jeff Lusted
 * @version 1.0 28-May-2003
 * @since   AstroGrid 1.2
 */
public class DatasetAgent {

	/** Compile-time switch used to turn tracing on/off. 
	  * Set this to false to eliminate all trace statements within the byte code.*/	 
	private static final boolean 
		TRACE_ENABLED = true ;
			
	private static final String 
		CONFIG_FILENAME              = "ASTROGRID_datasetconfig.properties",
		CONFIG_MESSAGES_BASENAME     = "MESSAGES.INSTALLATION.BASENAME" ,
		CONFIG_MESSAGES_LANGUAGECODE = "MESSAGES.INSTALLATION.LANGUAGECODE" ,
	    CONFIG_MESSAGES_COUNTRYCODE  = "MESSAGES.INSTALLATION.COUNTRYCODE" ;
	    
	private static final String
	    ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE    = "AGDTCZ00001:JobController: Could not read my configuration file",
	    ASTROGRIDERROR_DATASETAGENT_NOT_INITIALIZED = "AGDTCZ00002:JobController: Not initialized. Perhaps my configuration file is missing.",
	    ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST  = "AGDTCE00030",
	    ASTROGRIDERROR_ULTIMATE_QUERYFAILURE        = "AGDTCE00040";
	    
	private static final String
		PARSER_VALIDATION = "PARSER.VALIDATION" ;	    
	    			
	private static Logger 
		logger = Logger.getLogger( DatasetAgent.class ) ;
		
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
	  * @see org.astrogrid.jes.JobController
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
	  * @see org.astrogrid.jes.JobController
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
			
			// JBL Note: various things are done here to make failure
			// relative and more tasteful. Whether these are worthwhile
			// remains to be seen.
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
	  * @see org.astrogrid.jes.JobController
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
	

	/**
	  *  
	  * Default constructor.
	  * <p>
	  * Debug purposes - here for the trace statements.
	  * 
	  **/       	
	public DatasetAgent() {
		if( TRACE_ENABLED ) logger.debug( "DatasetAgent(): entry") ;
		if( TRACE_ENABLED ) logger.debug( "DatasetAgent(): exit") ;	
	}
	
	
	/**
	  * <p> 
	  * Represents the mainline workflow argument for the DatasetAgent. 
	  * <p>
	  * Shows the DatasetAgent to be a pristine component with no state.
	  * It neither uses nor creates instance variables, and therefore in
	  * the EJB model would be considered a stateless session bean.
	  * 
	  * 
	  **/     
    public String runQuery( String jobXML ) { 	
    	if( TRACE_ENABLED ) logger.debug( "runQuery() entry") ;
    	
		String
			response = null ;
		Job
		    job = null ;
		Query
			query = null ;
		Allocation
		    allocation = null ;
			
		try { 
			// If properties file is not loaded, we bail out...
			// Each DatasetAgent MUST be properly initialized! 
	        checkPropertiesLoaded() ;
    		
    		// Parse the request and create the necessary Job structures, including Query...
         	Document
    		   queryDoc = parseRequest( jobXML ) ;
            job = Job.getFactory().create( queryDoc ) ;
            
            // Execute the Query...
            job.setStatus( Job.STATUS_RUNNING ) ;
            Job.getFactory().update( job );
			job.getJobStep().getQuery().execute() ;
			   	
			// Acquire some temporary file space...		   
			allocation = Allocation.getFactory().allocateCacheSpace( job.getId() ) ;
			   
			// Produce VOTable and write it to a temporary file...   
			VOTable
			   votable = query.toVOTable( allocation ) ;
			  
			// Inform MySpace that file is ready for pickup...   
			allocation.informMySpace() ;
            			
			// Inform JobMonitor (within JES) of successful jobstep completion...
			job.informJobMonitor( true ) ;
			
			job.setStatus( Job.STATUS_COMPLETED ) ;
			Job.getFactory().update( job );		
				 
			// temporary, for testing
			response = votable.toString() ;	
    	}
    	catch( DatacenterException dex ) {
			Message
			    detailMessage = dex.getAstroGridMessage() ,  
				generalMessage = new Message( ASTROGRIDERROR_ULTIMATE_QUERYFAILURE ) ;
			logger.error( detailMessage.toString(), dex ) ;
			logger.error( generalMessage.toString() ) ;
			
			job.setStatus( Job.STATUS_IN_ERROR ) ;		
			try{ Job.getFactory().update( job ); } catch( Exception ex ) {;}     
 
			// Inform JobMonitor within JES of unsuccessful jobstep completion...
			job.informJobMonitor( false ) ;	
					
			// If we were responding, we would format our error response here...
			if( response == null ) {
				response = generalMessage.toString() + "/n" + detailMessage.toString();
			}
    	}
    	finally {
    		resourceCleanup( query, allocation ) ;
			if( TRACE_ENABLED ) logger.debug( "runQuery() exit") ;
    	}
    	
        return response ;   	
    	 	
    } // end runQuery()


    private void checkPropertiesLoaded() throws DatasetAgentException {
		if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() entry") ;
		if( configurationProperties == null ) {
			Message
				message = new Message( ASTROGRIDERROR_DATASETAGENT_NOT_INITIALIZED ) ;
			logger.error( message.toString() ) ;
		}
		if( TRACE_ENABLED ) logger.debug( "checkPropertiesLoaded() exit") ;
    } // end checkPropertiesLoaded()
    

    private Document parseRequest( String jobXML ) throws DatasetAgentException {  	
		if( TRACE_ENABLED ) logger.debug( "parseRequest() entry") ;
		
		Document 
		   queryDoc = null;
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
		   queryDoc = builder.parse( jobSource );
		}
		catch ( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST ) ; 
			logger.error( message.toString(), ex ) ;
			throw new DatasetAgentException( message, ex );
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "parseRequest() exit") ;	
		}
		
		return queryDoc ;

    } // end parseRequest()
    

    private void resourceCleanup( Query query, Allocation allocation ) {   	
		if( TRACE_ENABLED ) logger.debug( "resourceCleanup() entry") ;
		if( query != null ) query.close() ;
		if( allocation != null ) allocation.close() ;
		if( TRACE_ENABLED ) logger.debug( "resourceCleanup() exit") ;  		 	
    }

 
} // end of class DatasetAgent
