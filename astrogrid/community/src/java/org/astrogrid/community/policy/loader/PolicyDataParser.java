/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/loader/Attic/PolicyDataParser.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/24 15:47:38 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyDataParser.java,v $
 *   Revision 1.1  2003/09/24 15:47:38  dave
 *   Added policy database loader tools.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.loader ;

import org.xml.sax.SAXException ;

import org.astrogrid.community.common.util.xml.sax.SAXElementHandler ;
import org.astrogrid.community.common.util.xml.sax.SAXDocumentHandler ;
import org.astrogrid.community.common.util.xml.sax.SAXAttributeHandler ;
import org.astrogrid.community.common.util.xml.sax.SAXCharacterHandler ;

import org.astrogrid.community.policy.server.PolicyManager ;

/**
 * An XML parser for loading the our database from an XML file.
 *
 */
public class PolicyDataParser
	extends SAXElementHandler
	{

	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * Reference to our policy manager.
	 *
	 */
	private PolicyManager manager ;

	/**
	 * Access to our manager.
	 *
	 */
	public PolicyManager getManager()
		{
		return manager ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public PolicyDataParser(PolicyManager manager)
		{
		//
		// Initialise our base class.
		super("community") ;
		//
		// Initialise our manager.
		this.manager = manager ;
		}

	/**
	 * Initialise our parser tree.
	 *
	 */
	public void init()
		{

		if (DEBUG_FLAG) System.out.println("CommunityParser.init()") ;
		if (DEBUG_FLAG) System.out.println("  Manager : " + manager) ;

		//
		// Add a version attribute handler.
		addAttributeHandler(
			new SAXAttributeHandler("version")
				{
				public void parseAttribute(String value)
					{
					setVersion(value) ;
					}
				}
			) ;
		//
		// Add a parser for the accounts.
		AccountDataParser account  = new AccountDataParser(this) ;
		SAXElementHandler accounts = new SAXElementHandler("accounts") ;
		accounts.addElementHandler(account) ;
		addElementHandler(accounts) ;
		//
		// Add a parser for the groups.
		GroupDataParser   group  = new GroupDataParser(this) ;
		SAXElementHandler groups = new SAXElementHandler("groups") ;
		groups.addElementHandler(group) ;
		addElementHandler(groups) ;
		//
		// Add a parser for the resources.
		ResourceDataParser resource  = new ResourceDataParser(this) ;
		SAXElementHandler  resources = new SAXElementHandler("resources") ;
		resources.addElementHandler(resource) ;
		addElementHandler(resources) ;

		//
		// Add a parser for the group membership.
		GroupMemberParser member  = new GroupMemberParser(this) ;
		SAXElementHandler members = new SAXElementHandler("membership") ;
		members.addElementHandler(member) ;
		addElementHandler(members) ;

		//
		// Add a parser for the community data.
		CommunityDataParser community  = new CommunityDataParser(this) ;
		SAXElementHandler communities = new SAXElementHandler("communities") ;
		communities.addElementHandler(community) ;
		addElementHandler(communities) ;

		//
		// Add a parser for the permission data.
		PermissionDataParser permission  = new PermissionDataParser(this) ;
		SAXElementHandler permissions = new SAXElementHandler("permissions") ;
		permissions.addElementHandler(permission) ;
		addElementHandler(permissions) ;




		}

	/**
	 * Handle the version attribute.
	 *
	 */
	protected void setVersion(String value)
		{
		if (DEBUG_FLAG) System.out.println("  Community data version : " + value) ;
		}

	}

