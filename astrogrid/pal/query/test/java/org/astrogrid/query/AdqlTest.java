/*
 * $Id: AdqlTest.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.query.Query;
import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.query.adql.AdqlXml074Parser;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;


/**
 * Round-trip tests - can we accept ADQL queries, parse them & produce matching ADQL?
 */

public class AdqlTest extends TestCase   {

   public void testNvo1() throws Exception {
      roundParse(new AdqlTestHelper().getNvo1());
   }
   public void testNvo2() throws Exception {
      roundParse(new AdqlTestHelper().getNvo2());
   }
   public void testNvo3() throws Exception {
      roundParse(new AdqlTestHelper().getNvo3());
   }
   public void testNvo4() throws Exception {
      roundParse(new AdqlTestHelper().getNvo4());
   }
   
   public void roundParse(Element fileAdqlDom) throws Exception {
      Query query = AdqlXml074Parser.makeQuery(fileAdqlDom);
      String writtenAdql = Adql074Writer.makeAdql(query);
      Element writtenAdqlDom = DomHelper.newDocument(writtenAdql).getDocumentElement();
      System.out.print(writtenAdql);
      new AdqlTestHelper().assertElementsEqual(fileAdqlDom, writtenAdqlDom);
   }
   
   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(AdqlTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

/*
 $Log: AdqlTest.java,v $
 Revision 1.1  2005/02/17 18:37:34  mch
 *** empty log message ***

 Revision 1.2  2005/02/17 18:18:59  mch
 Moved in from datacenter project

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.6.12.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.6  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.6.1  2004/10/21 19:10:24  mch
 Removed deprecated translators, moved SqlMaker back to server,

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate


 */


