package org.astrogrid.ace.sextractor;


import java.io.*;
import org.astrogrid.tools.ascii.AsciiInputStream;
import org.astrogrid.log.Log;
import org.astrogrid.tools.votable.*;
import org.astrogrid.tools.xml.*;
import org.astrogrid.tools.util.Angle;

import org.astrogrid.ace.service.*;
import org.astrogrid.common.wrapper.SingleParameter;

/**
 * Converter to take SExtractor ASCII output data from SExtractor
 * and convert it to VOTable format.
 *
 */

public class SexAscii2Vot
{
   public static void convert(String inFilename, String outFilename, AceParameterBundle aBundle) throws IOException
   {
      AsciiInputStream nativeResults = new AsciiInputStream(new FileInputStream(inFilename));
      VotOutputStream votResults = new VotOutputStream(new FileOutputStream(outFilename));
      convert(nativeResults, votResults, aBundle);
      nativeResults.close();
      votResults.close();
   }

   /**
    * Converter - takes given streams and converts from the asciiIn to the
    * votOut.  NB Needs checking...
    */
   public static void convert(AsciiInputStream asciiIn, VotOutputStream votOut, AceParameterBundle aBundle) throws IOException
   {
      VotTag votBlock = votOut.newTag();
      votBlock.writeDescription("SExtractor results");
      VotTag.DefinitionTag defBlock = votBlock.newDefinitionTag();

      Log.logWarning("Coordinate system is currently softwired");
      defBlock.writeCooSys("myJ2000", "eq_FK5","2000","2000");//needs some work...!
      defBlock.close();
      VotTag.ResourceTag resBlock = votBlock.newResourceTag("name=\"GSC1.2\"");
      resBlock.writeDescription("SExtractor configuration (input)");
 //     resBlock.writeParam("RA","degrees","unknown");
  //    resBlock.writeParam("Dec","degrees","unknown");
  //    resBlock.writeParam("Radius","degrees","unknown");
      VotTag.TableTag tableBlock = resBlock.newTableTag();
      tableBlock.writeDescription("SExtractor result catagalogue");

      tableBlock.writeField(new Field("ID","ID",
                                      VotDatatype.INT,  Field.NOT_ARRAY,
                                      "ID_MAIN",
                                      null,
                                      "Record Identifier - this table only"
      ));

      String[] columns = aBundle.getOutputColumns();
      String[] colUcds = new String[columns.length];
      String units = null;

      for (int col=0;col<columns.length;col++)
      {
         units = null; // unless we know otherwise
         colUcds[col] = AceDictionary.getInstance().getUcdFor(columns[col]);

         //special fix for SEDs; if the passband has been given by the user, use
         //that as the UCD.  It's not quite right, but it will do...
         if (columns[col].equals("MAG_AUTO"))
         {
            colUcds[col] = ((SingleParameter) aBundle.getChild("Wavelength")).getValue();
         }

         //special fix for aladin, which does not use UCDs, and needs RA & DEC
         //in the column name
         if (columns[col].equals("X_WORLD"))
         {
            columns[col] = "RA"; //"X_WORLD (RA)";
            units = "deg";
         }
         if (columns[col].equals("Y_WORLD"))
         {
            columns[col] = "DEC"; //"Y_WORLD (DEC)";
            units = "deg";
         }
         /**
         if (colUcd == null)
         {
            colUcd = "";
         }
          /**/

         tableBlock.writeField(new Field(
                           columns[col], columns[col],
                           VotDatatype.FLOAT,  Field.NOT_ARRAY,
                           colUcds[col],
                           units,
                           "UCD Desc unknown"
         ));
      }
      VotTag.TableDataTag tableData = tableBlock.newTableDataTag();

      int recordNum = 0;
      String sexValue = null;
      Angle anAngle = new Angle();
      while (asciiIn.available()>0)
      {
         recordNum++;

         tableData.startNewRow();

         tableData.writeValue(""+asciiIn.readString(' ')); //column number

         for (int col=0;col<columns.length;col++)
         {
            sexValue = asciiIn.readString(' ').trim();

            if (colUcds[col] != null)
            {
               //write RA & Dec as deg mins secs
               if (colUcds[col].equalsIgnoreCase("POS_EQ_DEC_MAIN"))
               {
                  anAngle.setDegrees(Double.parseDouble(sexValue));
                  sexValue = ""+(anAngle.asDegrees());
               }
               if (colUcds[col].equalsIgnoreCase("POS_EQ_RA_MAIN"))
               {
                  anAngle.setDegrees(Double.parseDouble(sexValue));
//                  sexValue = ""+(anAngle.asDegrees()/15.0);   //as hours
                  sexValue = ""+(anAngle.asDegrees());   //as degrees
               }
            }

            tableData.writeValue(sexValue);
         }
         asciiIn.readToEOL();
      }
   }

   /**
    * Main application method - takes input file as first argument
    * and output file as second argument and converts from one to the
    * other
    */
   public static void main(String args[]) throws IOException
   {
      if ((args == null) || (args.length < 2))
      {
         printHelp();
//       System.exit(1);
         args = new String[2]; args[0] = "sexResults.txt"; args[1] = "sexResults.vot";
      }

      Log.logInfo("Converting "+args[0]+" to "+args[1]+"...");


      org.w3c.dom.Element e = org.astrogrid.test.ConfigElementLoader.loadElement("default.xml");

      org.astrogrid.common.service.ParameterExtractor extractor = new org.astrogrid.common.service.ParameterExtractor(AceContext.getInstance());
      AceParameterBundle aBundle = (AceParameterBundle) extractor.loadRootBundle(e);

      try
      {
         SexAscii2Vot.convert(args[0], args[1], aBundle);
         Log.logInfo("...done");
      }
      catch (IOException ioe)
      {
         Log.logError("Oh dear, failed ",ioe);
      }
   }

   /**
    * Displays user command line help to the console
    */
   public static void printHelp()
   {
      System.out.println("SexAscii2Vot");
      System.out.println("------------------");
      System.out.println("Converts SExtractor ASCII catalogue");
      System.out.println("data, generated by SExtractor, ");
      System.out.println("into VOTable format");
      System.out.println("");
      System.out.println("Usage: java SuperCosmosCsv2Vot <asciiInFile> <votTargetFile>");
      System.out.println("");
   }

}
