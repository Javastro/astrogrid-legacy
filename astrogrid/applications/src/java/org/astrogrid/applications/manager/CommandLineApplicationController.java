/*
 * $Id: CommandLineApplicationController.java,v 1.14 2003/12/31 00:56:17 pah Exp $
 *
 * Created on 13 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.astrogrid.applications.ApplicationFactory;
import org.astrogrid.applications.ParameterValues;
import org.astrogrid.applications.commandline.CmdLineApplicationEnvironment;
import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.commandline.CmdLineApplicationCreator;
import org.astrogrid.applications.commandline.exceptions.ApplicationExecutionException;
import org.astrogrid.applications.commandline.exceptions.CannotCreateWorkingDirectoryException;
import org.astrogrid.applications.description.ParameterLoader;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.community.User;

public class CommandLineApplicationController extends AbstractApplicationController  {
   
   
 

   /**
    * Standard Constructor.
    */
   public CommandLineApplicationController() {
      super();
      runningApplications=new HashMap();
      
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#executeApplication(int)
    */
   public boolean executeApplication(String executionId) {
      boolean success = false;
      
      
      
      if (runningApplications.containsKey(executionId)) {
         CmdLineApplication app =
            (CmdLineApplication)runningApplications.get(executionId);
            try {
               success = app.execute();
            }
            catch (ApplicationExecutionException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
      }
      
      
      return success;
      
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#queryApplicationExecutionStatus(int)
    */
   public String queryApplicationExecutionStatus(String executionId) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.queryApplicationExecutionStatus() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#initializeApplication(java.lang.String, java.lang.String, java.lang.String, org.astrogrid.community.User, org.astrogrid.applications.ParameterValues)
    */
   public String initializeApplication(
      String applicationID,
      String jobstepID,
      String jobMonitorURL,
      User user,
      ParameterValues parameters) {
         int executionId = -1;
         
         
         // create the application object
         ApplicationFactory factory = CmdLineApplicationCreator.getInstance(this);
        try {
             CmdLineApplication cmdLineApplication = (CmdLineApplication)factory.createApplication(applicationID, user);
             
            
               
            try {
               // create the application environment
                 CmdLineApplicationEnvironment environment = new CmdLineApplicationEnvironment();
                 cmdLineApplication.setApplicationEnvironment(environment);
                 executionId = environment.getExecutionId();
                 try {
                  cmdLineApplication.setApplicationInterface(cmdLineApplication.getApplicationDescription().getInterface(parameters.getMethodName()));
               }
               catch (InterfaceDescriptionNotFoundException e1) {
                  logger.error("cannot find interface", e1);
               }
                 
              //TODO parse the parameter values and set up the parameter array
              ParameterLoader pl = new ParameterLoader(cmdLineApplication);
              pl.loadParameters(parameters.getParameterSpec());
         
              
            
              // add this application to the execution map
              runningApplications.put(Integer.toString(executionId), cmdLineApplication);
            
            
            }
            catch (CannotCreateWorkingDirectoryException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
               
            }
            
         }
         catch (ApplicationDescriptionNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }     
       
       // the external representation of the execution ID is a string
       return Integer.toString(executionId);
   }

   /**
    *@link aggregation 
    *      @associates org.astrogrid.applications.commandline.CmdLineApplicationEnvironment
    */
    private Map runningApplications;
   /**
    * Return a running application object.this is package private to assist with the unit tests primarily.
    * @return
    */
    CmdLineApplication getRunningApplication(String executionID) {
      CmdLineApplication app = null;
      
      if (runningApplications.containsKey(executionID)) {
         app = (CmdLineApplication)runningApplications.get(executionID);         
      }
      return app;
   }

}

