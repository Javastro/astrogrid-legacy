/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/filemanager/integration/Attic/FileManagerIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:18:28 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerIntegrationTest.java,v $
 *   Revision 1.2  2004/11/25 00:18:28  jdt
 *   Merge from dave-dev-200410061224-200411221626
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
package org.astrogrid.filemanager.integration ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.filemanager.client.FileManagerDelegate;
import org.astrogrid.filemanager.client.FileManagerDelegateTest;
import org.astrogrid.filemanager.client.FileManagerCoreDelegate;

import org.astrogrid.filemanager.resolver.FileManagerDelegateResolver;
import org.astrogrid.filemanager.resolver.FileManagerDelegateResolverImpl;

/**
 * A JUnit test case for the live FileManager service.
 *
 */
public class FileManagerIntegrationTest
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
        // Create our test targets.
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
