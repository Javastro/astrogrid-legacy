/*
 * $Id: CommonExecutionConnectorServiceSoapBindingImpl.java,v 1.2 2004/03/29 12:32:46 pah Exp $
 * 
 * Created on 25-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.service.v1.cea;

import java.rmi.RemoteException;

import org.apache.axis.description.ServiceDesc;


import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.axis.ceabase.ApplicationBase;
import org.astrogrid.applications.beans.v1.axis.ceabase._ApplicationList;
import org.astrogrid.applications.manager.CommandLineApplicationController;
import org.astrogrid.applications.service.v1.cea.impl._returnRegistryEntryResponse_returnRegistryEntryReturn;
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.axis._tool;

/**
 * This is the main implementation of the CommonExecutionConnectorService. This is the class that should be referenced in the Axis wsdd file. 
 * It works by creating an instance of {@link org.astrogrid.applications.manager.CommandLineApplicationController}, and passing off calls to that after doing the translation of axis objects to castor a 
 * @author Paul Harrison (pah@jb.man.ac.uk) 25-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class CommonExecutionConnectorServiceSoapBindingImpl
   implements CommonExecutionConnector {
      static private org.apache.commons.logging.Log logger =
         org.apache.commons.logging.LogFactory.getLog(
            CommonExecutionConnectorServiceSoapBindingImpl.class);
      
      protected final CommandLineApplicationController clec;

   /**
    * 
    */
   public CommonExecutionConnectorServiceSoapBindingImpl() {
      //REFACTORME - need to use picocontainer to get full IoC - this constructor uses configuration based locators...
      ServiceDesc servicedesc = org.apache.axis.MessageContext.getCurrentContext().getService().getServiceDescription();
      //FIXME - need to pass thsi into the application controller
      clec = new CommandLineApplicationController(servicedesc);
      
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#execute(org.astrogrid.workflow.beans.v1.axis._tool, org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, java.lang.String)
    */
   public String execute(_tool tool, JobIdentifierType jobstepID, String jobMonitorURL)
      throws RemoteException, CeaFault {
         Tool ctool = Axis2Castor.convert(tool);
         
         String executionId = null;
         
         try {
            executionId = clec.execute(ctool, jobstepID.toString(), jobMonitorURL);
         }
         catch (CeaException e) {
           throw CeaFault.makeFault(e);
         }
         return executionId;
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#abort(java.lang.String)
    */
   public boolean abort(String executionId) throws RemoteException, CeaFault {
      boolean result = false;
      result = clec.abort(executionId);
     return result;
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#listApplications()
    */
   public _ApplicationList listApplications() throws RemoteException, CeaFault {
      _ApplicationList applist = null;
      
      ApplicationList outlist;
      try {
         outlist = clec.listApplications();
      }
      catch (CeaException e) {
         throw CeaFault.makeFault(e);
      }
      applist = Castor2Axis.convert(outlist);
      return applist;
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#getApplicationDescription(java.lang.String)
    */
   public ApplicationBase getApplicationDescription(String applicationID)
      throws RemoteException {
         ApplicationBase result = null;
         org.astrogrid.applications.beans.v1.ApplicationBase ab;
         try {
            ab = clec.getApplicationDescription(applicationID);
         }
         catch (CeaException e) {
            throw CeaFault.makeFault(e);
         }
         result = Castor2Axis.convert(ab);
         return result;
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#queryExecutionStatus(java.lang.String)
    */
   public MessageType queryExecutionStatus(String executionId)
      throws RemoteException, CeaFault {
         MessageType result = null;
         try {
            org.astrogrid.applications.beans.v1.cea.castor.MessageType mess = clec.queryExecutionStatus(executionId);
            result = Castor2Axis.convert(mess);
         }
         catch (CeaException e) {
            throw CeaFault.makeFault(e);
         }
         return result;
   }

   /** 
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#returnRegistryEntry()
    */
   public _returnRegistryEntryResponse_returnRegistryEntryReturn returnRegistryEntry()
      throws RemoteException {
      // FIXME Auto-generated method stub
      throw new UnsupportedOperationException("CommonExecutionConnectorServiceSoapBindingImpl.returnRegistryEntry() not implemented");
   }
   



}
