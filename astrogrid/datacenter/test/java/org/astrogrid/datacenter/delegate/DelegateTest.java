/*
 * $Id: DelegateTest.java,v 1.4 2003/09/10 17:57:52 mch Exp $
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
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.common.ServiceIdHelper;
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class DelegateTest extends TestCase implements DatacenterStatusListener
{
   Vector statusChangedList = new Vector();

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

      //'submit' query to dummy service
      int count = delegate.adqlCountDatacenter(adqlQuery);

      Element votable = delegate.adqlQuery(adqlQuery);

      //check that some statuses are returned
      assertTrue("Status's not been returned", statusChangedList.size() != 0);

      //check the votable is valid
      //errr somehow

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
   public void datacenterStatusChanged(String newStatus)
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

