//-------------------------------------------------------------------------
// FILE: XmlValidatorIfc.java
// PACKAGE: org.astrogrid.xmlutils
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 14/10/02   KEA       Initial prototype
// 02/12/02   KEA       Update for supporting setting of features
//-------------------------------------------------------------------------

package org.astrogrid.xmlutils;

import java.io.Reader;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXException;

import java.util.Map;

/**
 * <p>Interface class for syntactic and semantic validation of XML
 * input documents.
 *
 * <p>Intended to provide a generic interface for which particular
 * implementations (Xerces-based, custom etc) can be supplied
 * as required.
 *
 * <p>See Validate.java for a command-line harness.
 * 
 * <p><strong>Usage example with namespace map:</strong>
 * <pre>
 *       // Required includes
 *       //
 *       import java.util.Map;
 *       import java.util.TreeMap;
 *			import org.astrogrid.xmlutils.XmlValidatorIfc;
 *			import org.astrogrid.xmlutils.XmlValidatorXercesImpl;
 * 
 *       // Usage snippet
 *       //
 *       XmlValidatorIfc validator = new XmlValidatorXercesImpl();
 *       validator.setFeature("UseNamespaces",true);
 *
 *       Map namespaceMap = new TreeMap();
 *       namespaceMap.put(
 *             "http://www.astrogrid.org/namespace/AceInput-1_0",
 *             "http://astrogrid.ast.cam.ac.uk/namespace/AceInput-1_0.xsd"
 *       );
 *       namespaceMap.put(
 *             "http://www.astrogrid.org/namespace/AceInput",
 *             "http://astrogrid.ast.cam.ac.uk/namespace/AceInput.xsd"
 *       );
 *       namespaceMap.put(
 *             "http://www.astrogrid.org/namespace/SExtractor_2_2_2",
 *             "http://astrogrid.ast.cam.ac.uk/namespace/SExtractor_2_2_2.xsd"
 *       );
 *
 *       validator.validate("blah.xml",namespaceMap);
 * </pre>  
 *
 * @see org.astrogrid.xmlutils.XmlValidatorXercesImpl
 * @see org.astrogrid.xmlutils.Validate
 *
 * @author Kona Andrews,
 * <a href="mailto:kea@ast.cam.ac.uk">kea@ast.cam.ac.uk</a>
 * @version 1.0
 *
 *
 * (c) Copyright Astrogrid 2002; all rights reserved.
 * See http://www.astrogrid.org/code_licence.html for terms of usage.
 */
public interface XmlValidatorIfc 
{
	/**
	 * Syntactically and semantically validates an input XML file
	 * against its schema(s), the location of which must be specified 
    * in the <tt>xsi:schemaLocation</tt> attribute of the toplevel 
    * document element.
    *
	 * @param reader  A (pre-initialised) reader for the input XML.
	 *
	 * @throws org.xml.sax.SAXException if input document is invalid.
	 */
	public void validate(Reader reader) throws SAXException;


	/**
	 * Syntactically and semantically validates an input XML file
	 * against its schema(s), the location of which must be specified 
	 * in the supplied Map matching namespaces to schema locations.
	 *
	 * @param reader  A (pre-initialised) reader for the input XML.
	 * @param namespaceMap  A Map type containing string:string pairs,
	 * the key being a namespace URI, the value being the location of
	 * the schema for that namespace.
	 *
	 * @throws org.xml.sax.SAXException if input document is invalid.
	 */
	public void validate(Reader reader, Map namespaceMap) throws SAXException;


	/**
	 * Syntactically and semantically validates an input XML file
	 * against its schema(s), the location of which must be specified 
    * in the <tt>xsi:schemaLocation</tt> attribute of the toplevel 
    * document element.
	 *
	 * @param systemResource  The filename of the input XML.
	 *
	 * @throws org.xml.sax.SAXException if input document is invalid.
	 */
	public void validate(String fileName) throws SAXException;

	/**
	 * Syntactically and semantically validates an input XML file
	 * against its schema, the location of which must be specified 
	 * in the supplied Map matching namespaces to schema locations.
	 *
	 * @param systemResource  The specified system resource from which to
	 * read the input XML (e.g. a local filename).
	 * 
	 * @param namespaceMap  A Map type containing string:string pairs,
	 * the key being a namespace URI, the value being the location of
	 * the schema for that namespace.
	 *
	 * @throws org.xml.sax.SAXException if input document is invalid.
	 */
	public void validate(String fileName, Map namespaceMap) throws SAXException;


	/**
	 * Allows named features to be switched on or off in the underlying 
	 * implementation class (supported feature-set may vary from 
	 * implementation to implementation).
	 *
	 * @param feature  A string identifying a feature by name.
	 * @param value  The boolean value to which the named feature 
	 * should be set.
	 * 
	 * @throws  org.xml.sax.SAXNotRecognizedException if the named 
	 * feature is not recognised.
	 */
	public void setFeature(String feature, boolean value) 
			throws org.xml.sax.SAXNotRecognizedException;


	/**
	 * Allows named features to be interrogated for their current value in
	 * in the underlying implementation class (supported feature-set may 
	 * vary from implementation to implementation).
	 *
	 * @param feature  A string identifying a feature by name.
	 *
	 * @return The current boolean value of the named feature.
	 * 
	 * @throws  org.xml.sax.SAXNotRecognizedException if the named 
	 * feature is not recognised.
	 */
	public boolean getFeature(String feature)
			throws org.xml.sax.SAXNotRecognizedException;
}
//-------------------------------------------------------------------------
