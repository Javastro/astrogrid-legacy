/*
 * $Id: HomeQuerierTester.java,v 1.3 2003/09/24 21:04:52 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.mch;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.DatabaseQuerierManager;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.service.AxisDataServer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * This is a test suite for running the service code against the environment
 * on my home machine
 *
 * @author M Hill
 */


public class HomeQuerierTester extends TestCase
{

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
      Configuration.setProperty(DatabaseQuerierManager.DATABASE_QUERIER_KEY, org.astrogrid.datacenter.queriers.mysql.MySqlQuerier.class.getName());

      //create the server
      AxisDataServer server = new AxisDataServer();

      //load test query file
      URL url = getClass().getResource("testQuery.xml");
      Document fileDoc = XMLUtils.newDocument(url.openConnection().getInputStream());

      //submit query
      Element votable = server.doQuery(fileDoc.getDocumentElement());
   }

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(HomeQuerierTester.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }

}

