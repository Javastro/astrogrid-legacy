/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/data/Attic/DataNodeParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/22 04:03:41 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: DataNodeParser.java,v $
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.data ;

import org.xml.sax.SAXException ;

import org.astrogrid.portal.util.xml.sax.SAXElementHandler  ;
import org.astrogrid.portal.util.xml.sax.SAXDocumentHandler ;
import org.astrogrid.portal.util.xml.sax.SAXAttributeHandler ;
import org.astrogrid.portal.util.xml.sax.SAXCharacterHandler ;

/**
 * A SAX XML parser to handle data items.
 *
 */
public class DataNodeParser
	extends SAXElementHandler
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true ;

	/**
	 * Reference to our DataNodeHandler.
	 * Notified when we have completed a new DataNode.
	 *
	 */
	protected DataNodeHandler handler ;

	/**
	 * Public constructor.
	 * Needs a reference to a DataNodeHandler to pass the completed DataNode to.
	 *
	 */
	public DataNodeParser(DataNodeHandler handler)
		{
		//
		// Initialise our XML parser.
		super("dataItemRecord") ;
		//
		// Initialise our handler
		this.handler = handler ;
		}

	/**
	 * Our new DataNode.
	 * This is NOT thread safe.
	 *
	 */
	protected DataNode node ;

	/**
	 * Handle the start of our XML element.
	 * Creates a new DataNode to populate.
	 *
	 */
	protected void startElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("Start of data node element") ;
		//
		// Create a new TreeNode.
		this.node = new DataNode() ;
		}

	/**
	 * Handle the end of our XML element.
	 * Notifies our DataNodeHandler, passing it the completed DataNode.
	 *
	 */
	protected void closeElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("Close of data node element") ;
		//
		// Notify our handler.
		if (null != handler)
			{
			handler.handleDataNode(node) ;
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
		// Add a <dataItemName> handler.
		addElementHandler(
			new SAXElementHandler("dataItemName")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Path : " + text) ;
								if (null != node)
									{
									//
									// Name in XML is actually the full path.
									node.setPath(text) ;
									}
								}
							}
						);
					}
				}
			) ;

		//
		// Add a <dataItemID> handler.
		addElementHandler(
			new SAXElementHandler("dataItemID")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Ident : " + text) ;
								if (null != node)
									{
									node.setIdent(text) ;
									}
								}
							}
						);
					}
				}
			) ;

		//
		// Add a <size> handler.
		addElementHandler(
			new SAXElementHandler("size")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Size : " + text) ;
								if (null != node)
									{
									//
									// Not implemented ....
									//
									}
								}
							}
						);
					}
				}
			) ;

		//
		// Add a <type> handler.
		addElementHandler(
			new SAXElementHandler("type")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Type : " + text) ;
								if (null != node)
									{
									node.setType(text) ;
									}
								}
							}
						);
					}
				}
			) ;

		//
		// Add a <ownerID> handler.
		addElementHandler(
			new SAXElementHandler("ownerID")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Owner : " + text) ;
								if (null != node)
									{
									//
									// Not implemented ....
									//
									}
								}
							}
						);
					}
				}
			) ;

		//
		// Add a <creationDate> handler.
		addElementHandler(
			new SAXElementHandler("creationDate")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Created : " + text) ;
								if (null != node)
									{
									//
									// Not implemented ....
									//
									}
								}
							}
						);
					}
				}
			) ;

		//
		// Add a <expiryDate> handler.
		addElementHandler(
			new SAXElementHandler("expiryDate")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Expires : " + text) ;
								if (null != node)
									{
									//
									// Not implemented ....
									//
									}
								}
							}
						);
					}
				}
			) ;

		//
		// Add a <permissionsMask> handler.
		addElementHandler(
			new SAXElementHandler("permissionsMask")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("Perm : " + text) ;
								if (null != node)
									{
									//
									// Not implemented ....
									//
									}
								}
							}
						);
					}
				}
			) ;

		//
		// Add a <dataHolderURI> handler.
		addElementHandler(
			new SAXElementHandler("dataHolderURI")
				{
				public void init()
					{
					setCharacterHandler(
						new SAXCharacterHandler()
							{
							public void parseText(String text)
								throws SAXException
								{
								if (DEBUG_FLAG) System.out.println("URI  : " + text) ;
								if (null != node)
									{
									//
									// Not implemented ....
									//
									}
								}
							}
						);
					}
				}
			) ;
		}
	}
