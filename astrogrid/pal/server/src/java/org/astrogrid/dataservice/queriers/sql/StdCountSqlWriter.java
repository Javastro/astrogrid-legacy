/*
 * $Id: StdCountSqlWriter.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.sql;

import org.astrogrid.query.returns.ReturnSpec;


/**
 * For writing out Querys as SQL statment strings, as close as we can to 'standard SQL', for doing a 'count'.
 */

public class StdCountSqlWriter extends StdSqlWriter
{

   public void visitReturnSpec(ReturnSpec spec) {
      sql.append("SELECT COUNT(*)");
   }

   /** Has no place in a count statement, so leave blank */
   public void visitLimit(long limit) {
   }
   
}

/*
 $Log: StdCountSqlWriter.java,v $
 Revision 1.1  2005/02/17 18:37:35  mch
 *** empty log message ***

 Revision 1.1  2005/02/17 18:17:46  mch
 Moved SqlWriters back into server as they need metadata information

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.1  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.1.2.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults


 */




