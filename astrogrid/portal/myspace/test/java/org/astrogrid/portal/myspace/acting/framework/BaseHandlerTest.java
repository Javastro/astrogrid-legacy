package org.astrogrid.portal.myspace.acting.framework;

import mock.org.astrogrid.store.delegate.MockStoreClient;
import mock.org.astrogrid.portal.myspace.acting.framework.MockContextWrapper;

import junit.framework.TestCase;

/**
 * @author peter.shillan
 */
public class BaseHandlerTest extends TestCase {
  protected static final int BASE_RESULT_NUMBER = 2;
  
  // Handler to test.
  protected MySpaceHandler handler;
  
  // Mocks and utilities.
  protected MockContextWrapper context;
  protected MockStoreClient storeClient;
  
  /**
   * Constructor for BaseHandlerTest.
   * @param name test name
   */
  public BaseHandlerTest(String name) {
    super(name);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();

    // Create the mock (and support) objects.
    context = new MockContextWrapper();
    storeClient = new MockStoreClient();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    // Mocks etc. no longer needed.
    context = null;
    storeClient = null;

    super.tearDown();
  }

  /**
   * Verify the mocks.
   */
  protected void verify() {
    context.verify();
    storeClient.verify();
  }
}
