//-------------------------------------------------------------------------
// FILE:    XmlToNativeIfc.java
// PACKAGE: org.astrogrid.ace.utils
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 14/10/02   KEA       Initial prototype
//-------------------------------------------------------------------------


package org.astrogrid.ace.utils;

import java.io.Reader;
import java.io.Writer;


/**
 * <p>Interface class for conversions from Ace XML to SExtractor 
 *  native parameter file format.
 *
 * <p>Intended to provide a generic interface for which particular
 * implementations (DOM-parser-based, XSLT-based etc) can be supplied
 * as required.
 *
 * <p>See Translator.java for a usage example, and Tester.java  
 * for a run-time test harness.
 * 
 *
 * @see org.astrogrid.ace.utils.XmlToNativeDomImpl
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
public interface XmlToNativeIfc
{
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
	public void makeNative(Reader XmlIn, Writer configOutWriter,
									Writer catalogOutWriter) throws Exception;
}
//-------------------------------------------------------------------------
