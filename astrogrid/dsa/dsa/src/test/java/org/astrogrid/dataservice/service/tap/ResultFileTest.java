package org.astrogrid.dataservice.service.tap;

import java.io.File;
import junit.framework.TestCase;

/**
 * JUnit 3 tests for ResultsFile.
 *
 * @author Guy Rixon
 */
public class ResultFileTest extends TestCase {
  
  public void setUp() throws Exception {
    new File("/tmp/foo.vot").delete();
    new File("/tmp/foo.csv").delete();
    new File("/tmp/foo.html").delete();
  }
  
  public void tearDown() throws Exception {
    new File("/tmp/foo.vot").delete();
    new File("/tmp/foo.csv").delete();
    new File("/tmp/foo.html").delete();
  }
  
  public void testVotable() throws Exception {
    ResultFile sut1 = new ResultFile("foo", "VOTABLE");
    assertEquals("/tmp/foo.vot", sut1.getFile().getAbsolutePath());
    assertEquals("application/x-votable+xml", sut1.getMimeType());
    new File("/tmp/foo.vot").createNewFile();
    ResultFile sut2 = new ResultFile("foo");
    assertEquals("/tmp/foo.vot", sut2.getFile().getAbsolutePath());
    assertEquals("application/x-votable+xml", sut2.getMimeType());
  }
  
  public void testCsv() throws Exception {
    ResultFile sut1 = new ResultFile("foo", "CSV");
    assertEquals("/tmp/foo.csv", sut1.getFile().getAbsolutePath());
    assertEquals("text/csv", sut1.getMimeType());
    new File("/tmp/foo.csv").createNewFile();
    ResultFile sut2 = new ResultFile("foo");
    assertEquals("/tmp/foo.csv", sut2.getFile().getAbsolutePath());
    assertEquals("text/csv", sut2.getMimeType());
  }
  
  public void testHtml() throws Exception {
    ResultFile sut1 = new ResultFile("foo", "HTML");
    assertEquals("/tmp/foo.html", sut1.getFile().getAbsolutePath());
    assertEquals("text/html", sut1.getMimeType());
    new File("/tmp/foo.html").createNewFile();
    ResultFile sut2 = new ResultFile("foo");
    assertEquals("/tmp/foo.html", sut2.getFile().getAbsolutePath());
    assertEquals("text/html", sut2.getMimeType());
  }
  
}
