/*
 * $Id: MySqlQuerier.java,v 1.6 2003/09/03 13:47:30 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.astrogrid.datacenter.adql.*;
import org.astrogrid.datacenter.queriers.*;
import org.astrogrid.datacenter.queriers.sql.SqlResults;
import org.astrogrid.datacenter.query.Query;
import org.exolab.castor.xml.MarshalException;
import org.w3c.dom.Node;

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
         //Class.forName("org.gjt.mm.mysql.Driver").newInstance();
         Class.forName("com.mysql.jdbc.Driver").newInstance();
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
   public QueryResults queryDatabase(Node n) throws DatabaseAccessException
   {
      String sql = null;
      try
      {
         Statement statement = jdbcConnection.createStatement();
         QOM qom = ADQLUtils.unmarshalSelect(n);
         //sql = query.toSQLString(); //store this so we can use it in case of exceptions
         QueryTranslator trans = new MySqlQueryTranslator();
         sql = trans.translate(qom);
         statement.execute(sql);
         ResultSet results = statement.getResultSet();

         return new SqlResults(results);
      }      catch (SQLException e) {
         throw new DatabaseAccessException(e, "Could not query database using '" + sql + "'");
      } catch (MarshalException e) {
          throw new DatabaseAccessException(e,"Could not construct qom" );
      } catch (Exception e) {
          throw new DatabaseAccessException(e,"an error occurred");
      }
   }



}

