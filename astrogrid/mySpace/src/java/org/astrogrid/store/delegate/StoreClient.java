/*
 * $Id: StoreClient.java,v 1.4 2004/03/01 22:38:46 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;

/**
 * These are the methods that delegates to storage mechanisms used by Astrogrid must implement.
 * <p>
 * StoreClients are those that give a standard access to the various
 * stores on the net.  Amongst there are the AstroGrid MySpace services, but also
 * a local filestore (eg for intranets), ftp and one day grid ftp delegates.
 *
 * This interface defines run-of-the-mill file operations; use StoreAdminClient
 * for store administration tasks such as adding & removing users
 *
 * Note that there is a difference between the user (termed *operator* here) of
 * this class, and the owner of the files that might be being browsed.
 */

public interface StoreClient {
   
   /**
    * Returns the user of this delegate - ie the account it is being used by
    */
   public User getOperator();
   
   /**
    * Returns the Agsl to the service this client is connected to
    */
   public Agsl getEndpoint();
   
   /**
    * Returns a tree representation of the files that match the expression
    */
   public StoreFile getFiles(String filter) throws IOException;

   /**
    * Returns a list of all the files that match the expression
    */
   public StoreFile[] listFiles(String filter) throws IOException;

   /**
    * Returns the StoreFile representation of the file at the given AGSL
    */
   public StoreFile getFile(String path) throws IOException;

   /**
    * Puts the given string into the given location
    */
   public void putString(String contents, String targetPath, boolean append) throws IOException;

   /**
    * Copies the contents of the file at the given source url to the given location
    */
   public void putUrl(URL source, String targetPath, boolean append) throws IOException;

   /**
    * Streaming output - returns a stream that can be used to output to the given
    * location
    */
   public OutputStream putStream(String targetPath) throws IOException;

   /**
    * Gets a file's contents as a stream
    */
   public InputStream getStream(String sourcePath) throws IOException;
   
   /**
    * Gets the url to stream
    */
   public URL getUrl(String sourcePath) throws IOException;

   /**
    * Delete a file
    */
   public void delete(String deletePath) throws IOException;

   /**
    * Copy a file
    */
   public void copy(String sourcePath, Agsl targetPath) throws IOException;
   
   /**
    * Moves/Renames a file
    */
   public void move(String sourcePath, Agsl targetPath) throws IOException;
   
   /**
    * Create a container
    */
   public void newFolder(String targetPath) throws IOException ;
}

/*
$Log: StoreClient.java,v $
Revision 1.4  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

 */

