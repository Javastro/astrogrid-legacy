/*
 * $Id: TestDelegate.java,v 1.1 2003/08/28 00:02:20 mch Exp $
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
import org.astrogrid.datacenter.query.QueryException;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class TestDelegate extends TestCase implements DatacenterStatusListener
{
   Vector statusChangedList = new Vector();

   public void testDelegates() throws ServiceException, MalformedURLException, SAXException, ParserConfigurationException, IOException, QueryException
   {
      DatacenterDelegate delegate = DatacenterDelegate.makeDelegate(null);

      delegate.registerStatusListener(this);

      //load test query file
      URL url = getClass().getResource("testQuery.xml");
      Element adqlQuery = XMLUtils.newDocument(url.openConnection().getInputStream()).getDocumentElement();

      //'submit' query to dummy service
      delegate.adqlCountDatacenter(adqlQuery);

      delegate.adqlQueryDatacenter(adqlQuery);

      //check that some statuses are returned
      assertTrue("Status's not been returned", statusChangedList.size() != 0);

   }

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
        return new TestSuite(TestDelegate.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
}

