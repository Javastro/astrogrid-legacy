/*
 * $Id: QuerierStatus.java,v 1.5 2004/10/25 10:43:12 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import java.util.Date;
import java.util.Vector;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.status.TaskStatus;

/**
 * Records the status and past activity of a querier
 */

public abstract class QuerierStatus implements TaskStatus
{
   /** List of messages for user - eg errors, information, etc */
   Vector details = new Vector();
   
   /** Current message - used for progress indication, etc */
   String note = "";

   /** Progress message */
   String progressNote = "";
   
   /** Progress index */
   long progressPos = -1;
   
   /** Progress max: -1 = unknown*/
   long progressMax = -1;


   /** Previous status */
   QuerierStatus previous = null;

   /** Timestamp of when this status occured */
   Date timestamp = new Date();
   
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

   /** Initial constructor - should only be called by subclasses that form
    * the beginning of the task status chain (see 'previous')*/
   protected QuerierStatus() {
   }
   
   /** Makes a status from a previous status.  This means
    * we can carry previous information along with it
    */
   public QuerierStatus(QuerierStatus previousStatus) {
      assert previousStatus != null;
      
      this.previous = previousStatus;
   }
   
   /** Returns ID of query this status refers to */
   public String getId() { return previous.getId(); }

   /** Returns the start time of the task */
   public Date getStartTime() { return previous.getStartTime(); }
   
   /** Returns the owner of the task */
   public Account getOwner() { return previous.getOwner(); }

   /** Returns the timestamp of this status - ie when the task reached this status */
   public Date getTimestamp() { return timestamp; }
   
   
   /** Returns true if this status should come before the given status */
   public boolean isBefore(QuerierStatus status)
   {
      return this.getState().getOrder() < status.getState().getOrder();
   }

   /** Subclasses should return the appropriate enumerated state */
   public abstract QueryState getState();
   
   public String toString() {
      return getState().toString();
   }
   
   /** Returns a full human-readable message string */
   public String asFullMessage() {
      StringBuffer s = new StringBuffer(getState().toString()+": "+getMessage()+" ("+getProgressMsg()+")");
      for (int i = 0; i < details.size(); i++) {
         s.append("\n"+details.get(i));
      }
      return s.toString();
   }
   
   public void addDetail(String message) {
      details.add(message);
   }

   
   public String[] getDetails() {
      return (String[]) details.toArray(new String[] {} );
   }

   public QuerierStatus getPrevious() {
      return previous;
   }
   
   public void setMessage(String newNote) { this.note = newNote; }
   
   public String getMessage() { return note; }
   
   public void newProgress(String note, long max) {
      progressNote = note;
      progressPos = -1;
      progressMax = max;
   }
      
   public void newProgress() {
      newProgress("", -1);
   }

   public void setProgress(long newProgress) {
      progressMax = newProgress;
   }
   public long getProgress() { return progressPos; }
   public long getProgressMax() { return progressMax; }
   public long getProgressMin() { return 0; }
   
   /** Returns the progress as a human readable string */
   public String getProgressMsg() {
      if (progressPos==-1) {
         return progressNote;
      }
      else if (progressMax==-1) {
         return progressNote+" "+progressPos;
      }
      else {
         int percent = (int) ( progressPos *100 / progressMax);
         return progressNote+" "+progressPos+" of "+progressMax;
      }
   }
   
}

/*
$Log: QuerierStatus.java,v $
Revision 1.5  2004/10/25 10:43:12  jdt
Merges from branch PAL_MCH

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
