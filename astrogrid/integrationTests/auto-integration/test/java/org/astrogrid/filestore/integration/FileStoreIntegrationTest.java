/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/filestore/integration/FileStoreIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/23 16:14:55 $</cvs:date>
 * <cvs:version>$Revision: 1.6 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreIntegrationTest.java,v $
 *   Revision 1.6  2004/08/23 16:14:55  dave
 *   Changed integration test configuration to run two filestores.
 *
 *   Revision 1.5.8.3  2004/08/21 00:04:32  dave
 *   Changed myspace manager to use filestrore one.
 *   Changed filestore tests to use filestore two.
 *
 *   Revision 1.5.8.2  2004/08/20 21:29:33  dave
 *   Moved test properties back to main config.
 *
 *   Revision 1.5.8.1  2004/08/20 21:08:01  dave
 *   Separated out config and deployment for filestore-one.
 *
 *   Revision 1.5  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
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
		// Initialise our config.
		config = SimpleConfig.getSingleton();
        //
        // Create our test targets.
		FileStoreDelegateResolver resolver = new FileStoreDelegateResolver() ;
		this.target = 
			(FileStoreCoreDelegate)
			resolver.resolve(
				new Ivorn(
					getConfigProperty(
						"org.astrogrid.filestore.two.ivorn"
						)
					)
				) ;
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

