/*
 * $Id: DummyQuerier.java,v 1.1 2004/02/16 23:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sitedebug;
import org.astrogrid.datacenter.queriers.*;

import java.io.IOException;
import java.util.Date;

import org.astrogrid.datacenter.axisdataserver.types.Query;

/**
 * A 'blind' querier that ignores the incoming query and returns a prepared
 * VOTable
 *
 * @author M Hill
 */

public class DummyQuerier extends Querier
{
   public DummyQuerier(String id, Query query) throws IOException
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

