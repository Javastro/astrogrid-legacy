/*
 * Created on 30-Apr-2003
 */
package org.astrogrid.registry.messaging.queue;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.QueueSender;
import javax.jms.TextMessage;
import javax.naming.NamingException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class OpenJmsSender extends OpenJmsBase {
  // Queue sender.
  private QueueSender queueSender;
  
  public OpenJmsSender(Properties properties) throws NamingException {
    super(properties);
  }
  
  public void sendTextMessage(String content, int deliveryMode) throws JMSException {
    TextMessage textMessage = getQueueSession().createTextMessage();
    textMessage.setJMSDeliveryMode(deliveryMode);
    textMessage.setText(content);
    
    queueSender.send(textMessage);
  }

  public void connect(String queueName, boolean transactional, int acknowledgement) throws NamingException, JMSException {
    super.connect(queueName, transactional, acknowledgement);

    queueSender = getQueueSession().createSender(getQueue());
  }
}
