package org.astrogrid.contracts;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import junit.framework.TestCase;

/**
 * JUnit tests for SchemaMap.
 *
 * @author Guy Rixon
 */
public class SchemaMapTest extends TestCase {
  
  public void testAllEntriesPresent() throws Exception {
    Map m = SchemaMap.ALL;
    
    // The values in the schema map are the URLs leading to the schemata; the
    // keys are the namespace URIs. The URLs typically lead into the contracts
    // jar. For each key, check that there is a value that the value is a URL
    // and the the URL points to data. Don't bother reading the data.
    for (Iterator i = m.keySet().iterator(); i.hasNext(); ) {
      Object k = i.next();
      System.out.println("\n" + k);
      Object v = m.get(k);
      assertNotNull(v);
      System.out.println(v.getClass());
      assertTrue(v instanceof URL);
      URL u = (URL)v;
      u.openStream(); // throws exceptions if URL has no data
    }
  }
  
}
