/*
 * @(#)JobImpl.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */
package org.astrogrid.datacenter.impl;

import org.astrogrid.datacenter.Job ;
import org.astrogrid.datacenter.JobStep ;
import org.astrogrid.datacenter.JobDocDescriptor ;
import org.astrogrid.datacenter.JobException ;
import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;
import org.w3c.dom.* ;


public class JobImpl extends Job {

	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobImpl.class ) ;
		
	private static String
	    ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT = "AGDTCE00180" ;
			
	private boolean
	   dirty = false ;
	
	private String
	   status = null,
	   jobURN = null,
	   name = null,
	   community = null,
	   userId = null,
	   jobMonitorURL = null ;
	   
	private JobStep
	   jobStep ;
	   
	   
	public JobImpl( Element jobElement ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "JobImpl(): entry") ;  
		 	   
		try {

			name = jobElement.getAttribute( JobDocDescriptor.JOB_NAME_ATTR ) ;
			jobURN = jobElement.getAttribute( JobDocDescriptor.JOB_URN_ATTR ) ;
			jobMonitorURL = jobElement.getAttribute( JobDocDescriptor.JOB_MONITOR_URL_ATTR ) ;
			
			NodeList
			   nodeList = jobElement.getChildNodes() ;
			Element
			   element ;
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				element = (Element) nodeList.item(i) ;
				if( element.getTagName().equals( JobDocDescriptor.USERID_ELEMENT ) ) {
					userId = element.getNodeValue() ;
				}
				else if( element.getTagName().equals( JobDocDescriptor.COMMUNITY_ELEMENT ) ) {
					community = element.getNodeValue() ;
				}
				else if( element.getTagName().equals( JobDocDescriptor.JOBSTEP_ELEMENT ) ) {
				    jobStep = new JobStep( element ) ;   
				}
				
			} // end for		
			
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_UNABLE_TO_CREATE_JOB_FROM_REQUEST_DOCUMENT ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "JobImpl(): exit") ;   	
		}
		
	} // end of JobImpl()
	

	/* (non-Javadoc)
	 * @see org.astrogrid.datacenter.Job#informJobMonitor(boolean)
	 */
	public void informJobMonitor(boolean bCompletion) {
		// TODO Auto-generated method stub

	}

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
	
	public boolean isDirty() {
		return this.dirty ;
	}

	public void setName( String name ) { this.name = name; }
	public String getName() { return this.name; }

	public void setCommunity( String community ) { this.community = community; }
	public String getCommunity() { return community; }

	public void setUserId( String userId ) { this.userId = userId; }
	public String getUserId() { return userId; } 

	public void setJobMonitorURL( String jobMonitorURL ) { this.jobMonitorURL = jobMonitorURL; }
	public String getJobMonitorURL() { return jobMonitorURL; }

	public void setJobStep( JobStep jobStep ) { this.jobStep = jobStep; }
	public JobStep getJobStep() { return jobStep; }

} // end of class JobImpl
