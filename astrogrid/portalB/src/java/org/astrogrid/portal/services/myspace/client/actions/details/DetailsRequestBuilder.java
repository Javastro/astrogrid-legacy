/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/details/Attic/DetailsRequestBuilder.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: DetailsRequestBuilder.java,v $
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.details ;

/**
 * Class to encapsulate a details request.
 *
 */
public class DetailsRequestBuilder
	{

	/**
	 * Public constructor.
	 *
	 */
	public DetailsRequestBuilder(String path)
		{
		this.path = path ;
		}

	/**
	 * Our path.
	 *
	 */
	private String path ;

	/**
	 * Get our request as a String containing XML.
	 *
	 */
	public String toString()
		{
		StringBuffer buffer = new StringBuffer() ;
		buffer.append(
			"<request>"
			) ;
		buffer.append(
			"<userID>frog</userID>"
			) ;
		buffer.append(
			"<communityID>frogs</communityID>"
			) ;
		buffer.append(
			"<jobID>0000</jobID>"
			) ;
		buffer.append(
			"<serverFileName>"
			) ;
		buffer.append(
			this.path
			) ;
		buffer.append(
			"</serverFileName>"
			) ;
		buffer.append(
			"</request>"
			) ;
		return buffer.toString() ;
		}
	}
