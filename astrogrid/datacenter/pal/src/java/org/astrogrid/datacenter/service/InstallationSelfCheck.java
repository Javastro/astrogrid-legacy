/*$Id: InstallationSelfCheck.java,v 1.16 2004/11/03 00:17:56 mch Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.service;



import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.net.URL;
import junit.framework.TestCase;
import net.ivoa.SkyNode.SkyNodeLocator;
import net.ivoa.SkyNode.SkyNodeSoap;
import net.ivoa.SkyNode.VOData;
import net.ivoa.www.xml.ADQL.v0_7_4.SelectType;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.QuerierPluginFactory;
import org.astrogrid.datacenter.queriers.sql.SqlMaker;
import org.astrogrid.datacenter.query.AdqlQueryMaker;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.datacenter.service.v05.AxisDataService_v05;
import org.astrogrid.slinger.NullWriter;
import org.astrogrid.slinger.TargetMaker;

/** Unit test for checking an installation - checks location of config files, etc.
 * <p>
 * not intended for use during development - hence the different naming convention.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 * (MCH Dec ratpak - removed 'fails' so that original exceptions propogate nicely
 * to nice web self-test page)
 */
public class InstallationSelfCheck extends TestCase {
   
   /**
    * Constructor for InstallationSelfCheck.
    * @param arg0
    */
   public InstallationSelfCheck(String arg0) {
      super(arg0);
   }
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(InstallationSelfCheck.class);
   }
   
   /** Checks we can create the various interfaces */
   public void testInstantiateServer() throws IOException {
      new AxisDataService_v05();
      //new SkyNode();
   }
   
   /** Checks the characteristics of the plugin */
   public void testPluginDefinition() throws Exception {
      String pluginClass = SimpleConfig.getSingleton().getString(QuerierPluginFactory.QUERIER_PLUGIN_KEY);
      assertNotNull(QuerierPluginFactory.QUERIER_PLUGIN_KEY + " is not defined",pluginClass);
      // try to load plugin class.
      Class plugin = null;
//      try {
      plugin = Class.forName(pluginClass);
      assertNotNull(QuerierPluginFactory.QUERIER_PLUGIN_KEY + " could not be found",plugin);
      // check its type
      assertTrue(QuerierPluginFactory.QUERIER_PLUGIN_KEY + " does not extend QuerierPlugin",QuerierPlugin.class.isAssignableFrom(plugin));
      // we expect a contructor as follows
      Constructor constr = plugin.getConstructor(new Class[]{  });
      assertNotNull("Plugin class must provide constructor(String,Query)",constr);
   }

   /** Ask a query to get all of the database using ADQL. Should fail on all but the sample dbs... */
   public void testBigAdqlSearch() throws Throwable
   {
      DataServer server = new DataServer();
      String adql = "<?xml version='1.0' encoding='utf-8'?>"+
                     "<Select xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns='http://www.ivoa.net/xml/ADQL/v0.7.4'>"+
                     "  <SelectionList> <Item xsi:type='allSelectionItemType' /> </SelectionList>"+
                     "  <From> <Table xsi:type='tableType' Name='"+SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_TABLE_KEY)+"' Alias='s' />  </From>"+
                     "</Select>";

      server.askQuery(Account.ANONYMOUS, AdqlQueryMaker.makeQuery(adql, TargetMaker.makeIndicator(new NullWriter()), ReturnTable.CSV));
   }

   /** Checks the querier/plugin operates - runs a cone query that will exercise it - so
    * this will also test the connection to the backend database. */
   public void testConeSearch() throws Throwable {
      StringWriter sw = new StringWriter(); //although we throw away the results
      DataServer server = new DataServer();
      server.askQuery(Account.ANONYMOUS,
                      SimpleQueryMaker.makeConeQuery(30,30,2, TargetMaker.makeIndicator(sw), ReturnTable.VOTABLE));
   }

   /** Checks that the delegates can connect correctly */
   public void testSoapDelegate() throws Throwable {
      
      //this test is called as a servlet, so get url stem from servlet context
      String endpoint = ServletHelper.getUrlStem()+"/services/AxisDataService05";
      
      ConeSearcher searcher = DatacenterDelegateFactory.makeConeSearcher(Account.ANONYMOUS,
                                                                         endpoint,
                                                                         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      InputStream is = searcher.coneSearch(30, 30, 6);

      assertNotNull(is);
   }
   
   /** Checks that the SkyNode interface works OK */
   public void testSkyNode() throws Throwable {
      
      //this test is called as a servlet, so get url stem from servlet context
      String endpoint = ServletHelper.getUrlStem()+"/services/SkyNode074";

      //construct SOAP client
      SkyNodeSoap skyNodeClient = new SkyNodeLocator().getSkyNodeSoap(new URL(endpoint));

      //make the query
      SelectType adql = null;
      
      //make call
      VOData vodata = skyNodeClient.performQuery(adql, "VOTABLE");

      //check results
      assertNotNull(vodata);
   }

   /** Submits a number of cone searches, starting small and getting larger
   - this is a silly idea to run on real services. ahem.  MC
   public void testMiniSoak() throws Throwable
   {
      DataServer server = new DataServer();

      for (int i=0;i<100;i++) {
         server.submitQuery(Account.ANONYMOUS,
                            SimpleQueryMaker.makeConeQuery(30, 30, 6+i, TargetIndicator.makeIndicator(new NullWriter()), ReturnTable.CSV));
      }
   }
    */
   
   /* not used
   public void testCanCreateWorkspace() throws IOException {
         Workspace ws = new Workspace("test-workspace");
         assertNotNull("Could not create test workspace - returned null", ws);
         ws.close();
   }
   
   /** Checks that the default myspace is accessible, if given
    */
   /* not used
   public void testCanSeeMySpace() throws IOException, Exception {

      URL defaultMyspace = SimpleConfig.getSingleton().getUrl(QuerierManager.DEFAULT_MYSPACE, null);

      if (defaultMyspace == null) {
         return;
      }
      
      StoreClient myspace = StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl(new Msrl(defaultMyspace)));
      
      myspace.getFiles("*");
   }
    */
   
   /**
    * Checks metadata is OK
    */
   public void testMetadata() throws IOException {
      VoDescriptionServer.getVoDescription();
   }
   
}


