/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/client/FileStoreCoreDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/23 09:11:16 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreCoreDelegate.java,v $
 *   Revision 1.5  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.4.6.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
 *
 *   Revision 1.4  2004/07/21 18:11:55  dave
 *   Merged development branch, dave-dev-200407201059, into HEAD
 *
 *   Revision 1.3.2.1  2004/07/21 12:25:59  dave
 *   Updated client to inport from URL
 *
 *   Revision 1.3  2004/07/19 23:42:07  dave
 *   Merged development branch, dave-dev-200407151443, into HEAD
 *
 *   Revision 1.2.4.6  2004/07/19 19:40:28  dave
 *   Debugged and worked around Axis Exception handling
 *
 *   Revision 1.2.4.5  2004/07/16 18:02:28  dave
 *   Added debug to tests
 *
 *   Revision 1.2.4.4  2004/07/16 16:43:19  dave
 *   Added debug
 *
 *   Revision 1.2.4.3  2004/07/16 16:40:21  dave
 *   Added debug
 *
 *   Revision 1.2.4.2  2004/07/16 16:39:09  dave
 *   Added debug
 *
 *   Revision 1.2.4.1  2004/07/16 16:37:45  dave
 *   Added debug
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.7  2004/07/13 16:37:29  dave
 *   Refactored common and client to use an array of FileProperties (more SOAP friendly)
 *
 *   Revision 1.1.2.6  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.5  2004/07/08 07:31:30  dave
 *   Added container impl and tests
 *
 *   Revision 1.1.2.4  2004/07/07 14:54:35  dave
 *   Changed DataInfo to File Info (leaves room to use DataInfo for the more abstract VoStore interface).
 *
 *   Revision 1.1.2.3  2004/07/07 11:55:54  dave
 *   Fixed byte array compare in tests
 *
 *   Revision 1.1.2.2  2004/07/06 10:49:45  dave
 *   Added SOAP delegate
 *
 *   Revision 1.1.2.1  2004/07/06 09:16:12  dave
 *   Added delegate interface and mock implementation
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.client ;

import java.rmi.RemoteException ;

import org.astrogrid.filestore.common.FileStore ;
import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;
import org.astrogrid.filestore.common.exception.FileStoreTransferException ;

/**
 * Core implementation of the delegate interface.
 * Handles unwrapping remote Exceptions from a service call.
 *
 */
