package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;
import java.util.HashMap;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.astrogrid.mySpace.mySpaceStatus.*;
import org.astrogrid.mySpace.mySpaceUtil.*;

import org.apache.log4j.Logger;


/**
 * <p>
 * The mySpaceInterface class is the external interface to the
 * MySpace system.  It handles all interaction between a MySpace
 * manager and external components.  In particular it provides
 * methods to:
 * </p>
 * <ul>
 *   <li> implement all the actions which the MySpace system
 *    can perform,
 *   <li> perform operations on the MySpace server.
 * </ul>
 * <p>
 * The following actions are currently supported:
 * </p>
 * <ul>
 *   <li> lookup DataHolders details,
 *   <li> lookup DataHolder details,
 *   <li> copy DataHolder,
 *   <li> move DataHolder,
 *   <li> export DataHolder,
 *   <li> create container,
 *   <li> delete DataHolder or container.
 * </ul>
 * <p>
 * Each action is implemented as a separate method and this
 * method is a wrap-around for, and has the same name as, a
 * method in the MySpaceManager class.
 * </p>
 * <p>
 * The following operations on a MySpace server are currently
 * supported:
 * </p>
 * <ul>
 *   <li> TBD.
 * </ul>
 * 
 * @author A C Davenhall
 * @version Iteration 2.
 */

public class MySpaceManager{
	private static Logger logger = Logger.getLogger(MySpaceUtils.class);
	private static boolean DEBUG = true;
	private static MySpaceStatus status = new MySpaceStatus();
	private MySpaceUtils util = new MySpaceUtils();
	private MySpaceActions myspace = new MySpaceActions();
	private HashMap request = new HashMap();
	private String response = " ";
	private String userID = " ";
	private String communityID = " ";
	private String jobID = " ";
	private String query = " ";
	private int oldDataItemID = 0;
	private String newDataItemName = " ";
	private int dataItemID = 0;
	private String newContainerName = " ";
	private String returnStatus = " "; //if the method is completed successful
	private String details = " "; //details of the error message
	private String content = " "; //content of the file from datacentre
	private String serverFileName = " "; //file location in user's machine
	private String newDataHolderName = " ";
	private int fileSize = 1;
	private static final String SUCCESS = "SUCCESS";
	private static final String FAULT = "FAULT";

// Constructor.

/**
 * Default constructor.
 */

   public MySpaceManager(){
   	super();
   }

// =================================================================

//
// Action methods.
//
// Each of the following methods implements one of the actions which
// the MySpace system can perform.

// -----------------------------------------------------------------

/**
 * Upload/save dataholder.
 */
public String upLoad(String jobDetails){
	if ( DEBUG )  logger.debug("MySpaceManager.upLoad");
	DataItemRecord dataitem = null;
	Call call = null;
	String path="/tmp/test"; //hard coded for now should be discussed
		try{
			request = util.getRequestAttributes(jobDetails);
			try{
				if(request.get("userID")!=null) userID = request.get("userID").toString();
				if(request.get("communityID")!=null) communityID = request.get("communityID").toString();
				if(request.get("jobID")!=null) jobID = request.get("jobID").toString();				
				if(request.get("newDataHolderName")!=null) newDataHolderName = request.get("newDataHolderName").toString();
				
				try{
					if(request.get("dataItemID")!=null) dataItemID = Integer.parseInt(request.get("dataItemID").toString());
					if(request.get("fileSize")!=null) fileSize = Integer.parseInt(request.get("fileSize").toString());
				}catch(NumberFormatException nfe){
					MySpaceMessage message = new MySpaceMessage("NUMBER_FORMAT_ERROR");
					status.addCode(MySpaceStatusCode.NUMBER_FORMAT_ERROR,MySpaceStatusCode.ERROR);
					response = MySpaceStatusCode.NUMBER_FORMAT_ERROR;
					return response;
				}
				//create a instance of DataItemRecord
				dataitem = new DataItemRecord("",  dataItemID, "", userID,  Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), fileSize,  0,  "");

			}catch(NullPointerException npe){
				MySpaceMessage message = new MySpaceMessage("NULL_POINTER_GETTING_REQUEST");
				status.addCode(MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST,MySpaceStatusCode.ERROR);
				response = MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST;
				return response;
			}
			
			if ( DEBUG ){
				logger.debug("serverFileName Existans is "+request.containsKey("serverFileName") +"SIZE of request keys: "+request.size());
				Object o[] = request.keySet().toArray();
				for(int i=0;i<o.length;i++){
					logger.debug("NameSetOfRequest: "+o[i] +request.get(o[i]).toString());
					}
			}
			if ( DEBUG )logger.debug("FILE lOCATION: "+request.get("serverFileName"));
			serverFileName = request.get("serverFileName").toString();
			if ( DEBUG ) logger.debug(userID+"  "+communityID +"   "+jobID+"   "+newDataHolderName+"   "+fileSize+"   "+" serverFileName == "+serverFileName);
			
			//registry is returning a null pointer so comment out for now awaiting for new code for MySpaceActions to be completed.
			//dataitem = myspace.importDataHolder(userID,communityID,jobID,newDataHolderName,serverFileName,fileSize);
			
			content = MySpaceUtils.readFromFile(new File(serverFileName));

			call = createServerManagerCall();
			call.setOperationName( "saveDataHolder" );			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
		
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			String serverResponse = (String)call.invoke( new Object[] {content,path} );
			if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: "+serverResponse);
			
			//use serverResponse to build returnStatus and details for datacentre/portal
			if(serverResponse.startsWith(SUCCESS)){
				returnStatus = SUCCESS;
			}else if(serverResponse.startsWith(FAULT)){
				int len = serverResponse.length()-1;
				details = serverResponse.trim().substring(5,len);
			}
	
		//   Get other stuff which can usefully be returned.
		//   (Note that the current date needs to be returned to facilitate
		//   comparisons with the expiry date; the MySpace system might be in
		//   a different time-zone to the Explorer or portal.)
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
		
			response = util.buildMySpaceManagerResponse(dataitem, returnStatus, details,""); 
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.MS_E_FLMOVDH,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_FLMOVDH");
			response = MySpaceStatusCode.MS_E_FLMOVDH;  
			return response;
		}
		return response;
	
}

