/*
 * $Id: WarehouseServiceIfc.java,v 1.1 2003/10/06 15:47:49 kea Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.warehouse.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.w3c.dom.Element;

/**
 * At the moment, basically a clone of the WarehouseDelegate interface.
 * Can add service-specific functionality in here later.
 *
 * @author K Andrews 
 */
public interface WarehouseServiceIfc 
{
/**
  * Returns the metadata file
  * @soap
  */
  public Element getMetadata();

   /**
    * Returns metadata in a format suitable for a VO Registry
    * @soap
    */
  public Element getVoRegistryMetadata();

 /**
  * Returns VOTable
  */
  /*
  public Element coneSearch(double ra, double dec, double radius);
 */

 /**
 * takes an adql document and returns an id for the query.
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
$Log: WarehouseServiceIfc.java,v $
Revision 1.1  2003/10/06 15:47:49  kea
Initial check-in of interface skeletons, maven setup etc.

*/
