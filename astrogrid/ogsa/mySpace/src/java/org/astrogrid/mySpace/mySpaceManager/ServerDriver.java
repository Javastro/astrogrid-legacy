package org.astrogrid.mySpace.mySpaceManager;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.astrogrid.mySpace.mySpaceStatus.*;

import org.apache.log4j.Logger;

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
 * @version Iteration 3.
 */

public class ServerDriver
{  
   private static Logger logger = Logger.getLogger(MySpaceActions.class);
   private static boolean DEBUG = true;	
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
  *
  * @param newDataHolderFileName  The name of the file on the server
  *  corresponding to the DataHolder to be copied to (ie. the
  *  output file).  The file name should include the full directory
  *  specification, as known to the server operating system.
  */

   public boolean upLoadString(String contents,
     String newDataHolderFileName)
   {  boolean successStatus = true;

      try
      {  call = createServerCall();
         call.setOperationName( "upLoadString" );		
         call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
         call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
		
         call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
         String serverResponse = (String)call.invoke( new Object[]
           {contents,newDataHolderFileName} );

         if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: " + serverResponse);
      }
      catch(Exception e)
      {  successStatus = false;
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
  *
  * @param newDataHolderFileName  The name of the file on the server
  *  corresponding to the DataHolder to be copied to (ie. the
  *  output file).  The file name should include the full directory
  *  specification, as known to the server operating system.
  */

   public boolean importDataHolder(String importURI, 
     String newDataHolderFileName)
   {  boolean successStatus = true;

      try
      {  call = createServerCall();
         call.setOperationName( "importDataHolder" );
         call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
         call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
	
         call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
         String serverResponse = (String)call.invoke( new Object[]
           {importURI,newDataHolderFileName} );

         if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: " + serverResponse);
      }
      catch(Exception e)
      {  successStatus = false;
      }

      return successStatus;
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

      try
      {  call = createServerCall();
         call.setOperationName( "copyDataHolder" );		
         call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
         call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
		
         call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
         String serverResponse = (String)call.invoke( new Object[]
           {oldDataHolderFileName,newDataHolderFileName} );

         if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: " + serverResponse);
      }
      catch(Exception e)
      {  successStatus = false;
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

      try
      {  call = createServerCall();
         call.setOperationName( "deleteDataHolder" );			
         call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
		
         call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
	 String serverResponse = (String)call.invoke( new Object[]
           {dataHolderFileName} );

         if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: "+serverResponse);
      }
      catch(Exception e)
      {  successStatus = false;
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
      {  MySpaceMessage message =
           new MySpaceMessage("ERROR_CALL_SERVER_MANAGER");
           message.getMessage(e.toString());
      }	
      return call;
   }   
}
