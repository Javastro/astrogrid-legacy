package org.astrogrid.pal.xmldbexistserver.junit;

import java.io.InputStream;
import java.io.File;
import java.io.StringWriter;
import java.security.Principal;
import java.util.Iterator;
import org.astrogrid.xmldb.client.XMLDBManager;
import org.astrogrid.xmldb.client.XMLDBFactory;
import org.astrogrid.xmldb.client.XMLDBService;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xmldb.api.base.Collection;

import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.query.Query;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.dataservice.queriers.QuerierPluginFactory;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.query.adql.AdqlXml074Parser;
import org.astrogrid.query.sql.SqlParser;

import java.security.Principal;

import java.util.Hashtable;

import junit.framework.*;
import java.util.Map;
import java.util.Iterator;
import java.util.Properties;
import org.astrogrid.config.Config;
import org.astrogrid.cfg.ConfigReader;
import org.astrogrid.cfg.ConfigFactory;

/**
 * Class: RegistryQueryTest
 * Description: Tests out the various Query methods of the server side Registry.
 * @author Kevin Benson
 *
 */
public class XMLDBQueryTest extends TestCase {
    
    XMLDBService xdbService;
    protected DataServer server;
    protected Query query;
    
    public Principal TESTPrincipal = new LoginAccount("UnitTester", "test.org");
    
    public XMLDBQueryTest() {
        
    }
    
    /**
     * Setup our test.
     *
    */ 
    public void setUp() throws Exception {
        super.setUp();
        File fi = new File("target/test-classes/conf.xml");
        Properties props = new Properties();
        props.setProperty("create-database", "true");
        props.setProperty("configuration",fi.getAbsolutePath());
        System.out.println("registering xmldbmanager");
        if(fi != null) {
          XMLDBManager.registerDB(props);
        }
        System.out.println("creating xdbservice");
        xdbService = XMLDBFactory.createXMLDBService();
        
        System.out.println("setting properties");
        //SampleStarsPlugin.initConfig();
        ConfigFactory.getCommonConfig().setProperty(QuerierPluginFactory.QUERIER_PLUGIN_KEY, "org.astrogrid.xdbserver.xql.XMLDBPlugin");
        ConfigFactory.getCommonConfig().setProperty("datacenter.xmldb.collection", "junit-tests");
        System.out.println("creating dataserver");
        server = new DataServer();
    }
    
