/*
 * @(#)OrderBy.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.query;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.astrogrid.datacenter.Util;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.i18n.AstroGridMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <code>OrderBy</code> class represents the Order By clause of an SQL query
 * <p>
 * Introductory text.... For example:
 * <p><blockquote><pre>
 *
 * </pre></blockquote>
 * <p>
 *
 * @author  Phill Nicolson
 * @author  Jeff Lusted
 * @version 1.0 27-May-2003
 * @see     org.astrogrid.datacenter.Query
 * @since   AstroGrid 1.2
 */
public class OrderBy extends SQLComponent {

   private static final boolean
      TRACE_ENABLED = true ;

   public final static String
         SUBCOMPONENT_NAME =  Util.getComponentName( OrderBy.class ) ;

   private static Logger
      logger = Logger.getLogger( OrderBy.class ) ;

   private static final String
       ASTROGRIDERROR_COULD_NOT_CREATE_ORDERBY_FROM_ELEMENT = "AGDTCE00520",
       ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_ORDERBY = "AGDTCE00530",
       ASTROGRIDERROR_UNABLE_TO_MAP_CATALOG_UCD_TO_COLUMN_HEADING = "AGDTCE00250" ;

   private Operation
       operation ;

   private Catalog
      catalog ;

   public OrderBy( Element orderElement, Catalog catalog ) throws QueryException {
      if( TRACE_ENABLED ) logger.debug( "OrderBy(Element): entry") ;

      try {

         NodeList
            nodeList = orderElement.getChildNodes() ;

         Element
            operationElement ;

         this.catalog = catalog;

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
            message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_ORDERBY_FROM_ELEMENT ) ;
         logger.error( message.toString(), ex ) ;
         throw new QueryException( message, ex );
      }
      finally {
         if( TRACE_ENABLED ) logger.debug( "OrderBy(Element): exit") ;
      }

   } // end of Order( Element )


   /**
    * Returns the OrderBy part of the SQL query
    *
    * @return String
    */
      public String toSQLString() {

         String
            returnValue = null;

         try {
            returnValue = this.operation.toSQLString();
         }

         catch( Exception ex) {
            AstroGridMessage
               message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_SQL_FOR_ORDERBY ) ;
            logger.error( message.toString(), ex ) ;
         }
         finally {
            if( TRACE_ENABLED ) logger.debug( "ORDER(Element): exit") ;
         }

         return returnValue;

      } // end of toSQLString()


   /**
    * Returns a <code>String</code> value of the column heading for a specific catalog, table
    * and UCD (unified content descriptor) combination. The mapping is detailed in the
    * <code>ASROGRID_datasetconfig.properties</code> file. The format of the mapping key is:
    * <catalog>.<table>.<UCD>
    *
    * @see             org.astrogrid.datacenter.ASROGRID_datasetconfig.properties
    * @param catalog catalogue that the query will run against
    * @param ucd     Universal Column Descriptor
    * @return        <code>String</code> columnHeading
    */
      private String getColumnHeading(Catalog catalog, String UCD) {
         if( TRACE_ENABLED ) logger.debug( "getColumnHeading(): entry") ;
         String
            columnHeading = "" ;
         StringBuffer
            buffer = new StringBuffer(64) ;

         try {

            // If no tables assosciated with catalog assume table name same as catalog...
            if ( catalog.getNumberTables() <= 0 ) {
               buffer
                   .append(catalog.getName() )
                   .append( "." )
                   .append( catalog.getName() )
                   .append( "." )
                   .append( UCD ) ;
               logger.debug( "Return: getColumnHeading(): key: "+buffer.toString().toUpperCase() );
               columnHeading = Configuration.getProperty( buffer.toString());
            }
            else {

               Iterator
                   iterator = catalog.getTables() ;
               Table
                   table = null ;

               while( iterator.hasNext() ) {
                   table = ( Table )iterator.next() ;
                  buffer
                      .append( catalog.getName() )
                       .append( "." )
                       .append( table.getName().toUpperCase() )
                       .append( "." )
                       .append( UCD ) ;
                   logger.debug( "Return: getColumnHeading(): key: "+buffer.toString().toLowerCase().toUpperCase() ) ;
                  columnHeading = Configuration.getProperty( buffer.toString());
                  if ( columnHeading.length() > 0 ) // break as soon as column heading found
                      break;
                  buffer.delete( 0,buffer.length() ) ;
               } // end of while

            } // end else

         }
         catch (Exception ex) {
            AstroGridMessage
               message = new AstroGridMessage( ASTROGRIDERROR_UNABLE_TO_MAP_CATALOG_UCD_TO_COLUMN_HEADING ) ;
            logger.error( message.toString(), ex ) ;
         }
         finally {
            if( TRACE_ENABLED ) logger.debug( "getColumnHeading(): exit") ;
         }
         return ( columnHeading=="" )?UCD:columnHeading ;
      } // end of getColumnHeading()

   public void setOperation(Operation operation) { this.operation = operation ; }

} // end of class Order
