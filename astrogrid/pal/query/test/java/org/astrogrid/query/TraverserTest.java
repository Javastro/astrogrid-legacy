/*
 * $Id: TraverserTest.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import java.io.IOException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.query.DefaultQueryTraverser;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryTraverser;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.sql.SqlParser;


/**
 * Tests SQL - ADQL translator. Doesn't actually check the results properly...
 */

public class TraverserTest extends TestCase   {
   
   
   public void testSimple()  throws IOException  {
      Query query = SimpleQueryMaker.makeConeQuery(20,30,6);
      
      QueryTraverser t = new DefaultQueryTraverser();
      
      query.acceptVisitor(t);
   }
   
   public void testMore()  throws IOException  {
      Query query = SqlParser.makeQuery("SELECT SOURCE.RMAG, SOURCE.BMAG FROM SOURCE AS S, TURKEY AS T WHERE CIRCLE('J2000',20,30,6) AND ((SOURCE.RMAG > 5 OR SOURCE.BMAG > 5) OR TURKEY.COOKED LIKE 'TRUE')");
      
      QueryTraverser t = new DefaultQueryTraverser();
      
      query.acceptVisitor(t);
   }
   
   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(TraverserTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

/*
 $Log: TraverserTest.java,v $
 Revision 1.1  2005/02/17 18:37:34  mch
 *** empty log message ***

 Revision 1.2  2005/02/17 18:18:59  mch
 Moved in from datacenter project

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.3  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.1.2.2  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.1.2.1  2004/12/05 19:38:37  mch
 changed skynode to 'raw' soap (from axis) and bug fixes

 Revision 1.1.2.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.12  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.11  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.7.6.1  2004/10/21 19:10:24  mch
 Removed deprecated translators, moved SqlMaker back to server,

 Revision 1.7  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.6.2.1  2004/10/15 19:59:05  mch
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


