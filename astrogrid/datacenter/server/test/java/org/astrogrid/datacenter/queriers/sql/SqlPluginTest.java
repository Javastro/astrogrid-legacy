/*$Id: SqlPluginTest.java,v 1.2 2004/03/12 20:11:09 mch Exp $
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
import java.io.InputStreamReader;
import java.io.StringWriter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.test.DummySqlPlugin;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.RawSqlQuery;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** test out the vanilla sql querier on the Dummy SQL Plugin
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 * @author mch
 *
 */
public class SqlPluginTest extends ServerTestCase {
   
   
   public SqlPluginTest(String arg0) {
      super(arg0);
   }

   QuerierManager manager = new QuerierManager("SqlQuerierTest");
   
   /**
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();

      DummySqlPlugin.populateDb();
      DummySqlPlugin.initConfig();
      
   }
   
   public void testCone() throws Exception {
      StringWriter sw = new StringWriter();
      Querier q = Querier.makeQuerier(Account.ANONYMOUS, new ConeQuery(30,30,6), sw, QueryResults.FORMAT_VOTABLE);
      manager.askQuerier(q);
      Document results = DomHelper.newDocument(sw.toString());
      assertIsVotable(results);
   }
   
   public void testAdql1() throws Exception {
      askAdqlFromFile("sql-querier-test-2.xml");
   }
   
   public void testAdl2() throws Exception {
      askAdqlFromFile("sql-querier-test-3.xml");
   }

   public void testAdl3() throws Exception {
      askAdqlFromFile("sql-querier-test-3.xml");
   }
   
   /** Read ADQL input document, run query on dummy SQL plugin, and return VOTable document
    *
    * @param queryFile resource file of query
    */
   protected void askAdqlFromFile(String queryFile) throws Exception {
      assertNotNull(queryFile);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      assertNotNull("Could not open query file :" + queryFile,is);
      Select select = Select.unmarshalSelect(new InputStreamReader(is));
      assertNotNull(select);

      StringWriter sw = new StringWriter();
      Querier q = Querier.makeQuerier(Account.ANONYMOUS, new AdqlQuery(select), sw, QueryResults.FORMAT_VOTABLE);

      manager.askQuerier(q);
      
      Document results = DomHelper.newDocument(sw.toString());
      assertIsVotable(results);
   }
   
   
   public void testSQLPassthru() throws Exception {
      //should fail - default should be off
      try {
         doSql();
         
         fail("Should have failed not allowing passthrough");
      }
      catch (IllegalArgumentException iae) {} //should throw this
      
      SimpleConfig.setProperty(Querier.SQL_PASSTHROUGH_ENABLED,"true");
      
      doSql();
   }

   private void doSql() throws Exception {
      StringWriter sw = new StringWriter();
      Querier q = Querier.makeQuerier(Account.ANONYMOUS, new RawSqlQuery("select * from SampleStars"), sw, QueryResults.FORMAT_VOTABLE);

      manager.askQuerier(q);
   
      Document results = DomHelper.newDocument(sw.toString());
      assertIsVotable(results);
   }
   
    /** Test Results  - could add tests to status updates here... */
    public void testResults() throws Exception  {

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


/*
 $Log: SqlPluginTest.java,v $
 Revision 1.2  2004/03/12 20:11:09  mch
 It05 Refactor (Client)

 Revision 1.1  2004/03/12 04:54:06  mch
 It05 MCH Refactor

 Revision 1.12  2004/03/08 00:31:28  mch
 Split out webservice implementations for versioning

 Revision 1.11  2004/02/24 16:03:48  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.10  2004/02/16 23:07:05  mch
 Moved DummyQueriers to std server and switched to AttomConfig

 Revision 1.9  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.8.10.3  2004/01/08 15:37:27  nw
 added tests for SQL passthru

 Revision 1.8.10.2  2004/01/08 09:43:40  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.8.10.1  2004/01/07 11:51:07  nw
 found out how to get wsdl to generate nice java class names.
 Replaced _query with Query throughout sources.

 Revision 1.8  2003/12/01 20:58:42  mch
 Abstracting coarse-grained plugin

 Revision 1.7  2003/12/01 16:44:11  nw
 dropped QueryId, back to string

 Revision 1.6  2003/11/28 16:10:30  nw
 finished plugin-rewrite.
 added tests to cover plugin system.
 cleaned up querier & queriermanager. tested

 Revision 1.5  2003/11/27 17:28:09  nw
 finished plugin-refactoring

 Revision 1.4  2003/11/27 00:52:58  nw
 refactored to introduce plugin-back end and translator maps.
 interfaces in place. still broken code in places.

 Revision 1.3  2003/11/21 17:37:56  nw
 made a start tidying up the server.
 reduced the number of failing tests
 found commented out code

 Revision 1.2  2003/11/20 15:45:47  nw
 started looking at tese tests

 Revision 1.1  2003/11/14 00:38:29  mch
 Code restructure

 Revision 1.13  2003/11/05 18:54:43  mch
 Build fixes for change to SOAPy Beans and new delegates

 Revision 1.12  2003/09/25 01:19:33  nw
 updated to fit with hsqldb test fixes

 Revision 1.11  2003/09/22 16:52:12  mch
 Fixes for changes to posts results to dummy myspace

 Revision 1.10  2003/09/19 12:01:34  nw
 fixed flakiness in db tests

 Revision 1.9  2003/09/17 14:53:02  nw
 tidied imports

 Revision 1.8  2003/09/11 11:06:10  nw
 fixed to work with new query format

 Revision 1.7  2003/09/10 18:58:56  mch
 Preparing to generalise Query

 Revision 1.6  2003/09/10 14:48:35  nw
 fixed breaking tests

 Revision 1.5  2003/09/10 10:01:38  nw
 fixed setup.

 Revision 1.4  2003/09/08 16:43:50  mch
 Removed toInputStream() from QueryResults as we don't really know what it's for yet

 Revision 1.3  2003/09/07 18:58:58  mch
 Updated tests for weekends changes to main code (mostly threaded queries, typesafe ServiceStatus)

 Revision 1.2  2003/09/05 13:24:13  nw
 now uses close method

 Revision 1.1  2003/09/05 01:05:32  nw
 added testing of SQLQuerier over an in-memory Hsql database,

 relies on hsqldb.jar (added to project.xml)

 */
