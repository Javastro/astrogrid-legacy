/*
 * @(#)From.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.query;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.datasetagent.*;
import org.astrogrid.datacenter.i18n.*;
import org.w3c.dom.* ;

import java.util.ArrayList ;
import java.util.List ;
import java.util.Iterator ;

/**
 * The <code>From</code> class represents ...
 * <p>
 * Introductory text.... For example:
 * <p><blockquote><pre>
 *     
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class From {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( From.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_CREATE_FROM_FROM_ELEMENT = "AGDTCE00200",
	    ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_FROM = "AGDTCE00210" ;
	    
	private static final String
	/** Property file key for the job database JNDI location */    			
	    QUERY_FROM_SEPARATOR = "QUERY.FROM.SEPARATOR" ;	    
	    		
	private List
	   catalogs = new ArrayList() ;
	   
	public From( Element fromElement ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "From(Element): entry") ;
		   		
		try {

			NodeList
			   nodeList = fromElement.getChildNodes() ;
			Element
			    catalogElement ;
						   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {	
				if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
					continue ;							
				catalogElement = (Element) nodeList.item(i) ;				
				if( catalogElement.getTagName().equals( RunJobRequestDD.CATALOG_ELEMENT ) ) {
					this.addCatalog( new Catalog( catalogElement ) ) ;
				}
				
			} // end for		
			
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_FROM_FROM_ELEMENT ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "From(Element): exit") ;   	
		}
		   
	} // end of From( Element )
	
/**
 * Returns a String containing catalogue and table details.
 * If the table element is empty then assume owner and table name are the same
 * and the return is of the form: catalogue..catalogue.
 * If there is one table element then the return is of the form: catalogue..table
 * If there are a number of tables then the return will be a comma seperated list in the form:
 * catalogue..table1, catalogue..table2, catalogue..table3 
 */
	public String toSQLString() {
		
		StringBuffer
			buffer = new StringBuffer(64) ;
		
    	try {

            Iterator
                iterator = this.getCatalogs() ;
        	    
        	while( iterator.hasNext() ) {
        		
        		Catalog
        		    catalog = (Catalog)iterator.next() ;
        		
        		Table 
        	        table = null ;
        	
        		if ( catalog.getNumberTables() <= 0 ) {  // no table specified assume owner and table name are same
        			buffer.append( catalog.getName() );
        			buffer.append( DatasetAgent.getProperty( QUERY_FROM_SEPARATOR ) ); //Bug #15
        			buffer.append( catalog.getName() );
        			buffer.append( ", " );
        		}
        	
        		else {
        			
        			Iterator
        			    iterator2 = catalog.getTables() ;
        		
			    	while( iterator2.hasNext() ) {
			    		
			    		table = (Table)iterator2.next() ;
			    		
					    buffer.append( catalog.getName() );
				    	buffer.append(  DatasetAgent.getProperty( QUERY_FROM_SEPARATOR ) ); //Bug #15
				        buffer.append( table.getName() );				
				        buffer.append( ", " );
			           		
					} // end of inner while		
        		}
    		} // end of outer while
    		
			buffer.deleteCharAt(buffer.length()-2); // remove final ", "
			
    	}
		catch( Exception ex) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_FROM ) ;
			logger.error( message.toString(), ex ) ;   		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Return(Element): exit") ;   	
		}
		
		return buffer.toString() ;
    	
	} // end of toSQLString()
		
    public Iterator getCatalogs() { return this.catalogs.iterator() ; }
    public boolean addCatalog( Catalog catalog ) { return catalogs.add( catalog ); }
    public Catalog removeCatalog( int index ) { return (Catalog)catalogs.remove( index ) ; }
	public Catalog getCatalog( int index ) { return (Catalog)this.catalogs.get( index ) ; }
			   

} // end of class From 
