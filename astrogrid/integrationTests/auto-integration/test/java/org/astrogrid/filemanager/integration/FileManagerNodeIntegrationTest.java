/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/filemanager/integration/Attic/FileManagerNodeIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/12/17 16:31:52 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerNodeIntegrationTest.java,v $
 *   Revision 1.2  2004/12/17 16:31:52  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.3  2004/12/08 05:57:15  dave
 *   Updated FileManagerNode test ...
 *
 *   Revision 1.1.2.2  2004/11/26 04:49:10  dave
 *   Fixed typeo ..
 *
 *   Revision 1.1.2.1  2004/11/26 04:40:19  dave
 *   Added integration tests for FileManagerNode ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.integration ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.config.Config ;
import org.astrogrid.config.SimpleConfig ;

import org.astrogrid.filemanager.client.FileManagerNodeTest;
import org.astrogrid.filemanager.client.FileManagerDelegate;
import org.astrogrid.filemanager.client.FileManagerCoreDelegate;

import org.astrogrid.filemanager.resolver.FileManagerDelegateResolver;
import org.astrogrid.filemanager.resolver.FileManagerDelegateResolverImpl;

/**
 * A JUnit test case for the live FileManager service.
 *
 */
public class FileManagerNodeIntegrationTest
	extends FileManagerNodeTest
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
