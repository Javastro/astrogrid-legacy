/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/common/util/xml/sax/Attic/SAXCharacterHandler.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SAXCharacterHandler.java,v $
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

import org.xml.sax.XMLReader ;
import org.xml.sax.SAXException ;
import org.xml.sax.SAXParseException ;

import org.xml.sax.Locator ;
import org.xml.sax.Attributes ;
import org.xml.sax.ContentHandler ;
import org.xml.sax.helpers.DefaultHandler ;

/**
 * A class to handle SAX character data.
 *
 */
public class SAXCharacterHandler
    {
    /**
     * Our debug flag.
     *
     */
    private static final boolean DEBUG_FLAG = false ;

    /**
     * Our internal buffer to hold the character data.
     *
     */
    private StringBuffer buffer ;

    /**
     * Access to our current buffer.
     *
     */
    protected StringBuffer getBuffer()
        {
        return this.buffer ;
        }

    /**
     * Access to our buffer contents so far.
     *
     */
    public String getString()
        {
        return this.buffer.toString() ;
        }

    /**
     * Process our element text.
     * This will be called with the accumulated buffer contents at the end of our element.
     * Default implementation does nothing.
     *
     */
    public void parseText(String text)
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("SAXCharacterHandler::processText") ;
        if (DEBUG_FLAG) System.out.println(" ---- Text ----") ;
        if (DEBUG_FLAG) System.out.println(text) ;
        if (DEBUG_FLAG) System.out.println(" ---- Text ----") ;
        }

    /**
     * Notify method, should be called at the start of our element.
     *
     */
    protected void startElement()
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("SAXCharacterHandler::startElement") ;
        //
        // Create a new buffer.
        this.buffer = new StringBuffer() ;
        }

    /**
     * Notify method, should be called at the end of our element.
     *
     */
    protected void endElement()
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("SAXCharacterHandler::endElement") ;
        //
        // Convert our buffer contents into a string and pass it on.
        parseText(this.buffer.toString()) ;
        }

    /**
     * Handle some character data for this element.
     *
     */
    public void characters(char[] data, int start, int length)
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("SAXCharacterHandler::characters") ;
        //
        // Add the characters to our buffer.
        this.buffer.append(data, start, length) ;
        }
    }
