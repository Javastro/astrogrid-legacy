package org.astrogrid.registry.messaging.processor;

import javax.xml.rpc.server.ServletEndpointContext;

import org.apache.axis.utils.XMLUtils;
import org.apache.log4j.Category;
import org.w3c.dom.Element;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class AnotherTestProcessor implements ElementProcessor {
  // logger
  private Category logger = Category.getInstance(getClass());

  /**
   * @see org.astrogrid.registry.messaging.ElementProcessor#process(org.w3c.dom.Element, javax.xml.rpc.server.ServletEndpointContext)
   */
  public Element process(Element element, ServletEndpointContext servletEndpointContext) throws Exception {
    // TODO Auto-generated method stub
    logger.debug("[process] received: " + XMLUtils.ElementToString(element));
    return XMLUtils.StringToElement("myNS", "another-test-processed", "true");
  }

}
