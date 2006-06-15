/*$Id: PostgresSqlMaker.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.tableserver.jdbc.postgres;

import java.io.IOException;
import org.astrogrid.query.Query;
import org.astrogrid.tableserver.jdbc.CountSqlWriter;
import org.astrogrid.tableserver.jdbc.StdSqlMaker;

/**
 * Produced Postgres-specific SQL.  This means:
 * <p>
 * * All <> are replaced by &&
 * <p>
 * * No aggregate functions in WHERE
 * <p>
 * * Trig functions assume radians as arguments
 */
public class PostgresSqlMaker extends StdSqlMaker {

   /**
    * Constructs an SQL statement for the given ADQL document by getting the
    * (super) ADQL/sql and replacing the region
    */
   public String makeSql(Query query) throws IOException {
      
      PostgresSqlWriter sqlMaker = new PostgresSqlWriter();
      query.acceptVisitor(sqlMaker);
      
      return sqlMaker.getSql();
   }
   
   /**
    * Constructs an SQL count statement for the given Query.
    */
   public String makeCountSql(Query query) throws IOException {

      PostgresCountSqlWriter countWriter = new PostgresCountSqlWriter();
      query.acceptVisitor(countWriter);
      return countWriter.getSql();
   }
}


/*
$Log: PostgresSqlMaker.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.1  2006/04/20 15:23:08  kea
Checking old sources in in oldserver directory (rather than just
deleting them, might still be useful).

Revision 1.2  2005/05/27 16:21:06  clq2
mchv_1

Revision 1.1.24.2  2005/05/13 16:56:32  mch
'some changes'

Revision 1.1.24.1  2005/04/29 16:55:47  mch
prep for type-fix for postgres

Revision 1.1  2005/03/10 16:42:55  mch
Split fits, sql and xdb

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.3.12.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

 
*/

