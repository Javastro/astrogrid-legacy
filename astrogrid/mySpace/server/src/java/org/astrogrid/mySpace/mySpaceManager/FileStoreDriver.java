/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/server/src/java/org/astrogrid/mySpace/mySpaceManager/FileStoreDriver.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/08/18 19:00:01 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreDriver.java,v $
 *   Revision 1.2  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.1.2.12  2004/08/09 11:17:00  dave
 *   Added resource URL and resource IVORN to properties.
 *
 *   Revision 1.1.2.11  2004/08/06 13:43:18  dave
 *   Added initial filestore copy
 *
 *   Revision 1.1.2.10  2004/08/05 16:58:28  dave
 *   Added delete to filestore driver
 *
 *   Revision 1.1.2.9  2004/08/05 15:44:27  dave
 *   Added delete to filestore driver
 *
 *   Revision 1.1.2.8  2004/08/05 15:39:10  dave
 *   Added delete to filestore driver
 *
 *   Revision 1.1.2.7  2004/08/04 04:55:21  dave
 *   Refactored Actions to use filestore for get URL transfer ....
 *
 *   Revision 1.1.2.6  2004/08/04 02:56:43  dave
 *   Reafactored Action.getBytes to use filestore
 *
 *   Revision 1.1.2.5  2004/08/04 01:51:03  dave
 *   Refactored Actions and FileStoreDriver to use Ivorn rather than Uri
 *
 *   Revision 1.1.2.4  2004/08/02 18:19:15  dave
 *   Added export string to filestore driver
 *
 *   Revision 1.1.2.3  2004/08/02 16:14:41  dave
 *   Added debug for property
 *
 *   Revision 1.1.2.2  2004/08/02 14:54:15  dave
 *   Trying to get integration tests to run
 *
 *   Revision 1.1.2.1  2004/07/28 05:01:09  dave
 *   Started adding the FileStore driver .... extremely broke at the moment
 *
 * </cvs:log>
 *
 */
package org.astrogrid.mySpace.mySpaceManager ;

import org.astrogrid.mySpace.mySpaceStatus.Logger ;

import java.net.URL ;
import java.net.URISyntaxException ;
import java.net.MalformedURLException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.client.FileStoreDelegate ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;

import org.astrogrid.filestore.common.transfer.UrlGetTransfer ;

import org.astrogrid.filestore.resolver.FileStoreDelegateResolver ;
import org.astrogrid.filestore.resolver.FileStoreResolverException ;

import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;

import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;
import org.astrogrid.filestore.common.exception.FileStoreTransferException ;


/**
 * A wrapper class to integrate the FileStore delegate into the MySpaceManager.
 *
 */
