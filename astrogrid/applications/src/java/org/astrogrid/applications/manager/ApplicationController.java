/*
 * $Id: ApplicationController.java,v 1.5 2003/11/25 12:55:09 pah Exp $
 *
 * Created on 03 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.ApplicationDescription;

/**
 * Describes the operations that are required of an application Controller. This includes listing the applications managed by the controller, including the detailed description of each application.
 * 
 * In addition the application controller is responsible for initializing the application with its parameters and then running the application, as well as returning the application
 * @author pah 
 * @since Iteration 4
 * @version $Name:  $
 * 
 */
public interface ApplicationController {
   /**
    * Returns a list of the application ids of the applications that the controller manages. 
    */
   String[] listApplications();

   /**
    * returns a full application description (including parameter descriptions) for a particular application.
    * @stereotype query 
    */
   ApplicationDescription getApplicationDescription(String applicationID);

   /**
    * Initialize the application environment and set up the parameters.
    * @param applicationID the application identifier as returned by a call to @link #listApplications .
    * @param jobstepID the jobstep that is requesting the execution.
    * @param jobMonitorURL the jobmonitor service to call to notify of execution completion.
    * @param parameters the description of the job parameters.
    * @return an identifier for this particular execution instance.
    */
   int initializeApplication(String applicationID, String jobstepID, String jobMonitorURL, org.astrogrid.applications.ParameterValues parameters);

   /**
    * Executes a particular application asynchronously that has previously been intialized by @link #initializeApplication
    * @param executionId The executionId returned by @link #initializeApplication .
    */
   void executeApplication(int executionId);

   /**
    * Query the status of a particular application execution.
    * @param executionId The executionId returned by @link #initializeApplication .
    * @return
    */
   String queryApplicationExecutionStatus(int executionId);

   /**
    * Returns an xml string describing the application service controller and the applications that it controls. This string would be suitable for putting in the registry.
    * @return
    */
   String returnRegistryEntry();

   /**
    * @label Manages
    * @clientCardinality 1
    * @supplierCardinality 1..*
    * @labelDirection forward
    * @directed 
    */
   /*# org.astrogrid.applications.Application lnkApplication; */

   /** @link dependency 
    * @clientRole informs
    * @label Application Execution Completion*/
   /*# org.astrogrid.jes.jobmonitor.JobMonitor lnkJobMonitor; */
}
