/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/transfer/TransferProperties.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/21 18:11:55 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: TransferProperties.java,v $
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

import org.astrogrid.filestore.common.file.FileProperty ;

import org.astrogrid.filestore.common.identifier.UniqueIdentifier ;

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
	public TransferProperties(UniqueIdentifier ident)
		{
		this(ident, null) ;
		}

	/**
	 * Public constructor.
	 * @param ident A unique identifier for the transfer.
	 * @param file  The properties for the file to transfer
	 *
	 */
	public TransferProperties(UniqueIdentifier ident, FileProperty[] file)
		{
		//
		// Check for a null url.
		if (null == ident)
			{
			throw new IllegalArgumentException(
				"Null identifier"
				) ;
			}
		//
		// Set our properties.
		this.setIdent(
			ident.toString()
			) ;
		this.setFileProperties(
			file
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
	 * The transfer source URL.
	 * eg http://host.domain/path/file
	 *
	 */
	private String source ;

	/**
	 * Access to the source URL.
	 * eg http://host.domain/path/file
	 *
	 */
	public String getSource()
		{
		return this.source ;
		}

	/**
	 * Access to the source URL.
	 * eg http://host.domain/path/file
	 *
	 */
	public void setSource(String value)
		{
		this.source = value ;
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
	 * Access to our file properties.
	 *
	 */
	public void setFileProperties(FileProperty[] properties)
		{
		this.properties = properties ;
		}

	}

