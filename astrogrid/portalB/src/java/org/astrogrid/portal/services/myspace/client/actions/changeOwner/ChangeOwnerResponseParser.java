
package org.astrogrid.portal.services.myspace.client.actions.changeOwner ;

import java.io.Reader ;
import java.io.StringReader ;
import java.io.IOException ;

import org.xml.sax.SAXException ;

import org.astrogrid.portal.util.xml.sax.SAXDocumentHandler ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNode ;

/**
 * An XML parser to handle a extendlease response.
 *
 */
public class ChangeOwnerResponseParser
	extends SAXDocumentHandler
	implements ChangeOwnerResultHandler
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
	public ChangeOwnerResponseParser()
		{
		//
		// Initialise our base class.
		super() ;
		}

	/**
	 * Parse a response String.
	 *
	 */
	public void parse(String response)
		throws IOException, SAXException
		{
		if (DEBUG_FLAG)
			{
			System.out.println("ChangeOwnerResponseParser.parse(String)") ;
			System.out.println("----") ;
			System.out.println(response) ;
			System.out.println("----") ;
			}
		//
		// If the response starts with a bad header.
		String header = "<?xml version=1.0 encoding=UTF-8?>" ;
		if (response.startsWith(header))
			{
			//
			// Remove the header from the response.
			response = response.substring(header.length()) ;
			}
		//
		// Wrap it in a String Reader.
		Reader reader = new StringReader(response) ;
		//
		// Parse the response.
		this.parse(reader) ;
		}

	/**
	 * Our response status.
	 *
	 */
	protected StatusNode status ;

	/**
	 * Access to our response status.
	 *
	 */
	public StatusNode getStatus()
		{
		return this.status ;
		}

	/**
	 * Our data node.
	 *
	 */
	protected DataNode data ;

	/**
	 * Access our data node.
	 *
	 */
	public DataNode getData()
		{
		return data ;
		}

	/**
	 * Initialise our parsers.
	 *
	 */
	public void init()
		{
		//
		// Add a ChangeOwnerResultParser.
		addElementHandler(
			new ChangeOwnerResultParser(this)
			) ;
		}

	/**
	 * Handle a StatusNode from our StatusNodeParser.
	 *
	 */
	public void handleStatusNode(StatusNode status)
		{
		this.status = status ;
		}

	/**
	 * Handle a DataNode from our DataNode parser.
	 *
	 */
	public void handleDataNode(DataNode data)
		{
		this.data = data ;
		}
	}
