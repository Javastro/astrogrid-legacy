/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/identifier/UniqueIdentifier.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: UniqueIdentifier.java,v $
 *   Revision 1.2  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.1.2.2  2004/06/17 13:38:58  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.identifier ;

import java.rmi.server.UID ;

/**
 * A base class for unique identifiers.
 * This enable us to swap between different identifier generator implementations.
 *
 */
public class UniqueIdentifier
    {
    /**
     * Create a new unique identifier.
     *
     */
    public UniqueIdentifier()
        {
        //
        // Use the RMI library to generate a new identifier.
        UID uid = new UID() ;
        this.string = uid.toString() ;
        }

    /**
     * Create an identifier from a string.
     *
     */
    public UniqueIdentifier(String value)
        {
        this.string = value ;
        }

    /**
     * An internal string representation of the identifier.
     *
     */
    private String string ;

    /**
     * Convert the identifier to a String.
     *
     */
    public String toString()
        {
        return this.string ;
        }

    }


