/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/common/src/java/org/astrogrid/filestore/common/identifier/UniqueIdentifier.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:19:27 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: UniqueIdentifier.java,v $
 *   Revision 1.4  2004/11/25 00:19:27  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.3.46.1  2004/10/21 21:00:13  dave
 *   Added mock://xyz URL handler to enable testing of transfer.
 *   Implemented importInit to the mock service and created transfer tests.
 *
 *   Revision 1.3  2004/08/18 19:00:01  dave
 *   Myspace manager modified to use remote filestore.
 *   Tested before checkin - integration tests at 91%.
 *
 *   Revision 1.2.22.1  2004/08/04 17:03:33  dave
 *   Added container to servlet
 *
 *   Revision 1.2  2004/07/14 13:50:29  dave
 *   Merged development branch, dave-dev-200406301228, into HEAD
 *
 *   Revision 1.1.2.3  2004/07/12 14:39:03  dave
 *   Added server repository classes
 *
 *   Revision 1.1.2.2  2004/07/07 14:24:14  dave
 *   Changed internal class DataConrainer to FileContainer
 *
 *   Revision 1.1.2.1  2004/07/05 04:50:29  dave
 *   Created initial FileStore components
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filestore.common.identifier ;

import java.rmi.server.UID ;

/**
 * A base class for unique identifiers.
 * This enable us to swap between different identifier generator implementations.
 * @todo Move this to astrogrid common ?
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
		//
		// Split it on non.
		String[] temp = uid.toString().split("[^a-zA-Z0-9]") ;
		//
		// Start with an empty StringBuffer.
		StringBuffer buffer = new StringBuffer() ;
		for (int i = 0 ; i < temp.length ; i++)
			{
			if (temp[i].length() > 0)
				{
				if (i > 0)
					{
					buffer.append(".") ;
					}
				buffer.append(
					temp[i]
					) ;
				}
			}
		this.string = buffer.toString() ;
        }

    /**
     * Create an identifier from a string.
     * @param ident The identifier string.
	 * @throws IllegalArgumentException If the identifier is null or invalid.
     * @todo This should throw an Exception if the format is not valid.
     *
     */
    public UniqueIdentifier(String ident)
        {
		if (null == ident)
			{
			throw new IllegalArgumentException(
				"Null identifier"
				) ;
			}
        this.string = ident ;
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

	/**
	 * Generate a new identifier.
	 *
	 */
	public static UniqueIdentifier next()
		{
		return new UniqueIdentifier() ;
		}

    }


