/*
 * $Id: LocalFileStore.java,v 1.3 2004/09/06 11:29:24 mch Exp $
 *
 */

package org.astrogrid.store.delegate.local;
import org.astrogrid.store.delegate.*;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.User;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Agsl;

/**
 * This is ClientStore implementation for reaching local files directly.
 * Because of this, it can be used to test applications
 * against myspace delegates but without actually needing access to a myspace
 * service. It basically stores files locally, with a root directory given in the
 * configuration, or off the working directory if none is given.
 *
 * @author: M Hill
 */

public class LocalFileStore extends StoreDelegate implements StoreAdminClient {
   
   
   
   /** The root directory for this file store instance */
   private File rootDir = null;

   static private Log log = LogFactory.getLog(LocalFileStore.class);

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
    * Creates a StoreClient that can be used to list, copy, delete, etc files
    * on disk.  The root is determined from the configuration root,
    * or creates one off the system-dependent
    * temporary directory (as the owner will always be able to create one there
    * even if they can't in the working directory)
    */
   public LocalFileStore() throws IOException {

      super(User.ANONYMOUS);
      
      String root = SimpleConfig.getSingleton().getString("LocalFileStoreRoot", null);
      
      if (root == null) {
         //find the temporary file area as assigned by the operating system
         rootDir = File.createTempFile("LocalFileStore","Root" ); //create file in temporary area
         rootDir.delete();    //don't need the file

         //create directory LocalFileStore off temporary area.  NB we could use
         //the created file above as a directory name, but that means that two
         //created file stores would not overlap.
         rootDir = new File(rootDir.getParentFile(), "LocalFileStore");
         if (rootDir.exists() && (!rootDir.isDirectory())) {
            boolean success = rootDir.delete();
            if (!success) {
               throw new IOException("LocalFileStore ["+rootDir+"] is not a directory and yet cannot be deleted to make room for the local store");
            }
         }
         
         if (!rootDir.exists()) {
            rootDir.mkdir();
         }
      }
   }
   
   /** As the empty constructor, but creates a subdirectory off the normal
    * root with the name given.  This allows us to create several local stores
    * in the local filespace
    */
   public LocalFileStore(String name) throws IOException {
      this();
      
      rootDir = new File(rootDir, name);
      if (!rootDir.exists()) {
         rootDir.mkdir();
      }
      log.debug("LocalFileStore["+name+"] created at "+rootDir);
   }

   /** As the named constructor, but uses the given Agsl that defines the name
    * Could do with better checking that the agsl is correct for this type...
    * @deprecated - you should be using StoreDelegateFactory anyway... */
   public LocalFileStore(Agsl agsl) throws IOException {
      this(agsl.getEndpoint().substring(7)); //chop off 'file://'
   }

   /** Creates a local file store with the given file as the root
    */
   public LocalFileStore(File givenRoot)  {
      super(User.ANONYMOUS);

      this.rootDir = givenRoot;
   }
   
   /**
    * Returns the endpoint
    */
   public Agsl getEndpoint() {
      String s = Agsl.SCHEME+":file://"+rootDir.getName();
      try {
         return new Agsl(s);
      } catch (MalformedURLException mue) {
         //shouldn't happen as this is a generated string...
         throw new RuntimeException("Program error: generating bad url '"+s+"'",mue);
      }
   }

   /**
    * Puts the given string into the given location
    * now done via putBytes()
   public void putString(String contents, String targetPath, boolean append) throws IOException {

      //remove colons, spaces and slashes from filename
      File target = makeLocalPath(targetPath);

      DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(target, append)));
      out.writeBytes(contents);
      out.close();
   }
   
   /**
    * Puts the given bytes into the given location
    */
   public void putBytes(byte[] bytes, int offset, int length, String targetPath, boolean append) throws IOException {

      //remove colons, spaces and slashes from filename
      File target = makeLocalPath(targetPath);

      DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(target, append)));
      out.write(bytes, offset, length);
      out.close();
         
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
      
