package org.astrogrid.ace.sextractor;


import org.astrogrid.common.wrapper.*;
import java.io.*;

import org.astrogrid.ace.service.*;
import org.astrogrid.common.ucd.*;
import org.astrogrid.tools.ascii.*;

import org.astrogrid.log.*;

/**
 * File Writer that creates the native SExtractor files from the dictionary
 * and parameter bundles
 *
 * @author M Hill
 */

public class SexNativeFileWriter
{

   public static void writeNativeFiles(AceParameterBundle bundle, String configFilename, String paramFilename) throws IOException
   {
      writeNativeConfigFile(bundle, configFilename);
      writeNativeParamFile(bundle, paramFilename);
   }

   public static void writeNativeConfigFile(AceParameterBundle bundle, String filename) throws IOException
   {
      bundle.setConfigFile(filename);

      AsciiOutputStream out = new AsciiOutputStream(new FileOutputStream(filename));
      writeNativeConfigFile(bundle, out);
   }

   /**
     Writes out the parameters in SExtractor config file format
     <p>
    */
   public static void writeNativeConfigFile(AceParameterBundle bundle, AsciiOutputStream out) throws IOException
   {
      AceDictionary dictionary = AceDictionary.getInstance();

      String[] configTerms = dictionary.getLocalConfigFileDictionary();

      for (int i=0;i<configTerms.length;i++)
      {
         Parameter parameter = bundle.getChild(configTerms[i]);

         //check here for whether term is required and it's missing
         if ( dictionary.isLocalTermRequired(configTerms[i])
              && (parameter == null))
         {
            throw new RuntimeException("Required term "+configTerms[i]+" is missing from parameter bundle");
         }

         if (parameter != null)
         {
            writeNativeConfigParameter(parameter, out);
         }

      }

   }


   public static void writeNativeConfigParameter(Parameter parameter, OutputStream out) throws IOException
   {
         String value = "";

         if (parameter instanceof SingleParameter)
         {
            value = ((SingleParameter) parameter).getValue().trim();

            if (value.equals("true"))
            {
               value = "Y";
            }
            else if (value.equals("false"))
            {
               value = "N";
            }
         }
         else if (parameter instanceof IndexedParameter)
         {
            String[] allValues = ((IndexedParameter) parameter).getValues();
            for (int j=0;j<allValues.length;j++)
            {
               value = value + allValues[j]+" ";
            }
         }
         else
         {
            throw new RuntimeException("AceParameterBundle contains child bundles, and it should not");
         }

         String line = parameter.getId() + "  " + value + '\n';
         out.write(line.getBytes());
   }

   /**
    * Writes out the SExtractor native Param file - the list of columns that
    * will be included in the output file
     <p>
    */
   public static void writeNativeParamFile(AceParameterBundle bundle, String filename) throws IOException
   {
      Log.trace("Writing native param (output cols) file '"+filename+"...");

      bundle.setParamFile(filename);

      String[] outputCols = bundle.getOutputColumns();
      Log.affirm(outputCols != null, "No output columns given - this should have been validated earlier");

      AsciiOutputStream out = new AsciiOutputStream(new FileOutputStream(filename));

      out.writeLine("NUMBER");   //index
      for (int i=0;i<outputCols.length;i++)
      {
         if (outputCols[i].equalsIgnoreCase("MAG_APER") ||
             outputCols[i].equalsIgnoreCase("MAG_APERERR") ||
             outputCols[i].equalsIgnoreCase("FLUX_APER") ||
             outputCols[i].equalsIgnoreCase("FLUX_APERERR"))
         {
            //special 'vector column' - number of elements equal to number
            //of apertures specified in PHOT_APERTURES
            IndexedParameter p = (IndexedParameter) bundle.getChild("PHOT_APERTURES");
            out.writeLine(outputCols[i]+"("+p.getChildCount()+")");
         }
         else
         {
            out.writeLine(outputCols[i]);
         }
      }
      out.close();
   }


}

