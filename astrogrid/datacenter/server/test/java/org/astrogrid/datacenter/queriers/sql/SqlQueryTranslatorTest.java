/*$Id: SqlQueryTranslatorTest.java,v 1.17 2004/08/05 09:55:54 mch Exp $
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
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.queriers.test.DummySqlPlugin;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class SqlQueryTranslatorTest extends ServerTestCase {

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
       
       DummySqlPlugin.initConfig(); //so we have a valid config to build the SQL against
       
       
       String sql = translator.fromCone(new ConeQuery(20,20,3));
       
       //this would be a nasty check...
    }

    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void test1Adql05() throws Exception { doFromFile("adql0.5", 1); }
//    public void test1Adql073() throws Exception { doFromFile("adql0.7.3", 1); }
    public void test1Adql074() throws Exception { doFromFile("adql0.7.4", 1); }
    public void test1Sadql11() throws Exception { doFromFile("sadql1.1", 1); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void test2Adql05() throws Exception { doFromFile("adql0.5", 2); }
//    public void test2Adql073() throws Exception { doFromFile("adql0.7.3", 2); }
    public void test2Sadql11() throws Exception { doFromFile("sadql1.1", 2); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void test3AdqlCone05() throws Exception { doFromFile("adql0.5", 3); }
    public void test3AdqlCone074() throws Exception { doFromFile("adql0.7.4", 3); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void test4Adql() throws Exception { doFromFile("adql0.5", 4); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void test5Adql() throws Exception { doFromFile("adql0.5", 5); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    public void test6Adql() throws Exception { doFromFile("adql0.5", 6); }
    
    /** Test makes valid SQL from simple adql */
    public void doFromFile(String ver, int testNum) throws Exception {
       String filename = "sample-"+ver+"-"+testNum+".xml";
       InputStream in = this.getClass().getResourceAsStream(filename);
       assertNotNull(in);
       Document adqlDom = DomHelper.newDocument( in );
       AdqlQuery adqlQuery = new AdqlQuery(adqlDom.getDocumentElement());
       
     
       String result = translator.fromAdql(adqlQuery).trim();
       String correct = correctSql.getProperty("sample"+testNum).trim();
       assertEquals(correct.trim().toLowerCase(), result.trim().toLowerCase());
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.run(SqlQueryTranslatorTest.class);
    }

}


/*
$Log: SqlQueryTranslatorTest.java,v $
Revision 1.17  2004/08/05 09:55:54  mch
Removed ADQL 073 tests (no longer used)

Revision 1.16  2004/08/02 14:59:15  mch
Fix to cone search test to correctly populate config

Revision 1.15  2004/07/07 19:33:59  mch
Fixes to get Dummy db working and xslt sheets working both for unit tests and deployed

Revision 1.14  2004/07/06 18:48:34  mch
Series of unit test fixes

Revision 1.13  2004/07/01 23:07:14  mch
Introduced metadata generator

Revision 1.12  2004/04/01 17:20:54  mch
Removed tests for unused/not ready versions

Revision 1.11  2004/03/17 21:03:20  mch
Added SQL transformation tests

Revision 1.10  2004/03/17 18:02:01  mch
Adding translation tests

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
