/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/exception/CommunityException.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/12 15:22:17 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityException.java,v $
 *   Revision 1.2  2004/03/12 15:22:17  dave
 *   Merged development branch, dave-dev-200403101018, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/10 15:29:28  dave
 *   Added CommunityException base class.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.exception ;

/**
 * Base class for our Exceptions.
 * This means that client applications can catch a generic CommunityException if they need to.
 * As I add better exception handling I will add more specific types of CommunityException.
 *
 */
public class CommunityException
	extends Exception
	{
	}
