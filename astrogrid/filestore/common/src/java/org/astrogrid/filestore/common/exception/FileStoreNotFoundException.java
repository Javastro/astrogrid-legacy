/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/exception/FileStoreNotFoundException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/07/14 13:50:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileStoreNotFoundException.java,v $
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.1  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.exception ;

/**
 * An Exception throws when the service can't locate a file.
 *
 */
public class FileStoreNotFoundException
	extends FileStoreException
	{
	/**
	 * Standard Exception message.
	 *
	 */
	public static final String DEFAULT_MESSAGE = "Unable to locate file : " ;

	/**
	 * Public constructor.
	 *
	 */
	public FileStoreNotFoundException()
		{
		super() ;
		}

	/**
	 * Public constructor.
	 * @param ident The internal identifier of the file.
	 *
	 */
	public FileStoreNotFoundException(String ident)
		{
		super(
			DEFAULT_MESSAGE + ident
			) ;
		}
	}
