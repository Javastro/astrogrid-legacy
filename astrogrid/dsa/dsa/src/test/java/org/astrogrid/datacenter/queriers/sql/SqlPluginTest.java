/*$Id: SqlPluginTest.java,v 1.1 2009/05/13 13:20:58 gtr Exp $
 * Created on 04-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers.sql;

import java.io.InputStream;
import java.io.StringWriter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierManager;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.tableserver.VoTableTestHelper;
import org.astrogrid.tableserver.jdbc.JdbcPlugin;
import org.astrogrid.tableserver.jdbc.RdbmsTableMetaDocGenerator;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;

/** test out the vanilla sql querier on the Dummy SQL Plugin
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 * @author mch
 *
 */
public class SqlPluginTest extends TestCase {
   
   protected static final Log log = LogFactory.getLog(SqlPluginTest.class);
   
   public SqlPluginTest(String arg0) {
      super(arg0);
   }
   
   QuerierManager manager = QuerierManager.getManager("SqlQuerierTest");
   
   /**
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      
      //set max returns to something reasonably small as some of the results processing is a bit CPU intensive
      ConfigFactory.getCommonConfig().setProperty(Query.MAX_RETURN_KEY, "300");
      
   }
   
   // Do a simple test query
   public void testTestQuery() throws Exception 
   {   
      //make sure the configuration is correct for the plugin
      SampleStarsPlugin.initConfig();

      String catalogID = ConfigFactory.getCommonConfig().getString(
            "datacenter.self-test.catalog", null);
      String tableID = ConfigFactory.getCommonConfig().getString(
            "datacenter.self-test.table", null);
      String catalogName = TableMetaDocInterpreter.getCatalogNameForID(
            catalogID);
      String tableName = TableMetaDocInterpreter.getTableNameForID(
            catalogID,tableID);

      StringWriter sw = new StringWriter();
      Query q = SimpleQueryMaker.makeTestQuery(catalogName, tableName,
          new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE));
      manager.askQuerier(Querier.makeQuerier(LoginAccount.ANONYMOUS, q, this));
      log.info("Checking results...");
      //System.out.println(sw.toString());
      Document results = VoTableTestHelper.assertIsVotable(sw.toString());
      long numResults = results.getElementsByTagName("TR").getLength();
      log.info("Number of results = "+numResults);
      assert (numResults == 100);
   }
   
   public void testCone1() throws Exception {      askCone(30,30,6);  }
   
   //negative dec
   public void testCone2() throws Exception {      askCone(30,-30,6);  }
   
   //across zero ra
   public void testCone3() throws Exception {      askCone(1,-30,6);  }
   
   //around pole, across ra
   public void testCone4() throws Exception {      askCone(1,-87,6);  }
   
   /** Tests that we get back a known set of results from a search on the 'pleidies' dummies
    These are stars grouped < 0.3 degree across on ra=56.75, dec=23.867
    */
   public void testPleidiesCone() throws Exception {
      
      Document results = askCone(56.75, 23.867, 0.3);
      DomHelper.DocumentToStream(results, System.out);
   }
   
   public Document askCone(double ra, double dec, double r) throws Exception {
      
      //make sure the configuration is correct for the plugin
      SampleStarsPlugin.initConfig();
      
      StringWriter sw = new StringWriter();
      Query q = new Query(
            "CatName_SampleStarsCat", "TabName_SampleStars", "deg",
            "ColName_RA", "ColName_Dec",
            ra, dec, r, 
            new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE));
      manager.askQuerier(Querier.makeQuerier(LoginAccount.ANONYMOUS, q, this));
      log.info("Checking results...");
      System.out.println(sw.toString());
      Document results = VoTableTestHelper.assertIsVotable(sw.toString());
      long numResults = results.getElementsByTagName("TR").getLength();
      log.info("Number of results = "+numResults);
      return results;
   }

   public void testAdql1() throws Exception {
      
      askAdqlFromFile("dummydb-adql-simple.xml");
   }
   
    // Reinstate this one with an up-to-date query eventually. 
    /*
   public void testAdql2() throws Exception {
      
      askAdqlFromFile("sample_circle_1_0.xml");
   }
   */
   
   public void testPleidies() throws Exception {
      askAdqlFromFile("dummydb-adql-pleidies.xml");
   }
   
   /** Read ADQL input document, run query on dummy SQL plugin, and return VOTable document
    *
    * @param queryFile resource file of query
    */
   protected void askAdqlFromFile(String queryFile) throws Exception {
      assertNotNull(queryFile);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      assertNotNull("Could not open query file :" + queryFile,is);
      
      StringWriter sw = new StringWriter();
      //Querier q = Querier.makeQuerier(LoginAccount.ANONYMOUS, AdqlQueryMaker.makeQuery(is, new WriterTarget(sw), ReturnTable.VOTABLE), this);
      Querier q = Querier.makeQuerier(LoginAccount.ANONYMOUS, 
         new Query(is, new ReturnTable(
             new WriterTarget(sw), ReturnTable.VOTABLE)),
         this);
      
      manager.askQuerier(q);
      
      log.info("Checking results...");
      Document results = VoTableTestHelper.assertIsVotable(sw.toString());
      long numResults = results.getElementsByTagName("TR").getLength();
      log.info("Number of results = "+numResults);
   }
   
   
   /** Test Results  - could add tests to status updates here... */
   public void testResults() throws Exception  {
      
   }
   
   public void testResourceMaker() throws Exception {
      setUp();
      
      RdbmsTableMetaDocGenerator generator = new RdbmsTableMetaDocGenerator();
      
      //generate metadata - use the default catalog rather than specifying
      //catalog names.
      String resources = generator.getMetaDoc(null);
      
      Document metaDoc = DomHelper.newDocument(resources.toString());
      
      //debug
      DomHelper.DocumentToStream(metaDoc, System.out);
      
      //check results
      long numTables = metaDoc.getElementsByTagName("Table").getLength();
      assertEquals("Should be four tables (plates, stars, galaxies, stars2) in metadata", 4, numTables);
      
   }
   
   /*
    // NO LONGER NEEDED
   public void testDescriptionMaker_v0_10() throws Exception {
      setUp();
      
      //generate metadata
      Document metaDoc = VoDescriptionServer.getVoDescription(VoDescriptionServer.V0_10);
      
      //debug
      DomHelper.DocumentToStream(metaDoc, System.out);
      
      //check results
      long numTables = metaDoc.getElementsByTagNameNS("*","table").getLength();
      assertEquals("Should be four tables (plates, stars, galaxies, stars2) in metadata", 4, numTables);
      
   }
   */
   public void testDescriptionMaker_v1_0() throws Exception {
      setUp();
      
      //generate metadata
      Document metaDoc = VoDescriptionServer.getVoDescription(VoDescriptionServer.V1_0);
      
      //debug
      DomHelper.DocumentToStream(metaDoc, System.out);
      
      //check results
      long numTables = metaDoc.getElementsByTagNameNS("*","table").getLength();
      assertEquals("Should be four tables (plates, stars, galaxies, stars2) in metadata", 4, numTables);
   }
   
   /** Test harness - runs tests
    */
   public static void main(String[] args) {
      junit.textui.TestRunner.run(SqlPluginTest.class);
   }
   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(SqlPluginTest.class);
   }
   
   
   
}

