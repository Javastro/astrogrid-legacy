/*
 * $Id: DummyQuerier.java,v 1.3 2003/11/25 14:21:49 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.util.Date;

import org.astrogrid.datacenter.axisdataserver.types.Query;

/**
 * DummyQuerier.java
 *
 * @author M Hill
 */

public class DummyQuerier extends Querier
{
   public DummyQuerier() throws IOException
   {
      super();
      this.setHandle(QuerierManager.generateHandle());
   }

   public QueryResults queryDatabase(Query query ) throws DatabaseAccessException
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

