/*
 * @(#)Operation_AND.java   1.0
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
import org.astrogrid.Configurator;


/**
 * The <code>Operation_AND</code> class represents operation within an 
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
public class Operation_AND extends Operation {
	
	private static final boolean 
		TRACE_ENABLED = true ;
        
    public final static String
        SUBCOMPONENT_NAME = Configurator.getClassName( Operation_AND.class ) ;                 
	
	private static Logger 
		logger = Logger.getLogger( Operation_AND.class ) ;
		
	// TemplateS for the SQL logical AND query   (JBL Note: crude but effective)
	public static final String []
		TEMPLATES   =  { "( {0} AND {1} )"
					   , "( {0} AND {1} AND {2} )"
					   , "( {0} AND {1} AND {2} AND {3} )"
					   , "( {0} AND {1} AND {2} AND {3} AND {4} )"
					   , "( {0} AND {1} AND {2} AND {3} AND {4} AND {5} )" 
					   , "( {0} AND {1} AND {2} AND {3} AND {4} AND {5} AND {6} )"
					   , "( {0} AND {1} AND {2} AND {3} AND {4} AND {5} AND {6} AND {7} )"
					   , "( {0} AND {1} AND {2} AND {3} AND {4} AND {5} AND {6} AND {7} AND {8} )"
					   , "( {0} AND {1} AND {2} AND {3} AND {4} AND {5} AND {6} AND {7} AND {8} AND {9} )" } ;
			
	private List
	   operands ;
	
	   
	public Operation_AND( Element opElement , Catalog catalog ) throws QueryException {
		super( opElement, catalog ) ;
	}
	
	
	public String toSQLString() {
		if( TRACE_ENABLED ) logger.debug( "Operation_AND.toSQLString(): entry") ;  
        
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
			if( TRACE_ENABLED ) logger.debug( "Operation_AND.toSQLString(): exit") ;         	        
        }
        
		return retValue ;
		
	} // end of toSQLString()


    public void push( Operand operand ) {
		if( TRACE_ENABLED ) logger.debug( "Operation_AND.push(): entry") ;  
		
		try {
			
			if( operands == null ) operands = new ArrayList() ;
			operands.add( operand ) ;
			
		} finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_AND.push(): exit") ; 
		}

    	
    } // end of push()

	
	public String getTemplate() { return TEMPLATES[ operands.size() - 2 ] ; }

	
} // end of class Operation_AND