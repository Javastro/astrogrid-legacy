/*$Id: SqlMaker.java,v 1.3 2004/10/25 00:49:17 jdt Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.queriers.sql;

import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;

/**
 * Translates queries into SQL
 */
public abstract class SqlMaker  {
   
   /** Key used to look up the column containing RA for cone searches */
   public static final String CONE_SEARCH_TABLE_KEY = "conesearch.table";
   public static final String CONE_SEARCH_RA_COL_KEY = "conesearch.ra.column";
   public static final String CONE_SEARCH_DEC_COL_KEY = "conesearch.dec.column";

   /** A temporary key specifying whether the db columns asre in degrees or radians */
   public static final String CONE_SEARCH_COL_UNITS_KEY = "conesearch.columns.units";
   
   /** Key used to look up the column containing HTM for cone searches */
   public static final String CONE_SEARCH_HTM_KEY = "conesearch.htm.column";

   /** Key specifying whether the db trig functions take degrees or radians */
   public static final String DB_TRIGFUNCS_IN_RADIANS = "db.trigfuncs.in.radians";

   /** A temporary key specifying whether the db columns asre in degrees or radians */
//   public static final String DB_COLS_IN_RADIANS = "conesearch.columns.in.radians";

   /**
    * Makes an SQL string from the given Query */
   public abstract String getSql(Query query) throws QueryException;

}


/*
$Log: SqlMaker.java,v $
Revision 1.3  2004/10/25 00:49:17  jdt
Merges from branch PAL_MCH

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
