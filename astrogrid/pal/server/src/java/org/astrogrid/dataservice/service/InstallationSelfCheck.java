/*$Id: InstallationSelfCheck.java,v 1.4 2005/03/22 12:57:37 mch Exp $
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
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.api.nvocone.NvoConeSearcher;
import org.astrogrid.dataservice.impl.roe.SssImagePlugin;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.queriers.QuerierPlugin;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.dataservice.service.skynode.v074.SkyNodeService;
import org.astrogrid.dataservice.service.soap.AxisDataService_v06;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.sql.SqlParser;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.xml.DomHelper;
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
   
   /** Checks we can create the various interfaces */
   public void testInstantiateServer() throws IOException {
      new AxisDataService_v06();
      new SkyNodeService();
   }
   
   /** Checks the characteristics of the plugin */
   public void testPluginDefinition() throws Exception {
      String pluginClass = ConfigFactory.getCommonConfig().getString(QuerierPluginFactory.QUERIER_PLUGIN_KEY);
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

   /**
    * Creates a test query from properties, defaulting to cone(30,-80,0.1)
    */
   public Query makeTestQuery(TargetIdentifier target, String format) throws QueryException, ParserConfigurationException, IOException, SAXException {
      String sql = ConfigFactory.getCommonConfig().getString("datacenter.testquery.sql", null);
      if (sql != null) {
         return SqlParser.makeQuery(sql, target, format);
      }
      //no sql given, make a cone searcher
      String ra = ConfigFactory.getCommonConfig().getString("datacenter.testquery.ra","30");
      String dec = ConfigFactory.getCommonConfig().getString("datacenter.testquery.dec","-80");
      String radius = ConfigFactory.getCommonConfig().getString("datacenter.testquery.radius","0.1");
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

   /** Checks that we can submit ADQL through the AxisDataService, again an internal check */
   public void testAxisServiceClassAdql() throws Throwable {
      
      AxisDataService_v06 server = new AxisDataService_v06();
      Query testQuery = makeTestQuery(TargetMaker.makeTarget(""), ReturnTable.VOTABLE);
      String adql = Adql074Writer.makeAdql(testQuery);
      String votable = server.askAdql(DomHelper.newDocument(adql).getDocumentElement(), ReturnTable.VOTABLE);
      assertNotNull(votable);
   }

   /** Checks that we can submit a count query with ADQL through the AxisDataService c*/
   public void testAxisServiceClassCount() throws Throwable {
      
      AxisDataService_v06 server = new AxisDataService_v06();
      Query testQuery = makeTestQuery(TargetMaker.makeTarget(""), ReturnTable.VOTABLE);
      String adql = Adql074Writer.makeAdql(testQuery);
      long count = server.askCount(DomHelper.newDocument(adql).getDocumentElement());
   }

   /** Checks that we can a cone search, which only really applies to sky services... */
   public void testCone() throws Throwable {
      
      //this test is called as a servlet, so get url stem from servlet context
      String endpoint = ServletHelper.getUrlStem()+"/SubmitCone";
      
      NvoConeSearcher searcher = new NvoConeSearcher(endpoint);
      
      InputStream is = searcher.coneSearch(30, -80, 0.1);

      assertNotNull(is);
   }

   /** Checks that we can reach the SOAP service
   public void testSoapv06() throws Throwable {
      
      //this test is called as a servlet, so get url stem from servlet context
      String endpoint = ServletHelper.getUrlStem()+"/services/AxisDataService05";
      
      
      InputStream is = new URL(endpoint).openStream();

      assertNotNull(is);
   }
   
   /** Checks that the SkyNode interface works OK using the generated SkyNode
    * binding *
   public void testSkyNodeAxisBinding() throws Throwable {
      
      //this test is called as a servlet, so get url stem from servlet context
      String endpoint = ServletHelper.getUrlStem()+"/services/SkyNode074";

      //make the query - a cone search around the south pole
      if (ConfigFactory.getCommonConfig().getString(StdSqlWriter.CONE_SEARCH_DEC_COL_KEY, null) == null) {
         fail("Would test using a search on DEC, but "+StdSqlWriter.CONE_SEARCH_DEC_COL_KEY+" and/or "+StdSqlWriter.CONE_SEARCH_TABLE_KEY+" is not set");
      }
      SelectType adql = new SelectType();
      //select
      adql.setSelectionList(new SelectionListType());
      adql.getSelectionList().setItem(new SelectionItemType[] { new AllSelectionItemType() });
      
      //from
      FromType from = new FromType();
      TableType fromTable = new TableType();
      fromTable.setName(ConfigFactory.getCommonConfig().getString(StdSqlWriter.CONE_SEARCH_TABLE_KEY));
      fromTable.setAlias(ConfigFactory.getCommonConfig().getString(StdSqlWriter.CONE_SEARCH_TABLE_KEY));
      from.setTable(new FromTableType[] { fromTable});
      adql.setFrom(from);
      
         
      adql.setWhere(new WhereType());
      ComparisonPredType comp = new ComparisonPredType();
      comp.setArg(new ScalarExpressionType[2]);
      adql.getWhere().setCondition(comp);
      ColumnReferenceType decCol = new ColumnReferenceType();
      decCol.setName(ConfigFactory.getCommonConfig().getString(StdSqlWriter.CONE_SEARCH_DEC_COL_KEY));
      decCol.setTable(ConfigFactory.getCommonConfig().getString(StdSqlWriter.CONE_SEARCH_TABLE_KEY));
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
   
   
   /** Would check the CEA Interface, but the CEA stuff is very clever and so
    completely obscure for all practical purposes  *
   public void testCea() throws IOException {
      new DatacenterApplication();
   }
    */

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
   
   /**
    * Checks metadata is OK
    */
   public void testMetadata() throws IOException {
      VoDescriptionServer.getVoDescription();
   }
   
   /** For running standalone, so it can be used from an IDE for quick tests against services */
   public static void main(String[] args) {
      //temporary for testing SSA Image Plugin
      ConfigFactory.getCommonConfig().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY, SssImagePlugin.class.getName());
      ServletHelper.setUrlStem("http://grendel12.roe.ac.uk:8080/pal-sss");
//      ServletHelper.setUrlStem("http://localhost.roe.ac.uk:8080/pal-samples");
      
      junit.textui.TestRunner.run(InstallationSelfCheck.class);
   }
}


/*
 $Log: InstallationSelfCheck.java,v $
 Revision 1.4  2005/03/22 12:57:37  mch
 naughty bunch of changes

 Revision 1.3  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.2  2005/02/18 18:16:54  mch
 Fixed a few compile problems


 */


