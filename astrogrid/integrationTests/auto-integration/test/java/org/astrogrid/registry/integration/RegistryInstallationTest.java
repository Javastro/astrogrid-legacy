/*$Id: RegistryInstallationTest.java,v 1.13 2005/08/04 09:40:10 clq2 Exp $
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
public class RegistryInstallationTest extends AbstractTestForRegistry {
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
        Document doc = delegate.loadRegistry();
        assertNotNull(doc);
        XMLUtils.PrettyDocumentToStream(doc,System.out);
        assertVODescription(doc);        
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
Revision 1.13  2005/08/04 09:40:10  clq2
kevin's second batch

Revision 1.12.148.1  2005/07/20 08:17:07  KevinBenson
fixed a few registry tests and now no longer requires 0.9 registry but 0.10

Revision 1.12  2004/09/22 10:47:25  nw
renamed abstract class BaseRegistryTest to AsbtractTestForRegistry, to stop the int.tests from trying to run it.

Revision 1.11  2004/09/03 10:01:12  nw
made a start adding assertions on document format. could go furhter yet..

Revision 1.10  2004/09/03 09:45:59  nw
asserted that a vodescription is returned.

Revision 1.9  2004/08/05 12:13:58  KevinBenson
Changed the name of the BaseTestCase to BaseTest and made sure all it's child classes extnded reference the new name.

Revision 1.8  2004/08/05 12:08:08  KevinBenson
small changes to extend the regsitrybasetestcase

Revision 1.6.6.1  2004/08/03 20:50:05  KevinBenson
Small changes to use the new interface look to the registry

Revision 1.7  2004/08/03 13:41:29  KevinBenson
result of a merge with Itn06_case3 to change to using registry-client-lite and add some more int-test for fits and sec datacenter

Revision 1.6.2.1  2004/07/27 07:35:12  KevinBenson
Changed to use the new lighter version of the registry

Revision 1.6  2004/07/26 15:44:42  KevinBenson
*** empty log message ***

Revision 1.5  2004/07/26 15:03:56  KevinBenson
*** empty log message ***

Revision 1.4  2004/05/07 15:32:36  pah
more registry tests to flush out the fact that new entries are not being added

Revision 1.3  2004/05/07 10:23:38  pah
added managed authorities test

Revision 1.2  2004/04/22 08:59:07  nw
tweaked

Revision 1.1  2004/04/15 11:55:16  nw
added registy installation test
 
*/