/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/filestore/integration/FileStoreIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/19 23:42:07 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreIntegrationTest.java,v $
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

import org.astrogrid.filestore.common.FileStoreTest ;
import org.astrogrid.filestore.client.FileStoreSoapDelegate ;

/**
 * A JUnit test case for the store service.
 *
 */
public class FileStoreIntegrationTest
	extends FileStoreTest
	{
	/**
	 * URL for the service to test.
	 * Temp fix, until I get the service registered.
	 *
	 */
	public static final String SERVICE_ENDPOINT = "http://localhost:8080/astrogrid-filestore-SNAPSHOT/services/FileStore" ;

    /**
     * Setup our test.
     *
     */
    public void setUp()
        throws Exception
        {
        //
        // Create our test targets.
		this.target = new FileStoreSoapDelegate(
			SERVICE_ENDPOINT
			) ;
		}
	}

