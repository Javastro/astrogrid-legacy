/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/common/util/xml/sax/Attic/SAXDocumentHandler.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SAXDocumentHandler.java,v $
 *   Revision 1.2  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.1  2003/09/24 15:47:38  dave
 *   Added policy database loader tools.
 *
 * </cvs:log>
 *
 * A set of tools to make XML parsing easier.
 * Donated to AstroGrid by dave.morris@codon.demon.co.uk
 *
 */
package org.astrogrid.community.common.util.xml.sax ;

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
     * Parse an XML document.
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
     * Parse an XML document.
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
    }