      if (!filter.equals("*")) {
         //@todo - borrow from somwhere
         throw new UnsupportedOperationException("Can only filter * for now");
      }
      return new LocalFile(this, rootDir);
   }

   /**
    * Returns a list of the children of the given folder */
   public StoreFile[] getChildren(StoreFile folder, String filter) {

      if (!filter.equals("*")) {
         //@todo - borrow from somwhere
         throw new UnsupportedOperationException("Can only filter * for now");
      }

      return new LocalFile(this, new File(folder.getPath())).listFiles();
   }

   /**
    * Returns the parent of the given file
    */
   public StoreFile getParent(StoreFile child) {
      return new LocalFile(this, new File(child.getPath()).getParentFile());
   }
   
   /**
    * Returns the full path, within the context of this server, to the given
    * file, including the filename
    */
   public String getServerPath(File file) throws StoreException
   {
      if (file.equals(rootDir)) {
         return null; //not a path
      }
      
      String path = "";
      //navigate up the file until we reach the root
      while ((file != null) && (!file.equals(rootDir))) {
         path = "/"+file.getName()+path;
         file = file.getParentFile();
      }

      //check it's all OK
      if (file == null) {
         throw new StoreException("File "+file+" not in local file store "+this);
      }

      return path.substring(1); //chop off initial slash
   }
   
   /**
    * Returns the file representation of that at the given server path */
   public StoreFile getFile(String path) {

      File f = makeLocalPath(path);
      if (!f.exists()) {
         return null;
      } else {
         return new LocalFile(this, f);
      }
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
   public OutputStream putStream(String targetPath, boolean append) throws IOException {

      File target = makeLocalPath(targetPath);
      
      return new FileOutputStream(target, append);
   }
   
   /**
    * Delete a file
    */
   public void delete(String deletePath) throws IOException {
      File fileToDel = makeLocalPath(deletePath);
      boolean success = fileToDel.delete();
      if (!success) {
         throw new IOException("'"+this+"' failed to delete '"+deletePath+"' (don't know why, possibly file open/locked)");
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
    * done in StoreDelegate
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
   public void copy(String sourcePath, Agsl target) throws IOException {

      InputStream in = new FileInputStream(makeLocalPath(sourcePath));
      OutputStream out = null;
      
      if (target.getEndpoint().equals("file://"+rootDir.getName())) {
         //same store so make a file
         out = new FileOutputStream(makeLocalPath(target.getPath()), false);
      }
      else {
         StoreClient targetStore = StoreDelegateFactory.createDelegate(getOperator(), target);
         out = targetStore.putStream(target.getPath(), false);
      }
      
      //transfer
      Piper.bufferedPipe(in, out);
      in.close();
      out.close();
      
   }
   

   /**
    * Takes the server path and returns the  representation on the local
    * filesystem
    */
   protected File makeLocalPath(String path)
   {
      if (path != null) {
         //remove colons, spaces and slashes from filename
         path = path.replace(':','_');
         path = path.replaceAll("\\\\","_");
         path = path.replace(' ','_');
         path = path.replace('?','_');
         path = path.replace('&','_');
         path = path.replace('=','_');
      }
      return new File(rootDir, path);
   }


   /** USer friendly representation of this store */
   public String toString() {
      return "LocalFileStore ["+rootDir.getName()+"]";
   }
   
   /**
    * Delete a user - this does nothing on a local file store at the moment,
    * as I'm a bit iffy about deleting directory structures that might have
    * valuable data in it for now.
    */
   public void deleteUser(User delUser) {
      log.warn("User "+delUser+" filespace not deleted");
   }
   
   /**
    * Create a new user - creates a directory 'individual@community'
    */
   public void createUser(User newUser) throws IOException {
      newFolder(newUser.getUserId()+"@"+newUser.getCommunity());
   }
   
   
}

/*
$Log: LocalFileStore.java,v $
Revision 1.3  2004/09/06 11:29:24  mch
Added comments

Revision 1.2  2004/06/14 23:08:53  jdt
Merge from branches

ClientServerSplit_JDT

and

MySpaceClientServerSplit_JDT



MySpace now split into a client/delegate jar

astrogrid-myspace-<version>.jar

and a server/manager war

astrogrid-myspace-server-<version>.war

Revision 1.1.2.1  2004/06/14 22:33:21  jdt
Split into delegate jar and server war.
Delegate: astrogrid-myspace-SNAPSHOT.jar
Server/Manager: astrogrid-myspace-server-SNAPSHOT.war

Package names unchanged.
If you regenerate the axis java/wsdd/wsdl files etc you'll need
to move some files around to ensure they end up in the client
or the server as appropriate.
As of this check-in the tests/errors/failures is 162/1/22 which
matches that before the split.

Revision 1.9  2004/05/03 08:55:53  mch
Fixes to getFiles(), introduced getSize(), getOwner() etc to StoreFile

Revision 1.8  2004/04/23 11:38:19  mch
Fixes to return correct AGSL plus change to File model for It05 delegate

Revision 1.7  2004/04/06 15:40:46  mch
Minor change to javadoc

Revision 1.6  2004/03/22 10:25:42  mch
Added VoSpaceClient, StoreDelegate, some minor changes to StoreClient interface

Revision 1.5  2004/03/19 12:39:37  mch
Added StoreAdminClient implementation to LocalFileStore

Revision 1.4  2004/03/17 15:17:29  mch
Added putBytes

Revision 1.3  2004/03/14 13:30:08  mch
Fix for unix rootDir not created and so not being deleted

Revision 1.2  2004/03/13 22:58:43  mch
Fixed nul pointer exception when path is empty

Revision 1.1  2004/03/04 12:51:31  mch
Moved delegate implementations into subpackages

Revision 1.4  2004/03/02 11:53:35  mch
Fixes to copy and move tests

Revision 1.3  2004/03/02 00:15:39  mch
Renamed MyspaceIt04Delegate from misleading ServerDelegate

Revision 1.2  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

Revision 1.1  2004/03/01 15:15:04  mch
Updates to Store delegates after myspace meeting

 */

