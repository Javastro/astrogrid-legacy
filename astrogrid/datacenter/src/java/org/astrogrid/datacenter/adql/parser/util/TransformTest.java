package  org.astrogrid.datacenter.parser.util;

import java.io.*;
import java.net.*;

import junit.framework.*;

/**
 *
 * @author Pedro Contreras <mailto:p.contreras@qub.ac.uk><p>
 * @see School of Computer Science   <br>
 * The Queen's University of Belfast <br>
 * {@link http://www.cs.qub.ac.uk}
 * <p>
 * ASTROGRID Project {@link http://www.astrogrid.org}<br>
 * Data Centre Group
 *
 */

public class TransformTest
    extends TestCase {

  /**
   *
   * @param args String[]
   */
  public static void main(String args[]) {
    junit.textui.TestRunner.run(TransformTest.class);
  }

  public Transform transform = null;

  String pathFileA = "http://astrogrid.cs.qub.ac.uk/~pedro/code/sql/select.sql";
  //this line should change to point to a file in your local system
  String pathFileB = "D://temp//select4.sql";

  public TransformTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    transform = new Transform();
  }

  public void testSameFileEqual() throws MalformedURLException,
      FileNotFoundException, NullPointerException, MalformedURLException {
    String expectedA = Transform.URLFileToString(pathFileA);
    assertEquals(expectedA, Transform.URLFileToString(pathFileA));
    System.out.print(expectedA);
  }

  public void testSameFileEqualB() throws MalformedURLException,
      MalformedURLException, IOException {
    String expectedB = Transform.LocalFileToString(pathFileB);
    assertEquals(expectedB, Transform.LocalFileToString(pathFileB));
    System.out.print(expectedB);
  }

}
