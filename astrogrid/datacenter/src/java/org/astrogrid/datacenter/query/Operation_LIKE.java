/*
 * @(#)Operation_LIKE.java   1.0
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
import org.astrogrid.Configurator;

/**
 * The <code>Operation_LIKE</code> class represents operations within an 
 * SQL query string.
 * <p>
 * The LIKE predicate applies ony to character strings and tests to see if a 
 * string conforms to a specified pattern.Two wild-cards are allowed, '%' and '_'. 
 * The '_' character represents a single arbitary character, 
 * the '%' represents an arbitary substring, possibly of zero length.
 * <p>
 * For example:
 * <p><blockquote><pre>
 * SELECT *
 *     FROM USNOB
 * WHERE COLUMN_TWO LIKE '%STAR'    
 * </pre></blockquote>
 * <p>
 *
 * @author  Phill Nicolson
 * @author  Jeff Lusted
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Operation_LIKE extends Operation_MagnitudeComparison {
	 
	private static final boolean 
		TRACE_ENABLED = true ;
	
	public final static String
			SUBCOMPONENT_NAME = Configurator.getClassName( Operation_LIKE.class ) ;
			
	private static Logger 
		logger = Logger.getLogger( Operation_LIKE.class ) ;
			
	// Template for the SQL LIKE query   
	public static final String
		TEMPLATE = "( {0} LIKE ''{1}'' )" ;
		
	public Operation_LIKE( Element opElement , Catalog catalog ) throws QueryException {	
		super( opElement, catalog ) ; 
	}
		
	public String getTemplate() { return TEMPLATE ; }

} // end of class Operation_LIKE
