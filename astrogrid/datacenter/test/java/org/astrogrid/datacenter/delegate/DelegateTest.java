/*
 * $Id: DelegateTest.java,v 1.5 2003/09/15 17:00:12 mch Exp $
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
import java.net.UnknownHostException;
import java.net.URL;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.common.ServiceIdHelper;
import org.astrogrid.datacenter.common.ServiceStatus;
import org.astrogrid.datacenter.common.StatusHelper;
import org.astrogrid.datacenter.delegate.dummy.DummyDelegate;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DelegateTest extends TestCase implements DatacenterStatusListener
{
   Vector statusChangedList = new Vector();

   /**
    * Test created delegate types
    */
   public void testDelegateTypes() throws MalformedURLException, IOException, ServiceException
   {
      DummyDelegate dummy = (DummyDelegate) DatacenterDelegate.makeDelegate(null);

      //these will throw exceptions, but will at least test creation code
      try
      {
         SocketDelegate socket = (SocketDelegate) DatacenterDelegate.makeDelegate("socket://wibble:20");
      }
      catch (UnknownHostException se)   {  } //expect to not connect

      try
      {
         WebDelegate web = (WebDelegate) DatacenterDelegate.makeDelegate("http://wibble");
      }
      catch (IOException se)     { } // expect to not connect
   }

   /**
    * Creates a delegate, passes it a query and checks the return values
    */
   public void testBlockingQuery() throws ServiceException, MalformedURLException, SAXException, ParserConfigurationException, IOException, QueryException
   {
      DatacenterDelegate delegate = DatacenterDelegate.makeDelegate(null);

      delegate.registerStatusListener(this);

      //load test query file
      URL url = getClass().getResource("testQuery.xml");
      Element adqlQuery = XMLUtils.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      //'submit' query to dummy service for count results
      int count = delegate.adqlCountDatacenter(adqlQuery);

      //submit query for votable results
      Element results = delegate.adqlQuery(adqlQuery);

      //check results look ok
      assertEquals(ResponseHelper.DATACENTER_RESULTS_TAG, results.getNodeName());
      assertEquals(DummyDelegate.SERVICE_ID, ServiceIdHelper.getServiceId(results));
      assertEquals(ServiceStatus.FINISHED, StatusHelper.getServiceStatus(results));

      //check that some statuses are returned
      assertTrue("Status's not been returned", statusChangedList.size() != 0);

      //check the votable is valid
      assertTrue(results.getElementsByTagName("VOTABLE").getLength() >0);

      //errr some more

   }

   /**
    * test spawning query
    */
   public void testSpawnQuery() throws ServiceException, MalformedURLException, SAXException, ParserConfigurationException, IOException, QueryException
   {
      DatacenterDelegate delegate = DatacenterDelegate.makeDelegate(null);

      delegate.registerStatusListener(this);

      //load test query file
      URL url = getClass().getResource("testQuery.xml");
      Element adqlQuery = XMLUtils.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      //start query
      Element response = delegate.spawnAdqlQuery(adqlQuery);

      //check status
      String id = ServiceIdHelper.getServiceId(response);
      assertNotNull(id);

      response = delegate.getResults(id);



   }

   /**
    * test getting metadata
    */
   public void testMetadata() throws IOException, ServiceException, ParserConfigurationException, SAXException
   {
      DatacenterDelegate delegate = DatacenterDelegate.makeDelegate(null);

      Element voRegistry = delegate.getRegistryMetadata();
   }

   /** 'Callback' method called by Delegate when its status changes.  Stores
    * the status returned so that the tests above can examine them
    */
   public void datacenterStatusChanged(String id, String newStatus)
   {
      statusChangedList.add(newStatus);
   }



    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
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

