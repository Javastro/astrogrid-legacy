/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/exception/Attic/NodeNotFoundException.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: NodeNotFoundException.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.1  2004/10/13 06:33:17  dave
 *   Refactored exceptions ...
 *   Refactored the container API
 *   Added placeholder file interface ...
 *
 *   Revision 1.1.2.1  2004/10/09 04:28:31  dave
 *   Added initial account and container methods ....
 *
 *   Revision 1.1.2.1  2004/10/07 16:04:38  dave
 *   Added exception to addAccount
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common.exception ;

/**
 * Exception thrown when attempting to access a node that is not in the database.
 *
 */
public class NodeNotFoundException
    extends FileManagerException
    {
    /**
     * The default exception message.
     *
     */
    public static final String DEFAULT_MESSAGE = "Node not found" ;

    /**
     * Public constructor using the default message.
     *
     */
    public NodeNotFoundException()
        {
        this(DEFAULT_MESSAGE) ;
        }

    /**
     * Public constructor.
     * @param message The exception message.
     *
     */
    public NodeNotFoundException(String message)
        {
        super(message) ;
        }

    }
