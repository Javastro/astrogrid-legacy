/*
 * Created on 30-Apr-2003
 */
package org.astrogrid.registry.messaging.queue;

import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.QueueReceiver;
import javax.jms.TextMessage;
import javax.naming.NamingException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class OpenJmsReceiver extends OpenJmsBase implements OpenJmsReceiverInterface {
  // Queue receiver.
  private QueueReceiver queueReceiver;
  
  public OpenJmsReceiver(Properties properties) throws NamingException {
    super(properties);
  }
  
  public void connect(String queueName, boolean transactional, int acknowledgement) throws NamingException, JMSException {
    super.connect(queueName, transactional, acknowledgement);
    
    queueReceiver = getQueueSession().createReceiver(getQueue());
  }
  
  public TextMessage getTextMessage() throws JMSException {
    TextMessage message = (TextMessage) queueReceiver.receive(getTimeout());
    
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
  
  public void setMessageListener(MessageListener listener) throws JMSException {
    queueReceiver.setMessageListener(listener);
  }
}
