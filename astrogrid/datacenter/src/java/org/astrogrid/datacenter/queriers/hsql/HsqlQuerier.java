/*$Id: HsqlQuerier.java,v 1.1 2003/09/05 13:21:08 nw Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.hsql;

import java.util.Properties;

import javax.sql.DataSource;

import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Sep-2003
 *
 */
public class HsqlQuerier extends SqlQuerier {
    public static final String HSQL_DRIVER ="org.hsqldb.jdbcDriver";
    
    public HsqlQuerier(DataSource ds) throws DatabaseAccessException {
        super(ds);
    }
    
    public HsqlQuerier(String url,Properties props) throws DatabaseAccessException {
        super(url,HSQL_DRIVER,props);
    }
}


/* 
$Log: HsqlQuerier.java,v $
Revision 1.1  2003/09/05 13:21:08  nw
added hsqlDb querier
updated others to accept properties in constructor
 
*/