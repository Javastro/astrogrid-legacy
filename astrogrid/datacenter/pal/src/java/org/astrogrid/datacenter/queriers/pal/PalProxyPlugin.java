/*
 * $Id: PalProxyPlugin.java,v 1.1 2004/10/12 23:09:53 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.pal;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.VotableInResults;
import org.astrogrid.datacenter.queriers.sql.StdSqlMaker;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.slinger.UriTarget;

/**
 * A plugin that passes the query on to a remote PAL installation
 *
 *  * @author M Hill
 */

public class PalProxyPlugin extends QuerierPlugin {
   
   public static final String PAL_TARGET = "datacenter.palproxy.targetstem";
   
   public PalProxyPlugin(Querier querier)  {
      super(querier);
   }
   

   /** performs a synchronous call to the database, submitting the given query
    *
    */
   public void askQuery() throws IOException {
      useServlet();

   }
   
   
   /** Use the remote PAL's servlet interface */
   public void useServlet() throws IOException {
      URL targetPal = new URL(SimpleConfig.getSingleton().getUrl(PAL_TARGET)+"/SubmitAdqlSql");
      
      querier.getStatus().addDetail("Proxying to: "+targetPal);
      log.info("Proxying to: "+targetPal);
      
      HttpURLConnection connection = (HttpURLConnection) targetPal.openConnection();
      
//      connection.setRequestProperty("AdqlXml", Query2Adql074.makeAdql(querier.getQuery()));
      connection.setRequestMethod("POST");
      connection.setRequestProperty("AdqlSql", new StdSqlMaker().getSql(querier.getQuery()));
      connection.setRequestProperty("Format", "VOTABLE");

      if (querier.getQuery().getTarget() instanceof UriTarget) {
         connection.setRequestProperty("Target", ((UriTarget) querier.getQuery().getTarget()).toURI().toString());
      }

      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      
      InputStream in = connection.getInputStream();

      if (!querier.getQuery().getTarget().isForwardable()) {
         VotableInResults vot = new VotableInResults(querier, in);
         vot.send(querier.getQuery().getResultsDef(), querier.getUser());
      }
      
   }

   /** Use the remote PAL's SOAP interface */
   public void useSoap() {
      throw new UnsupportedOperationException("Use the servlet method for now");
   }
   
}


