/*$Id: XPathQueryMaker.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.query;

import org.astrogrid.datacenter.queriers.query.AdqlQuery;
import org.astrogrid.datacenter.queriers.query.ConeQuery;
import org.astrogrid.datacenter.queriers.query.Query;
import org.astrogrid.datacenter.queriers.query.QueryException;
import org.astrogrid.datacenter.queriers.query.RawSqlQuery;

/**
 * Translates queries into SQL
 */
public class XPathQueryMaker  {
   
   public String getXPath(Query query) throws QueryException {
      if (query instanceof ConeQuery) {
         return xpathFromCone((ConeQuery) query);
      }
      else if (query instanceof AdqlQuery) {
         return xpathFromAdql((AdqlQuery) query);
      }
      else if (query instanceof RawSqlQuery) {
         return xpathFromSql((RawSqlQuery) query);
      }
      else {
         throw new QueryException("Don't know how to query using a "+query.getClass());
      }
   }
   
   /**
    * Constructs an XPath statement for the given cone query
    */
   public String xpathFromCone(ConeQuery query) throws QueryException {
      throw new QueryException("Don't know how to query using a "+query.getClass());
   }
   
   /**
    * Constructs an XPath statement for the given ADQL
    */
   public String xpathFromAdql(AdqlQuery query) throws QueryException {
      throw new QueryException("Don't know how to query using a "+query.getClass());
   }

   /**
    * Returns an XPath statement in the given RawSqlQUery
    */
   public String xpathFromSql(RawSqlQuery query) throws QueryException {
      throw new QueryException("Don't know how to query using a "+query.getClass());
   }

}


/*
$Log: XPathQueryMaker.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 
*/
