package org.astrogrid.mySpace.mySpaceManager;

import java.util.*;
import java.util.HashMap;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import org.astrogrid.Configurator;
import org.astrogrid.i18n.*;
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

public class MySpaceManager {
	private static Logger logger = Logger.getLogger(MySpaceUtils.class);
	private static boolean DEBUG = true;
	private static MySpaceStatus status = new MySpaceStatus();
	
	private MySpaceUtils util = new MySpaceUtils();
	private HashMap request = new HashMap();
	private String response = " ";
	private String userID = " ";
	private String communityID = " ";
	private String jobID = " ";
	private String query = " ";
	private int oldDataItemID = 0;
	private String newDataItemName = " ";
	private int extentionPeriod = 0;
	private int dataItemID = 0;
	private String newContainerName = " ";
	private String returnStatus = " "; //if the method is completed successful
	private String details = " "; //details of the error message
	private String content = " "; //content of the file from datacentre
	private String serverFileName = " "; //file location in user's machine
	private String newDataHolderName = " ";
	private String contentsType=" ";
	private String importURI=" ";
	private String fileContent = " ";
	private String category = " ";
	private String action = " ";
	private int fileSize = 1;
	private String errCode="";
	private Vector v = new Vector();
	private String registryName = " ";
	private MySpaceActions msA = new MySpaceActions();


// Constructor.

/**
 * Default constructor.
 */

   public MySpaceManager(){  
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
	
		try{
			response = getValues(jobDetails);

			if ( DEBUG ) logger.debug("About to invoke myspaceaction.importdataholder");  
			msA.setRegistryName(registryName);
			RegistryManager reg = new RegistryManager(registryName);
			String mySpaceFileName = "f" + reg.getNextDataItemID();

			//this need to be considered when to invoke import when to invoke upload.
			reg.rewriteRegistryFile();
			dataitem = msA.upLoadDataHolder(
				userID, communityID, jobID, newDataHolderName,
				mySpaceFileName, contentsType );

			if ( DEBUG ) logger.debug("userid:"+userID+"comID"+communityID+"jobid"+jobID+"newDataHN"+newDataHolderName+"filenm:"+mySpaceFileName
								   +"fileSize"+fileSize);
			if( DEBUG ) logger.debug("UploaderroCode is:" +errCode);
			if ( errCode!="" )    
			  errCode = errCode +"," +checkStatus("UPLOADStatusCode");
			else 
			  errCode = checkStatus("UPLOAStatusCode");
			if(DEBUG) logger.debug("UPLOAD CHECKING ATTRIBUTES FOR CREATING DATAITEM: userid:"
				+ userID +"comID"+communityID +"jobID"+jobID +"newDHName"+newDataHolderName
				+"serverFileName"+serverFileName +"filesize"+fileSize 
				+"mySpaceFileName"+mySpaceFileName+"registryName:"+registryName);
			
			//commented out for testing transfer large files
			//content = MySpaceUtils.readFromFile(new File(serverFileName));
			String contentPath = serverFileName;
			if (DEBUG)  logger.debug("ServerFileName = " +serverFileName);
			call = createServerManagerCall();
			call.setOperationName( "saveDataHolder" );			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			String serverResponse = (String)call.invoke( new Object[] {contentPath,mySpaceFileName} );
			if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: "+serverResponse);
			
			//use serverResponse to build returnStatus and details for datacentre/portal
			if(serverResponse.startsWith(MMC.SUCCESS)){
				returnStatus = MMC.SUCCESS;
			}else if(serverResponse.startsWith(MMC.FAULT)){
				int len = serverResponse.length()-1;
				details = serverResponse.trim().substring(5,len);
				response = util.buildMySpaceManagerResponse(null,MMC.FAULT,details,"");
				return response;
			}
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
			if(dataitem!=null){
				response = util.buildMySpaceManagerResponse(dataitem, returnStatus, details,"");
				if (successStatus){
					if (errCode=="")
					  response = util.buildMySpaceManagerResponse(dataitem, MMC.SUCCESS, "","");	
					else
					  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,""); 	    	
				}else {
					response = getErrorCode();
						    	
				}	
			} else{
				status.addCode(MySpaceStatusCode.AGMMCE00202,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
				AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00202", this.getComponentName()) ;
				if(errCode=="")
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
				else
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");  
				return response;
			}
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.AGMMCE00216,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00216", this.getComponentName()) ;
			if(errCode=="")
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),""); 		  
			return response;
		}
		return response;
	
}

