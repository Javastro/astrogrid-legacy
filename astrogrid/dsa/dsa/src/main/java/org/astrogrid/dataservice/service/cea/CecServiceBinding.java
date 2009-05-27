/*
 * Copyright 2009 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.dataservice.service.cea;

import java.rmi.RemoteException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.service.v1.cea.CeaFault;
import org.astrogrid.applications.service.v1.cea.CommonExecutionConnector;
import org.astrogrid.applications.service.v1.cea.CommonExecutionConnectorServiceSoapBindingImpl;
import org.astrogrid.security.AxisServiceSecurityGuard;

/**
 * A specialization of the standard, CEC SOAP-binding to better handle
 * credentials. User credentials are obtained from the authentication handler
 * and cached in the application class (the latter has a static map of
 * credentials keyed by job ID).
 */
public class CecServiceBinding 
    extends CommonExecutionConnectorServiceSoapBindingImpl
    implements CommonExecutionConnector {

  private static final Log logger = LogFactory.getLog(CecServiceBinding.class);


   /**
    * @see org.astrogrid.applications.service.v1.cea.CommonExecutionConnector#execute(java.lang.String)
    */
  @Override
  public boolean execute(String arg0) throws RemoteException, CeaFault {
    logger.debug(String.format("execute(%s)", arg0));
    try {
      AxisServiceSecurityGuard g =
          AxisServiceSecurityGuard.getInstanceFromContext();
      DatacenterApplication.putCredentials(arg0, g);
      return cec.execute(arg0);
    }
    catch (Exception e) {
      logger.error("execute("+ arg0 + ")", e);
      throw CeaFault.makeFault(e);
    }
    catch (Throwable e) {
      logger.error("execute("+ arg0 +")", e);
      throw CeaFault.makeFault(new Exception("a throwable occurred in execute-"+e.getMessage(),e)); 
    }
  }

}
