/*
 * $Id: Sql2Adql.java,v 1.3 2004/10/25 00:49:17 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import java.io.IOException;

/**
 * Convenience class for converting ADQL/SQL to ADQL/XML
 *
 * @author M Hill
 */


public class Sql2Adql {

   
   /** Constructs an ADQL 0.7.4 document from the given SQL */
   public static String translateToAdql074(String sql) throws QueryException, IOException {
      return Adql074Writer.makeAdql(SqlQueryMaker.makeQuery(sql));
   }

   
}
/*
 $Log: Sql2Adql.java,v $
 Revision 1.3  2004/10/25 00:49:17  jdt
 Merges from branch PAL_MCH

 Revision 1.2.6.1  2004/10/21 19:10:24  mch
 Removed deprecated translators, moved SqlMaker back to server,

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.1  2004/10/11 15:53:59  mch
 New plugin key constant



 */



