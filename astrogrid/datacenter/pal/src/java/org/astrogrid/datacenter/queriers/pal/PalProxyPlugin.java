/*
 * $Id: PalProxyPlugin.java,v 1.12 2004/11/12 13:49:12 mch Exp $
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
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.DsaDomHelper;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.VotableInResults;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.Adql074Writer;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.slinger.targets.UriTarget;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

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
      proxyResults = SimpleConfig.getSingleton().getBoolean(PROXY_RESULTS, proxyResults);
   }
   
   /** performs a synchronous call to the database, submitting the given query
    *
    */
   public void askQuery(Account user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      useServlet(query, querier);
   }
   
   /** Use the Datacenter delegate */
   public void useDelegate(Query query, Querier querier) throws IOException {

      proxyResults = true; //need an asynch way of specifying target. Or just use servlets
      
      String endpoint = SimpleConfig.getSingleton().getUrl(PAL_TARGET)+"/services/AxisDataService05";
      
      QuerySearcher delegate = null;
      try {
         delegate = DatacenterDelegateFactory.makeQuerySearcher(
            Account.ANONYMOUS,
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
      URL targetPal = new URL(SimpleConfig.getSingleton().getUrl(PAL_TARGET)+"/SubmitAdql");
      
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
      out.writeBytes( "AdqlXml="+URLEncoder.encode(Adql074Writer.makeAdql(query))+
                      "&Format="+URLEncoder.encode("VOTABLE")
                    );

      if (!query.getTarget().isForwardable()) {
         proxyResults = true; //has to proxy non-forwardable targets, eg streams
      }
      
      if (proxyResults) {
         //send results back to this proxy
         out.writeBytes("&TargetResponse="+URLEncoder.encode("true"));
      }
      else {
         out.writeBytes("&TargetURI="+URLEncoder.encode( ((UriTarget) query.getTarget()).toURI().toString()));
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
   public long getCount(Account user, Query query, Querier querier) throws IOException {
            throw new UnsupportedOperationException("Not done yet");
   }

   /**
    * Returns the VOResource elements of the remote service.
    * @deprecated - use UrlResourcePlugin to get the rdbms resource, and CeaResourceServer to
    * build the righr CEA stuff for *this* service
    */
   public String[] getVoResources() throws IOException {
      URL targetPal = new URL(SimpleConfig.getSingleton().getUrl(PAL_TARGET)+"/GetMetadata");
      log.info("Proxying VoResources to: "+targetPal);
      InputStream vodescIn = targetPal.openStream();
      Document vodescription = null;
      try {
            vodescription = DomHelper.newDocument(vodescIn);
      }
      catch (ParserConfigurationException e) {
         throw new RuntimeException("Server not configured correctly: "+e);
      }
      catch (SAXException e) {
         throw new IOException(e+" reading VODescription from "+targetPal);
      }
      
      Element[] resources = DsaDomHelper.getChildrenByTagName(vodescription.getDocumentElement(), "Resource");

      String[] s = new String[resources.length];
      for (int i = 0; i < resources.length; i++) {
         s[i] = DomHelper.ElementToString(resources[i]);
      }
      return s;
   }
   
   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return VotableInResults.getFormats();
   }
   
   
}




