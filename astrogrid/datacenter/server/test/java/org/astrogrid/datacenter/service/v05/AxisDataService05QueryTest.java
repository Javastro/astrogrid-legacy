/*$Id: AxisDataService05QueryTest.java,v 1.8 2004/09/08 16:33:06 mch Exp $
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

import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.ServerTestCase;
import org.astrogrid.datacenter.metadata.FileResourcePlugin;
import org.astrogrid.datacenter.queriers.sql.SqlPluginTest;
import org.astrogrid.datacenter.queriers.test.SampleStarsPlugin;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.QueryState;
import org.astrogrid.util.DomHelper;
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

       SampleStarsPlugin.initConfig();
       
       server = new AxisDataService_v05();
       
       query1 = new AdqlQuery(SqlPluginTest.class.getResourceAsStream("sample-adql0.7.4-1.xml"));

       query2 = new AdqlQuery(SqlPluginTest.class.getResourceAsStream("sample-adql0.7.4-3.xml"));
       
//       query3 = new AdqlQuery(SqlPluginTest.class.getResourceAsStream("sample-adql0.7.4-3.xml"));
    }

    public void testDoOneShotAdql() throws Exception {
       
        String results = server.askAdqlQuery(query1.toAdqlString(), "VOTABLE");
        Document doc = DomHelper.newDocument(results);
        assertIsVotableResultsResponse(doc);
    }
    
    public void testGetMetadata() throws Exception {
       SimpleConfig.setProperty(FileResourcePlugin.METADATA_URL_LOC_KEY, ""+this.getClass().getResource("metadata.xml"));
        String result = server.getMetadata();
        assertNotNull(result);
        Document doc = DomHelper.newDocument(result);
        assertIsMetadata(doc);
    }
    
    public void testAbort() throws Exception {
       
       String qid = server.submitAdqlQuery(query1.toAdqlString(), "mailto:mch@roe.ac.uk", "VOTABLE");
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
Revision 1.8  2004/09/08 16:33:06  mch
Added SampleStars init

Revision 1.7  2004/09/08 16:12:32  mch
Fix tests to use ADQL 0.7.4

Revision 1.6  2004/09/06 20:23:00  mch
Replaced metadata generators/servers with plugin mechanism. Added Authority plugin

Revision 1.5  2004/09/01 13:19:54  mch
Added sample stars metadata

Revision 1.4  2004/08/18 22:30:04  mch
Improved some tests

Revision 1.3  2004/08/18 18:44:12  mch
Created metadata plugin service and added helper methods

Revision 1.2  2004/08/02 14:59:43  mch
Fix to have valid Agsl

Revision 1.1  2004/07/06 18:48:34  mch
Series of unit test fixes


*/
