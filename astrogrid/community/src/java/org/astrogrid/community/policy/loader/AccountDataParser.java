/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/loader/Attic/AccountDataParser.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/24 21:56:06 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: AccountDataParser.java,v $
 *   Revision 1.2  2003/09/24 21:56:06  dave
 *   Added setPassword() to AccountManager
 *
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

import org.astrogrid.community.policy.data.AccountData ;

import org.astrogrid.community.policy.server.PolicyManager ;

/**
 * An XML parser for handling Account data.
 *
 */
public class AccountDataParser
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
	public AccountDataParser(PolicyDataParser parent)
		{
		this(parent, "account") ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public AccountDataParser(PolicyDataParser parent, String name)
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
		// Initialise our AccountIdentParser.
		super.init() ;
		//
		// Add an attrib handler for our ident.
		addAttributeHandler(
			new SAXAttributeHandler("ident")
				{
				public void parseAttribute(String value)
					{
					setIdent(value) ;
					}
				}
			) ;
		//
		// Add an attrib handler for our password.
		addAttributeHandler(
			new SAXAttributeHandler("password")
				{
				public void parseAttribute(String value)
					{
					setPassword(value) ;
					}
				}
			) ;
		//
		// Add an character handler for our description.
		addElementHandler(
			new SAXElementHandler("description")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String value)
								throws SAXException
								{
								if (null != account)
									{
									if (DEBUG_FLAG) System.out.println("  PASS : Got account") ;
									account.setDescription(value) ;
									}
								else {
									if (DEBUG_FLAG) System.out.println("  FAIL : Null account") ;
									}
								}
							}
						) ;
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
	 * Our current Account data.
	 *
	 */
	protected AccountData account ;

	/**
	 * Handle the start of our <account> element.
	 * Re-sets our AccountData.
	 *
	 */
	protected void startElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountDataParser.startElement()") ;

		//
		// Reset our account data.
		account = null  ;

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Handle the end of our <account> element.
	 * Calls our PolicyManager to update the server with our AccountData.
	 *
	 */
	protected void closeElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountDataParser.closeElement()") ;

		//
		// If we have an Account.
		if (null != account)
			{
			if (DEBUG_FLAG) System.out.println("  PASS : Got account") ;
			//
			// Try updating the database
			if (null != getManager())
				{
				if (DEBUG_FLAG) System.out.println("  PASS : Got manager") ;
				try {
					account = getManager().setAccount(account) ;
					}
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("  FAIL : Exception while creating Account") ;
					ouch.printStackTrace() ;
					}
				if (null != account)
					{
					if (DEBUG_FLAG) System.out.println("  PASS : Updated account") ;
					}
				}
			}
		//
		// If we don't have an Account.
		else {
			if (DEBUG_FLAG) System.out.println("  FAIL : Null account") ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Handle our Account ident.
	 * Calls our PolicyManager to load or create our AccountData.
	 * Creates a new Account if it does not exist.
	 *
	 */
	protected void setIdent(String ident)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountDataParser.setIdent()") ;
		if (DEBUG_FLAG) System.out.println("  Ident : " + ident) ;

		if (null != getManager())
			{
			//
			// See if we can load the Account.
			if (null == account)
				{
				if (DEBUG_FLAG) System.out.println("  Loading account ....") ;
				try {
					account = getManager().getAccount(ident) ;
					}
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("  FAIL : Exception while loading Account") ;
					ouch.printStackTrace() ;
					}
				}

			//
			// See if we can create the Account.
			if (null == account)
				{
				if (DEBUG_FLAG) System.out.println("  Creating account ....") ;
				try {
					account = getManager().addAccount(ident) ;
					}
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("  FAIL : Exception while creating Account") ;
					ouch.printStackTrace() ;
					}
				}

			if (null != account)
				{
				if (DEBUG_FLAG) System.out.println("  PASS : Got account") ;
				}
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}

	/**
	 * Handle our Account password.
	 * This should be handled by a separate call to the PolicyManager.
	 *
	 */
	protected void setPassword(String value)
		{
		if (DEBUG_FLAG) System.out.println("") ;
		if (DEBUG_FLAG) System.out.println("----\"----") ;
		if (DEBUG_FLAG) System.out.println("AccountDataParser.setPassword()") ;
		if (DEBUG_FLAG) System.out.println("  Password : " + value) ;

		//
		// Update the Account.
		if (null != account)
			{
			if (DEBUG_FLAG) System.out.println("  PASS : Got account") ;
			//
			// Try updating the database
			if (null != getManager())
				{
				if (DEBUG_FLAG) System.out.println("  PASS : Got manager") ;
				try {
					account = getManager().setPassword(account.getIdent().toString(), value) ;
					}
				catch (Exception ouch)
					{
					if (DEBUG_FLAG) System.out.println("  FAIL : Exception while creating Account") ;
					ouch.printStackTrace() ;
					}
				}
			if (null != account)
				{
				if (DEBUG_FLAG) System.out.println("  PASS : Got account") ;
				}
			}
		//
		// If we don't have an Account.
		else {
			if (DEBUG_FLAG) System.out.println("  FAIL : Null account") ;
			}

		if (DEBUG_FLAG) System.out.println("----\"----") ;
		}
	}

