/*
 * $Id: HyperZVOTableReader.java,v 1.8 2008/09/03 14:18:54 pah Exp $
 * 
 * Created on 18-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.commandline.hyperz;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.commandline.DefaultCommandLineParameterAdapter;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.impl.CommandLineParameterDefinition;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.protocol.ExternalValue;

import cds.savot.model.FieldSet;
import cds.savot.model.SavotField;
import cds.savot.model.SavotResource;
import cds.savot.model.SavotVOTable;
import cds.savot.model.TDSet;
import cds.savot.model.TRSet;
import cds.savot.pull.SavotPullEngine;
import cds.savot.pull.SavotPullParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class HyperZVOTableReader extends DefaultCommandLineParameterAdapter implements HyperZVOTableWriter.VOTableSource {
   /** Construct a new HyperZVOTableReader
     * @param val
     * @param descr
     */
    public HyperZVOTableReader(ApplicationInterface interf, ParameterValue val, CommandLineParameterDefinition descr,ApplicationEnvironment env, ExternalValue ival, final String bands) {
        super(interf,val, descr,ival,env);
      bandOrder = bands;
    }
   private SavotVOTable vot;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(HyperZVOTableReader.class);
   private final String bandOrder;
   /**
    * @see org.astrogrid.applications.ParameterAdapter#process()
    */
   public Object process() throws CeaException {
      super.process();
       // apply vot
       File source = this.referenceFile;
       File plainFile = env.getTempFile(description.getId());
       // must set the command line values
       this.referenceFile = plainFile;
       this.commandLineVal = plainFile.getName();
       
       vot = readInternal(source, plainFile);
       logger.debug("written hyperz input file to "+ plainFile.getName());
       return referenceFile.getName();   
   }
   
   public SavotVOTable getVOTable() {
       return vot;
   }
   /**
    * This reads the input VOTable file and converts it to a file suitable for hyperZ input. This is simply a file with an index number in the first column, followed by all the photometry values, followed by all the photometry error values... 
    * @param source
    * @param target
    * @TODO better error reporting by far - propagate exceptions up so that they get reported.
    */
   private SavotVOTable readInternal(File source, File target) {

      
      //FIXME - need better errors out of this method - should throw some exception.
      // the whole VOTable file is put into memory
      SavotPullParser sb = new SavotPullParser(source.getAbsolutePath(), SavotPullEngine.FULL);
      // the array of indexes that point to where the magnitudes are - initialized to -1
      int idxs[];
      idxs = new int[bandOrder.length()*2];
      Arrays.fill(idxs, -1);

      logger.debug(
         "Resource name : "
            + ((SavotResource)sb.getVOTable().getResources().getItemAt(0)).getName());

      // get the VOTable object
      SavotVOTable sv = sb.getVOTable();

      try {
         BufferedWriter bw = null;

         if (target != null) {
            bw = new BufferedWriter(new FileWriter(target));
         }

         SavotResource currentResource = (SavotResource) (sv.getResources().getItemAt(0));

         //identify the fields of interest from the file - i.e. the photometry in the different bands
         FieldSet fieldSet = currentResource.getFieldSet(0);

         for (int i = 0; i < fieldSet.getItemCount(); i++) {
            SavotField field = (SavotField)fieldSet.getItemAt(i);
            String ucd = field.getUcd();
            //FIXME this could pick up some other spurious entries e.g. phot_mag_rubbish_err... need to make stricter
            if ( ucd.startsWith("PHOT_MAG_")) {
               char band = ucd.toUpperCase().charAt(9);
               int validx = bandOrder.indexOf(band);
               
               if (validx != -1) {
                  int erridx = validx + bandOrder.length();

                  if (ucd.endsWith("ERR")) {
                     idxs[erridx] = i;
                  }
                  else {
                     idxs[validx] = i;
                  }
               }
             }

         }

         // get all the rows of the first table
         TRSet tr = currentResource.getTRSet(0);

         logger.debug(
            "Number of items in TRset (= number of <TR></TR>) : " + tr.getItemCount());

         // for each row
         for (int i = 0; i < tr.getItemCount(); i++) {

            // get all the data of the row
            TDSet theTDs = tr.getTDSet(i);
            StringBuffer currentLine = new StringBuffer();
//            logger.debug(
//               "Number of items in TDSet for the index "
//                  + (i + 1)
//                  + " tr (= number of <TD></TD>) : "
//                  + theTDs.getItemCount());
//
            // for each data of the row, select the correct fields from the band idxs
            currentLine.append(i + 1);

            for (int j = 0; j < idxs.length; j++) {
               currentLine.append(" ");
               if(idxs[j] != -1){
                  currentLine.append(theTDs.getContent(idxs[j]));
                  
               }
               else
               {
                  currentLine.append("*");
                  logger.error("there appears to be no input for band "+ bandOrder.charAt(j));
               }
            }


               bw.write(currentLine.toString());
               bw.newLine();

         }

         if (target != null) {
            bw.flush();
            bw.close();
         }

      }
      catch (IOException e) {
         logger.error(e);
      }
      catch (Exception e) {
        logger.error(target, e);
      };
      return sv;
   }


}
