/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/server/src/java/org/astrogrid/filestore/server/FileStoreImpl.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:44:03 $</cvs:date>
 * <cvs:version>$Revision: 1.12 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreImpl.java,v $
 *   Revision 1.12  2005/01/28 10:44:03  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.11.6.1  2005/01/17 17:19:21  dave
 *   Fixed bug in FileManagerImpl test (missing '/' in repository path on Unix) ...
 *   Changed tabs to spaces ..
 *
 *   Revision 1.11  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.9.14.7  2004/12/08 17:54:55  dave
 *   Added update to FileManager client and server side ...
 *
 *   Revision 1.9.14.6  2004/11/09 17:41:36  dave
 *   Added file:// URL handling to allow server URLs to be tested.
 *   Added importInit and exportInit to server implementation.
 *   Moved remaining tests out of extended test abd removed it.
 *
 *   Revision 1.9.14.5  2004/10/27 10:56:30  dave
 *   Changed inport init to save the details, and simplified tests for debug
 *
 *   Revision 1.9.14.4  2004/10/26 16:11:31  dave
 *   Fixed debug ..
 *
 *   Revision 1.9.14.3  2004/10/26 15:07:12  dave
 *   Added initial code for import init ...
 *
 *   Revision 1.9.14.2  2004/10/26 11:13:11  dave
 *   Changed transfer properties 'source' to 'location', makes more sense for PUT transfers.
 *
 *   Revision 1.9.14.1  2004/10/19 14:56:16  dave
 *   Refactored config and resolver to enable multiple instances of mock implementation.
 *   Required to implement handling of multiple FileStore(s) in FileManager.
 *
 *   Revision 1.9  2004/09/17 06:57:10  dave
 *   Added commons logging to FileStore.
 *   Updated logging properties in Community.
 *   Fixed bug in AGINAB deployment.
 *   Removed MySpace tests with hard coded grendel address.
 *
 *   Revision 1.8.20.1  2004/09/17 01:08:36  dave
 *   Updated debug to use commons logging API ....
 *
 *   Revision 1.8  2004/08/27 22:43:15  dave
 *   Updated filestore and myspace to report file size correctly.
 *
 *   Revision 1.7.12.1  2004/08/26 19:06:50  dave
 *   Modified filestore to return file size in properties.
 *
 *   Revision 1.7  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.6  2004/08/18 10:03:19  gtr
 *   BZ486. I added an early version of the getSecureUrl operation. This implementation doesn't yet work.
 *
 *   Revision 1.5.10.2  2004/08/06 22:25:06  dave
 *   Refactored bits and broke a few tests ...
 *
 *   Revision 1.5.10.1  2004/08/04 06:35:02  dave
 *   Added initial stubs for servlet ....
 *
 *   Revision 1.5  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.4.6.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
 *
 *   Revision 1.4  2004/07/21 18:11:55  dave
 *   Merged development branch, dave-dev-200407201059, into HEAD
 *
 *   Revision 1.3.2.1  2004/07/21 16:28:16  dave
 *   Added content properties and tests
 *
 *   Revision 1.3  2004/07/19 23:42:07  dave
 *   Merged development branch, dave-dev-200407151443, into HEAD
 *
 *   Revision 1.2.4.1  2004/07/19 19:40:28  dave
 *   Debugged and worked around Axis Exception handling
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.3  2004/07/14 10:34:08  dave
 *   Reafctored server to use array of properties
 *
 *   Revision 1.1.2.2  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.1  2004/07/08 07:31:30  dave
 *   Added container impl and tests
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.server ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.net.URL ;
import java.net.MalformedURLException ;

import java.io.IOException ;

import java.util.Map ;
import java.util.HashMap ;

import java.rmi.RemoteException  ;
import org.apache.axis.AxisFault ;

import org.astrogrid.filestore.common.FileStore ;
import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.FileIdentifier ;
import org.astrogrid.filestore.common.transfer.TransferProperties ;
import org.astrogrid.filestore.common.exception.FileStoreException ;
import org.astrogrid.filestore.common.exception.FileStoreNotFoundException ;
import org.astrogrid.filestore.common.exception.FileStoreIdentifierException ;
import org.astrogrid.filestore.common.exception.FileStoreServiceException ;
import org.astrogrid.filestore.common.exception.FileStoreTransferException ;

