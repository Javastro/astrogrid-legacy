/*
 * $Id: TestMySqlQuerier.java,v 1.4 2003/08/29 15:20:55 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.mysql;


/**
 * TestMySqlQuerier.java
 *
 * @author M Hill
 */

import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.queriers.QuerierTest;
import org.astrogrid.datacenter.query.AdqlTags;
import org.astrogrid.datacenter.query.Query;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TestMySqlQuerier extends TestCase
{

   /** Test harness for running with local MySQL database server
    */
   public void testLocal() throws Exception
   {
      //load test query file
      URL url = getClass().getResource("testQuery.xml");
      Document fileDoc = XMLUtils.newDocument(url.openConnection().getInputStream());

      //extract <query> node
      NodeList queryNodes = fileDoc.getDocumentElement().getElementsByTagName(AdqlTags.QUERY_ELEMENT);
      Element queryElement = (Element) queryNodes.item(0); //should only be 1

      assertNotNull(queryElement);

      //convert to query
      Query query = new Query(queryElement);

      //make connection to database
      MySqlQuerier querier = new MySqlQuerier();

      //send query
      querier.queryDatabase(query);

   }

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(TestMySqlQuerier.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }

}

