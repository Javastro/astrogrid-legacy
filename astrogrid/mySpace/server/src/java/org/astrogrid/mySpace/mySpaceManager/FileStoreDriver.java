/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/mySpace/server/src/java/org/astrogrid/mySpace/mySpaceManager/FileStoreDriver.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/02/17 16:01:10 $</cvs:date>
 * <cvs:version>$Revision: 1.8 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreDriver.java,v $
 *   Revision 1.8  2005/02/17 16:01:10  jdt
 *   Rolled back to 15 Feb 00:00, before community_pah_910, mySpace_pah_910 and Reg_KMB_913
 *
 *   Revision 1.6  2004/11/25 10:52:49  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.5.24.3  2004/11/19 11:11:28  dave
 *   Updated FileStoreDriver to match changes in FileStore
 *
 *   Revision 1.5.24.2  2004/11/02 23:18:17  dave
 *   Updated to match changes to FileStore FileProperties ...
 *
 *   Revision 1.5.24.1  2004/10/26 13:34:27  dave
 *   Updated FileStoreDelegateResolver to FileStoreDelegateResolverImpl ....
 *
 *   Revision 1.5  2004/09/09 01:19:50  dave
 *   Updated MIME type handling in MySpace.
 *   Extended test coverage for MIME types in FileStore and MySpace.
 *   Added VM memory data to community ServiceStatusData.
 *
 *   Revision 1.4.6.3  2004/09/08 20:59:40  dave
 *   Added check for fake null in mime type ....
 *
 *   Revision 1.4.6.2  2004/09/08 14:06:35  dave
 *   Added check for existing mime type.
 *
 *   Revision 1.4.6.1  2004/09/08 13:20:24  dave
 *   Updated mime type handling and tests ...
 *
 *   Revision 1.4  2004/09/02 10:25:41  dave
 *   Updated FileStore and MySpace to handle mime type and file size.
 *   Updated Community deployment script.
 *
 *   Revision 1.3.2.1  2004/09/01 03:01:48  dave
 *   Updated to pass mime type to filestore.
 *
 *   Revision 1.3  2004/08/27 22:43:15  dave
 *   Updated filestore and myspace to report file size correctly.
 *
 *   Revision 1.2.12.1  2004/08/27 14:06:52  dave
 *   Modified FileStoreDriver and DataItemRecord to propagate size.
 *
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
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverImpl ;
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
	 * Property key for the myspace file name.
	 *
	 */
	public static final String MYSPACE_NAME_PROPERTY = "org.astrogrid.myspace.name" ;

	/**
	 * Property key for the myspace file ident.
	 *
	 */
	public static final String MYSPACE_IDENT_PROPERTY = "org.astrogrid.myspace.ident" ;

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
			new FileStoreDelegateResolverImpl() ;
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
			new FileStoreDelegateResolverImpl(
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
				initProperties(item),
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
				initProperties(item),
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
		throws FileStoreServiceException, FileStoreTransferException, FileStoreIdentifierException
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
					source,
					initProperties(item)
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
	 * @throws FileStoreTransferException if unable to transfer the data.
	 * @throws FileStoreIdentifierException if the source identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public void duplicate(DataItemRecord source, DataItemRecord destination)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException, FileStoreTransferException
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
		throws FileStoreIdentifierException
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
		throws FileStoreIdentifierException
		{
		//
		// Update the data item properties.
		item.setDataItemFile(
			properties.getStoreResourceIdent()
			) ;
		item.setDataItemIvorn(
			properties.getStoreResourceIvorn().toString()
			) ;
		item.setDataItemUri(
			properties.getStoreResourceUrl().toString()
			) ;
		item.setSize(
			properties.getContentSize()
			) ;
		item.setDataItemMime(
			properties.getContentType()
			) ;
		}

	/**
	 * Create an initial set of properties for an item.
	 * This includes the guess for the mime type based on the file name.
	 * @param item The data item record.
	 * @return An array of properties for the item.
	 *
	 */
	private FileProperty[] initProperties(DataItemRecord item)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreDriver.initProperties()") ;
		if (DEBUG_FLAG) System.out.println("  Name : " + item.getDataItemName()) ;
		if (DEBUG_FLAG) System.out.println("  Mime : " + item.getDataItemMime()) ;
		//
		// Create our file properties.
		FileProperties properties = new FileProperties() ;
		//
		// Set the myspace properties.
		properties.setProperty(
			MYSPACE_NAME_PROPERTY,
			item.getDataItemName()
			) ;
		properties.setProperty(
			MYSPACE_IDENT_PROPERTY,
			String.valueOf(item.getDataItemID())
			) ;
		//
		// Get the current mime type.
		String mime = item.getDataItemMime() ;
		//
		// Check for a fake null.
		if ("null".equals(mime))
			{
			if (DEBUG_FLAG) System.out.println("  Fake null in data item mime type.") ;
			mime = null ;
			}
		//
		// If the mime type has not already been set.
		if (null == mime)
			{
			if (DEBUG_FLAG) System.out.println("  Null mime type in data item.") ;
			if (DEBUG_FLAG) System.out.println("  Generating mime type from name.") ;
			//
			// Get the item file name.
			String name = item.getDataItemName() ;
			//
			// If we have a file name.
			if (null != name)
				{
				//
				// Find the last '.' in the name.
				int index = name.lastIndexOf('.') ;
				//
				// If we found a '.' in the name.
				if (index != -1)
					{
					//
					// Check for recognised types.
					String type = name.substring(
						index
						).toLowerCase() ;
					//
					// Vanilla XML.
					if (".xml".equals(type))
						{
						mime = FileProperties.MIME_TYPE_XML ;
						}
					//
					// Vanilla XSL.
					if (".xsl".equals(type))
						{
						mime = FileProperties.MIME_TYPE_XML ;
						}
					//
					// Astrogrid VoTable.
					if (".vot".equals(type))
						{
						mime = FileProperties.MIME_TYPE_VOTABLE ;
						}
					if (".votable".equals(type))
						{
						mime = FileProperties.MIME_TYPE_VOTABLE ;
						}
					//
					// Astrogrid VoList.
					if (".vol".equals(type))
						{
						mime = FileProperties.MIME_TYPE_VOLIST ;
						}
					if (".volist".equals(type))
						{
						mime = FileProperties.MIME_TYPE_VOLIST ;
						}
					//
					// Astrogrid Job details.
					if (".job".equals(type))
						{
						mime = FileProperties.MIME_TYPE_JOB ;
						}
					//
					// Astrogrid workflow.
					if (".work".equals(type))
						{
						mime = FileProperties.MIME_TYPE_WORKFLOW ;
						}
					if (".flow".equals(type))
						{
						mime = FileProperties.MIME_TYPE_WORKFLOW ;
						}
					if (".workflow".equals(type))
						{
						mime = FileProperties.MIME_TYPE_WORKFLOW ;
						}
					//
					// Astrogrid ADQL.
					if (".adql".equals(type))
						{
						mime = FileProperties.MIME_TYPE_ADQL ;
						}
					}
				}
			}
		//
		// If we found a mime type.
		if (null != mime)
			{
			//
			// Set the mime property.
			properties.setProperty(
				FileProperties.MIME_TYPE_PROPERTY,
				mime
				) ;
			}
		//
		// Return the new properties as an array.
		return properties.toArray() ;
		}
	}

