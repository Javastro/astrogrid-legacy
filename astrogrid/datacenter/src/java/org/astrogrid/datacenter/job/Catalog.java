/*
 * @(#)Catalog.java   1.0
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.datacenter.job;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.datasetagent.*;
import org.astrogrid.datacenter.i18n.*;
import org.w3c.dom.* ;

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
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Catalog {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Catalog.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_dosomething = "AGDTCE00???" ;	
		
    private String
        name ;
		
	private Table []
	    tables ;
	    
	private Service []
	    services ;
	    
	public Catalog( Element catalogElement ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Operation(Element): entry") ;
		   		
		try {
			
			setName(catalogElement.getAttribute( RunJobRequestDD.CATALOG_NAME_ATTR )) ;

			NodeList
			   nodeList = catalogElement.getElementsByTagName( RunJobRequestDD.TABLE_ELEMENT ) ;
			   
			Element
				tableElement,
				serviceElement ;
				
			setTables(new Table[ nodeList.getLength() ]);
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				tableElement = (Element) nodeList.item(i) ;
				if( tableElement.getTagName().equals( RunJobRequestDD.TABLE_ELEMENT ) ) {
					getTables()[i] = new Table( tableElement ) ;
				}
				else  {
					; // JBL Note: What do I do here?
				}
				
			} // end for		
			
			nodeList = catalogElement.getElementsByTagName( RunJobRequestDD.SERVICE_ELEMENT ) ;
			setServices(new Service[ nodeList.getLength() ]);
			   
			for( int i=0 ; i < nodeList.getLength() ; i++ ) {				
				serviceElement = (Element) nodeList.item(i) ;
				if( serviceElement.getTagName().equals( RunJobRequestDD.SERVICE_ELEMENT ) ) {
					getServices()[i] = new Service( serviceElement ) ;
				}
				else  {
					; // JBL Note: What do I do here?
				}
				
			} // end for
	
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_dosomething ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Operation(Element): exit") ;   	
		}
		
	} // end of Catalog( Element )


	public void setTables(Table[] tables) {
		this.tables = tables;
	}

	public Table[] getTables() {
		return tables;
	}

	public void setServices(Service[] services) {
		this.services = services;
	}

	public Service[] getServices() {
		return services;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}	

	
} // end of class Catalog
