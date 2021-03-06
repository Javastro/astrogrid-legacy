package org.astrogrid.mySpace.mySpaceServer;

// AstroGrid.

import org.astrogrid.mySpace.mySpaceStatus.*;
import org.astrogrid.mySpace.mySpaceUtil.MySpaceUtils;


// Java.

//import java.io.PrintWriter;
import java.io.File;
//import java.io.FileOutputStream;
//import java.util.Properties;

// Log4j etc.

import org.apache.log4j.Logger;

import org.astrogrid.Configurator;
import org.astrogrid.mySpace.mySpaceUtil.*;

/**
 * @WebService
 * 
 * <p>
 * The ServerManager class is invoked (usually as a Web service) to
 * manipulate files on a MySpace server.
 *
 * @author C L Qin
 * @author A C Davenhall
 * @version Iteration 3.
 */

public class ServerManager
{  private static Logger logger = Logger.getLogger(ServerManager.class);
   private static boolean DEBUG = true;
   private static MySpaceStatus status = new MySpaceStatus();

   private String response = ""; // Response returned by all the methods.
   private String dataholderpath = " ";
   
// -----------------------------------------------------------------

/**
  * Write the contents of an input string as a new file a MySpace
  * server.
  *
  * @param contents The string containing the contents to be written
  *  to the new file.
  *
  * @param newDataHolderFileName  The name of the file on the server
  *  corresponding to the DataHolder to be copied to (ie. the
  *  output file).  The file name should include the full directory
  *  specification, as known to the server operating system.
  */

