/*
 * $Id: SqlQuerier.java,v 1.3 2003/09/02 14:47:26 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.Query;
import org.w3c.dom.Node;

/**
 * A general purpose SQL Querier that will (hopefully) produce bog standard
 * realbasic SQL from the ADQL, throwing an exception if it can't be done
 *
 * @author M Hill
 */

public class SqlQuerier extends DatabaseQuerier
{
   public QueryResults queryDatabase(Node n)
   {
      return null;
   }

}

