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
import org.astrogrid.registry.messaging.processor.ProcessorException;
import org.w3c.dom.Element;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class MessagingService implements ServiceLifecycle {
  // Constants.
  private static String REGISTRY_FILENAME = "message-processor.reg";
  
  // Logging.
  private Category logger = Category.getInstance(getClass());
  
  // Endpoint context object.
  private ServletEndpointContext servletEndpointContext;
    
  // Element processor registry.
  private Map processorRegistry;

  /**
   * @see javax.xml.rpc.server.ServiceLifecycle#init(java.lang.Object)
   */
  public void init(Object context) throws ServiceException {
   // assert (context instanceof ServletEndpointContext) : "context object is not ServletEndpointContext";
    
    logger.debug("[init] >>>");
    
    servletEndpointContext = (ServletEndpointContext) context;
    ServletContext servletContext = servletEndpointContext.getServletContext();

    // read element processor registry
    processorRegistry = initProcessorRegistry(servletContext);
    
    if(logger.isInfoEnabled()) {
      logger.info("[init] element processors: " + processorRegistry);
    }
    
    logger.debug("[init] <<<");
  }

  /**
   * @see javax.xml.rpc.server.ServiceLifecycle#destroy()
   */
  public void destroy() {
    logger.debug("[destroy] >>>");
    
    processorRegistry = null;
    
    logger.debug("[destroy] <<<");
  }

  public Element[] process(Element[] xml) throws ServiceException {
    //assert (xml != null) : "xml is <null>";
    //assert (xml.length == 1) : "xml array is not of length 1";

    logger.debug("[process] >>>");
    
    List resultList = new ArrayList(xml.length);
    Element currentElement = null;
    Element resultElement = null;
    for(int xmlIndex = 0; xmlIndex < xml.length; xmlIndex++) {
      if(xml[xmlIndex] != null) {
        currentElement = xml[xmlIndex];
        if(logger.isDebugEnabled()) {
          logger.debug("[process] current element: " + XMLUtils.ElementToString(currentElement));
        }

        resultElement = process(currentElement);
        if(logger.isDebugEnabled()) {
          logger.debug("[process] result element: " + XMLUtils.ElementToString(resultElement));
        }

        resultList.add(resultElement); 
      }
    }
    
    logger.debug("[process] <<<");

    return (Element[]) resultList.toArray(new Element[0]);
  }

  private Element process(Element element) throws ServiceException {
    //assert (element != null) : "element is null";
  
    Element result = null;
    
    Element messageContent = (Element) element.getChildNodes().item(1);
    logger.debug("[process] processing: " + messageContent.getNodeName());
    
    ElementProcessor processor = (ElementProcessor) processorRegistry.get(messageContent.getLocalName());
    if(processor != null) {
      try {
        result = processor.process(messageContent, servletEndpointContext);
        logger.debug("[process] result: " + result);
      }
      catch(Exception e) {
        throw new ServiceException("processing error: " + e.getMessage(), e);
      }
    }
    else {
      throw new ServiceException("no processor for " + messageContent.getTagName());
    }
    
    return result;
  }
  
  private Map initProcessorRegistry(ServletContext servletContext) throws ServiceException {
    Map registry = new HashMap();
    
    try {
      Properties elementProcessors = new Properties();
      elementProcessors.load(new FileInputStream(servletContext.getRealPath(REGISTRY_FILENAME)));
      
      Map clazzMap = new HashMap();

      String clazzName = null;      
      Class clazz = null;
      String tagName = null;
      ElementProcessor processor = null;
      Iterator processorIt = elementProcessors.keySet().iterator();
      while(processorIt.hasNext()) {
        tagName = (String) processorIt.next();
        logger.debug("[initProcessorRegistry] tag name: " + tagName);
        
        try {
          clazzName = elementProcessors.getProperty(tagName);
          logger.debug("[initProcessorRegistry] class name: " + clazzName);
          if(clazzMap.containsKey(clazzName)) {
            processor = (ElementProcessor) clazzMap.get(clazzName);
          }
          else {
            clazz = Class.forName(clazzName);
            logger.debug("[initProcessorRegistry] class: " + clazz);

            processor = (ElementProcessor) clazz.newInstance();
            logger.debug("[initProcessorRegistry] got new instance: " + clazz);

            processor.init(servletEndpointContext);
            logger.debug("[initProcessorRegistry] init'd: " + clazz);
            
            clazzMap.put(clazzName, processor);
          }

          registry.put(tagName, processor);
        }
        catch(ClassNotFoundException e) {
          logger.warn("[initProcessorRegistry] could not find class for tag name: " + tagName, e);
        }
        catch(InstantiationException e) {
          logger.warn("[initProcessorRegistry] could instantiate class for tag name: " + tagName, e);
        }
        catch(IllegalAccessException e) {
          logger.warn("[initProcessorRegistry] illegal access to class for tag name: " + tagName, e);
        }
        catch(ProcessorException e) {
          logger.error("[initProcessorRegistry] could not initialise processor for tag name: " + tagName, e);
          throw new ServiceException("could not initialise processor for tag name: " + tagName, e);
        }
        catch(Exception e) {
          logger.error("[initProcessorRegistry] unknown exception: " + e.getMessage(), e);
          throw new ServiceException("unknown exception: " + e.getMessage(), e);
        }
      }
    }
    catch(IOException e) {
      throw new ServiceException("could not load astrolog registry", e);
    }
    
    return registry;
  }
  
  private void destroyProcessorRegistry(Map registry) throws ServiceException {
    try {
      String tagName = null;
      ElementProcessor processor = null;
      Iterator processorIt = registry.keySet().iterator();
      while(processorIt.hasNext()) {
        tagName = (String) processorIt.next();
        processor = (ElementProcessor) registry.get(tagName);
        processor.destroy();
      }
    }
    catch(ProcessorException e) {
      throw new ServiceException("", e);
    }
  }
}
