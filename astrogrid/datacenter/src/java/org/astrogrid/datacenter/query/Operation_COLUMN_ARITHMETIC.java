/*
 * @(#)Operation_COLUMN_ARITHMETIC.java   1.0
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

import org.apache.log4j.Logger;

import org.w3c.dom.Element;


/**
 * The <code>Operation_COLUMN_ARITHMETIC</code> class represents an ARITHMETIC operation within an
 * SQL query string. Combines with:
 * <UL>
 * <li>Operation_PLUS</li>
 * <li>Operation_MINUS</li>
 * <li>Operation_MULTIPLY</li>
 * <li>Operation_DIVIDE</li>
 * </UL>
 * <p>
 * Some example text. For example:
 * <p><blockquote><pre>
 *     SELECT *
 *       FROM USNOB
 *     WHERE ( ( COLUMN_ONE + COLUMN_TWO ) > ( COLUMN_THREE - COLUMN_FOUR - COLUMN_FIVE ) )
 * </pre></blockquote>
 * <p>
 *
 * @author  Jeff Lusted
 * @author  Phill Nicolson
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class Operation_COLUMN_ARITHMETIC extends Operation {

   private static final boolean
      TRACE_ENABLED = true ;

   private static Logger
      logger = Logger.getLogger( Operation_COLUMN_ARITHMETIC.class ) ;

   // TemplateS for the ARITHMETIC query   (PJN Note: crude but effective)
   public static final String []
      TEMPLATES   =  { "( {0} )"
                  , "( {0} {1} )"
                  , "( {0} {1} {2} )"
                  , "( {0} {1} {2} {3} )"
                  , "( {0} {1} {2} {3} {4} )"
                  , "( {0} {1} {2} {3} {4} {5} )"
                  , "( {0} {1} {2} {3} {4} {5} {6} )"
                   , "( {0} {1} {2} {3} {4} {5} {6} {7} )"
                  , "( {0} {1} {2} {3} {4} {5} {6} {7} {8} )"
                  , "( {0} {1} {2} {3} {4} {5} {6} {7} {8} {9} )" } ;

   private List
       operands ;


   public Operation_COLUMN_ARITHMETIC( Element opElement , Catalog catalog ) throws QueryException {
      super( opElement, catalog ) ;
   }


   public String toSQLString() {
      if( TRACE_ENABLED ) logger.debug( "Operation_COLUMN_ARITHMETIC.toSQLString(): entry") ;

      String
          retValue = null ;

      Object []
          inserts = new Object[ operands.size() ] ;

            try {

         for( int i = 0; i < operands.size(); i++ ) {
            inserts[i] = ((Operand)operands.get( i )).toSQLString() ;
         }
                retValue = MessageFormat.format( this.getTemplate(), inserts ) ;

            }
            finally {
         if( TRACE_ENABLED ) logger.debug( "Operation_COLUMN_ARITHMETIC.toSQLString(): exit") ;
            }

      return retValue ;

   } // end of toSQLString()


      public void push( Operand operand ) {
      if( TRACE_ENABLED ) logger.debug( "Operation_COLUMN_ARITHMETIC.push(): entry") ;

      try {

         if( operands == null ) operands = new ArrayList() ;
         operands.add( operand ) ;

      } finally {
         if( TRACE_ENABLED ) logger.debug( "Operation_COLUMN_ARITHMETIC.push(): exit") ;
      }


      } // end of push()


   public String getTemplate() { return TEMPLATES[ operands.size() - 1 ] ; }


} // end of class Operation_COLUMN_ARITHMETIC
