package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;
import java.util.HashMap;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.apache.axis.AxisProperties;

import org.astrogrid.mySpace.mySpaceStatus.*;
import org.astrogrid.mySpace.mySpaceUtil.*;

import org.apache.log4j.Logger;


/**
 * <p>
 * The MySpaceManager class is the external interface to the
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
 *   <li> upload (import) DataHolder,
 *   <li> lookup DataHolder details,
 *   <li> lookup DataHolders details,
 *   <li> copy DataHolder,
 *   <li> move DataHolder,
 *   <li> export DataHolder,
 *   <li> create container,
 *   <li> delete DataHolder or container.
 * </ul>
 * <p>
 * Each action is implemented as a separate method and this
 * method is a wrap-around for, and has the same name as, a
 * method in the MySpaceActions class.
 * </p>
 * <p>
 * The following operations on a MySpace server are currently
 * supported:
 * </p>
 * <ul>
 *   <li> TBD.
 * </ul>
 *
 * @author C L Qin
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
	private static Properties conProperties = new Properties();
	private static final String REGPATH = "REGISTRYCONF";
	private static final String TEMPPATHTO ="TEMPPATHTO";
	private static String regPathTemp = AxisProperties.getProperty("catalina.home")+"/conf/astrogrid/mySpace/" +"statuscodes.lis";
	private static String serverManagerLoc = "SERVERMANAGER";
	private String errCode="";
	private Vector v = new Vector();
	private String registryName = " ";
	private MySpaceActions msA = new MySpaceActions();


// Constructor.

/**
 * Default constructor.
 */

   public MySpaceManager(){
   	super();
   	status.reset();
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
	loadProperties();
	String path=conProperties.getProperty(TEMPPATHTO); //Server Side file location,hard coded for now should be discussed
	registryName = conProperties.getProperty(REGPATH);
	if( DEBUG )  logger.debug("MySpaceManager.upLoad, getting file path for data holder to store on server." +path);
		try{
			request = util.getRequestAttributes(jobDetails);
			try{
				if(request.get("userID")!=null) userID = request.get("userID").toString();
				if(request.get("communityID")!=null) communityID = request.get("communityID").toString();
				if(request.get("jobID")!=null) jobID = request.get("jobID").toString();				
				if(request.get("newDataHolderName")!=null) newDataHolderName = request.get("newDataHolderName").toString();
				
				try{
					if(request.get("fileSize")!=null) fileSize = Integer.parseInt(request.get("fileSize").toString());
				}catch(NumberFormatException nfe){
					MySpaceMessage message = new MySpaceMessage("NUMBER_FORMAT_ERROR");
					status.addCode(MySpaceStatusCode.NUMBER_FORMAT_ERROR,MySpaceStatusCode.ERROR);
					response = MySpaceStatusCode.NUMBER_FORMAT_ERROR;
					return response;
				}
				//create a instance of DataItemRecord

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

			if ( DEBUG ) logger.debug("About to invoke myspaceaction.importdataholder");  
			msA.setRegistryName(registryName);
			RegistryManager reg = new RegistryManager(registryName);
			String mySpaceFileName = "f" + reg.getNextDataItemID();
			reg.rewriteRegistryFile();
			dataitem = msA.importDataHolder(
			    userID, communityID, jobID, newDataHolderName,
			    mySpaceFileName, fileSize );
			
			if(DEBUG) logger.debug("UploaderroCode is:" +errCode);
			if ( errCode!="" )    
			  errCode = errCode +"," +checkStatus("UPLOADStatusCode");
			else 
			  errCode = checkStatus("UPLOAStatusCode");
			if(DEBUG) logger.debug("UPLOAD CHECKING ATTRIBUTES FOR CREATING DATAITEM:"
			    + userID +"   "+communityID +"   "+jobID +"   "+newDataHolderName +"   "
			      +serverFileName +"   "+fileSize +"   " +"   "+mySpaceFileName+"   registryName:"+registryName);
			
			content = MySpaceUtils.readFromFile(new File(serverFileName));

			call = createServerManagerCall();
			call.setOperationName( "saveDataHolder" );			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
		
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			path = path+mySpaceFileName;
			String serverResponse = (String)call.invoke( new Object[] {content,path} );
			if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: "+serverResponse);
			
			//use serverResponse to build returnStatus and details for datacentre/portal
			if(serverResponse.startsWith(SUCCESS)){
				returnStatus = SUCCESS;
			}else if(serverResponse.startsWith(FAULT)){
				int len = serverResponse.length()-1;
				details = serverResponse.trim().substring(5,len);
				response = util.buildMySpaceManagerResponse(null,FAULT,details,"");
				return response;
			}
	
		//   Get other stuff which can usefully be returned.
		//   (Note that the current date needs to be returned to facilitate
		//   comparisons with the expiry date; the MySpace system might be in
		//   a different time-zone to the Explorer or portal.)
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
			if(dataitem!=null){
				response = util.buildMySpaceManagerResponse(dataitem, returnStatus, details,"");
				if (successStatus){
					if (errCode=="")
					  response = util.buildMySpaceManagerResponse(dataitem, SUCCESS, "","");	
					else
					  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,""); 	    	
				}else {
					v = status.getCodes();
					for(int i=0;i<=v.size();i++){
						MySpaceStatusCode currCode = (MySpaceStatusCode)v.elementAt(i); 
						errCode=errCode+","+currCode.getCode();
						response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");					
					}		    	
				}	
			} else{
				status.addCode(MySpaceStatusCode.MS_E_FLCRTDH,MySpaceStatusCode.ERROR);
				MySpaceMessage message =  new MySpaceMessage("MS_E_FLCRTDH");
				if(errCode=="")
				  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_FLCRTDH,"");
				else
				  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");  
				return response;
			}
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.MS_E_UPLOAD,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_UPLOAD");
			if(errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_UPLOAD,"");
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,""); 		  
			return response;
		}
		return response;
	
}

