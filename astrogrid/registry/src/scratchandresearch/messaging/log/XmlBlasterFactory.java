package org.astrogrid.registry.messaging.log;

import org.apache.log4j.Category;

import mock.org.astrogrid.registry.messaging.log.MockXmlBlaster;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class XmlBlasterFactory {
  // Mock XMLBlaster.
  private static MockXmlBlaster MOCK_XML_BLASTER = null;
  
  // Logger.
  private static Category LOGGER = Category.getInstance(XmlBlasterFactory.class);

  public static synchronized XmlBlaster getXmlBlaster() {
    XmlBlaster result = null;
    
    String testProperty = System.getProperty("TEST", "false");
    boolean testXmlBlaster = Boolean.valueOf(testProperty).booleanValue();
    
    if(testXmlBlaster) {
      result = MOCK_XML_BLASTER;
      LOGGER.info("[getXmlBlaster] mock XMLBlaster");
    }
    else {
      result = new XmlBlasterUtil();
      LOGGER.info("[getXmlBlaster] real XMLBlaster");
    }
    
    return result;
  }
  
  public static synchronized void setMockXmlBlaster(MockXmlBlaster mockXmlBlaster) {
    XmlBlasterFactory.MOCK_XML_BLASTER = mockXmlBlaster;
  }
  
  // Never instantiated.
  private XmlBlasterFactory() {
  }
}
