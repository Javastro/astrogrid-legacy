/*
 * @(#)Operation_GREATER_THAN_OR_EQUALS.java   1.0
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

/**
 * The <code>Operation_GREATER_THAN_OR_EQUALS</code> class represents operations within an 
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
public class Operation_GREATER_THAN_OR_EQUALS extends Operation_MagnitudeComparison {
	 
	private static final boolean 
		TRACE_ENABLED = true ;
	
	private static Logger 
		logger = Logger.getLogger( Operation_GREATER_THAN_OR_EQUALS.class ) ;
		
	
	// Template for the SQL query   
	public static final String
		TEMPLATE = "( {0} >= {1} )" ;
		
	public Operation_GREATER_THAN_OR_EQUALS( Element opElement, Catalog catalog ) throws QueryException {	
		super( opElement, catalog ) ; 
	}
		
	public String getTemplate() { return TEMPLATE ; }

} // end of class Operation_GREATER_THAN_OR_EQUALS
