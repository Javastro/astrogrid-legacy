package org.astrogrid.dataservice.service.tap;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit-4 tests for {@link org.astrogrid.dataservice.service.tap.TapOutputFormat}.
 *
 * @author Guy Rixon
 */
public class TapOutputFormatTest {

  /**
   * Tests the parsing of names to MIME-types as specified in
   * IVOA document PR-TAP-1.0-20100225. Note that the TAP standard requires the
   * parsing of format names to be insensitive to case, while the DSA architecture
   * requires the MIME types to be output in lower case.
   */
  @Test
  public void testSupportedTapStandardValues() throws Exception {
    assertEquals("application/x-votable+xml", new TapOutputFormat("application/x-votable+xml").toString());
    assertEquals("application/x-votable+xml", new TapOutputFormat("Application/x-votable+XML").toString());
    assertEquals("application/x-votable+xml", new TapOutputFormat("APPLICATION/X-VOTABLE+XML").toString());

    // If the requested format is text/xml, keep that as the MIME type.
    assertEquals("text/xml", new TapOutputFormat("text/xml").toString());
    assertEquals("text/xml", new TapOutputFormat("text/XML").toString());
    assertEquals("text/xml", new TapOutputFormat("TEXT/XML").toString());

    // If the requested format is the alias votable, use the more-explicit MIME type.
    assertEquals("application/x-votable+xml", new TapOutputFormat("votable").toString());
    assertEquals("application/x-votable+xml", new TapOutputFormat("VOTable").toString());
    assertEquals("application/x-votable+xml", new TapOutputFormat("VOTABLE").toString());

    assertEquals("text/csv", new TapOutputFormat("text/csv").toString());
    assertEquals("text/csv", new TapOutputFormat("text/CSV").toString());
    assertEquals("text/csv", new TapOutputFormat("TEXT/CSV").toString());

    assertEquals("text/csv", new TapOutputFormat("csv").toString());
    assertEquals("text/csv", new TapOutputFormat("Csv").toString());
    assertEquals("text/csv", new TapOutputFormat("CSV").toString());

    assertEquals("text/tab-separated-values", new TapOutputFormat("text/tab-separated-values").toString());
    assertEquals("text/tab-separated-values", new TapOutputFormat("text/Tab-Separated-Values").toString());
    assertEquals("text/tab-separated-values", new TapOutputFormat("TEXT/TAB-SEPARATED-VALUES").toString());

    assertEquals("text/tab-separated-values", new TapOutputFormat("tsv").toString());
    assertEquals("text/tab-separated-values", new TapOutputFormat("Tsv").toString());
    assertEquals("text/tab-separated-values", new TapOutputFormat("TSV").toString());

    assertEquals("text/html", new TapOutputFormat("text/html").toString());
    assertEquals("text/html", new TapOutputFormat("text/HTML").toString());
    assertEquals("text/html", new TapOutputFormat("TEXT/HTML").toString());

    assertEquals("text/html", new TapOutputFormat("html").toString());
    assertEquals("text/html", new TapOutputFormat("Html").toString());
    assertEquals("text/html", new TapOutputFormat("HTML").toString());
  }

  /**
   * Tests the handling of leading and trailing white space, which should be
   * ignored under DSA parsing-rules.
   */
  @Test
  public void testWhiteSpace() throws Exception {
    assertEquals("text/html", new TapOutputFormat(" text/html").toString());
    assertEquals("text/html", new TapOutputFormat("text/html ").toString());
  }

  /**
   * Tests the name-parsing of an unsupported format.
   */
  @Test(expected=TapException.class)
  public void testApplicationFits() throws Exception {
    new TapOutputFormat("application/fits");
  }


  /**
   * Tests the name-parsing of an unsupported format.
   */
  @Test(expected=TapException.class)
  public void testFits() throws Exception {
    new TapOutputFormat("fits");
  }

  /**
   * Tests the name-parsing of an unsupported format.
   */
  @Test(expected=TapException.class)
  public void testTextPlain() throws Exception {
    new TapOutputFormat("text/plain");
  }

  /**
   * Tests the name-parsing of an unsupported format.
   */
  @Test(expected=TapException.class)
  public void testText() throws Exception {
    new TapOutputFormat("test");
  }

  /**
   * Tests the name-parsing of an uknown format.
   */
  @Test(expected=TapException.class)
  public void testBogus() throws Exception {
    new TapOutputFormat("bogus");
  }

  /**
   * Tests the name-parsing of a null format-string.
   * The default for DSA is the VOTable MIME-type.
   */
  @Test
  public void testNull() throws Exception {
    assertEquals("application/x-votable+xml", new TapOutputFormat(null).toString());
  }

  /**
   * Tests the name-parsing of a blank format-string.
   */
  @Test(expected=TapException.class)
  public void testBlank() throws Exception {
    new TapOutputFormat("    ");
  }

  /**
   * Tests the name-parsing of a zero-length format-string.
   */
  @Test(expected=TapException.class)
  public void testZeroLength() throws Exception {
    new TapOutputFormat("");
  }

}
