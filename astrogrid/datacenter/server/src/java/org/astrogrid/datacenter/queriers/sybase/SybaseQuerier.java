/*$Id: SybaseQuerier.java,v 1.3 2003/11/27 00:52:58 nw Exp $
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

import java.io.IOException;

import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.astrogrid.datacenter.queriers.sql.SqlQuerierSPI;
import org.xml.sax.SAXException;

/** DatabaseQuerier implementation for Sybase.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2003
 * @todo fill tis in.
 */
public class SybaseQuerier extends SqlQuerierSPI
{
    /**
     * @param ds
     * @throws DatabaseAccessException
     */
   public SybaseQuerier() throws DatabaseAccessException, IOException, SAXException
   {
        super();
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
Revision 1.3  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.

Revision 1.2  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.1  2003/11/14 00:38:30  mch
Code restructure

Revision 1.10  2003/09/26 11:38:00  nw
improved documentation, fixed imports

Revision 1.9  2003/09/24 21:03:19  nw
altered querier constructors.

Revision 1.8  2003/09/22 16:51:24  mch
Now posts results to dummy myspace

Revision 1.7  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.6  2003/09/15 11:34:32  mch
made startDrivers() static, now must be called from application layer

Revision 1.5  2003/09/08 19:15:46  mch
Workspace constructor now throws IOException

Revision 1.4  2003/09/07 18:56:42  mch
Moved ADQL package dependency to QueryTranslator only

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
