/*
 * @(#)Services.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.job;

import org.apache.log4j.Logger;
import org.astrogrid.i18n.*;
import org.astrogrid.jes.jobcontroller.*;
import org.astrogrid.Configurator ;
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
        
    private final static String 
        SUBCOMPONENT_NAME = Configurator.getClassName( Service.class );             
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_SERVICE = "AGJESE00630" ;
	
	private String 
	   name,
	   url ;
	   
	private Catalog
	   parent ;
	   
	
	public Service( Catalog parent ) {
		setParent( parent ) ;
	}
	
	   
	public Service( Catalog parent, Element serviceElement ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "Service(Element): entry") ;  
		 	
		setParent(parent) ;
		 		
		try {
			setName(serviceElement.getAttribute( SubmissionRequestDD.SERVICE_NAME_ATTR )) ;
			setUrl(serviceElement.getAttribute( SubmissionRequestDD.SERVICE_URL_ATTR )) ;			
		}
		catch( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_SERVICE
                                              , SUBCOMPONENT_NAME ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Service(Element): exit") ;   	
		}
		   
	} // end of Service( Element )


	public void setName(String name) { this.name = name.trim(); }
	public String getName() { return name.trim(); }

	public void setUrl(String url) { this.url = url; }
	public String getUrl() { return url; }

	public void setParent(Catalog parent) {	this.parent = parent; }
	public Catalog getParent() { return parent; }	   

} // end of class Service 
