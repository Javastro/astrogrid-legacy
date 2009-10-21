/*$Id: DataServiceTest.java,v 1.2 2009/10/21 19:00:59 gtr Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.service;
import java.io.StringWriter;
import java.security.Principal;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.queriers.status.QuerierAborted;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.dataservice.service.DataServiceStatus;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.NullTarget;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.status.TaskStatus;
import org.astrogrid.tableserver.VoTableTestHelper;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/** Tests that the DataService can run queries - ie it tests right from just
 * behind the web (or whatever) interface through to the dummy sql database
 * at the back, including copying stuff to myspace and getting results back
 * to the front end.
 */
public class DataServiceTest extends TestCase {

   protected DataServer server;

   protected Query query1;
   protected Query query1c;
   protected Query query2;
   protected Query query2c;
   protected Query query3;
   protected Query query3c;
   
   protected StringWriter sw1 = new StringWriter();
   protected StringWriter sw1c = new StringWriter();
   protected StringWriter sw2 = new StringWriter();
   protected StringWriter sw2c = new StringWriter();
   protected StringWriter sw3 = new StringWriter();
   protected StringWriter sw3c = new StringWriter();

   public Principal TESTPrincipal = new LoginAccount("UnitTester", "test.org");
   
   public DataServiceTest(String arg0) {
        super(arg0);
    }

   @Override
    protected void setUp() throws Exception {
       super.setUp();
       SimpleConfig.setProperty("datacenter.cache.directory", "target");
       SampleStarsPlugin.initConfig();
       
       server = new DataServer();
       String catalogID = ConfigFactory.getCommonConfig().getString(
             "datacenter.self-test.catalog", null);
       String tableID = ConfigFactory.getCommonConfig().getString(
             "datacenter.self-test.table", null);
       String catalogName = TableMetaDocInterpreter.getCatalogNameForID(
             catalogID);
       String tableName = TableMetaDocInterpreter.getTableNameForID(
             catalogID,tableID);

       
      query1 = SimpleQueryMaker.makeTestQuery(tableName,
         new ReturnTable(new WriterTarget(sw1), ReturnTable.VOTABLE));

      query1c = SimpleQueryMaker.makeTestQuery(catalogName, tableName,
         new ReturnTable(new WriterTarget(sw1c), ReturnTable.VOTABLE));
       
      query2 = SimpleQueryMaker.makeTestQuery(tableName,
         new ReturnTable(new WriterTarget(sw2), ReturnTable.VOTABLE));

      query2c = SimpleQueryMaker.makeTestQuery(catalogName, tableName,
         new ReturnTable(new WriterTarget(sw2c), ReturnTable.VOTABLE));
      
      query3 = SimpleQueryMaker.makeTestQuery(tableName,
         new ReturnTable(new WriterTarget(sw3), ReturnTable.VOTABLE));

      query3c = SimpleQueryMaker.makeTestQuery(catalogName, tableName,
         new ReturnTable(new WriterTarget(sw3c), ReturnTable.VOTABLE));
    }

