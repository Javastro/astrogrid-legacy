
package org.astrogrid.datacenter.impl ;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.DatasetAgent ;
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
import java.sql.PreparedStatement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.sql.Timestamp ;
import java.text.MessageFormat ;
import java.util.Date ;


public class JobFactoryImpl implements JobFactory {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobFactoryImpl.class ) ;
	    
	public static final String
		JOB_DATASOURCE_LOCATION = "JOB.DATASOURCE",
		JOB_TABLENAME = "JOB.TABLENAME" ;
		
	private static DataSource
		datasource = null ;
	    
	public static final String
	    INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, JOBNAME, RUNTIMESTAMP, USERID, COMMUNITY, STATUS, COMMENT ) " +
	                      "VALUES ( '{1}', '{2}', '{3}', '{4}', '{5}', '{6}', '{7}' )" ,
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
		ASTROGRIDERROR_COULD_NOT_CREATE_JOB_DATASOURCE            = "AGDTCE00150",
		ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION            = "AGDTCE00160",
	    ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT = "AGDTCE00170"  ;
	    

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
					   datasourceName = DatasetAgent.getProperty( JOB_DATASOURCE_LOCATION ) ;
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

	
    public Job create( Document jobDoc ) throws JobException  {
		if( TRACE_ENABLED ) logger.debug( "createJob(): entry") ;  
		 	
    	JobImpl
    	   job = null ;
		Statement   
		   statement = null ;
    	   
    	try {
			Element 
			   docElement = jobDoc.getDocumentElement();
			job = new JobImpl( docElement ) ;
		    job.setFactoryImpl( this ) ;
			
			Object []
			   inserts = new Object[8] ;
			inserts[0] = DatasetAgent.getProperty( JOB_TABLENAME ) ;
			inserts[1] = job.getId() ;
			inserts[2] = job.getName() ;
			inserts[3] = new Timestamp( job.getDate().getTime() ).toString(); //JBL Note: this may give us grief
			inserts[4] = job.getUserId() ;
			inserts[5] = job.getCommunity() ;
			inserts[6] = job.getStatus() ;
			inserts[7] = job.getComment() ;

			String
			   updateString = MessageFormat.format( INSERT_TEMPLATE, inserts ) ; 			
			statement = job.getConnection().createStatement() ;
			statement.executeUpdate( updateString );
			job.setDirty( false ) ;
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
		
    } // end of create()  


    public void update( Job job ) {
		if( TRACE_ENABLED ) logger.debug( "updateJob(): entry") ;  
		  	   
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
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), ex ) ;   		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "updateJob(): exit") ;   	
		}
    	   	   	
    } // end of update()


    public Job find( String jobURN ) {
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
		  
    } // end of find()


    public String delete( Job job )  { 
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
		
    } // end of delete(Job)
    
  
} // end of class JobFactoryImpl
