/*
 * $Id: NvoConePlugin.java,v 1.2 2006/06/15 16:50:10 clq2 Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.dataservice.queriers.nvocone;

import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.astrogrid.dataservice.queriers.DefaultPlugin;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.VotableInResults;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.dataservice.DatacenterException;
import org.astrogrid.query.Query;
import org.astrogrid.query.keyword.KeywordMaker;
import org.astrogrid.geom.Angle;

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
   public void askQuery(Principal user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus(), query.toString()));

      if (tableUrls == null) initialise();

      KeywordMaker maker = new KeywordMaker(query);
      
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
   public long getCount(Principal user, Query query, Querier querier) throws IOException {
       return getCountFromResults(user, query, querier);
   }
   
   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return VotableInResults.listFormats();
   }
   
}

/*
$Log: NvoConePlugin.java,v $
Revision 1.2  2006/06/15 16:50:10  clq2
PAL_KEA_1612

Revision 1.1.2.1  2006/04/20 15:24:22  kea
Missed bits.

Revision 1.2  2005/03/21 18:45:55  mch
Naughty big lump of changes

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.7.2.4  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.7.2.3  2004/11/25 00:30:56  mch
that fiddly sky package

Revision 1.7.2.2  2004/11/24 20:59:37  mch
doc fixes and added slinger browser

Revision 1.7.2.1  2004/11/22 00:57:16  mch
New interfaces for SIAP etc and new slinger package

Revision 1.7  2004/11/12 13:49:12  mch
Fix where keyword maker might not have had keywords made

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



