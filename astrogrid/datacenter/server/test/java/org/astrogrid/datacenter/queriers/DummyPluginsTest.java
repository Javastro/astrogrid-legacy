/*
 * $Id: DummyPluginsTest.java,v 1.4 2004/07/06 18:48:34 mch Exp $
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
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.queriers.sql.JdbcConnections;
import org.astrogrid.datacenter.queriers.test.DummySqlPlugin;
import org.astrogrid.datacenter.queriers.test.PrecannedResults;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Tests the dummy querier and resultset.
 * also uses the dummy querier to test behaviours common to all queriers.
 * @author M Hill
 */

public class DummyPluginsTest extends TestCase {
   /** Tests the precanned results.  Plugin test is done through QuerierTest */
   public void testFixedResults() throws IOException, SAXException, SQLException {
      
      //test the fixed example ones
      QueryResults results = new PrecannedResults("test");
      results.toVotable(new StringWriter(), null);
   }
   
   public void testDummyCatalog() throws IOException, SQLException {
      
      //test the dummy sql ones
      DummySqlPlugin.populateDb();
      
      Connection connection = JdbcConnections.makeFromConfig().createConnection();
      Statement s = connection.createStatement();
      s.execute("SELECT * FROM SampleStars WHERE Ra<100");
      
      ResultSet sqlResults = s.getResultSet();
      assertNotNull(sqlResults);

      int numResults = 0;
      while (sqlResults.next()) {
         numResults ++; //increment
      }
      
      assertTrue(numResults==20); //should find this number
      
   }
   
   /** Tests the generated metadata */
   public void testAutoMetadata() throws Exception {
      
      DummySqlPlugin.populateDb();

      QuerierPlugin plugin = new DummySqlPlugin(null);
      
      //generate metadata
      Document metadata = plugin.getMetadata();
      
      //debug
      DomHelper.DocumentToStream(metadata, System.out);
      
   }
   
   /**
    * Assembles and returns a test suite made up of all the testXxxx() methods
    * of this class.
    */
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(DummyPluginsTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