/**
 * Upload/save dataholder.
 */
public String upLoadURL(String jobDetails){
	if ( DEBUG )  logger.debug("MySpaceManager.upLoad");
	DataItemRecord dataitem = null;
	Call call = null;
	
		try{
			response = getValues(jobDetails);

			if ( DEBUG ) logger.debug("About to invoke myspaceaction.importdataholder");  
			msA.setRegistryName(registryName);
			RegistryManager reg = new RegistryManager(registryName);
			String mySpaceFileName = "f" + reg.getNextDataItemID();

			//this need to be considered when to invoke import when to invoke upload.
			reg.rewriteRegistryFile();
			dataitem = msA.importDataHolder(
				userID, communityID, jobID, importURI,
				mySpaceFileName, contentsType );

			if ( DEBUG ) logger.debug("userid:"+userID+"comID"+communityID+"jobid"+jobID+"newDataHN"+newDataHolderName+"filenm:"+mySpaceFileName
								   +"fileSize"+fileSize);
			if( DEBUG ) logger.debug("UploaderroCode is:" +errCode);
			if ( errCode!="" )    
			  errCode = errCode +"," +checkStatus("UPLOADStatusCode");
			else 
			  errCode = checkStatus("UPLOAStatusCode");
			if(DEBUG) logger.debug("UPLOAD CHECKING ATTRIBUTES FOR CREATING DATAITEM: userid:"
				+ userID +"comID"+communityID +"jobID"+jobID +"newDHName"+newDataHolderName
				+"serverFileName"+serverFileName +"filesize"+fileSize 
				+"mySpaceFileName"+mySpaceFileName+"registryName:"+registryName);
			
			//commented out for testing transfer large files
			//content = MySpaceUtils.readFromFile(new File(serverFileName));
			String contentPath = serverFileName;
			if (DEBUG)  logger.debug("ServerFileName = " +serverFileName);
			call = createServerManagerCall();
			call.setOperationName( "saveDataHolder" );			
			call.addParameter("arg0", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("arg1", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType( org.apache.axis.encoding.XMLType.XSD_STRING);
			String serverResponse = (String)call.invoke( new Object[] {contentPath,mySpaceFileName} );
			if ( DEBUG )  logger.debug("GOT SERVERRESPONSE: "+serverResponse);
			
			//use serverResponse to build returnStatus and details for datacentre/portal
			if(serverResponse.startsWith(MMC.SUCCESS)){
				returnStatus = MMC.SUCCESS;
			}else if(serverResponse.startsWith(MMC.FAULT)){
				int len = serverResponse.length()-1;
				details = serverResponse.trim().substring(5,len);
				response = util.buildMySpaceManagerResponse(null,MMC.FAULT,details,"");
				return response;
			}
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
			if(dataitem!=null){
				response = util.buildMySpaceManagerResponse(dataitem, returnStatus, details,"");
				if (successStatus){
					if (errCode=="")
					  response = util.buildMySpaceManagerResponse(dataitem, MMC.SUCCESS, "","");	
					else
					  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,""); 	    	
				}else {
					response = getErrorCode();
						    	
				}	
			} else{
				status.addCode(MySpaceStatusCode.AGMMCE00202,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
				AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00202", this.getComponentName()) ;
				if(errCode=="")
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
				else
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");  
				return response;
			}
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.AGMMCE00216,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00216", this.getComponentName()) ;
			if(errCode=="")
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),""); 		  
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

		try{
			response = getValues(jobDetails);

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
				  response = util.buildMySpaceManagerResponse(dataitem, MMC.SUCCESS, "","");	
				else
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,"");	    	
			}else {
				response = getErrorCode();
					    	
			}	
		} else{
			status.addCode(MySpaceStatusCode.AGMMCE00202,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00202", this.getComponentName()) ;
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");
			return response;
		}
			
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.AGMMCE00218,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00218", this.getComponentName()) ;
			if (errCode=="") 
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");
			return response;
		}
   }

// -----------------------------------------------------------------

