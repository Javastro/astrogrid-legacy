/*
 * $Id: ExecutionController.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

import org.astrogrid.applications.CeaException;
import org.astrogrid.workflow.beans.v1.Tool;

/**
  Interface to a service that allows applications to be executed and controlled
 * * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public interface ExecutionController {
    /** create a new application
     * 
     * @param tool provides application name, interface name, parameters
     * @param jobstepID id assigned by client to this new execution
     * @return id assigned by server to this new execution.
     * @throws CeaException
     */
    public String init(Tool tool, String jobstepID) throws CeaException;
    

   /** starts asynchronous executoion of a application
    * 
    * @param executionId the server-assigned id of a previously initialized application
    * @return true if execution started successfully
    * @throws CeaException
    */
   public boolean execute(String executionId) throws CeaException;

   /** abort execution of an application
    * 
    * @param executionId the servier-assigned id of an application
    * @return true if the application could be aborted
    * @throws CeaException
    */
   public boolean abort(String executionId) throws  CeaException;




}
