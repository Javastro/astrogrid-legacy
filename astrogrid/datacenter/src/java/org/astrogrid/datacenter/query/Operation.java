/*
 * @(#)Operation.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.query;

import org.astrogrid.log.Log;

import org.astrogrid.i18n.AstroGridMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * The <code>Operation</code> class represents operations within an
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
public abstract class Operation /*extends SQLComponent*/ implements Operand {

   private static final String
      ASTROGRIDERROR_COULD_NOT_CREATE_OPERATION_FROM_ELEMENT = "AGDTCE00410",
       ASTROGRIDERROR_UNSUPPORTED_SQL_OPERATION = "AGDTCE00400" ;

    public static final String
    // JBL Note: I've ignored "AVERAGE", "ANY" and "ALL" for this iteration (AG 1.2).
    // I'm not sure, in any case, that "AVERAGE" belongs here.
        AND = "AND",
        OR  = "OR",
        NOT = "NOT",
        LESS_THAN = "LESS_THAN",
        GREATER_THAN = "GREATER_THAN",
        DIFFERENCE = "DIFFERENCE",
        CONE = "CONE",
        EQUALS = "EQUALS",
        NOT_EQUALS = "NOT_EQUALS",
        GREATER_THAN_OR_EQUALS = "GREATER_THAN_OR_EQUALS",
        LESS_THAN_OR_EQUALS = "LESS_THAN_OR_EQUALS",
        IN = "IN",
        NOT_NULL = "NOT_NULL",
        BETWEEN = "BETWEEN",
        LIKE = "LIKE",
        COLUMN_ARITHMETIC = "COLUMN_ARITHMETIC",
        MINUS = "MINUS",
        PLUS = "PLUS",
        MULTIPLY = "MULTIPLY",
        DIVIDE = "DIVIDE",
        EXISTS = "EXISTS",
        ORDER_BY_DESC = "ORDER_BY_DESC",
        ORDER_BY_ASC = "ORDER_BY_ASC",
        ORDER_BY_AND = "ORDER_BY_AND",
        GROUP_BY = "GROUP_BY",
        SUBQUERY = "SUBQUERY",
        MIN = "MIN",
        MAX = "MAX",
        AVG = "AVG" ;

   private String
      name = null ;

   private Catalog
      catalog;


   public static Operation createOperation( Element opElement , Catalog catalog ) throws QueryException {
      Log.trace( "Operation.createOperation(): entry") ;

      Operation
          newOp = null ;
      String
          opName = null ;

      try {

         opName = opElement.getAttribute( AdqlTags.OP_NAME_ATTR ).trim().toUpperCase() ;

         // JBL Note: This is the nearest I've come to designing a virtual constructor,
         // which is - of course - logically impossible. With a little ingenuity I believe
         // it could accommodate most if not all of the possible SQL operations within
         // a selection statement, including sub-queries.
         if( opName.equals( Operation.AND) ) {
            newOp = new Operation_AND( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.OR ) ) {
            newOp = new Operation_OR( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.NOT ) ) {
            newOp = new Operation_LOGICAL_NOT( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.LESS_THAN ) ) {
            newOp = new Operation_LESS_THAN( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.GREATER_THAN ) ) {
            newOp = new Operation_GREATER_THAN( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.CONE ) ) {
            newOp = new Operation_CONE( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.EQUALS ) ) {
            newOp = new Operation_EQUALS( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.GREATER_THAN_OR_EQUALS ) ) {
            newOp = new Operation_GREATER_THAN_OR_EQUALS( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.LESS_THAN_OR_EQUALS ) ) {
            newOp = new Operation_LESS_THAN_OR_EQUALS( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.IN ) ) {
            newOp = new Operation_IN( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.NOT_EQUALS ) ) {
            newOp = new Operation_NOT_EQUALS( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.NOT_NULL ) ) {
            newOp = new Operation_NOT_NULL( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.BETWEEN ) ) {
            newOp = new Operation_BETWEEN( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.LIKE ) ) {
            newOp = new Operation_LIKE( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.COLUMN_ARITHMETIC ) ) {
            newOp = new Operation_COLUMN_ARITHMETIC( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.MINUS ) ) {
            newOp = new Operation_COLUMN_MINUS( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.PLUS ) ) {
            newOp = new Operation_COLUMN_PLUS( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.MULTIPLY ) ) {
            newOp = new Operation_COLUMN_MULTIPLY( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.DIVIDE ) ) {
            newOp = new Operation_COLUMN_DIVIDE( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.EXISTS ) ) {
            newOp = new Operation_EXISTS( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.ORDER_BY_DESC ) ) {
            newOp = new Operation_ORDER_BY_DESC( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.ORDER_BY_ASC ) ) {
            newOp = new Operation_ORDER_BY_ASC( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.ORDER_BY_AND ) ) {
            newOp = new Operation_ORDER_BY_AND( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.GROUP_BY ) ) {
            newOp = new Operation_GROUP_BY( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.SUBQUERY ) ) {
            newOp = new Operation_SUBQUERY( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.MIN ) ) {
            newOp = new Operation_MIN( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.MAX ) ) {
            newOp = new Operation_MAX( opElement, catalog ) ;
         }
         else if( opName.equals( Operation.AVG ) ) {
            newOp = new Operation_AVG( opElement, catalog ) ;
         }
         else {
            AstroGridMessage
                  message = new AstroGridMessage( ASTROGRIDERROR_UNSUPPORTED_SQL_OPERATION
                                                 , Operation.class.getName()
                                                 , opName ) ;
                Log.logError( message.toString() ) ;
                throw new QueryException( message );
         }

         newOp.setName( opName ) ;

      }
      finally {
         Log.trace( "Operation.createOperation(): exit") ;
      }

      return newOp ;

   } // end of createOperation()


