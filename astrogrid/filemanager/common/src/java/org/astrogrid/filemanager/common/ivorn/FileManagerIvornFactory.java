/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/ivorn/Attic/FileManagerIvornFactory.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:29 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileManagerIvornFactory.java,v $
 *   Revision 1.2  2004/11/25 00:20:29  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.4  2004/11/18 17:10:21  dave
 *   Updated mock ivorn handling
 *
 *   Revision 1.1.2.3  2004/11/18 16:06:11  dave
 *   Added delegate resolver and tests ....
 *
 *   Revision 1.1.2.2  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.1  2004/11/04 15:50:17  dave
 *   Added ivorn pareser and factory.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common.ivorn ;

import java.util.Random ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.store.Ivorn ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

import java.net.URISyntaxException ;

/**
 * A factory for generating filemanager related Ivorn identifiers.
 *
 */
public class FileManagerIvornFactory
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(FileManagerIvornFactory.class);

    /**
     * Create a filemanager ivorn.
     * @param  base The base filemanager ivorn.
     * @return A new resource ivorn.
     * @throws FileManagerIdentifierException if the filemanager or resource identifiers are invalid or null.
     *
     */
    public Ivorn ivorn(Ivorn base)
        throws FileManagerIdentifierException
		{
		try {
			return new Ivorn(
				ident(
					base
					)
				) ;
			}
		catch (URISyntaxException ouch)
			{
			throw new FileManagerIdentifierException(
				ouch
				) ;
			}
		}

    /**
     * Create a filemanager ident.
     * @param  base   The filemanager service ivorn.
     * @return A new (ivorn) identifer.
     * @throws FileManagerIdentifierException if the filemanager or resource identifiers are null.
     * @todo Check that the base does not already have a #fragment ....
     *
     */
    public String ident(Ivorn base)
        throws FileManagerIdentifierException
        {
		return this.ident(
			base.toString()
			);
		}

    /**
     * Create a filemanager ident.
     * @param  base   The filemanager service ivorn.
     * @param  ident  The resource identifier.
     * @return A new (ivorn) identifer.
     * @throws FileManagerIdentifierException if the filemanager or resource identifiers are null.
     * @todo Check that the base does not already have a #fragment ....
     *
     */
    public String ident(String base)
        throws FileManagerIdentifierException
        {
		return this.ident(
			base,
			this.unique()
			);
		}

    /**
     * Create a filemanager ident.
     * @param  base   The filemanager service ivorn.
     * @param  ident  The resource identifier.
     * @return A new (ivorn) identifer.
     * @throws FileManagerIdentifierException if the filemanager or resource identifiers are null.
     * @todo Check that the base does not already have a #fragment ....
     *
     */
    public String ident(String base, String ident)
        throws FileManagerIdentifierException
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("FileManagerIvornFactory.ident(String, String)") ;
        log.debug("  Base   : " + base) ;
        log.debug("  Ident  : " + ident) ;
        //
        // Check for null params.
        if (null == base)
        	{
        	throw new FileManagerIdentifierException(
        		"Null service identifier"
        		) ;
        	}
        if (null == ident)
        	{
        	throw new FileManagerIdentifierException(
        		"Null resource identifier"
        		) ;
        	}
        //
        // Put it all together.
        StringBuffer buffer = new StringBuffer() ;
        //
        // If the service identifier isn't an Ivorn yet.
        if (false == base.startsWith(Ivorn.SCHEME))
            {
            buffer.append(Ivorn.SCHEME) ;
            buffer.append("://") ;
            }
        buffer.append(base) ;
        buffer.append("#") ;
        buffer.append(ident) ;
        log.debug("  Result : " + buffer.toString()) ;
        //
        // Return the new string.
        return buffer.toString() ;
        }

	/**
	 * Internal random number generator.
	 *
	 */
	private Random random = new Random() ;

	/**
	 * Generate a new unique identifier.
	 * @return A new node identifier.
	 *
	 */
	public String unique()
		{
		StringBuffer buffer = new StringBuffer() ;
		buffer.append("node") ;
		buffer.append("-") ;
		buffer.append(
			Long.toHexString(
				System.currentTimeMillis()
				).toUpperCase()
			) ;
		buffer.append("-") ;
		buffer.append(
			Integer.toHexString(
				random.nextInt()
				).toUpperCase()
			) ;
		return buffer.toString() ;
		}
    }
