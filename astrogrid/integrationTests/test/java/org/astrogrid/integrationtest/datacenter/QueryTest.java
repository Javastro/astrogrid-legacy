/*$Id: QueryTest.java,v 1.1 2004/03/22 17:23:06 mch Exp $
 * Created on 22-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.integrationtest.datacenter;

import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.RawSqlQuery;
import org.astrogrid.integrationtest.common.ConfManager;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** Exercises methods of the full query delegate
 *
 */
public class QueryTest extends TestCase {

   private static final Log log = LogFactory.getLog(QueryTest.class);
   
   protected QuerySearcher delegate;

   protected void setUp() throws Exception {
      String endpoint = ConfManager.getInstance().getStdDatacenterEndPoint();
      delegate = DatacenterDelegateFactory.makeQuerySearcher(
         Account.ANONYMOUS,
         endpoint,
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE
      );
      assertNotNull("delegate was null",delegate);
   }
   
   /** test we can get the metadata from the server. not much we can test here */
   public void testGetMetadata() throws Exception {
      String m = delegate.getMetadata();
      assertNotNull("metadata was null",m);
      Document doc = DomHelper.newDocument(m);
      assertNotNull("document in metadata was null",doc);
      NodeList voreg = doc.getElementsByTagName("VOResource");
      assertTrue("no voregistry element in metadata",voreg.getLength()>0); // check merlin dataserver has a voregistry entry.
   }
   
   /** do a blocking query */
   public void testAsk() throws Exception {
      InputStream results = delegate.askQuery(new ConeQuery(30,30,6), QuerySearcher.VOTABLE);
      assertNotNull("blocking query returned null results",results);
      
      Document votable = DomHelper.newDocument(results);
      assertNotNull("result votable document is null",votable);
      validateVOTableResult(votable.getDocumentElement());
   }
   
   /** Make sure SQL fails (freshly installed datacenter) */
   public void testSqlFail() throws IOException {
      try {
         delegate.askQuery(new RawSqlQuery("SELECT *"), QuerySearcher.VOTABLE);
         fail("Server should reject raw SQL");
      }
      catch (Exception e) {
         //do nothing, expected to fail
      }
   }
   
   /** Make sure SQL passes ?? */
   public void testSqlPass() {
   }

   /** test the result is a votable, and that it contains the expected data
    * @todo fill in.
    */
   private void validateVOTableResult(Element votable) {
      
   }
   
   
   public QueryTest(String arg0) {
      super(arg0);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(QueryTest.class);
   }
   
   
}


/*
$Log: QueryTest.java,v $
Revision 1.1  2004/03/22 17:23:06  mch
moved from old package name

Revision 1.5  2004/02/17 23:59:17  jdt
commented out lines killing the build, and made to conform to

coding stds

Revision 1.4  2004/02/16 18:02:14  jdt
removed a line that was breaking the build

Revision 1.3  2004/01/23 09:08:09  nw
added cone searcher test too

Revision 1.2  2004/01/22 16:16:40  nw
minor doc fix

Revision 1.1  2004/01/22 16:14:59  nw
added integration test for datacenter full searcher.
 
*/
