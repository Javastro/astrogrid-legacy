/*
 * @(#)Operation_GREATER_THAN.java   1.0
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
import org.astrogrid.datacenter.Util;
import org.w3c.dom.Element;

/**
 * The <code>Operation_GREATER_THAN</code> class represents operations within an 
 * SQL query string.
 * <p>
 * Some example text. For example:
 * <p><blockquote><pre>
 *     >
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.query.Query
 * @since   AstroGrid 1.2
 */
public class Operation_GREATER_THAN extends Operation_MagnitudeComparison {
	 
	private static final boolean 
		TRACE_ENABLED = true ;
        
    private final static String
        SUBCOMPONENT_NAME =  Util.getComponentName( Operation_GREATER_THAN.class ) ;                                          
	
	private static Logger 
		logger = Logger.getLogger( Operation_GREATER_THAN.class ) ;
		
	
	// Template for the SQL query   
	public static final String
		TEMPLATE = "( {0} > {1} )" ;
		
	public Operation_GREATER_THAN( Element opElement , Catalog catalog ) throws QueryException {	
		super( opElement, catalog ) ; 
	}
		
	public String getTemplate() { return TEMPLATE ; }

} // end of class Operation_GREATER_THAN
