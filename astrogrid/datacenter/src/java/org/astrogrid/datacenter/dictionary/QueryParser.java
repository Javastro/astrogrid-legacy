package org.astrogrid.datacenter.dictionary;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class QueryParser {
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
  
  public static String generateSql(Document document, Dictionary dictionary) {
    String result = null;
    
    Element root = document.getRootElement();
    List ucds = root.getChildren("ucd");
    Iterator ucdIt = ucds.iterator();
    Element ucd = null;
    String key = null;
    DictionaryEntry entry = null;
    while(ucdIt.hasNext()) {
      ucd = (Element) ucdIt.next();
      key = ucd.getTextNormalize();
      if(dictionary.containsKey(key)) {
        entry = (DictionaryEntry) dictionary.get(key);
      }
    }
    
    return result;
  }
  
}
