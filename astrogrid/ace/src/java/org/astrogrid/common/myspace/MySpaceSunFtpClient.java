/*
 $Id: MySpaceSunFtpClient.java,v 1.1 2003/08/25 18:36:22 mch Exp $

 Date         Author      Changes
 1 Nov 2002   M Hill      Created

 (c) Copyright...
 */

package org.astrogrid.common.myspace;

import java.io.*;
import java.net.*;
import sun.net.ftp.*;
import java.util.Enumeration;

import org.astrogrid.log.Log;

/**
 * An implenentation of myspace using an ordinary FTP server.
 *
 * Developed using the built-in ftp clients supplied with java - but note
 * that these are NOT supported directly.
 */

public class MySpaceSunFtpClient implements MySpaceClient
{
   private String server = null;
   private int port = DEFAULT_PORT;

   private String user = ANON_USER;
   private String password = "mch@roe.ac.uk";

   private FullFtpClient ftpConnection = null;

   private final static int DEFAULT_PORT = 21;
   private final static String ANON_USER = "anonymous";

   /**
    * A more complete FTP client based on sun's
    *
    * @version 1.0, 01/19/97, 1.1 6/11/02
    * @author  Elliotte Rusty Harold, extended further by M C Hill
    */
   public class FullFtpClient extends FtpClient {

      /** New FullFtpClient connected to host <i>host</i>. */
      public FullFtpClient(String host) throws IOException {
         super(host);
      }

      /** New FullFtpClient connected to host <i>host</i>, port <i>port</i>. */
      public FullFtpClient(String host, int port) throws IOException {
         super(host, port);
      }

      /** Move up one directory in the ftp file system *
      public void cdup() throws IOException {
         issueCommandCheck("CDUP");
      }

      /** Create a new directory named s in the ftp file system */
      public void mkdir(String s) throws IOException
      {
         // this.sendServer("mkdir "+s+"\n");
         issueCommandCheck("mkdir " + s);
      }

      /** Ensure directory s exists - Create a new directory named s if it does not already exist */
      public void endir(String s) throws IOException
      {
         try
         {
            mkdir(s);
         }
         catch (FtpProtocolException fpe)
         {
            if (fpe.getMessage().indexOf("521") == -1) //ignore this one - error 521 = directory exists
            {
               throw fpe;
            }
         }
      }

      /** Delete the specified directory from the ftp file system */
      public void rmdir(String s) throws IOException {
         issueCommandCheck("RMD " + s);
      }

      /** Delete the file s from the ftp file system */
      public void delete(String s) throws IOException {
         issueCommandCheck("DELE " + s);
      }

      /** Get the name of the present working directory on the ftp file system */
      public String currentDir() throws IOException
      {
         //this.sendServer("PWD");
         issueCommandCheck("PWD");
//         StringBuffer result = new StringBuffer();
//         for (Enumeration e = serverResponse.elements(); e.hasMoreElements();) {
//            result.append((String) e.nextElement());
//         }
//         return result.toString();
         String response = getResponseString();
         return response.substring(response.indexOf('"'), response.lastIndexOf('"'));
      }

      /** Move up a directory (there is a command for this is 1.4, but not 1.3 */
      public void cdup() throws IOException {
         issueCommandCheck("CD .. ");
      }

      // all client commands are sent through here
      public int issueCommand(String cmd) throws IOException
      {
         org.astrogrid.log.Log.trace("FTP>"+cmd);
         return super.issueCommand(cmd);
      }

      public int readServerResponse() throws IOException
      {
         int result = super.readServerResponse();
         for (Enumeration e = serverResponse.elements() ; e.hasMoreElements() ;)
         {
            org.astrogrid.log.Log.trace("FTP<"+e.nextElement());
         }
         return result;

      }

      public String toString()
      {
         return "Sun-derived FTP Client to "+server+":"+port;
      }
   }

   /**
    * Construct myspace client using given server address and defalut port
    */
   public MySpaceSunFtpClient(String mySpaceServer)
   {
      this(mySpaceServer, DEFAULT_PORT);
   }

   /**
    * Construct myspace client using given server address and port
    */
   public MySpaceSunFtpClient(String mySpaceServer, int serverPort)
   {
      this.server = mySpaceServer;
      this.port = serverPort;
   }

   /**
    * Set login parameters.  by default, this myspace tries to login
    * as anonymous.
    */
   public void setLogin(String loginUser, String loginPassword)
   {
      this.user = loginUser;
      this.password = loginPassword;
   }