    /**
     * Method: testSearchByIdent
     * Description: test to perform an adql query for an identifier known in the registry.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testSearchSimple() throws Throwable {
        //Document adql = askQueryFromFile("QueryForIdentifier--adql-v0.7.4.xml");
        //Collection coll = xdbService.openCollection("junit-tests");
        System.out.println("testSerch");
        Hashtable keywords = new Hashtable();
        keywords.put("vor:VOResources/vor:Resource/vr:identifier","*registry*");
        keywords.put("vor:VOResources/vor:Resource/vr:title","*rog*");
        StringWriter sw = new StringWriter();
        System.out.println("making Query object");
        query = new Query(SimpleQueryMaker.makeKeywordCondition(keywords),
                new ReturnTable(new WriterTarget(sw), "VOTABLE"));
        System.out.println("now doing an askQuery");
        server.askQuery(TESTPrincipal, query, this);
        String results = sw.toString();
        System.out.println("the results = " + results);
        VoTableTestHelper.assertIsVotable(results);
    }
    
    /**
     * Method: testSearchByIdent
     * Description: test to perform an adql query for an identifier known in the registry.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testSearchSelection() throws Throwable {
        System.out.println("testSerchSelection");
        String sql = "Select vor:VOResources/vor:Resource/vr:identifier from junit-tests where vor:VOResources/vor:Resource/vr:identifier like '*regis*'";
        System.out.println("making Query object for sql");        
        Query q = SqlParser.makeQuery(sql);        
        String adql = Adql074Writer.makeAdql(q, "Hello");        
        System.out.println(adql);
        System.out.println("making Query object for adql");                
        q = AdqlXml074Parser.makeQuery(adql);
        StringWriter sw = new StringWriter();
        ReturnSpec rs = q.getResultsDef();
        rs.setTarget(new WriterTarget(sw));
        rs.setFormat("VOTABLE");               
        System.out.println("now doing an askQuery");
        server.askQuery(TESTPrincipal, q, this);
        String results = sw.toString();
        System.out.println("the results = " + results);
        VoTableTestHelper.assertIsVotable(results);        
    }  
    
    /**
     * Method: testSearchByIdent
     * Description: test to perform an adql query for an identifier known in the registry.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testSearchMultSelection() throws Throwable {
        System.out.println("testSerchMultSelection");
        String sql = "Select vor:VOResources/vor:Resource/vr:identifier, vor:VOResources/vor:Resource/vr:title  from junit-tests where vor:VOResources/vor:Resource/vr:identifier like '*regis*'";
        System.out.println("making Query object for sql");        
        Query q = SqlParser.makeQuery(sql);        
        String adql = Adql074Writer.makeAdql(q, "Hello");        
        System.out.println(adql);
        System.out.println("making Query object for adql");                
        q = AdqlXml074Parser.makeQuery(adql);
        StringWriter sw = new StringWriter();
        ReturnSpec rs = q.getResultsDef();
        rs.setTarget(new WriterTarget(sw));
        rs.setFormat("VOTABLE");               
        System.out.println("now doing an askQuery");
        server.askQuery(TESTPrincipal, q, this);
        String results = sw.toString();
        System.out.println("the results = " + results);
        VoTableTestHelper.assertIsVotable(results);        
    } 
    
    
    /**
     * Method: testSearchByIdent
     * Description: test to perform an adql query for an identifier known in the registry.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testSearchMultSelection2() throws Throwable {
        System.out.println("testSerchMultSelection2");
        String sql = "Select vor:VOResources/vor:Resource/vr:identifier, vor:VOResources/vor:Resource/vr:content  from junit-tests where vor:VOResources/vor:Resource/vr:identifier like '*regis*'";
        System.out.println("making Query object for sql");        
        Query q = SqlParser.makeQuery(sql);        
        String adql = Adql074Writer.makeAdql(q, "Hello");        
        System.out.println(adql);
        System.out.println("making Query object for adql");                
        q = AdqlXml074Parser.makeQuery(adql);
        StringWriter sw = new StringWriter();
        ReturnSpec rs = q.getResultsDef();
        rs.setTarget(new WriterTarget(sw));
        rs.setFormat("VOTABLE");               
        System.out.println("now doing an askQuery");
        server.askQuery(TESTPrincipal, q, this);
        String results = sw.toString();
        System.out.println("the results = " + results);
        VoTableTestHelper.assertIsVotable(results);        
    } 

    /**
     * Method: testSearchByIdent
     * Description: test to perform an adql query for an identifier known in the registry.
     * @throws Exception standard junit exception to be thrown.
     */    
    public void testSearchMultSelection3() throws Throwable {
        System.out.println("testSerchMultSelection3");
        String sql = "Select vor:VOResources/vor:Resource/vr:identifier, vor:VOResources/vor:Resource/vg:managedAuthority  from junit-tests where vor:VOResources/vor:Resource/vr:identifier like '*regis*'";
        System.out.println("making Query object for sql");        
        Query q = SqlParser.makeQuery(sql);        
        String adql = Adql074Writer.makeAdql(q, "Hello");        
        System.out.println(adql);
        System.out.println("making Query object for adql");                
        q = AdqlXml074Parser.makeQuery(adql);
        StringWriter sw = new StringWriter();
        ReturnSpec rs = q.getResultsDef();
        rs.setTarget(new WriterTarget(sw));
        rs.setFormat("VOTABLE");               
        System.out.println("now doing an askQuery");
        server.askQuery(TESTPrincipal, q, this);
        String results = sw.toString();
        System.out.println("the results = " + results);
        VoTableTestHelper.assertIsVotable(results);        
    }      
    
    
   

         
    /**
     * Method: askQueryFromFile
     * Description: Obtains a File on the local system as a inputstream and feeds it into a Document DOM object to be
     * returned.
     * @param queryFile - File name of a xml resource.
     * @return Document DOM object of a XML file.
     * @throws Exception  Any IO Exceptions or DOM Parsing exceptions could be thrown.
     */       
    protected Document askQueryFromFile(String queryFile) throws Exception {
        assertNotNull(queryFile);
        InputStream is = this.getClass().getResourceAsStream(queryFile);
        
        assertNotNull("Could not open query file :" + queryFile,is);
        Document queryDoc = DomHelper.newDocument(is);
        
        //Document queryDoc = DomHelper.newDocument(new File(queryFile));
        return queryDoc;
    }    
}