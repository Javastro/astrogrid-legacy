/*$Id: SsaProxyPluginTest.java,v 1.2 2004/10/13 01:31:34 mch Exp $
 * Created on 04-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.deployment.ssa;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.UrlResourcePlugin;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.queriers.pal.PalProxyPlugin;
import org.astrogrid.datacenter.queriers.sql.SqlMaker;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.WriterTarget;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 *
 */
public class SsaProxyPluginTest extends TestCase {
   
   protected static final Log log = LogFactory.getLog(SsaProxyPluginTest.class);
   
   QuerierManager manager = QuerierManager.getManager("PalProxyQuerierTest");

   
   /**
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();

      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_TABLE_KEY, "Detection");
      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_RA_COL_KEY, "Ra");
      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_DEC_COL_KEY, "Dec");
      SimpleConfig.setProperty(SqlMaker.CONE_SEARCH_COL_UNITS_KEY, "deg");
      SimpleConfig.setProperty(SqlMaker.DB_TRIGFUNCS_IN_RADIANS, "true");
      
      SimpleConfig.setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY, "org.astrogrid.datacenter.queriers.pal.PalProxyPlugin");
      SimpleConfig.setProperty(PalProxyPlugin.PAL_TARGET, "http://astrogrid.roe.ac.uk:8080/pal-ssa");
      SimpleConfig.setProperty(UrlResourcePlugin.RESOURCE_PLUGIN_KEY, "org.astrogrid.datacenter.metadata.UrlResourcePlugin");
      SimpleConfig.setProperty(UrlResourcePlugin.METADATA_URL_LOC_KEY, "http://astrogrid.roe.ac.uk:8080/pal-ssa/GetMetadata?Resource=RdbmsMetadata");
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
      
      StringWriter sw = new StringWriter();
      Querier q = Querier.makeQuerier(Account.ANONYMOUS, SimpleQueryMaker.makeConeQuery(ra,dec,r, new WriterTarget(sw), ReturnTable.VOTABLE));
      manager.askQuerier(q);
      log.info("Checking results...");
      String s = sw.toString();
      System.out.println(s);
      Document results = DomHelper.newDocument(s);
      //assertIsVotable(results);
      long numResults = results.getElementsByTagName("TR").getLength();
      log.info("Number of results = "+numResults);
//    assertTrue(numResults > 0);
      return results;
   }

   public void testAdql1() throws Exception {   askAdqlFromFile("ssa-adql-simple.xml"); }
   
   /** Read ADQL input document, run query on dummy SQL plugin, and return VOTable document
    *
    * @param queryFile resource file of query
    */
   protected void askAdqlFromFile(String queryFile) throws Exception {
      assertNotNull(queryFile);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      assertNotNull("Could not open query file :" + queryFile,is);
      
      StringWriter sw = new StringWriter();
      Querier q = Querier.makeQuerier(Account.ANONYMOUS, AdqlQueryMaker.makeQuery(is, new WriterTarget(sw), ReturnTable.VOTABLE));
      
      manager.askQuerier(q);
      
      log.info("Checking results...");
      Document results = DomHelper.newDocument(sw.toString());
      //assertIsVotable(results);
      long numResults = results.getElementsByTagName("TR").getLength();
      log.info("Number of results = "+numResults);
   }
   
   
   /** Test Results  - could add tests to status updates here... */
   public void testResults() throws Exception  {
      
   }

   public void testMetadata() throws IOException {
      VoDescriptionServer.getVoDescription();
   }
   
   
   /** Test harness - runs tests
    */
   public static void main(String[] args) {
      junit.textui.TestRunner.run(SsaProxyPluginTest.class);
   }
   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(SsaProxyPluginTest.class);
   }
   
   
   
}


/*
 $Log: SsaProxyPluginTest.java,v $
 Revision 1.2  2004/10/13 01:31:34  mch
 Added screen dump of returned values.  Need to add better check

 Revision 1.1  2004/10/12 23:05:16  mch
 Seperated tests properly

 */