   /**
    * Connect to server, get authorised and move to the directory to be used
    * for temporary publications
    */
   public void connect() throws IOException
   {
      org.astrogrid.log.Log.trace("Connecting to "+this+"...");

      try
      {
         ftpConnection = new FullFtpClient(server, port);
         ftpConnection.login(user, password);
         ftpConnection.binary();
         // ftpConnection.endir("myspace");  //ensure directory myspace exists
         ftpConnection.cd("myspace");
         Log.trace("...logged on to "+ftpConnection /* not in 1.3 +" running "+ftpConnection.system() **/ +"...");
      }
      catch (IOException ioe)
      {
         ftpConnection = null;
         throw ioe;
      }
   }

   /**
    * Disconnects from server, translating all faults into IOExceptions
    */
   public void disconnect() throws IOException
   {
      ftpConnection.closeServer();
      ftpConnection = null;
   }

   public String toString()
   {
      return "FtpMySpace "+server+":"+port;
   }

   /**
    * Copies given file to server; unique Id specifies the subdirectory the
    * file will be placed in.  That subdirectory will be created if it does
    * not already exist.
    * Returns the URL of the file on the server
    */
   public String publicise(String uniqueId, String localFilename) throws IOException
   {
      org.astrogrid.log.Log.affirm(ftpConnection != null, "Trying to publish before connecting. Call connect() before publicise(...)");

      File localFile = new File(localFilename);
      String filename = localFile.getName(); // the name without the path

      org.astrogrid.log.Log.trace("Publicise: Copying '"+localFilename+"' to '"+server+"'...");

      try
      {
         //         ftpConnection.endir(uniqueId);   //ensure directory exists
         ftpConnection.cd(uniqueId);
         OutputStream remoteOut = new BufferedOutputStream(ftpConnection.put(filename));
         InputStream localIn     = new BufferedInputStream(new FileInputStream(localFile));

         //copy
         int i = 0;
         while ((i = localIn.read()) != -1)
         {
            remoteOut.write((byte) i);
         }
         localIn.close();
         remoteOut.close();

         //work out where it is
         String remoteUrl = "ftp://"+server+":"+port
            //+"/astrogrid/myspace/"+uniqueId+"/"+filename;
            +ftpConnection.currentDir()+"/"+filename;

         ftpConnection.cdup();
         return remoteUrl;
      }
      catch (Exception e)
      {
         IOException ioe = new IOException(e+", sending '"+localFile+"' to "+this);
         //ioe.setStackTrace(e.getStackTrace());  //java v1.4+
         ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
   }

   /**
    * Gets the file identified by the remote string (this might be a url)
    * and copy it to the local filename.  If possible, it should only do this
    * if required - ie if poss it should do some sort of time/date or
    * checksum comparison.  Returns full path to local file
    */
   public String update(String remoteUrl, String localDir) throws IOException
   {
      org.astrogrid.log.Log.affirm(localDir.endsWith("/"), "'"+localDir+"' should terminate with a /");

      //we assume we are in the ordinary login directory.  The filename will
      // be preceeded by the unique id directory.

      //get *name* of remote file
      int fni = remoteUrl.lastIndexOf('/');
      String filename = remoteUrl.substring(fni+1);

      //get previous /<something>/ bit
      int pdi = remoteUrl.lastIndexOf('/',fni-1);
      String dir = remoteUrl.substring(pdi+1, fni);

      org.astrogrid.log.Log.trace("Update: Copying '"+remoteUrl+"' to '"+localDir+"'...");

      try
      {
         ftpConnection.cd(dir);
         InputStream remoteIn = new BufferedInputStream(ftpConnection.get(filename));
         OutputStream localOut = new BufferedOutputStream(new FileOutputStream(localDir+filename));

         //copy
         int i = 0;
         while ((i = remoteIn.read()) != -1)
         {
            localOut.write((byte) i);
         }
         remoteIn.close();
         localOut.close();

         ftpConnection.cdup();
         return localDir+filename;
      }
      catch (Exception e)
      {
         IOException ioe = new IOException(e+", getting '"+filename+"' from "+this);
         //ioe.setStackTrace(e.getStackTrace());  //java v1.4+
         ioe.fillInStackTrace(); //java v1.3
         throw ioe;
      }
   }



   public static void main (String[] args)
   {
      try
      {
         MySpaceSunFtpClient pub = new MySpaceSunFtpClient("grendel12.roe.ac.uk");
         pub.setLogin("astrogrid","astrogrid");

         pub.connect();

         String url = pub.publicise("1a","/home/mch/r229192_red.fits");
         org.astrogrid.log.Log.trace("File available at '"+url+"'");
         pub.update(url, "/home/mch/tmp/");

         pub.disconnect();

         org.astrogrid.log.Log.trace("...disconnected");
      }
      catch (IOException ioe)
      {
         org.astrogrid.log.Log.logError("",ioe);
      }
   }

}
