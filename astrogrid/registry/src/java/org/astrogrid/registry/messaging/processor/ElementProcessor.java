package org.astrogrid.registry.messaging.processor;

import javax.xml.rpc.server.ServletEndpointContext;

import org.w3c.dom.Element;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public interface ElementProcessor {
  Element process(Element element, ServletEndpointContext servletEndpointContext) throws Exception;
}
