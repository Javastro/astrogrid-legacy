/**
 * $Id: Workspace.java,v 1.6 2003/09/07 18:45:56 mch Exp $
 */

package org.astrogrid.datacenter.service;

import java.io.File;
import java.io.IOException;
import org.astrogrid.datacenter.config.Configuration;
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

   /** Path to this workspace instance */
   protected String workspacePath = null;

   /** File representation of this workspace instance */
   protected File workspaceFile = null;

   /** Used to mark when the directory has been closed (deleted) */
//   protected boolean closed = false; this is meaningless if we expose workspaceFile


   /**
    * Creates a temporary directory with the name given by workspaceId
    * in the workspace directory given in the configuration file
    */
   public Workspace(String workspaceId)
   {
      String workRoot = Configuration.getProperty( WORKSPACE_DIRECTORY  );

      workspacePath = workRoot + File.separator + workspaceId;

      File workspaceFile = new File(workspacePath);

      if (workspaceFile.exists())
      {
         throw new IllegalArgumentException(ERR_WORKSPACE_ALREADY_IN_USE);
      }

      workspaceFile.mkdir();

      //workspaceFile.deleteOnExit(); nb for debug we let it carry on existing
   }

   /**
    * Use when you've finished with the space and you want to tidy up
    */
   public void close() throws IOException
   {
      //empty(workspacePath);  //commented out at the moment so we can debug contents

//      closed = true;
   }

   /**
    * Returns the path to the workspace
    */
   public File getWorkspace()
   {
      return workspaceFile;
   }

   /**
    * Deletes the contents of the workspace
    */
   public void empty() throws IOException
   {
//      if (closed)
//      {
//         throw new UnsupportedOperationException("This workspace has been closed");
//      }

      emptyDirectory(workspaceFile);
   }

   /**
    * So you need to make a temporary file in the workspace - this returns
    * a reference to it given a filename
    */
   public File makeWorkFile(String filename)
   {
      return new File(workspacePath + File.separator + filename);
   }

   /**
    * General purpose method that deletes the contents of the given directory
    */
   public static void emptyDirectory(File dir) throws IOException
   {
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


