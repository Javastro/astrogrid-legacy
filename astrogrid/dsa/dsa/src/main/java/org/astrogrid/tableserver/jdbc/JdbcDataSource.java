/*
 * $Id: JdbcDataSource.java,v 1.2 2010/12/08 12:46:35 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A data-source implementation using explict URLs and credentials for connection.
 * This class is used to represent the data source when the application is managing
 * the connections; c.f the case where the JEE container manages the connections
 * and supplies its own {@code DataSource} objects.
 * <p>
 * The caller must load the appropriate JDBC-driver. This may be done either
 * before or after instantiating the {@code JdbcDataSource} but must be done before
 * calling {@link #getConnection}.
 * <p>
 * This implementation does not provide connection pooling.
 */

public class JdbcDataSource implements DataSource {

   /** 
    * User name for JDBC connections.
    */
   private final String userId;

   /**
    * Password for JDBC connections.
    */
   private final String password;

   /**
    * URL identifying the database.
    */
   private final String jdbcUrl;

   private static final Log log = LogFactory.getLog(JdbcDataSource.class);
   
   /**
    * Creates a data source.
    *
    * @param databaseURL The JDBC-scheme URL identifying the database (must not be null).
    * @param userName The user-name known to the DBMS (null implies anonymous connections).
    * @param password The password known to the DBMS (null implies unauthenticated connections).
    */
   public JdbcDataSource(String databaseUrl, String userName, String password) {
      this.userId   = userName;
      this.password = password;
      this.jdbcUrl  = databaseUrl;

      log.info("JDBC data-source set to "+jdbcUrl+" ("+userId+")");
   }

  /**
   * Gets a database connection using the given credentials.
   * This connection uses the configured URL to find the database and the
   * configured JDBC driver.
   *
   * @param userName The user name as known to the DBMS.
   * @param password The password as known to the DBMS.
   */
  public Connection getConnection(String userName, String password) throws SQLException {
    log.debug("Creating JDBC Connection from DriverManager, to Url "+jdbcUrl+" ("+userName+")");
    try {
      return DriverManager.getConnection(jdbcUrl, userName, password);
    }
    catch (SQLException se) {
      //add more info to the sql exception.  Especially the URL, as it is often a mistyped URL that gives 'no suitable driver'
      SQLException newSe = new SQLException(se.getMessage()+" ["+se.getErrorCode()+"], connecting to "+jdbcUrl,
                                            se.getSQLState(), se.getErrorCode());
      newSe.setStackTrace(se.getStackTrace());
      throw newSe;
    }
  }

  /**
   * Gets a database connection using the configured credentials.
   * This connection uses the configured URL to find the database and the
   * configured JDBC driver.
   *
   * @param userName The user name as known to the DBMS.
   * @param password The password as known to the DBMS.
   */
  public Connection getConnection() throws SQLException {
    log.debug("Creating JDBC Connection from DriverManager, to Url "+jdbcUrl);
    try {
      if (userId != null) {
        return DriverManager.getConnection(jdbcUrl, userId, password);
      }
      else {
        return DriverManager.getConnection(jdbcUrl);
      }
    }
    catch (SQLException se) {
      //add more info to the sql exception.  Especially the URL, as it is often a mistyped URL that gives 'no suitable driver'
      SQLException newSe = new SQLException(se.getMessage()+" ["+se.getErrorCode()+"], connecting to "+jdbcUrl,
                                            se.getSQLState(), se.getErrorCode());
      newSe.setStackTrace(se.getStackTrace());
      throw newSe;
    }
  }

  public PrintWriter getLogWriter() throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setLogWriter(PrintWriter out) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void setLoginTimeout(int seconds) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public int getLoginTimeout() throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public <T> T unwrap(Class<T> type) throws SQLException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public boolean isWrapperFor(Class<?> type) throws SQLException {
    return false;
  }

}
