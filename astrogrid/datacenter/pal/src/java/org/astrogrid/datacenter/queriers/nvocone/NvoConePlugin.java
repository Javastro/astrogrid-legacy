/*
 * $Id: NvoConePlugin.java,v 1.5 2004/11/11 23:23:29 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.queriers.nvocone;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.VotableInResults;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.condition.CircleCondition;
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

public class NvoConePlugin extends DefaultPlugin
{

   /** base url to service */
   protected String serverUrl = null;

   /**
    * Makes the URL required to talk to the server
    */
   public URL makeUrl(CircleCondition cone) throws IOException
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
    * Sends the query to the nvo cone search.  NB this routes the results through
    * this server, which is not necesssarily the best thing.  Ho hum.
    */
   public void askQuery(Account user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus()));
      
      Condition coneFunc = query.getCriteria();
      
      //check that the query is simple and only a cone search
      if (!(coneFunc instanceof Function) ||
           !( ((Function) coneFunc).getName().equals("CIRCLE"))) {
         throw new QueryException("Only simple circle criteria are available on NVO cone search catalogues.  Specify one condition that is a circle function");
      }

      CircleCondition circle = CircleCondition.makeCircle( (Function) coneFunc );
      
      URL url = makeUrl(circle);
      
      //start query & pick up handle to results
      VotableInResults results = new VotableInResults(querier, url.openStream());

      if (!aborted) {
         results.send(query.getResultsDef(), querier.getUser());
      }
   }
   
   /** Returns just the number of matches rather than the list of matches */
   public long getCount(Account user, Query query, Querier querier) throws IOException {
       return getCountFromResults(user, query, querier);
   }
   
}

/*
$Log: NvoConePlugin.java,v $
Revision 1.5  2004/11/11 23:23:29  mch
Prepared framework for SSAP and SIAP

Revision 1.4  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.3.6.1  2004/10/27 00:43:39  mch
Started adding getCount, some resource fixes, some jsps

Revision 1.3  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.2.2.1  2004/10/15 19:59:06  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.2  2004/10/06 21:12:17  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.2  2004/08/02 11:34:33  mch
Completed the askQuery


*/


