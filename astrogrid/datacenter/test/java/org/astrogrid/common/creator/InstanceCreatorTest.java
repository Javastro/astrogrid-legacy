package org.astrogrid.common.creator;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class InstanceCreatorTest extends TestCase {

  /**
   * Constructor for FooTest.
   * @param name
   */
  public InstanceCreatorTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(InstanceCreatorTest.class);
  }

  public void testGetInstance() throws Exception {
    InstanceCreator creator = new InstanceCreator();
    InstanceCreatorTestDummy dummy = (InstanceCreatorTestDummy) creator.getInstance("org.astrogrid.common.creator.InstanceCreatorTestDummy");
    assertNotNull("no valid InstanceCreatorTestDummy class", dummy);
  }

  public void testGetInstanceParameters() throws Exception {
    InstanceCreator creator = new InstanceCreator();
    
    Object[] parameters = new Object[2];
    parameters[0] = new String("foo");
    parameters[1] = new Integer(1); 
    
    InstanceCreatorTestDummy dummy = (InstanceCreatorTestDummy) creator.getInstance("org.astrogrid.common.creator.InstanceCreatorTestDummy", parameters);
    assertNotNull("no valid InstanceCreatorTestDummy class", dummy);
    assertEquals("foo", dummy.getFoo());
    assertEquals(1, dummy.getBar());
  }

  public void testNoSuchMethod() throws Exception {
    InstanceCreator creator = new InstanceCreator();
    try {
      InstanceCreatorTestDummy dummy = (InstanceCreatorTestDummy) creator.getInstance("org.astrogrid.common.creator.InstanceCreatorTestDummy", "noMethod", null);
      assertTrue("getInstance should have failed ... no such method", false);
    }
    catch(InstanceCreatorException e) {
      assertTrue("getInstance failed as expected", true);
    }
  }

  protected void setUp() {
    PropertyConfigurator.configure("log4j.properties");
  }
  
  protected void tearDown() {
    BasicConfigurator.resetConfiguration();
  }
}