/**
  * Lookup the details of a single DataHolder.
  */

   public String lookupDataHolderDetails(String jobDetails)
   {

//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

     //MySpaceActions myspace = new MySpaceActions();

      DataItemRecord dataItem = myspace.lookupDataHolderDetails(
        userID, communityID, jobID, dataItemID);

//
//   Get other stuff which can usefully be returned.
//   (Note that the current date needs to be returned to facilitate
//   comparisons with the expiry date; the MySpace system might be in
//   a different time-zone to the Explorer or portal.)

      boolean successStatus = status.getSuccessStatus();
      boolean warningStatus = status.getWarningStatus();

      Date currentMySpaceDate = new Date();

//
//   Format and return the results as XML.

//   ................

      String xmlString = "";
      return xmlString;
   }

// -----------------------------------------------------------------

/**
  * Lookup the details of a named set of DataHolders.
  */

   public String lookupDataHoldersDetails(String jobDetails){

//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      //MySpaceActions myspace = new MySpaceActions();

      Vector itemRecVector = new Vector();
      itemRecVector = myspace.lookupDataHoldersDetails(
        userID, communityID, jobID, query);

//
//   Get other stuff which can usefully be returned.
//   (Note that the current date needs to be returned to facilitate
//   comparisons with the expiry date; the MySpace system might be in
//   a different time-zone to the Explorer or portal.)

      boolean successStatus = status.getSuccessStatus();
      boolean warningStatus = status.getWarningStatus();

      Date currentMySpaceDate = new Date();

//
//   Format and return the results as XML.

//   ................
      return response;
   }

// -----------------------------------------------------------------

/**
  * Copy a DataHolder from one location on a MySpace server to another
  * location on the same server.
  */

   public String copyDataHolder(String jobDetails)
   {

//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      //MySpaceActions myspace = new MySpaceActions();

      DataItemRecord dataitem = myspace.copyDataHolder(
        userID, communityID, jobID, oldDataItemID,
        newDataItemName);

//
//   Get other stuff which can usefully be returned.
//   (Note that the current date needs to be returned to facilitate
//   comparisons with the expiry date; the MySpace system might be in
//   a different time-zone to the Explorer or portal.)

      boolean successStatus = status.getSuccessStatus();
      boolean warningStatus = status.getWarningStatus();

      Date currentMySpaceDate = new Date();

//
//   Format and return the results as XML.

//   ................

      String xmlString = "";
      return xmlString;
   }

// -----------------------------------------------------------------

