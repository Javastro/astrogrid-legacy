
package org.astrogrid.registry.xquery;

import junit.framework.*;

public class JUnitTestTransform extends TestCase {

  public static void main(String args[]) {
	junit.textui.TestRunner.run(JUnitTestTransform.class);
  }

  public Transform transform = null;

  String pathFileA = "http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/xquery/query.xml";
  String pathFileB = "http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/xquery/registry.xquery";
  String pathFileC = "http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/xquery/registry2.xml";
  String pathFileD = "http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/xquery/registry2.xml";
  String pathFileE = "http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/xquery/registry.xml";
  String pathFileF = "http://msslxy.mssl.ucl.ac.uk:8080/org/astrogrid/registry/xquery/query.xml";

  String StringFileB = "//registry/service/contact";

  public JUnitTestTransform(String name) {
	  super(name);
  }

  protected void setUp() throws Exception {
	super.setUp();
	transform = new Transform();
  }

  public void testSameFileEqual() {
	String expectedA = Transform.fileToString(pathFileA);
	assertEquals(expectedA, Transform.fileToString(pathFileA));
  }

  public void testTwoFilesEqual() {
	String expectedA = Transform.fileToString(pathFileA);
	String expectedF = Transform.fileToString(pathFileF);
	assertEquals(expectedA, expectedF);
  }

  public void testFileAndStringEqual() {
	String expectedA = Transform.fileToString(pathFileB);
	assertEquals(expectedA, StringFileB);
  }

  public void testTwoXMLFilesEqual() {
	String expectedC = Transform.fileToString(pathFileC);
	String expectedD = Transform.fileToString(pathFileD);
	assertEquals(expectedC, expectedD);
  }


}