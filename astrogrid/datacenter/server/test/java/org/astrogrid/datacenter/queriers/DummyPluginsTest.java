/*
 * $Id: DummyPluginsTest.java,v 1.1 2004/03/12 04:54:06 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.queriers.sql.JdbcQuerier;
import org.astrogrid.datacenter.queriers.test.DummySqlPlugin;
import org.astrogrid.datacenter.queriers.test.PrecannedResults;
import org.xml.sax.SAXException;

/**
 * Tests the dummy querier and resultset.
 * also uses the dummy querier to test behaviours common to all queriers.
 * @author M Hill
 */

public class DummyPluginsTest extends TestCase
{
    /** Tests the precanned results.  Plugin test is done through QuerierTest */
    public void testPrecannedInitialises() throws IOException, SAXException, SQLException {

       //test the fixed example ones
       QueryResults results = new PrecannedResults("test");
       results.toVotable(null);

       //test the dummy sql ones
       DummySqlPlugin.populateDb();

       Connection connection = JdbcQuerier.createConnection();
       Statement s = connection.createStatement();
       s.execute("SELECT * FROM SampleStars WHERE Ra<100");
       assertNotNull(s.getResultSet());
    }
    

    /**
     * Assembles and returns a test suite made up of all the testXxxx() methods
      * of this class.
     */
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(DummyPluginsTest.class);
    }

    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }

}

