/*$Id: IndexQueryFitsTest.java,v 1.3 2005/05/27 16:21:07 clq2 Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers.fits;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URISyntaxException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.fitsserver.fits.FitsQuerierPlugin;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.adql.AdqlQueryMaker;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.xmldb.client.XMLDBFactory;
import org.w3c.dom.Document;

/** Test the Fits processing classes
 */
public class IndexQueryFitsTest extends TestCase
{
   private static boolean uploadedFiles = false;
   private static boolean registeredDB = false;
   
   public IndexQueryFitsTest() {
       
   }

   public void setUp() throws Exception {
       super.setUp();
       System.out.println("Try to find exist.xml");
       ConfigFactory.getCommonConfig().setProperty("xmldb.uri", "xmldb:exist://");
       ConfigFactory.getCommonConfig().setProperty("xmldb.driver", "org.exist.xmldb.DatabaseImpl");
       ConfigFactory.getCommonConfig().setProperty("xmldb.query.service", "XQueryService");
       ConfigFactory.getCommonConfig().setProperty("xmldb.admin.user", "admin");
       ConfigFactory.getCommonConfig().setProperty("xmldb.admin.password", "");
       if(!registeredDB) {
           File fi = new File("target/test-classes/exist.xml");
           System.out.println("Got the File now try to register the db");
           if(fi != null) {
               XMLDBFactory.registerDB(fi.getAbsolutePath());
           }
           System.out.println("database registered now set the query plugin");
           registeredDB = true;
       }//if
       ConfigFactory.getCommonConfig().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY, FitsQuerierPlugin.class.getName());
   }

   /** Check to see the right plugin is made */
   public void testPluginClass() throws IOException, IOException, URISyntaxException {
      System.out.println("entered testPlugin");
      //Querier querier = Querier.makeQuerier(LoginAccount.ANONYMOUS, SimpleQueryMaker.makeConeQuery(300, 60, 12, new NullTarget(), ReturnTable.VOTABLE), this);
      
      //assertTrue("Plugin '"+querier.getPlugin()+"' not FitsQuerierPlugin", querier.getPlugin() instanceof FitsQuerierPlugin);
   }
   
   public void testQuery() throws Exception
   {
      System.out.println("entered testQuery");
      
      Document doc = askQueryFromFile("ADQLQueryForFits1ForIndex.xml");
      StringWriter sw = new StringWriter();
      Querier querier = Querier.makeQuerier(LoginAccount.ANONYMOUS, AdqlQueryMaker.makeQuery(doc.getDocumentElement(), new WriterTarget(sw), ReturnTable.VOTABLE), this);
      querier.ask();
      String results = sw.toString();
      System.out.println("THE RESULTS OF QUERY = " +  results);
  }
   
   protected Document askQueryFromFile(String queryFile) throws Exception {
       System.out.println("entered askQueryFromFile");

       assertNotNull(queryFile);
       InputStream is = this.getClass().getResourceAsStream(queryFile);
       
       assertNotNull("Could not open query file :" + queryFile,is);
       Document queryDoc = DomHelper.newDocument(is);
       
       //Document queryDoc = DomHelper.newDocument(new File(queryFile));
       return queryDoc;
   }


   
   public static Test suite()
   {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(IndexQueryFitsTest.class);
   }
   
   
   /**
    * Runs the test case.
    */

   public static void main(String args[]) throws IOException
   {
      junit.textui.TestRunner.run(suite());
   }

   
}


/*
 $Log: IndexQueryFitsTest.java,v $
 Revision 1.3  2005/05/27 16:21:07  clq2
 mchv_1

 Revision 1.2.14.2  2005/05/13 10:13:45  mch
 'some fixes'

 Revision 1.2.14.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.2  2005/03/22 22:47:29  KevinBenson
 changed back to commonconfig stuff.

 Revision 1.1  2005/03/22 10:04:11  KevinBenson
 New Tests for Indexgenerator

 Revision 1.1  2005/03/14 16:09:31  KevinBenson
 Fixed up some more tests for the IndexGenerator

 Revision 1.8  2005/03/11 14:50:59  KevinBenson
 added catch for parserconfigurationexception

 Revision 1.7  2005/03/10 16:42:55  mch
 Split fits, sql and xdb

 Revision 1.6  2005/03/10 14:01:35  KevinBenson
 new test for FitsQuerier

 Revision 1.4  2005/03/08 15:03:24  KevinBenson
 new stuff for Fits querier to work with an internal xml database

 Revision 1.3  2005/02/28 19:36:39  mch
 Fixes to tests

 Revision 1.2  2005/02/28 18:47:05  mch
 More compile fixes

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:25  mch
 Initial checkin

 Revision 1.8.2.3  2004/11/29 21:52:18  mch
 Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

 Revision 1.8.2.2  2004/11/23 11:55:06  mch
 renamved makeTarget methods

 Revision 1.8.2.1  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.8  2004/11/11 20:42:50  mch
 Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

 Revision 1.7  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.6  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.5.6.1  2004/11/02 19:41:26  mch
 Split TargetIndicator to indicator and maker

 Revision 1.5  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.4.2.2  2004/10/16 14:35:53  mch
 Forwardable null targets

 Revision 1.4.2.1  2004/10/15 19:59:06  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.4  2004/10/08 17:14:23  mch
 Clearer separation of metadata and querier plugins, and improvements to VoResource plugin mechanisms

 Revision 1.3  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.2  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:11:33  mch
 Moved server test directory to pal

 Revision 1.22  2004/09/08 17:51:49  mch
 Fixes to log and metadata views

 Revision 1.21  2004/09/01 12:10:58  mch
 added results.toHtml

 Revision 1.20  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.19  2004/08/19 08:32:48  mch
 Fix to target indicator

 Revision 1.18  2004/08/18 21:36:28  mch
 Added better error msg

 Revision 1.17  2004/08/18 18:44:12  mch
 Created metadata plugin service and added helper methods

 Revision 1.16  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.15  2004/03/14 04:13:16  mch
 Wrapped output target in TargetIndicator

 Revision 1.14  2004/03/13 23:38:56  mch
 Test fixes and better front-end JSP access

 Revision 1.13  2004/03/12 20:11:09  mch
 It05 Refactor (Client)

 Revision 1.12  2004/03/12 04:54:06  mch
 It05 MCH Refactor

 Revision 1.11  2004/03/09 21:05:57  mch
 Removed hugely long Fits tests

 Revision 1.10  2004/03/08 15:58:26  mch
 Fixes to ensure old ADQL interface works alongside new one and with old plugins

 Revision 1.9  2004/03/08 00:31:28  mch
 Split out webservice implementations for versioning

 Revision 1.8  2004/02/24 16:03:48  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.7  2004/02/16 23:07:05  mch
 Moved DummyQueriers to std server and switched to AttomConfig

 Revision 1.6  2004/01/23 11:14:09  nw
 altered to extend org.astrogrid.test.OptionalTestCase -
 means that these tests can be disabled as needed

 Revision 1.5  2004/01/14 16:00:13  nw
 tidied up switching out long tests.

 Revision 1.4  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.3.6.2  2004/01/08 09:43:40  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.3.6.1  2004/01/07 13:02:09  nw
 removed Community object, now using User object from common

 Revision 1.3  2003/12/03 19:37:03  mch
 Introduced DirectDelegate, fixed DummyQuerier

 Revision 1.2  2003/12/01 20:58:42  mch
 Abstracting coarse-grained plugin

 Revision 1.1  2003/11/28 20:00:24  mch
 Added cone search test


 */
