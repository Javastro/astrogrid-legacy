/*
 * $Id: MicrosoftSqlWriter.java,v 1.1 2005/03/10 16:42:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc.sqlserver;

import org.astrogrid.tableserver.jdbc.StdSqlWriter;


/**
 * For writing out Querys as Microsoft SQL-server statment strings
 */

public class MicrosoftSqlWriter extends StdSqlWriter
{

   /** Adds 'top' if appropriate  */
   public void visitLimit(long limit) {
      if (limit>0) {
         sql.append(" TOP "+limit+" ");
      }
   }
   
}

/*
 $Log: MicrosoftSqlWriter.java,v $
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





