/*
 * $Id: WebServiceApplicationController.java,v 1.4 2004/03/29 12:32:11 pah Exp $
 *
 * Created on 09 February 2004 by Paul Harrison
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.applications.manager;

import java.rmi.RemoteException;

import org.apache.axis.description.ServiceDesc;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.ParameterValues;
import org.astrogrid.applications.common.config.CeaControllerConfig;
import org.astrogrid.applications.manager.externalservices.*;
import org.astrogrid.applications.service.v1.cea.CeaFault;
import org.astrogrid.community.User;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.MessageType;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.axis._tool;

public class WebServiceApplicationController extends AbstractApplicationController {

   /**
    * @param registryQueryLocator
    * @param mySpaceLocator
    */
   public WebServiceApplicationController(CeaControllerConfig conf,
      RegistryQueryLocator registryQueryLocator, RegistryAdminLocator adminLocator,
      MySpaceLocator mySpaceLocator) {
      super(conf, registryQueryLocator, adminLocator, mySpaceLocator, new ServiceDesc());
      // TODO Auto-generated constructor stub
   }

 
   /** 
    * @see org.astrogrid.applications.manager.CommonExecutionController#abort(java.lang.String)
    */
   public boolean abort(String executionId) throws CeaException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("WebServiceApplicationController.abort() not implemented");
   }

   /** 
    * @see org.astrogrid.applications.manager.CommonExecutionController#execute(org.astrogrid.workflow.beans.v1.Tool, java.lang.String, java.lang.String)
    */
   public String execute(Tool tool, String jobstepID, String jobMonitorURL)
      throws CeaException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("WebServiceApplicationController.execute() not implemented");
   }

   /** 
    * @see org.astrogrid.applications.manager.CommonExecutionController#queryExecutionStatus(java.lang.String)
    */
   public org
      .astrogrid
      .applications
      .beans
      .v1
      .cea
      .castor
      .MessageType queryExecutionStatus(
      String executionId)
      throws CeaException {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("WebServiceApplicationController.queryExecutionStatus() not implemented");
   }

}
