/**
 * ApplicationControllerServiceSoapBindingImpl.java
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

public class ApplicationControllerServiceSoapBindingImpl
   implements org.astrogrid.applications.service.ApplicationController {

   CommandLineApplicationController applicationController;

   public ApplicationControllerServiceSoapBindingImpl() {
      // do we do anything special to check that  only one application controller is created?
      applicationController = new CommandLineApplicationController();
   }
   public java.lang.String[] listApplications()
      throws java.rmi.RemoteException {
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
      SimpleApplicationDescription desc =
         applicationController.getApplicationDescription(applicationID);
      org
         .astrogrid
         .applications
         .delegate
         .beans
         .SimpleApplicationDescription outdesc =
         new org
            .astrogrid
            .applications
            .delegate
            .beans
            .SimpleApplicationDescription();
      try {
         BeanUtils.copyProperties(desc, outdesc);
      }
      catch (IllegalAccessException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (InvocationTargetException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return outdesc;
   }

   public int initializeApplication(
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

   public boolean executeApplication(int executionId)
      throws java.rmi.RemoteException {
      return applicationController.executeApplication(executionId);
   }

   public java.lang.String queryApplicationExecutionStatus(int executionId)
      throws java.rmi.RemoteException {
      return applicationController.queryApplicationExecutionStatus(executionId);
   }

   public java.lang.String returnRegistryEntry()
      throws java.rmi.RemoteException {
      return applicationController.returnRegistryEntry();
   }

}
