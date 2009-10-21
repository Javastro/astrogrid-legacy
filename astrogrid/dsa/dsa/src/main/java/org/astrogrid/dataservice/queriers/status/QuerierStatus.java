/*
 * $Id: QuerierStatus.java,v 1.2 2009/10/21 19:01:00 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.status;

import org.astrogrid.query.QueryState;
import org.astrogrid.status.DefaultTaskStatus;

/**
 * Records the status and past activity of a querier
 */

public abstract class QuerierStatus extends DefaultTaskStatus
{

   
   /** A constructor makes a status from a previous status.  This means
    * we can carry previous information along with it
    *
   public QuerierStatus(Querier querier) {
      if ( (querier!= null) && (querier.getStatus() != null)) {
         //copy in old details to new ones
         String[] oldDetails = querier.getStatus().getDetails();
         for (int i=0;i<oldDetails.length;i++) { addDetail(oldDetails[i]); }
         id = querier.getId();
      }
   }
   */

   /** Initial constructor - should only be called by subclasses that form
    * the beginning of the task status chain (see 'previous')*/
   protected QuerierStatus() {
   }

   /** Constructor from a previous status */
   protected QuerierStatus(QuerierStatus oldStatus, String newStage) {
      super(oldStatus, newStage);
   }
   
   /** Returns true if this status should come before the given status */
   public boolean isBefore(QuerierStatus status)
   {
      return this.getState().getOrder() < status.getState().getOrder();
   }

   /** Subclasses should return the appropriate enumerated state */
   public abstract QueryState getState();

   @Override
   public String toString() {
      return getState().toString();
   }
   
   /** Returns a full human-readable message string */
   public String asFullMessage() {
      StringBuffer s = new StringBuffer(getState().toString()+": "+getMessage()+" ("+getProgressMsg()+")");
      String[] details = getDetails();
      for (int i = 0; i < details.length; i++) {
         s.append("\n"+details[i]);
      }
      return s.toString();
   }
   
   /** Start a new progress with the given descriptive note and max value for pos
    * (leave max -1 if unknown) */
   public void newProgress(String note, long max) {
      setProgressText(note);
      setProgressMax(max);
      setProgress(-1);
   }
   
   /** Resets the progress message to nothing - ie clears it */
   public void clearProgress() {
      newProgress("", -1);
   }
}

/*
$Log: QuerierStatus.java,v $
Revision 1.2  2009/10/21 19:01:00  gtr
V2009.1.01, merged.

Revision 1.1.1.1.2.1  2009/09/19 14:51:29  gtr
Starts of query is now noted.

Revision 1.1.1.1  2009/05/13 13:20:27  gtr


Revision 1.3  2007/03/21 18:59:41  kea
Preparatory work for v1.0 resources (not yet supported);  and also
cleaning up details of completed jobs to save memory.

Revision 1.2  2007/03/02 13:49:41  kea
Clarified comment location.

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.10.2.4  2004/11/26 18:17:21  mch
More status persisting, browsing and aborting

Revision 1.10.2.3  2004/11/25 18:33:43  mch
more status (incl persisting) more tablewriting lots of fixes

Revision 1.10.2.2  2004/11/24 20:59:37  mch
doc fixes and added slinger browser

Revision 1.10.2.1  2004/11/22 00:57:16  mch
New interfaces for SIAP etc and new slinger package

Revision 1.10  2004/11/11 20:42:50  mch
Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

Revision 1.9  2004/11/08 23:15:38  mch
Various fixes for SC demo, more store browser, more Vizier stuff

Revision 1.8  2004/11/08 15:03:23  mch
Added doc

Revision 1.7  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.3.10.2  2004/11/01 15:04:58  mch
Fix to progress pos update

Revision 1.3.10.1  2004/10/20 18:12:45  mch
CEA fixes, resource tests and fixes, minor navigation changes

Revision 1.3.12.1  2004/10/20 12:43:28  mch
Fixes to CEA interface to write directly to target

Revision 1.3  2004/10/05 14:57:01  mch
Added queued

Revision 1.2  2004/10/01 18:04:59  mch
Some factoring out of status stuff, added monitor page

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.3  2004/09/01 21:37:59  mch
Fixes for Servlets, more servlets and better and nicer status reports

Revision 1.2  2004/03/15 19:16:12  mch
Lots of fixes to status updates

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */

