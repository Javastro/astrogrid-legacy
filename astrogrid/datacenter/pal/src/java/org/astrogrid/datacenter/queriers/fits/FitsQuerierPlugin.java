/*
 * $Id: FitsQuerierPlugin.java,v 1.8 2004/11/17 13:06:43 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.fits;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.community.Account;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.UrlListResults;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.queriers.xql.XqlMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.util.DomHelper;
import org.astrogrid.xmldb.eXist.server.QueryDBService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The Standard Fits Querier plugin uses an XML 'index' document to search on.
 * This index is generated once by running the IndexGenerator in this package
 * across all the files. A configuration setting points to this index file, which
 * is loaded into memory at the first query.
 *
 * However some indexes are too big to load, in which case the eXist XML database
 * might be used.  (This also gives better query options).  In this case, leave
 * the index references blank, and this class will send the query to the
 * org.astrogrid.xmldb.eXist.server.QueryDBService; see elsewhere (unhelpful) for
 * how to configure that.
 *
 *
 * @author M Hill
 */

public class FitsQuerierPlugin extends DefaultPlugin
{
   private static Document index = null;

   public final static String FITS_INDEX_URL = "datacenter.fits.index.url";
   public final static String FITS_INDEX_FILENAME = "datacenter.fits.index.filename";
   

   /** Plugin implementation - carries out query.
    */
   public void askQuery(Account user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus()));

      if (index == null) {
         loadIndex();
      }
      String[] filenames = null;

      XqlMaker maker = new XqlMaker();
      String xql = maker.getXql(query);
      
      filenames = useeXist(xql);
      
      if ((!aborted) && (filenames != null)) {
         querier.getStatus().addDetail(filenames+" files matched");
         UrlListResults results = new UrlListResults(querier, filenames);
         results.send(query.getResultsDef(), querier.getUser());
      }
   }

   /** Plugin implementation - returns number of matches
    */
   public long getCount(Account user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus()));

      if (index == null) {
         loadIndex();
      }
      String[] filenames = null;

      XqlMaker maker = new XqlMaker();
      String xql = maker.getXql(query);
      
      filenames = useeXist(xql);

      return filenames.length;
   }
   
   private String[] useeXist(String xql) throws IOException {
      Document resultDoc = null;
      String []files = null;
      try {
         QueryDBService qdb = new QueryDBService();
         resultDoc = qdb.runQuery("dcfitsfiles",xql);
      }catch(ParserConfigurationException pce) {
         throw new RuntimeException("Server configuration error",pce);
      }catch(SAXException se) {
         throw new IOException("FitsQuerierPlugin index not valid xml");
      }
      if(resultDoc != null) {
         NodeList fileNames = resultDoc.getElementsByTagName("Filename");
         files = new String[fileNames.getLength()];
         for(int i = 0;i < fileNames.getLength();i++) {
            if(fileNames.item(i).hasChildNodes()) {
               files[i] = fileNames.item(i).getFirstChild().getNodeValue();
            }//if
         }//for
      }//if
      return files;
   }

   
   /**
    * locates all the fits files in this dataset that overlap the given
    * circular region
    */
   public String[] coneSearch(double ra, double dec, double sr) throws IOException
   {
      Ellipse2D cone = new Ellipse2D.Double(ra-sr, dec-sr, sr*2,sr*2);
      Area matchingArea = new Area(cone);
      
      Hashtable intersectingFits = new Hashtable();
      
      //first locate all the coverage nodes in the index
      NodeList coverages = index.getElementsByTagName("Coverage");
      
      for (int i=0;i<coverages.getLength();i++)
      {
         //extract coverage region
         Area coverage = new Area(getCoverageFromIndex( (Element) coverages.item(i)));
         
         //does it intersect with circle?
         if (intersects(matchingArea, coverage))
         {
            //find parent tag describing whole file
            Element parent = (Element) coverages.item(i).getParentNode();
            
            String filename = DomHelper.getValue(parent, "Filename");
            //add if not already there - ie ignore duplicates, which might
            //happen if a FITS has several images
            if (intersectingFits.get(filename) == null)
            {
               intersectingFits.put(filename, filename);
            }
         }
      
         if (aborted) break;  //stop if we've been aborted
      }
      
      return (String[]) intersectingFits.values().toArray(new String[] {});
   }
   
   /**
    * Looks through the coverage in the index and returns a suitable 'shape' to
    * represent that region.  At the moment it just returns a 'general path' with
    * the given points in it
    */
   public GeneralPath getCoverageFromIndex(Element dom)
   {
      GeneralPath region = new GeneralPath();
      
      NodeList points = dom.getElementsByTagName("Point");
      
      for (int l=0;l<points.getLength();l++)
      {
         float ra = Float.parseFloat(DomHelper.getValue((Element) points.item(l), "RA"));
         float dec = Float.parseFloat(DomHelper.getValue((Element) points.item(l), "Dec"));

         if (l==0) {
            region.moveTo(ra, dec);
         } else {
            region.lineTo(ra, dec);
         }
      }
      
      return region;
   }
   
   /**
    * returns true if area 1 intersects with area 2
    * At the moment checks if the rectangular *boundaries* of area 1 intersect
    * with area 2.
    * @todo do properly
    */
   public boolean intersects(Area a1, Area a2)
   {
      return a2.intersects(a1.getBounds2D());
   }
   
   /**
    * Loads the index - one off operation
    */
   protected synchronized void loadIndex() throws IOException {
      if (index != null) return;

      //find the index
      URL url = SimpleConfig.getSingleton().getUrl(FITS_INDEX_URL, null);
      
      if (url == null) {
         String filename = SimpleConfig.getSingleton().getString(FITS_INDEX_FILENAME, null);
         if (filename != null) {
            File f = new File(filename);
            if (!f.exists()) {
               throw new ConfigException("Fits index "+filename+" given by "+FITS_INDEX_FILENAME+" in config ("+SimpleConfig.loadedFrom()+") does not exist");
            }
            url = f.toURL();
         }
         if (url == null) {
            throw new ConfigException("Fits index not specified with "+FITS_INDEX_URL+" or "+FITS_INDEX_FILENAME+" in config ("+SimpleConfig.loadedFrom()+")");
         }
      }
      else {
         String strURL = url.toExternalForm();

         //not quite sure what this does - and it won't work if the index has been made from a file (eg during unit tests)
         SimpleConfig.getSingleton().setProperty("exist.db.url",strURL.substring(0,strURL.indexOf("/servlet")));
      }
      
      
      //load & parse index
      try {
         index = DomHelper.newDocument(url.openStream());
      }
      catch (ParserConfigurationException e) {
         throw new RuntimeException("Server configuration error",e);
      }
      catch (SAXException e) {
         //throw new QuerierPluginException("FitsQuerierPlugin index not valid xml",e);
         throw new IOException(e+"reading index from "+url);

      }
   }
   
   
 
}

