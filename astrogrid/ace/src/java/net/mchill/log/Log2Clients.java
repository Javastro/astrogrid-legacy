package net.mchill.log;

import java.io.IOException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.NullPointerException;
import java.util.Vector;
import java.net.*;
import java.io.*;


/**
 * A Log Handler that forwards all messages to any tcp/ip clients that are
 * connected to the server socket that this will create.  Currently
 * the forwarding is a simple message string reflecting the event.
 *
 * @version          :
 * @Created          : Aug 2001
 * @Last Update      :
 *
 * @author           : M Hill
 **/

public class Log2Clients extends FilteredHandlerAdaptor implements Runnable
{
   private ServerSocket socket = null;
   private Vector clients = new Vector();

   public static final int DEFAULT_PORT = 5000;

   /**
    * Constructor.  Creates server socket and spins of thread to listen
    * to it.
    */
   public Log2Clients(int port)
   {
      try {
         socket = new ServerSocket(port);
         Thread serverListener = new Thread(this, "Log2Clients server-socket listening thread");
         serverListener.start();
      }
      catch (IOException ioe)
      {
         Log.logError("Could not create Log2Clients class for port "+port,ioe);
      }
   }

   /**
    * Default constructor, registers socket server on port 50
    */
   public Log2Clients()
   {
      this(DEFAULT_PORT);
   }
   
   /**
    * Writes out the event to all the connected clients (if any)
    * By being synchronized avoids different threads mixing up errors
    */
   public synchronized void  messageLogged(LogEvent anEvent)
   {
      //make a copy for threadsafety
      Vector copyOfClients = (Vector) clients.clone();
      Socket clientSocket = null;
      OutputStream out = null;
      
      for (int i = 0; i<copyOfClients.size(); i++)
      {
         try
         {
            clientSocket = (Socket) copyOfClients.elementAt(i);
            out = clientSocket.getOutputStream();
            
            //check to see if outputStream still exists
            if (out == null) throw new IOException("OutputStream null");
            
            writeEvent(anEvent, out);
         }
         catch (IOException ioe)
         {
            //caused by some kind of disconnection
            //remove client from list and log this as info
            //nb - not sure about recursiveness here - shouldn't
            //matter.
            clients.remove(i);
            Log.logWarning(null,"Client "+clientSocket.getInetAddress()+" disconnected from log",ioe);
         }
      }
   }

   /** Writes an event to the output stream - called by messageLogged
    * to write the event to each of the connected clients.
    * A separate method so that
    * it can be overwridden by subclasses, allowing us to write out
    * messages in different formats.
    * This one writes using simple ASCII print-like statements
    */
   protected void writeEvent(LogEvent anEvent, OutputStream out)
   {
      //create formatter - automatic flush=true
      PrintWriter writer = new PrintWriter(out, true);

      //write to client
      writer.println(anEvent);
            
      if (anEvent.getException() != null)
      {
         anEvent.getException().printStackTrace(writer);
      }
    }/**/

   /** Writes an event to the output stream - called by messageLogged
    * to write the event to each of the connected clients.
    * A separate method so that
    * it can be overwridden by subclasses, allowing us to write out
    * messages in different formats.
    * This one writes using object stream
    *
   protected void writeEvent(LogEvent anEvent, OutputStream out) throws IOException
   {
      new ObjectOutputStream(out).writeObject(anEvent);
   }
    */
   
    /**
    * Keeps continuously checking TCP/IP server port and handling connection
    * requests.
    */
   public void run()
   {
      Socket clientSocket = null;
      
      while (true)
      {
         try
         {
            clientSocket = socket.accept();
            clients.add(clientSocket);
            Log.logInfo("Client "+clientSocket.getInetAddress()+" connected to logging");
         }
         catch (IOException ioe)
         {
            Log.logError("Log2Client Server Socket Error ",ioe);
         }
      }

   }

}
