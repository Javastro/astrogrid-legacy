/*
 * $Id: NvoConePlugin.java,v 1.1 2004/03/13 01:06:03 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.queriers.nvocone;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.query.ConeQueryMaker;
import org.astrogrid.datacenter.query.ConeQuery;

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
    * Simple blocking query; submit Query
    */
   public void askQuery() throws IOException {
      
      //get cone query
      ConeQueryMaker maker = new ConeQueryMaker();
      ConeQuery coneQuery = maker.getConeQuery(querier.getQuery());
      
      URL url = makeUrl(coneQuery);
      
      //test url
      url.openConnection();
   }
   
   /**
    * Attempt to stop a query - does nothing.
    */
   public void abortQuesry(String id) throws IOException {

   }
   
      
   
}

/*
$Log: NvoConePlugin.java,v $
Revision 1.1  2004/03/13 01:06:03  mch
It05 Refactor (Client)

Revision 1.5  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.4  2003/11/18 00:34:37  mch
New Adql-compliant cone search

Revision 1.3  2003/11/17 16:59:12  mch
ConeSearcher.coneSearch now returns stream not parsed element, throws IOException

Revision 1.2  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates



*/


