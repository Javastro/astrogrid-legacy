/*
 * $Id: PrecannedPlugin.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.test;
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

public class PrecannedPlugin extends QuerierPlugin
{
   public PrecannedPlugin(Querier querier) throws IOException
   {
      super(querier);
   }

   public void askQuery() throws IOException
   {
      Date today = new Date();
      PrecannedResults results = new PrecannedResults("Created "+ today.getDate()+"-"+today.getMonth()+"-"+today.getYear()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
      
      processResults(results);
   }



}

