/*
   MySpaceClient.java

   Date         Author      Changes
   11 Nov 2002  M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.myspace;

import java.io.*;
import java.net.*;

/**
 * Defines the actions a my space client must carry out - ie be able to
 * publish files to the myspace server, and retrieve them.  later we'll look
 * at pipes and 3rd party connections.
 */

public interface MySpaceClient
{

   /**
    * Connect to server, get authorised and be ready to move files
    */
   public void connect() throws IOException;

   /**
    * Disconnects from server, translating all faults into IOExceptions
    */
   public void disconnect() throws IOException;

   /**
    * Copies given file to server; unique Id specifies the subdirectory the
    * file will be placed in.
    * Returns the URL of the file on the server
    */
   public String publicise(String uniqueId, String localFilename) throws IOException;

   /**
    * Gets the file identified by the remote string (this might be a url)
    * and copy it to the local directory.  If possible, it should only do this
    * if required - ie if poss it should do some sort of time/date or
    * checksum comparison.  Returns the full local path.
    */
   public String update(String remoteUrl, String localDir) throws IOException;

}
