/*
 * $Id: QuerierStatus.java,v 1.3 2004/09/01 21:37:59 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import java.util.Vector;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryState;

/**
 * Records the status and past activity of a querier
 */

public abstract class QuerierStatus
{
   /** List of messages for user - eg errors, information, etc */
   Vector details = new Vector();
   
   /** Current message - used for progress indication, etc */
   String note = "";

   /** Progress message */
   String progressNote = "";
   
   /** Progress index */
   long progressPos = -1;
   
   /** Progress max */
   long progressMax = -1;
   
   /** A constructor makes a status from a previous status.  This means
    * we can carry previous information along with it
    */
   public QuerierStatus(Querier querier) {
      if ( (querier!= null) && (querier.getStatus() != null)) {
         //copy in old details to new ones
         String[] oldDetails = querier.getStatus().getDetails();
         for (int i=0;i<oldDetails.length;i++) { addDetail(oldDetails[i]); }
      }
   }
   
   public boolean isBefore(QuerierStatus status)
   {
      return this.getState().getOrder() < status.getState().getOrder();
   }
   
   public abstract QueryState getState();
   
   public String toString() {
      return getState().toString();
   }
   
   public void addDetail(String message) {
      details.add(message);
   }

   
   public String[] getDetails() {
      return (String[]) details.toArray(new String[] {} );
   }
   
   public void setNote(String newNote) { this.note = newNote; }
   
   public String getNote() { return note; }
   
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
   
   /** Returns the progress as a human readable string */
   public String getProgressMsg() {
      String s = progressNote+" "+progressPos;
      if (progressPos>-1) {
         return s+" of "+progressPos;
      }
      else {
         return s+" (of unknown)";
      }
   }
   
}

/*
$Log: QuerierStatus.java,v $
Revision 1.3  2004/09/01 21:37:59  mch
Fixes for Servlets, more servlets and better and nicer status reports

Revision 1.2  2004/03/15 19:16:12  mch
Lots of fixes to status updates

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
