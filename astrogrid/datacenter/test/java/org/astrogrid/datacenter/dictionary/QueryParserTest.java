package org.astrogrid.datacenter.dictionary;

import java.io.StringReader;

import org.jdom.Document;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class QueryParserTest extends TestCase {
  private static final String QUERY_XML =
      "<query>" +      "  <ucd>ucd01</ucd>" +
      "  <ucd>ucd02</ucd>" +
      "  <ucd>ucd03</ucd>" +
      "</query>";
      
  private static final String DICTIONARY_XML = 
      "<?xml version=\"1.0\"?>" + 
      "<dictionary>" +
      "  <entry>" +
      "    <ucd>" +
      "      ucd01" +
      "    </ucd>" +
      "    <table>" +
      "      db-table01" +
      "    </table>" +
      "    <column>" +
      "      db-column01" +
      "    </column>" +
      "  </entry>" +
      "  <entry>" +
      "    <ucd>" +
      "      ucd02" +
      "    </ucd>" +
      "    <table>" +
      "      db-table02" +
      "    </table>" +
      "    <column>" +
      "      db-column02" +
      "    </column>" +
      "  </entry>" +
      "  <entry>" +
      "    <ucd>" +
      "      ucd03" +
      "    </ucd>" +
      "    <table>" +
      "      db-table03" +
      "    </table>" +
      "    <column>" +
      "      db-column03" +
      "    </column>" +
      "  </entry>" +
      "</dictionary>";

  /**
   * Constructor for QueryParserTest.
   * @param name
   */
  public QueryParserTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(QueryParserTest.class);
  }

  public void testParseReader() throws Exception {
    StringReader reader = new StringReader(QUERY_XML);

    try {
      Document document = QueryParser.parse(reader);
    
      assertNotNull("document is null", document);
    }
    catch(Exception e) {
      throw e;
    }
    finally {
      reader.close();
    }
  }

  public void testParseInputSource() throws Exception {
    StringReader reader = new StringReader(QUERY_XML);
    InputSource is = new InputSource(reader);
    try {
      Document document = QueryParser.parse(is);
    
      assertNotNull("document is null", document);
    }
    catch(Exception e) {
      throw e;
    }
    finally {
      reader.close();
    }
  }

  /*
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /*
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception {
    super.tearDown();
  }

}
