/*
 * $Id: FitsQuerierPlugin.java,v 1.3 2005/03/21 18:45:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.fitsserver.fits;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.cfg.ConfigException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.DefaultPlugin;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.UrlListResults;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.query.Query;
import org.astrogrid.query.xql.XqlMaker;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.astrogrid.xmldb.client.QueryService;
import org.astrogrid.xmldb.client.XMLDBFactory;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

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
   public final static String FITS_DEFAULT_COLLECTION = "dcfitsfiles";
   
   private XMLDBFactory xdb = new XMLDBFactory();
   

   /** Plugin implementation - carries out query.
    */
   public void askQuery(Principal user, Query query, Querier querier) throws IOException {

      String[] filenames = null;

      XqlMaker maker = new XqlMaker();
      String xql = maker.getXql(query);
      
      querier.setStatus(new QuerierQuerying(querier.getStatus(), xql));

      filenames = useeXist(xql);
      
      if ((!aborted) && (filenames != null)) {
         querier.getStatus().addDetail(filenames+" files matched");
         UrlListResults results = new UrlListResults(querier, filenames);
         results.send(query.getResultsDef(), querier.getUser());
      }
   }

   /** Plugin implementation - returns number of matches
    */
   public long getCount(Principal user, Query query, Querier querier) throws IOException {

      String[] filenames = null;

      XqlMaker maker = new XqlMaker();
      String xql = maker.getXql(query);
      
      querier.setStatus(new QuerierQuerying(querier.getStatus(), xql));

      filenames = useeXist(xql);

      return filenames.length;
   }
   
   private String[] useeXist(String xql) throws IOException {
      Document resultDoc = null;
      String []files = new String[0];
      Collection coll = null;
      try {
          coll = xdb.openCollection(FITS_DEFAULT_COLLECTION);
          //log.info("Got Collection");
          QueryService xqs = xdb.getQueryService(coll);
          
          long beginQ = System.currentTimeMillis();
          ResourceSet rs = xqs.query(xql);
          //log.info("Total Query Time = " + (System.currentTimeMillis() - beginQ));
          //log.info("Number of results found in query = " + rs.getSize());
          if(rs.getSize() > 0) {
              Resource xmlr = rs.getMembersAsResource();
              resultDoc = DomHelper.newDocument(xmlr.getContent().toString());
          }//if
      }catch(XMLDBException xdbe) {
          xdbe.printStackTrace();
          throw new IOException("XMLDB exception, query most likely wrong or xml db is down XMLDB Exception");
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
       /*
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
      */
      throw new IOException("ConeSearches currently not supported by FitsQuerierPlugin.");
   }
   
   /**
    * Looks through the coverage in the index and returns a suitable 'shape' to
    * represent that region.  At the moment it just returns a 'general path' with
    * the given points in it
    */
   public GeneralPath getCoverageFromIndex(Element dom)
   {
       return null;
      /*
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
      */
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
      return;
   }
   
   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return UrlListResults.listFormats();
   }
 
}

/*
 $Log: FitsQuerierPlugin.java,v $
 Revision 1.3  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.2  2005/03/11 14:50:59  KevinBenson
 added catch for parserconfigurationexception

 Revision 1.1  2005/03/10 16:42:55  mch
 Split fits, sql and xdb

 Revision 1.3  2005/03/10 13:59:00  KevinBenson
 corrections to fits and testing to fits

 Revision 1.2  2005/03/08 15:03:24  KevinBenson
 new stuff for Fits querier to work with an internal xml database

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.7.2.3  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.7.2.2  2004/11/24 20:59:37  mch
 doc fixes and added slinger browser

 Revision 1.7.2.1  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.7  2004/11/12 13:49:12  mch
 Fix where keyword maker might not have had keywords made

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




