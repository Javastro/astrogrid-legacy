/*
 * $Id: QuerierStatus.java,v 1.2 2004/03/15 19:16:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import java.util.Vector;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.query.QueryState;

public abstract class QuerierStatus
{
   /** List of messages for user - eg errors, information, etc */
   Vector details = new Vector();
   
   /** Current message - used for progress indication, etc */
   String note = "";
   
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
   
}

/*
$Log: QuerierStatus.java,v $
Revision 1.2  2004/03/15 19:16:12  mch
Lots of fixes to status updates

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
