/*
 * $Id: CommandLineApplicationController.java,v 1.12 2003/12/08 17:06:35 pah Exp $
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
import org.astrogrid.applications.commandline.ApplicationEnvironment;
import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.commandline.CmdLineApplicationCreator;
import org.astrogrid.applications.commandline.exceptions.ApplicationExecutionException;
import org.astrogrid.applications.commandline.exceptions.CannotCreateWorkingDirectoryException;
import org.astrogrid.applications.description.ParameterLoader;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
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
   public boolean executeApplication(int executionId) {
      boolean success = false;
      
      Integer id = new Integer(executionId);
      
      if (runningApplications.containsKey(id)) {
         CmdLineApplication app =
            (CmdLineApplication)runningApplications.get(id);
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
   public String queryApplicationExecutionStatus(int executionId) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.queryApplicationExecutionStatus() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#initializeApplication(java.lang.String, java.lang.String, java.lang.String, org.astrogrid.community.User, org.astrogrid.applications.ParameterValues)
    */
   public int initializeApplication(
      String applicationID,
      String jobstepID,
      String jobMonitorURL,
      User user,
      ParameterValues parameters) {
         int executionId = -1;
         
         // create the application object
         ApplicationFactory factory = CmdLineApplicationCreator.getInstance(applicationDescriptions);
        try {
             CmdLineApplication cmdLineApplication = (CmdLineApplication)factory.createApplication(applicationID);
            
            
               
            try {
               // create the application environment
                 ApplicationEnvironment environment = new ApplicationEnvironment();
                 cmdLineApplication.setApplicationEnvironment(environment);
                 executionId = environment.getExecutionId();
                 
              //TODO parse the parameter values and set up the parameter array
              ParameterLoader pl = new ParameterLoader(cmdLineApplication);
              pl.loadParamters(parameters.getParameterSpec());
         
              
            
              // add this application to the execution map
              runningApplications.put(new Integer(executionId), cmdLineApplication);
            
            
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
       
       return executionId;
   }

   /**
    *@link aggregation 
    *      @associates org.astrogrid.applications.commandline.ApplicationEnvironment
    */
    private Map runningApplications;
   /**
    * Return a running application object.this is package private to assist with the unit tests primarily.
    * @return
    */
    CmdLineApplication getRunningApplication(int executionID) {
      CmdLineApplication app = null;
      Integer i = new Integer(executionID);
      if (runningApplications.containsKey(i)) {
         app = (CmdLineApplication)runningApplications.get(i);         
      }
      return app;
   }

}

