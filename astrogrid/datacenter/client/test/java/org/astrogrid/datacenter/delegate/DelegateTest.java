/*
 * $Id: DelegateTest.java,v 1.10 2004/03/07 21:15:07 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.delegate;


/**
 * Tests DatacenterDelegate - or at least the dummy one
 *
 * @author M Hill
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.agws.WebDelegate;
import org.astrogrid.datacenter.delegate.dummy.DummyDelegate;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DelegateTest extends TestCase implements DelegateQueryListener
{
   Vector statusChangedList = new Vector();

   /**
    * Test created delegate types
    */
   public void testDelegateTypes() throws MalformedURLException, IOException, ServiceException
   {
      DummyDelegate dummy = (DummyDelegate) DatacenterDelegateFactory.makeFullSearcher(null);
      dummy.setTimeout(200);

      try
      {
         WebDelegate web = (WebDelegate) DatacenterDelegateFactory.makeFullSearcher("http://wibble");
      }
      catch (IOException se)     { } // expect to not connect
      
   }
   
   /**
    * Creates a delegate, passes it a query and checks the return values
    */
   public void testBlockingQuery() throws ServiceException, MalformedURLException, SAXException, ParserConfigurationException, IOException, QueryException, ADQLException
   {
      FullSearcher delegate = DatacenterDelegateFactory.makeFullSearcher(null);

      //load test query file
      URL url = getClass().getResource("adqlQuery.xml");
      Element adqlQuery = DomHelper.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      Select adql = ADQLUtils.unmarshalSelect(adqlQuery);
      
      //'submit' query to dummy service for count results
      int count = delegate.countQuery(ADQLUtils.toQueryBody(adql));

      //submit query for votable results
      DatacenterResults results = delegate.doQuery(FullSearcher.VOTABLE, ADQLUtils.toQueryBody(adql));

      checkResults(results);
   }

   /**
    * Private method used to check that the results document is valid
    * @todo provide impelemnation that does some tests.
    */
   private void checkResults(DatacenterResults results)
   {
      /**
      //check results look ok
      assertEquals(ResponseHelper.DATACENTER_RESULTS_TAG, results.getNodeName());
      assertEquals(DummyDelegate.QUERY_ID, QueryIdHelper.getQueryId(results));
      assertEquals(QueryStatus.FINISHED, StatusHelper.getServiceStatus(results));

      //check that some statuses are returned
//      assertTrue("Status's not been returned", statusChangedList.size() != 0);

      //check the votable is valid
      assertTrue(results.getElementsByTagName("VOTABLE").getLength() >0);

      //errr some more
       */
   }

   /**
    * test spawning query
    */
   public void testSpawnQuery() throws ServiceException, MalformedURLException, SAXException, ParserConfigurationException, IOException, QueryException, ADQLException
   {
      FullSearcher delegate = DatacenterDelegateFactory.makeFullSearcher(null);

      //load test query file
      URL url = getClass().getResource("adqlQuery.xml");
      Element adqlQuery = DomHelper.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      Select adql = ADQLUtils.unmarshalSelect(adqlQuery);
      
      //create query
      DatacenterQuery query = delegate.makeQuery(ADQLUtils.toQueryBody(adql));
      assertNotNull(query);
      assertEquals(DummyDelegate.QUERY_ID, query.getId());

      query.registerListener(this);

      //check status
      QueryState status = query.getStatus();

      //start query
      query.start();

      //check status
      status = query.getStatus();

      //get results
      DatacenterResults results = query.getResultsAndClose();
      checkResults(results);
   }

   /**
    * test getting metadata
    */
   public void testMetadata() throws IOException, ServiceException, ParserConfigurationException, SAXException
   {
      FullSearcher delegate = DatacenterDelegateFactory.makeFullSearcher(null);

      Metadata meta = delegate.getMetadata();
      
      assertNotNull(meta);
   }

   /** 'Callback' method called by Delegate when its status changes.  Stores
    * the status returned so that the tests above can examine them
    */
   public void delegateQueryChanged(DatacenterQuery query, QueryState newStatus)
   {
      statusChangedList.add(newStatus);
   }



   public static Test suite()
   {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(DelegateTest.class);
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
 * $Log: DelegateTest.java,v $
 * Revision 1.10  2004/03/07 21:15:07  mch
 * Changed apache XMLUtils to implementation-independent DomHelper
 *
 * Revision 1.9  2004/03/07 00:33:50  mch
 * Started to separate It4.1 interface from general server services
 *
 * Revision 1.8  2004/02/15 23:22:55  mch
 * Removed socket-based services
 *
 * Revision 1.7  2004/01/13 00:32:47  nw
 * Merged in branch providing
 * * sql pass-through
 * * replace Certification by User
 * * Rename _query as Query
 *
 * Revision 1.6.10.3  2004/01/08 09:42:26  nw
 * tidied imports
 *
 * Revision 1.6.10.2  2004/01/08 09:10:20  nw
 * replaced adql front end with a generalized front end that accepts
 * a range of query languages (pass-thru sql at the moment)
 *
 * Revision 1.6.10.1  2004/01/07 13:01:44  nw
 * removed Community object, now using User object from common
 *
 * Revision 1.6  2003/12/02 17:56:39  mch
 * Added sql pass through test
 *
 * Revision 1.5  2003/11/26 16:31:46  nw
 * altered transport to accept any query format.
 * moved back to axis from castor
 *
 * Revision 1.4  2003/11/21 17:30:19  nw
 * improved WSDL binding - passes more strongly-typed data
 *
 * Revision 1.3  2003/11/17 15:40:51  mch
 * Package movements
 *
 * Revision 1.2  2003/11/13 22:58:08  mch
 * Added Log to end
 *
 */


