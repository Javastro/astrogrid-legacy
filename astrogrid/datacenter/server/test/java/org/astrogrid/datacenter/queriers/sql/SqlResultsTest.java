/*$Id: SqlResultsTest.java,v 1.3 2003/11/21 17:37:56 nw Exp $
 * Created on 03-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.astrogrid.datacenter.ServerTestCase;
import org.w3c.dom.Document;


/** test that exercises SqlResults
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2003
 *
 */
public class SqlResultsTest extends ServerTestCase {

    /**
     * Constructor for SqlResultsTest.
     * @param arg0
     */
    public SqlResultsTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SqlResultsTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
         conn = new HsqlTestCase.HsqlDataSource().getConnection();
         assertNotNull(conn);
           String script = getResourceAsString("create-test-db.sql");
         HsqlTestCase.runSQLScript(script,conn);
        Statement stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmnt.executeQuery("select id,firstName,lastName from people order by id"); // deterministic query
        assertNotNull(rs);
        assertTrue(rs.first());
        res = new SqlResults(rs, null);
        assertNotNull(res);
     }
     protected Connection conn;
     protected SqlResults res;

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        if (conn != null) {
               conn.close();
        }
        super.tearDown();
    }

    /** @todo testing commented out - add back in */
    public void testToVotable() throws Exception  {

        
//temporarily removed as its changed - should write a test, slap wrist now,
//        InputStream is = res.getInputStream();
//        assertNotNull(is);
        // compare with result we saved earlier - good to check that nothing is changing.
//        String result = HsqlTestCase.streamToString(is);
//        assertNotNull(result);
//        String expected = HsqlTestCase.getResourceAsString("expected-votable.xml");
//        assertEquals(expected.trim(),result.trim());
       
       Document votable = res.toVotable();
       assertNotNull(votable);
       assertIsVotable(votable);    
    }
    
    
    
    // would like to do same with other method too. - needs link to workspace.
}


/*
$Log: SqlResultsTest.java,v $
Revision 1.3  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.2  2003/11/20 15:45:47  nw
started looking at tese tests

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.4  2003/10/02 13:00:41  mch
It03-Close

Revision 1.3  2003/09/19 12:01:34  nw
fixed flakiness in db tests

Revision 1.2  2003/09/08 16:43:16  mch
Moved workspace parameter from QueryResults.toVotable() to QueryResults() constructor, created from querier which already has workspace

Revision 1.1  2003/09/05 01:05:32  nw
added testing of SQLQuerier over an in-memory Hsql database,

relies on hsqldb.jar (added to project.xml)
 
*/
