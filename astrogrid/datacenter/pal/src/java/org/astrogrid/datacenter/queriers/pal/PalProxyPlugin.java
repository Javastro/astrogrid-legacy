/*
 * $Id: PalProxyPlugin.java,v 1.3 2004/10/18 13:11:30 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.pal;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.VotableInResults;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.queriers.status.QuerierStatus;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.Query2Adql074;
import org.astrogrid.slinger.UriTarget;

/**
 * A plugin that passes the query on to a remote PAL installation
 *
 *  * @author M Hill
 */

public class PalProxyPlugin extends DefaultPlugin {
   
   public static final String PAL_TARGET = "datacenter.palproxy.targetstem";
   
   /** performs a synchronous call to the database, submitting the given query
    *
    */
   public void askQuery(Account user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      useServlet(query, querier);
   }
   
   
   /** Use the remote PAL's servlet interface */
   public void useServlet(Query query, Querier querier) throws IOException {
      URL targetPal = new URL(SimpleConfig.getSingleton().getUrl(PAL_TARGET)+"/SubmitAdql");
      
      querier.getStatus().addDetail("Proxying to: "+targetPal);
      log.info("Proxying to: "+targetPal);
      
      URLConnection connection = targetPal.openConnection();
      
//      connection.setRequestProperty("AdqlXml", Query2Adql074.makeAdql(querier.getQuery()));
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setUseCaches(false);
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      //write POST parameters
      DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//      out.writeBytes( "AdqlSql="+URLEncoder.encode(new StdSqlMaker().getSql(querier.getQuery()))+
      out.writeBytes( "AdqlXml="+URLEncoder.encode(Query2Adql074.makeAdql(query))+
                      "&Format="+URLEncoder.encode("VOTABLE")
                    );

      if (query.getTarget() instanceof UriTarget) {
         out.writeBytes("&Target="+URLEncoder.encode( ((UriTarget) query.getTarget()).toURI().toString()));
      }
      out.flush();
      out.close();
      
      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      
      InputStream in = connection.getInputStream();

      if (!query.getTarget().isForwardable()) {
         VotableInResults vot = new VotableInResults(querier, in);
         vot.send(query.getResultsDef(), querier.getUser());
      }
      
   }

   /** Use the remote PAL's SOAP interface */
   public void useSoap() {
      throw new UnsupportedOperationException("Use the servlet method for now");
   }
   
}


