/*
 * @(#)Operation_AVG.java   1.0
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

import org.astrogrid.log.Log;
import org.w3c.dom.Element;


/**
 * The <code>Operation_AVG</code> class represents operation within a
 * SQL query string.
 * <p>
 * Some example text. For example:
 * <p><blockquote><pre>
 *     OR
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Operation_AVG extends Operation {

   // Template for the SQL AVG
   public static final String
      TEMPLATE = " AVG({0}) "  ;

   private List
      operands ;

   public Operation_AVG( Element opElement , Catalog catalog ) throws QueryException {
      super( opElement, catalog ) ;
   }


   public String toSQLString() {
      Log.trace( "Operation_AVG.toSQLString(): entry") ;

      String
         retValue = null ;

      Object []
         insert = new Object[1] ;

      try {

         insert[0] = ((Operand)operands.get( 0 )).toSQLString() ;
         retValue = MessageFormat.format( this.getTemplate(), insert ) ;

      }
      finally {
         Log.trace( "Operation_AVG.toSQLString(): exit") ;
      }

      return retValue ;

   } // end of toSQLString()


   public void push( Operand operand ) {
      Log.trace( "Operation_AVG.push(): entry") ;

      try {

         if( operands == null ) operands = new ArrayList() ;
         operands.add( operand ) ;

      } finally {
         Log.trace( "Operation_AVG.push(): exit") ;
      }


      } // end of push()

   public String getTemplate() { return TEMPLATE ; }


} // end of class Operation_AVG
