/*
 * @(#)Service.java	1.2 
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;
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
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Service {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Service.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_dosomething = "AGDTCE00???" ;
	
	private String 
	   name,
	   url ;
	   
	public Service( Element serviceElement ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Service(Element): entry") ;  
		 		
		try {
			setName(serviceElement.getAttribute( JobDocDescriptor.SERVICE_NAME_ATTR )) ;
			setUrl(serviceElement.getAttribute( JobDocDescriptor.SERVICE_URL_ATTR )) ;			
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_dosomething ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Service(Element): exit") ;   	
		}
		   
	} // end of Service( Element )


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}	   

} // end of class Service 
