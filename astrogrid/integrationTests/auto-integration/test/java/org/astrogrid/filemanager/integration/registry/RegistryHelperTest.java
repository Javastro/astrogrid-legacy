/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/filemanager/integration/registry/Attic/RegistryHelperTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:44:01 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: RegistryHelperTest.java,v $
 *   Revision 1.2  2005/01/28 10:44:01  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.4  2005/01/23 06:17:40  dave
 *   Added initial FileManagerClient test ...
 *
 *   Revision 1.1.2.3  2005/01/21 14:38:47  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.1.2.2  2005/01/21 09:32:35  dave
 *   Fixed bug in test and added service kind to filemanager registry entries ..
 *
 *   Revision 1.1.2.1  2005/01/21 06:30:14  dave
 *   Added test for RegistryHelper ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.integration.registry;

import junit.framework.TestCase ;

import org.w3c.dom.Document;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.registry.client.RegistryDelegateFactory ;
import org.astrogrid.registry.client.query.RegistryService ;

import org.astrogrid.filemanager.common.BaseTest ;
import org.astrogrid.filemanager.client.registry.RegistryHelper ;

/**
 * JUnit test case for the RegistryHelper.
 *
 */
public class RegistryHelperTest
    extends TestCase
    {
    /**
     * Our AstroGrid configuration.
     */
    private Config config ;

    /**
     * Helper method to get a config property.
     *
     */
    public String getConfigProperty(String name)
        {
        return (String) config.getProperty(
            name
            ) ;
        }

    /**
     * Set up our test.
     *
     */
    public void setUp()
        throws Exception
        {
        super.setUp();
        //
        // Initialise our config.
        config = SimpleConfig.getSingleton();
        }

    /**
     * Check we can access the registry.
     *
     */
    public void testCreate()
        throws Exception
        {
        //
        // Create our delegate factory.
        RegistryDelegateFactory factory =
            new RegistryDelegateFactory();
        //
        // Create our registry delegate.
        RegistryService registry =
            factory.createQuery();
        //
        // Check we got a registry delegate.
        assertNotNull(
            registry
            );
        }

    /**
     * Check we can lookup an endpoint.
     *
     */
    public void testEndpoint()
        throws Exception
        {
        //
        // Create our delegate factory.
        RegistryDelegateFactory factory =
            new RegistryDelegateFactory();
        //
        // Create our registry delegate.
        RegistryService registry =
            factory.createQuery();
        //
        // Create our target ivorn.
        Ivorn ivorn = new Ivorn(
            getConfigProperty(
                "org.astrogrid.filemanager.test.ivorn"
                )
            );
        //
        // Try to resolve the endpoint.
        String endpoint = registry.getEndPointByIdentifier(
            ivorn
            );
        //
        // Check we got an endpoint.
        assertNotNull(
            endpoint
            );
        }

    /**
     * Check we can get the resource DOM.
     *
     */
    public void testGetResource()
        throws Exception
        {
        //
        // Create our delegate factory.
        RegistryDelegateFactory factory =
            new RegistryDelegateFactory();
        //
        // Create our registry delegate.
        RegistryService registry =
            factory.createQuery();
        //
        // Create our target ivorn.
        Ivorn ivorn = new Ivorn(
            getConfigProperty(
                "org.astrogrid.filemanager.test.ivorn"
                )
            );
        //
        // Try to resolve the resource.
        Document doc = registry.getResourceByIdentifier(
            ivorn.getPath()
            );
        //
        // Check we got a DOM object.
        assertNotNull(
            doc
            );
        }

    /**
     * Check we can get the service kind entries.
     *
     */
    public void testGetServiceKindList()
        throws Exception
        {
        //
        // Create our delegate factory.
        RegistryDelegateFactory factory =
            new RegistryDelegateFactory();
        //
        // Create our registry delegate.
        RegistryService registry =
            factory.createQuery();
        //
        // Create our target ivorn.
        Ivorn ivorn = new Ivorn(
            getConfigProperty(
                "org.astrogrid.filemanager.test.ivorn"
                )
            );
        //
        // Create our registry extension.
        RegistryHelper helper = new RegistryHelper(
            registry
            );
        //
        // Get the list of service kind entries.
        RegistryHelper.ServiceInformation info = helper.service(
            ivorn
            );
        //
        // Check our list contains the required entry.
        assertTrue(
            info.provides(
                "org.astrogrid",
                "FileManagerKind"
                )
            );
        }
    }

