/*
 * $Id: PrecannedPlugin.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.test;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import org.astrogrid.dataservice.queriers.DefaultPlugin;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.TableResults;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.query.Query;


/**
 * A 'blind' querier that ignores the incoming query and returns a prepared
 * VOTable
 *
 * @author M Hill
 */

public class PrecannedPlugin extends DefaultPlugin
{

   public void askQuery(Principal user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus(), query.toString()));
      querier.getStatus().setMessage("Precanned Plugin");
      Date today = new Date();
      PrecannedResults results = new PrecannedResults(querier, "Created "+ today.getDate()+"-"+today.getMonth()+"-"+today.getYear()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds());
      
      results.send(query.getResultsDef(), querier.getUser());
   }

   public long getCount(Principal user, Query query, Querier querier) throws IOException {
      return 15;
   }

   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return TableResults.listFormats();
   }
   

}

