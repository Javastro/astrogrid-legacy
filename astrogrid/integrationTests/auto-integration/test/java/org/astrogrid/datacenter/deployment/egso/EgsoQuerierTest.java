/*$Id: EgsoQuerierTest.java,v 1.1 2004/10/05 16:45:28 mch Exp $
 * Created on 01-Dec-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.deployment.egso;

import java.io.InputStream;
import java.io.StringWriter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.returns.TargetIndicator;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Dec-2003
 *
 */
public class EgsoQuerierTest extends TestCase {
   
   QuerierManager manager = QuerierManager.getManager("SqlQuerierTest");
   
   public EgsoQuerierTest(String arg0) {
   }
   
   public void testAdql1() throws Exception {
      askAdqlFromFile("sql-querier-test-5.xml");
   }

   public void testAdql2() throws Exception {
    askAdqlFromFile("sql-querier-test-3.xml");
   }
   
   public void testAdql3() throws Exception {
    askAdqlFromFile("sql-querier-test-3.xml");
   }
   
   public void testMetaAll() throws Exception {
    askAdqlFromFile("meta-all.xml");
   }
   
   public void testNamedTarget() throws Exception {
    askAdqlFromFile("named-target.xml");
   }
   
   /** Read ADQL input document, run query on dummy SQL plugin, and return VOTable document
    *
    * @param queryFile resource filename of query
    */
   protected void askAdqlFromFile(String queryFile) throws Exception {
      
      assertNotNull(queryFile);
      InputStream is = this.getClass().getResourceAsStream(queryFile);
      assertNotNull("Could not open query file :" + queryFile,is);
      
      StringWriter sw = new StringWriter();
      Querier q = Querier.makeQuerier(Account.ANONYMOUS, new AdqlQuery(is), new TargetIndicator(sw), ReturnTable.VOTABLE);
      
      manager.askQuerier(q);
      
      Document results = DomHelper.newDocument(sw.toString());
      AstrogridAssert.assertVotable(results);
   }
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(EgsoQuerierTest.class);
   }
   
   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(EgsoQuerierTest.class);
   }
   
}


/*
 $Log: EgsoQuerierTest.java,v $
 Revision 1.1  2004/10/05 16:45:28  mch
 Moved proxy tests to integration tests from unit tests

 Revision 1.8  2004/10/01 18:04:58  mch
 Some factoring out of status stuff, added monitor page

 Revision 1.7  2004/09/29 13:55:27  mch
 Updated ADQL versions

 Revision 1.6  2004/09/29 13:37:36  mch
 Removed obsolete ADQL 0.5

 Revision 1.5  2004/09/02 12:44:35  mch
 Fixed FORMAT_VOTABLEs

 Revision 1.4  2004/08/27 14:32:35  KevinBenson
 class was placed at another package level, which caused a compile bug

 Revision 1.3  2004/08/19 17:50:22  mch
 Fix for TargetIndicator move

 Revision 1.2  2004/07/26 13:50:25  KevinBenson
 Small test case that goes out and queries sec based on an adql query

 Revision 1.1  2004/07/07 09:17:40  KevinBenson
 New SEC/EGSO proxy to query there web service on the Solar Event Catalog

 Revision 1.2  2004/03/16 01:32:35  mch
 Fixed for cahnges to code to work with new plugins

 Revision 1.1  2003/12/01 16:51:04  nw
 added tests for cds spi
 
 */
