/*
 * $Id: SocketDelegate.java,v 1.6 2003/12/02 19:49:44 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate.agss;
import java.io.IOException;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.delegate.Certification;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.delegate.DelegateQueryListener;
import org.astrogrid.datacenter.delegate.Metadata;
import org.astrogrid.datacenter.snippet.io.XmlDocInputStream;
import org.astrogrid.datacenter.snippet.io.XmlDocOutputStream;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.snippet.DocHelper;
import org.astrogrid.datacenter.snippet.DocMessageHelper;
import org.astrogrid.datacenter.snippet.QueryIdHelper;
import org.astrogrid.datacenter.snippet.StatusHelper;
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

public class SocketDelegate implements AdqlQuerier
{
   /** The socket connection - opened when the instance is constructed */
   private Socket socket = null;

   /** Output stream to the socket connection */
   private XmlDocOutputStream out = null;
   /** Input stream from the socket connection */
   private XmlDocInputStream in = null;

   /** listeners to query status changes, keyed by queryId */
   //private Hashtable listeners = new Hashtable();

  
   /** User certification */
   private Certification certification = null;
   
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
   public SocketDelegate(InetSocketAddress socketAddress, Certification givenCertification) throws IOException
   {
      this.certification = givenCertification;
      setSocket(new Socket(socketAddress.getAddress(), socketAddress.getPort()));
   }

   /** Don't use this directly - use the factory method
    * DatacenterDelegate.makeDelegate() in case we need to create new sorts
    * of datacenter delegates in the future...
    */
   public SocketDelegate(String endPoint, Certification givenCertification) throws IOException
   {
      this.certification = givenCertification;

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
      out = new XmlDocOutputStream(socket.getOutputStream());

//      TraceInputStream tin = new TraceInputStream(socket.getInputStream());
//      tin.setState(true);
//      tin.copy2File(new File("incomingMsgs.log"));
//      in = new SocketXmlInputStream(tin);
      in = new XmlDocInputStream(socket.getInputStream());
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
    * Class used to manage the query at the server
    */
   private class SocketQueryDelegate implements DatacenterQuery
   {
      private String queryId = null;
      
      public SocketQueryDelegate(String id)
      {
         this.queryId = id;
      }

      public String getId()
      {
         return queryId;
      }
      
      
      public DatacenterResults getResultsAndClose() throws IOException
      {
         String reqTag = "<"+REQ_RESULTS_TAG+">\n"
//                     +getUserTag()+"\n"
                        +QueryIdHelper.makeQueryIdTag(queryId)+"\n"
                        +"\n</"+REQ_RESULTS_TAG+">";

         Element response = atomicSendReceive(reqTag);
         
         NodeList urls = response.getElementsByTagName(DocMessageHelper.RESULTS_TAG);
         
         return new DatacenterResults(new String[] { urls.item(0).getNodeValue() });
      }
      
      
      public void setResultsDestination(URL myspace) throws IOException
      {
         throw new UnsupportedOperationException();
      }
      
      public void start() throws IOException
      {
         String reqTag = "<"+START_QUERY_TAG+">\n"
                     //+getUserTag()+"\n"
                     +QueryIdHelper.makeQueryIdTag(queryId)+"\n"
                     +"\n</"+START_QUERY_TAG+">";

         atomicSendReceive(reqTag);
      }
      
      public QueryStatus getStatus() throws IOException
      {
         String reqTag = "<"+REQ_STATUS_TAG+">\n"
      //               +getUserTag()+"\n"
                     +QueryIdHelper.makeQueryIdTag(queryId)+"\n"
                     +"\n</"+REQ_STATUS_TAG+">";

         Element response = atomicSendReceive(reqTag);

         return StatusHelper.getServiceStatus(response);
      }
      
      public void abort() throws DatacenterException
      {
         
      }
      
      public void registerListener(DelegateQueryListener newListener)
      {
         throw new UnsupportedOperationException();
      }
      public void registerWebListener(URL url)
      {
         throw new UnsupportedOperationException();
      }
      public void registerJobMonitor(URL url)
      {
         throw new UnsupportedOperationException();
      }
      
      
   }
   

   /**
    * Register listener. The delegate is already a listener for all status
    * updates; the given listener is therefore registered locally to be
    * informed as necessary
    *
   public void registerListener(String queryId, DatacenterStatusListener listener)
   {
      listeners.put(queryId, listener);
   }

   /**
    * Returns the number of items that match the given query.  This is useful for
    * doing checks on how big the result set is likely to be before it has to be
    * transferred about the net.
    */
   public int countQuery(Select adql) throws DatacenterException
   {
      throw new UnsupportedOperationException();
   }

   /**
    * Support method for creating the right xml from the adql object model. Bit
    * of a pain this SOAPy beans in some way - means we keep converting from &
    * to XML, but at least things are being type-checked...
    */
   private String getAdqlXml(Select adql)
   {
      StringWriter writer = new StringWriter();
      try {
         adql.marshal(writer);
         return writer.toString();
      }
      catch (org.exolab.castor.xml.CastorException e)
      {
         throw new RuntimeException("Could not write adql to string");
      }
   }
   
   
   /**
    * Simple blocking query.
    * @param user user information for authentication/authorisation
    * @param resultsformat string specifying how the results will be returned (eg
    * votable, fits, etc) strings as given in the datacenter's metadata
    * @param ADQL
    * @todo move adql package into common
    */
   public DatacenterResults doQuery(String resultsFormat, Select adql) throws IOException
   {
      String reqTag = "<"+DO_QUERY_TAG+">\n"
                     //+getUserTag()+"\n"
                     +getAdqlXml(adql)
                     +"\n</"+DO_QUERY_TAG+">";

      return new DatacenterResults(atomicSendReceive(reqTag));
   }
   
   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param adql object model
    * @param givenId an id for the query is assigned here rather than
    * generated by the server
    */
   public DatacenterQuery makeQuery(Select adql, String givenId) throws IOException
   {
      String reqTag = "<"+CREATE_QUERY_TAG+">\n";
      if (givenId != null) {
         reqTag = reqTag +"<"+DocMessageHelper.ASSIGN_QUERY_ID_TAG+">"+givenId+"</"+DocMessageHelper.ASSIGN_QUERY_ID_TAG+">";
      }

      reqTag = reqTag //+getUserTag()+"\n"
                     +getAdqlXml(adql)
                     +"\n</"+CREATE_QUERY_TAG+">";

      Element response = atomicSendReceive(reqTag);
      
      return new SocketQueryDelegate(QueryIdHelper.getQueryId(response));
   }

   /**
    * Constructs the query at the
    * server end, but does not start it yet as other Things May Need To Be Done
    * such as registering listeners or setting the destination for the results.
    *
    * @param adql object model
    */
   public DatacenterQuery makeQuery(Select adql) throws IOException
   {
      return makeQuery(adql, null);
   }
   
   /**
   * returns the full datacenter metadata.  Implementations might like to
   * cache it locally (but remember threadsafety)...
   */
   public Metadata getMetadata() throws IOException
   {
      String reqTag = "<"+REQ_METADATA_TAG+"/>\n";

      return new Metadata(atomicSendReceive(reqTag));
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

         throw new UnsupportedOperationException();
//         QueryStatusListener listener = (QueryStatusListener) listeners.get(queryId);
//         if (listener != null)
//         {
//            listener.queryStatusChanged(queryId, StatusHelper.getServiceStatus(status));
//         }
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
         if (response.getElementsByTagName(DocMessageHelper.ERROR_TAG).getLength() >0)
         {
            //response was an error - not sure what to do with this - log it and return it for now
            Log.logError("Server error: "+
                        response.getElementsByTagName(DocMessageHelper.ERROR_TAG).item(0).getNodeValue()
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
Revision 1.6  2003/12/02 19:49:44  mch
Moved snippet and socket-processing stuff into their own packages

Revision 1.5  2003/11/26 16:31:46  nw
altered transport to accept any query format.
moved back to axis from castor

Revision 1.4  2003/11/18 11:02:46  mch
Removing client dependencies on server

Revision 1.3  2003/11/17 12:46:15  mch
Moving common to snippet

Revision 1.2  2003/11/17 12:32:27  mch
Moved QueryStatus to query pacakge

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.2  2003/11/05 18:52:53  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

Revision 1.18  2003/10/02 12:53:49  mch
It03-Close

Revision 1.17  2003/09/22 16:51:24  mch
Now posts results to dummy myspace

Revision 1.16  2003/09/18 13:12:27  nw
renamed delegate methods to match those in web service

Revision 1.15  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

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





