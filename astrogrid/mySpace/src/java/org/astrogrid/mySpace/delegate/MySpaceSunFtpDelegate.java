/*
 $Id: MySpaceSunFtpDelegate.java,v 1.1 2003/12/03 17:26:00 mch Exp $

 (c) Copyright...
 */

package org.astrogrid.mySpace.delegate;
import org.astrogrid.mySpace.delegate.*;

import java.io.*;

import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import org.astrogrid.datacenter.io.Piper;
import org.astrogrid.mySpace.delegate.MySpaceManagerDelegate;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

/**
 * An implenentation of myspace using an ordinary FTP server.
 * <P>
 * Developed using the built-in ftp clients supplied with java - but note
 * that these are NOT supported directly.
 * <P>
 * Adapted from the ACE project
 */

public class MySpaceSunFtpDelegate implements MySpaceClient
{
   private String server = null;
   private int port = DEFAULT_PORT;
   private String rootDir = ""; //root directory for this user

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
   public MySpaceSunFtpDelegate(String givenEndPoint) throws IOException
   {
      URL url = new URL(givenEndPoint);
      server = url.getHost();
      port = url.getPort();
      if (port == -1) { port = DEFAULT_PORT; }
      rootDir = url.getPath();
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
    * Copies given file to server; subdir specifies the subdirectory the
    * file will be placed in.  That subdirectory will be created if it does
    * not already exist.
    * Returns the URL of the file on the server
    */
   public String publiciseToServer(String subdir, String localFilename) throws IOException
   {
      org.astrogrid.log.Log.affirm(ftpConnection != null, "Trying to publish before connecting. Call connect() before publicise(...)");

      File localFile = new File(localFilename);
      String filename = localFile.getName(); // the name without the path

      org.astrogrid.log.Log.trace("Publicise: Copying '"+localFilename+"' to '"+server+"'...");

      try
      {
         //         ftpConnection.endir(uniqueId);   //ensure directory exists
         ftpConnection.cd(subdir);
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
      
         String remoteUrl = ftpConnection.getUrl(ftpConnection.currentDir(), filename);

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
    * Publishes the given stream to the server.
    * Returns the URL of the file on the server
    */
   public String publiciseToServer(String remoteFilename, InputStream source) throws IOException
   {
      assert ftpConnection != null : "Trying to publish before connecting. Call connect() before publicise(...)";

      org.astrogrid.log.Log.trace("Publicise: Copying '"+source+"' to '"+server+"' as '"+remoteFilename+"'...");

      OutputStream remoteOut = ftpConnection.put(remoteFilename);
      Piper.bufferedPipe(source, remoteOut);
         
      return ftpConnection.getUrl(remoteFilename);
   }
   
   /**
    * Publishes the given local file to the server (with the same name)
    * Returns the URL of the file on the server
    */
   public String publiciseToServer(String filename) throws IOException
   {
      return publiciseToServer(filename, new FileInputStream(filename));
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
    * Create a new user on the current MSS.
    *
    * @param userId User identifier.
    * @param servers Vector of server names on which containers will be
    *   created for the user.
    *
    * @return boolean; true the user was created successfully.
    * @throws Exception
    */
   public boolean createUser(String userId, String communityId, String credential, Vector servers) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Retrieve a copy of a dataHolder on a remote MSS and save it with a
    * specified MySpace name on the current MSS.
    *
    * @param userId User identifier.
    * @param communityId Community identifier.
    * @param remoteMssUrl URL of the remote MSS.
    * @param remoteMySpaceName MySpace name of the dataHolder to be
    *   retrieved from the remote MSS.
    * @param newMySpaceName MySpace name for the copy of the dataHolder on
    *   the current MSS.
    *
    * @return boolean; true if the copy succeeded.
    * @throws Exception
    */
   public boolean copyRemoteDataHolding(String userId, String communityId, String credential, String remoteMssUrl, String remoteMySpaceName, String newMySpaceName) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Retrieve the contents of a dataHolder and supply them as the String
    * returned by the method.
    *
    * @param userId User identifier.
    * @param communityId community identifier.
    * @param  mySpaceName MySpace name of the dataHolder whose contents are
    *   to be retrieved.
    *
    * @return: A String containing the contents of the specified dataHolder.
    */
   public String getDataHolding(String userId, String communityId, String credential, String mySpaceName) throws Exception {

      InputStream serverIn = ftpConnection.get(mySpaceName);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Piper.bufferedPipe(serverIn, out); //copy from remote url to byte array
      serverIn.close();
      out.close();
      
      return out.toString();
   }
   
   /**
    * Delete a user from the current MSS.
    *
    * @param userId User identifier.
    *
    * @return boolean; true is the user was deleted successfully.
    * @throws Exception
    */
   public boolean deleteUser(String userId, String communityId, String credential) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    * No idea what this is supposed to do
    * @param userId
    * @param communityId
    * @param serverFileName: full file name eg: /clq/serv1/File1.xml
    * @return
    * @throws Exception
    */
   public String listDataHolding(String userId, String communityId, String credential, String serverFileName) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    *
    * @param dataHolderName: file working on
    * @param newOwnerID: userId changing to
    * @return
    * @throws Exception
    */
   public String changeOwner(String userId, String communityId, String credential, String dataHolderName, String newOwnerID) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    *
    * @param userId
    * @param communityId
    * @param serverFileName: Full file name which you want to delete
    * @return
    * @throws Exception
    */
   public String deleteDataHolding(String userId, String communityId, String credential, String serverFileName) throws Exception {
      ftpConnection.delete(serverFileName);
      return null; //?
   }
   
   /**
    * Search a list of MSSs and return the MySpace names of entries which
    * match the query.
    *
    * @param userId User identifier.
    * @param communityId Community identifier.
    * @param query Query which MySpace names should match, eg:
    *    /userid@communityid/server/workflows/A*
    *
    * @return A list of MySpace names which match the query.  Note that
    * short (and incomplete) MySpace names are returned, with the details
    * of their enclosing containers removed.  A Vector is returned, each
    * element of which corresponds to one of the MSS searched.  Each of
    * these elements is itself a Vector, whose elements are Strings
    * containing the MySpace names which matched the query.
    */
   public Vector listDataHoldings(String userId, String communityId, String credential, String query) throws Exception {
      //oh ye gods.  OK I shall assume only one FTP server is searched (ie this one) and just return a list of names
      BufferedReader reader = new BufferedReader(new InputStreamReader(ftpConnection.nameList(query)));
   
      Vector files = new Vector();
      
      String line = "";
      while (line != null)
      {
         line = reader.readLine();
         if (line != null) files.add(line);
      }
      
      Vector servers = new Vector();
      servers.add(files);

      return servers;
   }
   
   /**
    * Copies a file on the server.
    */
   public String copyDataHolding(String userId, String communityId, String credential, String serverFileName, String newDataItemName) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Does nothing on a FTP server
    */
   public String extendLease(String userId, String communityId, String credential, String serverFileName, int extentionPeriod) throws Exception {
      return null;
   }
   
   /**
    * Saves the contents of the file at the given URL to the given filename
    * <P>At the moment the data is routed through this machine... is there a way of doing this remotely?
    */
   public boolean saveDataHoldingURL(String userId, String communityId, String credential, String fileName, String importURL, String category, String action) throws Exception {

      URL sourceUrl = new URL(importURL);
      InputStream in = sourceUrl.openStream();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Piper.bufferedPipe(in, out);
      in.close();
      out.close();
      
      return saveDataHolding(userId, communityId, credential, fileName, out.toString(), "", OVERWRITE);
   }
   
   /**
    * Search a list of MSSs and return the details of all the MySpace
    * entries which match the query.  The details for each entry are
    * a string containing XML.
    *
    * @param userId User identifier.
    * @param communityId Community identifier.
    * @param query Query which MySpace names should match, eg:
    *    /userid@communityid/server/workflows/A*
    *
    * @return A list of MySpace entries which match the query.  For each
    * matching entry an XML string containing the details is returned.
    * A Vector is returned, each element of which corresponds to one of
    * the MSS searched.  Each of these elements is itself a Vector,
    * whose elements are Strings containing the details of the entry
    *which matched the query.
    */
   public Vector listDataHoldingsGen(String userId, String communityId, String credential, String query) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    *
    * @param userId
    * @param communityId
    * @param newContainerName
    * @return
    */
   public String createContainer(String userId, String communityId, String credential, String newContainerName) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Return the URL of a dataHolder.  NB - assumes this is in the 'current directory'
    */
   public String getDataHoldingUrl(String userId, String communityId, String credential, String mySpaceName) throws Exception {

      return ftpConnection.getUrl(mySpaceName);
   }
   
   /** Creates/Appends a file on the server with the given filename, with
    * contents as given in the given string
    *
    * @param: fileContent: content of workflow or data query
    * @param: category "WF" or "QUERY", if not set, default is "VOTable"
    * @param: action "Overwrite" or "Append", if not set, default is "Overwrite"
    * @return: boolean true if file successfully stored in MySapce false otherwise.
    */
   public boolean saveDataHolding(String userId, String communityId, String credential, String fileName, String fileContent, String category, String action) throws Exception {
      
      OutputStream out = null;
      if (action.equals(APPEND)) {
         out = ftpConnection.append(fileName);
      } else if (action.equals(OVERWRITE)) {
         out = ftpConnection.put(fileName);
      } else {
         throw new IllegalArgumentException("Unknown action "+action);
      }
      
      InputStream in = new StringBufferInputStream(fileContent);
      Piper.pipe(in, out);
      in.close();
      out.close();
      return true;
   }
   
   /**
    *
    * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userId/communityId/jobID/serverFileName
    * @return
    * @throws Exception
    */
   public String publish(String userId, String communityId, String credential, String mySpaceName) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    *
    * @param userId
    * @param communityId
    * @param serverFileName: ole file full name
    * @param newDataItemName: new file full name
    * @return
    * @throws Exception
    */
   public String renameDataHolding(String userId, String communityId, String credential, String serverFileName, String newDataItemName) throws Exception {
      throw new UnsupportedOperationException();
   }
   
}
