/*$Id: InstallationSelfCheck.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service;



import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.security.Principal;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import org.astrogrid.account.LoginAccount;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.impl.roe.SssImagePlugin;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.queriers.QuerierPlugin;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.dataservice.service.skynode.v074.SkyNodeService;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.sql.SqlParser;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;

/** Unit test for checking an installation - checks location of config files, etc.
 * <p>
 * not intended for use during development - hence the different naming convention.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 * (MCH Dec ratpak - removed 'fails' so that original exceptions propogate nicely
 * to nice web self-test page)
 */
public class InstallationSelfCheck extends TestCase {
   
   private Principal testPrincipal = new LoginAccount("SelfTest", "localhost");
   
   /**
    * Constructor for InstallationSelfCheck.
    * @param arg0
    */
   public InstallationSelfCheck(String arg0) {
      super(arg0);
   }

   /** For running standalone */
   public static void main(String[] args) {
      //temporary for testing SSA Image Plugin
      SimpleConfig.getSingleton().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY, SssImagePlugin.class.getName());
      ServletHelper.setUrlStem("http://grendel12.roe.ac.uk:8080/pal-sss");
//      ServletHelper.setUrlStem("http://localhost.roe.ac.uk:8080/pal-samples");
      
      junit.textui.TestRunner.run(InstallationSelfCheck.class);
   }
   
   /** Checks we can create the various interfaces */
   public void testInstantiateServer() throws IOException {
      new AxisDataService_v05();
      new AxisDataService_v06();
      new SkyNodeService();
   }
   
   /** Would check the CEA Interface, but the CEA stuff is very clever and so
    completely obscure for all practical purposes  *
   public void testCea() throws IOException {
      new DatacenterApplication();
   }
    */

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

   /** Ask a query to get all of the database using ADQL. The query will be
    * from property datacenter.selftest.sql, and defaults to SELECT * FROM <CONESEARCHTABLE>
   public void testBigQuery() throws Throwable
   {
      DataServer server = new DataServer();
      
      String sql = SimpleConfig.getSingleton().getString("datacenter.selftest.sql", "SELECT * FROM "+SimpleConfig.getSingleton().getString(SqlWriter.CONE_SEARCH_TABLE_KEY));

      //String adql = "<?xml version='1.0' encoding='utf-8'?>"+
      //               "<Select xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns='http://www.ivoa.net/xml/ADQL/v0.7.4'>"+
      //               "  <SelectionList> <Item xsi:type='allSelectionItemType' /> </SelectionList>"+
      //               "  <From> <Table xsi:type='tableType' Name='"++"' Alias='s' />  </From>"+
      //               "</Select>";
      server.askQuery(testPrincipal, SqlParser.makeQuery(sql, TargetMaker.makeIndicator("null"), ReturnTable.CSV), this);
   }
    */

   /**
    * Creates a test query from properties, defaulting to cone(30,-80,0.1)
    */
   public Query makeTestQuery(TargetIdentifier target, String format) throws QueryException, ParserConfigurationException, IOException, SAXException {
      String sql = SimpleConfig.getProperty("datacenter.testquery.sql", null);
      if (sql != null) {
         return SqlParser.makeQuery(sql, target, format);
      }
      //no sql given, make a cone searcher
      String ra = SimpleConfig.getProperty("datacenter.testquery.ra","30");
      String dec = SimpleConfig.getProperty("datacenter.testquery.dec","-80");
      String radius = SimpleConfig.getProperty("datacenter.testquery.radius","0.1");
      return SimpleQueryMaker.makeConeQuery(Double.parseDouble(ra),Double.parseDouble(dec),Double.parseDouble(radius), target, format);
   }

   /** Checks the querier/plugin operates - runs a test query that will exercise it
    * direct to the data server - so
    * this will also test the connection to the backend database, but not any of the
    * public interfaces */
   public void testQueryDirect() throws Throwable {
      
      StringWriter sw = new StringWriter(); //although we throw away the results
      DataServer server = new DataServer();
      server.askQuery(testPrincipal,makeTestQuery(TargetMaker.makeTarget(sw), ReturnTable.VOTABLE), this);
   }

   /** Checks that we can submit ADQL through the AxisDataService*/
   public void testAxisAdql() throws Throwable {
      
      AxisDataService_v06 server = new AxisDataService_v06();
      Query testQuery = makeTestQuery(TargetMaker.makeTarget(""), ReturnTable.VOTABLE);
      String adql = Adql074Writer.makeAdql(testQuery);
      String votable = server.askAdql(DomHelper.newDocument(adql).getDocumentElement(), ReturnTable.VOTABLE);
      assertNotNull(votable);
   }

   /** Checks that we can submit a count query with ADQL through the AxisDataService c*/
   public void testAxisCount() throws Throwable {
      
      AxisDataService_v06 server = new AxisDataService_v06();
      Query testQuery = makeTestQuery(TargetMaker.makeTarget(""), ReturnTable.VOTABLE);
      String adql = Adql074Writer.makeAdql(testQuery);
      long count = server.askCount(DomHelper.newDocument(adql).getDocumentElement());
   }

   /** Checks that the delegates can connect correctly */
   public void testNaturalSoapBinding() throws Throwable {
      
      //this test is called as a servlet, so get url stem from servlet context
      String endpoint = ServletHelper.getUrlStem()+"/services/AxisDataService05";
      
      ConeSearcher searcher = DatacenterDelegateFactory.makeConeSearcher(testPrincipal,
                                                                         endpoint,
                                                                         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      InputStream is = searcher.coneSearch(30, -80, 0.1);

      assertNotNull(is);
   }
   
   /** Checks that the SkyNode interface works OK using the generated SkyNode
    * binding *
   public void testSkyNodeAxisBinding() throws Throwable {
      
      //this test is called as a servlet, so get url stem from servlet context
      String endpoint = ServletHelper.getUrlStem()+"/services/SkyNode074";

      //make the query - a cone search around the south pole
      if (SimpleConfig.getSingleton().getString(StdSqlWriter.CONE_SEARCH_DEC_COL_KEY, null) == null) {
         fail("Would test using a search on DEC, but "+StdSqlWriter.CONE_SEARCH_DEC_COL_KEY+" and/or "+StdSqlWriter.CONE_SEARCH_TABLE_KEY+" is not set");
      }
      SelectType adql = new SelectType();
      //select
      adql.setSelectionList(new SelectionListType());
      adql.getSelectionList().setItem(new SelectionItemType[] { new AllSelectionItemType() });
      
      //from
      FromType from = new FromType();
      TableType fromTable = new TableType();
      fromTable.setName(SimpleConfig.getSingleton().getString(StdSqlWriter.CONE_SEARCH_TABLE_KEY));
      fromTable.setAlias(SimpleConfig.getSingleton().getString(StdSqlWriter.CONE_SEARCH_TABLE_KEY));
      from.setTable(new FromTableType[] { fromTable});
      adql.setFrom(from);
      
         
      adql.setWhere(new WhereType());
      ComparisonPredType comp = new ComparisonPredType();
      comp.setArg(new ScalarExpressionType[2]);
      adql.getWhere().setCondition(comp);
      ColumnReferenceType decCol = new ColumnReferenceType();
      decCol.setName(SimpleConfig.getSingleton().getString(StdSqlWriter.CONE_SEARCH_DEC_COL_KEY));
      decCol.setTable(SimpleConfig.getSingleton().getString(StdSqlWriter.CONE_SEARCH_TABLE_KEY));
      comp.setArg(0, decCol);
      AtomType decValue = new AtomType();
      decValue.setLiteral(new RealType());
      ((RealType) decValue.getLiteral()).setValue(-89.5);
      comp.setArg(1, decValue);
      comp.setComparison(ComparisonType.fromValue("<"));
      
      //construct SOAP client
      SkyNodeSoap skyNodeClient = new SkyNodeLocator().getSkyNodeSoap(new URL(endpoint));

      //make call
      VOData vodata = skyNodeClient.performQuery(adql, "VOTABLE");

      //check results
      assertNotNull(vodata);
   }
    */
   public void testSkyNodeSoap() throws Throwable {
      //this test is called as a servlet, so get url stem from servlet context
      String endpoint = ServletHelper.getUrlStem()+"/services/SkyNode074";
      
      SkyNode074Client client = new SkyNode074Client(new URL(endpoint));
      
      InputStream results = client.askQuery(SimpleQueryMaker.makeConeQuery(0,-89.5,1));
      
      assertNotNull(results);
   }
   
   
   /** Checks that the CEA interface works OK.  Except that again CEA wouldn't be
    * straightforward to use like any other SOAP service, oh no.
   public void testCeaSoapBinding() throws Throwable {
      
      
      //construct SOAP client
      SkyNodeSoap skyNodeClient = new SkyNodeLocator().getSkyNodeSoap(new URL(endpoint));

      //make call
      VOData vodata = skyNodeClient.performQuery(adql, "VOTABLE");

      //check results
      assertNotNull(vodata);
   }
    */
   /** Submits a number of cone searches, starting small and getting larger
   - this is a silly idea to run on real services. ahem.  MC
   public void testMiniSoak() throws Throwable
   {
      DataServer server = new DataServer();

      for (int i=0;i<100;i++) {
         server.submitQuery(LoginAccount.ANONYMOUS,
                            SimpleQueryMaker.makeConeQuery(30, 30, 6+i, TargetIndicator.makeIndicator(new NullWriter()), ReturnTable.CSV));
      }
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
 Revision 1.1  2005/02/17 18:37:35  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.19.2.10  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.19.2.9  2004/12/05 19:33:16  mch
 changed skynode to 'raw' soap (from axis) and bug fixes

 Revision 1.19.2.8  2004/12/03 18:30:30  mch
 Removed most dependencies on generated skynode code

 Revision 1.19.2.7  2004/11/30 01:04:02  mch
 Rationalised tablewriters, reverted AxisDataService06 to string

 Revision 1.19.2.6  2004/11/29 23:16:26  mch
 added from

 Revision 1.19.2.5  2004/11/29 21:52:18  mch
 Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

 Revision 1.19.2.4  2004/11/27 10:06:02  mch
 added count selftest

 Revision 1.19.2.3  2004/11/26 18:17:21  mch
 More status persisting, browsing and aborting

 Revision 1.19.2.2  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.19.2.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.19  2004/11/11 20:42:50  mch
 Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

 Revision 1.18  2004/11/10 22:01:50  mch
 skynode starts and some fixes

 Revision 1.17  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

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
 Replaced Principal in StoreClient with User

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


