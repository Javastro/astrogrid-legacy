/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/exception/Attic/FileManagerPropertiesException.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerPropertiesException.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.1  2004/11/04 15:50:17  dave
 *   Added ivorn pareser and factory.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common.exception ;

/**
 * An Exception thrown if there is a problem with a set of properties.
 *
 */
public class FileManagerPropertiesException
	extends FileManagerException
	{

	/**
	 * Public constructor.
	 *
	 */
	public FileManagerPropertiesException()
		{
		super() ;
		}

	/**
	 * Public constructor.
	 * @param message The exception message.
	 *
	 */
	public FileManagerPropertiesException(String message)
		{
		super(message) ;
		}

	}
