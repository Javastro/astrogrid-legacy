/*$Id: SqlQuerierTest.java,v 1.13 2003/11/05 18:54:43 mch Exp $
 * Created on 04-Sep-2003
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Query;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/** test out the vanilla sql querier over an in-memory hsqldb database
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 *
 */
public class SqlQuerierTest extends TestCase {


    public SqlQuerierTest(String arg0) {
        super(arg0);
    }

    /**
    * Test harness - runs tests
    */
    public static void main(String[] args)
    {
      /*NWW - moved configuration (setting of keys) to setUp -
      as setUp is guaranteed to be called before a test, while main() is not -
      automated testing, testing within a GUI / IDE,,etc */

       junit.textui.TestRunner.run(SqlQuerierTest.class);
    }

    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(SqlQuerierTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        HsqlTestCase.initializeConfiguration();
        querier = new SqlQuerier();
        String script = HsqlTestCase.getResourceAsString("create-test-db.sql");
        conn = new HsqlTestCase.HsqlDataSource().getConnection();
        HsqlTestCase.runSQLScript(script,conn);
    }

    protected SqlQuerier querier;
    protected Connection conn;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        if (conn != null) {
            conn.close();
        }
       if (querier != null) {
           querier.close();
       }
    }

    public void test1() throws Exception {
        performQuery("sql-querier-test-1.xml");
    }

    public void test2() throws Exception {
        performQuery("sql-querier-test-2.xml");
    }

    public void test3() throws Exception {
        performQuery("sql-querier-test-3.xml");
    }

    /** perform an end-to-end query - from ADQL input document to VOTable output document
     *
     * @param queryFile resource file of query
     * @throws Exception
     */
    protected void performQuery(String queryFile) throws Exception {
        InputStream is = this.getClass().getResourceAsStream(queryFile);
        assertNotNull("Could not open query file :" + queryFile,is);
        Document doc = XMLUtils.newDocument(is);
        assertNotNull(doc);

        Element queryElement = doc.getDocumentElement();
        assertNotNull(queryElement);
        assertEquals("query",queryElement.getLocalName());

        QueryResults results = querier.queryDatabase(new Query(queryElement));
        assertNotNull(results);

        Document voElement = results.toVotable();
//        InputStream voStream = results.getInputStream();
//        assertNotNull(voStream);
//        Document resultDoc = XMLUtils.newDocument(voStream);
//        assertNotNull(resultDoc);

//        Element voElement = resultDoc.getDocumentElement();
        assertNotNull(voElement);
        assertEquals("VOTABLE",voElement.getDocumentElement().getLocalName());
        // could add extra checking to compare with expected results here..
    }

}


/*
$Log: SqlQuerierTest.java,v $
Revision 1.13  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.12  2003/09/25 01:19:33  nw
updated to fit with hsqldb test fixes

Revision 1.11  2003/09/22 16:52:12  mch
Fixes for changes to posts results to dummy myspace

Revision 1.10  2003/09/19 12:01:34  nw
fixed flakiness in db tests

Revision 1.9  2003/09/17 14:53:02  nw
tidied imports

Revision 1.8  2003/09/11 11:06:10  nw
fixed to work with new query format

Revision 1.7  2003/09/10 18:58:56  mch
Preparing to generalise Query

Revision 1.6  2003/09/10 14:48:35  nw
fixed breaking tests

Revision 1.5  2003/09/10 10:01:38  nw
fixed setup.

Revision 1.4  2003/09/08 16:43:50  mch
Removed toInputStream() from QueryResults as we don't really know what it's for yet

Revision 1.3  2003/09/07 18:58:58  mch
Updated tests for weekends changes to main code (mostly threaded queries, typesafe ServiceStatus)

Revision 1.2  2003/09/05 13:24:13  nw
now uses close method

Revision 1.1  2003/09/05 01:05:32  nw
added testing of SQLQuerier over an in-memory Hsql database,

relies on hsqldb.jar (added to project.xml)

*/
