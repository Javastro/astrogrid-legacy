/*
 * $Id: NvoConeSearchDelegate.java,v 1.1 2003/10/06 18:55:21 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.nvocone;
import org.astrogrid.datacenter.delegate.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.QOM;
import org.astrogrid.datacenter.common.QueryStatus;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
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
    * @param sr search radius.
    * @return VOTable
    * @todo return VOTable instance not Element
    */
   public Element coneSearch(double ra, double dec, double sr) throws DatacenterException
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
            return XMLUtils.newDocument(url.openStream()).getDocumentElement();
      }
      catch (MalformedURLException mue)
      {
         throw new DatacenterException("server URL invalid: '"+queryUrl+"'",mue);
      }
      catch (ParserConfigurationException e)
      {
         throw new DatacenterException("Invalid setup",e);
      }
      catch (SAXException e)
      {
         throw new DatacenterException("Results are invalid XML",e);
      }
      catch (IOException e)
      {
         throw new DatacenterException("Error connecting to service",e);
      }
   }
   
   
   
}

/*
$Log: NvoConeSearchDelegate.java,v $
Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates



*/


