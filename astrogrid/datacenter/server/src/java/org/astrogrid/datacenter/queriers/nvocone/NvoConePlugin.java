/*
 * $Id: NvoConePlugin.java,v 1.2 2004/08/02 11:34:33 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.queriers.nvocone;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.query.ConeQueryMaker;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.io.Piper;

/**
 * The National Virtual Observatory, an American effort, defined a simple
 * cone search service:
 * @see http://www.us-vo.org/metadata/conesearch/
 * <p>
 * This plugin gives us the capability to query such datacenters
 * <p>
 * Cunning plan - there is no real need for this plugin to connect to the URL
 * unless it needs to stream the results back to the front end.  The URL can
 * just be given to the StoreClient to connect to.  No status info though...
 *
 * @author M Hill
 */

public class NvoConePlugin extends QuerierPlugin
{
   Log log = LogFactory.getLog(NvoConePlugin.class);

   /** base url to service */
   protected String serverUrl = null;

   public NvoConePlugin(Querier querier)  {
      super(querier);
   }

   /**
    * Makes the URL required to talk to the server
    */
   public URL makeUrl(ConeQuery coneQuery) throws IOException
   {
      String queryUrl = serverUrl;
      
      //add query stuff - there might already be query stuff in the base url
      if (serverUrl.indexOf("?")>-1) {
         queryUrl = queryUrl + "&";
      }
      else {
         queryUrl = queryUrl + "?";
      }
      
      return new URL(queryUrl+"RA="+coneQuery.getRa()+"&DEC="+coneQuery.getDec()+"&SR="+coneQuery.getRadius());
   }
   
   
   /**
    * Simple blocking query; submit Query.  NB this routes the results through
    * this server, which is not necesssarily the best thing.  Ho hum.
    */
   public void askQuery() throws IOException {
      
      //get cone query
      ConeQueryMaker maker = new ConeQueryMaker();
      ConeQuery coneQuery = maker.getConeQuery(querier.getQuery());
      
      URL url = makeUrl(coneQuery);
      
      //start query
      InputStream source = url.openStream();
      
      //where to send to
      Writer target = querier.getResultsTarget().resolveWriter(Account.ANONYMOUS);
      
      //pipe
      Piper.pipe(new InputStreamReader(source), target);
   }
   
   /**
    * Attempt to stop a query - does nothing.
    */
   public void abortQuesry(String id) throws IOException {

   }
   
      
   
}

/*
$Log: NvoConePlugin.java,v $
Revision 1.2  2004/08/02 11:34:33  mch
Completed the askQuery


*/


