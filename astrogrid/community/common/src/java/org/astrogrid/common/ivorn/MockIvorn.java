/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/common/ivorn/MockIvorn.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/12 15:22:17 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: MockIvorn.java,v $
 *   Revision 1.2  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/12 13:44:43  dave
 *   Moved MockIvornFactory to MockIvorn
 *
 *   Revision 1.1.2.2  2004/03/12 13:38:03  dave
 *   Added test case for MockIvorn.
 *   Fixed bugs in CommunityIvornParser.
 *
 *   Revision 1.1.2.1  2004/03/12 00:46:25  dave
 *   Added CommunityIvornFactory and CommunityIvornParser.
 *   Added MockIvorn (to be moved to common project).
 *
 * </cvs:log>
 *
 */
package org.astrogrid.common.ivorn ;

import java.net.URI ;
import java.net.URISyntaxException ;

import org.astrogrid.store.Ivorn ;

/**
 * A factory for mock Ivorn identifiers.
 * If this works, then most of it should probably be move to org.astrogrid.common.MockIvorn
 *
 */
public class MockIvorn
	{

	/**
	 * The default mock identifier.
	 * If an Ivorn matches this ident, delegate factories should generate a mock delegate.
	 *
	 */
	public static final String MOCK_IDENT = "org.astrogrid.mock" ;

	/**
	 * Create a mock Ivorn.
	 * @param ident The ivo ident, with no extra fields.
	 *
	 */
	public static Ivorn createIvorn(String ident)
		throws URISyntaxException
		{
		return new Ivorn(
			createIdent(ident)
			) ;
		}

	/**
	 * Create a mock Ivorn.
	 * @param ident The ivo ident, with no extra fields.
	 * @param path  The path, added after the ident.
	 *
	 */
	public static Ivorn createIvorn(String ident, String path)
		throws URISyntaxException
		{
		return new Ivorn(
			createIdent(ident, path)
			) ;
		}

	/**
	 * Create a mock ident.
	 * @param ident The ivo ident, with no extra fields.
	 *
	 */
	public static String createIdent(String ident)
		{
		return createIdent(ident, null, null, null) ;
		}

	/**
	 * Create a mock ident.
	 * @param ident The ivo ident, with no extra fields.
	 * @param path  The path, added after the ident.
	 *
	 */
	public static String createIdent(String ident, String path)
		{
		return createIdent(ident, path, null, null) ;
		}

	/**
	 * Create a mock ident.
	 * @param ident     The ivo ident, with no extra fields.
	 * @param path      The path, added after the ident.
	 * @param fragment  The URI fragment string.
	 *
	 */
	public static String createIdent(String ident, String path, String fragment)
		{
		return createIdent(ident, path, null, fragment) ;
		}

	/**
	 * Create a mock ident.
	 * This allows you to set almost all of the URI fields.
	 * @param ident     The ivo ident, with no extra fields.
	 * @param path      The path, added after the ident.
	 * @param query     The URI query string.
	 * @param fragment  The URI fragment string.
	 *
	 */
	public static String createIdent(String ident, String path, String query, String fragment)
		{
		//
		// Put it all back together.
		StringBuffer buffer = new StringBuffer() ;
		//
		// Start with the ivo scheme.
		buffer.append(Ivorn.SCHEME) ;
		buffer.append("://") ;
		//
		// If we have an ident.
		if (null != ident)
			{
			//
			// If the ident is not empty.
			if (ident.length() > 0)
				{
				//
				// If the ident is not a mock mock ident
				if (false == ident.startsWith(MOCK_IDENT))
					{
					//
					// Add the mock ident to the beginning.
					buffer.append(MOCK_IDENT) ;
					buffer.append(".") ;
					}
				buffer.append(ident) ;
				}
			//
			// If the ident is empty.
			else {
				//
				// Use the mock ident instead.
				buffer.append(MOCK_IDENT) ;
				}
			}
		//
		// If we don't have an ident.
		else {
			//
			// Use the mock ident.
			buffer.append(MOCK_IDENT) ;
			}
		//
		// Add the rest of the elements.
		if (null != path)
			{
			buffer.append("/") ;
			buffer.append(path) ;
			}
		if (null != query)
			{
			buffer.append("?") ;
			buffer.append(query) ;
			}
		if (null != fragment)
			{
			buffer.append("#") ;
			buffer.append(fragment) ;
			}
		//
		// Return the new string.
		return buffer.toString() ;
		}

	/**
	 * Returns true if the Ivorn is a mock identifier.
	 * If this works, then it should probably be move to org.astrogrid.store.Ivorn.
	 *
	 */
	public static boolean isMock(Ivorn ivorn)
		{
		//
		// Convert the Ivorn into a String.
		return isMock(
			ivorn.toString()
			) ;
		}

	/**
	 * Returns true if the String is a mock identifier.
	 * If this works, then it should probably be move to org.astrogrid.store.Ivorn.
	 *
	 */
	public static boolean isMock(String string)
		{
		//
		// Convert the String into a URI.
		try {
			return isMock(
				new URI(string)
				) ;
			}
		//
		// If it isn't valid, then it isn't a valid mock.
		catch (URISyntaxException ouch)
			{
			return false ;
			}
		}

	/**
	 * Returns true if the URI is a mock identifier.
	 * If this works, then it should probably be move to org.astrogrid.store.Ivorn.
	 *
	 */
	public static boolean isMock(URI uri)
		{
		//
		// Check if the host part starts with the mock ident.
		return (null != uri.getHost()) ? uri.getHost().startsWith(MOCK_IDENT) : false ;
		}

	}
