/*
 * $Id TestWorkspace.java $
 *
 */

package org.astrogrid.datacenter.service;


/**
 * Unit tests for the Server
 *
 * @author M Hill
 */

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerierManager;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ServerTest extends ServerTestCase
{
    public ServerTest(String name) {
        super(name);
    }

   /**
    * Tests the query service by itself
    */
   public void testQueryService() throws IOException, QueryException,
                                          DatabaseAccessException,
                                          SAXException, ParserConfigurationException,
                                          Throwable
   {
      //make sure database querier to be used is the dummy one - only available
      //in the test suite
      SimpleConfig.setProperty(DatabaseQuerierManager.DATABASE_QUERIER_KEY, "org.astrogrid.datacenter.queriers.DummyQuerier");

      //create the server
      AxisDataServer server = new AxisDataServer();
      assertNotNull(server);

      //load test query file
      URL url = getClass().getResource("adqlQuery3.xml");
      Document fileDoc = XMLUtils.newDocument(url.openConnection().getInputStream());
      assertNotNull(fileDoc);

      Select adql = ADQLUtils.unmarshalSelect(fileDoc);
      Query q = new Query();
      q.setSelect(adql);
      
      //submit query
      String result = server.doQuery(AdqlQuerier.VOTABLE,q);
      assertNotNull(result);  
      assertIsResultsResponse(result);
      
      
      
   }

   /**
    * Tests the data server as if this was an axis server
    *
   public void testDataServer()
   {
   }
    */

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(ServerTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }


}

/*
$Log: ServerTest.java,v $
Revision 1.2  2003/11/21 17:37:56  nw
made a start tidying up the server.
reduced the number of failing tests
found commented out code

Revision 1.1  2003/11/14 00:38:29  mch
Code restructure

Revision 1.8  2003/11/06 22:10:22  mch
Work with adql not it02 query

Revision 1.7  2003/11/05 18:54:43  mch
Build fixes for change to SOAPy Beans and new delegates

Revision 1.6  2003/09/24 21:12:35  nw
updated to refer to manager

Revision 1.5  2003/09/17 14:53:02  nw
tidied imports

Revision 1.4  2003/09/10 12:16:44  mch
Changes to make web interface more consistent

Revision 1.3  2003/09/07 18:58:58  mch
Updated tests for weekends changes to main code (mostly threaded queries, typesafe ServiceStatus)

Revision 1.2  2003/09/05 01:02:33  nw
minor changes

Revision 1.1  2003/08/29 15:27:20  mch
Renamed TestXxxx to XxxxxTest so Maven runs them

Revision 1.2  2003/08/28 17:29:25  mch
Added test based on dummyQuerier

Revision 1.1  2003/08/28 13:21:20  mch
Added service test


*/
