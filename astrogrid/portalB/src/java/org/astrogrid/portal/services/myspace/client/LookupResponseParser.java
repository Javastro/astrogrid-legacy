/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/Attic/LookupResponseParser.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/18 01:33:15 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: LookupResponseParser.java,v $
 * Revision 1.1  2003/06/18 01:33:15  dave
 * Moved message parser into separate class and added service lookup to pages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client ;

import java.io.Reader ;
import java.io.StringReader ;
import java.io.IOException ;

import java.util.Vector ;
import java.util.Collection ;

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
		//
		// Remove the bad header from the response.
		String header = "<?xml version=1.0 encoding=UTF-8?>" ;
		response = response.substring(header.length()) ;
		//
		// Convert it into an InputStream.
		Reader reader = new StringReader(response) ;
		//
		// Parse the response.
		this.parse(reader) ;
		}

	/**
	 * Our DataItemRecords, not thread safe.
	 *
	 */
	protected Collection results ;

	/**
	 * Access to our results.
	 *
	 */
	public Collection getResults()
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
					results = new Vector() ;
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
														if (DEBUG_FLAG) System.out.println("Details ....") ;
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
											results.add(item) ;
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


/*
 *
		//
		// Catch any IO exceptions.
		catch (IOException ouch)
			{
//
// FIXME ....
//
			if (DEBUG_FLAG) System.out.println("IOException while parsing lookupDataHoldersDetails") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
		//
		// Catch any SAX exceptions.
		catch (SAXException ouch)
			{
//
// FIXME ....
//
			if (DEBUG_FLAG) System.out.println("SAXException while parsing lookupDataHoldersDetails") ;
			if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
			}
 *
 */
	}
