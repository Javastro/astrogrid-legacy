/*
 * $Id: NvoConeSearchDelegate.java,v 1.3 2003/11/17 16:59:12 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.nvocone;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.xml.sax.SAXException;

/**
 * The National Virtual Observatory, an American effort, defined a simple
 * cone search service:
 * @see http://www.us-vo.org/metadata/conesearch/
 * <p>
 * This delegate implements this as a ConeSearch, and also accepts
 * very simple Adql-queries that can be adapted if possible.
 *
 * @author M Hill
 */

public class NvoConeSearchDelegate implements ConeSearcher
{
   
   String serverUrl = null;
   
   public NvoConeSearchDelegate(String baseUrl)
   {
      this.serverUrl = baseUrl;
   }
   
   /**
    * Simple cone-search http call.
    * @param ra Right Ascension in decimal degrees, J2000
    * @param dec Decliniation in decimal degress, J2000
    * @param sr search radius in decimal degrees
    * @return input stream to results, which will be a VOTable document
    */
   public InputStream coneSearch(double ra, double dec, double sr) throws IOException
   {
      String queryUrl = serverUrl;
      
      //add query stuff - there might already be query stuff in the base url
      if (serverUrl.indexOf("?")>-1) {
         queryUrl = queryUrl + "&";
      }
      else {
         queryUrl = queryUrl + "?";
      }
      
      queryUrl = queryUrl+"RA="+ra+"&DEC="+dec+"&SR="+sr;

      try
      {
         
         URL url = new URL(queryUrl);
         
         //run query
         return url.openStream();
      }
      catch (MalformedURLException mue)
      {
         throw new DatacenterException("server URL invalid: '"+queryUrl+"'",mue);
      }
      catch (IOException e)
      {
         throw new DatacenterException("Error connecting to service",e);
      }
   }
   
   
   
}

/*
$Log: NvoConeSearchDelegate.java,v $
Revision 1.3  2003/11/17 16:59:12  mch
ConeSearcher.coneSearch now returns stream not parsed element, throws IOException

Revision 1.2  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates



*/


