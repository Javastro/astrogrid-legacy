/*
 * $Id: FitsQuerier.java,v 1.5 2003/11/28 19:16:53 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.fits;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.generated.Circle;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.spi.BaseQuerierSPI;
import org.astrogrid.datacenter.queriers.spi.QuerierSPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.awt.geom.Area;

/**
 *
 *
 * @author M Hill
 */

public class FitsQuerier extends BaseQuerierSPI implements QuerierSPI
{

   
   /**
    * locates all the fits files in this dataset that overlap the given
    * circular region
    */
   public URL[] findFitsInCone(double ra, double dec, double sr) throws IOException
   {
      String rawIndex = IndexGenerator.generateIndex(
         new URL[] {
               new URL("http://www.roe.ac.uk/~mch/r169411.fit"),
               new URL("http://www.roe.ac.uk/~mch/r169097.fit"),
               new URL("http://www.roe.ac.uk/~mch/r169101.fit")
         }
      );
      Document index = loadIndex(rawIndex);

      Ellipse2D cone = new Ellipse2D.Double(ra-sr, dec-sr, sr*2,sr*2);
      Area matchingArea = new Area(cone);
      
      Vector intersectingFits = new Vector();
      
      //first locate all the coverage nodes in the index
      NodeList coverages = index.getElementsByTagName("Coverage");

      for (int i=0;i<coverages.getLength();i++)
      {
         //extract coverage region
         Area coverage = new Area(getCoverage( (Element) coverages.item(i)));
         
         //does it intersect with circle?
         if (intersects(matchingArea, coverage))
         {
            //find parent tag describing whole file
            Element parent = (Element) coverages.item(i).getParentNode();
            Node fileNode = parent.getElementsByTagName("Filename").item(0);
            
            intersectingFits.add(fileNode.getNodeValue());
         }
      
      }
      
      return null;
   }

   public Polygon getCoverage(Element dom)
   {
      Polygon region = new Polygon();

      NodeList points = dom.getElementsByTagName("Point");
         
      for (int l=0;l<points.getLength();l++)
      {
         Node raNode = ((Element) points.item(l)).getElementsByTagName("RA").item(0);
         Node decNode = ((Element) points.item(l)).getElementsByTagName("Dec").item(0);
         
         region.addPoint(Integer.parseInt(raNode.getNodeValue()),
                         Integer.parseInt(decNode.getNodeValue()));
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
    * Loads the index from the given string.  Useful for testing...
    */
   public Document loadIndex(String indexXml) throws IOException
   {
      try
      {
         return XMLUtils.newDocument(indexXml);
      }
      catch (org.xml.sax.SAXException e) {}
      catch (javax.xml.parsers.ParserConfigurationException e) {}
      
      return null;
   }
   
   
   /** Querier implemenation - Updates the status and does the query (by calling the abstract
    * queryDatabase() overridden by subclasses) and returns the results.
    * Use by both synchronous (blocking) and asynchronous (threaded) querying
    */
   public QueryResults doQuery(Object o ,Class type) throws DatabaseAccessException {
      // TODO
      return null;
   }

/* (non-Javadoc)
 * @see org.astrogrid.datacenter.queriers.spi.QuerierSPI#getPluginInfo()
 */
public String getPluginInfo() {
    return "FITS Querier";
}

}

/*
$Log: FitsQuerier.java,v $
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

