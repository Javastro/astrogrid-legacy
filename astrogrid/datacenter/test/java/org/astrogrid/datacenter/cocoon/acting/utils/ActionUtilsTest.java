package org.astrogrid.datacenter.cocoon.acting.utils;

import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class ActionUtilsTest extends TestCase {
  private ActionUtils utils;

  /**
   * Constructor for ActionUtilsTest.
   * @param name
   */
  public ActionUtilsTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(ActionUtilsTest.class);
  }

  /*
   * Test for String getRequestParameter(String, String, Parameters, Request)
   */
  public void testGetRequestParameterStringStringParametersRequest() {
  }

  /*
   * Test for String getRequestParameter(String, Parameters, Request)
   */
  public void testGetRequestParameterStringParametersRequest() {
  }

  /*
   * Test for Object getNewObject(String, Parameters, Request, Object[])
   */
  public void testGetNewObjectStringParametersRequestObjectArray() {
  }

  /*
   * Test for Object getNewObject(String, String, Parameters, Request, Object[])
   */
  public void testGetNewObjectStringStringParametersRequestObjectArray() {
  }

  public void testValidate() {
    String xml =
        "<?xml version=\"1.0\"?>" +
        "<bob>" +
        "  <fred/>" +
        "</bob>";
    
    ValidationHandler handler = utils.validate(xml);
    assertTrue(handler.valid());
  }
  
  public void testValidateInvalidXML() throws Exception {
    String xml =
        "<?xml version=\"1.0\"?>" +
        "<bob>" +
        "  <fred>" +
        "</bob>";
    
    ValidationHandler handler = utils.validate(xml);
    assertFalse(handler.valid());
  }

  public void testShouldValidate() {
  }

  public void testShouldBeNSAware() {
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    
    utils = new ActionUtilsDefault();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    utils = null;
    
    super.tearDown();
  }
}
