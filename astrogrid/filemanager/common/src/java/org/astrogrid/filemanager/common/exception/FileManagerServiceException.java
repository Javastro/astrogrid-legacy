/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/exception/Attic/FileManagerServiceException.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerServiceException.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.3  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 *   Revision 1.1.2.2  2004/11/13 01:39:03  dave
 *   Modifications to support the new client API ...
 *
 *   Revision 1.1.2.1  2004/10/21 21:08:52  dave
 *   Added config interface and mock implementation.
 *   Partial implementation of data import into FileStore via direct URL transfer.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common.exception ;

/**
 * Exception thrown to indicate a problem with the FileManager service, e.g A problem with the service configuration.
 *
 */
public class FileManagerServiceException
    extends FileManagerException
    {

    /**
     * Public constructor.
     * Required to enable Axis to create an exception.
     *
     */
    public FileManagerServiceException()
        {
        super() ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     *
     */
    public FileManagerServiceException(String message)
        {
        super(message) ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     * @param cause The exception cause.
     *
     */
    public FileManagerServiceException(String message, Throwable cause)
        {
        super(message, cause) ;
        }
    }
