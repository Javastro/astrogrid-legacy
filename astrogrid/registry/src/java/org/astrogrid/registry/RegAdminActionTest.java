package org.astrogrid.registry;

import java.io.*;

import de.gmd.ipsi.domutil.*;
import junit.framework.*;
import de.gmd.ipsi.domutil.DOMParseException;

public class RegAdminActionTest
    extends TestCase {

  /**
   * Test class for RegAdminActionTest.java file
   */

  public static void main(String args[]) {
    junit.textui.TestRunner.run(RegAdminActionTest.class);
  }

  private RegAdminAction regAdminAction = null;

  String pathFileA = "http://143.117.59.17/~astrogrid/xmlfiles/query.xml";
  String pathFileB = "http://143.117.59.17/~astrogrid/xmlfiles/registry.xquery";
  String pathFileC = "http://143.117.59.17/~astrogrid/xmlfiles/registry2.xml";
  String pathFileD = "http://143.117.59.17/~astrogrid/xmlfiles/service.xml";
  //these addres *must* change if the machine in which the test is carry out changes
  String pathFileE = "//home//pedro//gmd-ipsi//xmlfiles//serviceOutput.xml";
  String pathFileF = "//home//pedro//gmd-ipsi//xmlfiles//newFile.xml";

  public RegAdminActionTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    /**@todo verify the constructors*/
    regAdminAction = new RegAdminAction();
  }

  // include test here, basically in a method that pass two or three
  // variables depending of the problem.  Always the fine name is
  // passed, and then the node that want to be deleted, add or update.

  public void testDeleteNode() throws IOException, DOMParseException {
    // node to be deleted
    // first parameter xmlFile
    // second parameter new xmlFile
    // third Parameter node name to be deleted
    RegAdminAction.deleteNode(pathFileD, pathFileE, "curation");

  }

  public void testReplaceFile() throws IOException, DOMParseException {
    // The first parameter is the new file name, the second one is the old file name
    RegAdminAction.replaceFile(pathFileA,pathFileF);
  }


  public void testUpdateNode() {
    // node to be Updated
    //RegAdminAction("http://143.117.59.17/~astrogrid/xmlfiles/query.xml", "masters", "masters1");
  }

  public void testAddNode() {
    // node to be add
    //RegAdminAction.addNode("http://143.117.59.17/~astrogrid/xmlfiles/query.xml", "keyword","keyword","text inside keyword 1");
  }

}
