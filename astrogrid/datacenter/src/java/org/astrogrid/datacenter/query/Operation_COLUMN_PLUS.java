/*
 * @(#)Operation_COLUMN_PLUS.java   1.0
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
 * The <code>Operation_COLUMN_PLUS</code> class represents operation within an
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
public class Operation_COLUMN_PLUS extends Operation {

   // TemplateS for the PLUS query   (PJN Note: crude but effective)
   public static final String []
      TEMPLATES   =  {  " + {0} "
                    , "( {0} + {1} )"
                   , "( {0} + {1} + {2} )"
                   , "( {0} + {1} + {2} + {3} )"
                   , "( {0} + {1} + {2} + {3} + {4} )"
                   , "( {0} + {1} + {2} + {3} + {4} + {5} )"
                   , "( {0} + {1} + {2} + {3} + {4} + {5} + {6} )"
                   , "( {0} + {1} + {2} + {3} + {4} + {5} + {6} + {7} )"
                   , "( {0} + {1} + {2} + {3} + {4} + {5} + {6} + {7} + {8} )"
                   , "( {0} + {1} + {2} + {3} + {4} + {5} + {6} + {7} + {8} + {9} )" } ;

   private List
       operands ;


   public Operation_COLUMN_PLUS( Element opElement , Catalog catalog ) throws QueryException {
      super( opElement, catalog ) ;
   }


   public String toSQLString() {
      Log.trace( "Operation_COLUMN_PLUS.toSQLString(): entry") ;

      String
          retValue = null ;

      if ( operands == null ) {
         retValue = " + " ;
      }
      else {

      Object []
          inserts = new Object[ operands.size() ] ;

      try {

         for( int i = 0; i < operands.size(); i++ ) {
            inserts[i] = ( (Operand)operands.get( i ) ).toSQLString() ;
         }
                retValue = MessageFormat.format( this.getTemplate(), inserts ) ;

            }
            finally {
         Log.trace( "Operation_COLUMN_PLUS.toSQLString(): exit" ) ;
            }
      } //end of else

      return retValue ;

   } // end of toSQLString()


      public void push( Operand operand ) {
      Log.trace( "Operation_COLUMN_PLUS.push(): entry") ;

      try {

         if( operands == null ) operands = new ArrayList() ;
         operands.add( operand ) ;

      } finally {
         Log.trace( "Operation_COLUMN_PLUS.push(): exit") ;
      }


      } // end of push()


   public String getTemplate() { return TEMPLATES[ operands.size() - 1] ; }


} // end of class Operation_COLUMN_PLUS
