/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/AccountData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountData.java,v $
 *   Revision 1.3  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
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

public class AccountData
	{
	/**
	 * Public constructor.
	 *
	 */
	public AccountData()
		{
		this(null, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public AccountData(String ident)
		{
		this(ident, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public AccountData(String ident, String description)
		{
		this.ident = ident ;
		this.description = description ;
		}

	/**
	 * Our Account ident.
	 *
	 */
	private String ident ;

	/**
	 * Access to our Account ident.
	 *
	 */
	public String getIdent()
		{
		return this.ident ;
		}

	/**
	 * Access to our Account ident.
	 *
	 */
	public void setIdent(String value)
		{
		this.ident = value ;
		}

	/**
	 * Our Account description.
	 *
	 */
	private String description ;

	/**
	 * Access to our Account description.
	 *
	 */
	public String getDescription()
		{
		return this.description ;
		}

	/**
	 * Access to our Account description.
	 *
	 */
	public void setDescription(String value)
		{
		this.description = value ;
		}

	}
