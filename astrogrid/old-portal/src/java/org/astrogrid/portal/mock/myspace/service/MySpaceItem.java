/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/java/org/astrogrid/portal/mock/myspace/service/Attic/MySpaceItem.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/09 13:33:33 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: MySpaceItem.java,v $
 * Revision 1.1  2003/06/09 13:33:33  dave
 * Fixed bad directory structure
 *
 * Revision 1.1  2003/06/09 10:20:50  dave
 * Added Axis integration tests
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.mock.myspace.service ;

/**
 * Class to encapsulate a data item in MySpace.
 * Based on the current MySpace code in CVS.
 *
 */
public class MySpaceItem
	{
	/**
	 * Public constructor.
	 *
	 */
	public MySpaceItem()
		{
		}

	/**
	 * Public constructor.
	 *
	 */
	public MySpaceItem(
		String ident,
		String name,
		String path
		)
		{
		this.ident = ident ;
		this.name  = name ;
		this.path  = path ;
		}

	/**
	 * Our item identifier.
	 *
	 */
	private String ident ;

	/**
	 * Access to our identifier.
	 *
	 */
	public String getIdent()
		{
		return this.ident ;
		}

	/**
	 * Access to our identifier.
	 *
	 */
	public void setIdent(String ident)
		{
		this.ident = ident ;
		}

	/**
	 * Our item name.
	 *
	 */
	private String name ;

	/**
	 * Access to our name.
	 *
	 */
	public String getName()
		{
		return this.name ;
		}

	/**
	 * Access to our name.
	 *
	 */
	public void setName(String name)
		{
		this.name = name ;
		}

	/**
	 * Our item path.
	 *
	 */
	private String path ;

	/**
	 * Access to our path.
	 *
	 */
	public String getPath()
		{
		return this.path ;
		}

	/**
	 * Access to our path.
	 *
	 */
	public void setPath(String path)
		{
		this.path = path ;
		}

	}
