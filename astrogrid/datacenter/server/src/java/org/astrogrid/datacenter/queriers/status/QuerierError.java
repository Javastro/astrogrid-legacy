/*
 * $Id: QuerierError.java,v 1.3 2004/03/14 19:12:33 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.astrogrid.datacenter.query.QueryState;

public class QuerierError extends QuerierStatus implements QuerierClosed
{
   Throwable cause;
   String message;
   
   public QuerierError(String givenMessage, Throwable causeOfError) {
      this.cause = causeOfError;
      this.message = givenMessage;
      StringWriter sw = new StringWriter();
      cause.printStackTrace(new PrintWriter(sw));
      this.details.add(sw.toString());
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
Revision 1.3  2004/03/14 19:12:33  mch
Added stack trace to error details

Revision 1.2  2004/03/14 00:39:55  mch
Added error trapping to DataServer and setting Querier error status

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
