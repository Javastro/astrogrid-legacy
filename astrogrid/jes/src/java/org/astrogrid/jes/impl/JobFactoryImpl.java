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

import org.astrogrid.jes.jobcontroller.SubmissionRequestDD ;

import org.apache.log4j.Logger; 
import org.astrogrid.i18n.*;
import org.astrogrid.jes.job.* ;
import org.astrogrid.jes.JES ;
import org.astrogrid.Configurator ;

import org.w3c.dom.* ;

import javax.sql.DataSource ;
import javax.naming.*; 
import java.sql.Connection ;
import java.sql.Statement ;
import java.sql.PreparedStatement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.sql.Timestamp ;
import java.util.Date ;
import java.text.*;
import java.text.MessageFormat ;
import java.util.ArrayList;
import java.util.HashSet ;
import java.util.Iterator ;
import java.util.ListIterator ;

import org.astrogrid.community.common.util.CommunityMessage;
import org.astrogrid.community.delegate.policy.PolicyServiceDelegate;
import org.astrogrid.community.policy.data.PolicyPermission;;


public class JobFactoryImpl implements JobFactory {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobFactoryImpl.class ) ;
        
    private final static String 
        SUBCOMPONENT_NAME = Configurator.getClassName( JobFactoryImpl.class );        
	    
	public static final String
		JOB_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, JOBNAME, STATUS, SUBMITTIMESTAMP, USERID, COMMUNITY, COGROUP, COTOKEN, JOBXML, DESCRIPTION ) " +
												"VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )" ,	                          
	    JOB_UPDATE_TEMPLATE = "UPDATE {0} SET STATUS = ''{1}'' WHERE JOBURN = ''{2}''" ,
	    JOB_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = ''{1}''" ,
	    JOB_GENERAL_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE {1}" ,	
        JOB_SELECT_USERSJOBS_TEMPLATE = "SELECT * FROM {0} WHERE USERID = ''{1}'' AND COMMUNITY = ''{2}'' ORDER BY SUBMITTIMESTAMP",    
	    JOB_DELETE_TEMPLATE = "DELETE FROM {0} WHERE JOBURN = ''{1}''" ;
	    
	private static final int
	    COL_JOBURN = 1,
	    COL_JOBNAME = 2,
	    COL_STATUS = 3,
	    COL_SUBMITTIMESTAMP = 4,
	    COL_USERID = 5,
	    COL_COCOMMUNITY = 6,
        COL_COGROUP = 7,
        COL_TOKEN = 8,
	    COL_JOBXML = 9,
        COL_DESCRIPTION = 10 ;
	 
	public static final String
        //JBL altered for iteration 3 - added SEQUENCENUMBER, JOINCONDITION and ORDER BY
		JOBSTEP_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER, STEPNAME, STATUS, COMMENT, SEQUENCENUMBER, JOINCONDITION ) " +
							  "VALUES ( ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}'', ''{7}'' )" ,
        JOBSTEP_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = ''{1}'' ORDER BY STEPNUMBER ASC, SEQUENCENUMBER ASC",
        JOBSTEP_UPDATE_TEMPLATE = "UPDATE {0} SET STATUS =''{1}'', COMMENT = ''{2}''  WHERE JOBURN = ''{3}'' AND STEPNUMBER = ''{4}''"  ; 

	private static final int
		COL_JOBSTEP_JOBURN = 1,
		COL_JOBSTEP_STEPNUMBER = 2,
		COL_JOBSTEP_STEPNAME = 3,
		COL_JOBSTEP_STATUS = 4,
		COL_JOBSTEP_COMMENT = 5,  
        COL_JOBSTEP_SEQUENCENUMBER = 6, //JBL added iteration 3
        COL_JOBSTEP_JOINCONDITION = 7 ; //JBL added iteration 3
	
    public static final String
       TOOL_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER, TOOLNAME ) " +
                              "VALUES ( ''{1}'', ''{2}'', ''{3}'' )" ,
       TOOL_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = ''{1}'' AND STEPNUMBER = ''{2}''" ;    
  
    private static final int
        COL_TOOL_JOBURN = 1,
        COL_TOOL_STEPNUMBER = 2,
        COL_TOOL_TOOLNAME = 3,
        COL_TOOL_LOCATION = 4 ; 
     
    public static final String
       PARAMETER_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER, TOOLNAME, PARAMNAME, DIRECTION, TYPE, LOCATION, CONTENTS  ) " +
                                   "VALUES ( ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}'', ''{7}'', ''{8}'' )" ,
       PARAMETER_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = ''{1}'' AND STEPNUMBER = ''{2}'' AND TOOLNAME = ''{3}''" ;    
   
    private static final int
        COL_PARAMETER_JOBURN = 1,
        COL_PARAMETER_STEPNUMBER = 2,
        COL_PARAMETER_TOOLNAME = 3,
        COL_PARAMETER_PARAMNAME = 4,
        COL_PARAMETER_DIRECTION = 5,
        COL_PARAMETER_TYPE = 6,
        COL_PARAMETER_LOCATION = 7,
        COL_PARAMETER_CONTENTS = 8 ;   
   
    
