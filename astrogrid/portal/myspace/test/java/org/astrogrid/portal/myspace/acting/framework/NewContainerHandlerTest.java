package org.astrogrid.portal.myspace.acting.framework;

import java.util.Map;

/**
 * @author peter.shillan
 */
public class NewContainerHandlerTest extends BaseHandlerTest {
  /**
   * Constructor for NewContainerHandlerTest.
   * @param name
   */
  public NewContainerHandlerTest(String name) {
    super(name);
  }

  public void testExecute() throws Exception {
    final int noSuccessResults = 0;
    
    // Ensure success and correct handler class.
    context.addExpectedSetLocalAttributeValues(MySpaceHandler.PARAM_ACTION, "true");
    context.addExpectedSetLocalAttributeValues(
        MySpaceHandler.PARAM_ACTION_HANDLER,
        NewContainerHandler.class.getName());

    // Setup MySpace destination parameter.
    context.addExpectedGetParameterStringValues(MySpaceHandler.PARAM_DEST);
    context.setupGetParameterString("dstfile");
    
    // Setup store client... should have newFolder() called on it once only.
    context.setupGetStoreClient(storeClient);
    storeClient.setExpectedNewFolderCalls(1);

    // Execute the handler.
    handler = new NewContainerHandler(context);
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
