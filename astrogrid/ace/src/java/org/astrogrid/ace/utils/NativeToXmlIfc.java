//-------------------------------------------------------------------------
// FILE: NativeToXmlIfc.java
// PACKAGE: org.astrogrid.ace.utils
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 14/10/02   KEA       Initial prototype
// 13/12/02   KEA       Added support for new "Wavelength" XML tag
//-------------------------------------------------------------------------


package org.astrogrid.ace.utils;

import java.io.Reader;
import java.io.Writer;


/**
 * <p>Interface class for conversions from SExtractor native parameter 
 * files to Ace XML format.
 *
 * <p>Intended to provide a generic interface for which particular
 * implementations can be supplied as required.
 *
 * <p>See Translator.java for a usage example, and Tester.java 
 * for a run-time test harness.
 *
 *
 * @see org.astrogrid.ace.utils.NativeToXmlImpl
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
public interface NativeToXmlIfc
{
	/**
	 * Converts SExtractor input (configuration file *.sex and catalog 
	 * parameter file *.param) to Ace XML output.
	 *
	 * @param configInReader  A (pre-initialised) reader for the input 
	 *     SExtractor configuration parameter file (*.sex).
	 *
	 * @param catalogInReader A (pre-initialised) writer for the output
	 *     SExtractor catalog parameter file (*.param).
	 *
	 * @param xmlOutWriter  A (pre-initialised) writer for the output
	 *     Ace XML file.
	 *
	 * @param imageToCatalog  The name of the input image file for SExtractor.
	 *
	 * @param imageToMeasure  The name of the second (optional) input image 
	 *     file for SExtractor.
	 *
	 * @param wavelength  The wavelength descriptor for the input data
	 *     (used by Ace in producing VOTable output with useful UCDs).
	 */
	public void makeXml(Reader configInReader, Reader catalogInReader, 
									Writer xmlOutWriter, String imageToCatalog,
									String imageToMeasure, String wavelength) 
									throws Exception;
}
//-------------------------------------------------------------------------
