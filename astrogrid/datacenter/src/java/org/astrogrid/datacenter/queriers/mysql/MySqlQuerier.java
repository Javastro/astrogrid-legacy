/*
 * $Id: MySqlQuerier.java,v 1.9 2003/09/05 13:21:08 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.mysql;

import java.util.Properties;

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


   public static final String MYSQL_DRIVER = "org.gjt.mm.mysql.Driver";
/**
    * Constructor for test purposes really - assumes there is a mySQL
    * database server running on the localhost
    * @deprecated - dodgy leaving here. better to move to a testing sub-class.
    */
   public MySqlQuerier() throws DatabaseAccessException
   {
      this("jdbc:mysql://localhost/Catalogue",null);
   }

    /**
     * @param ds
     * @throws DatabaseAccessException
     */
    public MySqlQuerier(DataSource ds) throws DatabaseAccessException {
        super(ds);
    }

   /**

    */
   public MySqlQuerier(String url,Properties props) throws DatabaseAccessException
   {
        super(url,MYSQL_DRIVER,props); 
   }

    protected QueryTranslator createQueryTranslator() {
        return new MySqlQueryTranslator();
    }

}

