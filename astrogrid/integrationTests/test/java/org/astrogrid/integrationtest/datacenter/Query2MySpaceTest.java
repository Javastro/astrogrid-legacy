/*$Id: Query2MySpaceTest.java,v 1.1 2004/03/22 17:23:06 mch Exp $
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

import java.net.URL;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.integrationtest.common.ConfManager;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * Tests that queries are carried out OK and sent to myspace correctly
 *
 */
public class Query2MySpaceTest extends TestCase {

   private static final Log log = LogFactory.getLog(QueryTest.class);
   
   protected QuerySearcher delegate;
   protected URL myspaceEndpoint;
   private static final String resultsPath = "avodemo@test.astrogrid.org/serv1/votable/autoIntegrationTest.results";

   protected void setUp() throws Exception {
      String endpoint = ConfManager.getInstance().getStdDatacenterEndPoint();
      delegate = DatacenterDelegateFactory.makeQuerySearcher(
         Account.ANONYMOUS,
         endpoint,
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE
      );
      assertNotNull("delegate was null",delegate);
   }
   
   /** do a non-blocking query, where results are left in myspace.
    * retreive from myspace, check they're what we expect
    */
   public void testSubmit() throws Exception {
      
      Agsl resultsTarget = new Agsl(myspaceEndpoint.toString(), resultsPath);
      
      String queryId = delegate.submitQuery(
         new ConeQuery(30,30,6),
         resultsTarget,
         QuerySearcher.VOTABLE
      );
         
      String stat = delegate.getStatus(queryId);

      //wait until query finishes
      do {
         stat = delegate.getStatus(queryId);
      } while (!stat.equals(QueryState.FINISHED) && (!stat.equals(QueryState.ERROR))); /* need some extra timout here too */
 
      //see if results are in expected myspace location
      StoreClient store = StoreDelegateFactory.createDelegate(Account.ANONYMOUS.toUser(), resultsTarget);
      StoreFile file = store.getFile(resultsTarget.getPath());
      
      Document resultDoc = DomHelper.newDocument(store.getStream(resultsTarget.getPath()));
      assertNotNull("null result document",resultDoc);
      
   }
   
   public Query2MySpaceTest(String arg0) {
      super(arg0);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(QueryTest.class);
   }
   
   
}


/*
$Log: Query2MySpaceTest.java,v $
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
