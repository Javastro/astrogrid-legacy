/*
 * $Id: QuerierQueried.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryState;

/** Defines the state after a query has been run on the database but before,
 * for example, the results have been processed.  States after this one should
 * subclass this and update details such as where the results have gone to, etc */

public class QuerierQueried extends QuerierStatus
{
   long timeTaken;
   int resultsCount =-1;
   String resultsCanBeFoundAt = null;
   
   public QuerierQueried(Querier querier) {
      timeTaken = querier.getQueryTimeTaken();
//    resultsCount = querier.get
   }
   
   public QueryState getState() { return QueryState.QUERY_COMPLETE; }
   
   /** Returns where the results have been sent to, if applicabale. */
   public String getResults() {     return resultsCanBeFoundAt;   }

   /** Returns the number of results found; returns -1 if unknown */
   public int getResultsCount() {      return resultsCount; }
}

/*
$Log: QuerierQueried.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
