//-------------------------------------------------------------------------
// FILE: NativeToXmlImpl.java
// PACKAGE: org.astrogrid.ace.utils
//
// DATE       AUTHOR    NOTES
// ----       ------    -----
// 14/10/02   KEA       Initial prototype
// 04/12/02   KEA       Revised to deal with new Ace Schema (v.1_0).
//                      NB this is an interim release.
// 13/12/02   KEA       Added support for new "Wavelength" XML tag
//-------------------------------------------------------------------------


package org.astrogrid.ace.utils;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * <p>Fairly dumb tokenizer-based implementation of file format converter
 * for conversions from SExtractor native parameter files to Ace XML
 * format.
 *
 * <p>See Translator.java for a usage example, and Tester.java
 * for a run-time test harness.
 *
 * <p>TO DO:  <ul>
 * <li>More robust error-checking and handling?</li>
 * <li>Nicer exception-throwing and handling?</li>
 * </ul>
 *
 *
 * @see org.astrogrid.ace.utils.NativeToXmlIfc
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
public class NativeToXmlImpl implements NativeToXmlIfc
{
   /**
    * Hardwired XML output file preamble
    */
   protected static final String startString =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
   +  "<ace:AceInputDoc\n"
   +  "  xmlns:ace=\"http://www.astrogrid.org/namespace/AceInput-1_0\"\n"
   +  "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
   +  "  xsi:schemaLocation="
   +  "\"http://www.astrogrid.org/namespace/AceInput-1_0\n"
   +  "  http://astrogrid.ast.cam.ac.uk/namespace/AceInput-1_0.xsd\">\n\n";

   /**
    * Hardwired last entry in XML output file
    */
   protected static final String endString = "</ace:AceInputDoc>\n";

   /**
    * Tag names / output tokens representing Booleans: these need to
    * be handled separately as SExtractor needs "Y/y/N/n" rather than
    * "true/false" in its native config files.
    */
   protected static final String[] booleans = { "CLEAN",
                                                "FILTER",
                                                "FITS_UNSIGNED",
                                                "WEIGHT_GAIN" };
   /**
    * Tag names / output tokens to be omitted from the output XML.
    */
   protected static final String[] omits = { "CATALOG_NAME",
                                             "CATALOG_TYPE",
                                             "PARAMETERS_NAME" };
   /**
    * Dummy constructor - does nothing.
    */
    public NativeToXmlImpl()
    {
    }

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
    */
   public void makeXml(Reader configInReader,
                        Reader catalogInReader,
                        Writer xmlOutWriter,
                        String imageToCatalog,
                        String imageToMeasure,
                        String wavelength) throws IOException
   {
      // Write toplevel info to file
      //
      xmlOutWriter.write(startString);

      xmlOutWriter.write("\t<Wavelength>\n\t\t<arg>" + wavelength
               + "</arg>\n\t</Wavelength>\n\n");

      xmlOutWriter.write("\t<ImageToCatalog>\n\t\t<arg>" + imageToCatalog
               + "</arg>\n\t</ImageToCatalog>\n\n");

      xmlOutWriter.write("\t<ImageToMeasure>\n\t\t<arg>" + imageToMeasure
               + "</arg>\n\t</ImageToMeasure>\n\n");

      // Process config parameters
      makeSexXml(configInReader, xmlOutWriter);
       
       
      // Process catalog parameters
      makeParamXml(catalogInReader, xmlOutWriter);

       
       xmlOutWriter.write(endString);
   }

   public static void startXml(Writer xmlOut) throws IOException
   {
      xmlOut.write(startString);
   }
   
