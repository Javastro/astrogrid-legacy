/*
 * $Id: ApplicationControllerDelegate.java,v 1.5 2004/02/02 16:49:44 pah Exp $
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

import java.net.URL ;
import java.net.MalformedURLException ;

import javax.xml.rpc.ServiceException;

import org.astrogrid.applications.delegate.beans.ParameterValues;
import org.astrogrid.applications.delegate.beans.SimpleApplicationDescription;
import org.astrogrid.applications.delegate.beans.User;


/**
 * ApplicationController Delegate. At the moment the delegate mirrors the methods supplied by the applicationController service so documentation is kept up to date @see org.astrogrid.applications.manager.ApplicationController
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationControllerDelegate implements ApplicationController {

   private ApplicationControllerServiceSoapBindingStub binding = null;
   
   
   /**
    * Standard Constructor.
    * @param address The address of the Application Controller Service that you want to use.
    */
   public ApplicationControllerDelegate(String address)
   {
      initialize(address);
   }
   /**
    * Initialize the binding to the application controller web service.
    * @param address The address of the service to bind to - if null then the standard localhost version will be bound
    * @return
    */
   private boolean initialize(String address) {
      
      boolean retval = true;
      
      // create the service locator
      try {
         ApplicationControllerServiceLocator locator = new ApplicationControllerServiceLocator();
         if (address != null && address.length() > 0) {
            URL url = new URL(address);
            binding = (ApplicationControllerServiceSoapBindingStub)locator.getApplicationControllerService(url);
            
         }
         else{
            binding = (ApplicationControllerServiceSoapBindingStub)locator.getApplicationControllerService();
         }
      }
      catch (ServiceException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         retval = false;
      } catch (MalformedURLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         retval = false;
      }
      return retval;
   }
   
   /**
    * @see org.astrogrid.applications.manager.ApplicationController#listApplications()
    */
   public String[] listApplications() throws RemoteException {
        return binding.listApplications(); 
    }

   /**
    * @see org.astrogrid.applications.manager.ApplicationController#getApplicationDescription(java.lang.String)
    */
   public SimpleApplicationDescription getApplicationDescription(String applicationID)
      throws RemoteException {
         return binding.getApplicationDescription(applicationID);
   }

   /**
    * @see org.astrogrid.applications.manager.ApplicationController#initializeApplication(java.lang.String, java.lang.String, org.astrogrid.community.User, org.astrogrid.applications.ParameterValues)
    */
   public String initializeApplication(
      String applicationID,
      String jobstepID,
      String jobMonitorURL,
      User user,
      ParameterValues parameters)
      throws RemoteException {
       return binding.initializeApplication(applicationID, jobstepID, jobMonitorURL, user, parameters);
   }

   /**
    * @see org.astrogrid.applications.manager.ApplicationController#executeApplication(java.lang.String)
    */
   public boolean executeApplication(String executionId) throws RemoteException {
      return binding.executeApplication(executionId);
   }

   /**
    * @see org.astrogrid.applications.manager.ApplicationController#queryApplicationExecutionStatus(java.lang.String)
    */
   public String queryApplicationExecutionStatus(String executionId)
      throws RemoteException {
         return binding.queryApplicationExecutionStatus(executionId);
   }

   /**
    * 
    * @see org.astrogrid.applications.manager.ApplicationController#returnRegistryEntry()
    */
   public String returnRegistryEntry() throws RemoteException {
      return binding.returnRegistryEntry();
   }

}