/**
  * Lookup the details of a named set of DataHolders.
  */

   public String lookupDataHoldersDetails(String jobDetails){
	if ( DEBUG )  logger.debug("MySpaceManager.lookupDataHolderSDetails CATHERINE"+jobDetails);
	DataItemRecord dataitem = null;
	Vector itemRecVector = new Vector();

		try{
			response = getValues(jobDetails);

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
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
			String header = "";
			String footer = "";
			String element = "";
	
			if(itemRecVector!=null){
				header = util.buildMySpaceManagerResponseHeader( MMC.SUCCESS, "");
				footer = util.buildMySpaceManagerResponseFooter();
				logger.debug("DATAITEMRECORD VEC SIZE: "+itemRecVector.size());
				for(int i =0; i<itemRecVector.size();i++){
					if(itemRecVector.elementAt(i)!=null){
						logger.debug("STATUS: "+successStatus +"   "+warningStatus);
						if (successStatus){
							logger.debug("i = "+i);
							element = element+util.buildMySpaceManagerResponseElement((DataItemRecord)itemRecVector.elementAt(i), MMC.SUCCESS, "");
							logger.debug("GETTING MULTI RESPONSE FOR ELEMENTS: "+response);	    	
						}else {
							response = getErrorCode();
							return response;								    	
						}				
					}else{
						status.addCode(MySpaceStatusCode.AGMMCE00202,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
						AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00202", this.getComponentName()) ;
						if (errCode=="")
						  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");  
						else
						  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");
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
			status.addCode(MySpaceStatusCode.AGMMCE00217,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00217", this.getComponentName()) ;
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");			
			return response;
		}
		
   }

// -----------------------------------------------------------------

/**
  * Copy a DataHolder from one location on a MySpace server to another
  * location on the same server.
  */

   public String copyDataHolder(String jobDetails){
	if ( DEBUG )  logger.debug("MySpaceManager.copyDataHolder "+jobDetails);
	DataItemRecord dataitem = null;

		try{
			response = getValues(jobDetails);

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
				  response = util.buildMySpaceManagerResponse(dataitem, MMC.SUCCESS, "","");	
				else
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,"");	
			}else {
				response = getErrorCode();
						    	
			}	
		} else{
			status.addCode(MySpaceStatusCode.AGMMCE00202,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00202", this.getComponentName()) ;
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");			
			  
			return response;
		}

			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR ERR_COPY_DATA_HOLDER MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.AGMSCE01043,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMSCE01043", this.getComponentName()) ;
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");									 
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

		try{
			response = getValues(jobDetails);
             
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
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
			if( DEBUG ) logger.debug("SUCCESSSTATUS = "+successStatus);
		//   Format and return the results as XML.
			if (successStatus){
				if (errCode=="")
				  response = util.buildMySpaceManagerResponse(dataitem, MMC.SUCCESS, "","");
				else
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,"");					  		    	
			}else {
				response = getErrorCode();
						    	
			}				
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR MOVING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.AGMMCE00205,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00205", this.getComponentName()) ;
			if (errCode=="") 
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");						
			 
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

		try{
			response = getValues(jobDetails);

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
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
			if (successStatus){
				if (errCode=="")
				  response = util.buildMySpaceManagerResponse(null, MMC.SUCCESS, "",dataHolderURI);
				else
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,"");				 		    	
			}else {
				response = getErrorCode();
						    	
			}	

			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR EXPORT MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.AGMMCE00219,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00219", this.getComponentName()) ;
			if (errCode=="")  
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),dataHolderURI);
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");				
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

		try{
			response = getValues(jobDetails);

			msA.setRegistryName(registryName);
			dataitem = msA.createContainer(
			userID, communityID, jobID, newContainerName);		
			if ( errCode!="" )
			  errCode = errCode +"," +checkStatus("CREATECONTAINER STATUS ");	
			else
			  errCode = checkStatus("CREATECONTAINER STATUS ");				
	
			boolean successStatus = status.getSuccessStatus();
			boolean warningStatus = status.getWarningStatus();
	
			Date currentMySpaceDate = new Date();
	
		//   Format and return the results as XML.
		if(dataitem!=null){
			if (successStatus){
				if (errCode=="")
				  response = util.buildMySpaceManagerResponse(dataitem, MMC.SUCCESS, "","");		    	
				else
				response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,"");
			}else {
				response = getErrorCode();
						    	
			}	
		} else{
			status.addCode(MySpaceStatusCode.AGMMCE00202,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00202", this.getComponentName()) ;
			response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			return response;
		}
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.AGMMCE00205,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00205", this.getComponentName()) ;
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");			
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
	
		try{
			response = getValues(jobDetails);

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
				  response = util.buildMySpaceManagerResponse(null, MMC.SUCCESS, "","");	
				else	
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,"");    	
			}else {
				response = getErrorCode();
						    	
			}	
		} else{
			status.addCode(MySpaceStatusCode.AGMMCE00202,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			MySpaceMessage message =  new MySpaceMessage("AGMMCE00202");
			if (errCode=="")
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,"AGMMCE00202","");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,"");	
			 
			return response;
		} 
			if( DEBUG ) logger.debug("RESPONSE: "+response); 
			return response;
		}catch(Exception e){
			logger.error("ERROR UPLOADING MYSPACEMANAGER" +e.toString());
			status.addCode(MySpaceStatusCode.AGMSCE01046,MySpaceStatusCode.ERROR,  MySpaceStatusCode.NOLOG, this.getComponentName());
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMSCE01046", this.getComponentName()) ;
			if (errCode=="")    
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			else
			  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");	
			 
			return response;
		} 

   }