    public void testConeSearch1() throws Throwable 
    {
      // This tests a small-radius conesearch
       StringWriter sw = new StringWriter();
       Query coneQuery = new Query(
             "CatName_SampleStarsCat", "TabName_SampleStars", "deg",
             "ColName_RA", "ColName_Dec",
             50.0, 50.0, 0.1,
             new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE));
       server.askQuery(TESTPrincipal, coneQuery, this);
       String results = sw.toString();
       VoTableTestHelper.assertIsVotable(results);
    }
    public void testConeSearch2() throws Throwable 
    {
      // This tests a larger-radius conesearch
       StringWriter sw = new StringWriter();
       Query coneQuery = new Query(
             "CatName_SampleStarsCat", "TabName_SampleStars", "deg",
             "ColName_RA", "ColName_Dec",
             50.0, 50.0, 0.6,
             new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE));
       server.askQuery(TESTPrincipal, coneQuery, this);
       String results = sw.toString();
       VoTableTestHelper.assertIsVotable(results);
    }
    

   /**
    * Tests the query service by itself
    */
   public void testQueryService() throws Throwable
   {
      //submit query
      //StringWriter sw = new StringWriter();
      //query1.setResultsDef(new ReturnTable(new WriterTarget(sw), "VOTABLE"));
      server.askQuery(TESTPrincipal, query1, this);
      String result = sw1.toString();
      assertNotNull(result);
      VoTableTestHelper.assertIsVotable(result);
      
   }
   public void testQueryServiceAll() throws Throwable
   {
      server.askQuery(TESTPrincipal, query1, this);
      server.askQuery(TESTPrincipal, query2, this);
      server.askQuery(TESTPrincipal, query3, this);
      server.askQuery(TESTPrincipal, query1c, this);
      server.askQuery(TESTPrincipal, query2c, this);
      server.askQuery(TESTPrincipal, query3c, this);

      String result1 = sw1.toString();
      assertNotNull(result1);
      VoTableTestHelper.assertIsVotable(result1);
      String result1c = sw1c.toString();
      assertNotNull(result1c);
      VoTableTestHelper.assertIsVotable(result1c);

      String result2 = sw2.toString();
      assertNotNull(result2);
      VoTableTestHelper.assertIsVotable(result2);
      String result2c = sw2c.toString();
      assertNotNull(result2c);
      VoTableTestHelper.assertIsVotable(result2c);
      
      String result3 = sw3.toString();
      assertNotNull(result3);
      VoTableTestHelper.assertIsVotable(result3);
      String result3c = sw3c.toString();
      assertNotNull(result3c);
      VoTableTestHelper.assertIsVotable(result3c);
   }

   /**
    * Tests the status */
   public void testStatus() throws Throwable {
      //submit queries
      query1.setResultsDef(new ReturnTable(new NullTarget(), "VOTABLE"));
      server.submitQuery(TESTPrincipal, query1, this);

      query2.setResultsDef(new ReturnTable(new NullTarget(), "VOTABLE"));
      server.submitQuery(TESTPrincipal, query2, this);

      query3.setResultsDef(new ReturnTable(new NullTarget(), "VOTABLE"));
      server.submitQuery(TESTPrincipal, query3, this);

      DataServiceStatus status = DataServer.getStatus();
      TaskStatus[] tasks = status.getTasks();
      assertTrue(tasks.length>1);
      
//      new ServerStatusHtmlRenderer().writeHtmlStatus(new StringWriter(), status.getServiceStatus(), null);
   }

   /** Tests the count return */
   public void testCount() throws Throwable {
      
      long expected = 16232;
      long count = server.askCount(TESTPrincipal, query2, this);
      
      assertTrue("testCount didn't have expect count of 16232",count == 16232);
      /*
      //now do same query but get full results (to check count is equal)
      StringWriter sw = new StringWriter();
      query2.setResultsDef(new ReturnTable(new WriterTarget(sw)));
      server.askQuery(TESTPrincipal, query2, this);
      
      Document votDoc = DomHelper.newDocument(sw.toString());
      NodeList rows = votDoc.getElementsByTagNameNS("*", "TR");
      
      assertTrue("askQuery returns different result ("+rows.getLength()+") to askCount ("+count+")", rows.getLength() == count);
      */
   }
    
   // KEA: THIS TEST IS TIME-SENSITIVE AND SOMETIMES FAILS SPURIOUSLY - 
   // DISABLING IT FOR THE TIME BEING
   /*
    public void testAbort() throws Throwable {
      Query query = SimpleQueryMaker.makeConeQuery(30,30,180);
      query.setResultsDef(new ReturnTable(new NullTarget()));
      String qid = server.submitQuery(TESTPrincipal, query, this);
      Thread.currentThread().sleep(500); //give a moment for the other thread to kick off
      server.abortQuery(TESTPrincipal, qid);
      Thread.currentThread().sleep(5000); //give it time
      QuerierStatus status = server.getQueryStatus(TESTPrincipal, qid);
      System.out.println("QUERIER STATUS IS "+status.toString());
      assertTrue(status instanceof QuerierAborted);
      assertTrue(status.isFinished());
      assertTrue(status.getStage().equals(status.ABORTED));
   }
   */

 

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(DataServiceTest.class);
    }

}


