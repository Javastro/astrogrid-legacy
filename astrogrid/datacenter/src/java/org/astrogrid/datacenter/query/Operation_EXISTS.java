/*
 * @(#)Operation_EXISTS.java   1.0
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
import org.w3c.dom.Element;

/**
 * The <code>Operation_EXISTS</code> class represents operations within an 
 * SQL query string.
 * <p>
 * The EXISTS predicate differs slightly from other predicates in that, instead of 
 * testing a simple value or a row value constructor against something, it returns
 * a result purely on the basis of the characteristics of the results of a subquery.
 * <p>
 * For example:
 * <p><blockquote><pre>
 * SELECT COLUMN_ONE
 *     FROM USNOB
 * WHERE EXISTS (SELECT COLUMN_TWO
 *               FROM USNOB
 *               WHERE (COLUMN_THREE = COLUMN_FOUR)
 * </pre></blockquote>
 * <p>
 *
 * @author  Phill Nicolson
 * @author  Jeff Lusted
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Operation_EXISTS extends Operation_MagnitudeComparison {
	 
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Operation_EXISTS.class ) ;
		
	
	// Template for the SQL EXISTS query   
	public static final String
		TEMPLATE = "( {0} EXISTS {1} )" ;
		
	public Operation_EXISTS( Element opElement , Catalog catalog ) throws QueryException {	
		super( opElement, catalog ) ; 
	}
		
	public String getTemplate() { return TEMPLATE ; }

} // end of class Operation_EXISTS
