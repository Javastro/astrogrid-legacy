package org.astrogrid.warehouse.ogsadai;

import junit.framework.TestCase;
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
		= "http://hydra.star.le.ac.uk:8082/gdw/services/ogsadai/DAIServiceGroupRegistry";	
	private String query1 = "SELECT * FROM first WHERE POS_EQ_DEC &gt; 59.9";	
	private String query2 = "SELECT * FROM first WHERE POS_EQ_RA &lt; 0.5";
	private int timeoutValue = 15;		

	public void testGetFactoryUrlFromRegistry() throws Exception{
		GdsDelegate gds = new GdsDelegate();
		String factoryPort = null;
		
		// Test method with valid testRegistryUrl:
		
		// Get factory URL from registry
/*
		try {
			GdsDelegate.getFactoryUrlFromRegistry(testRegistryUrl1, timeoutValue);
			assertNotNull(gds.factoryPort);
		}
		catch (Exception e){}
*/		
		// Test method with invalid testRegistryUrl:
		
		String bogusRegistryUrl = "bogus";
		// Get factory URL from registry
/*
		try {
			GdsDelegate.getFactoryUrlFromRegistry(bogusRegistryUrl, timeoutValue);
			fail("Factory Url passed back from bogus registry");
		}
		catch (Exception e){}	

		// Test method with invalid timeoutValue:
*/		
		int bogusTimeoutValue = -1;
		// Get factory URL from registry
/*
		try {
			GdsDelegate.getFactoryUrlFromRegistry(testRegistryUrl1, bogusTimeoutValue);
			fail("Factory Url passed back with timeoutValue set to -1");
		}
		catch (Exception e){}
*/						
	}
	
	public void testConnect() throws Exception {

		GdsDelegate gds = new GdsDelegate();
				
		// Make sure that service is initially unconnected and factory
		// port, service instance, and application port are null.
		assertFalse(gds.connected);
		assertNull(gds.factoryPort);
		assertNull(gds.applicationPort);
		
		// Connect the service.
		try {
			gds.connect();
		} catch (Exception e) {
			fail("Service did not connect: " + e);
		}
				
		// Make sure that service is now connected and factory
		// port, service instance, and application port are not null.		
		assertTrue(gds.connected);
		assertNotNull(gds.factoryPort);
		assertNotNull(gds.applicationPort);
				
	}
	
	public void testPerformSelect(){
		// Test "could not build doc" exception is not thrown.
		// Test "could not execute query" exception is not thrown.
		// Test result.	
	} 
  
}
