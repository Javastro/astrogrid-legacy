/*
 * $Id: FitsQuerierPlugin.java,v 1.1 2004/03/12 04:45:26 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.fits;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.QuerierPluginException;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.query.ConeQuery;
import org.astrogrid.datacenter.queriers.query.ConeQueryMaker;
import org.astrogrid.datacenter.queriers.status.QuerierAborted;
import org.astrogrid.datacenter.snippet.DocHelper;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 *
 * @author M Hill
 */

public class FitsQuerierPlugin extends QuerierPlugin
{
   private static Document index = null;

   private static boolean indexLoaded = false;
   
   public final static String FITS_INDEX_URL = "datacenter.fits.index.url";
   public final static String FITS_INDEX_FILENAME = "datacenter.fits.index.filename";
   
   public FitsQuerierPlugin(Querier querier) throws IOException
   {
      super(querier);
   }

   /** Plugin implementation - carries out query.
    * Should get an XPath from the query and apply that; but at the moment just
    * does a cone search.
    */
   public void askQuery() throws IOException
   {
      if (!indexLoaded) {
         loadIndex();
      }
      
         String[] filenames = coneSearch(new ConeQueryMaker().getConeQuery(querier.getQuery()));

         if (!aborted) {
            querier.setStatus(new QuerierAborted());
         }
         else {
            processResults(new FitsResults(filenames));
         }
   }

   public String[] coneSearch(ConeQuery query) throws IOException {
      return coneSearch(query.getRa(), query.getDec(), query.getRadius());
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
            
            String filename = DocHelper.getTagValue(parent, "Filename");
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
         float ra = Float.parseFloat(DocHelper.getTagValue((Element) points.item(l), "RA"));
         float dec = Float.parseFloat(DocHelper.getTagValue((Element) points.item(l), "Dec"));

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
      if (indexLoaded) return;
      
      URL url = SimpleConfig.getSingleton().getUrl(FITS_INDEX_URL, null);
      if (url != null) {
         try {
            index = DomHelper.newDocument(url.openStream());
         }
         catch (ParserConfigurationException e) {
            throw new RuntimeException("Server configuration error",e);
         }
         catch (SAXException e) {
            throw new QuerierPluginException("FitsQuerierPlugin index not valid xml",e);

         }
      }
   }
   
   
 
}

/*
 $Log: FitsQuerierPlugin.java,v $
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




