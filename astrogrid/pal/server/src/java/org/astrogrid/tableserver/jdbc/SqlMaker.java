/*$Id: SqlMaker.java,v 1.1 2005/03/10 16:42:55 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.tableserver.jdbc;

import java.io.IOException;
import org.astrogrid.query.Query;

/**
 * Translates queries into SQL
 */
public interface SqlMaker  {
   

   /** A temporary key specifying whether the db columns asre in degrees or radians */
//   public static final String DB_COLS_IN_RADIANS = "conesearch.columns.in.radians";

   /**
    * Make an SQL string from the given Query */
   public String makeSql(Query query) throws IOException;

   /**
    * Make a COUNT SQL string from the given Query */
   public String makeCountSql(Query query) throws IOException;

}


/*
$Log: SqlMaker.java,v $
Revision 1.1  2005/03/10 16:42:55  mch
Split fits, sql and xdb

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.6.12.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.6  2004/11/03 01:35:18  mch
PAL_MCH_Candidate2 merge Part II

Revision 1.2.2.2  2004/10/27 00:43:39  mch
Started adding getCount, some resource fixes, some jsps

Revision 1.2.2.1  2004/10/22 09:05:15  mch
Moved SqlMakers back to server

Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.4  2004/08/06 12:04:19  mch
Added unit description to conesearch columns to cope with ESO milliarcseconds (& others in future)

Revision 1.2.10.1  2004/08/05 17:57:08  mch
Merging Itn06 fixes into Itn05

Revision 1.3  2004/07/12 23:26:51  mch
Fixed (somewhat) SQL for cone searches, added tests to Dummy DB

Revision 1.2  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 
*/
