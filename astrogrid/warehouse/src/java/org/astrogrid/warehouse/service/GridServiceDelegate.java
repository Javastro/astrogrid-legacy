package org.astrogrid.warehouse.service;

import java.net.URL;
import java.rmi.Remote;
import java.util.Calendar;
import org.gridforum.ogsi.ExtendedDateTimeType;
import org.gridforum.ogsi.ExtensibilityType;
import org.gridforum.ogsi.Factory;
import org.gridforum.ogsi.FaultType;
import org.gridforum.ogsi.GridService;
import org.gridforum.ogsi.HandleType;
import org.gridforum.ogsi.LocatorType;
import org.gridforum.ogsi.OGSIServiceGridLocator;
import org.globus.ogsa.impl.core.service.ServiceLocator;
import org.globus.ogsa.utils.GridServiceFactory;
import org.globus.ogsa.wsdl.GSR;
import org.globus.ogsa.utils.QueryHelper;


public class GridServiceDelegate {

  /**
   * The Grid Service Handle for the service's factory.
   * Since this code is built for Globus Toolkit 3,
   * the handle is expected to be a URL.
   */
  protected String     factoryHandle    = null;
  protected HandleType instanceHandle   = null;
  protected Remote     applicationPort  = null; 
  protected boolean    connected        = false;
  private   boolean    throwsExceptions = false;
  private   Exception  error            = null;
  private   boolean    simulating       = false;
  

  /**
   * Gives access to operations on the Factory port; a web-service stub.
   */
  protected Factory factoryPort = null;

  /**
   * Gives access to operation on the instance's grid-service port;
   * a web-service stub.
   */
  protected GridService instanceGridServicePort = null;

  /**
   * Identifies the service instance.  The GSH and GSR can be got
   * from this property. The proxies for the service's ports
   * can be got directly without using the GSH or GSR explictly.
   */
  protected LocatorType instanceLocator = null;


  /** 
   * Returns the Grid Service Handle for the factory that will make 
   * the service instance managed by this delegate.
   */
  public String getFactoryHandle() {
    return this.factoryHandle;
  }

  /**
   * Sets the Grid Service Handle for the factory that will make the 
   * service instance managed by this delegate.
   */
  public void setFactoryHandle(String factoryHandle) {
    this.factoryHandle = factoryHandle;
  }


  /**
   * Return the Grid Service Handle for the service instance, 
   * or null if no instance is connected.
   */
  public String getInstanceHandle () throws Exception {
    if (this.instanceHandle != null) {
      return this.instanceHandle.toString();
    }
    else {
      return null;
    }
  }


  /**
   * Determines whether this delegate throws exceptions 
   * (the alternative is to catch the exceptions and to present
   * their messages through the bean properties).
   */
  public boolean getThrowsExceptions() {
    return this.throwsExceptions;
  }

  /**
   * Specifies whether this delegate throws exceptions (the alternative is to catch the exceptions and to present
   * their messages through the bean properties).
   */
  public void setThrowsExceptions(boolean throwsExceptions) {
    this.throwsExceptions = throwsExceptions;
  }

  /**
   * Indicates whether this delegate has caught an exception
   * since construction or since the last call to clearErrors.
   */
  public boolean isFailed() {
    return (this.error != null);
  }

  /**
  * Returns the error message from the last exception to be caught,
  * or "OK" if no exception has yet been caught.
  */
  public String getErrorMessage() {
    if (this.isFailed()) {
      return this.error.getMessage();
    }
    else {
      return "OK";
    }
  }
  

  /** 
   * Clears previously-caught exceptions, causing the object to behave as 
   * if it has not yet caught an exception.
   */
  public void clearErrors() {
    this.error  = null;
  }


  /**
   * Reports a given exception, either by throwing it or storing
   * it in the error property, according to the state of the
   * throwsException property.
   */
  public void reportError(Exception e) throws Exception {
    if (this.throwsExceptions) {
      throw e;
    }
    else {
      this.error = e;
    }
  }
  
  /**
   * Sets the flag for simulation of the link with the
   * delegate's service.  The interpretation of this flag
   * is determined by the subclasses of this class.
   */
  public void setSimulating (boolean simulating) {
    this.simulating = simulating;
  }
  
