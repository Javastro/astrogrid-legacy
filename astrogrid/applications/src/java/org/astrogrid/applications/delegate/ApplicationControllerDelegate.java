/*
 * $Id: ApplicationControllerDelegate.java,v 1.1 2003/11/25 12:25:26 pah Exp $
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

import org.astrogrid.applications.delegate.beans.ApplicationDescription;
import org.astrogrid.applications.delegate.beans.ParameterValues;


/**
 * ApplicationController Delegate
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationControllerDelegate implements ApplicationController {

   private ApplicationControllerServiceSoapBindingStub binding = null;
   
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
   
   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#listApplications()
    */
   public String[] listApplications() throws RemoteException {
        return binding.listApplications(); 
    }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#getApplicationDescription(java.lang.String)
    */
   public ApplicationDescription getApplicationDescription(String applicationID)
      throws RemoteException {
         return binding.getApplicationDescription(applicationID);
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
       return binding.initializeApplication(applicationID, jobstepID, jobMonitorURL, parameters);
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#executeApplication(int)
    */
   public void executeApplication(int executionId) throws RemoteException {
      binding.executeApplication(executionId);
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#queryApplicationExecutionStatus(int)
    */
   public String queryApplicationExecutionStatus(int executionId)
      throws RemoteException {
         return binding.queryApplicationExecutionStatus(executionId);
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.ApplicationController#returnRegistryEntry()
    */
   public String returnRegistryEntry() throws RemoteException {
      return binding.returnRegistryEntry();
   }

}
