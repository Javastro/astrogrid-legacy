/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/Attic/LookupResponseParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/18 23:28:23 $</cvs:author>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 * $Log: LookupResponseParser.java,v $
 * Revision 1.3  2003/06/18 23:28:23  dave
 * Added LookupResponseItem to LookupResponseParser
 *
 * Revision 1.2  2003/06/18 12:05:43  dave
 * Added debug and response status
 *
 * Revision 1.1  2003/06/18 01:33:15  dave
 * Moved message parser into separate class and added service lookup to pages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client ;

import java.io.Reader ;
import java.io.InputStream ;
import java.io.StringReader ;
import java.io.IOException ;

import java.util.Map ;
import java.util.Vector ;
import java.util.Hashtable ;
import java.util.Collection ;
import java.util.StringTokenizer ;


import org.xml.sax.SAXException ;
import org.xml.sax.SAXParseException ;

import org.astrogrid.portal.util.xml.sax.SAXElementHandler  ;
import org.astrogrid.portal.util.xml.sax.SAXDocumentHandler ;
import org.astrogrid.portal.util.xml.sax.SAXAttributeHandler ;
import org.astrogrid.portal.util.xml.sax.SAXCharacterHandler ;

/**
 * An XML parser to handle a lookup response.
 *
 */
public class LookupResponseParser
	extends SAXDocumentHandler
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
	public LookupResponseParser()
		{
		//
		// Initialise our base class.
		super() ;
		}

	/**
	 * Parse a response String.
	 *
	 */
	public void parseResponse(String response)
		throws IOException, SAXException
		{
		if (DEBUG_FLAG)
			{
			System.out.println("LookupResponseParser.parseResponse()") ;
			System.out.println("----") ;
			System.out.println(response) ;
			System.out.println("----") ;
			}

		//
		// Remove the bad header from the response.
		String header = "<?xml version=1.0 encoding=UTF-8?>" ;
		response = response.substring(header.length()) ;
		//
		// Wrap it in a character stream Reader.
		Reader reader = new StringReader(response) ;
		//
		// Parse the response.
		this.parse(reader) ;
		}

	/**
	 * Parse a response stream.
	 *
	 */
	public void parseResponse(InputStream response)
		throws IOException, SAXException
		{
		if (DEBUG_FLAG)
			{
			System.out.println("LookupResponseParser.parseResponse(InputStream)") ;
			}
		//
		// Parse the response stream.
		this.parse(response) ;
		}

	/**
	 * The response status.
	 *
	 */
	protected MySpaceResponseStatus status ;

	/**
	 * Access to the response status.
	 *
	 */
	public MySpaceResponseStatus getStatus()
		{
		return this.status ;
		}

	/**
	 * Our DataItemRecords, not thread safe.
	 *
	 */
	protected LookupResponseItem results ;

	/**
	 * Access to our results.
	 *
	 */
	public LookupResponseItem getResults()
		{
		return results ;
		}

	/**
	 * Initialise our parser.
	 *
	 */
	public void init()
		{
		//
		// Add a<results> element handler.
		addElementHandler(
			new SAXElementHandler("results")
				{
				//
				// Start of results element.
				protected void startElement()
					throws SAXException
					{
					if (DEBUG_FLAG) System.out.println("----") ;
					if (DEBUG_FLAG) System.out.println("Start of results") ;
					//
					// Initialise our results.
					results = new LookupResponseItem("") ;
					status  = new MySpaceResponseStatus() ;
					}
				//
				// Close of results element.
				protected void closeElement()
					throws SAXException
					{
					if (DEBUG_FLAG) System.out.println("Close of results") ;
					if (DEBUG_FLAG) System.out.println("----") ;
					}
				//
				// Initialise results handler.
				public void init()
					{
					//
					// Add a <status> handler.
					addElementHandler(
						new SAXElementHandler("status")
							{
							//
							// Initialise <status> handler.
							public void init()
								{
								//
								// Add a <status> handler.
								addElementHandler(
									new SAXElementHandler("status")
										{
										public void init()
											{
											//
											// Handle text content of <status>
											setCharacterHandler(
												new SAXCharacterHandler()
													{
													public void parseText(String text)
														throws SAXException
														{
														if (DEBUG_FLAG) System.out.println("Status : " + text) ;
														if (null != status)
															{
															status.setStatus(text) ;
															}
														}
													}
												);
											}
										}
									) ;
								//
								// Add a <details> handler.
								addElementHandler(
									new SAXElementHandler("details")
										{
										public void init()
											{
											setCharacterHandler(
												new SAXCharacterHandler()
													{
													public void parseText(String text)
														throws SAXException
														{
														if (DEBUG_FLAG) System.out.println("Details : " + text) ;
														if (null != status)
															{
															status.setDetails(text) ;
															}
														}
													}
												);
											}
										}
									) ;
								//
								// Add a <currentDate> handler.
								addElementHandler(
									new SAXElementHandler("currentDate")
										{
										public void init()
											{
											setCharacterHandler(
												new SAXCharacterHandler()
													{
													public void parseText(String text)
														throws SAXException
														{
														if (DEBUG_FLAG) System.out.println("Date : " + text) ;
														}
													}
												);
											}
										}
									) ;
								}
							}
						) ;
					//
					// Add a <dataItemRecords> handler.
					addElementHandler(
						new SAXElementHandler("dataItemRecords")
							{
							public void init()
								{
								//
								// Add a DataItemRecord handler ....
								addElementHandler(
									new DataItemRecordParser()
										{
										public void dataItemRecord(DataItemRecord item)
											{
											//
											// Add the item to our results.
											addDataItem(item) ;
											}
										}
									) ;
								}
							}
						) ;
					}
				}
			) ;
		}

	/**
	 * Add a record to our tree.
	 *
	 */
	protected void addDataItem(DataItemRecord item)
		{
		if (DEBUG_FLAG) System.out.println("----") ;
		if (DEBUG_FLAG) System.out.println("addDataItem") ;
		//
		// Get the item path.
		String path = item.getName() ;
		if (DEBUG_FLAG) System.out.println("Path : " + path) ;
		//
		// Split the path into tokens.
		StringTokenizer tokens = new StringTokenizer(path, "/") ;
		//
		// Start with the top node.
		LookupResponseItem node = results ;
		//
		// Step down the path.
		while (tokens.hasMoreTokens())
			{
			String step = tokens.nextToken() ;
			if (DEBUG_FLAG) System.out.println("Step : " + step) ;
			//
			// Look for an existing item at this level.
			LookupResponseItem child = node.getChild(step) ;
			//
			// If we didn't find a node.
			if (null == child)
				{
				if (DEBUG_FLAG) System.out.println("No child found for " + step) ;
				//
				// If we have more steps in our path.
				if (tokens.hasMoreTokens())
					{
					if (DEBUG_FLAG) System.out.println("Creating empty child for " + step) ;
					//
					// Create a new (empty) node.
					child = new LookupResponseItem(step) ;
					}
				//
				// If we don't have any more steps.
				else {
					if (DEBUG_FLAG) System.out.println("Creating item child for " + step) ;
					//
					// Create a new node for this item.
					child = new LookupResponseItem(step, item) ;
					}
				//
				// If we crated a new child
				if (null != child)
					{
					if (DEBUG_FLAG) System.out.println("Adding child for " + step) ;
					//
					// Add the new child to our node.
					node.addChild(step, child) ;
					}
				}
			//
			// If we did find a child.
			else {
				if (DEBUG_FLAG) System.out.println("Found child for " + step) ;
				}
			//
			// Step down to the child node.
			if (null != child)
				{
				node = child ;
				}
			}
		if (DEBUG_FLAG) System.out.println("----") ;
		}
	}
