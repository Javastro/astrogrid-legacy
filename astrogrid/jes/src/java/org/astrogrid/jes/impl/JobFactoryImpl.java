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
package org.astrogrid.jes.impl;

import org.apache.log4j.Logger;
import org.astrogrid.jes.jobcontroller.JobController;
import org.astrogrid.jes.i18n.*;
import org.astrogrid.jes.job.* ;

import org.w3c.dom.* ;

import javax.sql.DataSource ;
import javax.naming.*; 
import java.sql.Connection ;
import java.sql.Statement ;
// import java.sql.PreparedStatement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.sql.Timestamp ;
import java.text.MessageFormat ;
// import java.util.Date ;
import java.util.HashSet ;
import java.util.Iterator ;
import java.lang.Math ;


public class JobFactoryImpl implements JobFactory {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobFactoryImpl.class ) ;
	    
	public static final String
		JOB_DATASOURCE_LOCATION = "JOB.DATASOURCE",
		JOB_TABLENAME = "JOB.TABLENAME",
	    JOBSTEP_TABLENAME = "JOBSTEP.TABLENAME",
	    QUERY_TABLENAME = "QUERY.TABLENAME",
	    CATALOG_TABLENAME = "CATALOG.TABLENAME",
	    TABLE_TABLENAME = "TABLE.TABLENAME",
	    SERVICE_TABLENAME = "SERVICE.TABLENAME",
	    JES_ID = "JES.ID" ;
		
	public static final String
	    JOB_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, JOBNAME, STATUS, SUBMITTIMESTAMP, USERID, COMMUNITY, JOBXML ) " +
	                          "VALUES ( ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}'', ''{7}'' )" ,
	    JOB_UPDATE_TEMPLATE = "UPDATE {0} SET STATUS = {1} WHERE JOBURN = {2}" ,
	    JOB_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = {1}" ,
	    JOB_GENERAL_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE {1}" ,	    
	    JOB_DELETE_TEMPLATE = "DELETE FROM {0} WHERE JOBURN = {1}" ;
	    
	private static final int
	    COL_JOBURN = 1,
	    COL_JOBNAME = 2,
	    COL_STATUS = 3,
	    COL_SUBMITTIMESTAMP = 4,
	    COL_USERID = 5,
	    COL_COMMUNITY = 6,
	    COL_JOBXML = 7 ;
	 
	public static final String
		JOBSTEP_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER, STEPNAME, STATUS, COMMENT ) " +
							  "VALUES ( ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'' )" ,
        JOBSTEP_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = {1}",
        JOBSTEP_UPDATE_TEMPLATE = "UPDATE {0} SET ( STATUS, COMMENT ) = ( {1}, {2} ) WHERE JOBURN = {3} AND STEPNUMBER = {4}"  ; 

	private static final int
		COL_JOBSTEP_JOBURN = 1,
		COL_JOBSTEP_STEPNUMBER = 2,
		COL_JOBSTEP_STEPNAME = 3,
		COL_JOBSTEP_STATUS = 4,
		COL_JOBSTEP_COMMENT = 5 ;  
	
	public static final String
        QUERY_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER ) " +
						  "VALUES ( ''{1}'', ''{2}'' )" ,
	    QUERY_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = {1} AND STEPNUMBER = {2}" ;    

	public static final String
		CATALOG_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER, CATALOGNAME ) " +
						  "VALUES ( ''{1}'', ''{2}'', ''{3}'' )" ,
		CATALOG_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = {1} AND STEPNUMBER = {2}" ;  
		
	private static final int
		COL_CATALOG_JOBURN = 1,
		COL_CATALOG_STEPNUMBER = 2,
		COL_CATALOG_CATALOGNAME = 3 ;    
    
	public static final String
		TABLE_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER, CATALOGNAME, TABLENAME ) " +
						  "VALUES ( ''{1}'', ''{2}'', ''{3}'', ''{4}'' )" , 
        TABLE_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = {1} AND STEPNUMBER = {2} AND CATALOGNAME = {3}" ; 
          
	private static final int
		COL_TABLE_JOBURN = 1,
		COL_TABLE_STEPNUMBER = 2,
		COL_TABLE_CATALOGNAME = 3,
		COL_TABLE_TABLENAME = 4 ;  
						  
	public static final String
		SERVICE_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER, CATALOGNAME, SERVICENAME, SERVICEURL ) " +
						  "VALUES ( ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'' )" ,  						    
        SERVICE_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = {1} AND STEPNUMBER = {2} AND CATALOGNAME = {3}" ;  
    
	private static final int
		COL_SERVICE_JOBURN = 1,
		COL_SERVICE_STEPNUMBER = 2,
		COL_SERVICE_CATALOGNAME = 3,
		COL_SERVICE_SERVICENAME = 4,
        COL_SERVICE_SERVICEURL = 5 ;      ;     
    
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_JOB_DATASOURCE            = "AGJESE00150",
		ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION            = "AGJESE00160",
	    ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT = "AGJESE00170",
	    ASTROGRIDERROR_FAILED_TO_COMMIT                           = "AGJESE00300",
	    ASTROGRIDERROR_FAILED_TO_ROLLBACK                         = "AGJESE00310",
	    ASTROGRIDERROR_FAILED_TO_CLOSE_CONNECTION                 = "AGJESE00320",
        ASTROGRIDERROR_DUPLICATE_JOB_FOUND                        = "AGJESE00700", 
        ASTROGRIDERROR_JOB_NOT_FOUND                              = "AGJESE00710",
        ASTROGRIDERROR_JOBS_NOT_FOUND                             = "AGJESE00720",
        ASTROGRIDERROR_JOBSTEP_NOT_FOUND                          = "AGJESE00730",
        ASTROGRIDERROR_DUPLICATE_QUERY_FOUND                      = "AGJESE00740",
        ASTROGRIDERROR_QUERY_NOT_FOUND                            = "AGJESE00750",
        ASTROGRIDERROR_CATALOG_NOT_FOUND                          = "AGJESE00760",
        ASTROGRIDERROR_TABLE_NOT_FOUND                            = "AGJESE00770",
        ASTROGRIDERROR_SERVICE_NOT_FOUND                          = "AGJESE00780",       
        ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST            = "AGJESE00790", 
        ASTROGRIDERROR_UNABLE_TO_COMPLETE_UPDATE_REQUEST          = "AGJESE00800" ;
	    
	private static DataSource
		datasource = null ;

	//JBL Note: this is a fudge for the moment		
	private static int
	    uniqueID =  ( new Double( Math.random() * Integer.MAX_VALUE ) ).intValue() ;
	    
	private Connection
		connection = null ;

    //JBL Note: this is a fudge for the moment
    private int generateUniqueInt() {
    	
    	int
    	    retValue = 0 ;
    	
    	synchronized ( JobFactoryImpl.class ) {
    		retValue = uniqueID++ ;
    	}
    	
    	return retValue ;
    	
    } // end of generateUniqueInt()


	public static DataSource getDataSource() throws JobException {
		if( TRACE_ENABLED ) logger.debug( "getDataSource(): entry") ; 
	
		String
		   datasourceName = null ;
					
		try{
			
			// Note the double lock strategy			 
			if( datasource == null ){
			   synchronized ( JobFactoryImpl.class ) {
				   if( datasource == null ){
				   	   InitialContext
					      initialContext = new InitialContext() ;
					   datasourceName = JobController.getProperty( JOB_DATASOURCE_LOCATION ) ;
					   datasource = (DataSource) initialContext.lookup( datasourceName ) ;
				   }
			   } // end synchronized
			}
			
		}
		catch( NamingException ne ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_JOB_DATASOURCE
									 , (datasourceName != null ? datasourceName : "") ) ;			                     
			logger.error( message.toString(), ne ) ;
			throw new JobException( message, ne );
		}
		finally{
			if( TRACE_ENABLED ) logger.debug( "getDataSource(): exit") ; 	
		}
		
		return datasource ;	
			
	} // end of getDataSource()	


	public Connection getConnection() throws JobException {
		if( TRACE_ENABLED ) logger.debug( "getConnection(): entry") ; 
		
		try{
			if( connection == null ) {
				connection = getDataSource().getConnection() ;
				connection.setAutoCommit( false ) ;
			}
		}
		catch( SQLException e) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION ) ;
			logger.error( message.toString(), e ) ;
			throw new JobException( message, e );
		}
		finally{
			if( TRACE_ENABLED ) logger.debug( "getConnection(): exit") ; 		
		}
		    
		return connection ;  

	} // end of getConnection()

	
    public Job createJob( Document jobDoc, String jobXML ) throws JobException  {
		if( TRACE_ENABLED ) logger.debug( "createJob(): entry") ;  
		 	
    	JobImpl
    	   job = null ;
		Statement   
		   statement = null ;
    	   
    	try {

			job = new JobImpl( jobDoc, jobXML ) ;
		    job.setFactoryImpl( this ) ;
			job.setId( generateUniqueJobURN( job ) ) ;
			job.setStatus( Job.STATUS_INITIALIZED ) ;
			
			Object []
			   inserts = new Object[8] ;
			inserts[0] = JobController.getProperty( JOB_TABLENAME ) ;
			inserts[1] = job.getId() ;
			inserts[2] = job.getName() ;
			inserts[3] = job.getStatus() ;
			inserts[4] = new Timestamp( job.getDate().getTime() ).toString(); //JBL Note: this may give us grief
			inserts[5] = job.getUserId() ;
			inserts[6] = job.getCommunity() ;
			inserts[7] = job.getDocumentXML() ;

			String
			   updateString = MessageFormat.format( JOB_INSERT_TEMPLATE, inserts ) ; 
			logger.debug( "Create Job: " + updateString ) ;			
			statement = getConnection().createStatement() ;
			statement.executeUpdate( updateString );
			job.setDirty( false ) ;
			createJobSteps( job ) ;
    	}
    	catch( SQLException sex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), sex ) ;
			throw new JobException( message, sex );    		
    	}
    	finally {
    		if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "createJob(): exit") ;   	
    	}
    	
		return job ;
		
    } // end of createJob()  


    public void updateJob( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "updateJob(): entry") ;  
		 
		Statement   
		   statement = null ;
		  	   
		try {
			Object []
			   inserts = new Object[3] ;
			inserts[0] = JobController.getProperty( JOB_TABLENAME ) ;
			inserts[1] = job.getStatus() ;
			inserts[2] = job.getId() ;

			String
			   updateString = MessageFormat.format( JOB_UPDATE_TEMPLATE, inserts ) ; 			
			statement = this.getConnection().createStatement() ;
			statement.executeQuery( updateString );
			updateJobSteps( job ) ;
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_COMPLETE_UPDATE_REQUEST ) ;
			logger.error( message.toString(), ex ) ;   		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "updateJob(): exit") ;   	
		}
    	   	   	
    } // end of updateJob()


    public Job findJob( String jobURN ) throws NotFoundException, DuplicateFoundException, JobException {
		if( TRACE_ENABLED ) logger.debug( "findJob(): entry") ;  
		 	
		JobImpl
		   job = new JobImpl() ;
		Statement   
		   statement = null ;
		ResultSet
		   rs = null ;
    	   
		try {

			Object []
			   inserts = new Object[2] ;
			inserts[0] = JobController.getProperty( JOB_TABLENAME ) ;
			inserts[1] = jobURN ;

			String
			   selectString = MessageFormat.format( JOB_SELECT_TEMPLATE, inserts ) ; 			
			statement = this.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			
			if( !rs.isBeforeFirst() ) {
				// Whoops! Job not found when it should have been...
				Message
				   message = new Message( ASTROGRIDERROR_JOB_NOT_FOUND, jobURN ) ;
				throw new NotFoundException( message ) ;
			}
			else {
				rs.next() ; // position on the first job found (hopefully the only one!)
				
				job.setId( rs.getString( COL_JOBURN ) ) ;
				job.setName( rs.getString( COL_JOBNAME ) ) ;
				job.setDate( rs.getTimestamp( COL_SUBMITTIMESTAMP ) ) ;
				job.setUserId( rs.getString( COL_USERID ) ) ;
				job.setCommunity( rs.getString( COL_COMMUNITY ) ) ;
				job.setDocumentXML( rs.getString( COL_JOBXML ) ) ;
				job.setDirty( false ) ;
				
				findJobSteps( job ) ;
				
				if( rs.next() == true ) {
					// We have a duplicate Job!!! This should be impossible...
					Message
					   message = new Message( ASTROGRIDERROR_DUPLICATE_JOB_FOUND, jobURN ) ;
					throw new DuplicateFoundException( message ) ;
				}

			} // end else
						   
		}
		catch( SQLException ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex ) ;   		
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "findJob(): exit") ;   	
		}  	
    	
		return new JobImpl() ; 
		  
    } // end of findJob()


	public Iterator findJobsWhere( String criteriaString ) throws NotFoundException, JobException  {
		if( TRACE_ENABLED ) logger.debug( "findJobsWhere(): entry") ;  
		 		 	
		HashSet
			set = null  ;
		Statement   
		   statement = null ;
		ResultSet
		   rs = null ;
    	   
		try {

			Object []
			   inserts = new Object[2] ;
			inserts[0] = JobController.getProperty( JOB_TABLENAME ) ;
			inserts[1] = criteriaString ;

			String
			   selectString = MessageFormat.format( JOB_GENERAL_SELECT_TEMPLATE, inserts ) ; 			
			statement = this.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			
			if( !rs.isBeforeFirst() ) {
				// Whoops! No Jobs found when perhaps there should have been...
				Message
				   message = new Message( ASTROGRIDERROR_JOBS_NOT_FOUND, criteriaString  ) ; 
				throw new NotFoundException( message ) ;
			}
			else {
				
				set = new HashSet() ;
				
				while( rs.next() ) {
					
					JobImpl
					   job = new JobImpl() ;
				
				    job.setId( rs.getString( COL_JOBURN ) ) ;
				    job.setName( rs.getString( COL_JOBNAME ) ) ;
				    job.setDate( rs.getTimestamp( COL_SUBMITTIMESTAMP ) ) ;
				    job.setUserId( rs.getString( COL_USERID ) ) ;
				    job.setCommunity( rs.getString( COL_COMMUNITY ) ) ;
				    job.setDocumentXML( rs.getString( COL_JOBXML ) ) ;
				    job.setDirty( false ) ;
				    
					findJobSteps( job ) ;
					
				    set.add( job ) ;
				    
				} // end while
				
			} // end else
						   
		}
		catch( SQLException ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST ) ;
			logger.error( message.toString(), ex ) ; 
			throw new JobException( message, ex ) ;  		
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "findJobsWhere(): exit") ;   	
		}  		
		
		return set.iterator() ;

	} // end of findJobsWhere()


    public String deleteJob( Job job )  { 
		if( TRACE_ENABLED ) logger.debug( "deleteJob(): entry") ;  
		 	
		Statement   
		   statement = null ;
    	   
		try {

			Object []
			   inserts = new Object[2] ;
			inserts[0] = JobController.getProperty( JOB_TABLENAME ) ;
			inserts[1] = job.getId() ;

			String
			   deleteString = MessageFormat.format( JOB_DELETE_TEMPLATE, inserts ) ; 			
			statement = getConnection().createStatement() ;
			statement.executeUpdate( deleteString );
			   
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), ex ) ;		
		}
		finally {
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "deleteJob(): exit") ;   	
		}  	
    	    	
		return job.getId() ;   
		
    } // end of deleteJob(Job)
    
    
	public void begin() {
		; // We adopt the lazey initialization pattern. Do nothing!
	}
	
	
	public boolean end( boolean bCommit ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "end(): enter") ;   
		
		boolean retVal = false ;
		
		try {		
			if( connection != null ) {
				
				if(	bCommit )
				    connection.commit() ;
				else
				    connection.rollback()  ;  
				
			} // end if
			retVal = true ;
		}
		catch( SQLException sex ) { 
			
			Message
				message ;
			if( bCommit ) {
				message = new Message( ASTROGRIDERROR_FAILED_TO_COMMIT, sex ) ;
			}
			else{
				message = new Message( ASTROGRIDERROR_FAILED_TO_ROLLBACK, sex ) ;
			}
			logger.error( message.toString() ) ; 
			throw new JobException( message ) ;	
		}
		finally {
			
			try { 
				if( connection != null ) connection.close() ; 
			} 
			catch( SQLException e ) { 
				logger.error( new Message( ASTROGRIDERROR_FAILED_TO_CLOSE_CONNECTION ) ) ; 
			}
			connection = null ;
			if( TRACE_ENABLED ) logger.debug( "end(): exit") ;   	
		}
		
		return retVal ;
 
	} // end of end()
	
	
	private void createJobSteps( Job job ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "createJobSteps(): exit") ;   
					
		try {
			
			int
			   count = 0 ;
			Iterator
			   iterator = job.getJobSteps() ;
			JobStep
			   jobStep = null ;
		   
			while ( iterator.hasNext() ) {
				jobStep.setStepNumber( ++count ) ;
			    insertOneJobStep( (JobStep)iterator.next() ) ;			
			} 

		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "createJobSteps(): exit") ;   	
		}
			
	} // end of createJobSteps(Job)
  
  
	private void insertOneJobStep ( JobStep jobStep ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "insertOneJobStep(): entry") ;   
			
		Statement   
		   statement = null ;
					
		try {

			Object []
			   inserts = new Object[6] ;
			inserts[0] = JobController.getProperty( JOBSTEP_TABLENAME ) ;
			inserts[1] = jobStep.getParent().getId() ;            // foreign key to parent
			inserts[2] = jobStep.getStepNumber() ;                // unique for step within job
			inserts[3] = jobStep.getName() ;
			inserts[4] = jobStep.getStatus() ;            
			inserts[5] = jobStep.getComment() ;

			String
			   updateString = MessageFormat.format( JOBSTEP_INSERT_TEMPLATE, inserts ) ; 
			logger.debug( "Create JobStep: " + updateString ) ;			
			statement = getConnection().createStatement() ;
			statement.executeUpdate( updateString );
			createQuery( jobStep.getQuery() ) ;
		}
		catch( SQLException sex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), sex ) ;
			throw new JobException( message, sex );    		
		}
		finally {
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "insertOneJobStep(): exit") ;   	
		}
				
	} // end of insertOneJobStep()
	
	
	public void findJobSteps( Job job ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "findJobSteps(): entry") ;  
		 	
		JobStep
		   jobStep = null ;
		Statement   
		   statement = null ;
		ResultSet
		   rs = null ;
    	   
		try {

			Object []
			   inserts = new Object[2] ;
			inserts[0] = JobController.getProperty( JOBSTEP_TABLENAME ) ;
			inserts[1] = job.getId() ;

			String
			   selectString = MessageFormat.format( JOBSTEP_SELECT_TEMPLATE, inserts ) ; 	
			logger.debug( "Find jobsteps: " + selectString ) ;		
			statement = this.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			
			if( !rs.isBeforeFirst() ) {
				// Whoops! JobStep not found when it should have been...
				Message
				   message = new Message( ASTROGRIDERROR_JOBSTEP_NOT_FOUND, job.getId() ) ;
				throw new NotFoundException( message ) ;
			}
			else {
				
				while( rs.next() ) {
					
					jobStep = new JobStep( job ) ; 
					
					jobStep.setStepNumber( new Integer( rs.getString( COL_JOBSTEP_STEPNUMBER ) ) ) ;
					jobStep.setName( rs.getString( COL_JOBSTEP_STEPNAME ) ) ;
					jobStep.setStatus( rs.getString( COL_JOBSTEP_STATUS ) ) ;
					jobStep.setComment( rs.getString( COL_JOBSTEP_COMMENT ) ) ;
					
					findQuery( jobStep ) ;
					
				} // end while 

			} // end else
						   
		}
		catch( SQLException ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex ) ;   		
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "findJobSteps(): exit") ;   	
		}  	
		  
	} // end of findJobSteps()
	
	
	
	private void updateJobSteps( Job job ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "updateJobSteps(): exit") ;   
					
		try {
			
			int
			   count = 0 ;
			Iterator
			   iterator = job.getJobSteps() ;
			JobStep
			   jobStep = null ;
		   
			while ( iterator.hasNext() ) {
				updateOneJobStep( (JobStep)iterator.next() ) ;			
			} 

		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "updateJobSteps(): exit") ;   	
		}
			
	} // end of updateJobSteps(Job)
  
  
	private void updateOneJobStep ( JobStep jobStep ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "updateOneJobStep(): entry") ;   
			
		Statement   
		   statement = null ;
					
		try {
			
			// "UPDATE {0} SET ( STATUS, COMMENT ) = ( {1}, {2} ) WHERE JOBURN = {3} AND STEPNUMBER = {4}" 

			Object []
			   inserts = new Object[5] ;
			inserts[0] = JobController.getProperty( JOBSTEP_TABLENAME ) ;
			inserts[1] = jobStep.getStatus() ;
			inserts[2] = jobStep.getComment() ;
			inserts[3] = jobStep.getParent().getId() ;
			inserts[4] = jobStep.getStepNumber().toString() ;      

			String
			   updateString = MessageFormat.format( JOBSTEP_UPDATE_TEMPLATE, inserts ) ; 
			logger.debug( "Update JobStep: " + updateString ) ;			
			statement = getConnection().createStatement() ;
			statement.executeUpdate( updateString );
			createQuery( jobStep.getQuery() ) ;
		}
		catch( SQLException sex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_COMPLETE_UPDATE_REQUEST ) ;
			logger.error( message.toString(), sex ) ;
			throw new JobException( message, sex );    		
		}
		finally {
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "updateOneJobStep(): exit") ;   	
		}
				
	} // end of updateOneJobStep()
	
	 
	public void createQuery( Query query ) throws JobException  {
		 if( TRACE_ENABLED ) logger.debug( "createQuery(): entry") ;  
		 	
		 Statement    
			statement = null ;
    	   
		 try {
			
			 Object []
				inserts = new Object[3] ;
			 inserts[0] = JobController.getProperty( QUERY_TABLENAME ) ;
			 inserts[1] = query.getParent().getParent().getId() ;
			 inserts[2] = query.getParent().getStepNumber() ;

			 String
				updateString = MessageFormat.format( QUERY_INSERT_TEMPLATE, inserts ) ; 
		     logger.debug( "Create Query: " + updateString ) ;			
			 statement = getConnection().createStatement() ;
			 statement.executeUpdate( updateString );
			 createCatalogs( query ) ;

		 }
		 catch( SQLException sex ) {
			 Message
				 message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			 logger.error( message.toString(), sex ) ;
			 throw new JobException( message, sex );    		
		 }
		 finally {
			 if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			 if( TRACE_ENABLED ) logger.debug( "createQuery(): exit") ;   	
		 }
		
	 } // end of createQuery() 


	public void findQuery( JobStep jobStep ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "findQuery(): entry") ;  
		 	
		Query
		   query = null ;
		Statement   
		   statement = null ;
		ResultSet
		   rs = null ;
    	   
		try {

			Object []
			   inserts = new Object[2] ;
			inserts[0] = JobController.getProperty( QUERY_TABLENAME ) ;
			inserts[1] = jobStep.getParent().getId() ;
			inserts[2] = jobStep.getStepNumber() ;

			String
			   selectString = MessageFormat.format( QUERY_SELECT_TEMPLATE, inserts ) ; 	
			logger.debug( "findQuery: " + selectString ) ;		
			statement = this.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			
			if( !rs.isBeforeFirst() ) {
				// Whoops! Query not found when it should have been...
				// AGJESE00750=:JobFactoryImpl: Query not found for Job with URN [{0}] and JobStep number [{1}]
				Message
				   message = new Message( ASTROGRIDERROR_QUERY_NOT_FOUND
				                        , jobStep.getParent().getId()
				                        , jobStep.getStepNumber() ) ;
				throw new NotFoundException( message ) ;
			}
			else {
				rs.next() ; // position on the first query found (hopefully the only one!)
	
	            // There is little stored in Query AT THE MOMENT.
	            // (In fact nothing but foreign keys)
	            // We assume here that everything is OK... (no database corruption)
				query = new Query( jobStep ) ;
				
//				findCatalogs( query ) ;
				
				if( rs.next() == true ) {
					// We have a duplicate Query!!! This should be impossible...
					Message
					   message = new Message( ASTROGRIDERROR_DUPLICATE_QUERY_FOUND
											, jobStep.getParent().getId()
                                            , jobStep.getStepNumber() ) ;
					throw new DuplicateFoundException( message ) ;
				}

			} // end else
						   
		}
		catch( SQLException ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex ) ;   		
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "findQuery(): exit") ;   	
		}  	
		  
	} // end of findQuery()
	
	
	private void createCatalogs( Query query ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "createJobSteps(): exit") ;   
					
		try {
			
			int
			   count = 0 ;
			Iterator
			   iterator = query.getCatalogs() ;
		   
			while ( iterator.hasNext() ) {
				insertOneCatalog( (Catalog)iterator.next() ) ;			
			} 

		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "createJobSteps(): exit") ;   	
		}
			
	} // end of createCatalogs(Job)


	private void insertOneCatalog ( Catalog catalog ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "insertOneCatalog(): exit") ;   
			
		Statement   
		   statement = null ;
					
		try {

			Object []
			   inserts = new Object[4] ;
			inserts[0] = JobController.getProperty( CATALOG_TABLENAME ) ;
			inserts[1] = catalog.getParent().getParent().getParent().getId() ;            
			inserts[2] = catalog.getParent().getParent().getStepNumber() ;
			inserts[3] = catalog.getName() ;


			String
			   updateString = MessageFormat.format( CATALOG_INSERT_TEMPLATE, inserts ) ; 	
			logger.debug( "Create Catalog: " + updateString ) ;		
			statement = getConnection().createStatement() ;
			statement.executeUpdate( updateString );
			createTables( catalog ) ;
			createServices( catalog ) ;
		}
		catch( SQLException sex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), sex ) ;
			throw new JobException( message, sex );    		
		}
		finally {
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "insertOneCatalog(): exit") ;   	
		}
				
	} // end of insertOneCatalog()
	
	
	public void findCatalogs( Query query ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "findCatalogs(): entry") ;  
		 	
		Catalog
		   catalog = null ;
		Statement   
		   statement = null ;
		ResultSet
		   rs = null ;
    	   
		try {

			Object []
			   inserts = new Object[3] ;
			inserts[0] = JobController.getProperty( CATALOG_TABLENAME ) ;
			inserts[1] = query.getParent().getParent().getId() ;
			inserts[2] = query.getParent().getStepNumber() ;

			String
			   selectString = MessageFormat.format( CATALOG_SELECT_TEMPLATE, inserts ) ; 	
			logger.debug( "Find catalogs: " + selectString ) ;		
			statement = this.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			
			if( !rs.isBeforeFirst() ) {
				// Whoops! Catalog not found when it should have been...
				Message
				   message = new Message( ASTROGRIDERROR_CATALOG_NOT_FOUND
				                        , query.getParent().getParent().getId()
				                        , query.getParent().getStepNumber() ) ;
				throw new NotFoundException( message ) ;
			}
			else {
				
				while( rs.next() ) {
					
					catalog = new Catalog( query ) ; 					
					catalog.setName( rs.getString( COL_CATALOG_CATALOGNAME ) ) ;
					findTables( catalog ) ;
					findServices( catalog ) ;
					
				} // end while 

			} // end else
						   
		}
		catch( SQLException ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex ) ;   		
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "findCatalogs(): exit") ;   	
		}  	
		  
	} // end of findCatalogs()
	
	
	private void createTables( Catalog catalog ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "createTables(): exit") ;   
					
		try {
			
			Iterator
			   iterator = catalog.getTables() ;
		   
			while ( iterator.hasNext() ) {
				insertOneTable( (Table)iterator.next() ) ;			
			} 

		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "createTables(): exit") ;   	
		}
			
	} // end of createTables(Job) 
	
	
	private void createServices( Catalog catalog ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "createServices(): exit") ;   
					
		try {
			
			Iterator
			   iterator = catalog.getServices() ;
		   
			while ( iterator.hasNext() ) {
				insertOneService( (Service)iterator.next() ) ;			
			} 

		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "createServices(): exit") ;   	
		}
			
	} // end of createTables(Job) 
	
	
	private void insertOneTable( Table table ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "insertOneTable(): exit") ;   
			
		Statement   
		   statement = null ;
					
		try {

			Object []
			   inserts = new Object[5] ;
			inserts[0] = JobController.getProperty( TABLE_TABLENAME ) ;
			inserts[1] = table.getParent().getParent().getParent().getParent().getId() ;  // JobURN
			inserts[2] = table.getParent().getParent().getParent().getStepNumber() ;      // step number
			inserts[3] = table.getParent().getName() ;                                    // catalog name
			inserts[4] = table.getName() ;                                                // table name
   
			String
			   updateString = MessageFormat.format( TABLE_INSERT_TEMPLATE, inserts ) ; 
			logger.debug( "Create Table: " + updateString ) ;			
			statement = getConnection().createStatement() ;
			statement.executeUpdate( updateString );
		}
		catch( SQLException sex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), sex ) ;
			throw new JobException( message, sex );    		
		}
		finally {
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "insertOneTable(): exit") ;   	
		}
				
	} // end of insertOneTable()
	

	private void insertOneService( Service service ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "insertOneService(): exit") ;   
			
		Statement   
		   statement = null ;
					
		try {

			Object []
			   inserts = new Object[6] ;
			inserts[0] = JobController.getProperty( SERVICE_TABLENAME ) ;
			inserts[1] = service.getParent().getParent().getParent().getParent().getId() ; // JobURN  
			inserts[2] = service.getParent().getParent().getParent().getStepNumber() ;     // step number
			inserts[3] = service.getParent().getName() ;                                   // catalog name
			inserts[4] = service.getName() ;                                               // service name
			inserts[5] = service.getUrl() ;                                                // service url
  		 
			String
			   updateString = MessageFormat.format( SERVICE_INSERT_TEMPLATE, inserts ) ; 
			logger.debug( "Create Service: " + updateString ) ;			
			statement = getConnection().createStatement() ;
			statement.executeUpdate( updateString );
		}
		catch( SQLException sex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), sex ) ;
			throw new JobException( message, sex );    		
		}
		finally {
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "insertOneService(): exit") ;   	
		}
				
	} // end of insertOneService()
	
		
	 
	private String generateUniqueJobURN( Job job )  {
		
	    StringBuffer
	        buffer = new StringBuffer( 64 ) ;
	        
	    buffer
	       .append( job.getUserId() )
	       .append( ':' )
	       .append( job.getCommunity() )
	       .append( ':' )
	       .append( JobController.getProperty( JES_ID ) )
	       .append( ':' )
	       .append( generateUniqueInt() ) ;
	       
		return buffer.toString() ;
		
	} // end of generateUniqueJobURN()
	
	
	public void findTables( Catalog catalog ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "findTables(): entry") ;  
		 	
		Table
		   table = null ;
		Statement   
		   statement = null ;
		ResultSet
		   rs = null ;
    	   
		try {

			Object []
			   inserts = new Object[4] ;
			inserts[0] = JobController.getProperty( TABLE_TABLENAME ) ;
			inserts[1] = catalog.getParent().getParent().getParent().getId() ;
			inserts[2] = catalog.getParent().getParent().getStepNumber() ;
			inserts[3] = catalog.getName() ;			

			String
			   selectString = MessageFormat.format( TABLE_SELECT_TEMPLATE, inserts ) ; 	
			logger.debug( "Find tables: " + selectString ) ;		
			statement = this.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			
			if( !rs.isBeforeFirst() ) {
				// Whoops! Table not found when it should have been...
				Message
				   message = new Message( ASTROGRIDERROR_TABLE_NOT_FOUND
				                        , catalog.getName()
										, catalog.getParent().getParent().getParent().getId()
										, catalog.getParent().getParent().getStepNumber() ) ;
				throw new NotFoundException( message ) ;
			}
			else {
				
				while( rs.next() ) {
					
					table = new Table( catalog ) ; 					
					table.setName( rs.getString( COL_TABLE_TABLENAME ) ) ;
					
				} // end while 

			} // end else
						   
		}
		catch( SQLException ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex ) ;   		
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "findTables(): exit") ;   	
		}  	
		  
	} // end of findTables()
  
  
	public void findServices( Catalog catalog ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "findServices(): entry") ;  
		 	
		Service
		   service = null ;
		Statement   
		   statement = null ;
		ResultSet
		   rs = null ;
    	   
		try {

			Object []
			   inserts = new Object[4] ;
			inserts[0] = JobController.getProperty( SERVICE_TABLENAME ) ;
			inserts[1] = catalog.getParent().getParent().getParent().getId() ;
			inserts[2] = catalog.getParent().getParent().getStepNumber() ;
			inserts[3] = catalog.getName() ;			

			String
			   selectString = MessageFormat.format( SERVICE_SELECT_TEMPLATE, inserts ) ; 	
			logger.debug( "Find services: " + selectString ) ;		
			statement = this.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			
			if( !rs.isBeforeFirst() ) {
				// Whoops! Service not found when it should have been...
				Message
				   message = new Message( ASTROGRIDERROR_SERVICE_NOT_FOUND
										, catalog.getName()
										, catalog.getParent().getParent().getParent().getId()
										, catalog.getParent().getParent().getStepNumber() ) ;
				throw new NotFoundException( message ) ;
			}
			else {
				
				while( rs.next() ) {
					
				    service = new Service( catalog ) ; 					
					service.setName( rs.getString( COL_SERVICE_SERVICENAME ) ) ;
					service.setUrl( rs.getString( COL_SERVICE_SERVICEURL ) ) ;
					
				} // end while 

			} // end else
						   
		}
		catch( SQLException ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex ) ;   		
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "findServices(): exit") ;   	
		}  	
		  
	} // end of findServices()
  
  
} // end of class JobFactoryImpl
