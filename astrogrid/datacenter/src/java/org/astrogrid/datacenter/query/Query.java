/*
 * @(#)Query.java   1.0
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

//import org.astrogrid.datacenter.impl.QueryFactoryImpl;
import org.astrogrid.i18n.AstroGridMessage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Query {

    private static final String
       ASTROGRIDERROR_COULD_NOT_CREATE_QUERY_FROM_ELEMENTS = "AGDTCE00055" ;

    public static final String
      QUERYFACTORY_KEY_SUFFIX = ".QUERYFACTORY" ;

    private Criteria
        criteria ;

    private Return
        returnObject ;

    private From
        fromObject ;

    private OrderBy
        orderByObject ;

   private GroupBy
       groupByObject ;

    public Query( Element elementIncQuery) throws QueryException {
      Log.trace( "Query(Element,QueryFactory): entry") ;

      Element queryElement = null;

      //extract query element from DOM
      if (elementIncQuery.getTagName().equalsIgnoreCase(AdqlTags.QUERY_ELEMENT))
      {
         queryElement = elementIncQuery;
      }
      else
      {
         NodeList queryNodes = elementIncQuery.getElementsByTagName(AdqlTags.QUERY_ELEMENT);
         queryElement = (Element) queryNodes.item(0); //should only be 1
      }

      try {

         NodeList
            nodeList = queryElement.getChildNodes() ;
         Element
            element ;
         Catalog
             catalog = null;

         // JBL Note: the following piece of code is fragile...
         // If the FROM element is not the first, the constructors for
         // Criteria and Return will contain a null Catalog

         for( int i=0 ; i < nodeList.getLength() ; i++ ) {

            if( nodeList.item(i).getNodeType() != Node.ELEMENT_NODE )
               continue ;
            element = (Element) nodeList.item(i) ;

            if( element.getTagName().equals( AdqlTags.FROM_ELEMENT ) ) {

               setFrom(new From( element )) ;

               // OK! We have a From element. Now we need to hunt for a catalog...
               NodeList nodeList2 = element.getChildNodes();
               middle_for: for( int j=0 ; j < nodeList2.getLength() ; j++ ) {

                   if (nodeList2.item(j).getNodeType() != Node.ELEMENT_NODE)
                      continue;
                  for (int k= 0 ; k < nodeList2.getLength() ; k++) {
                     if (nodeList2.item(k).getNodeType() != Node.ELEMENT_NODE)
                        continue;
                     element = (Element) nodeList2.item(k) ;
                     if (element.getTagName().equals(AdqlTags.CATALOG_ELEMENT))
                        catalog = new Catalog( element );
                        break middle_for; // OK! We've got the Catalog. Abandon search.
                  } // end of k

               } // end of for j

            } // end of if( element.getTagName().equals( AdqlTags.FROM_ELEMENT ) ) {
            else if( element.getTagName().equals( AdqlTags.CRITERIA_ELEMENT ) ) {
               setCriteria(new Criteria( element, catalog )) ;
            }
            else if( element.getTagName().equals( AdqlTags.RETURN_ELEMENT ) ) {
                setReturn(new Return( element, catalog )) ;
            }
            else if( element.getTagName().equals( AdqlTags.ORDER_ELEMENT ) ) {
               setOrderBy(new OrderBy( element, catalog )) ;
            }
            else if( element.getTagName().equals( AdqlTags.GROUP_ELEMENT ) ) {
               setGroupBy(new GroupBy( element, catalog )) ;
            }
         } // end for

      }
      catch( Exception ex ) {
         AstroGridMessage
            message = new AstroGridMessage( ASTROGRIDERROR_COULD_NOT_CREATE_QUERY_FROM_ELEMENTS
                                              , this);
         Log.logError( message.toString(), ex ) ;
         throw new QueryException( message, ex );
      }
      finally {
         Log.trace( "Query(Element,QueryFactory): exit") ;
      }

    } // end of Query()

    /*
    public QueryFactory getFactory() {
      return factory ;
    }




    public void execute() throws QueryException  {
      Log.trace( "Query.execute(): entry") ;
      factory.execute( this ) ;
      Log.trace( "Query.execute(): exit") ;
    }




    public void close() {
      factory.end() ;
    }
     */

    public String toSQLString() {
      Log.trace( "Query.toSQLString(): entry") ;

      StringBuffer
         buffer = new StringBuffer(256) ;

      try {

         // Some queries may have no criteria
         // (That means all rows will be selected!)...
         // The test for null Criteria in the following is to allow for this.

         buffer
             .append( "SELECT " )
             .append( getReturn().toSQLString() )
             .append( " FROM ")
             .append( getFrom().toSQLString() )
             .append( (this.criteria == null)  ?  ""  :  " WHERE "  )
             .append( (this.criteria == null)  ?  ""  :  getCriteria().toSQLString() )
             .append( (this.orderByObject == null)  ?  ""  :  " ORDER BY "  )
            .append( (this.orderByObject == null)  ?  ""  :  getOrderBy().toSQLString() )
             .append( (this.groupByObject == null)  ?  ""  :  " GROUP BY "  )
              .append( (this.groupByObject == null)  ?  ""  :  getGroupBy().toSQLString() ) ;

      }
      finally {
         Log.logDebug( "SQL Query: " + buffer.toString() ) ;
         Log.trace( "Query.toSQLString(): exit") ;
      }

      return buffer.toString() ;

    } // end of toSQLString()


   public void setCriteria(Criteria criteria) { this.criteria = criteria; }
   public Criteria getCriteria() {  return criteria; }

   public void setReturn(Return returnObject) { this.returnObject = returnObject; }
   public Return getReturn() {   return returnObject; }

   public void setFrom(From fromObject) { this.fromObject = fromObject; }
   public From getFrom() { return fromObject; }

   public void setOrderBy(OrderBy orderByObject) { this.orderByObject = orderByObject; }
   public OrderBy getOrderBy() { return orderByObject; }

   public void setGroupBy(GroupBy groupByObject) { this.groupByObject = groupByObject; }
   public GroupBy getGroupBy() { return groupByObject; }

} // end of class Query
