

package org.astrogrid.datacenter;

import org.astrogrid.i18n.* ;

import org.apache.log4j.Logger;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader ;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource ;

public class DatasetAgent {
	
	private static final boolean 
		TRACE_ENABLED = true ;
			
	private static final String 
		CONFIG_FILENAME = "ASTROGRID_datasetconfig.properties" ;
		
	private static final String
	    ASTROGRIDERROR_FAILED_TO_PARSE_JOB_REQUEST = "Failed to parse job request",
	    ASTROGRIDERROR_COULD_NOT_READ_CONFIGFILE = "Could not read configuration file",
	    ASTROGRIDERROR_TERMINAL_FAILURE = "Query ultimately failed",
	    ASTROGRIDERROR_DATASETAGENT_NOT_INITIALIZED = "DatasetAgent not initialized" ;
			
	private static Logger 
		logger = Logger.getLogger( DatasetAgent.class ) ;
		
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
	
	
	public DatasetAgent() {
		if( TRACE_ENABLED ) logger.debug( "DatasetAgent(): entry") ;
		if( TRACE_ENABLED ) logger.debug( "DatasetAgent(): exit") ;	
	}
	
	
    public String runQuery( String jobXML ) { 	
    	if( TRACE_ENABLED ) logger.debug( "runQuery() entry") ;
    	
		String
			response = null ;
		Query
			query = null ;
			
		try {  
			 	
    	    if( configurationProperties == null ) {
			    Message
	                message = new Message( ASTROGRIDERROR_DATASETAGENT_NOT_INITIALIZED ) ;
                logger.error( message.toString() ) ;
                response = message.toString() ;
    	    }
    		else {    
    		
         	   Document
    		      queryDoc = parseRequest( jobXML ) ;
               //
               // Note - nothing on Jobs so far...
               //   	
    		   QueryFactory
    		      queryFactory = Query.getFactory( "USNOB" ) ;
    		   query = queryFactory.createQuery( queryDoc ) ;
			   query.execute() ;
			   VOTable
			      votable = query.toVOTable() ;
/*			   
			MySpaceFactory
			   mySpaceFactory = Allocation.getFactory() ;
			Allocation
			   allocation = mySpaceFactory.allocateCacheSpace( "Job/Jobstep unique id", 256 ) ;   
			votable.stream( allocation ) ;
*/			
               response = votable.toString() ;			
			   // Now touch the job monitor if you can....
			
    		} // end else		
    			
    	}
    	catch( Exception ex ) {
    		// If we were responding, we would format our error response here...
			response = ex.getMessage() ; 
			Message
				message = new Message( ASTROGRIDERROR_TERMINAL_FAILURE ) ;
			logger.error( message.toString(), ex ) ;
			if( response == null ) {
				response = message.toString() ;
			}
    	}
    	finally {
    		resourceCleanup( query ) ;
			if( TRACE_ENABLED ) logger.debug( "runQuery() exit") ;
    	}
    	
        return response ;   	
    	 	
    } // end runQuery()


    private Document parseRequest( String jobXML ) throws DatasetAgentException {  	
		if( TRACE_ENABLED ) logger.debug( "parseRequest() entry") ;
		
		Document 
		   queryDoc = null;
		DocumentBuilderFactory 
		   factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder 
	       builder = null;
	       
		try {
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
			throw new DatasetAgentException( message.toString(), ex );
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "parseRequest() exit") ;	
		}
		
		return queryDoc ;

    } // end parseRequest()
    

    private void resourceCleanup( Query query ) {   	
		if( TRACE_ENABLED ) logger.debug( "resourceCleanup() entry") ;
		if( query != null )
		    query.close() ;
		if( TRACE_ENABLED ) logger.debug( "resourceCleanup() exit") ;  		 	
    }

 
}
