/*$Id: StressTest.java,v 1.6 2004/11/11 22:54:13 mch Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.stress;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import junit.framework.TestSuite;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.integration.DatacenterTestCase;
import org.astrogrid.datacenter.integration.StdKeys;
import org.astrogrid.datacenter.integration.clientside.RdbmsTest;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.slinger.targets.NullTarget;
import org.astrogrid.slinger.targets.TargetMaker;
import org.xml.sax.SAXException;

/**
 * Fires off a stupidly large number of queries to the local datacenter
 *
 */
public class StressTest extends DatacenterTestCase implements StdKeys {

 
   /**
    * Submits (asynchronous) 100 sample query on std PAL
    */
   public void testAdqlSubmit() throws IOException, ServiceException, URISyntaxException, MalformedURLException, IOException, SAXException, ParserConfigurationException, QueryException {

      Query query = loadSampleQuery(RdbmsTest.class, "SimpleStarQuery-adql074.xml");

      QuerySearcher delegate = DatacenterDelegateFactory.makeQuerySearcher(Account.ANONYMOUS,PAL_v05_ENDPOINT,DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      assertNotNull("delegate was null",delegate);

      String[] queryIds = new String[100];
      for (int i = 0; i < 100; i++) {
   
         query.getResultsDef().setTarget(TargetMaker.makeIndicator(NullTarget.NULL_TARGET_URI));
         query.getResultsDef().setFormat("VOTABLE");
         queryIds[i] = delegate.submitQuery(query);
         assertNotNull(queryIds[i]);
      }

      //test get status
      for (int t = 0; t< 3; t++) {
         for (int i = 0; i < 100; i++) {
            delegate.getStatus(queryIds[i]);
         }
      }
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(new TestSuite(StressTest.class));
   }
}


/*
$Log: StressTest.java,v $
Revision 1.6  2004/11/11 22:54:13  mch
Moved targets

Revision 1.5  2004/11/03 00:31:03  mch
PAL_MCH Candidate 2 merge

Revision 1.4  2004/10/28 18:22:12  mch
Fix - pass in null target instead of file which breaks

Revision 1.3  2004/10/12 23:05:16  mch
Seperated tests properly

Revision 1.2  2004/10/06 22:03:45  mch
Following Query model changes in PAL

Revision 1.1  2004/09/09 12:23:10  mch
Added stress tests

Revision 1.1  2004/09/08 13:58:48  mch
Separated out tests by datacenter and added some

Revision 1.7  2004/09/02 12:33:49  mch
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