/**
  * Lookup the details of a single DataHolder.
  */

   public String lookupDataHolderDetails(String jobDetails){
	if ( DEBUG )  logger.debug("MySpaceManager.lookupDataHolderDetails");
	DataItemRecord dataitem = null;
	loadProperties();
	registryName = conProperties.getProperty(REGPATH);	
	
		try{
			request = util.getRequestAttributes(jobDetails);
			try{
				if(request.get("userID")!=null) userID = request.get("userID").toString();
				if(request.get("communityID")!=null) communityID = request.get("communityID").toString();
				if(request.get("jobID")!=null) jobID = request.get("jobID").toString();
				if(request.get("serverFileName")!=null) serverFileName = request.get("serverFileName").toString();				
				if ( DEBUG ) logger.debug(userID +"   "+communityID+"   " + jobID+"   " + 
                     "SERVERFILENAME = "+serverFileName +"registryName= "+registryName);
				
				msA.setRegistryName(registryName);
				Vector dataItemRecords = msA.lookupDataHoldersDetails(
				  userID, communityID, jobID, serverFileName);
				
				if ( errCode!="" )
				  errCode = errCode +"," +checkStatus("LOOKUPDATAHOLDERDETAILS STATUS LOOKUPDATAHOLDERsDETAILS");
				else 
				  errCode = checkStatus("LOOKUPDATAHOLDERDETAILS STATUS LOOKUPDATAHOLDERsDETAILS");
				if (dataItemRecords != null)
				{  DataItemRecord dataItem = (DataItemRecord)dataItemRecords.elementAt(0);
					dataItemID = dataItem.getDataItemID();
				   logger.debug("LOOKUPDATAHOLDERDETAILS TRING TO GET DATAITEMID: " +dataItemID);
				}else{
					logger.debug("LOOKUPDATAHOLDERDETAILS DATAITEMRCORDS = NULL!");
				}
				  
				//create a instance of DataItemRecord
				dataitem = msA.lookupDataHolderDetails(
				userID,communityID, jobID, dataItemID);	
				if ( errCode!="" )
				  errCode = errCode +"," +checkStatus("LOOKUPDATAHOLDERDETAILS STATUS AFTERCALLING MSA.LOOKUPDATAHOLDERDETAILS ");
				else
				  errCode = checkStatus("LOOKUPDATAHOLDERDETAILS STATUS AFTERCALLING MSA.LOOKUPDATAHOLDERDETAILS ");

			}catch(NullPointerException npe){
				MySpaceMessage message = new MySpaceMessage("NULL_POINTER_GETTING_REQUEST");
				status.addCode(MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST,MySpaceStatusCode.ERROR);
				response = MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST;
				return response;
			}
							
		//   Get other stuff which can usefully be returned.
		//   (Note that the current date needs to be returned to facilitate
		//   comparisons with the expiry date; the MySpace system might be in
		//   a different time-zone to the Explorer or portal.)
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
		if(dataitem!=null){
			if (successStatus){
				if (errCode=="")
				  response = util.buildMySpaceManagerResponse(dataitem, SUCCESS, "","");	
				else
				  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");	    	
			}else {
				v = status.getCodes();
				for(int i=0;i<=v.size();i++){
					MySpaceStatusCode currCode = (MySpaceStatusCode)v.elementAt(i); 
					errCode=errCode+","+currCode.getCode();
					response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");					
				}		    	
			}	
		} else{
			status.addCode(MySpaceStatusCode.MS_E_FLCRTDH,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_FLCRTDH");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_FLCRTDH,"");  
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");
			return response;
		}
			
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.MS_E_LOOKUP_DATAHOLDER,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_LOOKUP_DATAHOLDER");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_LOOKUP_DATAHOLDER,""); 
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");
			
			return response;
		}
   }

// -----------------------------------------------------------------

/**
  * Lookup the details of a named set of DataHolders.
  */

   public String lookupDataHoldersDetails(String jobDetails){
	if ( DEBUG )  logger.debug("MySpaceManager.lookupDataHolderSDetails ");
	DataItemRecord dataitem = null;
	Vector itemRecVector = new Vector();
	loadProperties();
	registryName = conProperties.getProperty(REGPATH);	
		try{
			request = util.getRequestAttributes(jobDetails);
			try{
				if(request.get("userID")!=null) userID = request.get("userID").toString();
				if(request.get("communityID")!=null) communityID = request.get("communityID").toString();
				if(request.get("jobID")!=null) jobID = request.get("jobID").toString();
				if(request.get("query")!=null) query = request.get("query").toString();			
				

			}catch(NullPointerException npe){
				MySpaceMessage message = new MySpaceMessage("NULL_POINTER_GETTING_REQUEST");
				status.addCode(MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST,MySpaceStatusCode.ERROR);
				response = MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST;
				return response;
			}
			
			//registry is returning a null pointer so comment out for now awaiting for new code for MySpaceActions to be completed.
			logger.debug("REGISTRYNAME IN LOOKUPDATAHOLDERSDETAILS: "+registryName);
			msA.setRegistryName(registryName);
			itemRecVector = msA.lookupDataHoldersDetails( userID, 
			communityID, jobID, query);		

			MySpaceStatus stat1 = new MySpaceStatus();
			boolean successStat = stat1.getSuccessStatus();
			Vector err = stat1.getCodes();
			for (int i =0;i<err.size();i++){
				MySpaceStatusCode code = (MySpaceStatusCode)err.elementAt(i);
				String codeS = code.getCode();
				if (DEBUG)logger.debug("STATUS CODE IN LOOKUPDATAHOLERSDETAILS IS: " +codeS);
			}
			if(DEBUG) logger.debug("SATAUES is :" +successStat);
			if(DEBUG) logger.debug("LOOKUPDATAHOLDERSDETAILS CHECKING ATTRIBUTES FOR CREATING DATAITEM:"
				+ userID +"   "+communityID +"   "+jobID +"   "+newDataHolderName +"   "
				  +serverFileName +"   "+fileSize +"   " +"   registryName:"+registryName);
								
	
		//   Get other stuff which can usefully be returned.
		//   (Note that the current date needs to be returned to facilitate
		//   comparisons with the expiry date; the MySpace system might be in
		//   a different time-zone to the Explorer or portal.)
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
		    String header = "";
		    String footer = "";
		    String element = "";
		    logger.debug("before getting vection size:");
		    if(itemRecVector!=null){
		    	header = util.buildMySpaceManagerResponseHeader( SUCCESS, "");
		    	footer = util.buildMySpaceManagerResponseFooter();
			    logger.debug("DATAITEMRECORD VEC SIZE: "+itemRecVector.size());
			    for(int i =0; i<itemRecVector.size();i++){
					if(itemRecVector.elementAt(i)!=null){
						logger.debug("STATUS: "+successStatus +"   "+warningStatus);
						if (successStatus){
							logger.debug("i = "+i);
							element = element+util.buildMySpaceManagerResponseElement((DataItemRecord)itemRecVector.elementAt(i), SUCCESS, "");
							logger.debug("GETTING MULTI RESPONSE FOR ELEMENTS: "+response);	    	
						}else {
							v = status.getCodes();
							for(int j=0;i<=v.size();j++){
								MySpaceStatusCode currCode = (MySpaceStatusCode)v.elementAt(i); 
								errCode=errCode+","+currCode.getCode();
								response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");
								logger.debug("ERROR LOOKUPDATAHOLDERSDETAILS: RESPONSE =" +response);
								return response;					
							}		    	
						}				
					}else{
						status.addCode(MySpaceStatusCode.MS_E_FLCRTDH,MySpaceStatusCode.ERROR);
						MySpaceMessage message =  new MySpaceMessage("MS_E_FLCRTDH");
						if (errCode=="")
						  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_FLCRTDH,"");  
						else
						  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");
						
						return response;
					}				
			    }
		    }else if(itemRecVector==null){
		    	element = util.buildMySpaceManagerResponse(null,"WARNING","NO DATAITEMRECORD RETURND FOR YOUR QUERY","");
			}
			response = header+element+footer;
			if( DEBUG ) logger.debug("LOOKUPDATAHOLDERSDETAILS: RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.MS_E_LOOKUP_DATAHOLDERS,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_LOOKUP_DATAHOLDERS");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_LOOKUP_DATAHOLDERS,"");   
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");			
			return response;
		}
		
   }

