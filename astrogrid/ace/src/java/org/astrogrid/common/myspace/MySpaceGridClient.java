/*
   $Id: MySpaceGridClient.java,v 1.1 2003/08/25 18:36:21 mch Exp $

   Date         Author      Changes
   1 Nov 2002   M Hill      Created

   (c) Copyright...
*/
package org.astrogrid.common.myspace;

import java.io.*;
import java.net.*;
import org.globus.ftp.*;
import org.globus.ftp.exception.*;
import org.globus.security.*;

/**
 * A class that takes an image (or any other file) and copies it to some
 * public location where the grid service can find it.
 *
 * Currently it does this using gridftp.
 *
 * One instance 'wraps' one public service.
 */

public class MySpaceGridClient implements MySpaceClient
{
   String server = null;
   int port = DEFAULT_PORT;
   
   GridFTPClient ftpConnection = null;
   
   private final static int DEFAULT_PORT = 8080;
   
   public MySpaceGridClient(String serverToPublishTo) throws IOException
   {
      this(serverToPublishTo, DEFAULT_PORT);
   }

   public MySpaceGridClient(String gridServer, int gridPort) throws IOException
   {
      this.server = gridServer;
      this.port = gridPort;
   }

   /**
    * Connect to server and get authorised
    */
   public void connect() throws IOException
   {
      try
      {
         ftpConnection = new GridFTPClient(server, port);
         ftpConnection.authenticate(GlobusProxy.getDefaultUserProxy());
         ftpConnection.setProtectionBufferSize(16384);
         ftpConnection.setDataChannelAuthentication(DataChannelAuthentication.NONE);
         ftpConnection.setDataChannelProtection(GridFTPSession.PROTECTION_SAFE);
      }
      catch (ServerException se)
      {
         ftpConnection = null;
         throw new IOException("Could not reach server '"+server+"' port "+port+": "+se);
      }
      catch (GlobusProxyException gpe)
      {
         ftpConnection = null;
         throw new IOException("Could not create default user proxy: "+gpe);
      }
      
   }

   public void disconnect() throws IOException
   {
      try
      {
         ftpConnection.close();
      }
      catch (ServerException se)
      {
         throw new IOException("Exception closing "+this+": "+se);
      }
      finally
      {
         ftpConnection = null;
      }
   }

   public String toString()
   {
      return "FTP "+server+":"+port;
   }
   
   /**
    * Copies given file to server
    */
   public String publicise(String localId, String localFile) throws IOException
   {
      DataSource source = new DataSourceStream(new FileInputStream(localFile));
      
      try
      {
         ftpConnection.put("testFile", source, null);
      }
      catch (Exception e)
      {
         throw new IOException("Exception sending '"+localFile+"' to "+this+": "+e);
      }
      
      throw new UnsupportedOperationException("Yet to do");
   }


   /**
    * Gets the file identified by the remote string (this might be a url)
    * and copy it to the local filename.  If possible, it should only do this
    * if required - ie if poss it should do some sort of time/date or
    * checksum comparison.  Returns full path to local file
    */
   public String update(String remoteUrl, String localDir) throws IOException
   {
      throw new UnsupportedOperationException("Not done yet");
   }
   
   
   
   /**
    * Test
    */
   public static void main (String[] args) throws Exception
   {
      MySpaceGridClient pub = new MySpaceGridClient("grendel12");
      
      pub.publicise("mch","test.xml");
   }
   
}
