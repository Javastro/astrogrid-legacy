/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/filestore/integration/FileStoreIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/27 13:41:17 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreIntegrationTest.java,v $
 *   Revision 1.4  2004/07/27 13:41:17  dave
 *   Merged development branch, dave-dev-200407261230, into HEAD
 *
 *   Revision 1.3.12.1  2004/07/27 13:27:07  dave
 *   Added registry entry and resolver to integtation tests
 *
 *   Revision 1.3  2004/07/22 13:40:26  dave
 *   Merged development branch, dave-dev-200407211922, into HEAD
 *
 *   Revision 1.2.4.1  2004/07/22 13:10:37  dave
 *   Updated integration test config
 *
 *   Revision 1.2  2004/07/19 23:42:07  dave
 *   Merged development branch, dave-dev-200407151443, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/16 13:05:34  dave
 *   Renamed filestore test
 *
 *   Revision 1.1.2.1  2004/07/16 12:40:02  dave
 *   Added filestore tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.integration ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.filestore.common.FileStoreTest ;
import org.astrogrid.filestore.client.FileStoreSoapDelegate ;
import org.astrogrid.filestore.client.FileStoreCoreDelegate ;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolver ;

/**
 * A JUnit test case for the store service.
 *
 */
public class FileStoreIntegrationTest
	extends FileStoreTest
	{

    /**
     * Setup our test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Create our test targets.
		FileStoreDelegateResolver resolver = new FileStoreDelegateResolver() ;
		this.target = 
			(FileStoreCoreDelegate)
			resolver.resolve(
				new Ivorn(
					getConfigProperty(
						"org.astrogrid.filestore.service"
						)
					)
				) ;
		}

	/**
	 * Our AstroGrid configuration.
	 */
	private static Config config = SimpleConfig.getSingleton();

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