   public static void endXml(Writer xmlOut) throws IOException
   {
      xmlOut.write(endString);
      xmlOut.close();
 
   }
   
   
   /**
    * Converts SExtractor input configuration file *.sex only, to a file
    * with the relevent XML tags.  NB the result may not be valid, as it
    * may not be complete without other input information.
    *
    * @param configInReader  A (pre-initialised) reader for the input
    *     SExtractor configuration parameter file (*.sex).
    *
    * @param xmlOutWriter  A (pre-initialised) writer for the output
    *     Ace XML file.
    *
    */
   public static void makeSexXml(Reader sexIn, Writer xmlOut) throws IOException
   {
      BufferedReader config = new BufferedReader(sexIn);
      String configLine = config.readLine();
      int lineCount = 1;
      while (!(configLine == null))
      {
         // Throw away any trailing comments
         //
         int index = configLine.indexOf('#');
         if (index != -1)  // Trailing comment present
         {
            configLine = configLine.substring(0,index);
         }

         // Tokenize line with tokens: {" \t=,;\""}; (from SEx source code)
         //
         StringTokenizer tokenizer =
               new StringTokenizer(configLine," \t=,;\"",false);

         if ((tokenizer.countTokens() != 0)) // Not blank/comment line
         {
            //Get keyword
            //
            String keyword;
            try
            {
               keyword = tokenizer.nextToken();
            }
            catch(NoSuchElementException e)
            {
               throw new IOException(
                     "Expected a leading keyword on line "
                  +  Integer.toString(lineCount)
                  +  " of configuration file, got null");
            }
            // Check whether we should omit this keyword
            //
            boolean skip = false;
            for (int i = 0; i < omits.length; i++)
            {
               if (omits[i].equals(keyword) )
               {
                  skip = true;
               }
            }
            if (!skip)
            {
               xmlOut.write("\t<" + keyword + ">\n");

               //Get first argument (must be at least 1)
               //
               String arg;
               try
               {
                  arg = tokenizer.nextToken();
               }
               catch(NoSuchElementException e)
               {
                  throw new IOException(
                        "Expected an argument for keyword " +  keyword
                     +  " on line "
                     +  Integer.toString(lineCount)
                     +  " of configuration file, got null");
               }
               // Booleans are a special case
               //
               boolean done = false;
               for (int i = 0; i < booleans.length; i++)
               {
                  if (keyword.equals(booleans[i]))
                  {
                     if ((arg.equals("Y")) || (arg.equals("y")))
                     {
                        arg = "true";
                     }
                     else if ((arg.equals("N")) || (arg.equals("n")))
                     {
                        arg = "false";
                     }
                     else
                     {
                        throw new IOException("Unexpected token '" + arg
                           + "' for boolean keyword " + keyword);
                     }
                  }
               }
               xmlOut.write("\t\t<arg>" + arg + "</arg>\n");

               while (tokenizer.hasMoreTokens())
               {
                  arg = tokenizer.nextToken();
                  xmlOut.write("\t\t<arg>" + arg + "</arg>\n");
               }
               xmlOut.write("\t</" + keyword + ">\n\n");
            }
         }
         lineCount = lineCount + 1;
         configLine = config.readLine();
      }
      
   }

   /**
    * Converts SExtractor param configuration file *.param only, to a file
    * with the relevent XML tags.  NB the result may not be valid, as it
    * may not be complete without other input information.
    *
    * @param paramInReader A (pre-initialised) reader for the output
    *     SExtractor catalog parameter file (*.param).
    *
    * @param xmlOutWriter  A (pre-initialised) writer for the output
    *     Ace XML file.
    *
    */
   public static void makeParamXml(Reader paramIn, Writer xmlOutWriter) throws IOException
   {
      BufferedReader catalog = new BufferedReader(paramIn);
      Vector plainTag = new Vector();
      Vector oneVector = new Vector();
      Vector twoVector = new Vector();

      // Read all lines into the appropriate vector
      //
      String catalogLine = catalog.readLine();
      int lineCount = 1;
      while (!(catalogLine == null))
      {
         // Throw away any trailing comments
         //
         int index = catalogLine.indexOf('#');
         if (index != -1)  // Trailing comment present
         {
            catalogLine = catalogLine.substring(0,index);
         }

         StringTokenizer tokenizer =
               new StringTokenizer(catalogLine,"(,)",false);

         int tokenCount = tokenizer.countTokens();
         if ( (tokenCount >= 1) && (tokenCount <=3) ) // HAVE KEYWORD
         {
            plainTag.add("\t\t<arg>" + tokenizer.nextToken() + "</arg>\n");
         }
         catalogLine = catalog.readLine();
      }
      if (plainTag.size() > 0)
      {
         xmlOutWriter.write("\t<OUTPUT_COLUMNS>\n");

         for (int i = 0; i < plainTag.size(); i++)
         {
            String entry = (String)plainTag.get(i);
            xmlOutWriter.write(entry);
         }
         xmlOutWriter.write("\t</OUTPUT_COLUMNS>\n\n");
      }
   }
   
}
//-------------------------------------------------------------------------
