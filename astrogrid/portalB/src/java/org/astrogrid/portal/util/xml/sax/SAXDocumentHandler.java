/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/util/xml/sax/Attic/SAXDocumentHandler.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/17 15:17:34 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: SAXDocumentHandler.java,v $
 * Revision 1.1  2003/06/17 15:17:34  dave
 * Added links to live MySpace, including initial XML parser for lookup results
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.util.xml.sax ;

import javax.xml.parsers.SAXParser ;
import javax.xml.parsers.SAXParserFactory ;
import javax.xml.parsers.FactoryConfigurationError ;
import javax.xml.parsers.ParserConfigurationException ;

import org.xml.sax.XMLReader ;
import org.xml.sax.helpers.XMLReaderFactory ;
import org.xml.sax.helpers.DefaultHandler ;

import org.xml.sax.SAXException ;
import org.xml.sax.SAXParseException ;

import org.xml.sax.Locator ;
import org.xml.sax.Attributes ;
import org.xml.sax.InputSource ;
import org.xml.sax.ContentHandler ;

import java.util.Map ;
import java.util.HashMap ;

import java.net.URL ;
import java.io.File ;
import java.io.Reader ;
import java.io.FileReader ;
import java.io.InputStream ;
import java.io.IOException ;

/**
 * A class to handle SAX element events.
 * 
 */
public class SAXDocumentHandler extends SAXElementHandler
	{
	/**
	 * Our debug flag.
	 *
	 */
	private static final boolean DEBUG_FLAG = false ;

	/**
	 * Public constructor.
	 *
	 */
	public SAXDocumentHandler()
		{
		//
		// Use base class constructor.
		super(null) ;
		}

	/**
	 * Parse an XML file.
	 *
	 */
	public void parse(File file)
		throws SAXException, IOException
		{
		if (DEBUG_FLAG) System.out.println("SAXDocumentHandler::parse") ;
		if (DEBUG_FLAG) System.out.println("  File : " + file) ;
		try {
			//
			// Create our XML parser factory.
			SAXParserFactory factory = SAXParserFactory.newInstance() ;
			//
			// Create our XML parser.
			SAXParser parser = factory.newSAXParser() ;
			//
			// Get the XMLReader.
			XMLReader reader = parser.getXMLReader() ;
			//
			// Set our XMLReader
			setReader(reader) ;
			//
			// Start to parse our document.
			parser.parse(file, this) ;
			}
		catch(FactoryConfigurationError ouch)
			{
			throw new SAXException("XML parser configuration error") ;
			}
		catch(ParserConfigurationException ouch)
			{
			throw new SAXException("XML parser configuration error", ouch) ;
			}
		}

	/**
	 * Parse an XML InputStream.
	 *
	 */
	public void parse(InputStream stream)
		throws SAXException, IOException
		{
		if (DEBUG_FLAG) System.out.println("SAXDocumentHandler::parse") ;
		if (DEBUG_FLAG) System.out.println("  InputStream ....") ;
		try {
			//
			// Wrap our InputStream as an InputSource.
			InputSource source = new InputSource(stream) ;
			//
			// Create our XML parser factory.
			SAXParserFactory factory = SAXParserFactory.newInstance() ;
			//
			// Create our XML parser.
			SAXParser parser = factory.newSAXParser() ;
			//
			// Get the XMLReader.
			XMLReader reader = parser.getXMLReader() ;
			//
			// Set our XMLReader
			setReader(reader) ;
			//
			// Start to parse our document.
			parser.parse(source, this) ;
			}
		catch(FactoryConfigurationError ouch)
			{
			throw new SAXException("XML parser configuration error") ;
			}
		catch(ParserConfigurationException ouch)
			{
			throw new SAXException("XML parser configuration error", ouch) ;
			}
		}

	/**
	 * Parse an XML input Reader.
	 *
	 */
	public void parse(Reader input)
		throws SAXException, IOException
		{
		if (DEBUG_FLAG) System.out.println("SAXDocumentHandler::parse") ;
		if (DEBUG_FLAG) System.out.println("  Input Reader ....") ;
		try {
			//
			// Wrap our Reader as an InputSource.
			InputSource source = new InputSource(input) ;
			//
			// Create our XML parser factory.
			SAXParserFactory factory = SAXParserFactory.newInstance() ;
			//
			// Create our XML parser.
			SAXParser parser = factory.newSAXParser() ;
			//
			// Get the XMLReader.
			XMLReader reader = parser.getXMLReader() ;
			//
			// Set our XMLReader
			setReader(reader) ;
			//
			// Start to parse our document.
			parser.parse(source, this) ;
			}
		catch(FactoryConfigurationError ouch)
			{
			throw new SAXException("XML parser configuration error") ;
			}
		catch(ParserConfigurationException ouch)
			{
			throw new SAXException("XML parser configuration error", ouch) ;
			}
		}


	}
