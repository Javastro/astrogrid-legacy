/*
 * $Id: WarehouseDelegateIfc.java,v 1.2 2003/10/07 17:09:51 kea Exp $
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

/**
 * Delegate interface for sending jobs/queries to a grid-based data warehouse.
 *
 * To fix: What sort of exceptions should the methods throw?
 *  Currently throwing plain "Exception" (yes, evil) to allow for
 *  implementing classes to throw arbitrary exceptions, until I'm 
 *  enlightened as how to throw AG-standard exceptions.
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
      throws Exception;
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
      throws Exception;

 /**
  * set the myspace server where the results should be stored, for the 
  * given query id.
  */
  public void setResultsDestination(String id, URL myspace) 
      throws Exception; 

 /** 
  * starts given query running
  */
  public void startQuery(String id) throws Exception;

 /**
  * - stops the given query
  */
  public void abortQuery(String id) throws Exception;

 /**
  * - gets info on the query status
  */
  public Element getStatus(String id) throws Exception;
}

/*
$Log: WarehouseDelegateIfc.java,v $
Revision 1.2  2003/10/07 17:09:51  kea
Adding webservice / webdelegate skeletons.
Having diffs with wsdl2java-generated class names/structure.

Revision 1.1  2003/10/06 15:47:49  kea
Initial check-in of interface skeletons, maven setup etc.

*/
