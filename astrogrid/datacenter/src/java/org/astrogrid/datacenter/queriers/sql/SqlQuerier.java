/*
 * $Id: SqlQuerier.java,v 1.1 2003/08/27 22:45:49 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.Query;

/**
 * A general purpose SQL Querier that will (hopefully) produce bog standard
 * realbasic SQL from the ADQL, throwing an exception if it can't be done
 *
 * @author M Hill
 */

public class SqlQuerier implements DatabaseQuerier
{
   public QueryResults queryDatabase(Query query)
   {
      query.toSQLString();
      return null;
   }

}

