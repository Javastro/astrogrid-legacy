/*
 * $Id: MySqlQuerier.java,v 1.4 2003/11/28 16:10:30 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.mysql;

import java.io.IOException;

import org.astrogrid.datacenter.queriers.DatabaseAccessException;
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
     * @throws DatabaseAccessException
     * @throws IOException
     * @throws SAXException
     */
    public MySqlQuerier() throws DatabaseAccessException, IOException, SAXException {
        super();
    }
    static {
        // override translator to use
        map.add("http://tempuri.org/adql",new MySqlQueryTranslator());
    }


}

