package org.astrogrid.datacenter.cocoon.acting.utils;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.ObjectModelHelper;

import mock.apache.cocoon.environment.MockRequest;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class ActionTestHelper {
  // Constants.
  public static final String ADQL_SAMPLE_01 =
      "<Select xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
      "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
      "  <Selection>\n" +
      "    <AllSelectionItem />\n" +
      "  </Selection>\n" +
      "  <TableClause>\n" +
      "    <FromClause>\n" +
      "      <TableReference>\n" +
      "        <Table>\n" +
      "          <Name>Tab</Name>\n" +
      "          <AliasName>t</AliasName>\n" +
      "        </Table>\n" +
      "      </TableReference>\n" +
      "    </FromClause>\n" +
      "  </TableClause>\n" +
      "</Select>";

  public static final String MY_SPACE_NAME_01 = "myspace-name-01";
  public static final String MY_SPACE_END_POINT_01 = "http://localhost:8080/myspace";
  
  public MockRequest setUpGetRequestParameters(MockRequest request, String[][] kvPairs) {
    for(int kvIndex = 0; kvIndex < kvPairs.length; kvIndex++) {
      request.addExpectedGetParameterValues(kvPairs[kvIndex][0]);
      request.setupGetParameter(kvPairs[kvIndex][1]);
    }
    
    return request;
  }
  
  public MockRequest setUpGetRequestAttributes(MockRequest request, Object[][] kvPairs) {
    for(int kvIndex = 0; kvIndex < kvPairs.length; kvIndex++) {
      request.addExpectedGetAttributeValues((String) kvPairs[kvIndex][0]);
      request.setupGetAttribute(kvPairs[kvIndex][1]);
    }
    
    return request;
  }
  
  public MockRequest setUpSetRequestAttributes(MockRequest request, Object[][] kvPairs) {
    for(int kvIndex = 0; kvIndex < kvPairs.length; kvIndex++) {
      request.addExpectedSetAttributeValues((String) kvPairs[kvIndex][0], kvPairs[kvIndex][1]);
    }
    
    return request;
  }
  
  public Parameters setUpSitemapParameters(String[][] kvPairs) {
    Parameters params = new Parameters();
    
    for(int kvIndex = 0; kvIndex < kvPairs.length; kvIndex++) {
      params.setParameter(kvPairs[kvIndex][0], kvPairs[kvIndex][1]);
    }
    
    return params;
  }
  
  public Map addRequest(MockRequest request, Map objectModel) {
    objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
    
    return objectModel;
  }
  
  public boolean getValueAsBoolean(Object key, Map map) {
    return Boolean.valueOf((String) map.get(key)).booleanValue();
  }
}
