package org.astrogrid.registry.messaging.processor;

import javax.xml.rpc.server.ServletEndpointContext;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Category;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class QueueMessageCallbackProcessor implements ElementProcessor {
  // Constants.
  private static final int DEFAULT_CLEANUP_TIMEOUT = -1;

  // Logger.
  private Category logger = Category.getInstance(QueueMessageCallbackProcessor.class);
  
  // Map identifiers to SOAP proxies.
  private ConcurrentReaderHashMap soapProxyMap;

  public QueueMessageCallbackProcessor() {
    soapProxyMap = new ConcurrentReaderHashMap();
  }
  
  /**
   * @see org.astrogrid.registry.messaging.processor.ElementProcessor#process(org.w3c.dom.Element, javax.xml.rpc.server.ServletEndpointContext)
   */
  public Element process(Element element, ServletEndpointContext servletEndpointContext) throws Exception {
    if(logger.isDebugEnabled()) {
      logger.debug("[process] element: " + XMLUtils.ElementToString(element));
    }
    
    Node node = element.getFirstChild();
    String nodeName = node.getNodeName();
    if(nodeName.equals("register")) {
      register(node);
    }
    else if(nodeName.equals("deregister")) {
      deregister(node);
    }
    
    // TODO get SOAP parameters
    // TODO create new SOAP proxy
    // TODO activate SOAP proxy
    // TODO place in proxy pool
    
    return null;
  }

  private void register(Node registerNode) {
  }

  private void deregister(Node registerNode) {
  }
}
