/*
 * Created on 30-Apr-2003
 */
package org.astrogrid.registry.astromessagelog;

import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class OpenJmsUtil {
  // Constants.
  public static final String CONNECTION_FACTORY_NAME_PROPERTY = "topicFactory";
  public static final String CONNECTION_FACTORY_NAME_DEFAULT = "JmsTopicConnectionFactory";
  public static final String TOPIC_NAME_PROPERTY = "topic";
  public static final String TOPIC_NAME_DEFAULT = "topic1";
  public static final String SUBSCRIBER_NAME_PROPERTY = "subscriber";
  public static final String SUBSCRIBER_NAME_DEFAULT = "sub1";
  public static final String MESSAGE_TIMEOUT_PROPERTY = "timeout";
  public static final String MESSAGE_TIMEOUT_DEFAULT = "1000";

  // JNDI naming context.
  private InitialContext jndiContext;

  // Topic publication objects.
  private String connectionFactoryName;
  private String topicName;
  private String subscriberName;
  private long timeout;
  private TopicConnection topicConnection;
  private TopicPublisher topicPublisher;
  private TopicSession topicPublisherSession;
  private TopicSubscriber topicSubscriber;
  private Topic topic;
  
  public OpenJmsUtil(Properties properties) throws NamingException {
    jndiContext = new InitialContext();
    
    connectionFactoryName = properties.getProperty(CONNECTION_FACTORY_NAME_PROPERTY, CONNECTION_FACTORY_NAME_DEFAULT);
    topicName = properties.getProperty(TOPIC_NAME_PROPERTY, TOPIC_NAME_DEFAULT);
    subscriberName = properties.getProperty(SUBSCRIBER_NAME_PROPERTY, SUBSCRIBER_NAME_DEFAULT);
    timeout = Long.parseLong(properties.getProperty(MESSAGE_TIMEOUT_PROPERTY, MESSAGE_TIMEOUT_DEFAULT));
  }

  public void publishTextMessage(String content) throws JMSException {
    TextMessage textMessage = topicPublisherSession.createTextMessage();
    textMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
    textMessage.setText(content);
    
    topicPublisher.publish(textMessage);
  }
  
  public Message getMessage() throws JMSException {
    Message message = topicSubscriber.receive(timeout);
    
    return message;
  }
  
  public List getAllMessages() {
    Vector messageTexts = new Vector();
    
    Message message = null;
    
    boolean receivedGoodMessage = true;
    boolean noJmsException = true;
    while (receivedGoodMessage && noJmsException) {
      try {
        message = getMessage();
        
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
    TopicConnectionFactory topicConnectionFactory =
      (TopicConnectionFactory) jndiContext.lookup(connectionFactoryName);
      
    topicConnection = topicConnectionFactory.createTopicConnection();

    topicPublisherSession =
      topicConnection.createTopicSession(false, 
                                         Session.AUTO_ACKNOWLEDGE);
                                         
    topic = topicPublisherSession.createTopic(topicName);
    
    topicPublisher = topicPublisherSession.createPublisher(topic);

    topicSubscriber =
      topicPublisherSession.createDurableSubscriber(topic, subscriberName);
      
    topicConnection.start();
  }
  
  public void disconnect() throws JMSException {
    topicConnection.stop();
    topicConnection.close();
  }
}