// -----------------------------------------------------------------

/**
  * Copy a DataHolder from one location on a MySpace server to another
  * location on the same server.
  */

   public String copyDataHolder(String jobDetails)
   {
	if ( DEBUG )  logger.debug("MySpaceManager.copyDataHolder");
	DataItemRecord dataitem = null;
	loadProperties();
	registryName = conProperties.getProperty(REGPATH);	
	
		try{
			request = util.getRequestAttributes(jobDetails);
			try{
				if(request.get("userID")!=null) userID = request.get("userID").toString();
				if(request.get("communityID")!=null) communityID = request.get("communityID").toString();
				if(request.get("jobID")!=null) jobID = request.get("jobID").toString();				
				if(request.get("newDataItemName")!=null) newDataItemName = request.get("newDataItemName").toString();
				if(request.get("serverFileName")!=null) serverFileName = request.get("serverFileName").toString();
				if ( DEBUG ) logger.debug(userID +"   "+communityID+"   " + jobID+"   " + newDataItemName +"SERVERFILENAME = "+serverFileName);
				
				msA.setRegistryName(registryName);
				Vector dataItemRecords = msA.lookupDataHoldersDetails(
				  userID, communityID, jobID, serverFileName);
				if ( errCode!="" )
				  errCode = errCode +"," +checkStatus("COPYDATAHOLDERS STATUS ");
				else
				  errCode = checkStatus("COPYDATAHOLDERS STATUS ");
				if (dataItemRecords != null)
				{  DataItemRecord dataItem = (DataItemRecord)dataItemRecords.elementAt(0);
				   oldDataItemID = dataItem.getDataItemID();
				   logger.debug("TRING TO GET OLDDATAITEMID: " +oldDataItemID);
				}else{
					logger.debug("DATAITEMRCORDS = NULL!");
				}
				  
				//create a instance of DataItemRecord
				dataitem = msA.copyDataHolder(
					userID, communityID, jobID, oldDataItemID, newDataItemName);
				if ( errCode!="" )	
				  errCode = errCode +"," +checkStatus("COPYDATAHOLDERS STATUS AFTERCALLING MSA.COPYDATAHOLDER ");
				else
				  errCode = checkStatus("COPYDATAHOLDERS STATUS AFTERCALLING MSA.COPYDATAHOLDER ");

			}catch(NullPointerException npe){
				MySpaceMessage message = new MySpaceMessage("NULL_POINTER_GETTING_REQUEST");
				status.addCode(MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST,MySpaceStatusCode.ERROR);
				response = MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST;
				return response;
			}
			
		
		//   Get other stuff which can usefully be returned.
		//   (Note that the current date needs to be returned to facilitate
		//   comparisons with the expiry date; the MySpace system might be in
		//   a different time-zone to the Explorer or portal.)
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
		if(dataitem!=null){
			response = util.buildMySpaceManagerResponse(dataitem, returnStatus, details,"");
			if (successStatus){
				if (errCode=="")
				  response = util.buildMySpaceManagerResponse(dataitem, SUCCESS, "","");	
				else
				  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");	
			}else {
				v = status.getCodes();
				for(int i=0;i<=v.size();i++){
					MySpaceStatusCode currCode = (MySpaceStatusCode)v.elementAt(i); 
					errCode=errCode+","+currCode.getCode();
					response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");					
				}		    	
			}	
		} else{
			status.addCode(MySpaceStatusCode.MS_E_FLCRTDH,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_FLCRTDH");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_FLCRTDH,"");  
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");			
			  
			return response;
		}

			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR ERR_COPY_DATA_HOLDER MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.ERR_COPY_DATA_HOLDER,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("ERR_COPY_DATA_HOLDER");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.ERR_COPY_DATA_HOLDER,"");   
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");						
			 
			return response;
		}

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
	DataItemRecord dataitem = null;
	loadProperties();
	registryName = conProperties.getProperty(REGPATH);	
	
		try{
			request = util.getRequestAttributes(jobDetails);
			try{
				if(request.get("userID")!=null) userID = request.get("userID").toString();
				if(request.get("communityID")!=null) communityID = request.get("communityID").toString();
				if(request.get("jobID")!=null) jobID = request.get("jobID").toString();				
				if(request.get("newDataItemName")!=null) newDataItemName = request.get("newDataItemName").toString();
				if(request.get("serverFileName")!=null) serverFileName = request.get("serverFileName").toString();
				logger.debug(userID +"   "+communityID+"   " + jobID+"   " + newDataItemName +"SERVERFILENAME = "+serverFileName
				   +"  registryName = "+registryName);
				
				msA.setRegistryName(registryName);
				Vector dataItemRecords = msA.lookupDataHoldersDetails(
				  userID, communityID, jobID, serverFileName);
				if ( errCode!="" )
				  errCode = errCode +"," +checkStatus("moveDATAHOLDERS STATUS ");
				else
				  errCode = checkStatus("moveDATAHOLDERS STATUS ");
				if (dataItemRecords != null){
					DataItemRecord dataItem = (DataItemRecord)dataItemRecords.elementAt(0);
				    oldDataItemID = dataItem.getDataItemID();
				    logger.debug("TRING TO GET OLDDATAITEMID: " +oldDataItemID);
				}else{
					logger.debug("DATAITEMRCORDS = NULL!");
				}
				  
				//create a instance of DataItemRecord
				dataitem = msA.moveDataHolder(
					userID, communityID, jobID, oldDataItemID, newDataItemName);
				if ( errCode!="" )	
				  errCode = errCode +"," +checkStatus("moveDATAHOLDERS STATUS AFTERCALLING MSA.moveDATAHOLDER ");
				else
				  errCode = checkStatus("moveDATAHOLDERS STATUS AFTERCALLING MSA.moveDATAHOLDER ");

			}catch(NullPointerException npe){
				MySpaceMessage message = new MySpaceMessage("NULL_POINTER_GETTING_REQUEST");
				status.addCode(MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST,MySpaceStatusCode.ERROR);
				response = MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST;
				return response;
			}
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	        if( DEBUG ) logger.debug("SUCCESSSTATUS = "+successStatus);
		//   Format and return the results as XML.
			if (successStatus){
				if (errCode=="")
				  response = util.buildMySpaceManagerResponse(dataitem, SUCCESS, "","");
				else
				  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");					  		    	
			}else {
				v = status.getCodes();
				for(int i=0;i<=v.size();i++){
					MySpaceStatusCode currCode = (MySpaceStatusCode)v.elementAt(i); 
					errCode=errCode+","+currCode.getCode();
					if( DEBUG ) logger.debug("ERRCODE = "+errCode);
					response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");					
				}		    	
			}	
			
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR MOVING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.MS_E_FLMOVDH,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_FLMOVDH");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_FLMOVDH,"");   
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");						
			 
			return response;
		} 	
   }


