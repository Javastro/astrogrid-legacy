//-------------------------------------------------------------------------
// FILE: XmlToNativeXsltImpl.java 
// PACKAGE: org.astrogrid.ace.utils
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 18/10/02   KEA       Initial prototype
// 04/12/02   KEA       Revised to deal with new Ace Schema (v.1_0).
//                      NB this is an interim release.
//-------------------------------------------------------------------------

package org.astrogrid.ace.utils;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

import java.util.Vector;

/**
 * <p>XSLT-based implementation of file format converter for 
 * conversions from Ace XML format to SExtractor native parameter 
 * files.
 *
 * <p>This implementation produces slightly less pretty SExtractor
 * configuration files (*.sex) than the DOM version (XmlToNativeDomImpl), 
 * because I haven't yet figured out how to do flexible padding to 
 * make columns line up in XSLT.
 *
 * <p>This version uses a TransformerFactory, which creates a 
 * transformer specified by the javax.xml.transform.TransformerFactory
 * system property setting.
 *
 *
 * <p> In this interim release, output catalog vector parameters 
 * are <strong>not</strong> supported, i.e.:
 *
 * <ul>
 * <li> MAG_APER(n) </li>
 * <li> MAGERR_APER(n) </li>
 * <li> FLUX_APER(n) </li>
 * <li> FLUXERR_APER(n) </li>
 * <li> VECTOR_SOMFIT(n) </li>
 * <li> VECTOR_ASSOC(n) </li>
 * <li> FLUX_GROWTH(n) </li>
 * <li> VIGNET(m,n) </li>
 * <li> VIGNET_SHIFT(m,n) </li>
 * </ul> 
 *       
 * are not supported.
 *
 *
 * <p>See Translator.java for a usage example, and Tester.java 
 * for a run-time test harness.
 * 
 * <p>TO DO:  <ul>
 * <li>Nicer exception-throwing and handling?</li>
 * <li>Smarter whitespace padding in the XSLT templates.</li>
 * </ul>
 *
 *
 * @see org.astrogrid.ace.utils.XmlToNativeDomImpl
 * @see org.astrogrid.ace.utils.XmlToNativeIfc
 * @see org.astrogrid.ace.utils.Translator 
 * @see org.astrogrid.ace.utils.Tester 
 * 
 * @author Kona Andrews,
 * <a href="mailto:kea@ast.cam.ac.uk">kea@ast.cam.ac.uk</a>
 * @version 1.0
 *
 *
 * (c) Copyright Astrogrid 2002; all rights reserved. 
 * See http://www.astrogrid.org/code_licence.html for terms of usage.
 */
public class XmlToNativeXsltImpl implements XmlToNativeIfc
{

	protected static final String CONFIG_STYLESHEET = 
		"http://astrogrid.ast.cam.ac.uk/namespace/AceConfig-1_0.xsl";

	protected static final String CATALOG_STYLESHEET = 
		"http://astrogrid.ast.cam.ac.uk/namespace/AceCatalog-1_0.xsl";

	/**
	 * Dummy constructor - does nothing.
	 */
	 public XmlToNativeXsltImpl()
	 {
	 }

	/**
	 * Converts Ace XML input to SExtractor output (configuration
	 * file *.sex and catalog parameter file *.param).
	 * 
	 * @param xmlIn  A (pre-initialised) reader for the input XML
	 *
	 * @param configOutWriter  A (pre-initialised) writer for the output 
	 *     SExtractor *.sex * file.
	 *
	 * @param catalogOutWriter  A (pre-initialised) writer for the output 
	 *     SExtractor *.param file.
	 */
	public void makeNative(Reader xmlIn, Writer configOutWriter, 
										Writer catalogOutWriter) throws Exception
	{
		// The transformer seems to close its input stream after reading
		// from it (xmlIn.reset() throws an "IOException: Stream closed") -
		// so read from the reader input now as a string, and then create 
		// new StringReaders to pass to the transformer as required.
		// 
		String xmlString = getInput(xmlIn);

		TransformerFactory tFactory = TransformerFactory.newInstance();

		// First transformation - the config file
		//
	   Transformer transformer = tFactory.newTransformer( 
				new StreamSource(CONFIG_STYLESHEET));

		transformer.transform(new StreamSource(new StringReader(xmlString)), 
									new StreamResult(configOutWriter));


		// Second transformation - the catalog file
		//
   	transformer = tFactory.newTransformer(
				new StreamSource(CATALOG_STYLESHEET));

		transformer.transform(new StreamSource(new StringReader(xmlString)), 
									new StreamResult(catalogOutWriter));
	}


	/**
	 * Reads the full contents of a Reader and returns them as a String.
	 * This is a utility function to enable us to re-use the data in the
	 * Reader, regardless of whether it supports reset().
	 *
	 * @param reader  A (pre-initialised) reader
	 *
	 * @return A string containing the full information read from the
	 * Reader.
	 */
	protected String getInput(Reader reader) throws Exception
	{
		Vector lines = new Vector();
		int readSize = 500;
		char[] buf = new char[readSize];
		int count = reader.read(buf,0,readSize);
		while (count == readSize) 
		{
			lines.add(new String(buf,0,readSize));
			count = reader.read(buf,0,readSize);
		}
		if (count != -1) 
		{
			lines.add(new String(buf,0,count));
		}
		String retString = "";
		for (int i = 0; i < lines.size(); i++) 
		{
			retString = retString + lines.get(i);	
		}
		return retString;
	}
}
//-------------------------------------------------------------------------
