/*
 * $Id: SampleStarsPluginTest.java,v 1.8 2004/11/05 12:28:31 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.queriers.sql.JdbcConnections;
import org.astrogrid.datacenter.queriers.sql.RdbmsResourceGenerator;
import org.astrogrid.datacenter.queriers.test.PrecannedResults;
import org.astrogrid.datacenter.queriers.test.SampleStarsPlugin;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.slinger.TargetMaker;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Tests the dummy querier and resultset.
 * also uses the dummy querier to test behaviours common to all queriers.
 * @author M Hill
 */

public class SampleStarsPluginTest extends TestCase {

   public void setUp() {
      SampleStarsPlugin.initConfig();
   }
   
   /** Tests the precanned results.  Plugin test is done through QuerierTest */
   public void testFixedResults() throws IOException, SAXException, SQLException {
      
      //test the fixed example ones
      QueryResults results = new PrecannedResults(null, "test");
      results.writeVotable(new StringWriter(), null);
   }

   /** Test that we can connect to the dummy database */
   public void testDummyCatalog() throws IOException, SQLException {
      
      Connection connection = JdbcConnections.makeFromConfig().createConnection();
      Statement s = connection.createStatement();
      s.execute("SELECT * FROM SampleStars WHERE Ra<100");
      
      ResultSet sqlResults = s.getResultSet();
      assertNotNull(sqlResults);

      int numResults = 0;
      while (sqlResults.next()) {
         numResults ++; //increment
      }
      
//      assertTrue(numResults==32); //should find this number
      
   }

   /** Test that we can reach the dummy catalogue through the dummy plugin */
   public void testDummyPlugin() throws IOException, SQLException, SAXException, ParserConfigurationException {
      
      QuerierManager manager = new QuerierManager("DummyTest");

      StringWriter sw = new StringWriter();
      Querier q = Querier.makeQuerier(Account.ANONYMOUS, SimpleQueryMaker.makeConeQuery(30,30,6, TargetMaker.makeIndicator(sw), ReturnTable.VOTABLE));
      manager.askQuerier(q);
      String results = sw.toString();
      Document resultsDom = DomHelper.newDocument(sw.toString());
   }
      
   
   /** Tests the generated metadata */
   public void testAutoMetadata() throws Exception {
      
      RdbmsResourceGenerator plugin = new RdbmsResourceGenerator();
      
      //generate metadata
      String[] metadata = plugin.getVoResources();
      
      //debug
      System.out.println("AutoMetadata:");
      for (int i = 0; i < metadata.length; i++) {
         System.out.print(metadata[i]);

         //check valid xml
         //ocument resultsDom = DomHelper.newDocument(metadata[i]);
      }

      
   }
   
   /** Tests the served data plugin */
   public void testMetadataServer() throws Exception {
      Document metadata = VoDescriptionServer.getVoDescription();
      
      //debug
      DomHelper.DocumentToStream(metadata, System.out);
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

