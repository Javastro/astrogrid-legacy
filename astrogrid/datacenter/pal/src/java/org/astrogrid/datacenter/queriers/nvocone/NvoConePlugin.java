/*
 * $Id: NvoConePlugin.java,v 1.6 2004/11/12 10:44:54 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.queriers.nvocone;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.impl.cds.KeywordMaker;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.VotableInResults;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.sky.Angle;

/**
 * The National Virtual Observatory, an American effort, defined a simple
 * cone search service:
 * @see http://www.us-vo.org/metadata/conesearch/
 * <p>
 * This plugin gives us the capability to query such datacenters by mapping table
 * names to the URLs.
 * <p>
 * Cunning plan - there is no real need for this plugin to connect to the URL
 * unless it needs to stream the results back to the front end.  The URL can
 * just be given to the StoreClient to connect to.  No status info though...
 *
 * @author M Hill
 */

public class NvoConePlugin extends DefaultPlugin
{

   /** index of table names and the corresponding cone search */
   private static Hashtable tableUrls = null;

   /** populates the tableUrls with virtual table names and corresponding URLs.
    * At the moment this is hardwired, but should be loaded from config or even
    * better got from the registry. @see http://nvo.stsci.edu/voregistry/QueryRegistry.aspx */
   public static synchronized void initialise() {
      if (tableUrls != null) {
         tableUrls.put("VIRTUALSKY_MESSIER", "http://virtualsky.org/servlet/cover?CAT=messier");
         tableUrls.put("COPERNICUS", "http://archive.stsci.edu/copernicus/search.php?");
         tableUrls.put("HIPPARCOS", "http://chart.stsci.edu/GSCVO/HIPVO.jsp?");
      }
   }
   
   /** returns the list of virtual tables that correspond to the services */
   public static String[] getTables() {
      if (tableUrls == null) initialise();
      Vector tables = new Vector();
      Enumeration keys = tableUrls.keys();
      while (keys.hasMoreElements()) {
         Object key = keys.nextElement();
         tables.add(tableUrls.get(key));
      }
      return (String[]) tables.toArray(new String[] {} );
   }
   
   /**
    * Sends the query to the nvo cone search.  NB this routes the results through
    * this server, which is not necesssarily the best thing.  Ideally the URL should
    * be passed to the storepoint to upload directly.
    */
   public void askQuery(Account user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus()));

      if (tableUrls == null) initialise();

      KeywordMaker maker = new KeywordMaker();
      maker.makeKeywords(query);
      
      Angle ra = Angle.parseAngle(maker.getRequiredValue(maker.RA_KEYWORD).toString());
      Angle dec = Angle.parseAngle(maker.getRequiredValue(maker.DEC_KEYWORD).toString());
      Angle radius = Angle.parseAngle(maker.getRequiredValue(maker.RADIUS_KEYWORD).toString());

      String[] tables = query.getScope();
      if (tables.length>1) {
         throw new DatacenterException("Datacenters can only proxy to one NVO cone search at a time; just give one table in the FROM");
      }
      if (tables.length==0) {
         throw new DatacenterException("No table given in scope; check the metadata for valid tables");
      }
      
      String queryUrl = tableUrls.get(tables[0]).toString();
      if (queryUrl == null) {
         throw new DatacenterException("Table "+tables[0]+" is unknown - check the metadata for valid tables");
      }
      
      //add query stuff - there might already be query stuff in the base url
      if (queryUrl.indexOf("?")>-1) {
         queryUrl = queryUrl + "&";
      }
      else {
         queryUrl = queryUrl + "?";
      }
      
      URL url = new URL(queryUrl+"RA="+ra.asDegrees()+"&DEC="+dec.asDegrees()+"&SR="+radius.asDegrees());

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
Revision 1.6  2004/11/12 10:44:54  mch
More resources, siap stuff, ssap stuff, SSS

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



