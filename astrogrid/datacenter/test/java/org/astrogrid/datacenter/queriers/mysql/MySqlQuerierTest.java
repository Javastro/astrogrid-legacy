/*
 * $Id: MySqlQuerierTest.java,v 1.3 2003/09/07 18:58:58 mch Exp $
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
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;
import org.astrogrid.datacenter.query.AdqlTags;
import org.astrogrid.datacenter.service.Workspace;
import org.astrogrid.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MySqlQuerierTest extends TestCase
{

   /** Test harness for running with local MySQL database server
    */
   public void testLocal() throws Exception
   {
      Workspace workspace = new Workspace("Test");

      //load test query file
      URL url = getClass().getResource("testQuery.xml");
      Document fileDoc = XMLUtils.newDocument(url.openConnection().getInputStream());

      //extract <query> node
      NodeList queryNodes = fileDoc.getDocumentElement().getElementsByTagName(AdqlTags.QUERY_ELEMENT);
      Element queryElement = (Element) queryNodes.item(0); //should only be 1

      assertNotNull(queryElement);

      //convert to query
     // Query query = new Query(queryElement);

      //make connection to database
      MySqlQuerier querier = new MySqlQuerier();

      //send query & store results
      QueryResults results = querier.queryDatabase(queryElement);

      //convert results to VOTable
      Document votable = results.toVotable(workspace);

      //check results
      //er somehow
   }

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(MySqlQuerierTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       Log.traceOn();

       //put driver into config file
       Configuration.setProperty(SqlQuerier.JDBC_DRIVERS_KEY, "org.gjt.mm.mysql.Driver");

       //register URL in config file
       Configuration.setProperty(SqlQuerier.JDBC_URL_KEY, "jdbc:mysql://localhost/Catalogue");

       junit.textui.TestRunner.run(suite());
    }

}

