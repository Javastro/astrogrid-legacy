/*
 * $Id: SocketHandler.java,v 1.5 2003/09/11 09:57:15 nw Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.service.ServiceListener;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Handles a single socket for the socket-based server -
 * @see SocketHandler
 * @author M Hill
 */

public class SocketHandler extends ServiceServer implements Runnable, ServiceListener
{
   private Socket socket = null;
   private DataOutputStream out = null;

   public SocketHandler(Socket givenSocket) throws IOException
   {
      this.socket = givenSocket;
      out = new DataOutputStream(socket.getOutputStream());

      //create a thread running this isntance and start it
      new Thread(this).start();
   }

   /** called when the status changes - writes out the new status to the
    * socket
    */
   public void serviceStatusChanged(DatabaseQuerier querier)
   {
      try
      {
         out.writeChars(""+querier.getStatus());
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
    */
   public void run()
   {
      try
      {
         InputStream in = socket.getInputStream();
         while (socket.isConnected())
         {
            try
            {
               //read incoming document
               Element docRequest = XMLUtils.newDocument(in).getDocumentElement();

               //assume a blocking query
               DatabaseQuerier querier = DatabaseQuerier.doQueryGetResults(docRequest);

               querier.setStatus(ServiceStatus.RUNNING_RESULTS);

               Document response = ResponseHelper.makeResultsResponse(querier, querier.getResults().toVotable().getDocumentElement());

               XMLUtils.DocumentToStream(response, out);
            }
            catch (IOException e)
            {
               e.printStackTrace(System.out);
            }
            catch (SAXException e)
            {
               //need to tell user
               out.writeChars("Oh no it's all gone horribly wrong "+e);
               e.printStackTrace(System.out);
            }
            catch (QueryException e)
            {
               //need to tell user
               out.writeChars("Oh no it's all gone horribly wrong "+e);
               e.printStackTrace(System.out);
            }
            catch (Throwable e)
            {
               out.writeChars("Oh no it's all gone horribly wrong "+e);
               e.printStackTrace(System.out);
            }
         }
      }
      catch (IOException e)
      {
         e.printStackTrace(System.out);
      }
   }


}

/*
$Log: SocketHandler.java,v $
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

