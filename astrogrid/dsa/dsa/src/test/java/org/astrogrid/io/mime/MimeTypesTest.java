package org.astrogrid.io.mime;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit-4 tests for {@link org.astrogrid.io.mime.MimeTypes}.
 *
 * @author Guy Rixon
 */
public class MimeTypesTest {

  @Test
  public void testVotableAlias() throws Exception {
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"", MimeTypes.toMimeType("votable"));
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"", MimeTypes.toMimeType("VOTable"));
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"", MimeTypes.toMimeType("VOTABLE"));
  }

  @Test
  public void testVotableTabledata() throws Exception {
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"",
                 MimeTypes.toMimeType("application/x-votable+xml; encoding=\"tabledata\""));
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"",
                 MimeTypes.toMimeType("application/x-votable+xml; encoding=\"TABLEDATA\""));
    
  }

  @Test
  public void testTextXml() throws Exception {
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"", MimeTypes.toMimeType("text/xml"));
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"", MimeTypes.toMimeType("text/XML"));
  }

  @Test
  public void testVotableBinary() throws Exception {
    assertEquals("application/x-votable+xml; encoding=\"binary\"",
                 MimeTypes.toMimeType("application/x-votable+xml; encoding=\"binary\""));
    assertEquals("application/x-votable+xml; encoding=\"binary\"",
                 MimeTypes.toMimeType("application/x-votable+xml; encoding=\"BINARY\""));
    assertEquals("application/x-votable+xml; encoding=\"binary\"",
                 MimeTypes.toMimeType("application/x-votable+XML; encoding=\"binary\""));
  }

  @Test
  public void testCsv() throws Exception {
    assertEquals("text/csv", MimeTypes.toMimeType("csv"));
    assertEquals("text/csv", MimeTypes.toMimeType("CSV"));
    assertEquals("text/csv", MimeTypes.toMimeType("text/csv"));
    assertEquals("text/csv", MimeTypes.toMimeType("text/CSV"));
    assertEquals("text/csv", MimeTypes.toMimeType("Text/CsV"));
  }

  @Test
  public void testTsv() throws Exception {
    assertEquals("text/tab-separated-values", MimeTypes.toMimeType("tsv"));
    assertEquals("text/tab-separated-values", MimeTypes.toMimeType("TSV"));
    assertEquals("text/tab-separated-values", MimeTypes.toMimeType("Tsv"));
    assertEquals("text/tab-separated-values", MimeTypes.toMimeType("text/tab-separated-values"));
  }

  @Test
  public void testDefault() throws Exception {
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"", MimeTypes.toMimeType(""));
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"", MimeTypes.toMimeType(null));
  }

  @Test
  public void testTrimming() throws Exception {
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"", MimeTypes.toMimeType("  votable"));
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"", MimeTypes.toMimeType("votable  "));
    assertEquals("application/x-votable+xml; encoding=\"tabledata\"", MimeTypes.toMimeType("votable "));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGarbage() throws Exception {
    MimeTypes.toMimeType("wibble");
  }

}
