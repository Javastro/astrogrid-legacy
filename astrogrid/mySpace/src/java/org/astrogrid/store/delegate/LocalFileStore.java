/*
 * $Id: LocalFileStore.java,v 1.1 2004/03/01 15:15:04 mch Exp $
 *
 */

package org.astrogrid.store.delegate;

import java.io.*;

import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.io.Piper;

/**
 * This is ClientStore implementation for reaching local files directly.
 * Because of this, it can be used to test applications
 * against myspace delegates but without actually needing access to a myspace
 * service. It basically stores files locally, with a root directory given in the
 * configuration, or off the working directory if none is given.
 *
 * @author: M Hill
 */

public class LocalFileStore implements StoreClient
{
   /** The root directory for this file store instance */
   private File rootDir = null;

   static private Log log = LogFactory.getLog(LocalFileStore.class);

   public static final String DUMMY = "http://Dummy.address/"; //use this URL to ask for a dummy delegate

   /**
    * Class for filtering filenames based on the given criteria
    */
   private class CriteriaFilenameFilter implements FilenameFilter
   {
      String criteria = null;

      public CriteriaFilenameFilter(String givenCriteria)
      {
         this.criteria = givenCriteria;
      }

      public boolean accept(File dir, String name)
      {
         if (criteria.equals("*"))
         {
            return true;
         }
         else
         {
            throw new UnsupportedOperationException();
         }
      }
   }

   /**
    * Sets up the root for this instance determined from the configuration root,
    * or creates one off the system-dependent
    * temporary directory (as the owner will always be able to create one there
    * even if they can't in the working directory
    */
   public LocalFileStore() throws IOException {
      
      String root = SimpleConfig.getProperty("LocalFileStoreRoot", null);
      
      if (root == null) {
         //create file in temporary area, and
         rootDir = File.createTempFile("LocalFileStore","Root" );
         // change to be a directory here.
         rootDir.delete();
         rootDir.mkdir();
      }
   }
   
   /** As the empty constructor, but uses the given creates a subdirectory off the normal
    * root with the name given.  This allows us to create several local stores
    * in the local filespace
    */
   public LocalFileStore(String name) throws IOException {
      this();
      
      rootDir = new File(rootDir, name);
      rootDir.mkdir();
   }

   /** Creates a local file store with the given file as the root
    */
   public LocalFileStore(File givenRoot) {

      this.rootDir = givenRoot;
   }
   
   /**
    * Puts the given string into the given location
    */
   public void putString(String contents, String targetPath, boolean append) throws IOException {

      //remove colons, spaces and slashes from filename
      File target = makeLocalPath(targetPath);

      DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(target, append)));
      out.writeChars(contents);
      out.close();
   }
   
   /**
    * Returns the user of this delegate - ie the account it is being used by
    */
   public Account getOperator() {
      return Account.ANONYMOUS;
   }
   
   /**
    * Gets the url to stream.  Note that in this case, it can only return a file
    * url - the localfilestore does not know if this file is also published via
    * eg http.
    */
   public URL getUrl(String sourcePath) throws IOException {
      return makeLocalPath(sourcePath).toURL();
   }
   
   /**
    * Returns a tree representation of the files that match the expression.
    */
   public StoreFile getFiles(String filter) throws IOException {

      throw new UnsupportedOperationException();
   }

   /**
    * Returns a list of the files in the given directory */
   public StoreFile[] listFiles(String filter) {

      File dir = makeLocalPath(filter);
      
      File[] children =  dir.listFiles(new CriteriaFilenameFilter(filter));
      
      throw new UnsupportedOperationException();
   }
   
   /**
    * Returns the file representation of that at the given path */
   public StoreFile getFile(String path) {

      throw new UnsupportedOperationException();
      /**
      File dir = makeLocalPath(searchPath);

      new File
      
      File[] matchedFiles = dir.listFiles(new CriteriaFilenameFilter(filter));
      
      dir.
      
      for (int i=0;i<matchedFiles.length;i++) {
       **/
   }

   /**
    * Create a container
    */
   public void newFolder(String targetPath) throws IOException {
      File newFolder = new File(rootDir, targetPath);
      newFolder.mkdir();
   }
   
   /**
    * Streaming output - returns a stream that can be used to output to the given
    * location
    */
   public OutputStream putStream(String targetPath) throws IOException {

      File target = makeLocalPath(targetPath);
      
      return new FileOutputStream(target);
   }
   
   /**
    * Delete a file
    */
   public void delete(String deletePath) throws IOException {
      File fileToDel = makeLocalPath(deletePath);
      boolean success = fileToDel.delete();
      if (!success) {
         throw new IOException("Failed to delete "+deletePath);
      }
   }
   
   /**
    * Gets a file's contents as a stream
    */
   public InputStream getStream(String sourcePath) throws IOException {
      File source = makeLocalPath(sourcePath);
      
      return new FileInputStream(source);
   }
   
   /**
    * Copies the contents of the file at the given source url to the given location
    */
   public void putUrl(URL source, String targetPath, boolean append) throws IOException {

      File target = makeLocalPath(targetPath);

      InputStream in = source.openStream();
      OutputStream out = new FileOutputStream(target);

      Piper.bufferedPipe(in, out);

      in.close();
      out.close();
   }
   
   /**
    * Copy a file
    */
   public void copy(String sourcePath, String targetPath) throws IOException {
      File source = makeLocalPath(sourcePath);
      File target = makeLocalPath(targetPath);

      InputStream in = new FileInputStream(source);
      OutputStream out = new FileOutputStream(target);

      Piper.bufferedPipe(in, out);

      in.close();
      out.close();
   }
   

   /**
    * Takes the myspace path and returns the representation on the local
    * filesystem
    */
   protected File makeLocalPath(String path)
   {
      //remove colons, spaces and slashes from filename
      path = path.replace(':','_');
      path = path.replaceAll("\\\\","_");
      path = path.replace(' ','_');
      path = path.replace('?','_');
      path = path.replace('&','_');
      path = path.replace('=','_');
      
      return new File(rootDir, path);
   }
   
   
}

/*
$Log: LocalFileStore.java,v $
Revision 1.1  2004/03/01 15:15:04  mch
Updates to Store delegates after myspace meeting

 */

