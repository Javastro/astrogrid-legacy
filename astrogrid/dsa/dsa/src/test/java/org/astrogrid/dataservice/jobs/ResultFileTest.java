package org.astrogrid.dataservice.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import org.astrogrid.config.SimpleConfig;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit-4 tests for {@link org.astrogrid.dataservice.jobs.ResultFile}.
 *
 * @author Guy Rixon
 */
public class ResultFileTest {

  /**
   * Determines the direcory where the files are written by the SUT.
   */
  @Before
  public void setCacheDirectory() throws Exception {
    SimpleConfig.setProperty("datacenter.cache.directory", "job-cache");
    new File("job-cache").mkdir();
  }

  @Test
  public void testMetadata() throws Exception {
    ResultFile sut = new ResultFile("j1");

    // Default MIME-type.
    assertEquals("text/plain", sut.getMimeType());

    // Imposed MIME-type
    sut.setMimeType("ooh/shiny");
    assertEquals("ooh/shiny", sut.getMimeType());
  }

  /**
   * Writes a string to a textual {@code ResultFile} and reads it back.
   */
  @Test
  public void testText() throws Exception {
    ResultFile sut = new ResultFile("j2");
    Writer w = sut.openWriter();
    w.write("j2\n");
    w.flush();
    BufferedReader r = new BufferedReader(new FileReader(sut));
    assertEquals("j2", r.readLine());
  }

  /**
   * Writes a bainary number to a {@code ResultFile} and reads it back.
   */
  @Test
  public void testBinary() throws Exception {
    ResultFile sut = new ResultFile("j3");
    ObjectOutputStream o = new ObjectOutputStream(sut.openOutputStream());
    o.writeLong(42);
    o.flush();
    ObjectInputStream i = new ObjectInputStream(new FileInputStream(sut));
    long x = i.readLong();
    assertEquals(42, x);
  }

}
