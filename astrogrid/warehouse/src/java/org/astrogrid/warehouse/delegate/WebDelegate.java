/* * $Id: WebDelegate.java,v 1.3 2003/10/23 17:19:45 kea Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.warehouse.delegate;

import java.net.URL;
/*
import java.io.IOException;
import java.net.MalformedURLException;
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

  public WebDelegate(URL givenEndPoint) throws WarehouseException{
    try {
      WarehouseServiceLocator locator = new WarehouseServiceLocator();
      binding = (WarehouseServiceIfcSoapBindingStub)
                 locator.getWarehouseServiceIfc(givenEndPoint);
    }
    catch (Exception e) {
      // TOFIX - Temporary error handling!
      // What about internationalisation of message etc?
      throw new WarehouseException("Couldn't bind to webservice",e);
    }
  }

 /**
  * Returns VOTable
  */
  /*
  public Element coneSearch(double ra, double dec, double radius) 
                            throws WarehouseException {
    try {
      binding.coneSearch(ra, dec, radius);
    }
    catch (Exception e) {
      // TOFIX - Temporary error handling!
      // What about internationalisation of message etc?
      throw new WarehouseException("Couldn't do cone search ",e);
    }
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
                          throws WarehouseException {
    try {
      return binding.makeQuery(adql, resultsFormat);
    }
    catch (Exception e) {
      // TOFIX - Temporary error handling!
      // What about internationalisation of message etc?
      throw new WarehouseException("Couldn't make query",e);
    }
  };

 /**
  * set the myspace server where the results should be stored, for the 
  * given query id.
  */
  public void setResultsDestination(String id, String myspaceUrl) 
                                    throws WarehouseException {
    try {
      binding.setResultsDestination(id, myspaceUrl);
    }
    catch (Exception e) {
      // TOFIX - Temporary error handling!
      // What about internationalisation of message etc?
      throw new WarehouseException("Couldn't set result destination",e);
    }
  } 

 /** 
  * starts given query running
  */
  public void startQuery(String id) throws WarehouseException {
    try {
      binding.startQuery(id);
    }
    catch (Exception e) {
      // TOFIX - Temporary error handling!
      // What about internationalisation of message etc?
      throw new WarehouseException("Couldn't start query",e);
    }
  }

 /**
  * - stops the given query
  */
  public void abortQuery(String id) throws WarehouseException {
    try {
      binding.abortQuery(id);
    }
    catch (Exception e) {
      // TOFIX - Temporary error handling!
      // What about internationalisation of message etc?
      throw new WarehouseException("Couldn't abort query",e);
    }
  }

 /**
  * - gets info on the query status
  */
  public Element getStatus(String id) throws WarehouseException {
    try {
      return binding.getStatus(id);
    }
    catch (Exception e) {
      // TOFIX - Temporary error handling!
      // What about internationalisation of message etc?
      throw new WarehouseException("Couldn't get query status",e);
    }
  }

 /** 
  * Temporary test harness - TOFIX - remove!
  */
  public static void main (String[] args) throws Exception {

    if (args.length != 1) {
      System.err.println("usage: WebDelegate <factory-URL>");
      return;
    }

    try {
      WebDelegate delegate = new WebDelegate(new URL(args[0]));

      System.out.print("Making dummy query...  ");
      String queryID = delegate.makeQuery(null,"dummy");
      System.out.println("done: query ID is "+ queryID);

      System.out.print("Setting destination...  ");
      delegate.setResultsDestination(queryID,"OGSA_TEST_OUTPUT");
      System.out.println("done...  ");

      System.out.print("Starting query...  ");
      delegate.startQuery(queryID);
      System.out.println("done...  ");
    }
    catch (WarehouseException e) {
      e.printStackTrace();
    }
  }
}

/*
$Log: WebDelegate.java,v $
Revision 1.3  2003/10/23 17:19:45  kea
Starting to add OGSA-DAI functionality to webservice implementation,
to allow webservice to perform queries on OGSA-DAI database.
Mostly commented out at the moment because tomcat is giving me big
heap trouble.

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
