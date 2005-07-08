package org.astrogrid.registry.webapp.junit;

import java.io.InputStream;
import java.io.File;
import java.util.Iterator;
import org.astrogrid.xmldb.client.XMLDBFactory;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.apache.axis.AxisFault;

import java.util.HashMap;

import junit.framework.*;
import org.astrogrid.registry.server.admin.RegistryAdminService;
import org.astrogrid.registry.server.RegistryServerHelper;
import org.astrogrid.registry.common.RegistrySchemaMap;
import java.util.Map;
import java.util.Iterator;
import org.astrogrid.config.Config;
//import org.astrogrid.config.FallbackConfig;

public class RegistryAdminTest extends TestCase {
    
    
    public RegistryAdminTest() {
        
    }
    
    /**
     * Setup our test.
     *
    */ 
    public void setUp() throws Exception {
        super.setUp();
      //  XMLDBFactory xdf = new XMLDBFactory();
        File fi = new File("target/test-classes/exist.xml");
        if(fi != null) {
            /*
          System.out.println("can read = " + fi.canRead());
          System.out.println("isFile = " + fi.isFile());
          System.out.println("get name = " + fi.getName());
          System.out.println("get abspath = " + fi.getAbsolutePath());
          System.out.println("get conpath = " + fi.getCanonicalPath());
          */
          XMLDBFactory.registerDB(fi.getAbsolutePath());
        }
    }
    
    
    public void testUpdateAReg() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("ARegistry.xml");
        ras.Update(doc);
        HashMap hm = RegistryServerHelper.getManagedAuthorities("astrogridv0_9","0.9");
        System.out.println("managed authorities size = " + hm.size());
        assertEquals(1,hm.size());
    }
    
    public void testUpdateARegv10() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("ARegistryv10.xml");
        ras.Update(doc);
        HashMap hm = RegistryServerHelper.getManagedAuthorities("astrogridv0_10","0.10");
        System.out.println("managed authorities size = " + hm.size());
        assertEquals(1,hm.size());
    }
    
    
    public void testUpdateAuthority0_9() throws Exception {
        //XMLDBFactory xdf = new XMLDBFactory();
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("AstrogridStandardAuthority.xml");
        ras.Update(doc);
        HashMap hm = RegistryServerHelper.getManagedAuthorities("astrogridv0_9","0.9");
        System.out.println("managed authorities size = " + hm.size());
        assertEquals(2,hm.size());
    }

    public void testUpdateAuthorityv10() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("AstrogridStandardAuthorityv10.xml");
        ras.Update(doc);
        HashMap hm = RegistryServerHelper.getManagedAuthorities("astrogridv0_10","0.10");
        System.out.println("managed authorities size = " + hm.size());
        assertEquals(2,hm.size());
    }
    
    public void testUpdateInvalidv0_9NotManaged() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("InvalidEntry.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
    
    public void testUpdateInvalidv0_10NotManaged() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("InvalidEntryv10.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
    
    public void testUpdateOtherRegistry0_9() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("Cambridge0_9_Reg.xml");
        ras.Update(doc);
        HashMap hm = RegistryServerHelper.getManagedAuthorities("astrogridv0_9","0.9");
        System.out.println("managed authorities size = " + hm.size());
        assertEquals(3,hm.size());
    }
    
    public void testUpdateOtherRegv10() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("Cambridge0_10_Reg.xml");
        ras.Update(doc);
        HashMap hm = RegistryServerHelper.getManagedAuthorities("astrogridv0_10","0.10");
        System.out.println("managed authorities size = " + hm.size());
        assertEquals(3,hm.size());
    }
    
    public void testUpdateAuthorityInvalidv0_9NotManaged() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("AstrogridStandardAuthorityv0_9Invalid.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
    
    public void testUpdateAuthorityInvalidv0_10NotManaged() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("AstrogridStandardAuthorityv10Invalid.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
    
    public void testUpdateRegistryInvalidv0_9MisMatch() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("Cambridge0_9_RegInvalid.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
    
    public void testUpdateRegistryInvalidv0_10MisMatch() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("Cambridge0_10_RegInvalid.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
    
    
    public void testUpdateRegistryInvalidv0_9MissingAuth() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("Cambridge0_9_RegInvalid2.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
    
    public void testUpdateRegistryInvalidv0_10MissingAuth() throws Exception {
        RegistryAdminService ras = new RegistryAdminService();
        Document doc = askQueryFromFile("Cambridge0_10_RegInvalid2.xml");
        Document docUpdate = ras.Update(doc);
        assertEquals(1,docUpdate.getElementsByTagNameNS("*","Fault").getLength());
    }
    
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
    protected Document askQueryFromFile(String queryFile) throws Exception {
        assertNotNull(queryFile);
        InputStream is = this.getClass().getResourceAsStream(queryFile);
        
        assertNotNull("Could not open query file :" + queryFile,is);
        Document queryDoc = DomHelper.newDocument(is);
        
        //Document queryDoc = DomHelper.newDocument(new File(queryFile));
        return queryDoc;
    }    
}