/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/PolicyPermission.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/08/28 17:33:56 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyPermission.java,v $
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

public class PolicyPermission
	{
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
	 */
	public PolicyPermission(String resource, String group, String action)
		{
		this.group    = group    ;
		this.action   = action   ;
		this.resource = resource ;
		this.valid    = false    ;
		this.reason   = DEFAULT_REASON ;
		}

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
	private boolean valid ;

	/**
	 * Access to the status.
	 *
	 */
	public boolean isValid()
		{
		return this.valid ;
		}

	/**
	 * Access to the status.
	 *
	 */
	public void setValid(boolean valid)
		{
		this.valid = valid ;
		}

	/**
	 * The status reason.
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
