/*
 * @(#)Operation_NOT_NULL.java   1.0
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
// import org.astrogrid.datacenter.datasetagent.*;
// import org.astrogrid.datacenter.i18n.*;
import org.w3c.dom.* ;

import java.text.MessageFormat ;


/**
 * The <code>Operation_NOT_NULL</code> class represents operation within an 
 * SQL query string.
 * <p>
 * Some example text. For example:
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
public class Operation_NOT_NULL extends Operation {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Operation_NOT_NULL.class ) ;
		
	// Template for the SQL NOT NULL query   
	public static final String
		TEMPLATE = "( {0} NOT NULL )" ;
			
	private Operand
	   operandSingleton ;
	
	   
	public Operation_NOT_NULL( Element opElement , Catalog catalog ) throws QueryException {
		super( opElement, catalog ) ;
	}
	
	
	public String toSQLString() {
		if( TRACE_ENABLED ) logger.debug( "Operation_NOT_NULL.toSQLString(): entry") ;  
		 	
		String
		   retValue = null ; 	
		
		Object []
           inserts = new Object[1] ;   
        
        try {   
           inserts[0] = operandSingleton.toSQLString() ;
           retValue = MessageFormat.format( this.getTemplate(), inserts ) ;
        }
        finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_NOT_NULL.toSQLString(): exit") ;         	        
        }
        
		return retValue ;
		
	} // end of toSQLString()


    public void push( Operand operand ) {
		if( TRACE_ENABLED ) logger.debug( "Operation_NOT_NULL.push(): entry") ;  
		
		try {
			
			if(  (operandSingleton == null)   &&   (operand instanceof Field) ){
				operandSingleton = operand ;
			}
			else {
				; // a serious error  has occurred
			}
			
		} finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_NOT_NULL.push(): exit") ; 
		}

    	
    } // end of push()

	
	public String getTemplate() { return TEMPLATE ; }

	
} // end of class Operation_NOT_NULL