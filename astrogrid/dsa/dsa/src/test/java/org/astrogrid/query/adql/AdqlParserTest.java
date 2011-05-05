package org.astrogrid.query.adql;

import org.astrogrid.adql.beans.SelectDocument;
import org.junit.Test;
import org.astrogrid.adql.AdqlParserSVNC;
import static org.junit.Assert.*;

/**
 * A basic test for the ADQL parser.
 * 
 * @author Guy Rixon
 */
public class AdqlParserTest {

  @Test
  public void smoke() throws Exception {
    AdqlParserSVNC sut = new AdqlParserSVNC();
    SelectDocument d = sut.parseToXML("SELECT * FROM table_a AS t");
    assertNotNull("Select document is null", d);
    assertTrue("Select document is invalid", d.validate());
  }

}
