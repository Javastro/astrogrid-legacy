/*
 * @(#)Operation_CONE.java   1.0
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
 * The <code>Operation_CONE</code> class represents operation within an 
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
public class Operation_CONE extends Operation {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Operation_CONE.class ) ;
		
	public static final String
//		TEMPLATE 		= "(DEGREES(ACOS(SIN(RADIANS(DEC)) * SIN(RADIANS( {0} )) + COS(RADIANS(DEC)) * COS(RADIANS( {0})) " +
//		                  "COS(RADIANS( {1} ))) < {2} )))" ,
		TEMPLATE = "( ((2 * ASIN(SQRT(POW(SIN({1}-UDEC)/2),2) + COS(UDEC) + POW(SIN({0}-URA)/2),2)) < {2} )" ;	
			
	private Field
	   fieldRA,
	   fieldDEC,
	   fieldRADIUS ;
	
	   
	public Operation_CONE( Element opElement , Catalog catalog ) throws QueryException {
		super( opElement, catalog ) ;
	}
	
	
	public String toSQLString() {
		if( TRACE_ENABLED ) logger.debug( "Operation_CONE.toSQLString(): entry") ; 
		
		String
		   retValue = null ; 	
		
		Object []
           inserts = new Object[3] ;   // Only three operands are allowed for a cone search
        
        try {   
           inserts[0] = fieldRA.toSQLString() ;
           inserts[1] = fieldDEC.toSQLString() ;
		   inserts[2] = fieldRADIUS.toSQLString() ;
           retValue = MessageFormat.format( this.getTemplate(), inserts ) ;
        }
        finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_CONE.toSQLString(): exit") ;         	        
        }
        
		return retValue ;
		
	} // end of toSQLString()


    public void push( Operand operand ) {
		if( TRACE_ENABLED ) logger.debug( "Operation_CONE.push(): entry") ;  
		
		try {
			
			// JBL Note: this should be an assert, but for some reason I cannot get it
			// past the syntax checker.  
			if( operand instanceof Field == false ) {
                logger.debug( "Operand is not an instance of Field") ;
			}
			
			Field
			   field = (Field)operand ;
			   
            if( field.getName().equals( "RA" ) ) {
                fieldRA = field ;
            }
            else if( field.getName().equals( "DEC" ) ) {
			    fieldDEC = field ;
			}
			else if( field.getName().equals( "RADIUS" ) ) {
				fieldRADIUS = field ;
			}
			else {
				; // a serious error  has occurred
			}
			
		} finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_CONE.push(): exit") ; 
		}
    	
    } // end of push()

	
	public String getTemplate() { return TEMPLATE ; }

	
} // end of class Operation_CONE