public class FileStoreDriver
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * A reference to our FileStore delegate.
	 *
	 */
	private FileStoreDelegate filestore ;

	/**
	 * Public constructor using a specific filestore delegate.
	 * @paran filestore Our filestore delegate.
	 *
	 */
	public FileStoreDriver(FileStoreDelegate filestore)
		{
		if (null == filestore)
			{
			throw new IllegalArgumentException(
				"Null filestore delegate"
				) ;
			}
		this.filestore = filestore ;
		}

	/**
	 * A factory to create a new FileStoreDriver using the default registry and filestore.
	 * This uses the astrogrid configuration to lookup the default registry URL.
	 * This uses the MMC configurator to lookup the default filestore ivorn.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     * @throws FileStoreResolverException If unable to resolve the identifier.
	 *
	 */
	public static FileStoreDriver create()
		throws FileStoreResolverException, FileStoreIdentifierException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.create()") ;
		try {
			String property = MMC.getProperty(
				"MYSPACESERVERURL",
				"MYSPACEMANAGER"
				) ;
			if (DEBUG_FLAG) System.out.println("  property : " + property) ;
			return create(
				new Ivorn(
				 	property
					)
				) ;
			}
		catch(FileStoreResolverException ouch)
			{
			throw ouch ;
			}
		catch(FileStoreIdentifierException ouch)
			{
			throw ouch ;
			}
		catch(Throwable ouch)
			{
			throw new FileStoreResolverException(
				"Failed to resolve filestore delegate",
				ouch
				) ;
			}
		}

	/**
	 * A factory to create a new FileStoreDriver using the default registry.
	 * This uses the astrogrid configuration to lookup the default registry URL.
	 * @param ivorn The ivorn of the filestore service.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     * @throws FileStoreResolverException If unable to resolve the identifier.
	 *
	 */
	public static FileStoreDriver create(String ivorn)
		throws FileStoreResolverException, FileStoreIdentifierException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.create()") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
		try {
			return create(
				new Ivorn(
					ivorn
					)
				) ;
			}
		catch (URISyntaxException ouch)
			{
			throw new FileStoreIdentifierException(
				ouch
				) ;
			}
		}

	/**
	 * A factory to create a new FileStoreDriver using the default registry.
	 * This uses the astrogrid configuration to lookup the default registry URL.
	 * @param ivorn The ivorn of the filestore service.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     * @throws FileStoreResolverException If unable to resolve the identifier.
	 *
	 */
	public static FileStoreDriver create(Ivorn ivorn)
		throws FileStoreResolverException, FileStoreIdentifierException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.create()") ;
		if (DEBUG_FLAG) System.out.println("  Ivorn : " + ivorn) ;
		//
		// Create our resolver using the default registry.
		FileStoreDelegateResolver resolver =
			new FileStoreDelegateResolver() ;
		//
		// Use our resolver to create our filestore delegate.
		return new FileStoreDriver(
			resolver.resolve(
				ivorn
				)
			) ;
		}

	/**
	 * A factory to create a new FileStoreDriver.
	 * @paran ivorn The ivorn for our FileStore service.
	 * @paran registry The endpoint URL for our registry service.
     * @throws FileStoreIdentifierException If the identifier is not valid.
     * @throws FileStoreResolverException If unable to resolve the identifier.
	 *
	 */
	public static FileStoreDriver create(Ivorn ivorn, URL registry)
		throws FileStoreResolverException, FileStoreIdentifierException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.create()") ;
		if (DEBUG_FLAG) System.out.println("  Registry : " + registry) ;
		if (DEBUG_FLAG) System.out.println("  Ivorn    : " + ivorn) ;
		//
		// Create our resolver using the registry endpoint.
		FileStoreDelegateResolver resolver =
			new FileStoreDelegateResolver(
				registry
				) ;
		//
		// Use our resolver to create our filestore delegate.
		return new FileStoreDriver(
			resolver.resolve(
				ivorn
				)
			) ;
		}

	/**
	 * Import (store) a string in a new file.
	 * @param item The data item record for the data.
	 * @param data The string of data to store.
	 * @throws FileStoreException if the data string is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public void importString(DataItemRecord item, String data)
		throws FileStoreServiceException, FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.importString()") ;
		//
		// Transfer the data, and update the item with the results.
		updateDataItem(
			item,
			filestore.importString(
				null,
				data
				)
			) ;
		}

	/**
	 * Import (store) an array of bytes in a new file.
	 * @param item The data item record for the data.
	 * @param data The data to store.
	 * @throws FileStoreException if the data string is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public void importBytes(DataItemRecord item, byte[] data)
		throws FileStoreServiceException, FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.importBytes()") ;
		//
		// Transfer the data, and update the item with the results.
		updateDataItem(
			item,
			filestore.importBytes(
				null,
				data
				)
			) ;
		}

	/**
	 * Append (store) a string to an existing file.
	 * @param item The data item record for the data.
	 * @param data The string of data to store.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreException if the string is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public void appendString(DataItemRecord item, String data)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException, FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.appendString()") ;
		//
		// Transfer the data, and update the item with the results.
		updateDataItem(
			item,
			filestore.appendString(
				item.getDataItemFile(),
				data
				)
			) ;
		}

	/**
	 * Append (store) an array of bytes to an existing file.
	 * @param item The data item record for the data.
	 * @param data The data to store.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreException if the string is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public void appendBytes(DataItemRecord item, byte[] data)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException, FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.appendBytes()") ;
		//
		// Transfer the data, and update the item with the results.
		updateDataItem(
			item,
			filestore.appendBytes(
				item.getDataItemFile(),
				data
				)
			) ;
		}

	/**
	 * Import (store) the contents of a URL in a new file.
	 * @param item The data item record for the data.
	 * @param source The source URL.
	 * @throws FileStoreServiceException if unable handle the request.
	 * @throws FileStoreTransferException if the transfer properties not valid.
	 *
	 */
	public void importUrl(DataItemRecord item, URL source)
		throws FileStoreServiceException, FileStoreTransferException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.importUrl()") ;
		//
		// Transfer the data, and update the item with the results.
		updateDataItem(
			item,
			filestore.importData(
				new UrlGetTransfer(
					source
					)
				)
				.getFileProperties()
			) ;
		}

	/**
	 * Get a file contents as a string.
	 * @param item The data item record for the data.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public String exportString(DataItemRecord item)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.exportString()") ;
		return filestore.exportString(
			item.getDataItemFile()
			) ;
		}

	/**
	 * Get a file contents as a byte array.
	 * @param item The data item record for the data.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public byte[] exportBytes(DataItemRecord item)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.exportBytes()") ;
		return filestore.exportBytes(
			item.getDataItemFile()
			) ;
		}


	/**
	 * Delete data from the store.
	 * @param item The data item record for the data.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 *
	 */
	public void delete(DataItemRecord item)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.delete()") ;
		updateDataItem(
			item,
			filestore.delete(
				item.getDataItemFile()
				)
			) ;
		}

	/**
	 * Copy a file within the server.
	 * @param source      - the data item to copy from.
	 * @param destination - the data item to copy into.
	 * @throws FileStoreIdentifierException if the source identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public void duplicate(DataItemRecord source, DataItemRecord destination)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.duplicate()") ;
		updateDataItem(
			destination,
			filestore.duplicate(
				source.getDataItemFile(),
				null
				)
			) ;
		}

	/**
	 * Update a DataItemRecord from data in an array of FileProperty.
	 * @param item - the myspace data item to update.
	 * @param properties - the original properties from the file store.
	 *
	 */
	private void updateDataItem(DataItemRecord item, FileProperty[] properties)
		{
		updateDataItem(
			item,
			new FileProperties(
				properties
				)
			) ;
		}

	/**
	 * Update a DataItemRecord from data in a FileProperties.
	 * @param item - the myspace data item to update.
	 * @param properties - the original properties from the file store.
	 *
	 */
	private void updateDataItem(DataItemRecord item, FileProperties properties)
		{
		//
		// Update the data item identifier.
		item.setDataItemFile(
			properties.getResourceIdent()
			) ;
		//
		// Update the data item Ivorn.
		item.setDataItemIvorn(
			properties.getResourceIvorn().toString()
			) ;
		//
		// Update the data item URL.
		item.setDataItemUri(
			properties.getResourceUrl().toString()
			) ;
		}
	}

