/*
 * $Id: CommonExecutionConnectorDummyDelegate.java,v 1.3 2004/08/18 12:30:43 nw Exp $
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

import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Tool;

import java.net.URI;

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
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#abort(java.lang.String)
    */
   public boolean abort(final String executionId) throws CEADelegateException {
       return true;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#queryExecutionStatus(java.lang.String)
    */
   public MessageType queryExecutionStatus(final String executionId) throws CEADelegateException {
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

/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#init(org.astrogrid.workflow.beans.v1.Tool, org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType)
 */
public String init(Tool tool, JobIdentifierType jobstepID) throws CEADelegateException {
    // TODO Auto-generated method stub
     throw new  UnsupportedOperationException("CommonExecutionConnectorDummyDelegate.init() not implemented");
 
}

/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#registerProgressListener(java.lang.String, java.net.URI)
 */
public void registerProgressListener(String executionId, URI listenerEndpoint) throws CEADelegateException {
  }

/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#registerResultsListener(java.lang.String, java.net.URI)
 */
public void registerResultsListener(String executionId, URI listenerEndpoint) throws CEADelegateException {
  }

/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#execute(java.lang.String)
 */
public boolean execute(String executionId) throws CEADelegateException {
    return true;
}

/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#getResults(java.lang.String)
 */
public ResultListType getResults(String executionId) throws CEADelegateException {
    // TODO Auto-generated method stub
     throw new  UnsupportedOperationException("CommonExecutionConnectorDummyDelegate.getResults() not implemented");
 
}

/**
 * @see org.astrogrid.applications.delegate.CommonExecutionConnectorClient#getExecutionSumary(java.lang.String)
 */
public ExecutionSummaryType getExecutionSumary(String executionId) throws CEADelegateException {
    // TODO Auto-generated method stub
     throw new  UnsupportedOperationException("CommonExecutionConnectorDummyDelegate.getExecutionSummary() not implemented");
 
}


}
