/*
 * @(#)JobStep.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.datacenter;

import org.w3c.dom.* ;
import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;



public class JobStep {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobStep.class ) ;
	
	private static final String
	    QUERYFACTORY_KEY_SUFFIX = ".QUERYFACTORY" ;
	
	private String
	    name = null ;
	    
	private Query
	    query = null ;
	    
	public JobStep( Element element ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "JobStep(): entry") ; 
		
		try {
		 
		   name = element.getAttribute( JobDocDescriptor.JOBSTEP_NAME_ATTR ) ;
		
		   NodeList
		      nodeList = element.getChildNodes() ;
		   Element
		      queryChild = null,
		      catalogChild = null ;
			   
		   for( int i=0 ; i < nodeList.getLength() ; i++ ) {
		   	   if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
		   	       continue ;				
			   queryChild = (Element) nodeList.item(i) ;
			   if( queryChild.getTagName().equals( JobDocDescriptor.QUERY_ELEMENT ) ) 
			       break ;
		   }
		
		   nodeList = queryChild.getElementsByTagName( JobDocDescriptor.CATALOG_ELEMENT ) ;
		
		   for( int i=0 ; i < nodeList.getLength() ; i++ ) {
			   if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
			       continue ;								
			   catalogChild = (Element) nodeList.item(i) ;
			   if( catalogChild.getTagName().equals( JobDocDescriptor.CATALOG_ELEMENT ) ) 
				   break ;
		   }	
		
		   String
		       keyToFactory = catalogChild.getAttribute( JobDocDescriptor.CATALOG_NAME_ATTR ) + Query.QUERYFACTORY_KEY_SUFFIX ;		
		 
		   query = Query.getFactory( keyToFactory ).createQuery( queryChild ) ;
		   
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "JobStep(): exit") ; 			 
		}

	} // end of constructor JobStep(Element)
	
	    
	public String getName() { return this.name ; }
	public void setName( String name ) { this.name = name ; }
	
	public Query getQuery() { return this.query ; }
	public void setQuery( Query query ) { this.query = query ; }
	
	
} // end of class JobStep
