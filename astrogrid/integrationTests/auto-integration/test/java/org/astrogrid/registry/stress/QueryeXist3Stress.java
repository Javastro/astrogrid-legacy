package org.astrogrid.registry.stress;

import org.astrogrid.integration.AbstractTestForIntegration;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import java.net.*;
import org.astrogrid.util.*;
import org.w3c.dom.*;
import java.io.*;

public class QueryeXist3Stress extends AbstractTestForIntegration {

    private static final String FULL_REGISTRY_URL = "http://hydra.star.le.ac.uk:8080/exist/servlet/db/astrogridv0_9";
    
  public QueryeXist3Stress(String arg0) {
      super(arg0);
  }


    public void testQuery3() throws Exception {
      Document queryDoc = null;
      String numberOfResourcesReturned = "25";
      String declarations = SimpleConfig.getProperty("declare.namespace.0_9");
      String xql = declarations + " //vr:Resource[vr:Identifier/vr:AuthorityID = 'CDS']";
      String query = "<query xmlns=\"http://exist.sourceforge.net/NS/exist\"" +
         " start=\"1\" max=\"" + numberOfResourcesReturned + "\">" +
         "<text><![CDATA[" + xql + "]]></text></query>";
      queryDoc = DomHelper.newDocument(query);
      System.out.println("the exist query to be posted = " + query);
      

      URL postURL = new URL(FULL_REGISTRY_URL);
      long beginQ = System.currentTimeMillis();
      HttpURLConnection huc = (HttpURLConnection)postURL.openConnection();
      huc.setRequestProperty("Content-Type", "text/xml");
      huc.setDoOutput(true);
      huc.setDoInput(true);
      huc.connect();
      DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
      DomHelper.DocumentToStream(queryDoc,dos);
      dos.flush();
      dos.close();
      System.out.println("Time to send query = " + (System.currentTimeMillis() - beginQ));
      Document resultDoc = DomHelper.newDocument(huc.getInputStream());
      System.out.println("Time for total transaction = " + (System.currentTimeMillis() - beginQ));    
      //System.out.println("the resultDoc in QueryDbservice = " + DomHelper.DocumentToString(resultDoc));
      long endQ = System.currentTimeMillis();
      //return resultDoc;
  }  

/* (non-Javadoc)
 * @see junit.framework.TestCase#setUp()
 */
protected void setUp() throws Exception {
   super.setUp();
}

}
