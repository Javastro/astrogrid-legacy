/*
 * $Id: AdqlXmlParserTest.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.adql;

import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.oldquery.OldQuery;
import org.astrogrid.oldquery.SimpleQueryMaker;
import org.astrogrid.oldquery.returns.ReturnTable;
import org.astrogrid.oldquery.adql.AdqlXml074Parser;
import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;


/**
 * Some helper routines for loading test queries, etc
 */

public class AdqlXmlParserTest extends TestCase   {

   public void testSimple() throws Exception {
      OldQuery q = SimpleQueryMaker.makeConeQuery(30.0, -80.0, 0.1, new UrlSourceTarget(new URL("ftp://ftp.etc.etc/path/path")), "VOTABLE");      

      String adql = Adql074Writer.makeAdql(q);

      ReturnTable returnTable = new ReturnTable(
          new UrlSourceTarget(new URL("ftp://ftp.etc.etc/path/path")),
          "VOTABLE"
      );
      AdqlXml074Parser.makeQuery( adql);
      //q = new OldQuery(adql, returnTable);
   }
   
   /*
   public void testParseNvo1() throws Exception {
      AdqlXml074Parser.makeQuery( (new AdqlTestHelper().getNvo1()).getDocumentElement() );
   }
   public void testParseNvo2() throws Exception {
      AdqlXml074Parser.makeQuery( (new AdqlTestHelper().getNvo2())getDocumentElement() );
   }
   public void testParseNvo3() throws Exception {
      AdqlXml074Parser.makeQuery( (new AdqlTestHelper().getNvo3()).getDocumentElement() );
   }
   public void testParseNvo4() throws Exception {
      AdqlXml074Parser.makeQuery( (new AdqlTestHelper().getNvo4()).getDocumentElement() );
   }
   public void testParseSample1() throws Exception {
      AdqlXml074Parser.makeQuery( (new AdqlTestHelper().getSampleDbPleidies()).getDocumentElement() );
   }
   
   */
   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(AdqlXmlParserTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

/*
 $Log: AdqlXmlParserTest.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/21 11:26:43  kea
 Moving tests to new packagename.

 Revision 1.1.2.1  2006/04/20 15:18:03  kea
 Adding old query code into oldquery directory (rather than simply
 chucking it away - bits may be useful).

 Revision 1.4.2.1  2006/04/10 16:17:44  kea
 Bits of registry still depending (implicitly) on old Query model, so
 moved this sideways into OldQuery, changed various old-model-related
 classes to use OldQuery and slapped deprecations on them.  Need to
 clean them out eventually, once registry can find another means to
 construct ADQL from SQL, etc.

 Note that PAL build currently broken in this branch.

 Revision 1.4  2006/03/22 15:10:13  clq2
 KEA_PAL-1534

 Revision 1.3.62.1  2006/02/16 17:13:05  kea
 Various ADQL/XML parsing-related fixes, including:
  - adding xsi:type attributes to various tags
  - repairing/adding proper column alias support (aliases compulsory
     in adql 0.7.4)
  - started adding missing bits (like "Allow") - not finished yet
  - added some extra ADQL sample queries - more to come
  - added proper testing of ADQL round-trip conversions using xmlunit
    (existing test was not checking whole DOM tree, only topmost node)
  - tweaked test queries to include xsi:type attributes to help with
    unit-testing checks

 Revision 1.3  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.2.24.2  2005/05/13 16:56:29  mch
 'some changes'

 Revision 1.2.24.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.2  2005/03/10 20:19:21  mch
 Fixed tests more metadata fixes

 Revision 1.1  2005/02/28 19:36:39  mch
 Fixes to tests

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.2  2005/02/17 18:18:59  mch
 Moved in from datacenter project

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.2.24.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.6  2004/10/12 23:09:53  mch
 Lots of changes to querying to get proxy querying to SSA, and registry stuff

 Revision 1.5  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.4  2004/09/08 21:24:14  mch
 Commented out tests on things not yet implemented

 Revision 1.3  2004/08/27 09:31:16  mch
 Added limit, order by, some page tidying, etc

 Revision 1.2  2004/08/26 11:47:16  mch
 Added tests based on Patricios errors and other SQl statements, and subsequent fixes...

 Revision 1.1  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 */


