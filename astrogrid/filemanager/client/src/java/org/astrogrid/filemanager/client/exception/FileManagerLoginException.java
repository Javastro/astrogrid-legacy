/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/client/exception/Attic/FileManagerLoginException.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:44:00 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerLoginException.java,v $
 *   Revision 1.2  2005/01/28 10:44:00  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.1.2.1  2005/01/23 05:39:44  dave
 *   Added initial implementation of FileManagerClient ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.client.exception;

import org.astrogrid.filemanager.common.exception.FileManagerException;

/**
 * Exception thrown to indicate a problem when conneting to a FileManager service.
 *
 */
public class FileManagerLoginException
    extends FileManagerException
    {

    /**
     * Public constructor.
     * Required to enable Axis to create an exception.
     *
     */
    public FileManagerLoginException()
        {
        super() ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     *
     */
    public FileManagerLoginException(String message)
        {
        super(message) ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     * @param cause The exception cause.
     *
     */
    public FileManagerLoginException(String message, Throwable cause)
        {
        super(message, cause) ;
        }
    }
