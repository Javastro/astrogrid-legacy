/*$Id: AxisDataService05QueryTest.java,v 1.1 2004/07/06 18:48:34 mch Exp $
 * Created on 05-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.service.v05;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.axis.AxisFault;
import org.apache.axis.types.URI;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.metadata.MetadataServer;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierListener;
import org.astrogrid.datacenter.queriers.sql.SqlPluginTest;
import org.astrogrid.datacenter.queriers.test.DummySqlPlugin;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.util.DomHelper;
import org.astrogrid.util.Workspace;
import org.w3c.dom.Document;

/** Exercises the It4.1 AxisDataServices interface
 */
public class AxisDataService05QueryTest extends ServerTestCase {

   protected AxisDataService_v05 server;

   protected AdqlQuery query1;
   protected AdqlQuery query2;
   protected AdqlQuery query3;
   
   public AxisDataService05QueryTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
       super.setUp();

       DummySqlPlugin.initConfig();
       DummySqlPlugin.populateDb();
       
       server = new AxisDataService_v05();
       
       query1 = new AdqlQuery(SqlPluginTest.class.getResourceAsStream("sample-adql0.5-1.xml"));

       query2 = new AdqlQuery(SqlPluginTest.class.getResourceAsStream("sample-adql0.5-2.xml"));
       
       query3 = new AdqlQuery(SqlPluginTest.class.getResourceAsStream("sample-adql0.5-3.xml"));
    }

    public void testDoOneShotAdql() throws Exception {
       
        String results = server.askAdqlQuery(query1.toAdqlString(), "VOTABLE");
        Document doc = DomHelper.newDocument(results);
        assertIsVotableResultsResponse(doc);
    }
    
    public void testGetMetadata() throws Exception {
       SimpleConfig.setProperty(MetadataServer.METADATA_URL_LOC_KEY, ""+this.getClass().getResource("metadata.xml"));
        String result = server.getMetadata();
        assertNotNull(result);
        Document doc = DomHelper.newDocument(result);
        assertIsMetadata(doc);
    }
    
    public void testAbort() throws Exception {
       
       String qid = server.submitAdqlQuery(query1.toAdqlString(), "astrogrid:store:null", "VOTABLE");
       assertNotNull(qid);
       server.abortQuery(qid);

       //should either be aborted or completed.
       String status = server.getQueryStatus(qid).getState();
       
       if (!status.equals(QueryState.ABORTED.toString()) && !status.equals(QueryState.FINISHED)) {
          fail("Query in state "+status+" after aborting");
       }
    }


    /**
     * Runs the test case.
     */
    public static void main(String args[])
    {
       junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(AxisDataService05QueryTest.class);
    }



}


/*
$Log: AxisDataService05QueryTest.java,v $
Revision 1.1  2004/07/06 18:48:34  mch
Series of unit test fixes


*/
