/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/exception/Attic/FileIdentifierException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileIdentifierException.java,v $
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.exception ;

/**
 * An Exception thrown if there is a problem with a file identifier.
 *
 */
public class FileIdentifierException
	extends FileStoreException
	{
	/**
	 * Invalid identifier message.
	 *
	 */
	public static final String INVALID_IDENT_MESSAGE = "Invalid file identifier : " ;

	/**
	 * Null identifier message.
	 *
	 */
	public static final String NULL_IDENT_MESSAGE = "Null file identifier" ;

	/**
	 * Create an Exception message.
	 *
	 */
	public static String message(String ident)
		{
		if (null != ident)
			{
			return INVALID_IDENT_MESSAGE + ident ;
			}
		else {
			return NULL_IDENT_MESSAGE ;
			}
		}

	/**
	 * Public constructor.
	 *
	 */
	public FileIdentifierException()
		{
		this(null) ;
		}

	/**
	 * Public constructor.
	 * @param ident The file identifier that caused the problem.
	 *
	 */
	public FileIdentifierException(String ident)
		{
		super(
			message(ident)
			) ;
		}
	}
