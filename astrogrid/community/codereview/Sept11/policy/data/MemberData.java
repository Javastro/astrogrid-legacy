package org.astrogrid.community.policy.data;


public class MemberData {

   
	/**
	 * Public constructor.
	 *
	 */
	public MemberData()
		{
		this(null, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public MemberData(String groupIdent)
		{
		this(groupIdent, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public MemberData(String groupIdent, String accountIdent)
		{
		this.groupIdent = groupIdent ;
		this.accountIdent = accountIdent ;
		}

	/**
	 * Our Group ident.
	 *
	 */
	private String groupIdent ;

	/**
	 * Access to our Group ident.
	 *
	 */
	public String getGroupIdent()
		{
		return this.groupIdent ;
		}

	/**
	 * Access to our Group ident.
	 *
	 */
	public void setGroupIdent(String value)
		{
		this.groupIdent = value ;
		}

	/**
	 * Our Group description.
	 *
	 */
	private String accountIdent ;

	/**
	 * Access to our Group description.
	 *
	 */
	public String getAccountIdent()
		{
		return this.accountIdent ;
		}

	/**
	 * Access to our Group description.
	 *
	 */
	public void setAccountIdent(String value)
		{
		this.accountIdent = value ;
		}


}