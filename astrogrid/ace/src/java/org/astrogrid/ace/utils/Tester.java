//-------------------------------------------------------------------------
// FILE:    Tester.java 
// PACKAGE: org.astrogrid.ace.utils
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 14/10/02   KEA       Initial prototype
// 13/12/02   KEA       Added support for new "Wavelength" XML tag
//-------------------------------------------------------------------------


package org.astrogrid.ace.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.FileReader;
import java.io.FileWriter;

import org.astrogrid.ace.utils.Translator;


/**
 * <p>Test harness for Ace utility file translation functionality.
 *
 * <p>To translate from Ace XML to SExtractor native parameter files:
 * <p><tt>
 *    java org.astrogrid.ace.utils.Translator xml2native	
 *			&lt;inputXMLFile&gt; &lt;outputConfigFile&gt; &lt;outputParamFile&gt;
 * </tt>				
 *
 * <p>To translate from SExtractor native parameter files to Ace XML:
 * <p><tt>
 *    java org.astrogrid.ace.utils.Tester native2xml 
 *			&lt;inputConfigFile&gt; &lt;inputParamFile&gt; &lt;outputXMLFile&gt;
 *			&lt;wavelength&gt;
 * </tt>				
 * 
 * <p>NOTE: This test harness uses dummy file names for the SExtractor
 * input image files (i.e. these are not accepted on the command line).  
 * This test harness is intended to provide a code example for using 
 * the Translator, rather than being very useful in itself.
 *
 *
 * @see org.astrogrid.ace.utils.Translator
 *
 * @author Kona Andrews,
 * <a href="mailto:kea@ast.cam.ac.uk">kea@ast.cam.ac.uk</a>
 * @version 1.0
 *
 *
 * (c) Copyright Astrogrid 2002; all rights reserved. 
 * See http://www.astrogrid.org/code_licence.html for terms of usage.
 */
public class Tester 
{
	/**
	 * Test harness for Ace utility file translation functionality.
	 * See class usage notes for invocation details. 
	 */
	public static void main(String [] args) throws Exception
	{
		if ( 
			((args[0].equals("native2xml")) && (args.length == 5)) ||
			((args[0].equals("xml2native")) && (args.length == 4)) 
		)
		{
			Translator translator = new Translator();
	
			if (args[0].equals("xml2native")) 
			{
				// Translate from XML to SExtractor native format
				//	
				BufferedReader xmlReader =  
						new BufferedReader(new FileReader(args[1]));

				BufferedWriter confWriter = 
						new BufferedWriter(new FileWriter(args[2]));

				BufferedWriter catWriter =  
						new BufferedWriter(new FileWriter(args[3]));

				translator.doXmlToNative(xmlReader,confWriter,catWriter);


				xmlReader.close();
				confWriter.close();
				catWriter.close();
			}
			else if (args[0].equals("native2xml"))
			{
				// Translate from SExtractor native format to XML
				//	
				BufferedReader confReader =  
						new BufferedReader(new FileReader(args[1]));

				BufferedReader catReader = 
						new BufferedReader(new FileReader(args[2]));

				BufferedWriter xmlWriter =  
						new BufferedWriter(new FileWriter(args[3]));

				translator.doNativeToXml(confReader,catReader,xmlWriter,
								"dummyImageFile1", "dummyImageFile2", args[4]);

				confReader.close();
				catReader.close();
				xmlWriter.close();
			}
		}
		else 
		{
			// Print some usage information
			System.out.println(
					"\nUSAGE: \n"
				+ "java org.astrogrid.ace.utils.Tester xml2native " 
				+ "<inputXMLFile> <outputConfigFile> <outputParamFile>\n"
				+ " -OR-\n"
				+ "java org.astrogrid.ace.utils.Tester native2xml" 
				+ "<inputConfigFile> <inputParamFile> <outputXMLFile> "
				+ "<wavelength>"
				);
		}
	}
}
//-------------------------------------------------------------------------
