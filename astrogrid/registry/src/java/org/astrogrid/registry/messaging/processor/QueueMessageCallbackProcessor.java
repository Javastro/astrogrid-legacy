package org.astrogrid.registry.messaging.processor;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServletEndpointContext;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Category;
import org.astrogrid.registry.messaging.processor.util.QueueMessageCallbackCleaner;
import org.astrogrid.registry.messaging.processor.util.SoapProxy;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class QueueMessageCallbackProcessor extends QueueMessageProcessorBase implements ElementProcessor {
  // Configuration file.
  private static final String CALLBACK_PROPERTIES_FILENAME = "queue-callback.properties";

  // Configuration properties.
  private static final String SLEEP_TIME_PROPERTY = "sleepTime";
  private static final String DEFAULT_SLEEP_TIME = "10000";
  private static final String PROXY_LIFETIME_PROPERTY = "proxyLifetime";
  private static final String DEFAULT_PROXY_LIFETIME = "10000";
  
  // Start/Stop tags.
  private static final String CALLBACK_START = "queue-callback-start";
  private static final String CALLBACK_STOP = "queue-callback-stop";
  
  // SOAP call properties.
  private static final String END_POINT = "end-point";
  private static final String PORT_TYPE = "port-type";
  private static final String OPERATION_NAME = "operation-name";
  private static final String RETURN_TYPE = "return-type";
  private static final String STYLE = "style";
  private static final String USE  = "use";
  private static final String CLIENT_TAG = "client-tag";
  private static final String PROXY_NAME  = "id";
  
  // OpenJMS queue.
  private static final String QUEUE_NAME = "queue-name";

  // Logger.
  private Category logger = Category.getInstance(getClass());
  
  // Map identifiers to SOAP proxies - this object is thread-safe.
  private ConcurrentReaderHashMap soapProxyMap;
  
  // Proxy clean up thread.
  private QueueMessageCallbackCleaner soapProxyCleaner; 

  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#init(javax.xml.rpc.server.ServletEndpointContext)
   */
  public void init(ServletEndpointContext servletEndpointContext) throws ProcessorException {
    super.init(servletEndpointContext);
    
    ServletContext servletContext = servletEndpointContext.getServletContext();
    
    soapProxyMap = new ConcurrentReaderHashMap();
    soapProxyCleaner = createSoapProxyCleaner(servletContext.getRealPath(CALLBACK_PROPERTIES_FILENAME));
  }
  
  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#destroy()
   */
  public void destroy() throws ProcessorException {
    String soapProxyName = null;
    Iterator soapProxyIt = soapProxyMap.keySet().iterator();
    while(soapProxyIt.hasNext()) {
      soapProxyName = (String) soapProxyIt.next();
      
    }
    
    super.destroy();
  }

  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#process(org.w3c.dom.Element, javax.xml.rpc.server.ServletEndpointContext)
   */
  public Element process(Element element, ServletEndpointContext servletEndpointContext) throws ProcessorException {
    Element result = null;
    
    if(logger.isDebugEnabled()) {
      logger.debug("[process] element: " + XMLUtils.ElementToString(element));
    }
    
    String nodeName = element.getLocalName();
    logger.debug("[process] node name: " + nodeName);
    if(nodeName.equals(CALLBACK_START)) {
      logger.debug("[process] starting proxy");
      result = start(element);
    }
    else if(nodeName.equals(CALLBACK_STOP)) {
      logger.debug("[process] stoping proxy");
      result = stop(element);
    }
    else {
      logger.debug("[process] unknown command");
    }
    
    return result;
  }

  private Element start(Element startElement) throws ProcessorException {
    logger.debug("[start] >>>");
    
    Element result = null;
    
    String queueName = startElement.getAttribute(QUEUE_NAME);
    
    String endPoint = getFirstValue(startElement, END_POINT);
    String portType = getFirstValue(startElement, PORT_TYPE);
    String operationName = getFirstValue(startElement, OPERATION_NAME);
    String returnType = getFirstValue(startElement, RETURN_TYPE);
    String style = getFirstValue(startElement, STYLE);
    String use = getFirstValue(startElement, USE);
    String clientTag = getFirstValue(startElement, CLIENT_TAG);

    try {
      SoapProxy soapProxy = new SoapProxy(
        new URL(endPoint),
        new QName(portType),
        new QName(operationName),
        new QName(returnType),
        style,
        use,
        clientTag);

      String soapProxyName = soapProxy.getName();

      if(logger.isDebugEnabled()) {
        logger.debug("[start] starting<<<" + soapProxyName + ">>>");

        logger.debug("[start] number of proxies: " + soapProxyMap.size());
        Iterator keyIt = soapProxyMap.keySet().iterator();
        while(keyIt.hasNext()) {
          logger.debug("[start] next key  <<<" + keyIt.next() + ">>>" );
        }
      }

      if(!soapProxyMap.containsKey(soapProxyName)) {
        soapProxyMap.put(soapProxyName, soapProxy);
        soapProxy.startup(queueName, openJmsProperties);
        logger.debug("[start] added proxy for: " + soapProxyName);
      }
      else {
        logger.debug("[start] cannot register proxy for: " + soapProxyName + " .. already registered");
        throw new ProcessorException("cannot register proxy for: " + soapProxyName + " .. already registered");
      }
    }
    catch(MalformedURLException e) {
      logger.error("[start] invalid URL", e);
      throw new ProcessorException("invalid URL", e);
    }
    catch(ServiceException e) {
      logger.error("[start] could not create service call", e);
      throw new ProcessorException("could not create service call", e);
    }
      
    logger.debug("[start] <<<");

    return result;
  }

  private Element stop(Element stopElement) throws ProcessorException {
    logger.debug("[stop] >>>");

    Element result = null;

    String endPoint = getFirstValue(stopElement, END_POINT);
    String portType = getFirstValue(stopElement, PORT_TYPE);
    String operationName = getFirstValue(stopElement, OPERATION_NAME);
    String returnType = getFirstValue(stopElement, RETURN_TYPE);
    String style = getFirstValue(stopElement, STYLE);
    String use = getFirstValue(stopElement, USE);
    String clientTag = getFirstValue(stopElement, CLIENT_TAG);
    
    String soapProxyName = null;
    try {
      soapProxyName = SoapProxy.getName(
        new URL(endPoint),
        new QName(portType),
        new QName(operationName),
        new QName(returnType),
        style,
        use,
        clientTag);
      logger.debug("[stop] stopping proxy: " + soapProxyName);

      String soapProxyName2 = SoapProxy.getName(
        new URL(endPoint),
        new QName(portType),
        new QName(operationName),
        new QName(returnType),
        style,
        use,
        clientTag);
        
      logger.debug("[stop] proxy1.equals(proxy2): " + soapProxyName.equals(soapProxyName2));
    }
    catch(MalformedURLException e) {
      logger.error("[stop] bad end point supplied", e);
      throw new ProcessorException("bad end point supplied", e);
    }
        
    try {
      if(logger.isDebugEnabled()) {
        logger.debug("[stop] stopping <<<" + soapProxyName + ">>>");

        logger.debug("[stop] number of proxies: " + soapProxyMap.size());
        Iterator keyIt = soapProxyMap.keySet().iterator();
        while(keyIt.hasNext()) {
          logger.debug("[stop] next key  <<<" + keyIt.next() + ">>>" );
        }
      }
      
      if(soapProxyMap.containsKey(soapProxyName)) {
        SoapProxy soapProxy = (SoapProxy) soapProxyMap.get(soapProxyName);
        soapProxy.shutdown();
        soapProxyMap.remove(soapProxyName);
      }
      else {
        logger.debug("[stop] no proxy for: " + soapProxyName);
        throw new ProcessorException(" no proxy for: " + soapProxyName);
      }
    }
    catch(ServiceException e) {
      logger.error("[stop] could not shutdown SOAP proxy", e);
      throw new ProcessorException("could not shutdown SOAP proxy", e);
    }

    logger.debug("[stop] <<<");

    return result;
  }
  
  private String getFirstValue(Element element, String tagName) {
    String result = null;
    
    logger.debug("[getFirstValue] getting value of " + tagName);
    
    boolean nodeNotFound = true;
    Element currentElement = null;
    NodeList children = element.getElementsByTagName("*");
    
    for(int childIndex = 0; childIndex < children.getLength() && nodeNotFound; childIndex++) {
      currentElement = (Element) children.item(childIndex);
      logger.debug("[getFirstValue] current node: " + currentElement.getLocalName());
      if(currentElement != null && tagName.equals(currentElement.getLocalName())) {
        NodeList childNodes = currentElement.getChildNodes();
        result = childNodes.item(0).getNodeValue();
        nodeNotFound = false;
      }
    }
    
    logger.debug("[getFirstValue] result: " + result);
    
    return result;
  }
  
  private QueueMessageCallbackCleaner createSoapProxyCleaner(String propertiesFilename) throws ProcessorException {
    long sleepTime = 0L;
    long proxyLifetime = 0L;
    
    Properties queueCallbackProperties = new Properties();
    try {
      queueCallbackProperties.load(new FileInputStream(propertiesFilename));
      
      sleepTime = Long.parseLong(
        queueCallbackProperties.getProperty(SLEEP_TIME_PROPERTY, DEFAULT_SLEEP_TIME));
      
      proxyLifetime = Long.parseLong(
        queueCallbackProperties.getProperty(PROXY_LIFETIME_PROPERTY, DEFAULT_PROXY_LIFETIME));
    }
    catch(Exception e) {
      logger.warn("[createSoapProxyCleaner] could not load queue callback properties ... using defaults", e);
    }
    
    return new QueueMessageCallbackCleaner(sleepTime, proxyLifetime, soapProxyMap);
  }
}
