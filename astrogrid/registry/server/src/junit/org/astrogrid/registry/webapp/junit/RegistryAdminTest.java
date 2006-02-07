package org.astrogrid.registry.webapp.junit;

import java.io.InputStream;
import java.io.File;
import java.util.Iterator;
import org.astrogrid.xmldb.client.XMLDBManager;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.axis.AxisFault;

import java.util.HashMap;

import junit.framework.*;
import org.astrogrid.registry.server.admin.RegistryAdminService;
import org.astrogrid.registry.server.admin.AuthorityListManager;
import java.util.Map;
import java.util.Iterator;
import java.util.Properties;

import org.astrogrid.config.Config;
//import org.astrogrid.config.FallbackConfig;

/**
 * Class: RegistryAdminTest
 * Description: Test for the admin ability of the registry which is mainly adding&updating xml resources to the
 * xml database (eXist).
 * @author Kevin Benson
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RegistryAdminTest extends TestCase {
    
    RegistryAdminService ras = null;
    AuthorityListManager alm = null;
    public RegistryAdminTest() {
        
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
        
        if(fi != null) {
          XMLDBManager.registerDB(props);
        }
        ras = new RegistryAdminService();
        alm = new AuthorityListManager();
    }
    
        
    public void testUpdateARegv10() throws Exception {
        Document doc = askQueryFromFile("ARegistryv10.xml");
        ras.Update(doc);
        HashMap hm = alm.getManagedAuthorities("astrogridv0_10","0.10");
        System.out.println("managed authorities size = " + hm.size());
        assertEquals(1,hm.size());
    }
    

    public void testUpdateAuthorityv10() throws Exception {
        Document doc = askQueryFromFile("AstrogridStandardAuthorityv10.xml");
        ras.Update(doc);
        HashMap hm = alm.getManagedAuthorities("astrogridv0_10","0.10");
        System.out.println("managed authorities size = " + hm.size());
        assertEquals(2,hm.size());
    }
        
    public void testUpdateInvalidv0_10NotManaged() throws Exception {
        Document doc = askQueryFromFile("InvalidEntryv10.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
        
    public void testUpdateOtherRegv10() throws Exception {
        Document doc = askQueryFromFile("Cambridge0_10_Reg.xml");
        ras.Update(doc);
        HashMap hm = alm.getManagedAuthorities("astrogridv0_10","0.10");
        System.out.println("managed authorities size = " + hm.size());
        assertEquals(3,hm.size());
    }
        
    public void testUpdateAuthorityInvalidv0_10NotManaged() throws Exception {
        Document doc = askQueryFromFile("AstrogridStandardAuthorityv10Invalid.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
        
    public void testUpdateRegistryInvalidv0_10MisMatch() throws Exception {
        Document doc = askQueryFromFile("Cambridge0_10_RegInvalid.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
    
        
    public void testUpdateRegistryInvalidv0_10MissingAuth() throws Exception {
        Document doc = askQueryFromFile("Cambridge0_10_RegInvalid2.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
    
    /*
    public void testUpdateOAIv0_10() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("OAIHandlerv0_10.xml");
        ras.updateNoCheck(doc,"0.10");        
    }
    
    public void testUpdateOAIv0_9() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("OAIHandlerv0_9.xml");
        ras.updateNoCheck(doc,"0.9");
    }
    */

    /*
    public void testUpdateOAIInvalidv0_10() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("OAIHandlerInvalidv0_10.xml");
        ras.updateNoCheck(doc,"0.10");
        fail("ERROR: Should have caught an invalid exception and it passed.");        
    }
    
    public void testUpdateOAIInvalidv0_9() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("OAIHandlerInvalidv0_9.xml");
        ras.updateNoCheck(doc,"0.9");
        fail("ERROR: Should have caught an invalid exception and it passed.");            
    }
    */
    
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