/*
 * $Id: NvoConePlugin.java,v 1.2 2004/10/06 21:12:17 mch Exp $
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
import org.astrogrid.datacenter.queriers.VotableInResults;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.condition.Circle;
import org.astrogrid.datacenter.query.condition.Condition;
import org.astrogrid.datacenter.query.condition.Function;

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
      
      Condition cone = querier.getQuery().getCriteria();
      
      //check that the query is simple and only a cone search
      if (!(cone instanceof Function) ||
           !( ((Function) cone).getName().equals("CIRCLE"))) {
         throw new QueryException("Only simple circle criteria are available on NVO cone search catalogues.  Specify one condition that is a circle function");
      }
   }

   /**
    * Makes the URL required to talk to the server
    */
   public URL makeUrl(Circle cone) throws IOException
   {
      String queryUrl = serverUrl;
      
      //add query stuff - there might already be query stuff in the base url
      if (serverUrl.indexOf("?")>-1) {
         queryUrl = queryUrl + "&";
      }
      else {
         queryUrl = queryUrl + "?";
      }
      
      return new URL(queryUrl+"RA="+cone.getRa()+"&DEC="+cone.getDec()+"&SR="+cone.getRadius());
   }
   
   
   /**
    * Simple blocking query; submit Query.  NB this routes the results through
    * this server, which is not necesssarily the best thing.  Ho hum.
    */
   public void askQuery() throws IOException {
      
      Circle cone = Circle.makeCircle( (Function) querier.getQuery().getCriteria());
      
      URL url = makeUrl(cone);
      
      //start query & pick up handle to results
      VotableInResults results = new VotableInResults(querier, url.openStream());

      if (!aborted) {
         results.send(querier.getQuery().getResultsDef(), querier.getUser());
      }
   }
   
   
}

/*
$Log: NvoConePlugin.java,v $
Revision 1.2  2004/10/06 21:12:17  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.2  2004/08/02 11:34:33  mch
Completed the askQuery


*/


