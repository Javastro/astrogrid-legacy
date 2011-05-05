/*
 * $Id: QuerierTest.java,v 1.5 2011/05/05 14:49:36 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.datacenter.DsaUnitTest;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierListener;
import org.astrogrid.dataservice.queriers.status.QuerierComplete;
import org.astrogrid.dataservice.queriers.status.QuerierError;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryState;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.tableserver.VoTableTestHelper;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.w3c.dom.Document;

/**
 * Tests the querier, using the dummy plugins. Subclass from SqlPluginTest for
 * the moment so we get all the dummy sql stuff set up
 * @author M Hill
 */

public class QuerierTest extends DsaUnitTest {

   protected Querier querier;
   protected MockListener listener;
   protected StringWriter sw;

   @Override
   public void setUp() throws Exception{
      super.setUp();

      String catalogID = ConfigFactory.getCommonConfig().getString(
            "datacenter.self-test.catalog", null);
      String tableID = ConfigFactory.getCommonConfig().getString(
            "datacenter.self-test.table", null);
      String catalogName = TableMetaDocInterpreter.getCatalogNameForID(
            catalogID);
      String tableName = TableMetaDocInterpreter.getTableNameForID(
            catalogID,tableID);

      sw = new StringWriter();
      Query query = new Query("SELECT TOP 10 * FROM TabName_SampleStars",
                              new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE));
      querier = new Querier(LoginAccount.ANONYMOUS, query, this);
      listener = new MockListener();
      querier.addListener(listener);
   }
   
   public void testStatusUpdates() throws Exception {
      // just been created.
      assertEquals(QueryState.CONSTRUCTED, querier.getStatus().getState());

      // check we can change to next step.
      querier.setStatus(new QuerierQuerying(querier.getStatus(),"NoRealQuery"));
      assertEquals(QueryState.RUNNING_QUERY,querier.getStatus().getState());
      listener.reset();
      
      // check we can jump some steps..
      querier.setStatus(new QuerierComplete(querier.getStatus()));
      assertEquals(QueryState.FINISHED,querier.getStatus().getState());
      listener.reset();
   }
   
   public void testErrorBehaviour() {
      //check it starts correct
      assertEquals(QueryState.CONSTRUCTED,querier.getStatus().getState());

      // now set an error
      Exception e = new Exception("blerghh");
      querier.setStatus(new QuerierError(querier.getStatus(), "More bleurgh",e));
      assertEquals(QueryState.ERROR,querier.getStatus().getState());
      listener.reset();
      assertEquals(e, ((QuerierError) querier.getStatus()).getCause());
   }
   
   /**
    * Tests running a querier (using the dummy sql plugin)
    */
   public void testQuerierRuns() throws Exception {

      querier.ask();
      
      assertEquals(QueryState.FINISHED,querier.getStatus().getState());
      
      System.out.println(listener.statusList); // test on something here?
      
      //and results should have already produce a valid xml document in the StringWriter sw
      assertNotNull("results return null",sw.toString());

      Document doc = VoTableTestHelper.assertIsVotable(sw.toString());
      //does a quick check for certain elements
      assertTrue(doc.getElementsByTagName("TR").getLength() > 1);
      
      //does a quick check for certain elements
      assertTrue("there are no 'FIELD' tags in the VOtable",doc.getElementsByTagName("FIELD").getLength() > 1);
      
   }
   
   
   protected class MockListener implements QuerierListener {
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

