package org.astrogrid.warehouse.queriers.ogsadai;

import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.config.SimpleConfig;
import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;


/**
 * Command-line test harness for the WarehouseQuerier.
 * For development and testing purposes only.
 *
 * @author K Andrews
 */
public class WarehouseQuerierClient
{
  public static void main(String args[]) throws Exception {

    String errorString = 
      "Usage: java WarehouseQuerierClient <properties_fileName> <sql_query>";

    // We're not running within a datacenter;  throw junk
    // parameters at the constructor as we won't be using them.
    // TOFIX this may break in the future.
    WarehouseQuerier querier = new WarehouseQuerier("Blah", null);

    String sql;
    String propertiesFile;
    try {
      int len = args.length;
      if (len == 0) {
        throw new DatabaseAccessException(errorString);
      }
      propertiesFile= args[0];
      if (propertiesFile.equals(null)) {
        throw new DatabaseAccessException(errorString);
      }
      sql = args[1];
      if (sql.equals(null)) {
        throw new DatabaseAccessException(errorString);
      }
      if (len > 2) {
        throw new DatabaseAccessException(errorString);
      }
    }
    catch (ArrayIndexOutOfBoundsException e) {
      throw new DatabaseAccessException(errorString);
    }
    // Load config properties from config file
    SimpleConfig.load(propertiesFile);

    // Do actual query
    Document result = querier.doShelledOutQuery(sql, null); 
    printNode(result);
  }

  // TOFIX Borrowed this from google
  protected static void printNode(Node node) {
    switch (node.getNodeType()) {
      case Node.DOCUMENT_NODE:
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF8\"?>");
        Document doc = (Document)node;
        printNode(doc.getDocumentElement());
        break;
      case Node.ELEMENT_NODE:
        Element element = (Element)node;
        element.normalize();
        String name = node.getNodeName();
        System.out.print("<" + name);
        NamedNodeMap attributes = node.getAttributes();
        for (int i=0; i<attributes.getLength(); i++) {
          Node attr = attributes.item(i);
          System.out.print(" " + attr.getNodeName() + "=\""
                               + attr.getNodeValue() + "\"");
        }
        System.out.println(">");
        NodeList children = node.getChildNodes();
        if (children != null) {
          for (int i=0; i<children.getLength();i++) {
            printNode(children.item(i));
          }
        }
        System.out.println("</" + name + ">");
        break;
      case Node.TEXT_NODE:
      case Node.CDATA_SECTION_NODE:
         System.out.println(node.getNodeValue()+"");  // needs filtering!
        break;
      case Node.PROCESSING_INSTRUCTION_NODE:
        break;
      case Node.ENTITY_REFERENCE_NODE:
        System.out.println("&" + node.getNodeName() + ";");
        break;
      case Node.DOCUMENT_TYPE_NODE:
        break;
    }
  }
}
