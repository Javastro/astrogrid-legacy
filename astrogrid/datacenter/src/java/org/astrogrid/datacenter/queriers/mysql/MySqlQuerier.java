/*
 * $Id: MySqlQuerier.java,v 1.4 2003/08/28 13:23:45 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.sql.SqlResults;
import org.astrogrid.datacenter.query.Query;

/**
 * A querier that works with the MySQL database.
 *
 * @author M Hill
 */

public class MySqlQuerier extends DatabaseQuerier
{
   /** the standard sql jdbc connection, opened when the instance is created */
   private Connection jdbcConnection = null;

   /**
    * Constructor for test purposes really - assumes there is a mySQL
    * database server running on the localhost
    */
   public MySqlQuerier() throws DatabaseAccessException
   {
      this("jdbc:mysql://localhost/Catalogue");
   }

   /**
    * Constructor takes the address of the server in the form
    * jdbc:subprotocol:subname, eg:
    * jdbc:mysql://localhost/Catalogue
    */
   public MySqlQuerier(String url) throws DatabaseAccessException
   {
      try
      {
         //declaring like this ensures the compiler checks that the driver is available
         //org.gjt.mm.mysql.Driver.class.newInstance();

         //or... which is not checked at compiletime but can be configured at runtime
         Class.forName("org.gjt.mm.mysql.Driver").newInstance();

         jdbcConnection = DriverManager.getConnection(url);
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
      catch (SQLException se)
      {
         throw new DatabaseAccessException(se,"Could not connect to MySQL server: " + se.toString());
      }

   }

   /**
    * Synchronous call to the mysql database, submitting the given query
    * in sql form and returning the results as an SqlResults wrapper around
    * the SQL ResultSet.
    */
   public QueryResults queryDatabase(Query query) throws DatabaseAccessException
   {
      String sql = null;
      try
      {
         Statement statement = jdbcConnection.createStatement();
         sql = query.toSQLString(); //store this so we can use it in case of exceptions
         statement.execute(sql);
         ResultSet results = statement.getResultSet();

         return new SqlResults(results);
      }
      catch (SQLException e)
      {
         throw new DatabaseAccessException(e, "Could not query database using '" + sql + "'");
      }
   }



}

