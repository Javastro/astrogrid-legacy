/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/CommunityData.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/06 20:10:07 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityData.java,v $
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.data ;

public class CommunityData
	{
	/**
	 * Public constructor.
	 *
	 */
	public CommunityData()
		{
		this(null, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public CommunityData(String ident)
		{
		this(ident, null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public CommunityData(String ident, String description)
		{
		this.ident = ident ;
		this.description = description ;
		}

	/**
	 * Our Community ident.
	 *
	 */
	private String ident ;

	/**
	 * Access to our Community ident.
	 *
	 */
	public String getIdent()
		{
		return this.ident ;
		}

	/**
	 * Access to our Community ident.
	 *
	 */
	public void setIdent(String value)
		{
		this.ident = value ;
		}

	/**
	 * Our service url.
	 *
	 */
	private String service ;

	/**
	 * Access to our service url.
	 *
	 */
	public String getServiceUrl()
		{
		return this.service ;
		}

	/**
	 * Access to our Community url.
	 *
	 */
	public void setServiceUrl(String value)
		{
		this.service = value ;
		}

	/**
	 * Our manager url.
	 *
	 */
	private String manager ;

	/**
	 * Access to our manager url.
	 *
	 */
	public String getManagerUrl()
		{
		return this.manager ;
		}

	/**
	 * Access to our Community url.
	 *
	 */
	public void setManagerUrl(String value)
		{
		this.manager = value ;
		}

	/**
	 * Our Community description.
	 *
	 */
	private String description ;

	/**
	 * Access to our Community description.
	 *
	 */
	public String getDescription()
		{
		return this.description ;
		}

	/**
	 * Access to our Community description.
	 *
	 */
	public void setDescription(String value)
		{
		this.description = value ;
		}

	}
