/*
 * $Id: NvoConeSearcher.java,v 1.1.1.1 2009/05/13 13:20:23 gtr Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.dataservice.api.nvocone;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.astrogrid.dataservice.DatacenterException;
import org.astrogrid.query.QueryState;

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

public class NvoConeSearcher
{
   protected String serverUrl = null;

   private int timeout = 0;
   
   public NvoConeSearcher(String baseUrl)
   {
      this.serverUrl = baseUrl;
   }
   
   /** Sets the timeout in seconds.  Set before doing search */
   public void setTimeout(int newTimeout) {
      this.timeout = newTimeout;
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
   
   
   public String getMetadata() {
      return "Not done yet";
   }
   
   public String getStatus(String id) {
      return ""+QueryState.UNKNOWN;
   }
   
}

/*
$Log: NvoConeSearcher.java,v $
Revision 1.1.1.1  2009/05/13 13:20:23  gtr


Revision 1.2  2005/02/28 19:36:39  mch
Fixes to tests


*/


