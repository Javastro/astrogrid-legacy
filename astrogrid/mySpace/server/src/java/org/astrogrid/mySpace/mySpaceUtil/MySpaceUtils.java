package org.astrogrid.mySpace.mySpaceUtil;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader ;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.lang.reflect.Array;

import org.astrogrid.mySpace.mySpaceStatus.*;
import org.astrogrid.mySpace.mySpaceManager.MMC;
import org.astrogrid.Configurator;
import org.astrogrid.i18n.*;

/**
 * @author C L QIN
 * @since Iteration 2.
 * @version Iteration 5.
 */

public class MySpaceUtils
{  private static Logger logger = new Logger ();
   private static boolean DEBUG = true;
   private static MySpaceStatus msstatus = new MySpaceStatus();
   private static String response = " ";


// ---------------------------------------------------------------------

/**
 * Read a file and return its contents as a String.
 *
 * @param file The name of the file.
 * @return The file contents.
 */

   public static String readFromFile(File file)
   {  FileReader fileReader = null;

      try
      {  if (file == null || !file.exists())
         {  return null;
         }

//
//      Open the file to read from.

         fileReader = new FileReader(file);
         
         StringBuffer strbufFileContent = new StringBuffer(8192);
         char charFileText[] = new char[2048];
         
         int iTotalCharactersRead = 0;
         int iCharactersRead = 0;
         
         while((iCharactersRead = fileReader.read(charFileText,0,2048)) != -1)
         {
//
//         Add number of characters read to the total read.

            iTotalCharactersRead += iCharactersRead;
            
//
//         Add the characters read to the buffer.

            strbufFileContent.append(charFileText);
         }

//         
//      Size buffer to fit data.

         strbufFileContent.setLength(iTotalCharactersRead);

//
//      Return the data as a String.

         return strbufFileContent.toString();
      }
      catch (Exception e)
      {
         logger.appendMessage(
           "Exception caught while reading from file " +
           "MySpaceUtils.readFromFile: "+e.toString());
         msstatus.addCode(MySpaceStatusCode.AGMMCE00103,MySpaceStatusCode.ERROR, 
           MySpaceStatusCode.NOLOG, new MySpaceUtils().getComponentName());
         response = MMC.FAULT+MySpaceStatusCode.AGMMCE00103;
         return response;
      }
      finally
      {  try
         {  if (fileReader != null)
            {fileReader.close();
            }
         }
         catch(Exception e)
         {  return "";
         }
      }
   }


// ---------------------------------------------------------------------

/**
 * Save the contents of a String as a file.
 *
 * @param file Name of the file.
 * @param theString The String to be saved.
 * @param appendFlag If true the contents will be appended to the end
 *   of an existing file; otherwise any existing file will be
 *   overwritten.
 */

   public static boolean writeToFile(File file, String theString,
     boolean appendFlag)
   {  PrintWriter printWriter = null;   
    
      try
      {
//
//       Open file to write into
   
          printWriter = new PrintWriter(new BufferedWriter(
            new FileWriter (file, appendFlag) ) );              
          if ( DEBUG )
          {  logger.appendMessage("MySpaceUtil file name is:"
               + file);
          }

//
//      Write to file.

         printWriter.print(theString);          
         return true;               
      }
      catch (Exception e)
      {  return false;
      }
      finally
      {
//
//      Close the file.

         try
         {  if(printWriter != null)
            {  printWriter.close();
            }
         }
         catch (Exception e)
         {               
         }
      }
   }


// ---------------------------------------------------------------------

/**
 * Read a file and return its contents as an array of bytes.
 *
 * @param file The name of the file.
 * @return The file contents.
 */

   public static byte[] readFromBinaryFile(File file)
   {  byte[] returnArray = null;

      try
      {  int fileSize = (int)file.length();

         byte[] contents = new byte[fileSize];

         DataInputStream in = new DataInputStream(
           new FileInputStream(file) );

         in.readFully(contents);
         in.close();

         returnArray = contents;
      }
      catch (Exception e)
      {  logger.appendMessage("Failure reading file: " + file);
         logger.appendMessage(e.toString() );
      }

      return returnArray;
   }


// ---------------------------------------------------------------------

/**
 * Save the contents of an array of bytes as a file.
 *
 * @param file Name of the file.
 * @param contents The array of bytes to be saved.
 * @param appendFlag If true the contents will be appended to the end
 *   of an existing file; otherwise any existing file will be
 *   overwritten.
 */

   public static boolean writeToBinaryFile(File file, byte[] contents,
     boolean appendFlag)
   {  boolean isOk = true;
    
      try
      {  DataOutputStream out = new DataOutputStream(
           new FileOutputStream(file, appendFlag) );

         int contentsLength = Array.getLength(contents);
         out.write(contents, 0, contentsLength);
         out.close();
      }
      catch (Exception e)
      {  logger.appendMessage("Failure writing file: " + file);
         logger.appendMessage(e.toString() );

         isOk = false;
      }

     return isOk;
   }


// ---------------------------------------------------------------------

   protected String getComponentName()
   { return Configurator.getClassName( MySpaceUtils.class);
   }
}

