/*
 * $Id: CommonExecutionController.java,v 1.2 2004/03/23 12:51:26 pah Exp $
 * 
 * Created on 22-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.manager;

import java.rmi.RemoteException;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * This is the server side interface that represents the castor beans version of @link org.astrogrid.applications.service.v1.cea.CommonExecutionConnector. This could potentially be refactored away once we have a single bean generation tool.
 * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public interface CommonExecutionController {
   // Run an application asynchronously
   public String execute(Tool tool, String jobstepID, String jobMonitorURL) throws CeaException;

   // Abort a running application
   public boolean abort(String executionId) throws  CeaException;

   // List the applications that this CommonExecutionController manages
   public ApplicationList listApplications() throws CeaException;

   // Get a particular applicationID
   public ApplicationBase getApplicationDescription(String applicationID) throws CeaException;

   // uery the status of a running application
   public MessageType queryExecutionStatus(String executionId) throws CeaException;

   // return an entry for the registry - this really belongs in an interface
   // that the registry should be providing
   public String returnRegistryEntry();

}