// =================================================================

//
// Server methods.
//
// The followng methods are provided to access a MySpace server.

	private Call createServerManagerCall(){
		String servermanagerloc = MMC.getProperty(MMC.serverManagerLoc,MMC.CATLOG);
		Call call = null;
		try{
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
				logger.debug("codeS:" +codeS +":codeS");
			}else{
				codeS = codeS +"," +code.getCode();
				}
			if (DEBUG)logger.debug("STATUS CODE IS: " +message +"  "+codeS);
		}    	
		return codeS;
	}
    
	private String getErrorCode(){
    	
			v = status.getCodes();
			for(int i=0;i<=v.size();i++){
				MySpaceStatusCode currCode = (MySpaceStatusCode)v.elementAt(i); 
				if (DEBUG) logger.debug("expirementing jConfig: "+currCode.getCode());
				AstroGridMessage generalMessage = new AstroGridMessage( currCode.getCode(), this.getComponentName()) ;
				if (DEBUG) logger.debug("expirementing jConfig get code: "+ generalMessage.toString());
				errCode=errCode+","+currCode.getCode();
				response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,"");					
			}	
    	
		return response;
	}
    
	private String getValues(String jobDetails) throws Exception{
		//load jConfig file.
		setUp();

		if ( DEBUG )logger.debug("registryName = "+registryName);
		request = util.getRequestAttributes(jobDetails);
		try{
			if(request.get("userID")!=null) userID = request.get("userID").toString();
			if(request.get("communityID")!=null) communityID = request.get("communityID").toString();
			if(request.get("jobID")!=null) jobID = request.get("jobID").toString();	
			if(request.get("query")!=null) query = request.get("query").toString();		
			if(request.get("newDataItemName")!=null) newDataItemName = request.get("newDataItemName").toString();
			if(request.get("newContainerName")!=null) newContainerName = request.get("newContainerName").toString();	
			if(request.get("serverFileName")!=null) serverFileName = request.get("serverFileName").toString();
			if(request.get("fileContent")!=null) fileContent = request.get("fileContent").toString();
			if(request.get("category")!=null) category = request.get("category").toString();
			if(request.get("importURI")!=null) importURI = request.get("importURI").toString();
			if(request.get("contentsType")!=null) contentsType = request.get("contentsType").toString();
			if(request.get("action")!=null) action = request.get("action").toString();

			try{
				if(request.get("fileSize")!=null) fileSize = Integer.parseInt(request.get("fileSize").toString());
				if(request.get("extentionPeriod")!=null) extentionPeriod = Integer.parseInt(request.get("extentionPeriod").toString());
			}catch(NumberFormatException nfe){
				AstroGridMessage generalMessage = new AstroGridMessage( "AGMSCE01004", this.getComponentName()) ;
				status.addCode(MySpaceStatusCode.AGMSCE01004,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
				response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
				if ( DEBUG )  logger.debug("generalMessage.toString: "+generalMessage.toString());
				if ( DEBUG )  logger.debug("response in upload = "+response);
				return response;
			}

		}catch(NullPointerException npe){
			AstroGridMessage generalMessage = new AstroGridMessage( "AGMSCE01006", this.getComponentName()) ;
			status.addCode(MySpaceStatusCode.AGMSCE01006,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
			response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
			return response;
		} 
		   	
		if ( DEBUG ){
			logger.debug("serverFileName Existans is "+request.containsKey("serverFileName") +"SIZE of request keys: "+request.size());
			Object o[] = request.keySet().toArray();
			for(int i=0;i<o.length;i++){
				logger.debug("NameSetOfRequest: "+o[i] +request.get(o[i]).toString());
				}
		}
        
		
		if ( request.containsKey("newDataHolderName")){
			if (request.get("newDataHolderName").toString().trim().length()>0){
				logger.debug("MySpaceManager.getValue newDataHolderName:"+newDataHolderName);
				if ( DEBUG ) logger.debug("newdatraholdernema:"+newDataHolderName+"done");
				newDataHolderName = request.get("newDataHolderName").toString();
				
			}else{
				logger.debug("MySpaceManager.getValue serverFileName length:"+serverFileName.length()+", name: "+serverFileName.toString());
				logger.debug("MySpaceManager.getValue"+serverFileName.lastIndexOf("/"));
				newDataHolderName = "/" +userID +"/serv1" +serverFileName.substring(serverFileName.lastIndexOf("/"),serverFileName.length());
				if ( DEBUG ) logger.debug("newdataholdername:"+newDataHolderName+"done2");
			}		
		}
		
		if ( DEBUG ) logger.debug(" userid:" +userID+" comID:"+communityID +
								  " jobID:"+jobID+" newdataholdername:"+newDataHolderName+
								  " filesize:"+fileSize+
								  " serverFileName == "+serverFileName+
								  " query = "+query+
								  " newDataItemName"+newDataItemName+
								  " newContainerName"+newContainerName);
		if ( DEBUG ) logger.debug("getValue msManager response :"+response);                              
		return response;
	}
    
    
	public String extendLease(String jobDetails){
		if ( DEBUG )  logger.debug("MySpaceManager.extendLease");
		DataItemRecord dataitem = null;
		Call call = null;
	
			try{
				response = getValues(jobDetails);

				if ( DEBUG ) logger.debug("About to invoke myspaceaction.advanceExpiryDataHolder");  
				msA.setRegistryName(registryName);

				Vector dataItemRecords = msA.lookupDataHoldersDetails(
				  userID, communityID, jobID, serverFileName);

				if (dataItemRecords != null)
				{  DataItemRecord dataItem = (DataItemRecord)dataItemRecords.elementAt(0);
					dataItemID = dataItem.getDataItemID();
				   logger.debug("EXTENDLEASE TRING TO GET DATAITEMID: " +dataItemID);
				}else{
					logger.debug("EXTENDLEASE DATAITEMRCORDS = NULL!");
				}
				
				dataitem = msA.advanceExpiryDataHolder(userID, communityID, jobID, dataItemID, extentionPeriod );
				
				if ( DEBUG ) logger.debug("userid:"+userID+"comID"+communityID+"jobid"+jobID+"dataItemID"+dataItemID
									   +"extentionPeriod"+extentionPeriod);
				if ( errCode!="" )
				  errCode = errCode +"," +checkStatus("EXTENDLEASE STATUS EXTENDLEASE");
				else 
				  errCode = checkStatus("EXTENDLEASE STATUS EXTENDLEASE");
	
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
						  response = util.buildMySpaceManagerResponse(dataitem, MMC.SUCCESS, "","");	
						else
						  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,""); 	    	
					}else {
						response = getErrorCode();
						    	
					}	
				} else{
					status.addCode(MySpaceStatusCode.AGMMCE00202,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
					AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00202", this.getComponentName()) ;
					if(errCode=="")
					  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
					else
					  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");  
					return response;
				}
				if( DEBUG ) logger.debug("RESPONSE: "+response); 
			}catch(Exception e){
				logger.error("ERROR EXTEND LEASE MYSPACEMANAGER" +e.toString());
				status.addCode(MySpaceStatusCode.AGMMCE00216,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
				AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00216", this.getComponentName()) ;
				if(errCode=="")
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
				else
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),""); 		  
				return response;
			}
			return response;

		}
		
	public String publish(String jobDetails){
			return "Not Implemented";
		}		
		
	public boolean createUser(String userid, String communityid, Vector servers){
		if ( DEBUG )  logger.debug("MySpaceManager.createUser");  
		boolean isUserCreated = false;
			try{			
				setUp();		
				isUserCreated = msA.createUser(userid, communityid, " ",servers);
			}catch(Exception e){
				logger.error("ERROR CREATEUSER MYSPACEMANAGER" +e.toString());
				status.addCode(MySpaceStatusCode.AGMMCE00216,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
				AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00216", this.getComponentName()) ;
				if(errCode=="")
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
				else
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),""); 		  
			}
			return isUserCreated;

		}		
		
	public boolean deleteUser(String userid, String communityid){
		if ( DEBUG )  logger.debug("MySpaceManager.deleteUser");  
		boolean isUserDeleted = false;
			try{			
				setUp();		
				isUserDeleted = msA.deleteUser(userid,communityid, " ");
			}catch(Exception e){
				logger.error("ERROR DELETEUSER MYSPACEMANAGER" +e.toString());
				status.addCode(MySpaceStatusCode.AGMMCE00216,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
				AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00216", this.getComponentName()) ;
				if(errCode=="")
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
				else
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),""); 		  
			}
			return isUserDeleted;
		}		
		
	public String changeOwner(String userid, String communityid, String dataHolderName, String newOwnerID){
		if ( DEBUG )  logger.debug("MySpaceManager.changeOwner");
		DataItemRecord dataitem = null;	     
			try{
				//response = getValues(jobDetails);
				setUp();
				msA.setRegistryName(registryName);

				Vector dataItemRecords = msA.lookupDataHoldersDetails(
				  userID, communityID, jobID, serverFileName);

				if (dataItemRecords != null)
				{  DataItemRecord dataItem = (DataItemRecord)dataItemRecords.elementAt(0);
					dataItemID = dataItem.getDataItemID();
				   logger.debug("CHANGEOWNER TRING TO GET DATAITEMID: " +dataItemID);
				}else{
					logger.debug("CHANGEOWNER DATAITEMRCORDS = NULL!");
				}
				
				dataitem = msA.changeOwnerDataHolder(userid, communityid, " ", dataItemID, newOwnerID);
				
				if ( DEBUG ) logger.debug("userid:"+userID+"comID"+communityID+"dataItemID"+dataItemID
									   +"newOwnerID"+newOwnerID);				
	
     			boolean successStatus = status.getSuccessStatus();
				boolean warningStatus = status.getWarningStatus();
				
			//   Format and return the results as XML.
				if(dataitem!=null){
					response = util.buildMySpaceManagerResponse(dataitem, returnStatus, details,"");
					if (successStatus){
						if (errCode=="")
						  response = util.buildMySpaceManagerResponse(dataitem, MMC.SUCCESS, "","");	
						else
						  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode,""); 	    	
					}else {
						response = getErrorCode();
						    	
					}	
				} else{
					status.addCode(MySpaceStatusCode.AGMMCE00202,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
					AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00202", this.getComponentName()) ;
					if(errCode=="")
					  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
					else
					  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),"");  
					return response;
				}
				if( DEBUG ) logger.debug("RESPONSE: "+response); 
			}catch(Exception e){
				logger.error("ERROR CHANGEOWNER MYSPACEMANAGER" +e.toString());
				status.addCode(MySpaceStatusCode.AGMMCE00216,MySpaceStatusCode.ERROR, MySpaceStatusCode.NOLOG, this.getComponentName());
				AstroGridMessage generalMessage = new AstroGridMessage( "AGMMCE00216", this.getComponentName()) ;
				if(errCode=="")
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,generalMessage.toString(),"");
				else
				  response = util.buildMySpaceManagerResponse(null,MMC.FAULT,errCode+","+generalMessage.toString(),""); 		  
				return response;
			}
			return response;

		}
		
		public String listExpiredDataHolders(String jobDetails){
		//String userID,String communityID, String jobID, String query)
			return "Not Implemented";
		}				
		
		private void setUp()throws Exception{
			MMC.getInstance().checkPropertiesLoaded();
			registryName = MMC.getProperty(MMC.REGISTRYCONF, MMC.CATLOG);	

		}
	protected String getComponentName() { return Configurator.getClassName( MySpaceManager.class) ; }    

}


