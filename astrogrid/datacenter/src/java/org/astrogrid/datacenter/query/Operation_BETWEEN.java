/*
 * @(#)Operation_BETWEEN.java   1.0
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
 * The <code>Operation_BETWEEN</code> class represents operation within an
 * SQL query string.
 * <p>
 * The BETWEEN predicate tests if the values of one <b>row value constructor</b>
 * lie between the values of two other value expressions.
 * <p>
 * For example:
 * <p><blockquote><pre>
 *         SELECT URA, UDEC, NDETS
 *           FROM USNOB
 *         WHERE URA BETWEEN 234.56 AND 234.6
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Operation_BETWEEN extends Operation {

   private static final boolean
      TRACE_ENABLED = true ;

   private static Logger
      logger = Logger.getLogger( Operation_BETWEEN.class ) ;

   public static final String
      TEMPLATE = "( {0} BETWEEN {1} AND {2} )" ;

   private Field
      fieldSubject,
      fieldFirstComparator,
      fieldSecondComparator ;


   public Operation_BETWEEN( Element opElement , Catalog catalog ) throws QueryException {
      super( opElement, catalog ) ;
      if( TRACE_ENABLED ) logger.debug( "Operation_BETWEEN(): entry/exit") ;
   }


   public String toSQLString() {
      if( TRACE_ENABLED ) logger.debug( "Operation_BETWEEN.toSQLString(): entry") ;

      String
         retValue = null ;

      Object []
           inserts = new Object[3] ;   // Only three operands are allowed

        try {
           inserts[0] = fieldSubject.toSQLString() ;
           inserts[1] = fieldFirstComparator.toSQLString() ;
         inserts[2] = fieldSecondComparator.toSQLString() ;
           retValue = MessageFormat.format( this.getTemplate(), inserts ) ;
        }
        finally {
         if( TRACE_ENABLED ) logger.debug( "Operation_BETWEEN.toSQLString(): exit") ;
        }

      return retValue ;

   } // end of toSQLString()


    public void push( Operand operand ) {
      if( TRACE_ENABLED ) logger.debug( "Operation_BETWEEN.push(): entry") ;

      try {

         // JBL Note: this should be an assert, but for some reason I cannot get it
         // past the syntax checker.
         if( operand instanceof Field == false ) {
                logger.debug( "Operand is not an instance of Field") ;
         }

         Field
            field = (Field)operand ;

            if( fieldSubject == null ) {
                fieldSubject = field ;
            }
            else if( fieldFirstComparator == null ) {
            fieldFirstComparator = field ;
         }
         else if( fieldSecondComparator == null ) {
            fieldSecondComparator = field ;
         }
         else {
            ; // a serious error  has occurred
         }

      } finally {
         if( TRACE_ENABLED ) logger.debug( "Operation_BETWEEN.push(): exit") ;
      }

    } // end of push()


   public String getTemplate() { return TEMPLATE ; }


} // end of class Operation_BETWEEN
