/*$Id: SybaseQuerier.java,v 1.3 2003/09/05 13:21:08 nw Exp $
 * Created on 03-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.sybase;

import java.util.Properties;

import javax.sql.DataSource;

import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;


/** DatabaseQuerier implementation for Sybase.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2003
 * @todo fill tis in.
 */
public class SybaseQuerier extends SqlQuerier {

    public static final String SYBASE_DRIVER = "com.sybase.jdbc2.jdbc.SybDriver";

    /**
     * @param ds
     * @throws DatabaseAccessException
     */
    public SybaseQuerier(DataSource ds) throws DatabaseAccessException {
        super(ds);
    }

    /**
     * @param url
     * @param props
     * @throws DatabaseAccessException
     */
    public SybaseQuerier(String url,Properties props) throws DatabaseAccessException {
        super(url,SYBASE_DRIVER,props);
    }



    /* (non-Javadoc)
     * @see org.astrogrid.datacenter.queriers.sql.SqlQuerier#createQueryTranslator()
     */
    protected QueryTranslator createQueryTranslator() {
        return new SybaseQueryTranslator();
    }

}


/* 
$Log: SybaseQuerier.java,v $
Revision 1.3  2003/09/05 13:21:08  nw
added hsqlDb querier
updated others to accept properties in constructor

Revision 1.2  2003/09/04 14:40:37  nw
fixed db driver name

Revision 1.1  2003/09/04 09:23:16  nw
added martin's query results implementation
abstract functionality from mysqlQuerier, places in SqlQuerier.
Added implementation classes for different db flavouors
 
*/