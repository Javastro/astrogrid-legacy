package org.astrogrid.registry.xquery;


import junit.framework.*;
import java.net.MalformedURLException;

public class JUnitTestTransform extends TestCase {

  public static void main(String args[]) {
	junit.textui.TestRunner.run(JUnitTestTransform.class);
  }

  public Transform transform = null;

  String pathFileA = "http://143.117.59.17/~astrogrid/xmlfiles/query.xml";
  String pathFileB = "http://143.117.59.17/~astrogrid/xmlfiles/registry.xquery";
  String pathFileC = "http://143.117.59.17/~astrogrid/xmlfiles/registry2.xml";
  String pathFileD = "http://143.117.59.17/~astrogrid/xmlfiles/registry2.xml";
  String pathFileE = "http://143.117.59.17/~astrogrid/xmlfiles/registry.xml";
  String pathFileF = "http://143.117.59.17/~astrogrid/xmlfiles/query.xml";

  String StringFileB = "//registry/service/contact";

  public JUnitTestTransform(String name) {
	  super(name);
  }

  protected void setUp() throws Exception {
	super.setUp();
	transform = new Transform();
  }

  public void testSameFileEqual() throws MalformedURLException {
	String expectedA = Transform.URLFileToString(pathFileA);
	assertEquals(expectedA, Transform.URLFileToString(pathFileA));
  }

  public void testTwoFilesEqual() throws MalformedURLException {
	String expectedA = Transform.URLFileToString(pathFileA);
	String expectedF = Transform.URLFileToString(pathFileF);
	assertEquals(expectedA, expectedF);
  }

  public void testFileAndStringEqual() throws MalformedURLException {
	String expectedA = Transform.URLFileToString(pathFileB);
	assertEquals(expectedA, StringFileB);
  }

  public void testTwoXMLFilesEqual() throws MalformedURLException {
	String expectedC = Transform.URLFileToString(pathFileC);
	String expectedD = Transform.URLFileToString(pathFileD);
	assertEquals(expectedC, expectedD);
  }
}