/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/exception/Attic/FileManagerIdentifierException.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerIdentifierException.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/11/04 15:50:17  dave
 *   Added ivorn pareser and factory.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common.exception ;

import java.net.URISyntaxException ;

/**
 * An Exception thrown if there is a problem with a file identifier.
 *
 */
public class FileManagerIdentifierException
    extends FileManagerException
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
    public FileManagerIdentifierException()
        {
        super() ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     *
     */
    public FileManagerIdentifierException(String message)
        {
        super(message) ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     * @param ident The identifier that caused the Exception.
     *
     */
    public FileManagerIdentifierException(String message, String ident)
        {
        this(message) ;
        this.ident = ident ;
        }

    /**
     * Public constructor.
     * @param cause The root cause of this Exception.
     *
     */
    public FileManagerIdentifierException(URISyntaxException cause)
        {
        super(cause) ;
        }

    }
