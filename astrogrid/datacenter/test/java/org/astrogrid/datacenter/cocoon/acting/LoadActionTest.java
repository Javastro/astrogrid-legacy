package org.astrogrid.datacenter.cocoon.acting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mock.apache.cocoon.environment.MockRequest;
import mock.astrogrid.datacenter.cocoon.action.MockActionUtils;
import mock.astrogrid.mySpace.delegate.mySpaceManager.MockMySpaceManagerDelegate;

import org.apache.avalon.framework.parameters.Parameters;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionTestHelper;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtilsFactory;
import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class LoadActionTest extends TestCase {
  private LoadAction action;

  /**
   * Constructor for LoadActionTest.
   * @param name
   */
  public LoadActionTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(LoadActionTest.class);
  }
  
  public void testAct() throws Exception {
    ActionTestHelper helper = new ActionTestHelper();
    
    // Setup stub sitemap parameters.
    String[][] sitemapKVPairs =
    {
      {
        "myspace-end-point",
        ActionTestHelper.MY_SPACE_END_POINT_01
      },
      {
        "myspace-delegate-class",
        "mock.astrogrid.mySpace.delegate.MySpaceManagerDelegate"
      },
      {
        "myspace-name",
        "name"
      }
    };
    Parameters sitemapParameters = helper.setUpSitemapParameters(sitemapKVPairs);

    // Setup mock request parameters ...
    MockRequest request = new MockRequest();
    
    // - setup expected request attributes set.
    Object[][] requestSetAttrKVPairs =
    {
      {
        "adql-document",
        ActionTestHelper.ADQL_SAMPLE_01
      },
      {
        "adql-document-loaded",
        "true"
      }
    };
    request = helper.setUpSetRequestAttributes(request, requestSetAttrKVPairs);

    // Setup mock MySpace delegate.
    MockMySpaceManagerDelegate delegate =
        new MockMySpaceManagerDelegate(ActionTestHelper.MY_SPACE_END_POINT_01);
        
    delegate.addExpectedGetDataHoldingValues("gps", "tag", ActionTestHelper.MY_SPACE_NAME_01);
    delegate.setupGetDataHolding(ActionTestHelper.ADQL_SAMPLE_01);

    // Setup object model.
    Map objectModel = new HashMap();
    helper.addRequest(request, objectModel);
    
    // Setup mock action utilities ...
    MockActionUtils utils = new MockActionUtils();
    
    // - setup utils.getNewObject().
    List expectedGetNewObjectArgs = new ArrayList();
    expectedGetNewObjectArgs.add(ActionTestHelper.MY_SPACE_END_POINT_01);
    utils.addExpectedGetNewObjectStringParametersRequestListValues(
        "myspace-delegate-class", sitemapParameters,
        request, expectedGetNewObjectArgs);
    utils.setupGetNewObjectStringParametersRequestList(delegate);
    
    // - setup utils.getRequestParameter().
    utils.addExpectedGetRequestParameterStringParametersRequestValues(
        "myspace-name", sitemapParameters, request);
    utils.setupGetRequestParameterStringParametersRequest(ActionTestHelper.MY_SPACE_NAME_01);
    
    ActionUtilsFactory.addMock(utils);
    
    // Perform the test.
    Map result = action.act(null, null, objectModel, null, sitemapParameters);
    
    // Verify the mock objects.
    request.verify();
    utils.verify();
    delegate.verify();
    
    // Validate the results.
    assertNotNull(result);
    assertEquals(1, result.size());
    assertTrue(result.containsKey("adql-document-loaded"));
    assertTrue(helper.getValueAsBoolean("adql-document-loaded", result));
  }

  public void testActException() throws Exception {
    ActionTestHelper helper = new ActionTestHelper();
    
    // Setup stub sitemap parameters.
    String[][] sitemapKVPairs =
    {
      {
        "myspace-end-point",
        ActionTestHelper.MY_SPACE_END_POINT_01
      },
      {
        "myspace-delegate-class",
        "mock.astrogrid.mySpace.delegate.MySpaceManagerDelegate"
      },
      {
        "myspace-name",
        "name"
      }
    };
    Parameters sitemapParameters = helper.setUpSitemapParameters(sitemapKVPairs);

    // Setup mock request parameters ...
    MockRequest request = new MockRequest();
    
    // - setup expected request attributes set.
    Object[][] requestSetAttrKVPairs =
    {
      {
        "adql-document-loaded",
        "false"
      }
    };
    request = helper.setUpSetRequestAttributes(request, requestSetAttrKVPairs);

    // Setup mock MySpace delegate to throw an exception.
    MockMySpaceManagerDelegate delegate =
        new MockMySpaceManagerDelegate(ActionTestHelper.MY_SPACE_END_POINT_01);
        
    delegate.setupExceptionGetDataHolding(new Exception(getClass().getName()));
    delegate.addExpectedGetDataHoldingValues("gps", "tag", ActionTestHelper.MY_SPACE_NAME_01);
    delegate.setupGetDataHolding(ActionTestHelper.ADQL_SAMPLE_01);

    // Setup object model.
    Map objectModel = new HashMap();
    helper.addRequest(request, objectModel);
    
    // Setup mock action utilities ...
    MockActionUtils utils = new MockActionUtils();
    
    // - setup utils.getNewObject().
    List expectedGetNewObjectArgs = new ArrayList();
    expectedGetNewObjectArgs.add(ActionTestHelper.MY_SPACE_END_POINT_01);
    utils.addExpectedGetNewObjectStringParametersRequestListValues(
        "myspace-delegate-class", sitemapParameters,
        request, expectedGetNewObjectArgs);
    utils.setupGetNewObjectStringParametersRequestList(delegate);
    
    // - setup utils.getRequestParameter().
    utils.addExpectedGetRequestParameterStringParametersRequestValues(
        "myspace-name", sitemapParameters, request);
    utils.setupGetRequestParameterStringParametersRequest(ActionTestHelper.MY_SPACE_NAME_01);
    
    ActionUtilsFactory.addMock(utils);
    
    // Perform the test.
    Map result = action.act(null, null, objectModel, null, sitemapParameters);
    
    // Verify the mock objects.
    request.verify();
    utils.verify();
    delegate.verify();
    
    // Validate the results.
    assertNull(result);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    
    action = new LoadAction();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    action = null;
    
    super.tearDown();
  }
}
