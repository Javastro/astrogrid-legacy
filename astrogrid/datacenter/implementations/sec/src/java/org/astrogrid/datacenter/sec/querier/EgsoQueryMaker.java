/*$Id: EgsoQueryMaker.java,v 1.2 2004/07/07 14:32:54 KevinBenson Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.sec.querier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.RawSqlQuery;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.queriers.sql.SqlMaker;
import org.astrogrid.datacenter.queriers.sql.StdSqlMaker;
import org.w3c.dom.Element;

/**
 * Produced Postgres-specific SQL
 */
public class EgsoQueryMaker  {

   private static final Log log = LogFactory.getLog(EgsoQueryMaker.class);

   /**
    * Makes an SQL string from the given Query */
   public EgsoQuery getEgsoQuery(Query query) throws QueryException {
      EgsoQuery egso = new EgsoQuery();
      StdSqlMaker ssm = new StdSqlMaker();
      String sql = ssm.getSql(query);
      egso.setSQL(sql);
      return egso;
   }
}


/*
$Log: EgsoQueryMaker.java,v $
Revision 1.2  2004/07/07 14:32:54  KevinBenson
Few small changes because I had it referencing "cds" at the moment.

Revision 1.1  2004/07/07 09:17:40  KevinBenson
New SEC/EGSO proxy to query there web service on the Solar Event Catalog

Revision 1.1  2004/03/13 23:40:59  mch
Changes to adapt to It05 refactor

Revision 1.4  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.3  2004/03/12 05:03:23  mch
Removed unused code

Revision 1.2  2004/03/12 05:01:22  mch
Changed doc

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 
*/

