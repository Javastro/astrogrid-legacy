/*
 * $Id: QuerierPlugin.java,v 1.1 2009/05/13 13:20:26 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers;



import java.io.IOException;
import java.security.Principal;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;

/**
 * Querier Plugins are used to carry out all the database specific backend
 * tasks.  They are given a parent at construction time; this is the Querier
 * class that provides all the information a young plugin needs, such as the
 * Query itself, a way of updating status, etc
 *
 */

public interface QuerierPlugin  {

   /** This is the method called to carry out the query.
     * Used by both synchronous (blocking) and asynchronous (threaded) querying
     * through processQuery. Should run the query and send the results although
     * the parent has methods to help with this.  The plugin should have everyting
     * tidied up and discarded as nec before returning - there is no close() method
     */
   public void askQuery(Principal user, Query query, Querier querier) throws IOException, QueryException;
   
   /** Abort - if this is called, try and top the query and tidy up.   */
   public void abort();
   
   /** Returns just the number of matches rather than the list of matches */
   public long getCount(Principal user, Query query, Querier querier) throws IOException, QueryException;
   
   /** Returns a string array of the formats that can be produced */
   public String[] getFormats();
}
/*
 $Log: QuerierPlugin.java,v $
 Revision 1.1  2009/05/13 13:20:26  gtr
 *** empty log message ***

 Revision 1.2  2006/06/15 16:50:08  clq2
 PAL_KEA_1612

 Revision 1.1.1.1.92.1  2006/04/21 11:54:05  kea
 Changed QueryException from a RuntimeException to an Exception.

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.5.2.1  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.5  2004/11/12 13:49:12  mch
 Fix where keyword maker might not have had keywords made

 Revision 1.4  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.3.6.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.3  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.2.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.2  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.16  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.15  2004/09/06 20:23:00  mch
 Replaced metadata generators/servers with plugin mechanism. Added Authority plugin

 Revision 1.14  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.13  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.12  2004/04/01 17:15:21  mch
 Attempt to remove plugin on close

 Revision 1.11  2004/03/22 12:31:10  mch
 Removed datacenter prefix from emailing keys

 Revision 1.10  2004/03/22 12:26:12  mch
 Removed writer close

 Revision 1.9  2004/03/18 18:32:45  mch
 Added info

 Revision 1.8  2004/03/18 00:31:33  mch
 Added adql 7.3.1 tests and max row information to status

 Revision 1.7  2004/03/15 19:16:12  mch
 Lots of fixes to status updates

 Revision 1.6  2004/03/15 17:13:21  mch
 changed to configurable from address

 Revision 1.5  2004/03/15 11:25:35  mch
 Fixes to emailer and JSP targetting

 Revision 1.4  2004/03/14 16:55:48  mch
 Added XSLT ADQL->SQL support

 Revision 1.3  2004/03/14 04:13:04  mch
 Wrapped output target in TargetIndicator

 Revision 1.2  2004/03/14 02:17:07  mch
 Added CVS format and emailer

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 */




