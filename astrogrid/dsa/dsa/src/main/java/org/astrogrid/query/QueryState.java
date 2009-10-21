package org.astrogrid.query;
/**
 * The possible states of a query.
 *
 *
 * @author M Hill
 * @author G Rixon
 */

public enum QueryState {

   /** Service has been initialised (ie created) but no other methods called */
   CONSTRUCTED(0, "Constructed"),

   /** Service instance is waiting in queue */
   QUEUED(5, "Queued"),

   /** Service instance is starting, ie initialising... */
   STARTING(10, "Starting"),

   /** Service instance is running, ie query is being processed... */
   RUNNING_QUERY(20, "Querying"),

   /** Service instance is running, query has finished (probably waiting for results to be processed)... */
   QUERY_COMPLETE(30, "Query Complete"),

   /** Service is processing results, eg converting to VOTable */
   RUNNING_RESULTS(40, "Processing Results"),

   /** Service instance has finished/closed/no longer available */
   FINISHED(50, "COMPLETED"),

   /** Service instance has aborted, is finished */
   ABORTED(50, "ABORTED"),

   /** Service instance is unknown, or cannot provide state */
   UNKNOWN(-1, "Unknown"),

   /** Service has had an error (usually in spawned thread).  'Last' in order
    * as no other activities should take place afterwards! */
   ERROR(999, "ERROR");

   /** A job can only progress in a particular order; this ordering is defined
    * by this variable. -1 means irrelevent (eg for UNKNOWN) */
   private int order = 0;

   /**
    * A label for the state.
    */
   String label;

   /** 
    * Creates a new instance with the given order position and description.
    */
   private QueryState(int givenOrder, String description) {
     order = givenOrder;
     label = description;
   }

   /**
    * Reveals the order of precedence of this state.
    *
    * @return The order.
    */
   public int getOrder() {
      return order;
   }
   
   /**
    * Returns true if the order of this instance is before the given one, ie
    * this.order < givenStatus.order
    */
   public boolean isBefore(QueryState givenStatus) {
      return (this.order < givenStatus.order);
   }

   /**
    * Supplies the label for the state.
    */
   @Override
   public String toString() {
     return label;
   }

}