/*
 * $Id: DBSetupTest.java,v 1.3 2003/12/11 14:36:16 pah Exp $
 * 
 * Created on 02-Dec-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.common.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.AssertionFailedError;

/**
 * Test the basic database connectivity...
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class DBSetupTest extends BaseDBTestCase {


   /**
    * @param name
    */
   public DBSetupTest(String name) {
      super(name);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(DBSetupTest.class);
   }

   final public void testDB() {
      Statement stmt;
      
      try {
         stmt=conn.createStatement();
         if (stmt.execute("select * from exestat where program='testapp'")) {
            ResultSet rs = stmt.getResultSet();
            if(rs.next()){
               assertEquals("executionId", 0, rs.getInt("executionId"));
               assertEquals("jobstepId", "jobstep1", rs.getString("jobstepId"));
               assertEquals("program", "testapp", rs.getString("program"));
               
               //TODO should really do the dates as well....
              
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
