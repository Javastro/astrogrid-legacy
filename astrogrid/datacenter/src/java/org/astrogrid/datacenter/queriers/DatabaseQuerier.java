/*
 * $Id: DatabaseQuerier.java,v 1.4 2003/08/28 17:26:14 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.config.Configuration;

/**
 * The abstract class including factory, that all database queriers must implement
 *
 * @author M Hill
 */

public abstract class DatabaseQuerier
{
   /** Key to configuration files' entry that tells us what database querier
    * to use with this service
    */
   public static final String DATABASE_QUERIER = "Database Querier Class";

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
   public static DatabaseQuerier createQuerier(Query query) throws DatabaseAccessException
   {
      String querierClass = Configuration.getProperty(DATABASE_QUERIER);
//       "org.astrogrid.datacenter.queriers.sql.SqlQuerier"    //default to general SQL querier

      if (querierClass == null)
      {
         throw new DatabaseAccessException("Database Querier key ["+DATABASE_QUERIER+"] cannot be found in the configuration file(s) '"+Configuration.getLocations()+"'" );
      }

      try
      {
         return (DatabaseQuerier) querierClass.getClass().forName(querierClass).newInstance();
      }
      catch (ClassNotFoundException e)
      {
         throw new DatabaseAccessException(e,"Could not load DatabaseQuerier '"+querierClass+"'");
      }
      catch (IllegalAccessException e)
      {
         throw new DatabaseAccessException(e,"Could not load DatabaseQuerier '"+querierClass+"'");
      }
      catch (InstantiationException e)
      {
         throw new DatabaseAccessException(e,"Could not load DatabaseQuerier '"+querierClass+"'");
      }
      //return new org.astrogrid.datacenter.queriers.mysql.MySqlQuerier();
   }

}

