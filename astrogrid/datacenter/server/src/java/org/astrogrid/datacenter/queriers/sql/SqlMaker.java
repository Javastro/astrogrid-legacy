/*$Id: SqlMaker.java,v 1.2 2004/03/12 20:04:57 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sql;

import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.RawSqlQuery;

/**
 * Translates queries into SQL
 */
public abstract class SqlMaker  {
   
   /** Key used to look up the column containing RA for cone searches */
   public static final String CONE_SEARCH_TABLE_KEY = "conesearch.table";
   public static final String CONE_SEARCH_RA_COL_KEY = "conesearch.ra.column";
   public static final String CONE_SEARCH_DEC_COL_KEY = "conesearch.dec.column";
   
   /** Key used to look up the column containing HTM for cone searches */
   public static final String CONE_SEARCH_HTM_KEY = "conesearch.htm.column";
   
   /**
    * Makes an SQL string from the given Query */
   public String getSql(Query query) throws QueryException {
      if (query instanceof RawSqlQuery) {
         return ((RawSqlQuery) query).getSql();
      }
      else if (query instanceof ConeQuery) {
         return fromCone((ConeQuery) query);
      }
      else if (query instanceof AdqlQuery) {
         return fromAdql((AdqlQuery) query);
      }
      else {
         throw new QueryException("Don't know how to make an ADQL Query from a "+query.getClass());
      }
   }
   
   /**
    * Constructs an SQL statement for the given cone query
    */
   public abstract String fromCone(ConeQuery query) throws QueryException;
   
   /**
    * Constructs an SQL statement for the given ADQL
    */
   public abstract String fromAdql(AdqlQuery query) throws QueryException;
   
}


/*
$Log: SqlMaker.java,v $
Revision 1.2  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 
*/
