/*
 * $Id: DummyQuerier.java,v 1.6 2003/12/03 19:37:03 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.util.Date;
import org.astrogrid.datacenter.axisdataserver.types._query;

/**
 * DummyQuerier.java
 *
 * @author M Hill
 */

public class DummyQuerier extends Querier
{
   public DummyQuerier(String id, _query query) throws IOException
   {
      super(id, query);
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

