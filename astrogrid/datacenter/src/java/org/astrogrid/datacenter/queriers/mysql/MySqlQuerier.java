/*
 * $Id: MySqlQuerier.java,v 1.2 2003/08/27 11:21:46 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.sql.SqlResults;
import org.astrogrid.datacenter.query.Query;

/**
 * A querier that works with the MySQL database.
 *
 * @author M Hill
 */

public class MySqlQuerier implements DatabaseQuerier
{
   Connection jdbcConnection = null;

   /**
    * Constructor for test purposes really - assumes there is a mySQL
    * database server running on the localhost
    */
   public MySqlQuerier() throws SQLException
   {
      this("jdbc:mysql://localhost/Catalogue");
   }

   /**
    * Constructor takes the address of the server in the form
    * jdbc:subprotocol:subname
    */
   public MySqlQuerier(String url) throws SQLException
   {
      try
      {
         org.gjt.mm.mysql.Driver.class.newInstance();

         //or... which is not checked at compiletime but can be configured at runtime
         //Class.forName("org.gjt.mm.mysql.Driver").newInstance();
      }
      catch (IllegalAccessException e)
      {
         //rethrow as SQLException - not quite right but easier to catch
         throw new SQLException("JDBC Driver error: "+e.toString());
      }
      catch (InstantiationException e)
      {
         //rethrow as SQLException - not quite right but easier to catch
         throw new SQLException("JDBC Driver error: "+e.toString());
      }
      //catch (ClassNotFoundException e) {}

      jdbcConnection = DriverManager.getConnection(url);
   }

   public QueryResults queryDatabase(Query query)
   {
      try
      {
         Statement statement = jdbcConnection.createStatement();
         statement.execute(query.toSQLString());

         ResultSet results = statement.getResultSet();

         return new SqlResults(results);
      }
      catch (SQLException e)
      {
         e.printStackTrace(System.out);
      }
      return null;
   }



}

