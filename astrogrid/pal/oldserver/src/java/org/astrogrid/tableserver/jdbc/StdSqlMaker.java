/*$Id: StdSqlMaker.java,v 1.2 2006/06/15 16:50:10 clq2 Exp $
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.tableserver.jdbc.SqlMaker;
import org.astrogrid.query.Query;

/**
 * A 'standard' translator that creates 'standard' SQL. Uses the 'StdSqlWriter'
 */
public class StdSqlMaker implements SqlMaker  {

   
   /**
    * Constructs an SQL statement for the given ADQL document by getting the
    * (super) ADQL/sql and replacing the region
    */
   public String makeSql(Query query) throws IOException {
      
      StdSqlWriter sqlMaker = new StdSqlWriter();
      query.acceptVisitor(sqlMaker);
      
      return sqlMaker.getSql();
   }
   
   /**
    * Constructs an SQL count statement for the given Query.
    */
   public String makeCountSql(Query query) throws IOException {

      CountSqlWriter countWriter = new CountSqlWriter();
      query.acceptVisitor(countWriter);
      return countWriter.getSql();
   }
   
}


/*
 $Log: StdSqlMaker.java,v $
 Revision 1.2  2006/06/15 16:50:10  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/20 15:23:08  kea
 Checking old sources in in oldserver directory (rather than just
 deleting them, might still be useful).

 Revision 1.2  2005/05/27 16:21:05  clq2
 mchv_1

 Revision 1.1.24.2  2005/05/13 16:56:32  mch
 'some changes'

 Revision 1.1.24.1  2005/04/29 16:55:47  mch
 prep for type-fix for postgres

 Revision 1.1  2005/03/10 16:42:55  mch
 Split fits, sql and xdb

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.2  2005/02/17 18:17:46  mch
 Moved SqlWriters back into server as they need metadata information

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.6.12.4  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.6.12.3  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.6.12.2  2004/11/25 00:30:56  mch
 that fiddly sky package

 Revision 1.6.12.1  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.6  2004/11/03 01:35:18  mch
 PAL_MCH_Candidate2 merge Part II

 Revision 1.2.2.4  2004/11/01 12:25:29  mch
 Fix for dec -90 to +90

 Revision 1.2.2.3  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.2.2.2  2004/10/22 14:34:56  mch
 fixes for limiting sql on ms sql server

 Revision 1.2.2.1  2004/10/22 09:05:15  mch
 Moved SqlMakers back to server

 Revision 1.7.6.1  2004/10/21 16:14:21  mch
 Changes to take home

 Revision 1.7  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.6.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.6  2004/10/13 01:30:58  mch
 Added adqlsql (keeps CIRCLE)

 Revision 1.5  2004/10/12 22:45:45  mch
 Added spaces around operators so SqlParser can work with it

 Revision 1.4  2004/10/08 15:19:36  mch
 Removed unnecessary imports

 Revision 1.3  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.2  2004/10/06 22:24:58  mch
 Fixed wrong brackets in replaceRegion

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.24  2004/09/08 21:55:14  mch
 Uncommented SQL/ADQL tests

 Revision 1.23  2004/09/07 13:22:26  mch
 Throws better error if ADQL 0.5 is submitted

 Revision 1.22  2004/09/07 02:28:29  mch
 Removed ADQL 0.5 tests

 Revision 1.21  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.20  2004/08/27 09:31:16  mch
 Added limit, order by, some page tidying, etc

 Revision 1.19  2004/08/24 20:08:31  mch
 Moved xslts to classpaths to fix ADQL-SQL translation problems, slightly improved query builder pages

 Revision 1.18  2004/08/24 19:06:44  mch
 Improvements to JSP pages, lots to query building and translating

 Revision 1.17  2004/08/24 12:55:09  mch
 Minor fixes to xslt translator

 Revision 1.16  2004/08/06 12:04:19  mch
 Added unit description to conesearch columns to cope with ESO milliarcseconds (& others in future)

 Revision 1.6.10.1  2004/08/05 17:57:08  mch
 Merging Itn06 fixes into Itn05

 Revision 1.15  2004/08/05 10:56:56  mch
 Fix for negative dec

 Revision 1.14  2004/08/05 09:52:59  mch
 Removed aql073 and added 08

 Revision 1.13  2004/08/02 11:35:03  mch
 Commented out incorrect square bounds maker

 Revision 1.12  2004/07/14 18:04:25  mch
 Fixed SQL/Angles

 Revision 1.11  2004/07/12 23:26:51  mch
 Fixed (somewhat) SQL for cone searches, added tests to Dummy DB

 Revision 1.10  2004/07/12 14:12:04  mch
 Fixed ADQL 0.7.4 xslt

 Revision 1.9  2004/07/07 19:33:59  mch
 Fixes to get Dummy db working and xslt sheets working both for unit tests and deployed

 Revision 1.8  2004/07/06 18:48:34  mch
 Series of unit test fixes

 Revision 1.7  2004/07/01 23:07:14  mch
 Introduced metadata generator

 Revision 1.6  2004/03/17 21:03:20  mch
 Added SQL transformation tests

 Revision 1.5  2004/03/17 18:03:20  mch
 Added v0.8 ADQL

 Revision 1.4  2004/03/17 01:47:26  mch
 Added v05 Axis web interface

 Revision 1.3  2004/03/14 16:55:48  mch
 Added XSLT ADQL->SQL support

 Revision 1.2  2004/03/12 20:04:57  mch
 It05 Refactor (Client)

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 
 */



