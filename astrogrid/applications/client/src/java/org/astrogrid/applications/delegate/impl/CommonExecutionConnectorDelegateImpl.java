/*
 * $Id: CommonExecutionConnectorDelegateImpl.java,v 1.4 2004/09/06 15:21:58 nw Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.service.v1.cea.CommonExecutionConnector;
import org.astrogrid.applications.service.v1.cea.CommonExecutionConnectorServiceLocator;
import org.astrogrid.applications.service.v1.cea.CommonExecutionConnectorServiceSoapBindingStub;
import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.common.bean.Castor2Axis;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.axis._tool;

import org.apache.axis.types.URI.MalformedURIException;

import java.net.URI;
import java.net.URL;
import java.rmi.RemoteException;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class CommonExecutionConnectorDelegateImpl
   extends CommonExecutionConnectorDelegate {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(CommonExecutionConnectorDelegateImpl.class);

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
   public String init(Tool tool, JobIdentifierType jobstepID)
      throws CEADelegateException {
      String result;
      CommonExecutionConnector cec = getBinding();
      try {
         
         _tool atool = Castor2Axis.convert(tool);
         
         result = cec.init(atool, jobstepID);
      }
      catch (RemoteException e) {
         logger.error(e);
         throw new CEADelegateException("failed to 'init' cea application" + jobstepID, e);
      }


        if (log.isTraceEnabled()) {
            log.trace("init(Tool, JobIdentifierType) - end - return value = " + result);
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
          logger.error(e);
         throw new CEADelegateException("failed to 'abort' cea application " + executionId, e);
      }
      return result;

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
          logger.error(e);
         throw new CEADelegateException("failed to query execution status for " + executionId, e);
      }
      return result;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#returnRegistryEntry()
    */
   public String returnRegistryEntry() throws CEADelegateException {
      String result;
      CommonExecutionConnector cec = getBinding();
      try {
       result = cec.returnRegistryEntry();
      }
      catch (RemoteException e) {
          logger.error(e);
         throw new CEADelegateException("failed to query cea for template registry entry", e);
      }
      return result;
     
   }

/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#registerProgressListener(java.lang.String, java.net.URI)
 */
public void registerProgressListener(String executionId, URI listenerEndpoint) throws CEADelegateException {
    CommonExecutionConnector cec = getBinding();
    try {
        org.apache.axis.types.URI uri = new org.apache.axis.types.URI(listenerEndpoint.toString());
        cec.registerProgressListener(executionId,uri);   
    } catch (RemoteException e) {
        logger.error(e);
        throw new CEADelegateException("problem registering progress listener" + listenerEndpoint + " for app " + executionId,e);
    } catch (MalformedURIException e) {
        // very unlikely to happen.
        throw new CEADelegateException("mismatch between java.net.URI and axis URI",e);
    }
}

/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#registerResultsListener(java.lang.String, java.net.URI)
 */
public void registerResultsListener(String executionId, URI listenerEndpoint) throws CEADelegateException {
    CommonExecutionConnector cec = getBinding();
     try {
         org.apache.axis.types.URI uri = new org.apache.axis.types.URI(listenerEndpoint.toString());
         cec.registerResultsListener(executionId,uri);   
     } catch (RemoteException e) {
         logger.error(e);
         throw new CEADelegateException("problem registering results listener " + listenerEndpoint + " for app " + executionId,e);
     } catch (MalformedURIException e) {
         // very unlikely to happen.
         throw new CEADelegateException("mismatch between java.net.URI and axis URI",e);
     }    
}

/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#execute(java.lang.String)
 */
public boolean execute(String executionId) throws CEADelegateException {
    CommonExecutionConnector cec = getBinding();
    try {
       return cec.execute(executionId);
    }
    catch (RemoteException e) {
        logger.error(e);
       throw new CEADelegateException("failed to execute application: " + executionId, e);
    }
    
}
 
/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#getResults(java.lang.String)
 */
public ResultListType getResults(String executionId) throws CEADelegateException {
    CommonExecutionConnector cec = getBinding();
    try {
       org.astrogrid.jes.types.v1.cea.axis.ResultListType axisList =  cec.getResults(executionId);
       return Axis2Castor.convert(axisList);
    }  catch (RemoteException e) {
        logger.error(e);
       throw new CEADelegateException("failed to get results for application " + executionId, e);
    }
}

/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#getExecutionSumary(java.lang.String)
 */
public ExecutionSummaryType getExecutionSumary(String executionId) throws CEADelegateException {
    CommonExecutionConnector cec = getBinding();
    try {
       org.astrogrid.jes.types.v1.cea.axis.ExecutionSummaryType axisSummary =  cec.getExecutionSummary(executionId);
       return Axis2Castor.convert(axisSummary);
    }
    catch (RemoteException e) {
       throw new CEADelegateException("problem when tying to get execution summary", e);
    }   
}

}
