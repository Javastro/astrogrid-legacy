/*$Id: MsSqlServerQuerier.java,v 1.2 2003/09/05 13:21:08 nw Exp $
 * Created on 03-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.mssqlserver;

import java.util.Properties;

import javax.sql.DataSource;

import org.astrogrid.datacenter.queriers.*;
import org.astrogrid.datacenter.queriers.sql.*;
import org.w3c.dom.Node;

/** Database Querier implementation for Microsoft SQL Server
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2003
 * @todo fill in.
 */
public class MsSqlServerQuerier extends SqlQuerier {



    public static final String SQL_SERVER_DRIVER = "unknown.class.fill.in";
    /**
     * @param url db url
     * @param props connection parameters
     * @throws DatabaseAccessException
     */
    public MsSqlServerQuerier(String url,Properties props)
        throws DatabaseAccessException {
        super(url, SQL_SERVER_DRIVER,props);
    }

    /**
     * @param ds
     * @throws DatabaseAccessException
     */
    public MsSqlServerQuerier(DataSource ds) throws DatabaseAccessException {
        super(ds);
    }
    

    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.sql.SqlQuerier#createQueryTranslator()
     */
    protected QueryTranslator createQueryTranslator() {
        return new MsSqlServerQueryTranslator();
    }

}


/* 
$Log: MsSqlServerQuerier.java,v $
Revision 1.2  2003/09/05 13:21:08  nw
added hsqlDb querier
updated others to accept properties in constructor

Revision 1.1  2003/09/04 09:23:16  nw
added martin's query results implementation
abstract functionality from mysqlQuerier, places in SqlQuerier.
Added implementation classes for different db flavouors
 
*/