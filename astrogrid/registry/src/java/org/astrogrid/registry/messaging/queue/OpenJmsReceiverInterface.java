package org.astrogrid.registry.messaging.queue;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public interface OpenJmsReceiverInterface extends OpenJms {
  public abstract TextMessage getTextMessage() throws JMSException;
  public abstract List getAllTextMessages();
  public abstract void setMessageListener(MessageListener listener) throws JMSException;
}