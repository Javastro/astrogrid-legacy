/*
 * @(#)JobFactoryImpl.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 
package org.astrogrid.datacenter.impl ;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.FactoryProvider;
import org.astrogrid.datacenter.Util;
import org.astrogrid.datacenter.config.ConfigurableImpl;
import org.astrogrid.datacenter.config.ConfigurationKeys;
import org.astrogrid.datacenter.job.Job;
import org.astrogrid.datacenter.job.JobException;
import org.astrogrid.datacenter.job.JobFactory;
import org.astrogrid.i18n.AstroGridMessage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * The <code>JobFactoryImpl</code> class contains all persistence
 * processing as relates to the <code>Job</code> entity.
 * It implements the critical interface <code>JobFactory</code>.
 * <p>
 * <code>JobFactoryImpl</code> or any implementation of the interface
 * <code>JobFactory</code> is a single instance object that is shared!
 * As such it should contain no state that is not thread-safe, and
 * preferably none at all. In the implementation shown below,
 * the only state of significance is the DataSource; this is thread-
 * safe and its initialization routine is synchronized.
 *
 * @author  Jeff Lusted
 * @version 1.0 08-Jun-2003
 * @see     org.astrogrid.JobFactory
 * @see     org.astrogrid.Job
 * @since   AstroGrid 1.2
 */
public class JobFactoryImpl  extends ConfigurableImpl implements JobFactory{
	
	/** Compile-time switch used to turn tracing on/off. */
	private static final boolean 
		TRACE_ENABLED = true ;

	/** Log4J logger for this class. */    				
	private static Logger 
		logger = Logger.getLogger( JobFactoryImpl.class ) ;
        
    private final static String SUBCOMPONENT_NAME = Util.getComponentName( JobFactoryImpl.class );
        	

