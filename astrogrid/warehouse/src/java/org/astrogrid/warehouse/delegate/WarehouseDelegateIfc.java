/*
 * $Id: WarehouseDelegateIfc.java,v 1.1 2003/10/06 15:47:49 kea Exp $
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
 * Blah blah blah.
 *
 * @author K Andrews 
 */
public interface WarehouseDelegateIfc
{
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
  public String makeQuery(Element adql, String resultsFormat);

 /**
  * set the myspace server where the results should be stored, for the 
  * given query id.
  */
  public void setResultsDestination(String id, URL myspace); 

 /** 
  * starts given query running
  */
  public void startQuery(String id);

 /**
  * - stops the given query
  */
  public void abortQuery(String id);

 /**
  * - gets info on the query status
  */
  public Element getStatus(String id);
}

/*
$Log: WarehouseDelegateIfc.java,v $
Revision 1.1  2003/10/06 15:47:49  kea
Initial check-in of interface skeletons, maven setup etc.

*/
