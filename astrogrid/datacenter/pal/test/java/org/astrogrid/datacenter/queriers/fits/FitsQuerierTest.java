/*$Id: FitsQuerierTest.java,v 1.3 2004/10/07 10:34:44 mch Exp $
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.fits.FitsTest;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.NullTarget;
import org.astrogrid.slinger.TargetIndicator;

/** Test the Fits processing classes
 */
public class FitsQuerierTest extends TestCase
{
   
   protected void setUp() throws Exception{
      SimpleConfig.setProperty(QuerierPluginFactory.PLUGIN_KEY, FitsQuerierPlugin.class.getName());
      
      //set up test index first
      String index = new FitsTest().generateTestIndex();

      File indexFile = new File("fitsIndex.xml");
      FileOutputStream out = new FileOutputStream(indexFile);
      out.write(index.getBytes());
      out.close();
      
      SimpleConfig.setProperty(FitsQuerierPlugin.FITS_INDEX_FILENAME, "fitsIndex.xml");
   }

   public void testPluginClass() throws IOException {
      Querier querier = Querier.makeQuerier(Account.ANONYMOUS, SimpleQueryMaker.makeConeQuery(300, 60, 12, new NullTarget(), ReturnTable.VOTABLE));
      
      assertTrue("Plugin '"+querier.getPlugin()+"' not FitsQuerierPlugin", querier.getPlugin() instanceof FitsQuerierPlugin);
   }
   
   public void testCone() throws IOException
   {
      StringWriter sw = new StringWriter();
      Querier querier = Querier.makeQuerier(Account.ANONYMOUS, SimpleQueryMaker.makeConeQuery(300, 60, 12, new NullTarget(), ReturnTable.VOTABLE));
      
      assertTrue("Plugin '"+querier.getPlugin()+"' not FitsQuerierPlugin", querier.getPlugin() instanceof FitsQuerierPlugin);
      
      querier.ask();
      
      //have a look in sw for results
   }

   
   public static Test suite()
   {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(FitsQuerierTest.class);
   }
   
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) throws IOException
   {
      org.astrogrid.log.Log.logToConsole();
      junit.textui.TestRunner.run(suite());
   }
   
}


/*
 $Log: FitsQuerierTest.java,v $
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
