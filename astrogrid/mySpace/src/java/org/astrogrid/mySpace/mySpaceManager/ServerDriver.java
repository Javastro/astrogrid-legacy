package org.astrogrid.mySpace.mySpaceManager;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.astrogrid.mySpace.mySpaceStatus.*;
import org.astrogrid.mySpace.mySpaceServer.ServerManager;

// import org.apache.log4j.Logger;

/**
 * <p>
 * The ServerDriver class is a utility class of methods to invoke
 * the functions of a MySpace server.
 * </p>
 * <p>
 * All the code necessary to invoke a MySpace server as a Web service
 * is included in the methods of this class.
 * </p>
 *
 * @author C L Qin
 * @author A C Davenhall (Edinburgh)
 * @version Iteration 4.
 */

public class ServerDriver
{  private static Logger logger = new Logger();
   Call call = null;

//
// Constructor.

/**
 * Default constructor.
 */

   public ServerDriver()
   {  super();
   }

// -----------------------------------------------------------------

/**
 * Write the contents of an input string as a new file a MySpace
 * server.
 *
 * @param contents The string containing the contents to be written
 *  to the new file.
 * @param newDataHolderFileName  The name of the file on the server
 *  corresponding to the DataHolder to be copied to (ie. the
 *  output file).  The file name should include the full directory
 *  specification, as known to the server operating system.
 * @param appendFlag If true the contents will be appended to the end
 *   of an existing file; otherwise any existing file will be
 *   overwritten.
 */

   public boolean upLoadString(String contents,
     String newDataHolderFileName, boolean appendFlag)
   {  boolean successStatus = true;

      Configuration config = new Configuration();

      try
      {  if (config.getSERVERDEPLOYMENT() == config.SEPARATESERVERS)
         {  
//
//         [TODO]: This method is now broken.  The appendFlag
//           argument should be added.

            call = createServerCall();
            call.setOperationName( "upLoadString" );		
            call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
		
            call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
            String serverResponse = (String)call.invoke( new Object[]
              {contents,newDataHolderFileName} );

            if (config.getDEBUG() )
            {  logger.appendMessage("GOT SERVERRESPONSE: " + serverResponse);
            }
         }
         else if (config.getSERVERDEPLOYMENT() == config.INTERNALSERVERS)
         {  ServerManager server = new ServerManager();
            server.upLoadString(contents, newDataHolderFileName,
              appendFlag);
         }
         else if (config.getSERVERDEPLOYMENT() == config.MANAGERONLY)
         {  logger.appendMessage(
              "Attempt to upload a string to a dataHolder on a server:\n"
              + "  output server file name: " + newDataHolderFileName + "\n"
              + "  contents: \n\n" + contents);
         }
         else
         {  throw new Exception("Invalid SERVERDEPLOYMENT.");
         }
      }
      catch(Exception e)
      {  successStatus = false;

         if (config.getDEBUG() )
         {  e.printStackTrace();
         }
      }

      return successStatus;
   }

// -----------------------------------------------------------------

/**
 * Import a new dataHolder into a MySpace sercer.  A remote file is
 * imported into the MySpace server.  This remote file is identified by
 * a URI, which is passed as one of the input arguments.  In Iteration
 * 3 this URI must be a URL.
 *
 * @param importURI The URI of the remote file to be imported.
 * @param newDataHolderFileName  The name of the file on the server
 *  corresponding to the DataHolder to be copied to (ie. the
 *  output file).  The file name should include the full directory
 *  specification, as known to the server operating system.
 * @param appendFlag If true the contents will be appended to the end
 *   of an existing file; otherwise any existing file will be
 *   overwritten.
 */

   public boolean importDataHolder(String importURI, 
     String newDataHolderFileName, boolean appendFlag)
   {  boolean successStatus = true;

      Configuration config = new Configuration();

      try
      {  if (config.getSERVERDEPLOYMENT() == config.SEPARATESERVERS)
         {
//
//         [TODO]: This method is now broken.  The appendFlag
//           argument should be added.

            call = createServerCall();
            call.setOperationName( "importDataHolder" );
            call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
	
            call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
            String serverResponse = (String)call.invoke( new Object[]
              {importURI,newDataHolderFileName} );

            if (config.getDEBUG() )
            {  logger.appendMessage("GOT SERVERRESPONSE: " + serverResponse);
            }
         }
         else if (config.getSERVERDEPLOYMENT() == config.INTERNALSERVERS)
         {  ServerManager server = new ServerManager();
            server.importDataHolder(importURI, newDataHolderFileName,
              appendFlag);
         }
         else if (config.getSERVERDEPLOYMENT() == config.MANAGERONLY)
         {  logger.appendMessage(
              "Attempt to import a dataHolder on a server:\n"
              + "  input URI: "+ importURI + "\n"
              + "  output server file name: " + newDataHolderFileName);
         }
         else
         {  throw new Exception("Invalid SERVERDEPLOYMENT.");
         }
      }
      catch(Exception e)
      {  successStatus = false;

         if (config.getDEBUG() )
         {  e.printStackTrace();
         }
      }

      return successStatus;
   }

// -----------------------------------------------------------------

/**
 * Read the contents of a file held on a MySpace server and return
 * them as a String.
 *
 * @param contents The string containing the contents to be written
 *  to the new file.
 *
 * @param newDataHolderFileName The name of the file on the server
 * which is to be read.
 *
 * @return The contents of the file.
 */

