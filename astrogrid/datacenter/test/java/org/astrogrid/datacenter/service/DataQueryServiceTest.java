/*$Id: DataQueryServiceTest.java,v 1.1 2003/09/05 13:25:29 nw Exp $
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

import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.sql.HsqlTestCase;
import org.astrogrid.datacenter.servicestatus.ServiceStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import junit.framework.*;

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
        wsTest.setUp(); //sets up workspace 
        DataSource ds = new HsqlDataSource();
        conn = ds.getConnection();
          String script = getResourceAsString("/org/astrogrid/datacenter/queriers/sql/create-test-db.sql");
        runSQLScript(script,conn);  
        Configuration.setProperty(DatabaseQuerier.DATABASE_QUERIER,"org.astrogrid.datacenter.queriers.hsql.HsqlQuerier");
        // woud be simplest to pass datasource via JNDI, but no guarantee jndi is available
        Configuration.setProperty(DatabaseQuerier.JDBC_URL,"jdbc:hsqldb:.");
        Configuration.setProperty(DatabaseQuerier.CONNECTION_PROPERTIES,"user=sa");
    }

    protected Connection conn;
    protected final MyWorkspaceTest wsTest = new MyWorkspaceTest(null);

    /* 
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        if (conn != null) {
            conn.createStatement().execute("SHUTDOWN");
            conn.close();
        }
        wsTest.tearDown(); //tears down workspace
    }
    
    /** work around for lack of multiple inheritance..
     * just want to get access to the setup and tearDown methods 
     * @author Noel Winstanley nw@jb.man.ac.uk 05-Sep-2003
     *
     */
    private class MyWorkspaceTest extends WorkspaceTest {
        public MyWorkspaceTest(String s) {
            super(s);
        }
    }


    public void testRunQuery() throws Exception {
        DataQueryService s = new DataQueryService();
        TestListener l = new TestListener();
        s.registerServiceListener(l);        
        InputStream is = this.getClass().getResourceAsStream("/org/astrogrid/datacenter/queriers/sql/sql-querier-test-3.xml");
        assertNotNull(is);
        Document testDoc = XMLUtils.newDocument(is);
        assertNotNull(testDoc);
        Element result = s.runQuery(testDoc.getDocumentElement());
        assertNotNull(result);
        assertEquals("VOTABLE",result.getLocalName());
        assertEquals(s.getStatus(),ServiceStatus.FINISHED);
        // check what the listener recorded.
        assertEquals(4,l.statusList.size());
        String[] expected = new String[]{ServiceStatus.STARTING,ServiceStatus.RUNNING_QUERY,ServiceStatus.RUNNING_RESULTS,ServiceStatus.FINISHED};
        assertTrue(Arrays.equals(expected,l.statusList.toArray(new String[]{}) ));
        
    }

    public void testHandleUniqueness() {
        DataQueryService s1 = new DataQueryService();
        assertNotNull(s1);
        DataQueryService s2 = new DataQueryService();
        assertNotNull(s2);
        assertNotSame(s1,s2);
        assertTrue(! s1.getHandle().trim().equals(s2.getHandle().trim()));
    }
    
    public void testStatus() {
        DataQueryService s1 = new DataQueryService();
        assertEquals(s1.getStatus(),ServiceStatus.UNKNOWN);
    }
 
    static class TestListener implements ServiceListener {
        public List statusList = new ArrayList();
        /* (non-Javadoc)
         * @see org.astrogrid.datacenter.service.ServiceListener#serviceStatusChanged(java.lang.String)
         */
        public void serviceStatusChanged(String newStatus) {
            statusList.add(newStatus);            
        }
    }

}


/* 
$Log: DataQueryServiceTest.java,v $
Revision 1.1  2003/09/05 13:25:29  nw
added end-to-end test of DataQueryService
 
*/