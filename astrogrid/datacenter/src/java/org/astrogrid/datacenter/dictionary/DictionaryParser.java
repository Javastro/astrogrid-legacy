package org.astrogrid.datacenter.dictionary;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.xml.sax.InputSource;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class DictionaryParser {
  public static Document parse(Reader reader) throws JDOMException {
    Document result = null;
    
    InputSource is = new InputSource(reader);
    result = DictionaryParser.parse(is);
    
    return result;
  }
  
  public static Document parse(InputSource is) throws JDOMException {
    Document result = null;
    SAXBuilder builder = new SAXBuilder();
    result = builder.build(is);
    
    return result;
  }
  
  public static Dictionary generateDictionary(Document document) {
    Dictionary result = new Dictionary();
    
    Element root = document.getRootElement();
    List entries = root.getChildren("entry");
    Iterator entryIt = entries.iterator();
    Element entry = null;
    while(entryIt.hasNext()) {
      entry = (Element) entryIt.next();
      DictionaryEntry dictEntry =
          new DictionaryEntry(
              entry.getChildTextNormalize("ucd"),
              entry.getChildTextNormalize("table"),
              entry.getChildTextNormalize("column"));
              
      result.put(dictEntry.getUcd(), dictEntry);
    }
    
    return result;
  }
}
