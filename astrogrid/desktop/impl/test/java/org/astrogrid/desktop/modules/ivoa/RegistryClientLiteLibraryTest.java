/**
 * 
 */
package org.astrogrid.desktop.modules.ivoa;

import java.net.URL;

import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.v1_0.RegistryService;
import org.astrogrid.util.DomHelper;
import static org.custommonkey.xmlunit.XMLAssert.*;
import org.w3c.dom.Document;

import junit.framework.TestCase;

/** library test of the registry delegate - use with tcpmon to capture the soap requests and responses, which assists in
 * implementing the custom streaming delegate.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jan 12, 200810:32:43 AM
 */
public class RegistryClientLiteLibraryTest extends TestCase {

    private RegistryService service;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    //    URL endpoint = new URL("http://msslxv.mssl.ucl.ac.uk:8080/mssl-registry/services/RegistryQueryv1_0");
        //tcpmon
     //   URL endpoint = new URL("http://127.0.0.1:8099/mssl-registry/services/RegistryQueryv1_0");
        URL endpoint = new URL("http://registry.astrogrid.org/astrogrid-registry/services/RegistryQueryv1_0");
        this.service = RegistryDelegateFactory.createQueryv1_0(endpoint);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        service = null;
    }
//    
//    public void testIdentity() throws Exception {
//        Document document = service.getIdentity();
//        DomHelper.DocumentToStream(document,System.err);
//    }
    public void testAdql() throws Exception {
        Document doc = DomHelper.newDocument(ExternalRegistryADQLSystemTest.ADQLX_QUERY_STRING);
        assertNotNull(doc);
       Document search = service.search(doc);
       DomHelper.DocumentToStream(search,System.err);
        
    }
    
    public void testCaseInsensitive() throws Exception {
        Document doc = service.getResourceByIdentifier("ivo://nasa.heasarc/skyview/dss2");
        assertNotNull(doc);
        Document doc1 = service.getResourceByIdentifier("ivo://nasa.heasarc/SKYVIEW/Dss2");
        assertNotNull(doc1);
        assertXMLEqual(doc,doc1);
    }

}
