package org.astrogrid.portal.myspace.acting.framework;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * @author peter.shillan
 */
public class UploadHandlerTest extends BaseHandlerTest {
  /**
   * Constructor for UploadHandlerTest.
   * @param name
   */
  public UploadHandlerTest(String name) {
    super(name);
  }

  public void testExecute() throws Exception {
    final int noSuccessResults = 0;
    
    // Ensure success and correct handler class.
    context.addExpectedSetLocalAttributeValues(MySpaceHandler.PARAM_ACTION, "true");
    context.addExpectedSetLocalAttributeValues(
        MySpaceHandler.PARAM_ACTION_HANDLER,
        UploadHandler.class.getName());

    // Setup MySpace destination parameter.
    context.addExpectedGetParameterStringValues(MySpaceHandler.PARAM_DEST);
    context.setupGetParameterString("dstfile");
    
    // Setup MySpace file parameter.
    context.addExpectedGetParameterStringValues(MySpaceHandler.PARAM_FILE);
    context.setupGetParameterString("srcfile");

    // Setup input stream parameter.
    context.addExpectedGetFileInputStreamValues("srcfile");
    context.setupGetFileInputStream(new FileInputStream("test/myspace-upload.txt"));
    
    // Setup store client... should have putStream() called on it once only.
    context.setupGetStoreClient(storeClient);
    storeClient.addExpectedPutStreamValues("dstfile", false);
    storeClient.setupPutStream(new FileOutputStream("test/myspace-uploaded.txt"));

    // Execute the handler.
    handler = new UploadHandler(context);
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
