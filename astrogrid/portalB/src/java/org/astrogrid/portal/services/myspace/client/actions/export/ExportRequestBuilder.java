/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/export/Attic/ExportRequestBuilder.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/29 02:45:22 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: ExportRequestBuilder.java,v $
 * Revision 1.1  2003/06/29 02:45:22  dave
 * Fixed display styles in explorer and add VOTable transform
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.export ;

/**
 * Class to encapsulate an export request.
 *
 */
public class ExportRequestBuilder
	{

	/**
	 * Public constructor.
	 *
	 */
	public ExportRequestBuilder(String path)
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
