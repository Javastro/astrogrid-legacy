/*$Id: DataQueryServiceTest.java,v 1.8 2003/11/27 17:28:09 nw Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.axisdataserver.types.QueryHelper;
import org.astrogrid.datacenter.axisdataserver.types._query;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.DummyQuerierSPI;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.queriers.sql.HsqlTestCase;
import org.astrogrid.datacenter.query.QueryStatus;
import org.w3c.dom.Document;

/** Test the entire DataQueryService, end-to-end, over a Hsql database
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Sep-2003
 *@todo reinstate dummy myspace, once we get a myspace delegate built.
 */
public class DataQueryServiceTest extends ServerTestCase {

    /**
     * Constructor for DataQueryServiceTest.
     * @param arg0
     */
    public DataQueryServiceTest(String arg0) {
        super(arg0);
    }

    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(DataQueryServiceTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        //wsTest.setUp(); //sets up workspace
        HsqlTestCase.initializeConfiguration();
        SimpleConfig.setProperty(QuerierManager.RESULTS_TARGET_KEY,Querier.TEMPORARY_DUMMY);
        DataSource ds = new HsqlTestCase.HsqlDataSource();
        //File tmpDir = WorkspaceTest.setUpWorkspace(); // dunno if we need to hang onto this for any reason..
        conn = ds.getConnection();
          String script = getResourceAsString("/org/astrogrid/datacenter/queriers/sql/create-test-db.sql");
        HsqlTestCase.runSQLScript(script,conn);
    }

    protected Connection conn;
    //protected final MyWorkspaceTest wsTest = new MyWorkspaceTest(null);

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
        super.tearDown();
    }

    public void testHandleUniqueness() throws Exception {
        SimpleConfig.setProperty(QuerierManager.QUERIER_SPI_KEY,DummyQuerierSPI.class.getName());
         Querier s1 = QuerierManager.createQuerier(QueryHelper.buildMinimalQuery());
         assertNotNull(s1);
         Querier s2 = QuerierManager.createQuerier(QueryHelper.buildMinimalQuery());
         assertNotNull(s2);
         assertNotSame(s1,s2);
         assertTrue(! s1.getHandle().trim().equals(s2.getHandle().trim()));
     }


    public void testRunQuery() throws Exception
    {
       //set up
       //get test query
        InputStream adqlIn = this.getClass().getResourceAsStream("/org/astrogrid/datacenter/queriers/sql/sql-querier-test-3.xml");
        assertNotNull(adqlIn);
        Select s =Select.unmarshalSelect(new InputStreamReader(adqlIn));
        _query query = new _query();
         query.setQueryBody(ADQLUtils.marshallSelect(s).getDocumentElement());
        
        Querier querier =  QuerierManager.createQuerier(query);
        querier.setResultsDestination(Querier.TEMPORARY_DUMMY);
        assertNotNull(querier);
        assertEquals(QueryStatus.UNKNOWN,querier.getStatus());
        assertEquals(-1.0,querier.getQueryTimeTaken(),0.001);
        TestListener l = new TestListener();
        querier.registerListener(l);
       

       //run query
        querier.run(); // note this is running in the same thread - simpler for testing - we don't mind wating.
        if (querier.getStatus().equals(QueryStatus.ERROR)){
            Throwable t = querier.getError();
            t.printStackTrace();
            fail("querier raised error" + t.getMessage());
        }        
        assertEquals(QueryStatus.FINISHED,querier.getStatus());
        assertTrue(querier.getQueryTimeTaken() > 0);
        URL votableLoc = new URL(querier.getResultsLoc());
        assertNotNull(votableLoc);
        Document votable = XMLUtils.newDocument(votableLoc.openStream());       
        assertIsVotable(votable);
    
       // check what the listener recorded. - should always be the same pattern for this query.
        assertEquals(4,l.statusList.size());
        QueryStatus[] expected = new QueryStatus[]{QueryStatus.RUNNING_QUERY,QueryStatus.QUERY_COMPLETE,QueryStatus.RUNNING_RESULTS,QueryStatus.FINISHED};
        for (int i = 0; i < l.statusList.size(); i++) {
            assertEquals(expected[i],l.statusList.get(i));
        }
    }

 



    static class TestListener implements QuerierListener {
        public List statusList = new ArrayList();
        /* (non-Javadoc)
         * @see org.astrogrid.datacenter.service.ServiceListener#serviceStatusChanged(java.lang.String)
         */

        public void queryStatusChanged(Querier querier) {
            statusList.add(querier.getStatus());
        }

        public QueryStatus getLast()
        {
           return (QueryStatus) statusList.get(statusList.size()-1);
        }
    }
    
    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
    
}


/*
$Log: DataQueryServiceTest.java,v $
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
