/*$Id: SqlQueryTranslatorTest.java,v 1.6 2004/02/16 23:07:05 mch Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sql;

import org.astrogrid.config.AttomConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.queriers.spi.Translator;
import org.astrogrid.datacenter.sql.SQLUtils;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class SqlQueryTranslatorTest extends ServerTestCase {

    /**
     * Constructor for SqlQueryTranslatorTest.
     * @param arg0
     */
    public SqlQueryTranslatorTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SqlQueryTranslatorTest.class);
    }

    /*
     * @see ServerTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        trans = new SqlQueryTranslator();
        AttomConfig.setProperty(SqlQueryTranslator.SQL_PASSTHRU_ENABLED_KEY,"true");
    }
    protected Translator trans;
    /*
     * @see ServerTestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReturnType() {
        assertEquals(String.class,trans.getResultType());
    }
    public final static String QUERY = "select * from bling";
    public final static String INVALID_XML = "<?xml version='1.0'?><Sql>" + QUERY + "</Sql>";
    public final static String DUPLICATE_XML = "<?xml version='1.0'?><foo><sql></sql><sql></sql></foo>";
    public final static String QUERY1 = "select * from hens where teeth < 1"; // has a < in it
    public final static String CDATA_XML = "<?xml version='1.0'?><sql><![CDATA[" + QUERY1 + "]]></sql>";
    public final static String NESTED_XML = "<?xml version='1.0'?><some><tags><foo /><sql>"+QUERY +"</sql></tags></some>";

    
    public void testValid() throws Exception {
        Object o = trans.translate(SQLUtils.toQueryBody(QUERY));
        assertNotNull(o);
        assertEquals(trans.getResultType(),o.getClass());
        assertEquals(QUERY,o);
    }
    
    public void testInvalid() throws Exception {
        try {
        Document doc = stringToDocument(INVALID_XML);
        Object o = trans.translate(doc.getDocumentElement());
        fail("should have barfed");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }
    
    public void testMultiple() throws Exception {
        try {
            Document doc = stringToDocument(DUPLICATE_XML);
            Object o = trans.translate(doc.getDocumentElement());
            fail("should have barfed");
        } catch (IllegalArgumentException e) {
            //expected
        }
    }
    
    public void testCDATA() throws Exception {
        Document doc= stringToDocument(CDATA_XML);
        Object o = trans.translate(doc.getDocumentElement());
        assertNotNull(o);
        assertEquals(QUERY1,o);
    }

    public void testNested() throws Exception {
        Document doc = stringToDocument(NESTED_XML);
        Object o = trans.translate(doc.getDocumentElement());
        assertNotNull(o);
        assertEquals(QUERY,o);
    }

}


/*
$Log: SqlQueryTranslatorTest.java,v $
Revision 1.6  2004/02/16 23:07:05  mch
Moved DummyQueriers to std server and switched to AttomConfig

Revision 1.5  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.4.10.1  2004/01/08 15:37:27  nw
added tests for SQL passthru

Revision 1.4  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested
 
*/
