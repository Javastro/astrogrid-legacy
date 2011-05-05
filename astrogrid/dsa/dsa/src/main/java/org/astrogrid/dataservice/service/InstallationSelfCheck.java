/*$Id: InstallationSelfCheck.java,v 1.4 2011/05/05 14:49:36 gtr Exp $
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
import java.security.Principal;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.PropertyNotFoundException;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.dataservice.api.nvocone.NvoConeSearcher;
import org.astrogrid.dataservice.queriers.QuerierPlugin;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.dataservice.metadata.MetadataException;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.xml.DomHelper;
import org.xml.sax.SAXException;


// For validation
import org.w3c.dom.Document;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.contracts.SchemaMap;
import org.astrogrid.dataservice.jobs.Job;


/** Unit test for checking an installation - checks location of config files, etc.
 * <p>
 * not intended for use during development - hence the different naming convention.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Nov-2003
 *
 * (MCH Dec ratpak - removed 'fails' so that original exceptions propogate nicely
 * to nice web self-test page)
 */
public class InstallationSelfCheck extends InstallationPropertiesCheck {
   
   private Principal testPrincipal = new LoginAccount("SelfTest", "localhost");
   

   /** Checks the characteristics of the plugin */
   public void testPluginDefinition() throws Exception {
      initConfig();
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
    * Checks that the job database is accessible.
    */
   public void testJobDatabase() throws Exception {
     initConfig();
     assertNotNull("Job database is not available", Job.list());
   }

   /**
    * Creates a test query from properties, defaulting to cone(30,-80,0.1)
    */
   public Query makeTestQuery(TargetIdentifier target, String format) throws QueryException, ParserConfigurationException, IOException, SAXException {
      initConfig();
      String catalogID = ConfigFactory.getCommonConfig().getString("datacenter.self-test.catalog", null);
      String tableID = ConfigFactory.getCommonConfig().getString("datacenter.self-test.table", null);
      String catalogName = TableMetaDocInterpreter.getCatalogNameForID(
            catalogID);
      String tableName = TableMetaDocInterpreter.getTableNameForID(
            catalogID,tableID);
      return new Query("SELECT TOP 10 * FROM " + catalogName + "." + tableName,
                       new ReturnTable(target, format));
   }

   /** Checks the querier/plugin operates - runs a test query that will exercise it
    * direct to the data server - so
    * this will also test the connection to the backend database, but not any of the
    * public interfaces */
   public void testQueryDirect() throws Throwable {
      initConfig();
      // First check that metadoc is ok - otherwise we will get a confusing
      // error message if it isn't
      TableMetaDocInterpreter.initialize();

      StringWriter sw = new StringWriter(); //although we throw away the results
      DataServer server = new DataServer();
      server.askQuery(testPrincipal,makeTestQuery(new WriterTarget(sw), ReturnTable.VOTABLE), this);

      // Check that the VOTable response is schema-valid XML 
      // NB: This doesn't actually test that the returned VOTable 
      // contains real data rather than e.g. an error message
      // or no data
      Document doc = DomHelper.newDocument(sw.toString());
      String rootElement = doc.getDocumentElement().getLocalName();
      if(rootElement == null) {
         rootElement = doc.getDocumentElement().getNodeName();
      }
      AstrogridAssert.assertSchemaValid(doc,rootElement,SchemaMap.ALL);
   }


   /** Checks that we can a cone search, which only really applies to 
    * sky services... 
    *
    * Also performs a rudimentary check to see if the search failed
    * (any error message is returned in the stream - so a non-null stream
    * *doesn't* mean a successful search necessarily).
    */
   public void testCone() throws Throwable {
      initConfig(); 
      String endpoint;
      String querierPlugin = ConfigFactory.getCommonConfig().getString(
          "datacenter.querier.plugin","");
      String coneEnabled = ConfigFactory.getCommonConfig().getString(
          "datacenter.implements.conesearch","");
      if (querierPlugin.equals(
            "org.astrogrid.tableserver.test.SampleStarsPlugin")) {
         // Doesn't run conesearch if using samplestars plugin;
         // this is so that self-tests pass "out of the box" 
         // without the user having to set the 'datacenter.url'
         // property (this is reassuring).
         //
         // (NB: Tried to get the actual url stem of the installed webapp
         // into this test, rather than use the datacenter.url property;
         // However, the JUnitEE test infrastructure that runs the tests
         // in this class swallows the HttpServletRequest which we might 
         // have used to extract the installation URL.   After much pain 
         // with reflection, classloaders and other java jewels, have 
         // simply disabled this test in the default samplestars config.
         // Bah.
         return;
      }
      else if (coneEnabled.equals("false") || coneEnabled.equals("FALSE")) {
        // Don't run conesearch test if conesearch switched off in config
         return;
      }
      // OK, conesearch is enabled.
     
      // First check that metadoc is ok - otherwise we will get a confusing
      // error message if it isn't
      TableMetaDocInterpreter.initialize();

      // Find a table to conesearch
      TableInfo[] coneTables = 
         TableMetaDocInterpreter.getConesearchableTables();
      if (coneTables.length == 0) {
         // No conesearchable tables
         return;
      }
      // Use the first one
      String catalogName = coneTables[0].getCatalogName(); 
      String tableName = coneTables[0].getName(); 

      double minRad;
      String minRadString = ConfigFactory.getCommonConfig().getString(
        "conesearch.radius.limit","");
      if ("0".equals(minRadString)) {
        minRad = 0.01;
      }
      else {
        // Ensure radius is less than specified conesearch limit
        minRad = Double.parseDouble(minRadString) * 0.99;
      }
      // Otherwise, use context configured in properties file.
      // this test is called as a servlet, so get url stem from 
      // servlet context
      endpoint = ServletHelper.getUrlStem()+"/SubmitCone?DSACAT="
         +catalogName+"&DSATAB=" +tableName;

      NvoConeSearcher searcher = new NvoConeSearcher(endpoint);
      
      // Exercise haversine conesearch 
      InputStream is = searcher.coneSearch(30, -80, minRad);
      assertNotNull(is);

      // Read first 1000 bytes to look for an error message
      byte[] block = new byte[1000];
      int read = is.read(block);
      String s = new String(block);
      //System.out.println("String is ");
      //System.out.println(s);
      if (s.indexOf("ASTROGRID DSA ERROR") != -1) {
        // Got an error message in the stream
        throw new QueryException(s);
      }

      // Exercise greatcircle conesearch unless conesearch limit is lower
      if (minRad >= 0.168) {
        is = searcher.coneSearch(30, -80, 0.168);
        assertNotNull(is);

        // Read first 1000 bytes to look for an error message
        read = is.read(block);
        s = new String(block);
        //System.out.println("String is ");
        //System.out.println(s);
        if (s.indexOf("ASTROGRID DSA ERROR") != -1) {
          // Got an error message in the stream
          throw new QueryException(s);
        }
     }
   }


   /**
    * Checks Metadoc file is valid
    */
   public void testMetadocValidity() throws MetadataException, IOException {
      initConfig();
      // This will check the metadoc
      TableMetaDocInterpreter.initialize();
   }

   /** Test to ensure that the specified publishing registry can publish 
    * registrations with the specified authority ID.
    */

   /* KONA TOFIX ADD TESTS FOR VOSI ENDPOINTS */
   public void testAuthorityIdAgainstReg() 
               throws MetadataException, IOException 
   {
      String querierPlugin = ConfigFactory.getCommonConfig().getString(
          "datacenter.querier.plugin","");
      if (querierPlugin.equals(
            "org.astrogrid.tableserver.test.SampleStarsPlugin")) {
         // Doesn't run conesearch if using samplestars plugin;
         // this is so that self-tests pass "out of the box" 
         return;
      }
      String authorityID = "";
      try {
        authorityID=  ConfigFactory.getCommonConfig().getString(
              "datacenter.authorityId");
      }
      catch (PropertyNotFoundException pnfe) {
         //Shouldn't get here if self-tests ok
         throw new IOException(
               "Configuration error: Property datacenter.authorityId" +
               " is not set, please set it!"); 
      }
      String registryURL = "";
      try {
          registryURL =  ConfigFactory.getCommonConfig().getString("datacenter.publishing.registry");
      }
      catch (PropertyNotFoundException pnfe) {
         //Shouldn't get here if property self-tests ok
         throw new IOException("Configuration error: Property 'datacenter.publishing.registry is not set, please set it!"); 
      }
      if (!registryURL.endsWith("/")) {
         registryURL = registryURL + "/";
      }
      //We need to check that the publishing registry recognises our
      //authority ID. 
      //First, construct publishing registry's query url
      String queryRegUrl = registryURL+"services/RegistryQueryv1_0";
      //Get authority ID
      HashMap hm = null;
      try {
        RegistryService rs = RegistryDelegateFactory.createQueryv1_0(
              new java.net.URL(queryRegUrl));
        hm = rs.managedAuthorities();
      }      
      catch (Exception e) {
         throw new IOException("Can't talk to the registry: " + e.getMessage());
      }
      // Check for authority ID
      if (!hm.containsKey(authorityID)) {
         throw new IOException("The authority ID " + 
            authorityID  + 
            " is not currently managed by the registry at " +
            registryURL +
            ".  This means you cannot your DSA on that registry.  If this a new authority ID, you will need to configure the specified registry to manage it. Please consult your registry administrator for further advice.");
      }
   }

   
   /** For running standalone, so it can be used from an IDE for quick tests against services 
    * @TOFIX Put some kind of runtime test here? */
   public static void main(String[] args) throws Exception {
      /*
      //temporary for testing SSA Image Plugin
      ConfigFactory.getCommonConfig().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY, SssImagePlugin.class.getName());
      ServletHelper.setUrlStem("http://localhost.roe.ac.uk:8080/pal-samples");
      
      junit.textui.TestRunner.run(InstallationSelfCheck.class);
      */
     throw new Exception("Main routine not defined in InstallationSelfCheck");
   }




   /* =====================================================================*/
   /* LEGACY TESTS BELOW HERE, NO LONGER USED */
   /* =====================================================================*/

   /**
    * Checks VOResource metadata is OK.
    * NB This test will not pick up empty metadata 
    * V0.10 NOT USED ANYMORE
    */
   /*
   public void testMetadata_v0_10() throws IOException, SAXException, ParserConfigurationException {
      initConfig();
      Document doc = 
         VoDescriptionServer.getVoDescription(VoDescriptionServer.V0_10);
      String rootElement = doc.getDocumentElement().getLocalName();
      if(rootElement == null) {
         rootElement = doc.getDocumentElement().getNodeName();
      }
      AstrogridAssert.assertSchemaValid(doc,rootElement,SchemaMap.ALL);
   }
   */
   /** Checks we can create the various interfaces */
   /*
    *  Not supporting either of these interfaces now
   public void testInstantiateServer() throws IOException {
      new AxisDataService_v06();
      new SkyNodeService();
   }
   */


   /** Would check the CEA Interface, but the CEA stuff is very clever and so
    completely obscure for all practical purposes  *
   public void testCea() throws IOException {
      new DatacenterApplication();
   }
    */

   /** Checks that we can submit ADQL through the AxisDataService, again an internal check */
   /*
    * // No longer supported
   public void testAxisServiceClassAdql() throws Throwable {
      
      AxisDataService_v06 server = new AxisDataService_v06();
      Query testQuery = makeTestQuery(new NullTarget(), ReturnTable.VOTABLE);
      //String adql = Adql074Writer.makeAdql(testQuery);
      String adql = testQuery.getAdqlString();
      String votable = server.askAdql(DomHelper.newDocument(adql).getDocumentElement(), ReturnTable.VOTABLE);
      assertNotNull(votable);
   }
   */

   /** Checks that we can submit a count query with ADQL through the AxisDataService c*/
   /*
    * // No longer supported
   public void testAxisServiceClassCount() throws Throwable {
      
      AxisDataService_v06 server = new AxisDataService_v06();
      Query testQuery = makeTestQuery(new NullTarget(), ReturnTable.VOTABLE);
      //String adql = Adql074Writer.makeAdql(testQuery);
      String adql = testQuery.getAdqlString();
      long count = server.askCount(DomHelper.newDocument(adql).getDocumentElement());
   }
   */
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
}
