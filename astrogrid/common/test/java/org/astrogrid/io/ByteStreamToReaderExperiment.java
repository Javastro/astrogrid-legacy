/*
 * $Id: ByteStreamToReaderExperiment.java,v 1.1 2004/10/05 10:35:39 pah Exp $
 * 
 * Created on 29-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.io;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import junit.framework.TestCase;
import junit.framework.TestFailure;

/**
 * A test to see what effect writing an arbitrary byte stream to a socket and then reading it as a char stream. See if it causes an out of memory problem.
 * Initial results would seem to indicate that it does have a pathological behaviour in that the reader just hangs in this case because in this example it never sees and EOL - but it does not loop forever consuming more input, which was the main conjecture. 
 * However, if the input was large enough, then this would cause a problem if the model code was expecting to be processing line-by-line, and thus expecting typical "line size" chunks to be read into memory.
 * 
 * @author Paul Harrison (pah@jb.man.ac.uk) 29-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class ByteStreamToReaderExperiment extends TestCase {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ByteStreamToReaderExperiment.class);

    private static File testFile;
    private static int port=9999;
    private static boolean finished=false;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ByteStreamToReaderExperiment.class);
    }
    
    public void testit() throws Exception
    {
        testFile = File.createTempFile("ByteStreamToReaderTest", null);
        assertNotNull(testFile);
        byte[] testContents = new byte[1024*1024];
        Arrays.fill(testContents, (byte)1);
        FileOutputStream fs = new FileOutputStream(testFile);
        Piper.pipe(new ByteArrayInputStream(testContents), fs);
        fs.close();
        
        MyMonitor monitor = new MyMonitor();
        
        Thread serverThread = new Thread(new Server());
        serverThread.start();
        Thread clientThread = new Thread(new Client(monitor));
        clientThread.start();
        
       monitor.isFinished();
    }
    
    private static class Server implements Runnable
    {
        /**
         * Logger for this class
         */
        private static final Log logger = LogFactory.getLog(Server.class);

        BufferedInputStream fis;
        public Server()
        {
            try {
                fis = new BufferedInputStream( new FileInputStream(testFile));
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
            try {
                System.out.println("starting server thread");
                ServerSocket svr = new ServerSocket(port);
                Socket outsocket = svr.accept(); // wait for a connection...
                System.out.println("a client has connected");
                BufferedOutputStream os = new BufferedOutputStream(outsocket.getOutputStream());
                byte[] theByte = new byte[1024];
                int len;
                int total = 0;
                while ((len = fis.read(theByte)) != -1) {
                    total += len;
                    os.write(theByte);
                    System.out.println("written "+total+" bytes");
                }
                System.out.println("the whole contents have been written");

            }
            catch (IOException e) {
                
                e.printStackTrace();
               
            }            
        }
        
    }
    
    private static class Client implements Runnable
    {
        private MyMonitor theMon;

        /**
         * 
         */
        public Client(MyMonitor mon) {
            theMon = mon;
       }
        /**
         * Logger for this class
         */
        private static final Log logger = LogFactory.getLog(Client.class);

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
          
            try {
                System.out.println("the client has started");
                Socket socket = new Socket(InetAddress.getLocalHost(), port);
                System.out.println("the client has connected to the socket");
                BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                    String str;
                    int len;
                    while ((str = rd.readLine()) != null) {
                       
                       System.out.println("have read "+ str.length());
                    }
                    System.out.println("reader has finished");
            }
            catch (UnknownHostException e) {
              
                e.printStackTrace();
            }
            catch (IOException e) {
               
                e.printStackTrace();
            }
            
            theMon.setFinished(true);
        }
        
    }
    private class MyMonitor
    {
        private boolean finished = false;
        MyMonitor()
        {
            
        }
        
        public synchronized boolean isFinished() {
            try {
                wait();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            return finished;
        }
        public synchronized void setFinished(boolean finished) {
            this.finished = finished;
            notifyAll();
        }
    }
}
