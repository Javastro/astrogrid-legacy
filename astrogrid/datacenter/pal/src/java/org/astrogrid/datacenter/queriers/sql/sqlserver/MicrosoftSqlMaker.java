/*$Id: MicrosoftSqlMaker.java,v 1.2 2004/11/03 01:35:18 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sql.sqlserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.sql.StdSqlMaker;
import org.astrogrid.datacenter.query.Query;

/**
 * Produced Postgres-specific SQL.  This means:
 * <p>
 * * All <> are replaced by &&
 * <p>
 * * No aggregate functions in WHERE
 * <p>
 * * Trig functions assume radians as arguments
 */
public class MicrosoftSqlMaker extends StdSqlMaker {

   /**
    * Constructs an SQL statement for the given ADQL.
    */
   public String makeSql(Query query) {
      
      String stdSql = super.makeSql(query);

      //add 'top'  to just after SELECT
      long limit = query.getLocalLimit();
      if (limit != -1) {
         int selectIdx = stdSql.toUpperCase().indexOf("SELECT");
         
         String msSql = stdSql.substring(0, selectIdx+6)+" TOP "+limit+" "+stdSql.substring(selectIdx+6);
         
         return msSql;
      }
      else {
         return stdSql;
      }
   }

   public String makeCountSql(Query query) {
      throw new UnsupportedOperationException("Need to get rid of LIMIT before can do this");
   }
   
}


/*
$Log: MicrosoftSqlMaker.java,v $
Revision 1.2  2004/11/03 01:35:18  mch
PAL_MCH_Candidate2 merge Part II

Revision 1.1.2.3  2004/11/01 16:01:25  mch
removed unnecessary getLocalLimit parameter, and added check for abort in sqlResults

Revision 1.1.2.2  2004/10/27 00:43:40  mch
Started adding getCount, some resource fixes, some jsps

Revision 1.1.2.1  2004/10/22 14:34:56  mch
fixes for limiting sql on ms sql server

Revision 1.2  2004/10/06 21:12:17  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.2  2004/08/05 12:49:50  mch
Removed obsolete circle condition generator

Revision 1.1  2004/04/01 17:17:57  mch
Added postgres-specific SQL translator

Revision 1.5  2004/03/24 15:57:31  kea
Updated Javadocs etc.

Revision 1.4  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.3  2004/03/12 05:03:23  mch
Removed unused code

Revision 1.2  2004/03/12 05:01:22  mch
Changed doc

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor
 
*/

