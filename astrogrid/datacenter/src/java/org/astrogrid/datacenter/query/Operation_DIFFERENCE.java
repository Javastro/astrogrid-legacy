/*
 * @(#)Operation_DIFFERENCE.java   1.0
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

import org.apache.log4j.Logger;

import org.w3c.dom.Element;


/**
 * The <code>Operation_DIFFERENCE</code> class represents operation within an
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
public class Operation_DIFFERENCE extends Operation {

   private static final boolean
      TRACE_ENABLED = true ;

  private static Logger
      logger = Logger.getLogger( Operation_DIFFERENCE.class ) ;

   // Template for the SQL AND query
   public static final String
      TEMPLATE = "( DIFFERENCE {0} {1} )" ; // JBL Note: check the syntax for a DIFFERENCE

   private Operand
      operandOne,
      operandTwo ;


   public Operation_DIFFERENCE( Element opElement , Catalog catalog ) throws QueryException {
      super( opElement, catalog ) ;
   }


   public String toSQLString() {
      if( TRACE_ENABLED ) logger.debug( "Operation_DIFFERENCE.toSQLString(): entry") ;

      String
         retValue = null ;

        // JBL: For the moment, only two operands are allowed for an AND.
        // This could easily be relaxed by building a dynamic template of the form...
        // "( {0} AND {1} AND {2} )" and so on for the requisite number of parameters.
      Object []
           inserts = new Object[2] ;

        try {
           inserts[0] = operandOne.toSQLString() ;
           inserts[1] = operandTwo.toSQLString() ;
           retValue = MessageFormat.format( this.getTemplate(), inserts ) ;
        }
        finally {
         if( TRACE_ENABLED ) logger.debug( "Operation_DIFFERENCE.toSQLString(): exit") ;
        }

      return retValue ;

   } // end of toSQLString()


    public void push( Operand operand ) {
      if( TRACE_ENABLED ) logger.debug( "Operation_DIFFERENCE.push(): entry") ;

      try {

         if( operandOne == null ){
            operandOne = operand ;
         }
         else if( operandTwo == null ) {
            operandTwo = operand ;
         }
         else {
            ; // a serious error  has occurred
         }

      } finally {
         if( TRACE_ENABLED ) logger.debug( "Operation_DIFFERENCE.push(): exit") ;
      }


    } // end of push()


   public String getTemplate() { return TEMPLATE ; }


} // end of class Operation_DIFFERENCE
