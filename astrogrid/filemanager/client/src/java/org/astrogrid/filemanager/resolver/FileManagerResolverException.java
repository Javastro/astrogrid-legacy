/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/java/org/astrogrid/filemanager/resolver/FileManagerResolverException.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerResolverException.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.1  2005/01/12 14:20:57  dave
 *   Replaced tabs with spaces ....
 *
 *   Revision 1.2  2004/11/25 00:20:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/18 16:06:11  dave
 *   Added delegate resolver and tests ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.resolver ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.exception.FileManagerException ;

/**
 * An Exception thrown if there is a problem with the service resolver.
 *
 */
public class FileManagerResolverException
    extends FileManagerException
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
    public FileManagerResolverException()
        {
        super() ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     *
     */
    public FileManagerResolverException(String message)
        {
        super(message) ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     * @param ivorn The service identifier that caused the Exception.
     *
     */
    public FileManagerResolverException(String message, Ivorn ivorn)
        {
        this(message, ivorn.toString()) ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     * @param ident The service identifier that caused the Exception.
     *
     */
    public FileManagerResolverException(String message, String ident)
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
    public FileManagerResolverException(String message, Throwable cause)
        {
        super(message, cause) ;
        }

    }