//	public static final String
//        QUERY_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER ) " +
//						  "VALUES ( ''{1}'', ''{2}'' )" ,
//	    QUERY_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = ''{1}'' AND STEPNUMBER = ''{2}''" ;    
//
//	public static final String
//		CATALOG_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER, CATALOGNAME ) " +
//						  "VALUES ( ''{1}'', ''{2}'', ''{3}'' )" ,
//		CATALOG_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = ''{1}'' AND STEPNUMBER = ''{2}''" ;  
//		
//	private static final int
//		COL_CATALOG_JOBURN = 1,
//		COL_CATALOG_STEPNUMBER = 2,
//		COL_CATALOG_CATALOGNAME = 3 ;    
//    
//	public static final String
//		TABLE_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER, CATALOGNAME, TABLENAME ) " +
//						  "VALUES ( ''{1}'', ''{2}'', ''{3}'', ''{4}'' )" , 
//        TABLE_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = ''{1}'' AND STEPNUMBER = ''{2}'' AND CATALOGNAME = ''{3}''" ; 
//          
//	private static final int
//		COL_TABLE_JOBURN = 1,
//		COL_TABLE_STEPNUMBER = 2,
//		COL_TABLE_CATALOGNAME = 3,
//		COL_TABLE_TABLENAME = 4 ;  
//						  
//	public static final String
//		SERVICE_INSERT_TEMPLATE = "INSERT INTO {0} ( JOBURN, STEPNUMBER, CATALOGNAME, SERVICENAME, SERVICEURL ) " +
//						  "VALUES ( ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'' )" ,  						    
//        SERVICE_SELECT_TEMPLATE = "SELECT * FROM {0} WHERE JOBURN = ''{1}'' AND STEPNUMBER = ''{2}'' AND CATALOGNAME = ''{3}''" ;  
//    
//	private static final int
//		COL_SERVICE_JOBURN = 1,
//		COL_SERVICE_STEPNUMBER = 2,
//		COL_SERVICE_CATALOGNAME = 3,
//		COL_SERVICE_SERVICENAME = 4,
//        COL_SERVICE_SERVICEURL = 5 ;      
        
    public static final String
        JOBID_SELECT_TEMPLATE = "SELECT ASSIGNED FROM {0}",
        JOBID_INSERT_TEMPLATE = "INSERT INTO {0} ( ASSIGNED ) VALUES ( ''{1}'' )" ; 
        
    public static final int
        COL_JOBID_ASSIGNED = 1 ;                
    
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
        ASTROGRIDERROR_DUPLICATE_TOOL_FOUND                       = "AGJESE00740",
        ASTROGRIDERROR_TOOL_NOT_FOUND                             = "AGJESE00750",
        ASTROGRIDERROR_PARAMETER_NOT_FOUND                        = "AGJESE00760",
        ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST            = "AGJESE00790", 
        ASTROGRIDERROR_UNABLE_TO_COMPLETE_UPDATE_REQUEST          = "AGJESE00800",
        ASTROGRIDERROR_JOBID_SINGLETON_ROW_FAULT                  = "AGJESE00810",
        ASTROGRIDERROR_UNEXPECTED_SQL_ERROR_ON_TABLE              = "AGJESE00820",
        ASTROGRIDERROR_JES_PERMISSION_DENIED                      = "AGJESE01000",
        ASTROGRIDERROR_PASSTHRU                                   = "AGJESE01010";
        
    private static final String
        AUTHORIZATION_RESOURCE_JOB = "job",
        AUTHORIZATION_ACTION_EDIT = "edit" ;
    
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
					   datasourceName = JES.getProperty( JES.JOB_DATASOURCE
                                                       , JES.JOB_CATEGORY ) ;
					   datasource = (DataSource) initialContext.lookup( datasourceName ) ;
				   }
			   } // end synchronized
			}
			
		}
		catch( NamingException ne ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_JOB_DATASOURCE
                                              , SUBCOMPONENT_NAME
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
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION
                                              , SUBCOMPONENT_NAME ) ;
			logger.error( message.toString(), e ) ;
			throw new JobException( message, e );
		}
		finally{
			if( TRACE_ENABLED ) logger.debug( "getConnection(): exit") ; 		
		}
		    
		return connection ;  

	} // end of getConnection()

	
    public Job createJob( Document jobDoc, String jobXML ) throws JobException  {
		if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.createJob(): entry") ;  
		 	
    	JobImpl
    	   job = null ;
		PreparedStatement
			pStatement = null ;
        String
            communitySnippet = null ;
    	   
    	try {

			job = new JobImpl( jobDoc, jobXML ) ;
		    job.setFactoryImpl( this ) ;
			job.setId( generateUniqueJobURN( job ) ) ;
			job.setStatus( Job.STATUS_INITIALIZED ) ;
            
            //
            // JL: this is probably the place to check authorization...
            //
            communitySnippet = CommunityMessage.getMessage( job.getToken()
                                                          , job.getUserId() + "@" + job.getCommunity() 
                                                          , job.getGroup() ) ; 
            
//            jobXML.substring( jobXML.indexOf( SubmissionRequestDD.COMMUNITY_TAG )
//                                               + SubmissionRequestDD.COMMUNITY_TAG.length()
//                                               , jobXML.indexOf( SubmissionRequestDD.COMMUNITY_ENDTAG ) ) ;

            logger.debug( "communitySnippet: " + communitySnippet ) ;
            
//            this.checkPermissions( AUTHORIZATION_RESOURCE_JOB, AUTHORIZATION_ACTION_EDIT, communitySnippet ) ;

			pStatement = ((JobImpl)job.getImplementation()).getPreparedStatement() ;
            
            logger.debug( "job.getId(): " + job.getId() ) ;
            logger.debug( "job.getName(): " + job.getName() ) ;
            logger.debug( "job.getStatus(): " + job.getStatus() ) ;
            logger.debug( "Timestamp: " + new Timestamp( job.getDate().getTime() ).toString() ) ;
            logger.debug( "job.getUserId(): " + job.getUserId() ) ;
            logger.debug( "job.getCommunity(): " + job.getCommunity() ) ;
            logger.debug( "job.getGroup(): " + job.getGroup() ) ;
            logger.debug( "job.getToken(): " + job.getToken() ) ;
            logger.debug( "job.getDocumentXML(): " + job.getDocumentXML() ) ;
            logger.debug( "job.getDescription(): " + job.getDescription() ) ;
			
			pStatement.setString( 1, job.getId() ) ; 
			pStatement.setString( 2, job.getName() ) ; 
			pStatement.setString( 3, job.getStatus() ) ;
			pStatement.setString( 4, new Timestamp( job.getDate().getTime() ).toString() ) ; 
			pStatement.setString( 5, job.getUserId() ) ; 
			pStatement.setString( 6, job.getCommunity() ) ;
            pStatement.setString( 7, job.getGroup() ) ; 
            pStatement.setString( 8, job.getToken() ) ; 
			pStatement.setString( 9, job.getDocumentXML() ) ;
            pStatement.setString( 10, job.getDescription() ) ;
			
			pStatement.execute();
			job.setDirty( false ) ;
			createJobSteps( job ) ;
    	}
    	catch( SQLException sex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT 
                                              , SUBCOMPONENT_NAME                ) ;
			logger.error( message.toString(), sex ) ;
			throw new JobException( message, sex );    		
    	}
    	finally {
    		if( pStatement != null) { try { pStatement.close(); } catch( SQLException sex ) {;} }
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
			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_JOB
                                        , JES.JOB_CATEGORY ) ;
			inserts[1] = job.getStatus() ;
			inserts[2] = job.getId() ;

			String
			   updateString = MessageFormat.format( JOB_UPDATE_TEMPLATE, inserts ) ; 
			if( TRACE_ENABLED ) logger.debug( "JobFactoryImp.updateJob() sql : " + updateString ) ; 			   			
			statement = this.getConnection().createStatement() ;
			statement.executeUpdate( updateString );
			updateJobSteps( job ) ;
		}
		catch( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_UPDATE_REQUEST
                                              , SUBCOMPONENT_NAME ) ;
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
			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_JOB
                                        , JES.JOB_CATEGORY ) ;
			inserts[1] = jobURN ;

			String
			   selectString = MessageFormat.format( JOB_SELECT_TEMPLATE, inserts ) ;
			if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.findJob() sql: " + selectString) ;			    			
			statement = this.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			
			if( !rs.isBeforeFirst() ) {
				// Whoops! Job not found when it should have been...
				AstroGridMessage
				   message = new AstroGridMessage( ASTROGRIDERROR_JOB_NOT_FOUND
                                                 , SUBCOMPONENT_NAME
                                                 , jobURN ) ;
				throw new NotFoundException( message ) ;
			}
			else {
				rs.next() ; // position on the first job found (hopefully the only one!)
				
				job.setId( rs.getString( COL_JOBURN ) ) ;
				job.setName( rs.getString( COL_JOBNAME ) ) ;
                job.setStatus( rs.getString( COL_STATUS ) ) ;
                job.setDate( this.formatDate( rs.getString( COL_SUBMITTIMESTAMP ) ) ) ;
				job.setUserId( rs.getString( COL_USERID ) ) ;
				job.setCommunity( rs.getString( COL_COCOMMUNITY ) ) ;
                job.setCommunity( rs.getString( COL_COGROUP ) ) ;  
                job.setToken( rs.getString( COL_TOKEN ) ) ;                
				job.setDocumentXML( rs.getString( COL_JOBXML ) ) ;
                job.setDescription( rs.getString( COL_DESCRIPTION ) ) ;
				job.setDirty( false ) ;
				
				findJobSteps( job ) ;
				
				if( rs.next() == true ) {
					// We have a duplicate Job!!! This should be impossible...
					AstroGridMessage
					   message = new AstroGridMessage( ASTROGRIDERROR_DUPLICATE_JOB_FOUND
                                                     , SUBCOMPONENT_NAME
                                                     , jobURN ) ;
					throw new DuplicateFoundException( message ) ;
				}

			} // end else
						   
		}
		catch( SQLException ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST
                                              , SUBCOMPONENT_NAME ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex ) ;   		
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "findJob(): exit") ;   	
		}  	
    	
		return job ; 
		  
    } // end of findJob()


    public ListIterator findUserJobs( String userid, String community, String jobXML ) throws JobException  {
        if( TRACE_ENABLED ) logger.debug( "findUserJobs(): entry") ;  
                    
        ArrayList
            list = null  ;
        Statement   
           statement = null ;
        ResultSet
           rs = null ;
        String
           communitySnippet = null ;
           
        try {
                      
            //
            // JL: this is probably the place to check authorization (at present: Sept 2003)...
            //
            communitySnippet = jobXML.substring( jobXML.indexOf( SubmissionRequestDD.COMMUNITY_TAG )
                                               + SubmissionRequestDD.COMMUNITY_TAG.length()
                                               , jobXML.indexOf( SubmissionRequestDD.COMMUNITY_ENDTAG ) ) ;

            logger.debug( "communitySnippet: " + communitySnippet ) ;
            
//            this.checkPermissions( AUTHORIZATION_RESOURCE_JOB, AUTHORIZATION_ACTION_EDIT, communitySnippet ) ;

            Object []
               inserts = new Object[3] ;
            inserts[0] = JES.getProperty( JES.JOB_TABLENAME_JOB
                                        , JES.JOB_CATEGORY ) ;
            inserts[1] = userid ;
            inserts[2] = community ;

            String
               selectString = MessageFormat.format( JOB_SELECT_USERSJOBS_TEMPLATE, inserts ) ; 
            logger.debug( "JobFactoryImp.findUserJobs() sql : " + selectString ) ;         
            statement = this.getConnection().createStatement() ;
            rs = statement.executeQuery( selectString );
            
            if( !rs.isBeforeFirst() ) {
                // No Jobs found...
       
            }
            else {
                
                list = new ArrayList() ;
                
                while( rs.next() ) {
                    
                    JobImpl
                       job = new JobImpl() ;
                
                    job.setId( rs.getString( COL_JOBURN ) ) ;
                    job.setName( rs.getString( COL_JOBNAME ) ) ;
                    job.setStatus( rs.getString( COL_STATUS ) ) ;
                    job.setDate( this.formatDate( rs.getString( COL_SUBMITTIMESTAMP ) ) ) ;
                    job.setUserId( rs.getString( COL_USERID ) ) ;
                    job.setCommunity( rs.getString( COL_COCOMMUNITY ) ) ;
                    job.setCommunity( rs.getString( COL_COGROUP ) ) ;  
                    job.setToken( rs.getString( COL_TOKEN ) ) ;                
                    job.setDocumentXML( rs.getString( COL_JOBXML ) ) ;
                    job.setDescription( rs.getString( COL_DESCRIPTION ) ) ;
                    job.setDirty( false ) ;
                    
                    findJobSteps( job ) ;
                    
                    list.add( job ) ;
                    
                } // end while
                
            } // end else
                           
        }
        catch( SQLException ex ) {
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST
                                              , SUBCOMPONENT_NAME ) ;
            logger.error( message.toString(), ex ) ; 
            throw new JobException( message, ex ) ;         
        }
        finally {
            if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
            if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
            if( TRACE_ENABLED ) logger.debug( "findUserJobs(): exit") ;    
        }       
        
        return list.listIterator() ;

    } // end of findUserJobs()


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
			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_JOB
                                        , JES.JOB_CATEGORY ) ;
			inserts[1] = criteriaString ;

			String
			   selectString = MessageFormat.format( JOB_GENERAL_SELECT_TEMPLATE, inserts ) ; 
			if( TRACE_ENABLED ) logger.debug( "JobFactoryImp.findJobsWhere() sql : " + selectString ) ;			
			statement = this.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			
			if( !rs.isBeforeFirst() ) {
				// Whoops! No Jobs found when perhaps there should have been...
				AstroGridMessage
				   message = new AstroGridMessage( ASTROGRIDERROR_JOBS_NOT_FOUND
                                                 , SUBCOMPONENT_NAME
                                                 , criteriaString  ) ; 
				throw new NotFoundException( message ) ;
			}
			else {
				
				set = new HashSet() ;
				
				while( rs.next() ) {
					
					JobImpl
					   job = new JobImpl() ;
				
                    job.setId( rs.getString( COL_JOBURN ) ) ;
                    job.setName( rs.getString( COL_JOBNAME ) ) ;
                    job.setStatus( rs.getString( COL_STATUS ) ) ;
                    job.setDate( this.formatDate( rs.getString( COL_SUBMITTIMESTAMP ) ) ) ;
                    job.setUserId( rs.getString( COL_USERID ) ) ;
                    job.setCommunity( rs.getString( COL_COCOMMUNITY ) ) ;
                    job.setCommunity( rs.getString( COL_COGROUP ) ) ;  
                    job.setToken( rs.getString( COL_TOKEN ) ) ;                
                    job.setDocumentXML( rs.getString( COL_JOBXML ) ) ;
                    job.setDescription( rs.getString( COL_DESCRIPTION ) ) ;
				    job.setDirty( false ) ;
				    
					findJobSteps( job ) ;
					
				    set.add( job ) ;
				    
				} // end while
				
			} // end else
						   
		}
		catch( SQLException ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST
                                              , SUBCOMPONENT_NAME ) ;
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
			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_JOB
                                        , JES.JOB_CATEGORY ) ;
			inserts[1] = job.getId() ;

			String
			   deleteString = MessageFormat.format( JOB_DELETE_TEMPLATE, inserts ) ; 
			if( TRACE_ENABLED ) logger.debug( "JobFactoryImp.deleteJob() sql : " + deleteString ) ; 			
			statement = getConnection().createStatement() ;
			statement.executeUpdate( deleteString );
			   
		}
		catch( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT 
                                              , SUBCOMPONENT_NAME) ;
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
			
			AstroGridMessage
				message ;
			if( bCommit ) {
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_COMMIT
                                              , SUBCOMPONENT_NAME
                                              , sex ) ;
			}
			else{
				message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_ROLLBACK
                                              , SUBCOMPONENT_NAME
                                              , sex ) ;
			}
			logger.error( message.toString() ) ; 
			throw new JobException( message ) ;	
		}
		finally {
			
			try { 
				if( connection != null ) connection.close() ; 
			} 
			catch( SQLException e ) { 
				logger.error( new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_CLOSE_CONNECTION
                                                  , SUBCOMPONENT_NAME ) ) ; 
			}
			connection = null ;
			if( TRACE_ENABLED ) logger.debug( "end(): exit") ;   	
		}
		
		return retVal ;
 
	} // end of end()
	
	
	private void createJobSteps( Job job ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "createJobSteps(): entry") ;   
					
		try {
			
			int
			   count = 0 ;
			Iterator
			   iterator = job.getJobSteps() ;
			JobStep
			   jobStep = null ;
		   
			while ( iterator.hasNext() ) {
				jobStep = (JobStep)iterator.next() ;
			    insertOneJobStep( jobStep ) ;			
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
			   inserts = new Object[8] ;                          //JBL altered iteration 3
			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_JOBSTEP
                                        , JES.JOB_CATEGORY ) ;
			inserts[1] = jobStep.getParent().getId() ;            // foreign key to parent
			inserts[2] = jobStep.getStepNumber() ;                // unique for step within job
			inserts[3] = jobStep.getName() ;
			inserts[4] = jobStep.getStatus() ;            
			inserts[5] = jobStep.getComment() ;
            inserts[6] = jobStep.getSequenceNumber() ;            //JBL added iteration 3
            inserts[7] = jobStep.getJoinCondition() ;             //JBL added iteration 3

			String
			   updateString = MessageFormat.format( JOBSTEP_INSERT_TEMPLATE, inserts ) ; 
			logger.debug( "Create JobStep: " + updateString ) ;			
			statement = getConnection().createStatement() ;
			statement.executeUpdate( updateString );
			createTool( jobStep.getTool() ) ;
		}
		catch( SQLException sex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT
                                              , SUBCOMPONENT_NAME ) ;
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
			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_JOBSTEP
                                        , JES.JOB_CATEGORY ) ;
			inserts[1] = job.getId() ;
			String
			   selectString = MessageFormat.format( JOBSTEP_SELECT_TEMPLATE, inserts ) ; 	
			logger.debug( "Find jobsteps: " + selectString ) ;		
			statement = this.getConnection().createStatement() ;
			rs = statement.executeQuery( selectString );
			
			if( !rs.isBeforeFirst() ) {
				// Whoops! JobStep not found when it should have been...
				AstroGridMessage
				   message = new AstroGridMessage( ASTROGRIDERROR_JOBSTEP_NOT_FOUND
                                                 , SUBCOMPONENT_NAME
                                                 , job.getId() ) ;
				throw new NotFoundException( message ) ;
			}
			else {
				
				while( rs.next() ) {
					
					jobStep = new JobStep( job ) ; 					
					jobStep.setStepNumber( new Integer( rs.getString( COL_JOBSTEP_STEPNUMBER ).trim() ) ) ;
					jobStep.setName( rs.getString( COL_JOBSTEP_STEPNAME ).trim() ) ;
					jobStep.setStatus( rs.getString( COL_JOBSTEP_STATUS ).trim() ) ;
					jobStep.setComment( rs.getString( COL_JOBSTEP_COMMENT ) ) ;
                    //JBL added iteration 3 - sequenceNumber and joinCondition ...
                    jobStep.setSequenceNumber( new Integer( rs.getString( COL_JOBSTEP_SEQUENCENUMBER ).trim() ) ) ;
                    jobStep.setJoinCondition( rs.getString( COL_JOBSTEP_JOINCONDITION ).trim() ) ;
					findTool( jobStep ) ;
					job.addJobStep( jobStep ) ;
					
				} // end while 

			} // end else
						   
		}
		catch( SQLException ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST
                                              , SUBCOMPONENT_NAME ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex ) ;   		
		}
		finally {
			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "findJobSteps(): exit") ;   	
		}  	
		  
	} // end of findJobSteps()
    
    
    private void createTool( Tool tool ) throws JobException {
      if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.createTool(): entry") ;  
          
      Statement    
         statement = null ;
         
      try {
          
          Object []
             inserts = new Object[4] ;
          inserts[0] = JES.getProperty( JES.JOB_TABLENAME_TOOL
                                      , JES.JOB_CATEGORY ) ;
          inserts[1] = tool.getParent().getParent().getId() ;
          inserts[2] = tool.getParent().getStepNumber() ;
          inserts[3] = tool.getName() ;

          String
             updateString = MessageFormat.format( TOOL_INSERT_TEMPLATE, inserts ) ; 
          logger.debug( "Create Tool: " + updateString ) ;           
          statement = getConnection().createStatement() ;
          statement.executeUpdate( updateString );
          createParameters( tool ) ;

      }
      catch( SQLException sex ) {
          AstroGridMessage
              message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT
                                            , SUBCOMPONENT_NAME ) ;
          logger.error( message.toString(), sex ) ;
          throw new JobException( message, sex );         
      }
      finally {
          if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
          if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.createQuery(): exit") ;      
      }
      
    }
	
    
    public void findTool( JobStep jobStep ) throws JobException {
        if( TRACE_ENABLED ) logger.debug( "JobFactoryImp.findTool(): entry") ;  
          
        // "SELECT * FROM {0} WHERE JOBURN = ''{1}'' AND STEPNUMBER = ''{2}''" ;      
          
        Tool
            tool = null  ;
        Statement   
            statement = null ;
        ResultSet
            rs = null ;
         
        try {

            Object []
                inserts = new Object[3] ;
            inserts[0] = JES.getProperty( JES.JOB_TABLENAME_TOOL
                                        , JES.JOB_CATEGORY ) ;
            inserts[1] = jobStep.getParent().getId() ;
            inserts[2] = jobStep.getStepNumber() ;

            String
                selectString = MessageFormat.format( TOOL_SELECT_TEMPLATE, inserts ) ;  
            logger.debug( "findTool: " + selectString ) ;      
            statement = this.getConnection().createStatement() ;
            rs = statement.executeQuery( selectString );
          
            if( !rs.isBeforeFirst() ) {
                // Whoops! Tool not found when it should have been...
                // AGJESE00750=:JobFactoryImpl: Query not found for Job with URN [{0}] and JobStep number [{1}]
                AstroGridMessage
                    message = new AstroGridMessage( ASTROGRIDERROR_TOOL_NOT_FOUND
                                                  , SUBCOMPONENT_NAME
                                                  , jobStep.getParent().getId()
                                                  , jobStep.getStepNumber() ) ;
                logger.error( message.toString() ) ;
                throw new NotFoundException( message ) ;
            }
            else {
                rs.next() ; // position on the first tool found (hopefully the only one!)
  
                tool = new Tool() ;
                tool.setParent( jobStep ) ;
                tool.setName( rs.getString( COL_TOOL_TOOLNAME ) ) ;
                jobStep.setTool( tool ) ;
                findParameters( tool ) ;
                       
                if( rs.next() == true ) {
                    // We have a duplicate Tool!!! This should be impossible...
                    AstroGridMessage
                        message = new AstroGridMessage( ASTROGRIDERROR_DUPLICATE_TOOL_FOUND
                                                      , SUBCOMPONENT_NAME
                                                      , jobStep.getParent().getId()
                                                      , jobStep.getStepNumber() ) ;
                    logger.error( message.toString() ) ;
                    throw new DuplicateFoundException( message ) ;
                }

            } // end else
                         
        }
        catch( SQLException ex ) {
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST
                                              , SUBCOMPONENT_NAME ) ;
            logger.error( message.toString(), ex ) ;
            throw new JobException( message, ex ) ;         
        }
        finally {
            if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
            if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
            if( TRACE_ENABLED ) logger.debug( "JobFactoryImp.findTool(): exit") ;    
        }   
        
    } // end of JobFactoryImp.findTool()
    
    
    
    
    
    private void createParameters( Tool tool ) throws JobException {
      if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.createParameters(): entry") ;   
                  
      try {
          
            Iterator
                iterator = tool.getInputParameters() ;
         
            while ( iterator.hasNext() ) {
                insertOneParameter( (Parameter)iterator.next(), "input" ) ;          
            } 
           
            iterator = tool.getInputParameters() ; 
            
            while ( iterator.hasNext() ) {
                insertOneParameter( (Parameter)iterator.next(), "output" ) ;          
            } 

        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.createParameters()(): exit") ;       
        }
          
    } // end of JobFactoryImpl.createParameters()
    
    
    public void findParameters( Tool tool ) throws JobException {
        if( TRACE_ENABLED ) logger.debug( "JobFactoryImp.findParameters(): entry") ;  
          
        Parameter
            parameter = null ;
        Statement   
            statement = null ;
        ResultSet
            rs = null ;
         
        try {

            Object []
                inserts = new Object[4] ;
            inserts[0] = JES.getProperty( JES.JOB_TABLENAME_PARAMETER
                                        , JES.JOB_CATEGORY ) ;
            inserts[1] = tool.getParent().getParent().getId() ;
            inserts[2] = tool.getParent().getStepNumber() ;
            inserts[3] = tool.getName() ;

            String
                selectString = MessageFormat.format( PARAMETER_SELECT_TEMPLATE, inserts ) ;    
            logger.debug( "Find parameters: " + selectString ) ;      
            statement = this.getConnection().createStatement() ;
            rs = statement.executeQuery( selectString );
          
            if( !rs.isBeforeFirst() ) {
                // Whoops! No parameters found when there should have been...
                AstroGridMessage
                    message = new AstroGridMessage( ASTROGRIDERROR_PARAMETER_NOT_FOUND
                                                  , SUBCOMPONENT_NAME
                                                  , tool.getParent().getParent().getId()
                                                  , tool.getParent().getStepNumber() ) ;
              throw new NotFoundException( message ) ;
            }
            else {
                
                String
                    direction = null ;
                ArrayList
                    inputParams = new ArrayList(),
                    outputParams = new ArrayList() ;
              
                while( rs.next() ) {
                  
                    parameter = new Parameter() ;                    
                    parameter.setName( rs.getString( COL_PARAMETER_PARAMNAME ) ) ;
                    direction = rs.getString( COL_PARAMETER_DIRECTION ) ;
                    parameter.setType( rs.getString( COL_PARAMETER_TYPE ) );
                    parameter.setLocation( rs.getString( COL_PARAMETER_LOCATION) ) ;
                    parameter.setContents( rs.getString( COL_PARAMETER_CONTENTS ) ) ;
                    
                    if( direction.equals( "input") ) {
                        inputParams.add( parameter ) ;
                    }
                    else if( direction.equals( "output") ) {
                        outputParams.add( parameter ) ;
                    } 
                  
                } // end while 
                
                tool.setInputParameters( inputParams ) ;
                tool.setOutputParameters( outputParams ) ;  

            } // end else
                         
        }
        catch( SQLException ex ) {
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST
                                              , SUBCOMPONENT_NAME ) ;
            logger.error( message.toString(), ex ) ;
            throw new JobException( message, ex ) ;         
        }
        finally {
            if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
            if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
            if( TRACE_ENABLED ) logger.debug( "JobFactoryImp.findParameters(): exit") ;     
        }   
        
    } // end of JobFactoryImp.findParameters()
    
    
    private void insertOneParameter( Parameter param, String direction ) throws JobException {
      if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.insertOneParameter(): entry") ;   
          
      // INSERT INTO {0} ( JOBURN, STEPNUMBER, TOOLNAME, PARAMNAME, DIRECTION, TYPE, LOCATION, CONTENTS  ) 
      // VALUES ( ''{1}'', ''{2}'', ''{3}'', ''{4}'', ''{5}'', ''{6}'', ''{7}'', ''{8}'' ) 
                    
        Statement   
            statement = null ;
                  
        try {

            Object []
                inserts = new Object[9] ;
                
            inserts[0] = JES.getProperty( JES.JOB_TABLENAME_PARAMETER
                                        , JES.JOB_CATEGORY ) ;
            inserts[1] = param.getParent().getParent().getParent().getId() ;            
            inserts[2] = param.getParent().getParent().getStepNumber() ;
            inserts[3] = param.getParent().getName() ;
            inserts[4] = param.getName() ;
            inserts[5] = direction ;
            inserts[6] = param.getType() ;
            inserts[7] = param.getLocation() ;
            inserts[8] = param.getContents() ;

            String
                updateString = MessageFormat.format( PARAMETER_INSERT_TEMPLATE, inserts ) ;    
            logger.debug( "Create Parameter: " + updateString ) ;     
            statement = getConnection().createStatement() ;
            statement.executeUpdate( updateString );
      }
      catch( SQLException sex ) {
          AstroGridMessage
              message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT
                                            , SUBCOMPONENT_NAME ) ;
          logger.error( message.toString(), sex ) ;
          throw new JobException( message, sex );         
      }
      finally {
          if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
          if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.insertOneParameter(): exit") ;     
      } 
             
    } // end of JobFactoryImpl.insertOneParameter()
    
       
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
			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_JOBSTEP
                                        , JES.JOB_CATEGORY ) ;
			inserts[1] = jobStep.getStatus() ;
			inserts[2] = jobStep.getComment() ;
			inserts[3] = jobStep.getParent().getId() ;
			inserts[4] = jobStep.getStepNumber().toString() ;      

			String
			   updateString = MessageFormat.format( JOBSTEP_UPDATE_TEMPLATE, inserts ) ; 
			logger.debug( "Update JobStep: " + updateString ) ;			
			statement = getConnection().createStatement() ;
			statement.executeUpdate( updateString );
		}
		catch( SQLException sex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_UPDATE_REQUEST
                                              , SUBCOMPONENT_NAME ) ;
			logger.error( message.toString(), sex ) ;
			throw new JobException( message, sex );    		
		}
		finally {
			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
			if( TRACE_ENABLED ) logger.debug( "updateOneJobStep(): exit") ;   	
		}
				
	} // end of updateOneJobStep()
	
	 
