/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/filemanager/integration/Attic/FileManagerClientIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/02/18 12:09:31 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerClientIntegrationTest.java,v $
 *   Revision 1.4  2005/02/18 12:09:31  clq2
 *   roll back to before 889
 *
 *   Revision 1.2  2005/01/28 10:44:00  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.2  2005/01/25 12:20:12  dave
 *   Fixed bug in test case ..
 *
 *   Revision 1.1.2.1  2005/01/25 11:22:09  dave
 *   Refactored client test ..
 *
 *   Revision 1.1.2.2  2005/01/23 07:02:03  dave
 *   Fixed dumb bug in test case ...
 *
 *   Revision 1.1.2.1  2005/01/23 06:17:40  dave
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
package org.astrogrid.filemanager.integration;

import junit.framework.TestCase ;

import org.astrogrid.store.Ivorn;

import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientTest;
import org.astrogrid.filemanager.client.FileManagerClientFactory;

/**
 * Integration test for the FileManagerClient.
 *
 */
public class FileManagerClientIntegrationTest
    extends FileManagerClientTest
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
     * Helper method to get a local property.
     *
     */
    public String getTestProperty(String name)
        {
        return (String) config.getProperty(
            TEST_PROPERTY_PREFIX + "." + name
            ) ;
        }

    /**
     * Setup our test.
     *
     */
    public void setUp()
        throws Exception
        {
        super.setUp();
        //
        // Initialise our config.
        config = SimpleConfig.getSingleton();
        //
        // Setup our filestore identifiers.
        filestores = new Ivorn[]
            {
            new Ivorn(
                getConfigProperty(
                    "org.astrogrid.filestore.one.ivorn"
                    )
                ),
            new Ivorn(
                getConfigProperty(
                    "org.astrogrid.filestore.two.ivorn"
                    )
                )
            } ;
        }

    /**
     * Check that we can create a factory.
     *
     */
    public void testCreateFactory()
        throws Exception
        {
        FileManagerClientFactory factory = new FileManagerClientFactory();
        assertNotNull(
            factory
            );
        }

    /**
     * Check that we can create a client.
     *
     */
    public void testLoginAnon()
        throws Exception
        {
        FileManagerClientFactory factory = new FileManagerClientFactory();
        assertNotNull(
            factory
            );
        FileManagerClient client = factory.login();
        assertNotNull(
            client
            );
        }
    }
