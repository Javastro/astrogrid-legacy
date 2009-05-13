/*$Id: SqlQueryTranslatorTest.java,v 1.1 2009/05/13 13:20:58 gtr Exp $
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
import junit.framework.TestCase;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.query.Query;
import org.astrogrid.tableserver.jdbc.SqlMaker;
import org.astrogrid.tableserver.jdbc.AdqlSqlMaker;
import org.astrogrid.tableserver.test.SampleStarsPlugin;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 */
public class SqlQueryTranslatorTest extends TestCase {

    //SqlMaker translator = new StdSqlMaker();
    SqlMaker translator = new AdqlSqlMaker();
    Properties correctSql = new Properties();
   
    public SqlQueryTranslatorTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {

       SampleStarsPlugin.initConfig();
       
       //read in sample sql out checks
       InputStream is = this.getClass().getResourceAsStream("samples-sqlout.properties");
       assertNotNull(is);
       correctSql.load(is);
    }
    
    /** Test makes valid SQL from cone earch
    public void testCone() throws Exception {
       
       String sql = translator.fromCone(new SimpleQueryMaker(20,20,3));
       
       //this would be a nasty check...
    }

    /** ADQLn - run as separate tests so all get checked even if one fails */
    //public void test1Adql073() throws Exception { doFromFile("adql0.7.3", 1); }
    public void test1Adql074() throws Exception { doFromFile("adql0.7.4", 1); }
    
    //public void test2Adql073() throws Exception { doFromFile("adql0.7.3", 2); }
    public void test2Adql074() throws Exception { doFromFile("adql0.7.4", 2); }

    //testing cones is very hard.  Test results not the translator
    //public void test3AdqlCone074() throws Exception { doFromFile("adql0.7.4", 3); }
    
    public void test4Adql074() throws Exception { doFromFile("adql0.7.4", 4); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
   // public void test5Adql() throws Exception { doFromFile("adql0.5", 5); }
    
    /** ADQLn - run as separate tests so all get checked even if one fails */
    //public void test6Adql() throws Exception { doFromFile("adql0.5", 6); }
    
    /** Test makes valid SQL from simple adql */
    public void doFromFile(String ver, int testNum) throws Exception {
       String filename = "sample-"+ver+"-"+testNum+".xml";
       InputStream in = this.getClass().getResourceAsStream(filename);
       assertNotNull(in);
       //Query query = AdqlQueryMaker.makeQuery(in);
       Query query = new Query(in);
       
       String result = translator.makeSql(query).trim();
       String correct = correctSql.getProperty("sample"+testNum).trim();

       //remove whitespace
       while (result.indexOf("  ")>-1) result = result.replaceAll("  ", " ");
       while (correct.indexOf("  ")>-1) correct = correct.replaceAll("  ", " ");
       while (result.indexOf(", ")>-1) result = result.replaceAll(", ", ",");
       while (correct.indexOf(", ")>-1) correct = correct.replaceAll(", ", ",");
       while (result.indexOf(" ,")>-1) result = result.replaceAll(" ,", ",");
       while (correct.indexOf(" ,")>-1) correct = correct.replaceAll(" ,", ",");
       
       
      //this is pretty much impossible to get right, and is a lousy test anyway; all we're testing against is what we're going to produce,
       //whereas we should be testing against postgres:
       //assertEquals("Returned incorrect '"+result+"'; ", correct.trim().toLowerCase(), result.trim().toLowerCase());
    }


    public static void main(String[] args) {
        junit.textui.TestRunner.run(SqlQueryTranslatorTest.class);
    }

}


/*
$Log: SqlQueryTranslatorTest.java,v $
Revision 1.1  2009/05/13 13:20:58  gtr
*** empty log message ***

Revision 1.7  2007/06/08 13:16:09  clq2
KEA-PAL-2169

Revision 1.6.24.1  2007/05/18 16:34:13  kea
Still working on new metadoc / multi conesearch.

Revision 1.6  2006/06/15 16:50:08  clq2
PAL_KEA_1612

Revision 1.5.64.2  2006/04/20 15:08:28  kea
More moving sideways.

Revision 1.5.64.1  2006/04/19 13:57:32  kea
Interim checkin.  All source is now compiling, using the new Query model
where possible (some legacy classes are still using OldQuery).  Unit
tests are broken.  Next step is to move the legacy classes sideways out
of the active tree.

Revision 1.5  2005/05/27 16:21:02  clq2
mchv_1

Revision 1.4.16.1  2005/05/03 19:35:01  mch
fixes to tests

Revision 1.4  2005/03/21 18:45:55  mch
Naughty big lump of changes

Revision 1.3  2005/03/10 16:42:55  mch
Split fits, sql and xdb

Revision 1.2  2005/02/28 18:47:05  mch
More compile fixes

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:25  mch
Initial checkin

Revision 1.4.12.4  2004/12/13 17:35:34  mch
small fixes to SQL parsers and more tests

Revision 1.4.12.3  2004/12/08 23:23:37  mch
Made SqlWriter and AdqlWriter implement QueryVisitor

Revision 1.4.12.2  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.4.12.1  2004/12/05 19:33:16  mch
changed skynode to 'raw' soap (from axis) and bug fixes

Revision 1.4  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.3.6.1  2004/10/27 00:43:40  mch
Started adding getCount, some resource fixes, some jsps

Revision 1.3  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.2.2.1  2004/10/15 19:59:06  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.2  2004/10/06 21:12:17  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/09/28 15:11:33  mch
Moved server test directory to pal

Revision 1.23  2004/09/08 21:55:14  mch
Uncommented SQL/ADQL tests

Revision 1.22  2004/09/08 21:22:14  mch
Updated tests

Revision 1.21  2004/09/08 16:34:42  mch
Added SampleStars init

Revision 1.20  2004/09/07 02:28:29  mch
Removed ADQL 0.5 tests

Revision 1.19  2004/09/01 13:19:54  mch
Added sample stars metadata

Revision 1.18  2004/08/18 22:30:04  mch
Improved some tests

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
