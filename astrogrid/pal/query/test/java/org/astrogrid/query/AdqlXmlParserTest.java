/*
 * $Id: AdqlXmlParserTest.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.query.adql.AdqlXml074Parser;


/**
 * Some helper routines for loading test queries, etc
 */

public class AdqlXmlParserTest extends TestCase   {

   public void testParseNvo1() throws Exception {
      AdqlXml074Parser.makeQuery( new AdqlTestHelper().getNvo1() );
   }
   public void testParseNvo2() throws Exception {
      AdqlXml074Parser.makeQuery( new AdqlTestHelper().getNvo2() );
   }
   public void testParseNvo3() throws Exception {
      AdqlXml074Parser.makeQuery( new AdqlTestHelper().getNvo3() );
   }
   public void testParseNvo4() throws Exception {
      AdqlXml074Parser.makeQuery( new AdqlTestHelper().getNvo4() );
   }
   public void testParseSample1() throws Exception {
      AdqlXml074Parser.makeQuery( new AdqlTestHelper().getSampleDbPleidies() );
   }
   
   
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
 Revision 1.1  2005/02/17 18:37:34  mch
 *** empty log message ***

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


