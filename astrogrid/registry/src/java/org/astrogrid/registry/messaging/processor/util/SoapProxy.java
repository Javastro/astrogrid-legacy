package org.astrogrid.registry.messaging.processor.util;

import java.net.URL;
import java.rmi.RemoteException;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPBodyElement;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Category;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class SoapProxy implements MessageListener {
  // Logger.
  private Category logger = Category.getInstance(SoapProxy.class);

  // SOAP parameters.  
  private URL endPoint;
  private QName portName;
  private QName operationName;
  private QName returnType;
  
  // SOAP call.
  private Call call;
  
  // Time last message was sent.
  private long lastMessageTime;

  public SoapProxy(URL endPoint, QName portName, QName operationName, QName returnType) {
    this.endPoint = endPoint;
    this.portName = portName;
    this.operationName = operationName;
    this.returnType = returnType;
  }

  public void startup() throws ServiceException {
    Service service = new Service();
    call = (Call) service.createCall();
    
    if(endPoint != null) {
      call.setTargetEndpointAddress(endPoint);
    }
    
    if(portName != null) {
      call.setPortName(portName);
    }
    
    if(operationName != null) {
      call.setOperationName(operationName);
    }
    
    if(returnType != null) {
      call.setReturnType(returnType);
    }
    
    // TODO connect to OpenJMS
    // TODO start receiving messages
  }

  public void shutdown() {
    // TODO stop message processing
    // TODO disconnect from OpenJMS
  }
  
  public long getLastMessageTime() {
    return lastMessageTime;
  }

  /**
   * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
   */
  public void onMessage(Message message) {
    assert message instanceof TextMessage;
    
    // TODO send message
    SOAPBodyElement[] bodyElements = new SOAPBodyElement[0];
    Object result = null;
    try {
      call.invoke(bodyElements);
    }
    catch(RemoteException e) {
      logger.error("[onMessage] failed to send message: " + e.getMessage(), e);
    }
    finally {
      lastMessageTime = System.currentTimeMillis();
    }
  }
}
