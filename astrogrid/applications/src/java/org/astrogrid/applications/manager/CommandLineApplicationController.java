/*
 * $Id: CommandLineApplicationController.java,v 1.24 2004/04/02 20:48:15 pah Exp $
 *
 * Created on 13 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis.description.ServiceDesc;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import org.astrogrid.applications.ApplicationExecutionException;
import org.astrogrid.applications.ApplicationFactory;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.CmdLineApplicationCreator;
import org.astrogrid.applications.ParameterValues;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.commandline.CmdLineApplication;
import org.astrogrid.applications.commandline.CmdLineApplicationEnvironment;
import org
   .astrogrid
   .applications
   .commandline
   .exceptions
   .CannotCreateWorkingDirectoryException;
import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.description.ParameterLoader;
import org
   .astrogrid
   .applications
   .description
   .exception
   .ApplicationDescriptionNotFoundException;
import org
   .astrogrid
   .applications
   .description
   .exception
   .InterfaceDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterValuesParseError;
import org.astrogrid.applications.manager.externalservices.MySpaceFromConfig;
import org.astrogrid.applications.manager.externalservices.MySpaceLocator;
import org.astrogrid.applications.manager.externalservices.RegistryAdminFromConfig;
import org.astrogrid.applications.manager.externalservices.RegistryAdminLocator;
import org.astrogrid.applications.manager.externalservices.RegistryFromConfig;
import org.astrogrid.applications.manager.externalservices.RegistryQueryLocator;
import org.astrogrid.community.User;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * This provides a mechanism to run command line tools.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 * @TODO try inversion of control refactoring - plug in the actual environment setter callback mechanism etc...
 */
public class CommandLineApplicationController extends AbstractApplicationController {

   /**
    * This is a bit of a cheat to get at the instance of the applicationController - should be done elsewhere
    */
   private static CommandLineApplicationController instance = null;
   /**
    * Small class to indicate that we really do want to create a CeaControllerConfig
    * @author Paul Harrison (pah@jb.man.ac.uk) 19-Mar-2004
    * @version $Name:  $
    * @since iteration5
    */
   private static class ThisConfig extends CeaControllerConfig {
      public static CeaControllerConfig getInstance() {
         return CeaControllerConfig.getInstance();
      }
   }

   /**
    * @TODO get rid of me with inversion of control.
    */
   public CommandLineApplicationController() {

      this(null); // make a dummy service description...

   }

   public CommandLineApplicationController(ServiceDesc desc) {
      this(
         ThisConfig.getInstance(),
         new RegistryFromConfig(ThisConfig.getInstance()),
         new RegistryAdminFromConfig(ThisConfig.getInstance()),
         new MySpaceFromConfig(ThisConfig.getInstance()),
         desc);

   }

   /**
    * @param config
    * @param registryQueryLocator
    * @param mySpaceLocator
    * @param desc
    */
   public CommandLineApplicationController(
      CeaControllerConfig iconfig,
      RegistryQueryLocator iregistryQueryLocator,
      RegistryAdminLocator iregistryAdminLocator,
      MySpaceLocator imySpaceLocator,
      ServiceDesc idesc) {

      super(
         iconfig,
         iregistryQueryLocator,
         iregistryAdminLocator,
         imySpaceLocator,
         idesc);
      logger.info("starting CommandLineApplicationController");
      instance = this;
      runningApplications = new HashMap();
      //REFACTORME this should be part of the scheduler
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#executeApplication(int)
    */
   private boolean executeApplication(String executionId) throws CeaException {
      boolean success = false;

      if (runningApplications.containsKey(executionId)) {
         CmdLineApplication app =
            (CmdLineApplication)runningApplications.get(executionId);
         success = app.execute();
      }

      return success;

   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#initializeApplication(java.lang.String, java.lang.String, java.lang.String, org.astrogrid.community.User, org.astrogrid.applications.ParameterValues)
    */
   private String initializeApplication(
      String applicationID,
      String jobstepID,
      String jobMonitorURL,
      ParameterValues parameters)
      throws
         ApplicationDescriptionNotFoundException,
         CannotCreateWorkingDirectoryException,
         InterfaceDescriptionNotFoundException, ParameterValuesParseError {
      int executionId = -1;

      // create the application object
      ApplicationFactory factory = CmdLineApplicationCreator.getInstance(this);
      User user = new User(); //TODO this needs to be obtained from the context
      CmdLineApplication cmdLineApplication =
         (CmdLineApplication)factory.createApplication(applicationID, user);

      // create the application environment
      CmdLineApplicationEnvironment environment =
         new CmdLineApplicationEnvironment(config);
      cmdLineApplication.setApplicationEnvironment(environment);
      executionId = environment.getExecutionId();

      cmdLineApplication.setApplicationInterface(
         cmdLineApplication.getApplicationDescription().getInterface(
            parameters.getMethodName()));

      //TODO parse the parameter values and set up the parameter array
      ParameterLoader pl = new ParameterLoader(cmdLineApplication);
      pl.loadParameters(parameters.getParameterSpec());

      // add this application to the execution map
      runningApplications.put(Integer.toString(executionId), cmdLineApplication);

      // set up the job step paramters
      cmdLineApplication.setJobMonitorURL(jobMonitorURL);
      cmdLineApplication.setJobStepID(jobstepID);
      cmdLineApplication.setStatus(Status.INITIALIZED);

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

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#abortApplication(java.lang.String)
    */
   public boolean abort(String executionId) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.abort() not implemented");
   }
   /** 
    * @see org.astrogrid.applications.manager.CommonExecutionController#execute(org.astrogrid.workflow.beans.v1.Tool, java.lang.String, java.lang.String)
    */
   public String execute(Tool tool, String jobstepID, String jobMonitorURL)
      throws CeaException {
      // TODO need to drive the castor object model lower than this.
      ParameterValues parameters = new ParameterValues();
      parameters.setMethodName(tool.getInterface());
      StringWriter sw = new StringWriter();
      try {
         tool.marshal(sw);
         parameters.setParameterSpec(sw.toString());
      }
      catch (MarshalException e) {
         throw new ApplicationExecutionException(
            "could not marshal the tool parameters to a string",
            e);
      }
      catch (ValidationException e) {
         throw new ApplicationExecutionException(
            "validation error when marshalling tool parameters",
            e);
      }
      String executionId;
      executionId =
         initializeApplication(tool.getName(), jobstepID, jobMonitorURL, parameters);
      executeApplication(executionId);
      return executionId;
   }

   /** 
    * @see org.astrogrid.applications.manager.CommonExecutionController#queryExecutionStatus(java.lang.String)
    */
   public MessageType queryExecutionStatus(String executionId) throws CeaException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.queryExecutionStatus() not implemented");
   }

}
