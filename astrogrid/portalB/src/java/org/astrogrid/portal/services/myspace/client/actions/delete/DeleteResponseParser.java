/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/delete/Attic/DeleteResponseParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/24 10:43:25 $</cvs:author>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 * $Log: DeleteResponseParser.java,v $
 * Revision 1.2  2003/06/24 10:43:25  dave
 * Fixed bugs in DataTreeWalker and tree page
 *
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.delete ;

import java.io.Reader ;
import java.io.StringReader ;
import java.io.IOException ;

import org.xml.sax.SAXException ;

import org.astrogrid.portal.util.xml.sax.SAXDocumentHandler ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNode ;

/**
 * An XML parser to handle a delete response.
 *
 */
public class DeleteResponseParser
	extends SAXDocumentHandler
	implements DeleteResultHandler
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
	public DeleteResponseParser()
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
			System.out.println("DeleteResponseParser.parse(String)") ;
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
	 * Our tree of data nodes.
	 *
	 */
	protected DataNode data ;

	/**
	 * Access our tree of data nodes.
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
		addElementHandler(
			new DeleteResultParser(this)
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
