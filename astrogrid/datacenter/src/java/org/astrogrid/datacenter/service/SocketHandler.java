/*
 * $Id: SocketHandler.java,v 1.8 2003/09/15 16:42:03 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.astrogrid.datacenter.common.StatusHelper;
import org.astrogrid.datacenter.delegate.SocketDelegate;
import org.astrogrid.datacenter.io.SocketXmlInputStream;
import org.astrogrid.datacenter.io.SocketXmlOutputStream;
import org.astrogrid.datacenter.io.TraceInputStream;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.service.ServiceListener;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Handles a single socket for the socket-based server -
 * @see SocketHandler
 * @author M Hill
 */

public class SocketHandler extends ServiceServer implements Runnable, ServiceListener
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
//      tin.setState(true);
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
         out.writeAsDoc(StatusHelper.makeStatusTag(querier.getHandle(), querier.getStatus()));
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
    * NB each request/response is still a pair, so that we don't end
    * up with responses getting mixed up.
    */
   public void run()
   {
         //InputStream in = socket.getInputStream();
         boolean fatal = false;

         while (socket.isConnected() && !fatal)
         {
            try
            {
               Log.trace("SocketHandler: Reading/Waiting on incoming document");
               //read incoming document
               Document docRequest = in.readDoc();

               //what kind of operation is it?
               if (docRequest.getElementsByTagName(SocketDelegate.REQ_REG_METADATA_TAG).getLength() > 0)
               {
                  //requested registry metadata
                  Log.trace("SocketHandler: Writing registry metadata");

                  out.writeAsDoc(getVOResource());
               }
               else
               {
                  //assume a blocking query
                  DatabaseQuerier querier = DatabaseQuerier.doQueryGetResults(docRequest.getDocumentElement());

                  querier.setStatus(ServiceStatus.RUNNING_RESULTS);

                  Document response = ResponseHelper.makeResultsResponse(querier, querier.getResults().toVotable().getDocumentElement());

                  out.writeAsDoc(response.getDocumentElement());
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

