/*$Id: VizierQuerierTest.java,v 1.3 2004/10/07 10:48:20 mch Exp $
 * Created on 01-Dec-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.deployment.vizier;

import java.io.StringWriter;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.impl.cds.VizierQuerierPlugin;
import org.astrogrid.datacenter.impl.cds.VizierResourcePlugin;
import org.astrogrid.datacenter.integration.DatacenterTestCase;
import org.astrogrid.datacenter.metadata.VoResourcePlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierManager;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.TargetIndicator;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Dec-2003
 *
 */
public class VizierQuerierTest extends DatacenterTestCase {

//    protected QuerierSPI spi;
    protected VizierQuerierPlugin spi;

    /**
     * Constructor for VizierQuerierTest.
     */
    public VizierQuerierTest() {
        super();
    }

    public static void main(String[] args) {
        System.out.println("hello.");
        junit.textui.TestRunner.run(VizierQuerierTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();


    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }


   /**
    *  Perform a simple cone-search of a specified Vizier catalogue.
    */

    public void testDoQuery() throws Exception
    {  SimpleConfig.setProperty(QuerierPluginFactory.PLUGIN_KEY,
          VizierQuerierPlugin.class.getName());

//
//    Specify which Vizier catalogue to query.  The value chosen
//    corresponds to Dixon's Master List of Non-stellar Objects and is
//    the catalogue used in the examples from CDS.

       SimpleConfig.setProperty(VizierQuerierPlugin.CATALOGUE_NAME,
         "Dixon");


//
//    Generate a QuerierManager, generate the query and perform it.

       QuerierManager manager = QuerierManager.getManager("DummyTest");

       StringWriter sw = new StringWriter();

//
//    The values for the cone query correspond to the RA, Dec of M31,
//    which is the object used in the examples from CDS.  All the values
//    are in decimal degrees.  The search radius is equivalent to
//    10 arcmin.

       Querier q = Querier.makeQuerier(Account.ANONYMOUS,
         SimpleQueryMaker.makeConeQuery(10.684620, 41.269278, 0.166667,
         TargetIndicator.makeIndicator(sw),
         ReturnTable.VOTABLE));

       manager.askQuerier(q);

       Document results = DomHelper.newDocument(sw.toString());

//
//    The ideal test would be:
//
//       assertIsVotable(results);
//
//    However, this does not work becuase Vizier returns a document
//    conforming to the VOtable _schema_ whereas the assertion uses
//    the VOtable DTD.  the following tests are used as a substitute.

       assertNotNull(results);
       Element rootElement = results.getDocumentElement();
//       System.out.println("rootElement: " + rootElement.getTagName() );
       assertEquals(rootElement.getTagName(), "VOTABLE");
    }


   /**
    *  Perform a simple cone-search of a specified Vizier catalogue,
    *  return the metadata rather than executing a genuine query.
    */

    public void testDoMetaDataQuery() throws Exception
    {  SimpleConfig.setProperty(QuerierPluginFactory.PLUGIN_KEY,
          VizierQuerierPlugin.class.getName());

//
//    Specify which Vizier catalogue to query.  The value chosen
//    corresponds to Dixon's Master List of Non-stellar Objects and is
//    the catalogue used in the examples from CDS.

       SimpleConfig.setProperty(VizierQuerierPlugin.CATALOGUE_NAME,
         "Dixon");

       /*
//    The values for the cone query correspond to the RA, Dec of M31,
//    which is the object used in the examples from CDS.  All the values
//    are in decimal degrees.  The search radius is equivalent to
//    10 arcmin.

       Querier q = Querier.makeQuerier(Account.ANONYMOUS,
         SimpleQueryMaker.makeConeQuery(10.684620, 41.269278, 0.166667),
         TargetIndicator.makeIndicator(new StringWriter()),
         ReturnTable.VOTABLE);
        */
      VoResourcePlugin resourcePlugin = new VizierResourcePlugin();
          
       String[] resources = resourcePlugin.getVoResources();

//
//    The ideal test would be:
//
//       assertIsVotable(results);
//
//    However, this does not work becuase Vizier returns a document
//    conforming to the VOtable _schema_ whereas the assertion uses
//    the VOtable DTD.  the following tests are used as a substitute.

       assertNotNull(resources);
//       Element rootElement = results.getDocumentElement();
//       System.out.println("rootElement: " + rootElement.getTagName() );
//       assertEquals(rootElement.getTagName(), "VOTABLE");

    }
}


/*
$Log: VizierQuerierTest.java,v $
Revision 1.3  2004/10/07 10:48:20  mch
Fixes to EGSO tests

Revision 1.2  2004/10/06 22:03:45  mch
Following Query model changes in PAL

Revision 1.1  2004/10/05 16:45:28  mch
Moved proxy tests to integration tests from unit tests

Revision 1.9  2004/10/04 11:31:42  mch
removed test that breaks compile

Revision 1.8  2004/10/01 18:04:58  mch
Some factoring out of status stuff, added monitor page

Revision 1.7  2004/09/29 18:45:55  mch
Bringing Vizier into line with new(er) metadata stuff

Revision 1.6  2004/09/02 12:44:35  mch
Fixed FORMAT_VOTABLEs

Revision 1.5  2004/08/27 14:53:20  KevinBenson
bug in compile not referencing the new location of TargetIndicator

Revision 1.4  2004/08/17 20:19:36  mch
Moved TargetIndicator to client

Revision 1.3  2004/08/14 14:38:30  acd
Test for the revised Vizier Proxy cone search.

Revision 1.4  2004/08/13 17:27:00  acd
Ensured that all the necessary properties were set programmatically
and added a test to perform a metadata query.

Revision 1.3  2004/08/12 17:25:00  acd
Refactored, as they say, to execute test correctly.

Revision 1.2  2004/03/16 01:32:35  mch
Fixed for cahnges to code to work with new plugins

Revision 1.1  2003/12/01 16:51:04  nw
added tests for cds spi
 
*/