//	public void createQuery( Query query ) throws JobException  {
//		 if( TRACE_ENABLED ) logger.debug( "createQuery(): entry") ;  
//		 	
//		 Statement    
//			statement = null ;
//    	   
//		 try {
//			
//			 Object []
//				inserts = new Object[3] ;
//			 inserts[0] = JES.getProperty( JES.JOB_TABLENAME_QUERY
//                                         , JES.JOB_CATEGORY ) ;
//			 inserts[1] = query.getParent().getParent().getId() ;
//			 inserts[2] = query.getParent().getStepNumber() ;
//
//			 String
//				updateString = MessageFormat.format( QUERY_INSERT_TEMPLATE, inserts ) ; 
//		     logger.debug( "Create Query: " + updateString ) ;			
//			 statement = getConnection().createStatement() ;
//			 statement.executeUpdate( updateString );
//			 createCatalogs( query ) ;
//
//		 }
//		 catch( SQLException sex ) {
//			 AstroGridMessage
//				 message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT
//                                               , SUBCOMPONENT_NAME ) ;
//			 logger.error( message.toString(), sex ) ;
//			 throw new JobException( message, sex );    		
//		 }
//		 finally {
//			 if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
//			 if( TRACE_ENABLED ) logger.debug( "createQuery(): exit") ;   	
//		 }
//		
//	 } // end of createQuery() 
//
//
//	public void findQuery( JobStep jobStep ) throws JobException {
//		if( TRACE_ENABLED ) logger.debug( "findQuery(): entry") ;  
//		 	
//		Query
//		   query = null ;
//		Statement   
//		   statement = null ;
//		ResultSet
//		   rs = null ;
//    	   
//		try {
//
//			Object []
//			   inserts = new Object[3] ;
//			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_QUERY
//                                        , JES.JOB_CATEGORY ) ;
//			inserts[1] = jobStep.getParent().getId() ;
//			inserts[2] = jobStep.getStepNumber() ;
//
//			String
//			   selectString = MessageFormat.format( QUERY_SELECT_TEMPLATE, inserts ) ; 	
//			logger.debug( "findQuery: " + selectString ) ;		
//			statement = this.getConnection().createStatement() ;
//			rs = statement.executeQuery( selectString );
//			
//			if( !rs.isBeforeFirst() ) {
//				// Whoops! Query not found when it should have been...
//				// AGJESE00750=:JobFactoryImpl: Query not found for Job with URN [{0}] and JobStep number [{1}]
//				AstroGridMessage
//				   message = new AstroGridMessage( ASTROGRIDERROR_QUERY_NOT_FOUND
//                                                 , SUBCOMPONENT_NAME
//				                                 , jobStep.getParent().getId()
//				                                 , jobStep.getStepNumber() ) ;
//                logger.error( message.toString() ) ;
//				throw new NotFoundException( message ) ;
//			}
//			else {
//				rs.next() ; // position on the first query found (hopefully the only one!)
//	
//	            // There is little stored in Query AT THE MOMENT.
//	            // (In fact nothing but foreign keys)
//	            // We assume here that everything is OK... (no database corruption)
//				query = new Query( jobStep ) ;
//				findCatalogs( query ) ;
//				jobStep.setQuery( query ) ;		
//					
//				if( rs.next() == true ) {
//					// We have a duplicate Query!!! This should be impossible...
//					AstroGridMessage
//					   message = new AstroGridMessage( ASTROGRIDERROR_DUPLICATE_QUERY_FOUND
//                                                     , SUBCOMPONENT_NAME
//											         , jobStep.getParent().getId()
//                                                     , jobStep.getStepNumber() ) ;
//                    logger.error( message.toString() ) ;
//					throw new DuplicateFoundException( message ) ;
//				}
//
//			} // end else
//						   
//		}
//		catch( SQLException ex ) {
//			AstroGridMessage
//				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST
//                                              , SUBCOMPONENT_NAME ) ;
//			logger.error( message.toString(), ex ) ;
//			throw new JobException( message, ex ) ;   		
//		}
//		finally {
//			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
//			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
//			if( TRACE_ENABLED ) logger.debug( "findQuery(): exit") ;   	
//		}  	
//		  
//	} // end of findQuery()
//	
//	
//	private void createCatalogs( Query query ) throws JobException {
//		if( TRACE_ENABLED ) logger.debug( "createJobSteps(): exit") ;   
//					
//		try {
//			
//			int
//			   count = 0 ;
//			Iterator
//			   iterator = query.getCatalogs() ;
//		   
//			while ( iterator.hasNext() ) {
//				insertOneCatalog( (Catalog)iterator.next() ) ;			
//			} 
//
//		}
//		finally {
//			if( TRACE_ENABLED ) logger.debug( "createJobSteps(): exit") ;   	
//		}
//			
//	} // end of createCatalogs(Job)
//
//
//	private void insertOneCatalog ( Catalog catalog ) throws JobException {
//		if( TRACE_ENABLED ) logger.debug( "insertOneCatalog(): exit") ;   
//			
//		Statement   
//		   statement = null ;
//					
//		try {
//
//			Object []
//			   inserts = new Object[4] ;
//			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_CATALOG
//                                        , JES.JOB_CATEGORY ) ;
//			inserts[1] = catalog.getParent().getParent().getParent().getId() ;            
//			inserts[2] = catalog.getParent().getParent().getStepNumber() ;
//			inserts[3] = catalog.getName() ;
//
//
//			String
//			   updateString = MessageFormat.format( CATALOG_INSERT_TEMPLATE, inserts ) ; 	
//			logger.debug( "Create Catalog: " + updateString ) ;		
//			statement = getConnection().createStatement() ;
//			statement.executeUpdate( updateString );
//			createTables( catalog ) ;
//			createServices( catalog ) ;
//		}
//		catch( SQLException sex ) {
//			AstroGridMessage
//				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT
//                                              , SUBCOMPONENT_NAME ) ;
//			logger.error( message.toString(), sex ) ;
//			throw new JobException( message, sex );    		
//		}
//		finally {
//			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
//			if( TRACE_ENABLED ) logger.debug( "insertOneCatalog(): exit") ;   	
//		}
//				
//	} // end of insertOneCatalog()
//	
//	
//	public void findCatalogs( Query query ) throws JobException {
//		if( TRACE_ENABLED ) logger.debug( "findCatalogs(): entry") ;  
//		 	
//		Catalog
//		   catalog = null ;
//		Statement   
//		   statement = null ;
//		ResultSet
//		   rs = null ;
//    	   
//		try {
//
//			Object []
//			   inserts = new Object[3] ;
//			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_CATALOG
//                                        , JES.JOB_CATEGORY ) ;
//			inserts[1] = query.getParent().getParent().getId() ;
//			inserts[2] = query.getParent().getStepNumber() ;
//
//			String
//			   selectString = MessageFormat.format( CATALOG_SELECT_TEMPLATE, inserts ) ; 	
//			logger.debug( "Find catalogs: " + selectString ) ;		
//			statement = this.getConnection().createStatement() ;
//			rs = statement.executeQuery( selectString );
//			
//			if( !rs.isBeforeFirst() ) {
//				// Whoops! Catalog not found when it should have been...
//				AstroGridMessage
//				   message = new AstroGridMessage( ASTROGRIDERROR_CATALOG_NOT_FOUND
//                                                 , SUBCOMPONENT_NAME
//				                                 , query.getParent().getParent().getId()
//				                                 , query.getParent().getStepNumber() ) ;
//				throw new NotFoundException( message ) ;
//			}
//			else {
//				
//				while( rs.next() ) {
//					
//					catalog = new Catalog( query ) ; 					
//					catalog.setName( rs.getString( COL_CATALOG_CATALOGNAME ) ) ;
//					findTables( catalog ) ;
//					findServices( catalog ) ;
//					query.addCatalog( catalog ) ;
//					
//				} // end while 
//
//			} // end else
//						   
//		}
//		catch( SQLException ex ) {
//			AstroGridMessage
//				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST
//                                              , SUBCOMPONENT_NAME ) ;
//			logger.error( message.toString(), ex ) ;
//			throw new JobException( message, ex ) ;   		
//		}
//		finally {
//			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
//			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
//			if( TRACE_ENABLED ) logger.debug( "findCatalogs(): exit") ;   	
//		}  	
//		  
//	} // end of findCatalogs()
//	
//	
//	private void createTables( Catalog catalog ) throws JobException {
//		if( TRACE_ENABLED ) logger.debug( "createTables(): exit") ;   
//					
//		try {
//			
//			Iterator
//			   iterator = catalog.getTables() ;
//		   
//			while ( iterator.hasNext() ) {
//				insertOneTable( (Table)iterator.next() ) ;			
//			} 
//
//		}
//		finally {
//			if( TRACE_ENABLED ) logger.debug( "createTables(): exit") ;   	
//		}
//			
//	} // end of createTables(Job) 
//	
//	
//	private void createServices( Catalog catalog ) throws JobException {
//		if( TRACE_ENABLED ) logger.debug( "createServices(): exit") ;   
//					
//		try {
//			
//			Iterator
//			   iterator = catalog.getServices() ;
//		   
//			while ( iterator.hasNext() ) {
//				insertOneService( (Service)iterator.next() ) ;			
//			} 
//
//		}
//		finally {
//			if( TRACE_ENABLED ) logger.debug( "createServices(): exit") ;   	
//		}
//			
//	} // end of createTables(Job) 
//	
//	
//	private void insertOneTable( Table table ) throws JobException {
//		if( TRACE_ENABLED ) logger.debug( "insertOneTable(): exit") ;   
//			
//		Statement   
//		   statement = null ;
//					
//		try {
//
//			Object []
//			   inserts = new Object[5] ;
//			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_TABLE
//                                        , JES.JOB_CATEGORY ) ;
//			inserts[1] = table.getParent().getParent().getParent().getParent().getId() ;  // JobURN
//			inserts[2] = table.getParent().getParent().getParent().getStepNumber() ;      // step number
//			inserts[3] = table.getParent().getName() ;                                    // catalog name
//			inserts[4] = table.getName() ;                                                // table name
//   
//			String
//			   updateString = MessageFormat.format( TABLE_INSERT_TEMPLATE, inserts ) ; 
//			logger.debug( "Create Table: " + updateString ) ;			
//			statement = getConnection().createStatement() ;
//			statement.executeUpdate( updateString );
//		}
//		catch( SQLException sex ) {
//			AstroGridMessage
//				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT
//                                              , SUBCOMPONENT_NAME ) ;
//			logger.error( message.toString(), sex ) ;
//			throw new JobException( message, sex );    		
//		}
//		finally {
//			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
//			if( TRACE_ENABLED ) logger.debug( "insertOneTable(): exit") ;   	
//		}
//				
//	} // end of insertOneTable()
//	
//
//	private void insertOneService( Service service ) throws JobException {
//		if( TRACE_ENABLED ) logger.debug( "insertOneService(): exit") ;   
//			
//		Statement   
//		   statement = null ;
//					
//		try {
//
//			Object []
//			   inserts = new Object[6] ;
//			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_SERVICE
//                                        , JES.JOB_CATEGORY ) ;
//			inserts[1] = service.getParent().getParent().getParent().getParent().getId() ; // JobURN  
//			inserts[2] = service.getParent().getParent().getParent().getStepNumber() ;     // step number
//			inserts[3] = service.getParent().getName() ;                                   // catalog name
//			inserts[4] = service.getName() ;                                               // service name
//			inserts[5] = service.getUrl() ;                                                // service url
//  		 
//			String
//			   updateString = MessageFormat.format( SERVICE_INSERT_TEMPLATE, inserts ) ; 
//			logger.debug( "Create Service: " + updateString ) ;			
//			statement = getConnection().createStatement() ;
//			statement.executeUpdate( updateString );
//		}
//		catch( SQLException sex ) {
//			AstroGridMessage
//				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT
//                                              , SUBCOMPONENT_NAME ) ;
//			logger.error( message.toString(), sex ) ;
//			throw new JobException( message, sex );    		
//		}
//		finally {
//			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
//			if( TRACE_ENABLED ) logger.debug( "insertOneService(): exit") ;   	
//		}
//				
//	} // end of insertOneService()
//	
		
	 
	private String generateUniqueJobURN( Job job ) throws JobException {
		
	    StringBuffer
	        buffer = new StringBuffer( 64 ) ;
	        
	    buffer
	       .append( job.getUserId() )
	       .append( ':' )
	       .append( job.getCommunity() )
	       .append( ':' )
	       .append( JES.getProperty( JES.JES_ID, JES.JES_CATEGORY ) )
	       .append( ':' )
	       .append( generateUniqueInt() ) ;
	       
		return buffer.toString() ;
		
	} // end of generateUniqueJobURN()
	
	
