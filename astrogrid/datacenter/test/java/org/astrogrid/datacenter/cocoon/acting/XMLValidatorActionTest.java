package org.astrogrid.datacenter.cocoon.acting;

import java.util.HashMap;
import java.util.Map;

import mock.apache.cocoon.environment.MockRequest;
import mock.astrogrid.datacenter.cocoon.action.MockActionUtils;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.log4j.BasicConfigurator;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionTestHelper;
import org.astrogrid.datacenter.cocoon.acting.utils.ActionUtilsFactory;
import org.astrogrid.datacenter.cocoon.acting.utils.ValidationHandler;

import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class XMLValidatorActionTest extends TestCase {
  private XMLValidatorAction action;

  /**
   * Constructor for XMLValidatorActionTest.
   * @param name
   */
  public XMLValidatorActionTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(XMLValidatorActionTest.class);
  }
  
  public void testActValid() throws Exception {
    ActionTestHelper helper = new ActionTestHelper();
    
    // Setup stub sitemap parameters.
    String[][] sitemapKVPairs =
    {
      {
        "xml-param",
        "query"
      }
    };
    Parameters sitemapParameters = helper.setUpSitemapParameters(sitemapKVPairs);
    
    // Setup mock request parameters ...
    MockRequest request = new MockRequest();
    
    // - setup expected request attributes set.
    Object[][] requestSetAttrKVPairs =
    {
      {
        "xml-valid",
        "true"
      }
    };
    request = helper.setUpSetRequestAttributes(request, requestSetAttrKVPairs);

    // Setup object model.
    Map objectModel = new HashMap();
    helper.addRequest(request, objectModel);
    
    // Setup mock action utilities ...
    MockActionUtils utils = new MockActionUtils();
    
    // - setup utils.getRequestParameter().
    utils.addExpectedGetRequestParameterStringParametersRequestValues(
        "xml-param", sitemapParameters, request);
    utils.setupGetRequestParameterStringParametersRequest(ActionTestHelper.ADQL_SAMPLE_01);
    
    // - setup utils.validate().
    ValidationHandler validationHandler = new ValidationHandler();
    utils.addExpectedValidateValues(ActionTestHelper.ADQL_SAMPLE_01);
    utils.setupValidate(validationHandler);

    ActionUtilsFactory.addMock(utils);
    
    // Perform the test.
    Map result = action.act(null, null, objectModel, null, sitemapParameters);
    
    // Verify the mock objects.
    request.verify();
    utils.verify();
    
    // Verify the results.
    assertNotNull(result);
    assertEquals(1, result.size());
    assertTrue(result.containsKey("xml-valid"));
    assertTrue(helper.getValueAsBoolean("xml-valid", result)); 
  }

  public void testActInvalid() throws Exception {
    ActionTestHelper helper = new ActionTestHelper();
    
    // Setup stub sitemap parameters.
    String[][] sitemapKVPairs =
    {
      {
        "xml-param",
        "query"
      }
    };
    Parameters sitemapParameters = helper.setUpSitemapParameters(sitemapKVPairs);
    
    // Setup mock request parameters ...
    MockRequest request = new MockRequest();
    
    // - setup expected request attributes set.
    ValidationHandler validationHandler = new ValidationHandler();
    validationHandler.exception(new Exception(getClass().getName()));
    Object[][] requestSetAttrKVPairs =
    {
      {
          "xml-validation-errors",
          validationHandler.getErrorMessages()
      },
      {
          "xml-validation-warnings",
          validationHandler.getWarningMessages()
      },
      {
          "xml-validation-fatal-errors",
          validationHandler.getFatalErrorsMessages()
      },
      {
          "xml-valid",
          "false"
      },
      {
          "xml-errors",
          Boolean.toString(validationHandler.anyErrors())
      },
      {
          "xml-warnings",
          Boolean.toString(validationHandler.anyWarnings())
      },
      {
          "xml-fatal-errors",
          Boolean.toString(validationHandler.anyFatalErrors())
      }
    };
    request = helper.setUpSetRequestAttributes(request, requestSetAttrKVPairs);

    // Setup object model.
    Map objectModel = new HashMap();
    helper.addRequest(request, objectModel);
    
    // Setup mock action utilities ...
    MockActionUtils utils = new MockActionUtils();
    
    // - setup utils.getRequestParameter().
    utils.addExpectedGetRequestParameterStringParametersRequestValues(
        "xml-param", sitemapParameters, request);
    utils.setupGetRequestParameterStringParametersRequest(ActionTestHelper.ADQL_SAMPLE_01);
    
    // - setup utils.validate().
    utils.addExpectedValidateValues(ActionTestHelper.ADQL_SAMPLE_01);
    utils.setupValidate(validationHandler);

    ActionUtilsFactory.addMock(utils);
    
    // Perform the test.
    Map result = action.act(null, null, objectModel, null, sitemapParameters);
    
    // Verify the mock objects.
    request.verify();
    utils.verify();
    
    // Verify the results.
    assertNull(result);
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    
    BasicConfigurator.configure();
    
    action = new XMLValidatorAction();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    action = null;

    BasicConfigurator.resetConfiguration();
    
    super.tearDown();
  }

}
