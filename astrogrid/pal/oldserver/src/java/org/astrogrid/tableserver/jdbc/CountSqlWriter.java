/*
 * $Id: CountSqlWriter.java,v 1.2 2006/06/15 16:50:10 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import org.astrogrid.query.returns.ReturnSpec;


/**
 * For writing 'count' sql queries; overrides the return spec to have 'count'
 * and removes any 'limit'
 */

public class CountSqlWriter extends StdSqlWriter
{

   public CountSqlWriter() {
   }
      
      
   /** Override all return spec to replace with count */
   public void visitReturnSpec(ReturnSpec spec) {
      select.append(" COUNT(*) ");
   }

   /** Has no place in a count statement, so leave blank */
   public void visitLimit(long limit) {
   }
   
}

/*
 $Log: CountSqlWriter.java,v $
 Revision 1.2  2006/06/15 16:50:10  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/20 15:23:08  kea
 Checking old sources in in oldserver directory (rather than just
 deleting them, might still be useful).

 Revision 1.2  2005/05/27 16:21:04  clq2
 mchv_1

 Revision 1.1.2.2  2005/05/13 16:56:32  mch
 'some changes'

 Revision 1.1.2.1  2005/04/29 16:55:47  mch
 prep for type-fix for postgres

 Revision 1.2  2005/03/21 18:45:55  mch
 Naughty big lump of changes

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




