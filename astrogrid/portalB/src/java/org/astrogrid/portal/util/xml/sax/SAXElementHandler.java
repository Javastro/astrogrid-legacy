/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/util/xml/sax/Attic/SAXElementHandler.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/17 15:17:34 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: SAXElementHandler.java,v $
 * Revision 1.1  2003/06/17 15:17:34  dave
 * Added links to live MySpace, including initial XML parser for lookup results
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.util.xml.sax ;

import org.xml.sax.XMLReader ;
import org.xml.sax.SAXException ;
import org.xml.sax.SAXParseException ;

import org.xml.sax.Locator ;
import org.xml.sax.Attributes ;
import org.xml.sax.ContentHandler ;
import org.xml.sax.helpers.DefaultHandler ;

import java.util.Map ;
import java.util.HashMap ;

/**
 * A class to handle SAX element events.
 * 
 */
public class SAXElementHandler extends DefaultHandler
	{
	/**
	 * Our debug flag.
	 *
	 */
	private static final boolean DEBUG_FLAG = false ;

	/**
	 * Handle the start of our element.
	 *
	 */
	protected void startElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("SAXElementHandler::startElement") ;
		}

	/**
	 * Handle the end of our element.
	 *
	 */
	protected void closeElement()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("SAXElementHandler::closeElement") ;
		}

	/**
	 * Handle the start of our attributes.
	 *
	 */
	protected void startAttributes()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("SAXElementHandler::startAttributes") ;
		}

	/**
	 * Handle the end of our attributes.
	 *
	 */
	protected void doneAttributes()
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("SAXElementHandler::doneAttributes") ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public SAXElementHandler()
		{
		//
		// Use next constructor.
		this(null) ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public SAXElementHandler(String name)
		{
		this.name = name ;
		//
		// Initialise our parsers.
		this.init() ;
		}

	/**
	 * Initialise our parsers.
	 * Default implementation does nothing ....
	 * This is a hook for anonymous derived classes to initialise parsers on construction.
	 *
	 */
	protected void init()
		{
		//
		// Do nothing ....
		// Hook for anonymous derived classes to initialise parsers on construction.
		//
		}

	/**
	 * Our element name.
	 *
	 */
	private String name ;

	/**
	 * Access to our name.
	 *
	 */
	public String getName()
		{
		return this.name ;
		}

	/**
	 * Our list of SAXElementHandlers.
	 *
	 */
	private Map elements = new HashMap() ;

	/**
	 * Add an SAXElementHandler.
	 *
	 */
	public void addElementHandler(SAXElementHandler handler)
		{
		//
		// Add the handler to our list.
		this.elements.put(handler.getName(), handler) ;
		}

	/**
	 * Find a matching SAXElementHandler.
	 *
	 */
	protected SAXElementHandler getElementHandler(String name)
		{
		//
		// Find a matching handler in our list.
		return (SAXElementHandler) this.elements.get(name) ;
		}

	/**
	 * Our list of AttributeHandlers.
	 *
	 */
	private Map attributes = new HashMap() ;

	/**
	 * Add an SAXElementHandler.
	 *
	 */
	public void addAttributeHandler(SAXAttributeHandler handler)
		{
		//
		// Add the handler to our list.
		this.attributes.put(handler.getName(), handler) ;
		}

	/**
	 * Find a matching SAXAttributeHandlers.
	 *
	 */
	protected SAXAttributeHandler getAttributeHandler(String name)
		{
		//
		// Find a matching handler in our list.
		return (SAXAttributeHandler) this.attributes.get(name) ;
		}

	/**
	 * Our current XMLReader.
	 *
	 */
	private XMLReader reader ;

	/**
	 * The document Locator.
	 *
	 */
	private Locator locator ;

	/**
	 * The previous ContentHandler.
	 *
	 */
	private ContentHandler parent ;

	/**
	 * Register as a ContentHandler.
	 *
	 */
	protected void setReader(XMLReader reader)
		{
		//
		// Set our current reader.
		this.reader = reader ;
		//
		// Reset our references.
		this.locator = null ;
		this.parent  = null ;
		//
		// If we have a new reader.
		if (null != reader)
			{
			//
			// Grab the previous ContentHandler.
			this.parent = this.reader.getContentHandler() ;
			//
			// Register a new ContentHandler.
			this.reader.setContentHandler(this) ;
			}
		}

	/**
	 * Restore the previous ContentHandler.
	 *
	 */
	protected void unsetReader()
		{
		//
		// Restore the previous ContentHandler.
		if (null != this.reader)
			{
			this.reader.setContentHandler(parent) ;
			}
		//
		// Reset our references.
		this.reader  = null ; 
		this.locator = null ;
		this.parent  = null ;
		}

	/**
	 * Handle the start of our element.
	 *
	 */
	public void parseElement(
			XMLReader reader,
			String uri,
			String local,
			String name,
			Attributes attrib
			)
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("SAXElementHandler::parseElement") ;
		if (DEBUG_FLAG) System.out.println("    Handler : " + this.name) ;
		if (DEBUG_FLAG) System.out.println("    Element : " + name) ;
		//
		// Set our current reader.
		setReader(reader) ;
		//
		// If we have a character handler.
		if (null != this.charHandler)
			{
			//
			// Notify our character handler.
			this.charHandler.startElement() ;
			}
		//
		// Start our element.
		startElement() ;
		//
		// Pass on the attributes.
		parseAttributes(attrib) ;
		}

	/**
	 * Handle the start of a new element.
	 *
	 */
	public void startElement(
			String uri,
			String local,
			String name,
			Attributes attrib
			)
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("SAXElementHandler::startElement") ;
		if (DEBUG_FLAG) System.out.println("    Handler : " + this.name) ;
		if (DEBUG_FLAG) System.out.println("    Element : " + name) ;
		//
		// Find a matching handler in our list.
		SAXElementHandler handler = getElementHandler(name) ;
		//
		// If we found a matching handler.
		if (null != handler)
			{
			//
			// Pass the event to the handler.
			handler.parseElement(reader, uri, local, name, attrib) ;
			}
		//
		// If we didn't find a matching handler.
		else {
			//
			// Throw an Exceprion.
			throw new SAXParseException("Unknown element \"" + name + "\"", locator) ;
			}
		}

	/**
	 * Handle the end of our element.
	 *
	 */
	public void endElement(
			String uri,
			String local,
			String name
			)
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("SAXElementHandler::endElement") ;
		if (DEBUG_FLAG) System.out.println("    Handler : " + this.name) ;
		if (DEBUG_FLAG) System.out.println("    Element : " + name) ;
		//
		// If we have a character handler.
		if (null != this.charHandler)
			{
			//
			// Notify our character handler.
			this.charHandler.endElement() ;
			}
		//
		// Replace the original ContentHandler.
		unsetReader() ;
		//
		// Close our element.
		closeElement() ;
		}

	/**
	 * Process our attributes.
	 *
	 */
	protected void parseAttributes(Attributes attrib)
		throws SAXException
		{
		//
		// Start our attributes.
		startAttributes() ;
		//
		// Process each of our attributes.
		for (int i = 0 ; i < attrib.getLength() ; i++)
			{
			String name  = attrib.getQName(i) ;
			String value = attrib.getValue(i) ;
			//
			// Process the attribute.
			parseAttribute(name, value) ;
			}
		//
		// Done our attributes.
		doneAttributes() ;
		}

	/**
	 * Parse an attribute.
	 *
	 */
	protected void parseAttribute(String name, String value)
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("SAXElementHandler::parseAttribute") ;
		if (DEBUG_FLAG) System.out.println("    Name  : \"" + name + "\"")  ;
		if (DEBUG_FLAG) System.out.println("    Value : \"" + value + "\"") ;
		//
		// Try and find an attribute handler.
		SAXAttributeHandler handler = getAttributeHandler(name) ;
		//
		// If we found a handler.
		if (null != handler)
			{
			//
			// Pass on the attribute.
			handler.parseAttribute(value) ;
			}
		//
		// If we didn't find a handler.
		else {
			//
			// Throw an Exception.
			throw new SAXParseException("Unknown attribute \"" + name + "\"", locator) ;
			}
		}

	/**
	 * Set our document event Locator.
	 *
	 */
	public void setDocumentLocator(Locator locator)
		{
		this.locator = locator ;
		}

	/**
	 * Our character data handler.
	 *
	 */
	private SAXCharacterHandler charHandler ;

	/**
	 * Set our character data handler.
	 *
	 */
	protected void setCharacterHandler(SAXCharacterHandler handler)
		{
		this.charHandler = handler ;
		}

	/**
	 * Get our character data handler.
	 *
	 */
	protected SAXCharacterHandler getCharacterHandler()
		{
		return this.charHandler ;
		}

	/**
	 * Handle some character data for this element.
	 *
	 */
	public void characters(char[] data, int start, int length)
		throws SAXException
		{
		if (DEBUG_FLAG) System.out.println("SAXElementHandler::characters") ;
		//
		// If we have a character data handler.
		if (null != this.charHandler)
			{
			this.charHandler.characters(data, start, length) ;
			}
		}
	}
