/*$Id: RegistryServiceClientTest.java,v 1.6 2004/09/22 10:47:25 nw Exp $
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
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.scripting.Astrogrid;
import org.astrogrid.scripting.Service;

import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import java.net.URL;

/**
 * @author Kevin Benson
 *
 */
public class RegistryServiceClientTest extends AbstractTestForRegistry {
    
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
   }

}


/* 
*/