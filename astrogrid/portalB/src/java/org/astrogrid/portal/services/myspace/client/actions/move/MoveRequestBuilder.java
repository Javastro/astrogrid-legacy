/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/move/Attic/MoveRequestBuilder.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 22:29:48 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: MoveRequestBuilder.java,v $
 * Revision 1.1  2003/06/22 22:29:48  dave
 * Added message, actions and page for move
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.move ;

/**
 * Class to encapsulate a copy request.
 *
 */
public class MoveRequestBuilder
	{

	/**
	 * Public constructor.
	 *
	 */
	public MoveRequestBuilder(String source, String destination)
		{
		this.source = source ;
		this.destination = destination ;
		}

	/**
	 * Our source path.
	 *
	 */
	private String source ;

	/**
	 * Our destination path.
	 *
	 */
	private String destination ;

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
			this.source
			) ;
		buffer.append(
			"</serverFileName>"
			) ;
		buffer.append(
			"<newDataItemName>"
			) ;
		buffer.append(
			this.destination
			) ;
		buffer.append(
			"</newDataItemName>"
			) ;
		buffer.append(
			"</request>"
			) ;
		return buffer.toString() ;
		}
	}
