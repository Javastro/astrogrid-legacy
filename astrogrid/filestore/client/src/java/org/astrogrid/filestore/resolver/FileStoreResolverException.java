/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/client/src/java/org/astrogrid/filestore/resolver/FileStoreResolverException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/23 09:11:16 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreResolverException.java,v $
 *   Revision 1.2  2004/07/23 09:11:16  dave
 *   Merged development branch, dave-dev-200407221513, into HEAD
 *
 *   Revision 1.1.2.2  2004/07/23 04:09:02  dave
 *   Fixed resolver bugs
 *
 *   Revision 1.1.2.1  2004/07/23 03:09:00  dave
 *   Added new resolver
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.resolver ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filestore.common.exception.FileStoreException ;

/**
 * An Exception thrown if there is a problem with the service resolver.
 *
 */
public class FileStoreResolverException
	extends FileStoreException
	{

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
	public FileStoreResolverException()
		{
		super() ;
		}

	/**
	 * Public constructor.
	 * @param message The exception message.
	 *
	 */
	public FileStoreResolverException(String message)
		{
		super(message) ;
		}

	/**
	 * Public constructor.
	 * @param message The exception message.
	 * @param ivorn The service identifier that caused the Exception.
	 *
	 */
	public FileStoreResolverException(String message, Ivorn ivorn)
		{
		this(message, ivorn.toString()) ;
		}

	/**
	 * Public constructor.
	 * @param message The exception message.
	 * @param ident The service identifier that caused the Exception.
	 *
	 */
	public FileStoreResolverException(String message, String ident)
		{
		this(message) ;
		this.ident = ident ;
		}

	/**
	 * Public constructor.
	 * @param message The exception message.
	 * @param cause The original cause.
	 *
	 */
	public FileStoreResolverException(String message, Throwable cause)
		{
		super(message, cause) ;
		}

	}