   public String retrieveString(String dataHolderFileName)
   {  String contents = null;

      Configuration config = new Configuration();

      try
      {  if (config.getSERVERDEPLOYMENT() == config.SEPARATESERVERS)
         {  
//
//         [TODO]: Add the retrieveString function when the Servers
//         are deployed as separate Web Services.

            logger.appendMessage("retrieveString no available in this mode.");
         }
         else if (config.getSERVERDEPLOYMENT() == config.INTERNALSERVERS)
         {  ServerManager server = new ServerManager();
            contents = server.retrieveString(dataHolderFileName);
         }
         else if (config.getSERVERDEPLOYMENT() == config.MANAGERONLY)
         {  logger.appendMessage(
              "Attempt to read a dataHolder from a server:\n"
              + "  server file name: "+ dataHolderFileName);
         }
         else
         {  throw new Exception("Invalid SERVERDEPLOYMENT.");
         }
      }
      catch(Exception e)
      {  contents = null;

         if (config.getDEBUG() )
         {  e.printStackTrace();
         }
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

   public boolean copyDataHolder(String oldDataHolderFileName, 
     String newDataHolderFileName)
   {  boolean successStatus = true;

      Configuration config = new Configuration();

      try
      {  if (config.getSERVERDEPLOYMENT() == config.SEPARATESERVERS)
         {  call = createServerCall();
            call.setOperationName( "copyDataHolder" );		
            call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
            call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
		
            call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
            String serverResponse = (String)call.invoke( new Object[]
              {oldDataHolderFileName,newDataHolderFileName} );

            if (config.getDEBUG() )
            {  logger.appendMessage("GOT SERVERRESPONSE: " + serverResponse);
            }
         }
         else if (config.getSERVERDEPLOYMENT() == config.INTERNALSERVERS)
         {  ServerManager server = new ServerManager();
            server.copyDataHolder(oldDataHolderFileName,
              newDataHolderFileName);
         }
         else if (config.getSERVERDEPLOYMENT() == config.MANAGERONLY)
         {  logger.appendMessage(
              "Attempt to copy a dataHolder on a server:\n"
              + "  input server file name: "+ oldDataHolderFileName + "\n"
              + "  output server file name: " + newDataHolderFileName);

         }
         else
         {  throw new Exception("Invalid SERVERDEPLOYMENT.");
         }
      }
      catch(Exception e)
      {  successStatus = false;

         if (config.getDEBUG() )
         {  e.printStackTrace();
         }
      }

      return successStatus;
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

   public boolean deleteDataHolder(String dataHolderFileName)
   {  boolean successStatus = true;

      Configuration config = new Configuration();

      try
      {  if (config.getSERVERDEPLOYMENT() == config.SEPARATESERVERS)
         {  call = createServerCall();
            call.setOperationName( "deleteDataHolder" );
            call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
		
            call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
	    String serverResponse = (String)call.invoke( new Object[]
              {dataHolderFileName} );

            if (config.getDEBUG() )
            {  logger.appendMessage("GOT SERVERRESPONSE: "+serverResponse);
            }
         }
         else if (config.getSERVERDEPLOYMENT() == config.INTERNALSERVERS)
         {  ServerManager server = new ServerManager();
            server.deleteDataHolder(dataHolderFileName);
         }
         else if (config.getSERVERDEPLOYMENT() == config.MANAGERONLY)
         {  logger.appendMessage(
              "Attempt to delete a dataHolder on a server:\n"
              + "  server file name: "+ dataHolderFileName);
         }
         else
         {  throw new Exception("Invalid SERVERDEPLOYMENT.");
         }
      }
      catch(Exception e)
      {  successStatus = false;

         if (config.getDEBUG() )
         {  e.printStackTrace();
         }
      }

      return successStatus;
   }

// -----------------------------------------------------------------


   private Call createServerCall()
   {  Call call = null;
      try
      {  String endpoint  =
           "http://localhost:8080/axis/services/ServerManager";
         Service service = new Service();
         call = (Call)service.createCall();
         call.setTargetEndpointAddress( new java.net.URL(endpoint) );
		
         /*
         call.setOperationName( "moveDataHolder" );			
         call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
         call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
		
         call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
         String serverResponse = call.invoke( new Object[] {content,path} );
         */
      }
      catch(Exception e)
      {  logger.appendMessage("ERROR_CALL_SERVER_MANAGER");
         logger.appendMessage(
           "  Exception thrown in ServerDriver.createServerCall.");
         logger.appendMessage("  " + e.toString() );
      }	
      return call;
   }   
}