/*
 $Id: FtpDelegate.java,v 1.1 2004/02/24 20:01:36 mch Exp $

 (c) Copyright...
 */

package org.astrogrid.store.delegate;

import java.io.*;

import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import org.astrogrid.io.Piper;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;
import org.astrogrid.community.Account;

/**
 * An implenentation of myspace using an ordinary FTP server.
 * <P>
 * Developed using the built-in ftp clients supplied with java - but note
 * that these are NOT supported directly.
 * <P>
 * Adapted from the ACE project
 */

public class FtpDelegate implements StoreClient
{
   private String server = null;
   private int port = DEFAULT_PORT;
   private String rootDir = ""; //root directory for this user

   private Account operator = Account.ANONYMOUS;

   private FullFtpClient ftpConnection = null;

   private final static int DEFAULT_PORT = 21;

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

      /**
       * Returns the URL of the given file in the given directory
       */
      public String getUrl(String directory, String filename) throws IOException
      {
         return "ftp://"+server+":"+port+directory+"/"+filename;
      }
   
      /**
       * Returns the URL of the given file in the current directory
       */
      public String getUrl(String filename) throws IOException
      {
         //work out where it is
         return "ftp://"+server+":"+port+currentDir()+"/"+filename;
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

      /** Move to given path.  Path may include filename, so should end up in
       * that directory */
      public void cdPath(String targetPath) {
         throw new UnsupportedOperationException();
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
    * Construct myspace client using given endpoint, which is a complete location, eg
    * ftp://ftp.roe.ac.uk/pub/astrogrid
    */
   public FtpDelegate(String givenEndPoint) throws IOException
   {
      URL url = new URL(givenEndPoint);
      server = url.getHost();
      port = url.getPort();
      if (port == -1) { port = DEFAULT_PORT; }
      rootDir = url.getPath();
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
         ftpConnection.login(operator.getIndividual(), operator.getAstrogridId());
         ftpConnection.binary();
         // ftpConnection.endir("myspace");  //ensure directory myspace exists
         ftpConnection.cd(rootDir);
         org.astrogrid.log.Log.trace("...logged on to "+ftpConnection /* not in 1.3 +" running "+ftpConnection.system() **/ +"...");
      }
      catch (IOException ioe)
      {
         IOException nioe = new IOException(ioe+" ("+ftpConnection.getResponseString()+")");
         nioe.setStackTrace(ioe.getStackTrace());
         ftpConnection = null;
         throw nioe;
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

   /** For debug, returns server & port */
   public String toString()
   {
      return this.getClass()+" "+server+":"+port;
   }

   /**
    * Copies given stream to server; subdir specifies the subdirectory the
    * file will be placed in.  That subdirectory will be created if it does
    * not already exist.
    * Returns the URL of the file on the server
    */
   public String pushStream(String targetPath, InputStream source, boolean append) throws IOException
   {
      assert ftpConnection != null : "Trying to publish before connecting. Call connect() before publicise(...)";

      org.astrogrid.log.Log.trace("Publicise: Copying '"+source+"' to '"+server+"' as '"+targetPath+"'...");

      ftpConnection.cdPath(targetPath);
      String filename = new File(targetPath).getName();

      OutputStream remoteOut;
      if (append) {
         remoteOut = ftpConnection.append(filename);
      } else {
         remoteOut = ftpConnection.put(filename);
      }

      Piper.bufferedPipe(source, remoteOut);
      remoteOut.close();
      source.close();
      
      return ftpConnection.getUrl(ftpConnection.currentDir(), filename);
   }

   /**
    * Publishes the given local file to the server (with the same name)
    * Returns the URL of the file on the server
    */
   public String pushFile(String filename) throws IOException
   {
      return pushStream(filename, new FileInputStream(filename), false);
   }

   /**
    * Gets the file identified by the remote string (this might be a url)
    * and copy it to the local filename.  If possible, it should only do this
    * if required - ie if poss it should do some sort of time/date or
    * checksum comparison.  Returns full path to local file
    */
   public String updateFromServer(String remoteUrl, String localDir) throws IOException
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

   
   /**
    * Returns a tree representation of the files that match the expression
    */
   public File getEntries(Account forAccount, String filter) throws IOException {
      
      throw new UnsupportedOperationException();
      /*
      //@todo
      //oh ye gods.  OK I shall assume only one FTP server is searched (ie this one) and just return a list of names
      BufferedReader reader = new BufferedReader(new InputStreamReader(ftpConnection.nameList(filter)));
   
      Vector files = new Vector();
      
      String line = "";
      while (line != null)
      {
         line = reader.readLine();
         if (line != null) files.add(line);
      }
      
      Vector servers = new Vector();
      servers.add(files);

      return null;
       */
   }
   
   
   /**
    * Copies the contents of the file at the given source url to the given location
    */
   public void putUrl(URL source, String targetPath, boolean append) throws IOException {
      pushStream(targetPath, source.openStream(), append);
   }
   
   /**
    * Puts the given string into the given location
    */
   public void putString(String contents, String targetPath, boolean append) throws IOException {

      pushStream(targetPath, new StringBufferInputStream(contents), append);
   }
   
   /**
    * Copy a file
    */
   public void copy(String sourcePath, String targetPath) throws IOException {
      // TODO
      throw new UnsupportedOperationException();
   }
   
   /**
    * Streaming output - returns a stream that can be used to output to the given
    * location
    */
   public OutputStream putStream(String targetPath) throws IOException {
      // TODO
      throw new UnsupportedOperationException();
   }
   
   /**
    * Gets a file's contents as a stream
    */
   public InputStream getStream(String sourcePath) throws IOException {
      return getUrl(sourcePath).openStream();
   }
   
   /**
    * Create a container
    */
   public void newFolder(String targetPath) throws IOException {
      // TODO
   }
   
   /**
    * Delete a file
    */
   public void delete(String deletePath) throws IOException {
      ftpConnection.delete(deletePath);
   }
   
   /**
    * Returns the user of this delegate - ie the account it is being used by
    */
   public Account getOperator() {
      return operator;
   }
   
   /**
    * Gets the url to stream
    */
   public URL getUrl(String sourcePath) throws IOException {
      ftpConnection.cdPath(sourcePath);
      return new URL(ftpConnection.getUrl(new File(sourcePath).getName()));
   }
   
}
