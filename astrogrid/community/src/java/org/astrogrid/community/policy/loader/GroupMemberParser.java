/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/loader/Attic/GroupMemberParser.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/24 15:47:38 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: GroupMemberParser.java,v $
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

import org.astrogrid.community.policy.data.GroupMemberData ;

import org.astrogrid.community.policy.server.PolicyManager ;

/**
 * An XML parser for handling GroupMembership data.
 *
 */
public class GroupMemberParser
	extends SAXElementHandler
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	protected static final boolean DEBUG_FLAG = true ;

	/**
	 * A reference to our parent parser.
	 *
	 */
	private PolicyDataParser parent ;

	/**
	 * Access to our parent parser.
	 *
	 */
	public PolicyDataParser getParent()
		{
		return this.parent ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public GroupMemberParser(PolicyDataParser parent)
		{
		this(parent, "member") ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public GroupMemberParser(PolicyDataParser parent, String name)
		{
		super(name) ;
		this.parent = parent ;
		}

	/**
	 * Initialise our parser tree.
	 *
	 */
	public void init()
		{
		//
		// Initialise our GroupIdentParser.
		super.init() ;
		//
		// Add an attrib handler for our Group ident.
		addAttributeHandler(
			new SAXAttributeHandler("group")
				{
				public void parseAttribute(String value)
					{
					group = value ;
					}
				}
			) ;
		//
		// Add an attrib handler for our Account ident.
		addAttributeHandler(
			new SAXAttributeHandler("account")
				{
				public void parseAttribute(String value)
					{
					account = value ;
					}
				}
			) ;
		}

	/**
	 * Reference to our PolicyManager.
	 *
	 */
	private PolicyManager manager ;

	/**
	 * Access to our PolicyManager.
	 *
	 */
	public PolicyManager getManager()
		{
		if (null == manager)
			{
			if (null != parent)
				{
				//
				// Get the PolicyManager from our parent.
				manager = parent.getManager() ;
				}
			}
		return this.manager ;
		}

	/**
	 * Our Group ident.
	 *
	 */
	protected String group ;

	/**
	 * Our Account ident.
	 *
	 */
	protected String account ;

	/**
	 * Handle the start of our element.
	 * Re-sets our Group and Account idents.
	 *
	 */
	protected void startElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupDataParser.startElement()") ;

		//
		// Reset our data.
		group   = null ;
		account = null ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Handle the end of our element.
	 * Calls our PolicyManager to add the account to the group.
	 *
	 */
	protected void closeElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("GroupDataParser.closeElement()") ;

		GroupMemberData membership = null ;
		//
		// If we have a Group and Account.
		if ((null != group) && (null != account))
			{
			if (DEBUG_FLAG) System.out.println("  PASS : Got group and account") ;
			//
			// Try updating the database
			if (null != getManager())
				{
				if (DEBUG_FLAG) System.out.println("  PASS : Got manager") ;
				try {
					membership = getManager().addGroupMember(account, group) ;
					}
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("  FAIL : Exception while creating GroupMember") ;
					ouch.printStackTrace() ;
					}
				if (null != group)
					{
					if (DEBUG_FLAG) System.out.println("  PASS : Updated group") ;
					}
				}
			}
		//
		// If we don't have Group and Account.
		else {
			if (DEBUG_FLAG) System.out.println("  FAIL : Null group or account") ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	}

