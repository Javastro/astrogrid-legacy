/*$Id: SqlQueryTranslatorTest.java,v 1.1 2003/11/14 00:38:29 mch Exp $
 * Created on 29-Aug-2003
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
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import junit.framework.TestCase;

import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.astrogrid.datacenter.queriers.sql.SqlQueryTranslator;
/** test the query translator - maybe n the wrong package, but convenient to have it here for now.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Aug-2003
 * * @todo add wider range of tests.
 */
public class SqlQueryTranslatorTest extends TestCase {

    /**
     * Constructor for MySQLVisitorTest.
     * @param arg0
     */
    public SqlQueryTranslatorTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SqlQueryTranslatorTest.class);
    }
    public void test1() throws Exception {
        processFile("sample1.xml");
    }

    public void test2() throws Exception {
        processFile("sample2.xml");
    }

    public void test3() throws Exception {
        processFile("sample3.xml");
    }
    
    public void test4() throws Exception {
        processFile("sample4.xml");
    }
    
    public void test5() throws Exception {
        processFile("sample5.xml");
    }
    public void test6() throws Exception {
        processFile("sample6.xml");
    }
    protected void processFile(String path) throws Exception {
          InputStream is = this.getClass().getResourceAsStream(path);
          assertNotNull(is);
          Reader reader = new InputStreamReader(is);
          Select query = Select.unmarshalSelect(reader);
          assertNotNull(query);
          assertTrue(query.isValid());

          Properties results = new Properties();
          InputStream propsStream  = this.getClass().getResourceAsStream("mysql-translations.properties");
          results.load(propsStream);
          assertFalse(results.isEmpty());
            try {
            QueryTranslator visitor = new SqlQueryTranslator();
            String sql = visitor.translate(query);
            assertNotNull(sql);
             System.out.println(sql);
             String expectedSQL = results.getProperty(path);
             assertNotNull(expectedSQL);
             assertEquals(expectedSQL.trim(),sql.trim());
          } catch (Exception e) {
              e.printStackTrace();
              fail("Caught Exception " + e.getMessage());
          }
 
        

      }

}


/* 
$Log: SqlQueryTranslatorTest.java,v $
Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.1  2003/10/14 12:59:40  nw
moved SqlQueryTranslator here from removed adql package

Revision 1.2  2003/09/17 14:53:02  nw
tidied imports

Revision 1.1  2003/09/03 14:45:59  nw
renamed test to match renamed class

Revision 1.1  2003/09/02 14:41:15  nw
added tests for ADQL parser
 
*/