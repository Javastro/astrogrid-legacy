/*
 * @(#)Operation_EQUALS.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.query;


import org.w3c.dom.Element;

/**
 * The <code>Operation_EQUALS</code> class represents operations within an
 * SQL query string.
 * <p>
 * For example:
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
public class Operation_EQUALS extends Operation_MagnitudeComparison {

   // Template for the SQL EQUALS query
   public static final String
      TEMPLATE = "( {0} = {1} )" ;

   public Operation_EQUALS( Element opElement , Catalog catalog ) throws QueryException {
      super( opElement, catalog ) ;
   }

   public String getTemplate() { return TEMPLATE ; }

} // end of class Operation_EQUALS