// -----------------------------------------------------------------

/**
  * Export a DataHolder from the MySpace system.  In Iteration 2 a
  * DataHolder is exported by returning a URL from which a copy of it
  * can be retrieved.
  */

   public String exportDataHolder(String jobDetails){
	if ( DEBUG )  logger.debug("MySpaceManager.exportDataHolder");
	DataItemRecord dataitem = null;
	String dataHolderURI = "";
	loadProperties();
	registryName = conProperties.getProperty(REGPATH);	
	
		try{
			request = util.getRequestAttributes(jobDetails);
			try{
				if(request.get("userID")!=null) userID = request.get("userID").toString();
				if(request.get("communityID")!=null) communityID = request.get("communityID").toString();
				if(request.get("jobID")!=null) jobID = request.get("jobID").toString();				
				if(request.get("serverFileName")!=null) serverFileName = request.get("serverFileName").toString();				
				if ( DEBUG ) logger.debug(userID +"   "+communityID+"   " + jobID+"   " + 
					 "SERVERFILENAME = "+serverFileName +"registryName= "+registryName);
				
				msA.setRegistryName(registryName);
				Vector dataItemRecords = msA.lookupDataHoldersDetails(
				  userID, communityID, jobID, serverFileName);
				
				if ( errCode!="" )
				  errCode = errCode +"," +checkStatus("EXPORTDATAHOLDER STATUS LOOKUPDATAHOLDERsDETAILS");
				else
				  errCode = checkStatus("EXPORTDATAHOLDER STATUS LOOKUPDATAHOLDERsDETAILS");
				if (dataItemRecords != null)
				{  DataItemRecord dataItem = (DataItemRecord)dataItemRecords.elementAt(0);
					dataItemID = dataItem.getDataItemID();
				   logger.debug("EXPORTDATAHOLDER TRING TO GET DATAITEMID: " +dataItemID);
				}else{
					logger.debug("EXPORTDATAHOLDER DATAITEMRCORDS = NULL!");
				}
				  
				//create a instance of DataItemRecord
				dataHolderURI = msA.exportDataHolder(
				userID,communityID, jobID, dataItemID);	
				if ( errCode!="" )
				  errCode = errCode +"," +checkStatus("EXPORTDATAHOLDER STATUS AFTERCALLING MSA.LOOKUPDATAHOLDERSDETAILS ");
				else
				  errCode = checkStatus("EXPORTDATAHOLDER STATUS AFTERCALLING MSA.LOOKUPDATAHOLDERSDETAILS ");
				if ( DEBUG ) logger.debug("EXPORT: DATAHOLDERURI = "+dataHolderURI);

			}catch(NullPointerException npe){
				MySpaceMessage message = new MySpaceMessage("NULL_POINTER_GETTING_REQUEST");
				status.addCode(MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST,MySpaceStatusCode.ERROR);
				response = MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST;
				return response;
			}
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
			if (successStatus){
				if (errCode=="")
				  response = util.buildMySpaceManagerResponse(null, SUCCESS, "",dataHolderURI);
                else
				  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");				 		    	
			}else {
				v = status.getCodes();
				for(int i=0;i<=v.size();i++){
					MySpaceStatusCode currCode = (MySpaceStatusCode)v.elementAt(i); 
					errCode=errCode+","+currCode.getCode();
					response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");					
				}		    	
			}	

			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR EXPORT MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.MS_E_EXPORT,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_EXPORT");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_EXPORT,dataHolderURI);    
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");	
			
			return response;
		} 	
   }

