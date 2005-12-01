package org.astrogrid.common.j2ee.environment;

import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A Java bean representing the environment of a web application
 * as initialized from the deployment descriptor (web.xml).
 * Servlets and JSPs can use this bean to display and edit the
 * environment.
 * <p>
 * The bean has five properties: deploymentDescriptor,
 * contextPath, tomcatContextFileName and envEntries.
 * <p>
 * The deploymentDescriptor property holds a URI (written out as
 * a String) by which the web application can access web.xml. This
 * parameter must be written by the client to initialize the bean;
 * setting the parameter causes the bean to parse web.xml and to
 * store the relevant parts of the environment description. This
 * property may not be read back by the client.
 * <p>
 * The contextPath property holds the context path of the root of
 * the web-application (i.e. the path to the application front page).
 * The context path always begins with a slash.
 * <p>
 * The tomcatContextFileName property holds the unqualified file-name
 * of the file which could be used to configure the Tomcat context if
 * the web application is running in the Tomcat web-container. If the
 * context path is /myWebApp then the Tomcat context can be set by
 * writing the file $CATALINA_HOME/conf/Catalina/myWebApp.xml and
 * the property is set to myWebApp.xml.
 * <p>
 * The envEntry property, which is indexed, holds the collection of
 * {@link org.astrogrid.common.j2ee.environemnt.EnvEntry} beans
 * derived from the env-entry elements in web.xml.
 *
 * @author Guy Rixon
 */
public class Environment {

  /** Creates a new instance of Environment */
  public Environment() {}

  /**
   * The context path of the web application.
   */
  private String contextPath;

  /**
   * Gets the context path.
   */
  public String getContextPath() {
    return this.contextPath;
  }

  /**
   * Sets the context path. Also sets the
   * tomcatContextFileName property.
   */
  public void setContextPath(String path) {
    this.contextPath = path;
    this.tomcatContextFileName = this.contextPath.substring(1) + ".xml";
  }

  /**
   * The Tomcat context-file name for this application.
   */
  private String tomcatContextFileName;

  /**
   * Gets the context-file name.
   */
  public String getTomcatContextFileName() {
    return this.tomcatContextFileName;
  }

  /**
   * The environment entries required by this application.
   */
  private EnvEntry[] envEntries;

  /**
   * Gets all the environment entries.
   */
  public EnvEntry getEnvEntry(int index) {
    return this.envEntries[index];
  }

  /**
   * Gets all the environment entries.
   */
  public EnvEntry[] getEnvEntry() {
    return this.envEntries;
  }

  /**
   * Sets the URL for the deployment descriptor (web.xml).
   * This causes the bean to parse the descriptor and thus
   * to extract the environment entries.
   */
  public void setDeploymentDescriptor(String webDotXmlUri) throws Exception {
    this.parse(webDotXmlUri);
  }

  /**
   * Parses a given file and returns the environment entries.
   */
  private void parse(String webDotXmlUri) throws Exception {

    // Parse web.xml into a DOM tree. Validate the input; this is important
    // as the transcription from DOM to Java beans is simplistic and
    // requires schema correctness.
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setValidating(true);  // web.xml should be validated as it is read.
    Document webDotXmlDoc = factory.newDocumentBuilder().parse(webDotXmlUri);

    // Isolate the environment entries, count them and set up their beans.
    NodeList envEntryNodes = webDotXmlDoc.getElementsByTagName("env-entry");
    int nBeans = envEntryNodes.getLength();
    this.envEntries = new EnvEntry[nBeans];
    for (int i = 0; i < nBeans; i++) {
      this.envEntries[i] = this.parseEnvEntry(envEntryNodes.item(i));
    }
  }

  /**
   * Parses an env-entry element represented as a DOM fragment.
   *
   * @param envEntryNode The Element node representng the env-entry element.
   * @return A bean representing the env-entry element.
   */
  private EnvEntry parseEnvEntry(Node envEntryNode) throws Exception {
    EnvEntry envEntry = new EnvEntry();

    // Transcribe the values from the DOM nodes to the bean.
    // One bean expresses one entry value but is made from several
    // node values. Write the value of the entry as a String;
    // let the bean decode it.
    NodeList childNodes = envEntryNode.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node childNode = childNodes.item(i);
      String nodeName = childNode.getNodeName();
      if (nodeName.equals("env-entry-name")) {
        envEntry.setName(this.getElementValue(childNode));
      }
      else if (nodeName.equals("env-entry-type")) {
        envEntry.setType(this.getElementValue(childNode));
      }
      else if (nodeName.equals("env-entry-value")) {
        envEntry.setDefaultValue(this.getElementValue(childNode));
      }
      else if (nodeName.equals("description")) {
        envEntry.setDescription(this.getElementValue(childNode));
      }
      // If it's anything else then we just don't care about it.
    }
    return envEntry;
  }

  /**
   * Gets the value of an XML element represented by a DOM node.
   * The element must be of a simple type with a text nodes in the
   * DOM fragment. This method gets the concatenated value of the text nodes.
   *
   * @param node The DOM node representing the element.
   * @return The value of the text-node child, or null if there is no text node.
   */
  private String getElementValue(Node node) {
    // Trap all errors, including particularly Null Pointer Exceptions.
    try {
      // Make sure that there is at most one text node holding all the
      // text for this element.
      node.normalize();

      // Get all the children. Go through the family and return the
      // text node if there is one.
      NodeList children = node.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        Node child = children.item(i);
        if (child.getNodeType() == Node.TEXT_NODE) {
          return child.getNodeValue();
        }
      }

      // At this point we know that there was no text node,
      // so the value is null.
      return null;
    }
    catch (Throwable t) {
      return null;
    }
  }

}
