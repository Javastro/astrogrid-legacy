/*$Id: DataServiceTest.java,v 1.5 2010/12/08 12:46:36 gtr Exp $
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
import junit.framework.TestCase;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.jobs.Job;
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.tableserver.VoTableTestHelper;
import org.astrogrid.tableserver.test.SampleStarsPlugin;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;

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
       Job.initialize();
       SampleStarsPlugin.initialise();
       
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
   * Tests the count query.
   */
  public void testCount() throws Throwable {
    assertEquals(16232L, server.askCount(TESTPrincipal, query2, this));
  }
  
}