/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/test/org/astrogrid/filemanager/client/registry/Attic/RegistryHelperTestCase.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:06 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: RegistryHelperTestCase.java,v $
 *   Revision 1.3  2005/03/11 13:37:06  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.2.4.2  2005/02/11 14:30:23  nw
 *   changes to follow source refactoring - tests themselves may
 *   need to be refactoed later
 *
 *   Revision 1.2.4.1  2005/02/10 16:24:17  nw
 *   formatted code, added AllTests that draw all tests together for easy execution from IDE
 *
 *   Revision 1.2  2005/01/28 10:44:03  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.2  2005/01/23 05:39:44  dave
 *   Added initial implementation of FileManagerClient ...
 *
 *   Revision 1.1.2.1  2005/01/21 05:57:32  dave
 *   Added registry helper to get service kind entries ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client.registry;

import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.store.Ivorn;

import org.w3c.dom.Document;

import junit.framework.TestCase;

/**
 * JUnit test case for the RegistryHelper.
 *  
 */
public class RegistryHelperTestCase extends TestCase {

    /**
     * Check we can access the registry.
     *  
     */
    public void testCreate() throws Exception {
        //
        // Create our delegate factory.
        RegistryDelegateFactory factory = new RegistryDelegateFactory();
        //
        // Create our registry delegate.
        RegistryService registry = factory.createQuery();
        //
        // Check we got a registry delegate.
        assertNotNull(registry);
    }

    /**
     * Check we can lookup an endpoint.
     *  
     */
    public void testEndpoint() throws Exception {
        //
        // Create our delegate factory.
        RegistryDelegateFactory factory = new RegistryDelegateFactory();
        //
        // Create our registry delegate.
        RegistryService registry = factory.createQuery();
        //
        // Create our target ivorn.
        Ivorn ivorn = new Ivorn("ivo://org.astrogrid.test/filemanager");
        //
        // Try to resolve the endpoint.
        String endpoint = registry.getEndPointByIdentifier(ivorn);
        //
        // Check we got an endpoint.
        assertNotNull(endpoint);
    }

    /**
     * Check we can get the resource DOM.
     *  
     */
    public void testGetResource() throws Exception {
        //
        // Create our delegate factory.
        RegistryDelegateFactory factory = new RegistryDelegateFactory();
        //
        // Create our registry delegate.
        RegistryService registry = factory.createQuery();
        //
        // Create our target ivorn.
        Ivorn ivorn = new Ivorn("ivo://org.astrogrid.test/filemanager");
        //
        // Try to resolve the resource.
        Document doc = registry.getResourceByIdentifier(ivorn.getPath());
        //
        // Check we got a DOM object.
        assertNotNull(doc);
    }

    /**
     * Check we can get the service kind entries.
     *  
     */
    public void testGetServiceKindList() throws Exception {
        //
        // Create our delegate factory.
        RegistryDelegateFactory factory = new RegistryDelegateFactory();
        //
        // Create our registry delegate.
        RegistryService registry = factory.createQuery();
        //
        // Create our target ivorn.
        Ivorn ivorn = new Ivorn("ivo://org.astrogrid.test/filemanager");
        //
        // Create our registry extension.
        RegistryHelper helper = new RegistryHelper(registry);
        //
        // Get the list of service kind entries.
        ServiceInformation info = helper.service(ivorn);
        //
        // Check our list contains the required entry.
        assertTrue(info.provides("org.astrogrid", "FileManagerKind"));
    }
}

