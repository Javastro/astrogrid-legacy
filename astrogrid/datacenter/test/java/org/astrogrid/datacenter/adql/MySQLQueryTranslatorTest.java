/*$Id: MySQLQueryTranslatorTest.java,v 1.1 2003/09/02 14:41:15 nw Exp $
 * Created on 29-Aug-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.adql;
import java.io.*;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.datacenter.adql.generated.*;
import org.astrogrid.datacenter.queriers.mysql.MySqlQueryTranslator;
import java.util.Properties;
/** test the query translator - maybe n the wrong package, but convenient to have it here for now.
 * @author Noel Winstanley nw@jb.man.ac.uk 29-Aug-2003
 * * @todo add wider range of tests.
 */
public class MySQLQueryTranslatorTest extends ExamplesTest {

    /**
     * Constructor for MySQLVisitorTest.
     * @param arg0
     */
    public MySQLQueryTranslatorTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MySQLQueryTranslatorTest.class);
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
            MySqlQueryTranslator visitor = new MySqlQueryTranslator();
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
$Log: MySQLQueryTranslatorTest.java,v $
Revision 1.1  2003/09/02 14:41:15  nw
added tests for ADQL parser
 
*/