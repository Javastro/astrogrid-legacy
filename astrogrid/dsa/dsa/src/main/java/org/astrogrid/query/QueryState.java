/*
 * $Id: QueryState.java,v 1.1 2009/05/13 13:20:38 gtr Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.query;

import org.astrogrid.utils.TypeSafeEnumerator;

/**
 * Defines the service status' possible results.
 *
 *
 * @author M Hill
 */

public class QueryState extends TypeSafeEnumerator
{
   /** Service has been initialised (ie created) but no other methods called */
   public static final QueryState CONSTRUCTED = new QueryState(0, "Constructed");
   /** Service instance is waiting in queue */
   public static final QueryState QUEUED = new QueryState(5, "Queued");
   /** Service instance is starting, ie initialising... */
   public static final QueryState STARTING = new QueryState(10, "Starting");
   /** Service instance is running, ie query is being processed... */
   public static final QueryState RUNNING_QUERY = new QueryState(20, "Querying");
   /** Service instance is running, query has finished (probably waiting for results to be processed)... */
   public static final QueryState QUERY_COMPLETE = new QueryState(30, "Query Complete");
   /** Service is processing results, eg converting to VOTable */
   public static final QueryState RUNNING_RESULTS = new QueryState(40, "Processing Results");
   /** Service instance has finished/closed/no longer available */
   public static final QueryState FINISHED = new QueryState(50, "COMPLETED");
   /** Service instance has aborted, is finished */
   public static final QueryState ABORTED = new QueryState(50, "ABORTED");
   /** Service instance is unknown, or cannot provide state */
   public static final QueryState UNKNOWN = new QueryState(-1, "Unknown");
   /** Service has had an error (usually in spawned thread).  'Last' in order
    * as no other activities should take place afterwards! */
   public static final QueryState ERROR = new QueryState(999, "ERROR");

   /** A job can only progress in a particular order; this ordering is defined
    * by this variable. -1 means irrelevent (eg for UNKNOWN) */
   private int order = 0;

   /** Creates a new instance with the given order position and description */
   private QueryState(int givenOrder, String description)
   {
      super(description);
      this.order = givenOrder;
   }

   public int getOrder() {
      return order;
   }
   
   /**
    * Returns true if the order of this instance is before the given one, ie
    * this.order < givenStatus.order
    */
   public boolean isBefore(QueryState givenStatus)
   {
      return (this.order < givenStatus.order);
   }

   /**
    * Returns the Service Status for the given string.  Used to re-establish
    * the typesafe enumeration after reading status from streams
    */
   public static QueryState getFor(String status)
   {
      return (QueryState) getFor(QueryState.class, status);
   }

}

/*
$Log: QueryState.java,v $
Revision 1.1  2009/05/13 13:20:38  gtr
*** empty log message ***

Revision 1.2  2005/03/22 12:57:37  mch
naughty bunch of changes

Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.3  2004/10/05 14:55:41  mch
Added queued

Revision 1.2  2004/03/10 02:35:39  mch
Published order so we can compare

Revision 1.1  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

Revision 1.2  2003/11/17 20:47:57  mch
Adding Adql-like access to Nvo cone searches

Revision 1.1  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/09/15 22:05:34  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.3  2003/09/15 18:24:55  mch
Removed done todo

Revision 1.2  2003/09/15 14:33:27  mch
Fixes to reading status id and service id

Revision 1.1  2003/09/09 17:46:27  mch
New package for stuff common to client and server

Revision 1.2  2003/09/07 18:43:49  mch
Made typesafe, added order

Revision 1.1  2003/08/28 13:07:33  mch
Added service listener placeholders


*/

