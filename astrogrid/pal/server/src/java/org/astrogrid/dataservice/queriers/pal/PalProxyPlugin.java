/*
 * $Id: PalProxyPlugin.java,v 1.6 2006/06/15 16:50:10 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.pal;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Date;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;
import org.astrogrid.dataservice.metadata.v0_10.ProxyResourceSupport;
import org.astrogrid.dataservice.queriers.DefaultPlugin;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.VotableInResults;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.query.Query;
//import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;

/**
 * A plugin that passes the query on to a remote PAL installation
 *
 *  * @author M Hill
 */

public class PalProxyPlugin extends DefaultPlugin implements VoResourcePlugin {
   
   
   public static final String PAL_TARGET = "datacenter.palproxy.targetstem";

   /** false if forwardable targets are given to target; true if results must come back through the proxy */
   public static final String PROXY_RESULTS = "datacenter.palproxy.proxyresults";

   private boolean proxyResults = true;
   
   public PalProxyPlugin() {
      proxyResults = ConfigFactory.getCommonConfig().getBoolean(PROXY_RESULTS, proxyResults);
   }
   
   /** performs a synchronous call to the database, submitting the given query
    *
    */
   public void askQuery(Principal user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus(), query.toString()));
      useServlet(query, querier);
   }
   
   /** Use the Datacenter delegate
   public void useDelegate(Query query, Querier querier) throws IOException {

      proxyResults = true; //need an asynch way of specifying target. Or just use servlets
      
      String endpoint = ConfigFactory.getCommonConfig().getUrl(PAL_TARGET)+"/services/AxisDataService05";
      
      QuerySearcher delegate = null;
      try {
         delegate = DatacenterDelegateFactory.makeQuerySearcher(
            querier.getUser(),
            endpoint,
            DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);

      }
      catch (ServiceException e) {
         throw new IOException(e+", connecting to "+endpoint);
      }

      delegate.setTimeout(60000);
      InputStream in = delegate.askQuery(query);
      
      if (proxyResults) {
         VotableInResults vot = new VotableInResults(querier, in);
         vot.send(query.getResultsDef(), querier.getUser());
      }
      
   }
   
   
   /** Use the remote PAL's servlet interface */
   public void useServlet(Query query, Querier querier) throws IOException {
      URL targetPal = new URL(ConfigFactory.getCommonConfig().getUrl(PAL_TARGET)+"/SubmitAdql");
      
      querier.getStatus().addDetail("Proxying to: "+targetPal);
      log.info("Proxying Query to: "+targetPal);
      
      URLConnection connection = targetPal.openConnection();
      
//      connection.setRequestProperty("AdqlXml", Query2Adql074.makeAdql(querier.getQuery()));
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setUseCaches(false);
      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      //write POST parameters
      DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//      out.writeBytes( "AdqlSql="+URLEncoder.encode(new StdSqlMaker().getSql(querier.getQuery()))+
      //out.writeBytes( "AdqlXml="+URLEncoder.encode(Adql074Writer.makeAdql(query), "UTF-8")+
      out.writeBytes( "AdqlXml="+URLEncoder.encode(query.getAdqlString(), "UTF-8")+
                      "&Format="+URLEncoder.encode("VOTABLE", "UTF-8")
                    );

      if (!(query.getTarget() instanceof UrlSourceTarget)) {
         proxyResults = true; //has to proxy non-forwardable targets, eg streams
      }
      
      if (proxyResults) {
         //send results back to this proxy
         out.writeBytes("&TargetResponse="+URLEncoder.encode("true", "UTF-8"));
      }
      else {
         out.writeBytes("&TargetURI="+URLEncoder.encode( ((UrlSourceTarget) query.getTarget()).toURI().toString(), "UTF-8"));
      }
      out.flush();
      out.close();
      
//done already      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      querier.getStatus().addDetail("Submitted to target at "+new Date());
      
      InputStream in = connection.getInputStream();

      if (proxyResults) {
         VotableInResults vot = new VotableInResults(querier, in);
         vot.send(query.getResultsDef(), querier.getUser());
      }
      
   }

   /** Returns just the number of matches rather than the list of matches */
   public long getCount(Principal user, Query query, Querier querier) throws IOException {
            throw new UnsupportedOperationException("Not done yet");
   }

   /**
    * Returns the VOResource elements of the remote service.
    * @deprecated - use UrlResourcePlugin to get the rdbms resource, and CeaResourceServer to
    * build the righr CEA stuff for *this* service
    */
   public String getVoResource() throws IOException {
      URL targetPal = new URL(ConfigFactory.getCommonConfig().getUrl(PAL_TARGET)+"/GetMetadata");
      log.info("Proxying VoResources to: "+targetPal);

      ProxyResourceSupport proxyer = new ProxyResourceSupport();
      return proxyer.makeLocal(targetPal.openStream());
   }
   
   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return VotableInResults.listFormats();
   }
   
   
}




