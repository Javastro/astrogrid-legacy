/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/exception/FileStoreIdentifierException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/23 09:11:16 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreIdentifierException.java,v $
 *   Revision 1.2  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/23 02:10:58  dave
 *   Added IvornFactory and IvornParser
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.exception ;

import java.net.URISyntaxException ;

/**
 * An Exception thrown if there is a problem with a file identifier.
 *
 */
public class FileStoreIdentifierException
	extends FileStoreException
	{
	/**
	 * Invalid identifier message.
	 *
	 */
	public static final String INVALID_IDENT_MESSAGE = "Invalid identifier" ;

	/**
	 * Null identifier message.
	 *
	 */
	public static final String NULL_IDENT_MESSAGE = "Null identifier" ;

	/**
	 * The identifier that caused the Exception.
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
	 * Public constructor.
	 *
	 */
	public FileStoreIdentifierException()
		{
		super() ;
		}

	/**
	 * Public constructor.
	 * @param message The exception message.
	 *
	 */
	public FileStoreIdentifierException(String message)
		{
		super(message) ;
		}

	/**
	 * Public constructor.
	 * @param message The exception message.
	 * @param ident The identifier that caused the Exception.
	 *
	 */
	public FileStoreIdentifierException(String message, String ident)
		{
		this(message) ;
		this.ident = ident ;
		}

    /**
     * Public constructor.
     * @param cause The root cause of this Exception.
     *
     */
    public FileStoreIdentifierException(URISyntaxException cause)
        {
        super(cause) ;
        }

	}
