/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/PolicyPermission.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/03 06:39:13 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyPermission.java,v $
 *   Revision 1.1  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.data ;

public class PolicyPermission
	{
	/**
	 * Status code for permission granted.
	 *
	 */
	public static final int STATUS_PERMISSION_GRANTED = 0xFF ;

	/**
	 * Status code for no permission.
	 *
	 */
	public static final int STATUS_PERMISSION_UNKNOWN = 0x00 ;

	/**
	 * Status code for permission revoked.
	 *
	 */
	public static final int STATUS_PERMISSION_REVOKED = 0x01 ;

	/**
	 * The default reason, no permission set.
	 *
	 */
	public static final String DEFAULT_REASON = "No permissions set" ;

	/**
	 * Public constructor.
	 *
	 */
	public PolicyPermission()
		{
		}

	/**
	 * Public constructor.
	 *
	public PolicyPermission(String resource, String group, String action)
		{
		this.group    = group    ;
		this.action   = action   ;
		this.resource = resource ;
		this.valid    = false    ;
		this.reason   = DEFAULT_REASON ;
		}
	 */

	/**
	 * Our Group ident.
	 *
	 */
	private String group ;

	/**
	 * Access to our Group ident.
	 *
	 */
	public String getGroup()
		{
		return this.group ;
		}

	/**
	 * Access to our Group ident.
	 *
	 */
	public void setGroup(String group)
		{
		this.group = group ;
		}

	/**
	 * Our Resource ident.
	 *
	 */
	private String resource ;

	/**
	 * Access to our Resource ident.
	 *
	 */
	public String getResource()
		{
		return this.resource ;
		}

	/**
	 * Access to our Resource ident.
	 *
	 */
	public void setResource(String resource)
		{
		this.resource = resource ;
		}

	/**
	 * The allowed action.
	 *
	 */
	private String action ;

	/**
	 * Access to the action.
	 *
	 */
	public String getAction()
		{
		return this.action ;
		}

	/**
	 * Access to the action.
	 *
	 */
	public void setAction(String action)
		{
		this.action = action ;
		}

	/**
	 * The permission status.
	 *
	 */
	private int status ;

	/**
	 * Access to the status.
	 *
	 */
	public int getStatus()
		{
		return this.status ;
		}

	/**
	 * Access to the status.
	 *
	 */
	public void setStatus(int status)
		{
		this.status = status ;
		}

	/**
	 * The status reason, explains status.
	 *
	 */
	private String reason ;

	/**
	 * Access to the reason.
	 *
	 */
	public String getReason()
		{
		return this.reason ;
		}

	/**
	 * Access to the reason.
	 *
	 */
	public void setReason(String reason)
		{
		this.reason = reason ;
		}
	}
