/*
 * $Id: QuerierError.java,v 1.5 2004/03/15 19:16:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPluginException;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryState;

public class QuerierError extends QuerierStatus implements QuerierClosed
{
   Throwable cause;
   String message;
   
   public QuerierError(Querier querier, String givenMessage, Throwable causeOfError) {
      super(querier);
      this.message = givenMessage;

      //unwrap wrapping exceptions
      if ((causeOfError instanceof QuerierPluginException) ||
         (causeOfError instanceof QueryException))
      {
         addDetail(causeOfError.toString());
         addDetail("Caused By: ");
         causeOfError = causeOfError.getCause();
      }

      this.cause = causeOfError;
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
Revision 1.5  2004/03/15 19:16:12  mch
Lots of fixes to status updates

Revision 1.4  2004/03/15 17:11:31  mch
Better information

Revision 1.3  2004/03/14 19:12:33  mch
Added stack trace to error details

Revision 1.2  2004/03/14 00:39:55  mch
Added error trapping to DataServer and setting Querier error status

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
