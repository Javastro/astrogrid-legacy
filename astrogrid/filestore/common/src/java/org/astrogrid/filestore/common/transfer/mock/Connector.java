/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/transfer/mock/Connector.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:19 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: Connector.java,v $
 *   Revision 1.2  2004/11/25 00:19:19  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/10/21 21:00:13  dave
 *   Added mock://xyz URL handler to enable testing of transfer.
 *   Implemented importInit to the mock service and created transfer tests.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.transfer.mock ;

import java.io.InputStream ;
import java.io.OutputStream ;
import java.io.IOException ;

/**
 * An interface to handle creating IOStreams for a mock URLConnection.
 *
 */
public interface Connector
	{
	/**
	 * Get an InputStream to the URL content.
	 *
	 */
	public InputStream getInputStream()
		throws IOException ;

	/**
	 * Get an OutputStream to the URL content.
	 *
	 */
	public OutputStream getOutputStream()
		throws IOException ;

	}
