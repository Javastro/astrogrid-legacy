package org.astrogrid.warehouse.ogsadai;

import java.util.GregorianCalendar;
import junit.framework.TestCase;
import org.gridforum.ogsi.GridService;

/**
 * Tests {@link GdsDelegate}.
 * Live tests are performed against a grid service.
 */
public class GdsDelegateTest extends TestCase {
	public GdsDelegateTest() {}
	
	/**
	 * Exercise connect, findFactoryUrlFromRegistry, and performSelect methods.
	 */ 
	
	private String testRegistryUrl1
		= "http://astrogrid.ast.cam.ac.uk:4040/gdw/services/ogsadai/DAIServiceGroupRegistry";
	private String testRegistryUrl2
		= "http:/					/hydra.star.le.ac.uk:8082/gdw/services/ogsadai/DAIServiceGroupRegistry";	
	private String query1 = "SELECT * FROM first WHERE POS_EQ_DEC &gt; 59.9";	
	private String query2 = "SELECT * FROM first WHERE POS_EQ_RA &lt; 0.5";
	private int timeoutValue = 15;		

	public void testGetFactoryUrlFromRegistry(){
		GdsDelegate gds = new GdsDelegate();
		
		// Make sure that DAI service locator and port type are null.
		assertNull(gds.gdsrLocator);
		assertNull(gds.gdsrPort);
		assertNull(gds.portTypes);
		assertNull(gds.query);
		assertNull(gds.result);
				
		// Get factory URL from registry
		gds.getFactoryUrlFromRegistry(testRegistryUrl1, timeoutValue);

		// Make sure that DAI service locator and port type are not null.
		assertNotNull(gds.gdsrLocator);
		assertNotNull(gds.gdsrPort);
		assertNotNull(gds.portTypes);
		assertNotNull(gds.query);
		assertNotNull(gds.result);
						
		// Test that no "can't find registry" exception is thrown.
		// Test that registry is found.
		// Test that "no locators" exception is not thrown.
		// Test that "no handles" exception is not thrown.
		// Test that haveRegistryUrl is true.
		// Test that "no factories registered" exception is not thrown.
		// Test that "could not find factory url" exception is not thrown.
		// Test factoryURL string
	}
	
	public void testConnect(){

		GdsDelegate gds = new GdsDelegate();
				
		// Make sure that service is initially unconnected and factory
		// port, service instance, and application port are null.
		assertFalse(gds.connected);
		assertNull(gds.factoryPort);
		assertNull(gds.serviceInstance);
		assertNull(gds.applicationPort);
		
		// Connect the service.
		gds.connect();
				
		// Make sure that service is now connected and factory
		// port, service instance, and application port are not null.		
		assertTrue(gds.connected);
		assertNotNull(gds.factoryPort);
		assertNotNull(gds.serviceInstance);
		assertNotNull(gds.applicationPort);
				
	}
	
	public void testPerformSelect(){
		// Test "could not build doc" exception is not thrown.
		// Test "could not execute query" exception is not thrown.
		// Test result.	
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
