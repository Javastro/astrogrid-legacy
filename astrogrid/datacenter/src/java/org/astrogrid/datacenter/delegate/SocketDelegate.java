/*
 * $Id: SocketDelegate.java,v 1.10 2003/09/15 22:05:34 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.io.SocketXmlInputStream;
import org.astrogrid.datacenter.io.SocketXmlOutputStream;
import org.astrogrid.datacenter.io.TraceInputStream;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * A socket AstroGrid datacenter delegate implementation.  Talks directly
 * to the server socket managed by SocketHandler on the server end.
 *
 * NB this is not properly threadsafe - eg request/response needs to be
 * atomic.  At the moment, a thread can call getMetadata() while another
 * is calling getQuery, and this will get mixed up responses.
 *
 * @todo make threadsafe
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
   private SocketXmlOutputStream out = null;

   /** Input stream from the socket connection */
   private SocketXmlInputStream in = null;

   /** String used to request registry metadata */
   public static final String REQ_REG_METADATA_TAG = "RequestRegistryMetata";

   /** String used to request metadata */
   public static final String REQ_METADATA_TAG = "RequestMetata";

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
      String host = endPoint.substring(9); //remove 'socket://'
      int colon = host.indexOf(":");
      if (colon == -1) { throw new MalformedURLException("Socket 'url' invalid: '"+endPoint+"', no server port given"); }
      String address = host.substring(0,colon);
      int port = Integer.parseInt(host.substring(colon+1));
      setSocket(new Socket(address, port));
   }

   /**
    * Used by constructor
    */
   private void setSocket(Socket aSocket) throws IOException
   {
      Log.trace("Connecting to "+aSocket);

      socket = aSocket;
      out = new SocketXmlOutputStream(socket.getOutputStream());

      TraceInputStream tin = new TraceInputStream(socket.getInputStream());
//      tin.setState(true);
      tin.copy2File(new File("incomingMsgs.log"));
      in = new SocketXmlInputStream(tin);
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
      Log.trace("SocketDelegate: Writing Query");
      //send query document
      out.writeAsDoc(adql);

      try
      {
         //blocking read for response
         Log.trace("SocketDelegate: Waiting for query response...");
         return in.readDoc().getDocumentElement();
      }
      catch (org.xml.sax.SAXException e)
      {
         //somethings gone wrong at the server end - returning invalid XML
         IOException ioe = new IOException("Invalid XML returned by server");
         ioe.initCause(e);
         throw ioe;
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

      out.writeAsDoc("<"+REQ_REG_METADATA_TAG+"/>\n");

      try
      {
         Log.trace("SocketDelegate: Waiting for metadata...");
         Document response = in.readDoc();
         Log.trace("Response="+response.getDocumentElement().getNodeName());

         if (response.getElementsByTagName(ResponseHelper.ERROR_TAG).getLength() >0)
         {
            //response was an error
            Log.logError("Server error getting metadata: "+
                        response.getElementsByTagName(ResponseHelper.ERROR_TAG).item(0).getNodeValue()
                        );
            return null;
         }

         return response.getDocumentElement();
      }
      catch (ParserConfigurationException e)
      {
         throw new RuntimeException("Application not set up correctly",e);
      }
      catch (SAXException e)
      {
         throw new IOException("Response from server not XML: "+e);
      }
   }

   /**
    * Polls the service and asks for the current status
    */
   public synchronized QueryStatus getServiceStatus(String id)
   {
      return QueryStatus.UNKNOWN;
   }


   /**
    * Register listener
    */
   public void registerListener(String queryId, DatacenterStatusListener listener)
   {
      //action depends on listener
      throw new UnsupportedOperationException("Not implemented yet");
   }


}

/*
$Log: SocketDelegate.java,v $
Revision 1.10  2003/09/15 22:05:34  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.9  2003/09/15 21:27:15  mch
Listener/state refactoring.

Revision 1.8  2003/09/15 16:59:53  mch
Added better test coverage and error recovery

Revision 1.7  2003/09/15 16:06:11  mch
Fixes to make maven happ(ier)

Revision 1.6  2003/09/15 13:17:36  mch
Changed RemoteException thrown to more appropriate IOException

Revision 1.5  2003/09/14 22:47:25  mch
Various fixes :-)

Revision 1.4  2003/09/14 22:07:55  mch
Added Socket stream

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