/*
 $Log: InstallationSelfCheck.java,v $
 Revision 1.16  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.12.6.3  2004/11/02 19:41:26  mch
 Split TargetIndicator to indicator and maker

 Revision 1.12.6.2  2004/10/29 18:03:24  mch
 Started SIAP, config pages

 Revision 1.12.6.1  2004/10/21 16:14:21  mch
 Changes to take home

 Revision 1.12  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.11.2.1  2004/10/16 09:54:27  mch
 Fix to plugin constructor and SOAP endpoint

 Revision 1.11  2004/10/12 23:09:53  mch
 Lots of changes to querying to get proxy querying to SSA, and registry stuff

 Revision 1.10  2004/10/12 12:33:22  mch
 Removed soak test as it's inappropriate to run on real services

 Revision 1.9  2004/10/08 17:14:22  mch
 Clearer separation of metadata and querier plugins, and improvements to VoResource plugin mechanisms

 Revision 1.8  2004/10/08 15:16:04  mch
 More on providing status

 Revision 1.7  2004/10/06 22:14:38  mch
 Added default cone scope

 Revision 1.6  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.5  2004/10/05 18:30:13  mch
 Used NullWriter to throw away results rather than StringWriter which will hold them in memory

 Revision 1.4  2004/10/05 18:24:19  mch
 Added SOAP test

 Revision 1.3  2004/09/29 17:29:21  mch
 Removed unused tests

 Revision 1.2  2004/09/28 18:23:29  mch
 Fixed package name

 Revision 1.1  2004/09/28 18:22:55  mch
 Moved Installation tests to better package

 Revision 1.1  2004/09/28 18:12:03  mch
 Moved Installation tests to server

 Revision 1.1  2004/09/28 15:11:33  mch
 Moved server test directory to pal

 Revision 1.24  2004/09/06 20:23:00  mch
 Replaced metadata generators/servers with plugin mechanism. Added Authority plugin

 Revision 1.23  2004/09/01 12:10:58  mch
 added results.toHtml

 Revision 1.22  2004/08/27 17:47:19  mch
 Added first servlet; started making more use of ReturnSpec

 Revision 1.21  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.20  2004/03/14 04:13:16  mch
 Wrapped output target in TargetIndicator

 Revision 1.19  2004/03/14 00:54:45  mch
 Changed test on myspace to one that works on deprecated delegate

 Revision 1.18  2004/03/14 00:50:31  mch
 Fixed to submit cone query test

 Revision 1.17  2004/03/13 23:38:56  mch
 Test fixes and better front-end JSP access

 Revision 1.16  2004/03/13 14:32:25  mch
 Removed Cea test

 Revision 1.15  2004/03/12 20:11:09  mch
 It05 Refactor (Client)

 Revision 1.14  2004/03/12 04:54:06  mch
 It05 MCH Refactor

 Revision 1.13  2004/03/08 00:31:28  mch
 Split out webservice implementations for versioning

 Revision 1.12  2004/03/07 00:33:50  mch
 Started to separate It4.1 interface from general server services

 Revision 1.11  2004/03/06 22:34:58  mch
 Added metadata test

 Revision 1.10  2004/03/02 01:32:18  mch
 Replaced Account in StoreClient with User

 Revision 1.9  2004/02/24 16:03:48  mch
 Config refactoring and moved datacenter It04.1 VoSpaceStuff to myspace StoreStuff

 Revision 1.8  2004/02/17 03:38:40  mch
 Various fixes for demo

 Revision 1.7  2004/02/16 23:07:04  mch
 Moved DummyQueriers to std server and switched to AttomConfig

 Revision 1.6  2004/02/15 23:20:25  mch
 Fixes to use It04.1 myspaces

 Revision 1.5  2004/01/13 00:33:14  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.4.4.3  2004/01/08 09:43:40  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.4.4.2  2004/01/07 13:02:09  nw
 removed Community object, now using User object from common

 Revision 1.4.4.1  2004/01/07 11:51:07  nw
 found out how to get wsdl to generate nice java class names.
 Replaced _query with Query throughout sources.

 Revision 1.4  2003/12/16 11:09:00  mch
 Added myspace check, removed some fails to let exceptions through

 Revision 1.3  2003/12/09 12:36:01  nw
 improved installation-tests

 Revision 1.2  2003/12/01 20:58:42  mch
 Abstracting coarse-grained plugin

 Revision 1.1  2003/11/28 16:10:30  nw
 finished plugin-rewrite.
 added tests to cover plugin system.
 cleaned up querier & queriermanager. tested
 
 */

