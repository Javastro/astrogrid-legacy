/*
 * $Id: SocketServer.java,v 1.1 2003/12/02 19:50:56 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.astrogrid.config.SimpleConfig;
import org.astrogrid.log.Log;
 
/**
 * A quick and dirty tcp/ip socket server that listens on a port, reading
 * SOAP messages and returning result documents.
 * <p>
 * This means we can run a datacenter without runing Tomcat, Axis, etc, etc.
 * <p>
 * @author M Hill
 */

public class SocketServer implements Runnable
{
   /** Listening socket tied to a port that clients will connect to*/
   private ServerSocket serverSocket = null;

   /** Publically available standard AstroGrid datacenter server socket, so
    * that other code (eg test!) can reach it */
   public static final int DEFAULT_PORT = 2901;

   /**
    * Constructs a server socket on the standard/configured port and starts
    * to listen on it
    */
   public SocketServer() throws IOException
   {
      this(Integer.parseInt(SimpleConfig.getProperty("SocketServerPort", ""+DEFAULT_PORT)));
   }

   /** Constructs a server socket on the given port.  To start listening, use
    * the run() method
    */
   public SocketServer(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
   }

   /**
    * Threads can be used to run this instance.
    * Continuously listens out on server socket,
    * spawning SocketHandler instances when a connection is made to handle
    * that particular connection
    */
   public void run()
   {
      while (true)
      {
         try
         {
            Log.trace("Waiting for socket connection on port "+serverSocket.getLocalPort()+"...");
            //wait for connection - when you get one create
            //new socket handler to manage it
            Socket socket = serverSocket.accept();
            Log.trace("...socket connection made! Spawning handler for "+socket.getInetAddress()+":"+socket.getPort()+"...");
            SocketHandler handler = new SocketHandler(socket);
         }
         catch (IOException ioe)
         {
            Log.logError("Failure managing server (listening) socket", ioe);
         }
      }
   }

   /**
    * Application starting point - run this class to start your Socket Server
    */
   public static void main(String args[]) throws IOException
   {
      SocketServer server = new SocketServer();

      server.run();
   }
}

