/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/status/Attic/StatusNodeParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: StatusNodeParser.java,v $
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.status ;

import org.xml.sax.SAXException ;

import org.astrogrid.portal.util.xml.sax.SAXElementHandler  ;
import org.astrogrid.portal.util.xml.sax.SAXDocumentHandler ;
import org.astrogrid.portal.util.xml.sax.SAXAttributeHandler ;
import org.astrogrid.portal.util.xml.sax.SAXCharacterHandler ;

/**
 * A SAX XML parser to parse the status of a MySpace request.
 *
 */
public class StatusNodeParser
	extends SAXElementHandler
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true ;

	/**
	 * Reference to our StatusNodeHandler.
	 * Notified when we have completed a new StatusNode.
	 *
	 */
	protected StatusNodeHandler handler ;

	/**
	 * Public constructor.
	 * Needs a reference to a StatusNodeHandler to pass the completed StatusNode to.
	 *
	 */
	public StatusNodeParser(StatusNodeHandler handler)
		{
		//
		// Initialise our XML parser.
		super("status") ;
		//
		// Initialise our handler
		this.handler = handler ;
		}

	/**
	 * Our new StatusNode.
	 * This is NOT thread safe.
	 *
	 */
	protected StatusNode status ;

	/**
	 * Handle the start of our XML element.
	 * Creates a new StatusNode to populate.
	 *
	 */
	protected void startElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("Start of status node element") ;
		//
		// Create a new StatusNode.
		this.status = new StatusNode() ;
		}

	/**
	 * Handle the end of our XML element.
	 * Notifies our StatusNodeHandler, passing it the completed StatusNode.
	 *
	 */
	protected void closeElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("Close of status node element") ;
		//
		// Notify our handler.
		if (null != handler)
			{
			handler.handleStatusNode(status) ;
			}
		if (DEBUG_FLAG) System.out.println("----") ;
		}

	/**
	 * Initialise our element handlers.
	 *
	 */
	public void init()
		{
		//
		// Add a <status> handler.
		addElementHandler(
			new SAXElementHandler("status")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Status : " + text) ;
								if (null != status)
									{
									status.setStatus(text) ;
									}
								}
							}
						);
					}
				}
			) ;
		//
		// Add a <details> handler.
		addElementHandler(
			new SAXElementHandler("details")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Details : " + text) ;
								if (null != status)
									{
									status.setDetails(text) ;
									}
								}
							}
						);
					}
				}
			) ;
		//
		// Add a <currentDate> handler.
		addElementHandler(
			new SAXElementHandler("currentDate")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Date : " + text) ;
								//
								// Not implemented ....
								//
								}
							}
						);
					}
				}
			) ;
		}
	}
