/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/webapp/src/java/org/astrogrid/filestore/webapp/FileStoreSelfTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2006/06/01 14:53:12 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreSelfTest.java,v $
 *   Revision 1.2  2006/06/01 14:53:12  clq2
 *   dave-dev-200605311657 - fix the broken selftests page
 *
 *   Revision 1.1.2.1  2006/06/01 13:01:14  dave
 *   Fixed self test page in webapp
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.webapp ;

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
public class FileStoreSelfTest
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
				new Ivorn(getTestProperty("ivorn"))
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

