/*$Id: PostgresSqlTest.java,v 1.3 2006/08/21 15:39:30 clq2 Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.tableserver.jdbc.postgres;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.slinger.targets.ByteArrayTarget;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.xml.sax.SAXException;

/**
 * tests the table metadata resources
 *
 */
public class PostgresSqlTest extends TestCase  {
   
   
   //checks that the metadoc is valid and reads OK
   public void testPostgresSql() throws IOException, SAXException {

      String sql = "SELECT * FROM TABLEA, TABLEB Where circle('j2000', 20.0, 21.0, 6.0)";
      
   }
   
   
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
   
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(PostgresSqlTest.class);
    }
   
}



