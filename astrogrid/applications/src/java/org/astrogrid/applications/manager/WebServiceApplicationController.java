/*
 * $Id: WebServiceApplicationController.java,v 1.2 2004/02/10 14:38:12 pah Exp $
 *
 * Created on 09 February 2004 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import org.astrogrid.applications.ParameterValues;
import org.astrogrid.community.User;

public class WebServiceApplicationController extends AbstractApplicationController {

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#initializeApplication(java.lang.String, java.lang.String, java.lang.String, org.astrogrid.community.User, org.astrogrid.applications.ParameterValues)
    */
   public String initializeApplication(String applicationID, String jobstepID, String jobMonitorURL, User user, ParameterValues parameters) {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("WebServiceApplicationController.initializeApplication() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#executeApplication(java.lang.String)
    */
   public boolean executeApplication(String executionId) {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("WebServiceApplicationController.executeApplication() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.manager.ApplicationController#queryApplicationExecutionStatus(java.lang.String)
    */
   public String queryApplicationExecutionStatus(String executionId) {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("WebServiceApplicationController.queryApplicationExecutionStatus() not implemented");
   }
}
