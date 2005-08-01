/*$Id: Query2MySpaceTest.java,v 1.2 2005/08/01 08:15:52 clq2 Exp $
 * Created on 22-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.integration.clientside;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.integration.StdKeys;
import org.astrogrid.datacenter.integration.TimeStamp;
import org.astrogrid.datacenter.queriers.test.SampleStarsPlugin;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.slinger.targets.TargetIndicator;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.VoSpaceResolver;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Tests that queries are carried out OK and sent to myspace correctly
 *
 */
public class Query2MySpaceTest extends TestCase implements StdKeys {

   private static final Log log = LogFactory.getLog(Query2MySpaceTest.class);
   
   protected QuerySearcher delegate;

   protected void setUp() throws Exception {
      delegate = DatacenterDelegateFactory.makeQuerySearcher(
         Account.ANONYMOUS,
         StdKeys.PAL_v05_ENDPOINT,
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE
      );
      assertNotNull("delegate was null",delegate);
      
      //temporary until we get circle/cone search scope sorted out
      SampleStarsPlugin.initConfig();

   }
   
   /** do a blocking query, where results are left in myspace - this is handy
    * as we can get the exception back if there's a problem, before we do the
    * submit (non-blocking) query
    * @todo - no web binding for this
   public void testAsk() throws Exception {

      Agsl resultsTarget = new Agsl(new Msrl(StdKeys.MYSPACE), resultsPath);

      InputStream in = this.getClass().getResourceAsStream("SimpleStarQuery-adql05.xml");
      assertNotNull("Could not find test query", in);
      
      String queryId = delegate.askQuery(
         new AdqlQuery(in),
         resultsTarget,
         QuerySearcher.VOTABLE
      );
         
      String stat = delegate.getStatus(queryId);
      TimeStamp timeout = new TimeStamp();
      
      //wait until query finishes
      do {
         stat = delegate.getStatus(queryId);
      }
      while (!stat.equals(QueryState.FINISHED.toString()) && (!stat.equals(QueryState.ERROR.toString()))
            && (timeout.getSecsSince()<60) ); // ..or timesout

      if (timeout.getSecsSince()>=60) {
         fail("query timed out, query ["+queryId+"] status="+stat);
      }
      
      assertTrue("Query ["+queryId+"] failed with "+stat+" see "+PAL_QUERYSTATUS+queryId, stat.equals(QueryState.FINISHED.toString()));
      
      //see if results are in expected myspace location
      StoreClient store = StoreDelegateFactory.createDelegate(Account.ANONYMOUS.toUser(), resultsTarget);
      checkResults(store.getStream(resultsPath));
   }

   /** do a non-blocking query, where results are left in myspace.
    * retreive from myspace, check they're what we expect
    */
   public void testSubmit() throws Exception {
      // fail("This is causing the integration tests to freeze");

      String resultsPath = "anonymous/Query2MySpaceTest.Submit.vot";
      Agsl resultsTarget = new Agsl(new Msrl(StdKeys.MYSPACE), resultsPath);


      InputStream in = this.getClass().getResourceAsStream("SimpleStarQuery-adql074.xml");
      assertNotNull("Could not find test query", in);

      Query query = AdqlQueryMaker.makeQuery(in, TargetMaker.makeIndicator(resultsTarget), QuerySearcher.VOTABLE);
      
      String queryId = delegate.submitQuery(query);
         
      String stat = delegate.getStatus(queryId);
      TimeStamp timeout = new TimeStamp();
      
      //wait until query finishes
      do {
         stat = delegate.getStatus(queryId);
      }
      while (!stat.equals(QueryState.FINISHED.toString()) && (!stat.equals(QueryState.ERROR.toString()))
            && (timeout.getSecsSince()<60) ); // ..or timesout

      if (timeout.getSecsSince()>=60) {
         fail("query timed out, query ["+queryId+"] status="+stat);
      }
      
      assertTrue("Query ["+queryId+"] failed with "+stat+" see "+PAL_QUERYSTATUS+queryId, stat.equals(QueryState.FINISHED.toString()));
      
      //see if results are in expected myspace location
      StoreClient store = StoreDelegateFactory.createDelegate(Account.ANONYMOUS.toUser(), resultsTarget);
      checkResults(store.getFile(resultsPath), store.getStream(resultsPath));
   }