   public String upLoadString(String contents,
     String newDataHolderFileName)
   {  

//
//   Attempt to write the file from the given string.

      try
      {  File newDataHolderFile  = new File(newDataHolderFileName);
         boolean isOk = MySpaceUtils.writeToFile(newDataHolderFile,
           contents);
         if (isOk)
         {  response = MSC.SUCCESS + " File up-loaded.";
         }
         else
         {  status.addCode(MySpaceStatusCode.AGMSCE01040,
              MySpaceStatusCode.ERROR,
              MySpaceStatusCode.LOG, this.getClassName() );
            response = MSC.FAULT + " " +
              status.translateCode(MySpaceStatusCode.AGMSCE01040);
         }
      }
      catch (Exception e)
      {  status.addCode(MySpaceStatusCode.AGMSCE01040,
           MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
         response = MSC.FAULT + " " +
           status.translateCode(MySpaceStatusCode.AGMSCE01040);
      }

      return response;
   }

// -----------------------------------------------------------------

/**
  * Import a new dataHolder into a MySpace server.  A remote file is
  * imported into the MySpace server.  This remote file is identified by
  * a URI, which is passed as one of the input arguments.  In Iteration
  * 3 this URI must be a URL.
  *
  * @param importURI The URI of the remote file to be imported.
  *
  * @param newDataHolderFileName  The name of the file on the server
  *  corresponding to the DataHolder to be copied to (ie. the
  *  output file).  The file name should include the full directory
  *  specification, as known to the server operating system.
  */

   public String importDataHolder(String importURI, 
     String newDataHolderFileName)
   {  
    
//
//   Attempt to retrieve the file from the specified URL.

      try
      {  
      	
		setUp();
      	//FileTransfer fetch = new FileTransfer(importURI,
          // newDataHolderFileName);
        String fullPath=dataholderpath+newDataHolderFileName;
		FileTransfer fetch = new FileTransfer(importURI, fullPath);
        if (DEBUG) logger.debug("inside ServerManager.. importURI: "+importURI +"  dataholderpath = "+dataholderpath+"newDataHolderFileName : "+newDataHolderFileName);
         fetch.transfer();
         if (fetch.getChosenUrl() != null)
         {  response = MSC.SUCCESS + " File imported.";
         }
         else
         {  status.addCode(MySpaceStatusCode.AGMSCE01040,
              MySpaceStatusCode.ERROR,
              MySpaceStatusCode.LOG, this.getClassName() );
            response = MSC.FAULT + " " +
              status.translateCode(MySpaceStatusCode.AGMSCE01040);
         }
      }
      catch (Exception e)
      {  status.addCode(MySpaceStatusCode.AGMSCE01040,
           MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
         response = MSC.FAULT + " " +
           status.translateCode(MySpaceStatusCode.AGMSCE01040);
      }

      return response;
   }

// -----------------------------------------------------------------

/**
  * Copy a DataHolder from one location on a MySpace server to another
  * location on the same server.
  *
  * @param oldDataHolderFileName The name of the file on the server
  *   corresponding to the DataHolder to be copied from (ie. the
  *   input file).  The file name should include the full directory
  *   specification, as known to the server operating system.
  * @param newDataHolderFileName  The name of the file on the server
  *   corresponding to the DataHolder to be copied to (ie. the
  *   output file).  The file name should include the full directory
  *   specification, as known to the server operating system.
  */

   public String copyDataHolder(String oldDataHolderFileName, 
     String newDataHolderFileName)
   {  try
      {  File oldDataHolderFile  = new File(oldDataHolderFileName);
         File newDataHolderFile  = new File(newDataHolderFileName);

//
//      Determine the size of the old file and check whether it should
//      be copied using Java code or by spawning an O.S. command.

         long fileSize = oldDataHolderFile.length();

         String command;
         long sizeLimit;

         try
         {  command = MSC.getProperty(MSC.copyCommand, MSC.CATLOG);
            sizeLimit = 
              Long.parseLong(MSC.getProperty(MSC.sizeLimit, MSC.CATLOG));
         }
         catch (Exception e)
         {  command = "cp";
            sizeLimit = 5000000;
         }

         if (fileSize>=sizeLimit)
         {  Runtime.getRuntime().exec(command);
         }
         else
         {  String contents = MySpaceUtils.readFromFile(oldDataHolderFile);

            boolean isOk = MySpaceUtils.writeToFile(newDataHolderFile,
              contents);
            if (isOk)
            {  response = MSC.SUCCESS + " File copied.";
            }
            else
            {  status.addCode(MySpaceStatusCode.AGMSCE01043,
                 MySpaceStatusCode.ERROR,
                 MySpaceStatusCode.LOG, this.getClassName() );
               response = MSC.FAULT + " " +
                 status.translateCode(MySpaceStatusCode.AGMSCE01043);
               System.out.println("burble");
            }
         }
      }
      catch (Exception e)
      {  status.addCode(MySpaceStatusCode.AGMSCE01043,
           MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
         response = MSC.FAULT + " " +
           status.translateCode(MySpaceStatusCode.AGMSCE01043);
               System.out.println("yibble.");
      }

      return response;
   }

// -----------------------------------------------------------------

/**
  * Delete a DataHolder from a MySpace server.
  *
  * @param dataHolderFileName The name of the file on the server
  *   corresponding to the DataHolder to be deleted.  The file name
  *   should include the full directory specification, as known to the
  *   server operating system.
  */

   public String deleteDataHolder(String dataHolderFileName)
   {  
    
//
//   Attempt to delete the file.

      try
      {  File dataHolderFile  = new File(dataHolderFileName);
         if  (dataHolderFile != null || dataHolderFile.exists() )
         {  boolean isDeleted = dataHolderFile.delete();
            if (isDeleted)
            {  response = MSC.SUCCESS + " File deleted.";
            }
            else
            {  status.addCode(MySpaceStatusCode.AGMSCE01046,
                 MySpaceStatusCode.ERROR,
                 MySpaceStatusCode.LOG, this.getClassName() );
               response = MSC.FAULT + " " +
                 status.translateCode(MySpaceStatusCode.AGMSCE01046);
            }
         }
         else
         {  status.addCode(MySpaceStatusCode.AGMSCE01047,
              MySpaceStatusCode.ERROR,
              MySpaceStatusCode.LOG, this.getClassName() );
            response = MSC.FAULT + " " +
              status.translateCode(MySpaceStatusCode.AGMSCE01047);
         }
      }
      catch (Exception e)
      {  status.addCode(MySpaceStatusCode.AGMSCE01046,
           MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
         response = MSC.FAULT + " " +
           status.translateCode(MySpaceStatusCode.AGMSCE01046);
      }

      return response;
   }

// -----------------------------------------------------------------

/**
 * Obtain the name of the current Java class.
 */

   protected String getClassName()
   { Class currentClass = this.getClass();
     String name =  currentClass.getName();
     int dotPos = name.lastIndexOf(".");
     if (dotPos > -1)
     {  name = name.substring(dotPos+1, name.length() );
     }

     return name;
   }
   private void setUp()throws Exception{
	   MSC.getInstance().checkPropertiesLoaded();
	   dataholderpath = MSC.getProperty(MSC.dataHolderFolder, MSC.CATLOG);	
   }
		
   protected String getComponentName() { return Configurator.getClassName( ServerManager.class) ; }
}
