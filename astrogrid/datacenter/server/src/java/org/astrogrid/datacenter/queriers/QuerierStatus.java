/*
 * $Id: QuerierStatus.java,v 1.1 2004/03/07 00:33:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import org.astrogrid.datacenter.query.QueryState;

/**
 * Represents current querier status.
 *
 * @author M Hill
 */


public class QuerierStatus
{
   String id;
   QueryState state = QueryState.UNKNOWN;
   String detail = null;
   
   public QuerierStatus(String forQueryId, QueryState givenState) {
      this.id = forQueryId;
      this.state = givenState;
   }

   public String toString() {
      return "Querier ["+id+"] Status ="+state;
   }
   
   public QueryState getState() {
      return state;
   }
   
}

/*
$Log: QuerierStatus.java,v $
Revision 1.1  2004/03/07 00:33:50  mch
Started to separate It4.1 interface from general server services

 */

