/*
 * $Id JarFinder.java $
 *
 */

package org.astrogrid.devtools;


/**
 * Locates the jar file in a subdirectory of this one that has the given
 * class in it.  Used for all those nasty NoClassDefFoundError which means
 * you haven't included some jar file in your classpath but you have no idea
 * which one it is.
 * <p>
 * This does of course mean that you have to have that jar file present (so
 * it can be found) but you can run this on a working system to find the name
 * of it
 * <p>
 *
 * @author M Hill
 */

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.astrogrid.util.ClassPathUtils;

public class JarFinder
{
   /*
    * Looks for the class in the classpath - has ready made found jars/directories
    */
   public static String findInClasspath(String className) throws IOException
   {
//      try
//      {
//         Class givenClass = JarFinder.class.forName(className);
         
         String[] paths = ClassPathUtils.getClassPathList();
         
         for (int i=0; i<paths.length;i++)
         {
            String path = paths[i];
            
            if (path.endsWith(".jar"))
            {
               if (isClassInJar(className, path))
               {
                  return path;
               }
            }
         }
         
         return null;
//      }
//      catch (ClassNotFoundException e)
//      {
//         return "Class "+className+" not loadable";
//      }
   }

   public static boolean isClassInJar(String className, String jarPath) throws IOException
   {
      JarFile jarFile = new JarFile(new File(jarPath));
      
      Enumeration entries = jarFile.entries();
      
      while (entries.hasMoreElements())
      {
         ZipEntry entry = (ZipEntry) entries.nextElement();
          
         String entryName = entry.getName();
         
         if (entryName.indexOf(className) >-1)
         {
            System.out.println("Found "+entryName+" in "+jarPath);
            return true;
         }
         
      }
      
      
      return false;
   }
   
   
   
   
   /**
    *
    */
   public static void main(String[] args) throws IOException
   {
//      String classname = args[0];
      String classname = "DocumentRange";
      String jar = findInClasspath(classname);
      System.out.println(jar);
      //findInDirectory(classname);
   }
}

/*
$Log: JarFinder.java,v $
Revision 1.2  2003/09/24 18:33:13  mch
Wrong previous checkin message: this is for finding which jar a class is in

Revision 1.1  2003/09/24 18:31:55  mch
Tool for checking classpath

*/

