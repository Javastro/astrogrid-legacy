/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/create/Attic/CreateRequestBuilder.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/23 23:21:11 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: CreateRequestBuilder.java,v $
 * Revision 1.1  2003/06/23 23:21:11  dave
 * Updated the page actions
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.create ;

/**
 * Class to encapsulate a create container request.
 *
 */
public class CreateRequestBuilder
	{

	/**
	 * Public constructor.
	 *
	 */
	public CreateRequestBuilder(String name, String path)
		{
		this.name = name ;
		this.path = path ;
		}

	/**
	 * Our container name.
	 *
	 */
	private String name ;

	/**
	 * Our container path.
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
			"<newContainerName>"
			) ;
		buffer.append(
			this.path
			) ;
		buffer.append(
			"/"
			) ;
		buffer.append(
			this.name
			) ;

		buffer.append(
			"</newContainerName>"
			) ;
		buffer.append(
			"</request>"
			) ;
		return buffer.toString() ;
		}
	}
