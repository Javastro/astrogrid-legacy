/*
 * $Id: WarehouseServiceImpl.java,v 1.2 2003/10/08 15:25:35 kea Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.warehouse.service;

import java.io.IOException;
import java.rmi.RemoteException;

import org.w3c.dom.Element;

/**
 * At the moment, basically a clone of the WarehouseDelegate interface.
 * Can add service-specific functionality in here later.
 *
 * @author K Andrews 
 */
public class WarehouseServiceImpl
{
/**
  * Returns the metadata file
  * @soap
  */
  public Element getMetadata() throws java.rmi.RemoteException {
      throw new RemoteException("Method not implemented yet\n");
  }

   /**
    * Returns metadata in a format suitable for a VO Registry
    * @soap
    */
  public Element getVoRegistryMetadata() throws java.rmi.RemoteException {
      throw new RemoteException("Method not implemented yet\n");
  }

 /**
  * Returns VOTable
  */
  /*
  public Element coneSearch(double ra, double dec, double radius) 
                            throws java.rmi.RemoteException {
    throw new RemoteException("Method not implemented yet\n");
   }
 */

 /**
 * takes an adql document and returns an id for the query.
 */
  public String makeQuery(Element adql, String resultsFormat) 
                          throws java.rmi.RemoteException {
      return "123456";
  }

 /**
  * set the myspace server where the results should be stored, for the 
  * given query id.
  */
  public void setResultsDestination(String id, String myspaceUrl) 
                                    throws java.rmi.RemoteException {
    throw new RemoteException("Method not implemented yet\n");
  }

 /** 
  * starts given query running
  */
  public void startQuery(String id) throws java.rmi.RemoteException {
    throw new RemoteException("Method not implemented yet\n");
  }

 /**
  * - stops the given query
  */
  public void abortQuery(String id) throws java.rmi.RemoteException {
    throw new RemoteException("Method not implemented yet\n");
  }

 /**
  * - gets info on the query status
  */
  public Element getStatus(String id) throws java.rmi.RemoteException {
    throw new RemoteException("Method not implemented yet\n");
  }
}
/*
$Log: WarehouseServiceImpl.java,v $
Revision 1.2  2003/10/08 15:25:35  kea
Finalised interface classes required for end IT4 wk 2:
    org.astrogrid.warehouse.delegate.WarehouseDelegateIfc
    org.astrogrid.warehouse.service.WarehouseServiceIfc

Changed URL parameter to String parameter in setResultsDestination()
  methods to help with wsdl2java/java2wsdl auto-tooling.

Added package-specific WarehouseException.

Added wsdd files for deploying to Tomcat/Axis, and added temporary
testing harness just to be sure we can deploy to Axis and talk to the
(mostly unimplemented!) web service.

Revision 1.1  2003/10/07 17:09:51  kea
Adding webservice / webdelegate skeletons.
Having diffs with wsdl2java-generated class names/structure.

*/
