/*
 * $Id: WarehouseDelegateIfc.java,v 1.3 2003/10/08 15:25:35 kea Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.warehouse.delegate;

import org.w3c.dom.Element;
import org.astrogrid.warehouse.common.QueryStatus;
import org.astrogrid.warehouse.delegate.WarehouseException;

/**
 * Delegate interface for sending jobs/queries to a grid-based data warehouse.
 *
 * Current status: These methods have been suggested as a starting point
 * by M Hill of the datacentre group.
 *
 * All methods throw a package-specific WarehouseException (a subclass of 
 * the java.rmi.RemoteException thrown by Axis webservices).
 *
 * @author K Andrews 
 */
public interface WarehouseDelegateIfc
{
 /**
  * Returns VOTable
  */
  /*
  public Element coneSearch(double ra, double dec, double radius) 
                            throws WarehouseException;
 */

 /**
 * takes an
 * adql document - this should really be an object model, which we have but
 * it's all tied up in the datacenter at the moment.  Returns an id for the
 * query. Constructs a query on the server but does not start it yet as we
 * may want to change properties on it.  'resultsFormat' at the moment is
 * only 'votable'
 */
  public String makeQuery(Element adql, String resultsFormat) 
                          throws WarehouseException;

 /**
  * set the myspace server where the results should be stored, for the 
  * given query id.
  */
  public void setResultsDestination(String id, String myspaceURL) 
                                    throws WarehouseException; 

 /** 
  * starts given query running
  */
  public void startQuery(String id) throws WarehouseException;

 /**
  * - stops the given query
  */
  public void abortQuery(String id) throws WarehouseException;

 /**
  * - gets info on the query status
  */
  public Element getStatus(String id) throws WarehouseException;
}

/*
$Log: WarehouseDelegateIfc.java,v $
Revision 1.3  2003/10/08 15:25:35  kea
Finalised interface classes required for end IT4 wk 2:
    org.astrogrid.warehouse.delegate.WarehouseDelegateIfc
    org.astrogrid.warehouse.service.WarehouseServiceIfc

Changed URL parameter to String parameter in setResultsDestination()
  methods to help with wsdl2java/java2wsdl auto-tooling.

Added package-specific WarehouseException.

Added wsdd files for deploying to Tomcat/Axis, and added temporary
testing harness just to be sure we can deploy to Axis and talk to the
(mostly unimplemented!) web service.

Revision 1.2  2003/10/07 17:09:51  kea
Adding webservice / webdelegate skeletons.
Having diffs with wsdl2java-generated class names/structure.

Revision 1.1  2003/10/06 15:47:49  kea
Initial check-in of interface skeletons, maven setup etc.

*/
