package org.astrogrid.mySpace.mySpaceServer;

// AstroGrid.

import org.astrogrid.mySpace.mySpaceManager.DataItemRecord;
import org.astrogrid.mySpace.mySpaceStatus.*;
import org.astrogrid.mySpace.mySpaceUtil.MySpaceUtils;
import org.astrogrid.mySpace.mySpaceUtil.FileTransferFake;

// Java.

import java.io.File;

/**
 * @WebService
 * 
 * <p>
 * The ServerManager class is invoked (usually as a Web service) to
 * manipulate files on a MySpace server.
 *
 * @author C L Qin
 * @author A C Davenhall
 * @since Iteration 3.
 * @version Iteration 5.
 */

public class ServerManager
{  private static Logger logger = new Logger();
   private static boolean DEBUG = true;
   private static MySpaceStatus status = new MySpaceStatus();

   private String response = ""; // Response returned by all the methods.

// -----------------------------------------------------------------

/**
 * Write the contents of an input string as a new file a MySpace
 * server.
 *
 * @param contentsType flag indicating the type of contents of the file:
 *   true means the contents are Strin, false that they are an array
 *   of bytes.
 * @param stringContents The String containing the contents to be written
 *  to the new file.
 * @param byteContents A byte array containing the contents to be written
 *  to the new file.
 * @param newDataHolderFileName  The name of the file on the server
 *  corresponding to the DataHolder to be copied to (ie. the
 *  output file).  The file name should include the full directory
 *  specification, as known to the server operating system.
 * @param appendFlag If true the contents will be appended to the end
 *   of an existing file; otherwise any existing file will be
 *   overwritten.
 */

   public String upLoadString(boolean contentsType, String stringContents,
     byte[] byteContents, String newDataHolderFileName, boolean appendFlag)
   {  if (DEBUG)
      {  logger.appendMessage("ServerManager.upLoadString:-");
         logger.appendMessage("  contentsType: " + contentsType);
         logger.appendMessage("  newDataHolderFileName: " 
           + newDataHolderFileName);
      }

//
//   Attempt to write the file from the given string.

      try
      {  File newDataHolderFile  = new File(newDataHolderFileName);
         boolean isOk = true;

         if (contentsType)
         {  isOk = MySpaceUtils.writeToFile(newDataHolderFile,
              stringContents, appendFlag);
         }
         else
         {  isOk = MySpaceUtils.writeToBinaryFile(newDataHolderFile,
              byteContents, appendFlag);
         }

         if (isOk)
         {  response = MSC.SUCCESS + " Wrote file: " + newDataHolderFile;
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

      logger.appendMessage(response);
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
 * @param appendFlag If true the contents will be appended to the end
 *   of an existing file; otherwise any existing file will be
 *   overwritten.
 */

   public String importDataHolder(String importURI, 
     String newDataHolderFileName, boolean appendFlag)
   {  if (DEBUG)
      {  logger.appendMessage("ServerManager.importDataHolder:-");
         logger.appendMessage("  importURI: " + importURI);
         logger.appendMessage("  newDataHolderFileName: " + newDataHolderFileName);
      }

//
//   Attempt to retrieve the file from the specified URL.

      try
      {  FileTransferFake fetch = new FileTransferFake(importURI,
           newDataHolderFileName, appendFlag);
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

      logger.appendMessage(response);
      return response;
   }


// -----------------------------------------------------------------

/**
 * Read the contents of a file held on a MySpace server and return
 * them as a String.
 *
 * @param newDataHolderFileName The name of the file on the server
 *   whose contents are to be retrieved.
 *
 * @return The contents of the file.
 */

   public String retrieveString(String dataHolderFileName)
   {  if (DEBUG)
      {  logger.appendMessage("ServerManager retrieve.String:-");
         logger.appendMessage("  dataHolderFileName: " + dataHolderFileName);
      }

      String contents = null;

//
//   Attempt to read the file from the given string.

      try
      {  File dataHolderFile  = new File(dataHolderFileName);
         contents = MySpaceUtils.readFromFile(dataHolderFile);

         if (contents == null)
         {  status.addCode(MySpaceStatusCode.AGMSCE01047,
              MySpaceStatusCode.ERROR,
              MySpaceStatusCode.LOG, this.getClassName() );
         }
      }
      catch (Exception e)
      {  status.addCode(MySpaceStatusCode.AGMSCE01047,
           MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return contents;
   }


// -----------------------------------------------------------------

/**
 * Read the contents of a file held on a MySpace server and return
 * them as an array of bytes.
 *
 * @param newDataHolderFileName The name of the file on the server
 *   whose contents are to be retrieved.
 *
 * @return The contents of the file.
 */

   public byte[] retrieveBytes(String dataHolderFileName)
   {  if (DEBUG)
      {  logger.appendMessage("ServerManager retrieve.Bytes:-");
         logger.appendMessage("  dataHolderFileName: " + dataHolderFileName);
      }

      byte[] contents = new byte[1];

//
//   Attempt to read the file from the given string.

      try
      {  File dataHolderFile  = new File(dataHolderFileName);
         contents = MySpaceUtils.readFromBinaryFile(dataHolderFile);

         if (contents == null)
         {  status.addCode(MySpaceStatusCode.AGMSCE01047,
              MySpaceStatusCode.ERROR,
              MySpaceStatusCode.LOG, this.getClassName() );
         }
      }
      catch (Exception e)
      {  status.addCode(MySpaceStatusCode.AGMSCE01047,
           MySpaceStatusCode.ERROR,
           MySpaceStatusCode.LOG, this.getClassName() );
      }

      return contents;
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
   {  if (DEBUG)
      {  logger.appendMessage("ServerManager.copyDataHolder:-");
         logger.appendMessage("  oldDataHolderFileName: " + oldDataHolderFileName);
         logger.appendMessage("  newDataHolderFileName: " + newDataHolderFileName);
      }

      try
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
              contents, false);
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

      logger.appendMessage(response);
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
   {  if (DEBUG)
      {  logger.appendMessage("ServerManager.deleteDataHolder:-");
         logger.appendMessage("  dataHolderFileName: " + dataHolderFileName);
      }

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

      logger.appendMessage(response);
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

}
