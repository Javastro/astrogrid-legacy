/*
 * @(#)Operation_OR.java   1.0
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
import java.util.ArrayList;
import java.util.List ;
// import java.util.Iterator ;
import org.w3c.dom.* ;

import java.text.MessageFormat ;


/**
 * The <code>Operation_OR</code> class represents operation within an 
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
public class Operation_OR extends Operation {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Operation_OR.class ) ;
		
	// TemplateS for the SQL logical OR query   (JBL Note: crude but effective)
	public static final String []
		TEMPLATES   =  { "( {0} OR {1} )"
			           , "( {0} OR {1} OR {2} )"
			           , "( {0} OR {1} OR {2} OR {3} )"
			           , "( {0} OR {1} OR {2} OR {3} OR {4} )"
			           , "( {0} OR {1} OR {2} OR {3} OR {4} OR {5} )" 
			           , "( {0} OR {1} OR {2} OR {3} OR {4} OR {5} OR {6} )"
			           , "( {0} OR {1} OR {2} OR {3} OR {4} OR {5} OR {6} OR {7} )"
			           , "( {0} OR {1} OR {2} OR {3} OR {4} OR {5} OR {6} OR {7} OR {8} )"
                       , "( {0} OR {1} OR {2} OR {3} OR {4} OR {5} OR {6} OR {7} OR {8} OR {9} )" } ;
			
	private List
	   operands ;
	   
	public Operation_OR( Element opElement , Catalog catalog ) throws QueryException {
		super( opElement, catalog ) ;
	}
	
	
	public String toSQLString() {
		if( TRACE_ENABLED ) logger.debug( "Operation_OR.toSQLString(): entry") ;  
		 	
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
			if( TRACE_ENABLED ) logger.debug( "Operation_OR.toSQLString(): exit") ;         	        
        }
        
		return retValue ;
		
	} // end of toSQLString()


    public void push( Operand operand ) {
		if( TRACE_ENABLED ) logger.debug( "Operation_OR.push(): entry") ;  
		
		try {
			
			if( operands == null ) operands = new ArrayList() ;
			operands.add( operand ) ;
			
		} finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_OR.push(): exit") ; 
		}

    	
    } // end of push()

	
	public String getTemplate() { return TEMPLATES[ operands.size() - 2 ] ; }

	
} // end of class Operation_OR