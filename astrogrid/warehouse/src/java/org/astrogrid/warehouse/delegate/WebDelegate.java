/*
 * $Id: WebDelegate.java,v 1.1 2003/10/07 17:09:51 kea Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.warehouse.delegate;

import java.net.URL;
/*
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
*/

import org.astrogrid.warehouse.common.QueryStatus;
import org.w3c.dom.Element;
import org.astrogrid.warehouse.service.WarehouseServiceIfcSoapBindingStub;
import org.astrogrid.warehouse.service.WarehouseServiceLocator;

/**
 * Delegate implementation (currently just a dummy) for invoking a 
 * warehouse web service.
 *
 * @author K Andrews 
 */
public class WebDelegate implements WarehouseDelegateIfc
{
  protected WarehouseServiceIfcSoapBindingStub binding;

  public WebDelegate(URL givenEndPoint) throws javax.xml.rpc.ServiceException{
    binding = (WarehouseServiceIfcSoapBindingStub) 
        new WarehouseServiceLocator().getWarehouseServiceIfc(givenEndPoint);
  }

 /**
  * Returns VOTable
  */
  /*
  public Element coneSearch(double ra, double dec, double radius);
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
      throws java.rmi.RemoteException {
    return binding.makeQuery(adql, resultsFormat);
  };

 /**
  * set the myspace server where the results should be stored, for the 
  * given query id.
  */
  public void setResultsDestination(String id, URL myspace) 
      throws java.rmi.RemoteException {
    binding.setResultsDestination(id, myspace);
  } 

 /** 
  * starts given query running
  */
  public void startQuery(String id) throws java.rmi.RemoteException {
    binding.startQuery(id);
  }

 /**
  * - stops the given query
  */
  public void abortQuery(String id) throws java.rmi.RemoteException {
    binding.abortQuery(id);
  }

 /**
  * - gets info on the query status
  */
  public Element getStatus(String id) throws java.rmi.RemoteException {
    return binding.getStatus(id);
  }
}

/*
$Log: WebDelegate.java,v $
Revision 1.1  2003/10/07 17:09:51  kea
Adding webservice / webdelegate skeletons.
Having diffs with wsdl2java-generated class names/structure.

*/
