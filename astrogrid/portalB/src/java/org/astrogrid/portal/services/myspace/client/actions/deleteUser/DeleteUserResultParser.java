
package org.astrogrid.portal.services.myspace.client.actions.deleteUser ;

import org.xml.sax.SAXException ;

import org.astrogrid.portal.util.xml.sax.SAXElementHandler ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.data.DataItemParser  ;

import org.astrogrid.portal.services.myspace.client.status.StatusNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNodeParser  ;

/**
 * An XML parser to handle a extendlease response.
 *
 */
public class DeleteUserResultParser
	extends SAXElementHandler
	implements DeleteUserResultHandler
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
	private DeleteUserResultHandler handler ;

	/**
	 * Public constructor.
	 *
	 */
	public DeleteUserResultParser(DeleteUserResultHandler handler)
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
		if (DEBUG_FLAG) System.out.println("Start of results DeleteUser") ;
		}

	/**
	 * Handle the end of a results element.
	 *
	 */
	protected void closeElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("Close of results DeleteUser") ;
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
