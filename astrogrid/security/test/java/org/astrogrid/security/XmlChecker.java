package org.astrogrid.security;

import java.util.List;
import java.util.Hashtable;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.xpath.DefaultXPath;

/**
 * Testing utility for XML. Given XML is sarched using given
 * XPath statements. Each test executes one XPath statement and
 * returns the number of matching nodes in the XML.
 *
 * The XML is given in serailized form, as a String, to the constructor
 * and is parsed using DOM (currently, using dom4j) during construction.
 * This avoids reparsing XML for each test.  If the XML is not well
 * formed, then an exception is thrown; this is a preliminary test of
 * the XML.
 *
 * The caller carries out a test by calling {@link countMatchingNodes},
 * which takes XPath in a String as an argument. The XPath is compiled
 * and the method returns the number of matching nodes. Note that
 * "the number of nodes" includes all the nodes in the sub-structure of
 * all nodes that match. Therefore, a result of zero clearly means that
 * no nodes matched and a result of one means that exactly one nodes matched
 * and that node was empty. A result greater than one means either that two
 * or more nodes matched the XPath, or that one node matched and had
 * child nodes, either text nodes or other elements.
 *
 * After construction, the caller may define namespace prefices for the
 * XPath by calling {@link addXpathNamespace}. This enables namespaces to
 * be specified in the XPath by using preficies, e.g.
 * //soap:Header/wsse:Security. If the XML being tested uses namespaces
 * then those namespaces must be set up in the XmlChecker, otherwise
 * the XPath will fail to match the namespace-qualified nodes. However,
 * there is no need for the prefices in the XML and the prefices in the
 * XPath to match. What is important is that the two sets of prefices
 * map to the same namespace URIs.
 *
 * @author Guy Rixon
 */
public class XmlChecker {

  /**
   * The XML on which the searches operate, parsed into a DOM.
   */
  private Document document;

  /**
   * The mapping of namespace prefices (hash keys) to
   * namespace URIs (hash values) for the XPath statements.
   */
  private Hashtable namespaces;


  /**
   * Constructs an XmlChecker initialized with the given XML.
   * The XML is parsed during construction.
   *
   * @param xml the XML on which subsequent searches operate
   * @throws Exception if the XML parsing fails
   */
  public XmlChecker (String xml) throws Exception {
    this.document = DocumentHelper.parseText(xml);
    this.namespaces = new Hashtable();  // No mappings initially.
  }


  /**
   * Adds a namespace mapping to the set supported in the
   * XPath statements.
   *
   * @param prefix the namespace prefix to appear in the XPath
   * @param uri the namespace URI to which the prefix refers
   */
  public void addXpathNamespace (String prefix, String uri) {
    this.namespaces.put(prefix, uri);
  }


  /**
   * Runs the given XPath search against the stored XML and
   * returns the number of nodes in the matching node-set.
   *
   * @param xpath the XPath statement
   * @return the number of nodes in the XPath result
   * @throws Exception if the XPath statement cannot be parsed.
   */
  public int countMatchingNodes (String xpath) throws Exception {
    DefaultXPath xp = new DefaultXPath(xpath);
    xp.setNamespaceURIs(this.namespaces);
    List nodeList = xp.selectNodes(this.document);
    return nodeList.size();
  }

}

