/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/transfer/UrlGetRequest.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:20 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: UrlGetRequest.java,v $
 *   Revision 1.2  2004/11/25 00:19:20  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/10/29 15:54:50  dave
 *   Added exportInit to mock implementation ...
 *   Added UrlGetRequest to pass into exportInit ...
 *   Added test for exportInit and UrlGetRequest ...
 *
 *   Revision 1.3.30.2  2004/10/26 11:13:11  dave
 *   Changed transfer properties 'source' to 'location', makes more sense for PUT transfers.
 *
 *   Revision 1.3.30.1  2004/10/21 21:00:13  dave
 *   Added mock://xyz URL handler to enable testing of transfer.
 *   Implemented importInit to the mock service and created transfer tests.
 *
 *   Revision 1.3  2004/09/02 10:25:41  dave
 *   Updated FileStore and MySpace to handle mime type and file size.
 *   Updated Community deployment script.
 *
 *   Revision 1.2.46.1  2004/09/01 02:58:07  dave
 *   Updated to use external mime type for imported files.
 *
 *   Revision 1.2  2004/07/21 18:11:55  dave
 *   Merged development branch, dave-dev-200407201059, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/20 19:10:40  dave
 *   Refactored to implement URL import
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.transfer ;

import java.net.URL ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;

import org.astrogrid.filestore.common.identifier.UniqueIdentifier ;

/**
 * A transfer info to hold data about a URL get transfer.
 *
 */
public class UrlGetRequest
	extends TransferProperties
	{

	/**
	 * The transfer method.
	 *
	 */
	public static final String TRANSFER_METHOD = "GET" ;

	/**
	 * Create a new request.
	 * @param url The transfer source.
	 *
	 */
	public UrlGetRequest()
		{
		this((FileProperty[]) null) ;
		}

	/**
	 * Create a new request.
	 * @param url The transfer source.
	 * @param properties The properties for the file to transfer
	 *
	 */
	public UrlGetRequest(FileProperties properties)
		{
		this(
			(null != properties) ? properties.toArray() : (FileProperty[]) null
			) ;
		}

	/**
	 * Create a new request.
	 * @param url The transfer source.
	 * @param properties The properties for the file to transfer
	 *
	 */
	public UrlGetRequest(FileProperty[] properties)
		{
		//
		// Initialise our base class.
		super(
			UniqueIdentifier.next().toString(),
			properties
			) ;
		//
		// Set our transfer properties.
		this.setMethod(TRANSFER_METHOD) ;
		}
	}

