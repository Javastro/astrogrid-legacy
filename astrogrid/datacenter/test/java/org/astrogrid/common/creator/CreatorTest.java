package org.astrogrid.common.creator;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class CreatorTest extends TestCase {

  /**
   * Constructor for FooTest.
   * @param name
   */
  public CreatorTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(CreatorTest.class);
  }

  public void testNewInstance() throws Exception {
    Creator creator = new Creator();
    CreatorTestDummy dummy = (CreatorTestDummy) creator.newInstance("org.astrogrid.common.creator.CreatorTestDummy");
    assertNotNull("no valid CreatorTestDummy class", dummy);
  }

  public void testNewInstanceParameters() throws Exception {
    Creator creator = new Creator();
    
    Object[] parameters = new Object[2];
    parameters[0] = new String("foo");
    parameters[1] = new Integer(1); 
    
    CreatorTestDummy dummy = (CreatorTestDummy) creator.newInstance("org.astrogrid.common.creator.CreatorTestDummy", parameters);
    assertNotNull("no valid CreatorTestDummy class", dummy);
    assertEquals("foo", dummy.getFoo());
    assertEquals(1, dummy.getBar());
  }
  
  public void testNoSuchConstructor() throws Exception {
    Creator creator = new Creator();
    try {
      Object[] parameters = new Object[2];
      parameters[0] = new String("foo");
      parameters[1] = new Boolean(false); 

      CreatorTestDummy dummy = (CreatorTestDummy) creator.newInstance("org.astrogrid.common.creator.CreatorTestDummy", parameters);
      assertTrue("newInstance should have failed ... no such constructor", false);
    }
    catch(CreatorException e) {
      assertTrue("newInstance failed as expected", true);
    }
  }

  protected void setUp() {
    PropertyConfigurator.configure("log4j.properties");
  }
  
  protected void tearDown() {
    BasicConfigurator.resetConfiguration();
  }
}
