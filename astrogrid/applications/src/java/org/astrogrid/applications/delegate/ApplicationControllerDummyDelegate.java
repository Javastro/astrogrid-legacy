/*
 * $Id: ApplicationControllerDummyDelegate.java,v 1.1 2003/11/25 12:25:26 pah Exp $
 * 
 * Created on 25-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.delegate;

import java.rmi.RemoteException;

import org.astrogrid.applications.delegate.beans.ApplicationDescription;
import org.astrogrid.applications.delegate.beans.ParameterValues;

/**
 * A dummy delegate for the application controller service.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationControllerDummyDelegate
   implements ApplicationController {
      static private String[] applicationList = {"sextractor"};

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#listApplications()
    */
   public String[] listApplications() throws RemoteException {
      return applicationList;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#getApplicationDescription(java.lang.String)
    */
   public ApplicationDescription getApplicationDescription(String applicationID)
      throws RemoteException {
        ApplicationDescription d = new ApplicationDescription();
        d.setName("sextractor");
        d.setParameter("this should be an wsdl description of the parameters?");
        return d;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#initializeApplication(java.lang.String, java.lang.String, java.lang.String, org.astrogrid.applications.delegate.beans.ParameterValues)
    */
   public int initializeApplication(
      String applicationID,
      String jobstepID,
      String jobMonitorURL,
      ParameterValues parameters)
      throws RemoteException {
         return 10;
    }
    

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#executeApplication(int)
    */
   public void executeApplication(int executionId) throws RemoteException {
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#queryApplicationExecutionStatus(int)
    */
   public String queryApplicationExecutionStatus(int executionId)
   {
      return "Running";
    }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#returnRegistryEntry()
    */
   public String returnRegistryEntry() throws RemoteException {
      return "This should be the registry entry for the controller as well as registry entries for all of the applications that it controls";
   }

}
