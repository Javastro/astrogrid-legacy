/*
 * $Id: SocketDelegate.java,v 1.1 2003/09/09 17:55:49 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Element;

import org.astrogrid.datacenter.common.ServiceStatus;
import org.astrogrid.log.Log;

/**
 * A standard AstroGrid datacenter delegate implementation.
 *
 * @see DatacenterDelegate
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class SocketDelegate extends DatacenterDelegate
{
   private Socket socket = null;

   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public SocketDelegate(InetSocketAddress socketAddress) throws IOException
   {
      socket = new Socket(socketAddress.getAddress(), socketAddress.getPort());
   }

   /**
    * Sets the timeout for calling the service - ie how long after the initial call
    * is made before a timeout exception is thrown
    */
   public void setTimeout(int givenTimeout)
   {
      try
      {
         socket.setSoTimeout(givenTimeout);
      }
      catch (java.net.SocketException e)
      {
         Log.logError("Could not set timeout to "+givenTimeout, e);
      }

   }


  /**
    * General purpose query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * results part of the returned document, which may be VOTable or otherwise
    * depending on the results format specified in the ADQL
    */
   public Element adqlQuery(Element adql) throws IOException
   {
      //send query document
      socket.getOutputStream().write(adql.toString().getBytes());

      try
      {
         //blocking read for response
         return XMLUtils.newDocument(socket.getInputStream()).getDocumentElement();
      }
      catch (org.xml.sax.SAXException e)
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

   public Element getResults(String id) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public Element spawnAdqlQuery(Element adql) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
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
   public Element getRegistryMetadata() throws IOException
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Polls the service and asks for the current status
    */
   public ServiceStatus getServiceStatus(String id)
   {
      return ServiceStatus.UNKNOWN;
   }

   /**
    * Register web listener
    */
   public void registerWebListener(java.net.URL listenerUrl)
   {
      //need to send url to service
      throw new UnsupportedOperationException("Not implemented yet");
   }


}

/*
$Log: SocketDelegate.java,v $
Revision 1.1  2003/09/09 17:55:49  mch
New Delegate for socket datacenter servers

Revision 1.4  2003/09/08 16:34:04  mch
Added documentation

Revision 1.3  2003/09/07 18:51:12  mch
Added typesafe ServiceStatus

Revision 1.2  2003/08/31 15:23:08  mch
Removed unused listeners

Revision 1.1  2003/08/27 23:30:10  mch
Introduced DummyDatacenterDelegate, selfcontained package for other workgroups to test with

Revision 1.3  2003/08/27 22:40:55  mch
removed unnecessary import (maven report...!)

Revision 1.2  2003/08/25 22:52:11  mch
Combined code from DatasetAgentDelegate with DatacenterDelegate

Revision 1.1  2003/08/25 15:19:28  mch
initial checkin


*/

