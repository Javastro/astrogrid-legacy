/*
 * $Id: UnitTestData.java,v 1.1 2003/12/04 13:26:25 pah Exp $
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

package org.astrogrid.applications.common.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Encapsulates test data within a class. Implemented as a series of sql commands - perhaps it would be better to do real jdbc.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
   final class UnitTestData {
   
   private static String[] sqlcmds = {
   "insert into execution  values (1,'jobstep1','testapp','2001-01-01 10:00', '2001-01-01 12:00');",
   };
   
   static void load(Connection conn) throws SQLException
   {
      Statement stmt = conn.createStatement();
      for (int i = 0; i < sqlcmds.length; i++) {
         stmt.execute(sqlcmds[i]);
      }
   }

}
