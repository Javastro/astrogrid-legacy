/*
 * $Id: MySqlQuerier.java,v 1.5 2004/01/15 14:49:47 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.mysql;

import java.io.IOException;

import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.sql.SqlQuerierSPI;
import org.xml.sax.SAXException;

/**
 * Specialization of SqlQuerierSPI for MySQL Databases.
 * <p>
 * At present, just registers a custom query translator for ADQL - {@link MySqlServerQueryTranslator}
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
        map.add(ADQLUtils.ADQL_XMLNS,new MySqlQueryTranslator());
    }


}

