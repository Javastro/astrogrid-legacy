/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/filemanager/integration/delegate/Attic/FileManagerDelegateIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/02/18 12:09:31 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerDelegateIntegrationTest.java,v $
 *   Revision 1.4  2005/02/18 12:09:31  clq2
 *   roll back to before 889
 *
 *   Revision 1.2  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.1  2005/01/22 08:00:59  dave
 *   Refactored tests into delegate package ..
 *
 *   Revision 1.3.8.1  2005/01/21 14:38:47  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.3  2004/12/17 16:31:52  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.3  2004/12/08 04:23:35  dave
 *   Updated FileManager test to include FileStore ivorns ...
 *
 *   Revision 1.1.2.2  2004/11/18 16:41:25  dave
 *   Added call to super.setUp()
 *
 *   Revision 1.1.2.1  2004/11/18 16:04:17  dave
 *   Added FileManager integration test ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.integration.delegate;

import org.astrogrid.store.Ivorn;

import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;

import org.astrogrid.filemanager.client.delegate.FileManagerDelegate;
import org.astrogrid.filemanager.client.delegate.FileManagerDelegateTest;
import org.astrogrid.filemanager.client.delegate.FileManagerCoreDelegate;

import org.astrogrid.filemanager.resolver.FileManagerDelegateResolver;
import org.astrogrid.filemanager.resolver.FileManagerDelegateResolverImpl;

/**
 * A JUnit test case for the live FileManager service.
 *
 */
public class FileManagerDelegateIntegrationTest
    extends FileManagerDelegateTest
    {

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
        // Create our target delegate.
        FileManagerDelegateResolver resolver = new FileManagerDelegateResolverImpl() ;
        this.delegate = 
            (FileManagerCoreDelegate)
            resolver.resolve(
                new Ivorn(
                    getConfigProperty(
                        "org.astrogrid.filemanager.test.ivorn"
                        )
                    )
                ) ;
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

    }
