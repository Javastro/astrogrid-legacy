/*
 * $Id: ApplicationControllerDummyDelegate.java,v 1.4 2003/12/07 01:09:48 pah Exp $
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

import org.astrogrid.applications.delegate.beans.ParameterValues;
import org.astrogrid.applications.delegate.beans.SimpleApplicationDescription;
import org.astrogrid.applications.delegate.beans.User;

/**
 * A dummy delegate for the application controller service.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationControllerDummyDelegate
   implements ApplicationController {
      /**
       * use this value when as the argument to the @{link DelegateFactory#createDelegate} method to invoke the dummy delegate.
       */
      static public final String DUMMYADDRESS="invoke the DUMMY delegate";
      static private String[] applicationList = {"sextractor"};
      
     /**
      * Standard Constructor.
    * @param address This is not used in the dummy, but is here to mimic the call to the real delegate.
    */
   public ApplicationControllerDummyDelegate(String address)
     {
     }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#listApplications()
    */
   public String[] listApplications() throws RemoteException {
      return applicationList;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#getApplicationDescription(java.lang.String)
    */
   public SimpleApplicationDescription getApplicationDescription(String applicationID)
      throws RemoteException {
        SimpleApplicationDescription d = new SimpleApplicationDescription();
        d.setName("sextractor");
        d.setXmlDescriptor("this should be an wsdl description of the parameters?");
        return d;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#initializeApplication(java.lang.String, java.lang.String, java.lang.String, org.astrogrid.applications.delegate.beans.ParameterValues)
    */
   public int initializeApplication(
      String applicationID,
      String jobstepID,
      String jobMonitorURL,
      User user,
      ParameterValues parameters)
      throws RemoteException {
         return 10;
    }
    

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#executeApplication(int)
    */
   public boolean executeApplication(int executionId) throws RemoteException {
      return true;
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
