/*
 * $Id: QuerierStatus.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.status;

import java.util.Vector;
import org.astrogrid.datacenter.query.QueryState;

public abstract class QuerierStatus
{
   /** List of messages for user - eg errors, information, etc */
   Vector details = new Vector();
   
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
}

/*
$Log: QuerierStatus.java,v $
Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
