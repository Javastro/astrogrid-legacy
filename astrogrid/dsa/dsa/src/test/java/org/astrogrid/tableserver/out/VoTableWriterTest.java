package org.astrogrid.tableserver.out;

import java.util.Date;
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
}
