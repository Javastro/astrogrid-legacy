/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/GroupData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupData.java,v $
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.data ;

public class GroupData
	{
	/**
	 * Public constructor.
	 *
	 */
	public GroupData()
		{
		this(null, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public GroupData(String ident)
		{
		this(ident, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public GroupData(String ident, String description)
		{
		this.ident = ident ;
		this.description = description ;
		}

	/**
	 * Our Group ident.
	 *
	 */
	private String ident ;

	/**
	 * Access to our Group ident.
	 *
	 */
	public String getIdent()
		{
		return this.ident ;
		}

	/**
	 * Access to our Group ident.
	 *
	 */
	public void setIdent(String value)
		{
		this.ident = value ;
		}

	/**
	 * Our Group description.
	 *
	 */
	private String description ;

	/**
	 * Access to our Group description.
	 *
	 */
	public String getDescription()
		{
		return this.description ;
		}

	/**
	 * Access to our Group description.
	 *
	 */
	public void setDescription(String value)
		{
		this.description = value ;
		}

	}
