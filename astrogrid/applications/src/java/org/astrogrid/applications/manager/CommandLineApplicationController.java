/*
 * $Id: CommandLineApplicationController.java,v 1.1 2003/11/13 22:44:14 pah Exp $
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

public class CommandLineApplicationController extends AbstractApplicationController {
   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#executeApplication()
    */
   public void executeApplication() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.executeApplication() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#getApplicationDescription(java.lang.String)
    */
   public Application getApplicationDescription(String applicationID) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.getApplicationDescription() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#initializeApplication(java.lang.String, int, org.astrogrid.applications.Parameter[])
    */
   public int initializeApplication(
      String applicationID,
      int jobstepID,
      Parameter[] parameters) {
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
    * @see org.astrogrid.applications.manager.ApplicationController#queryApplicationStatus()
    */
   public String queryApplicationStatus() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommandLineApplicationController.queryApplicationStatus() not implemented");
   }

}

