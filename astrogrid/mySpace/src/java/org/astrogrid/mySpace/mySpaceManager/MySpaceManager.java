package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;
import java.util.HashMap;

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
	private  int dataItemID = 0;
	private String newContainerName = "";

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
	    response = util.buildMySpaceManagerResponse(dataitem); 
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

// TBD.
}


