/*
 * $Id: DummyQuerier.java,v 1.5 2003/12/01 20:58:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.util.Date;

/**
 * DummyQuerier.java
 *
 * @author M Hill
 */

public class DummyQuerier extends Querier
{
   public DummyQuerier() throws IOException
   {
      super(QuerierManager.generateQueryId(), null);
   }

   public QueryResults doQuery() throws DatabaseAccessException
   {
      Date today = new Date();
      return new DummyQueryResults("Created "+ today.getDate()+"-"+today.getMonth()+"-"+today.getYear()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
   }

   /** Publicicise set error status for test purposes */
   public void setErrorStatus(Throwable th)
   {
      super.setErrorStatus(th);
   }




}

