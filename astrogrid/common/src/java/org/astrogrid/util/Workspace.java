/**
 * $Id: Workspace.java,v 1.7 2004/03/12 16:49:00 mch Exp $
 */

package org.astrogrid.util;

import java.io.File;
import java.io.IOException;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;

/**
 * A temporary filespace manager for
 * service instances.  Actually just a set of helper methods for creating
 * and removing directories in the temporary workspace.  Create one of these
 * to manage one temporary directory - once 'close' is called, it should not
 * be used again.
 * <p>
 * NB the 'close' operation is not threadsafe (ie if other methods are called
 * while the close operation is running, behaviour is undefined....)
 * <p>
 * NB2 - File has a method .createTempFile() that might be better used...
 */

public class Workspace
{
   /** Key to workspace directory specified in configuration file */
   public static final String WORKSPACE_DIRECTORY_KEY = "WorkspaceDirectory";

   /** public marker as to whether workspace should tidy up when closed -
    * for debugging purposes it is sometimes useful to leave temporary files
    * for examination
    */
   public static boolean PERSIST = false;

   /** File representation of this workspace instance */
   private File workspaceFile = null;

   /**
    * Creates a temporary directory using File.createTempFile()
    * method
    */
   public Workspace() throws IOException
   {
      this((String) null);
   }

   /** default workroot, if no other is provided - a directory called <tt>workspaces</tt> in the current directory */
  public static final String DEFAULT_WORKSPACE_DIRECTORY = System.getProperty("user.dir") + System.getProperty("file.separator") + "workspaces";
   
   /**
    * Creates a temporary directory with the name prefixed by the given workspaceId
    * in the workspace directory given in the configuration file
    * @modified nww - altered default behaviour - if no workRoot given, place workspace in <tt><i>working-dir<i>/workspaces</tt>
    * Necessary because otherwise creating a workspace called 'test' will delete all your test classes, etc.
    */
   public Workspace(String workspaceId) throws IOException
   {
      //see if a working root has been specified
      File workRoot = new File (SimpleConfig.getSingleton().getString( WORKSPACE_DIRECTORY_KEY, DEFAULT_WORKSPACE_DIRECTORY  ));
                 
      // check working root exists, create if necessary
      if (!workRoot.exists()) {
         workRoot.mkdirs();
      }
      assert workRoot.exists() :
              "Working root '"+workRoot.getAbsolutePath()
                 +"' given by configuration key '"
                 +WORKSPACE_DIRECTORY_KEY+"' does not exist, and could not be created";
      
      assert workRoot.isDirectory() :
              "Working root '"+workRoot.getAbsolutePath()
                 +"' given by configuration key '"
                 +WORKSPACE_DIRECTORY_KEY+"' is not a directory";
      
      if (workspaceId == null)
      {
         workspaceFile = File.createTempFile("Workspace","", workRoot); //creates a FILE with a unique name
         workspaceFile.delete(); //remove FILE so that DIR gets made below
      }
      else
      {
         workspaceFile = new File(workRoot,workspaceId);
      }
  

      if (workspaceFile.exists())
      {
         throw new IllegalArgumentException("Workspace '"+workspaceId+"' already in use");
      }

      boolean createdOK = workspaceFile.mkdir();
      
      if (!createdOK) {
         throw new IOException("Directory "+workspaceFile+" did not create OK - can't tell why");
      }
         

      if (!PERSIST)
      {
         workspaceFile.deleteOnExit();
      }
      
      LogFactory.getLog(Workspace.class).debug("Workspace created at "+workspaceFile);
   }

   /**
    * Use when you've finished with the space and you want to tidy up.  NB no
    * other operations are possible after this - it is best to leave it to the
    * garbage collector to close() as part of the finalise(), to avoid other
    * unknown references to it trying to use it after close.
    */
   public synchronized void close() throws IOException
   {
      if (isClosed()) { throw new IllegalStateException("Trying to close a closed workspace"); }

      if (!PERSIST) { empty(); }

      //attempt at threadsafetying but haven't thought about it properly - MCH
      File tempFile = workspaceFile;
      workspaceFile = null;

      if (!PERSIST) { tempFile.delete(); }
   }

   /** Returns true if the workspace has been closed down - ie should not
    * be used any longer.  The best way to deal with closure safely is to
    * let the garbage collector do it through the finalize method
    */
   public boolean isClosed()
   {
      return (workspaceFile == null);
   }

   /**
    * Called by teh garbage collector when there are no more references to
    * it - check it's been closed and close if not
    */
   protected void finalize() throws Throwable
   {
      try  {
        close();
      }
      finally
      {
         super.finalize();
      }
   }

   /**
    * Deletes the contents of the workspace
    */
   public void empty() throws IOException
   {
      if (isClosed()) { throw new IllegalStateException("Trying to empty a closed workspace"); }

      emptyDirectory(workspaceFile);
   }

   /**
    * So you need to make a temporary file in the workspace - this returns
    * a reference to it given a filename
    */
   public File makeWorkFile(String filename) throws IOException
   {
      if (isClosed()) { throw new IllegalStateException("Trying to create a new file in a closed workspace"); }

      File file = new File(workspaceFile.getAbsolutePath() + File.separator + filename);
      if (file.createNewFile() == false)
      {
         throw new IOException("File '"+filename+"' already exists");
      }
      return file;
   }

   /**
    * Auto generate temp file
    */
   public File makeTempFile(String prefix, String suffix) throws IOException
   {
      return File.createTempFile(prefix, suffix, workspaceFile);
   }

   /**
    * Auto generate temp file
    */
   public File makeTempFile(String prefix) throws IOException
   {
      return File.createTempFile(prefix, "", workspaceFile);
   }

   /**
    * Auto generate new directory
    */
   public File makeTempDir(String prefix) throws IOException
   {
      File newFile = File.createTempFile(prefix, "", workspaceFile);
      newFile.delete();
      newFile.mkdir();
      return newFile;
   }

   /**
    * General purpose method that deletes the contents of the given directory
    */
   public static void emptyDirectory(File dir) throws IOException
   {
      assert dir != null : "Null File given as directory";

      if (!dir.exists())
      {
         throw new IOException("'"+dir + "' does not exist");
      }
      if (!dir.isDirectory())
      {
         throw new IOException("'"+dir + "' is not a directory");
      }

      File[] contents = dir.listFiles();
      for (int i = 0; i < contents.length; i++)
      {
          removeFileOrDir(contents[i]);
      }
   }

   /**
    * Deletes the given path - if the path refers to a directory, the directory
    * is emptied and removed, so it will recursively remove a tree.
    */
   protected static void removeFileOrDir(File fod) throws IOException
   {
      //if it's a directory, empty it first
      if (fod.isDirectory())
      {
         emptyDirectory(fod);
      }

      //delete it
      fod.delete();

      //check it's worked
      if (fod.exists())
      {
         throw new IOException("Couldn't delete '"+fod+"' (don't know why)");
      }
   }
   
   /**
    * temporary Test harness
    */
   public static void main(String[] args) throws IOException {
      Workspace ws = new Workspace("TestId");
      ws.makeTempDir("wibble");
      ws.close();
   }
}

/*
$Log: Workspace.java,v $
Revision 1.7  2004/03/12 16:49:00  mch
Added debug logging, more robust close?

 */


