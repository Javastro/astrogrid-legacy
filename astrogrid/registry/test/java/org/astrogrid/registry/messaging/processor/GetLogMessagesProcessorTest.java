package org.astrogrid.registry.messaging.processor;

import com.mockobjects.servlet.MockServletContext;

import org.apache.log4j.Category;
import org.astrogrid.registry.messaging.log.XmlBlasterFactory;
import org.w3c.dom.Element;
import org.xmlBlaster.util.MsgUnit;

import mock.javax.xml.rpc.server.MockServletEndpointContext;
import mock.org.astrogrid.registry.messaging.log.MockXmlBlaster;
import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 * 
 * TODO validate returned xml
 */
public class GetLogMessagesProcessorTest extends TestCase {
  // Test processor.
  private GetLogMessagesProcessor processor;
  
  // Context objects.
  MockServletContext servletContext;
  MockServletEndpointContext servletEndpointContext;

  // XMLBlaster mock object.
  MockXmlBlaster xmlBlaster;

  // Logger.
  private Category logger = Category.getInstance(getClass());
  
  /**
   * Constructor for GetLogMessagesProcessorTest.
   * @param name
   */
  public GetLogMessagesProcessorTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(GetLogMessagesProcessorTest.class);
  }

  public void testInitAndDestroy() throws Exception {
    servletContext.addRealPath("schema/astrogrid-log-messages.xml");
    servletEndpointContext.setupGetServletContext(servletContext);
    
    processor.init(servletEndpointContext);
    processor.destroy();
    
    servletEndpointContext.verify();
    servletContext.verify();
  }

  public void testProcess() throws Exception {
    servletContext.addRealPath("schema/astrogrid-log-messages.xml");
    servletEndpointContext.setupGetServletContext(servletContext);
    
    MsgUnit[] msgUnits = {
      XmlBlasterTestUtil.createMsgUnit("<name>bob</name>", "schema/log-message.xml")
    };
    
    xmlBlaster.setupConnect(null);
    xmlBlaster.setupDisconnect(true);
    xmlBlaster.setupGetMessages(msgUnits);

    XmlBlasterFactory.setMockXmlBlaster(xmlBlaster);

    try {
      Element element = XmlBlasterTestUtil.createXmlElement("schema/query-log-example.xml");
      processor.init(servletEndpointContext);
      Element result = processor.process(element, servletEndpointContext);
    }
    catch(Exception e) {
      throw e;
    }
    finally {
      processor.destroy();
    }
    
    servletEndpointContext.verify();
    servletContext.verify();
  }
  
  public void testProcessNoQuery() throws Exception {
    servletContext.addRealPath("schema/astrogrid-log-messages.xml");
    servletEndpointContext.setupGetServletContext(servletContext);
    
    MsgUnit[] msgUnits = {
      XmlBlasterTestUtil.createMsgUnit("<name>bob</name>", "schema/log-message.xml")
    };
    
    xmlBlaster.setupConnect(null);
    xmlBlaster.setupDisconnect(true);
    xmlBlaster.setupGetMessages(msgUnits);

    XmlBlasterFactory.setMockXmlBlaster(xmlBlaster);

    try {
      Element element = XmlBlasterTestUtil.createXmlElement("schema/query-log-no-query.xml");
      processor.init(servletEndpointContext);
      Element result = processor.process(element, servletEndpointContext);
      
      assertTrue("[testProcessNoQuery] message without XPath Query should fail", false);
    }
    catch(ProcessorException e) {
      assertTrue("[testProcessNoQuery] message without XPath Query should fail", true);
    }
    finally {
      processor.destroy();
    }
    
    servletEndpointContext.verify();
    servletContext.verify();
  }

  public void testProcessInvalidMsgUnits() throws Exception {
    servletContext.addRealPath("schema/astrogrid-log-messages.xml");
    servletEndpointContext.setupGetServletContext(servletContext);
    
    MsgUnit[] msgUnits = {
      null,
      null
    };
    
    xmlBlaster.setupConnect(null);
    xmlBlaster.setupDisconnect(true);
    xmlBlaster.setupGetMessages(msgUnits);

    XmlBlasterFactory.setMockXmlBlaster(xmlBlaster);

    try {
      Element element = XmlBlasterTestUtil.createXmlElement("schema/query-log-example.xml");
      processor.init(servletEndpointContext);
      Element result = processor.process(element, servletEndpointContext);
      
      assertTrue("[testProcessInvalidMsgUnits] message with null MsgUnits should fail", false);
    }
    catch(Exception e) {
      assertTrue("[testProcessInvalidMsgUnits] message with null MsgUnits should fail", true);
    }
    finally {
      processor.destroy();
    }
    
    servletEndpointContext.verify();
    servletContext.verify();
  }
  
  public void testBadLogMessagesBase() throws Exception {
    servletContext.addRealPath("schema/astrogrid-log-messages-bad.xml");
    servletEndpointContext.setupGetServletContext(servletContext);
    
    XmlBlasterFactory.setMockXmlBlaster(xmlBlaster);
    MsgUnit[] msgUnits = {
      XmlBlasterTestUtil.createMsgUnit("<name>bob</name>", "schema/log-message.xml")
    };
    
    xmlBlaster.setupConnect(null);
    xmlBlaster.setupDisconnect(true);
    xmlBlaster.setupGetMessages(msgUnits);

    XmlBlasterFactory.setMockXmlBlaster(xmlBlaster);

    try {
      Element element = XmlBlasterTestUtil.createXmlElement("schema/query-log-example.xml");
      processor.init(servletEndpointContext);
      Element result = processor.process(element, servletEndpointContext);
      
      assertTrue("[testBadLogMessagesBase] no base document for log messages should cause failure", false);
    }
    catch(Exception e) {
      assertTrue("[testBadLogMessagesBase] no base document for log messages should cause failure", true);
    }
    finally {
      processor.destroy();
    }
    
    servletEndpointContext.verify();
    servletContext.verify();
  }
  
  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    
    processor = new GetLogMessagesProcessor();

    servletContext = new MockServletContext(); 
    servletEndpointContext = new MockServletEndpointContext();

    xmlBlaster = new MockXmlBlaster();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    xmlBlaster = null;
    
    servletContext = null; 
    servletEndpointContext = null;

    processor = null;
    
    super.tearDown();
  }
}