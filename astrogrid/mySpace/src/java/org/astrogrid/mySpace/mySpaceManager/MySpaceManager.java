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
	private String response = "";
	private String userID = "";
	private String communityID = "";
	private String jobID = "";
	private String query = "";
	private int oldDataItemID = 0;
	private String newDataItemName = "";
	private int dataItemID = 0;
	private String newContainerName = "";
	private String returnStatus = ""; //if the method is completed successful
	private String details = ""; //details of the error message
	private String content = ""; //content of the file from datacentre
	private String fileLocation = ""; //file location in user's machine

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
	DataItemRecord dataitem = new DataItemRecord();
	Call call = null;
	//String content="UPLOAD TEST CONTENT";	
	String path="/tmp/test"; //hard coded for now should be discussed
	//if ( DEBUG ) logger.debug("cotent ="+content+"path="+path);
		try{
			request = util.getRequestAttributes(jobDetails);
			logger.debug("FILE LOCATION:......."+request.containsKey("fileLocation") +"SIZE: "+request.size());
			//Object o[] = request.keySet().toArray();
			//logger.debug("ARRAY SIZE: "+o.length);
			//for(int i=0;i<o.length;i++){
				//logger.debug("%%%%%%%"+o[i] +request.get(o[i]).toString());
			//}
			//Enumeration e = request.propertyNames();
			//while(e.hasMoreElements()){
			//	logger.debug("%%%"+e.nextElement());
			//}
			logger.debug("FILE lOCATION: "+request.get("fileLocation"));
			fileLocation = request.get("fileLocation").toString();
			if ( DEBUG ) logger.debug("filelocation == "+fileLocation);
			content = MySpaceUtils.readFromFile(new File(fileLocation));

			call = createServerManagerCall();
			call.setOperationName( "saveDataHolder" );			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
		
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			String serverResponse = (String)call.invoke( new Object[] {content,path} );
			if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: "+serverResponse);
			
			//use serverResponse to build returnStatus and details for datacentre/portal
			//hard code for now:
			returnStatus = "TEST_OK";
	
		//   Get other stuff which can usefully be returned.
		//   (Note that the current date needs to be returned to facilitate
		//   comparisons with the expiry date; the MySpace system might be in
		//   a different time-zone to the Explorer or portal.)
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
		
			response = util.buildMySpaceManagerResponse(dataitem, returnStatus, details); 
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			MySpaceMessage message =  new MySpaceMessage("MS-E-FLMOVDH");
			response = message.getMessage(e.toString());  
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
	            response = message.getMessage(nfe.toString());
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
	    response = util.buildMySpaceManagerResponse(dataitem, returnStatus, details); 
   	}catch(Exception e){
   		MySpaceMessage message =  new MySpaceMessage("MS-E-FLMOVDH");
   		response = message.getMessage(e.toString());  
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


