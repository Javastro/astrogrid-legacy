/*
 * @(#)Query.java   1.0
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
import java.util.Set ;
import java.util.HashSet ;
import java.util.Iterator ;

public class Query {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Query.class ) ;
        
    private final static String 
        SUBCOMPONENT_NAME = Configurator.getClassName( Job.class );                          
		
	private static final String
        ASTROGRIDERROR_COULD_NOT_CREATE_QUERY = "AGJESE00610" ;
        
	private Set
		catalogs = new HashSet() ;
		
    private JobStep
        parent ;
	
	
	public Query( JobStep parent ) {
		setParent( parent ) ;
	}
	
	
	public Query( JobStep parent, Element queryElement ) throws JobException {
		if( TRACE_ENABLED ) logger.debug( "Query(Element): entry") ;
		
		setParent(parent) ;
		
		try {
            
            String
                type = queryElement.getAttribute( SubmissionRequestDD.QUERY_TYPE_ATTR ),
                target = queryElement.getAttribute( SubmissionRequestDD.QUERY_TARGET_ATTR ) ; 
                
            if( type == SubmissionRequestDD.QUERY_TYPE_ADQL) {
                fixupCatalogsForADQL( target ) ;
                return ;
            }

			NodeList
			   nodeList = queryElement.getChildNodes() ;
			Element
				element ;
						   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {	
				
				if( nodeList.item(i).getNodeType() == Node.ELEMENT_NODE ) {
										
				    element = (Element) nodeList.item(i) ;
				
                    if( element.getTagName().equals( SubmissionRequestDD.FROM_ELEMENT ) ) {
	                    fixupCatalogs( element ) ;
                        break ;
				    }
				
				}
				
			} // end for		
			
		}
		catch( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_QUERY
                                              , SUBCOMPONENT_NAME ) ;
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


    private void fixupCatalogsForADQL( String target ) throws JobException {
        if( TRACE_ENABLED ) logger.debug( "fixupCatalogsForADQL: entry") ;
        
        try {
     
            logger.debug( "target: " + target ) ;
            
            Catalog
                catalog = new Catalog( this ) ;
                
            catalog.setName( Catalog.CATALOG_FOR_ADQL_BASED_DATACENTER ) ;
            
            Service
                service = new Service( catalog ) ;
            service.setName( Service.SERVICE_FOR_ADQL_BASED_DATACENTER ) ;
            service.setUrl( target ) ;
            catalog.addService( service ) ;
            
            Table
                table = new Table( catalog ) ;
            table.setName( Table.TABLE_FOR_ADQL_BASED_DATACENTER ) ;
            catalog.addTable( table ) ;
            
            this.catalogs.add( catalog ) ;
            
        }
        finally {
            if( TRACE_ENABLED ) logger.debug( "fixupCatalogsForADQL: exit") ;       
        }
        
    } // end of fixupCatalogsForADQL()


	public void setParent(JobStep parent) {	this.parent = parent; }
	public JobStep getParent() { return parent; }

	public Iterator getCatalogs() { return catalogs.iterator() ; }
	public boolean addCatalog( Catalog catalog ) { return catalogs.add( catalog ) ; }
	public boolean removeCatalog( Catalog catalog ) { return catalogs.remove( catalog ); }
	

} // end of class Query