  /**
   * Reports whether the delegate is simulating the
   * link with its service.  The meaning of simulation
   * depends on the sub-classes of this class.
   */
  public boolean isSimulating () {
    return this.simulating;
  }
  
  
  /**
   * Talks to the service and checks whether an instance is
   * connected to the delegate.
   *
   * @return true if there is an instance connected; false otherwise.
   */
  public boolean isConnected () {
    System.out.println("GSD: isConnected(): testing " + this.hashCode());
    if (!this.connected) {
      System.out.println("GSD: already flagged as disconnected.");
      return false;
    }
    
    if (this.simulating) {
      System.out.println("GSD: returning simulated value: " + this.connected);
      return this.connected;
    }
    
    // Talk to the service.  Ask it to return the service datum
    // for its termination time.  The answer is irrelevant, but if
    // the query is successful we know that there is an instance
    // on-line.  This particular service datum is required to be
    // present by the OGSI specification. 
    try {
      System.out.println("GSD: forming query.");
      ExtensibilityType x = QueryHelper.getNamesQuery("terminationTime");
      System.out.println("GSD: getting terminateTime SDE from service.");
      this.instanceGridServicePort.findServiceData(x);
      this.connected = true;
    }
    catch (FaultType f) {
      System.out.println("GSD: fault from service: " +
                         f.dumpToString());
      this.recordServiceDisconnected();
    }
    catch (Exception e) {
      System.out.println("GSD: error from service: " 
                         + " "
                         + e.getClass().getName()
                         + " "
                         + e.toString());
      this.recordServiceDisconnected();
    }
    
    System.out.println("GSD: connected = " + this.connected);
    return this.connected;
  }
  
    
  /**
   * Finds the service factory and makes it available via the
   * attribute factoryPort.
   */
  protected void findFactoryPort () throws Exception {
    OGSIServiceGridLocator l = new OGSIServiceGridLocator();
    this.factoryPort = l.getFactoryPort(new URL(this.factoryHandle));
    // NB: this assumes that a GSH is a URL, which is true for GT3 but may
    // not be true for all OGSI implementations.
  }


  /**
   * Creates a service instance, records its locator and GSH; finds
   * and caches a stub for the grid-service port.
   */
  protected void createServiceInstance () throws Exception {
    System.out.println("GSD: locating the factory port...");
    GridServiceFactory g = new GridServiceFactory(this.factoryPort);
    System.out.println("GSD: creating the service instance...");
    this.instanceLocator = g.createService();
    this.connected= true;
    
    // Recover the GSH for the new instance.  This has to be done
    // indirectly, since the LocatorType for the instance doesn't
    // have it.
    System.out.println("GSD: locating the service instance...");
    ServiceLocator s = new ServiceLocator();
    s.getServicePort(this.instanceLocator);
    GSR gsr = s.getGSR();
    System.out.println("GSD: got the GSR");
    this.instanceHandle = gsr.getHandle();
    System.out.println("GSD: handle: " + this.instanceHandle.toString());
    
    // Find the grid-service port on this instance.
    OGSIServiceGridLocator l = new OGSIServiceGridLocator();
    this.instanceGridServicePort = l.getGridServicePort(this.instanceLocator);    
  }
  

  /**
   * Sets the termination time of the service instance.
   * findInstanceGridServicePort must have been called before this method.
   */
  protected void setInstanceTerminationTime 
      (Calendar calendar) throws Exception {
    ExtendedDateTimeType t = new ExtendedDateTimeType(calendar);
    this.instanceGridServicePort.requestTerminationAfter(t);
  }


  /**
   * Destroys the service instance.  Sets to null the attributes referring to
   * the instance. 
   */
  protected void destroyServiceInstance () throws Exception {
    // Clear the metadata before destroying the instance;
    // therefore, remember the port from the metadata.
    // Clearing the metadata and failing to destroy the instance
    // leaks the instance, but normal OGSI state-management should
    // clear that up.  Failing to clear the metadata is much
    // more harmful.
    GridService port = this.instanceGridServicePort;
    this.recordServiceDisconnected();
    port.destroy();
  }
  


  /**
   * Resets the connection metadata in this object to
   * show that the service is not connected.  This method
   * does not try to talk to the service instance or service
   * factory.
   */
  private void recordServiceDisconnected () {
    this.instanceHandle          = null;
    this.instanceLocator         = null;
    this.instanceGridServicePort = null;
    this.applicationPort         = null;
    this.connected               = false;
  }
 
}
