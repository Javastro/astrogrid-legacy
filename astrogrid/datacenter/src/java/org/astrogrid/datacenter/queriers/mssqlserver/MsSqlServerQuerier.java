/*$Id: MsSqlServerQuerier.java,v 1.1 2003/09/04 09:23:16 nw Exp $
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
     * @param url
     * @param driver
     * @throws DatabaseAccessException
     */
    public MsSqlServerQuerier(String url)
        throws DatabaseAccessException {
        super(url, SQL_SERVER_DRIVER);
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
Revision 1.1  2003/09/04 09:23:16  nw
added martin's query results implementation
abstract functionality from mysqlQuerier, places in SqlQuerier.
Added implementation classes for different db flavouors
 
*/