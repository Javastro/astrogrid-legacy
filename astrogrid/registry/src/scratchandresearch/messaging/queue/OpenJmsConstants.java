package org.astrogrid.registry.messaging.queue;

import javax.jms.DeliveryMode;
import javax.jms.Session;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class OpenJmsConstants {
  public static final String OPENJMS_PROPERTIES_FILENAME = "openjms.properties";
  
  // Queue session properties. 
  public static final String QUEUE_CONNECTION_FACTORY_NAME_PROPERTY = "QueueFactory";
  public static final String QUEUE_CONNECTION_FACTORY_NAME_DEFAULT = "JmsQueueConnectionFactory";
  public static final String QUEUE_NAME_PROPERTY = "queue";
  public static final String QUEUE_NAME_DEFAULT = "queue1";
  public static final String MESSAGE_TIMEOUT_PROPERTY = "timeout";
  public static final String MESSAGE_TIMEOUT_DEFAULT = "1000";

  // Sender connection properties.
  public static final String SENDER_TRANSACTIONAL_PROPERTY = "sender.transactional";
  public static final String SENDER_TRANSACTIONAL_DEFAULT= "false";
  
  public static final String SENDER_ACKNOWLEDGEMENT_PROPERTY = "sender.acknowledgement";
  public static final String SENDER_ACKNOWLEDGEMENT_DEFAULT= Integer.toString(Session.AUTO_ACKNOWLEDGE);

  public static final String SENDER_DELIVERY_MODE_PROPERTY = "sender.deliveryMode";
  public static final String SENDER_DELIVERY_MODE_DEFAULT= Integer.toString(DeliveryMode.PERSISTENT);

  // Receiver connection properties.
  public static final String RECEIVER_TRANSACTIONAL_PROPERTY = "receiver.transactional";
  public static final String RECEIVER_TRANSACTIONAL_DEFAULT= "false";
  
  public static final String RECEIVER_ACKNOWLEDGEMENT_PROPERTY = "receiver.acknowledgement";
  public static final String RECEIVER_ACKNOWLEDGEMENT_DEFAULT= Integer.toString(Session.AUTO_ACKNOWLEDGE);

  // XML tag/attribute names.
  public static final String QUEUE_NAME = "queue-name";
}
