package net.mchill.log;

import net.mchill.log.*;
import java.net.*;
import java.io.*;
import java.util.*;

/**
 * A Client Log retrieves log events from a remote machine (through its
 * Log2Client handlers) and passes them on to the local Log distribution.
 * While this can mean infinite forwardings, it can be used to view and
 * log to file (for example) activities on a server machine where there
 * are no permissions to write to disk and no console.
 * <P>
 * Use start/stop to kick it off and it will run independently, continuously
 * trying to connect if not already connected and read otherwise
 */
public class ClientLog implements Runnable
{
   Socket socket = null;
   ObjectInputStream in = null;
   
   String address = "";
   int port = Log2Clients.DEFAULT_PORT;
   boolean terminate = true;
   
   boolean retrying = false;  //true if one+ attempt at connection has tried & failed
   
   /**
    * The constructor. This method is called when the frame is created.
    */
   public ClientLog(String aUrl, int aPort)
   {
      address = aUrl;
      port = aPort;
   }
   
   /**
    * Keeps attempting to connect.
    */
   public void start()
   {
      Thread monitorThread = new Thread(this, "Client Log "+socket);
      monitorThread.start();
      terminate = false;
   }
   /**
    * Marks thread to stop
    */
   public void stop()
   {
      terminate = true;
   }
   
   public void setAddress(String aUrl, int aPort)
   {
      disconnect();
      address = aUrl;
      port = aPort;
   }
   
   public String getAddress()
   {
      if (socket == null)
         return address+":"+port;
      else
         return socket.getInetAddress().toString();
   }
   
   
   /**
    * Open connection to address
    **/
   public void connect()
   {
      disconnect();

      //now open new one
         if (retrying)
         {
            Log.logInfo(this,"Retrying connection to "+getAddress()+"...");
         }
         else
         {
            Log.logInfo(this,"Opening connection to "+getAddress()+"...");
         }

      try
      {
         socket = new Socket(address,port);
         in = new ObjectInputStream(socket.getInputStream());

         Log.logInfo("Connection made to "+socket);
         retrying = false;
      }
      catch (IOException ioe)
      {
         if (!retrying)
            Log.logError(this,"Could not connect to "+getAddress()
                            +": "+ioe,null); //no need to do full stack trace
         else
            if (!(ioe instanceof ConnectException))
               Log.logInfo(this,"Retry failed: "+ioe);
         
         disconnect(); //make sure as tidy as possible.
         retrying = true;
      }
   }


   /**
    * Disconnects socket
    */
   public void disconnect()
   {
      in = null;
      //first close old one
      if (socket != null)
      {
         try {
            socket.close();
         }
         catch (IOException ioe)
         {
            Log.logError(this,"Error closing socket "+socket,ioe);
         }
      }
      socket = null;
   }
   
   /** Used to mark incoming messages' sources, so that the target
    * log can filter by that.*/
   public String getSource()
   {
      if (socket == null)
         return null;
      else
         return ""+socket.getInetAddress();
   }
   

   /**
    * Continuously reads the socket displaying whatever is received.
    * This uses ObjectInputStream to read data - I don't know what
    * happens if a connection is established halfway through a write...
    */
   public void run()
   {
      Object o = null;
      while (!terminate)
      {
         try
         {
            if (in == null)
            {
               if (retrying)
               {
                  //sleep
                  Thread.currentThread().sleep(500);
               }
               //so try to reconnect
               connect();
            }
            else
            {
               o = in.readObject();
               LogEvent logEvent = (LogEvent) o;
               Log.logEvent(logEvent);
            }
         }
         catch (IOException ioe)
         {
            Log.logError(this, "Reading event object", ioe);
            disconnect();
         }
         catch (InterruptedException ie)
         {
            Log.logError("Insomniac Thread could not sleep",ie);
         }
         catch (ClassNotFoundException cnfe)
         {
            Log.logError(this, "Class Not Found, reading from Log Server",cnfe);
         }
         catch (ClassCastException cce)
         {
            if (o == null)
               Log.logError(this, "Null received on "+socket,cce);
            else
               Log.logError(this, "Not a Log Event received on "+socket+" class"+o.getClass(),cce);
         }
      }

      //toggle has been released.  Stop monitoring.
      disconnect();
   }
   
   /** Test */
   public static void main(String[] args)
   {
      Log.addHandler(new Log2Console());
      
      Log2Clients log2Clients = new Log2Clients();
      Log.addHandler(log2Clients);
      
      ClientLog clientLog = new ClientLog("localhost",5000);
      log2Clients.addFilter(new NotSourceFilter(clientLog.getSource()));
      clientLog.start();
      
      Log.logError("Wibble");
   }
   
}
