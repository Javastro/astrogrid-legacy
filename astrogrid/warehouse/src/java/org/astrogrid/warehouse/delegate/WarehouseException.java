/*
 * $Id: WarehouseException.java,v 1.1 2003/10/08 15:25:35 kea Exp $
 */
package org.astrogrid.warehouse.delegate;

import java.rmi.RemoteException;

/**
 * Exception thrown by data warehouse delegates.  
 * Extends the java.rmi.RemoteException thrown by Axis web services.
 *
 * @author K Andrews
 */

public class WarehouseException extends java.rmi.RemoteException
{
  public WarehouseException(String message)
  {
    super(message);
  }

  public WarehouseException(String message, Throwable cause)
  {
    super(message, cause);
  }
}

/*
$Log: WarehouseException.java,v $
Revision 1.1  2003/10/08 15:25:35  kea
Finalised interface classes required for end IT4 wk 2:
    org.astrogrid.warehouse.delegate.WarehouseDelegateIfc
    org.astrogrid.warehouse.service.WarehouseServiceIfc

Changed URL parameter to String parameter in setResultsDestination()
  methods to help with wsdl2java/java2wsdl auto-tooling.

Added package-specific WarehouseException.

Added wsdd files for deploying to Tomcat/Axis, and added temporary
testing harness just to be sure we can deploy to Axis and talk to the
(mostly unimplemented!) web service.

*/
