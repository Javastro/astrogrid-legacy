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
package org.astrogrid.datacenter.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.datasetagent.RunJobRequestDD;
import org.astrogrid.i18n.AstroGridMessage;
import org.astrogrid.Configurator;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


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
 * @author  Jeff Lusted/Phil Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Catalog {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Catalog.class ) ;
		
    private final static String
        SUBCOMPONENT_NAME = Configurator.getClassName( Catalog.class ) ;                        
        
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_CATALOG_FROM_ELEMENT = "AGDTCE00420" ;	
		
    private String
        name ;
		
	private List
	    tables = new ArrayList() ;
	    
	private List
	    services = new ArrayList() ;
	    
	public Catalog( Element catalogElement ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Operation(Element): entry") ;
		   		
		try {
			
			setName(catalogElement.getAttribute( RunJobRequestDD.CATALOG_NAME_ATTR )) ;

			NodeList
			   nodeList = catalogElement.getElementsByTagName( RunJobRequestDD.TABLE_ELEMENT ) ;
			   
			Element
				tableElement,
				serviceElement ;
				
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				tableElement = (Element) nodeList.item(i) ;
				if( tableElement.getTagName().equals( RunJobRequestDD.TABLE_ELEMENT ) ) {
					this.addTable( new Table( tableElement ) ) ;
				}
				
			} // end for		
			
			nodeList = catalogElement.getElementsByTagName( RunJobRequestDD.SERVICE_ELEMENT ) ;
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				serviceElement = (Element) nodeList.item(i) ;
				if( serviceElement.getTagName().equals( RunJobRequestDD.SERVICE_ELEMENT ) ) {
					this.addService( new Service( serviceElement ) ) ;
				}
				
			} // end for
	
		}
		catch( Exception ex ) {
			AstroGridMessage
				message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_CATALOG_FROM_ELEMENT
                                              , SUBCOMPONENT_NAME ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Operation(Element): exit") ;   	
		}
		
	} // end of Catalog( Element )

	public Iterator getTables() { return this.tables.iterator() ; }
	public boolean addTable( Table table ) { return tables.add( table ); }
	public Table removeTable( int index ) { return (Table)tables.remove( index ) ; }
	public Table getTable( int index ) { return (Table)this.tables.get( index ) ; }
	public int getNumberTables() { return this.tables.size() ; }

	public Iterator getServices() { return this.services.iterator() ; }
	public boolean addService( Service Service ) { return services.add( Service ); }
	public Service removeService( int index ) { return (Service)services.remove( index ) ; }
	public Service getService( int index ) { return (Service)this.services.get( index ) ; }
	public int getNumberServices() { return  this.services.size() ; }

	public void setName(String name) { this.name = name; }
	public String getName() { return name; }	

	
} // end of class Catalog
