/*$Id: DeployedServicesTest.java,v 1.4 2004/09/29 18:42:46 mch Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.deployment;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.sqlparser.Sql2Adql074;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;

/**
 * Tests to see if the real deployed services are up.  This isn't really an
 * integration test, but it seems a convenient place to have it run and notify
 * us if services go down
 *
 */
public class DeployedServicesTest extends TestCase {


   /** Runs a cone search on 6dF */
   public void test6dF() throws IOException, SAXException, IOException, ParserConfigurationException  {
     ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(
         Account.ANONYMOUS,
         "http://grendel12.roe.ac.uk:8080/pal-6df/services/AxisDataService05",
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);

      InputStream is = delegate.coneSearch(10,10,2);
      assertNotNull(is);
      DomHelper.newDocument(is);
   }
   
   /** Runs a simple adql search on SEC proxy on grendel12. No point in running
    * a cone search - Solar Event Catalogues don't do cones */
   public void testGrendelSecProxy() throws IOException, SAXException, IOException, ParserConfigurationException, ServiceException  {
     QuerySearcher delegate = DatacenterDelegateFactory.makeQuerySearcher(
         Account.ANONYMOUS,
         "http://grendel12.roe.ac.uk:8080/pal-sec/services/AxisDataService05",
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);

      String adqls = "SELECT * FROM sgas_event WHERE nar>9500 AND nar<9600";
      String adqlx = Sql2Adql074.translate(adqls);
      
      InputStream is = delegate.askQuery(new AdqlQuery(adqlx), ReturnTable.VOTABLE);
      assertNotNull(is);
      DomHelper.newDocument(is);
   }

   /** Runs a cone search on Vizier proxy on grendel12 */
   public void testGrendelVizierProxy() throws IOException, SAXException, IOException, ParserConfigurationException  {
     ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(
         Account.ANONYMOUS,
         "http://grendel12.roe.ac.uk:8080/pal-vizier/services/AxisDataService05",
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);

      InputStream is = delegate.coneSearch(10,10,2);
      assertNotNull(is);
      DomHelper.newDocument(is);
   }

   /** Runs a cone search on SSA */
   public void testSsa() throws IOException, SAXException, IOException, ParserConfigurationException  {
     ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(
         Account.ANONYMOUS,
         "http://astrogrid.roe.ac.uk:8080/pal-sss/services/AxisDataService05",
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);

      InputStream is = delegate.coneSearch(10,10,2);
      assertNotNull(is);
      DomHelper.newDocument(is);
   }

   /** Runs a cone search on INT-WFS */
   public void testIntWfs() throws IOException, SAXException, IOException, ParserConfigurationException  {
     ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(
         Account.ANONYMOUS,
         "http://ag01.ast.cam.ac.uk:8080/astrogrid-pal-Itn05_release/services/AxisDataService05",
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);

      InputStream is = delegate.coneSearch(10,10,2);
      assertNotNull(is);
      DomHelper.newDocument(is);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(new TestSuite(DeployedServicesTest.class));
   }
}


/*
$Log: DeployedServicesTest.java,v $
Revision 1.4  2004/09/29 18:42:46  mch
Changed SEC test

Revision 1.3  2004/09/29 16:58:34  mch
Added SEC & Vizier grendel12 proxy tests

Revision 1.2  2004/09/09 12:31:56  mch
Switched to using correct port

Revision 1.1  2004/09/09 11:18:45  mch
Moved DeployedServicesTest to separate package

Revision 1.1  2004/09/08 20:35:10  mch
Added tests against deployed services

Revision 1.1  2004/09/08 20:06:11  mch
Added metadat push test

 
*/
