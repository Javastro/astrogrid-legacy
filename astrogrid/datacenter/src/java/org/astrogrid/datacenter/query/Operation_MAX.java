/*
 * @(#)Operation_MAX.java   1.0
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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.astrogrid.Configurator ;


/**
 * The <code>Operation_MAX</code> class represents operation within a 
 * SQL query string.
 * <p>
 * Some example text. For example:
 * <p><blockquote><pre>
 *     OR
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Operation_MAX extends Operation {
	
	private static final boolean 
		TRACE_ENABLED = true ;

		private static final String
				SUBCOMPONENT_NAME = Configurator.getClassName( Operation_MAX.class ) ;        
	
	private static Logger 
		logger = Logger.getLogger( Operation_MAX.class ) ;
		
	// Template for the SQL MAX
	public static final String
		TEMPLATE = " MAX({0}) "  ;
			
	private List
		operands ;
	   
	public Operation_MAX( Element opElement , Catalog catalog ) throws QueryException {
		super( opElement, catalog ) ;
	}
	
	
	public String toSQLString() {
		if( TRACE_ENABLED ) logger.debug( "Operation_MAX.toSQLString(): entry") ;  
		 	
		String
			retValue = null ; 	
		
		Object []
			insert = new Object[1] ;   
        
		try {  

			insert[0] = ((Operand)operands.get( 0 )).toSQLString() ;
			retValue = MessageFormat.format( this.getTemplate(), insert ) ;

		}
		finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_MAX.toSQLString(): exit") ;         	        
		}
        
		return retValue ;
		
	} // end of toSQLString()


	public void push( Operand operand ) {
		if( TRACE_ENABLED ) logger.debug( "Operation_MAX.push(): entry") ;  
		
		try {
			
			if( operands == null ) operands = new ArrayList() ;
			operands.add( operand ) ;
			
		} finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_MAX.push(): exit") ; 
		}

    	
		} // end of push()
	
	public String getTemplate() { return TEMPLATE ; }

	
} // end of class Operation_MAX