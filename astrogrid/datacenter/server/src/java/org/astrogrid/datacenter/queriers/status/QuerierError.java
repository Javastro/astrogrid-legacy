/*
 * $Id: QuerierError.java,v 1.2 2004/03/14 00:39:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import org.astrogrid.datacenter.query.QueryState;

public class QuerierError extends QuerierStatus implements QuerierClosed
{
   Throwable cause;
   String message;
   
   public QuerierError(String givenMessage, Throwable causeOfError) {
      this.cause = causeOfError;
      this.message = givenMessage;
   }
   
   public QueryState getState() { return QueryState.ERROR; }

   public Throwable getCause() {
      return cause;
   }
   
   public String message() {
      return message;
   }

   public String toString() {
      return super.toString()+": "+cause.getMessage()+" ("+message+")";
   }
   
}

/*
$Log: QuerierError.java,v $
Revision 1.2  2004/03/14 00:39:55  mch
Added error trapping to DataServer and setting Querier error status

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