// -----------------------------------------------------------------

/**
  * Create a new container.  The  operation is implemented by creating
  * a new entry in the MySpace registry.  The MySpace server is not
  * touched.
  */

   public String createContainer(String jobDetails){
	if ( DEBUG )  logger.debug("MySpaceManager.createContainer");
	DataItemRecord dataitem = null;
	loadProperties();
	registryName = conProperties.getProperty(REGPATH);	
	
		try{
			request = util.getRequestAttributes(jobDetails);
			try{
				if(request.get("userID")!=null) userID = request.get("userID").toString();
				if(request.get("communityID")!=null) communityID = request.get("communityID").toString();
				if(request.get("jobID")!=null) jobID = request.get("jobID").toString();				
				if(request.get("newContainerName")!=null) newContainerName = request.get("newContainerName").toString();
				if ( DEBUG ) logger.debug(userID +"   "+communityID+"   " + jobID+"   " + newDataItemName +"SERVERFILENAME = "+newContainerName);

				msA.setRegistryName(registryName);
				dataitem = msA.createContainer(
				userID, communityID, jobID, newContainerName);		
				if ( errCode!="" )
				  errCode = errCode +"," +checkStatus("CREATECONTAINER STATUS ");	
				else
				  errCode = checkStatus("CREATECONTAINER STATUS ");	

			}catch(NullPointerException npe){
				MySpaceMessage message = new MySpaceMessage("NULL_POINTER_GETTING_REQUEST");
				status.addCode(MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST,MySpaceStatusCode.ERROR);
				response = MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST;
				return response;
			}
			
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
		if(dataitem!=null){
			if (successStatus){
				if (errCode=="")
				  response = util.buildMySpaceManagerResponse(dataitem, SUCCESS, "","");		    	
				else
				response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");
			}else {
				v = status.getCodes();
				for(int i=0;i<=v.size();i++){
					MySpaceStatusCode currCode = (MySpaceStatusCode)v.elementAt(i); 
					errCode=errCode+","+currCode.getCode();
					response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");					
				}		    	
			}	
		} else{
			status.addCode(MySpaceStatusCode.MS_E_FLCRTDH,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_FLCRTDH");
			response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_FLCRTDH,"");  
			return response;
		}
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.MS_E_FLMOVDH,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_FLMOVDH");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_FLMOVDH,"");    
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");	
			
			return response;
		} 	
   }


