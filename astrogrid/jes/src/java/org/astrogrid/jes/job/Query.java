/*
 * @(#)Query.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.jes.job; 

import org.apache.log4j.Logger;
import org.astrogrid.jes.i18n.*;
import org.astrogrid.jes.jobcontroller.*;
import org.w3c.dom.* ;
import java.util.Set ;
import java.util.HashSet ;
import java.util.Iterator ;

public class Query {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Query.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_dosomething = "AGJESE00???" ;
        
	private Set
		catalogs = new HashSet() ;
		
    private JobStep
        parent ;
	
	
	public Query( JobStep parent, Element queryElement ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "Query(Element): entry") ;
		
		setParent(parent) ;
		
		try {

			NodeList
			   nodeList = queryElement.getChildNodes() ;
			Element
				element ;
						   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {	
				
				if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
										
				    element = (Element) nodeList.item(i) ;
				
                    if( element.getTagName().equals( SubmissionRequestDD.FROM_ELEMENT ) ) {
	                     fixupCatalogs( element ) ;
				    }
				
				}
				
			} // end for		
			
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_dosomething ) ;
			logger.error( message.toString(), ex ) ;
			throw new JobException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Query(Element): exit") ;   	
		}
		
	} // end of Query() 
	
	
	private void fixupCatalogs( Element fromElement ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "fixupCatalog: entry") ;
		
		try {

			NodeList
			   nodeList = fromElement.getChildNodes() ;
			Element
				element ;  
						   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {	
				
				if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
										
					element = (Element) nodeList.item(i) ;
				
					if( element.getTagName().equals( SubmissionRequestDD.CATALOG_ELEMENT ) ) {
						 catalogs.add( new Catalog( this, element ) ) ;
					}
				
				}
				
			} // end for		
				
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "fixupCatalog: exit") ;   	
		}
		
	} // end of fixupCatalog()


	public void setParent(JobStep parent) {	this.parent = parent; }
	public JobStep getParent() { return parent; }

	public Iterator getCatalogs() { return catalogs.iterator() ; }
	public boolean addCatalog( Catalog catalog ) { return catalogs.add( catalog ) ; }
	public boolean removeCatalog( Catalog catalog ) { return catalogs.remove( catalog ); }
	

} // end of class Query

