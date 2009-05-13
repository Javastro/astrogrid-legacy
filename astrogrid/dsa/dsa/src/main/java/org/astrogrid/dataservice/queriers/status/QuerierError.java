/*
 * $Id: QuerierError.java,v 1.1.1.1 2009/05/13 13:20:26 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.status;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.astrogrid.dataservice.queriers.QuerierPluginException;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.QueryState;

public class QuerierError extends QuerierStatus implements QuerierClosed
{
   Throwable cause;
   String errorMsg;
   
   public QuerierError(QuerierStatus previousStatus, String givenMessage, Throwable causeOfError) {
      super(previousStatus, ERROR);
      this.errorMsg = givenMessage;

      //unwrap wrapping exceptions
      if ((causeOfError != null) && (causeOfError.getCause() != null) && (
            (causeOfError instanceof QuerierPluginException) ||
            (causeOfError instanceof QueryException)
         ))
         
      {
         addDetail(causeOfError.toString());
         addDetail("Caused By: ");
         causeOfError = causeOfError.getCause();
      }

      this.cause = causeOfError;
      StringWriter sw = new StringWriter();
      cause.printStackTrace(new PrintWriter(sw));
      addDetail(sw.toString());
   }
   
   public QueryState getState() { return QueryState.ERROR; }

   public Throwable getCause() {
      return cause;
   }
   
   public String message() {
      return errorMsg;
   }

   public String toString() {
      return super.toString()+": "+cause.getMessage()+" ("+errorMsg+")";
   }

   /** Returns true */
   public boolean isFinished() { return true; }
   
}

/*
$Log: QuerierError.java,v $
Revision 1.1.1.1  2009/05/13 13:20:26  gtr


Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.2.30.1  2004/11/25 18:33:43  mch
more status (incl persisting) more tablewriting lots of fixes

Revision 1.2  2004/10/01 18:04:59  mch
Some factoring out of status stuff, added monitor page

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.6  2004/03/16 17:52:20  mch
Fix for nul cause of cause

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
