/*$Id: SqlResultsTest.java,v 1.1 2003/11/14 00:38:29 mch Exp $
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

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.w3c.dom.Document;

import junit.framework.TestCase;

/** test that exercises SqlResults
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Sep-2003
 *
 */
public class SqlResultsTest extends TestCase {

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
         conn = new HsqlTestCase.HsqlDataSource().getConnection();
         assertNotNull(conn);
           String script = HsqlTestCase.getResourceAsString("create-test-db.sql");
         HsqlTestCase.runSQLScript(script,conn);
     }
     protected Connection conn;

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        if (conn != null) {
               conn.close();
        }
    }


    public void testExerciseSqlResults() throws Exception  {
        Statement stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stmnt.executeQuery("select id,firstName,lastName from people order by id"); // deterministic query
        assertNotNull(rs);
        assertTrue(rs.first());
        SqlResults res = new SqlResults(rs, null);
        assertNotNull(res);
//temporarily removed as its changed - should write a test, slap wrist now,
//        InputStream is = res.getInputStream();
//        assertNotNull(is);
        // compare with result we saved earlier - good to check that nothing is changing.
//        String result = HsqlTestCase.streamToString(is);
//        assertNotNull(result);
//        String expected = HsqlTestCase.getResourceAsString("expected-votable.xml");
//        assertEquals(expected.trim(),result.trim());
       
       Document votable = res.toVotable();
    }
    
    // would like to do same with other method too. - needs link to workspace.
}


/*
$Log: SqlResultsTest.java,v $
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
