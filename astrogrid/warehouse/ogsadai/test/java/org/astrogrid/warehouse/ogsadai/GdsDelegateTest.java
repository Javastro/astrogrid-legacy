package org.astrogrid.warehouse.ogsadai;

import junit.framework.TestCase;
import org.gridforum.ogsi.ExtensibilityType;

/**
 * Exercise connect, findFactoryUrlFromRegistry, and performSelect methods.
 * Live tests are performed against a grid service.
 *
 * THE TESTED CLASS HAS BEEN DEPRECATED AND IS NO LONGER USED.
 * @deprecated
 */
public class GdsDelegateTest extends TestCase {

  public GdsDelegateTest() {}
		
  private String factoryGsh2
      = "http://hydra.star.le.ac.uk:8082/gdw/services/"
      + "ogsadai/GridDataServiceFactory";

  private ExtensibilityType document = null;
  private ExtensibilityType result = null;	
	
  private String testRegistryUrl1
    = "http://astrogrid.ast.cam.ac.uk:4040/gdw/services/ogsadai/DAIServiceGroupRegistry";
  private String testRegistryUrl2
    = "http://hydra.star.le.ac.uk:8082/gdw/services/ogsadai/DAIServiceGroupRegistry";	

  private String query1 = "SELECT * FROM first WHERE DECL &gt; 59.9 LIMIT 100";
  private String query2 = "SELECT * FROM first WHERE RA &lt; 0.5 LIMIT 100";

  private int timeoutValue = 15;		


  /**
   * Test method to acquire a grid service handle from the registry
   * and set the handle to the gds instance.
   */
  public void testRegistryLookup1() throws Exception {
    GdsDelegate gds = new GdsDelegate();
    String factoryPort = null;
		
    // Test method with valid testRegistryUrl:
    try {
      gds.setFactoryGshFromRegistry(testRegistryUrl2, timeoutValue);
      assertNotNull(gds.getFactoryHandle());
    }
    catch (Exception e) {}
		
    // Test method with invalid testRegistryUrl:
    String bogusRegistryUrl = "bogus";
    try {
      gds.setFactoryGshFromRegistry(bogusRegistryUrl, timeoutValue);
      fail("Factory handle passed back from bogus registry");
    }
    catch (Exception e) {}	

    // Test method with invalid timeoutValue:
    int bogusTimeoutValue = -1;
    try {
      gds.setFactoryGshFromRegistry(testRegistryUrl2, bogusTimeoutValue);
      fail("Factory handle passed back with timeoutValue set to -1");
    }
    catch (Exception e){}
  }


  /**
   * Test the reading of the factory GSH from the registry.
   * This tests the no-argument version of setFactoryGshFromRegistry
   * that relies on the registry GSH being set through setRegistryGsh.
   * A valid GSH for the registry is passed and a valid GSH
   * for the factory is expected.
   */
  public void testRegistryLookup3 () throws Exception {
    GdsDelegate gds = new GdsDelegate();
    gds.setRegistryGsh(testRegistryUrl2);
    gds.setFactoryGshFromRegistry();
    assertNotNull(gds.getFactoryHandle());
  }


  /**
   * Test the reading of the factory GSH from the registry.
   * This tests the no-argument version of setFactoryGshFromRegistry
   * that relies on the registry GSH being set through setRegistryGsh.
   * A bogus GSH for the registry is passed and an exception
   * is expected.
   */
  public void testRegistryLookup4 () throws Exception {
    GdsDelegate gds = new GdsDelegate();
    gds.setRegistryGsh("bogus");
    try {
      gds.setFactoryGshFromRegistry();
      fail("setFactoryGshFromRegistry() returned without " +
           "exception for a bogus registry-GSH.");
    }
    catch (Exception e) {}
    assertNull(gds.getFactoryHandle());
  }
    

  /**
   * Test service connection.
   */	
  public void testConnect() throws Exception {
    GdsDelegate gds = new GdsDelegate();
    gds.setRegistryGsh(testRegistryUrl2);
    gds.setFactoryGshFromRegistry();
				
    // Make sure that service is initially unconnected and factory
    // port, service instance, and application port are null.
    assertFalse(gds.connected);
    assertNull(gds.factoryPort);
    assertNull(gds.applicationPort);
		
    // Connect the service.
    try {
      gds.connect();
    }
    catch (Exception e) {
      fail("Service did not connect: " + e);
    }
				
    // Make sure that service is now connected and factory
    // port, service instance, and application port are not null.		
    assertTrue(gds.connected);
    assertNotNull(gds.factoryPort);
    assertNotNull(gds.applicationPort);
  }


  /**
   * Test the delegate methods for getting and setting
   * registry handles.
   */
  public void testRegistryHandle() throws Exception {
    GdsDelegate gds = new GdsDelegate();
    gds.setRegistryGsh(this.testRegistryUrl2);
    assertEquals(this.testRegistryUrl2, gds.getRegistryGsh());
  }

	
  /**
   * Test invocation of an SQL select statement on the GDS instance. 
   */
  public void testPerformSelect() throws Exception{
		
    // Set up the GDS delegate; make sure it knows where its factory is.
    GdsDelegate gds = new GdsDelegate();
    gds.setRegistryGsh(testRegistryUrl2);
    gds.setFactoryGshFromRegistry();
    gds.connect();

    // Run the query.
    ExtensibilityType result = gds.performSelect(this.query1);
		  
    // Make sure that query and result documents are now not null.
    assertNotNull(result);
		
    // Further tests: write a query that returns a single sky survey 
    // object from the GDW so that an "assertEquals" method could be 
    // applied practically (current queries return too many sky objects).
  }
}
