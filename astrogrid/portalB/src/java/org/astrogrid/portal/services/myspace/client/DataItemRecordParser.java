/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/Attic/DataItemRecordParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/18 01:33:15 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: DataItemRecordParser.java,v $
 * Revision 1.1  2003/06/18 01:33:15  dave
 * Moved message parser into separate class and added service lookup to pages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client ;

import org.xml.sax.SAXException ;

import org.astrogrid.portal.util.xml.sax.SAXElementHandler  ;
import org.astrogrid.portal.util.xml.sax.SAXDocumentHandler ;
import org.astrogrid.portal.util.xml.sax.SAXAttributeHandler ;
import org.astrogrid.portal.util.xml.sax.SAXCharacterHandler ;

/**
 * An XML parser to handle DataItemRecord elements.
 *
 */
public abstract class DataItemRecordParser
	extends SAXElementHandler
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
	public DataItemRecordParser()
		{
		//
		// Initialise our base class.
		super("dataItemRecord") ;
		}

	/**
	 * Our current data item, not thread safe.
	 *
	 */
	protected DataItemRecord item ;

	/**
	 * Callback method, called when a new DataItemRecord is completed.
	 *
	 */
	public abstract void dataItemRecord(DataItemRecord item) ;

	/**
	 * Handler the start of a DataItem record.
	 *
	 */
	protected void startElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("Start of DataItem record") ;
		//
		// Create a new DataItemRecord.
		this.item = new DataItemRecord() ;
		}

	/**
	 * Handler the end of a DataItem record.
	 *
	 */
	protected void closeElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("Close of DataItem record") ;
		if (DEBUG_FLAG) System.out.println("----") ;
		//
		// Call our callback.
		dataItemRecord(this.item) ;
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
								if (DEBUG_FLAG) System.out.println("Name : " + text) ;
								if (null != item)
									{
									item.setName(text) ;
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
								if (null != item)
									{
									item.setIdent(text) ;
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
								if (null != item)
									{
									item.setOwner(text) ;
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
								if (null != item)
									{
									item.setCreated(text) ;
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
								if (null != item)
									{
									item.setExpires(text) ;
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
								if (null != item)
									{
									item.setSize(text) ;
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
								if (null != item)
									{
									item.setType(text) ;
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
								if (null != item)
									{
									item.setPermissions(text) ;
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
								if (null != item)
									{
									item.setURI(text) ;
									}
								}
							}
						);
					}
				}
			) ;
		}
	}
