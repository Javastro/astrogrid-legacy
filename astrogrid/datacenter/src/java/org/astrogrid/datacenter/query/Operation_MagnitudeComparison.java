/*
 * @(#)Operation_MagnitudeComparison.java   1.0
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
 * The <code>Operation_MagnitudeComparison</code> class represents operation within an
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
public abstract class Operation_MagnitudeComparison extends Operation {

   private Operand
      operandOne,
      operandTwo ;


   public Operation_MagnitudeComparison( Element opElement , Catalog catalog ) throws QueryException {
      super( opElement, catalog ) ;
   }


   public String toSQLString() {
      Log.trace( "Operation_MagnitudeComparison.toSQLString(): entry") ;

      String
         retValue = null ;

      Object []
           inserts = new Object[2] ;   // Only two operands are allowed for a comparison

        try {
           inserts[0] = operandOne.toSQLString() ;
           inserts[1] = operandTwo.toSQLString() ;
           retValue = MessageFormat.format( this.getTemplate(), inserts ) ;
        }
        finally {
         Log.trace( "Operation_MagnitudeComparison.toSQLString(): exit") ;
        }

      return retValue ;

   } // end of toSQLString()


    public void push( Operand operand ) {
      Log.trace( "Operation_MagnitudeComparison.push(): entry") ;

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
         Log.trace( "Operation_MagnitudeComparison.push(): exit") ;
      }


    } // end of push()


   public abstract String getTemplate() ;


} // end of class Operation_MagnitudeComparison