      /** do a non-blocking query, where results are left in myspace.
    * retreive from myspace, check they're what we expect
    */
   public void testSubmitWithIvorn() throws Exception {

      String resultsPath = "anonymous/Query2MySpaceTest.Submit.vot";
      Ivorn resultsTarget = new Ivorn("ivo://org.astrogrid.localhost/myspace#"+resultsPath);

      InputStream in = this.getClass().getResourceAsStream("SimpleStarQuery-adql074.xml");
      assertNotNull("Could not find test query", in);
      
      Query query = AdqlQueryMaker.makeQuery(in, TargetMaker.makeIndicator(VoSpaceResolver.resolveAgsl(resultsTarget)), QuerySearcher.VOTABLE);

      String queryId = delegate.submitQuery(query);
         
      String stat = delegate.getStatus(queryId);

      //wait until query finishes
      TimeStamp timeout = new TimeStamp();
      do {
         stat = delegate.getStatus(queryId);
      }
      while ((!stat.equals(QueryState.FINISHED.toString()) && (!stat.equals(QueryState.ERROR.toString()))) // need some extra timout here too
            && (timeout.getSecsSince()<60) ); // ..or timesout

      if (timeout.getSecsSince()>=60) {
         fail("query timed out, query ["+queryId+"] status="+stat);
      }

      assertTrue("Query ["+queryId+"] failed with "+stat+" see "+PAL_QUERYSTATUS+queryId, stat.equals(QueryState.FINISHED.toString()));
      
      //see if results are in expected myspace location
      VoSpaceClient store = new VoSpaceClient(Account.ANONYMOUS.toUser());
      
      checkResults(store.getFile(resultsTarget), store.getStream(resultsTarget));
   }

   /** Checks that the given results Path does contain a votable */
   public void checkResults(StoreFile file, InputStream in) throws SAXException, IOException, ParserConfigurationException {
      
      assertNotNull("Results file not found on store", in);
      
      Document resultDoc = DomHelper.newDocument(in);
      assertNotNull("null result document",resultDoc);
      AstrogridAssert.assertVotable(resultDoc);
   }
   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(Query2MySpaceTest.class);
    }

    public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
   
   
}


/*
$Log: Query2MySpaceTest.java,v $
Revision 1.2  2005/08/01 08:15:52  clq2
Kmb 1293/1279/intTest1 FS/FM/Jes/Portal/IntTests

Revision 1.1.2.1  2005/07/12 11:21:06  KevinBenson
old datacenter moved out of the test area

Revision 1.5  2004/11/23 15:45:31  jdt
Merge from INT_JDT_757 (restoring mch's tests)

Revision 1.4.2.1  2004/11/23 15:12:52  jdt
Restored the broken tests that I removed in a hissy fit a week ago.

Revision 1.3  2004/11/11 22:54:13  mch
Moved targets

Revision 1.2  2004/11/03 00:31:03  mch
PAL_MCH Candidate 2 merge

Revision 1.1  2004/10/12 23:05:16  mch
Seperated tests properly

Revision 1.15  2004/10/06 22:03:45  mch
Following Query model changes in PAL

Revision 1.14  2004/09/09 11:35:37  mch
Updated from ADQL 0.7.4

Revision 1.13  2004/09/02 12:33:49  mch
Added better tests and reporting

Revision 1.12  2004/09/02 01:33:48  nw
added asssertions that valid VOTables are returned.

Revision 1.11  2004/07/08 07:48:40  mch
More timeout info

Revision 1.10  2004/05/25 13:34:33  mch
Fixed lack of timeout

Revision 1.9  2004/05/24 12:00:49  jdt
Disabled this test because it seems to be causing the whole suite

to seize up.

Revision 1.8  2004/05/24 11:56:06  jdt
Disabled this test because it seems to be causing the whole suite
to seize up.

Revision 1.7  2004/05/21 16:42:31  mch
Fixed compile errors

Revision 1.6  2004/05/21 10:52:31  jdt
Temporary quick fixes to deal with compilation errors following

changes to Agsl constructor.

See bugs 334 and 335

Revision 1.5  2004/05/14 11:02:05  mch
Fixed a number of errors

Revision 1.4  2004/05/13 12:25:04  mch
Fixes to create user, and switched to mostly testing it05 interface

Revision 1.3  2004/05/12 09:17:51  mch
Various fixes - forgotten whatfors...

Revision 1.2  2004/04/16 15:55:08  mch
added alltests

Revision 1.1  2004/04/16 15:41:03  mch
Added autotests

Revision 1.1  2004/03/22 17:23:06  mch
moved from old package name

Revision 1.5  2004/02/17 23:59:17  jdt
commented out lines killing the build, and made to conform to

coding stds

Revision 1.4  2004/02/16 18:02:14  jdt
removed a line that was breaking the build

Revision 1.3  2004/01/23 09:08:09  nw
added cone searcher test too

Revision 1.2  2004/01/22 16:16:40  nw
minor doc fix

Revision 1.1  2004/01/22 16:14:59  nw
added integration test for datacenter full searcher.
 
*/

