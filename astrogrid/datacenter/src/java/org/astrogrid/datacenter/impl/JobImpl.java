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
package org.astrogrid.datacenter.impl;

import org.astrogrid.datacenter.datasetagent.DatasetAgent;
import org.astrogrid.datacenter.datasetagent.RunJobRequestDD;
import org.astrogrid.datacenter.i18n.*;
import org.apache.log4j.Logger;
import org.astrogrid.datacenter.job.Job;
import org.astrogrid.datacenter.job.JobException;
import org.astrogrid.datacenter.job.JobStep;
import org.w3c.dom.* ;
import java.util.Date ;
import java.sql.Connection ;
import java.sql.PreparedStatement ;
import java.sql.ResultSet ;
import java.sql.SQLException ;
import java.text.MessageFormat ;

import org.apache.axis.client.Call ;
// import org.apache.axis.client.Service ;
// import org.apache.axis.message.SOAPBodyElement;
// import org.apache.axis.utils.XMLUtils;
import java.net.URL;


public class JobImpl extends Job {

	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobImpl.class ) ;
		
	private static final String
	    ASTROGRIDERROR_COULD_NOT_CREATE_JOB_CONNECTION            = "AGDTCE00160" ,  
	    ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT = "AGDTCE00180" ,
        ASTROGRIDERROR_AXIS_FAULT_WHEN_INVOKING_JOBMONITOR        = "" ;
	    
	private static final String
        JOB_MONITOR_REQUEST_TEMPLATE = "JOB.MONITOR.REQUEST_TEMPLATE" ;
	  
	private static JobFactoryImpl
	   factoryImpl = null ;
	   
	private Connection
		connection = null ;
	private PreparedStatement 
		preparedStatement = null ;  
	private ResultSet 
		resultSet = null ;	   
			
	private boolean
	   dirty = false ;
	
	private String
	   status = "",
	   jobURN = "",
	   name = "",
	   community = "",
	   userId = "",
	   jobMonitorURL = "",
	   comment = "";
	   
	private Date
	   date ;
	   
	private JobStep
	   jobStep ;

	  
	public JobImpl() {}
	 
	   
	public JobImpl( Element jobElement ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "JobImpl(): entry") ;  
		 	   
		try {

			date = new Date() ;
			name = jobElement.getAttribute( RunJobRequestDD.JOB_NAME_ATTR ).trim() ;
			jobURN = jobElement.getAttribute( RunJobRequestDD.JOB_URN_ATTR ).trim() ;
			jobMonitorURL = jobElement.getAttribute( RunJobRequestDD.JOB_MONITOR_URL_ATTR ).trim() ;
			
			NodeList
			   nodeList = jobElement.getChildNodes() ;
			Element
			   element ;
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {
				
				if ( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {		
					element = (Element) nodeList.item(i) ;
				 				 	
				 	if(element.getTagName().equals( RunJobRequestDD.USERID_ELEMENT ) ) {
					    //userId = element.getNodeValue().trim() ;
					    userId = element.getFirstChild().toString().trim();
					}
					
					else if( element.getTagName().equals( RunJobRequestDD.COMMUNITY_ELEMENT ) ) {
						//community = element.getNodeValue().trim() ;
						community = element.getFirstChild().toString().trim();
					}
					
					else if( element.getTagName().equals( RunJobRequestDD.JOBSTEP_ELEMENT ) ) {
				    	jobStep = new JobStep( element ) ; 
					} 
					 
				 } // end if
				 
			} // end for
			
			this.setStatus( Job.STATUS_INITIALIZED ) ;		
			
		}
		catch( Exception ex ) {		
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			this.setComment( message.toString() ) ;
			this.setStatus( Job.STATUS_IN_ERROR ) ;	
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
	

	/* (non-Javadoc)
	 * @see org.astrogrid.datacenter.Job#informJobMonitor()
	 */
	public void informJobMonitor() {       
		if( TRACE_ENABLED ) logger.debug( "informJobMonitor(): entry") ;	
		
		try {
			String
			   requestTemplate = DatasetAgent.getProperty( JOB_MONITOR_REQUEST_TEMPLATE ) ;
			Object []
			   inserts = new Object[8] ;
			inserts[0] = this.getName() ;
			inserts[1] = this.getUserId() ;
			inserts[2] = this.getCommunity() ;
			inserts[3] = this.getId() ;
			inserts[4] = this.getDate() ;
			inserts[5] = this.getJobStep().getName() ;
			inserts[6] = this.getJobStep().getStepNumber() ;
			inserts[7] = this.getStatus() ;
			Object []
			   parms = new Object[] { MessageFormat.format( requestTemplate, inserts ) } ;
					
			org.apache.axis.client.Call
			   call = new org.apache.axis.client.Service().createCall() ; 

			call.setTargetEndpointAddress( new URL( this.getJobMonitorURL() ) ) ;
			call.setProperty( Call.SOAPACTION_USE_PROPERTY, Boolean.TRUE ) ;
			call.setProperty( Call.SOAPACTION_URI_PROPERTY, "getQuote" ) ;
			call.setProperty( Call.ENCODINGSTYLE_URI_PROPERTY, "http://schemas.xmlsoap.org/soap/encoding/" ) ;
			call.setOperationName( new QName( "urn:xmltoday-delayed-quotes", "getQuote") ) ;
			call.addParameter( "symbol", XMLType.XSD_STRING, ParameterMode.IN ) ;
			call.setReturnType( XMLType.XSD_STRING ) ;
//			call.setProperty(Call.USERNAME_PROPERTY, opts.getUser());
//			call.setProperty(Call.PASSWORD_PROPERTY, opts.getPassword());

			String 
				result = (String) call.invoke( parms ) ;

		}
		catch ( AxisFault af ) {
			Message
				message = new Message( ASTROGRIDERROR_AXIS_FAULT_WHEN_INVOKING_JOBMONITOR, af ) ; 
			logger.error( message.toString(), af ) ;
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "informJobMonitor(): exit") ;	
		}
			
	} // end of informJobMonitor() 
	

	public Connection getConnection() throws JobException {
		if( TRACE_ENABLED ) logger.debug( "getConnection(): entry") ; 
		
		try{
			if( connection == null ) {
				connection = JobFactoryImpl.getDataSource().getConnection() ;
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


	public PreparedStatement getPreparedStatement() throws JobException, SQLException {
		if( TRACE_ENABLED ) logger.debug( "getPreparedStatement(): entry") ; 
		
		try { 			   
		
			if( preparedStatement == null ) {			
			   Object[]
				  inserts = new Object[1] ;
			   inserts[0] = DatasetAgent.getProperty( JobFactoryImpl.JOB_TABLENAME ) ;
			   String
				  updateString = MessageFormat.format( JobFactoryImpl.UPDATE_TEMPLATE, inserts ) ; 
			   preparedStatement = getConnection().prepareStatement( updateString ) ;		
			}
		    
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "getPreparedStatement(): exit") ; 	
		}
		
		return preparedStatement ;
		
	}// end of getPreparedStatement()


	/* (non-Javadoc)
	 * @see org.astrogrid.datacenter.Job#getStatus()
	 */
	public String getStatus() {
		return this.status;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.datacenter.Job#setStatus(java.lang.String)
	 */
	public void setStatus(String status) {
		if( this.status != null )
		    dirty = true ;
        this.status = status ;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.datacenter.Job#getId()
	 */
	public String getId() {	return jobURN ;	}
	public void setId( String jobURN ) { this.jobURN = jobURN ;}
	
	public void setDirty( boolean dirty ) { this.dirty = dirty ; }
	public boolean isDirty() {return this.dirty ; }

	public void setName( String name ) { this.name = name; }
	public String getName() { return this.name; }
	
	public Date getDate() { return this.date ; }
	public void setDate( Date date ) { this.date = date ; }

	public void setCommunity( String community ) { this.community = community; }
	public String getCommunity() { return community; }

	public void setUserId( String userId ) { this.userId = userId; }
	public String getUserId() { return userId; } 

	public void setJobMonitorURL( String jobMonitorURL ) { this.jobMonitorURL = jobMonitorURL; }
	public String getJobMonitorURL() { return jobMonitorURL; }

	public void setJobStep( JobStep jobStep ) { this.jobStep = jobStep; }
	public JobStep getJobStep() { return jobStep; }
	
	public String getComment() { return comment ; }
	public void setComment( String comment ) { this.comment = comment ; }
	
	public Object getImplementation() { return this ; }

	public void setFactoryImpl(JobFactoryImpl factoryImpl) { this.factoryImpl = factoryImpl ; }
	public JobFactoryImpl getFactoryImpl() { return factoryImpl ; }

} // end of class JobImpl