/*
 $Log: FitsQuerierPlugin.java,v $
 Revision 1.8  2004/11/17 13:06:43  jdt
 Rolled back to 20041115ish, see bugzilla 705

 Revision 1.6  2004/11/11 23:23:29  mch
 Prepared framework for SSAP and SIAP

 Revision 1.5  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.4  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.3.6.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.3  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.2.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.2  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.7  2004/09/08 17:44:18  mch
 Fix for URL not being a servlet

 Revision 1.6  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.5  2004/08/09 13:04:31  KevinBenson
 small change to get the servlet from the index

 Revision 1.4  2004/07/26 13:53:44  KevinBenson
 Changes to Fits to do an xquery on an xml file dealing with fits data.
 Small xsl style sheet to make the xql which will get the filename element

 Revision 1.3.30.1  2004/07/26 08:53:40  KevinBenson
 Still need to make a few more corrections, but wanted to check this in now.
 It is the fits querier that now uses exist for doing adql->xquery

 Revision 1.3  2004/03/15 19:16:12  mch
 Lots of fixes to status updates

 Revision 1.2  2004/03/12 20:04:57  mch
 It05 Refactor (Client)

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 Revision 1.14  2004/03/08 00:31:28  mch
 Split out webservice implementations for versioning

 Revision 1.13  2004/01/14 17:57:32  nw
 improved documentation

 Revision 1.12  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.11.6.2  2004/01/08 09:43:41  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.11.6.1  2004/01/07 11:51:07  nw
 found out how to get wsdl to generate nice java class names.
 Replaced _query with Query throughout sources.

 Revision 1.11  2003/12/09 12:31:23  mch
 New Fits Result set

 Revision 1.10  2003/12/03 19:37:03  mch
 Introduced DirectDelegate, fixed DummyQuerier

 Revision 1.9  2003/12/03 15:25:13  mch
 Fix to ignoring duplicates

 Revision 1.8  2003/12/02 17:58:05  mch
 Removed duplicate filenames

 Revision 1.7  2003/12/01 20:57:39  mch
 Abstracting coarse-grained plugin

 Revision 1.6  2003/11/28 19:57:15  mch
 Cone Search now works

 Revision 1.5  2003/11/28 19:16:53  mch
 Extended matching

 Revision 1.4  2003/11/28 18:22:18  mch
 IndexGenerator now working

 Revision 1.3  2003/11/28 16:10:30  nw
 finished plugin-rewrite.
 added tests to cover plugin system.
 cleaned up querier & queriermanager. tested

 Revision 1.2  2003/11/27 00:52:58  nw
 refactored to introduce plugin-back end and translator maps.
 interfaces in place. still broken code in places.

 Revision 1.1  2003/11/25 18:50:06  mch
 Abstracted Querier from DatabaseQuerier

 */




