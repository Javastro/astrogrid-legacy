/*
 * $Id: MySqlQuerier.java,v 1.3 2003/11/27 00:52:58 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.mysql;

import java.io.IOException;

import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.astrogrid.datacenter.queriers.sql.SqlQuerierSPI;
import org.xml.sax.SAXException;

/**
 * A querier that works with the MySQL database.
 *
 * @author M Hill
 */

public class MySqlQuerier extends SqlQuerierSPI
{


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

   /**
     * @throws DatabaseAccessException
     * @throws IOException
     * @throws SAXException
     */
    public MySqlQuerier() throws DatabaseAccessException, IOException, SAXException {
        super();
    }

    protected QueryTranslator createQueryTranslator()
   {
      return new MySqlQueryTranslator();
   }

}

