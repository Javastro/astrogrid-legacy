/*
 * $Id: ApplicationController.java,v 1.10 2003/12/02 18:13:31 pah Exp $
 *
 * Created on 03 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import org.astrogrid.applications.description.SimpleApplicationDescription;
import org.astrogrid.community.User;

/**
 * Describes the operations that are required of an application Controller. This includes listing the applications managed by the controller, including the detailed description of each application.
 * 
 * In addition the application controller is responsible for initializing the application with its parameters and then running the application, as well as returning the application metadata for use by the registry and the 
 * @author pah 
 * @since Iteration 4
 * @version $Name:  $
 * 
 */
public interface ApplicationController {
   /**
    * list the application ids of the applications that the controller manages. 
    * @return the array of application IDs
    */
   String[] listApplications();

   /**
    * returns a full application description (including parameter descriptions) for a particular application.
    * @param applicationID the application identifier as returned by a call to {@link #listApplications} .
    * @return This is a simple bean wrapper for the xml application description.
    * @stereotype query 
    */
   SimpleApplicationDescription getApplicationDescription(String applicationID);

   /**
    * Initialize the application environment and set up the parameters.
    * @param applicationID the application identifier as returned by a call to {@link #listApplications} .
    * @param jobstepID the jobstep that is requesting the execution.
    * @param jobMonitorURL the jobmonitor service to call to notify of execution completion.
    * @param user The user credentials for the user that the job will be run in the name of. This bean represents what was commonly known as the "community snippet".
    * @param parameters the description of the job parameters.
    * @return an identifier for this particular execution instance.
    */
   int initializeApplication(String applicationID, String jobstepID, String jobMonitorURL, User user, org.astrogrid.applications.ParameterValues parameters);

   /**
    * Executes a particular application asynchronously that has previously been intialized by {@link #initializeApplication}
    * @param executionId The executionId returned by {@link #initializeApplication} .
    */
   void executeApplication(int executionId);

   /**
    * Query the status of a particular application execution.
    * @param executionId The executionId returned by {@link #initializeApplication} .
    * @return A String describing the current status of the application.
    */
   String queryApplicationExecutionStatus(int executionId);

   /**
    * Returns an xml string describing the application service controller and the applications that it controls. This string would be suitable for putting in the registry.
    * @return A VOResource fragment suiltable for inclusion in the registry.
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
