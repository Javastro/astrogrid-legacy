/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/create/Attic/CreateResultParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/23 23:21:12 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: CreateResultParser.java,v $
 * Revision 1.1  2003/06/23 23:21:12  dave
 * Updated the page actions
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.create ;

import org.xml.sax.SAXException ;

import org.astrogrid.portal.util.xml.sax.SAXElementHandler ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.data.DataItemParser  ;

import org.astrogrid.portal.services.myspace.client.status.StatusNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNodeParser  ;

/**
 * An XML parser to handle a create container response.
 *
 */
public class CreateResultParser
	extends SAXElementHandler
	implements CreateResultHandler
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true ;

	/**
	 * Our results handler.
	 *
	 */
	private CreateResultHandler handler ;

	/**
	 * Public constructor.
	 *
	 */
	public CreateResultParser(CreateResultHandler handler)
		{
		//
		// Initialise our XML parser.
		super("results") ;
		//
		// Initialise our handler.
		this.handler = handler ;
		}

	/**
	 * Handle the start of a results element.
	 *
	 */
	protected void startElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("Start of results") ;
		}

	/**
	 * Handle the end of a results element.
	 *
	 */
	protected void closeElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("Close of results") ;
		if (DEBUG_FLAG) System.out.println("----") ;
		}

	/**
	 * Initialise our parsers.
	 *
	 */
	public void init()
		{
		//
		// Add a status parser.
		addElementHandler(
			new StatusNodeParser(this)
			) ;
		//
		// Add a data item parser.
		addElementHandler(
			new DataItemParser(this)
			) ;
		}

	/**
	 * Handle a StatusNode from our StatusNodeParser.
	 *
	 */
	public void handleStatusNode(StatusNode status)
		{
		if (null != handler)
			{
			handler.handleStatusNode(status) ;
			}
		}

	/**
	 * Handle a DataNode from our DataNode parser.
	 *
	 */
	public void handleDataNode(DataNode data)
		{
		if (null != handler)
			{
			handler.handleDataNode(data) ;
			}
		}
	}
