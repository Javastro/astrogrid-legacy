/*$Id: AxisDataServiceQueryTest.java,v 1.1 2004/03/12 04:54:07 mch Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.service.v041;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.axis.types.URI;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.axisdataserver.types.Language;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.queriers.test.DummySqlPlugin;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.mySpace.delegate.MySpaceDummyDelegate;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/** Exercises the It4.1 AxisDataServices interface
 */
public class AxisDataServiceQueryTest extends ServerTestCase {

   protected AxisDataServer_v0_4_1 server;

   protected Query query1;
   protected Query query2;
   protected Query query3;
   
   public AxisDataServiceQueryTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
       super.setUp();

       DummySqlPlugin.initConfig();
       DummySqlPlugin.populateDb();
       
       server = new AxisDataServer_v0_4_1();
       
       query1 = new Query();
       query1.setQueryBody(DomHelper.newDocument(this.getClass().getResourceAsStream("../adqlQuery1.xml")).getDocumentElement());

       query2 = new Query();
       query2.setQueryBody(DomHelper.newDocument(this.getClass().getResourceAsStream("../adqlQuery2.xml")).getDocumentElement());
       
       query3 = new Query();
       query3.setQueryBody(DomHelper.newDocument(this.getClass().getResourceAsStream("../adqlQuery3.xml")).getDocumentElement());
    }

    public void testDoOneShotQuery() throws Exception {
       
        String results = server.doQuery("VOTABLE",query1);
        Document doc = DomHelper.newDocument(results);
        assertIsVotableResultsResponse(doc);
    }
    
    public void testGetLanguageInfo() throws Exception {
        Language[] langs = server.getLanguageInfo(null);
        assertNotNull(langs);
        assertEquals(2,langs.length);
    }
    
    public void testGetMetadata() throws Exception {
        String result = server.getMetadata(new Object());
        assertNotNull(result);
        Document doc = DomHelper.newDocument(result);
        assertIsMetadata(doc);
    }
    
    public void testMakeQueryWithId() throws Exception {
        String qid = server.makeQueryWithId(query1,"foo");
        assertEquals("foo",qid);
    }
    
    public void testAbort() throws Exception {
        String qid = server.makeQuery(query1);
        assertNotNull(qid);
        server.abortQuery(qid);
        // should have gone now.. i.e. we can't do this..
        try {
        server.setResultsDestination(qid,new URI(MySpaceDummyDelegate.DUMMY));
        fail("Expected to barf");
        }catch (IllegalArgumentException e) {
            //ignored it
        }
    }


    public void testDoStagedQueryQuery() throws Exception    {
             String qid = server.makeQuery(query1);
             assertNotNull(qid);
             server.setResultsDestination(qid,new URI(MySpaceDummyDelegate.DUMMY));
             assertEquals(QueryState.CONSTRUCTED.toString(),server.getStatus(qid));

            TestListener l = new TestListener();
            //server.registerWebListener()
            server.startQuery(qid);
            String results = null;
            while (results == null) {
                results = server.getResultsAndClose(qid);
            }
            URL votableLoc = new URL(results); // probably won't work - need to extract from xml I tbink. this is why we need beans.
            Document votable = XMLUtils.newDocument(votableLoc.openStream());
            assertIsVotable(votable);
       

        /*
       // check what the listener recorded. - should always be the same pattern for this query.
        assertEquals(4,l.statusList.size());
        QueryStatus[] expected = new QueryStatus[]{QueryStatus.RUNNINGQuery,QueryStatus.QUERY_COMPLETE,QueryStatus.RUNNING_RESULTS,QueryStatus.FINISHED};
        for (int i = 0; i < l.statusList.size(); i++) {
            assertEquals(expected[i],l.statusList.get(i));
        }
      */
    }

 



    static class TestListener implements QuerierListener {
        public List statusList = new ArrayList();
        /* (non-Javadoc)
         * @see org.astrogrid.datacenter.service.ServiceListener#serviceStatusChanged(java.lang.String)
         */

        public void queryStatusChanged(Querier querier) {
            statusList.add(querier.getStatus().getState());
        }

        public QueryState getLast()
        {
           return (QueryState) statusList.get(statusList.size()-1);
        }
    }
    
    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(AxisDataServiceQueryTest.class);
    }

}


/*
$Log: AxisDataServiceQueryTest.java,v $
Revision 1.1  2004/03/12 04:54:07  mch
It05 MCH Refactor

Revision 1.20  2004/03/08 00:31:28  mch
Split out webservice implementations for versioning

Revision 1.19  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

Revision 1.18  2004/03/06 19:34:21  mch
Merged in mostly support code (eg web query form) changes

Revision 1.16  2004/02/24 16:03:48  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.15  2004/02/17 03:38:40  mch
Various fixes for demo

Revision 1.14  2004/02/16 23:07:05  mch
Moved DummyQueriers to std server and switched to AttomConfig

Revision 1.13  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.12.10.2  2004/01/08 09:43:40  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.12.10.1  2004/01/07 11:51:07  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.12  2003/12/02 18:00:03  mch
Moved MySpaceDummyDelegate

Revision 1.11  2003/12/01 20:58:42  mch
Abstracting coarse-grained plugin

Revision 1.10  2003/12/01 16:44:11  nw
dropped QueryId, back to string

Revision 1.9  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.8  2003/11/27 17:28:09  nw
finished plugin-refactoring

Revision 1.7  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.

Revision 1.6  2003/11/25 14:21:49  mch
Extracted Querier from DatabaseQuerier in prep for FITS querying

Revision 1.5  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.4  2003/11/18 14:36:59  nw
temporarily commented out references to MySpaceDummyDelegate, so that the sustem will build

Revision 1.3  2003/11/17 15:42:03  mch
Package movements

Revision 1.2  2003/11/17 12:16:33  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.18  2003/11/06 22:10:04  mch
Work with both real and dummy myspace

Revision 1.16  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.15  2003/10/13 14:08:10  nw
little fix to changed class name.

Revision 1.14  2003/09/24 21:11:14  nw
altered to fit with new configuration and behaviour

Revision 1.13  2003/09/22 16:52:12  mch
Fixes for changes to posts results to dummy myspace

Revision 1.12  2003/09/19 12:02:00  nw
fixed flakiness in db tests

Revision 1.11  2003/09/17 14:53:02  nw
tidied imports

Revision 1.10  2003/09/15 22:09:00  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.9  2003/09/15 21:28:09  mch
Listener/state refactoring.

Revision 1.8  2003/09/10 17:57:52  mch
Tidied xml doc helpers and fixed (?) job/web listeners

Revision 1.7  2003/09/10 14:48:35  nw
fixed breaking tests

Revision 1.6  2003/09/10 12:16:44  mch
Changes to make web interface more consistent

Revision 1.5  2003/09/10 10:02:17  nw
added key for hsqldb driver

Revision 1.4  2003/09/09 17:58:38  mch
Moved ServiceStatus

Revision 1.3  2003/09/08 19:18:55  mch
Workspace constructor now throws IOException

Revision 1.2  2003/09/07 18:58:58  mch
Updated tests for weekends changes to main code (mostly threaded queries, typesafe ServiceStatus)

Revision 1.1  2003/09/05 13:25:29  nw
added end-to-end test of DataQueryService

*/
