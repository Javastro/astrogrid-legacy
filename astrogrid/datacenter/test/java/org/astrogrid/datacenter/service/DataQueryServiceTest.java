/*$Id: DataQueryServiceTest.java,v 1.15 2003/10/13 14:08:10 nw Exp $
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.DatabaseQuerierManager;
import org.astrogrid.datacenter.queriers.DummyQuerier;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.sql.HsqlTestCase;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Document;

/** Test the entire DataQueryService, end-to-end, over a Hsql database
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Sep-2003
 *
 */
public class DataQueryServiceTest extends TestCase {

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
        //wsTest.setUp(); //sets up workspace
        HsqlTestCase.initializeConfiguration();
        Configuration.setProperty(DatabaseQuerierManager.RESULTS_TARGET_KEY,"fooble");
        DataSource ds = new HsqlTestCase.HsqlDataSource();
        File tmpDir = WorkspaceTest.setUpWorkspace(); // dunno if we need to hang onto this for any reason..
        conn = ds.getConnection();
          String script = HsqlTestCase.getResourceAsString("/org/astrogrid/datacenter/queriers/sql/create-test-db.sql");
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
    }

    public void testHandleUniqueness() throws IOException {
        Configuration.setProperty(DatabaseQuerierManager.DATABASE_QUERIER_KEY,DummyQuerier.class.getName());
         DatabaseQuerier s1 = DatabaseQuerierManager.createQuerier();
         assertNotNull(s1);
         DatabaseQuerier s2 = DatabaseQuerierManager.createQuerier();
         assertNotNull(s2);
         assertNotSame(s1,s2);
         assertTrue(! s1.getHandle().trim().equals(s2.getHandle().trim()));
     }


    public void testRunQuery() throws Exception
    {
       //set up
       //get test query
        InputStream queryIn = this.getClass().getResourceAsStream("/org/astrogrid/datacenter/queriers/sql/sql-querier-test-3.xml");
        assertNotNull(queryIn);
        Document testQuery = XMLUtils.newDocument(queryIn);
        assertNotNull(testQuery);
        
        DatabaseQuerier querier =  DatabaseQuerierManager.createQuerier(testQuery.getDocumentElement());
        assertNotNull(querier);
        assertEquals(QueryStatus.CONSTRUCTED,querier.getStatus());
        assertEquals(-1.0,querier.getQueryTimeTaken(),0.001);
        TestListener l = new TestListener();
        querier.registerListener(l);
       

       //run query
        querier.run(); // note this is running in the same thread - simpler for testing - we don't mind wating.
        assertEquals(QueryStatus.FINISHED,querier.getStatus());
        assertTrue(querier.getQueryTimeTaken() > 0);
        URL votableLoc = querier.getResultsLoc();
        assertNotNull(votableLoc);

        Document votable = XMLUtils.newDocument(votableLoc.openStream());
        assertEquals("VOTABLE",votable.getDocumentElement().getLocalName());
    
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

        public void queryStatusChanged(DatabaseQuerier querier) {
            statusList.add(querier.getStatus());
        }

        public QueryStatus getLast()
        {
           return (QueryStatus) statusList.get(statusList.size()-1);
        }
    }
}


/*
$Log: DataQueryServiceTest.java,v $
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
