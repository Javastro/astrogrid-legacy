/*$Id: RegistryInstallationTest.java,v 1.3 2004/05/07 10:23:38 pah Exp $
 * Created on 15-Apr-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.registry.integration;

import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.scripting.Astrogrid;
import org.astrogrid.scripting.Service;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2004
 *
 */
public class RegistryInstallationTest extends TestCase {
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public RegistryInstallationTest(String arg0) {
        super(arg0);
    }
    
    protected void setUp() throws Exception {
        Astrogrid ag = Astrogrid.getInstance();
        List regList = ag.getRegistries();
        assertNotNull(regList);
        assertTrue(regList.size() > 0);
        service = (Service)regList.get(0);
        delegate = (RegistryService)service.createDelegate();
    }
    RegistryService delegate;
    Service service;
    
    public void testConfiguration() throws Exception {
        assertNotNull(service.getEndpoint());
        System.out.println(service.getEndpoint());
    }
    
    public void testRoot() throws Exception {
        Document doc = delegate.loadRegistryDOM();
        assertNotNull(doc);
        XMLUtils.PrettyDocumentToStream(doc,System.out);
    }
    
    public void testAuthority() throws RegistryException
    {
       HashMap auth = delegate.managedAuthorities();
       assertNotNull(auth);
       assertTrue("There are no managed authorities", !auth.isEmpty());
    }

}


/* 
$Log: RegistryInstallationTest.java,v $
Revision 1.3  2004/05/07 10:23:38  pah
added managed authorities test

Revision 1.2  2004/04/22 08:59:07  nw
tweaked

Revision 1.1  2004/04/15 11:55:16  nw
added registy installation test
 
*/