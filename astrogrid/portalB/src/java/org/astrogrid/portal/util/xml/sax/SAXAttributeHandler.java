/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/util/xml/sax/Attic/SAXAttributeHandler.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/17 15:17:34 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: SAXAttributeHandler.java,v $
 * Revision 1.1  2003/06/17 15:17:34  dave
 * Added links to live MySpace, including initial XML parser for lookup results
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.util.xml.sax ;

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