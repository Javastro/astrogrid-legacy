/*
 * @(#)JobImpl.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */
package org.astrogrid.jes.impl;

import org.astrogrid.jes.job.Job ;
import org.astrogrid.jes.job.JobStep ;
import org.astrogrid.jes.job.JobDocDescriptor ;
import org.astrogrid.jes.job.JobException ;
import org.astrogrid.jes.i18n.*;
import org.apache.log4j.Logger;

import org.w3c.dom.* ;
import java.util.Date ;
import java.util.Set;
import java.util.HashSet ;
import java.util.Iterator ;


public class JobImpl extends Job {

	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobImpl.class ) ;
		
	private static String
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
	   
	private Set
	   jobSteps = new HashSet() ;

	  
	public JobImpl() {}
	 
	   
	public JobImpl( Document submitDoc, String submitXML ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "JobImpl(): entry") ;  
		 	   
		try {
			
			this.documentXML = submitXML ;
			date = new Date() ;
			
			Element
			   element = null ;			   
			NodeList
			   nodeList = submitDoc.getChildNodes() ;  
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {
				
				if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
					
					element = (Element) nodeList.item(i) ;
					
					if ( element.getTagName().equals( JobDocDescriptor.JOB_ELEMENT ) ) {
						name = element.getAttribute( JobDocDescriptor.JOB_NAME_ATTR ).trim() ;
					}
					
				} // end if
								
			} // end for		

			nodeList = element.getChildNodes() ;  			
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {
							
				if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
					
					element = (Element) nodeList.item(i) ;
					
				    if( element.getTagName().equals( JobDocDescriptor.USERID_ELEMENT ) ) {
					    userId = element.getNodeValue().trim() ;
				    }
				    else if( element.getTagName().equals( JobDocDescriptor.COMMUNITY_ELEMENT ) ) {
					    community = element.getNodeValue().trim() ;
				    }
				    else if( element.getTagName().equals( JobDocDescriptor.JOBSTEP_ELEMENT ) ) {
				        jobSteps.add( new JobStep( this, element ) ) ;   
				    }
				
				} // end if
				
			} // end for
			
		}
		catch( Exception ex ) {		
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
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

/*
	public PreparedStatement getPreparedStatement() throws JobException, SQLException {
		if( TRACE_ENABLED ) logger.debug( "getPreparedStatement(): entry") ; 
		
		try { 			   
		
			if( preparedStatement == null ) {			
			   Object[]
				  inserts = new Object[1] ;
			   inserts[0] = JobController.getProperty( JobFactoryImpl.JOB_TABLENAME ) ;
			   String
				  updateString = MessageFormat.format( JobFactoryImpl.UPDATE_TEMPLATE, inserts ) ; 
			   preparedStatement = factoryImpl.getConnection().prepareStatement( updateString ) ;		
			}
		    
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "getPreparedStatement(): exit") ; 	
		}
		
		return preparedStatement ;
		
	}// end of getPreparedStatement()
*/


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
	
	public Iterator getJobSteps() { return this.jobSteps.iterator() ; }
	
	public Object getImplementation() { return this ; }

	public void setFactoryImpl(JobFactoryImpl factoryImpl) { this.factoryImpl = factoryImpl ; }
	public JobFactoryImpl getFactoryImpl() { return factoryImpl ; }

	public void setDocumentXML( String submitDoc ) { this.documentXML = submitDoc ; }
	public String getDocumentXML() { return documentXML ; }

	public void setStatus(String status) { this.status = status; }
	public String getStatus() {	return status; }

} // end of class JobImpl
