/*$Id: PostgresSqlMaker.java,v 1.1 2005/03/10 16:42:55 mch Exp $
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
import org.astrogrid.tableserver.jdbc.StdSqlMaker;
import org.astrogrid.query.Query;

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
    * Constructs an SQL statement for the given ADQL.
    */
   public String makeSql(Query query) throws IOException {
      
      String stdSql = super.makeSql(query);
      
      String postgresSql = stdSql.replaceAll("<>","&&");
      
      return postgresSql;
   }

   /**
    * Constructs an SQL statement for the given ADQL.
    */
   public String makeCountSql(Query query) throws IOException {
      
      String stdSql = super.makeCountSql(query);
      
      String postgresSql = stdSql.replaceAll("<>","&&");
      
      return postgresSql;
   }
   
}


/*
$Log: PostgresSqlMaker.java,v $
Revision 1.1  2005/03/10 16:42:55  mch
Split fits, sql and xdb

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.3.12.2  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

 
*/

