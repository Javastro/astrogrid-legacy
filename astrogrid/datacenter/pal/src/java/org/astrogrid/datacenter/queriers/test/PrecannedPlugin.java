/*
 * $Id: PrecannedPlugin.java,v 1.8 2004/11/12 13:49:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.test;
import java.io.IOException;
import java.util.Date;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.Query;


/**
 * A 'blind' querier that ignores the incoming query and returns a prepared
 * VOTable
 *
 * @author M Hill
 */

public class PrecannedPlugin extends DefaultPlugin
{

   public void askQuery(Account user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      querier.getStatus().setMessage("Precanned Plugin");
      Date today = new Date();
      PrecannedResults results = new PrecannedResults(querier, "Created "+ today.getDate()+"-"+today.getMonth()+"-"+today.getYear()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
      
      results.send(query.getResultsDef(), querier.getUser());
   }

   public long getCount(Account user, Query query, Querier querier) throws IOException {
      return 15;
   }

   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return QueryResults.getFormats();
   }
   

}

