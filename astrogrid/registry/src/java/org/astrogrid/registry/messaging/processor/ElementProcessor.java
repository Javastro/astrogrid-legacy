package org.astrogrid.registry.messaging.processor;

import javax.xml.rpc.server.ServletEndpointContext;

import org.w3c.dom.Element;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public interface ElementProcessor {
  /**
   * Initialise the element processor.
   * <b>CONTRACT:</b> must be called before <code>process()</code>
   * 
   * @param servletEndpointContext context of calling web service
   */
  void init(ServletEndpointContext servletEndpointContext) throws ProcessorException;

  /**
   * Clean up the element processor.
   * <b>CONTRACT:</b> <code>process()</code> must not be called after <code>destroy()</code> 
   */
  void destroy() throws ProcessorException;
  
  /**
   * Work method.
   * 
   * @param servletEndpointContext context of calling web service
   */
  Element process(Element element, ServletEndpointContext servletEndpointContext) throws ProcessorException;
}
