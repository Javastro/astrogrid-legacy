/*$Id: SqlQueryTranslatorTest.java,v 1.9 2004/03/12 20:11:09 mch Exp $
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

import java.io.InputStream;
import java.util.Properties;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class SqlQueryTranslatorTest extends ServerTestCase {

    public final static String QUERY = "select * from bling";
    public final static String INVALID_XML = "<?xml version='1.0'?><Sql>" + QUERY + "</Sql>";
    public final static String DUPLICATE_XML = "<?xml version='1.0'?><foo><sql></sql><sql></sql></foo>";
    public final static String QUERY1 = "select * from hens where teeth < 1"; // has a < in it
    public final static String CDATA_XML = "<?xml version='1.0'?><sql><![CDATA[" + QUERY1 + "]]></sql>";
    public final static String NESTED_XML = "<?xml version='1.0'?><some><tags><foo /><sql>"+QUERY +"</sql></tags></some>";

    SqlMaker translator = new StdSqlMaker();
    Properties correctSql = new Properties();
   
    public SqlQueryTranslatorTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
       //read in sample sql out checks
       InputStream is = this.getClass().getResourceAsStream("samples-sqlout.properties");
       assertNotNull(is);
       correctSql.load(is);
    }
    
    /** Test makes valid SQL from cone earch */
    public void testCone() throws Exception {
       String sql = translator.fromCone(new ConeQuery(20,20,3));
       
       //this would be a nasty check...
    }

    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void testAdql1() throws Exception { doFromFile(1); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void testAdql2() throws Exception { doFromFile(2); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void testAdql3() throws Exception { doFromFile(3); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void testAdql4() throws Exception { doFromFile(4); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void testAdql5() throws Exception { doFromFile(5); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void testAdql6() throws Exception { doFromFile(6); }
    
    /** Test makes valid SQL from simple adql */
    public void doFromFile(int testNum) throws Exception {
       String filename = "sample"+testNum+".xml";
       Document adqlDom = DomHelper.newDocument( this.getClass().getResourceAsStream(filename));
       AdqlQuery adqlQuery = new AdqlQuery(adqlDom.getDocumentElement());
       
       String result = translator.fromAdql(adqlQuery).trim();
       String correct = correctSql.getProperty(filename).trim();
       assertEquals(correct, result);
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.run(SqlQueryTranslatorTest.class);
    }

}


/*
$Log: SqlQueryTranslatorTest.java,v $
Revision 1.9  2004/03/12 20:11:09  mch
It05 Refactor (Client)

Revision 1.8  2004/03/12 04:54:07  mch
It05 MCH Refactor

Revision 1.7  2004/02/24 16:03:48  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

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
