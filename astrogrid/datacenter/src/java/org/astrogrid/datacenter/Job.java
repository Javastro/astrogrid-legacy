package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;
import java.util.Date ;

public abstract class Job {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Job.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_JOBFACTORY_IMPL = "AGDTCE00140" ;
        
	private static final String
		JOBFACTORY_KEY = "JOB.FACTORY" ; 
	
	private static JobFactory 
	   factory ;
	   
	public static  final String
	    STATUS_INITIALIZED = "INITIALIZED",  // Newly created but not yet running
	    STATUS_RUNNING = "RUNNING",          // Currently executing
	    STATUS_COMPLETED = "COMPLETED",      // Completed OK
	    STATUS_IN_ERROR = "ERROR" ;          // Something bad happened


    public static JobFactory getFactory() throws JobException  { 
		if( TRACE_ENABLED ) logger.debug( "getFactory(): entry") ;   	
    	
		String
			implementationFactoryName = DatasetAgent.getProperty( JOBFACTORY_KEY ) ;
    	
		try{
			// Note the double lock strategy				
			if( factory == null ){
				synchronized ( Job.class ) {
					if( factory == null ){
						Object
						   obj = Class.forName( implementationFactoryName ).newInstance() ;			    			
						factory = (JobFactory)obj ;
					}
				} // end synchronized
			}
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_JOBFACTORY_IMPL, implementationFactoryName ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex );
		}
		finally{
			if( TRACE_ENABLED ) logger.debug( "getFactory(): exit") ; 	
		}    
					
		return factory; 
	
	} // end of getFactory()   	
    	
   
    public abstract void informJobMonitor( boolean bCompletion ) ;

    public abstract String getStatus() ;
    public abstract void setStatus(String status) ;

    public abstract String getId() ;   // Job URN
    
    public abstract String getName() ;
    public abstract void setName( String name ) ;
    
	public abstract Date getDate() ;
	public abstract void setDate( Date date ) ;
    
    public abstract String getUserId() ;
    public abstract void setUserId( String name ) ;
    
    public abstract String getCommunity() ;
    public abstract void setCommunity( String community ) ;
    
    public abstract String getJobMonitorURL() ;
    public abstract void setJobMonitorURL( String url ) ;
    
    public abstract JobStep getJobStep() ;
    public abstract void setJobStep( JobStep jobStep ) ;
    
	public abstract String getComment() ;
	public abstract void setComment( String comment ) ;
	
	public abstract Object getImplementation() ;
 
} // end of class Job
