/*$Id: VizierTest.java,v 1.1 2004/11/03 05:20:50 mch Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.deployed.vizier;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.transform.TransformerException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.impl.cds.VizierQuerierPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.query.SqlQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.TargetMaker;
import org.xml.sax.SAXException;

/**
 * Test querying using ADQL against std PAL
 *
 */
public class VizierTest extends TestCase {
   
   public void setUp() {
      SimpleConfig.getSingleton().setProperty("datacenter.querier.plugin", "org.astrogrid.datacenter.impl.cds.VizierQuerierPlugin");
   }
   
   public void testVizierPluginAdql() throws ServiceException, SAXException, IOException, TransformerException, ParserConfigurationException {

      askPlugin( AdqlQueryMaker.makeQuery(VizierTest.class.getResourceAsStream("SimpleVizierCircle-adql074.xml")) );
   }

   public void testVizierPluginCone() throws ServiceException, SAXException, IOException, TransformerException, ParserConfigurationException {

      askPlugin(SimpleQueryMaker.makeConeQuery(12, 20, 0.1));
   }

   public void testVizierPluginSql1() throws ServiceException, SAXException, IOException, TransformerException, ParserConfigurationException {

      askPlugin(SqlQueryMaker.makeQuery("SELECT * FROM DIXON WHERE Wavelength = 'optical' AND ra = 12 and dec = 20 and radius = 1 and unit = 'arcmin' and text = 'Dixon'"));
   }
   
   public void testVizierPluginSql2() throws ServiceException, SAXException, IOException, TransformerException, ParserConfigurationException {

      askPlugin(SqlQueryMaker.makeQuery("SELECT * FROM DIXON WHERE target='m31' and radius = 1 and unit = 'arcmin' and text = 'Dixon'"));
   }

   public void testVizierPluginSql3() throws ServiceException, SAXException, IOException, TransformerException, ParserConfigurationException {

      askPlugin(SqlQueryMaker.makeQuery("SELECT * FROM DIXON WHERE Wavelength = 'optical' AND target='m31' and radius = 1 and unit = 'arcmin' and text = 'Dixon'"));
   }

   public void askPlugin(Query query) throws IOException  {

      StringWriter responseContainer = new StringWriter();

      query.setResultsDef(new ReturnTable(TargetMaker.makeIndicator(responseContainer), QuerySearcher.VOTABLE));
      
      Querier querier = Querier.makeQuerier(Account.ANONYMOUS, query);
      
      VizierQuerierPlugin plugin = new VizierQuerierPlugin();
      plugin.askQuery(querier.getUser(), query, querier);

      String response = responseContainer.toString();

   }
   
   /*
   public void testVizierProxyAdql() throws ServiceException, SAXException, IOException, TransformerException, ParserConfigurationException {
      //load query
      System.out.println("Start testAdqlSearchForVizier");
      Query query = AdqlQueryMaker.makeQuery(VizierTest.class.getResourceAsStream("SimpleVizierCircle-adql074.xml"));
      query.getResultsDef().setFormat(QuerySearcher.VOTABLE);

      QuerySearcher delegate = DatacenterDelegateFactory.makeQuerySearcher(Account.ANONYMOUS,PAL_v05_VIZIER_ENDPOINT,DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);
      
      InputStream results = delegate.askQuery(query);
      Document vot = assertVotable(results);
      
      DomHelper.DocumentToStream(vot,System.out);
      System.out.println("End testAdqlSearchForVizier");
   }
   
   /**
    * Runs a cone search
    *
   public void testVizierProxyCone() throws SAXException, IOException, TransformerException, ParserConfigurationException {
      ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(Account.ANONYMOUS, PAL_v05_VIZIER_ENDPOINT, DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);
      
      InputStream is = delegate.coneSearch(30,30,6);
//      assertVotable(is);
   }
    */
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(new TestSuite(VizierTest.class));
   }
}


/*
 $Log: VizierTest.java,v $
 Revision 1.1  2004/11/03 05:20:50  mch
 Moved datacenter deployment tests out of standard tests

 Revision 1.1.2.1  2004/11/02 21:51:03  mch
 Moved deployment tests here - not exactly right either

 Revision 1.1  2004/10/12 23:05:16  mch
 Seperated tests properly

 Revision 1.3  2004/10/06 22:03:45  mch
 Following Query model changes in PAL

 Revision 1.2  2004/09/08 13:58:48  mch
 Separated out tests by datacenter and added some

 Revision 1.1  2004/09/02 12:33:49  mch
 Added better tests and reporting

 Revision 1.6  2004/09/02 01:33:48  nw
 added asssertions that valid VOTables are returned.

 Revision 1.5  2004/08/30 22:11:46  KevinBenson
 added a little more for a vizier test

 Revision 1.4  2004/08/03 13:41:29  KevinBenson
 result of a merge with Itn06_case3 to change to using registry-client-lite and add some more int-test for fits and sec datacenter

 Revision 1.3.42.2  2004/07/30 13:04:41  KevinBenson
 changed hardcoded urls to the appropriate endpoint.  Also added in the fits stuff

 Revision 1.3.42.1  2004/07/30 12:44:45  KevinBenson
 Added the Fits int test to it

 Revision 1.3  2004/05/13 12:25:04  mch
 Fixes to create user, and switched to mostly testing it05 interface

 Revision 1.2  2004/04/26 12:16:25  nw
 fixed static suite() method

 Revision 1.1  2004/04/26 09:05:10  mch
 Added adql test

 Revision 1.3  2004/04/16 15:55:08  mch
 added alltests

 Revision 1.2  2004/04/16 15:17:14  mch
 Copied in from integration tests

 Revision 1.1  2004/04/16 15:14:54  mch
 Auto cone test

 Revision 1.1  2004/03/22 17:23:06  mch
 moved from old package name

 Revision 1.2  2004/02/17 23:59:17  jdt
 commented out lines killing the build, and made to conform to

 coding stds

 Revision 1.1  2004/01/23 09:08:09  nw
 added cone searcher test too
 
 */
