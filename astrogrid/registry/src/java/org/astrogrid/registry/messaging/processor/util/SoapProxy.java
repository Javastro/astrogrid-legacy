package org.astrogrid.registry.messaging.processor.util;

import java.net.URL;
import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPBodyElement;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Category;
import org.astrogrid.registry.messaging.queue.OpenJmsReceiver;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class SoapProxy implements MessageListener {
  // Constants.
  private static final String NAME_SEPARATOR = "|";
  private static final int NO_ACKNOWLEDGEMENT_REQUIRED = 0;
  
  // Logger.
  private Category logger = Category.getInstance(getClass());
  
  // SOAP parameters.  
  private URL endPoint;
  private QName portName;
  private QName operationName;
  private QName returnType;
  private String style;
  private String use;
  private String clientTag;
  
  // OpenJMS connection.
  private OpenJmsReceiver messageReceiver; 
  
  // SOAP call.
  private Call call;
  
  // Time last message was sent.
  private long lastMessageTime;

  public SoapProxy(URL endPoint, QName portName, QName operationName, QName returnType, String style, String use, String clientTag) {
    this.endPoint = endPoint;
    this.portName = portName;
    this.operationName = operationName;
    this.returnType = returnType;
    this.style = style;
    this.use = use;
    this.clientTag = clientTag;
  }

  public static String getName(URL endPoint, QName portName, QName operationName, QName returnType, String style, String use, String clientTag) {
    return endPoint.toExternalForm() + NAME_SEPARATOR + portName + NAME_SEPARATOR + operationName + NAME_SEPARATOR + clientTag;
  }
  
  public String getName() {
    return SoapProxy.getName(endPoint, portName, operationName, returnType, style, use, clientTag);
  }

  public void startup(String queueName, Properties openJmsProperties) throws ServiceException {
    Service service = new Service();
    call = (Call) service.createCall();
    
    logger.debug("[startup] created call");
    
    if(endPoint != null) {
      call.setTargetEndpointAddress(endPoint);
      logger.debug("[startup] end point: " + endPoint);
    }
    
    if(portName != null) {
      call.setPortName(portName);
      logger.debug("[startup] port name: " + portName);
    }
    
    if(operationName != null) {
      call.setOperationName(operationName);
      logger.debug("[startup] operation name: " + operationName);
    }
    
    if(returnType != null) {
      call.setReturnType(returnType);
      logger.debug("[startup] return type: " + returnType);
    }
    
    if(style != null) {
      call.setOperationStyle(style);
      logger.debug("[startup] operation style: " + style);
    }
    
    if(use != null) {
      call.setOperationUse(use);
      logger.debug("[startup] operation use: " + use);
    }
    
    try {
      messageReceiver = new OpenJmsReceiver(openJmsProperties);
      
      logger.debug("[startup] connecting ...");
      messageReceiver.connect(queueName, true, NO_ACKNOWLEDGEMENT_REQUIRED);
      logger.debug("[startup] ... connected");

      messageReceiver.setMessageListener(this);

      logger.debug("[startup] starting ...");
      messageReceiver.start();
      logger.debug("[startup] ... started");
    }
    catch(Exception e) {
      logger.error("[startup] OpenJMS error: " + e.getMessage(), e);
      throw new ServiceException("could not establish OpenJMS connection", e);
    }
  }

  public void shutdown() throws ServiceException {
    try {
      messageReceiver.stop();
      messageReceiver.setMessageListener(null);
      messageReceiver.disconnect();
    }
    catch(JMSException e) {
      logger.warn("[shutdown] could not shutdown proxy: " + getName() + " ... assuming previous shutdown");
    }
  }
  
  public long getLastMessageTime() {
    return lastMessageTime;
  }
  
  /**
   * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
   */
  public void onMessage(Message message) {
    assert message instanceof TextMessage;
    
    SOAPBodyElement[] bodyElements = new SOAPBodyElement[0];
    Object result = null;
    try {
      logger.debug("[onMessage] invoking callback ...");
      result = call.invoke(bodyElements);
      logger.debug("[onMessage] ... call result: " + result);

      messageReceiver.commit();
      logger.debug("[onMessage] committed transaction");
    }
    catch(Exception e) {
      logger.error("[onMessage] rolling back and shutting down ... failed to send message: " + e.getMessage(), e);
      
      try {
        messageReceiver.rollback();
        shutdown();
      }
      catch(Exception inner) {
        logger.error("[onMessage] rollback error: " + inner.getMessage(), inner);
      }
    }
    finally {
      lastMessageTime = System.currentTimeMillis();
    }
  }
}