// -----------------------------------------------------------------

/**
  * Delete a DataHolder or container from a MySpace server.
  */

   public String deleteDataHolder(String jobDetails){
	if ( DEBUG )  logger.debug("MySpaceManager.deleteDataHolder");
	boolean isDeleted = false;
	DataItemRecord dataitem = null;
	loadProperties();
	registryName = conProperties.getProperty(REGPATH);	
	
		try{
			request = util.getRequestAttributes(jobDetails);
			try{
				if(request.get("userID")!=null) userID = request.get("userID").toString();
				if(request.get("communityID")!=null) communityID = request.get("communityID").toString();
				if(request.get("jobID")!=null) jobID = request.get("jobID").toString();				
				if(request.get("serverFileName")!=null) serverFileName = request.get("serverFileName").toString();
				
				if( DEBUG ) logger.debug("GETTING ATTRIBUTES DELETE:"+ userID +"   "+communityID +"   "
				    +jobID +"   "+"serverFileName :" +serverFileName);
				
				//create a instance of DataItemRecord
				msA.setRegistryName(registryName);
				Vector dataItemRecords = msA.lookupDataHoldersDetails(
				  userID, communityID, jobID, serverFileName);
				if (dataItemRecords != null)
				{  DataItemRecord dataItem = (DataItemRecord)dataItemRecords.elementAt(0);
				   dataItemID = dataItem.getDataItemID();
				   logger.debug("TRING TO GET dATAITEMID: " +dataItemID);
				}else{
					logger.debug("DATAITEMRCORDS = NULL!");
				}				
				isDeleted = msA.deleteDataHolder( userID, communityID,
				jobID, dataItemID);	
				if ( errCode!="" )	
				  errCode = errCode +"," +checkStatus("DELETEDATAHOLDERS STATUS ");
				else
				  errCode = checkStatus("DELETEDATAHOLDERS STATUS ");
				
			}catch(NullPointerException npe){
				MySpaceMessage message = new MySpaceMessage("NULL_POINTER_GETTING_REQUEST");
				status.addCode(MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST,MySpaceStatusCode.ERROR);
				response = MySpaceStatusCode.NULL_POINTER_GETTING_REQUEST;
				return response;
			}
			

			//registry is returning a null pointer so comment out for now awaiting for new code for MySpaceActions to be completed.
			//dataitem = myspace.importDataHolder(userID,communityID,jobID,newDataHolderName,serverFileName,fileSize);
					
	
		//   Get other stuff which can usefully be returned.
		//   (Note that the current date needs to be returned to facilitate
		//   comparisons with the expiry date; the MySpace system might be in
		//   a different time-zone to the Explorer or portal.)
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
		if ( DEBUG )  logger.debug("SUCCESSSTATUS = "+successStatus +"ISDELETED IS: " +isDeleted);
		if(isDeleted){		
			if (successStatus){
				if (errCode=="")
				  response = util.buildMySpaceManagerResponse(null, SUCCESS, "","");	
				else	
				  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");    	
			}else {
				v = status.getCodes();
				for(int i=0;i<=v.size();i++){
					MySpaceStatusCode currCode = (MySpaceStatusCode)v.elementAt(i); 
					errCode=errCode+","+currCode.getCode();
					response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");					
				}		    	
			}	
		} else{
			status.addCode(MySpaceStatusCode.MS_E_FLCRTDH,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("MS_E_FLCRTDH");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.MS_E_FLCRTDH,"");   
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");	
			 
			return response;
		} 
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.ERR_DELETE_DATA_HOLDER,MySpaceStatusCode.ERROR);
			MySpaceMessage message =  new MySpaceMessage("ERR_DELETE_DATA_HOLDER");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,FAULT,MySpaceStatusCode.ERR_DELETE_DATA_HOLDER,"");    
			else
			  response = util.buildMySpaceManagerResponse(null,FAULT,errCode,"");	
			 
			return response;
		} 

   }

