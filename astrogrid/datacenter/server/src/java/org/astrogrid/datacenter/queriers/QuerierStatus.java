/*
 * $Id: QuerierStatus.java,v 1.3 2004/03/10 02:36:25 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.util.Date;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.store.Agsl;

/**
 * Represents current querier status.
 *
 * @author M Hill
 */


public class QuerierStatus
{
   /** Timestamp for this status - ie when it was generated */
   Date atTime;
   
   String id;
   QueryState state = QueryState.UNKNOWN;
   
   /** Detail for things like error messages */
   String detail = null;

   
   public QuerierStatus(Querier forQuerier) {
      this.id = forQuerier.getQueryId();
      this.state = forQuerier.getState();
      atTime = new Date();
//    if (state.getOrder() > QueryState.QUERY_COMPLETE.getOrder()) {
//       detail = "Found "+forQuerier.getResults().getCount()
//    }
   }

   public String toString() {
      return "Querier ["+id+"] Status ="+state;
   }
   
   public QueryState getState() {
      return state;
   }
 
   public Agsl getResults() {
      //if the querier has finished, we should know where to find the results
      return null;
   }

   public String getDetail() {
      return detail;
   }
   
}

/*
$Log: QuerierStatus.java,v $
Revision 1.3  2004/03/10 02:36:25  mch
Added getCount

Revision 1.2  2004/03/08 00:31:28  mch
Split out webservice implementations for versioning

Revision 1.1  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

 */

