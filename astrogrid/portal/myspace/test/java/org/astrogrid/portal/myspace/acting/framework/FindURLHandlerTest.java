package org.astrogrid.portal.myspace.acting.framework;

import java.net.URL;
import java.util.Map;

/**
 * @author peter.shillan
 */
public class FindURLHandlerTest extends BaseHandlerTest {
  private static final String MYSPACE_FILE = "file://astrogrid-myspace-file";
  
  /**
   * Constructor for FindURLHandlerTest.
   * @param name test name
   */
  public FindURLHandlerTest(String name) {
    super(name);
  }

  public void testExecute() throws Exception {
    final int noSuccessResults = 1;
    
    // Ensure success and correct handler class.
    context.addExpectedSetLocalAttributeValues(
        MySpaceHandler.PARAM_URL,
        FindURLHandlerTest.MYSPACE_FILE);
    context.addExpectedSetLocalAttributeValues(MySpaceHandler.PARAM_ACTION, "true");
    context.addExpectedSetLocalAttributeValues(
        MySpaceHandler.PARAM_ACTION_HANDLER,
        FindURLHandler.class.getName());

    // Setup MySpace URL parameter.
    context.addExpectedGetParameterStringValues(MySpaceHandler.PARAM_SRC);
    context.setupGetParameterString("srcfile");

    // Setup store client... should have getUrl() called on it once only.
    final URL url = new URL(FindURLHandlerTest.MYSPACE_FILE);
    context.setupGetStoreClient(storeClient);
    storeClient.addExpectedGetUrlValues("srcfile");
    storeClient.setupGetUrl(url);
    
    // Should have the local result
    
    // Execute the handler.
    handler = new FindURLHandler(context);
    Map results = handler.execute();
    
    // Results should contain all success values + new end point.
    assertNotNull(results);
    assertEquals(
        BaseHandlerTest.BASE_RESULT_NUMBER + noSuccessResults, results.size());
    assertTrue(results.containsKey(MySpaceHandler.PARAM_URL));
    assertEquals(
        url.toString(), results.get(MySpaceHandler.PARAM_URL));
    
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
