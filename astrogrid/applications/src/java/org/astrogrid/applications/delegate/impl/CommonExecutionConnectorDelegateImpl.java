/*
 * $Id: CommonExecutionConnectorDelegateImpl.java,v 1.3 2004/03/30 22:45:09 pah Exp $
 * 
 * Created on 11-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.delegate.impl;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorDelegate;
import org.astrogrid.applications.service.v1.cea.CommonExecutionConnector;
import org.astrogrid.applications.service.v1.cea.CommonExecutionConnectorServiceLocator;
import org
   .astrogrid
   .applications
   .service
   .v1
   .cea
   .CommonExecutionConnectorServiceSoapBindingStub;
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.axis._tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class CommonExecutionConnectorDelegateImpl
   extends CommonExecutionConnectorDelegate {

   public CommonExecutionConnectorDelegateImpl(String targetEndPoint) {
      this.targetEndPoint = targetEndPoint;
   }

   public CommonExecutionConnectorDelegateImpl(String targetEndPoint, int timeout) {
      this.targetEndPoint = targetEndPoint;
      this.timeout = timeout;
   }

   private CommonExecutionConnector getBinding() throws CEADelegateException {
      try {
         CommonExecutionConnectorServiceSoapBindingStub binding =
            (CommonExecutionConnectorServiceSoapBindingStub)new CommonExecutionConnectorServiceLocator()
               .getCommonExecutionConnectorService(
               new URL(this.getTargetEndPoint()));
         binding.setTimeout(this.getTimeout());
         return binding;
      }
      catch (Exception e) {
         throw new CEADelegateException("cannot create binding", e);
      }

   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#execute(org.astrogrid.workflow.beans.v1.Tool, org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, java.lang.String)
    */
   public String execute(Tool tool, JobIdentifierType jobstepID, String jobMonitorURL)
      throws CEADelegateException {
      String result;
      CommonExecutionConnector cec = getBinding();
      try {
         _tool atool = Castor2Axis.convert(tool);
         result = cec.execute(atool, jobstepID, jobMonitorURL);
      }
      catch (RemoteException e) {
         throw new CEADelegateException("execution problem", e);
      }
      return result;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#abort(java.lang.String)
    */
   public boolean abort(String executionId) throws CEADelegateException {
      boolean result;
      CommonExecutionConnector cec = getBinding();
      try {
         result = cec.abort(executionId);
      }
      catch (RemoteException e) {
         throw new CEADelegateException("problem when tying to abort", e);
      }
      return result;

   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#listApplications()
    */
   public ApplicationList listApplications() throws CEADelegateException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommonExecutionConnectorDelegateImpl.listApplications() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#getApplicationDescription(java.lang.String)
    */
   public String getApplicationDescription(String applicationID)
      throws CEADelegateException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommonExecutionConnectorDelegateImpl.getApplicationDescription() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#queryExecutionStatus(java.lang.String)
    */
   public MessageType queryExecutionStatus(String executionId)
      throws CEADelegateException {
      MessageType result=null;
      CommonExecutionConnector cec = getBinding();
      try {
         org.astrogrid.jes.types.v1.cea.axis.MessageType message =
            cec.queryExecutionStatus(executionId);
            result = Axis2Castor.convert(message);
      }
      catch (RemoteException e) {
         throw new CEADelegateException("problem getting status", e);
      }
      return result;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#returnRegistryEntry()
    */
   public String returnRegistryEntry() throws CEADelegateException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("CommonExecutionConnectorDelegateImpl.returnRegistryEntry() not implemented");
   }

}
