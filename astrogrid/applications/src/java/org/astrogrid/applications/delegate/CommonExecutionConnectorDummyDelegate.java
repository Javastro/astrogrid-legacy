/*
 * $Id: CommonExecutionConnectorDummyDelegate.java,v 1.2 2004/03/23 12:51:25 pah Exp $
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

package org.astrogrid.applications.delegate;

import java.rmi.RemoteException;

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.service.v1.cea.CeaFault;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Tool;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk) 11-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class CommonExecutionConnectorDummyDelegate extends CommonExecutionConnectorDelegate {

   /**
    * 
    */
   public CommonExecutionConnectorDummyDelegate() {
      super();
      // TODO Auto-generated constructor stub
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#execute(org.astrogrid.workflow.beans.v1.Tool, org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType, java.lang.String)
    */
   public String execute(Tool tool, JobIdentifierType jobstepID, String jobMonitorURL) throws CEADelegateException {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("CommonExecutionConnectorDummyDelegate.execute() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#abort(java.lang.String)
    */
   public boolean abort(String executionId) throws CEADelegateException {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("CommonExecutionConnectorDummyDelegate.abort() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#listApplications()
    */
   public ApplicationList listApplications() throws CEADelegateException {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("CommonExecutionConnectorDummyDelegate.listApplications() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#getApplicationDescription(java.lang.String)
    */
   public String getApplicationDescription(String applicationID) throws CEADelegateException {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("CommonExecutionConnectorDummyDelegate.getApplicationDescription() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#queryExecutionStatus(java.lang.String)
    */
   public MessageType queryExecutionStatus(String executionId) throws CEADelegateException {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("CommonExecutionConnectorDummyDelegate.queryExecutionStatus() not implemented");
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#returnRegistryEntry()
    */
   public String returnRegistryEntry() throws CEADelegateException {
      // TODO Auto-generated method stub
      throw new  UnsupportedOperationException("CommonExecutionConnectorDummyDelegate.returnRegistryEntry() not implemented");
   }


}
