/*$Id: DataQueryServiceTest.java,v 1.7 2003/09/10 14:48:35 nw Exp $
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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.DummyQuerier;
import org.astrogrid.datacenter.queriers.sql.HsqlTestCase;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** Test the entire DataQueryService, end-to-end, over a Hsql database
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Sep-2003
 *
 */
public class DataQueryServiceTest extends HsqlTestCase {

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
        DataSource ds = new HsqlDataSource();
        conn = ds.getConnection();
          String script = getResourceAsString("/org/astrogrid/datacenter/queriers/sql/create-test-db.sql");
        runSQLScript(script,conn);
        //register HSQLDB driver with driver key in configration file
        //put driver into config file
        Configuration.setProperty(SqlQuerier.JDBC_DRIVERS_KEY, "org.hsqldb.jdbcDriver"  );
      
        Configuration.setProperty(DatabaseQuerier.DATABASE_QUERIER_KEY,"org.astrogrid.datacenter.queriers.hsql.HsqlQuerier");
        // woud be simplest to pass datasource via JNDI, but no guarantee jndi is available
        Configuration.setProperty(SqlQuerier.JDBC_URL_KEY,"jdbc:hsqldb:.");
        Configuration.setProperty(SqlQuerier.JDBC_CONNECTION_PROPERTIES_KEY,"user=sa");
        
    }

    protected Connection conn;
    //protected final MyWorkspaceTest wsTest = new MyWorkspaceTest(null);

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        if (conn != null) {
            //conn.createStatement().execute("SHUTDOWN");
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //wsTest.tearDown(); //tears down workspace
    }

    /** work around for lack of multiple inheritance..
     * just want to get access to the setup and tearDown methods
     * @author Noel Winstanley nw@jb.man.ac.uk 05-Sep-2003
     *
     * I gather this was a convenient way of creating temporary workspaces...
     * hopefully Workspace should now do that properly now
    private class MyWorkspaceTest extends WorkspaceTest {
        public MyWorkspaceTest(String s) {
            super(s);
        }
     }*/


    public void testRunQuery() throws Exception
    {
       //set up
        DatabaseQuerier querier = new DummyQuerier();
        TestListener l = new TestListener();
        querier.registerServiceListener(l);

       //get test query
        InputStream queryIn = this.getClass().getResourceAsStream("/org/astrogrid/datacenter/queriers/sql/sql-querier-test-3.xml");
        assertNotNull(queryIn);
        Document testQuery = XMLUtils.newDocument(queryIn);
        assertNotNull(testQuery);

       //run query
        querier.setQuery(testQuery.getDocumentElement());
        querier.doQuery();
        Document votable = querier.getResults().toVotable();
        assertNotNull(votable);
        
        assertEquals("VOTABLE",votable.getDocumentElement().getLocalName());
        assertEquals(ServiceStatus.QUERY_COMPLETE,querier.getStatus());

       // check what the listener recorded.
//not always        assertEquals(4,l.statusList.size());
//        String[] expected = new String[]{ServiceStatus.STARTING,ServiceStatus.RUNNING_QUERY,ServiceStatus.RUNNING_RESULTS,ServiceStatus.FINISHED};
        assertEquals(ServiceStatus.QUERY_COMPLETE,l.getLast());
    }
    
    public void testHandleUniqueness() throws IOException {
        DatabaseQuerier s1 = new DummyQuerier();
        assertNotNull(s1);
        DatabaseQuerier s2 = new DummyQuerier();
        assertNotNull(s2);
        assertNotSame(s1,s2);
        assertTrue(! s1.getHandle().trim().equals(s2.getHandle().trim()));
    }

    public void testStatus() throws IOException {
        DatabaseQuerier s1 = new DummyQuerier();
        assertEquals(s1.getStatus(),ServiceStatus.CONSTRUCTED);
    }

    static class TestListener implements ServiceListener {
        public List statusList = new ArrayList();
        /* (non-Javadoc)
         * @see org.astrogrid.datacenter.service.ServiceListener#serviceStatusChanged(java.lang.String)
         */
        public void serviceStatusChanged(ServiceStatus newStatus) {
            statusList.add(newStatus);
        }

        public ServiceStatus getLast()
        {
           return (ServiceStatus) statusList.get(statusList.size()-1);
        }
    }

}


/*
$Log: DataQueryServiceTest.java,v $
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
