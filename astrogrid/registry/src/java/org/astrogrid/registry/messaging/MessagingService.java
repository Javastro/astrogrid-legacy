package org.astrogrid.registry.messaging;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.server.ServiceLifecycle;
import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Category;
import org.astrogrid.registry.messaging.processor.ElementProcessor;
import org.w3c.dom.Element;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class MessagingService implements ServiceLifecycle {
  // Constants.
  private static String XMLBLASTER_PROPERTIES_FILENAME = "xmlblaster.properties";
  private static String REGISTRY_FILENAME = "astrolog-processor.reg";
  
  // Logging.
  private Category logger = Category.getInstance(MessagingService.class);
  
  // Endpoint context object.
  private ServletEndpointContext servletEndpointContext;
    
  // Element processor registry.
  private Map processorRegistry;

  /**
   * @see javax.xml.rpc.server.ServiceLifecycle#init(java.lang.Object)
   */
  public void init(Object context) throws ServiceException {
    assert context instanceof ServletEndpointContext;
    
    logger.debug("[init] >>>");
    
    servletEndpointContext = (ServletEndpointContext) context;
    ServletContext servletContext = servletEndpointContext.getServletContext();

    // read element processor registry
    processorRegistry = loadProcessorRegistry(servletContext);
    
    if(logger.isInfoEnabled()) {
      logger.info("[init] element processors: " + processorRegistry);
    }
    
    // TODO load xmlblaster properties
    
    logger.debug("[init] <<<");
  }

  /**
   * @see javax.xml.rpc.server.ServiceLifecycle#destroy()
   */
  public void destroy() {
    logger.debug("[destroy] >>>");
    
    processorRegistry = null;
    // TODO delete xmlblaster properties
    
    logger.debug("[destroy] <<<");
  }

  public Element[] process(Element[] xml) throws ServiceException {
    assert xml != null;
    assert xml.length == 1;

    logger.debug("[process] >>>");
    
    if(logger.isDebugEnabled()) {
      for(int xmlIndex = 0; xmlIndex < xml.length; xmlIndex++) {
        logger.debug("[process] xml[" + xmlIndex + "]: " + XMLUtils.ElementToString(xml[xmlIndex]));
        logger.debug("[process] xml[" + xmlIndex + "] tag name: " + xml[xmlIndex].getTagName());
      } 
    }
    
    List resultList = new ArrayList(xml.length);
    Element currentElement = null;
    for(int xmlIndex = 0; xmlIndex < xml.length; xmlIndex++) {
      if(xml[xmlIndex] != null) {
        currentElement = xml[xmlIndex];
        resultList.add(process(currentElement)); 
      }
    }
    
    logger.debug("[process] <<<");

    return (Element[]) resultList.toArray(new Element[0]);
  }

  private Element process(Element element) throws ServiceException {
    assert element != null;
  
    Element result = null;
    
    ElementProcessor processor = (ElementProcessor) processorRegistry.get(element.getTagName());
    if(processor != null) {
      try {
        result = processor.process(element, servletEndpointContext);
        logger.debug("[process] result: " + result);
      }
      catch(Exception e) {
        throw new ServiceException("processing error: " + e.getMessage(), e);
      }
    }
    else {
      throw new ServiceException("no processor for " + element.getTagName());
    }
    
    return result;
  }
  
  private Map loadProcessorRegistry(ServletContext servletContext) throws ServiceException {
    Map registry = new HashMap();
    
    try {
      Properties elementProcessors = new Properties();
      elementProcessors.load(
        new FileInputStream(
          servletContext.getRealPath(REGISTRY_FILENAME)));
      
      Class clazz = null;
      String tagName = null;
      Iterator processorIt = elementProcessors.keySet().iterator();
      while(processorIt.hasNext()) {
        tagName = (String) processorIt.next();
        
        try {
          clazz = Class.forName(elementProcessors.getProperty(tagName));
          registry.put(tagName, clazz.newInstance());
        }
        catch(ClassNotFoundException e) {
          logger.warn("could not find class for tag name: " + tagName, e);
        }
        catch(InstantiationException e) {
          logger.warn("could instantiate class for tag name: " + tagName, e);
        }
        catch(IllegalAccessException e) {
          logger.warn("illegal access to class for tag name: " + tagName, e);
        }
      }
    }
    catch(IOException e) {
      throw new ServiceException("could not load astrolog registry", e);
    }
    
    return registry;
  }

  private boolean isConnected() {
    return false;
  }
  
  private void connect() {
    if(!isConnected()) {
      // TODO Auto-generated method stub
    }
  }
  
  private void disconnect() {
    if(isConnected()) {
      // TODO Auto-generated method stub
    }
  }
}
