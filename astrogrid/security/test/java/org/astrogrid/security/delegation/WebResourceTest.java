package org.astrogrid.security.delegation;

import java.net.URI;
import junit.framework.TestCase;
import org.astrogrid.security.delegation.*;

/**
 *
 * @author Guy Rixon
 */
public class WebResourceTest extends TestCase {
  
   public void testGetSubordinateWebResource() throws Exception {
     WebResource w1 = new WebResource(new URI("http://foo.bar/baz"));
     WebResource w2 = w1.getSubordinateWebResource("bletch");
     System.out.println(w2.getUri());
   }
  
}
