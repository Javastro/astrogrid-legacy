/*
 * $Id ClassPathUtils.java $
 *
 */

package org.astrogrid.util;


/**
 * A set of methods for examining and checking the classpath
 *
 * @author M Hill
 */

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.JarFile;

public class ClassPathUtils
{
   
   /**
    * Examines class path for jar files that do not exist (as files) and
    * directory paths that are not directories...
    * <p>
    * @return a list of invalid paths and jar files - null if there are none
    */
   public static String[] getInvalidPaths()
   {
      String[] paths = getClassPathList();
      Vector invalidPaths = new Vector();
      
      for (int i=0; i<paths.length;i++)
      {
         String path = paths[i];
         
         if (path.toLowerCase().endsWith(".jar"))
         {
            String error = isJarFileValid(path);
            if (error != null) {
               invalidPaths.add(path+" ("+error+")");
            }
         }
         else
         {
            String error = isDirValid(path);
            if (error != null) {
               invalidPaths.add(path+" ("+error+")");
            }
         }
      }

      if (invalidPaths.size() == 0) {
         return null;
      } else {
         return (String[]) invalidPaths.toArray(new String[] {});
      }
   }

   /**
    * Checks that the given filename is a valid jar file. Returns null if
    * it is valid, or a string describing the reason if it is invalid.
    */
   public static String isJarFileValid(String filename)
   {
      try
      {
         JarFile jarFile = new JarFile(new File(filename));

         //not sure of the performance hit of this Enumeration entries = jarFile.entries();
         
         return null;
      }
      catch (IOException e)
      {
         return e.getMessage();
      }
   }
   
   /**
    * Checks that the given path is a valid directory. Returns null if
    * it is valid, or a string describing the reason if it is invalid.
    */
   public static String isDirValid(String dirname)
   {
      File file = new File(dirname);
      
      if (!file.exists())
      {
         return "Does not exist";
      }
      
      if (!file.isDirectory())
      {
         return "Not a directory";
      }

      return null;
   }
   

   /**
    * Returns an array of the class path elements, ie directories and
    * jar files
    */
   public static String[] getClassPathList()
   {
      String path = getClassPath();
      StringTokenizer tokenizer = new StringTokenizer(path, File.pathSeparator);
      Vector list = new Vector();
      
      while (tokenizer.hasMoreElements())
      {
         list.add(tokenizer.nextToken());
      }
      
      return (String[]) list.toArray(new String[] {});
   }
   
   /**
    * Returns the classpath as it is stored - in one long string of files
    * and directories separated by a platform specific character, eg colon
    * or semicolon.
    */
   public static String getClassPath()
   {
      return System.getProperty("java.class.path");
   }
   
   
   /**
    * Test harness - this can be run on its own to test the classpath
    * setup
    */
   public static void main(String[] args)
   {
      String[] invalidPaths = getInvalidPaths();
      
      if (invalidPaths == null) {
         System.out.println("all paths are valid");
      }
      else {
      
         for (int i=0;i<invalidPaths.length;i++)
         {
            System.out.println(invalidPaths[i]);
         }
      }
      
   }
}

/*
$Log: ClassPathUtils.java,v $
Revision 1.2  2003/11/13 22:14:56  mch
Removed unused import

Revision 1.1  2003/09/24 18:32:22  mch
Tool for checking classpath

*/
