package org.astrogrid.registry.messaging.queue;

import javax.jms.JMSException;
import javax.naming.NamingException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public interface OpenJms {
  public abstract void connect(String queueName, boolean transactional, int acknowledgement) throws NamingException, JMSException;
  public abstract void disconnect() throws JMSException;
  public abstract void start() throws JMSException;
  public abstract void stop() throws JMSException;
  public abstract void commit() throws JMSException;
  public abstract void rollback() throws JMSException;
}