/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/client/src/java/org/astrogrid/store/adapter/aladin/AladinAdapterFile.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 11:27:39 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 * <cvs:log>
 *   $Log: AladinAdapterFile.java,v $
 *   Revision 1.5  2005/01/13 11:27:39  jdt
 *   Merges from myspace-nww-890
 *
 *   Revision 1.4.8.1  2005/01/12 17:07:44  nw
 *   added getURL to interface
 *
 *   Revision 1.4  2004/11/17 16:22:53  clq2
 *   nww-itn07-704
 *
 *   Revision 1.3.16.2  2004/11/16 17:27:59  nw
 *   tidied imports
 *
 *   Revision 1.3.16.1  2004/11/16 16:47:28  nw
 *   copied aladinAdapter interfaces into a neutrally-named package.
 *   deprecated original interfaces.
 *   javadoc
 *
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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * A wrapper for the AstroGrid StoreFile to make it easier to integrate into Aladin.
 * * @deprecated use {@link org.astrogrid.store.tree} instead
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

	

/** access a URL from which the contents of this file can be read.
 * @deprecated doubly - only possible with legacy myspace implementation.
 * @return http / ftp url to file contents.
 * @throws AladinAdapterServiceExcepiton
 */
    public URL getURL() throws AladinAdapterServiceException;
}