/*
 * @(#)Catalog.java   1.0
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
import org.astrogrid.jes.i18n.*;
import org.astrogrid.jes.jobcontroller.*;
import org.w3c.dom.* ;
import java.util.Set ;
import java.util.HashSet;
import java.util.Iterator ;

/**
 * The <code>Catalog</code> class represents operations within an 
 * SQL query string.
 * <p>
 * Some example text. For example:
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
public class Catalog {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Catalog.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_CRITERIA = "AGJESE00600" ;
		
    private String
        name ;
		
	private Set
	    tables = new HashSet() ;
	    
	private Set
	    services = new HashSet() ;
	       
	private Query
	    parent ;
	    
	    
	public Catalog( Query parent ) {
		setParent( parent ) ;
	}
	    
	    
	public Catalog( Query parent, Element catalogElement ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "Catalog(Element): entry") ;
		   
		setParent(parent) ;
		   		
		try {
			
			setName(catalogElement.getAttribute( SubmissionRequestDD.CATALOG_NAME_ATTR )) ;
			   
			Element
				element ;
				
			NodeList
			   nodeList = catalogElement.getElementsByTagName( SubmissionRequestDD.TABLE_ELEMENT ) ;
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {
				
				if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {				
				
				    element = (Element) nodeList.item(i) ;
				
				    if( element.getTagName().equals( SubmissionRequestDD.TABLE_ELEMENT ) ) {
                        tables.add( new Table( this, element ) ) ;
				    }

				}

				
			} // end for		
			
			nodeList = catalogElement.getElementsByTagName( SubmissionRequestDD.SERVICE_ELEMENT ) ;

			for( int i=0 ; i < nodeList.getLength() ; i++ ) {
				
				if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {				
				
					element = (Element) nodeList.item(i) ;
				
					if( element.getTagName().equals( SubmissionRequestDD.SERVICE_ELEMENT ) ) {
						services.add( new Service( this, element ) ) ;
					}

				}

				
			} // end for				   
	
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_CRITERIA ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Catalog(Element): exit") ;   	
		}
		
	} // end of Catalog( Element )


	public void setName(String name) { this.name = name.trim(); }
	public String getName() { return name.trim(); }

	public Iterator getTables() { return tables.iterator() ; }
	public boolean addTable( Table table ) { return tables.add( table ) ; }
	public boolean removeTable( Table table ) { return tables.remove( table ) ; }
	
	public Iterator getServices() { return services.iterator() ; }
	public boolean addService( Service service ) { return services.add( service ) ; }
	public boolean removeService( Service service ) { return services.remove( service ) ; }

	public void setParent(Query parent) { this.parent = parent; }
	public Query getParent() { return parent; }	

	
} // end of class Catalog
