/*
 * $Id: SocketHandler.java,v 1.2 2003/12/15 14:35:01 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service.socket;
import org.astrogrid.datacenter.service.*;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.delegate.agss.SocketDelegate;
import org.astrogrid.datacenter.snippet.io.XmlDocInputStream;
import org.astrogrid.datacenter.snippet.io.XmlDocOutputStream;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.snippet.DocMessageHelper;
import org.astrogrid.datacenter.snippet.QueryIdHelper;
import org.astrogrid.datacenter.snippet.ResponseHelper;
import org.astrogrid.datacenter.snippet.StatusHelper;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Handles a single socket for the socket-based server -
 * @see SocketHandler
 * @author M Hill
 */

public class SocketHandler extends ServiceServer implements Runnable, QuerierListener
{
   /** Socket connection */
   private Socket socket = null;
   /** XML helper stream wrapped around socket output stream */
   private XmlDocOutputStream out = null;
   /** XML helper stream wrapped around socket input stream */
   private XmlDocInputStream in = null;

   /** Constructor sets up streams and starts thread to run this
    * handler
    */
   public SocketHandler(Socket givenSocket) throws IOException
   {
      this.socket = givenSocket;
      out = new XmlDocOutputStream(socket.getOutputStream());

//      TraceInputStream tin = new TraceInputStream(socket.getInputStream());
//      tin.setState(true);
//      tin.copy2File(new File("incomingMsgs.log"));
//      in = new SocketXmlInputStream(tin);
      in = new XmlDocInputStream(socket.getInputStream());


      //create a thread running this isntance and start it
      new Thread(this, "SocketHandler["+socket.getPort()+"]").start();
   }

   /** called when the status changes - writes out the new status to the
    * socket
    */
   public void queryStatusChanged(Querier querier)
   {
      try
      {
         String notifyTag =
            "<"+SocketDelegate.NOTIFY_STATUS_TAG+">\n"+
            StatusHelper.makeStatusTag(querier.getQueryId(), querier.getStatus())+
            "</"+SocketDelegate.NOTIFY_STATUS_TAG+">";

         out.writeAsDoc(notifyTag);
      }
      catch (IOException e)
      {
         Log.logError("Could not pass service Status change ("+querier.getStatus()+") to socket client",e);
      }

   }


   /**
    * Runs forever handling dociments sent to the
    * socket, and returning responses, until socket is
    * closed or some kind of timeout...
    * <p>
    * NB each request/response is still a pair, so that we don't end
    * up with responses getting mixed up.  Only status notifications get around
    * this...
    * <p>
    */
   public void run()
   {
      // This is quite a large method, handling all the various calls, but
      // given each one is only a few lines it seems a bit overkill to seperate
      // them all out into different methods

      boolean fatal = false;

      while (socket.isConnected() && !fatal)
      {
         try
         {
            Log.trace("SocketHandler["+socket.getPort()+"]: Reading/Waiting on incoming document");
            //read incoming document
            Document docRequest = in.readDoc();

            //what kind of operation is it?
            if (docRequest.getElementsByTagName(SocketDelegate.REQ_REGISTRY_METADATA_TAG).getLength() > 0)
            {
               //requested registry metadata
               Log.trace("SocketHandler["+socket.getPort()+"]: Writing registry metadata");

               out.writeAsDoc(getVOResource());
            }
            if (docRequest.getElementsByTagName(SocketDelegate.REQ_METADATA_TAG).getLength() > 0)
            {
               //requested metadata
               Log.trace("SocketHandler["+socket.getPort()+"]: Writing metadata");

               out.writeAsDoc(getMetadata());
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.CREATE_QUERY_TAG).getLength() > 0)
            {
               Log.trace("SocketHandler["+socket.getPort()+"]: Creating a query");
//               Querier querier = QuerierManager.createQuerier(docRequest.getDocumentElement());
//               out.writeDoc(ResponseHelper.makeQueryCreatedResponse(querier));
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.START_QUERY_TAG).getLength() > 0)
            {
               Log.trace("SocketHandler["+socket.getPort()+"]: Starting a query");
               Querier querier = getQuerierFromDoc(docRequest);

               Thread queryThread = new Thread(querier);
               queryThread.start();

               out.writeDoc(ResponseHelper.makeQueryStartedResponse(querier));
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.REGISTER_LISTENER_TAG).getLength() > 0)
            {
               Log.trace("SocketHandler["+socket.getPort()+"]: Registering listeners");
               Querier querier = getQuerierFromDoc(docRequest);
               querier.registerWebListeners(docRequest.getDocumentElement());
               out.writeDoc(ResponseHelper.makeStatusResponse(querier));
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.ABORT_QUERY_TAG).getLength() > 0)
            {
               Log.logError("Not implemented yet: "+ docRequest.getDocumentElement().getNodeName());
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.REQ_RESULTS_TAG).getLength() > 0)
            {
               Log.trace("SocketHandler["+socket.getPort()+"]: Results Requested");
               Querier querier = getQuerierFromDoc(docRequest);
               Document response = ResponseHelper.makeResultsResponse(querier, new URL(querier.getResultsLoc()));
               out.writeDoc(response);
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.REQ_STATUS_TAG).getLength() > 0)
            {
               Log.trace("SocketHandler["+socket.getPort()+"]: Status Requested");
               Querier querier = getQuerierFromDoc(docRequest);
               Document response = ResponseHelper.makeStatusResponse(querier);
               out.writeDoc(response);
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.DO_QUERY_TAG).getLength() > 0)
            {
               //a blocking/synchronous query
               // FIXME
               //DatabaseQuerier querier = (DatabaseQuerier) QuerierManager.createQuerier(docRequest.getDocumentElement());
               Querier querier = null;
               querier.registerListener(this);
               QueryResults results = querier.doQuery();

               querier.setStatus(QueryStatus.RUNNING_RESULTS);

               Document response = ResponseHelper.makeResultsResponse(querier, results.toVotable().getDocumentElement());

               out.writeAsDoc(response.getDocumentElement());
            }
            else
            {
               //unknown command
               Log.logError("Document received but don't know how to handle it.  Root element="+
                               docRequest.getDocumentElement().getNodeName());
            }
         }
         catch (SocketException e)
         {
            Log.logError(this.toString(), "(assuming fatal & terminating handler)", e);
            fatal = true;
         }
         catch (IOException e)
         {
            Log.logError("", e);
            //hmm should this be fatal too?
         }
         catch (SAXException e)
         {
            String s = "XML Error with message: (unknown)";
            Log.logError(s, e);
            writeError(s,e);
         }
         catch (QueryException e)
         {
            //need to tell user
            String s = "Given Query is invalid";
            Log.logError(s, e);
            writeError(s,e);
         }
         catch (Throwable e)
         {
            String s = "Oh no it's all gone horribly wrong";
            Log.logError(s, e);
            writeError(s,e);
         }
      }
   }

   /**
    * Returns the querier referred to in the document
    */
   private Querier getQuerierFromDoc(Document doc)
   {
      String queryId = QueryIdHelper.getQueryId(doc.getDocumentElement());
      Querier querier = null; //NWW - fix QuerierManager.getQuerier(queryId);
      if (querier == null)
      {
         throw new IllegalArgumentException("No querier found for id='"+queryId+"'"
                                               +", source doc="+XMLUtils.DocumentToString(doc));
      }
      return querier;
   }

   /**
    * Attempts to let client know of error
    * @todo wrap up in proper XML
    */
   private void writeError(String s, Throwable th)
   {
      try
      {
         out.writeAsDoc("<"+DocMessageHelper.ERROR_TAG+">"+s+" ["+th+"]"+"</"+DocMessageHelper.ERROR_TAG+">");
      }
      catch (IOException ioe) {}
   }

}

