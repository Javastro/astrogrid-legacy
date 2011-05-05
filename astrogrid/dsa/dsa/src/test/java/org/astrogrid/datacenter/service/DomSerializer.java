package org.astrogrid.datacenter.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

/**
 *
 * @author Guy Rixon
 */
public class DomSerializer {
  
  /**
   * Serializes the given document to standard output for debugging purposes.
   *
   * @param d The document.
   */
  public static void serializeToStdout(Document d) {
    try {
      DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
      DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");
      LSSerializer writer = impl.createLSSerializer();
      String str = writer.writeToString(d);
      System.out.println(str);
    }
    catch (ClassNotFoundException ex) {
      throw new RuntimeException(ex);
    }
    catch (InstantiationException ex) {
      throw new RuntimeException(ex);
    }
    catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   * Serializes the given element to standard output for debugging purposes.
   *
   * @param e The element.
   */
  public static void serializeToStdout(Element e) {
    try {
      DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
      DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");
      LSSerializer writer = impl.createLSSerializer();
      String str = writer.writeToString(e);
      System.out.println(str);
    }
    catch (ClassNotFoundException ex) {
      throw new RuntimeException(ex);
    }
    catch (InstantiationException ex) {
      throw new RuntimeException(ex);
    }
    catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }

}
