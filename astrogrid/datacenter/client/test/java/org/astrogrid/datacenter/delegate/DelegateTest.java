/*
 * $Id: DelegateTest.java,v 1.3 2003/11/17 15:40:51 mch Exp $
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
import java.net.UnknownHostException;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.datacenter.delegate.agss.SocketDelegate;
import org.astrogrid.datacenter.delegate.agws.WebDelegate;
import org.astrogrid.datacenter.delegate.dummy.DummyDelegate;
import org.astrogrid.datacenter.query.QueryException;
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
      DummyDelegate dummy = (DummyDelegate) DatacenterDelegateFactory.makeAdqlQuerier(null);
      dummy.setTimeout(200);

      //these will throw exceptions, but will at least test creation code
      try
      {
         SocketDelegate socket = (SocketDelegate) DatacenterDelegateFactory.makeAdqlQuerier("socket://wibble:20");
      }
      catch (UnknownHostException se)   {  } //expect to not connect

      try
      {
         WebDelegate web = (WebDelegate) DatacenterDelegateFactory.makeAdqlQuerier("http://wibble");
      }
      catch (IOException se)     { } // expect to not connect
   }

   /**
    * Creates a delegate, passes it a query and checks the return values
    */
   public void testBlockingQuery() throws ServiceException, MalformedURLException, SAXException, ParserConfigurationException, IOException, QueryException, ADQLException
   {
      AdqlQuerier delegate = DatacenterDelegateFactory.makeAdqlQuerier(null);

      //load test query file
      URL url = getClass().getResource("adqlQuery.xml");
      Element adqlQuery = XMLUtils.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      Select adql = ADQLUtils.unmarshalSelect(adqlQuery);
      
      //'submit' query to dummy service for count results
      int count = delegate.countQuery(adql);

      //submit query for votable results
      DatacenterResults results = delegate.doQuery(AdqlQuerier.VOTABLE, adql);

      checkResults(results);
   }

   /**
    * Private method used to check that the results document is valid
    * @todo
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
      AdqlQuerier delegate = DatacenterDelegateFactory.makeAdqlQuerier(null);

      //load test query file
      URL url = getClass().getResource("adqlQuery.xml");
      Element adqlQuery = XMLUtils.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      Select adql = ADQLUtils.unmarshalSelect(adqlQuery);
      
      //create query
      DatacenterQuery query = delegate.makeQuery(adql);
      assertNotNull(query);
      assertEquals(DummyDelegate.QUERY_ID, query.getId());

      query.registerListener(this);

      //check status
      QueryStatus status = query.getStatus();

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
      AdqlQuerier delegate = DatacenterDelegateFactory.makeAdqlQuerier(null);

      Metadata meta = delegate.getMetadata();
      
      assertNotNull(meta);
   }

   /** 'Callback' method called by Delegate when its status changes.  Stores
    * the status returned so that the tests above can examine them
    */
   public void delegateQueryChanged(DatacenterQuery query, QueryStatus newStatus)
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
 * Revision 1.3  2003/11/17 15:40:51  mch
 * Package movements
 *
 * Revision 1.2  2003/11/13 22:58:08  mch
 * Added Log to end
 *
 */


