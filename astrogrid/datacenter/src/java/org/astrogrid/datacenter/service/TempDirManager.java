/**
 * $Id: TempDirManager.java,v 1.1 2003/08/27 09:58:07 mch Exp $
 */

package org.astrogrid.datacenter.service;

import java.io.File;
import java.io.IOException;
import org.astrogrid.datacenter.config.ConfigurationDefaultImpl;
import org.astrogrid.datacenter.config.ConfigurationKeys;

/**
 * A temporary filespace manager for
 * service instances.  Actually just a set of helper methods for creating
 * and removing directories in the temporary workspace.  Create one of these
 * to manage one temporary directory - once 'close' is called, it should not
 * be used again.
 * NB the 'close' operation is not threadsafe (ie if other methods are called
 * while the close operation is running, behaviour is undefined....)
 */

public class TempDirManager
{
   protected final ConfigurationDefaultImpl config;
   private static final String WORKSPACE_DIRECTORY_KEY = "Workspace Directory";
   private static final String WORKSPACE_DIRECTORY_CAT = "Service";

   private static final String ERR_WORKSPACE_ALREADY_IN_USE = "Workspace Already In Use";

   /** Path to this workspace instance */
   protected String workspacePath = null;

   /** File representation of this workspace instance */
   protected File workspaceFile = null;

   /** Used to mark when the directory has been closed (deleted) */
   protected boolean closed = false;


   /**
    * Creates a temporary directory with the name given by workspaceId
    * in the workspace directory given in the configuration file
    */
   public TempDirManager(String workspaceId)
   {
      String workRoot = config.getProperty( WORKSPACE_DIRECTORY_KEY, WORKSPACE_DIRECTORY_CAT);

      workspacePath = workRoot + File.pathSeparator + workspaceId;

      File workspaceFile = new File(workspacePath);

      if (workspaceFile.exists())
      {
         throw new IllegalArgumentException(ERR_WORKSPACE_ALREADY_IN_USE);
      }

      workspaceFile.mkdir();
  }

  /**
   * Use when you've finished with the space and you want to tidy up
   */
  public void close() throws IOException
  {
     //emptyDir(workspacePath);  //commented out at the moment so we can debug contents

      closed = true;
  }

  /**
   * Returns the path to the workspace
   */
  public File getWorkspace()
  {
     return workspaceFile;
  }

   /**
    * Deletes the contents of the given directory
    */
   public void emptyDir(String path) throws IOException
   {
      File f = new File(path);
      if (f.exists())
      {
         if (f.isDirectory())
         {
            String[] contents = f.list();
            for (int i = 0; i < contents.length; i++)
            {
               removeFileOrDir(path + File.pathSeparator + contents[i]);
            }
         }
         else
         {
            throw new IOException("'"+path + "' does not seem to be a directory");
         }
      }
      else
      {
         throw new IOException("'"+path + "' does not exist");
      }
   }

   /**
    * Deletes the given path - if the path refers to a directory, the directory
    * is emptied and removed, so it will recursively remove a tree.
    */
   protected void removeFileOrDir(String path) throws IOException
   {
      File f = new File(path);

      //if it's a directory, empty it first
      if (f.isDirectory())
      {
         emptyDir(path);
      }

      f.delete();
      if (f.exists())
      {
         throw new IOException("Couldn't delete '"+path+"' (don't know why)");
      }
   }
}

