/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/data/Attic/DataTreeParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: DataTreeParser.java,v $
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
 * A SAX XML parser to handle a set of data items.
 *
 */
public class DataTreeParser
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
	public DataTreeParser(DataNodeHandler handler)
		{
		//
		// Initialise our XML parser.
		super("dataItemRecords") ;
		//
		// Initialise our handler
		this.handler = handler ;
		}

	/**
	 * Our tree of nodes.
	 * This is NOT thread safe.
	 *
	 */
	protected DataNode root ;

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
		if (DEBUG_FLAG) System.out.println("Start of data tree") ;
		//
		// Create a new TreeNode.
		this.root = new DataNode("") ;
		}

	/**
	 * Handle the end of our XML element.
	 * Notifies our DataNodeHandler, passing it a reference to our root DataNode.
	 *
	 */
	protected void closeElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("Close of data tree") ;
		//
		// Notify our handler, passing it our new tree.
		if (null != handler)
			{
			handler.handleDataNode(root) ;
			}
		if (DEBUG_FLAG) System.out.println("----") ;
		}

	/**
	 * Add a DataNode to our tree.
	 * Walks down our tree looking for the right place to add the leaf.
	 * Called by our DataNodeParser for each DataNode it creates.
	 *
	 */
	public void handleDataNode(DataNode leaf)
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("handleDataNode") ;
		if (DEBUG_FLAG) System.out.println("Path : " + leaf.getPath()) ;
		//
		// Split the leaf path into steps.
		StringTokenizer tokens = new StringTokenizer(leaf.getPath(), "/") ;
		//
		// Start with our tree root.
		DataNode tree = this.root ;
		//
		// Start with a blank path.
		String path = "" ;
		//
		// Step down the leaf path.
		while (tokens.hasMoreTokens())
			{
			//
			// Get the next step in the path.
			String step = tokens.nextToken() ;
			if (DEBUG_FLAG) System.out.println("Step : " + step) ;
			//
			// Add the step to our path.
			path += "/" + step ;
			//
			// Look for an existing child for this step.
			DataNode child = tree.getChild(step) ;
			//
			// If we didn't find a chils.
			if (null == child)
				{
				if (DEBUG_FLAG) System.out.println("No child for " + step) ;
				//
				// If we have more steps in our path.
				if (tokens.hasMoreTokens())
					{
					if (DEBUG_FLAG) System.out.println("Creating empty child for " + step) ;
					//
					// Create a new (empty) node.
					child = new DataNode(path, step) ;
					//
					// Add the child to our tree.
					tree.addChild(child) ;
					//
					// Step down to the child node.
					tree = child ;
					}
				//
				// If we don't have any more steps.
				else {
					if (DEBUG_FLAG) System.out.println("Adding tree node at " + step) ;
					//
					// Set the leaf name and path.
					leaf.setName(step) ;
					leaf.setPath(path) ;
					//
					// Add the leaf to our tree
					tree.addChild(leaf) ;
					}
				}
			//
			// If we did find a child.
			else {
				if (DEBUG_FLAG) System.out.println("Found child for " + step) ;
				//
				// If we have more steps in our path.
				if (tokens.hasMoreTokens())
					{
					//
					// Step down to the child node.
					tree = child ;
					}
				//
				// If we don't have any more steps.
				else {
					if (DEBUG_FLAG) System.out.println("ERROR : Conflicting node found at " + step) ;
					// FIXME - Need to work out what to do here.
					// FIXME - Means that the XML data is wrong. 
					}
				}
			}
		if (DEBUG_FLAG) System.out.println("----") ;
		}
	}

