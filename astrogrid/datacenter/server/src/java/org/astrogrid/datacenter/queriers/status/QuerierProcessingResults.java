/*
 * $Id: QuerierProcessingResults.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryState;

public class QuerierProcessingResults extends QuerierQueried
{
   String progress = "Processing Results";
   
   public QuerierProcessingResults(Querier querier) {
      super(querier);
   }
   
   public QueryState getState() { return QueryState.RUNNING_RESULTS; }

   public String getProgress() {
      return progress;
   }
   
   public void setProgress(String someIndication) {
      progress = someIndication;
   }
}

/*
$Log: QuerierProcessingResults.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
