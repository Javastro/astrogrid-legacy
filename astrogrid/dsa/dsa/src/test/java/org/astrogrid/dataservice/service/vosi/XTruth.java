package org.astrogrid.dataservice.service.vosi;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Guy Rixon
 */
public class XTruth {
  
  XPath xpath = null;
  
  NodeList result = null;
  
  public XTruth(String expression, InputSource xml, NamespaceContext namespaces) throws XPathExpressionException {
    xpath = XPathFactory.newInstance().newXPath();
    xpath.setNamespaceContext(namespaces);
    result = (NodeList) xpath.evaluate(expression, xml, XPathConstants.NODESET);
    if (result == null) {
      throw new RuntimeException("Null result from XPath");
    }
  }

  public boolean exists() {
    return (result.getLength() > 0);
  }

  public int count() {
    return result.getLength();
  }

  public boolean hasValue(String expression, String value) {
    return getValue().equals(value);
  }

  public boolean hasNamespacedValue(String expression, String namespace, String value) {
    String prefix = xpath.getNamespaceContext().getPrefix(namespace);
    return getValue().equals(prefix + ":" + value);
  }

  public String getValue() {
    return getValue(0);
  }

  public String getValue(int item) {
    if (result.getLength() == 0) {
      throw new RuntimeException("Result of XPath doesn't contain any nodes");
    }
    switch (result.item(item).getNodeType()) {
      case Node.ELEMENT_NODE:
        NodeList children = result.item(item).getChildNodes();
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < children.getLength(); i++) {
          Node n = children.item(i);
          if (n.getNodeType() == Node.TEXT_NODE) {
            b.append(n.getNodeValue());
          }
        }
        return b.toString();
      case Node.TEXT_NODE:
        return result.item(item).getNodeValue();
      case Node.ATTRIBUTE_NODE:
        return result.item(item).getNodeValue();
      default:
        throw new RuntimeException("Not a text or element node");
    }
  }

}
