/*
 * $Id: SqlQuerier.java,v 1.4 2003/09/04 09:23:16 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.QOM;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.exolab.castor.xml.MarshalException;
import org.w3c.dom.Node;

/**
 * A general purpose SQL Querier that will (hopefully) produce bog standard
 * realbasic SQL from the ADQL, throwing an exception if it can't be done
 * 
 * <p>
 * forms a basis for oter implementations for different db flavours
 * @author M Hill
 */

public class SqlQuerier extends DatabaseQuerier {
    /** the standard sql jdbc connection, opened when the instance is created */
    protected Connection jdbcConnection = null;
    

    
    /** 
     * construct a querier
     * @param url jdbc url to connect to 
     * @param driver classname of database driver to use
     * @throws DatabaseAccessException
     */
    public SqlQuerier(String url,String driver) throws DatabaseAccessException {
        try
        {
           //declaring like this ensures the compiler checks that the driver is available
           //org.gjt.mm.mysql.Driver.class.newInstance();

           //or... which is not checked at compiletime but can be configured at runtime
           //Class.forName("org.gjt.mm.mysql.Driver").newInstance();
           Class.forName(driver).newInstance();
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
           throw new DatabaseAccessException(se,"Could not connect to database: " + se.toString());
        }

    }
    /** construct a querier from a datasource 
     * 
     * @param ds datasource to take connection from
     * @throws DatabaseAccessException
     */
    public SqlQuerier(DataSource ds) throws DatabaseAccessException {
        try {
            jdbcConnection = ds.getConnection();
        } catch (SQLException se) {
            throw new DatabaseAccessException(se,"Could not connect to database: " + se.toString());
    }

    }
    /** factory method to create query translator 
     *  - overridable by extending classes
     * @return query translator appropriate for this daabase flavour
     */
    protected QueryTranslator createQueryTranslator() {
        return new SqlQueryTranslator();
    }

    /**
        * Synchronous call to the database, submitting the given query
        * in sql form and returning the results as an SqlResults wrapper around
        * the SQL ResultSet.
        */
    public QueryResults queryDatabase(Node n) throws DatabaseAccessException {
          String sql = null;
          try
          {
             Statement statement = jdbcConnection.createStatement();
             QOM qom = ADQLUtils.unmarshalSelect(n);
             QueryTranslator trans = createQueryTranslator();
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