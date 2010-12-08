package org.astrogrid.tableserver.out;

import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * JUnit-4 tests for {@link org.astrogrid.tableserver.out.VoTableWriter}.
 *
 * @author Guy Rixon
 */
public class VoTableWriterTest {

  @Test
  public void testFormatCell() throws Exception {
    VoTableWriter sut = new VoTableWriter(System.out, "Test", null);

    assertEquals("<TD>foo</TD>", sut.formatCell("foo"));
    assertEquals("<TD>42</TD>", sut.formatCell(new Integer(42)));
    assertEquals("<TD>123.456</TD>", sut.formatCell(new Double(123.456)));
    assertEquals("<TD></TD>", sut.formatCell(null));
    assertEquals("<TD>123456.5</TD>", sut.formatCell(new Date(123456500)));
    assertEquals("<TD>123456.501</TD>", sut.formatCell(new Date(123456501)));
    assertEquals("<TD>3 &gt; 2</TD>", sut.formatCell("3 > 2"));
    assertEquals("<TD>2 &lt; 3</TD>", sut.formatCell("2 < 3"));
    assertEquals("<TD>fish &amp; chips</TD>", sut.formatCell("fish & chips"));
  }


  @Test
  public void testUtype() throws Exception {
    StringWriter out = new StringWriter();
    VoTableWriter sut = new VoTableWriter(out, "Test", null);

    URL u = TableMetaDocInterpreter.class.getResource("metadocs/good_metadoc_1.2.xml");
    TableMetaDocInterpreter.clear();
    TableMetaDocInterpreter.initialize(u);

    // This column has a Utype in the metadoc.
    ColumnInfo c0 = TableMetaDocInterpreter.getColumnInfoByID("FIRST",
                                                              "catalogue1",
                                                              "POS_EQ_RA");
    assertEquals("Utype is wrong", "foo:bar.baz", c0.getUtype());

    // This writes an XML fragment to the buffer in "out".
    sut.startTable(new ColumnInfo[]{c0});
    out.close();
    System.out.println(out.toString());

    // The fragment should contain a utype attribute
    assertTrue(out.toString().contains("utype='foo:bar.baz' "));
  }

  @Test
  public void testNoUtype() throws Exception {
    StringWriter out = new StringWriter();
    VoTableWriter sut = new VoTableWriter(out, "Test", null);

    URL u = TableMetaDocInterpreter.class.getResource("metadocs/good_metadoc_1.2.xml");
    TableMetaDocInterpreter.clear();
    TableMetaDocInterpreter.initialize(u);

    // This column has no Utype in the metadoc.
    ColumnInfo c0 = TableMetaDocInterpreter.getColumnInfoByID("FIRST",
                                                              "catalogue1",
                                                              "POS_EQ_DEC");
    assertNull("Utype found where none expected", c0.getUtype());

    // This writes an XML fragment to the buffer in "out".
    sut.startTable(new ColumnInfo[]{c0});
    out.close();

    // The fragment should not contain any utype attribute.
    assertFalse(out.toString().contains("utype"));
  }

}
