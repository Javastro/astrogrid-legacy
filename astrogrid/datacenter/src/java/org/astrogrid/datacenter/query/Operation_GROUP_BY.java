/*
 * @(#)Operation_GROUP_BY.java   1.0
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
import org.astrogrid.datacenter.Util;
import org.w3c.dom.Element;

/**
 * The <code>Operation_GROUP_BY</code> class represents operations within an 
 * SQL query string.
 * <p>
 * The GROUP BY clause takes a table derived from the <from clause>, and the
 * <where clause> if specified, and produces a 'grouped' table.
 * <p>
 * For example:
 * <p><blockquote><pre>
 * SELECT *
 *     FROM USNOB
 * GROUP BY COLUMN_ONE, COLUMN_TWO
 * </pre></blockquote>
 * <p>
 *
 * @author  Phill Nicolson
 * @author  Jeff Lusted
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
 
public class Operation_GROUP_BY extends Operation {
	 
	private static final boolean 
		TRACE_ENABLED = true ;
	
	public final static String
			SUBCOMPONENT_NAME =  Util.getComponentName( Operation_GROUP_BY.class ) ;
			
	private static Logger 
		logger = Logger.getLogger( Operation_GROUP_BY.class ) ;		
	
	// TemplateS for the SQL GROUP BY clause   (PJN Note: crude but effective)
	public static final String []
		TEMPLATES   =  { " {0} "
					   , " {0}, {1} "
					   , " {0}, {1}, {2} "
					   , " {0}, {1}, {2}, {3} "
					   , " {0}, {1}, {2}, {3}, {4}"
					   , " {0}, {1}, {2}, {3}, {4}, {5} " 
					   , " {0}, {1}, {2}, {3}, {4}, {5}, {6} "
					   , " {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7} "
					   , " {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8} "
					   , " {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9} "
	                   , " {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10} ) )" } ;
	                   
	private List
	   operands ;	                   
		
	public Operation_GROUP_BY( Element opElement , Catalog catalog ) throws QueryException {	
		super( opElement, catalog ) ; 
	}

	public String toSQLString() {
		if( TRACE_ENABLED ) logger.debug( "Operation_GROUP_BY.toSQLString(): entry") ;  
		 	
		String
		    retValue = null ; 	
		
		Object []
		    inserts = new Object[ operands.size() ] ;   
        
		try {  
        	 
			for( int i = 0; i < operands.size(); i++ ) {
				inserts[i] = ((Operand)operands.get( i )).toSQLString() ;
		}
		
	    retValue = MessageFormat.format( this.getTemplate(), inserts ) ;   
	
	}
	finally {
	    if( TRACE_ENABLED ) logger.debug( "Operation_GROUP_BY.toSQLString(): exit") ;         	        
	}
        
	return retValue ;
		
	} // end of toSQLString()

	public void push( Operand operand ) {
	if( TRACE_ENABLED ) logger.debug( "Operation_GROUP_BY.push(): entry") ;  
		
	try {
			
		if( operands == null ) operands = new ArrayList() ;
		    operands.add( operand ) ;
			
	    } finally {
		    if( TRACE_ENABLED ) logger.debug( "Operation_GROUP_BY.push(): exit") ; 
	    }
    	
	} // end of push()
    	
		
	public String getTemplate() { return TEMPLATES[ operands.size() - 1 ] ; }

} // end of class Operation_GROUP_BY