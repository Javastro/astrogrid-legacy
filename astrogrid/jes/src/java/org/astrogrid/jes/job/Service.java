/*
 * @(#)Service.java	1.2 
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.jes.job;

import org.apache.log4j.Logger;
import org.astrogrid.jes.i18n.*;
import org.astrogrid.jes.jobcontroller.*;
import org.w3c.dom.* ;

/**
 * The <code>Service</code> class represents ...
 * <p>
 * Introductory text.... For example:
 * <p><blockquote><pre>
 *     
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.jes.Query
 * @since   AstroGrid 1.2
 */
public class Service {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Service.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_dosomething = "AGJESE00???" ;
	
	private String 
	   name,
	   url ;
	   
	private Catalog
	   parent ;
	   
	public Service( Catalog parent, Element serviceElement ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "Service(Element): entry") ;  
		 	
		setParent(parent) ;
		 		
		try {
			setName(serviceElement.getAttribute( SubmissionRequestDD.SERVICE_NAME_ATTR )) ;
			setUrl(serviceElement.getAttribute( SubmissionRequestDD.SERVICE_URL_ATTR )) ;			
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_dosomething ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Service(Element): exit") ;   	
		}
		   
	} // end of Service( Element )


	public void setName(String name) { this.name = name; }
	public String getName() { return name; }

	public void setUrl(String url) { this.url = url; }
	public String getUrl() { return url; }

	public void setParent(Catalog parent) {	this.parent = parent; }
	public Catalog getParent() { return parent; }	   

} // end of class Service 
