/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/lookup/Attic/LookupResultsParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: LookupResultsParser.java,v $
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.lookup ;

import org.xml.sax.SAXException ;

import org.astrogrid.portal.util.xml.sax.SAXElementHandler ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.data.DataTreeParser  ;
import org.astrogrid.portal.services.myspace.client.data.DataNodeHandler ;

import org.astrogrid.portal.services.myspace.client.status.StatusNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNodeParser  ;
import org.astrogrid.portal.services.myspace.client.status.StatusNodeHandler ;

/**
 * An XML parser to handle a lookup response.
 *
 */
public abstract class LookupResultsParser
	extends SAXElementHandler
	implements DataNodeHandler, StatusNodeHandler
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true ;

	/**
	 * Public constructor.
	 *
	 */
	public LookupResultsParser()
		{
		//
		// Initialise our XML parser.
		super("results") ;
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
		// Add a <status> handler.
		addElementHandler(
			new StatusNodeParser(this)
			) ;
		//
		// Add a <dataItemRecords> handler.
		addElementHandler(
			new DataTreeParser(this)
			) ;
		}

	/**
	 * Handle a DataNode.
	 *
	 */
	public abstract void handleDataNode(DataNode node) ;

	/**
	 * Handle a StatusNode.
	 *
	 */
	public abstract void handleStatusNode(StatusNode status) ;

	}
