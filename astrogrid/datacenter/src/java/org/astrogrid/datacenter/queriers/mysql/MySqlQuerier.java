/*
 * $Id: MySqlQuerier.java,v 1.14 2003/09/17 14:51:30 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.mysql;

import java.io.IOException;

import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;

/**
 * A querier that works with the MySQL database.
 *
 * @author M Hill
 */

public class MySqlQuerier extends SqlQuerier
{
   public MySqlQuerier() throws DatabaseAccessException, IOException
   {
      super();
   }

   /**
    * Also starts Sybase jdbc driver... hardcoded, don't like this!
    * @todo - check reason for hardcoded driver - MCH
    *
   public void startDrivers() throws DatabaseAccessException
   {
      //new com.sybase.jdbc2.jdbc.SybDriver(); //compile-time check
      try
      {
         Class.forName("org.gjt.mm.mysql.Driver").newInstance();
      }
      catch (IllegalAccessException e)
      {
         throw new DatabaseAccessException(e,"JDBC Driver error: " + e.toString());
      }
      catch (InstantiationException e)
      {
         throw new DatabaseAccessException(e, "JDBC Driver error: " + e.toString());
      }
      catch (ClassNotFoundException e)
      {
         throw new DatabaseAccessException(e, "JDBC Driver error: " + e.toString());
      }

      //start usual config ones
      super.startDrivers();
   }
    /**/

   protected QueryTranslator createQueryTranslator()
   {
      return new MySqlQueryTranslator();
   }

}

