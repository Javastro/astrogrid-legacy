package org.astrogrid.mySpace.mySpaceUtil;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader ;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;

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
   

   public static boolean writeToFile(File file, String theString)
   {  PrintWriter printWriter = null;   
    
      try
      {
//
//       Open file to write into
   
          printWriter = new PrintWriter(new BufferedWriter( new FileWriter (file, true)));              
          if ( DEBUG )
          {  logger.appendMessage("MySpaceUtil file is:"
               + file + ";  CONTENTS :" + theString);
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


   protected String getComponentName()
   { return Configurator.getClassName( MySpaceUtils.class);
   }
}

