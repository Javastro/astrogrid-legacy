package org.astrogrid.registry.messaging.processor;

import com.mockobjects.servlet.MockServletContext;

import org.apache.log4j.Category;

import mock.javax.xml.rpc.server.MockServletEndpointContext;
import mock.org.astrogrid.registry.messaging.queue.MockOpenJmsReceiverInterface;
import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class GetQueueMessageProcessorTest extends TestCase {
  // Test processor.
  private GetQueueMessageProcessor processor;
  
  // Context objects.
  MockServletContext servletContext;
  MockServletEndpointContext servletEndpointContext;

  // OpenJMS receiver mock object.
  MockOpenJmsReceiverInterface receiver;

  // Logger.
  private Category logger = Category.getInstance(getClass());

  /**
   * Constructor for GetQueueMessageProcessorTest.
   * @param name
   */
  public GetQueueMessageProcessorTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(GetQueueMessageProcessorTest.class);
  }
  
  public void testInitAndDestroy() throws Exception {
    servletContext.addRealPath("config/openjms.properties");
    servletContext.addRealPath("schema/astrogrid-queue-messages.xml");
    servletEndpointContext.setupGetServletContext(servletContext);
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

    processor = new GetQueueMessageProcessor();

    servletContext = new MockServletContext(); 
    servletEndpointContext = new MockServletEndpointContext();

    receiver = new MockOpenJmsReceiverInterface();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    receiver = null;
    
    servletContext = null; 
    servletEndpointContext = null;

    processor = null;

    super.tearDown();
  }

}
