/**
 * $Id: Workspace.java,v 1.9 2003/09/08 19:39:46 mch Exp $
 */

package org.astrogrid.datacenter.service;

import java.io.File;
import java.io.IOException;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.log.Log;

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
   public static final String WORKSPACE_DIRECTORY = "Workspace Directory";

   private static final String ERR_WORKSPACE_ALREADY_IN_USE = "Workspace Already In Use";

   /** public marker as to whether workspace should tidy up when closed -
    * for debugging purposes it is sometimes useful to leave temporary files
    * for examination
    */
   public static boolean PERSIST = false;
   
   /** File representation of this workspace instance */
   protected File workspaceFile = null;

   /**
    * Creates a temporary directory using File.createTempFile()
    * method
    */
   public Workspace() throws IOException
   {
      //see if a working root has been specified
      String workRoot = Configuration.getProperty( WORKSPACE_DIRECTORY  );

      if (workRoot != null)
      {
         //if a working root path has been given, create the workspace from that
         workspaceFile = File.createTempFile("Workspace", "", new File(workRoot));
      }
      else
      {
         //if not, create using system temporary area
         workspaceFile = File.createTempFile("Workspace", "");
      }

      //except that the above creates a file, and we want a directory...
      workspaceFile.delete();
      workspaceFile.mkdir();

      if (!PERSIST)
      {
         workspaceFile.deleteOnExit();
      }
   }
   
   /**
    * Creates a temporary directory with the name prefixed by the given workspaceId
    * in the workspace directory given in the configuration file
    */
   public Workspace(String workspaceId) throws IOException
   {
      //see if a working root has been specified
      String workRoot = Configuration.getProperty( WORKSPACE_DIRECTORY  );

      if (workRoot == null)
      {
         //if a working root is not given, use the working directory
         //File temp = File.createTempFile(workspaceId+"-", "");
         //workRoot = temp.getAbsolutePath();
         
         //or just use the working directory
         workspaceFile = new File(workspaceId);
      }
      else
      {
         //if a working root path has been given, create the workspace from that
         File workDir = new File(workRoot);
         Log.affirm(workDir.isDirectory(), "Working root '"+workRoot+"' given by configuration key '"+WORKSPACE_DIRECTORY+"' is not a directory");
         
         workspaceFile = new File(workRoot + File.separator + workspaceId);
      }
         
      if (workspaceFile.exists())
      {
         throw new IllegalArgumentException(ERR_WORKSPACE_ALREADY_IN_USE);
      }

      workspaceFile.mkdir();

      if (!PERSIST)
      {
         workspaceFile.deleteOnExit();
      }
   }

   /**
    * Use when you've finished with the space and you want to tidy up.  NB no
    * other operations are possible after this - it is best to leave it to the
    * garbage collector to close() as part of the finalise(), to avoid other
    * unknown references to it trying to use it after close.
    */
   public synchronized void close() throws IOException
   {
      if (isClosed()) throw new IllegalStateException("Trying to close a closed workspace");

      if (!PERSIST) empty();

      //attempt at threadsafetying but haven't thought about it properly - MCH
      File tempFile = workspaceFile;
      workspaceFile = null;

      if (!PERSIST) tempFile.delete();
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
      try
      {
         if (!isClosed())
         {
            close();
         }
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
      if (isClosed()) throw new IllegalStateException("Trying to empty a closed workspace");

      emptyDirectory(workspaceFile);
   }

   /**
    * So you need to make a temporary file in the workspace - this returns
    * a reference to it given a filename
    */
   public File makeWorkFile(String filename) throws IOException
   {
      if (isClosed()) throw new IllegalStateException("Trying to create a new file in a closed workspace");

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
    * General purpose method that deletes the contents of the given directory
    */
   public static void emptyDirectory(File dir) throws IOException
   {
      Log.affirm(dir != null, "Null File given as directory");
      
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
}



