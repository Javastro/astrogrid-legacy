/*
 * $Id: QuerierTest.java,v 1.3 2004/03/12 04:54:06 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.queriers.query.ConeQuery;
import org.astrogrid.datacenter.queriers.status.QuerierComplete;
import org.astrogrid.datacenter.queriers.status.QuerierError;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.queriers.test.DummySqlPlugin;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * Tests the querier, using the dummy plugins. Subclass from SqlPluginTest for
 * the moment so we get all the dummy sql stuff set up
 * @author M Hill
 */

public class QuerierTest extends ServerTestCase {

   protected Querier querier;
   protected MockListener listener;
   protected StringWriter sw;
   
   public QuerierTest(String name) {
      super(name);
   }
   
   protected void setUp() throws Exception{
      super.setUp();
      
      DummySqlPlugin.initConfig();
      DummySqlPlugin.populateDb();
      
      sw = new StringWriter();
      querier = Querier.makeQuerier(Account.ANONYMOUS, new ConeQuery(30,30,6), sw, QueryResults.FORMAT_VOTABLE);
      listener = new MockListener();
      querier.addListener(listener);
   }

   protected void tearDown() throws Exception {
      super.tearDown();
   }
   
   public void testStatusUpdates() throws Exception {
      // just been created.
      assertEquals(QueryState.CONSTRUCTED, querier.getStatus().getState());

      // check we can change to next step.
      querier.setStatus(new QuerierQuerying());
      assertEquals(QueryState.RUNNING_QUERY,querier.getStatus().getState());
      listener.reset();
      
      // check we can jump some steps..
      querier.setStatus(new QuerierComplete(querier));
      assertEquals(QueryState.FINISHED,querier.getStatus().getState());
      listener.reset();
      
      //check that we can;t go back a step however
      try {
         querier.setStatus(new QuerierProcessingResults(querier));
         fail("Should have failed trying to go back a step");
      } catch (IllegalStateException e) {
         //expected
      }
      assertFalse(listener.heard);
   }
   
   public void testErrorBehaviour() {
      //check it starts correct
      assertEquals(QueryState.CONSTRUCTED,querier.getStatus().getState());

      // now set an error
      Exception e = new Exception("blerghh");
      querier.setStatus(new QuerierError(e, "More bleurgh"));
      assertEquals(QueryState.ERROR,querier.getStatus().getState());
      listener.reset();
      assertEquals(e, ((QuerierError) querier.getStatus()).getCause());

      // now check we can't alter status any further
      try {
         querier.setStatus(new QuerierComplete(querier));
         fail("expected to barf");
      } catch (IllegalStateException ignored) {
         //expected
      }
      assertFalse(listener.heard);
      
   }
   
   /**
    * Tests running a querier (using the dummy sql plugin)
    */
   public void testQuerierRuns() throws Exception {

      querier.ask();
      
      assertEquals(QueryState.FINISHED,querier.getStatus().getState());
      
      System.out.println(listener.statusList); // test on something here?
      
      //and results should have already produce a valid xml document in the StringWriter sw
      Document doc = DomHelper.newDocument(sw.toString());
      assertNotNull("results return null",doc);
      assertIsVotable(doc);
      
      //does a quick check for certain elements
      assertTrue(doc.getElementsByTagName("TR").getLength() > 1);
      
      //does a quick check for certain elements
      assertTrue("there are no 'FIELD' tags in the VOtable",doc.getElementsByTagName("FIELD").getLength() > 1);
      
   }
   
   
   class MockListener implements QuerierListener {
      List statusList = new ArrayList();
      public void queryStatusChanged(Querier querier) {
         heard = true;
         statusList.add(querier.getStatus().getState());
      }
      boolean heard ;
      public void reset() {
         assertTrue("didn't receive notification",heard);
         heard = false;
      }
   }
   
   
   /**
    * Assembles and returns a test suite made up of all the testXxxx() methods
    * of this class.
    */
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(QuerierTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