/**
  * Move a DataHolder from one location on a MySpace server to another
  * location on the same server.
  *
  * The move operation is implemented entirely by manipulating the
  * entries in the MySpace registry.  The DataHolder itself is not
  * touched.
  */

   public String moveDataHolder(String jobDetails){  
   	if ( DEBUG )  logger.debug("MySpaceManager.moveDataHolder");
   	try{
	    request = util.getRequestAttributes(jobDetails);
	    userID = request.get("userID").toString();
	    communityID = request.get("communityID").toString();
	    jobID = request.get("jobID").toString();
	    try{
	    	String idString = request.get("oldDataItemID").toString();
	      	oldDataItemID = Integer.parseInt(idString);
	      	}catch(NumberFormatException nfe){
	      		MySpaceMessage message = new MySpaceMessage("NUMBER_FORMAT_ERROR");
			    status.addCode(MySpaceStatusCode.NUMBER_FORMAT_ERROR,MySpaceStatusCode.ERROR);
			    response = MySpaceStatusCode.NUMBER_FORMAT_ERROR;
	      	    return response;
	    }
	    newDataItemName = request.get("newDataItemName").toString();
	
	//   Create an instance of the MySpace manager and invoke the method
	//   corresponding to this action. 
	    DataItemRecord dataitem = myspace.moveDataHolder(
	        userID, communityID, jobID, oldDataItemID,
	        newDataItemName);
	
	//   Get other stuff which can usefully be returned.
	//   (Note that the current date needs to be returned to facilitate
	//   comparisons with the expiry date; the MySpace system might be in
	//   a different time-zone to the Explorer or portal.)
	
	    boolean successStatus = status.getSuccessStatus();
	    boolean warningStatus = status.getWarningStatus();
	
	    Date currentMySpaceDate = new Date();
	
	//   Format and return the results as XML.
	    response = util.buildMySpaceManagerResponse(dataitem, returnStatus, details,""); 
   	}catch(Exception e){
   		MySpaceMessage message =  new MySpaceMessage("MS_E_FLMOVDH");
		status.addCode(MySpaceStatusCode.MS_E_FLMOVDH,MySpaceStatusCode.ERROR);
		response = MySpaceStatusCode.MS_E_FLMOVDH;  
   		return response;
   	}
    return response;
   }


// -----------------------------------------------------------------

/**
  * Export a DataHolder from the MySpace system.  In Iteration 2 a
  * DataHolder is exported by returning a URL from which a copy of it
  * can be retrieved.
  */

   public String exportDataHolder(String jobDetails)
   {

//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      //MySpaceActions myspace = new MySpaceActions();

      String dataItemURI = myspace.exportDataHolder(
        userID, communityID, jobID, dataItemID);

//
//   Get other stuff which can usefully be returned.
//   (Note that the current date needs to be returned to facilitate
//   comparisons with the expiry date; the MySpace system might be in
//   a different time-zone to the Explorer or portal.)

      boolean successStatus = status.getSuccessStatus();
      boolean warningStatus = status.getWarningStatus();

      Date currentMySpaceDate = new Date();

//
//   Format and return the results as XML.

//   ................

      String xmlString = "";
      return xmlString;
   }

// -----------------------------------------------------------------

/**
  * Create a new container.  The  operation is implemented by creating
  * a new entry in the MySpace registry.  The MySpace server is not
  * touched.
  */

   public String createContainer(String jobDetails)
   {

//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      //MySpaceActions myspace = new MySpaceActions();

      DataItemRecord dataItem = myspace.createContainer(
        userID, communityID, jobID, newContainerName);

//
//   Get other stuff which can usefully be returned.
//   (Note that the current date needs to be returned to facilitate
//   comparisons with the expiry date; the MySpace system might be in
//   a different time-zone to the Explorer or portal.)

      boolean successStatus = status.getSuccessStatus();
      boolean warningStatus = status.getWarningStatus();

      Date currentMySpaceDate = new Date();

//
//   Format and return the results as XML.

//   ................

      String xmlString = "";
      return xmlString;
   }


// -----------------------------------------------------------------

/**
  * Delete a DataHolder or container from a MySpace server.
  */

   public String deleteDataHolder(String jobDetails){
//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      //MySpaceActions myspace = new MySpaceActions();

      boolean deletedOk = myspace.deleteDataHolder(
        userID, communityID, jobID, dataItemID);

//
//   Get other stuff which can usefully be returned.
//   (Note that the current date needs to be returned to facilitate
//   comparisons with the expiry date; the MySpace system might be in
//   a different time-zone to the Explorer or portal.)

      boolean successStatus = status.getSuccessStatus();
      boolean warningStatus = status.getWarningStatus();

      Date currentMySpaceDate = new Date();

//
//   Format and return the results as XML.

//   ................

      String xmlString = "";
      return xmlString;
   }

// =================================================================

//
// Server methods.
//
// The followng methods are provided to access a MySpace server.
private Call createServerManagerCall(){
	Call call = null;
	try{
		String endpoint  = "http://localhost:8080/axis/services/ServerManager";
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
	}catch(Exception e){
		MySpaceMessage message = new MySpaceMessage("ERROR_CALL_SERVER_MANAGER");
		message.getMessage(e.toString());
	}	
	return call;
}


}


