/*
 * $Id: CommandLineApplicationController.java,v 1.8 2003/12/04 13:26:25 pah Exp $
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
import org.astrogrid.applications.description.SimpleApplicationDescription;
import org.astrogrid.community.User;
import java.util.HashSet;

public class CommandLineApplicationController extends AbstractApplicationController  {
   
   
 

   /**
    * Standard Constructor.
    */
   public CommandLineApplicationController() {
      super();
      
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#executeApplication(int)
    */
   public void executeApplication(int executionId) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.executeApplication() not implemented");
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
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.initializeApplication() not implemented");
   }

   /**
    *@link aggregation
    *      @associates org.astrogrid.applications.commandline.ApplicationEnvironment
    */
   private HashSet runningApplications;
}

