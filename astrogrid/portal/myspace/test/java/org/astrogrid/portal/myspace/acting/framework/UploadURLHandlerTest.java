package org.astrogrid.portal.myspace.acting.framework;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * @author peter.shillan
 */
public class UploadURLHandlerTest extends BaseHandlerTest {
  /**
   * Constructor for UploadURLHandlerTest.
   * @param name
   */
  public UploadURLHandlerTest(String name) {
    super(name);
  }

  public void testExecute() throws Exception {
    final int noSuccessResults = 0;
    
    // Ensure success and correct handler class.
    context.addExpectedSetLocalAttributeValues(MySpaceHandler.PARAM_ACTION, "true");
    context.addExpectedSetLocalAttributeValues(
        MySpaceHandler.PARAM_ACTION_HANDLER,
        UploadURLHandler.class.getName());

    // Setup MySpace destination parameter.
    context.addExpectedGetParameterStringValues(MySpaceHandler.PARAM_DEST);
    context.setupGetParameterString("dstfile");
    
    // Setup MySpace URL parameter.
    context.addExpectedGetParameterStringValues(MySpaceHandler.PARAM_URL);
    context.setupGetParameterString("file://test/myspace-upload.txt");

    // Setup store client... should have putUrl() called on it once only.
    context.setupGetStoreClient(storeClient);
    storeClient.setExpectedPutUrlCalls(1);

    // Execute the handler.
    handler = new UploadURLHandler(context);
    Map results = handler.execute();
    
    // Results should contain all success values + new end point.
    assertNotNull(results);
    assertEquals(
        BaseHandlerTest.BASE_RESULT_NUMBER + noSuccessResults, results.size());
    
    // Verify mocks.
    verify();
  }
  
  /*
   * @see BaseHandlerTest#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /*
   * @see BaseHandlerTest#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
  }
}
