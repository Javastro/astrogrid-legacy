/*
 * $Id: StdDatacenterDelegate.java,v 1.1 2003/08/27 23:30:10 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.xml.rpc.ServiceException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.delegate.datasetAgent.DatasetAgentServiceLocator;
import org.astrogrid.datacenter.delegate.datasetAgent.DatasetAgentSoapBindingStub;
import org.w3c.dom.Element;

/**
 * A convenience class for java clients of datacenters.  They can create and
 * use this class to manage all the connections/calls/etc without having to
 * mess around with all the SOAP messages directly.
 *
 * An instance of one of these corresponds to one connection to one database
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class StdDatacenterDelegate extends DatacenterDelegate
{
   private Vector statusListeners = new Vector();

   private URL endpoint = null;
   DatasetAgentSoapBindingStub binding;

   /** Use the factory method DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public StdDatacenterDelegate(String givenEndPoint) throws MalformedURLException, ServiceException
   {
      this.endpoint = new URL(givenEndPoint);

      binding = (DatasetAgentSoapBindingStub)
            new DatasetAgentServiceLocator().getDatasetAgent( endpoint );
   }

   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown
    */
   public void setTimeout(int givenTimeout)
   {
      binding.setTimeout(givenTimeout);
   }


  /**
    * General purpose query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * results part of the returned document, which may be VOTable or otherwise
    * depending on the results format specified in the ADQL
    */
   public Element adqlQueryDatacenter(Element adql) throws RemoteException
   {
      String results = binding.runQuery(adql.toString());

      try
      {
         return XMLUtils.newDocument(results).getDocumentElement();
      }
      catch (org.xml.sax.SAXException e)
      {
         //somethings gone wrong at the server end - returning invalid XML
         throw new RemoteException("Invalid XML returned by server",e);
      }
      catch (java.io.IOException e)
      {
         //somethings gone wrong at the server end - returning invalid XML
         throw new RemoteException("Invalid XML returned by server",e);
      }
      catch (javax.xml.parsers.ParserConfigurationException e)
      {
         //somethings wrong here with the parser configuration, which is not normal
         throw new RuntimeException("Could not interpret returned XML, parser not configured?",e);
      }
   }

   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public int adqlCountDatacenter(Element adql)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema; I think that is what this should return...
    */
   public Element getRegistryMetadata()
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Polls the service and asks for the current status
    */
   public String getStatus()
   {
      return DatacenterStatusListener.UNKNOWN;
   }


}

/*
$Log: StdDatacenterDelegate.java,v $
Revision 1.1  2003/08/27 23:30:10  mch
Introduced DummyDatacenterDelegate, selfcontained package for other workgroups to test with

Revision 1.3  2003/08/27 22:40:55  mch
removed unnecessary import (maven report...!)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:28  mch
initial checkin


*/

