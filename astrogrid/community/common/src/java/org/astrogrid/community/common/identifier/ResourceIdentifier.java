/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/identifier/ResourceIdentifier.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceIdentifier.java,v $
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

/**
 * A class to create uniqie identifiers for Resources.
 *
 */
public class ResourceIdentifier
    extends UniqueIdentifier
    {
    /**
     * Create a new identifier.
     *
     */
    public ResourceIdentifier()
        {
        super() ;
        }

    /**
     * Create an identifier from a string.
     *
     */
    public ResourceIdentifier(String value)
        {
        super(value) ;
        }
    }


