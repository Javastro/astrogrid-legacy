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
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.axisdataserver.AxisDataServer;
import org.astrogrid.datacenter.axisdataserver.types.Query;
import org.astrogrid.datacenter.delegate.FullSearcher;
import org.astrogrid.datacenter.metadata.MetadataServer;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.spi.PluginQuerier;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.sitedebug.DummyQuerierSPI;
import org.astrogrid.util.DomHelper;
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
      SimpleConfig.setProperty(PluginQuerier.QUERIER_SPI_KEY, DummyQuerierSPI.class.getName());
      SimpleConfig.setProperty(QuerierManager.DATABASE_QUERIER_KEY, PluginQuerier.class.getName());

      //create the It4.1 server
      AxisDataServer server = new AxisDataServer_v0_4_1();
      assertNotNull(server);

      //load test query file
      URL url = getClass().getResource("adqlQuery3.xml");
      Document fileDoc = DomHelper.newDocument(url.openConnection().getInputStream());
      assertNotNull(fileDoc);

      Select adql = ADQLUtils.unmarshalSelect(fileDoc);
      Query q = new Query();
      q.setQueryBody(ADQLUtils.marshallSelect(adql).getDocumentElement());
      
      //submit query
      String result = server.doQuery(FullSearcher.VOTABLE,q);
      assertNotNull(result);
      assertIsVotableResultsResponse(result);
      
      
      
   }

   /**
    * Tests the data server as if this was an axis server
    */
   public void testGetMetadata() throws IOException
   {
      DataServer server = new DataServer();

      SimpleConfig.setProperty(MetadataServer.METADATA_FILE_LOC_KEY, "org/astrogrid/datacenter/service/metadata.xml");
      
      assertNotNull(MetadataServer.getMetadataUrl().openStream());
   }
    

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
Revision 1.10  2004/03/08 00:31:28  mch
Split out webservice implementations for versioning

Revision 1.9  2004/02/24 16:03:48  mch
Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

Revision 1.8  2004/02/16 23:07:05  mch
Moved DummyQueriers to std server and switched to AttomConfig

Revision 1.7  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.6.10.2  2004/01/08 09:43:40  nw
replaced adql front end with a generalized front end that accepts
a range of query languages (pass-thru sql at the moment)

Revision 1.6.10.1  2004/01/07 11:51:07  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.6  2003/12/01 20:58:42  mch
Abstracting coarse-grained plugin

Revision 1.5  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.4  2003/11/27 00:52:58  nw
refactored to introduce plugin-back end and translator maps.
interfaces in place. still broken code in places.

Revision 1.3  2003/11/25 14:21:49  mch
Extracted Querier from DatabaseQuerier in prep for FITS querying

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
