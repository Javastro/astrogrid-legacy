package org.astrogrid.datacenter.cocoon.acting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mock.apache.cocoon.environment.MockRequest;
import mock.astrogrid.datacenter.cocoon.action.MockActionUtils;
import mock.astrogrid.datacenter.delegate.MockDatacenterDelegate;
import mock.w3c.dom.MockElement;

import org.apache.avalon.framework.parameters.Parameters;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionTestHelper;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtilsFactory;

import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class TestActionTest extends TestCase {
  private TestAction action;

  /**
   * Constructor for TestActionTest.
   * @param name
   */
  public TestActionTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(TestActionTest.class);
  }

  public void testAct() throws Exception {
    ActionTestHelper helper = new ActionTestHelper();
    
    // Setup stub sitemap parameters.
    String[][] sitemapKVPairs =
    {
      {
        "adql-query",
        "query"
      },
      {
        "datacenter-delegate-class",
        "mock.astrogrid.datacenter.delegate.MockDatacenterDelegate"
      }
    };
    Parameters sitemapParameters = helper.setUpSitemapParameters(sitemapKVPairs);
    
    // Setup mock Datacenter delegate.
    MockDatacenterDelegate delegate = new MockDatacenterDelegate();
    MockElement queryIn = new MockElement();
    MockElement queryOut = new MockElement();
    delegate.addExpectedQueryValues(queryIn);
    delegate.setupQuery(queryOut);
    
    // Setup mock request parameters ...
    MockRequest request = new MockRequest();

    // - setup expected request attributes set.
    Object[][] requestSetAttrKVPairs =
    {
      {
        "adql-query-result",
        queryOut
      },
      {
        "adql-query-errors",
        "false"
      }
    };
    request = helper.setUpSetRequestAttributes(request, requestSetAttrKVPairs);

    // Setup object model.
    Map objectModel = new HashMap();
    helper.addRequest(request, objectModel);
    
    // Setup mock action utils.
    MockActionUtils utils = new MockActionUtils();
    
    // - setup utils.getDOMElement().
    utils.addExpectedGetDomElementValues(ActionTestHelper.ADQL_SAMPLE_01);
    utils.setupGetDomElement(queryIn);
    
    // - setup utils.getNewObject().
    utils.addExpectedGetNewObjectStringParametersRequestListValues(
        "datacenter-delegate-class", sitemapParameters,
        request, null);
    utils.setupGetNewObjectStringParametersRequestList(delegate);
    
    // - setup utils.getRequestParameter().
    utils.addExpectedGetRequestParameterStringParametersRequestValues(
        "adql-query", sitemapParameters, request);
    utils.setupGetRequestParameterStringParametersRequest(ActionTestHelper.ADQL_SAMPLE_01);

    ActionUtilsFactory.addMock(utils);
    
    // Perform test.
    Map result = action.act(null, null, objectModel, null, sitemapParameters);
    
    // Verify mock objects.
    request.verify();
    utils.verify();
    delegate.verify();
    queryIn.verify();
    queryOut.verify();
    
    // Verify test results.
    assertNotNull(result);
    assertEquals(1, result.size());
    assertTrue(result.containsKey("adql-query-errors"));
    assertFalse(helper.getValueAsBoolean("adql-query-errors", result));
  }

  public void testActException() throws Exception {
    ActionTestHelper helper = new ActionTestHelper();
    
    // Setup stub sitemap parameters.
    String[][] sitemapKVPairs =
    {
      {
        "adql-query",
        "query"
      },
      {
        "datacenter-delegate-class",
        "mock.astrogrid.datacenter.delegate.MockDatacenterDelegate"
      }
    };
    Parameters sitemapParameters = helper.setUpSitemapParameters(sitemapKVPairs);
    
    // Setup mock Datacenter delegate to throw exception.
    MockDatacenterDelegate delegate = new MockDatacenterDelegate();
    MockElement queryIn = new MockElement();
    MockElement queryOut = new MockElement();
    
    delegate.setupExceptionQuery(new IOException(getClass().getName()));
    delegate.addExpectedQueryValues(queryIn);
    delegate.setupQuery(queryOut);
    
    // Setup mock request parameters ...
    MockRequest request = new MockRequest();

    // - setup expected request attributes set.
    Object[][] requestSetAttrKVPairs =
    {
      {
        "adql-query-errors",
        "true"
      },
      {
        "adql-query-error-message",
        getClass().getName()
      }
    };
    request = helper.setUpSetRequestAttributes(request, requestSetAttrKVPairs);

    // Setup object model.
    Map objectModel = new HashMap();
    helper.addRequest(request, objectModel);
    
    // Setup mock action utils.
    MockActionUtils utils = new MockActionUtils();
    
    // - setup utils.getDOMElement().
    utils.addExpectedGetDomElementValues(ActionTestHelper.ADQL_SAMPLE_01);
    utils.setupGetDomElement(queryIn);
    
    // - setup utils.getNewObject().
    utils.addExpectedGetNewObjectStringParametersRequestListValues(
        "datacenter-delegate-class", sitemapParameters,
        request, null);
    utils.setupGetNewObjectStringParametersRequestList(delegate);
    
    // - setup utils.getRequestParameter().
    utils.addExpectedGetRequestParameterStringParametersRequestValues(
        "adql-query", sitemapParameters, request);
    utils.setupGetRequestParameterStringParametersRequest(ActionTestHelper.ADQL_SAMPLE_01);

    ActionUtilsFactory.addMock(utils);
    
    // Perform test.
    Map result = action.act(null, null, objectModel, null, sitemapParameters);
    
    // Verify mock objects.
    request.verify();
    utils.verify();
    delegate.verify();
    queryIn.verify();
    queryOut.verify();
    
    // Verify test results.
    assertNull(result);
  }

 /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    
    action = new TestAction();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    action = null;
    
    super.tearDown();
  }

}
