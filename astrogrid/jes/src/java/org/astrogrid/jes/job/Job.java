/*
 * @(#)Job.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.job;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.astrogrid.i18n.*;
import org.astrogrid.Configurator ;
import org.astrogrid.jes.JES ;
import java.util.Date ;


public abstract class Job {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Job.class ) ;
        
    private final static String 
        SUBCOMPONENT_NAME = Configurator.getClassName( Job.class );                  
		
	public static  final String
		STATUS_INITIALIZED = "INITIALIZED",  // Created but not yet running
		STATUS_RUNNING = "RUNNING",          // Currently executing
		STATUS_COMPLETED = "COMPLETED",      // Completed OK
		STATUS_IN_ERROR = "ERROR" ;          // Something bad happened
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_JOBFACTORY_IMPL = "AGJESE00140" ;
        
	private static final String
		JOBFACTORY_KEY = "JOB.FACTORY" ; 
	
	private static Class
	    factoryBuilder ;
		

	public static JobFactory getFactory() throws JobException  { 
		if( TRACE_ENABLED ) logger.debug( "getFactory(): entry") ;   	
    	
		String
			implementationFactoryName = JES.getProperty( JES.JOB_FACTORY
                                                       , JES.JOB_CATEGORY ) ;
			
		// JBL Note: We are holding txn state (the Connection) within JobFactoryImpl
		// and therefore we must return a new factory for each invocation! 
		// We may be able to get around this later and have the factory as basically
		// a single-instance object.
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
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_JOBFACTORY_IMPL
                                              , SUBCOMPONENT_NAME
                                              , implementationFactoryName ) ;
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
    
    public abstract String getDescription() ;
    public abstract void setDescription( String description ) ;
    
	public abstract Date getDate() ;
	public abstract void setDate( Date date ) ;
    
	public abstract String getUserId() ;
	public abstract void setUserId( String name ) ;
    
	public abstract String getCommunity() ;
	public abstract void setCommunity( String community ) ;
	
	public abstract Object getImplementation() ;
	
	public abstract Iterator getJobSteps() ;
	public abstract boolean addJobStep( JobStep jobStep ) ;
	public abstract boolean removeJobStep( JobStep jobStep ) ;
	
	public abstract void setDocumentXML( String docXML ) ; 
	public abstract String getDocumentXML() ;
	
	public abstract void setStatus(String status) ;
	public abstract String getStatus() ;

		
} // end of class Job