public class FileStoreCoreDelegate
	implements FileStore, FileStoreDelegate
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * Reference to our FileStore service.
	 *
	 */
	protected FileStore service ;

	/**
	 * Public constructor.
	 *
	 */
	public FileStoreCoreDelegate()
		{
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FileStoreCoreDelegate()") ;
		}

	/**
	 * Get the service identifier - used for testing.
	 * @return The ivo identifier for this service.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public String identifier()
		throws FileStoreServiceException
		{
		if (null != service)
			{
			try {
				return service.identifier() ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Import (store) a string of data.
	 * @param properties An array of FileProperties describing the imported data.
	 * @param data The string of data to store.
	 * @return An array of FileProperties describing the imported data.
	 * @throws FileStoreException if the data string is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] importString(FileProperty[] properties, String data)
		throws FileStoreServiceException, FileStoreException
		{
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FileStoreCoreDelegate.importString()") ;
        if (DEBUG_FLAG) System.out.println("  Data : " + data) ;
		if (null != service)
			{
			try {
				return service.importString(properties, data) ;
				}
			catch (RemoteException ouch)
				{

				if (DEBUG_FLAG) System.out.println("  >>>>") ;
				if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
				if (DEBUG_FLAG) System.out.println("  Type      : " + ouch.getClass()) ;
				if (DEBUG_FLAG) System.out.println("  Cause     : " + ouch.getCause()) ;
				if (DEBUG_FLAG) System.out.println("  >>>>") ;

				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				filestoreException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Import (store) a byte array of data.
	 * @param properties An array of FileProperties describing the imported data.
	 * @param data The byte array of data to store.
	 * @return An array of FileProperties describing the imported data.
	 * @throws FileStoreException if the data string is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] importBytes(FileProperty[] properties, byte[] data)
		throws FileStoreServiceException, FileStoreException
		{
		if (null != service)
			{
			try {
				return service.importBytes(properties, data) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				filestoreException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Append a string to existing data.
	 * @param ident The internal identifier of the target.
	 * @param data The string to append.
	 * @return An array of FileProperties describing the imported data.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreException if the string is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] appendString(String ident, String data)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException, FileStoreException
		{
		if (null != service)
			{
			try {
				return service.appendString(ident, data) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				notfoundException(ouch) ;
				filestoreException(ouch) ;
				identifierException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Append an array of bytes to existing data.
	 * @param ident The internal identifier of the target.
	 * @param data The bytes to append.
	 * @return An array of FileProperties describing the imported data.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreException if the array is null.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] appendBytes(String ident, byte[] data)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException, FileStoreException
		{
		if (null != service)
			{
			try {
				return service.appendBytes(ident, data) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				notfoundException(ouch) ;
				filestoreException(ouch) ;
				identifierException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Export (read) the contents of a file as a string.
	 * @param ident The internal identifier of the target file.
	 * @return The contents of a data object as a string.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public String exportString(String ident)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("FileStoreCoreDelegate.exportString()") ;
        if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;
		if (null != service)
			{
			try {
				return service.exportString(ident) ;
				}
			catch (RemoteException ouch)
				{
				if (DEBUG_FLAG) System.out.println("  ---- catch ----") ;
				if (DEBUG_FLAG) System.out.println("  Exception : " + ouch.getClass()) ;
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				notfoundException(ouch) ;
				identifierException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Export (read) the contents of a file as an array of bytes.
	 * @param ident The internal identifier of the target file.
	 * @return The contents of a data object as a string.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public byte[] exportBytes(String ident)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		if (null != service)
			{
			try {
				return service.exportBytes(ident) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				notfoundException(ouch) ;
				identifierException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Create a local duplicate (copy) of a file.
	 * @param ident The internal identifier of the target file.
	 * @param properties An optional array of FileProperties describing the copy.
	 * @return An array of FileProperties describing the copied data.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] duplicate(String ident, FileProperty[] properties)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		if (null != service)
			{
			try {
				return service.duplicate(ident, properties) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				notfoundException(ouch) ;
				identifierException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Delete a file.
	 * @param ident The internal identifier of the target file.
	 * @return An array of FileProperties describing the deleted data.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] delete(String ident)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		if (null != service)
			{
			try {
				return service.delete(ident) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				notfoundException(ouch) ;
				identifierException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Get the local meta data information for a file.
	 * @param ident The internal identifier of the target file.
	 * @return An array of FileProperties describing the data.
	 * @throws FileStoreIdentifierException if the identifier is null or not valid.
	 * @throws FileStoreNotFoundException if unable to locate the file.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public FileProperty[] properties(String ident)
		throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
		{
		if (null != service)
			{
			try {
				return service.properties(ident) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				notfoundException(ouch) ;
				identifierException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Prepare to receive a file from a remote source.
	 * @param transfer A TransferProperties object describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public TransferProperties importInit(TransferProperties transfer)
		throws FileStoreServiceException
		{
		if (null != service)
			{
			try {
				return service.importInit(transfer) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Import a file from a remote source.
	 * @param transfer A TransferProperties object describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 * @throws FileStoreServiceException if unable handle the request.
	 * @throws FileStoreTransferException if the transfer properties are null.
	 *
	 */
	public TransferProperties importData(TransferProperties transfer)
		throws FileStoreTransferException, FileStoreServiceException
		{
		if (null != service)
			{
			try {
				return service.importData(transfer) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				transferException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Prepare to send a file to a remote destination.
	 * @param transfer A TransferProperties object describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public TransferProperties exportInit(TransferProperties transfer)
		throws FileStoreServiceException
		{
		if (null != service)
			{
			try {
				return service.exportInit(transfer) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * Export a file to a remote destination.
	 * @param transfer A TransferProperties object describing the transfer.
	 * @return A new TransferProperties describing the transfer.
	 * @throws FileStoreServiceException if unable handle the request.
	 *
	 */
	public TransferProperties exportData(TransferProperties transfer)
		throws FileStoreServiceException
		{
		if (null != service)
			{
			try {
				return service.exportData(transfer) ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				serviceException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * A converter utility to unpack a FileStoreException from a RemoteException.
	 * @throws FileStoreException If the RemoteException cause was a FileStoreException.
	 *
	 */
	public void filestoreException(RemoteException ouch)
		throws FileStoreException
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreCoreDelegate.filestoreException") ;
		if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
		if (DEBUG_FLAG) System.out.println("  Type      : " + ouch.getClass()) ;
		if (DEBUG_FLAG) System.out.println("  Cause     : " + ouch.getCause()) ;
		//
		// If we have the original Exception.
		if (ouch.getCause() != null)
			{
			if (DEBUG_FLAG) System.out.println("  Got cause") ;
			if (ouch.getCause() instanceof FileStoreException)
				{
				throw (FileStoreException) ouch.getCause() ;
				}
			}
		//
		// If we don't have the original Exception.
		else {
			if (DEBUG_FLAG) System.out.println("  Null cause") ;
			//
			// If the message starts with our class name.
			String message  = ouch.getMessage() ;
			String template = FileStoreException.class.getName() + ": " ;
			if (DEBUG_FLAG) System.out.println("  Message  : '" + message  + "'") ;
			if (DEBUG_FLAG) System.out.println("  Template : '" + template + "'") ;
			if (null != message)
				{
				if (message.startsWith(template))
					{
					if (DEBUG_FLAG) System.out.println("  Matches template") ;
					throw new FileStoreException(
						message.substring(
							template.length()
							)
						) ;
					}
				}
			}
		if (DEBUG_FLAG) System.out.println("  Not handled") ;
		}

	/**
	 * A converter utility to unpack a FileStoreNotFoundException from a RemoteException.
	 * @throws FileStoreNotFoundException If the RemoteException cause was a FileStoreNotFoundException.
	 *
	 */
	public void notfoundException(RemoteException ouch)
		throws FileStoreNotFoundException
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreCoreDelegate.notfoundException") ;
		if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
		if (DEBUG_FLAG) System.out.println("  Type      : " + ouch.getClass()) ;
		if (DEBUG_FLAG) System.out.println("  Cause     : " + ouch.getCause()) ;
		//
		// If we have the original Exception.
		if (ouch.getCause() != null)
			{
			if (DEBUG_FLAG) System.out.println("  Got cause") ;
			if (ouch.getCause() instanceof FileStoreNotFoundException)
				{
				throw (FileStoreNotFoundException) ouch.getCause() ;
				}
			}
		//
		// If we don't have the original Exception.
		else {
			if (DEBUG_FLAG) System.out.println("  Null cause") ;
			//
			// If the message starts with our class name.
			String message  = ouch.getMessage() ;
			String template = FileStoreNotFoundException.class.getName() + ": " ;
			if (DEBUG_FLAG) System.out.println("  Message  : '" + message  + "'") ;
			if (DEBUG_FLAG) System.out.println("  Template : '" + template + "'") ;
			if (null != message)
				{
				if (message.startsWith(template))
					{
					if (DEBUG_FLAG) System.out.println("  Matches template") ;
					throw new FileStoreNotFoundException(
						message.substring(
							template.length()
							)
						) ;
					}
				}
			}
		if (DEBUG_FLAG) System.out.println("  Not handled") ;
		}

	/**
	 * A converter utility to unpack a FileStoreIdentifierException from a RemoteException.
	 * @throws FileStoreIdentifierException If the RemoteException cause was a FileStoreIdentifierException.
	 *
	 */
	public void identifierException(RemoteException ouch)
		throws FileStoreIdentifierException
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreCoreDelegate.identifierException") ;
		if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
		if (DEBUG_FLAG) System.out.println("  Type      : " + ouch.getClass()) ;
		if (DEBUG_FLAG) System.out.println("  Cause     : " + ouch.getCause()) ;
		//
		// If we have the original Exception.
		if (ouch.getCause() != null)
			{
			if (DEBUG_FLAG) System.out.println("  Got cause") ;
			if (ouch.getCause() instanceof FileStoreIdentifierException)
				{
				throw (FileStoreIdentifierException) ouch.getCause() ;
				}
			}
		//
		// If we don't have the original Exception.
		else {
			if (DEBUG_FLAG) System.out.println("  Null cause") ;
			//
			// If the message starts with our class name.
			String message  = ouch.getMessage() ;
			String template = FileStoreIdentifierException.class.getName() + ": " ;
			if (DEBUG_FLAG) System.out.println("  Message  : '" + message  + "'") ;
			if (DEBUG_FLAG) System.out.println("  Template : '" + template + "'") ;
			if (null != message)
				{
				if (message.startsWith(template))
					{
					if (DEBUG_FLAG) System.out.println("  Matches template") ;
					throw new FileStoreIdentifierException(
						message.substring(
							template.length()
							)
						) ;
					}
				}
			}
		if (DEBUG_FLAG) System.out.println("  Not handled") ;
		}

	/**
	 * A converter utility to unpack a FileStoreServiceException from a RemoteException.
	 * @throws FileStoreServiceException If the RemoteException cause was a FileStoreServiceException.
	 *
	 */
	public void serviceException(RemoteException ouch)
		throws FileStoreServiceException
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreCoreDelegate.serviceException") ;
		if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
		if (DEBUG_FLAG) System.out.println("  Type      : " + ouch.getClass()) ;
		if (DEBUG_FLAG) System.out.println("  Cause     : " + ouch.getCause()) ;
		//
		// If we have the original Exception.
		if (ouch.getCause() != null)
			{
			if (DEBUG_FLAG) System.out.println("  Got cause") ;
			if (ouch.getCause() instanceof FileStoreServiceException)
				{
				throw (FileStoreServiceException) ouch.getCause() ;
				}
			}
		//
		// If we don't have the original Exception.
		else {
			if (DEBUG_FLAG) System.out.println("  Null cause") ;
			//
			// If the message starts with our class name.
			String message  = ouch.getMessage() ;
			String template = FileStoreServiceException.class.getName() + ": " ;
			if (DEBUG_FLAG) System.out.println("  Message  : '" + message  + "'") ;
			if (DEBUG_FLAG) System.out.println("  Template : '" + template + "'") ;
			if (null != message)
				{
				if (message.startsWith(template))
					{
					if (DEBUG_FLAG) System.out.println("  Matches template") ;
					throw new FileStoreServiceException(
						message.substring(
							template.length()
							)
						) ;
					}
				}
			}
		if (DEBUG_FLAG) System.out.println("  Not handled") ;
		}

	/**
	 * Throw a FileStoreIdentifierException, useful for debugging the transfer of Exceptions via SOAP.
	 * @throws FileStoreIdentifierException unpacking it from the RemoteException when invoked via a SOAP call.
	 * @throws FileStoreServiceException if the service was unable to handle the request.
	 *
	 */
	public void throwIdentifierException()
		throws FileStoreIdentifierException, FileStoreServiceException
		{
		if (null != service)
			{
			try {
				service.throwIdentifierException() ;
				}
			catch (RemoteException ouch)
				{
				//
				// Unpack the expected Exception(s).
				identifierException(ouch) ;
				//
				// If we get this far, then we don't know what it is.
				throw new FileStoreServiceException(
					"WebService call failed - " + ouch,
					ouch
					) ;
				}
			}
		else {
			throw new FileStoreServiceException(
				"Service not initialised"
				) ;
			}
		}

	/**
	 * A converter utility to unpack a FileStoreTransferException from a RemoteException.
	 * @throws FileStoreTransferException If the RemoteException cause was a FileStoreTransferException.
	 *
	 */
	public void transferException(RemoteException ouch)
		throws FileStoreTransferException
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("FileStoreCoreDelegate.transferException") ;
		if (DEBUG_FLAG) System.out.println("  Exception : " + ouch) ;
		if (DEBUG_FLAG) System.out.println("  Type      : " + ouch.getClass()) ;
		if (DEBUG_FLAG) System.out.println("  Cause     : " + ouch.getCause()) ;
		//
		// If we have the original Exception.
		if (ouch.getCause() != null)
			{
			if (DEBUG_FLAG) System.out.println("  Got cause") ;
			if (ouch.getCause() instanceof FileStoreTransferException)
				{
				throw (FileStoreTransferException) ouch.getCause() ;
				}
			}
		//
		// If we don't have the original Exception.
		else {
			if (DEBUG_FLAG) System.out.println("  Null cause") ;
			//
			// If the message starts with our class name.
			String message  = ouch.getMessage() ;
			String template = FileStoreTransferException.class.getName() + ": " ;
			if (DEBUG_FLAG) System.out.println("  Message  : '" + message  + "'") ;
			if (DEBUG_FLAG) System.out.println("  Template : '" + template + "'") ;
			if (null != message)
				{
				if (message.startsWith(template))
					{
					if (DEBUG_FLAG) System.out.println("  Matches template") ;
					throw new FileStoreTransferException(
						message.substring(
							template.length()
							)
						) ;
					}
				}
			}
		if (DEBUG_FLAG) System.out.println("  Not handled") ;
		}

	}

