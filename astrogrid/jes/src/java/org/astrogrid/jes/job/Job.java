

package org.astrogrid.jes.job;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.astrogrid.jes.i18n.*;
import org.astrogrid.jes.jobcontroller.*;
import java.util.Date ;
import org.w3c.dom.* ;

/**
 * @persistent
 * @stereotype container 
 */
public abstract class Job {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Job.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_JOBFACTORY_IMPL = "AGJESE00140" ;
        
	private static final String
		JOBFACTORY_KEY = "JOB.FACTORY" ; 
	
	private static Class
	    factoryBuilder ;
		

	public static JobFactory getFactory() throws JobException  { 
		if( TRACE_ENABLED ) logger.debug( "getFactory(): entry") ;   	
    	
		String
			implementationFactoryName = JobController.getProperty( JOBFACTORY_KEY ) ;
		JobFactory
		    factory = null ;
    	
		try{
			// Note the double lock strategy				
			if( factoryBuilder == null ){
				synchronized ( Job.class ) {
					if( factoryBuilder == null ){
						factoryBuilder = Class.forName( implementationFactoryName ) ;			    			
					}
				} // end synchronized
			}
			factory = (JobFactory) factoryBuilder.newInstance() ;
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
    	

	public abstract String getId() ;   // Job URN
    
	public abstract String getName() ;
	public abstract void setName( String name ) ;
    
	public abstract Date getDate() ;
	public abstract void setDate( Date date ) ;
    
	public abstract String getUserId() ;
	public abstract void setUserId( String name ) ;
    
	public abstract String getCommunity() ;
	public abstract void setCommunity( String community ) ;
	
	public abstract Object getImplementation() ;
	
	public abstract Iterator getJobSteps() ;
	
	public abstract void setDocument( Document submitDoc ) ; 
	public abstract Document getDocument() ;
		
} // end of class Job
