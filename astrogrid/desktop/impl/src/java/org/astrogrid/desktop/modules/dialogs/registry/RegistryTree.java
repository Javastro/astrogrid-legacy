package org.astrogrid.desktop.modules.dialogs.registry;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class RegistryTree extends JTree {

	  public RegistryTree(Document doc) {
	    super(makeRootNode(doc));
	  }
	  
	  /**
	   * Commons Logger for this class
	   */
	  private static final Log logger = LogFactory.getLog(RegistryChooserPanel.class);
	  
	  // This method needs to be static so that it can be called
	  // from the call to the parent constructor (super), which
	  // occurs before the object is really built.
	  
	  private static DefaultMutableTreeNode makeRootNode(Document document) {
	    try {
	      // Use JAXP's DocumentBuilderFactory so that there
	      // is no code here that is dependent on a particular
	      // DOM parser. Use the system property
	      // javax.xml.parsers.DocumentBuilderFactory (set either
	      // from Java code or by using the -D option to "java").
	      // or jre_dir/lib/jaxp.properties to specify this.
	      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	      DocumentBuilder builder = builderFactory.newDocumentBuilder();
	      document.getDocumentElement().normalize();
	      Element rootElement = document.getDocumentElement();
	      DefaultMutableTreeNode rootTreeNode = buildTree(rootElement);
	      return(rootTreeNode);
	    } catch(Exception e) {
	      String errorMessage = "Error making root node: " + e;
	      logger.error("Error making root node: " + e);
	      return(new DefaultMutableTreeNode(errorMessage));
	    }
	  }

	  /*
	   * Make a JTree node for the root, then make JTree nodes for each child 
	   * and add them to the root node. The addChildren method is recursive.
	   */
	  private static DefaultMutableTreeNode buildTree(Element rootElement) {
	    DefaultMutableTreeNode rootTreeNode = new DefaultMutableTreeNode(treeNodeLabel(rootElement));
	    addChildren(rootTreeNode, rootElement);
	    return(rootTreeNode);
	  }
	  
	  /*
	   * Recursive method that finds all the child elements 
	   * and adds them to the parent node.
	   */
	  private static void addChildren(DefaultMutableTreeNode parentTreeNode,
	                        		  Node parentXMLElement) {
	    
	    NodeList childElements = parentXMLElement.getChildNodes();
	    for(int i=0; i<childElements.getLength(); i++) {
	      Node childElement = childElements.item(i);
	      if (!(childElement instanceof Text || childElement instanceof Comment)) {
	        DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(treeNodeLabel(childElement));
	        parentTreeNode.add(childTreeNode);
	        addChildren(childTreeNode, childElement);
	      }
	    }
	  }

	  // If the XML element has no attributes, the JTree node
	  // will just have the name of the XML element. If the
	  // XML element has attributes, the names and values of the
	  // attributes will be listed in parens after the XML
	  // element name. For example:
	  // XML Element: <blah>
	  // JTree Node:  blah
	  // XML Element: <blah foo="bar" baz="quux">
	  // JTree Node:  blah (foo=bar, baz=quux)

	  private static String treeNodeLabel(Node childElement) {
	    NamedNodeMap elementAttributes = childElement.getAttributes();
	    String treeNodeLabel = childElement.getNodeName();
	    if (elementAttributes != null && elementAttributes.getLength() > 0) {
	      treeNodeLabel = treeNodeLabel + " (";
	      int numAttributes = elementAttributes.getLength();
	      for(int i=0; i<numAttributes; i++) {
	        Node attribute = elementAttributes.item(i);
	        if (i > 0) {
	          treeNodeLabel = treeNodeLabel + ", ";
	        }
	        treeNodeLabel = treeNodeLabel + attribute.getNodeName() +
	          "=" + attribute.getNodeValue();
	      }
	      treeNodeLabel = treeNodeLabel + ")";
	    }
	    return(treeNodeLabel);
	  }
	}