import org.astrogrid.filestore.server.repository.Repository ;
import org.astrogrid.filestore.server.repository.RepositoryImpl ;
import org.astrogrid.filestore.server.repository.RepositoryContainer ;

import org.astrogrid.filestore.server.streamer.StreamTicket;
import org.astrogrid.filestore.server.streamer.StreamTicketList;

import org.astrogrid.filestore.common.FileStoreConfig ;
import org.astrogrid.filestore.server.FileStoreConfigImpl ;

/**
 * The main server implementation of the store service.
 *
 */
public class FileStoreImpl
    implements FileStore
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileStoreImpl.class);

    /**
     * Public constructor.
     *
     */
    public FileStoreImpl()
        {
        this(
            new FileStoreConfigImpl()
            ) ;
        }

    /**
     * Public constructor.
     * @param config The local repository configuration.
     *
     */
    public FileStoreImpl(FileStoreConfig config)
        {
        this(
            config,
            new RepositoryImpl(
                config
                )
            ) ;
        }

    /**
     * Public constructor.
     * @param config The local repository configuration.
     * @param repository A local file repository.
     *
     */
    public FileStoreImpl(FileStoreConfig config, Repository repository)
        {
        this.config = config ;
        this.repository = repository ;
        }

    /**
     * Reference to our configuration.
     *
     */
    private FileStoreConfig config ;

    /**
     * Reference to our repository.
     *
     */
    private Repository repository ;

    /**
     * Get the service identifier.
     * @return The ivo identifier for this service.
     * @throws FileStoreServiceException if unable handle the request.
     *
     */
    public String identifier()
        throws FileStoreServiceException
        {
        return config.getServiceIvorn().toString() ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreImpl.importString()") ;
        //
        // Check for null data.
        if (null == data)
            {
            throw new FileStoreException(
                "Null data"
                ) ;
            }
        //
        // Convert to bytes and import that.
        return this.importBytes(
            properties,
            data.getBytes()
            ) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreImpl.importBytes()") ;
        //
        // Check for null data.
        if (null == data)
            {
            throw new FileStoreException(
                "Null data"
                ) ;
            }
        //
        // Create a new container.
        RepositoryContainer container = repository.create(
            new FileProperties(
                properties
                )
            ) ;
        //
        // Import our data into the container.
        container.importBytes(
            data
            ) ;
        //
        // Return the container properties.
        return container.properties().toArray() ;
        }

    /**
     * Append a string to existing file.
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreImpl.appendString()") ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new FileStoreIdentifierException(
                FileStoreIdentifierException.NULL_IDENT_MESSAGE
                ) ;
            }
        //
        // Check for null data.
        if (null == data)
            {
            throw new FileStoreException(
                "Null data"
                ) ;
            }
        //
        // Convert to bytes and append that.
        return this.appendBytes(
            ident,
            data.getBytes()
            ) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreImpl.appendBytes()") ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new FileStoreIdentifierException(
                FileStoreIdentifierException.NULL_IDENT_MESSAGE
                ) ;
            }
        //
        // Check for null data.
        if (null == data)
            {
            throw new FileStoreException(
                "Null data"
                ) ;
            }
        //
        // Locate the container.
        RepositoryContainer container = repository.load(ident) ;
        //
        // Append the data to our container.
        container.appendBytes(
            data
            ) ;
        //
        // Return the container properties.
        return container.properties().toArray() ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreImpl.exportString()") ;
        return new String(
            this.exportBytes(
                ident
                )
            ) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreImpl.exportBytes()") ;
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new FileStoreIdentifierException(
                FileStoreIdentifierException.NULL_IDENT_MESSAGE
                ) ;
            }
        return repository.load(ident).exportBytes() ;
        }

    /**
     * Create a local duplicate (copy) of a file.
     * @param ident The internal identifier of the target file.
     * @param properties An optional array of FileProperties describing the copy.
     * @return An array of FileProperties describing the copied data.
     * @throws FileStoreTransferException if unable to transfer the data.
     * @throws FileStoreIdentifierException if the identifier is null or not valid.
     * @throws FileStoreNotFoundException if unable to locate the file.
     * @throws FileStoreServiceException if unable handle the request.
     *
     */
    public FileProperty[] duplicate(String ident, FileProperty[] properties)
        throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException, FileStoreTransferException
        {
        //
        // Check for null ident.
        if (null == ident)
            {
            throw new FileStoreIdentifierException(
                FileStoreIdentifierException.NULL_IDENT_MESSAGE
                ) ;
            }
        return repository.duplicate(ident).properties().toArray() ;
        }

    /**
     * Delete a file.
     * @param ident The internal identifier of the target file.
     * @return An array of FileProperties describing the deleted data.
     * @throws FileStoreIdentifierException if the identifier is null or not valid.
     * @throws FileStoreNotFoundException if unable to locate the file.
     * @throws FileStoreServiceException if unable handle the request.
     * @throws RemoteException If the WebService call fails.
     *
     */
    public FileProperty[] delete(String ident)
        throws FileStoreServiceException, FileStoreIdentifierException, FileStoreNotFoundException
        {
        //
        // Locate the container.
        RepositoryContainer container = repository.load(ident) ;
        //
        // Try to delete the container.
        container.delete() ;
        //
        // Return the original properties.
        return container.properties().toArray() ;
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
        //
        // Locate the container.
        RepositoryContainer container = repository.load(
            ident
            );
        //
        // Update and save the properties.
        container.update() ;
        //
        // Return the updated properties.
        return container.properties().toArray() ;
        }

    /**
     * Prepare to receive a data object from a remote source.
     * @param transfer A TransferProperties describing the transfer.
     * @return A new TransferProperties describing the transfer.
     * @throws FileStoreTransferException if there is an error in the transfer properties.
     *
     */
    public TransferProperties importInit(TransferProperties transfer)
        throws FileStoreServiceException, FileStoreTransferException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreImpl.importInit()") ;
        //
        // Check for null transfer properties.
        if (null == transfer)
            {
            throw new FileStoreTransferException(
                "Null transfer properties"
                ) ;
            }
        //
        // Create a new container.
        RepositoryContainer container = repository.create(
            new FileProperties(
                transfer.getFileProperties()
                )
            ) ;
        //
        // Set the transfer import URL.
        transfer.setLocation(
            config.getResourceUrl(
                container.ident().toString()
                ).toString()
            ) ;
        log.debug("  URL : " + transfer.getLocation()) ;
        //
        // Update and save the container.
        container.update() ;
        //
        // Add the updated file properties.
        transfer.setFileProperties(
            container.properties().toArray()
            ) ;
        //
        // Return the transfer properties.
        return transfer ;
        }

    /**
     * Import a data object from a remote source.
     * @param transfer A TransferProperties describing the transfer.
     * @return A new TransferProperties describing the transfer.
     * @throws FileStoreServiceException if there is an error in the service.
     * @throws FileStoreTransferException if there is an error in the transfer.
     *
     */
    public TransferProperties importData(TransferProperties transfer)
        throws FileStoreServiceException, FileStoreTransferException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreImpl.importData()") ;
        //
        // Check for null transfer properties.
        if (null == transfer)
            {
            throw new FileStoreTransferException(
                "Null transfer properties"
                ) ;
            }
        //
        // Check for null URL.
        if (null == transfer.getLocation())
            {
            throw new FileStoreTransferException(
                "Null transfer source"
                ) ;
            }
        //
        // Create a new container.
        RepositoryContainer container = repository.create(
            new FileProperties(
                transfer.getFileProperties()
                )
            ) ;
        //
        // Transfer the data into our container.
        try {
            container.importData(
                new URL(
                    transfer.getLocation()
                    )
                ) ;
            }
        catch (MalformedURLException ouch)
            {
            throw new FileStoreTransferException(
                ouch
                ) ;
            }
        //
        // Add the updated file properties.
        transfer.setFileProperties(
            container.properties().toArray()
            ) ;
        //
        // Return the transfer properties.
        return transfer ;
        }

    /**
     * Prepare to send a data object to a remote destination.
     * Essentially, this returns a vaild URL to access data in the TransferProperties location.
     * @param transfer A TransferProperties describing the transfer.
     * @throws FileStoreTransferException if the transfer properties are null.
     * @throws FileStoreNotFoundException If unable to locate the target object.
     * @throws FileStoreServiceException if unable handle the request.
     * @return A new TransferProperties describing the transfer.
     *
     */
    public TransferProperties exportInit(TransferProperties transfer)
        throws FileStoreTransferException, FileStoreServiceException, FileStoreNotFoundException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileStoreImpl.importInit()") ;
        //
        // Check for null transfer properties.
        if (null == transfer)
            {
            throw new FileStoreTransferException(
                "Null transfer properties"
                ) ;
            }
        //
        // Get the transfer file properties.
        FileProperties properties = new FileProperties(
            transfer.getFileProperties()
            ) ;
        //
        // Get the file identifier.
        String ident = null ;
        try {
            ident = properties.getStoreResourceIdent();
            }
        catch (FileStoreIdentifierException ouch)
            {
            throw new FileStoreNotFoundException(
                "Unable to parse resource identifier"
                ) ;
            }
        //
        // If we have a valid identifier.
        if (null != ident)
            {
            //
            // Try to locate the container.
            try {
                RepositoryContainer container = repository.load(ident) ;
//
// Update the transfer properties.
//
                //
                // Set the transfer import URL.
                transfer.setLocation(
                    config.getResourceUrl(
                        container.ident().toString()
                        ).toString()
                    ) ;
                log.debug("  URL : " + transfer.getLocation()) ;
                return transfer ;
                }
            catch (FileStoreIdentifierException ouch)
                {
                throw new FileStoreNotFoundException(
                    "Unable to load resource properties"
                    ) ;
                }
            }
        //
        // If we don't have a valid identifier.
        else {
            throw new FileStoreNotFoundException(
                "Null target file identifier"
                );
            }
        }

    /**
     * Export a data object to a remote destination.
     * @param transfer A TransferProperties describing the transfer.
     * @return A new TransferProperties describing the transfer.
     *
     */
    public TransferProperties exportData(TransferProperties transfer)
        {
        return transfer ;
        }

    /**
     * Throw a FileStoreIdentifierException, useful for debugging the transfer of Exceptions via SOAP.
     * @throws FileStoreIdentifierException as requested.
     *
     */
    public void throwIdentifierException()
        throws FileStoreIdentifierException
        {
        throw new FileStoreIdentifierException(
            "TEST FAULT",
            "TEST IDENT"
            ) ;
        }

    /**
    * Issues a stream ticket (see {@link org.astrogrid.filestore.server.streamer})
    * for a data item and returns a URL from which that item may be streamed.
    * The URL embeds the name of the ticket, which is not predictable and may
    * only be streamed once. Hence, the streaming is reasonably secure.
    * This operation is intended to make data items available to web browsers
    * without making them public.
    *
    * @param ident The filestore identifier for the data item to be streamed.
    * @return the URL from which the data may be streamed.
    * @throws FileStoreIdentifierException if the identifier is null or not valid.
    * @throws FileStoreNotFoundException if unable to locate the file.
    * @throws FileStoreServiceException if unable handle the request.
    * @throws RemoteException If the WebService call fails.
    */
    public String getSecureUrl(String ident)
        throws FileStoreServiceException,
             FileStoreIdentifierException,
             FileStoreNotFoundException,
             RemoteException {
        String streamUrl = null;

        // Locate the container for the data item.
        // This throws  FileStoreIdentifierException if the id
        // is null or otherwise invalid.
        RepositoryContainer container = repository.load(ident);

        // Issue the stream ticket, thus authorizing the streaming of the data.
        // The target property of the ticket is set to the data-item id; i.e. the
        // ticketing mechanism has to work with the repository mechanism to
        // find the data.
        StreamTicket ticket = new StreamTicket();
        ticket.setTarget(ident);
        // @TODO Set the MIME type of the data.
        StreamTicketList list = new StreamTicketList();
        list.add(ticket);
        streamUrl = ticket.getName();

        return streamUrl;
        }

    }

