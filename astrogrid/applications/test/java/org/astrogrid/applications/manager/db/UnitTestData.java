/*
 * $Id: UnitTestData.java,v 1.1 2003/12/01 22:24:59 pah Exp $
 * 
 * Created on 19-Sep-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Encapsulates test data within a class. The Class
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
   final class UnitTestData {
   
   private static String[] sqlcmds = {
   "insert into accounts  values ('test@login','password','This is a testuser');",
   "insert into accounts  values ('test@create','password2','This is another testuser');",
   "insert into accounts  values ('test@authenticate','password3','This is another testuser')"
   };
   
   static void load(Connection conn) throws SQLException
   {
      Statement stmt = conn.createStatement();
      for (int i = 0; i < sqlcmds.length; i++) {
         stmt.execute(sqlcmds[i]);
      }
   }

}
