/*
 * $Id: SocketServer.java,v 1.4 2003/09/15 11:15:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.astrogrid.datacenter.service.ServiceServer;
import org.astrogrid.datacenter.config.Configuration;
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
   ServerSocket serverSocket = null;

   /** Publically available standard AstroGrid datacenter server socket, so
    * that other code (eg test!) can reach it */
   public static final int DEFAULT_PORT = 2901;

   /**
    * Constructs a server socket on the standard/configured port and starts
    * to listen on it
    */
   public SocketServer() throws IOException
   {
      this(Integer.parseInt(Configuration.getProperty("SocketServerPort", ""+DEFAULT_PORT)));
   }

   /** Constructs a server socket on the given port and
    * starts to listen on it
    */
   public SocketServer(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
   }


   public void run()
   {
      while (true)
      {
         try
         {
            Log.trace("Waiting for socket connection...");
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

   public static void main(String args[]) throws IOException
   {
      SocketServer server = new SocketServer();

      server.run();
   }
}

