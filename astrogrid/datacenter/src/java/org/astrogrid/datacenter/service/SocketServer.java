/*
 * $Id: SocketServer.java,v 1.2 2003/09/09 17:52:29 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.astrogrid.datacenter.service.ServiceServer;
import org.astrogrid.datacenter.config.Configuration;

/**
 * A quick and dirty tcp/ip socket server that listens on a port, reading
 * SOAP messages and returning result documents.
 * <p>
 * This means we can run a datacenter without runing Tomcat, Axis, etc, etc.
 * <p>
 * @author M Hill
 */

public class SocketServer
{
   ServerSocket serverSocket = null;

   /**
    * Constructs a server socket on the standard/configured port and starts
    * to listen on it
    */
   public SocketServer() throws IOException
   {
      this(Integer.parseInt(Configuration.getProperty("SocketServerPort", "1901")));
   }

   /** Constructs a server socket on the given port and
    * starts to listen on it
    */
   public SocketServer(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
   }


   public void run() throws IOException
   {
      while (true)
      {
         //wait for connection - when you get one create
         //new socket handler to manage it
         Socket socket = serverSocket.accept();
         SocketHandler handler = new SocketHandler(socket);
      }
   }

   public static void main(String args[]) throws IOException
   {
      SocketServer server = new SocketServer();

      server.run();
   }
}

