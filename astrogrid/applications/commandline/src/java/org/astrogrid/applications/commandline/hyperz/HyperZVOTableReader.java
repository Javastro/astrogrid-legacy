/*
 * $Id: HyperZVOTableReader.java,v 1.5 2004/09/23 22:44:23 pah Exp $
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
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.commandline.CommandLineApplicationEnvironment;
import org.astrogrid.applications.commandline.DefaultCommandLineParameterAdapter;
import org.astrogrid.applications.commandline.CommandLineParameterDescription;
import org.astrogrid.applications.description.ApplicationInterface;
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
    public HyperZVOTableReader(ApplicationInterface interf, ParameterValue val, CommandLineParameterDescription descr,CommandLineApplicationEnvironment env, ExternalValue ival) {
        super(interf,val, descr,ival,env);
    }
   private SavotVOTable vot;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(HyperZVOTableReader.class);
   /**
    * @see org.astrogrid.applications.ParameterAdapter#process()
    */
   public Object process() throws CeaException {
       super.process();
       // apply vot
       File source = this.referenceFile;
       File votFile = env.getTempFile();
       this.referenceFile = votFile;
       vot = readInternal(source,votFile);
       return referenceFile;   
   }
   
   public SavotVOTable getVOTable() {
       return vot;
   }
   /**
    * This reads the input VOTable file and converts it to a file suitable for hyperZ input 
    * @param source
    * @param target
    */
   private SavotVOTable readInternal(File source, File target) {

      // the whole VOTable file is put into memory
      SavotPullParser sb = new SavotPullParser(source.getAbsolutePath(), SavotPullEngine.FULL);
      // the array of indexes that point to where the magnitudes are
      int idxs[] = { -1, -1, -1, -1, -1, -1, -1, -1 };
      //where in the above array each of the bands should go
      int bidx = 0,
         beidx = 4,
         vidx = 1,
         veidx = 5,
         iidx = 2,
         ieidx = 6,
         zidx = 3,
         zeidx = 7;
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
            if (ucd.startsWith("PHOT_MAG_")) {
               char band = ucd.toUpperCase().charAt(9);
               switch (band) {
                  case 'B' :
                     {
                        if (ucd.endsWith("ERR")) {
                           idxs[beidx] = i;
                        }
                        else {
                           idxs[bidx] = i;
                        }
                        break;
                     }
                  case 'V' :
                     {
                        if (ucd.endsWith("ERR")) {
                           idxs[veidx] = i;
                        }
                        else {
                           idxs[vidx] = i;
                        }
                        break;
                     }
                  case 'I' :
                     {
                        if (ucd.endsWith("ERR")) {
                           idxs[ieidx] = i;
                        }
                        else {
                           idxs[iidx] = i;
                        }
                        break;
                     }
                  case 'Z' :
                     {
                        if (ucd.endsWith("ERR")) {
                           idxs[zeidx] = i;
                        }
                        else {
                           idxs[zidx] = i;
                        }
                        break;
                     }

                  default :
                     break;
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
            logger.debug(
               "Number of items in TDSet for the index "
                  + (i + 1)
                  + " tr (= number of <TD></TD>) : "
                  + theTDs.getItemCount());

            // for each data of the row, select the correct fields from the band idxs
            currentLine.append(i + 1);

            for (int j = 0; j < idxs.length; j++) {
               currentLine.append(" ");

               currentLine.append(theTDs.getContent(idxs[j]));
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
