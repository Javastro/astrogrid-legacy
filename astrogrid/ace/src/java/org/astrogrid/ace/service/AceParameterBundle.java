/*
 * $Id: AceParameterBundle.java,v 1.1.1.1 2003/08/25 18:36:14 mch Exp $
 *
 * (c)
 */

package org.astrogrid.ace.service;
import org.astrogrid.ace.sextractor.*;

import org.astrogrid.common.wrapper.*;
import java.io.*;
import org.astrogrid.common.service.*;
import org.astrogrid.common.ucd.*;

/**
 * Parameter Bundle for the ACE (Astrogrid Catalogue Extractor) application service
 *
 * @author M Hill
 */


public class AceParameterBundle extends ParameterBundle
{
   //Catalogue types - for specifying output format
   public static final String ASCII = "ASCII";
   public static final String FITS = "FITS_1.0";

   //used internally to track the configuration filename
   String configFile = null;
   String imageToMeasureUrl = null;
   String imageToCatalogUrl = null;

   public AceParameterBundle(String givenId, String givenUnits)
   {
      super(givenId, givenUnits);
   }

   /**
    * Only gets called when reading from file.  Return warning that this
    * 'property' will be ignored, as it will be set internally - see
    * setOutputFile()
    */
   public void setCATALOG_NAME(String s)
   {
      throw new UnsupportedOperationException("CATALOG_NAME will be ignored - set internally by wrapper");
   }

   /**
    * Only gets called when reading from file.  Return warning that this
    * 'property' will be ignored, as it will be set internally - see
    * setOutputFormat()

    */

   public void setCATALOG_TYPE(String s)

   {

      throw new UnsupportedOperationException("CATALOG_TYPE will be ignored - returned format will be VOTable");

   }



   /**

    * Only gets called when reading from file.  Return warning that this

    * 'property' will be ignored, as it will be set internally - see

    * setParamFile()

    */

   public void setPARAMETERS_NAME(String s)

   {

      throw new UnsupportedOperationException("PARAMETERS_NAME will be ignored - set internally by wrapper");

   }



   /**

    * Set the parameter file name - this is the file that contains the list

    * of columns to be included in the output

    */

   public void setParamFile(String filename)

   {

      addChild(new SingleParameter("PARAMETERS_NAME",filename));

   }



   /**

    * Set the config file name - this is the file that contains the configuration

    * keywords and values that instruct the application on how to do the extraction

    */

   public void setConfigFile(String filename)

   {

      configFile = filename;

   }



   /**

    * returns the config file name - this is the file that contains the configuration

    * keywords and values

    */

   public String getConfigFile()

   {

      return configFile;

   }

   /**

    * Set the output file name - this is the file that contains the list

    * of columns to be included in the output

    */

   public void setOutputFile(String filename)

   {

      addChild(new SingleParameter("CATALOG_NAME",filename));

   }



   /**

    * Output format

    */

   public void setOutputFormat(String outputFormat)

   {

      if (outputFormat.equals(ASCII) || outputFormat.equals(FITS))

      {

         addChild(new SingleParameter("CATALOG_TYPE", outputFormat));

      }

      else

      {

         throw new IllegalArgumentException("Output format must be ASCII or FITS, not '"+outputFormat+"'");

      }

   }



   /**

    * Output columns - list of words giving the column headings of the output

    * file

    */

   public String[] getOutputColumns()

   {

      Parameter p = getChild(AceDictionary.OUTPUT_COLUMNS_LABEL);



      if (p == null)

      {

         return null;

      }

      else if (p instanceof IndexedParameter)

      {

         return ((IndexedParameter) p).getValues();

      }

      else

      {

         return new String[] { ((SingleParameter) p).getValue() };

      }

   }



   /**

    * Image to measure - the image that the catalogue fluxes will be

    * generated from.  Note that there may be a different image used to generate

    * the catalogue objects

    */

   public void setImageToMeasure(String imageUrl)

   {

      imageToMeasureUrl = imageUrl;

   }



   /**

    * Image to measure - the image that the catalogue fluxes will be

    * generated from.

    */

   public String getImageToMeasure()

   {

      return imageToMeasureUrl;

   }



   /**

    * Image to catalogue - the image that the catalogue objects will be

    * generated from.  Note that the fluxes will be generated from ImageToMeasure

    * property

    */

   public void setImageToCatalog(String imageUrl)

   {

      imageToCatalogUrl = imageUrl;

   }



   /**

    * Image to catalogue - the image that the catalogue objects will be

    * generated from.

    */

   public String getImageToCatalog()

   {

      return imageToCatalogUrl;

   }



   /**

    * This method is used by the XML parser to set the images properties

    * from the IMAGE tag

    *

   public void setIMAGE(String imageUrl)

   {

      setImageToMeasure(imageUrl);

      setImageToCatalog(imageUrl);

   }



   /**

    * Overrides addChild in order to carry out general validation - eg make

    * sure it's a UCD, etc, and convert into a local term with local units

    */

   public void addChild(Parameter child)

   {

      String childId = child.getId();



      try

      {

         AceContext.getInstance().getUcdDictionary().assertUcdValid(childId);

         AceContext.getInstance().getUnitDictionary().assertUnitValid(child.getUnits());



         //convert ucd to a local term if necessary, and convert from given

         //units to locally required units..



         //... to do ...

      }

      catch (Exception e)

      {

         throw new IllegalArgumentException(""+e);

      }



      super.addChild(child);

   }



   public static void main(String [] cmdargs) throws IOException

   {

      ParameterExtractor extractor = new ParameterExtractor(org.astrogrid.ace.service.AceContext.getInstance());



      AceParameterBundle aBundle = (AceParameterBundle) extractor.loadRootBundle(

         org.astrogrid.test.ConfigElementLoader.loadElement("default.xml")

      );



      if (aBundle != null)

      {

         try

         {

            //ParameterXmlWriter.writeXmlConfigFile(aBundle, new xml.XmlOutputStream(System.out));



            SexNativeFileWriter.writeNativeFiles(aBundle,"1_sexConfig.cfg","1_sexParam.cfg");

         }

         catch (IOException ioe)

         {

            ioe.printStackTrace(System.out);

         }

      }

   }



}



