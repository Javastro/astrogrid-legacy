/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/Attic/DataItemRecord.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/18 01:33:15 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: DataItemRecord.java,v $
 * Revision 1.1  2003/06/18 01:33:15  dave
 * Moved message parser into separate class and added service lookup to pages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client ;

/**
 * Class to encapsulate a DataRecord from MySpace.
 *
 */
public class DataItemRecord
	{
	/**
	 * Public constructor.
	 *
	 */
	public DataItemRecord()
		{
		}

	/**
	 * Our name.
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
	 * Our ident.
	 *
	 */
	private String ident ;

	/**
	 * Access to our ident.
	 *
	 */
	public String getIdent()
		{
		return this.ident ;
		}

	/**
	 * Access to our ident.
	 *
	 */
	public void setIdent(String ident)
		{
		this.ident = ident ;
		}

	/**
	 * Our owner.
	 *
	 */
	private String owner ;

	/**
	 * Access to our owner.
	 *
	 */
	public String getOwner()
		{
		return this.owner ;
		}

	/**
	 * Access to our owner.
	 *
	 */
	public void setOwner(String owner)
		{
		this.owner = owner ;
		}

	/**
	 * Our create date (stored as a string).
	 *
	 */
	private String created ;

	/**
	 * Access to our create date.
	 *
	 */
	public String getCreated()
		{
		return this.created ;
		}

	/**
	 * Access to our create date.
	 *
	 */
	public void setCreated(String created)
		{
		this.created = created ;
		}

	/**
	 * Our expiry date (stored as a string).
	 *
	 */
	private String expires ;

	/**
	 * Access to our expiry date.
	 *
	 */
	public String getExpires()
		{
		return this.expires ;
		}

	/**
	 * Access to our expiry date.
	 *
	 */
	public void setExpires(String expires)
		{
		this.expires = expires ;
		}

	/**
	 * Our data size (stored as a string).
	 *
	 */
	private String size ;

	/**
	 * Access to our data size.
	 *
	 */
	public String getSize()
		{
		return this.size ;
		}

	/**
	 * Access to our data size.
	 *
	 */
	public void setSize(String size)
		{
		this.size = size ;
		}

	/**
	 * Our data type (stored as a string).
	 *
	 */
	private String type ;

	/**
	 * Access to our data type.
	 *
	 */
	public String getType()
		{
		return this.type ;
		}

	/**
	 * Access to our data type.
	 *
	 */
	public void setType(String type)
		{
		this.type = type ;
		}

	/**
	 * Our access permissions.
	 *
	 */
	private String permissions ;

	/**
	 * Access to our access permissions.
	 *
	 */
	public String getPermissions()
		{
		return this.permissions ;
		}

	/**
	 * Access to our access permissions.
	 *
	 */
	public void setPermissions(String permissions)
		{
		this.permissions = permissions ;
		}

	/**
	 * Our data URI.
	 *
	 */
	private String uri ;

	/**
	 * Access to our data URI.
	 *
	 */
	public String getURI()
		{
		return this.uri ;
		}

	/**
	 * Access to our data URI.
	 *
	 */
	public void setURI(String uri)
		{
		this.uri = uri ;
		}
	}
