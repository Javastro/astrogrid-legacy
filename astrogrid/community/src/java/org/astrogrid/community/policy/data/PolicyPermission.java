/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/PolicyPermission.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 03:15:06 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyPermission.java,v $
 *   Revision 1.4  2003/09/11 03:15:06  dave
 *   1) Implemented PolicyService internals - no tests yet.
 *   2) Added getLocalAccountGroups and getRemoteAccountGroups to PolicyManager.
 *   3) Added remote access to groups.
 *
 *   Revision 1.3  2003/09/10 02:56:03  dave
 *   Added PermissionManager and tests
 *
 *   Revision 1.2  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
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
	 * Status code for credentials invalid.
	 *
	 */
	public static final int STATUS_CREDENTIALS_INVALID = 0x02 ;

	/**
	 * Permission unknown.
	 *
	 */
	public static final String REASON_PERMISSION_UNKNOWN = "Permissions unknown" ;

	/**
	 * No permission set.
	 *
	 */
	public static final String REASON_NO_PERMISSION = "No permissions set" ;

	/**
	 * Invalid credentials.
	 *
	 */
	public static final String REASON_CREDENTIALS_INVALID = "Credentials invalid" ;


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
		this.status   = STATUS_PERMISSION_UNKNOWN ;
		this.reason   = REASON_PERMISSION_UNKNOWN ;
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
	public void setGroup(String value)
		{
		this.group = value ;
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
	public void setResource(String value)
		{
		this.resource = value ;
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
	public void setAction(String value)
		{
		this.action = value ;
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
	public void setStatus(int value)
		{
		this.status = value ;
		}

	/**
	 * Easy check if the status is granteed.
	 *
	 */
	public boolean isValid()
		{
		return (STATUS_PERMISSION_GRANTED == this.status) ;
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
	public void setReason(String value)
		{
		this.reason = value ;
		}
	}
