/*
 * $Id: QueryStatus.java,v 1.2 2003/11/17 20:47:57 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.query;

import org.astrogrid.util.TypeSafeEnumerator;

/**
 * Defines the service status' possible results.
 *
 *
 * @author M Hill
 */

public class QueryStatus extends TypeSafeEnumerator
{
   /** Service has been initialised (ie created) but no other methods called */
   public static final QueryStatus CONSTRUCTED = new QueryStatus(0, "Constructed");
   /** Service instance is starting, ie initialising... */
   public static final QueryStatus STARTING = new QueryStatus(10, "Starting");
   /** Service instance is running, ie query is being processed... */
   public static final QueryStatus RUNNING_QUERY = new QueryStatus(20, "Querying");
   /** Service instance is running, query has finished (probably waiting for results to be processed)... */
   public static final QueryStatus QUERY_COMPLETE = new QueryStatus(30, "Query Complete");
   /** Service is processing results, eg converting to VOTable */
   public static final QueryStatus RUNNING_RESULTS = new QueryStatus(40, "Processing Results");
   /** Service instance has finished/closed/no longer available */
   public static final QueryStatus FINISHED = new QueryStatus(50, "COMPLETED");
   /** Service instance is unknown, or cannot provide state */
   public static final QueryStatus UNKNOWN = new QueryStatus(-1, "Unknown");
   /** Service has had an error (usually in spawned thread).  'Last' in order
    * as no other activities should take place afterwards! */
   public static final QueryStatus ERROR = new QueryStatus(999, "ERROR");

   /** A job can only progress in a particular order; this ordering is defined
    * by this variable. -1 means irrelevent (eg for UNKNOWN) */
   private int order = 0;

   /** Creates a new instance with the given order position and description */
   private QueryStatus(int givenOrder, String description)
   {
      super(description);
      this.order = givenOrder;
   }

   /**
    * Returns true if the order of this instance is before the given one, ie
    * this.order < givenStatus.order
    */
   public boolean isBefore(QueryStatus givenStatus)
   {
      return (this.order < givenStatus.order);
   }

   /**
    * Returns the Service Status for the given string.  Used to re-establish
    * the typesafe enumeration after reading status from streams
    */
   public static QueryStatus getFor(String status)
   {
      return (QueryStatus) getFor(QueryStatus.class, status);
   }

}

/*
$Log: QueryStatus.java,v $
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

