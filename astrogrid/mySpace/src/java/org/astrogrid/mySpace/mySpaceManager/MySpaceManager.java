package org.astrogrid.mySpace.mySpaceManager;

import java.io.*;
import java.util.*;

import org.astrogrid.mySpace.mySpaceStatus.*;

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

public class MySpaceInterface
{  private static MySpaceStatus status = new MySpaceStatus();
//
// Constructor.

/**
 * Default constructor.
 */

   public MySpaceInterface()
   {  super();
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

//
//   Decompose the userID, communityID, jobID and other details from
//   the jobDetails.

      String userID = "";
      String communityID = "";
      String jobID = "";

      int dataItemID = 0;

//
//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      MySpaceManager myspace = new MySpaceManager();

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

   public String lookupDataHoldersDetails(String jobDetails)
   {

//
//   Decompose the userID, communityID, jobID and other details from
//   the jobDetails.

      String userID = "";
      String communityID = "";
      String jobID = "";

      String query = "";

//
//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      MySpaceManager myspace = new MySpaceManager();

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

      String xmlString = "";
      return xmlString;
   }

// -----------------------------------------------------------------

/**
  * Copy a DataHolder from one location on a MySpace server to another
  * location on the same server.
  */

   public String copyDataHolder(String jobDetails)
   {

//
//   Decompose the userID, communityID, jobID and other details from
//   the jobDetails.

      String userID = "";
      String communityID = "";
      String jobID = "";

      int oldDataItemID = 0;
      String newDataItemName = "";

//
//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      MySpaceManager myspace = new MySpaceManager();

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

   public String moveDataHolder(String jobDetails)
   {

//
//   Decompose the userID, communityID, jobID and other details from
//   the jobDetails.

      String userID = "";
      String communityID = "";
      String jobID = "";

      int oldDataItemID = 0;
      String newDataItemName = "";

//
//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      MySpaceManager myspace = new MySpaceManager();

      DataItemRecord dataitem = myspace.moveDataHolder(
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
  * Export a DataHolder from the MySpace system.  In Iteration 2 a
  * DataHolder is exported by returning a URL from which a copy of it
  * can be retrieved.
  */

   public String exportDataHolder(String jobDetails)
   {

//
//   Decompose the userID, communityID, jobID and other details from
//   the jobDetails.

      String userID = "";
      String communityID = "";
      String jobID = "";

      int dataItemID = 0;

//
//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      MySpaceManager myspace = new MySpaceManager();

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

//
//   Decompose the userID, communityID, jobID and other details from
//   the jobDetails.

      String userID = "";
      String communityID = "";
      String jobID = "";

      String newContainerName = "";

//
//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      MySpaceManager myspace = new MySpaceManager();

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

   public String deleteDataHolder(String jobDetails)
   {

//
//   Decompose the userID, communityID, jobID and other details from
//   the jobDetails.

      String userID = "";
      String communityID = "";
      String jobID = "";

      int dataItemID = 0;

//
//   Create an instance of the MySpace manager and invoke the method
//   corresponding to this action.

      MySpaceManager myspace = new MySpaceManager();

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