// =================================================================

//
// Server methods.
//
// The followng methods are provided to access a MySpace server.
private Call createServerManagerCall(){
	loadProperties();
	String servermanagerloc = conProperties.getProperty(serverManagerLoc);
	Call call = null;
	try{
		//String endpoint  = "http://localhost:8080/axis/services/ServerManager";
		String endpoint  = servermanagerloc;
		if ( DEBUG ) logger.debug("servermanagerloc = "+servermanagerloc);
		Service service = new Service();
		call = (Call)service.createCall();
		call.setTargetEndpointAddress( new java.net.URL(endpoint) );
		
	}catch(Exception e){
		MySpaceMessage message = new MySpaceMessage("ERROR_CALL_SERVER_MANAGER");
		message.getMessage(e.toString());
	}	
	return call;
}

    private void loadProperties(){
		try {
			FileInputStream istream = new FileInputStream( regPathTemp );
		    conProperties.load(istream);
			istream.close();
		}catch ( IOException ex ) {
			if (DEBUG)  logger.error("MYSPACEUTILS IO EXCEPTION :" +ex.getMessage());
			}
    }
    
    private String checkStatus(String message){
		MySpaceStatus stat1 = new MySpaceStatus();
		boolean successStat = stat1.getSuccessStatus();
		String errCodes = "";
		if ( DEBUG ) logger.debug("inside checkStatus, successStat = " +successStat);
		if (!successStat){
			errCodes = getErrorCode(stat1, message);
		}
		return errCodes;
    }
    
    private String getErrorCode(MySpaceStatus stat1, String message){
    	String codeS ="";
		Vector err = stat1.getCodes();
		for (int i =0;i<err.size();i++){
			MySpaceStatusCode code = (MySpaceStatusCode)err.elementAt(i);
			if ( codeS==""){
				codeS = code.getCode();
				logger.debug("dddddddddddddddd" +codeS +"dddddd");
			}else{
				codeS = codeS +"," +code.getCode();
				}
			if (DEBUG)logger.debug("STATUS CODE IS: " +message +"  "+codeS);
		}    	
    	return codeS;
    }
}


