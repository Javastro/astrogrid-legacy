/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/store/tree/File.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:37:05 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: File.java,v $
 *   Revision 1.2  2005/03/11 13:37:05  clq2
 *   new filemanager merged with filemanager-nww-jdt-903-943
 *
 *   Revision 1.1.2.1  2005/02/27 23:03:12  nw
 *   first cut of talking to filestore
 *
 *   Revision 1.2  2004/11/17 16:22:53  clq2
 *   nww-itn07-704
 *
 *   Revision 1.1.2.2  2004/11/16 17:27:58  nw
 *   tidied imports
 *
 *   Revision 1.1.2.1  2004/11/16 16:47:28  nw
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
package org.astrogrid.store.tree;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Representation of a File in an Astrogrid Store
 *
 *@deprecated - just here for backwards compatability. use client accessed from  {@link org.astrogrid.filemanager.client.FileManagerClientFactory} instead.
 */
public interface File
	extends Node
	{

		/**
		 * Get the mime type for the file.
		 * @return The mime type for the file contents, or null if is not set..
	     * @throws TreeClientServiceException If the service is unable to handle the request.
		 *
		 */
		public String getMimeType() ;

	/**
	 * Get an OutputStream to send data to the file.
	 * Openning a new stream to an existing file will over-write the file contents.
	 * The client MUST close the output stream to force the transfer to complete.
     * @throws TreeClientServiceException If the service is unable to handle the request.
	 *
	 */
	public OutputStream getOutputStream()
		throws TreeClientServiceException ;

	/**
	 * Get an InputStream to read data from the file.
     * @throws TreeClientServiceException If the service is unable to handle the request.
	 *
	 */
	public InputStream getInputStream()
		throws TreeClientServiceException ;

	}
