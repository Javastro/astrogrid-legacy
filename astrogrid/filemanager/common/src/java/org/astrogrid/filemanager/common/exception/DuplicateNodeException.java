/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/exception/Attic/DuplicateNodeException.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: DuplicateNodeException.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/10/13 06:33:17  dave
 *   Refactored exceptions ...
 *   Refactored the container API
 *   Added placeholder file interface ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common.exception ;

/**
 * Exception thrown when attempting to create a duplicate node.
 *
 */
public class DuplicateNodeException
	extends FileManagerException
	{
	/**
	 * The default exception message.
	 *
	 */
	public static final String DEFAULT_MESSAGE = "Duplicate node name" ;

	/**
	 * Public constructor using the default message.
	 *
	 */
	public DuplicateNodeException()
		{
		this(DEFAULT_MESSAGE) ;
		}

	/**
	 * Public constructor.
	 * @param message The exception message.
	 *
	 */
	public DuplicateNodeException(String message)
		{
		super(message) ;
		}

	}