//	public void findTables( Catalog catalog ) throws JobException {
//		if( TRACE_ENABLED ) logger.debug( "findTables(): entry") ;  
//		 	
//		Table
//		   table = null ;
//		Statement   
//		   statement = null ;
//		ResultSet
//		   rs = null ;
//    	   
//		try {
//
//			Object []
//			   inserts = new Object[4] ;
//			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_TABLE
//                                        , JES.JOB_CATEGORY ) ;
//			inserts[1] = catalog.getParent().getParent().getParent().getId() ;
//			inserts[2] = catalog.getParent().getParent().getStepNumber() ;
//			inserts[3] = catalog.getName() ;			
//
//			String
//			   selectString = MessageFormat.format( TABLE_SELECT_TEMPLATE, inserts ) ; 	
//			logger.debug( "Find tables: " + selectString ) ;		
//			statement = this.getConnection().createStatement() ;
//			rs = statement.executeQuery( selectString );
//			
//			if( !rs.isBeforeFirst() ) {
//				// Whoops! Table not found when it should have been...
////				Message
////				   message = new Message( ASTROGRIDERROR_TABLE_NOT_FOUND
////				                        , catalog.getName()
////										, catalog.getParent().getParent().getParent().getId()
////										, catalog.getParent().getParent().getStepNumber() ) ;
////				throw new NotFoundException( message ) ;
//			}
//			else {
//				
//				while( rs.next() ) {
//					
//					table = new Table( catalog ) ; 					
//					table.setName( rs.getString( COL_TABLE_TABLENAME ) ) ;
//					catalog.addTable( table ) ;
//					
//				} // end while 
//
//			} // end else
//						   
//		}
//		catch( SQLException ex ) {
//			AstroGridMessage
//				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST
//                                              , SUBCOMPONENT_NAME ) ;
//			logger.error( message.toString(), ex ) ;
//			throw new JobException( message, ex ) ;   		
//		}
//		finally {
//			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
//			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
//			if( TRACE_ENABLED ) logger.debug( "findTables(): exit") ;   	
//		}  	
//		  
//	} // end of findTables()
//  
//  
//	public void findServices( Catalog catalog ) throws JobException {
//		if( TRACE_ENABLED ) logger.debug( "findServices(): entry") ;  
//		 	
//		Service
//		   service = null ;
//		Statement   
//		   statement = null ;
//		ResultSet
//		   rs = null ;
//    	   
//		try {
//
//			Object []
//			   inserts = new Object[4] ;
//			inserts[0] = JES.getProperty( JES.JOB_TABLENAME_SERVICE
//                                        , JES.JOB_CATEGORY ) ;
//			inserts[1] = catalog.getParent().getParent().getParent().getId() ;
//			inserts[2] = catalog.getParent().getParent().getStepNumber() ;
//			inserts[3] = catalog.getName() ;			
//
//			String
//			   selectString = MessageFormat.format( SERVICE_SELECT_TEMPLATE, inserts ) ; 	
//			logger.debug( "Find services: " + selectString ) ;		
//			statement = this.getConnection().createStatement() ;
//			rs = statement.executeQuery( selectString );
//			
//			if( !rs.isBeforeFirst() ) {
//				// Whoops! Service not found when it should have been...
//				AstroGridMessage
//				   message = new AstroGridMessage( ASTROGRIDERROR_SERVICE_NOT_FOUND
//                                                 , SUBCOMPONENT_NAME
//										         , catalog.getName()
//										         , catalog.getParent().getParent().getParent().getId()
//										         , catalog.getParent().getParent().getStepNumber() ) ;
//				throw new NotFoundException( message ) ;
//			}
//			else {
//				
//				while( rs.next() ) {
//					
//				    service = new Service( catalog ) ; 					
//					service.setName( rs.getString( COL_SERVICE_SERVICENAME ) ) ;
//					service.setUrl( rs.getString( COL_SERVICE_SERVICEURL ) ) ;
//					catalog.addService( service ) ;
//					
//				} // end while 
//
//			} // end else
//						   
//		}
//		catch( SQLException ex ) {
//			AstroGridMessage
//				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_COMPLETE_FIND_REQUEST
//                                              , SUBCOMPONENT_NAME ) ;
//			logger.error( message.toString(), ex ) ;
//			throw new JobException( message, ex ) ;   		
//		}
//		finally {
//			if( rs != null ) { try { rs.close(); } catch( SQLException sex ) {;} }
//			if( statement != null) { try { statement.close(); } catch( SQLException sex ) {;} }
//			if( TRACE_ENABLED ) logger.debug( "findServices(): exit") ;   	
//		}  	
//		  
//	} // end of findServices()
    
    
    private void checkPermissions ( String someResource, String anAction, String snippet ) throws JobException {
        if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.checkPermission() entry" ) ;
                        
        PolicyServiceDelegate 
            ps = null ;
        String
            communityAccount = null,
            credential = null ;
            
        try {
               
            ps = new PolicyServiceDelegate() ;
            communityAccount =  CommunityMessage.getAccount( snippet ) ;
            credential = CommunityMessage.getGroup( snippet ) ;
            boolean 
                authorized = ps.checkPermissions( communityAccount
                                                , credential
                                                , someResource
                                                , anAction ) ;             
               
            if( !authorized ) {
                    
                PolicyPermission pp = ps.getPolicyPermission();
                    
                String
                    reason = pp.getReason() ;
                        
                AstroGridMessage
                    message = new AstroGridMessage( ASTROGRIDERROR_PASSTHRU
                                                  , "Community"
                                                  , reason ) ;
                     
                throw new JobException( message ) ;
                        
            }
               
        }
        catch( JobException jex ) {
                
            AstroGridMessage 
                message = new AstroGridMessage( ASTROGRIDERROR_JES_PERMISSION_DENIED
                                              , JES.getClassName( this.getClass() )
                                              , jex.getAstroGridMessage().toString() ) ;
                     
            throw new JobException( message, (Exception)jex ) ;
                
        }
        catch( Exception ex ) {
                
            if( TRACE_ENABLED) ex.printStackTrace();  
               
            String
                localizedMessage = ex.getLocalizedMessage() ;    
               
           AstroGridMessage
               message = new AstroGridMessage( ASTROGRIDERROR_JES_PERMISSION_DENIED
                                             , JES.getClassName( this.getClass() )
                                             , (localizedMessage == null) ? "" : localizedMessage ) ;
                     
           throw new JobException( message, ex ) ;
               
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.checkPermission() exit" ) ;  
        }
             
    } // end of checkPermission()
    
    
    private Date formatDate( String dateString ) {
        if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.formatDate() entry" ) ;  
         
        final SimpleDateFormat
            dateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss.SSS" ) ;
        Date
            retDate = null ;           
           
        try {         
            logger.debug( "stringDate: " + dateString ) ;
            retDate = dateFormat.parse( dateString ); 
        }
        catch( ParseException pex ) { 
            retDate = new Date(); 
            logger.error( "Malformed date" ) ;
        } 
        catch( NullPointerException npex ) {
            retDate = new Date(); 
            logger.error( "Missing date" ) ;  
        }
        finally {
             if( TRACE_ENABLED ) logger.debug( "JobFactoryImpl.formatDate() exit" ) ;  
        }        
        
        return retDate ;   
                 
    } // end of formatDate()
        
               
} // end of class JobFactoryImpl