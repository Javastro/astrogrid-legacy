/*
 * $Id: DBSetupTest.java,v 1.2 2003/09/19 21:49:56 pah Exp $
 * 
 * Created on 07-Sep-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright ©2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.community.common.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.astrogrid.community.common.ConfigLoader;

import junit.framework.AssertionFailedError;

/**
 * @author pah
 * @version $Name:  $
 * @since iteration3
 */
public class DBSetupTest extends HsqlDBInMemTestCase {

   /**
    * Constructor for DBSetupTest.
    * @param name
    */
   public DBSetupTest(String name) {
      super(name);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(DBSetupTest.class);
   }

   /*
    * @see TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      // make sure that we are using a test setup...
      ConfigLoader.setConfigType(ConfigLoader.TEST_CONFIG); 

   }
   /**
    * Performs some simple tests to check that the test database has been set up properly
    */
   public void testSetup() {
      Statement stmt; 
      try {
         stmt = conn.createStatement();
         if(stmt.execute("SELECT accounts.ident,accounts.password,accounts.description FROM accounts WHERE accounts.ident = 'test@login'"))
         {
            ResultSet rs = stmt.getResultSet();
            if (rs.next()) {
               assertEquals("test@login", rs.getString("ident"));
               assertEquals("password", rs.getString("password"));
               assertEquals("This is a testuser", rs.getString("description"));
            }
            else{
               fail("no results returned from query");
            }
            if(rs.next()){
               fail("two results returned from query when there should be one");
            }
            
         }
         else
         {
            fail("no results from test query");
         }
      }
      catch (SQLException e) {

         throw new AssertionFailedError("sql error" + e.getMessage());
      }
      
   }
   

}
