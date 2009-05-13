/*
 * $Id: PrecannedPlugin.java,v 1.1.1.1 2009/05/13 13:20:52 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.test;
import java.io.IOException;
import java.security.Principal;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
      GregorianCalendar calendar = new GregorianCalendar();
      PrecannedResults results = new PrecannedResults(querier, 
            "Created "+ 
            calendar.get(Calendar.DAY_OF_MONTH)+
            "-"+
            calendar.get(Calendar.MONTH)+
            "-"+
            (calendar.get(Calendar.YEAR) - 1900) +
            " "+
            calendar.get(Calendar.HOUR_OF_DAY) +
            ":"+
            calendar.get(Calendar.MINUTE) +
            ":"+
            calendar.get(Calendar.SECOND)
            );
      
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

