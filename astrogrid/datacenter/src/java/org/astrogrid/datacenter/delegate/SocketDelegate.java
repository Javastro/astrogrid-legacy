/*
 * $Id: SocketDelegate.java,v 1.14 2003/09/16 17:36:13 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Hashtable;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.common.DocHelper;
import org.astrogrid.datacenter.common.QueryIdHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.common.StatusHelper;
import org.astrogrid.datacenter.io.SocketXmlInputStream;
import org.astrogrid.datacenter.io.SocketXmlOutputStream;
import org.astrogrid.datacenter.io.TraceInputStream;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
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

   /** listeners to query status changes, keyed by queryId */
   private Hashtable listeners = new Hashtable();

   /** String used to request registry metadata */
   public static final String REQ_REGISTRY_METADATA_TAG = "RequestRegistryMetata";
   /** String used to request metadata */
   public static final String REQ_METADATA_TAG = "RequestMetata";

   /** Synchronous/blocking query */
   public static final String DO_QUERY_TAG = "SynchronousQuery";

   /** String used to create a query */
   public static final String CREATE_QUERY_TAG = "CreateQuery";
   /** String used to start a query */
   public static final String START_QUERY_TAG = "StartQuery";

   public static final String REGISTER_LISTENER_TAG = "RegisterListener";
   public static final String ABORT_QUERY_TAG = "AbortQuery";
   public static final String REQ_RESULTS_TAG = "RequestResults";
   public static final String REQ_STATUS_TAG = "RequestStatus";

   /** Tag used when sending asynchronous status notifications */
   public static final String NOTIFY_STATUS_TAG = "NotifyStatus";

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
   public Element query(Element adql) throws IOException
   {
      //need to construct a wrapper around the given element, bit messy this
      String query = XMLUtils.ElementToString(adql);
      String reqTag = "<"+DO_QUERY_TAG+">\n"
                     +getUserTag()+"\n"
                     +query
                     +"\n</"+DO_QUERY_TAG+">";

      return atomicSendReceive(reqTag);
   }

   /** Constructs a query at the server with the given adql
    */
   public Element makeQuery(Element adql) throws RemoteException, IOException
   {
      //need to construct a wrapper around the given element, bit messy this
      String query = XMLUtils.ElementToString(adql);
      String reqTag = "<"+CREATE_QUERY_TAG+">\n"
                     +getUserTag()+"\n"
                     +query
                     +"\n</"+CREATE_QUERY_TAG+">";

      return atomicSendReceive(reqTag);
   }

   /** Constructs a query at the server with the given adql
    */
   public Element startQuery(String queryId) throws RemoteException, IOException
   {
      Log.affirm(queryId != null, "queryId is null");
      Log.affirm(queryId.length()>0, "queryId is empty");

      String reqTag = "<"+START_QUERY_TAG+">\n"
                     +getUserTag()+"\n"
                     +QueryIdHelper.makeQueryIdTag(queryId)+"\n"
                     +"\n</"+START_QUERY_TAG+">";

      return atomicSendReceive(reqTag);
   }

   /** Get the results of a query (or the status so far)
    */
   public Element getResults(String queryId) throws RemoteException, IOException
   {
      Log.affirm(queryId != null, "queryId is null");
      Log.affirm(queryId.length()>0, "queryId is empty");

      String reqTag = "<"+REQ_RESULTS_TAG+">\n"
                     +getUserTag()+"\n"
                     +QueryIdHelper.makeQueryIdTag(queryId)+"\n"
                     +"\n</"+REQ_RESULTS_TAG+">";

      return atomicSendReceive(reqTag);
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
      Element response = atomicSendReceive("<"+REQ_REGISTRY_METADATA_TAG+"/>\n");

      return response;
   }

   /**
    * Polls the service and asks for the current status
    */
   public QueryStatus getQueryStatus(String queryId) throws IOException
   {
      Log.affirm(queryId != null, "queryId is null");
      Log.affirm(queryId.length()>0, "queryId is empty");

      String reqTag = "<"+REQ_STATUS_TAG+">\n"
                     +getUserTag()+"\n"
                     +QueryIdHelper.makeQueryIdTag(queryId)+"\n"
                     +"\n</"+REQ_STATUS_TAG+">";

      Element response = atomicSendReceive(reqTag);

      return StatusHelper.getServiceStatus(response);
   }

   /**
    * Register listener. The delegate is already a listener for all status
    * updates; the given listener is therefore registered locally to be
    * informed as necessary
    */
   public void registerListener(String queryId, DatacenterStatusListener listener)
   {
      listeners.put(queryId, listener);
   }

   /**
    * An asynchronous message was received from the server.  Currently only
    * status notifications arrive this way - so call status listeners
    */
   private void notifyReceived(Element response)
   {
      NodeList statusNodes = response.getElementsByTagName(StatusHelper.STATUS_TAG);

      for (int i=0;i<statusNodes.getLength();i++)
      {
         Element status = ((Element) statusNodes.item(i));

         String queryId = status.getAttribute(QueryIdHelper.QUERY_ID_ATT);

         DatacenterStatusListener listener = (DatacenterStatusListener) listeners.get(queryId);
         if (listener != null)
         {
            listener.datacenterStatusChanged(queryId, StatusHelper.getServiceStatus(status));
         }
      }
   }

   /**
    * Takes the given xml snippet string, converts into a DOM and runs the
    * atomic SendReceive method
    *
    * @see atomicSendReceive(Element)
    */
   private Element atomicSendReceive(String xml) throws IOException
   {
      return atomicSendReceive(DocHelper.wrap(xml).getDocumentElement());
   }

   /**
    * Threadsafe 'atomic' operation that sends given Document and returns the
    * received Document.  As long as all operations by the delegate run through
    * this method, then all responses will match all requests (there is no
    * danger of a second request being sent before the first response has been
    * returned).  However the delegate
    * will block, so synchronous queries will prevent any other operations while
    * it runs
    */
   private synchronized Element atomicSendReceive(Element request) throws IOException
   {
      //send request
      Log.trace("SocketDelegate: Sending request ["+request.getNodeName()+"]...");
      out.writeAsDoc(request);

      //wait for response
      try
      {
         Log.trace("SocketDelegate: ...waiting for response...");

         //now just to make things difficult, notification messages may arrive
         //at any time, so we need to be able to trap those
         boolean responseToRequestReceived = false;//have we received the response to the above request?
         Document response = null;

         while (!responseToRequestReceived)
         {
            response = in.readDoc();
            Log.trace("SocketDelegate: ...response received ["+response.getDocumentElement().getNodeName()+"]");

            if (response.getElementsByTagName(NOTIFY_STATUS_TAG).getLength() >0)
            {
               //notification
               notifyReceived(response.getDocumentElement());
            }
            else
            {
               responseToRequestReceived = true;
            }
         }

         //check for error
         if (response.getElementsByTagName(ResponseHelper.ERROR_TAG).getLength() >0)
         {
            //response was an error - not sure what to do with this - log it and return it for now
            Log.logError("Server error: "+
                        response.getElementsByTagName(ResponseHelper.ERROR_TAG).item(0).getNodeValue()
                        );
         }

         return response.getDocumentElement();
      }
      catch (SAXException se)
      {
         throw new IOException("Server returned invalid XML: "+se);
      }
   }

}

/*
$Log: SocketDelegate.java,v $
Revision 1.14  2003/09/16 17:36:13  mch
Added checks for null query ids

Revision 1.13  2003/09/16 16:56:07  mch
Fix for no listeners

Revision 1.12  2003/09/16 15:23:16  mch
Listener fixes and rationalisation

Revision 1.11  2003/09/15 22:38:42  mch
Split spawnQuery into make and start, so we can add listeners in between

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




