/*
 * @(#)Operation_IN.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.datacenter.query ;

import java.text.MessageFormat ;
import java.util.ArrayList ;
import java.util.List ;

import org.apache.log4j.Logger ;
import org.w3c.dom.Element ;
import org.astrogrid.Configurator ;


/**
 * The <code>Operation_IN</code> class represents operation within an 
 * SQL query string.
 * <p>
 * The IN predicate tests if a row, defined by a <b>row value constructor</b>, exists
 * in a table defined by a subquery.
 * <p>
 * For example:
 * <p><blockquote><pre>
 *     SELECT COLUMN_ONE, COLUMN_TWO
 *       FROM USNOB..USNOB
 *     WHERE COLUMN_THREE IN
 *          (SELECT COLUMN_FOUR 
 *             FROM USNOB..USNOB 
 *           WHERE COLUMN_FIVE EQUALS COLUMN_SIX)
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Operation_IN extends Operation {
	
	private static final boolean 
		TRACE_ENABLED = true ;
        
    private final static String
         SUBCOMPONENT_NAME = Configurator.getClassName( Operation_IN.class ) ;                                                  
	
	private static Logger 
		logger = Logger.getLogger( Operation_IN.class ) ;
		
	// TemplateS for the SQL IN query   (JBL Note: crude but effective)
	public static final String []
		TEMPLATES   =  { "( {0} IN {1} )"
			           , "( {0} IN ( {1}, {2} ) )"
					   , "( {0} IN ( {1}, {2}, {3} ) )"
					   , "( {0} IN ( {1}, {2}, {3}, {4} ) )"
					   , "( {0} IN ( {1}, {2}, {3}, {4}, {5} ) )"
					   , "( {0} IN ( {1}, {2}, {3}, {4}, {5}, {6} ) )" 
					   , "( {0} IN ( {1}, {2}, {3}, {4}, {5}, {6}, {7} ) )"
					   , "( {0} IN ( {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8} ) )"
					   , "( {0} IN ( {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9} ) )"
					   , "( {0} IN ( {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10} ) )" } ;
			
	private List
	   operands ;
	
	   
	public Operation_IN( Element opElement , Catalog catalog ) throws QueryException {
		super( opElement, catalog ) ;
	}
	
	
	public String toSQLString() {
		if( TRACE_ENABLED ) logger.debug( "Operation_IN.toSQLString(): entry") ;  
		 	
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
			if( TRACE_ENABLED ) logger.debug( "Operation_IN.toSQLString(): exit") ;         	        
        }
        
		return retValue ;
		
	} // end of toSQLString()


    public void push( Operand operand ) {
		if( TRACE_ENABLED ) logger.debug( "Operation_IN.push(): entry") ;  
		
		try {
			
			if( operands == null ) operands = new ArrayList() ;
			operands.add( operand ) ;
			
		} finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_IN.push(): exit") ; 
		}

    	
    } // end of push()

	
	public String getTemplate() { return TEMPLATES[ operands.size() - 2 ] ; }

	
} // end of class Operation_IN