/*
 * $Id: MySqlQuerier.java,v 1.7 2003/09/04 09:23:16 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.mysql;

import javax.sql.DataSource;

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


   public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
/**
    * Constructor for test purposes really - assumes there is a mySQL
    * database server running on the localhost
    */
   public MySqlQuerier() throws DatabaseAccessException
   {
      this("jdbc:mysql://localhost/Catalogue");
   }

    /**
     * @param ds
     * @throws DatabaseAccessException
     */
    public MySqlQuerier(DataSource ds) throws DatabaseAccessException {
        super(ds);
    }

   /**
    * Constructor takes the address of the server in the form
    * jdbc:subprotocol:subname, eg:
    * jdbc:mysql://localhost/Catalogue
    */
   public MySqlQuerier(String url) throws DatabaseAccessException
   {
        super(url,MYSQL_DRIVER); // check this.
   }

    protected QueryTranslator createQueryTranslator() {
        return new MySqlQueryTranslator();
    }

}