   public Operation( Element operationElement , Catalog catalog ) throws QueryException {
      Log.trace( "Operation(Element): entry") ;

      try {

         this.catalog = catalog;

         NodeList
            nodeList = operationElement.getChildNodes() ;

         Element
            element ;


         if (operationElement.getAttribute( AdqlTags.OP_NAME_ATTR ).equals( AdqlTags.SUBQUERY_ELEMENT )) {

            for( int i=0 ; i < nodeList.getLength() ; i++ ) {
               if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
                  continue ;
                  element = (Element) nodeList.item(i) ;
                  if( element.getTagName().equals( AdqlTags.FROM_ELEMENT ) ) {
                     setFrom(new From(element)) ;
                  }
                 else if( element.getTagName().equals( AdqlTags.RETURN_ELEMENT ) ) {
                    setReturn(new Return(element, catalog)) ;
                  }
                  else if( element.getTagName().equals( AdqlTags.CRITERIA_ELEMENT ) ) {
                     setCriteria( new Criteria( element , catalog ) ) ;
                  }
                 else {
                     ; // PJN Note: What do I do here?
                  }

               } // end for
            } // end if

         else {
             for( int i=0 ; i < nodeList.getLength() ; i++ ) {
                if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
                    continue ;
                element = (Element) nodeList.item(i) ;
                if( element.getTagName().equals( AdqlTags.OP_ELEMENT ) ) {
                   this.push( Operation.createOperation( element , catalog ) ) ;
                }
                else if( element.getTagName().equals( AdqlTags.FIELD_ELEMENT ) ) {
                    this.push( new Field( element, catalog ) ) ;
                 }
                 else {
                   ; // JBL Note: What do I do here?
                }
             } // end for
         } // end else
      }
      catch( Exception ex ) {
         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_OPERATION_FROM_ELEMENT
                                              , this);
         Log.logError( message.toString(), ex ) ;
         throw new QueryException( message, ex );
      }
      finally {
         Log.trace( "Operation(Element): exit") ;
      }

   } // end of Operation( Element operationElement , Catalog catalog )


   public void setName(String name) { this.name = name; }
   public String getName() { return name; }

   public void setCatalog( Catalog catalog ) { this.catalog = catalog ; }
   public Catalog getCatalog() { return catalog; }

   public abstract void push( Operand operand ) ;
   // TODO - this is crap and needs to be fixed
   public void setFrom(From fromObject) { fromObject = fromObject ; }
   public void setReturn(Return returnObject) { returnObject = returnObject ; }
   public void setCriteria(Criteria criteria) { criteria = criteria ; }


} // end of class Operation
