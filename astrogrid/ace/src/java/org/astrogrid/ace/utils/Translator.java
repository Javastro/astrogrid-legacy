//-------------------------------------------------------------------------
// FILE: Translator.java
// PACKAGE: org.astrogrid.ace.utils
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 14/10/02   KEA       Initial prototype
// 13/12/02   KEA       Added support for new "Wavelength" XML tag
//-----------------------------------------------------------------------

package org.astrogrid.ace.utils;

import java.io.Reader;
import java.io.Writer;

import org.astrogrid.ace.utils.NativeToXmlIfc;
import org.astrogrid.ace.utils.NativeToXmlImpl;
import org.astrogrid.ace.utils.XmlToNativeIfc;
import org.astrogrid.ace.utils.XmlToNativeDomImpl;

/**
 * <p>Supplies invocation methods for translating from Ace XML to
 * SExtractor native parameter files and vice versa.
 *
 * <p>See Tester.java for a run-time test harness.
 *
 * <p>TO DO:  <ul>
 * <li>More robust error-checking and handling?</li>
 * <li>Nicer exception-handling?</li>
 * </ul>
 *
 *
 * @see org.astrogrid.ace.utils.NativeToXmlIfc
 * @see org.astrogrid.ace.utils.XmlToNativeIfc
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
public class Translator
{
   /**
    * Dummy constructor - does nothing.
    */
   public Translator()
   {
   }

   /**
    * Converts SExtractor input (configuration file *.sex and
    * catalog
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
    * @param imageToCatalog  The name of the primary input image file for
    * SExtractor.
    *
    * @param imageToMeasure  The name of the second (optional) input image
    *     file for SExtractor.
    *
    * @param wavelength  The wavelength descriptor for the input data
    *     (used by Ace in producing VOTable output with useful UCDs).
    */
   public void doNativeToXml(Reader configInReader,
                              Reader catalogInReader,
                              Writer xmlOutWriter,
                              String imageToCatalog,
                              String imageToMeasure,
                              String wavelength)
                              throws Exception
   {
      NativeToXmlIfc converter = new NativeToXmlImpl();
      converter.makeXml(configInReader, catalogInReader, xmlOutWriter,
                           imageToCatalog, imageToMeasure, wavelength);
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
   public void doXmlToNative(Reader xmlIn,
                              Writer configOutWriter,
                              Writer catalogOutWriter)
                              throws Exception
   {

      // DOM VERSION
      XmlToNativeIfc converter = new XmlToNativeDomImpl();

      // XSLT VERSION
      //XmlToNativeIfc converter = new XmlToNativeXsltImpl();

      converter.makeNative(xmlIn, configOutWriter, catalogOutWriter);
   }
}
//-------------------------------------------------------------------------
