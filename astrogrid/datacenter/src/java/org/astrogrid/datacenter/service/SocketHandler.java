/*
 * $Id: SocketHandler.java,v 1.15 2003/09/22 16:51:24 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.common.QueryIdHelper;
import org.astrogrid.datacenter.common.QueryStatus;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.common.StatusHelper;
import org.astrogrid.datacenter.delegate.SocketDelegate;
import org.astrogrid.datacenter.io.SocketXmlInputStream;
import org.astrogrid.datacenter.io.SocketXmlOutputStream;
import org.astrogrid.datacenter.io.TraceInputStream;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.QueryListener;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Handles a single socket for the socket-based server -
 * @see SocketHandler
 * @author M Hill
 */

public class SocketHandler extends ServiceServer implements Runnable, QueryListener
{
   /** Socket connection */
   private Socket socket = null;
   /** XML helper stream wrapped around socket output stream */
   private SocketXmlOutputStream out = null;
   /** XML helper stream wrapped around socket input stream */
   private SocketXmlInputStream in = null;

   /** Constructor sets up streams and starts thread to run this
    * handler
    */
   public SocketHandler(Socket givenSocket) throws IOException
   {
      this.socket = givenSocket;
      out = new SocketXmlOutputStream(socket.getOutputStream());

      TraceInputStream tin = new TraceInputStream(socket.getInputStream());
      tin.setState(true);
      tin.copy2File(new File("incomingMsgs.log"));
      in = new SocketXmlInputStream(tin);


      //create a thread running this isntance and start it
      new Thread(this, "SocketHandler").start();
   }

   /** called when the status changes - writes out the new status to the
    * socket
    */
   public void serviceStatusChanged(DatabaseQuerier querier)
   {
      try
      {
         String notifyTag =
            "<"+SocketDelegate.NOTIFY_STATUS_TAG+">\n"+
            StatusHelper.makeStatusTag(querier.getHandle(), querier.getStatus())+
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
            Log.trace("SocketHandler: Reading/Waiting on incoming document");
            //read incoming document
            Document docRequest = in.readDoc();

            //what kind of operation is it?
            if (docRequest.getElementsByTagName(SocketDelegate.REQ_REGISTRY_METADATA_TAG).getLength() > 0)
            {
               //requested registry metadata
               Log.trace("SocketHandler: Writing registry metadata");

               out.writeAsDoc(getVOResource());
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.CREATE_QUERY_TAG).getLength() > 0)
            {
               Log.trace("SocketHandler: Creating a query");
               DatabaseQuerier querier = DatabaseQuerier.createQuerier(docRequest.getDocumentElement());
               out.writeDoc(ResponseHelper.makeQueryCreatedResponse(querier));
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.START_QUERY_TAG).getLength() > 0)
            {
               Log.trace("SocketHandler: Starting a query");
               DatabaseQuerier querier = getQuerierFromDoc(docRequest);

               Thread queryThread = new Thread(querier);
               queryThread.start();

               out.writeDoc(ResponseHelper.makeQueryStartedResponse(querier));
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.REGISTER_LISTENER_TAG).getLength() > 0)
            {
               Log.trace("SocketHandler: Registering listeners");
               DatabaseQuerier querier = getQuerierFromDoc(docRequest);
               querier.registerWebListeners(docRequest.getDocumentElement());
               out.writeDoc(ResponseHelper.makeStatusResponse(querier));
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.ABORT_QUERY_TAG).getLength() > 0)
            {
               Log.logError("Not implemented yet: "+ docRequest.getDocumentElement().getNodeName());
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.REQ_RESULTS_TAG).getLength() > 0)
            {
               Log.trace("SocketHandler: Results Requested");
               DatabaseQuerier querier = getQuerierFromDoc(docRequest);
               Document response = ResponseHelper.makeResultsResponse(querier, querier.getResultsLoc());
               out.writeDoc(response);
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.REQ_STATUS_TAG).getLength() > 0)
            {
               Log.trace("SocketHandler: Status Requested");
               DatabaseQuerier querier = getQuerierFromDoc(docRequest);
               Document response = ResponseHelper.makeStatusResponse(querier);
               out.writeDoc(response);
            }
            else if (docRequest.getElementsByTagName(SocketDelegate.DO_QUERY_TAG).getLength() > 0)
            {
               //a blocking/synchronous query
               DatabaseQuerier querier = DatabaseQuerier.createQuerier(docRequest.getDocumentElement());
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
            Log.logWarning(this, "(assuming fatal & terminating handler)", e);
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
   private DatabaseQuerier getQuerierFromDoc(Document doc)
   {
      String queryId = QueryIdHelper.getQueryId(doc.getDocumentElement());
      DatabaseQuerier querier = DatabaseQuerier.getQuerier(queryId);
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
         out.writeAsDoc("<"+ResponseHelper.ERROR_TAG+">"+s+" ["+th+"]"+"</"+ResponseHelper.ERROR_TAG+">");
      }
      catch (IOException ioe) {}
   }

}

/*
$Log: SocketHandler.java,v $
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

