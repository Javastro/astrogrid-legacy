/*
 * $Id: MicrosoftSqlWriter.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc.sqlserver;

import org.astrogrid.query.QueryException;
import org.astrogrid.tableserver.jdbc.StdSqlWriter;


/**
 * For writing out Querys as Microsoft SQL-server statment strings
 */

public class MicrosoftSqlWriter extends StdSqlWriter
{

   /** Adds 'top' if appropriate, just after SELECT and before anything else  */
   public void visitLimit(long limit) {
      if (limit>0) {
//         String newSql = select.toString();
//         int selectIdx = newSql.indexOf("SELECT ");
//         if (selectIdx == -1) {
//            throw new QueryException("No SELECT found while adding TOP");
//         }
//         newSql = newSql.substring(0,selectIdx+7)+" TOP "+limit+" "+newSql.substring(selectIdx+7);
//
//         select = new StringBuffer(newSql);
         select.insert(0, " TOP "+limit+" ");
      }
   }
   
}

/*
 $Log: MicrosoftSqlWriter.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/20 15:23:08  kea
 Checking old sources in in oldserver directory (rather than just
 deleting them, might still be useful).

 Revision 1.3  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.2  2005/03/10 22:39:17  mch
 Fixed tests more metadata fixes

 Revision 1.1  2005/03/10 16:42:55  mch
 Split fits, sql and xdb

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1  2005/02/17 18:17:46  mch
 Moved SqlWriters back into server as they need metadata information

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.1  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.1.2.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults


 */





