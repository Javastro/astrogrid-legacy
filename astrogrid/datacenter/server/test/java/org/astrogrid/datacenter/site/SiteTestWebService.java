/*$Id: SiteTestWebService.java,v 1.2 2004/02/16 23:07:05 mch Exp $
 * Created on 21-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.site;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.rpc.ServiceException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.community.Account;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.delegate.FullSearcher;
import org.astrogrid.datacenter.delegate.Metadata;

/**
 * For testing astrogrid datacenter web services and their delegates
 *
 * @author M Hill
 *
 */
public class SiteTestWebService extends TestCase {

   String endPoint = null;
   
   public SiteTestWebService(String givenEndPoint)
   {
      this.endPoint = givenEndPoint;
   }

   public void testServer()
   {
   }
   
   public void testMetadata() throws IOException, ServiceException
   {
      FullSearcher querier = DatacenterDelegateFactory.makeFullSearcher(Account.ANONYMOUS, endPoint, DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      
      Metadata metadata = querier.getMetadata();
      
      assertNotNull(metadata);
   }
   
   public void testBlockingQuery() throws Exception
   {
      FullSearcher querier = DatacenterDelegateFactory.makeFullSearcher(Account.ANONYMOUS, endPoint, DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);

      InputStream is = SiteTestWebService.class.getResourceAsStream("test-query.adql");

        Select select = Select.unmarshalSelect(new InputStreamReader(is));
        assertNotNull(select);

      DatacenterResults results = querier.doQuery(FullSearcher.VOTABLE, ADQLUtils.toQueryBody(select));
      
      assertNotNull(results);
   }

   /**
     * Runs the full set.
     */
    public static void main(String args[]) throws Exception
    {

//      WebServiceSiteTest tester = new WebServiceSiteTest(args[0]);
      //WebServiceSiteTest tester = new WebServiceSiteTest("http://vm07.astrogrid.org:8080/pal/");
      SiteTestWebService tester = new SiteTestWebService("http://localhost:8080/pal/");
      tester.testBlockingQuery();
      
      
    }

    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(SiteTestWebService.class);
    }

}


/*
$Log: SiteTestWebService.java,v $
Revision 1.2  2004/02/16 23:07:05  mch
Moved DummyQueriers to std server and switched to AttomConfig

Revision 1.1  2004/01/15 18:12:25  nw
Renamed, otherwise it gets picked up by the nightly
build - its not suited for running with this, and fails.
(want to get to 100% :)

Revision 1.4  2004/01/15 18:05:46  nw
minor tweak

Revision 1.3  2004/01/15 18:05:17  nw
minor tweak

Revision 1.2  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.1.2.3  2004/01/08 09:43:41  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.1.2.2  2004/01/07 13:02:09  nw
removed Community object, now using User object from common

Revision 1.1.2.1  2004/01/07 11:51:07  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.1  2003/12/16 16:31:29  mch
Checks for individual sites

Revision 1.7  2003/12/01 20:58:42  mch
Abstracting coarse-grained plugin

Revision 1.6  2003/11/27 17:28:09  nw
finished plugin-refactoring

Revision 1.5  2003/11/24 21:04:54  nw
fixed test linking
tidied up database connection JNDI lookup

Revision 1.4  2003/11/21 18:18:30  mch
Added client tests to server to get complete tests for server

Revision 1.3  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.2  2003/11/17 12:16:33  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.14  2003/11/13 12:49:17  mch
Removed config test (now in common)

Revision 1.13  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.12  2003/09/19 12:02:37  nw
Added top level test - runs integration tests against an inprocess db and inprocess axis.

Revision 1.11  2003/09/11 13:28:24  nw
added xml creation tests

Revision 1.10  2003/09/05 12:05:42  mch
Removed tests on removed classes

Revision 1.9  2003/09/05 01:06:09  nw
linked in new tests

Revision 1.8  2003/09/02 14:41:39  nw
added test

Revision 1.7  2003/08/29 15:26:55  mch
Renamed TestXxxx to XxxxxTest so Maven runs them

Revision 1.6  2003/08/28 15:26:44  nw
unit tests for adql

Revision 1.4  2003/08/27 23:57:55  mch
added workspace and delegate tests

Revision 1.3  2003/08/26 23:37:11  mch
Added tests

Revision 1.2  2003/08/22 10:37:48  nw
added test hierarchy for Job / JobStep

Revision 1.1  2003/08/21 12:29:18  nw
added unit testing for factory manager hierarchy.
added 'AllTests' suite classes to draw unit tests together - single
place to run all.

*/
