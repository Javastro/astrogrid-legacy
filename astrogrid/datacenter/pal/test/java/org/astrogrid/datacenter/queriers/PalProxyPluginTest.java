/*
 * $Id: PalProxyPluginTest.java,v 1.2 2004/11/09 17:42:22 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.pal.PalProxyPlugin;
import org.astrogrid.datacenter.queriers.sql.StdSqlMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.slinger.targets.TargetMaker;
import org.xml.sax.SAXException;

/**
 * Tests the pal proxy plugin against SSA (tsk tsk)
 * @author M Hill
 */

public class PalProxyPluginTest extends TestCase {

   public void setUp() {
      SimpleConfig.getSingleton().setProperty(PalProxyPlugin.PAL_TARGET, "http://astrogrid.roe.ac.uk:8080/pal-ssa");
      SimpleConfig.getSingleton().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY, "org.astrogrid.datacenter.queriers.pal.PalProxyPlugin");
      SimpleConfig.getSingleton().setProperty(StdSqlMaker.CONE_SEARCH_TABLE_KEY,"SOURCE");
   }
   
   public void testServletAccess() throws IOException, SAXException, SQLException {

      StringWriter results = new StringWriter();
      Query query = SimpleQueryMaker.makeConeQuery(20,-89.9,0.2, TargetMaker.makeIndicator(results));
      Querier querier = new Querier(Account.ANONYMOUS, query);
      querier.ask();

      String s = results.toString();
      System.out.print(s);
   }

   
   /**
    * Assembles and returns a test suite made up of all the testXxxx() methods
    * of this class.
    */
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(PalProxyPluginTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

