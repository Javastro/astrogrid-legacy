/*
 * @(#)Operation_NOT.java   1.0
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

import org.astrogrid.log.Log;

import org.w3c.dom.Element;


/**
 * The <code>Operation_NOT</code> class represents operation within an
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
public class Operation_LOGICAL_NOT extends Operation {

   // Template for the SQL NOT query
   public static final String
      TEMPLATE = "( NOT {0} )" ;

   private Operand
      operandSingleton ;


   public Operation_LOGICAL_NOT( Element opElement , Catalog catalog ) throws QueryException {
      super( opElement, catalog ) ;
   }


   public String toSQLString() {
      Log.trace( "Operation_NOT.toSQLString(): entry") ;

      String
         retValue = null ;

      Object []
           inserts = new Object[1] ;

        try {
           inserts[0] = operandSingleton.toSQLString() ;
           retValue = MessageFormat.format( this.getTemplate(), inserts ) ;
        }
        finally {
         Log.trace( "Operation_NOT.toSQLString(): exit") ;
        }

      return retValue ;

   } // end of toSQLString()


    public void push( Operand operand ) {
      Log.trace( "Operation_NOT.push(): entry") ;

      try {

         if( operandSingleton == null ){
            operandSingleton = operand ;
         }
         else {
            ; // a serious error  has occurred
         }

      } finally {
         Log.trace( "Operation_NOT.push(): exit") ;
      }


    } // end of push()


   public String getTemplate() { return TEMPLATE ; }


} // end of class Operation_NOT