/*
$Log: SocketHandler.java,v $
Revision 1.2  2003/12/15 14:35:01  mch
Fix to logging to adjust for new, simpler logger

Revision 1.1  2003/12/02 19:50:56  mch
Moved snippet and socket-processing stuff into their own packages

Revision 1.13  2003/12/01 20:57:39  mch
Abstracting coarse-grained plugin

Revision 1.12  2003/12/01 16:43:52  nw
dropped _QueryId, back to string

Revision 1.11  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.10  2003/11/27 17:28:09  nw
finished plugin-refactoring

Revision 1.9  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.

Revision 1.8  2003/11/25 18:50:06  mch
Abstracted Querier from DatabaseQuerier

Revision 1.7  2003/11/25 14:17:24  mch
Extracting Querier from DatabaseQuerier to handle non-database backends

Revision 1.6  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.5  2003/11/18 11:10:16  mch
Removed client dependencies on server

Revision 1.4  2003/11/17 18:55:16  mch
Moved to snippet package

Revision 1.3  2003/11/17 15:41:48  mch
Package movements

Revision 1.2  2003/11/17 12:16:33  nw
first stab at mavenizing the subprojects.

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.19  2003/11/05 18:54:28  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.18  2003/10/06 18:56:58  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

Revision 1.17  2003/10/02 12:53:49  mch
It03-Close

Revision 1.16  2003/09/24 21:03:46  nw
altered to use DatabaseQuerierManager

Revision 1.15  2003/09/22 16:51:24  mch
Now posts results to dummy myspace

Revision 1.14  2003/09/17 14:51:30  nw
tidied imports - will stop maven build whinging

Revision 1.13  2003/09/16 17:36:46  mch
Added checks for null/missing query ids

Revision 1.12  2003/09/16 16:00:15  mch
Added more message handlers

Revision 1.11  2003/09/16 15:23:16  mch
Listener fixes and rationalisation

Revision 1.10  2003/09/15 22:05:34  mch
Renamed service id to query id throughout to make identifying state clearer

Revision 1.9  2003/09/15 21:27:15  mch
Listener/state refactoring.

Revision 1.8  2003/09/15 16:42:03  mch
Fixes to make maven happy(er)

Revision 1.7  2003/09/15 11:15:01  mch
Fixes to make it work... document reading/writing problems

Revision 1.6  2003/09/11 17:41:33  mch
Fixes to socket-based client/server

Revision 1.5  2003/09/11 09:57:15  nw
reordered catch statements - my compiler was complaining that
two were unreachable (as Throwable beats all)

Revision 1.4  2003/09/10 17:57:31  mch
Tidied xml doc helpers and fixed (?) job/web listeners

Revision 1.3  2003/09/10 12:08:44  mch
Changes to make web interface more consistent

Revision 1.2  2003/09/09 17:52:29  mch
ServiceStatus move and config key fix

Revision 1.1  2003/09/07 18:41:42  mch
Direct Socket (apacheless) server


*/

