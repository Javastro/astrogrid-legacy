package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

/**
 * @author peter.shillan
 */
public class ChangeHandlerTest extends BaseHandlerTest {
  /**
   * Constructor for ChangeHandlerTest.
   * @param name test name
   */
  public ChangeHandlerTest(String name) {
    super(name);
  }

  /**
   * Test that a valid user with no specified end point results in the default
   * end point being written into the session.
   *  
   * @throws Exception
   */
  public void testExecuteDefaultEndPoint() throws Exception {
    final int noSuccessResults = 1;
    
    // Ensure success and correct handler class.
    context.addExpectedSetLocalAttributeValues(MySpaceHandler.PARAM_ACTION, "true");
    context.addExpectedSetLocalAttributeValues(
        MySpaceHandler.PARAM_ACTION_HANDLER,
        ChangeHandler.class.getName());

    // Ensure that the end point is set globally.
    context.addExpectedSetGlobalAttributeValues(
        ContextWrapper.PARAM_END_POINT,
        ContextWrapper.DEFAULT_END_POINT);

    // Setup end point parameter.
    context.setupGetEndPoint(ContextWrapper.DEFAULT_END_POINT);
    
    // Execute the handler.
    handler = new ChangeHandler(context);
    Map results = handler.execute();
    
    // Results should contain all success values + new end point.
    assertNotNull(results);
    assertEquals(
        BaseHandlerTest.BASE_RESULT_NUMBER + noSuccessResults, results.size());
    assertTrue(results.containsKey(ContextWrapper.PARAM_END_POINT));
    assertEquals(
        ContextWrapper.DEFAULT_END_POINT, results.get(ContextWrapper.PARAM_END_POINT));
    
    // Verify mocks.
    verify();
  }
  
  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
  }
}
