/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/status/Attic/StatusNode.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: StatusNode.java,v $
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.status ;

/**
 * A class to encapsulate the status of a MySpace request.
 *
 */
public class StatusNode
	{
	/**
	 * Public constructor.
	 *
	 */
	public StatusNode()
		{
		}

	/**
	 * The response status.
	 *
	 */
	private String status ;

	/**
	 * Access to our status.
	 *
	 */
	public String getStatus()
		{
		return this.status ;
		}

	/**
	 * Access to our status.
	 *
	 */
	public void setStatus(String status)
		{
		this.status = status ;
		}

	/**
	 * The status details.
	 *
	 */
	private String details ;

	/**
	 * Access to our details.
	 *
	 */
	public String getDetails()
		{
		return this.details ;
		}

	/**
	 * Access to our details.
	 *
	 */
	public void setDetails(String details)
		{
		this.details = details ;
		}
	}
