package org.astrogrid.registry.messaging.queue;

import java.util.Properties;

import javax.naming.NamingException;

import org.apache.log4j.Category;

import mock.org.astrogrid.registry.messaging.queue.MockOpenJmsReceiverInterface;
import mock.org.astrogrid.registry.messaging.queue.MockOpenJmsSenderInterface;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class OpenJmsFactory {
  // Mock OpenJms.
  private static MockOpenJmsReceiverInterface MOCK_OPEN_JMS_RECEIVER = null;
  private static MockOpenJmsSenderInterface MOCK_OPEN_JMS_SENDER = null;
  
  // Logger.
  private static Category LOGGER = Category.getInstance(OpenJmsFactory.class);

  public static synchronized OpenJmsReceiverInterface getOpenJmsReceiver(Properties properties) throws NamingException {
    OpenJmsReceiverInterface result = null;
    
    String testProperty = System.getProperty("TEST", "false");
    boolean testOpenJms = Boolean.valueOf(testProperty).booleanValue();
    
    if(testOpenJms) {
      result = MOCK_OPEN_JMS_RECEIVER;
      LOGGER.info("[getOpenJmsReceiver] mock OpenJMS receiver");
    }
    else {
      result = new OpenJmsReceiver(properties);
      LOGGER.info("[getOpenJmsReceiver] real OpenJMS receiver");
    }
    
    return result;
  }
  
  public static synchronized OpenJmsSenderInterface getOpenJmsSender(Properties properties) throws NamingException {
    OpenJmsSenderInterface result = null;
    
    String testProperty = System.getProperty("TEST", "false");
    boolean testOpenJms = Boolean.valueOf(testProperty).booleanValue();
    
    if(testOpenJms) {
      result = MOCK_OPEN_JMS_SENDER;
      LOGGER.info("[getOpenJmsSender] mock OpenJMS receiver");
    }
    else {
      result = new OpenJmsSender(properties);
      LOGGER.info("[getOpenJmsSender] real OpenJMS receiver");
    }
    
    return result;
  }
  
  public static synchronized void setMockOpenJmsReceiver(MockOpenJmsReceiverInterface mockOpenJmsReceiver) {
    OpenJmsFactory.MOCK_OPEN_JMS_RECEIVER = mockOpenJmsReceiver;
  }
  
  public static synchronized void setMockOpenJmsSender(MockOpenJmsSenderInterface mockOpenJmsSender) {
    OpenJmsFactory.MOCK_OPEN_JMS_SENDER = mockOpenJmsSender;
  }
  
  // Never instantiated.
  private OpenJmsFactory() {
  }

}
