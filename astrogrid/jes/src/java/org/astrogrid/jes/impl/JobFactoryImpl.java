package org.astrogrid.jes.impl;

import org.apache.log4j.Logger;
import org.astrogrid.jes.jobcontroller.JobController;
import org.astrogrid.jes.i18n.*;
import org.astrogrid.jes.job.JobFactory ;
import org.astrogrid.jes.job.Job ;
import org.astrogrid.jes.job.JobStep ;
import org.astrogrid.jes.job.JobDocDescriptor ;
import org.astrogrid.jes.job.JobException ;

import org.w3c.dom.* ;

import javax.sql.DataSource ;
import javax.naming.*; 
import java.sql.Connection ;
import java.sql.Statement ;
import java.sql.PreparedStatement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.sql.Timestamp ;
import java.text.MessageFormat ;
import java.util.Date ;
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
	    FROM_TABLENAME = "FROM.TABLENAME",
	    CATALOG_TABLENAME = "CATALOG.TABLENAME",
	    TABLE_TABLENAME = "TABLE.TABLENAME",
	    SERVICE_TABLENAME = "SERVICE.TABLENAME",
	    JES_ID = "JES.ID" ;
		
	public static final String
	    JOB_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, JOBNAME, RUNTIMESTAMP, USERID, COMMUNITY ) " +
	                          "VALUES ( '{1}', '{2}', '{3}', '{4}', '{5}', '{6}', '{7}' )" ,
	    JOB_UPDATE_TEMPLATE = "UPDATE {0} SET RUNTIMESTAMP = ?, STATUS = ?, COMMENT = ? WHERE JOBURN = ?" ,
	    JOB_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = {1}" ,
	    JOB_DELETE_TEMPLATE = "DELETE FROM {0} WHERE JOBURN = {1}" ;
	    
	private static final int
	    COL_JOBURN = 1,
	    COL_JOBNAME = 2,
	    COL_RUNTIMESTAMP = 3,
	    COL_USERID = 4,
	    COL_COMMUNITY = 5,
	    COL_STATUS = 6,
	    COL_COMMENT = 7 ;
	    
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_JOB_DATASOURCE            = "AGDTCE00150",
		ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION            = "AGDTCE00160",
	    ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT = "AGDTCE00170"  ;
	    
	private static DataSource
		datasource = null ;

	//JBL Note: this is a fudge for the moment		
	private static int
	    uniqueID =  new Double( Math.random() ).intValue()  ;
	    	    
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
    	
    }


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

	
    public Job createJob( Document jobDoc ) throws JobException  {
		if( TRACE_ENABLED ) logger.debug( "createJob(): entry") ;  
		 	
    	JobImpl
    	   job = null ;
		Statement   
		   statement = null ;
    	   
    	try {

			job = new JobImpl( jobDoc ) ;
		    job.setFactoryImpl( this ) ;
			job.setId( generateUniqueJobURN( job ) ) ;
			
			Object []
			   inserts = new Object[6] ;
			inserts[0] = JobController.getProperty( JOB_TABLENAME ) ;
			inserts[1] = job.getId() ;
			inserts[2] = job.getName() ;
			inserts[3] = new Timestamp( job.getDate().getTime() ).toString(); //JBL Note: this may give us grief
			inserts[4] = job.getUserId() ;
			inserts[5] = job.getCommunity() ;

			String
			   updateString = MessageFormat.format( JOB_INSERT_TEMPLATE, inserts ) ; 			
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
		  	   
		try {
			
	        PreparedStatement
	           pStatement = ((JobImpl)job.getImplementation()).getPreparedStatement() ;
	           
	        job.setDate( new Date() ) ;
	        pStatement.setTimestamp( 1, new Timestamp( job.getDate().getTime() ) ) ;        
			pStatement.executeUpdate();
			
			( (JobImpl) job.getImplementation() ).setDirty( false ) ;
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), ex ) ;   		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "updateJob(): exit") ;   	
		}
    	   	   	
    } // end of updateJob()


    public Job findJob( String jobURN ) {
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
			inserts[0] = DatasetAgent.getProperty( JOB_TABLENAME ) ;
			inserts[1] = job.getId() ;

			String
			   selectString = MessageFormat.format( SELECT_TEMPLATE, inserts ) ; 			
			statement = job.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			if( rs.first() ){
				; //JBL Note: Job not found
			}
			else {
				job.setId( rs.getString( COL_JOBURN ) ) ;
				job.setName( rs.getString( COL_JOBNAME ) ) ;
				job.setDate( rs.getTimestamp( COL_RUNTIMESTAMP ) ) ;
				job.setUserId( rs.getString( COL_USERID ) ) ;
				job.setCommunity( rs.getString( COL_COMMUNITY ) ) ;
				job.setStatus( rs.getString( COL_STATUS ) ) ;
				job.setComment( rs.getString( COL_COMMENT ) ) ;
				
				job.setDirty( false ) ;
			}
			   
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), ex ) ;   		
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "findJob(): exit") ;   	
		}  	
    	
		return new JobImpl() ; 
		  
    } // end of findJob()





	public Iterator findJobsWhere( String queryString ) throws JobException  {
		

	}


    public String deleteJob( Job job )  { 
		if( TRACE_ENABLED ) logger.debug( "deleteJob(): entry") ;  
		 	
		Statement   
		   statement = null ;
    	   
		try {

			Object []
			   inserts = new Object[2] ;
			inserts[0] = DatasetAgent.getProperty( JOB_TABLENAME ) ;
			inserts[1] = job.getId() ;

			String
			   deleteString = MessageFormat.format( DELETE_TEMPLATE, inserts ) ; 			
			statement = ((JobImpl)job.getImplementation()).getConnection().createStatement() ;
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
	
	
	public void end( boolean bCommit ) {
	 
	}
	
	
	private void createJobSteps( Job job ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "createJobSteps(): exit") ;   
					
		try {
			
			int
			   count = 0 ;
			Iterator
			   iterator = job.getJobSteps() ;
		   
			while ( iterator.hasNext() ) {
				count++ ;
			    insertOneJobStep( (JobStep)iterator.next(), count ) ;			
			} 

		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "createJobSteps(): exit") ;   	
		}
			
	} // end of createJobSteps(Job)
  
  
	private void insertOneJobStep ( JobStep jobStep, int stepId ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "insertOneJobStep(): exit") ;   
			
		Statement   
		   statement = null ;
					
		try {

			Object []
			   inserts = new Object[6] ;
			inserts[0] = JobController.getProperty( JOBSTEP_TABLENAME ) ;
			inserts[1] = new Integer( stepId ) ;  // unique for step within job
			inserts[2] = jobStep.getName() ;
			inserts[3] = jobStep.getParent().getId() ;            // foreign key to parent

			String
			   updateString = MessageFormat.format( JOB_INSERT_TEMPLATE, inserts ) ; 			
			statement = getConnection().createStatement() ;
			statement.executeUpdate( updateString );
			createQuery( jobStep ) ;
		}
		catch( SQLException sex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), sex ) ;
			throw new JobException( message, sex );    		
		}
		finally {
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "createJobSteps(): exit") ;   	
		}
				
	} // end of insertOneJobStep()
	
  
	public void createQuery( JobStep jobStep ) throws JobException  {
		 if( TRACE_ENABLED ) logger.debug( "createQuery(): entry") ;  
		 	
		 Statement   
			statement = null ;
    	   
		 try {
			
			 Object []
				inserts = new Object[6] ;
			 inserts[0] = JobController.getProperty( JOB_TABLENAME ) ;
			 inserts[1] = job.getId() ;
			 inserts[2] = job.getName() ;
			 inserts[3] = new Timestamp( job.getDate().getTime() ).toString(); //JBL Note: this may give us grief
			 inserts[4] = job.getUserId() ;
			 inserts[5] = job.getCommunity() ;

			 String
				updateString = MessageFormat.format( JOB_INSERT_TEMPLATE, inserts ) ; 			
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
			 if( TRACE_ENABLED ) logger.debug( "createQuery(): exit") ;   	
		 }
		
	 } // end of createJob()  
	 
	 
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
  
  
} // end of class JobFactoryImpl
