/*$Id: RegistrySystemTest.java,v 1.1 2005/08/05 11:46:55 nw Exp $
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
    }

    public void testGetResourceData() throws Exception{
        ResourceInformation ri = registry.getResourceInformation(testURI);
        assertNotNull(ri);
        assertEquals(testURI,ri.getId());
        assertNotNull(ri.getTitle());
        assertNotNull(ri.getDescription());
        assertNotNull(ri.getAccessURL());
    }

    public static final String QUERY_STRING = "select * from Registry where vor:Resource/vr:identifier='ivo://uk.ac.le.star/filemanager'";
    public void testSearch()  throws Exception {
        Document result = registry.search(QUERY_STRING);
        assertNotNull(result);
    }


}


/* 
$Log: RegistrySystemTest.java,v $
Revision 1.1  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.
 
*/