/*
$Log: DataServiceTest.java,v $
Revision 1.2  2009/10/21 19:00:59  gtr
V2009.1.01, merged.

Revision 1.1.1.1.2.1  2009/09/18 13:39:02  gtr
datacenter.cache.directory is set to support use of the job database.

Revision 1.1.1.1  2009/05/13 13:20:58  gtr


Revision 1.8  2007/06/08 13:16:12  clq2
KEA-PAL-2169

Revision 1.7.2.2  2007/05/29 13:54:39  kea
Still working on new metadoc stuff.

Revision 1.7.2.1  2007/05/18 16:34:13  kea
Still working on new metadoc / multi conesearch.

Revision 1.7  2007/03/02 13:33:07  kea
Disabling flaky test.

Revision 1.6  2006/06/15 16:50:10  clq2
PAL_KEA_1612

Revision 1.5.64.5  2006/05/30 15:20:52  kea
Still working.

Revision 1.5.64.4  2006/05/10 13:25:22  kea
Conesearch and HSQLDB fixes/enhancements;  moving properties to web.xml
like other AG services.

Revision 1.5.64.3  2006/04/25 15:37:26  kea
Fixing unit tests, except conesearch-related ones (not implemented yet).

Revision 1.5.64.2  2006/04/21 12:10:37  kea
Renamed ReturnSimple back to ReturnTable (since it is indeed intended
for returning tables).

Revision 1.5.64.1  2006/04/20 15:08:28  kea
More moving sideways.

Revision 1.5  2005/05/27 16:21:21  clq2
mchv_1

Revision 1.4.16.5  2005/05/13 16:56:32  mch
'some changes'

Revision 1.4.16.4  2005/05/13 10:13:45  mch
'some fixes'

Revision 1.4.16.3  2005/05/04 10:24:33  mch
fixes to tests

Revision 1.4.16.2  2005/05/03 19:35:01  mch
fixes to tests

Revision 1.4.16.1  2005/04/21 17:20:51  mch
Fixes to output types

Revision 1.4  2005/03/21 18:45:55  mch
Naughty big lump of changes

Revision 1.3  2005/03/10 16:42:55  mch
Split fits, sql and xdb

Revision 1.2  2005/02/28 18:47:05  mch
More compile fixes

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:25  mch
Initial checkin

Revision 1.10.2.8  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.10.2.7  2004/12/07 21:21:09  mch
Fixes after a days integration testing

Revision 1.10.2.6  2004/12/03 11:58:57  mch
various fixes while at data mining

Revision 1.10.2.5  2004/11/29 21:52:18  mch
Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

Revision 1.10.2.4  2004/11/26 18:17:21  mch
More status persisting, browsing and aborting

Revision 1.10.2.3  2004/11/25 18:33:43  mch
more status (incl persisting) more tablewriting lots of fixes

Revision 1.10.2.2  2004/11/23 11:55:06  mch
renamved makeTarget methods

Revision 1.10.2.1  2004/11/22 00:57:16  mch
New interfaces for SIAP etc and new slinger package

Revision 1.10  2004/11/11 20:42:50  mch
Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

Revision 1.9  2004/11/10 22:01:50  mch
skynode starts and some fixes

Revision 1.8  2004/11/09 17:42:22  mch
Fixes to tests after fixes for demos, incl adding closable to targetIndicators

Revision 1.7  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.6.8.2  2004/11/02 19:41:26  mch
Split TargetIndicator to indicator and maker

Revision 1.6.8.1  2004/10/27 00:43:40  mch
Started adding getCount, some resource fixes, some jsps

Revision 1.6  2004/10/08 15:21:42  mch
updates for status

Revision 1.5  2004/10/07 10:34:44  mch
Fixes to Cone maker functions and reading/writing String comparisons from Query

Revision 1.4  2004/10/06 21:12:17  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.3  2004/10/05 15:27:35  mch
Minor changes to server status display

Revision 1.2  2004/10/01 18:04:59  mch
Some factoring out of status stuff, added monitor page

Revision 1.1  2004/09/28 15:11:33  mch
Moved server test directory to pal

Revision 1.14  2004/09/08 16:21:04  mch
Added SampleStarsPlugin init

Revision 1.13  2004/09/08 16:12:32  mch
Fix tests to use ADQL 0.7.4

Revision 1.12  2004/09/06 20:23:00  mch
Replaced metadata generators/servers with plugin mechanism. Added Authority plugin

Revision 1.11  2004/09/01 13:19:54  mch
Added sample stars metadata

Revision 1.10  2004/08/27 17:47:19  mch
Added first servlet; started making more use of ReturnSpec

Revision 1.9  2004/08/25 23:38:34  mch
(Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

Revision 1.8  2004/08/18 18:44:12  mch
Created metadata plugin service and added helper methods

Revision 1.7  2004/08/17 20:19:36  mch
Moved TargetIndicator to client

Revision 1.6  2004/07/06 20:43:17  mch
Fixes to tests

Revision 1.5  2004/03/14 04:13:16  mch
Wrapped output target in TargetIndicator

Revision 1.4  2004/03/14 00:39:30  mch
Added error trapping to DataServer and setting Querier error status

Revision 1.3  2004/03/13 23:38:56  mch
Test fixes and better front-end JSP access

Revision 1.2  2004/03/12 20:11:09  mch
It05 Refactor (Client)

Revision 1.1  2004/03/12 04:54:07  mch
It05 MCH Refactor

Revision 1.20  2004/03/08 00:31:28  mch
Split out webservice implementations for versioning

Revision 1.19  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

Revision 1.18  2004/03/06 19:34:21  mch
Merged in mostly support code (eg web query form) changes

Revision 1.16  2004/02/24 16:03:48  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.15  2004/02/17 03:38:40  mch
Various fixes for demo

Revision 1.14  2004/02/16 23:07:05  mch
Moved DummyQueriers to std server and switched to AttomConfig

Revision 1.13  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.12.10.2  2004/01/08 09:43:40  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.12.10.1  2004/01/07 11:51:07  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.12  2003/12/02 18:00:03  mch
Moved MySpaceDummyDelegate

Revision 1.11  2003/12/01 20:58:42  mch
Abstracting coarse-grained plugin

Revision 1.10  2003/12/01 16:44:11  nw
dropped QueryId, back to string

Revision 1.9  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.8  2003/11/27 17:28:09  nw
finished plugin-refactoring

Revision 1.7  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.

Revision 1.6  2003/11/25 14:21:49  mch
Extracted Querier from DatabaseQuerier in prep for FITS querying

Revision 1.5  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.4  2003/11/18 14:36:59  nw
temporarily commented out references to MySpaceDummyDelegate, so that the sustem will build

Revision 1.3  2003/11/17 15:42:03  mch
Package movements

Revision 1.2  2003/11/17 12:16:33  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.18  2003/11/06 22:10:04  mch
Work with both real and dummy myspace

Revision 1.16  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.15  2003/10/13 14:08:10  nw
little fix to changed class name.

Revision 1.14  2003/09/24 21:11:14  nw
altered to fit with new configuration and behaviour

Revision 1.13  2003/09/22 16:52:12  mch
Fixes for changes to posts results to dummy myspace

Revision 1.12  2003/09/19 12:02:00  nw
fixed flakiness in db tests

Revision 1.11  2003/09/17 14:53:02  nw
tidied imports

Revision 1.10  2003/09/15 22:09:00  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.9  2003/09/15 21:28:09  mch
Listener/state refactoring.

Revision 1.8  2003/09/10 17:57:52  mch
Tidied xml doc helpers and fixed (?) job/web listeners

Revision 1.7  2003/09/10 14:48:35  nw
fixed breaking tests

Revision 1.6  2003/09/10 12:16:44  mch
Changes to make web interface more consistent

Revision 1.5  2003/09/10 10:02:17  nw
added key for hsqldb driver

Revision 1.4  2003/09/09 17:58:38  mch
Moved ServiceStatus

Revision 1.3  2003/09/08 19:18:55  mch
Workspace constructor now throws IOException

Revision 1.2  2003/09/07 18:58:58  mch
Updated tests for weekends changes to main code (mostly threaded queries, typesafe ServiceStatus)

Revision 1.1  2003/09/05 13:25:29  nw
added end-to-end test of DataQueryService

*/
