/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/PolicyCredentials.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/04 23:58:10 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyCredentials.java,v $
 *   Revision 1.2  2003/09/04 23:58:10  dave
 *   Experimenting with using our own DataObjects rather than the Axis generated ones ... seems to work so far
 *
 *   Revision 1.1  2003/09/03 06:39:13  dave
 *   Rationalised things into one set of SOAP stubs and one set of data objects for both client and server.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.data ;

public class PolicyCredentials
	{
	/**
	 * Status code for valid credentials.
	 *
	 */
	public static final int STATUS_CREDENTIALS_VALID = 0xFF ;

	/**
	 * Status code for credentials unchecked.
	 *
	 */
	public static final int STATUS_CREDENTIALS_UNCHECKED = 0x00 ;

	/**
	 * Status code for credentials invalid.
	 *
	 */
	public static final int STATUS_CREDENTIALS_INVALID = 0x01 ;

	/**
	 * Public constructor.
	 *
	 */
	public PolicyCredentials()
		{
		this(null, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public PolicyCredentials(String account, String group)
		{
		this.group   = group   ;
		this.account = account ;
		}

	/**
	 * Our Account ident.
	 *
	 */
	private String account ;

	/**
	 * Access to our Account ident.
	 *
	 */
	public String getAccount()
		{
		return this.account ;
		}

	/**
	 * Access to our Account ident.
	 *
	 */
	public void setAccount(String account)
		{
		this.account = account ;
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
	 * The status value.
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
	 * The status reason, explains why.
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
