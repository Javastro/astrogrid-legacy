/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/src/java/org/astrogrid/store/adapter/aladin/AladinAdapterFile.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/10/05 15:39:29 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: AladinAdapterFile.java,v $
 *   Revision 1.3  2004/10/05 15:39:29  dave
 *   Merged changes to AladinAdapter ...
 *
 *   Revision 1.2.4.1  2004/10/05 15:30:44  dave
 *   Moved test base from test to src tree ....
 *   Added MimeTypeUtil
 *   Added getMimeType to the adapter API
 *   Added logout to the adapter API
 *
 *   Revision 1.2  2004/09/28 10:24:19  dave
 *   Added AladinAdapter interfaces and mock implementation.
 *
 *   Revision 1.1.2.4  2004/09/27 22:46:53  dave
 *   Added AdapterFile interface, with input and output stream API.
 *
 *   Revision 1.1.2.1  2004/09/24 01:36:18  dave
 *   Refactored File as Node and Container ...
 *
 *   Revision 1.1.2.2  2004/09/24 01:12:09  dave
 *   Added initial test for child nodes.
 *
 *   Revision 1.1.2.1  2004/09/23 16:32:01  dave
 *   Added better Exception handling ....
 *   Added initial mock container ....
 *   Added initial root container tests ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.store.adapter.aladin ;

import java.io.InputStream ;
import java.io.OutputStream ;

/**
 * A wrapper for the AstroGrid StoreFile to make it easier to integrate into Aladin.
 *
 */
public interface AladinAdapterFile
	extends AladinAdapterNode
	{

		/**
		 * Get the mime type for the file.
		 * @return The mime type for the file contents, or null if is not set..
	     * @throws AladinAdapterServiceException If the service is unable to handle the request.
		 *
		 */
		public String getMimeType() ;

	/**
	 * Get an OutputStream to send data to the file.
	 * Openning a new stream to an existing file will over-write the file contents.
	 * The client MUST close the output stream to force the transfer to complete.
     * @throws AladinAdapterServiceException If the service is unable to handle the request.
	 *
	 */
	public OutputStream getOutputStream()
		throws AladinAdapterServiceException ;

	/**
	 * Get an InputStream to read data from the file.
     * @throws AladinAdapterServiceException If the service is unable to handle the request.
	 *
	 */
	public InputStream getInputStream()
		throws AladinAdapterServiceException ;

	}
