package org.astrogrid.registry.messaging.queue;

import javax.jms.JMSException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public interface OpenJmsSenderInterface extends OpenJms {
  public abstract void sendTextMessage(String content, int deliveryMode) throws JMSException;
}