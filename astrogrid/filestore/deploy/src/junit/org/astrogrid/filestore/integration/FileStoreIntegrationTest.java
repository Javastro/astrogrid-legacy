/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/deploy/src/junit/org/astrogrid/filestore/integration/FileStoreIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/14 13:58:43 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreIntegrationTest.java,v $
 *   Revision 1.2  2005/03/14 13:58:43  clq2
 *   dave-dev-200503140252
 *
 *   Revision 1.1.2.1  2005/03/14 03:18:19  dave
 *   Added installation documentation - bugzilla 991
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
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverImpl ;

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
		FileStoreDelegateResolver resolver = new FileStoreDelegateResolverImpl() ;
		this.target = 
			(FileStoreCoreDelegate)
			resolver.resolve(
				new Ivorn(
					getConfigProperty(
						"org.astrogrid.filestore.test.ivorn"
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

