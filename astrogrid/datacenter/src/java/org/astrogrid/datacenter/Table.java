/*
 * @(#)Table.java	1.0 
 *
 * AstroGrid Copyright notice.
 * 
 */

package org.astrogrid.datacenter;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.i18n.*;
import org.w3c.dom.* ;

/**
 * The <code>Table</code> class represents ...
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
public class Table {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Table.class ) ;
		
	private static final String
		ASTROGRIDERROR_COULD_NOT_dosomething = "AGDTCE00???" ;
	
	private String 
	   name ;
	   
	public Table( Element tableElement ) throws QueryException {
		if( TRACE_ENABLED ) logger.debug( "Table(Element): entry") ;   
				
		try {
			setName(tableElement.getFirstChild().toString()) ;			
		}
		catch( Exception ex ) {
			Message
				message = new Message( ASTROGRIDERROR_COULD_NOT_dosomething ) ;
			logger.error( message.toString(), ex ) ;
			throw new QueryException( message, ex );    		
		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Table(Element): exit") ;   	
		}
		   
	} // end of Table( Element )


	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}	   

} // end of class Table 
