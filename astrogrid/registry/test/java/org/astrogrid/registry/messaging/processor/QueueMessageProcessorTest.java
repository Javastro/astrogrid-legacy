package org.astrogrid.registry.messaging.processor;

import com.mockobjects.servlet.MockServletContext;

import org.apache.log4j.Category;

import mock.javax.xml.rpc.server.MockServletEndpointContext;
import mock.org.astrogrid.registry.messaging.queue.MockOpenJmsSenderInterface;
import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class QueueMessageProcessorTest extends TestCase {
  // Test processor.
  private QueueMessageProcessor processor;
  
  // Context objects.
  MockServletContext servletContext;
  MockServletEndpointContext servletEndpointContext;

  // OpenJMS sender mock object.
  MockOpenJmsSenderInterface sender;

  // Logger.
  private Category logger = Category.getInstance(getClass());

  /**
   * Constructor for GetQueueMessageProcessorTest.
   * @param name
   */
  public QueueMessageProcessorTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(QueueMessageProcessorTest.class);
  }
  
  public void testInitAndDestroy() throws Exception {
    servletContext.addRealPath("config/openjms.properties");
    servletEndpointContext.setupGetServletContext(servletContext);
    
    processor.init(servletEndpointContext);
    processor.destroy();
    
    servletEndpointContext.verify();
    servletContext.verify();
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();

    processor = new QueueMessageProcessor();

    servletContext = new MockServletContext(); 
    servletEndpointContext = new MockServletEndpointContext();

    sender = new MockOpenJmsSenderInterface();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    sender = null;
    
    servletContext = null; 
    servletEndpointContext = null;

    processor = null;

    super.tearDown();
  }

}
