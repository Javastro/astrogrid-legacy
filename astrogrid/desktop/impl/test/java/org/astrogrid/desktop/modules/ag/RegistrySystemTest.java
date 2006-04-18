/*$Id: RegistrySystemTest.java,v 1.3 2006/04/18 23:25:47 nw Exp $
 * Created on 01-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.desktop.framework.ACRTestSetup;
import org.astrogrid.desktop.modules.system.ApiHelpTest;
import org.astrogrid.registry.client.query.ResourceData;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;

import java.net.URI;
import java.net.URL;



import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** test functionality of in-process registry.
 * system test - requires a running astrogrid to connect to.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Aug-2005
 *
 */
public class RegistrySystemTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ACR reg = getACR();
        registry = (Registry)reg.getService(Registry.class);
        assertNotNull(registry);
        testURI = new URI("ivo://uk.ac.le.star/filemanager");
    }
    protected URI testURI;
    protected Registry registry;
    
    protected ACR getACR() throws Exception {
        return ACRTestSetup.pico.getACR();
    }
    
    public static Test suite() {
        return new ACRTestSetup(new TestSuite(RegistrySystemTest.class),true); // login.
    }    
    
    public void testResolveIdentifier()  throws Exception{
        URL url = registry.resolveIdentifier(testURI);
        assertNotNull(url);
        assertEquals("http",url.getProtocol());
    }

    public void testGetRecord() throws Exception {
        Document doc = registry.getRecord(testURI);
        assertNotNull(doc);
        assertEquals("Resource",doc.getDocumentElement().getLocalName());
    }

    public void testGetResourceData() throws Exception{
        ResourceInformation ri = registry.getResourceInformation(testURI);
        assertNotNull(ri);
        assertEquals(testURI,ri.getId());
        assertNotNull(ri.getTitle());
        assertNotNull(ri.getDescription());
        assertNotNull(ri.getAccessURL());
    }

    public static final String QUERY_STRING = "select * from Registry where vr:identifier='ivo://uk.ac.le.star/filemanager'";
    public void testAdqlSearch()  throws Exception {
        Document result = registry.adqlSearch(QUERY_STRING);
        assertNotNull(result);
        assertNotNull(result.getDocumentElement());
        assertEquals("VOResources",result.getDocumentElement().getLocalName());
        assertEquals(1,result.getElementsByTagNameNS("*","Resource").getLength());
    }
    
    public void testAdqlSearchRI() throws Exception {
        ResourceInformation[] ris = registry.adqlSearchRI(QUERY_STRING);
        assertNotNull(ris);
        assertEquals(1,ris.length);
        ResourceInformation a = ris[0];
        assertNotNull(a);
        assertEquals(testURI,a.getId());
        assertNotNull(a.getTitle());
        assertNotNull(a.getDescription());
        assertNotNull(a.getAccessURL());
    }

    public void testAdqlSearchMiss() throws Exception {
        Document result = registry.adqlSearch("select * from Registry where vr:identifier='not present'");
        assertNotNull(result);
        assertNotNull(result.getDocumentElement());
        assertEquals("VOResources",result.getDocumentElement().getLocalName());
        assertEquals(0,result.getElementsByTagNameNS("*","Resource").getLength());        
    }
    
    public void testAdqlSearchRIMiss() throws Exception {
      ResourceInformation[] ris = registry.adqlSearchRI("select * from Registry where vr:identifier='not present'");
      assertNotNull(ris);
      assertEquals(0,ris.length);
    }
    
    public void testAdqlSearchMultiple() throws Exception {
        Document result = registry.adqlSearch(multipleQuery);
        assertNotNull(result);
        assertNotNull(result.getDocumentElement());
        assertEquals("VOResources",result.getDocumentElement().getLocalName());
        assertTrue(result.getElementsByTagNameNS("*","Resource").getLength() > 1);        
    }
    
    private static final String multipleQuery = "select * from Registry where @xsi:type like '%CeaServiceType'";
    
    public void testAdqlSearchRIMultiple() throws Exception {
        ResourceInformation[] ris = registry.adqlSearchRI(multipleQuery);
        assertNotNull(ris);
        assertTrue(ris.length > 1);
        for (int i = 0 ; i < ris.length; i++) {            
        ResourceInformation a = ris[i];
        assertNotNull(a);
        assertNotNull(a.getId());
        assertNotNull(a.getTitle());
        assertNotNull(a.getDescription());
        }
    }    
    
    public void testKeywordSearch() throws Exception {
        Document result = registry.keywordSearch("filestore",false);
        assertNotNull(result);
        XMLUtils.PrettyDocumentToStream(result,System.out);
    }
    

    public void testKeywordSearchRI() throws Exception {
        ResourceInformation[] result = registry.keywordSearchRI("filestore",false);
        assertNotNull(result);
        assertTrue(result.length > 0);
        //@todo add more checking here.
    }
    
    public void testXQuery() throws Exception {
        Document result = registry.xquerySearch(
                "declare namespace vr = \"http://www.ivoa.net/xml/VOResource/v0.10\"; declare namespace vor=\"http://www.ivoa.net/xml/RegistryInterface/v0.1\"; for $x in //vor:Resource where $x/vr:identifier = 'ivo://uk.ac.le.star/filemanager' return $x"        
        );
        assertNotNull(result);
        XMLUtils.PrettyDocumentToStream(result,System.out);
    }


}


/* 
$Log: RegistrySystemTest.java,v $
Revision 1.3  2006/04/18 23:25:47  nw
merged asr development.

Revision 1.2.64.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/