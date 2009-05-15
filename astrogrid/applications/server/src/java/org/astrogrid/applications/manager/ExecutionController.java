/*
 * $Id: ExecutionController.java,v 1.6 2009/05/15 22:51:19 pah Exp $
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

import java.util.List;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.authorization.AuthorizationPolicy;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.security.SecurityGuard;

/**
  Interface to a service that allows applications to be executed and controlled.
  This interface contains {@link SecurityGuard}s in each of the methods - the implementations are expected to do something with this information, - i.e. implement policy decision points using an {@link AuthorizationPolicy}.
  
  @TODO would be better to do the authentication stuff with aspect oriented programming.
 * * @author Paul Harrison (pah@jb.man.ac.uk) 22-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public interface ExecutionController {
    /** create a new application
     * 
     * @param tool provides application name, interface name, parameters
     * @param jobstepID id assigned by client to this new execution
     * @param securityGuard TODO
     * @return id assigned by server to this new execution.
     * @throws CeaException
     */
    public String init(Tool tool, String jobstepID, SecurityGuard securityGuard) throws CeaException;
    

   /** starts asynchronous execution of a application
    * 
    * @param executionId the server-assigned id of a previously initialized application
 * @param secGuard TODO
    * @return true if execution started successfully
    * @throws CeaException
    */
   public boolean execute(String executionId, SecurityGuard secGuard) throws CeaException;

   /** abort execution of an application (not supported by all applications)
    * 
    * @param executionId the servier-assigned id of an application
 * @param secGuard TODO
    * @return true if the application could be aborted
    * @throws CeaException
    */
   public boolean abort(String executionId, SecurityGuard secGuard) throws  CeaException;


   /**
    * return the list of applications currently in the queue (or executing) for this execution controller
 * @return
 */
public List<Application> getQueue();

}
