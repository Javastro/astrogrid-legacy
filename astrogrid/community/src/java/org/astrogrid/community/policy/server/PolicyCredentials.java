/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/PolicyCredentials.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/08/28 17:33:56 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyCredentials.java,v $
 *   Revision 1.1  2003/08/28 17:33:56  dave
 *   Initial policy prototype
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

public class PolicyCredentials
	{
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

	}
