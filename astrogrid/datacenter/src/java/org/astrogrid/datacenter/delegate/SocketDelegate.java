/*
 * $Id: SocketDelegate.java,v 1.3 2003/09/11 17:41:33 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import sun.security.krb5.internal.crypto.e;

/**
 * A socket AstroGrid datacenter delegate implementation.  Talks directly
 * to the server socket managed by SocketHandler on the server end.
 *
 * NB this is not properly threadsafe - eg request/response needs to be
 * atomic.  At the moment, a thread can call getMetadata() while another
 * is calling getQuery, and this will get mixed up responses.
 *
 * @see DatacenterDelegate
 *
 * @author M Hill
 * @author Jeff Lusted (from DatasetAgentDelegate)
 */

public class SocketDelegate extends DatacenterDelegate
{
   /** The socket connection - opened when the instance is constructed */
   private Socket socket = null;
   
   /** Output stream to the socket connection */
   private DataOutputStream out = null;

   /** Input stream from the socket connection */
   private DataInputStream in = null;

   /** String used to request registry metadata */
   public final static String REQ_REG_METADATA_TAG = "RequestRegistryMetata";
   
   /** String used to request metadata */
   public final static String REQ_METADATA_TAG = "RequestMetata";

   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public SocketDelegate(InetSocketAddress socketAddress) throws IOException
   {
      setSocket(new Socket(socketAddress.getAddress(), socketAddress.getPort()));
   }

   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public SocketDelegate(String endPoint) throws IOException
   {
      //parse string to extract address & port
      endPoint = endPoint.substring(9); //remove 'socket://'
      int colon = endPoint.indexOf(":");
      String address = endPoint.substring(0,colon);
      int port = Integer.parseInt(endPoint.substring(colon+1));
      setSocket(new Socket(address, port));
   }

   /**
    * Used by constructor
    */
   private void setSocket(Socket aSocket) throws IOException
   {
      Log.trace("Connecting to "+aSocket);

      socket = aSocket;
      in = new DataInputStream(aSocket.getInputStream());
      out = new DataOutputStream(aSocket.getOutputStream());
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
   public synchronized Element adqlQuery(Element adql) throws IOException
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

   public synchronized Element getResults(String id) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }

   public synchronized Element spawnAdqlQuery(Element adql) throws RemoteException
   {
      throw new UnsupportedOperationException("Not implemented yet");
   }


   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public synchronized int adqlCountDatacenter(Element adql)
   {
      throw new UnsupportedOperationException();
   }

   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema; I think that is what this should return...
    */
   public synchronized Element getRegistryMetadata() throws IOException
   {
      Log.trace("SocketDelegate: Writing Request Metadata tag");
      out.writeChars(
         "<?xml version='1.0' encoding='UTF-8'?>\n"+
         "<"+REQ_REG_METADATA_TAG+"/>\n"
      );
      
      try
      {
         Log.trace("SocketDelegate: Waiting for metadata...");
         XMLUtils.newDocument(in);
      }
      catch (ParserConfigurationException e)
      {
         throw new RuntimeException("Application not set up correctly",e);
      }
      catch (SAXException e)
      {
         throw new IOException("Response from server not XML: "+e);
      }
         
      
      throw new UnsupportedOperationException();
   }

   /**
    * Polls the service and asks for the current status
    */
   public synchronized ServiceStatus getServiceStatus(String id)
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
Revision 1.3  2003/09/11 17:41:33  mch
Fixes to socket-based client/server

Revision 1.2  2003/09/11 16:16:07  mch
Correction for 'socket' protocol, plus get registry metadata added

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


