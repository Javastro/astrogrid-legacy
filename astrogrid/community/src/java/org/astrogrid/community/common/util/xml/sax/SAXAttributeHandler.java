/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/common/util/xml/sax/Attic/SAXAttributeHandler.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: SAXAttributeHandler.java,v $
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

import org.xml.sax.SAXException ;

/**
 * A class to handle SAX element attributes.
 * 
 */
public class SAXAttributeHandler
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
    public SAXAttributeHandler()
        {
        //
        // Use next constructor.
        this(null) ;
        }

    /**
     * Public constructor.
     *
     */
    public SAXAttributeHandler(String name)
        {
        this.name = name ;
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
        return name ;
        }

    /**
     * Parse an attribute.
     *
     */
    public void parseAttribute(String value)
        throws SAXException
        {
        if (DEBUG_FLAG) System.out.println("SAXAttributeHandler::parseAttribute") ;
        if (DEBUG_FLAG) System.out.println("    Name  : \"" + name + "\"")  ;
        if (DEBUG_FLAG) System.out.println("    Value : \"" + value + "\"") ;
        }
    }