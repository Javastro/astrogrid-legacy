/*
 * @(#)Operation_DESC.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.query;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.astrogrid.Configurator;

/**
 * The <code>Operation_DESC</code> class represents operations within an 
 * SQL query string.
 * <p>
 * Some example text. For example:
 * <p><blockquote><pre>
 *     SELECT COLUMN_ONE, COLUMN_TWO, COLUMN_THREE 
 *       FROM USNOB
 *     WHERE (COLUMN_FOUR > COLUMN_FIVE )
 *     ORDER BY COLUMN_ONE DESC, COLUMN_TWO DESC,COLUMN_THREE DESC
 * </pre></blockquote>
 * <p>
 *
 * @author  Phill Nicolson
 * @author  Jeff Lusted
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
 
public class Operation_ORDER_BY_DESC extends Operation {
	 
	private static final boolean 
		TRACE_ENABLED = true ;
	
	public final static String
			SUBCOMPONENT_NAME = Configurator.getClassName( Operation_ORDER_BY_DESC.class ) ;
			
	private static Logger 
		logger = Logger.getLogger( Operation_ORDER_BY_DESC.class ) ;

	private Field
		 field ;		
	
	// Template for the SQL DESC query - the 'ORDER BY' part is included in the Query class
	// so that multiple elements are not a problem.   
	public static final String
		TEMPLATE = " {0} DESC " ;
		
	public Operation_ORDER_BY_DESC( Element opElement , Catalog catalog ) throws QueryException {	
		super( opElement, catalog ) ; 
	}

	public String toSQLString() {
		if( TRACE_ENABLED ) logger.debug( "Operation_ORDER_BY_DESC.toSQLString(): entry") ; 
		
		String
		    retValue = null ; 	
		
		Object []
		    inserts = new Object[1] ;
        
		try {   
		    inserts[0] = field.toSQLString() ;
			retValue = MessageFormat.format( this.getTemplate(), inserts ) ;
		}
		finally {
		    if( TRACE_ENABLED ) logger.debug( "Operation_ORDER_BY_DESC.toSQLString(): exit") ;         	        
		}
        
		return retValue ;
		
	} // end of toSQLString()

		public void push( Operand operand ) {
		if( TRACE_ENABLED ) logger.debug( "Operation_ORDER_BY_DESC.push(): entry") ;  
		
		try {
			  
			if( operand instanceof Field == false ) {
			    logger.debug( "Operand is not an instance of Field") ;
			}
			
			Field
			    field = (Field)operand ;
			
			this.field = field;			    
			    
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_ORDER_BY_DESC.push(): exit") ; 
		}
    	
	} // end of push()
		
	public String getTemplate() { return TEMPLATE ; }
	public Field getField() { return this.field ; }

} // end of class Operation_DESC