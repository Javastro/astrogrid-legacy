/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/exception/FileStoreTransferException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/21 18:11:55 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreTransferException.java,v $
 *   Revision 1.2  2004/07/21 18:11:55  dave
 *   Merged development branch, dave-dev-200407201059, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/20 19:10:40  dave
 *   Refactored to implement URL import
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.exception ;

/**
 * An Exception thrown if a transfer operation fails.
 *
 */
public class FileStoreTransferException
	extends FileStoreException
	{

    /**
     * Public constructor.
     * This should not be used in the main code.
     * This enables Axis to re-construct the Exception on the client side by treating it as a Bean.
     *
     */
    public FileStoreTransferException()
        {
        super() ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     *
     */
    public FileStoreTransferException(String message)
        {
        super(message) ;
        }

    /**
     * Public constructor.
     * @param cause The root cause of this Exception.
     *
     */
    public FileStoreTransferException(Throwable cause)
        {
        super(cause) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param cause   The root cause of this Exception.
     *
     */
    public FileStoreTransferException(String message, Throwable cause)
        {
        super(message, cause) ;
        }

	}
