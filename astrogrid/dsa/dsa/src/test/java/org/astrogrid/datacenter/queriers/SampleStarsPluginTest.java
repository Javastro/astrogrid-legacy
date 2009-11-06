/*
 * $Id: SampleStarsPluginTest.java,v 1.3 2009/11/06 18:41:29 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierManager;
import org.astrogrid.dataservice.queriers.TableResults;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.NullTarget;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.tableserver.jdbc.JdbcConnections;
import org.astrogrid.tableserver.jdbc.RdbmsTableMetaDocGenerator;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.out.VoTableWriter;
import org.astrogrid.tableserver.test.PrecannedResults;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Tests the dummy querier and resultset.
 * also uses the dummy querier to test behaviours common to all queriers.
 * @author M Hill
 */

public class SampleStarsPluginTest extends TestCase {

  @Override
  public void setUp() {
    SimpleConfig.setProperty("datacenter.cache.directory", "target");
    SampleStarsPlugin.initConfig();
  }
   
   /** Tests the precanned results.  Plugin test is done through QuerierTest */
   public void testFixedResults() throws IOException, SAXException, SQLException, URISyntaxException, IOException {
      
      //test the fixed example ones
      TableResults results = new PrecannedResults(null, "test");
      results.writeTable(new VoTableWriter(new NullTarget(), "Test", LoginAccount.ANONYMOUS), null);
   }

   /** Test that we can connect to the dummy database */
   public void testDummyCatalog() throws IOException, SQLException {
      
      Connection connection = JdbcConnections.makeFromConfig().getConnection();
      Statement s = connection.createStatement();
      s.execute("SELECT * FROM SampleStars WHERE RA<100");
      
      ResultSet sqlResults = s.getResultSet();
      assertNotNull(sqlResults);

      int numResults = 0;
      while (sqlResults.next()) {
         numResults ++; //increment
      }
      
//      assertTrue(numResults==32); //should find this number
      
   }

   /** Test that we can reach the dummy catalogue through the dummy plugin */
   public void testDummyPlugin() throws IOException, SQLException, SAXException, ParserConfigurationException, QueryException {
      
      String catalogID = ConfigFactory.getCommonConfig().getString(
            "datacenter.self-test.catalog", null);
      String tableID = ConfigFactory.getCommonConfig().getString(
            "datacenter.self-test.table", null);
      String catalogName = TableMetaDocInterpreter.getCatalogNameForID(
            catalogID);
      String tableName = TableMetaDocInterpreter.getTableNameForID(
            catalogID,tableID);

      QuerierManager manager = QuerierManager.getManager("DummyTest");

      StringWriter sw = new StringWriter();
      //Querier q = Querier.makeQuerier(LoginAccount.ANONYMOUS, SimpleQueryMaker.makeConeQuery(30,30,6, new WriterTarget(sw), ReturnTable.VOTABLE), this);
      Querier q = new Querier(LoginAccount.ANONYMOUS,
          SimpleQueryMaker.makeTestQuery(catalogName, tableName,
            new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE)), 
          this);
      manager.askQuerier(q);
      Document resultsDom = DomHelper.newDocument(sw.toString());
   }
      
   
   /** Tests the generated metadata */
   public void testAutoMetadata() throws Exception {
      
      RdbmsTableMetaDocGenerator generator = new RdbmsTableMetaDocGenerator();
      
      //generate metadata - use default catalog rather than specifying names
      String metadata = generator.getMetaDoc(null);
      
      //debug
      System.out.println("AutoMetadata:");
      System.out.print(metadata);

      //check valid xml
      Document resultsDom = DomHelper.newDocument(metadata);
   }
   
   /**
    * Assembles and returns a test suite made up of all the testXxxx() methods
    * of this class.
    */
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(SampleStarsPluginTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

