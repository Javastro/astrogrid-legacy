/*
 * @(#)Operation_SUBQUERY.java   1.0
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
import org.w3c.dom.* ;
import java.util.ArrayList;
import java.util.List ;


/**
 * The <code>Operation_SUBQUERY</code> class represents operations within an 
 * SQL query string.
 * <p>
 * Some example text. For example:
 * <p><blockquote><pre>
 *   SELECT COLUMN_ONE, COLUMN_TWO
 *     FROM USNOB..USNOB
 *   WHERE COLUMN_THREE EXISTS
 *     (SELECT COLUMN_FOUR 
 *        FROM USNOB..USNOB 
 *      WHERE ((COLUMN_FIVE EQUALS COLUMN_SIX)
 *        AND ( COLUMN_SEVEN > 1000 ))    
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Operation_SUBQUERY extends Operation {
	
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Operation_SUBQUERY.class ) ;
		
	private Return
	    returnObject ;
		
    private From
        fromObject ;
        
	private Criteria 
		criteria ;
			
	private List
		 operands ;
	
	   
	public Operation_SUBQUERY( Element opElement , Catalog catalog ) throws QueryException {
		super( opElement, catalog ) ;
	}
	
	
	public String toSQLString() {
	    if( TRACE_ENABLED ) logger.debug( "Operation_SUBQUERY.toSQLString(): entry") ;
		
		StringBuffer
			 buffer = new StringBuffer(256) ;
    	   	   
	    try {
			
		    // Some queries may have no criteria 
		    // (That means all rows will be selected!)...
		    // The test for null Criteria in the following is to allow for this.
		    // No orderby or groupby expected in subquery
			
		    buffer
				.append( " ( " )
				.append( " SELECT " )
				.append( getReturn().toSQLString() )
				.append( " FROM " ) 
				.append( getFrom().toSQLString() )
				.append( (this.criteria == null)  ?  ""  :  " WHERE "  )
				.append( (this.criteria == null)  ?  ""  :  getCriteria().toSQLString() ) 
				.append( " ) " ) ;		    
			     
	    }
	    finally {
		    logger.debug( "SQL Query: " + buffer.toString() ) ;
		    if( TRACE_ENABLED ) logger.debug( "Operation_SUBQUERY.toSQLString(): exit") ;
	    }    	     	   
	    
	    return buffer.toString() ;
    	
	} // end of toSQLString()


    public void push( Operand operand ) {
		if( TRACE_ENABLED ) logger.debug( "Operation_SUBQUERY.push(): entry") ;  
		
		try {
			
			if( operands == null ) operands = new ArrayList() ;
			operands.add( operand ) ;
			
		} 
		finally {
			if( TRACE_ENABLED ) logger.debug( "Operation_SUBQUERY.push(): exit") ; 
		}

	} // end of push()


	public void setReturn(Return returnObject) { this.returnObject = returnObject; }
	public Return getReturn() {	return returnObject; }

	public void setFrom(From fromObject) { this.fromObject = fromObject; }
	public From getFrom() {	return fromObject; } 
	
	public void setCriteria(Criteria criteria) { this.criteria = criteria; }
	public Criteria getCriteria() {	return criteria; }	
	
} // end of class Operation_SUBQUERY