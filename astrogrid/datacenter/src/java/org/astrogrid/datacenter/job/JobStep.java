/*
 * @(#)JobStep.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.job;

import org.w3c.dom.* ;
import org.apache.log4j.Logger;
import org.astrogrid.datacenter.datasetagent.*;
// import org.astrogrid.datacenter.i18n.*;


public class JobStep {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( JobStep.class ) ;
	
	private static final String
	    QUERYFACTORY_KEY_SUFFIX = ".QUERYFACTORY" ;
	
	private String
	    name = null,
        stepNumber = null ;

	private Query
	    query = null ;
	    
	public JobStep( Element element ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "JobStep(): entry") ; 
		
		try {
		 
		   name = element.getAttribute( RunJobRequestDD.JOBSTEP_NAME_ATTR ).trim() ;
		   stepNumber = element.getAttribute( RunJobRequestDD.JOBSTEP_STEPNUMBER_ATTR ).trim() ;
		   
		   NodeList
		      nodeList = element.getChildNodes() ;
		   Element
		      queryChild = null,
		      catalogChild = null ;
			   
		   for( int i=0 ; i < nodeList.getLength() ; i++ ) {
		   	   if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
		   	       continue ;				
			   queryChild = (Element) nodeList.item(i) ;
			   if( queryChild.getTagName().equals( RunJobRequestDD.QUERY_ELEMENT ) ) 
			       break ;
		   }
		
		   nodeList = queryChild.getElementsByTagName( RunJobRequestDD.CATALOG_ELEMENT ) ;
		
		   for( int i=0 ; i < nodeList.getLength() ; i++ ) {
			   if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
			       continue ;								
			   catalogChild = (Element) nodeList.item(i) ;
			   if( catalogChild.getTagName().equals( RunJobRequestDD.CATALOG_ELEMENT ) ) 
				   break ;
		   }	
		
		   String
		       keyToFactory = catalogChild.getAttribute( RunJobRequestDD.CATALOG_NAME_ATTR ) + Query.QUERYFACTORY_KEY_SUFFIX ;		
		 
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

	public void setStepNumber(String stepNumber) { this.stepNumber = stepNumber ; }
	public String getStepNumber() { return stepNumber; }
	
	
} // end of class JobStep
