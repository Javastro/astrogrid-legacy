/*$Id: MicrosoftSqlMaker.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.dataservice.queriers.sql.sqlserver;
import org.astrogrid.query.sql.sqlserver.*;

import java.io.IOException;
import org.astrogrid.dataservice.queriers.sql.StdSqlMaker;
import org.astrogrid.query.Query;

/**
 * Produces Microsoft SQL-Server SQL
 */
public class MicrosoftSqlMaker extends StdSqlMaker {

   /**
    * Constructs an SQL statement for the given ADQL.
    */
   public String makeSql(Query query) throws IOException {

      MicrosoftSqlWriter sqlMaker = new MicrosoftSqlWriter();
      query.acceptVisitor(sqlMaker);
      return sqlMaker.getSql();
   }

}


/*
$Log: MicrosoftSqlMaker.java,v $
Revision 1.1  2005/02/17 18:37:35  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.2.12.4  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

 
*/

