package org.astrogrid.warehouse.ogsadai;

import junit.framework.TestCase;
import org.gridforum.ogsi.ExtensibilityType;

/**
 * Exercise connect, findFactoryUrlFromRegistry, and performSelect methods.
 * Live tests are performed against a grid service.
 */
public class GdsDelegateTest extends TestCase {

  public GdsDelegateTest() {}
		
  private String factoryGsh1  
      = "http://astrogrid.ast.cam.ac.uk:8080/ogsa/services/"
      + "astrogrid/echo/EchoFactory";
  private String factoryGsh2
      = "http://hydra.star.le.ac.uk:8082/gdw/services/"
      + "ogsadai/GridDataServiceFactory";

  private ExtensibilityType document = null;
  private ExtensibilityType result = null;	
	
  private String testRegistryUrl1
    = "http://astrogrid.ast.cam.ac.uk:4040/gdw/services/ogsadai/DAIServiceGroupRegistry";
  private String testRegistryUrl2
    = "http://hydra.star.le.ac.uk:8082/gdw/services/ogsadai/DAIServiceGroupRegistry";	

  private String query1 = "SELECT * FROM first WHERE POS_EQ_DEC &gt; 59.9";
  private String query2 = "SELECT * FROM first WHERE POS_EQ_RA &lt; 0.5";

  private int timeoutValue = 15;		


  /**
   * Test method to acquire a grid service handle from the registry
   * and set the handle to the gds instance.
   */
  public void testSetFactoryGshFromRegistry() throws Exception {
    GdsDelegate gds = new GdsDelegate();
    String factoryPort = null;
    // Get factory handle from registry
		
    // Test method with valid testRegistryUrl:
    try {
      gds.setFactoryGshFromRegistry(testRegistryUrl1, timeoutValue);
      assertNotNull(gds.factoryPort);
    }
    catch (Exception e){}
		
    // Test method with invalid testRegistryUrl:
    String bogusRegistryUrl = "bogus";
    try {
      gds.setFactoryGshFromRegistry(bogusRegistryUrl, timeoutValue);
      fail("Factory handle passed back from bogus registry");
    }
    catch (Exception e){}	

    // Test method with invalid timeoutValue:
    int bogusTimeoutValue = -1;
    try {
      gds.setFactoryGshFromRegistry(testRegistryUrl1, bogusTimeoutValue);
      fail("Factory handle passed back with timeoutValue set to -1");
    }
    catch (Exception e){}
						
  }


  /**
   * Test service connection.
   */	
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
   * factory handles.
   */
  public void testFactoryHandle() throws Exception {
    GdsDelegate gds2 = new GdsDelegate();
    try {
      gds2.setFactoryGsh(this.factoryGsh2);
      assertEquals(this.factoryGsh2, gds2.getFactoryGsh());
    }
    catch(Exception e){}
  }
	
  /**
   * Test invocation of an SQL select statement on the GDS instance. 
   */
  public void testPerformSelect() throws Exception{
		
    // Make sure that query and result documents are null 
    // before method is called.
    try {
      GdsDelegate gds3 = new GdsDelegate();
      assertNull(this.document);
      assertNull(this.result);
      result = gds3.performSelect(query1);
		  
      // Make sure that query and result documents are now not null.
      assertNotNull(this.document);
      assertNotNull(this.result);
    }
    catch (Exception e) {}
		
    // Further tests|: write a query that returns a single sky survey 
    // object from the GDW so that an "assertEquals" method could be 
    // applied practically (current queries return too many sky objects).
  } 
  
}
