package org.astrogrid.warehouse.ogsadai;

import java.util.GregorianCalendar;
import junit.framework.TestCase;
import org.gridforum.ogsi.GridService;

/**
 * Tests {@link GridServiceDelegate}.
 * Live tests are performed against a grid service.
 */
public class GridServiceDelegateTest extends TestCase {

    /**
     * A plausible Grid Service Handle (GSH) for a
     * service factory (actually the old echo service,
     * running in Cambridge).
     */
    private String factoryGsh  
        = "http://astrogrid.ast.cam.ac.uk:8080/ogsa/services/"
        + "astrogrid/echo/EchoFactory";
        
    /**
     * Controls simulation of connections to a service.
     */
    private boolean simulating = true;

  /** @link dependency */
  /*# GridServiceDelegate lnkGridServiceDelegate; */

  public GridServiceDelegateTest () {}

  public void testExceptionHandling() throws Exception {
    GridServiceDelegate g = new GridServiceDelegate();

    // Check that it starts with no errors.
    assertFalse(g.getThrowsExceptions());
    assertFalse(g.isFailed());
    assertEquals(g.getErrorMessage(), "OK");

    // Check that it registers errors correctly.
    String message = "Testing...";
    g.reportError(new Exception(message));
    assertEquals(message, g.getErrorMessage());
    assertFalse(g.getThrowsExceptions());
    assertTrue(g.isFailed());

    // Check that all the errors go away on demand.
    g.clearErrors();
    assertFalse(g.getThrowsExceptions());
    assertFalse(g.isFailed());
    assertEquals("OK", g.getErrorMessage());

    // Check that turning on exceptions causes them to be thrown.
    g.setThrowsExceptions(true);
    assertTrue(g.getThrowsExceptions());
    String message2 = message;
    try {
      g.reportError(new Exception(message));
      fail("The system swallowed an exception it should have thrown.");
      g.reportError(new Exception("Should never get here."));
    }
    catch (Exception e) {
      message2 = e.getMessage();
    }
    assertEquals(message, message2);
    
    // Check that the non-public methods don't swallow exceptions.
    // (Only the outermost method of a call-stack, the one
    // called directly by the JSP, should catch the exceptions
    // and call reportError for them. The protected methods
    // are never called by JSPs and they should always throw
    // their exceptions, so that the public methods in delegate
    // sub-classes can depend on the exceptions being thrown.
    g.setThrowsExceptions(false);
    g.setFactoryHandle("bogus");
    try {
      g.findFactoryPort();
      fail("findFactoryPort swallowed an exception.");
    }
    catch (Exception e) {}
    
    try {
      g.createServiceInstance();
      fail("createServiceInstance swallowed an exception.");
    }
    catch (Exception e) {}
    
    try {
      g.setInstanceTerminationTime(new GregorianCalendar());
      fail("setInstanceTerminationTime swallowed an exception.");
    }
    catch (Exception e) {}
    
    try {
      g.destroyServiceInstance();
      fail("destroyServiceInstance swallowed an exception.");
    }
    catch (Exception e) {}
  }

  public void testFactoryHandle() {
    GridServiceDelegate g = new GridServiceDelegate();
    g.setFactoryHandle(this.factoryGsh);
    assertEquals(this.factoryGsh, g.getFactoryHandle());
  }


  /**
   * Exercises the utility functions for connecting to factories
   * and service instances.  Connects to the echo service
   * at Cambridge.
   */
  public void testConnectionUtilities () throws Exception {
    
    // Associate a new GSD with the echo service.
    GridServiceDelegate echo = new GridServiceDelegate();
    echo.setThrowsExceptions(true);
    echo.setFactoryHandle(this.factoryGsh);
    
    // Make available the factory port (using the factory handle
    // that was set above).
    assertNull(echo.factoryPort);
    echo.findFactoryPort();
    assertNotNull(echo.factoryPort);

    // Make available a service instance (using the factory port
    // that was found above).
    assertNull(echo.instanceLocator);
    assertFalse(echo.isConnected());
    assertNull(echo.getInstanceHandle());
    echo.createServiceInstance();
    assertNotNull(echo.instanceLocator);
    assertTrue(echo.isConnected());
    assertNotNull(echo.getInstanceHandle());
    
    // Set the termination time of the service instance to 24hrs in
    // advance.
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.add(GregorianCalendar.DAY_OF_YEAR, 1);
    echo.setInstanceTerminationTime(calendar);
    
    // Check that the instance is still on-line.
    // This involves reading one of its SDEs.
    // Do this several times, as there is some evidence that
    // it might work once and fail the second time.
    for (int i = 1; i <= 5; i++) {
      System.out.println("Connection test #" + i);
      assertTrue(echo.isConnected());
    }

    // Destroy the instance (using the grid-service port enabled above).
    echo.destroyServiceInstance();
    assertNull(echo.instanceGridServicePort);
    assertNull(echo.instanceLocator);
    assertFalse(echo.isConnected());
  }
  
  
  /**
   * Tests the detection of missing instances.
   * This exercises the code for the important case where the
   * delegate thinks the instance is still present but the
   * instance has gone. C.f. the case where destroyServiceInstance
   * has been called on the delegate in which case the delegate
   * assumes that the isnatnce is dead.
   * This test only makes sense when simulation is off.
   */
  public void testDetectionOfMissingInstance () throws Exception {
    GridServiceDelegate d = this.getConfiguredDelegate();
    d.setThrowsExceptions(true);
    d.setSimulating(false);
    
    System.out.println("Testing detection of missing services...");
    assertFalse(d.isConnected());
    d.findFactoryPort();
    d.createServiceInstance();
    assertTrue(d.isConnected());
    GridService g = d.instanceGridServicePort;
    g.destroy();
    assertFalse(d.isConnected());
  }

  
  /**
   * Tests the availability of the {@link Delegate.simulating} property.
   */
  public void testSimulation () throws Exception {
    GridServiceDelegate d = new GridServiceDelegate();
    
    // Simulation should be off after construction.
    assertFalse(d.isSimulating());
    
    // Check that we can turn on simulation.
    d.setSimulating(true);
    assertTrue(d.isSimulating());
    
    // Check that we can turn off simulating.
    d.setSimulating(false);
    assertFalse(d.isSimulating());
  }

  
  
  
  /**
   * Creates a fresh delegate for a unit test and sets up that delegate
   * according to the test configuration listed above.
   */
  /*
  private GridServiceDelegate getConfiguredDelegate() throws Exception {
    GridServiceDelegate d = new GridServiceDelegate();
    d.setSimulating(this.simulating);
    d.setThrowsExceptions(true);
    d.setFactoryHandle(this.factoryGsh);
    return d;
  }
  */
}
