/*$Id: SqlQuerierTest.java,v 1.2 2003/09/05 13:24:13 nw Exp $
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

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
/** test out the vanilla sql querier over an in-memory hsqldb database 
 * @author Noel Winstanley nw@jb.man.ac.uk 04-Sep-2003
 *
 */
public class SqlQuerierTest extends HsqlTestCase {

    /**
     * Constructor for SqlQuerierTest.
     * @param arg0
     */
    public SqlQuerierTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SqlQuerierTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        querier = new SqlQuerier(new HsqlDataSource());
          String script = getResourceAsString("create-test-db.sql");
        runSQLScript(script,querier.jdbcConnection);  
    }

    protected SqlQuerier querier;

    /* 
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
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
        assertEquals("Select",queryElement.getLocalName());
        
        QueryResults results = querier.queryDatabase(queryElement);
        assertNotNull(results);
        
        InputStream voStream = results.getInputStream();
        assertNotNull(voStream);
        Document resultDoc = XMLUtils.newDocument(voStream);
        assertNotNull(resultDoc);
        
        Element voElement = resultDoc.getDocumentElement();
        assertNotNull(voElement);
        assertEquals("VOTABLE",voElement.getLocalName());
        // could add extra checking to compare with expected results here..
    }

}


/* 
$Log: SqlQuerierTest.java,v $
Revision 1.2  2003/09/05 13:24:13  nw
now uses close method

Revision 1.1  2003/09/05 01:05:32  nw
added testing of SQLQuerier over an in-memory Hsql database,

relies on hsqldb.jar (added to project.xml)
 
*/