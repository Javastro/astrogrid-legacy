/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/actions/export/Attic/ExportResponseParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/29 02:45:22 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: ExportResponseParser.java,v $
 * Revision 1.1  2003/06/29 02:45:22  dave
 * Fixed display styles in explorer and add VOTable transform
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.actions.export ;

import java.io.Reader ;
import java.io.StringReader ;
import java.io.IOException ;

import org.xml.sax.SAXException ;

import org.astrogrid.portal.util.xml.sax.SAXDocumentHandler ;

import org.astrogrid.portal.services.myspace.client.data.DataNode ;
import org.astrogrid.portal.services.myspace.client.status.StatusNode ;

/**
 * An XML parser to handle a details response.
 *
 */
public class ExportResponseParser
	extends SAXDocumentHandler
	implements ExportResultHandler
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
	public ExportResponseParser()
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
			System.out.println("ExportResponseParser.parse(String)") ;
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
			new ExportResultParser(this)
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
