/*$Id: RegistryInstallationTest.java,v 1.2 2004/06/16 12:09:21 KevinBenson Exp $
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
 * @author Paul Harrison pah@jb.man.ac.uk 07-May-2004
 *
 */
public class RegistryInstallationTest extends RegistryBaseTestCase {
    /**
     * Constructor for RegistryInstallationTest.
     * @param arg0
     */
    public RegistryInstallationTest(String arg0) {
        super(arg0);
    }
    
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

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

}


/* 
$Log: RegistryInstallationTest.java,v $
Revision 1.2  2004/06/16 12:09:21  KevinBenson
New installer much like auto-integration except it only install the registry

Revision 1.1.2.1  2004/06/15 10:13:11  KevinBenson
A new installer much like auto-integration area.  That just installs the registry

Revision 1.4  2004/05/07 15:32:36  pah
more registry tests to flush out the fact that new entries are not being added

Revision 1.3  2004/05/07 10:23:38  pah
added managed authorities test

Revision 1.2  2004/04/22 08:59:07  nw
tweaked

Revision 1.1  2004/04/15 11:55:16  nw
added registy installation test
 
*/