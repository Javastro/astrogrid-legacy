/*
 * Created on 30-Apr-2003
 */
package org.astrogrid.registry.messaging.queue;

import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class OpenJmsUtil {
  // Constants.
  public static final String QUEUE_CONNECTION_FACTORY_NAME_PROPERTY = "QueueFactory";
  public static final String QUEUE_CONNECTION_FACTORY_NAME_DEFAULT = "JmsQueueConnectionFactory";
  public static final String QUEUE_NAME_PROPERTY = "queue";
  public static final String QUEUE_NAME_DEFAULT = "queue1";
  public static final String MESSAGE_TIMEOUT_PROPERTY = "timeout";
  public static final String MESSAGE_TIMEOUT_DEFAULT = "1000";

  // JNDI naming context.
  private InitialContext jndiContext;

  // Configuration properties.
  private String queueConnectionFactoryName;
  private String defaultQueueName;
  private long timeout;

  // Queue connection details.
  QueueConnection queueConnection;
  QueueSession queueSession;
  QueueReceiver queueReceiver;
  QueueSender queueSender;
  Queue queue;
  
  public OpenJmsUtil(Properties properties) throws NamingException {
    jndiContext = new InitialContext(properties);
    
    queueConnectionFactoryName = properties.getProperty(QUEUE_CONNECTION_FACTORY_NAME_PROPERTY, QUEUE_CONNECTION_FACTORY_NAME_DEFAULT);
    defaultQueueName = properties.getProperty(QUEUE_NAME_PROPERTY, QUEUE_NAME_DEFAULT);
    timeout = Long.parseLong(properties.getProperty(MESSAGE_TIMEOUT_PROPERTY, MESSAGE_TIMEOUT_DEFAULT));
  }
  
  public void sendTextMessage(String content) throws JMSException {
    TextMessage textMessage = queueSession.createTextMessage();
    textMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
    textMessage.setText(content);
    
    queueSender.send(textMessage);
  }

  public TextMessage getTextMessage() throws JMSException {
    TextMessage message = (TextMessage) queueReceiver.receive(timeout);
    
    return message; 
  }
  
  public List getAllTextMessages() {
    Vector messageTexts = new Vector();
    
    TextMessage message = null;
    
    boolean receivedGoodMessage = true;
    boolean noJmsException = true;
    while (receivedGoodMessage && noJmsException) {
      try {
        message = getTextMessage();
        
        if (message == null) {
          receivedGoodMessage = false;
        }
        else {
          messageTexts.add(message);
        }
      }
      catch (JMSException e) {
        noJmsException = false;
      }
    }
    
    return messageTexts;
  }
  
  public void connect() throws NamingException, JMSException {
    connect(null);
  }
  
  public void connect(String queueName) throws NamingException, JMSException {
    QueueConnectionFactory queueConnectionFactory =
      (QueueConnectionFactory) jndiContext.lookup(queueConnectionFactoryName);

    queueConnection = queueConnectionFactory.createQueueConnection();
    
    queueSession = queueConnection.createQueueSession(false,
                                                      Session.AUTO_ACKNOWLEDGE);
  
    if(queueName == null || queueName.length() == 0) {
      queue = queueSession.createQueue(defaultQueueName);
    }
    else {
      queue = queueSession.createQueue(queueName);
    }
  
    queueReceiver = queueSession.createReceiver(queue);
  
    queueSender = queueSession.createSender(queue);
    
    queueConnection.start();
  }
  
  public void disconnect() throws JMSException {
    queueConnection.stop();
    queueConnection.close();
  }
}
