/*
 * $Id: HyperZVOTableReader.java,v 1.1 2004/01/20 12:03:49 pah Exp $
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.astrogrid.applications.FileReferenceParameter;
import org.astrogrid.applications.Parameter;
import org.astrogrid.applications.commandline.CmdLineApplicationEnvironment;

import cds.savot.model.SavotResource;
import cds.savot.model.SavotVOTable;
import cds.savot.model.TDSet;
import cds.savot.model.TRSet;
import cds.savot.pull.SavotPullEngine;
import cds.savot.pull.SavotPullParser;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 */
public class HyperZVOTableReader {
   private File outfile;
   private CmdLineApplicationEnvironment env;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(HyperZVOTableReader.class);
   private FileReferenceParameter inFileparam;

   /**
    * 
    */
   public HyperZVOTableReader(Parameter catfile, CmdLineApplicationEnvironment env) {
      inFileparam = (FileReferenceParameter)catfile;
      this.env = env;
      outfile = env.getTempFile();

   }
   
   public void read() {
      String target = outfile.getAbsolutePath();
      String source =inFileparam.getRealFile().getAbsolutePath();
      readInternal(source, target);
      inFileparam.setRealFile(outfile);
   }
   /**
    * This reads the input VOTable file and converts it to a file suitable for hyperZ input 
    * @param source
    * @param target
    */
   private void readInternal(String source, String target) {

      // the whole VOTable file is put into memory
      SavotPullParser sb = new SavotPullParser(source, SavotPullEngine.FULL);

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

         // for each resource
         for (int l = 0; l < sb.getResourceCount(); l++) {

            SavotResource currentResource =
               (SavotResource) (sv.getResources().getItemAt(l));

            // for each table of the current resource
            for (int m = 0; m < currentResource.getTableCount(); m++) {

               // get all the rows of the table
               TRSet tr = currentResource.getTRSet(m);

               logger.debug(
                  "Number of items in TRset (= number of <TR></TR>) : "
                     + tr.getItemCount());

               // for each row
               for (int i = 0; i < tr.getItemCount(); i++) {

                  // get all the data of the row
                  TDSet theTDs = tr.getTDSet(i);
                  String currentLine = new String();
                  logger.debug(
                     "Number of items in TDSet for the index "
                        + (i + 1)
                        + " tr (= number of <TD></TD>) : "
                        + theTDs.getItemCount());

                  // for each data of the row, select the correct fields
                  for (int j = 0; j < theTDs.getItemCount(); j++) {
                     currentLine = currentLine + theTDs.getContent(j);

                     logger.debug("<" + theTDs.getContent(j) + ">");
                  }

                  if (target != null) {
                     if (target.compareTo("") != 0) {
                        bw.write(currentLine);
                        bw.newLine();
                     }
                  }
                  else
                     logger.debug(currentLine);
               }
            }
            if (target != null) {
               bw.flush();
               bw.close();
            }
         }
      }
      catch (IOException e) {
         System.err.println("PullFullSample2 : " + e);
      }
      catch (Exception e) {
         System.err.println("PullFullSample2 : " + e);
      };

   }
}
