/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/data/Attic/DataItemParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: DataItemParser.java,v $
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.data ;

import org.xml.sax.SAXException ;
import java.util.StringTokenizer ;

import org.astrogrid.portal.util.xml.sax.SAXElementHandler  ;
import org.astrogrid.portal.util.xml.sax.SAXDocumentHandler ;
import org.astrogrid.portal.util.xml.sax.SAXAttributeHandler ;
import org.astrogrid.portal.util.xml.sax.SAXCharacterHandler ;

/**
 * A SAX XML parser to handle a single data item.
 *
 */
public class DataItemParser
	extends SAXElementHandler
	implements DataNodeHandler
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true ;

	/**
	 * Reference to our DataNodeHandler.
	 * Notified when we have completed the data tree.
	 *
	 */
	protected DataNodeHandler handler ;

	/**
	 * Public constructor.
	 * Needs a reference to a DataNodeHandler to pass the completed DataNode to.
	 *
	 */
	public DataItemParser(DataNodeHandler handler)
		{
		//
		// Initialise our XML parser.
		super("dataItemRecords") ;
		//
		// Initialise our handler
		this.handler = handler ;
		}

	/**
	 * Initialise our DataNodeParser.
	 *
	 */
	public void init()
		{
		addElementHandler(
			new DataNodeParser(this)
			) ;
		}

	/**
	 * Handle the start of our XML element.
	 * Creates a new DataNode for our tree.
	 *
	 */
	protected void startElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("Start of data items") ;
		}

	/**
	 * Handle the end of our XML element.
	 * Notifies our DataNodeHandler, passing it a reference to our root DataNode.
	 *
	 */
	protected void closeElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("Close of data items") ;
		if (DEBUG_FLAG) System.out.println("----") ;
		}

	/**
	 * Collect our DataNode.
	 * Called by our DataNodeParser when it creates a DataNode.
	 *
	 */
	public void handleDataNode(DataNode node)
		{
		if (null != handler)
			{
			handler.handleDataNode(node) ;
			}
		}
	}

