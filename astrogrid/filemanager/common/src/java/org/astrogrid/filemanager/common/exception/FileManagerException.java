/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/exception/Attic/FileManagerException.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerException.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.3  2004/11/13 01:39:03  dave
 *   Modifications to support the new client API ...
 *
 *   Revision 1.1.2.2  2004/11/04 15:50:17  dave
 *   Added ivorn pareser and factory.
 *
 *   Revision 1.1.2.1  2004/10/07 16:04:38  dave
 *   Added exception to addAccount
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common.exception ;

/**
 * Base class for FileManager exceptions.
 *
 */
public class FileManagerException
    extends Exception
    {

    /**
     * Public constructor.
     *
     */
    public FileManagerException()
        {
        super() ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     *
     */
    public FileManagerException(String message)
        {
        super(message) ;
        }

    /**
     * Public constructor.
     * @param cause The exception cause.
     *
     */
    public FileManagerException(Throwable cause)
        {
        super(cause) ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     * @param cause The exception cause.
     *
     */
    public FileManagerException(String message, Throwable cause)
        {
        super(message, cause) ;
        }

    }
