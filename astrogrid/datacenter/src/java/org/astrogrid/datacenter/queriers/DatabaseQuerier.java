/*
 * $Id: DatabaseQuerier.java,v 1.3 2003/08/28 13:23:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import org.astrogrid.datacenter.query.Query;

/**
 * The abstract class including factory, that all database queriers must implement
 *
 * @author M Hill
 */

public abstract class DatabaseQuerier
{
   /**
    * Applies the given adql to the database, returning an abstract handle
    * to whatever the results are
    */
   public abstract QueryResults queryDatabase(Query query) throws DatabaseAccessException;

   /**
    * Returns a Querier implementation based on the value given in the
    * configuration file
    * @todo use config file, at the moment just returns a MySQL querier
    */
   public static DatabaseQuerier createQuerier() throws DatabaseAccessException
   {
      return new org.astrogrid.datacenter.queriers.mysql.MySqlQuerier();
   }

}

