/*
 * @(#)JobImpl.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.impl;

import org.astrogrid.jes.job.Job ;
import org.astrogrid.jes.job.JobStep ;
import org.astrogrid.jes.job.JobException ;
import org.astrogrid.jes.JES ;
import org.astrogrid.jes.jobcontroller.SubmissionRequestDD ;
import org.astrogrid.i18n.*;
import org.astrogrid.Configurator ;
import org.apache.log4j.Logger;

import org.w3c.dom.* ;
import java.util.Date ;
import java.util.Set;
import java.util.HashSet ;
import java.util.Iterator ;

import java.util.ArrayList ;
import java.util.ListIterator;

import java.sql.Connection ;
import java.sql.PreparedStatement ;
import java.sql.SQLException ;
import java.text.MessageFormat ;

import javax.sql.DataSource ;


public class JobImpl extends Job {

	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobImpl.class ) ;
        
    private final static String 
        SUBCOMPONENT_NAME = Configurator.getClassName( JobFactoryImpl.class );                
		
	private static String
		ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION            = "AGDTCE00160" ,
	    ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT = "AGJESE00180" ;
	  
	private JobFactoryImpl
	   factoryImpl = null ;
	   
	private boolean
	   dirty = false ;
	   
	private String
	   documentXML = null,
	   jobURN = "",
	   name = "",
	   community = "",
	   userId = "",
	   status ;
	   
	private Date
	   date = null ;

//JBL iteration 3	   
//	private Set
//	   jobSteps = new HashSet() ;

    private ArrayList
        jobSteps = new ArrayList() ;
   
	private Connection
		connection = null ;	   
	   
	private PreparedStatement 
		preparedStatement = null ;	   

	public JobImpl() {}
	 
	   
	public JobImpl( Document submitDoc, String submitXML ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "JobImpl(): entry") ;  
		 	   
		try {
			
			this.documentXML = submitXML ;
			date = new Date() ;
			
			Element
			   element = submitDoc.getDocumentElement() ;	
			   
			name = element.getAttribute( SubmissionRequestDD.JOB_NAME_ATTR ) ;
			   		   
			NodeList
			   nodeList = element.getChildNodes() ; 
			    		   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {			
				if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
					
					element = (Element) nodeList.item(i) ;
				
					if ( element.getTagName().equals( SubmissionRequestDD.JOBSTEP_ELEMENT ) ) {
//						name = element.getAttribute( SubmissionRequestDD.JOBSTEP_NAME_ATTR ).trim() ;
                        // We must be certain these appear in StepNumber order!
						jobSteps.add( new JobStep( this, element ) ) ;   
					}					
					else if (element.getTagName().equals( SubmissionRequestDD.USERID_ELEMENT) ) {					 	
						userId = element.getFirstChild().getNodeValue().trim();
					}
					else if (element.getTagName().equals( SubmissionRequestDD.COMMUNITY_ELEMENT) ) {					 	
					    community = element.getFirstChild().getNodeValue().trim();
				 	}
					
				} // end if
								
			} // end for		
/*
			nodeList = element.getChildNodes() ;  					   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {
							
				if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {				
					element = (Element) nodeList.item(i) ;					
				    if( element.getTagName().equals( SubmissionRequestDD.USERID_ELEMENT ) ) {				    	
					    userId = element.getNodeValue().trim() ;					    
				    }
				    else if( element.getTagName().equals( SubmissionRequestDD.COMMUNITY_ELEMENT ) ) {
					    community = element.getNodeValue().trim() ;					    
				    }
				    else if( element.getTagName().equals( SubmissionRequestDD.JOBSTEP_ELEMENT ) ) {
				        jobSteps.add( new JobStep( this, element ) ) ;   
				    }
				
				} // end if
				
			} // end for
*/			
		}
		catch( Exception ex ) {		
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT
                                              , SUBCOMPONENT_NAME ) ;
			logger.error( message.toString(), ex ) ;
			// We must have at least the semblance of a primary key for the Job entity...    
			if( (this.jobURN == null) || (this.jobURN.equals("")) ) {
				throw new JobException( message ) ;		
			}
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "JobImpl(): exit") ;   	
		}
		
	} // end of JobImpl()
	
	public PreparedStatement getPreparedStatement() throws JobException, SQLException {
		if( TRACE_ENABLED ) logger.debug( "getPreparedStatement(): entry") ; 
		
		try { 			   
		
			if( preparedStatement == null ) {
				Object[]
					inserts = new Object[1] ;
			 	inserts[0] = JES.getProperty( JES.JOB_TABLENAME_JOB
                                            , JES.JOB_CATEGORY ) ;
				String
				     updateString = MessageFormat.format( JobFactoryImpl.JOB_INSERT_TEMPLATE, inserts ) ; 									     			
				preparedStatement = getConnection().prepareStatement( updateString ) ;		
			}
		    
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "getPreparedStatement(): exit") ; 	
		}
		
		return preparedStatement ;
		
	}// end of getPreparedStatement()	

	public Connection getConnection() throws JobException {
		if( TRACE_ENABLED ) logger.debug( "getConnection(): entry") ; 
		
		try{
			if( connection == null ) {
				logger.debug( "connection is null" ) ;
				DataSource
					 dataSource ;
				logger.debug( "about to acquire datasource..." ) ;
				dataSource = JobFactoryImpl.getDataSource() ;
				logger.debug( "datasource acquired!" ) ;
				logger.debug( "about to acquire connection..." ) ;
				connection = dataSource.getConnection() ;
				logger.debug( "connection acquired!" ) ;
				logger.debug( "connection: " + connection.toString() ) ;
			}
			else {
				logger.debug( "connection is not null" ) ;
			}
		}
		catch( SQLException e ) {
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

	public String getId() {	return jobURN ;	}
	public void setId( String jobURN ) { this.jobURN = jobURN ;}
	
	public void setDirty( boolean dirty ) { this.dirty = dirty ; }
	public boolean isDirty() {return this.dirty ; }

	public void setName( String name ) { this.name = name; }
	public String getName() { return (this.name == null ? "" : this.name.trim() ); }
	
	public Date getDate() { return this.date ; }
	public void setDate( Date date ) { this.date = date ; }

	public void setCommunity( String community ) { this.community = community; }
	public String getCommunity() { return community; }

	public void setUserId( String userId ) { this.userId = userId; }
	public String getUserId() { return userId; } 
	
	public Iterator getJobSteps() { return this.jobSteps.iterator() ; }
	public boolean addJobStep( JobStep jobStep ) { return jobSteps.add( jobStep ); }
	public boolean removeJobStep( JobStep jobStep ) { return jobSteps.remove( jobStep ) ; }
	
	public Object getImplementation() { return this ; }

	public void setFactoryImpl(JobFactoryImpl factoryImpl) { this.factoryImpl = factoryImpl ; }
	public JobFactoryImpl getFactoryImpl() { return factoryImpl ; }

	public void setDocumentXML( String submitDoc ) { this.documentXML = submitDoc ; }
	public String getDocumentXML() { return documentXML ; }

	public void setStatus(String status) { this.status = status; }
	public String getStatus() {	return status; }

} // end of class JobImpl
