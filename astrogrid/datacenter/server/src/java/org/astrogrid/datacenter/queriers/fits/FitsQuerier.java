/*
 * $Id: FitsQuerier.java,v 1.14 2004/03/08 00:31:28 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.fits;

import java.io.*;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.net.URL;
import java.util.Hashtable;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Circle;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.snippet.DocHelper;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 *
 * @author M Hill
 */

public class FitsQuerier extends Querier
{
   Document index = null;

   public FitsQuerier(String id, Query query) throws IOException
   {
      super(id, query);
   }

   public QueryResults doQuery() throws DatabaseAccessException
   {
      try
      {
         Select adql = ADQLUtils.unmarshalSelect(getQueryingElement());
         
         Circle adqlRegion = adql.getTableClause().getWhereClause().getCircle();
         
         String[] filenames = coneSearch(adqlRegion.getRa().getValue(),
                                         adqlRegion.getDec().getValue(),
                                         adqlRegion.getRadius().getValue());

         //@todo now what?
         return new FitsResults(filenames);
      }
      catch (ADQLException e)
      {
         throw new DatabaseAccessException(e, "Error parsing adql");
      }
      catch (IOException e)
      {
         throw new DatabaseAccessException(e, "Error doing search");
      }
      
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
    * Loads the index from the given string.  Useful for testing...
    */
   public void setIndex(String indexXml) throws IOException
   {
      setIndex(new ByteArrayInputStream(indexXml.getBytes()));
   }

   /**
    * Loads the index from the given stream.
    */
   public void setIndex(InputStream indexStream) throws IOException
   {
      try
      {
         index = DomHelper.newDocument(indexStream);
      }
      catch (org.xml.sax.SAXException e) {  throw new IOException(e.toString()); }
      catch (javax.xml.parsers.ParserConfigurationException e) {  throw new IOException(e.toString()); }
      
   }
   
   /**
    * Set the index from the given string
    */
   public void setIndex(Document givenIndexDom)
   {
       index = givenIndexDom;
   }
   
   /** Querier implemenation - Updates the status and does the query (by calling the abstract
    * queryDatabase() overridden by subclasses) and returns the results.
    * Use by both synchronous (blocking) and asynchronous (threaded) querying
    */
   public QueryResults doQuery(Object o ,Class type) throws DatabaseAccessException {
      // TODO
      return null;
   }
   
 
   /**
    * Test harness
    */
   public static void main(String args[]) throws IOException
   {
      org.astrogrid.log.Log.logToConsole();
      
      File indexFile = new File("fitsIndex.xml");
      if (!indexFile.exists())
      {
         org.astrogrid.log.Log.trace("Generating index...");
         //create index file
         String rawIndex = IndexGenerator.generateIndex(
            new URL[] {
               new URL("http://www.roe.ac.uk/~mch/r169411.fit"),
                  new URL("http://www.roe.ac.uk/~mch/r169097.fit"),
                  new URL("http://www.roe.ac.uk/~mch/r169101.fit")
            }
         );
         org.astrogrid.log.Log.trace(rawIndex);
         FileOutputStream out = new FileOutputStream(indexFile);
         out.write(rawIndex.getBytes());
      }
      
      FitsQuerier querier = new FitsQuerier("test",null);
      querier.setIndex(new FileInputStream(indexFile));

      org.astrogrid.log.Log.trace("Starting cone search...");
      String[] foundUrls = querier.coneSearch(308,60,12);
      
      org.astrogrid.log.Log.trace("Found "+foundUrls.length+":");
      
      for (int i=0;i<foundUrls.length;i++)
      {
         org.astrogrid.log.Log.trace(foundUrls[i].toString());
      }
      
   }

}

/*
 $Log: FitsQuerier.java,v $
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