	public static final String
	    INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, JOBNAME, RUNTIMESTAMP, USERID, COMMUNITY, STATUS, COMMENT ) " +
	                      "VALUES ( ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}'', ''{7}'' )" ,              
	    UPDATE_TEMPLATE = "UPDATE {0} SET RUNTIMESTAMP = ?, STATUS = ?, COMMENT = ? WHERE JOBURN = ?" ,
	    SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = {1}" ,
	    DELETE_TEMPLATE = "DELETE FROM {0} WHERE JOBURN = {1}" ;
	    
	private static final int
	    COL_JOBURN = 1,
	    COL_JOBNAME = 2,
	    COL_RUNTIMESTAMP = 3,
	    COL_USERID = 4,
	    COL_COMMUNITY = 5,
	    COL_STATUS = 6,
	    COL_COMMENT = 7 ;
	    
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION            = "AGDTCE00160",
	    ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT = "AGDTCE00170",
	    ASTROGRIDERROR_UNABLE_TO_UPDATE_JOB                       = "AGDTCE00190",
	    ASTROGRIDERROR_UNABLE_TO_FIND_JOB_GIVEN_JOBID             = "AGDTCE00192",
	    ASTROGRIDERROR_UNABLE_TO_DELETE_JOB                       = "AGDTCE00194"  ;



	/**
	 * 
     * Creates a new and unique job in the Job database.
     * <p>
     * At present a Job is created (inserted into the Job database) 
     * by passing the request document to this method.
     * All associated objects (e.g. @see org.astrogrid.JobStep) are
     * chained from Job creation, and therefore this step of
     * Job creation needs all the associated information to pass on.
     * 
     * @see org.astrogrid.JobFactory 
     * @param jobDoc - the complete request document
     * @return  - newly created job instance.
     * @exception - org.astrogrid.JobException
	 * 
	 **/             
    public Job create( Document jobDoc ,FactoryProvider facMan) throws JobException  {
    
		if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.createJob(): entry") ;  
		 	
    	JobImpl
    	   job = null ;
		Statement   
		   statement = null ;
    	   
    	try {
			Element 
			   docElement = jobDoc.getDocumentElement(); //JBL Note: not sure this is required.
			   
			// This is critical. The creation of all associated job elements is
			// chained off of the creation of Job. When this returns, all
			// appropriate values have been set, ready for them to be inserted
			// into the database...
			job = new JobImpl( docElement,facMan ) ;
			job.setConfiguration(getConfiguration());
			
			// We need this for occasional fudging...
			// See update()
		    JobImpl.setFactoryImpl( this ) ;
			
			Object []
			   inserts = new Object[8] ;
			inserts[0] = getConfiguration().getProperty( ConfigurationKeys.JOB_TABLENAME, ConfigurationKeys.JOB_CATEGORY ) ;
			inserts[1] = job.getId() ;
			inserts[2] = job.getName() ;
			inserts[3] = new Timestamp( job.getDate().getTime() ).toString(); //JBL Note: this may give us grief
			inserts[4] = job.getUserId() ;
			inserts[5] = job.getCommunity() ;
			inserts[6] = job.getStatus() ;
			inserts[7] = job.getComment() ;

			String
			   updateString = MessageFormat.format( INSERT_TEMPLATE, inserts ) ;
			logger.debug("JobFactoryImpl.create(): updateString: " + updateString);			    			
			statement = job.getConnection().createStatement() ;
			statement.executeUpdate( updateString );
			
			// We unset the dirty flag to prevent unnecessary updates...
			job.setDirty( false ) ;
    	}
    	catch( SQLException sex ) {
    		// JBL Note: This is not good enough. We should also return a separate 
    		// and more specialized DuplicateJobFound exception.
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT
                                              , SUBCOMPONENT_NAME ) ;
			logger.error( message.toString(), sex ) ;
			throw new JobException( message, sex );    		
    	}
    	finally {
    		if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.createJob(): exit") ;   	
    	}
    	
		return job ;
		
    } // end of create()  


	/**
	   * <p>
	   * Updates a job instance in the Job database.
	   * <p>
	   * During the <code>DatasetAgent</code> workflow for
	   * running a query, this is called a number of times on
	   * an immediate-commit basis. For efficiency a prepared
	   * statement is used. 
	   * 
	   * @see org.astrogrid.JobFactory
	   * @param job - the instance whose values are to be written
	   *        back to the database.
	   * @return  void
	   * @exception - org.astrogrid.JobException
	   */	
    public void update( Job job ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.updateJob(): entry") ;  
		  	   
		try {
			
	        PreparedStatement
	           pStatement = ((JobImpl)job.getImplementation()).getPreparedStatement() ;
	           
	        job.setDate( new Date() ) ;
	        
	        pStatement.setTimestamp( 1, new Timestamp( job.getDate().getTime() ) ) ;
			pStatement.setString( 2, job.getStatus() ) ; 
			pStatement.setString( 3, job.getComment() ) ; 
			pStatement.setString( 4, job.getId() ) ;        
	
			pStatement.executeUpdate();
			
			( (JobImpl) job.getImplementation() ).setDirty( false ) ;
		}
		catch( SQLException ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_UPDATE_JOB
                                              , SUBCOMPONENT_NAME
                                              , job.getId() ) ;
			logger.error( message.toString(), ex ) ;  
			throw new JobException( message, ex ) ; 		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.updateJob(): exit") ;   	
		}
    	   	   	
    } // end of update()


	/**
	   * <p>
	   * Finds a job instance in the Job database.
	   * This is the case where a unique job is being
	   * looked for and therefore we only expect to
	   * find the one unique job.
	   * <p>
	   * 
	   * @see org.astrogrid.JobFactory
	   * @param jobURN - the job id.
	   * @return  the job instance.
	   * @exception - org.astrogrid.JobException
	   */	 
    public Job find( String jobURN ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.findJob(): entry") ;  
		 	
		JobImpl
		   job = new JobImpl() ;
		Statement   
		   statement = null ;
		ResultSet
		   rs = null ;
    	   
		try {

			Object []
			   inserts = new Object[2] ;
			inserts[0] = getConfiguration().getProperty( ConfigurationKeys.JOB_TABLENAME, ConfigurationKeys.JOB_CATEGORY ) ;
			inserts[1] = job.getId() ;

			String
			   selectString = MessageFormat.format( SELECT_TEMPLATE, inserts ) ; 			
			statement = job.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			if( !rs.first() ){
				//Job not found...
				AstroGridMessage
					message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_FIND_JOB_GIVEN_JOBID
                                                  , SUBCOMPONENT_NAME
                                                  , jobURN ) ;
				logger.error( message.toString() ) ;   	
				throw new JobException( message ) ;	
			}
			else {
				rs.next() ;
				job.setId( rs.getString( COL_JOBURN ) ) ;
				job.setName( rs.getString( COL_JOBNAME ) ) ;
				job.setDate( rs.getTimestamp( COL_RUNTIMESTAMP ) ) ;
				job.setUserId( rs.getString( COL_USERID ) ) ;
				job.setCommunity( rs.getString( COL_COMMUNITY ) ) ;
				job.setStatus( rs.getString( COL_STATUS ) ) ;
				job.setComment( rs.getString( COL_COMMENT ) ) ;			
				job.setDirty( false ) ;
			}
			if( rs.next() ) {
				// JBL Note: Whoops! More than one job found when only one was expected...
				// New exception required here...
			}
		}
		catch( SQLException ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_FIND_JOB_GIVEN_JOBID
                                              , SUBCOMPONENT_NAME
                                              , jobURN ) ;
			logger.error( message.toString(), ex ) ;   	
			throw new JobException( message, ex ) ;	
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.findJob(): exit") ;   	
		}  	
    	
		return new JobImpl() ; 
		  
    } // end of find()


	/**
	   * <p>
	   * Deletes a job instance from the Job database.
	   * <p>
	   * 
	   * @see org.astrogrid.JobFactory
	   * @param job - the job instance.
	   * @return  the jobURN of the deleted job.
	   * @exception - org.astrogrid.JobException
	   */	     
    public String delete( Job job ) throws JobException { 
		if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.deleteJob(): entry") ;  
		 	
		Statement   
		   statement = null ;
    	   
		try {

			Object []
			   inserts = new Object[2] ;
			inserts[0] = getConfiguration().getProperty( ConfigurationKeys.JOB_TABLENAME, ConfigurationKeys.JOB_CATEGORY ) ;
			inserts[1] = job.getId() ;

			String
			   deleteString = MessageFormat.format( DELETE_TEMPLATE, inserts ) ; 			
			statement = ((JobImpl)job.getImplementation()).getConnection().createStatement() ;
			statement.executeUpdate( deleteString );
			   
		}
		catch( SQLException ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_DELETE_JOB
                                              , SUBCOMPONENT_NAME
                                              , job.getId() ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex ) ;	
		}
		finally {
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.deleteJob(): exit") ;   	
		}  	
    	    	
		return job.getId() ;   
		
    } // end of delete(Job)
    
  
} // end of class JobFactoryImpl
