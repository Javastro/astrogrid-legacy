
package org.astrogrid.datacenter.impl ;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;
import org.astrogrid.datacenter.JobFactory ;
import org.astrogrid.datacenter.Job ;
import org.astrogrid.datacenter.JobStep ;
import org.astrogrid.datacenter.JobDocDescriptor ;
import org.astrogrid.datacenter.JobException ;

import org.w3c.dom.* ;

import javax.sql.DataSource ;
import javax.naming.*; 
import java.sql.Connection ;
import java.sql.Statement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;

public class JobFactoryImpl implements JobFactory {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobFactoryImpl.class ) ;
	
	private static InitialContext 
		initialContext = null ;
	    
	private static final String
		JOB_DATASOURCE_LOCATION = "JOB.DATASOURCE" ; 
	private static DataSource
		datasource = null ;
	    
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_JOB_DATASOURCE            = "AGDTCE00150",
		ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION            = "AGDTCE00160",
	    ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT = "AGDTCE00170"  ;
	    	    
	private Connection
		connection = null ;
	private Statement 
		statement = null ;  
	private ResultSet 
		resultSet = null ;
	
	
    public Job createJob( Document jobDoc ) throws JobException  {
		if( TRACE_ENABLED ) logger.debug( "createJob(): entry") ;  
		 	
    	JobImpl
    	   job = null ;
    	   
    	try {
			Element 
			   docElement = jobDoc.getDocumentElement();
			job = new JobImpl( docElement ) ;
    	}
    	catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex );    		
    	}
    	finally {
			if( TRACE_ENABLED ) logger.debug( "createJob(): exit") ;   	
    	}
    	
		return job ;
		
    } // end of createJob()  


    public Job findJob( String jobURN ) throws JobException { 
    	Element
    	   element = null ;
		return new JobImpl( element ) ;   
    }

    public String deleteJob( String jobURN ) throws JobException { 
		return new String( "" ) ;   
    }
    
	public String deleteJob( Job job ) throws JobException { 
		return new String( "" ) ;   
	}
  
} // end of class JobFactoryImpl
