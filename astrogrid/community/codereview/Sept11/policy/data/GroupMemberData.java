/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/codereview/Sept11/policy/data/Attic/GroupMemberData.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/11 10:24:21 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupMemberData.java,v $
 *   Revision 1.1  2003/09/11 10:24:21  KevinBenson
 *   *** empty log message ***
 *
 *   Revision 1.1  2003/09/09 13:48:09  dave
 *   Added addGroupMember - only local accounts and groups to start with.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.data ;

/**
 * A Castor data object to represent a member of a Group.
 * This should only be used server side, not client side.
 *
 */
public class GroupMemberData
	{
	/**
	 * Public constructor.
	 *
	 */
	public GroupMemberData()
		{
		this(null, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public GroupMemberData(String account, String group)
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
	public void setAccount(String value)
		{
		this.account = value ;
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
	}
