/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/transfer/UrlPutTransfer.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:20 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: UrlPutTransfer.java,v $
 *   Revision 1.2  2004/11/25 00:19:20  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/10/26 14:39:21  dave
 *   Added extended test to include tests for PUT transfer.
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
 * A transfer info to hold data about a URL put transfer.
 *
 */
public class UrlPutTransfer
	extends TransferProperties
	{

	/**
	 * The transfer method.
	 *
	 */
	public static final String TRANSFER_METHOD = "PUT" ;

	/**
	 * Create a new TransferInfo.
	 *
	 */
	public UrlPutTransfer()
		{
		this((FileProperty[]) null) ;
		}

	/**
	 * Create a new URL transfer.
	 * @param properties The properties for the file to transfer
	 *
	 */
	public UrlPutTransfer(FileProperties properties)
		{
		this(
			(null != properties) ? properties.toArray() : (FileProperty[]) null
			) ;
		}

	/**
	 * Create a new URL transfer.
	 * @param properties The properties for the file to transfer
	 *
	 */
	public UrlPutTransfer(FileProperty[] properties)
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

