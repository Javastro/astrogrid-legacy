/*
 * Created on 29-Apr-2003
 */
package org.astrogrid.registry.astromessagelog;

import javax.jms.Message;
import javax.jms.TextMessage;

import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class OpenJmsTest extends TestCase {
  // Constants.
  private static final String MESSAGE_TEXT =
    "<sample name='bob'>" +
    "  <element-01>" +
    "    <element-02 attr='fred'>" +
    "      Hello World!" +
    "    </element-02>" +
    "  <element-03/>" +
    "  <element-04>Hello, my friend, hello ...</element-04>" +
    "  </element-01>" +
    "</sample>";

  // JMS wrapper.  
  private OpenJmsUtil jmsWrapper;
  
  public OpenJmsTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(OpenJmsTest.class);
  }
  
  public void testConnectDisconnect() throws Exception {
    jmsWrapper.connect();
    jmsWrapper.disconnect();
  }

  public void testPublishString() throws Exception {
    jmsWrapper.connect();
    
    jmsWrapper.publishTextMessage(MESSAGE_TEXT);
    Message message = jmsWrapper.getMessage();
    assertNotNull(message);
    assertTrue(message instanceof TextMessage);
    
    jmsWrapper.disconnect();
  }

  protected void setUp() throws Exception {
    jmsWrapper = new OpenJmsUtil(System.getProperties());
  }

  protected void tearDown() throws Exception {
    jmsWrapper.connect();
    jmsWrapper.getAllMessages();
    jmsWrapper.disconnect();

    jmsWrapper = null;
  }
}
