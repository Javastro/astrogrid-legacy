/*$Id: Query2MySpaceTest.java,v 1.9 2004/05/24 12:00:49 jdt Exp $
 * Created on 22-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.integration;

import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
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

   private static final Log log = LogFactory.getLog(Query2MySpaceTest.class);
   
   protected QuerySearcher delegate;

   private static final String resultsPath = "avodemo/autoIntegrationTest.results";

   protected void setUp() throws Exception {
      delegate = DatacenterDelegateFactory.makeQuerySearcher(
         Account.ANONYMOUS,
         StdKeys.PAL_v05_ENDPOINT,
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE
      );
      assertNotNull("delegate was null",delegate);
   }
   
   /** do a non-blocking query, where results are left in myspace.
    * retreive from myspace, check they're what we expect
    */
   public void testSubmit() throws Exception {
       fail("This is causing the integration tests to freeze");
/* @TODO disabled by JDT
      Agsl resultsTarget = new Agsl(new Msrl(StdKeys.MYSPACE), resultsPath);

      InputStream in = this.getClass().getResourceAsStream("SimpleStarQuery-adql05.xml");
      assertNotNull("Could not find test query", in);
      
      String queryId = delegate.submitQuery(
         new AdqlQuery(in),
         resultsTarget,
         QuerySearcher.VOTABLE
      );
         
      String stat = delegate.getStatus(queryId);

      //wait until query finishes
      do {
         stat = delegate.getStatus(queryId);
      } while (!stat.equals(QueryState.FINISHED) && (!stat.equals(QueryState.ERROR))); // need some extra timout here too
 
      //see if results are in expected myspace location
      StoreClient store = StoreDelegateFactory.createDelegate(Account.ANONYMOUS.toUser(), resultsTarget);
      StoreFile file = store.getFile(resultsTarget.getPath());
      
      Document resultDoc = DomHelper.newDocument(store.getStream(resultsTarget.getPath()));
      assertNotNull("null result document",resultDoc);*/
   }
   
    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(Query2MySpaceTest.class);
    }

    public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
   
   
}


/*
$Log: Query2MySpaceTest.java,v $
Revision 1.9  2004/05/24 12:00:49  jdt
Disabled this test because it seems to be causing the whole suite
to seize up.

Revision 1.8  2004/05/24 11:56:06  jdt
Disabled this test because it seems to be causing the whole suite
to seize up.

Revision 1.7  2004/05/21 16:42:31  mch
Fixed compile errors

Revision 1.6  2004/05/21 10:52:31  jdt
Temporary quick fixes to deal with compilation errors following

changes to Agsl constructor.

See bugs 334 and 335

Revision 1.5  2004/05/14 11:02:05  mch
Fixed a number of errors

Revision 1.4  2004/05/13 12:25:04  mch
Fixes to create user, and switched to mostly testing it05 interface

Revision 1.3  2004/05/12 09:17:51  mch
Various fixes - forgotten whatfors...

Revision 1.2  2004/04/16 15:55:08  mch
added alltests

Revision 1.1  2004/04/16 15:41:03  mch
Added autotests

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

