/*
 * $Id: FitsQuerier.java,v 1.4 2003/11/28 18:22:18 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.fits;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.spi.BaseQuerierSPI;
import org.astrogrid.datacenter.queriers.spi.QuerierSPI;
import org.w3c.dom.Document;

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
//    Document index = loadIndex(asdfasdf);
      //first locate all the coverage nodes in the index
      
      
      Rectangle r = new Rectangle();
      
      return null;
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

