/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/data/Attic/CommunityIdent.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/10 06:03:27 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: CommunityIdent.java,v $
 *   Revision 1.2  2003/09/10 06:03:27  dave
 *   Added remote capability to Accounts
 *
 *   Revision 1.1  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.data ;

import org.astrogrid.community.policy.data.CommunityConfig ;

public class CommunityIdent
	{
	/**
	 * The separator for name and community.
	 *
	 */
	public static char IDENT_SEPARATOR = '@' ;

	/**
	 * Flag to indicate the ident is valid.
	 *
	 */
	private boolean valid = false ;

	/**
	 * Flag to indicate the ident is local.
	 *
	 */
	private boolean local = false ;

	/**
	 * Our ident.
	 *
	 */
	private String ident ;

	/**
	 * Our ident name.
	 *
	 */
	private String name ;

	/**
	 * Our Ident community.
	 *
	 */
	private String community ;

	/**
	 * Public constructor.
	 *
	 */
	public CommunityIdent(String ident)
		{
		//
		// Save the ident.
		this.ident = ident ;
		//
		// Find the first and last separator.
		int first = ident.indexOf(IDENT_SEPARATOR) ;
		int last  = ident.lastIndexOf(IDENT_SEPARATOR) ;
		//
		// If the ident contains separator.
		if (-1 != first)
			{
			//
			// If the first and last are the same.
			if (first == last)
				{
				//
				// Split the ident into name and community.
				this.name = ident.substring(0, first) ;
				this.community = ident.substring(first + 1) ;
//
// Check the lengths ...
//
				//
				// Check if the community is local.
				if (CommunityConfig.getConfig().getCommunityName().equals(this.community))
					{
					this.valid = true ;
					this.local = true ;
					}
				//
				// If the community is not local.
				else {
					this.valid = true ;
					this.local = false ;
					}
				}
			//
			// If the first and last do not match.
			else {
				this.valid = false ;
				this.local = false ;
				this.name  = null ;
				this.community = null ;
				}
			}
		//
		// If the ident does not contain a community.
		else {
			//
			// Use the local community ident.
			this.name = ident ;
			this.community = CommunityConfig.getConfig().getCommunityName() ;
			this.ident = this.name +  IDENT_SEPARATOR + this.community ;
			this.valid = true ;
			this.local = true ;
			}
		}

	/**
	 * Public constructor.
	 *
	 */
	public CommunityIdent(String name, String community)
		{
		this(name +  IDENT_SEPARATOR + community) ;
		}

	/**
	 * Is this a valid ident.
	 *
	 */
	public boolean isValid()
		{
		return this.valid ;
		}

	/**
	 * Is this a local ident.
	 *
	 */
	public boolean isLocal()
		{
		return this.local ;
		}

	/**
	 * Get the name from this ident.
	 *
	 */
	public String getName()
		{
		return this.name ;
		}

	/**
	 * Get the community name from this ident.
	 *
	 */
	public String getCommunity()
		{
		return this.community ;
		}

	/**
	 * Convert the ident as a string.
	 *
	 */
	public String toString()
		{
		return this.ident ;
		}

	}
