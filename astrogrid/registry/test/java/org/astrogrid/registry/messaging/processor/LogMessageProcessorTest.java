package org.astrogrid.registry.messaging.processor;

import com.mockobjects.servlet.MockServletContext;

import org.apache.log4j.Category;
import org.astrogrid.registry.messaging.log.XmlBlasterFactory;
import org.w3c.dom.Element;

import mock.javax.xml.rpc.server.MockServletEndpointContext;
import mock.org.astrogrid.registry.messaging.log.MockXmlBlaster;

import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 * 
 * TODO validate returned xml
 */
public class LogMessageProcessorTest extends TestCase {
  // Test processor.
  private LogMessageProcessor processor;
  
  // Context objects.
  MockServletContext servletContext;
  MockServletEndpointContext servletEndpointContext;

  // XMLBlaster mock object.
  MockXmlBlaster xmlBlaster;

  // Logger.
  private Category logger = Category.getInstance(getClass());

  /**
   * Constructor for LogMessageProcessorTest.
   * @param name
   */
  public LogMessageProcessorTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(LogMessageProcessorTest.class);
  }
  
  public void testInitAndDestroy() throws Exception {
    servletContext.addRealPath("xslt/astrogrid-xmlblaster.xsl");
    servletEndpointContext.setupGetServletContext(servletContext);
    
    processor.init(servletEndpointContext);
    processor.destroy();
    
    servletEndpointContext.verify();
    servletContext.verify();
  }

  public void testBadStyleSheet() throws Exception {
    servletContext.addRealPath("xslt/astrogrid-xmlblaster-bad.xsl");
    servletEndpointContext.setupGetServletContext(servletContext);
    
    try {
      processor.init(servletEndpointContext);

      assertTrue("[testBadStyleSheet] logging should fail with bad stylesheet", false);
    }
    catch(Exception e) {
      assertTrue("[testBadStyleSheet] logging should fail with bad stylesheet", true);
    }
    finally {
      processor.destroy();
    }
    
    servletEndpointContext.verify();
    servletContext.verify();
  }

  public void testProcess() throws Exception {
    servletContext.addRealPath("xslt/astrogrid-xmlblaster.xsl");
    servletEndpointContext.setupGetServletContext(servletContext);
    
    xmlBlaster.setupConnect(null);
    xmlBlaster.setupDisconnect(true);
    xmlBlaster.setupPublishStringStringString(null);

    XmlBlasterFactory.setMockXmlBlaster(xmlBlaster);

    try {
      processor.init(servletEndpointContext);
      
      Element element = XmlBlasterTestUtil.createXmlElement("schema/log-example-scratch.xml");
      processor.process(element, servletEndpointContext);
      
      assertTrue("[testProcess] process should be successful with valid file", true);
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

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    
    processor = new LogMessageProcessor();

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