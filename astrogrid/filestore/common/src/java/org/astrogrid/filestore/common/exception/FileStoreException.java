/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/exception/FileStoreException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreException.java,v $
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.2  2004/07/06 09:16:12  dave
 *   Added delegate interface and mock implementation
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.exception ;

/**
 * A generic Exception for FileStore operations.
 *
 */
public class FileStoreException
	extends Exception
	{
	/**
	 * Public constructor.
	 *
	 */
	public FileStoreException()
		{
		super() ;
		}

	/**
	 * Public constructor.
	 * @param message The Exception message.
	 *
	 */
	public FileStoreException(String message)
		{
		super(message) ;
		}

    /**
     * Public constructor.
     * @param cause The root cause of this Exception.
     *
     */
    public FileStoreException(Throwable cause)
        {
        super(cause) ;
        }

    /**
     * Public constructor.
     * @param message The Exception message.
     * @param cause   The root cause of this Exception.
     *
     */
    public FileStoreException(String message, Throwable cause)
        {
        super(message, cause) ;
        }
	}
