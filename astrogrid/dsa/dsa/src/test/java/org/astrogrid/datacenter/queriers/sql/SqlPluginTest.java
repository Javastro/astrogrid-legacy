/*$Id: SqlPluginTest.java,v 1.4 2011/05/05 14:49:36 gtr Exp $
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

import java.io.StringWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierManager;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.tableserver.VoTableTestHelper;
import org.astrogrid.tableserver.jdbc.RdbmsTableMetaDocGenerator;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.xml.DomHelper;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/** test out the vanilla sql querier on the Dummy SQL Plugin
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 * @author mch
 *
 */
public class SqlPluginTest {
   
   protected static final Log log = LogFactory.getLog(SqlPluginTest.class);
   
   QuerierManager manager = QuerierManager.getManager("SqlQuerierTest");
   
   @Before
   public void setUp() throws Exception {

     // Make the job database work. Put a blank database in the Maven target directory.
     SimpleConfig.setProperty("datacenter.cache.directory", "target");
     Job.initialize();
      
     //set max returns to something reasonably small as some of the results processing is a bit CPU intensive
     SimpleConfig.setProperty(Query.MAX_RETURN_KEY, "300");
      
   }
   
  // Do a simple test query
  @Test
  public void testTestQuery() throws Exception {   
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
      Query q =
          new Query("SELECT TOP 100 * FROM " + catalogName + "." + tableName,
          new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE));
      manager.askQuerier(new Querier(LoginAccount.ANONYMOUS, q, this));
      log.info("Checking results...");
      //System.out.println(sw.toString());
      Document results = VoTableTestHelper.assertIsVotable(sw.toString());
      long numResults = results.getElementsByTagName("TR").getLength();
      assertEquals("Wrong number of rows in results", 100, numResults);
   }

  @Test
   public void testCone1() throws Exception {      askCone(30,30,6);  }
   
   //negative dec
  @Test
   public void testCone2() throws Exception {      askCone(30,-30,6);  }
   
   //across zero ra
  @Test
   public void testCone3() throws Exception {      askCone(1,-30,6);  }
   
   //around pole, across ra
  @Test
   public void testCone4() throws Exception {      askCone(1,-87,6);  }
   
   /** Tests that we get back a known set of results from a search on the 'pleidies' dummies
    These are stars grouped < 0.3 degree across on ra=56.75, dec=23.867
    */
  @Test
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
      manager.askQuerier(new Querier(LoginAccount.ANONYMOUS, q, this));
      log.info("Checking results...");
      System.out.println(sw.toString());
      Document results = VoTableTestHelper.assertIsVotable(sw.toString());
      long numResults = results.getElementsByTagName("TR").getLength();
      log.info("Number of results = "+numResults);
      return results;
   }

  @Test
  public void testAdql1() throws Exception {
    runAdqlsQuery("SELECT * FROM TabName_SampleStars");
  }

  @Test
  public void testPleides() throws Exception {
    SampleStarsPlugin.initConfig();
    runAdqlsQuery("SELECT * FROM TabName_SampleStars " +
                  "WHERE ColName_RA BETWEEN 56.45 AND 57.05 " +
                  "AND ColName_Dec BETWEEN 23.567 AND 24.167");
  }

  private void runAdqlsQuery(String adqls) throws Exception {
    StringWriter sw = new StringWriter();
    Query q = new Query(adqls, new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE));
    Querier sut = new Querier(LoginAccount.ANONYMOUS, q, this);
    manager.askQuerier(sut);
    Document results = VoTableTestHelper.assertIsVotable(sw.toString());
    long numResults = results.getElementsByTagName("TR").getLength();
    log.info("Number of results = " + numResults);
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
   
}

