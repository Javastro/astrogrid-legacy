/*
 * $Id: ApplicationController.java,v 1.4 2003/11/25 12:25:26 pah Exp $
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
    * @return The ID of the particular application execution instance 
    */
   int initializeApplication(String applicationID, String jobstepID, String jobMonitorURL, org.astrogrid.applications.ParameterValues parameters);

   /**
    * Executes a partiular application asynchronously 
    */
   void executeApplication(int executionId);

   /**
    * Query the status of a particular application execution.
    * @param executionId The executionId returned by @link #initializeApplication
    * @return
    */
   String queryApplicationExecutionStatus(int executionId);

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
