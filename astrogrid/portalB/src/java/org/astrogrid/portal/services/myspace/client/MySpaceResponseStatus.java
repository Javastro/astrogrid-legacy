/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/Attic/MySpaceResponseStatus.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/18 12:05:43 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: MySpaceResponseStatus.java,v $
 * Revision 1.1  2003/06/18 12:05:43  dave
 * Added debug and response status
 *
 * Revision 1.1  2003/06/18 01:33:15  dave
 * Moved message parser into separate class and added service lookup to pages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client ;

/**
 * A class to encapsulate the results of a lookup details request.
 *
 */
public class MySpaceResponseStatus
	{
	/**
	 * Public constructor.
	 *
	 */
	public MySpaceResponseStatus()
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
