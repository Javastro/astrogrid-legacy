package org.astrogrid.datacenter.dictionary;

import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;

import org.jdom.Document;
import org.xml.sax.InputSource;

import junit.framework.TestCase;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class DictionaryParserTest extends TestCase {
  private static final String XML = 
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
   * Constructor for DictionaryParserTest.
   * @param name
   */
  public DictionaryParserTest(String name) {
    super(name);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(DictionaryParserTest.class);
  }

  public void testParseReader() throws Exception {
    StringReader reader = new StringReader(XML);

    try {
      Document document = DictionaryParser.parse(reader);
    
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
    StringReader reader = new StringReader(XML);
    InputSource is = new InputSource(reader);
    try {
      Document document = DictionaryParser.parse(is);
    
      assertNotNull("document is null", document);
    }
    catch(Exception e) {
      throw e;
    }
    finally {
      reader.close();
    }
  }

  public void testGenerateMap() throws Exception {
    StringReader reader = new StringReader(XML);

    try {
      Document document = DictionaryParser.parse(reader);
      Dictionary dictionary = DictionaryParser.generateDictionary(document);
    
      assertNotNull("document is null", document);
      assertNotNull("dictionary is null", dictionary);
      assertEquals("dictionary size wrong", 3, dictionary.size());
      
      verifyDictionary(dictionary);
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
  
  private void verifyDictionary(Dictionary dictionary) throws DictionaryEntry.DictionaryEntryException {
    Collection entries = dictionary.values();
    Iterator entryIt = entries.iterator();
    DictionaryEntry entry = null;
    while(entryIt.hasNext()) {
      entry = (DictionaryEntry) entryIt.next();
      entry.verify();
      System.out.println(entry);
    }
  }

}
