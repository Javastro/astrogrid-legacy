/*
 * @(#)Criteria.java   1.0
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
import org.astrogrid.i18n.AstroGridMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <code>Criteria</code> class represents ...
 * <p>
 * Introductory text.... For example:
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
public class Criteria extends  SQLComponent {

   private static final boolean
      TRACE_ENABLED = true ;

   private static Logger
      logger = Logger.getLogger( Criteria.class ) ;

   private static final String
      ASTROGRIDERROR_COULD_NOT_CREATE_CRITERIA_ELEMENT = "AGDTCE00220",
      ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_CRITERIA = "AGDTCE00230",
       ASTROGRIDERROR_UNABLE_TO_MAP_CATALOG_UCD_TO_COLUMN_HEADING = "AGDTCE00240";

   private Operation
      operation ;

   private Catalog
       catalog;


   public Criteria( Element criteriaElement, Catalog catalog ) throws QueryException {
      if( TRACE_ENABLED ) logger.debug( "Criteria(Element): entry") ;

      try {

         NodeList
            nodeList = criteriaElement.getChildNodes() ;
         Element
             operationElement ;

         for( int i=0 ; i < nodeList.getLength() ; i++ ) {

            if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
               continue ;
            operationElement = (Element) nodeList.item(i) ;
            if( operationElement.getTagName().equals( AdqlTags.OP_ELEMENT ) ) {
               setOperation( Operation.createOperation( operationElement, catalog) ) ;
            }

         } // end for

      }
      catch( Exception ex ) {
         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_CRITERIA_ELEMENT
                                              , this ) ;
         logger.error( message.toString(), ex ) ;
         throw new QueryException( message, ex );
      }
      finally {
         if( TRACE_ENABLED ) logger.debug( "Criteria(Element): exit") ;
      }

   } // end of Criteria( Element )


/**
 * Returns a <code>String</code> representing the criteria part of the
 * sql string from the <code>Criteria</code> object.
 *
 * @return String buffer.toString()
 */
   public String toSQLString() {
      if( TRACE_ENABLED ) logger.debug( "Criteria.toSQLString(): entry") ;

         String
             retValue = null ;

        try {
          retValue = this.operation.toSQLString() ;
         }
      finally {
         if( TRACE_ENABLED ) logger.debug( "Criteria.toSQLString(): exit") ;
      }

      return retValue ;

   } // end of toSQLString()


   public void setOperation(Operation operation) { this.operation = operation; }
   public Operation getOperation() { return operation; }

} // end of class Criteria
