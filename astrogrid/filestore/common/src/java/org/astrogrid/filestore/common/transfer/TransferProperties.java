/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/transfer/TransferProperties.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:20 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: TransferProperties.java,v $
 *   Revision 1.4  2004/11/25 00:19:20  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.3.30.5  2004/11/17 19:06:30  dave
 *   Updated server configuration ...
 *
 *   Revision 1.3.30.4  2004/11/06 12:17:36  dave
 *   Modified getServiceIdent() to return full ivorn string.
 *
 *   Revision 1.3.30.3  2004/10/29 15:54:50  dave
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

import org.astrogrid.filestore.common.file.FileProperty;
import org.astrogrid.filestore.common.file.FileProperties;

import org.astrogrid.filestore.common.FileStoreInputStream;
import org.astrogrid.filestore.common.FileStoreOutputStream;

import org.astrogrid.filestore.common.identifier.UniqueIdentifier;

/**
 * An object to hold metadata about a data transfer.
 *
 */
public class TransferProperties
	{

	/**
	 * Public constructor.
	 * Required for Axis to serialize this as a Bean.
	 *
	 */
	public TransferProperties()
		{
		}

	/**
	 * Public constructor.
	 * @param ident A unique identifier for the transfer.
	 *
	 */
	public TransferProperties(String ident)
		{
		this(
			ident,
			(FileProperty[]) null
			) ;
		}

	/**
	 * Public constructor.
	 * @param properties The properties for the file to transfer
	 *
	 */
	public TransferProperties(FileProperties properties)
		{
		this(
			null,
			(null != properties) ? properties.toArray() : (FileProperty[]) null
			) ;
		}

	/**
	 * Public constructor.
	 * @param ident A unique identifier for the transfer.
	 * @param properties The properties for the file to transfer
	 *
	 */
	public TransferProperties(String ident, FileProperties properties)
		{
		this(
			ident,
			(null != properties) ? properties.toArray() : (FileProperty[]) null
			) ;
		}

	/**
	 * Public constructor.
	 * @param ident A unique identifier for the transfer.
	 * @param properties The properties for the file to transfer
	 *
	 */
	public TransferProperties(String ident, FileProperty[] properties)
		{
		this.setIdent(
			ident
			) ;
		this.setFileProperties(
			properties
			) ;
		}

	/**
	 * The transfer identifier.
	 *
	 */
	private String ident ;

	/**
	 * Access to our ident.
	 *
	 */
	public String getIdent()
		{
		return this.ident ;
		}

	/**
	 * Access to our ident.
	 *
	 */
	public void setIdent(String value)
		{
		this.ident = value ;
		}

	/**
	 * The transfer location (URL).
	 * eg http://host.domain/path/file
	 *
	 */
	private String location ;

	/**
	 * Access to the transfer location (URL).
	 * eg http://host.domain/path/file
	 *
	 */
	public String getLocation()
		{
		return this.location ;
		}

	/**
	 * Access to the transfer location (URL).
	 * eg http://host.domain/path/file
	 * @param The transfer location (RL).
	 *
	 */
	public void setLocation(String value)
		{
		this.location = value ;
		}

	/**
	 * The transfer protocol.
	 * eg http, ftp
	 * @todo generate this from our URL ?
	 *
	 */
	private String protocol ;

	/**
	 * Access to the transfer protocol.
	 * eg http, ftp
	 * @todo generate this from our URL ?
	 *
	 */
	public String getProtocol()
		{
		return this.protocol ;
		}

	/**
	 * Access to the transfer protocol.
	 * eg http, ftp
	 * @todo generate this from our URL ?
	 *
	 */
	public void setProtocol(String value)
		{
		this.protocol = value ;
		}

	/**
	 * The transfer method.
	 * eg GET, PUT, POST
	 *
	 */
	private String method ;

	/**
	 * Access to the transfer method.
	 * eg GET, PUT, POST
	 *
	 */
	public String getMethod()
		{
		return this.method ;
		}

	/**
	 * Access to the transfer method.
	 * eg GET, PUT, POST
	 *
	 */
	public void setMethod(String value)
		{
		this.method = value ;
		}

	/**
	 * The file properties for the transferred data.
	 *
	 */
	private FileProperty[] properties ;

	/**
	 * Access to our file properties.
	 *
	 */
	public FileProperty[] getFileProperties()
		{
		return this.properties ;
		}

	/**
	 * Set the transfer properties.
	 *
	 */
	public void setFileProperties(FileProperties properties)
		{
		this.setFileProperties(
			properties.toArray()
			);
		}

	/**
	 * Set the transfer properties.
	 *
	 */
	public void setFileProperties(FileProperty[] properties)
		{
		this.properties = properties ;
		}
	}

