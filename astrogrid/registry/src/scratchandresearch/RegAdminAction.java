package org.astrogrid.registry;

import java.io.*;
import java.net.*;

import org.w3c.dom.*;
import de.gmd.ipsi.domutil.*;
import de.gmd.ipsi.pdom.*;

/**
 *
 * @author Pedro Contreras <mailto:p.contreras@qub.ac.uk><p>
 * @see School of Computer Science   <br>
 * The Queen's University of Belfast <br>
 * {@link http://www.cs.qub.ac.uk}
 * <p>
 * ASTROGRID Project {@link http://www.astrogrid.org}<br>
 * Registry Group
 *
 */


public class RegAdminAction {

  static final String fileNamePDom = "doc.pdom";
  /**
   * This Method delete any node contained in the variable "deleteNode" in the file
   *
   * @param xmlInputFile URL in which the input XML file is store
   * @param xmlOutputFile physical address and file name where the output file will be
   * @param nodeToDelete name of the node to delete
   * @throws DOMParseException
   * @throws IOException
   */
  public static void deleteNode(String xmlInputFile, String xmlOutputFile, String nodeToDelete) throws
    DOMParseException, IOException {

    //clean up
    File file = new File(fileNamePDom);
    file.delete();
    file = new File(fileNamePDom);

    //PDOM declaration
    PDocument doc = new PDocument(fileNamePDom);

    try {
      // URL xmlFileName variable stores XML file address to parse
      URL xmlFileName = new URL(xmlInputFile);
      InputStream xfl = xmlFileName.openStream();

      //Create a PDOM Document by parsing and XML input stream
      DOMUtil.parseXML(
          xfl,
          doc,
          false, // Parse mode: true = validating, false = non-validating
          DOMUtil.SKIP_IGNORABLE_WHITESPACE
          );
    }
    catch (DOMParseException e) {
      e.printStackTrace();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    // delete nodes in PDOM document
    deleteElements(doc, nodeToDelete);

    //defragmentation
    doc.defragment();

    //clear clache
    PDOM.clearCache();

    //Full garbange collection
    PDOM.collectDOMFileGarbage(fileNamePDom);
    file = new File(fileNamePDom);
    doc = new PDocument(fileNamePDom);

    // write the output in a file
    try {
      String os = xmlOutputFile;
      FileOutputStream outputStream = new FileOutputStream(os);
      XMLWriter out = new XMLWriter(outputStream);
      out.formatOutput(true);
      out.write(doc);
      out.writeln();
      out.flush();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    //Close the document
    doc.close();
 }

 /**
  * This method is called internally to delete a node
  * @param root PDocument
  * @param nodeToDelte node name to be deleted
  */
 static void deleteElements( Node root, String nodeToDelte ) {
     Node child = root.getFirstChild();
     while(child != null) {
             if (child instanceof Element &&
                     child.getNodeName() == nodeToDelte
             ) {
                     child = root.removeChild( child );
             }
             else {
                     deleteElements( child, nodeToDelte );
             }
             child = child.getNextSibling();
     }
 }


 /**
  * This method replace a comple XML file for another one
  * @param oldFile file that will be replaced, is a physical address
  * @param newFile is a URL address or physical
  * @throws DOMParseException
  * @throws IOException
  */
 static void replaceFile(String newFile, String oldFile) throws
    DOMParseException, IOException {

    //clean up
    File file = new File(fileNamePDom);
    file.delete();
    file = new File(fileNamePDom);

    //PDOM declaration
    PDocument doc = new PDocument(fileNamePDom);

    try {
      // URL xmlFileName variable stores XML file address to parse
      URL xmlFileName = new URL(newFile);
      InputStream xfl = xmlFileName.openStream();

      /**Create a PDOM Document by parsing and XML input stream
       * the document is parsed to test if is well formed
       */
      DOMUtil.parseXML(
          xfl,
          doc,
          false, // Parse mode: true = validating, false = non-validating
          DOMUtil.SKIP_IGNORABLE_WHITESPACE
          );
    }
    catch (DOMParseException e) {
      e.printStackTrace();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    //defragmentation
    doc.defragment();

    //clear clache
    PDOM.clearCache();

    //Full garbange collection
    PDOM.collectDOMFileGarbage(fileNamePDom);
    file = new File(fileNamePDom);
    doc = new PDocument(fileNamePDom);

    // write the output in a file
    try {
      String os = oldFile;
      FileOutputStream outputStream = new FileOutputStream(os);
      XMLWriter out = new XMLWriter(outputStream);
      out.formatOutput(true);
      out.write(doc);
      out.writeln();
      out.flush();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    //Close the document
    doc.close();
 }



}
