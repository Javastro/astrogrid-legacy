/*$Id: RegistryServiceClientTest.java,v 1.2 2004/08/03 13:41:29 KevinBenson Exp $
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

import java.net.URL;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Apr-2004
 * @author Paul Harrison pah@jb.man.ac.uk 07-May-2004
 *
 */
public class RegistryServiceClientTest extends TestCase {
    
   //public static final String SERVICE_ENDPOINT = "http://localhost:8080/astrogrid-registry-SNAPSHOT/services/Registry" ;    
   
   RegistryService rs = null; 
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public RegistryServiceClientTest(String arg0) {
        super(arg0);
        
    }
            
    public void testLoadRegistry() throws RegistryException
    {
       HashMap auth = rs.managedAuthorities();
       assertNotNull(auth);
       assertTrue("There are no managed authorities", !auth.isEmpty());
    }

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      //rs = new RegistryService(new URL(SERVICE_ENDPOINT));
      rs = new RegistryService();
   }

}


/* 
*/