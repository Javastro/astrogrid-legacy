/*
 * $Id: Query2Adql074.java,v 1.4 2004/10/18 13:11:30 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import java.io.IOException;

/**
 * Writes out a Query in ADQL 0.7.4
 * @deprecated use Adql074Writer
 */

public class Query2Adql074  {
   
   /** Convenience routine */
   public static String makeAdql(Query query) throws IOException {
      return new Adql074Writer(query).write(null);
   }

   /** Convenience routine */
   public static String makeAdql(Query query, String comment) throws IOException {
      return new Adql074Writer(query).write(comment);
   }

}

/*
 $Log: Query2Adql074.java,v $
 Revision 1.4  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.3.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.3  2004/10/08 09:40:52  mch
 Started proper ADQL parsing

 Revision 1.2  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.13  2004/09/10 11:30:12  mch
 reintroduced xml processing instruction

 Revision 1.12  2004/09/08 21:04:39  mch
 fix to case chagne

 Revision 1.11  2004/09/08 15:40:57  mch
 Fix for mixed case functions

 Revision 1.10  2004/09/07 11:17:25  mch
 Removed old 0.5 adql

 Revision 1.9  2004/09/06 20:42:34  mch
 Changed XmlPrinter attrs argument to array of attrs to avoid programmer errors mistaking attr for value...

 Revision 1.8  2004/09/06 20:31:55  mch
 Fix to select *

 Revision 1.7  2004/09/01 11:18:49  mch
 Removed initial processing instruction

 Revision 1.6  2004/08/26 11:47:16  mch
 Added tests based on Patricios errors and other SQl statements, and subsequent fixes...

 Revision 1.5  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.4  2004/08/24 22:58:49  mch
 added debug line

 Revision 1.3  2004/08/24 19:06:44  mch
 Improvements to JSP pages, lots to query building and translating

 Revision 1.2  2004/08/24 17:27:31  mch
 Fixed bugs in calls to XmlTagPrinters

 Revision 1.1  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder


 */




