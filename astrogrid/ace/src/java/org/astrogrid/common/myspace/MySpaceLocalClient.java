/*
   MySpaceLocalClient.java

   Date         Author      Changes
   11 Nov 2002  M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.myspace;

import java.io.*;
import java.net.*;

/**
 * A dummy 'place holder' implementation of a myspace client for handling
 * local files.  Does nothing.
 */

public class MySpaceLocalClient implements MySpaceClient
{

   /**
    * Connect to server, get authorised and be ready to move files
    */
   public void connect() throws IOException
   {
   }
   
   /**
    * Gets the file identified by the remote string (this might be a url)
    * and copy it to the local filename.  If possible, it should only do this
    * if required - ie if poss it should do some sort of time/date or
    * checksum comparison.  Returns full path to local file
    */
   public String update(String remoteUrl, String localDir) throws IOException
   {
      return remoteUrl;
   }
   
   
   /**
    * Disconnects from server, translating all faults into IOExceptions
    */
   public void disconnect() throws IOException
   {
   }
   
   /**
    * Copies given file to server; unique Id specifies the subdirectory the
    * file will be placed in.
    * Returns the URL of the file on the server
    */
   public String publicise(String uniqueId, String filename) throws IOException
   {
      return filename;
   }
   
   
}
