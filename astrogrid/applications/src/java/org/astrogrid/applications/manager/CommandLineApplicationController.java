/*
 * $Id: CommandLineApplicationController.java,v 1.5 2003/11/26 22:07:24 pah Exp $
 *
 * Created on 13 November 2003 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.ParameterValues;
import org.astrogrid.applications.description.ApplicationDescription;

public class CommandLineApplicationController extends AbstractApplicationController  {
 

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#executeApplication(int)
    */
   public void executeApplication(int executionId) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.executeApplication() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#getApplicationDescription(java.lang.String)
    */
   public ApplicationDescription getApplicationDescription(String applicationID) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.getApplicationDescription() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#initializeApplication(java.lang.String, java.lang.String, java.lang.String, org.astrogrid.applications.ParameterValues)
    */
   public int initializeApplication(
      String applicationID,
      String jobstepID,
      String jobMonitorURL,
      ParameterValues parameters) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.initializeApplication() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#listApplications()
    */
   public String[] listApplications() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.listApplications() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#queryApplicationExecutionStatus(int)
    */
   public String queryApplicationExecutionStatus(int executionId) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.queryApplicationExecutionStatus() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#returnRegistryEntry()
    */
   public String returnRegistryEntry() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.returnRegistryEntry() not implemented");
   }

}

