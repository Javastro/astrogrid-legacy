//-------------------------------------------------------------------------
// FILE: TempDirManager.java
// PACKAGE: org.astrogrid.common.service
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 05/12/02   KEA       Initial prototype
//
//-------------------------------------------------------------------------

package org.astrogrid.common.service;

import java.util.Random;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

public class TempDirManager
{
   protected static Random random = new Random();
   protected String prefix;
   protected String separator;

   public TempDirManager(String thePrefix)
   {
      prefix = thePrefix;
      separator = File.pathSeparator;
   }

   /**
    * Creates a directory based on a random number
    */
   public String mkDir() throws IOException
   {
      // Get randomly-generated directory name
      //
      String randName = generateHandle();
      File f = new File(prefix + randName);

      // Make sure it doesn't exist already
      while (f.exists())
      {
         randName = generateHandle();
         f = new File(prefix + randName);
      }

      // Create directory and test it exists
      //
      f.mkdir();
      if (!(f.exists()))
      {
         throw new IOException("Couldn't create temporary directory "
                              + prefix + randName);
      }

      // Return directory name
      //
      return randName;
   }


   /**
    * Deletes the contents of the given directory
    */
   public void emptyDir(String dirName) throws IOException
   {
      File f = new File(prefix + dirName);
      if (f.exists())
      {
         if (f.isDirectory())
         {
            String[] contents = f.list();
            for (int i = 0; i < contents.length; i++)
            {
               removeFileOrDir(dirName + separator + contents[i]);
            }
         }
         else
         {
            throw new IOException("The entry " + prefix + dirName
                     + "does not seem to be a directory");
         }
      }
      else
      {
            throw new IOException("The directory " + prefix + dirName
                     + "does not exist");
      }
   }

   /**
    * Deletes the contents of the given directory and removes that directory
    * also
    */
   public void rmDir(String dirName) throws IOException
   {
      removeFileOrDir(dirName);
   }

   /**
    * Returns true if the given string refers to an existing directory
    */
   public boolean dirExists(String dirName)
   {
      File f = new File(prefix + dirName);
      if (f.exists() && f.isDirectory())
      {
         return true;
      }
      return false;
   }


   /**
    * Returns a random number suitable for use as a temporary directory name
    *
   protected int getRandom()
   {
      // Integers in range 1000000 to 9999999
      //
      return random.nextInt(8999999) + 1000000;
   }

   /**
    * Generates a handle for use by a particular instance; uses the current
    * time to help us debug (ie we can look at the temporary directories and
    * see which was the last run). Later we could add service/user information
    * if available
    */
   public static String generateHandle()
   {
      Date todayNow = new Date();

      return todayNow.getYear()+""+todayNow.getMonth()+""+todayNow.getDate()+"_"+
               todayNow.getHours()+"_"+todayNow.getMinutes()+"_"+todayNow.getSeconds()+
               "_"+(random.nextInt(8999999) + 1000000);

   }

   /**
    * Generates a unique handle by generating handles and checking to see if
    * a directory/file already exists in the given directory with the given
    * postfix
    */
   public static String generateUniqueHandle(String prefix, String postfix)
   {
      String handle = generateHandle();

      File f = new File(prefix + handle + postfix);

      // Make sure it doesn't exist already
      while (f.exists())
      {
         handle = generateHandle();
         f = new File(prefix + handle + postfix);
      }
      return handle;
   }

   /**
    * Deletes the given path - if the path refers to a directory, the directory
    * is emptied and removed
    */
   protected void removeFileOrDir(String name) throws IOException
   {
      File f = new File(prefix + name);
      if (f.isFile())
      {
         f.delete();
         if (f.exists())
         {
            throw new IOException("Couldn't delete file or directory named "
                                 + prefix + name);
         }
      }
      else if (f.isDirectory())
      {
         emptyDir(name);
         f.delete();
         if (f.exists())
         {
            throw new IOException("Couldn't delete file or directory named "
                                 + prefix + name);
         }
      }
      else
      {
         throw new IOException("Entry named " + prefix + name
               + " does not seem to be a file or directory.");
      }
   }
}
//-------------------------------------------------------------------------
