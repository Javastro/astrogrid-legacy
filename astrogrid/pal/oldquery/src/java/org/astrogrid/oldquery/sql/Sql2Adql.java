/*
 * $Id: Sql2Adql.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.sql;

import java.io.IOException;
import org.astrogrid.oldquery.QueryException;
import org.astrogrid.oldquery.adql.Adql074Writer;

/**
 * Convenience class for converting ADQL/SQL to ADQL/XML
 *
 * @author M Hill
 * @deprecated  This class depends (indirectly) on the old
 * Query model (OldQuery), which needs to be removed.
 */

public class Sql2Adql {

   
   /** Constructs an ADQL 0.7.4 document from the given SQL */
   public static String translateToAdql074(String sql) throws QueryException, IOException {
      return Adql074Writer.makeAdql(SqlParser.makeQuery(sql));
   }

   public static void main(String[] args) throws QueryException, IOException {
      System.out.println(
      translateToAdql074("SELECT * from TraceData As T1 WHERE T1.Keywords/date_obs > '2002-07-28T05:00:00.000' AND T1.Keywords/date_obs <= '2002-07-28T06:00:00.000'")
      );
      
   
   }
}
/*
 $Log: Sql2Adql.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/21 10:58:25  kea
 Renaming package.

 Revision 1.1.2.1  2006/04/20 15:18:03  kea
 Adding old query code into oldquery directory (rather than simply
 chucking it away - bits may be useful).

 Revision 1.2.78.1  2006/04/10 16:17:44  kea
 Bits of registry still depending (implicitly) on old Query model, so
 moved this sideways into OldQuery, changed various old-model-related
 classes to use OldQuery and slapped deprecations on them.  Need to
 clean them out eventually, once registry can find another means to
 construct ADQL from SQL, etc.

 Note that PAL build currently broken in this branch.

 Revision 1.2  2005/03/30 15:18:55  mch
 debug etc for bad sql types

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.6  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.6.1  2004/10/21 19:10:24  mch
 Removed deprecated translators, moved SqlMaker back to server,

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.1  2004/10/11 15:53:59  mch
 New plugin key constant



 */



