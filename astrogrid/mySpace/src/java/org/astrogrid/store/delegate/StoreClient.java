/*
 * $Id: StoreClient.java,v 1.1 2004/02/24 15:59:56 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.astrogrid.community.Account;

/**
 * These are the methods that vospace delegates must implement.
 * <p>
 * VoSpace delegates are those that give a standard access to the various
 * stores on the vo net.  Amongst there are the AstroGrid MySpace services.
 *
 * Using an interface as a common reference point (that all VoSpace delegates
 * must implement) means if the MySpaceManagerDelegate interface changes, this
 * should be changed as well, and so will all delegates that implement it.  Thus we
 * can spot & fix such changes at build time.
 *
 * Note that there is a difference between the user (termed *operator* here) of
 * this class, and the owner of the files that might be being browsed
 */

public interface StoreClient {
   
   /**
    * Returns the user of this delegate - ie the account it is being used by
    */
   public Account getOperator();
   
   /**
    * Returns a tree representation of the files that match the expression
    */
   public File getEntries(Account forAccount, String filter) throws IOException;

   /**
    * Returns a list of all the files that match the expression
    *
   public List getEntriesList(User forAccount, String filter);
    /**/
   /**
    * getEntry obtain the details of a single, specified entry,
    *
   public File getEntry(User forAccount, String path);
    /**/

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
   public void copy(String sourcePath, String targetPath) throws IOException;
   
   /**
    * Create a container
    */
   public void newFolder(String targetPath) throws IOException ;

   /**
    * Copy a file
    *
   public void copy(User sourceAccount, String sourcePath, User targetAccount, String targetPath);

   /**
    * Move a file
    *
   public void move(User sourceAccount, String sourcePath, User targetAccount, String targetPath);

   /**
    * Explicit instructions to move an entry to a particular MySpace server
    *
   public void moveToServer(User forAccount, String sourcePath, String targetServer);

   /**
    * Create a new user
    *
   public void createUser(User newUser);

   /**
    * Delete a user
    *
   public void deleteUser(User delUser);
   
    /**/
   
}

/*

 */

