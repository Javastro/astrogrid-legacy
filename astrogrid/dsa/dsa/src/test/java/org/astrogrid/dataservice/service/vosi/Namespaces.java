package org.astrogrid.dataservice.service.vosi;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.NamespaceContext;

/**
 * Namespace mapping for use with XPath.
 *
 * @author Guy Rixon
 */
public class Namespaces implements NamespaceContext {

  private Map<String,Set<String>> namespaceToPrefices =
      new HashMap<String,Set<String>>();

  private Map<String,String> prefixToNamespace =
      new HashMap<String,String>();

  public String getNamespaceURI(String prefix) {
    return prefixToNamespace.get(prefix);
  }

  public String getPrefix(String namespaceUri) {
    if (namespaceToPrefices.containsKey(namespaceUri)) {
      return namespaceToPrefices.get(namespaceUri).iterator().next();
    }
    else {
      return null;
    }
  }

  public Iterator getPrefixes(String namespaceUri) {
    if (namespaceToPrefices.containsKey(namespaceUri)) {
      return namespaceToPrefices.get(namespaceUri).iterator();
    }
    else {
      return new HashSet<String>(0).iterator();
    }
  }

  public void addNamespace(String namespaceUri, String prefix) {
    if (namespaceToPrefices.containsKey(namespaceUri)) {
      namespaceToPrefices.get(namespaceUri).add(prefix);
    }
    else {
      Set<String> prefices = new HashSet<String>(1);
      prefices.add(prefix);
      namespaceToPrefices.put(namespaceUri, prefices);
    }
    prefixToNamespace.put(prefix, namespaceUri);
  }

}
