/*
 * Created on 30-Apr-2003
 */
package org.astrogrid.registry.messaging.queue;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class OpenJmsBase implements OpenJms {
  // JNDI naming context.
  private InitialContext jndiContext;

  // Configuration properties.
  private String queueConnectionFactoryName;
  private String defaultQueueName;
  private long timeout;

  // Queue connection details.
  private String queueName;
  private QueueConnection queueConnection;
  private QueueSession queueSession;
  private Queue queue;
  
  public OpenJmsBase(Properties properties) throws NamingException {
    jndiContext = new InitialContext(properties);
    
    queueConnectionFactoryName =
      properties.getProperty(
        OpenJmsConstants.QUEUE_CONNECTION_FACTORY_NAME_PROPERTY,
        OpenJmsConstants.QUEUE_CONNECTION_FACTORY_NAME_DEFAULT);
        
    defaultQueueName =
      properties.getProperty(
        OpenJmsConstants.QUEUE_NAME_PROPERTY,
        OpenJmsConstants.QUEUE_NAME_DEFAULT);
        
    timeout = Long.parseLong(
      properties.getProperty(
        OpenJmsConstants.MESSAGE_TIMEOUT_PROPERTY,
        OpenJmsConstants.MESSAGE_TIMEOUT_DEFAULT));
  }
  
  public void connect(String queueName, boolean transactional, int acknowledgement) throws NamingException, JMSException {
    QueueConnectionFactory queueConnectionFactory =
      (QueueConnectionFactory) jndiContext.lookup(queueConnectionFactoryName);

    queueConnection = queueConnectionFactory.createQueueConnection();
    
    queueSession = queueConnection.createQueueSession(transactional, acknowledgement);
  
    if(queueName == null || queueName.length() == 0) {
      queue = queueSession.createQueue(defaultQueueName);
    }
    else {
      queue = queueSession.createQueue(queueName);
    }
    
    this.queueName = queueName;
  }
  
  public void disconnect() throws JMSException {
    try {
      stop();
    }
    catch(JMSException e) {
      // assume already stopped
    }
    
    queueConnection.close();
  }
  
  public void start() throws JMSException {
    queueConnection.start();
  }
  
  public void stop() throws JMSException {
    queueConnection.stop();
  }
  
  public void commit() throws JMSException {
    queueSession.commit();
  }

  public void rollback() throws JMSException {
    queueSession.rollback();
  }

  protected long getTimeout() {
    return timeout;
  }

  protected QueueSession getQueueSession() {
    return queueSession;
  }

  protected Queue getQueue() {
    return queue;
  }
}
