/**
 * $Id: ApplicationControllerServiceSoapBindingImpl.java,v 1.8 2003/12/17 17:16:54 pah Exp $
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.applications.service;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import org.astrogrid.applications.ParameterValues;
import org.astrogrid.applications.description.SimpleApplicationDescription;
import org.astrogrid.applications.manager.CommandLineApplicationController;
import org.astrogrid.community.User;

/**
 * The soap binding that calls the reall ApplicationController.
 * @TODO needs to work with different application controllers (e.g. datacentre)
 * @TODO needs better error handling
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public class ApplicationControllerServiceSoapBindingImpl
   implements org.astrogrid.applications.service.ApplicationController {

   static CommandLineApplicationController applicationController = null;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(
         ApplicationControllerServiceSoapBindingImpl.class);

   public ApplicationControllerServiceSoapBindingImpl() {
      // do we do anything special to check that  only one application controller is created?
      setupAppController();
   }

   private synchronized void setupAppController() {

      if (applicationController == null) {
         applicationController = new CommandLineApplicationController();
      }
   }

   public java.lang.String[] listApplications() throws java.rmi.RemoteException {
      return applicationController.listApplications();
   }

   public org
      .astrogrid
      .applications
      .delegate
      .beans
      .SimpleApplicationDescription getApplicationDescription(
         java.lang.String applicationID)
      throws java.rmi.RemoteException {
         org.astrogrid.applications.delegate.beans.SimpleApplicationDescription outdesc =
            new org.astrogrid.applications.delegate.beans.SimpleApplicationDescription();
      SimpleApplicationDescription desc =
         applicationController.getApplicationDescription(applicationID);
         
      if (desc != null) {
         try {
            BeanUtils.copyProperties(outdesc, desc);
         }
         catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
      else
      {
         logger.warn("request for information about unknown application " + applicationID);
         outdesc.setName("unknown");
         outdesc.setXmlDescriptor("not found");
      }

      
      
      return outdesc;
   }

   public String initializeApplication(
      java.lang.String applicationID,
      java.lang.String jobstepID,
      java.lang.String jobMonitorURL,
      org.astrogrid.applications.delegate.beans.User user,
      org.astrogrid.applications.delegate.beans.ParameterValues parameters)
      throws java.rmi.RemoteException {
      User outuser = new User();
      ParameterValues outvals = new ParameterValues();
      try {

         BeanUtils.copyProperties(outuser, user);
         BeanUtils.copyProperties(outvals, parameters);

      }
      catch (IllegalAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (InvocationTargetException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return applicationController.initializeApplication(
         applicationID,
         jobstepID,
         jobMonitorURL,
         outuser,
         outvals);
   }

   public boolean executeApplication(String executionId) throws java.rmi.RemoteException {
      return applicationController.executeApplication(executionId);
   }

   public java.lang.String queryApplicationExecutionStatus(String executionId)
      throws java.rmi.RemoteException {
      return applicationController.queryApplicationExecutionStatus(executionId);
   }

   public java.lang.String returnRegistryEntry() throws java.rmi.RemoteException {
      return applicationController.returnRegistryEntry();
   }

}
